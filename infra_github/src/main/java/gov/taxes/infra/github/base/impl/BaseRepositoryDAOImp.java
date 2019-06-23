package gov.taxes.infra.github.base.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import gov.taxes.infra.github.annotation.InheritedComponent;
import gov.taxes.infra.github.base.BaseAbstractDTO;
import gov.taxes.infra.github.base.BaseAbstractEntity;
import gov.taxes.infra.github.base.BaseDAO;
import gov.taxes.infra.github.base.BaseRepository;
import gov.taxes.infra.github.exception.BusinessException;
import gov.taxes.infra.github.exception.FormattedRuntimeException;
import gov.taxes.infra.github.exception.TechnicalException;
import gov.taxes.infra.github.mapping.TaxesMapper;
import gov.taxes.infra.github.utils.EMUtils;
import gov.taxes.infra.github.utils.TaxesUtils;
import gov.taxes.infra.github.validation.TaxesValidation;

/**
 * @author Dan Erez
 *
 *         This is the base class for all DAO implementations with Long key.
 * 
 * @param <T> DTO type
 * @param <E> Entity Type
 */
@InheritedComponent
public abstract class BaseRepositoryDAOImp<T extends BaseAbstractDTO<Long>, E extends BaseAbstractEntity>
implements BaseDAO<T, Long> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected TaxesMapper mapper;

	@Autowired
	protected TaxesValidation validation;

	protected abstract BaseRepository<E, Long> getRepository();

	@Override
	public boolean isNameExist(String name) {
		try {
			Class<E> clazz = getGenericTypeClass();
			String className = clazz.getSimpleName();
			TypedQuery<Long> query = EMUtils.getEntityManager()
					.createQuery("SELECT id FROM " + className + " ent WHERE ent.name= :name", Long.class).setMaxResults(1)
					.setParameter("name", name);
			List<Long> ret = query.getResultList();
			return ret != null && !ret.isEmpty();
		} catch (Exception e) {
			FormattedRuntimeException fre = getFormattedRuntimeException(e, "ERROR_IS_NAME_EXIST", name);
			throw fre;
		}
	}

	@SuppressWarnings("unchecked")
	private Class<E> getGenericTypeClass() {
		Class<E> clazz = null;
		Type[] genericTypes = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
		if (genericTypes[1] instanceof ParameterizedType) {
			clazz = (Class<E>) ((ParameterizedType) genericTypes[1]).getRawType();
		} else {
			clazz = (Class<E>) genericTypes[1];
		}
		return clazz;
	}

	/**
	 * Retrieve Object by PK.
	 * 
	 * @param pk pk for retrieve
	 * @throws TechnicalException
	 */
	@Override
	public T retrieveByPK(Long pk) throws TechnicalException {
		try {
			T result = null;
			E entity = getRepository().getOne(pk);
			if (entity != null) {
				result = populateDTO(entity);
			}
			return result;
		} catch (Exception e) {
			FormattedRuntimeException fre = getFormattedRuntimeException(e, "ERROR_RETRIEVE_ENTITY", pk.toString());
			throw fre;
		}
	}

	@Override
	public List<T> retrieveByPKs(List<Long> ids) {
		try {
			List<T> result = null;
			List<E> entities = getRepository().findAllById(ids);
			if (entities != null) {
				result = new ArrayList<T>();
				for (E entity : entities) {
					result.add(populateDTO(entity));
				}
			}
			return result;
		} catch (Exception e) {
			FormattedRuntimeException fre = getFormattedRuntimeException(e, "ERROR_RETRIEVE_ENTITIES", "");
			throw fre;
		}
	}

	@Override
	public List<T> retrieveAll() throws TechnicalException {
		try {
			List<T> result = null;
			List<E> entities = getRepository().findAll();
			if (entities != null) {
				result = populateDTO(entities);
			}
			return result;
		} catch (Exception e) {
			FormattedRuntimeException fre = getFormattedRuntimeException(e, "ERROR_RETRIEVE_ENTITIES", "");
			throw fre;
		}
	}

	/**
	 * Update DTO.
	 * 
	 * @param data - DTO to be update.
	 * @throws TechnicalException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T update(T data) throws BusinessException, TechnicalException {
		try {
			T updatedData = null;

			E origEntity = getRepository().getOne(data.getId());

			T oldData = populateDTO(origEntity);

			E entityBeforUpdate = populateEntity(data);

			E newEntity = populateEntity(data);

			preUpdate(newEntity);

			newEntity = getRepository().save(newEntity);

			updateManagedEntityWithNulls(entityBeforUpdate, newEntity);

			postUpdate(newEntity, data);

			updatedData = populateDTO(newEntity);

			fireUpdateEvent(oldData, updatedData, updatedData.getClass(), TaxesUtils.getClassName(entityBeforUpdate));

			return updatedData;
		} catch (Exception e) {
			FormattedRuntimeException fre = getFormattedRuntimeException(e, "ERROR_UPDATE_ENTITY", data);
			throw fre;
		}
	}

	/**
	 * Updates a copy that was retrieved from the DB with all the values, as opposed
	 * to creating a new entity and populating it from the DTO.
	 * 
	 * @param data
	 * @return
	 * @throws BusinessException
	 * @throws TechnicalException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T updateDirect(T data) throws BusinessException, TechnicalException {
		try {
			T updatedData = null;

			E origEntity = getRepository().getOne(data.getId());

			T oldData = populateDTO(origEntity);

			E entityBeforUpdate = populateEntity(data);

			E newEntity = populateEntity(data);
			// Fields that were not null in origEntity and are currently null,
			// NEW TO THE FACT THEY DO NOT EXIST IN THE DTO, should receive their old value
			updateManagedEntityWithNotNulls(origEntity, newEntity, data);
			updateManagedEntityWithNotNulls(origEntity, entityBeforUpdate, data);

			preUpdate(newEntity);

			newEntity = getRepository().save(newEntity);

			updateManagedEntityWithNulls(entityBeforUpdate, newEntity);

			postUpdate(newEntity, data);

			updatedData = populateDTO(newEntity);

			fireUpdateEvent(oldData, updatedData, updatedData.getClass(), TaxesUtils.getClassName(entityBeforUpdate));

			return updatedData;
		} catch (Exception e) {
			FormattedRuntimeException fre = getFormattedRuntimeException(e, "ERROR_UPDATE_ENTITY", data);
			throw fre;
		}
	}

	/**
	 * create DTO.
	 * 
	 * @param data - DTO to be created.
	 *
	 * @throws TechnicalException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public T create(T data) throws BusinessException, TechnicalException {

		try {

			preCreate(data);

			E entity = populateEntity(data);

			T newData = create(entity);

			postCreate(newData);

			fireCreateEvent(newData, newData.getClass(), TaxesUtils.getClassName(entity));

			return newData;
		} catch (Exception e) {
			e.printStackTrace();
			FormattedRuntimeException fre = getFormattedRuntimeException(e, "ERROR_CREATE_ENTITY", data);
			throw fre;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public List<T> create(List<T> data) throws BusinessException, TechnicalException {
		List<T> ret = new ArrayList<>();
		for (T dto : data) {
			ret.add(create(dto));
		}
		return ret;
	}

	/**
	 * create Entity.
	 * 
	 * @param _entity - Entity to be created.
	 * @throws TechnicalException
	 * @throws BusinessException
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	protected T create(E _entity) throws BusinessException, TechnicalException {
		T newData = null;
		JpaRepository<E, Long> entityManagerIntf = getRepository();
		_entity = entityManagerIntf.save(_entity);
		newData = populateDTO(_entity);
		return newData;
	}

	/**
	 * delete DTO.
	 * 
	 * @param data - data to be created.
	 * @throws TechnicalException
	 * @throws BusinessException
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(T data) throws BusinessException, TechnicalException {
		try {
			E entity = getRepository().getOne(data.getId());
			T oldData = populateDTO(entity);
			preDelete(entity);
			getRepository().delete(entity);
			postDelete(entity);

			fireDeleteEvent(oldData, data, data.getClass(), TaxesUtils.getClassName(entity));
		} catch (Exception e) {
			FormattedRuntimeException fre = getFormattedRuntimeException(e, "ERROR_DELETE_ENTITY", data);
			throw fre;
		}
	}

	public void deleteAll() {
		getRepository().deleteAllInBatch();
	}

	@Override
	public void delete(Long pk) throws BusinessException, TechnicalException {
		try {
			E entity = getRepository().getOne(pk);
			preDelete(entity);
			getRepository().delete(entity);
			postDelete(entity);
		} catch (Exception e) {
			FormattedRuntimeException fre = getFormattedRuntimeException(e, "ERROR_DELETE_ENTITY", pk.toString());
			throw fre;
		}
	}

	@Override
	public long count() throws BusinessException, TechnicalException {
		return getRepository().count();
	}

	protected Collection<T> entityToDataMapping(Collection<E> entities) throws TechnicalException {
		Collection<T> dtos = new ArrayList<T>();
		for (E entity : entities) {
			dtos.add(populateDTO(entity));
		}

		return dtos;
	}

	protected Collection<E> dataToEntityMapping(Collection<T> dtos) throws TechnicalException {
		Collection<E> entities = new ArrayList<E>();
		for (T dto : dtos) {
			entities.add(populateEntity(dto));
		}

		return entities;
	}

	private void updateManagedEntityWithNotNulls(E entityBeforeUpdate, E _newEntity, T dto) {
		try {
			BeanInfo entityInfo = Introspector.getBeanInfo(entityBeforeUpdate.getClass());
			BeanInfo dtoInfo = Introspector.getBeanInfo(dto.getClass());
			for (PropertyDescriptor propDesc : entityInfo.getPropertyDescriptors()) {
				Method readMethod = propDesc.getReadMethod();
				Method writeMethod = propDesc.getWriteMethod();

				if (readMethod != null && writeMethod != null) {
					Object oldVal = readMethod.invoke(entityBeforeUpdate, new Object[] {});
					Object newVal = readMethod.invoke(_newEntity, new Object[] {});
					if (oldVal != null && newVal == null) {
						// If new val is null because this field is missing in the DTO, update old
						// value
						boolean found = false;
						for (PropertyDescriptor pd : dtoInfo.getPropertyDescriptors()) {
							if (pd.getName().equals(propDesc.getName())
									&& pd.getPropertyType().equals(propDesc.getPropertyType())) {
								found = true;
								break;
							}
						}

						if (!found) {
							writeMethod.invoke(_newEntity, oldVal);
						}
					}
				}
			}
		} catch (Exception ex) {
			StringBuilder message = new StringBuilder(
					"BaseDAOBean:updateManagedEntityWithNulls() Failed to Update Managed Entity With Null Value ");
			if (_newEntity != null) {
				message.append("Entity name: ").append(_newEntity.getClass().getCanonicalName());
				message.append(", data: ").append(_newEntity);
				message.append(", Id: ").append(_newEntity.getId());
			}
			throw new TechnicalException(message.toString(), ex);
		}
	}

	private void updateManagedEntityWithNulls(E entityBeforeUpdate, E _newEntity) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(entityBeforeUpdate.getClass());
			for (PropertyDescriptor propDesc : beanInfo.getPropertyDescriptors()) {
				Method readMethod = propDesc.getReadMethod();
				Method writeMethod = propDesc.getWriteMethod();

				if (readMethod != null && writeMethod != null) {
					Object obj = readMethod.invoke(entityBeforeUpdate, new Object[] {});
					if (obj == null) {
						// We will Insert Null To the New Entity
						writeMethod.invoke(_newEntity, obj);
					}
				}
			}
		} catch (Exception ex) {
			StringBuilder message = new StringBuilder(
					"BaseDAOBean:updateManagedEntityWithNulls() Failed to Update Managed Entity With Null Value ");
			if (_newEntity != null) {
				message.append("Entity name: ").append(_newEntity.getClass().getCanonicalName());
				message.append(", data: ").append(_newEntity);
				message.append(", Id: ").append(_newEntity.getId());
			}
			throw new TechnicalException(message.toString(), ex);
		}
	}

	protected E populateEntity(T _data) throws TechnicalException {
		E entity = null;
		Class<E> classEntity = getEntityClass();
		if (_data != null) {
			entity = mapper.map(_data, classEntity);
		}
		return entity;
	}

	protected E populateEntity(T _data, E _entity) throws TechnicalException {
		if (_data != null) {
			mapper.map(_data, _entity);
		}
		return _entity;
	}

	protected List<E> populateEntity(List<T> _data) throws TechnicalException {
		List<E> entities = new ArrayList<E>();

		if (_data != null) {
			for (T dto : _data) {
				E entity = populateEntity(dto);
				entities.add(entity);
			}
		}
		return entities;
	}

	@SuppressWarnings({ "unchecked" })
	protected Class<T> getDTOClass() {
		Class<T> clazz = null;
		Type[] genericTypes = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
		if (genericTypes[0] instanceof ParameterizedType) {
			clazz = (Class<T>) ((ParameterizedType) genericTypes[0]).getRawType();
		} else {
			clazz = (Class<T>) genericTypes[0];
		}
		return clazz;
	}

	@SuppressWarnings({ "unchecked" })
	protected Class<E> getEntityClass() {
		Class<E> clazz = null;
		Type[] genericTypes = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
		if (genericTypes[1] instanceof ParameterizedType) {
			clazz = (Class<E>) ((ParameterizedType) genericTypes[1]).getRawType();
		} else {
			clazz = (Class<E>) genericTypes[1];
		}
		return clazz;
	}

	protected T populateDTO(E _entity) throws TechnicalException {
		return populateOtherDTO(_entity, getDTOClass());
	}

	protected <K> K populateOtherDTO(E _entity, Class<K> dtoClassName) throws TechnicalException {
		K dto = null;
		if (_entity != null) {
			K map = mapper.map(_entity, dtoClassName);
			dto = map;
		}
		return dto;
	}

	protected T populateDTO(E _entity, T data) throws TechnicalException {

		if (_entity != null) {
			mapper.map(_entity, data);
		}
		return data;
	}

	protected <K> List<K> populateOtherDTO(List<E> _entities, Class<K> dtoClassName) throws TechnicalException {
		List<K> dtos = new ArrayList<K>();

		if (_entities != null) {
			for (E entity : _entities) {
				K dto = populateOtherDTO(entity, dtoClassName);
				dtos.add(dto);
			}
		}
		return dtos;
	}

	protected List<T> populateDTO(List<E> _entities) throws TechnicalException {
		return populateOtherDTO(_entities, getDTOClass());
	}

	protected void fireCreateEvent(T dto, Class<?> dtoClassName, String entityClassName) {
		preFireCreateEventEvent(dto);
	}

	protected void fireDeleteEvent(T dtoOld, T dto, Class<?> dtoClassName, String entityClassName) {

		preFireDeleteEvent(dtoOld, dto);
	}

	protected void fireUpdateEvent(T dtoOld, T dtoNew, Class<?> dtoClassName, String entityClassName) {
		preFireUpdateEvent(dtoOld, dtoNew);
	}

	protected void preFireUpdateEvent(T dtoOld, T dtoNew) {

	}

	protected void preFireDeleteEvent(T dtoOld, T dto) {

	}

	protected void preFireCreateEventEvent(T dto) {

	}

	protected void preDelete(E _entity) {
	}

	protected void postDelete(E _entity) {
	}

	protected void preUpdate(E _entity) {
	}

	protected void postUpdate(E _entity, T data) {
	}

	protected void preCreate(T _dto) {
	}

	protected void postCreate(T _dto) {
	}

	/*
	 * protected void updateDataWithAudit(T data) throws ValidationException,
	 * TechnicalException { if (data instanceof BaseDTO) { ((BaseDTO<P>)
	 * data).setAuditId(SMContext.getContext().getAuditId()); } }
	 * 
	 * protected void createAudit(T data) throws ValidationException,
	 * TechnicalException { if (data instanceof BaseDTO) { Long auditId =
	 * SMContext.getContext().getAuditId(); if (auditId == null) { auditId =
	 * audit.create(); SMContext.getContext().setAuditId(auditId); } } }
	 */

	private static final String ENTITY = "Entity";

	private String getEntitiyName(String persistentClassName) {
		if (persistentClassName != null && persistentClassName.endsWith(ENTITY)) {
			int lastDotIndex = persistentClassName.lastIndexOf('.');
			int entityIndex = persistentClassName.lastIndexOf(ENTITY);
			return persistentClassName.substring(lastDotIndex + 1, entityIndex);
		} else {
			return persistentClassName;
		}
	}

	private FormattedRuntimeException getFormattedRuntimeException(Exception cause, String errorCode, T data) {
		return getFormattedRuntimeException(cause, errorCode, data.getId() != null ? data.getId().toString() : null);
	}

	private FormattedRuntimeException getFormattedRuntimeException(Exception cause, String errorCode, String param) {
		String entityName = getEntitiyName(getGenericTypeClass().getName());
		List<String> parameters = new ArrayList<String>();
		parameters.add(entityName);
		if (param != null && param.length() > 0) {
			parameters.add(param);
		}
		return new FormattedRuntimeException(cause, errorCode, parameters);
	}

}
