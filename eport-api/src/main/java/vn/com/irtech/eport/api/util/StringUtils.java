package vn.com.irtech.eport.api.util;

import java.util.List;
import java.util.Map;

public class StringUtils extends org.apache.commons.lang3.StringUtils{
	
	public static String concat(String ...values) {
		StringBuilder builder = new StringBuilder();
		for (String value : values) {
			builder.append(value);
		}
		return builder.toString();
	}
	
	public static String join(List<String> target, char separator) {
		StringBuilder builder = new StringBuilder();
		for (String str : target) {
			builder.append(str).append(separator);
		}
		return builder.deleteCharAt(builder.length() - 1).toString();
	}
	
	public static String replaceNamePlaceholders(String value, Map<String, Object> params, String prefix, String safix) {
		String result = value;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			result = result.replace(prefix + entry.getKey() + safix, entry.getValue().toString());
		}
		return result;
	}
}
