package util;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public class NullUtil {

	 	public static boolean isNull(Object value) {
	        return value == null;
	    }
	 	
	 	public static boolean isNull(Number number) {
		       return (number == null);
		}
	    
	    public static boolean isNullOrEmpty(String value) {
	        return (value == null) || (value.trim().length() == 0);
	    }

	    public static boolean isNullOrEmpty(Object value) {
	        return (value == null);
	    }

	    public static <T> boolean isNullOrEmpty(Collection<T> collection) {
	        return (collection == null) || (collection.isEmpty());
	    }

	    public static boolean isNullOrEmpty(Number number) {
	        return (number == null) || (!(number.doubleValue() > 0));
	    }

	    public static boolean isNullOrEmpty(Date data) {
	        return (data == null) || (!(data.getTime() > 0));
	    }

	    public static <T> boolean isNullOrEmpty(Map<T, T> map) {
	        return (map == null) || (map.isEmpty());
	    }
	    
	    public static boolean isNullOrEmpty(File file) {
	        return isNull(file) || file.length() == 0;
	    }
	    
	    public static boolean isNullOrEmpty(Object[] array) {
	        return (array == null) || (array.length == 0);
	    }

		public static String getEmptyValue() {
			return "";
		}
		
		public static boolean isNullOrEmptyOrZero(String value) {
			return isNullOrEmpty(value) || value.equals("0")|| value.equals("00");
	    }
		
		public static boolean isNullOrEmptyOrValorZero(String value) {
			return isNullOrEmpty(value) || value.equals("0") || value.equals("0,00") || value.equals("0.00");
	    }
		
		public static boolean isNullOrEmpty(StringBuilder value) {
		    return value == null || value.toString().trim().length() == 0;
		}
		
		public static boolean isNullOrEmptyOrZero(Number value) {
			return isNullOrEmpty(value) || value.intValue() == 0;
	    }

		public static boolean isNodeNullOrEmpty(JsonNode node) {
			return node == null || node.isNull() || node.isMissingNode()
					|| (node.isTextual() && node.asText().trim().isEmpty());
		}
}
