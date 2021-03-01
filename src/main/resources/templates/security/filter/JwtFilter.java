package ${packagePath}.security.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ${packagePath}.base.controller.ResultMessage;
import ${packagePath}.cache.CacheKeys;
import ${packagePath}.entity.User;
import ${packagePath}.enums.SystemStatusCode;
import ${packagePath}.redis.RedisDatabase;
import ${packagePath}.redis.StringRedisService;
import ${packagePath}.security.token.JwtToken;
import ${packagePath}.service.UserService;
import ${packagePath}.utils.JwtUtils;
import ${packagePath}.utils.NetworkUtil;
import ${packagePath}.utils.RequestResponseUtil;
import com.google.common.base.Objects;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/19 10:12
 * description:
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    @Setter
    private UserService userService;
    @Setter
    private StringRedisService stringRedisService;
    @Setter
    private JwtUtils jwtUtils;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        return (null != subject && subject.isAuthenticated());
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        try {
            AuthenticationToken token = createJwtToken(request);
            this.getSubject(request, response).login(token);
            return true;
        } catch (AuthenticationException e) {
            String exceptionMsg = e.getMessage();
            if (Objects.equal(exceptionMsg,SystemStatusCode.ACCESS_TOKEN_EXPIRE.getName())) { //token过期
//                判断RefreshToken未过期就进行AccessToken刷新
                if (!this.refreshToken(request, response)) {
                    //jwt 已过期,通知客户端重新登录
                    ResultMessage message = new ResultMessage().error(SystemStatusCode.JWT_EXPIRE.getCode(), SystemStatusCode.JWT_EXPIRE.getName());
                    RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
                }
            }
            if (Objects.equal(exceptionMsg, SystemStatusCode.JWT_NOT_FOUND.getName())) { //没有发现token
                ResultMessage message = new ResultMessage().error(SystemStatusCode.JWT_NOT_FOUND.getCode(), SystemStatusCode.JWT_NOT_FOUND.getName());
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
            }

            if (Objects.equal(exceptionMsg, SystemStatusCode.USER_NOT_FOUND.getName())) { //没有发现账户
                ResultMessage message = new ResultMessage().error(SystemStatusCode.USER_NOT_FOUND.getCode(), SystemStatusCode.USER_DELETE.getName());
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
            }
        }

        return false;
        // 过滤链终止
    }

    private AuthenticationToken createJwtToken(ServletRequest request) {

        Map<String, String> requestParameters = RequestResponseUtil.getRequestParameters(request);
        Map<String, String> requestHeaders = RequestResponseUtil.getRequestHeaders(request);
        String ipHost = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
        String deviceInfo = Optional.ofNullable(requestHeaders.get("deviceInfo".toLowerCase()))
                .orElseGet(() -> Optional.ofNullable(requestParameters.get("deviceInfo")).orElse("")).toLowerCase();
        String token = Optional.ofNullable(requestHeaders.get("authorization"))
                .orElseGet(() -> Optional.ofNullable(requestParameters.get("authorization")).orElse(""));
        Long userId = jwtUtils.getUserIdFromToken(token);
        return new JwtToken(userId, ipHost, deviceInfo, token);
    }

    /**
     * 此处为AccessToken刷新，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     *
     * @param request  request
     * @param response response
     * @return ResultMessage
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        Map<String, String> requestHeaders = RequestResponseUtil.getRequestHeaders(request);
        Map<String, String> requestParameters = RequestResponseUtil.getRequestParameters(request);
        String token = Optional.ofNullable(requestHeaders.get("authorization"))
                .orElseGet(() -> Optional.ofNullable(requestParameters.get("authorization")).orElse(""));
        Long userId = jwtUtils.getUserIdFromToken(token);

        String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
        Long ipLong = NetworkUtil.ipToLong(ip) ;

        String jwtMobileRefreshTokenKey = CacheKeys.getJwtRefreshTokenKey( userId, ipLong);
        Optional<String> refreshTokenOption = stringRedisService.get(RedisDatabase.REDIS_ZERO, jwtMobileRefreshTokenKey);
        if (refreshTokenOption.isPresent()) {

            Optional<User> userOptional = userService.findByIdNotDelete(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String accessToken = jwtUtils.generateAccessToken(user);
                String refreshToken = jwtUtils.generateRefreshToken(user);
                //token 存储redis
                stringRedisService.saveRefreshToken(RedisDatabase.REDIS_ZERO, jwtMobileRefreshTokenKey, refreshToken);
                stringRedisService.saveAccessToken(RedisDatabase.REDIS_ZERO, CacheKeys.getJwtAccessTokenKey( userId, ipLong), accessToken);
                //发送新的token
                JSONObject accessTokenJSON = new JSONObject();
                accessTokenJSON.put("accessToken", accessToken);
                ResultMessage message = new ResultMessage()
                        .correctness(SystemStatusCode.NEW_JWT.getCode(), SystemStatusCode.NEW_JWT.getName())
                        .setData(accessTokenJSON);
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
                return true;
            }
        }

        return false;
    }
}
