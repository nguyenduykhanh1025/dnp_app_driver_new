package vn.com.irtech.eport.api.intercepter;

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

import vn.com.irtech.eport.api.common.consts.MessageConsts;
import vn.com.irtech.eport.api.common.message.MessageHelper;
import vn.com.irtech.eport.api.domain.UserDetails;
import vn.com.irtech.eport.api.service.impl.UserService;
import vn.com.irtech.eport.api.util.StringUtils;
import vn.com.irtech.eport.api.util.TokenUtils;
import vn.com.irtech.eport.common.core.domain.AjaxResult;

public class JwtAuthenticationInterceptor extends HandlerInterceptorAdapter {

	protected static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationInterceptor.class);

	@Autowired
	private UserService userService;

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
				Long userId = TokenUtils.getUserIdFromToken(token);

				UserDetails userDetails = (UserDetails) userService.loadUserByUserId(userId);

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

	private void setResponseError(HttpServletResponse response) throws IOException {
		AjaxResult responseData = AjaxResult
				.error(MessageHelper.getMessage(MessageHelper.getMessage(MessageConsts.E0002)));
		responseData.put("errorCode", HttpServletResponse.SC_UNAUTHORIZED);
		byte[] responseToSend = new ObjectMapper().writeValueAsString(responseData).getBytes();
		response.setHeader("Content-type", "application/json");
		response.getOutputStream().write(responseToSend);
	}

}
