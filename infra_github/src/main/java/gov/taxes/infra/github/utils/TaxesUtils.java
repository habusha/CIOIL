package gov.taxes.infra.github.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import gov.taxes.infra.github.context.ApplicationContextProvider;
import gov.taxes.infra.github.enums.DocType;

public class TaxesUtils {

	public static final String TAXES_PACKAGE = "gov.taxes";

	public static Object getBeanByName(String className) {
		try {
			Object o = ApplicationContextProvider.getBeanByClass(Class.forName(className));
			return o;
		} catch (Exception e) {
			Object o = ApplicationContextProvider.getBeanByName(className);
			return o;
		}
	}

	public static Object getBeanByName(ApplicationContext applicationContext, String className) {
		try {
			Object o = applicationContext.getBean(Class.forName(className));
			return o;
		} catch (Exception e) {
			int idx = className.lastIndexOf('.');
			if (idx < 0) {
				return className;
			}
			String beanName = className.substring(idx + 1);
			beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
			return applicationContext.getBean(beanName);
		}
	}

	/**
	 * Get class name as string
	 * 
	 * @param object
	 * @return
	 */
	public static String getClassName(Object object) {
		return object.getClass().getName().toString();
	}

	/**
	 * @param clz
	 * @return
	 */
	public static List<Class<?>> getImplsForInterface(Class<?> clz) {
		List<Class<?>> classes = new ArrayList<Class<?>>();

		String[] beanNames = ApplicationContextProvider.getApplicationContext().getBeanNamesForType(clz);
		for (String beanName : beanNames) {
			classes.add(ApplicationContextProvider.resolveBeanClassByName(beanName));
		}
		return classes;
	}

	public static List<Class<?>> getImplsForInterface(ApplicationContext applicationContext, Class<?> clz) {
		List<Class<?>> classes = new ArrayList<Class<?>>();

		String[] beanNames = applicationContext.getBeanNamesForType(clz);
		for (String beanName : beanNames) {
			classes.add(applicationContext.getType(beanName));
		}
		return classes;
	}

	public static DocType getDocType(String origFileName) {
		String suffix = getSuffix(origFileName);
		switch (suffix.toLowerCase()) {
		case "pdf":
			return DocType.PDF;
		case "xlsx": case "xls":
			return DocType.EXCEL;
		case "docx":
		case "doc":
			return DocType.WORD;
		case "csv": 
			return DocType.CSV;
		case "png": 
			return DocType.PNG;
		case "tiff": 
			return DocType.TIFF;
		case "jpeg": case "jpg":
			return DocType.JPEG;
		}
		return DocType.OTHER;
	}

	public static String getSuffix(String origFileName) {
		int idx = origFileName.lastIndexOf('.');
		if (idx < 0) {
			return ".tmp";
		}
		return origFileName.substring(idx + 1);
	}
	
	 public static String getHostname() {
			String hostname = null;
			try {
				hostname = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				//logger.error("Cannot get host info", e);
			}
			return hostname;
		}

}
