package vn.com.irtech.eport.carrier.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.system.service.ISysConfigService;

/**
 * 
 * @author admin
 */
@Controller
public class CarrierIndexController extends CarrierBaseController {

	@Autowired
	private ISysConfigService configService;

	@Autowired
	private IEdoService edoService;

	@GetMapping("/index")
	public String index(ModelMap mmap) {
		CarrierAccount user = ShiroUtils.getSysUser();
		mmap.put("user", user);
		mmap.put("sideTheme", configService.selectConfigByKey("sys.index.sideTheme"));
		mmap.put("skinName", configService.selectConfigByKey("sys.index.skinName"));
		mmap.put("appName", Global.getName());
		mmap.put("appVersion", Global.getVersion());
		mmap.put("copyrightYear", Global.getCopyrightYear());
		// permission to hide/show menu
		mmap.put("doPermission", hasDoPermission());
		mmap.put("edoPermission", hasEdoPermission());
		mmap.put("bookingPermission", hasBookingPermission());
		mmap.put("depoPermission", hasDepoPermission());
		return "index";
	}

	@GetMapping("/switchSkin")
	public String switchSkin(ModelMap mmap) {
		return "skin";
	}

	@GetMapping("/main")
	public String main(ModelMap mmap) {
		CarrierAccount user = ShiroUtils.getSysUser();
		Map<String, String> report = edoService.getReportByCarrierGroup(super.getGroupCodes().toString().split(","));
		if (report == null) {
			report = new HashMap<>();
			report.put("totalBl", "0");
			report.put("totalCont", "0");
			report.put("completedBl", "0");
			report.put("waitingBl", "0");
		}
		mmap.put("report", report);
		mmap.put("user", user);
		mmap.put("version", Global.getVersion());
		return "main";
	}

}
