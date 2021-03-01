package ${packagePath}.redis.impl;

import ${packagePath}.enums.TimeUnits;
import ${packagePath}.properties.JwtProperties;
import ${packagePath}.redis.RedisDatabase;
import ${packagePath}.redis.StringRedisService;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

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
@Slf4j
@AllArgsConstructor
@Component
public class StringRedisServiceImpl implements StringRedisService {

    private final StringRedisTemplate stringRedisTemplate;

    private final StringRedisTemplate stringRedisTemplateFirst;

    private final JwtProperties jwtProperties;

    private StringRedisTemplate getStringRedisTemplate(RedisDatabase redisDatabase) {
        StringRedisTemplate template;
        if (Objects.equal(redisDatabase, RedisDatabase.REDIS_ZERO)) {
            template = stringRedisTemplate;
        } else if (Objects.equal(redisDatabase, RedisDatabase.REDIS_FIRST)) {
            template = stringRedisTemplateFirst;
        } else {
            throw new RuntimeException("不存在当前库的配置");
        }
        return template;
    }


    @Override
    public void incrementNotContainExpire(RedisDatabase redisDatabase, String key, long delta) {
        ValueOperations<String, String> valueOperations = getStringRedisTemplate(redisDatabase).opsForValue();
        valueOperations.increment(key, delta);
    }

    @Override
    public void increment(RedisDatabase redisDatabase, String key, long delta) {
        increment(redisDatabase, key, delta, 1, TimeUnit.DAYS);
    }

    @Override
    public void increment(RedisDatabase redisDatabase, String key, long delta, long timeout, TimeUnit unit) {
        ValueOperations<String, String> valueOperations = getStringRedisTemplate(redisDatabase).opsForValue();
        valueOperations.increment(key, delta);
        getStringRedisTemplate(redisDatabase).expire(key, timeout, unit);
    }

    @Override
    public void expire(RedisDatabase redisDatabase, String key, long timeout, TimeUnit unit) {
        getStringRedisTemplate(redisDatabase).expire(key, timeout, unit);
    }

    @Override
    public void expire(RedisDatabase redisDatabase, String key, String value, long timeout, TimeUnit unit) {
        ValueOperations<String, String> valueOperations = getStringRedisTemplate(redisDatabase).opsForValue();
        valueOperations.set(key, value);
        getStringRedisTemplate(redisDatabase).expire(key, timeout, unit);
    }

    @Override
    public void setNotContainExpire(RedisDatabase redisDatabase, String key, String value) {
        ValueOperations<String, String> valueOperations = getStringRedisTemplate(redisDatabase).opsForValue();
        valueOperations.set(key, value);

    }

    @Override
    public void setContainExpire(RedisDatabase redisDatabase, String key, String value) {
        setContainExpire(redisDatabase, key, value, 30, TimeUnit.MINUTES);
    }

    @Override
    public void setContainExpire(RedisDatabase redisDatabase, String key, String value, long timeout, TimeUnit unit) {
        ValueOperations<String, String> valueOperations = getStringRedisTemplate(redisDatabase).opsForValue();
        valueOperations.set(key, value, timeout, unit);
    }

    @Override
    public Optional<String> get(RedisDatabase redisDatabase, String key) {
        ValueOperations<String, String> valueOperations = getStringRedisTemplate(redisDatabase).opsForValue();
        String s = valueOperations.get(key);
        return Optional.ofNullable(s);
    }

    @Override
    public Integer getIntegerClear(RedisDatabase redisDatabase, String key) {
        ValueOperations<String, String> valueOperations = getStringRedisTemplate(redisDatabase).opsForValue();
        String s = valueOperations.get(key);
        this.delete(redisDatabase, key);
        return Optional.ofNullable(s).map(Integer::valueOf).orElse(0);
    }

    @Override
    public Long expireTime(RedisDatabase redisDatabase, String key) {
        return getStringRedisTemplate(redisDatabase).getExpire(key);
    }


    @Override
    public Set<String> keys(RedisDatabase redisDatabase, String key) {
        return getStringRedisTemplate(redisDatabase).keys(key);
    }

    @Override
    public List<String> multiGet(RedisDatabase redisDatabase, List<String> keys) {
        ValueOperations<String, String> valueOperations = getStringRedisTemplate(redisDatabase).opsForValue();
        return valueOperations.multiGet(keys);
    }

    @Override
    public void flushDB(RedisDatabase redisDatabase) {
        getStringRedisTemplate(redisDatabase).execute((RedisCallback<String>) connection -> {
            connection.flushDb();
            return "ok";
        });
    }

    @Override
    public Long dbSize(RedisDatabase redisDatabase) {
        return getStringRedisTemplate(redisDatabase).execute(RedisServerCommands::dbSize);
    }

    @Override
    public void delete(RedisDatabase redisDatabase, String key) {
        getStringRedisTemplate(redisDatabase).delete(key);
    }

    @Override
    public void deleteByLike(RedisDatabase redisDatabase, String key) {
        Set<String> keys = this.keys(redisDatabase, key + "*");
        getStringRedisTemplate(redisDatabase).delete(keys);
    }

    @Override
    public void saveAccessToken(RedisDatabase redisDatabase, String key, String value) {
        saveAccessToken(redisDatabase, key, value, false);
    }

    @Override
    public void saveRefreshToken(RedisDatabase redisDatabase, String key, String value) {
        saveRefreshToken(redisDatabase, key, value, false);
    }

    @Override
    public void saveAccessToken(RedisDatabase redisDatabase, String key, String value, Boolean rememberMe) {
        ValueOperations<String, String> valueOperations = getStringRedisTemplate(redisDatabase).opsForValue();
        if (rememberMe) {
            valueOperations.set(key, value, jwtProperties.getRememberMeExpiration(), TimeUnits.getEnum(jwtProperties.getRememberMeExpirationUnit()).getTimeUnit());
        } else {
            valueOperations.set(key, value, jwtProperties.getExpiration(), TimeUnits.getEnum(jwtProperties.getExpirationUnit()).getTimeUnit());
        }
    }

    @Override
    public void saveRefreshToken(RedisDatabase redisDatabase, String key, String value, Boolean rememberMe) {
        ValueOperations<String, String> valueOperations = getStringRedisTemplate(redisDatabase).opsForValue();
        if (rememberMe) {
            valueOperations.set(key, value, jwtProperties.getRememberMeRefreshExpiration(), TimeUnits.getEnum(jwtProperties.getRememberMeRefreshExpirationUnit()).getTimeUnit());
        } else {
            valueOperations.set(key, value, jwtProperties.getRefreshExpiration(), TimeUnits.getEnum(jwtProperties.getRefreshExpirationUnit()).getTimeUnit());
        }
    }

}
