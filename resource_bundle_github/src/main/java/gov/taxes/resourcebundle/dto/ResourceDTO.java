package gov.taxes.resourcebundle.dto;

import gov.taxes.infra.github.base.BaseDTO;
import gov.taxes.resourcebundle.enums.LocaleEnum;
import lombok.Data;

/**
 * This class holds resource bundle information for the resource bundle REST services.
 * 
 * @author Eitan Nosraty
 */

@Data
public class ResourceDTO extends BaseDTO {


	private static final long serialVersionUID = 1L;
	
	private String ident;
	private LocaleEnum locale;
	private String message;
	private String system;

	public ResourceDTO() {
		super();
	}
	
	public ResourceDTO(String ident, LocaleEnum locale, String message, String system) {
		super();
		this.ident = ident;
		this.locale = locale;
		this.message = message;
		this.system = system;
	}

}
