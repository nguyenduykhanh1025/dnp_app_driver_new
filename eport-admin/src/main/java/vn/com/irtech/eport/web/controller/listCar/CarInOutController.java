package vn.com.irtech.eport.web.controller.listCar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.TruckEntrance;
import vn.com.irtech.eport.logistic.dto.EirGateDto;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.ITruckEntranceService;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
//@RequestMapping("/logistic/eir-gate")
@RequestMapping("/listCar/InOut")
public class CarInOutController extends AdminBaseController {
	private final String PREFIX = "listCar/inOut";

	@Autowired
	private ITruckEntranceService truckEntranceService;

	@GetMapping()
	public String reportContainer() {
		return PREFIX + "/listCar";
	}
	
	@PostMapping("/listCar")
	@ResponseBody
	public TableDataInfo list(@RequestBody PageAble<TruckEntrance> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		TruckEntrance truckEntrance = param.getData();
		List<TruckEntrance> truckEntrancesResult = new ArrayList<>();
		
		// search only truck no exist
		if (!truckEntrance.getTruckNo().equals("ALL")) {
			truckEntrancesResult = this.truckEntranceService.selectTruckEntranceFollowTruckNoList(truckEntrance);
		} else {
			truckEntrancesResult = this.truckEntranceService.selectTruckEntranceList(truckEntrance);
		}
		return getDataTable(truckEntrancesResult);
	}
}
