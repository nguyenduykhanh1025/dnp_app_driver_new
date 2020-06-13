package vn.com.irtech.eport.api.util;

import org.springframework.security.core.context.SecurityContextHolder;

import vn.com.irtech.eport.api.domain.UserDetails;

public class SecurityUtils {
	
	public static UserDetails getCurrentUser() {
		try {
			return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}
