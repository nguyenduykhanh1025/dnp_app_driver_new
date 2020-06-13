package vn.com.irtech.eport.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.logistic.domain.TransportAccount;
import vn.com.irtech.eport.logistic.mapper.TransportAccountMapper;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private TransportAccountMapper transportAccountMapper;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		TransportAccount user = transportAccountMapper.selectTransportAccountByMobileNumber(userName);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with name: " + userName);
		}
		return new vn.com.irtech.eport.api.domain.UserDetails(user);
	}

	public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
		TransportAccount user = transportAccountMapper.selectTransportAccountById(userId);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with id: " + userId);
		}
		return new vn.com.irtech.eport.api.domain.UserDetails(user);
	}
	
	public TransportAccount getUserInfor(Long userId) {
		return transportAccountMapper.selectTransportAccountById(userId);
	}
	
}
