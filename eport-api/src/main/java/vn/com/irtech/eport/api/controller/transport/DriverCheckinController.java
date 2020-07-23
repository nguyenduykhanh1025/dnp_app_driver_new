package vn.com.irtech.eport.api.controller.transport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.eport.api.form.CheckinForm;
import vn.com.irtech.eport.api.service.transport.IDriverCheckinService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;

@RestController
@RequestMapping("/transport/checkin")
public class DriverCheckinController extends BaseController  {
	
	@Autowired
	private IDriverCheckinService driverCheckinService;
	
	@PostMapping("")
	@ResponseBody
	public AjaxResult checkin(@Valid @RequestBody CheckinForm req) throws Exception{
		
		AjaxResult ajaxResult = AjaxResult.success();
		
		ajaxResult.put("qrString", driverCheckinService.checkin(req));
		
		return ajaxResult;
	}
	
	
}
