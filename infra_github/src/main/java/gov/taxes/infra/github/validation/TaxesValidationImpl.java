package gov.taxes.infra.github.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import gov.taxes.infra.github.base.BaseAbstractDTO;
import gov.taxes.infra.github.context.ValidationContext;
import gov.taxes.infra.github.exception.BusinessException;
import gov.taxes.infra.github.exception.TechnicalException;

/**
 * @author Dan Erez
 *
 * This class implements the DTO validation life cycle
 */
@Component
public class TaxesValidationImpl implements TaxesValidation {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void validate(BaseAbstractDTO<Long> dto, ValidationContext<?> context) throws BusinessException, TechnicalException {
		// Todo: apply validations, according to annotations		
	}

}
