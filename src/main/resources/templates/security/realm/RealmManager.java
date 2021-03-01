package ${packagePath}.security.realm;

import ${packagePath}.properties.AccountProperties;
import ${packagePath}.redis.StringRedisService;
import ${packagePath}.security.token.JwtToken;
import ${packagePath}.security.token.PasswordToken;
import ${packagePath}.service.UserService;
import ${packagePath}.utils.JwtUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 23:26
 * description: realm管理器
 */
@AllArgsConstructor
@Component
public class RealmManager {

    private final UserService userService;

    private final AccountProperties accountProperties;

    private final StringRedisService stringRedisService;

//    private final PermissionService permissionService;

    private final JwtUtils jwtUtils;


    public List<Realm> initGetRealm() {
        List<Realm> realmList = Lists.newArrayList();
        // ----- password
        PasswordRealm passwordRealm = new PasswordRealm();
        passwordRealm.setUserService(userService);
        passwordRealm.setAccountProperties(accountProperties);
        passwordRealm.setStringRedisService(stringRedisService);
        passwordRealm.setAuthenticationTokenClass(PasswordToken.class);
        realmList.add(passwordRealm);
        // ----- jwt
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setUserService(userService);
        jwtRealm.setJwtUtils(jwtUtils);
        jwtRealm.setStringRedisService(stringRedisService);
//        jwtRealm.setPermissionService(permissionService);
        jwtRealm.setAuthenticationTokenClass(JwtToken.class);
        realmList.add(jwtRealm);

        return Collections.unmodifiableList(realmList);
    }
}
