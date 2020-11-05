package com.example.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String CACHE_FOR_3_SECONDS = "cacheFor3Seconds";

    @Bean
    public CacheManager cacheManager(){
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        Cache<Object, Object> caffeineBuilder = Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build();

        CaffeineCache caffeineCache = new CaffeineCache(CACHE_FOR_3_SECONDS, caffeineBuilder);
        cacheManager.setCaches(Collections.singletonList(caffeineCache));

        return cacheManager;
    }
}
