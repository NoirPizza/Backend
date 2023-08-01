package com.pizza.pizzashop.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * This class provides methods for interacting with a Redis server for caching purposes.
 */
@Component
public class RedisHelper {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private Integer port;

    private final Jedis jedis;

    public RedisHelper() {
        // TODO: 'host' and 'port' aren't fetching from .env fast enough thus app crashes. Find a way to fix it
        this.jedis = new Jedis();
    }

    public RedisHelper(String host, Integer port) {
        this.jedis = new Jedis(host, port);
    }

    /**
     * Adds a value to a Redis set.
     *
     * @param set   The name of the set to which the value will be added.
     * @param value The value to add to the set.
     */
    public void add(String set, String value) {
        jedis.sadd(set, value);
    }

    /**
     * Checks if a value exists in a Redis set.
     *
     * @param set   The name of the set to check.
     * @param value The value to check for existence in the set.
     * @return True if the value exists in the set; otherwise, false.
     */
    public boolean check(String set, String value) {
        return jedis.sismember(set, value);
    }
}
