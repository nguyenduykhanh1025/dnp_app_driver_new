package vn.com.irtech.eport.api.util;

import org.springframework.security.core.context.SecurityContextHolder;

import vn.com.irtech.eport.api.security.CustomUserDetails;

public class SecurityUtils {
	
	public static CustomUserDetails getCurrentUser() {
		try {
			return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}
