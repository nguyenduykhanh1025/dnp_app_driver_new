package vn.com.irtech.eport.web.controller.gate;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;

@Controller
@RequestMapping("/gate/check")
public class GateCheckController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(GateCheckController.class);

	private final static String PREFIX = "gate/check";

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@GetMapping()
	public String getView() {
		return PREFIX + "/index";
	}

	@GetMapping("/job/info")
	@ResponseBody
	public AjaxResult getInfoByJobOrderNo(String jobOrderNo) {
		AjaxResult ajaxResult = AjaxResult.success();
		// Get shipment detail list by job order no
		ShipmentDetail shipmentDetailParam = new ShipmentDetail();
		shipmentDetailParam.setOrderNo(jobOrderNo);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetailParam);
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
			ajaxResult.put("shipmentId", shipmentDetails.get(0).getShipmentId());
		}
		ajaxResult.put("shipmentDetails", getDataTable(shipmentDetails));
		return ajaxResult;
	}

	@PostMapping("/pickup-assign/list")
	@ResponseBody
	public AjaxResult getPickupAssignList(String shipmentDetailIds, Long shipmentId, String jobOrderNo) {
		if (shipmentId == null) {
			return error("Không tìm thấy thông tin.");
		}

		return null;
	}
}
