package gov.taxes.infra.github.service;

import java.util.Map;

public interface CacheService {

	/**
	 * 
	 * @param Gets Map<String, String> from cache.
	 * @return Map
	 */
	Map<String, String> getMapValues(String key);

	/**
	 * Sets Map<String, String> to cache.
	 * 
	 * @param key
	 * @param map
	 */
	void setMapValues(String key, Map<String, String> map);

	/**
	 * Gets value from map
	 * 
	 * @param key
	 * @param mapKey
	 * @return String
	 */
	String getValueFromMap(String key, String mapKey);

	/**
	 * Sets value to map.
	 * 
	 * @param key
	 * @param mapKey
	 * @param value
	 */
	void setValueToMap(String key, String mapKey, String value);

	/**
	 * Deletes value from map
	 * 
	 * @param key
	 * @param mapKey
	 */
	void deleteValueFromMap(String key, String mapKey);

}