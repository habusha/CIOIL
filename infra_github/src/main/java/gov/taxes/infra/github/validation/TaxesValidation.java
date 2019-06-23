package gov.taxes.infra.github.validation;

import gov.taxes.infra.github.base.BaseAbstractDTO;
import gov.taxes.infra.github.context.ValidationContext;
import gov.taxes.infra.github.exception.BusinessException;
import gov.taxes.infra.github.exception.TechnicalException;

/**
 * @author Dan Erez
 *
 * Defines DTOs validation lifecycle
 */
public interface TaxesValidation {

	public void validate(BaseAbstractDTO<Long> dto, ValidationContext<?> context) throws BusinessException, TechnicalException;
	
}
