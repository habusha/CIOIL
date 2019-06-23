package gov.taxes.infra.github.base;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseAbstractEntity {

	@Version
	private Long version;
	
	public abstract Long getId();

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
	
	@PrePersist
	protected void prePersist() {

	}

	@PreUpdate
	protected void preUpdate() {

	}

	@PreRemove
	protected void preDelete() {

	}

}
