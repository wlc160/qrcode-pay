package com.qrpay.demo.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {
	
	/**
	 * 简要说明:redis 读取内容的template <br>
	 * 详细说明:TODO
	 * 创建者：lcwen
	 * 创建时间:2019年8月23日 下午2:51:33
	 * 更新者:
	 * 更新时间:
	 * @param factory
	 * @return
	 */
	@Bean
	public RedisTemplate<String, Object> getRedisTemplate(
			RedisConnectionFactory factory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(factory);
		redisTemplate.setKeySerializer(new StringRedisSerializer()); // key的序列化类型
		redisTemplate.setValueSerializer(new RedisObjectSerializer()); // value的序列化类型
		return redisTemplate;
	}
    
}
