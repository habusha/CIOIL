package gov.taxes.infra.github.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Dan Erez
 *
 * An holder for the entity manager, so we can create a new one for every request,
 * as the guideline demands.
 */
@Component
@Scope("prototype")
public class EMHolder {

	@PersistenceContext
	protected EntityManager entityManager;

	public EntityManager getEntityManager() {		
		return entityManager;
	}

}
