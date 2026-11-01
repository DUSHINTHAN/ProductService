package com.scaler.productservice.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.productservice.models.Product;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Product> redisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper){

        RedisTemplate<String, Product> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // String serialization for Redis keys
        StringRedisSerializer stringSerializer =
                new StringRedisSerializer();

        // JSON serialization for Product values
        Jackson2JsonRedisSerializer<Product> productSerializer =
                new Jackson2JsonRedisSerializer<>(
                        objectMapper, Product.class
                );

        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);

        redisTemplate.setValueSerializer(productSerializer);
        redisTemplate.setHashValueSerializer(productSerializer);

        return redisTemplate;

    }
}
