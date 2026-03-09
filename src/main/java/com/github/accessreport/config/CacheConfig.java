package com.github.accessreport.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.*;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {


    // configuring caffeine cache to store generated reports
    @Bean
    public CacheManager cacheManager(){

        CaffeineCacheManager manager = new CaffeineCacheManager();

        manager.setCaffeine(
                // cache size and expiration settings
                Caffeine.newBuilder()
                        .maximumSize(100)
                        .expireAfterWrite(5, TimeUnit.MINUTES)
        );

        return manager;
    }

}
