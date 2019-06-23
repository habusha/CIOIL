package gov.taxes.infra.github.exception;

import java.util.List;

/**
 * @Author Dan Erez
 * Exception with error code and parameters, to generate (translated) formatted error message according to the error code
 * which represents string message template with optional parameters which can be set in the message.
 */
public class FormattedRuntimeException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;
	
	String errorCode;
    List<String> parameters;

    public FormattedRuntimeException(Throwable cause, String errorCode, List<String> parameters) {
        super(cause);
        this.errorCode = errorCode;
        this.parameters = parameters;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
