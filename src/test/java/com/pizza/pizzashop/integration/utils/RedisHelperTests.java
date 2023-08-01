package com.pizza.pizzashop.integration.utils;

import com.pizza.pizzashop.utils.RedisHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import redis.clients.jedis.Jedis;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Testcontainers
class RedisHelperTests {
    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:latest")
            .withExposedPorts(6379);

    private RedisHelper redisHelper;
    private Jedis jedis;

    @BeforeEach
    void setUp() {
        String redisHost = redisContainer.getHost();
        Integer redisPort = redisContainer.getMappedPort(6379);

        redisHelper = new RedisHelper(redisHost, redisPort);
        jedis = new Jedis(redisHost, redisPort);
    }

    @AfterEach
    void tearDown() {
        jedis.flushAll();
        jedis.close();
    }

    @Test
    void testAddToSet() {
        String set = "testSet";
        String value = "testValue";

        redisHelper.add(set, value);

        assertTrue(jedis.sismember(set, value));
    }

    @Test
    void testCheckSetMembership_Exists() {
        String set = "testSet";
        String value = "testValue";
        jedis.sadd(set, value);

        boolean exists = redisHelper.check(set, value);

        assertTrue(exists);
    }

    @Test
    void testCheckSetMembership_NotExists() {
        String set = "testSet";
        String value = "testValue";

        boolean exists = redisHelper.check(set, value);

        assertFalse(exists);
    }
}
