package gov.taxes.infra.github.utils;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.taxes.infra.github.persistence.EMHolder;

public class EMUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(EMUtils.class);
	
	public static EntityManager getEntityManager() {
		EMHolder emHolder = null;
		try {
			emHolder = (EMHolder) TaxesUtils.getBeanByName(EMHolder.class.getCanonicalName());
		} catch (Exception e) {
			logger.debug("EMUtils.getEntityManager() throw Technical Exception when trying to getBeanByName(EMHolder.class.getCanonicalName())", e);
		} 
		return emHolder.getEntityManager();
	}

}
