
package gov.taxes.resourcebundle.dao;

import java.util.List;

import gov.taxes.infra.github.base.BaseDAO;
import gov.taxes.resourcebundle.dto.ResourceDTO;
import gov.taxes.resourcebundle.enums.LocaleEnum;

public interface ResourceDAO extends BaseDAO<ResourceDTO, Long> {

	ResourceDTO findByLocaleAndIdentAndSystem(LocaleEnum locale, String ident, String system);

	List<ResourceDTO> findBySystemAndLocale(String system, LocaleEnum locale);
}

