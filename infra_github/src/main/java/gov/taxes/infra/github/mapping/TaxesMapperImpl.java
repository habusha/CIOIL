package gov.taxes.infra.github.mapping;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapper;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.DozerEventListener;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import gov.taxes.infra.github.context.ApplicationContextProvider;
import gov.taxes.infra.github.context.TaxesContext;
import gov.taxes.infra.github.exception.MappingException;
import gov.taxes.infra.github.exception.TechnicalException;
import gov.taxes.infra.github.utils.TaxesUtils;

@Component
public class TaxesMapperImpl implements TaxesMapper, ApplicationContextAware  {

	/**
	 * logging member
	 */
	private static final Logger logger = LoggerFactory.getLogger(TaxesMapperImpl.class);


	/**
	 * Hold DozerBeanMapper which is operate all the mapping operation
	 */
	private Mapper mapper;
	
	private List<String> mappingFiles;

	private List<CustomConverter> customConverters;

	
	public static String APP_NAME;

	@Value("${spring.application.name}")
	public void setAppName(String appName) {
		APP_NAME = appName;
	}

	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		TaxesContext.getContext();//for init APP_NAME
		ApplicationContextProvider.ctx = applicationContext;
		init(applicationContext);
	}

	/**
	 * This method initialized all mapping files in the time the container is
	 * created.
	 * 
	 * @throws IOException	
	 */
	@SuppressWarnings("unused")
	private void init(ApplicationContext applicationContext) {

		List<String> mappingFiles = new ArrayList<String>();
		String xmlStr = null;

		try {
			// mapper =
			// DozerBeanMapperBuilder.create().withMappingBuilder(myMappingBuilder).withMappingFiles(myFiles)
			// .build();
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			Enumeration<URL> urlEnum = cl.getResources("dozerBeanMapping.xml");
			List<String> urls = new ArrayList<>();
			if (urlEnum != null) {
				while (urlEnum.hasMoreElements()) {
					urls.add(urlEnum.nextElement().toString());
				}
			}
			mapper = DozerBeanMapperBuilder.create().withMappingFiles(urls.toArray(new String[urls.size()])).build();
			populateCustomConverters();
			
			// Add Listeners event to the mapper instance. List<java.lang.Class<?>>
			List<java.lang.Class<?>> listenerClasses = TaxesUtils.getImplsForInterface(DozerEventListener.class);
			List<DozerEventListener> listeners = new ArrayList<DozerEventListener>();
			if (listenerClasses != null) {
				for (java.lang.Class<?> listenerClass : listenerClasses) {
					listeners.add((DozerEventListener) listenerClass.newInstance());
				}
				listeners.add(VersionMapperListener.class.newInstance());
				((DozerBeanMapper) mapper).setEventListeners(listeners);
			}

			logger.info("Dozer Mapping Loaded!");
		} catch (TechnicalException e) {
			logger.error("TaxesMapperBean:init() throw Excpetion on Server Startup", e);
			throw e;
		} catch (Exception e) {
			throw new TechnicalException("TaxesMapperBean:init() throw Excpetion on Server Startup", e);
		}

	}

	
	/**
	 * Mapping sourceObject to a new object which created from targetClass (delegate
	 * to dozer mapper)
	 * 
	 * @param sourceObject
	 *            source object for mapping
	 * @param targetClass
	 *            target class for mapping
	 * @return A mapped object, The return object is type of targetClass
	 * @throws MappingException
	 */
	@Override
	public <T> T map(Object _sourceObj, java.lang.Class<T> _targetClass) throws MappingException {
		T result = null;
		try {
			result = mapper.map(_sourceObj, _targetClass);
		} catch (org.dozer.MappingException dme) {
			StringBuilder message = new StringBuilder("Failed to map object to class: ");
			message.append("Source object=").append(getObjectClassName(_sourceObj));
			message.append("Target Class=").append(getClassName(_targetClass));
			throw new MappingException(message.toString(), dme);
		}
		return result;
	}

	/**
	 * Mapping sourceObject to targetObject
	 * 
	 * @param sourceObject
	 *            source object for mapping
	 * @param targetObject
	 *            target object for mapping
	 * @throws MappingException
	 */
	@Override
	public void map(Object _sourceObj, Object _targetObj) throws MappingException {
		try {
			mapper.map(_sourceObj, _targetObj);
		} catch (org.dozer.MappingException dme) {
			StringBuilder message = new StringBuilder("Failed to map object to object: ");
			message.append("Source object=").append(getObjectClassName(_sourceObj));
			message.append("Target object=").append(getObjectClassName(_targetObj));
			throw new MappingException(message.toString(), dme);
		}
	}
	
	/**
	 * Collect all custom converter and load it into the mapper object.
	 * 
	 * @throws TechnicalException
	 */
	private void populateCustomConverters() throws TechnicalException {
		customConverters = new ArrayList<CustomConverter>();
		try {
			List<java.lang.Class<?>> classes = TaxesUtils.getImplsForInterface(CustomConverter.class);
			for (java.lang.Class<?> clazz : classes) {
				if (CustomConverter.class.isAssignableFrom(clazz)) {
					CustomConverter currInstance;
					currInstance = (CustomConverter) clazz.newInstance();
					customConverters.add(currInstance);
				}
			}
			((DozerBeanMapper) mapper).setCustomConverters(customConverters);
		} catch (Exception e) {
			String message = "TaxesMapperBean.populateCustomConverters() - failed to collect all custom converters.";
			logger.error(message, e);
			throw new TechnicalException(message, e);
		}
	}

	
	private String getObjectClassName(Object obj) {
		String result = null;
		if (obj != null) {
			result = obj.getClass().getCanonicalName();
		}
		return result;
	}

	private String getClassName(@SuppressWarnings("rawtypes") java.lang.Class clazz) {
		String result = null;
		if (clazz != null) {
			result = clazz.getCanonicalName();
		}
		return result;
	}


}
