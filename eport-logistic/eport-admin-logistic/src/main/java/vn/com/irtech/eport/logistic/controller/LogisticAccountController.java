package vn.com.irtech.eport.logistic.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.UserConstants;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.service.ILogisticAccountService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.framework.mail.service.MailService;
import vn.com.irtech.eport.framework.shiro.service.SysPasswordService;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * Logistic accountController
 * 
 * @author admin
 * @date 2020-05-07
 */
@Controller
@RequestMapping("/logistic/account")
public class LogisticAccountController extends BaseController
{
    private String prefix = "logistic/account";

    @Autowired
    private MailService mailService;
    @Autowired
    private SysPasswordService passwordService;
    @Autowired
    private ILogisticAccountService logisticAccountService;

    @RequiresPermissions("logistic:account:view")
    @GetMapping()
    public String account()
    {
        return prefix + "/account";
    }

    /**
     * Get Logistic account List
     */
    @RequiresPermissions("logistic:account:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LogisticAccount logisticAccount)
    {
        startPage();
        logisticAccount.setDelFlag("0");
        LogisticGroup logisticGroup = logisticAccount.getLogisticGroup();
        logisticGroup.setGroupName(logisticGroup.getGroupName().toLowerCase());
        logisticAccount.setLogisticGroup(logisticGroup);
        logisticAccount.setEmail(logisticAccount.getEmail().toLowerCase());
        logisticAccount.setFullName(logisticAccount.getFullName().toLowerCase());
        List<LogisticAccount> list = logisticAccountService.selectLogisticAccountList(logisticAccount);
        return getDataTable(list);
    }

    /**
     * Export Logistic account List
     */
    @RequiresPermissions("logistic:account:export")
    @Log(title = "Logistic account", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LogisticAccount logisticAccount)
    {
        List<LogisticAccount> list = logisticAccountService.selectLogisticAccountList(logisticAccount);
        ExcelUtil<LogisticAccount> util = new ExcelUtil<LogisticAccount>(LogisticAccount.class);
        return util.exportExcel(list, "account");
    }

    /**
     * Add Logistic account
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Add or Update Logistic account
     */
    @RequiresPermissions("logistic:account:add")
    @Log(title = "Logistic account", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(LogisticAccount logisticAccount, String isSendEmail)
    {
        if (!Pattern.matches(UserConstants.EMAIL_PATTERN, logisticAccount.getEmail())) {
            return error("Email không hợp lệ!");
        }
//        if (logisticAccountService.checkEmailUnique(logisticAccount.getEmail().toLowerCase()).equals("1")) {
//            return error("Email đã tồn tại!");
//        }
        if (logisticAccount.getPassword().length() < 6) {
            return error("Mật khẩu không được ít hơn 6 ký tự!");
        }
        Map<String, Object> variables = new HashMap<>();
		variables.put("username", logisticAccount.getUserName());
        variables.put("password", logisticAccount.getPassword());
        variables.put("email", logisticAccount.getEmail());
        logisticAccount.setSalt(ShiroUtils.randomSalt());
        logisticAccount.setPassword(passwordService.encryptPassword(logisticAccount.getUserName()
        , logisticAccount.getPassword(), logisticAccount.getSalt()));
        logisticAccount.setCreateBy(ShiroUtils.getSysUser().getUserName());
        if (logisticAccountService.insertLogisticAccount(logisticAccount) == 1) {
            if (isSendEmail != null) {
                new Thread() {
                    public void run() {
                        try {
                            mailService.prepareAndSend("Cấp tài khoản truy cập", logisticAccount.getEmail(), variables, "email");  
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                }.start();
            }
            return success();
        }             
        return error();
        //return toAjax(logisticAccountService.insertLogisticAccount(logisticAccount));
    }

    /**
     * Update Logistic account
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        LogisticAccount logisticAccount = logisticAccountService.selectLogisticAccountById(id);
        mmap.put("logisticAccount", logisticAccount);
        return prefix + "/edit";
    }

    /**
     * Update Save Logistic account
     */
    @RequiresPermissions("logistic:account:edit")
    @Log(title = "Logistic account", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(LogisticAccount logisticAccount)
    {
        return toAjax(logisticAccountService.updateLogisticAccount(logisticAccount));
    }

    /**
     * Delete Logistic account
     */
    @RequiresPermissions("logistic:account:remove")
    @Log(title = "Logistic account", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(logisticAccountService.updateDelFlagLogisticAccountByIds(ids));
    }
    @Log(title = "Reset password", businessType = BusinessType.UPDATE)
    @GetMapping("/resetPwd/{id}")
    public String resetPwd(@PathVariable("id") Long id, ModelMap mmap)
    {
        mmap.put("logisticAccount", logisticAccountService.selectLogisticAccountById(id));
        return prefix + "/resetPwd";
    }
    
    @Log(title = "Reset password", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwdSave(LogisticAccount logisticAccount, String isSendEmail)
    {
        Map<String, Object> variables = new HashMap<>();
		variables.put("username", logisticAccount.getUserName());
        variables.put("password", logisticAccount.getPassword());
        variables.put("email", logisticAccount.getEmail());
        logisticAccount.setStatus("");
        logisticAccount.setUpdateBy(ShiroUtils.getSysUser().getUserName());
        logisticAccount.setSalt(ShiroUtils.randomSalt());
        logisticAccount.setPassword(passwordService.encryptPassword(logisticAccount.getUserName(), logisticAccount.getPassword(), logisticAccount.getSalt()));
        if (logisticAccountService.updateLogisticAccount(logisticAccount) == 1) {
            if (isSendEmail != null) {
                new Thread() {
                    public void run() {
                        try {
                            mailService.prepareAndSend("Thiết lập lại mật khẩu", logisticAccount.getEmail(), variables, "resetPassword");  
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                }.start();
            } 
            return success();
        }             
        return error();
    }
    
}
