package gov.taxes.infra.github.base;

import java.util.List;

import gov.taxes.infra.github.exception.BusinessException;
import gov.taxes.infra.github.exception.TechnicalException;

public interface BaseFEController<I extends BaseAbstractDTO<Long>, O extends BaseAbstractDTO<Long>> {
	
	public O save(I data) throws TechnicalException, BusinessException;

	public List<O> save(List<I> data) throws TechnicalException, BusinessException;

	public void delete(Long id) throws BusinessException, TechnicalException;

	public O findById(Long id) throws TechnicalException;

	public List<O> findAll() throws TechnicalException;
	
	public long count() throws TechnicalException, BusinessException;

	public boolean isNameExist(String name);
}
