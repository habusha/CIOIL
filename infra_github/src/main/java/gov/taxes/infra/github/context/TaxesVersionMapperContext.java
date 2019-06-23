package gov.taxes.infra.github.context;

import java.util.HashMap;
import java.util.Map;

public class TaxesVersionMapperContext {

	private static final ThreadLocal<TaxesVersionMapperContext> threadLocal = new ThreadLocal<>();

	private Map<Object, Object> objectMap;

	private TaxesVersionMapperContext() {
		objectMap = new HashMap<>();
	}

	public static TaxesVersionMapperContext getContext() {
		TaxesVersionMapperContext result = null;
		result = threadLocal.get();
		if (result == null) {
			result = new TaxesVersionMapperContext();
			threadLocal.set(result);
		}
		return result;
	}

	public Map<Object, Object> getObjectMap() {
		return objectMap;
	}

	public void clean() {
		this.objectMap.clear();
	}

}
