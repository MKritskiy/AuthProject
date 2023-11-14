package com.example.PR.security.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
@Component
public class RedisTokenStore {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void storeToken(String username, String token) {
        try {
            redisTemplate.opsForValue().set(username, token);
        } catch (Exception e) {
            // Обработка ошибки подключения к Redis
            e.printStackTrace(); // или логирование
        }
    }

    public void deleteToken(String username) {
        try {
            redisTemplate.delete(username);
        } catch (Exception e) {
            // Обработка ошибки подключения к Redis
            e.printStackTrace(); // или логирование
        }
    }

    public String getToken(String username) {
        try {
            return redisTemplate.opsForValue().get(username);
        } catch (Exception e) {
            // Обработка ошибки подключения к Redis
            e.printStackTrace(); // или логирование
            return null; // или другой код ошибки
        }
    }
}