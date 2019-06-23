package gov.taxes.infra.github.context;

import gov.taxes.infra.github.enums.OperationalEnum;

/**
 * @author Dan Erez
 *
 * This class holds the context for a validation.
 *  
 * @param <P> Id of the validated DTO
 */
public class ValidationContext<P> {
	
	private OperationalEnum operatinalType;
	
	private boolean collectAll;
	private String classType;
	private P dtoId;

	public OperationalEnum getOperatinalType() {
		return operatinalType;
	}

	public void setOperatinalType(OperationalEnum operatinalType) {
		this.operatinalType = operatinalType;
	}

	public boolean isCollectAll() {
		return collectAll;
	}

	public void setCollectAll(boolean collectAll) {
		this.collectAll = collectAll;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public P getDtoId() {
		return dtoId;
	}

	public void setDtoId(P dtoId) {
		this.dtoId = dtoId;
	}
	

}
