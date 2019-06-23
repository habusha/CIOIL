package gov.taxes.infra.github.redis.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
@Profile({"redis"})
public class SpringRedisConfiguration {
	
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisProperties properties = redisProperties();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(); 
        jedisConnectionFactory.getStandaloneConfiguration().setHostName(properties.getHost());
        jedisConnectionFactory.getStandaloneConfiguration().setPort(properties.getPort());
        return jedisConnectionFactory;
    }

    @Bean
    @Primary
    public RedisProperties redisProperties() {
        return new RedisProperties();
    }
    
    @Bean
    public RedisTemplate<String, Object> redisTemplateObject() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

}
