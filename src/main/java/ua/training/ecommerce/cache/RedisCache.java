package ua.training.ecommerce.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCache implements Cache {

    private final ObjectMapper objectMapper;
    private final Jedis jedis;

    @Override
    public <T> T getItem(String key, Class<T> type) {
        String jsonObject = jedis.get(key);
        try {
            return objectMapper.readValue(jsonObject, type);
        } catch (Exception e) {
            log.error("getItem", e);
            return null;
        }
    }

    @SneakyThrows
    @Override
    public <T> void setItem(String key, T item) {
        String jsonItem = objectMapper.writeValueAsString(item);
        jedis.set(key, jsonItem);
    }

    @Override
    public void removeItem(String key) {
        jedis.del(key);
    }

    @Override
    public <T> Collection<T> getList(String key, Class<T> type) {
        return getListFromRedis(key, type);
    }

    @Override
    public <T> Collection<T> addItemToList(String key, T item, Class<T> type) {
        try {
            String jsonItem = objectMapper.writeValueAsString(item);
            jedis.sadd(key, jsonItem);

            return this.getListFromRedis(key, type);
        } catch (Exception e) {
            log.error("addItemToList", e);
            return null;
        }
    }

    @Override
    public <T> Collection<T> removeItemFromList(String key, T item, Class<T> type) {

        getListFromRedis(key, item.getClass()).forEach(row -> {
            if (row.equals(item)) {
                try {
                    String jsonItem = objectMapper.writeValueAsString(row);
                    jedis.srem(key, jsonItem);
                } catch (Exception e) {
                    log.error("removeItemFromList", e);
                }
            } else {
                log.warn("Can't find object in Redis list with key {} for item {}", key, item);
            }
        });

        return getListFromRedis(key, type);
    }


    private <T> Collection<T> getListFromRedis(String key, Class<T> type) {
        return jedis.smembers(key)
                .stream()
                .map(jsonItem -> {
                    try {
                        return objectMapper.readValue(jsonItem, type);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .toList();
    }
}

