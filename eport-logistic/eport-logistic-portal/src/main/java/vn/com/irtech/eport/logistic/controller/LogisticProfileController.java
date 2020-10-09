package vn.com.irtech.eport.logistic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.framework.shiro.service.SysPasswordService;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.LogisticDelegated;
import vn.com.irtech.eport.logistic.service.ILogisticAccountService;
import vn.com.irtech.eport.logistic.service.ILogisticDelegatedService;

@Controller
@RequestMapping("/logistic/profile")
public class LogisticProfileController extends LogisticBaseController{
	private String prefix = "logistic/profile";
    @Autowired
    private ILogisticAccountService logisticAccountService;
    
    @Autowired
    private ILogisticDelegatedService logisticDelegatedService;
    
    @Autowired
    private SysPasswordService passwordService;
    
    @GetMapping()
    public String profile(ModelMap mmap)
    {
        LogisticAccount user = ShiroUtils.getSysUser();
        mmap.put("user", user);
        return prefix + "/profile";
    }

    @GetMapping("/resetPwd")
    public String resetPwd(ModelMap mmap)
    {
        LogisticAccount user = ShiroUtils.getSysUser();
        mmap.put("user", logisticAccountService.selectLogisticAccountById(user.getId()));
        return prefix + "/resetPwd";
    }
    @Log(title = "Cập Nhật Profile", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public AjaxResult update(LogisticAccount user)
    {
        LogisticAccount currentUser = ShiroUtils.getSysUser();
        currentUser.setFullName(user.getFullName());
        currentUser.setRemark(user.getRemark());
        if (logisticAccountService.updateLogisticAccount(currentUser) > 0)
        {
            ShiroUtils.setSysUser(logisticAccountService.selectLogisticAccountById(currentUser.getId()));
            return success();
        }
        return error();
    }
    
    @GetMapping("/checkPassword")
    @ResponseBody
    public boolean checkPassword(String password)
    {
        LogisticAccount user = ShiroUtils.getSysUser();
        if (passwordService.matches(user, password))
        {
            return true;
        }
        return false;
    }
    
    @Log(title = "Đổi Mật Khẩu", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
	@PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwd(String oldPassword, String newPassword)
    {
        LogisticAccount user = ShiroUtils.getSysUser();
        if (StringUtils.isNotEmpty(newPassword) && passwordService.matches(user, oldPassword))
        {
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(passwordService.encryptPassword(user.getUserName(), newPassword, user.getSalt()));
            if (logisticAccountService.resetUserPwd(user) > 0)
            {
                ShiroUtils.setSysUser(logisticAccountService.selectLogisticAccountById(user.getId()));
                return success();
            }
            return error();
        }
        else
        {
            return error("Mật khẩu cũ không đúng.");
        }
    }

    @GetMapping("/delegated")
    public String delegated(ModelMap mmap)
    {
        return prefix + "/delegated";
    }

    @PostMapping("/delegated/list")
    @ResponseBody
    public TableDataInfo delegatedList(@RequestBody PageAble<LogisticDelegated> param)
    {
        startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
        LogisticDelegated logisticDelegated = param.getData();
        if (logisticDelegated == null) {
            logisticDelegated = new LogisticDelegated();
        }
        logisticDelegated.setLogisticGroupId(getGroup().getId());
        logisticDelegated.setDelFlg(0);
        List<LogisticDelegated> list = logisticDelegatedService.selectLogisticDelegatedList(logisticDelegated);
        return getDataTable(list);
    }

  
}
