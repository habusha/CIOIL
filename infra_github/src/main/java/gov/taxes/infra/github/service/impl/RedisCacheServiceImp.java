package gov.taxes.infra.github.service.impl;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import gov.taxes.infra.github.service.CacheService;

@Service
@Profile("redis")
public class RedisCacheServiceImp implements CacheService {

	@Autowired
	private RedisTemplate<String, String> redisMapTemplate;

	private HashOperations<String, String, String> mapOperations;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostConstruct
	private void init() {
		mapOperations = redisMapTemplate.opsForHash();
	}

	@Override
	public Map<String, String> getMapValues(String key) {
		Map<String, String> map = null;
		try {
			map = mapOperations.entries(key);
			logger.info("Gets map with key = " + key + " from redis cache");
		} catch (Exception e) {
			logger.info("Redis is down");
		}
		return map;
	}

	@Override
	public void setMapValues(String key, Map<String, String> map) {
		try {
			mapOperations.putAll(key, map);
			logger.info("Puts map with key = " + key + " to redis cache");
		} catch (Exception e) {
			logger.info("Redis is down");
		}
	}

	@Override
	public String getValueFromMap(String key, String mapKey) {
		String value = null;
		try {
			value = mapOperations.get(key, mapKey);
			logger.info("Gets value with key = " + key + "_" + mapKey + " from redis cache");
		} catch (Exception e) {
			logger.info("Redis is down");
		}
		return value;
	}

	@Override
	public void setValueToMap(String key, String mapKey, String value) {
		try {
			mapOperations.putIfAbsent(key, mapKey, value);
			logger.info("Puts value with key = " + key + "_" + mapKey + " to redis cache");
		} catch (Exception e) {
			logger.info("Redis is down");
		}
	}

	@Override
	public void deleteValueFromMap(String key, String mapKey) {
		try {
			mapOperations.delete(key, mapKey);
			logger.info("Deletes value with key = " + key + "_" + mapKey + " to redis cache");
		} catch (Exception e) {
			logger.info("Redis is down");
		}
	}

}
