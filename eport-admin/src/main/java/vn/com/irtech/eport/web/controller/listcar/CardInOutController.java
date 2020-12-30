package vn.com.irtech.eport.web.controller.listcar;

import java.util.List;
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
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
//@RequestMapping("/logistic/eir-gate")
@RequestMapping("/listCard/InOut") 
public class CardInOutController extends AdminBaseController {
	private final String PREFIX = "listCard";

	@Autowired
	private IPickupAssignService  pickupAssignService;
	
	@GetMapping()
	public String reportContainer() {
		return PREFIX + "/listCard";
	}
	
	 @PostMapping("/listCard")
	    @ResponseBody
		public TableDataInfo list(@RequestBody PageAble<PickupAssign> param) {
			startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy()); 
			PickupAssign pickupAssign = param.getData();
			if (pickupAssign == null) {
				pickupAssign = new PickupAssign();
			} 
			
			
			List<PickupAssign> list = pickupAssignService.selectPickupAssignList(pickupAssign);
			
			
	        return getDataTable(list);
	    }
}


