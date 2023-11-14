package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTokenStore {
    @Autowired
    private StringRedisTemplate redisTemplate;
    public void storeToken(String username, String token) {
        redisTemplate.opsForValue().set(username, token);
    }
    public void deleteToken(String username) {
        redisTemplate.delete(username);
    }

    public String getToken(String username) {
        return redisTemplate.opsForValue().get(username);
    }
}
