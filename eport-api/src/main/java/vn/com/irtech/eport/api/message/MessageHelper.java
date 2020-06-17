package vn.com.irtech.eport.api.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import vn.com.irtech.eport.api.util.StringUtils;

@Component
public class MessageHelper implements InitializingBean{
	
	private static MessageHelper instance = null;
	
	@Autowired
	MessageSource messageSource;

	public static String getMessage(String key, Object... arguments) {
		List<String> strings = new ArrayList<String>();
		for (Object argument : arguments) {
			strings.add(argument == null?null: argument.toString());
		}
		return instance.messageSource.getMessage(key, strings.toArray(), Locale.getDefault());
	}
	
	public static String getMessage(String key, Map<String, Object> arguments) {
		String message = instance.messageSource.getMessage(key, null, Locale.getDefault());
		return StringUtils.replaceNamePlaceholders(message, arguments, "{", "}");
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (instance == null) {
			instance = this;
		}
	}

}
