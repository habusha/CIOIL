package gov.taxes.resourcebundle.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import gov.taxes.infra.github.base.BaseRepository;
import gov.taxes.infra.github.base.impl.BaseRepositoryDAOImp;
import gov.taxes.resourcebundle.dao.ResourceDAO;
import gov.taxes.resourcebundle.dto.ResourceDTO;
import gov.taxes.resourcebundle.entity.ResourceEntity;
import gov.taxes.resourcebundle.enums.LocaleEnum;
import gov.taxes.resourcebundle.repository.ResourceRepository;

public class ResourceDAOImpl extends BaseRepositoryDAOImp<ResourceDTO, ResourceEntity> implements ResourceDAO {

	@Autowired
	ResourceRepository resourceRepository;
	
	@Override
	protected BaseRepository<ResourceEntity, Long> getRepository() {
		return resourceRepository;
	}

	@Override
	public ResourceDTO findByLocaleAndIdentAndSystem(LocaleEnum locale, String ident, String system) {
		return populateDTO(resourceRepository.findByLocaleAndIdentAndSystem(locale, ident, system));
	}

	@Override
	public List<ResourceDTO> findBySystemAndLocale(String system, LocaleEnum locale) {
		return populateDTO(resourceRepository.findBySystemAndLocale(system, locale));
	}

}
