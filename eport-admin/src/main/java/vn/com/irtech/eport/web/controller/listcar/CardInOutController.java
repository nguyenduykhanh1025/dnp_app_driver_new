package vn.com.irtech.eport.web.controller.listcar;

import java.util.Calendar;
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
import vn.com.irtech.eport.logistic.dto.EirGateDto;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
//@RequestMapping("/logistic/eir-gate")
@RequestMapping("/listCard/InOut") 
public class CardInOutController extends AdminBaseController {
	private final String PREFIX = "listCard";

	@Autowired
	private IPickupAssignService  pickupAssignService;
	
	@Autowired
	private IPickupHistoryService pickupHistoryService;
	
	@GetMapping()
	public String reportContainer() {
		return PREFIX + "/listCard";
	}
	
	 @PostMapping("/listCard")
	    @ResponseBody
		public TableDataInfo list(@RequestBody PageAble<PickupHistory> param) {
			startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy()); 
			PickupHistory PickupHistory = param.getData();
			 
			if (PickupHistory == null) {
				PickupHistory = new PickupHistory();
			} 
			
			
			
			Map<String, Object> params = PickupHistory.getParams();
			if (params == null) {
				params = new HashMap<>();
			}
			
			if (params.get("inOut") != null) {
				
				if ("I".equalsIgnoreCase((String) params.get("inOut"))) {	
					params.put("cardIn",  "I");
				}
				if ("O".equalsIgnoreCase((String) params.get("inOut"))) {								
					params.put("cardOut",  "O");
				}
			}
			
			PickupHistory.setParams(params);
			//List<PickupAssign> list1 = pickupAssignService.selectPickupAssignList(pickupAssign);
			
			List<PickupHistory> list =  pickupHistoryService.selectPickupHistoryList(PickupHistory);
			
			
	        return getDataTable(list);
	    }
}


