package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.UserConstants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.framework.web.service.DictService;
import vn.com.irtech.eport.logistic.domain.LogisticDelegated;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.ILogisticAccountService;
import vn.com.irtech.eport.logistic.service.ILogisticDelegatedService;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.system.dto.PartnerInfoDto;

/**
 * Logistic GroupController
 * 
 * @author admin
 * @date 2020-05-07
 */
@Controller
@RequestMapping("/logistic/group")
public class LogisticGroupController extends BaseController
{
    private String prefix = "logistic/group";

    @Autowired
    private ILogisticGroupService logisticGroupService;
    
    @Autowired
    private ILogisticAccountService logisticAccountService;
    
    @Autowired
    private ILogisticDelegatedService logisticDelegatedService;

    @Autowired
    private ICatosApiService catosApiService;
    
    @Autowired
    private DictService dictService;

    @RequiresPermissions("logistic:group:view")
    @GetMapping()
    public String group()
    {
        return prefix + "/group";
    }

    /**
     * Get Logistic Group List
     */
    @RequiresPermissions("logistic:group:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(LogisticGroup logisticGroup)
    {
        startPage();
        logisticGroup.setDelFlag("0");
        logisticGroup.setGroupName(logisticGroup.getGroupName().toLowerCase());
        logisticGroup.setEmail(logisticGroup.getEmail().toLowerCase());
        List<LogisticGroup> list = logisticGroupService.selectLogisticGroupList(logisticGroup);
        return getDataTable(list);
    }

    /**
     * Export Logistic Group List
     */
    @RequiresPermissions("logistic:group:export")
    @Log(title = "Logistic Group", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(LogisticGroup logisticGroup)
    {
        List<LogisticGroup> list = logisticGroupService.selectLogisticGroupList(logisticGroup);
        ExcelUtil<LogisticGroup> util = new ExcelUtil<LogisticGroup>(LogisticGroup.class);
        return util.exportExcel(list, "group");
    }

    /**
     * Add Logistic Group
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
    	mmap.put("delegateTypes", dictService.getType("delegate_type_list"));
        return prefix + "/add";
    }

    /**
     * Add or Update Logistic Group
     */
    @RequiresPermissions("logistic:group:add")
    @Log(title = "Logistic Group", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody JSONObject requestParam)
    {
    	LogisticGroup logisticGroup = new Gson().fromJson(requestParam.getString("logisticGroup"), LogisticGroup.class);
    	List<LogisticDelegated> logisticDelegateds = new Gson().fromJson(requestParam.getString("delegatedLogistics"), new TypeToken<ArrayList<LogisticDelegated>>(){}.getType());
        if (!Pattern.matches(UserConstants.EMAIL_PATTERN, logisticGroup.getEmail())) {
            return error("Email không hợp lệ!");
        }
        if (!Pattern.matches(UserConstants.MST_PATTERN, logisticGroup.getMst())) {
        	return error("MST không hợp lệ. Từ 10 -> 15 số");
        }
//        if (!Pattern.matches(UserConstants.IDENTIFY_NO_PATTERN, logisticGroup.getIdentifyCardNo())){
//            return error("Chứng minh thư không hợp lệ. Từ 9->15 số");
//        }
//        if (!Pattern.matches(UserConstants.NUMBER_PATTERN, logisticGroup.getPhone())){
//            return error("Điện thoại cố định phải là số");
//        }
//        if (!Pattern.matches(UserConstants.NUMBER_PATTERN, logisticGroup.getFax())){
//            return error("Fax phải là số");
//        }
        // handle String mobile regex exclude (.,-,+,' ')
        String mobilePhone = logisticGroup.getMobilePhone();
        String replace = mobilePhone.replaceAll("[\\s,\\.,\\-,\\+]", "");
        logisticGroup.setMobilePhone(replace);
        if (!Pattern.matches(UserConstants.MOBILE_PHONE_PATTERN, logisticGroup.getMobilePhone())) {
        	return error("Điện thoại di động không hợp lệ");
        }
        logisticGroupService.insertLogisticGroup(logisticGroup);
        if (CollectionUtils.isNotEmpty(logisticDelegateds)) {
        	for (LogisticDelegated logisticDelegated : logisticDelegateds) {
        		logisticDelegated.setLogisticGroupId(logisticGroup.getId());
        		logisticDelegatedService.insertLogisticDelegated(logisticDelegated);
        	}
        }
        return success();
    }

    /**
     * Update Logistic Group
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        LogisticGroup logisticGroup = logisticGroupService.selectLogisticGroupById(id);
        mmap.put("logisticGroup", logisticGroup);
        mmap.put("delegateTypes", dictService.getType("delegate_type_list"));
        return prefix + "/edit";
    }

    /**
     * Update Save Logistic Group
     */
    @RequiresPermissions("logistic:group:edit")
    @Log(title = "Logistic Group", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody LogisticGroup logisticGroup)
    {
        if (!Pattern.matches(UserConstants.EMAIL_PATTERN, logisticGroup.getEmail())) {
            return error("Email không hợp lệ!");
        }
        if (!Pattern.matches(UserConstants.MST_PATTERN, logisticGroup.getMst())) {
        	return error("MST không hợp lệ. Từ 10 -> 15 số");
        }
//        if (!Pattern.matches(UserConstants.IDENTIFY_NO_PATTERN, logisticGroup.getIdentifyCardNo())){
//            return error("Chứng minh thư không hợp lệ. Từ 9->15 số");
//        }
//        if (!Pattern.matches(UserConstants.NUMBER_PATTERN, logisticGroup.getPhone())){
//            return error("Điện thoại cố định phải là số");
//        }
//        if (!Pattern.matches(UserConstants.NUMBER_PATTERN, logisticGroup.getFax())){
//            return error("Fax phải là số");
//        }
        // handle String mobile regex exclude (.,-,+,' ')
        String mobilePhone = logisticGroup.getMobilePhone();
        String replace = mobilePhone.replaceAll("[\\s,\\.,\\-,\\+]", "");
        logisticGroup.setMobilePhone(replace);
        if (!Pattern.matches(UserConstants.MOBILE_PHONE_PATTERN, logisticGroup.getMobilePhone())) {
        	return error("Điện thoại di động không hợp lệ");
        }
        return toAjax(logisticGroupService.updateLogisticGroup(logisticGroup));
    }

    /**
     * Delete Logistic Group
     */
    @RequiresPermissions("logistic:group:remove")
    @Log(title = "Logistic Group", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
    	try {
        		logisticGroupService.updateDelFlagLogisticGroupByIds(ids);
        		logisticAccountService.updateDelFlagLogisticAccountByGroupIds(ids);
        		logisticDelegatedService.updateDelFlgByGroupIds(ids);
        		return success();
        	
    	}catch(Exception e) {
    		e.getStackTrace();
    		return error();
    	}
        //return toAjax(logisticGroupService.updateDelFlagLogisticGroupByIds(ids));
    }
    
    /**
     * Search Carrier Group Name
     */
    @RequestMapping("/searchGroupNameByKeyword")
    @ResponseBody
    public List<JSONObject> searchGroupNameByKeyword(String keyword, Long groupId) {
        LogisticGroup logisticGroup = new LogisticGroup();
        logisticGroup.setGroupName(keyword.toLowerCase());
        logisticGroup.setDelFlag("0");
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

    @GetMapping("/company/{taxCode}")
    @ResponseBody
    public AjaxResult getCompanyInfoByTaxcode(@PathVariable String taxCode) {
        AjaxResult ajaxResult = AjaxResult.success();
		if (taxCode == null || "".equals(taxCode)) {
			return error();
		}
		PartnerInfoDto partner = catosApiService.getGroupNameByTaxCode(taxCode);
		String groupName = partner.getGroupName();
		String address = partner.getAddress();
		if (address != null) {
			ajaxResult.put("address", address);
		}
		if (groupName != null) {
			ajaxResult.put("groupName", groupName);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
    }
    
    @GetMapping("/consignee/{taxCode}")
    @ResponseBody
    public AjaxResult getConsigneeInfoByTaxcode(@PathVariable String taxCode) {
        AjaxResult ajaxResult = AjaxResult.success();
		if (taxCode == null || "".equals(taxCode)) {
			return error();
		}
		PartnerInfoDto partner = catosApiService.getConsigneeNameByTaxCode(taxCode);
		String groupName = partner.getGroupName();
		String address = partner.getAddress();
		if (address != null) {
			ajaxResult.put("address", address);
		}
		if (groupName != null) {
			ajaxResult.put("groupName", groupName);
		} else {
			ajaxResult = AjaxResult.error();
		}
		return ajaxResult;
    }
   
    @GetMapping("/delegate/edit/{delegateId}")
    public String getDelegateEditForm(@PathVariable("delegateId") Long id, ModelMap mmap) {
    	mmap.put("logisticDelegated", logisticDelegatedService.selectLogisticDelegatedById(id));
    	return prefix + "/editDelegate";
    }
    
    @PostMapping("/delegates")
    @ResponseBody
    public TableDataInfo getListLogisticDelegate(@RequestBody PageAble<LogisticDelegated> param) {
    	startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
    	LogisticDelegated logisticDelegated = param.getData();
    	if (logisticDelegated == null) {
    		logisticDelegated = new LogisticDelegated();
    	}
    	logisticDelegated.setDelFlg(0);
    	List<LogisticDelegated> logisticDelegateds = logisticDelegatedService.selectLogisticDelegatedList(logisticDelegated);
    	return getDataTable(logisticDelegateds);
    }
    
    @PostMapping("/delegate")
    @ResponseBody
    public AjaxResult addLogisticDelegate(@RequestBody @Validated LogisticDelegated delegatedLogistic) {
    	return toAjax(logisticDelegatedService.insertLogisticDelegated(delegatedLogistic));
    }
    
    @PostMapping("/delegate/edit")
    @ResponseBody
    public AjaxResult updateLogisticDelegate(@RequestBody LogisticDelegated logisticDelegated) {
    	return toAjax(logisticDelegatedService.updateLogisticDelegated(logisticDelegated));
    }
    
    @DeleteMapping("/delegate/{id}/delete")
    @ResponseBody
    public AjaxResult deleteLogisticDelegated(@PathVariable("id") Long id) {
    	LogisticDelegated logisticDelegated = new LogisticDelegated();
    	logisticDelegated.setId(id);
    	logisticDelegated.setDelFlg(1);
    	return toAjax(logisticDelegatedService.updateLogisticDelegated(logisticDelegated));
    }
}
