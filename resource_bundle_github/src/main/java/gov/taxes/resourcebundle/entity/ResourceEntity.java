package gov.taxes.resourcebundle.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import gov.taxes.infra.github.base.BaseEntity;
import gov.taxes.resourcebundle.enums.LocaleEnum;
import lombok.Data;

@Entity
@Table(name = "resources", uniqueConstraints = {@UniqueConstraint(name = "uc_ident_locale_system", columnNames = {"ident", "locale", "system_name"})})
@Data
public class ResourceEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "ident", nullable = false, length = 32)
	private String ident;
	
	@Column(name = "locale", nullable = false, length = 8)
	@Enumerated(EnumType.STRING)
	private LocaleEnum locale;
	
	@Column(name = "system_name", nullable = false, length = 16)
	private String system;
	
	private String message;
}
