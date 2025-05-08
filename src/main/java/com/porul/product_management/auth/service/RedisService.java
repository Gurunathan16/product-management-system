package com.porul.product_management.auth.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class RedisService
{
    RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }

    private String key(String username)
    {
        return "users_sessions" + username;
    }

    public void evictIfSessionLimitReached(String username, Integer maximumSessions)
    {
        Long size = redisTemplate.opsForList().size(key(username));

        if(size != null && size >= maximumSessions)
            redisTemplate.opsForList().leftPop(key(username));
    }

    public void storeRefreshToken(String username, String refreshToken)
    {
        redisTemplate.opsForList().rightPush(key(username), refreshToken);
        redisTemplate.expire(key(username), Duration.ofDays(7));
    }

    public boolean isRefreshTokenValid(String username, String refreshToken)
    {
        List<Object> refreshTokens = redisTemplate.opsForList().range(key(username), 0, -1);
        return refreshTokens != null && refreshTokens.contains(refreshToken);
    }

    public void removeRefreshToken(String username, String refreshToken)
    {
        redisTemplate.opsForList().remove(key(username), 1L, refreshToken);
    }
}
