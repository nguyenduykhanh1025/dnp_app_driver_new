package vn.com.irtech.eport.carrier.controller;

import java.security.KeyPair;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.CarrierApi;
import vn.com.irtech.eport.carrier.service.ICarrierApiService;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.SignatureUtils;
import vn.com.irtech.eport.framework.util.ShiroUtils;

/**
 * Carrier api Controller
 * 
 * @author Trong Hieu
 * @date 2020-09-28
 */
@Controller
@RequestMapping("/carrier/api")
public class CarrierApiController extends BaseController {
	private String prefix = "carrier/api";

	@Autowired
	private ICarrierApiService carrierApiService;

	@Autowired
	private ICarrierGroupService carrierGroupService;

	@RequiresPermissions("carrier:api:view")
	@GetMapping()
	public String api() {
		return prefix + "/api";
	}

	@GetMapping("/getOprCode")
	@ResponseBody
	public AjaxResult lisOprCode() {
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("oprCode", carrierGroupService.getCarrierCode());
		return ajaxResult;
	}

	@RequiresPermissions("carrier:api:view")
	@GetMapping("/addApi")
	public String addApi() {
		return prefix + "/add";
	}

	/**
	 * Get carrier api List
	 */
	@RequiresPermissions("carrier:api:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(CarrierApi carrierApi) {
		startPage();
		List<CarrierApi> list = carrierApiService.selectCarrierApiList(carrierApi);
		return getDataTable(list);
	}

	/**
	 * Add carrier api
	 */
	@GetMapping("/add")
	public String add() {
		return prefix + "/add";
	}

	/**
	 * Add carrier api
	 */
	@RequiresPermissions("carrier:api:add")
	@Log(title = "Add Carrier API", businessType = BusinessType.INSERT)
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(CarrierApi carrierApi) {
		if(StringUtils.isBlank(carrierApi.getOprCode()) || carrierApi.getGroupId() == null) {
			return error("Vui lòng nhập các trường bắt buộc");
		}
		if(StringUtils.isNotBlank(carrierApi.getOprCode())) {
			for(String opr : carrierApi.getOprCode().split(",")) {
				if (carrierApiService.checkOprCodeExist(opr)) {
		            return error("OPR "+opr+" đã tồn tại. Vui lòng kiểm tra lại");
		        }
			}
		}
        // Gerenate private key and public key
        KeyPair keyPair = SignatureUtils.generateKeyPair();
        if (keyPair != null) {
        	carrierApi.setApiPrivateKey(SignatureUtils.encodeToString(keyPair.getPrivate()));
        	carrierApi.setApiPublicKey(SignatureUtils.encodeToString(keyPair.getPublic()));
        }
        
        carrierApi.setCreateBy(ShiroUtils.getSysUser().getUserName());
        
		return toAjax(carrierApiService.insertCarrierApi(carrierApi));
	}

	/**
	 * Update Carrier api
	 */
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		CarrierApi carrierApi = carrierApiService.selectCarrierApiById(id);
		mmap.put("carrierApi", carrierApi);
		return prefix + "/edit";
	}

	/**
	 * Update Save Carrier Api
	 */
	@RequiresPermissions("carrier:api:edit")
	@Log(title = "Update save carrier api", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(CarrierApi carrierApi) {
		return toAjax(carrierApiService.updateCarrierApi(carrierApi));
	}

	/**
	 * Renew API Key
	 */
	@RequiresPermissions("carrier:api:edit")
	@Log(title = "Renew Api Key", businessType = BusinessType.UPDATE)
	@PostMapping("/renew/{id}")
	@ResponseBody
	public AjaxResult reviewKey(@PathVariable("id") Long id) {
		CarrierApi api = carrierApiService.selectCarrierApiById(id);
		if(api == null) {
			return error("Không tìm thấy API. Vui lòng kiểm tra lại.");
		}
		// renew API

        // Gerenate private key and public key
        KeyPair keyPair = SignatureUtils.generateKeyPair();
        if (keyPair != null) {
        	api.setApiPrivateKey(SignatureUtils.encodeToString(keyPair.getPrivate()));
        	api.setApiPublicKey(SignatureUtils.encodeToString(keyPair.getPublic()));
        }
		return toAjax(carrierApiService.updateCarrierApi(api));
	}

	/**
	 * Delete Carrier Api
	 */
	@RequiresPermissions("carrier:api:remove")
	@Log(title = "Delete carrier api", businessType = BusinessType.DELETE)
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(carrierApiService.deleteCarrierApiByIds(ids));
	}
}
