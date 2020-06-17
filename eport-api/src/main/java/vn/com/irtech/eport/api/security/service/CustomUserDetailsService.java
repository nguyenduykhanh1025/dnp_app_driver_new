package vn.com.irtech.eport.api.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.api.domain.EportUser;
import vn.com.irtech.eport.api.domain.EportUserType;
import vn.com.irtech.eport.api.security.CustomUserDetails;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.TransportAccount;
import vn.com.irtech.eport.logistic.mapper.LogisticAccountMapper;
import vn.com.irtech.eport.logistic.mapper.TransportAccountMapper;
import vn.com.irtech.eport.system.domain.SysUser;
import vn.com.irtech.eport.system.mapper.SysUserMapper;

/**
 * 
 * @author Bao
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private TransportAccountMapper transportAccountMapper;

	@Autowired
	private SysUserMapper sysUserMapper;

	@Autowired
	private LogisticAccountMapper logisticAccountMapper;

	private final EportUserType DEFAULT_USER_TYPE = EportUserType.ADMIN;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		EportUserType userType = DEFAULT_USER_TYPE;
		String loginName = userName;

		String[] arr = userName.split(" ");

		if (arr.length == 2) {
			loginName = arr[0];
			userType = EportUserType.fromString(arr[1]);
		}

		EportUser user = null;
		switch (userType) {
		case ADMIN:
			user = loadSysUserByUsername(loginName);
			break;
		case LOGISTIC:
			user = loadLogisticUserByUsername(loginName);
			break;
		case TRANSPORT:
			user = loadTransportUserByUsername(loginName);
			break;
		default:
			user = null;
			break;

		}

		if (user == null) {
			throw new UsernameNotFoundException("User not found with name: " + loginName);
		}

		return new CustomUserDetails(user);
	}

	private EportUser loadSysUserByUsername(String userName) {
		SysUser sysUser = sysUserMapper.selectUserByLoginName(userName);
		if (sysUser == null) {
			return null;
		}

		EportUser eportUser = new EportUser();
		eportUser.setUserId(sysUser.getUserId());
		eportUser.setLoginName(sysUser.getLoginName());
		eportUser.setPassword(sysUser.getPassword());
		eportUser.setSalt(sysUser.getSalt());
		eportUser.setIsDeleted("1".equals(sysUser.getDelFlag()));
		eportUser.setIsLocked("1".equals(sysUser.getStatus()));
		eportUser.setUserType(EportUserType.ADMIN);
		return eportUser;
	}

	private EportUser loadLogisticUserByUsername(String userName) {
		LogisticAccount logisticAccount = logisticAccountMapper.selectByUserName(userName);
		if (logisticAccount == null) {
			return null;
		}

		EportUser eportUser = new EportUser();
		eportUser.setUserId(logisticAccount.getId());
		eportUser.setLoginName(logisticAccount.getUserName());
		eportUser.setPassword(logisticAccount.getPassword());
		eportUser.setSalt(logisticAccount.getSalt());
		eportUser.setIsDeleted("1".equals(logisticAccount.getDelFlag()));
		eportUser.setIsLocked("1".equals(logisticAccount.getStatus()));
		eportUser.setUserType(EportUserType.LOGISTIC);
		return eportUser;
	}

	private EportUser loadTransportUserByUsername(String userName) {
		TransportAccount transportAccount = transportAccountMapper.selectTransportAccountByMobileNumber(userName);
		if (transportAccount == null) {
			return null;
		}

		EportUser eportUser = new EportUser();
		eportUser.setUserId(transportAccount.getId());
		eportUser.setLoginName(transportAccount.getMobileNumber());
		eportUser.setPassword(transportAccount.getPassword());
		eportUser.setSalt(transportAccount.getSalt());
		eportUser.setIsDeleted(transportAccount.getDelFlag());
		eportUser.setIsLocked("1".equals(transportAccount.getStatus()));
		eportUser.setUserType(EportUserType.TRANSPORT);
		return eportUser;
	}
}
