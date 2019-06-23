package gov.taxes.infra.github.mapping;

import gov.taxes.infra.github.exception.MappingException;


/**
 * 
 * @author Dan Erez
 *
 *         Interface for object mapping operations.
 */
public interface TaxesMapper {

	/**
	 * Mapping sourceObject to a new object which created from targetClass
	 * 
	 * @param _sourceObject
	 *            source object for mapping
	 * @param _targetClass
	 *            target class for mapping
	 * @return A mapped object, The return object is type of targetClass
	 * @throws MappingException
	 */
	public <T> T map(Object _sourceObject, Class<T> _targetClass) throws MappingException;
	

	/**
	 * Mapping sourceObject to targetObject
	 * 
	 * @param _sourceObject
	 *            source object for mapping
	 * @param _targetObject
	 *            target object for mapping
	 * @throws MappingException
	 */
	public void map(Object _sourceObject, Object _targetObject) throws MappingException;

	
}