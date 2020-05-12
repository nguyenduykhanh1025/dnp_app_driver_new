package vn.com.irtech.eport.framework.shiro.service;

import vn.com.irtech.eport.framework.domain.SysUser;

/**
 * Login verification method
 * 
 * @author admin
 */
public interface ISysLoginService
{
    /**
     * Login
     */
    public SysUser login(String username, String password);


    /**
     * Record login info
     */
    public void recordLoginInfo(SysUser user);
}
