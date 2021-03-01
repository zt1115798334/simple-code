package ${packagePath}.redis;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/13 10:48
 * description:
 */
public interface StringRedisService {

    /**
     * 增长值
     *
     * @param key   key值
     * @param delta 增长值
     */
    void incrementNotContainExpire(RedisDatabase redisDatabase, String key, long delta);

    void increment(RedisDatabase redisDatabase, String key, long delta);

    /**
     * 增长值
     *
     * @param key     key值
     * @param delta   增长值
     * @param timeout 缓存时间
     * @param unit    缓存时间单位
     */
    void increment(RedisDatabase redisDatabase, String key, long delta, long timeout, TimeUnit unit);

    /**
     * 设置过期
     *
     * @param key     key
     * @param value   value
     * @param timeout 缓存时间
     * @param unit    缓存时间单位
     */
    void expire(RedisDatabase redisDatabase, String key, long timeout, TimeUnit unit);

    void expire(RedisDatabase redisDatabase, String key, String value, long timeout, TimeUnit unit);

    /**
     * setNotContainExpire
     *
     * @param key   key
     * @param value value
     */
    void setNotContainExpire(RedisDatabase redisDatabase, String key, String value);

    /**
     * setContainExpire 包含过期时间
     *
     * @param key   key
     * @param value value
     */
    void setContainExpire(RedisDatabase redisDatabase, String key, String value);

    /**
     * setNotContainExpire 包含过期时间
     *
     * @param key     key
     * @param value   value
     * @param timeout 缓存时间
     * @param unit    缓存时间单位
     */
    void setContainExpire(RedisDatabase redisDatabase, String key, String value, long timeout, TimeUnit unit);

    /**
     * @param key key
     * @return Optional<String>
     */
    Optional<String> get(RedisDatabase redisDatabase, String key);


    /**
     * @param key key
     * @return Optional<String>
     */
    Integer getIntegerClear(RedisDatabase redisDatabase, String key);

    Long expireTime(RedisDatabase redisDatabase, String key);

    /**
     * keys
     *
     * @param key key
     * @return Set<String>
     */
    Set<String> keys(RedisDatabase redisDatabase, String key);

    /**
     * keys
     *
     * @param keys key集合
     * @return Set<String>
     */
    List<String> multiGet(RedisDatabase redisDatabase, List<String> keys);

    /**
     * flushDB
     */
    void flushDB(RedisDatabase redisDatabase);

    /**
     * dbSize
     */
    Long dbSize(RedisDatabase redisDatabase);

    /**
     * 删除
     *
     * @param key key
     */
    void delete(RedisDatabase redisDatabase, String key);

    /**
     * 删除
     *
     * @param key key
     */
    void deleteByLike(RedisDatabase redisDatabase, String key);

    /**
     * 保存 accessToken
     *
     * @param key   key
     * @param value val
     */
    void saveAccessToken(RedisDatabase redisDatabase, String key, String value);

    /**
     * 保存 refreshToken
     *
     * @param key   key
     * @param value value
     */
    void saveRefreshToken(RedisDatabase redisDatabase, String key, String value);

    /**
     * 保存 accessToken
     *
     * @param key        key
     * @param value      val
     * @param rememberMe 是否记住我
     */
    void saveAccessToken(RedisDatabase redisDatabase, String key, String value, Boolean rememberMe);

    /**
     * 保存 refreshToken
     *
     * @param key        key
     * @param value      value
     * @param rememberMe 是否记住我
     */
    void saveRefreshToken(RedisDatabase redisDatabase, String key, String value, Boolean rememberMe);

}
