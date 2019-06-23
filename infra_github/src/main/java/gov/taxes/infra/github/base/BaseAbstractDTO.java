package gov.taxes.infra.github.base;

import java.io.Serializable;

/**
 * @author Dan Erez
 *
 *         Base class for all DTOs.
 * 
 * @param <P> Key type
 */
public abstract class BaseAbstractDTO<P> implements Serializable {

	private static final long serialVersionUID = -1705309340267574511L;

	private Long version;
		
	private P id;

	public P getId() {
		return id;
	}

	public void setId(P id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseAbstractDTO other = (BaseAbstractDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}