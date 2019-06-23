package gov.taxes.infra.github.exception;

public class TechnicalException  extends RuntimeException {

	private static final long serialVersionUID = 9191005035701334351L;

	public TechnicalException() {
		super();
	}
	
	public TechnicalException(String message, Throwable cause) {
		super(message, cause);
	}

	public TechnicalException(String message) {
		super(message);
	}

	public TechnicalException(Throwable cause) {
		super(cause);
	}

	
}
