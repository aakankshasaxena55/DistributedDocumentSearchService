package com.assess.docservice.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TenantRateLimiter {

    private static final int LIMIT = 100;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    private final RedisTemplate<String, String> redisTemplate;

    public TenantRateLimiter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String tenantId) {
        String key = "rate:tenant:" + tenantId;

        Long count = redisTemplate.opsForValue().increment(key);

        if (count != null && count == 1) {
            redisTemplate.expire(key, WINDOW);
        }

        return count != null && count <= LIMIT;
    }

    public Bucket resolveBucket(String tenantId, long limitPerMinute) {
        Bandwidth limit = Bandwidth.simple(limitPerMinute, Duration.ofMinutes(1));

        // Use Redis to persist bucket state manually
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    public boolean tryConsume(String tenantId) {
        Bucket bucket = resolveBucket(tenantId, 1000); // example per-minute
        return bucket.tryConsume(1);
    }
}


