package gov.taxes.infra.github.base.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import gov.taxes.infra.github.annotation.InheritedComponent;
import gov.taxes.infra.github.annotation.InheritedController;
import gov.taxes.infra.github.base.BaseAbstractDTO;
import gov.taxes.infra.github.base.BaseFEController;
import gov.taxes.infra.github.base.BaseService;
import gov.taxes.infra.github.exception.BusinessException;
import gov.taxes.infra.github.exception.TechnicalException;
import gov.taxes.infra.github.mapping.TaxesMapper;

@InheritedController
@InheritedComponent
public abstract class BaseFEControllerImpl<I extends BaseAbstractDTO<Long>, O extends BaseAbstractDTO<Long>>
		implements BaseFEController<I, O> {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TaxesMapper mapper;

	protected abstract BaseService<I, Long> getService();

	@Override
	@PostMapping("/")
	public O save(@RequestBody I data) throws TechnicalException, BusinessException {
		I saved = getService().save(data);
		return populateFEDTO(saved, getOuputDtoClass());
	}

	@Override
	@PostMapping("/all")
	public List<O> save(List<I> data) throws TechnicalException, BusinessException {
		List<I> saved = getService().save(data);
		return populateFEDTOs(saved);
	}

	@Override
	@DeleteMapping("/")
	public void delete(@RequestParam Long id) throws BusinessException, TechnicalException {
		getService().delete(id);
	}

	@Override
	@GetMapping("/")
	public O findById(@RequestParam Long id) throws TechnicalException {
		I data = getService().findById(id);
		return populateFEDTO(data, getOuputDtoClass());
	}

	@Override
	@GetMapping("/all")
	public List<O> findAll() throws TechnicalException {
		List<I> data = getService().findAll();
		return populateFEDTOs(data);
	}

	@Override
	@GetMapping("/count")
	public long count() throws TechnicalException, BusinessException {
		return getService().count();
	}

	@Override
	@GetMapping("/isNameExist")
	public boolean isNameExist(String name) {
		return getService().isNameExist(name);
	}

	private O populateFEDTO(I saved, Class<O> clz) {
		return mapper.map(saved, clz);
	}

	private List<O> populateFEDTOs(List<I> saved) {
		List<O> fedtos = new ArrayList<O>();
		Class<O> clz = getOuputDtoClass();
		if (saved != null) {
			for (I dto : saved) {
				O fedto = populateFEDTO(dto, clz);
				fedtos.add(fedto);
			}
		}
		return fedtos;
	}

	@SuppressWarnings({ "unchecked" })
	private Class<O> getOuputDtoClass() {
		Class<O> clazz = null;
		Type[] genericTypes = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
		if (genericTypes[1] instanceof ParameterizedType) {
			clazz = (Class<O>) ((ParameterizedType) genericTypes[1]).getRawType();
		} else {
			clazz = (Class<O>) genericTypes[1];
		}
		return clazz;
	}
}
