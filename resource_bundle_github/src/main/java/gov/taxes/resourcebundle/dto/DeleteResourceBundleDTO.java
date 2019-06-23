package gov.taxes.resourcebundle.dto;

import gov.taxes.resourcebundle.enums.LocaleEnum;
import lombok.Data;

@Data
public class DeleteResourceBundleDTO {
	
	private String ident;
	private LocaleEnum locale;
	private String system;
	
	public DeleteResourceBundleDTO() {
		super();
	}
	
	public DeleteResourceBundleDTO(String ident, LocaleEnum locale, String system) {
		super();
		this.ident = ident;
		this.setLocale(locale);
		this.system = system;
	}
	
}
