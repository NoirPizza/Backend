package com.pizza.pizzashop.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

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

    public void add(String set, String value) {
        jedis.sadd(set, value);
    }

    public boolean check(String set, String value) {
        return jedis.sismember(set, value);
    }
}
