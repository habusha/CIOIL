package gov.taxes.resourcebundle.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.taxes.infra.github.base.BaseService;
import gov.taxes.infra.github.exception.BusinessException;
import gov.taxes.resourcebundle.dto.DeleteResourceBundleDTO;
import gov.taxes.resourcebundle.dto.ResourceDTO;
import gov.taxes.resourcebundle.enums.LocaleEnum;

public interface ResourceService extends BaseService<ResourceDTO, Long> {
		
	/** 
	 * Updates cache with the updated resources.
	 * @param systemSet
	 * @param locales
	 */
	void updateCache(Set<String> systems, Set<String> locales);
	
	/**
	 * Deletes resource from cache.
	 * @param resource
	 */
	void deleteFromCache(DeleteResourceBundleDTO resource);

	/**
	 * Returns all resources by system and locales.
	 * @param system
	 * @return Map<String, Map<String, String>>
	 */
	Map<String, Map<String, String>> getAllResourcesBySystemAndLocales(String system, List<String> locales);
	
	/**
	 * Returns resource by @UniqueConstraint (locale, ident and system).
	 * @param locale
	 * @param ident
	 * @param system
	 * @return @ResourceDTO
	 */
	ResourceDTO findByLocaleAndIdentAndSystem(LocaleEnum locale, String ident, String system);

	/**
	 * Returns resources by @UniqueConstraint (locale, idents and system).
	 * @param locale
	 * @param idents
	 * @param system
	 * @return
	 */
	Map<String, Map<String, String>> findResourcesByLocalesAndIdentsAndSystem(List<String> locales, List<String> idents, String system);

	/**
	 * Returns a message by @UniqueConstraint (locale, ident and system) from cache.
	 * @param locale
	 * @param ident
	 * @param system
	 * @return @ResourceDTO
	 * @throws BusinessException 
	 */
	String findMessageByLocaleAndIdentAndSystem(String locale, String ident, String system) throws BusinessException;

}
