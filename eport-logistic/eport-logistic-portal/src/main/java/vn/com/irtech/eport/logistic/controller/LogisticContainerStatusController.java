package vn.com.irtech.eport.logistic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;

@Controller
@RequestMapping("/logistic/container/status")
public class LogisticContainerStatusController extends LogisticBaseController {
	
	private final String PREFIX = "logistic/container/status";
	
	@Autowired
	private IShipmentDetailService shipmentDetailService;
	
	@GetMapping()
	public String reportContainer() {
		return PREFIX + "/index";
	}
	
	@GetMapping("/list")
	@ResponseBody
	public TableDataInfo listShipment() {
		startPage();
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setLogisticGroupId(super.getGroup().getId());
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		return getDataTable(shipmentDetails);
	}
}
