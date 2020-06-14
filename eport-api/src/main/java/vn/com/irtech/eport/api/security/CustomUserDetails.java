package vn.com.irtech.eport.api.security;

import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import vn.com.irtech.eport.api.domain.EportUser;

public class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;

	protected static final Logger logger = LoggerFactory.getLogger(CustomUserDetails.class);

	private EportUser user;

	public CustomUserDetails() {
	}

	public CustomUserDetails(EportUser user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(user.getUserType().value()));
	}

	@Override
	public String getPassword() {
		return user.getLoginName() + " " + user.getPassword() + " " + user.getSalt();
	}

	@Override
	public String getUsername() {
		return user.getLoginName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return !user.getIsDeleted();
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
		return !user.getIsLocked();
	}

	public EportUser getUser() {
		return user;
	}

	public void setUser(EportUser user) {
		this.user = user;
	}

}
