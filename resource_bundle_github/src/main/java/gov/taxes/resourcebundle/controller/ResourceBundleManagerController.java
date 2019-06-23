package gov.taxes.resourcebundle.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import gov.taxes.infra.github.base.BaseService;
import gov.taxes.infra.github.base.impl.BaseFEControllerImpl;
import gov.taxes.infra.github.exception.BusinessException;
import gov.taxes.infra.github.exception.TechnicalException;
import gov.taxes.infra.github.exception.ValidationError;
import gov.taxes.infra.github.reader.ExcelReader;
import gov.taxes.infra.github.utils.FileUtils;
import gov.taxes.resourcebundle.dto.DeleteResourceBundleDTO;
import gov.taxes.resourcebundle.dto.ResourceDTO;
import gov.taxes.resourcebundle.enums.LocaleEnum;
import gov.taxes.resourcebundle.service.ResourceService;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * The class handles REST requests for manager resource bundle.
 * 
 * @author Eitan Nosraty
 * 
 */
@RequestMapping("/resource_bundle_manager")
public class ResourceBundleManagerController extends BaseFEControllerImpl<ResourceDTO, ResourceDTO> {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ResourceService resourceService;
	
	@ApiOperation(value = "Inserts or updates resource bundle records.", 
			notes = "Excel file is supported.")
	@PostMapping("/uploadResourceBundle")
	@Transactional(propagation = Propagation.REQUIRED)
	public void uploadResourcesBundleFile(@RequestParam("file") MultipartFile file) throws Exception {
		List<ValidationError> errors = FileUtils.validate(file);
		Set<String> systems = new HashSet<>();
		Set<String> loacles = new HashSet<>();
		if (errors == null || errors.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				List<Map<String, Object>> excelRowsAsMap = ExcelReader.readAsJson(bytes);
				List<ResourceDTO> resources = new ArrayList<>();
				if (excelRowsAsMap != null) {
					for (Map<String, Object> map : excelRowsAsMap) {
						updateResourceBundle(systems, loacles, resources, map);
					}
				} else {
					throw new BusinessException("Empty file.");
				}
				// save the resources
				resourceService.save(resources);
				resourceService.updateCache(systems, loacles);
			} catch (Exception e) {
				throw e;
			}
		} else {
			logger.debug("Errors in file! {}", errors);
			throw new BusinessException("Errors in file:", errors);
		}
	}

	/**
	 * Creates/Updates resource bundle record.
	 * @param systems
	 * @param loacles
	 * @param resources
	 * @param map
	 * @throws BusinessException
	 */
	private void updateResourceBundle(Set<String> systems, Set<String> loacles, List<ResourceDTO> resources,
			Map<String, Object> map) throws BusinessException {
		LocaleEnum locale = LocaleEnum.valueOf(map.get("locale"));
		if (locale == null)
			throw new BusinessException("Language doesn't exist.");
		ResourceDTO resource = resourceService.findByLocaleAndIdentAndSystem(locale, (String)map.get("ident"), (String)map.get("system"));

		if (resource != null && resource.getMessage().equals((String)map.get("message")))
			return;

		if (resource == null) {
			resource = new ResourceDTO((String)map.get("ident"), locale, (String)map.get("message"), (String)map.get("system"));
		} else  {
			resource.setMessage((String)map.get("message"));
		}
		systems.add((String)map.get("system"));
		loacles.add((String)map.get("locale"));
		resources.add(resource);
	}
	
	@ApiOperation(value = "Deletes resource bundle records from the DB.", 
			notes = "The Json must contain the parameters: locale, ident and system.")
	@DeleteMapping("/deleteResourceBundle")
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteResourcesBundle(@RequestBody List<DeleteResourceBundleDTO> resources) throws TechnicalException, BusinessException {
		for (DeleteResourceBundleDTO resource : resources) {
			
			if (resource.getLocale() == null || resource.getIdent() == null)
				throw new BusinessException("The Json must contain the parameters: locale, ident and system!");
			
			ResourceDTO resourceDTO = resourceService.findByLocaleAndIdentAndSystem(resource.getLocale(), resource.getIdent(), resource.getSystem());
			if (resourceDTO != null) {
				resourceService.delete(resourceDTO.getId());
				resourceService.deleteFromCache(resource);
			}
				
			else throw new BusinessException("The record: ident = " + resource.getIdent() 
			+ ", locale = " + resource.getLocale()+ ", system = " + resource.getSystem() + " does not exist!");
		}
	}

	@Override
	protected BaseService<ResourceDTO, Long> getService() {
		return resourceService;
	}

}