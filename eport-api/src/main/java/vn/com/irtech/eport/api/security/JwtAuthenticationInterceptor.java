package vn.com.irtech.eport.api.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.com.irtech.eport.api.consts.BusinessConst;
import vn.com.irtech.eport.api.consts.MessageConsts;
import vn.com.irtech.eport.api.message.MessageHelper;
import vn.com.irtech.eport.api.security.service.CustomUserDetailsService;
import vn.com.irtech.eport.api.util.StringUtils;
import vn.com.irtech.eport.api.util.TokenUtils;
import vn.com.irtech.eport.common.core.domain.AjaxResult;

public class JwtAuthenticationInterceptor extends HandlerInterceptorAdapter {

	protected static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationInterceptor.class);
	
	@Autowired
	private CustomUserDetailsService userDetailService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI = request.getRequestURI();

		if (requestURI != null && (requestURI.contains("/login") || requestURI.contains("/error")
				|| StringUtils.isBlank(requestURI))) {
			return true;
		}

		String token = request.getHeader("Authorization");
		try {
			if (StringUtils.isNotBlank(token) && TokenUtils.validateToken(token)) {
				String subject = TokenUtils.getSubjectFromToken(token);
				
				if (!validatePermission(requestURI, subject)) {
					logger.debug("Do not permission!");
					setResponseError(response);
					return false;
				}
				
				CustomUserDetails userDetails = (CustomUserDetails) userDetailService.loadUserByUsername(subject);

				if (userDetails != null) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);

					response.setHeader("RefreshToken", TokenUtils.generateToken(userDetails));
					return true;
				} else {
					return false;
				}
			} else {
				logger.debug("Invalid token!");
				setResponseError(response);
				return false;
			}
		} catch (Exception e) {
			logger.debug("Failed on authentication user: " + e.getMessage());
			setResponseError(response);
			return false;
		}
	}
	
	private Boolean validatePermission(String requestUri, String subject) {
		try {
			String perm = subject.split(BusinessConst.BLANK)[1];
			if (requestUri.toLowerCase().contains(perm.toLowerCase())){
				return true;
			}else {
				return false;
			}
		} catch(Exception ex) {
			return false;
		}
	}

	private void setResponseError(HttpServletResponse response) throws IOException {
		AjaxResult responseData = AjaxResult
				.error(MessageHelper.getMessage(MessageHelper.getMessage(MessageConsts.E0002)));
		responseData.put("errorCode", HttpServletResponse.SC_UNAUTHORIZED);
		byte[] responseToSend = new ObjectMapper().writeValueAsString(responseData).getBytes();
		response.setHeader("Content-type", "application/json");
		response.getOutputStream().write(responseToSend);
	}

}
