package gov.taxes.infra.github.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import gov.taxes.infra.github.service.CacheService;

@Service
@Profile("!redis")
public class MockCacheServiceImp implements CacheService {
	
	Map<String, Map<String, String>> mapOfMaps;
	
	@PostConstruct
	public void init() {
		mapOfMaps = new HashMap<>();
	}

	@Override
	public Map<String, String> getMapValues(String key) {
		return mapOfMaps.get(key);
	}

	@Override
	public void setMapValues(String key, Map<String, String> map) {
		mapOfMaps.put(key, map);
	}

	@Override
	public String getValueFromMap(String key, String mapKey) {
		return mapOfMaps.get(key).get(mapKey);
	}

	@Override
	public void setValueToMap(String key, String mapKey, String Value) {
		if (mapOfMaps.get(key) == null) {
			mapOfMaps.put(key, new HashMap<>());
		} else {
			mapOfMaps.get(key).remove(mapKey);
		}
		mapOfMaps.get(key).put(mapKey, Value);
	}

	@Override
	public void deleteValueFromMap(String key, String mapKey) {
		if (mapOfMaps.get(key) != null) {
			mapOfMaps.get(key).remove(mapKey);
		}
	}

}
