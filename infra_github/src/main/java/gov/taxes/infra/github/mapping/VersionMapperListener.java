package gov.taxes.infra.github.mapping;

import org.dozer.DozerEventListener;
import org.dozer.event.DozerEvent;

import gov.taxes.infra.github.context.TaxesVersionMapperContext;

public class VersionMapperListener implements DozerEventListener {

	@Override
	public void mappingStarted(DozerEvent _event) {
	}

	@Override
	public void mappingFinished(DozerEvent _event) {
	}

	@Override
	public void preWritingDestinationValue(DozerEvent _event) {
	}

	@Override
	public void postWritingDestinationValue(DozerEvent _event) {
		TaxesVersionMapperContext.getContext().getObjectMap().put(_event.getSourceObject(), _event.getDestinationObject());
	}
}
