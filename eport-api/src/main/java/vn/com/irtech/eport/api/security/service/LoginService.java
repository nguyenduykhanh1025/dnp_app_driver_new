package vn.com.irtech.eport.api.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.api.consts.BusinessConst;
import vn.com.irtech.eport.api.consts.MessageConsts;
import vn.com.irtech.eport.api.domain.EportUserType;
import vn.com.irtech.eport.api.form.LoginForm;
import vn.com.irtech.eport.api.message.MessageHelper;
import vn.com.irtech.eport.api.security.CustomUserDetails;
import vn.com.irtech.eport.api.util.TokenUtils;
import vn.com.irtech.eport.common.exception.BusinessException;

@Service
public class LoginService {

	protected static final Logger logger = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	public String login(LoginForm loginForm, EportUserType userType) {

		Authentication authentication = null;

		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					loginForm.getUserName() + BusinessConst.BLANK + userType.value(), loginForm.getPassWord()));

		} catch (BadCredentialsException e) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0003));
		} catch (AccountExpiredException e) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0003));
		} catch (DisabledException e) {
			throw new BusinessException(MessageHelper.getMessage(MessageConsts.E0004));
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

		String token = TokenUtils.generateToken(userDetails);

		return token;
	}

}
