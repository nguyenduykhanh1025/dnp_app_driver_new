package vn.com.irtech.eport.carrier.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
import vn.com.irtech.eport.carrier.service.ICarrierAccountService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.shiro.service.SysPasswordService;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.system.domain.SysUser;

@Controller
@RequestMapping("/carrier/profile")
public class CarrierProfileController extends BaseController{
	private String prefix = "carrier/profile";
    @Autowired
    private ICarrierAccountService carrierAccountService;
    
    @Autowired
    private SysPasswordService passwordService;
    
    /**
     * 个人信息
     */
    @GetMapping()
    public String profile(ModelMap mmap)
    {
        CarrierAccount user = ShiroUtils.getSysUser();
        mmap.put("user", user);
//        mmap.put("roleGroup", carrierAccountService.selectUserRoleGroup(user.getId()));
//        mmap.put("postGroup", carrierAccountService.selectUserPostGroup(user.getId()));
        return prefix + "/profile";
    }

    @GetMapping("/resetPwd")
    public String resetPwd(ModelMap mmap)
    {
        CarrierAccount user = ShiroUtils.getSysUser();
        mmap.put("user", carrierAccountService.selectCarrierAccountById(user.getId()));
        return prefix + "/resetPwd";
    }
    @Log(title = "Profile", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public AjaxResult update(CarrierAccount user)
    {
        CarrierAccount currentUser = ShiroUtils.getSysUser();
        currentUser.setFullName(user.getFullName());
        currentUser.setGroupId(user.getGroupId());
        currentUser.setRemark(user.getRemark());
        if (carrierAccountService.updateCarrierAccount(currentUser) > 0)
        {
            ShiroUtils.setSysUser(carrierAccountService.selectCarrierAccountById(currentUser.getId()));
            return success();
        }
        return error();
    }
    
    @GetMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password)
    {
        CarrierAccount user = ShiroUtils.getSysUser();
        if (passwordService.matches(user, password))
        {
            return true;
        }
        return false;
    }
    
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwd(String oldPassword, String newPassword)
    {
        CarrierAccount user = ShiroUtils.getSysUser();
        if (StringUtils.isNotEmpty(newPassword) && passwordService.matches(user, oldPassword))
        {
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(passwordService.encryptPassword(user.getEmail(), newPassword, user.getSalt()));
            if (carrierAccountService.resetUserPwd(user) > 0)
            {
                ShiroUtils.setSysUser(carrierAccountService.selectCarrierAccountById(user.getId()));
                return success();
            }
            return error();
        }
        else
        {
            return error("Failed to change password, old password is wrong");
        }
    }
}
