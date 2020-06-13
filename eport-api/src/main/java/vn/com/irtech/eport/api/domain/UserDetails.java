package vn.com.irtech.eport.api.domain;

import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import vn.com.irtech.eport.logistic.domain.TransportAccount;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
	private static final long serialVersionUID = 1L;

	protected static final Logger logger = LoggerFactory.getLogger(UserDetails.class);

	private TransportAccount user;

	public UserDetails() {
	}

	public UserDetails(TransportAccount user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		return user.getMobileNumber() + " " + user.getPassword() + " " + user.getSalt();
	}

	@Override
	public String getUsername() {
		return user.getMobileNumber();
	}

	@Override
	public boolean isAccountNonExpired() {
		return !("1".equals(user.getDelFlag()));
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return !("1".equals(user.getStatus()));
	}

	public TransportAccount getUser() {
		return user;
	}

	public void setUser(TransportAccount user) {
		this.user = user;
	}

}
