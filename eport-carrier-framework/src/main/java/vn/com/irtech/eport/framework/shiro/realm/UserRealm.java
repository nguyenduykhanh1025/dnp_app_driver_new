package vn.com.irtech.eport.framework.shiro.realm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
import vn.com.irtech.eport.common.exception.user.CaptchaException;
import vn.com.irtech.eport.common.exception.user.RoleBlockedException;
import vn.com.irtech.eport.common.exception.user.UserBlockedException;
import vn.com.irtech.eport.common.exception.user.UserNotExistsException;
import vn.com.irtech.eport.common.exception.user.UserPasswordNotMatchException;
import vn.com.irtech.eport.common.exception.user.UserPasswordRetryLimitExceedException;
import vn.com.irtech.eport.framework.shiro.service.SysLoginService;
import vn.com.irtech.eport.framework.util.ShiroUtils;

/**
 * @author admin
 */
public class UserRealm extends AuthorizingRealm
{
    private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private SysLoginService loginService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0)
    {
        CarrierAccount user = ShiroUtils.getSysUser();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // Arrays.asList(perm.getRoleKey().trim().split(","))
        Set<String> perms = new HashSet<>();
//        if(StringUtils.isNotBlank(user.getPerms())) {
//        	perms.addAll(Arrays.asList(user.getPerms().trim().split(",")));
//        }
        // TODO dynamic setting from DB
        // set permission by flag
        if(user.getEdoFlg()) {
    		perms.add("carrier:edo");
    	}
        if(user.getDoFlg()) {
        	perms.add("carrier:do");
        }
        if(user.getBookingFlg()) {
        	perms.add("carrier:booking");
        }
        if(user.getDepoFlg()) {
        	perms.add("carrier:depo");
        }
        if(user.getEdoFlg() || user.getDoFlg()) {
        	perms.add("carrier:inquery");
        }
        info.setStringPermissions(perms);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
    {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        String password = "";
        if (upToken.getPassword() != null)
        {
            password = new String(upToken.getPassword());
        }

        CarrierAccount user = null;
        try
        {
            user = loginService.login(username, password);
        }
        catch (CaptchaException e)
        {
            throw new AuthenticationException(e.getMessage(), e);
        }
        catch (UserNotExistsException e)
        {
            throw new UnknownAccountException(e.getMessage(), e);
        }
        catch (UserPasswordNotMatchException e)
        {
            throw new IncorrectCredentialsException(e.getMessage(), e);
        }
        catch (UserPasswordRetryLimitExceedException e)
        {
            throw new ExcessiveAttemptsException(e.getMessage(), e);
        }
        catch (UserBlockedException e)
        {
            throw new LockedAccountException(e.getMessage(), e);
        }
        catch (RoleBlockedException e)
        {
            throw new LockedAccountException(e.getMessage(), e);
        }
        catch (Exception e)
        {
            log.info("Login verification for user [" + username + "] failed: {}", e.getMessage());
            throw new AuthenticationException(e.getMessage(), e);
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
        return info;
    }

    public void clearCachedAuthorizationInfo()
    {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
