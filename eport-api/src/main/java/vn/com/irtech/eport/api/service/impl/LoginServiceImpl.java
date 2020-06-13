package vn.com.irtech.eport.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.api.common.consts.MessageConsts;
import vn.com.irtech.eport.api.common.message.MessageHelper;
import vn.com.irtech.eport.api.domain.UserDetails;
import vn.com.irtech.eport.api.dto.request.LoginForm;
import vn.com.irtech.eport.api.service.ILoginService;
import vn.com.irtech.eport.api.util.TokenUtils;
import vn.com.irtech.eport.common.exception.BusinessException;

@Service
public class LoginServiceImpl implements ILoginService {
	
	protected static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public String login(LoginForm loginForm) {

		Authentication authentication = null;

		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginForm.getUserName(), loginForm.getPassWord()));

		} catch (BadCredentialsException e) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0003));
		} catch (AccountExpiredException e2) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0004));
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		String token = TokenUtils.generateToken(userDetails);

		return token;
	}

}
