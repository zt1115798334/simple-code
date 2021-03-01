package ${packagePath}.security.realm;

import ${packagePath}.cache.CacheKeys;
import ${packagePath}.entity.User;
import ${packagePath}.enums.LoginType;
import ${packagePath}.properties.AccountProperties;
import ${packagePath}.redis.RedisDatabase;
import ${packagePath}.redis.StringRedisService;
import ${packagePath}.security.token.PasswordToken;
import ${packagePath}.service.UserService;
import ${packagePath}.utils.DateUtils;
import ${packagePath}.utils.UserUtils;
import com.google.common.base.Objects;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 23:37
 * description:
 */
@Slf4j
public class PasswordRealm extends AuthorizingRealm {

    @Setter
    private UserService userService;

    @Setter
    private AccountProperties accountProperties;

    @Setter
    private StringRedisService stringRedisService;

    @Override
    public Class<?> getAuthenticationTokenClass() {
        return PasswordToken.class;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @SneakyThrows
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (!(authenticationToken instanceof PasswordToken)) {
            return null;
        }
        PasswordToken token = (PasswordToken) authenticationToken;
        String loginType = token.getLoginType();
        String name = token.getUsername();
        String shiroLoginCountKey = CacheKeys.getSecurityLoginCountKey(name);
        String shiroIsLockKey = CacheKeys.getSecurityIsLockKey(name);

        if (Objects.equal(LoginType.AJAX.getType(), loginType)) {
            String username = String.valueOf(token.getUsername());
            String password =String.valueOf(token.getPassword());
            Optional<String> shiroIsLock = stringRedisService.get(RedisDatabase.REDIS_ZERO, shiroIsLockKey);
            if (shiroIsLock.isPresent()) {
                if ("LOCK".equals(shiroIsLock.get())) {
                    Long expireTime = stringRedisService.expireTime(RedisDatabase.REDIS_ZERO, shiroIsLockKey);
                    throw new DisabledAccountException("由于多次输入错误密码，帐号已经禁止登录，请" + DateUtils.printHourMinuteSecond(expireTime) + "后重新尝试。");
                }
            }

            //密码进行加密处理  明文为  password+name
            // TODO: 2021/3/1 自己添加方法
//            Optional<User> userOptional = userService.findByAccount(name);
            Optional<User> userOptional = null;
            if (!userOptional.isPresent()) {
                //登录错误开始计数
                String msg = increment(shiroLoginCountKey, shiroIsLockKey);
                throw new AccountException(msg);
            }
            User sysUser = userOptional.get();
            UserUtils.checkUserState(sysUser);
            String pawDes = UserUtils.getEncryptPassword(username, password, sysUser.getSalt());
            // 从数据库获取对应用户名密码的用户
            String sysUserPassword = sysUser.getPassword();
            if (!Objects.equal(sysUserPassword, pawDes)) {
                //登录错误开始计数
                String msg = increment(shiroLoginCountKey, shiroIsLockKey);
                throw new AccountException(msg);
            } else {
                //登录成功
                //更新登录时间 last login time
                // TODO: 2021/3/1 自己添加方法
//                userService.updateLastLoginTime(sysUser.getId());
                //清空登录计数
                stringRedisService.setNotContainExpire(RedisDatabase.REDIS_ZERO, shiroLoginCountKey, "0");
                stringRedisService.delete(RedisDatabase.REDIS_ZERO, shiroIsLockKey);
            }
            log.info("身份认证成功，登录用户：" + name);
            return new SimpleAuthenticationInfo(sysUser, token.getPassword(), getName());
        }

        throw new AccountException("非法登陆！");
    }

    private String increment(String shiroLoginCountKey, String shiroIsLockKey) {
        //访问一次，计数一次
        stringRedisService.increment(RedisDatabase.REDIS_ZERO, shiroLoginCountKey, 1);
        //计数大于5时，设置用户被锁定10分钟
        return stringRedisService.get(RedisDatabase.REDIS_ZERO, shiroLoginCountKey).map(shiroLoginCount -> {
            Integer firstErrorAccountErrorCount = accountProperties.getFirstErrorAccountErrorCount();
            Integer secondErrorAccountErrorCount = accountProperties.getSecondErrorAccountErrorCount();
            Integer thirdErrorAccountErrorCount = accountProperties.getThirdErrorAccountErrorCount();
            int parseInt = Integer.parseInt(shiroLoginCount);
            int count = 0;
            if (parseInt <= firstErrorAccountErrorCount) {
                count = firstErrorAccountErrorCount - parseInt;
            } else if (parseInt <= secondErrorAccountErrorCount) {
                count = secondErrorAccountErrorCount - parseInt;
            } else if (parseInt <= thirdErrorAccountErrorCount) {
                count = thirdErrorAccountErrorCount - parseInt;
            }
            if (parseInt == firstErrorAccountErrorCount) {
                stringRedisService.expire(RedisDatabase.REDIS_ZERO, shiroIsLockKey, "LOCK", accountProperties.getFirstErrorAccountLockTime(), TimeUnit.MINUTES);
            } else if (parseInt == secondErrorAccountErrorCount) {
                stringRedisService.expire(RedisDatabase.REDIS_ZERO, shiroIsLockKey, "LOCK", accountProperties.getSecondErrorAccountLockTime(), TimeUnit.MINUTES);
            } else if (parseInt >= thirdErrorAccountErrorCount) {
                stringRedisService.expire(RedisDatabase.REDIS_ZERO, shiroIsLockKey, "LOCK", accountProperties.getThirdErrorAccountLockTime(), TimeUnit.HOURS);
            }
            return "帐号或密码错误！你还可以输入" + (count + 1) + "次";
        }).orElse("帐号或密码错误！");
    }

}
