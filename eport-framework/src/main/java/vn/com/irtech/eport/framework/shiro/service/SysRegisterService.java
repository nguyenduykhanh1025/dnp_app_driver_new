package vn.com.irtech.eport.framework.shiro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.constant.ShiroConstants;
import vn.com.irtech.eport.common.constant.UserConstants;
import vn.com.irtech.eport.common.utils.MessageUtils;
import vn.com.irtech.eport.common.utils.ServletUtils;
import vn.com.irtech.eport.framework.manager.AsyncManager;
import vn.com.irtech.eport.framework.manager.factory.AsyncFactory;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.system.domain.SysUser;
import vn.com.irtech.eport.system.service.ISysUserService;

/**
 * 注册校验方法
 * 
 * @author admin
 */
@Component
public class SysRegisterService
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPasswordService passwordService;

    /**
     * 注册
     */
    public String register(SysUser user)
    {
        String msg = "", username = user.getLoginName(), password = user.getPassword();

        if (!StringUtils.isEmpty(ServletUtils.getRequest().getAttribute(ShiroConstants.CURRENT_CAPTCHA)))
        {
            msg = "Captcha error";
        }
        else if (StringUtils.isEmpty(username))
        {
            msg = "Username can not be empty";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "User password cannot be empty";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "Account length must be between 2 and 20 characters";
        }
        else if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(username)))
        {
            msg = "Create user '" + username + "' , Registered account already exists";
        }
        else
        {
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
            boolean regFlag = userService.registerUser(user);
            if (!regFlag)
            {
                msg = "Registration failed, please contact system administrator";
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }
}
