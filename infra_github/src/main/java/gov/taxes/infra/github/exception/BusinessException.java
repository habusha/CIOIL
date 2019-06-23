package gov.taxes.infra.github.exception;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends Exception {
	
	private static final long serialVersionUID = -1485035514932904302L;
	private List<ValidationError> validationErrors;


	public BusinessException() {
		super();
	}
	
	public BusinessException(Throwable cause) {
		super(cause);
	}
	
	public BusinessException(String msg) {
		super(msg);
		this.validationErrors = new ArrayList<>();
	}
	
	/**
	 * @param msg
	 * @param data
	 */
	public BusinessException(String msg, List<ValidationError> validationErrors) {
		super(msg);
		this.validationErrors = validationErrors;
	}
	
	/**
	 * @param msg
	 * @param data
	 */
	public BusinessException(String msg, List<ValidationError> validationErrors, Throwable cause) {
		super(msg,cause);
		this.validationErrors = validationErrors;
	}

	public BusinessException(String msg, Throwable cause) {
		super(msg,cause);
	}

	public List<ValidationError> getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(List<ValidationError> validationErrors) {
		this.validationErrors = validationErrors;
	}
	
	@Override
	public String toString() {
		return super.toString() + ", validationErrors=" + (validationErrors != null ? validationErrors.toString() : "null");
	}


	
}
