package ${packagePath}.cache;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/12 15:30
 * description:
 */
public class CacheKeys {

    private static final String INTER_CACHE = "inter_cache";

    /**
     * 用户登录次数计数
     */
    private static final String PREFIX_SECURITY_LOGIN_COUNT = "security:login_count_";

    /**
     * 用户登录是否被锁定
     */
    private static final String PREFIX_SECURITY_IS_LOCK = "security:is_lock_";

    /**
     * security 缓存
     */
    private static final String PREFIX_SECURITY_CACHE = "security:cache:";

    /**
     * jwt验证token
     */
    private static final String PREFIX_JWT_ACCESS_TOKEN = "jwt:access_token:";

    private static final String PREFIX_JPUSH_TOKEN = "jpush:";

    /**
     * jwt刷新token
     */
    private static final String PREFIX_JWT_REFRESH_TOKEN = "jwt:refresh_token:";


    ///////////////////////////////////////////////////////////////////////////
    // 以下是图表缓存key值
    ///////////////////////////////////////////////////////////////////////////


    public static String getInterCache(Long userId) {
        return INTER_CACHE + ":" + userId;
    }

    public static String getSecurityLoginCountKey(String phoneNum) {
        return PREFIX_SECURITY_LOGIN_COUNT + phoneNum;
    }

    public static String getSecurityIsLockKey(String phoneNum) {
        return PREFIX_SECURITY_IS_LOCK + phoneNum;
    }

    public static String getSecurityCacheKey(Long key) {
        return PREFIX_SECURITY_CACHE + ":" + key;
    }

    public static String getJwtAccessTokenKey( Long userId, Long ipLong) {
        return PREFIX_JWT_ACCESS_TOKEN  + ":" + userId + ":" + ipLong;
    }

    public static String getJwtRefreshTokenKey( Long userId, Long ipLong) {
        return PREFIX_JWT_REFRESH_TOKEN  + ":" + userId + ":" + ipLong;
    }


}
