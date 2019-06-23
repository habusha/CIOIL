package gov.taxes.infra.github.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import gov.taxes.infra.github.exception.TechnicalException;

/**
 * 
 * @author Dan Erez
 *
 *         ApplicationContextAware to hold the context and provide some simple
 *         services.
 * 
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

	public static ApplicationContext ctx = null;

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		ApplicationContextProvider.ctx = ctx;
	}

	public static Class<?> resolveBeanClassByName(String beanName) {
		return ctx.getType(beanName);
	}

	public static Object getBeanByClass(Class<?> clazz) {
		try {
			return ctx.getBean(clazz);
		} catch (Exception e) {
			throw new TechnicalException("Canot resolved " + clazz.getName(), e);
		}
	}

	public static Object getBeanByName(String className) {
		String beanName = getBeanNameByClassName(className);
		return ctx.getBean(beanName);
	}

	private static String getBeanNameByClassName(String className) {
		int idx = className.lastIndexOf('.');
		if (idx < 0) {
			return className;
		}
		String beanName = className.substring(idx + 1);
		beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
		return beanName;
	}

	public static void publishEvent(ApplicationEvent event) {
		((ApplicationEventPublisher) ctx).publishEvent(event);
	}
}