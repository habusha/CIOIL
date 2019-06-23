package gov.taxes.resourcebundle.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import gov.taxes.infra.github.base.BaseService;
import gov.taxes.infra.github.base.impl.BaseFEControllerImpl;
import gov.taxes.infra.github.exception.BusinessException;
import gov.taxes.resourcebundle.dto.ResourceDTO;
import gov.taxes.resourcebundle.service.ResourceService;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * The class handles REST requests for resource bundle.
 * 
 * @author Eitan Nosraty
 * 
 */
@RequestMapping("/resource_bundle")
public class ResourceBundleController extends BaseFEControllerImpl<ResourceDTO, ResourceDTO> {

	@Autowired
	private ResourceService resourceService;

	@ApiOperation(value = "Gets resources by system and locales")
	@GetMapping("/allResourcesBySystemAndLocales")
	@ResponseBody
	public Map<String, Map<String, String>> getAllResourcesBySystemAndLocales(@RequestParam String system, 
			@RequestParam List<String> locales) throws JsonProcessingException {
		return resourceService.getAllResourcesBySystemAndLocales(system, locales);
	}

	@ApiOperation(value = "Gets resources by locales, idents and system")
	@GetMapping("/resourcesByLocalesAndIdentsAndSystem")
	@ResponseBody
	public Map<String, Map<String, String>> resourcesByLocalesAndIdentsAndSystem(@RequestParam List<String> locales,
			@RequestParam List<String> idents, @RequestParam String system) throws JsonProcessingException {
		return resourceService.findResourcesByLocalesAndIdentsAndSystem(locales, idents, system);
	}

	@ApiOperation(value = "Gets message by locale, ident and system")
	@GetMapping("/messageByLocaleAndIdentAndSystem")
	@ResponseBody
	public String messageByLocaleAndIdentAndSystem(@RequestParam String locale,
			@RequestParam String ident, @RequestParam String system) throws JsonProcessingException, BusinessException {
		return resourceService.findMessageByLocaleAndIdentAndSystem(locale, ident, system);
	}

	@Override
	protected BaseService<ResourceDTO, Long> getService() {
		return resourceService;
	}

}