package gov.taxes.infra.github.base;

import java.util.List;

import gov.taxes.infra.github.context.ValidationContext;
import gov.taxes.infra.github.exception.BusinessException;
import gov.taxes.infra.github.exception.TechnicalException;

/**
 * @author Dan Erez
 *
 *         Interface for all business logic classes.
 * 
 * @param <T>
 *            - Handled DTO type
 * @param <P>
 *            - Key type
 */
public interface BaseService<T extends BaseAbstractDTO<Long>, Long> {

	public void validate(T data, ValidationContext<Long> context) throws BusinessException, TechnicalException;

	public T update(T data) throws BusinessException, TechnicalException;

	public T updateDirect(T data) throws BusinessException, TechnicalException;

	public T create(T data) throws BusinessException, TechnicalException;

	public T save(T data) throws BusinessException, TechnicalException;

	public List<T> save(List<T> data) throws BusinessException, TechnicalException;

	public void delete(T data) throws BusinessException, TechnicalException;

	public void deleteAll() throws BusinessException, TechnicalException;
	
	public void delete(Long id) throws BusinessException, TechnicalException;

	public T findById(Long id) throws TechnicalException;

	public List<T> findAll() throws TechnicalException;

	public List<T> findbyIds(List<Long> ids) throws TechnicalException;

	public List<T> update(List<T> dto) throws BusinessException, TechnicalException;

	public List<T> create(List<T> dto) throws BusinessException, TechnicalException;

	public long count() throws TechnicalException, BusinessException;

	public boolean isNameExist(String name);
}