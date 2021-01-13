package vn.com.irtech.eport.web.controller.listCar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.TruckEntrance;
import vn.com.irtech.eport.logistic.dto.EirGateDto;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.logistic.service.IPickupHistoryService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.ITruckEntranceService;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/listCar/pickupHistories")
public class PickupHistoriesController extends AdminBaseController {
	private final String PREFIX = "listCar/pickupHistories";

	@Autowired
	private IPickupHistoryService pickupHistoryService;

	@Autowired
	private ILogisticGroupService logisticGroupService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@Autowired
	private IPickupAssignService pickupAssignService;

	@GetMapping()
	public String reportContainer() {
		return PREFIX + "/index";
	}

	@GetMapping("/add")
	public String reportAddForm(ModelMap mmap) {
		mmap.put("logisticGroups", this.logisticGroupService.selectLogisticGroupList(null));
		return PREFIX + "/add";
	}

	@GetMapping("/edit/{id}")
	public String reportEditForm(ModelMap mmap, @PathVariable("id") Long id) {

		PickupHistory pickupHistoryFromDB = this.pickupHistoryService.selectPickupHistoryById(id);

		mmap.put("logisticGroups", this.logisticGroupService.selectLogisticGroupList(null));
		mmap.put("pickupHistory", pickupHistoryFromDB);

		return PREFIX + "/edit";
	}

	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(@RequestBody PageAble<PickupHistory> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		PickupHistory pickupHistory = param.getData();
		return getDataTable(this.pickupHistoryService.selectPickupHistoryListForHistory(pickupHistory));
	}

	@PostMapping("/add")
	@ResponseBody
	public AjaxResult add(@RequestBody ShipmentDetail shipmentDetail) {

		List<ShipmentDetail> listShipmentDetailFromDB = this.shipmentDetailService
				.selectShipmentDetailList(shipmentDetail);
		if (listShipmentDetailFromDB.size() == 0) {
			return error("Container không tồn tại");
		}

		ShipmentDetail shipmentDetailFromDB = listShipmentDetailFromDB.get(0);

		// TODO: pickup assign id: shipmentId, logisticGroupId, serviceType, sdt

		PickupAssign pickupAssign = new PickupAssign();
		pickupAssign.setShipmentId(shipmentDetailFromDB.getShipmentId());
		pickupAssign.setLogisticGroupId(shipmentDetail.getLogisticGroupId());
		pickupAssign.setServiceType(shipmentDetailFromDB.getServiceType());
		pickupAssign.setPhoneNumber(shipmentDetailFromDB.getUserMobilePhone());

		List<PickupAssign> listPickupAssignFromDB = this.pickupAssignService.selectPickupAssignList(pickupAssign);
		if (listPickupAssignFromDB.size() == 0) {
			return error("Pickup assign không tồn tại");
		}

		return success();
	}
	
	@PutMapping("/edit")
	@ResponseBody
	public AjaxResult edit(@RequestBody ShipmentDetail shipmentDetail) {

		List<ShipmentDetail> listShipmentDetailFromDB = this.shipmentDetailService
				.selectShipmentDetailList(shipmentDetail);
		if (listShipmentDetailFromDB.size() == 0) {
			return error("Container không tồn tại");
		}

		ShipmentDetail shipmentDetailFromDB = listShipmentDetailFromDB.get(0);

		// TODO: pickup assign id: shipmentId, logisticGroupId, serviceType, sdt

		PickupAssign pickupAssign = new PickupAssign();
		pickupAssign.setShipmentId(shipmentDetailFromDB.getShipmentId());
		pickupAssign.setLogisticGroupId(shipmentDetail.getLogisticGroupId());
		pickupAssign.setServiceType(shipmentDetailFromDB.getServiceType());
		pickupAssign.setPhoneNumber(shipmentDetailFromDB.getUserMobilePhone());

		List<PickupAssign> listPickupAssignFromDB = this.pickupAssignService.selectPickupAssignList(pickupAssign);
		if (listPickupAssignFromDB.size() == 0) {
			return error("Pickup assign không tồn tại");
		}

		return success();
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	public AjaxResult deleteById(@PathVariable("id") Long id) {
		this.pickupHistoryService.deletePickupHistoryById(id);
		return AjaxResult.success();
	}
}
