package util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.BeanUtils;

public final class ObjectUtil {

	private ObjectUtil() { }
	
	@SuppressWarnings("rawtypes")
	public static final boolean isPreenchido(Object object) {
		if (object == null
				|| (object instanceof Number && ((Number) object).doubleValue() == 0)
				|| (object instanceof String && ((String) object).isEmpty())
				|| (object instanceof Collection && ((Collection) object).isEmpty())
				|| (object instanceof Map && ((Map) object).isEmpty())
				|| (object.getClass().isArray() && Array.getLength(object) == 0)) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void copiarPropriedades(Object origem, Object destino) throws IllegalAccessException, InvocationTargetException {
		if (origem == null || destino == null) {
			return;
		}
		
		BeanUtils.copyProperties(destino, origem);
	}
	
	public static <T> T criarAPartirDe(Object origem, Class<T> clazz) throws InvocationTargetException {
		if (origem == null) {
			return null;
		}
		
		try {
			T destino = clazz.newInstance();
			BeanUtils.copyProperties(destino, origem);
			return destino;
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static boolean notEquals(Object obj1, Object obj2) {
		return !equals(obj1, obj2);
	}

	public static boolean equals(Object obj1, Object obj2) {
		if (obj1 == obj2) {
			return true;
		}
		if (obj1 == null || obj2 == null) {
			return false;
		}
		return obj1.equals(obj2);
	}
	
}
