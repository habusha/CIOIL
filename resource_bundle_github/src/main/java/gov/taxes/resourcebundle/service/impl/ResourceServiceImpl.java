package gov.taxes.resourcebundle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.taxes.infra.github.base.BaseDAO;
import gov.taxes.infra.github.base.impl.BaseServiceImpl;
import gov.taxes.infra.github.exception.BusinessException;
import gov.taxes.infra.github.service.CacheService;
import gov.taxes.resourcebundle.dao.ResourceDAO;
import gov.taxes.resourcebundle.dto.DeleteResourceBundleDTO;
import gov.taxes.resourcebundle.dto.ResourceDTO;
import gov.taxes.resourcebundle.enums.LocaleEnum;
import gov.taxes.resourcebundle.service.ResourceService;

@Service
public class ResourceServiceImpl extends BaseServiceImpl<ResourceDTO, Long> implements ResourceService {
	
	private final String BUNDLE_KEY = "RESOURCE_BUNDLE";
	
	@Autowired
	private ResourceDAO resourceDAO;
	
	@Autowired
	private CacheService cacheService;
	
	@Override
	public void updateCache(Set<String> systems, Set<String> locales) {
		if (systems.isEmpty() || locales.isEmpty())
			return;
		
		systems.forEach(system -> {
			locales.stream().forEach(locale -> {
				loadResourcesPerSystemAndLocale(system, locale);
			});
		});
	}

	@Override
	public Map<String, Map<String, String>> getAllResourcesBySystemAndLocales(String system, List<String> locales) {
		Map<String, Map<String, String>> resources = new HashMap<String, Map<String, String>>(); 
		for (String locale : locales) {
			Map<String, String> resourcesPerLocale = cacheService.getMapValues(getKey(system, locale));
			if (resourcesPerLocale == null || resourcesPerLocale.isEmpty())
				resourcesPerLocale = loadResourcesPerSystemAndLocale(system, locale);
			resources.put(locale, resourcesPerLocale);
		}
		return resources;
	}
	
	private Map<String, String> loadResourcesPerSystemAndLocale(String system, String locale) {
		List<ResourceDTO> resourcesObjectes = resourceDAO.findBySystemAndLocale(system, LocaleEnum.valueOf(locale));
		Map<String, String> resoucesMap = new HashMap<String, String>();
		if (resourcesObjectes != null && !resourcesObjectes.isEmpty()) {
			resoucesMap = resourcesObjectes.stream().collect(Collectors.toMap(ResourceDTO::getIdent, ResourceDTO::getMessage));
			cacheService.setMapValues(getKey(system, locale), resoucesMap);
		}
		return resoucesMap;
	}
	
	@Override
	public String findMessageByLocaleAndIdentAndSystem(String locale, String ident, String system) throws BusinessException {
		String message = cacheService.getValueFromMap(getKey(system, locale), ident);
		if (message == null) {
			ResourceDTO resourceDTO = findByLocaleAndIdentAndSystem(LocaleEnum.valueOf(locale), ident, system);
			if (resourceDTO == null)
				throw new BusinessException("The record: ident = " + ident 
				+ ", locale = " + locale + ", system = " + system + " does not exist!");
			cacheService.setValueToMap(getKey(system, locale), ident, resourceDTO.getMessage());
		}
		return message;
	}
	
	@Override
	public ResourceDTO findByLocaleAndIdentAndSystem(LocaleEnum locale, String ident, String system) {
		return resourceDAO.findByLocaleAndIdentAndSystem(locale, ident, system);
	}
	
	@Override
	public Map<String, Map<String, String>> findResourcesByLocalesAndIdentsAndSystem(List<String> locales, List<String> idents,
			String system) {
		Map<String, Map<String, String>> resourcesByIdents = new HashMap<String, Map<String, String>>();
		Map<String, Map<String, String>> allResources = getAllResourcesBySystemAndLocales(system, locales);
		for (Entry<String, Map<String, String>> entry : allResources.entrySet()) {

			Map<String, String> collect = entry.getValue().entrySet().stream()
					.filter(x -> idents.contains(x.getKey()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

			if (!resourcesByIdents.containsKey(entry.getKey())) 
				resourcesByIdents.put(entry.getKey(), new HashMap<String, String>());
			resourcesByIdents.get(entry.getKey()).putAll(collect);
		}
		return resourcesByIdents;
	}

	@Override
	public void deleteFromCache(DeleteResourceBundleDTO resource) {
		cacheService.deleteValueFromMap(getKey(resource.getSystem(), resource.getLocale().toString()), resource.getIdent());
	}
	
	@Override
	protected BaseDAO<ResourceDTO, Long> getDAO() {
		return resourceDAO;
	}
	
	public String getKey(String system, String locale) {
		return BUNDLE_KEY + "_" + system + "_" + locale;
	}
	
	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

}
