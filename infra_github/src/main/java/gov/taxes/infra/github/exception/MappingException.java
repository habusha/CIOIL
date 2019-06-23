package gov.taxes.infra.github.exception;


/**
 * @author Dan Erez
 *
 * Signals that Exception was thrown during mapping operation. 
 * The exception that thrown can be found in the cause member. 
 */
public class MappingException extends TechnicalException {

	private static final long serialVersionUID = 9102017549929638086L;


	/**
	 * Constructs a MappingException with a string describing
	 * @param s   String describing the exception.
	 */
    public MappingException(String s) {
    	super(s);
    }    	


	/**
	 * Constructs a MappingException with a string describing
	 * and the exception causing the abort.
	 * @param s   String describing the exception.
	 * @param cause  Exception causing the abort.
	 */
    public MappingException(String s, Throwable cause) {  
		super(s,cause);

    }


	/**
	 * Constructs a MappingException with the exception causing the abort.
	 * @param cause  Exception causing the abort.
	 */
    public MappingException(Throwable cause) {  
		super(cause);
    }
    
}
