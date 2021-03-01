package ${packagePath}.security.realm;

import ${packagePath}.cache.CacheKeys;
import ${packagePath}.entity.User;
import ${packagePath}.enums.SystemStatusCode;
import ${packagePath}.redis.RedisDatabase;
import ${packagePath}.redis.StringRedisService;
import ${packagePath}.security.token.JwtToken;
import ${packagePath}.service.UserService;
import ${packagePath}.utils.JwtUtils;
import ${packagePath}.utils.NetworkUtil;
import com.google.common.collect.Sets;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Optional;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/19 10:41
 * description: 进行授权 以及 jwt的验证
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Setter
    private JwtUtils jwtUtils;
    @Setter
    private UserService userService;
    @Setter
    private StringRedisService stringRedisService;
//    @Setter
//    private PermissionService permissionService;

    @Override
    public Class<?> getAuthenticationTokenClass() {
        // 此realm只支持jwtToken
        return JwtToken.class;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        Set<String> permissionSet = Sets.newHashSet();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        if (user != null) {
////            if (UserUtils.checkUserExpired(user)) {
////                permissionSet.add(SystemStatusCode.USER_EXPIRE.getName());
////            } else if (UserUtils.checkUserFrozen(user)) {
////                permissionSet.add(SystemStatusCode.USER_FROZEN.getName());
////            } else if (Objects.equal(user.getDeleteState(), DeleteState.DELETE.getCode())) {
////                permissionSet.add(SystemStatusCode.USER_DELETE.getName());
////            } else {
////                permissionSet.add(SystemStatusCode.USER_NORMAL.getName());
//                //添加系统权限
////                List<Permission> permissionList = permissionService.findByUserId(user.getId());
////                Set<String> collect = permissionList.stream()
////                        .map(Permission::getPermission)
////                        .filter(StringUtils::isNotEmpty)
////                        .collect(Collectors.toSet());
////                permissionSet.addAll(collect);
////                if (UserUtils.checkUserAccountType(user)) {
////                    permissionSet.add(SystemStatusCode.USER_TYPE_ADMIN.getName());
////                }else {
////                    permissionSet.add(SystemStatusCode.USER_TYPE_ORDINARY.getName());
////                }
//            }
//
//        } else {
//            permissionSet.add(SystemStatusCode.USER_NOT_FOUND.getName());
//        }
//        info.setStringPermissions(permissionSet);

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        开始验证token值的正确...
        if (!(authenticationToken instanceof JwtToken)) {
            return null;
        }
        JwtToken jwtToken = (JwtToken) authenticationToken;

        String token = (String) jwtToken.getCredentials();
        String deviceInfo = jwtToken.getDeviceInfo();
        Long userId = jwtToken.getUserId();
        String ip = jwtToken.getIpHost();
        Long ipLong = NetworkUtil.ipToLong(ip);
        if (StringUtils.isNotBlank(token)) {
            if (userId != null) {
                Optional<String> accessTokenRedis = stringRedisService.get(RedisDatabase.REDIS_ZERO, CacheKeys.getJwtAccessTokenKey(userId, ipLong));
                if (accessTokenRedis.isPresent()) {
                    Optional<User> userOptional = userService.findByIdNotDelete(userId);
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        if (jwtUtils.validateToken(token, user)) {
                            return new SimpleAuthenticationInfo(user, token, getName());
                        } else {
                            throw new AuthenticationException(SystemStatusCode.ACCESS_TOKEN_EXPIRE.getName());
                        }
                    } else {
                        throw new AuthenticationException(SystemStatusCode.USER_NOT_FOUND.getName());
                    }
                } else {
                    throw new AuthenticationException(SystemStatusCode.ACCESS_TOKEN_EXPIRE.getName());
                }

            } else {
                throw new AuthenticationException(SystemStatusCode.JWT_NOT_FOUND.getName());
            }
        }
        throw new AuthenticationException(SystemStatusCode.JWT_NOT_FOUND.getName());
    }
}
