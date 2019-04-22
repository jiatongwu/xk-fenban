package cn.xvkang.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfig {

	@Autowired
	private JedisConnectionFactory jedisConnectionFactory ;
	@Bean
	<T> RedisTemplate<String, T> redisTemplate() {
		RedisTemplate<String, T> template = new RedisTemplate<String, T>();
		template.setConnectionFactory(jedisConnectionFactory);
		template.setStringSerializer(new StringRedisSerializer());
		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		template.setEnableDefaultSerializer(true);
		template.afterPropertiesSet();
		return template;
	}
	
}
