package gov.taxes.resourcebundle.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import gov.taxes.infra.github.base.BaseRepository;
import gov.taxes.resourcebundle.entity.ResourceEntity;
import gov.taxes.resourcebundle.enums.LocaleEnum;

@Repository
public interface ResourceRepository extends BaseRepository<ResourceEntity, Long> {
	
	ResourceEntity findByLocaleAndIdentAndSystem(LocaleEnum locale, String ident, String system);
	List<ResourceEntity> findBySystemAndLocale(String system, LocaleEnum locale);
}


