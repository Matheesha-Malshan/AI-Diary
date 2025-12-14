package org.AiDiary.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String,String> getCache(){

        return  Caffeine.newBuilder()
                .initialCapacity(20) // Sets the initial size of the cache
                .maximumSize(200)    // Restricts the cache to 200 entries
                .expireAfterWrite(2, TimeUnit.MINUTES)   // Evict entries 24 hours after creation
                .build();


    }

}
