package gov.taxes.infra.github.base;

import java.util.List;

import gov.taxes.infra.github.exception.BusinessException;
import gov.taxes.infra.github.exception.TechnicalException;

/**
 * @author Dan Erez
 *
 *         Interface for all dao classes.
 * 
 * @param <T>
 *            - Handled DTO type
 * @param <P>
 *            - Key type
 */
public interface BaseDAO<T extends BaseAbstractDTO<P>, P> {

	public T update(T data) throws BusinessException, TechnicalException;

	/**
	 * Updates a copy that was retrieved from the DB with all the values, as opposed
	 * to creating a new entity and populating it from the DTO.
	 * 
	 * @param data
	 * @return
	 * @throws BusinessException
	 * @throws TechnicalException
	 */
	public T updateDirect(T data) throws BusinessException, TechnicalException;

	public T create(T data) throws BusinessException, TechnicalException;
	
	public List<T> create(List<T> data) throws BusinessException, TechnicalException;

	public void delete(T data) throws BusinessException, TechnicalException;

	public void delete(P pk) throws BusinessException, TechnicalException;

	public long count() throws BusinessException, TechnicalException;

	public T retrieveByPK(P pk) throws TechnicalException;

	public List<T> retrieveByPKs(List<P> ids) throws TechnicalException;

	public List<T> retrieveAll() throws TechnicalException;
	
	public boolean isNameExist(String name);

	public void deleteAll();

}
