package vn.com.irtech.eport.api.common.encoder;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {

	protected static final Logger logger = LoggerFactory.getLogger(CustomPasswordEncoder.class);

	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String[] arr = encodedPassword.split(" ");

		if (arr.length == 3) {
			String userName = arr[0];
			String password = arr[1];
			String salt = arr[2];
			Boolean result = password.equals(encryptPassword(userName, rawPassword.toString(), salt));
			return result;
		}

		return false;
	}

	private String encryptPassword(String username, String password, String salt) {
		return new Md5Hash(username + password + salt).toHex();
	}
}
