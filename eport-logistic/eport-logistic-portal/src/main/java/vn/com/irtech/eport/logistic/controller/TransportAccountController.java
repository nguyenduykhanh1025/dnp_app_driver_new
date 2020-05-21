package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
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

import com.alibaba.fastjson.JSONObject;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.TransportAccount;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.ITransportAccountService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.framework.shiro.service.SysPasswordService;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

/**
 * Driver login infoController
 * 
 * @author ruoyi
 * @date 2020-05-19
 */
@Controller
@RequestMapping("/logistic/transport")
public class TransportAccountController extends BaseController
{
    private String prefix = "logistic/transport";
	public static final String PHONE_PATTERN = "^[0-9]{10,11}$";

    @Autowired
    private ITransportAccountService transportAccountService;
    
    @Autowired
    private ILogisticGroupService logisticGroupService;
    
    @Autowired
    private SysPasswordService passwordService;

    @GetMapping()
    public String account()
    {
        return prefix + "/index";
    }

    /**
     * Get Driver login info List
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TransportAccount transportAccount, String groupName)
    {
        startPage();
        transportAccount.setDelFlag(false);
        LogisticGroup logisticGroup = new LogisticGroup();
        logisticGroup.setGroupName(groupName.toLowerCase());
        transportAccount.setLogisticGroup(logisticGroup);
        transportAccount.setFullName(transportAccount.getFullName().toLowerCase());
        transportAccount.setPlateNumber(transportAccount.getPlateNumber().toLowerCase());
        List<TransportAccount> list = transportAccountService.selectTransportAccountList(transportAccount);
        return getDataTable(list);
    }

    /**
     * Export Driver login info List
     */
    @RequiresPermissions("system:account:export")
    @Log(title = "Driver login info", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TransportAccount transportAccount)
    {
        List<TransportAccount> list = transportAccountService.selectTransportAccountList(transportAccount);
        ExcelUtil<TransportAccount> util = new ExcelUtil<TransportAccount>(TransportAccount.class);
        return util.exportExcel(list, "account");
    }

    /**
     * Add Driver login info
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Add or Update Driver login info
     */
    @Log(title = "Driver login info", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TransportAccount transportAccount)
    {
        if (transportAccount.getPassword().length() < 6) {
            return error("Mật khẩu không được ít hơn 6 ký tự!");
        }
        if(transportAccountService.checkPhoneUnique(transportAccount.getMobileNumber()) > 0) {
        	return error("PhoneNumber này đã tồn tại!");
        }
        if(!Pattern.matches(PHONE_PATTERN, transportAccount.getMobileNumber())){
        	return error("PhoneNumber này phải từ 10 đến 11 số!");
        }
        transportAccount.setSalt(ShiroUtils.randomSalt());
        transportAccount.setPassword(passwordService.encryptPassword(transportAccount.getMobileNumber()
        , transportAccount.getPassword(), transportAccount.getSalt()));
        transportAccount.setCreateBy(ShiroUtils.getSysUser().getFullName());
        return toAjax(transportAccountService.insertTransportAccount(transportAccount));
    }

    /**
     * Update Driver login info
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        TransportAccount transportAccount = transportAccountService.selectTransportAccountById(id);
        mmap.put("transportAccount", transportAccount);
        return prefix + "/edit";
    }

    /**
     * Update Save Driver login info
     */
    @Log(title = "Driver login info", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TransportAccount transportAccount)
    {
        if(transportAccountService.checkPhoneUnique(transportAccount.getMobileNumber()) > 1) {
        	return error("PhoneNumber này đã tồn tại!");
        }
        if(!Pattern.matches(PHONE_PATTERN, transportAccount.getMobileNumber())){
        	return error("PhoneNumber này phải từ 10 đến 11 số!");
        }
        return toAjax(transportAccountService.updateTransportAccount(transportAccount));
    }

    /**
     * Delete Driver login info
     */
    @Log(title = "Driver login info", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(Long id)
    {
        return toAjax(transportAccountService.deleteTransportAccountById(id));
    }
    @Log(title = "Reset password", businessType = BusinessType.UPDATE)
    @GetMapping("/resetPwd/{id}")
    public String resetPwd(@PathVariable("id") Long id, ModelMap mmap)
    {
        mmap.put("transportAccount", transportAccountService.selectTransportAccountById(id));
        return prefix + "/resetPwd";
    }
    @Log(title = "Reset password", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwd")
    @ResponseBody
    public AjaxResult resetPwdSave(TransportAccount transportAccount)
    {
    	transportAccount.setUpdateBy(ShiroUtils.getSysUser().getFullName());
    	transportAccount.setSalt(ShiroUtils.randomSalt());
    	transportAccount.setPassword(passwordService.encryptPassword(transportAccount.getMobileNumber(), transportAccount.getPassword(), transportAccount.getSalt()));
        if(transportAccountService.updateTransportAccount(transportAccount) == 1)
        	return success();
        return error();
    }
    /**
     * Search Carrier Group Name
     */
    @RequestMapping("/searchGroupNameByKeyword")
    @ResponseBody
    public List<JSONObject> searchGroupNameByKeyword(String keyword, Long groupId) {
        LogisticGroup logisticGroup = new LogisticGroup();
        logisticGroup.setGroupName(keyword.toLowerCase());
        List<LogisticGroup> logisticGroups = logisticGroupService.selectLogisticGroupListByName(logisticGroup);
        List<JSONObject> result = new ArrayList<>();
		for (LogisticGroup i : logisticGroups) {
			if (i.getId() != groupId) {
                JSONObject json = new JSONObject();
                json.put("id", i.getId());
                json.put("text", i.getGroupName());
                result.add(json);
            }
		}
        return result;
    }
    @RequestMapping("/getGroupNameById")
    @ResponseBody
    public String getGroupNameById(long id) {
        LogisticGroup logisticGroup = logisticGroupService.selectLogisticGroupById(id);
        return logisticGroup.getGroupName();
    }
}
