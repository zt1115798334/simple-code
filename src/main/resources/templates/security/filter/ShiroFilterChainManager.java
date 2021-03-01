package ${packagePath}.security.filter;

import ${packagePath}.redis.StringRedisService;
import ${packagePath}.service.UserService;
import ${packagePath}.utils.JwtUtils;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 22:24
 * description: ShiroFilter 管理器
 */
@Slf4j
@Component
@AllArgsConstructor
public class ShiroFilterChainManager {

    private final UserService userService;

    private final StringRedisService stringRedisService;

    private final JwtUtils jwtUtils;

//    private final PermissionService permissionService;

    // 初始化获取过滤链
    public Map<String, Filter> initGetFilters(CacheManager cacheManager) {
        Map<String, Filter> filters = Maps.newLinkedHashMap();
        JwtFilter jwtFilter = new JwtFilter();
        jwtFilter.setUserService(userService);
        jwtFilter.setStringRedisService(stringRedisService);
        jwtFilter.setJwtUtils(jwtUtils);
        filters.put("JwtFilter", jwtFilter);
        return filters;
    }

    /**
     * 初始化获取过滤链规则
     *
     * @return Map<String, String>
     */
    public Map<String, String> initGetFilterChain() {
        Map<String, String> filterChain = Maps.newLinkedHashMap();
        filterChain.put("/static/**", "anon");
        filterChain.put("/webSocketMessage", "anon");


        filterChain.put("/api/login/ajaxLogin", "anon");

        filterChain.put("/page/convertUrl", "anon");
        filterChain.put("/api/**", "JwtFilter");
        filterChain.put("/inter/login/logout", "JwtFilter");

        filterChain.put("/api/login/logout", "JwtFilter");

        //   过滤链定义，从上向下顺序执行，一般将 /**放在最为下边
        return filterChain;
    }
}
