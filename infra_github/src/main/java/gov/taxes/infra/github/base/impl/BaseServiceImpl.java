package gov.taxes.infra.github.base.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.taxes.infra.github.annotation.InheritedComponent;
import gov.taxes.infra.github.base.BaseAbstractDTO;
import gov.taxes.infra.github.base.BaseDAO;
import gov.taxes.infra.github.base.BaseService;
import gov.taxes.infra.github.context.ValidationContext;
import gov.taxes.infra.github.enums.OperationalEnum;
import gov.taxes.infra.github.exception.BusinessException;
import gov.taxes.infra.github.exception.TechnicalException;
import gov.taxes.infra.github.validation.TaxesValidation;

/**
 * @author Dan Erez
 *
 *         This is the base class for all BL implementations.
 * 
 * @param <T>
 *            DTO type
 * @param <P>
 *            Key Type
 */
@InheritedComponent
public abstract class BaseServiceImpl<T extends BaseAbstractDTO<P>, P> implements BaseService<T, P> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TaxesValidation validation;

	protected abstract BaseDAO<T, P> getDAO();

	@Override
	public boolean isNameExist(String name) {
		return getDAO().isNameExist(name);
	}	

	@SuppressWarnings("unchecked")
	@Override
	public void validate(T data, ValidationContext<P> context) throws BusinessException, TechnicalException {
		T dto = preValidate(data, context);
		validation.validate((BaseAbstractDTO<Long>) dto, context);
	}

	protected T preValidate(T data, ValidationContext<P> context) throws BusinessException, TechnicalException {
		return data;
	}

	@Override
	public T update(T data) throws BusinessException, TechnicalException {
		T result = null;
		BaseDAO<T, P> dao = getDAO();
		if (dao != null) {
			ValidationContext<P> context = createValidationContext(data, OperationalEnum.UPDATE);
			validate(data, context);
			preUpdate(data);
			result = dao.update(data);
			postUpdate(result);
		}
		return result;
	}

	@Override
	public T updateDirect(T data) throws BusinessException, TechnicalException {
		T result = null;
		BaseDAO<T, P> dao = getDAO();
		if (dao != null) {
			ValidationContext<P> context = createValidationContext(data, OperationalEnum.UPDATE);
			validate(data, context);
			preUpdate(data);
			result = dao.updateDirect(data);
			postUpdate(result);
		}
		return result;
	}

	public long count() throws TechnicalException, BusinessException {
		return getDAO().count();
	}

	@Override
	public T create(T data) throws BusinessException, TechnicalException {
		T result = null;
		BaseDAO<T, P> dao = getDAO();
		if (dao != null) {
			ValidationContext<P> context = createValidationContext(data, OperationalEnum.CREATE);
			validate(data, context);
			preCreate(data);
			result = dao.create(data);
			postCreate(result);
		}
		return result;
	}

	@Override
	public void delete(T data) throws BusinessException, TechnicalException {
		BaseDAO<T, P> dao = getDAO();
		if (dao != null) {
			ValidationContext<P> context = createValidationContext(data, OperationalEnum.DELETE);
			validate(data, context);
			preDelete(data);
			dao.delete(data);
		}
	}
	
	@Override
	public void deleteAll() throws BusinessException, TechnicalException {
		getDAO().deleteAll();
	}
	

	@Override
	public T findById(P pk) throws TechnicalException {
		T result = null;
		BaseDAO<T, P> dao = getDAO();
		if (dao != null) {
			result = dao.retrieveByPK(pk);
		}
		return result;
	}

	/**
	 * @param ids
	 *            - List of PKs for retrieving dtos.
	 * @throws TechnicalException
	 * @return List<T>
	 */
	@Override
	public List<T> findbyIds(List<P> ids) throws TechnicalException {
		List<T> result = new ArrayList<T>();
		BaseDAO<T, P> dao = getDAO();
		if (dao != null) {
			result = dao.retrieveByPKs(ids);
		}
		return result;
	}

	public List<T> findAll() throws TechnicalException {
		List<T> result = new ArrayList<T>();
		BaseDAO<T, P> dao = getDAO();
		if (dao != null) {
			result = dao.retrieveAll();
		}
		return result;
	}

	/**
	 * Update bulk of DTO's stored inside the bulkContext.
	 * 
	 * @param _entity
	 *            - Entity to be created.
	 * @throws TechnicalException
	 * @return List<T>
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED )
	public List<T> update(List<T> dtos) throws BusinessException, TechnicalException {

		List<T> result = new ArrayList<T>();
		if (dtos != null && !dtos.isEmpty()) {
			for (T currDTO : dtos) {
				T currResult = update(currDTO);
				result.add(currResult);
			}
		}
		return result;
	}

	/**
	 * Create bulk of DTO's stored inside the bulkContext.
	 * 
	 * @param _entity
	 *            - Entity to be created.
	 * @throws TechnicalException
	 * @throws BusinessException
	 * @return List<T>
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED )
	public List<T> create(List<T> dtos) throws BusinessException, TechnicalException {

		List<T> result = new ArrayList<T>();
		if (dtos != null && !dtos.isEmpty()) {
			for (T currDTO : dtos) {
				T currResult = create(currDTO);
				result.add(currResult);
			}
		}
		return result;
	}

	@Override
	public T save(T data) throws BusinessException, TechnicalException {
		if (data.getId() != null) {
			return update(data);
		} else {
			return create(data);
		}
	}

	@Override
	public List<T> save(List<T> dtos) throws BusinessException, TechnicalException {
		List<T> result = new ArrayList<T>();
		if (dtos != null && !dtos.isEmpty()) {
			for (T currDTO : dtos) {
				T currResult = save(currDTO);
				result.add(currResult);
			}
		}
		return result;
	}

	@Override
	public void delete(P pk) throws BusinessException, TechnicalException {
		BaseDAO<T, P> dao = getDAO();
		if (dao != null) {
			T data = findById(pk);
			ValidationContext<P> context = createValidationContext(data, OperationalEnum.DELETE);
			validate(data, context);
			preDelete(data);
			dao.delete(data);
		}
	}

	/**
	 * Delete bulk of DTO's stored inside the bulkContext.
	 * 
	 * @param _entity
	 *            - Entity to be created.
	 * @throws TechnicalException
	 * @throws BusinessException
	 * @return void
	 */
	@Transactional(propagation = Propagation.REQUIRED )
	public void delete(List<T> dtos) throws BusinessException, TechnicalException {

		if (dtos != null && !dtos.isEmpty()) {
			for (T currDTO : dtos) {
				delete(currDTO);
			}
		}
	}

	public ValidationContext<P> createValidationContext(T data, OperationalEnum operation) {
		ValidationContext<P> context = new ValidationContext<P>();
		context.setOperatinalType(operation);
		context.setCollectAll(true);
		context.setClassType(data.getClass().getCanonicalName());
		context.setDtoId(data.getId());
		return context;
	}

	/**
	 * preDelete as hook for the Application
	 * 
	 * @param dto
	 */
	protected void preDelete(T dto) throws BusinessException {
	}

	/**
	 * preUpdate as hook for the Application
	 * 
	 * @param dto
	 */
	protected void preUpdate(T dto) throws BusinessException {
	}

	/**
	 * postUpdate as hook for the Application
	 * 
	 * @param dto
	 */
	protected void postUpdate(T dto) throws BusinessException {
	}

	/**
	 * preCreate as hook for the Application
	 * 
	 * @param dto
	 */
	protected void preCreate(T dto) throws BusinessException {
	}

	/**
	 * postCreate as hook for the Application
	 * 
	 * @param dto
	 */
	protected void postCreate(T dto) throws BusinessException {
	}

}
