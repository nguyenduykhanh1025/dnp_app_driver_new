package vn.com.irtech.eport.logistic.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

@Controller
@RequestMapping("/logistic/receiveContFull")
public class LogisticReceiveContFull extends LogisticBaseController {
	
	private final String prefix = "logistic/receiveContFull";

	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@GetMapping()
	public String receiveContFull() {
		return prefix + "/index";
	}

	@RequestMapping("/listShipment")
	@ResponseBody
	public TableDataInfo listShipment(Shipment shipment) {
		startPage();
		LogisticAccount user = getUser();
		shipment.setLogisticAccountId(user.getId());
		List<Shipment> shipments = shipmentService.selectShipmentList(shipment);
		return getDataTable(shipments);
	}

	@RequestMapping("/listShipmentDetail")
	@ResponseBody
	public List<ShipmentDetail> listShipmentDetail(ShipmentDetail shipmentDetail) {
		return shipmentDetailService.selectShipmentDetailList(shipmentDetail);
	}

	@GetMapping("/add")
	public String add(ModelMap mmap) {
		mmap.put("groupName", getGroup().getGroupName());
		return prefix + "/add";
	}

	@PostMapping("/addShipment")
    @ResponseBody
    public AjaxResult addShipment(Shipment shipment) {
		LogisticAccount user = getUser();
		shipment.setLogisticAccountId(user.getId());
		shipment.setLogisticGroupId(user.getGroupId());
		shipment.setCreateTime(new Date());
		shipment.setCreateBy(user.getFullName());
		shipment.setServiceId(1);
		if (shipmentService.insertShipment(shipment) == 1) {
			return success("Thêm lô thành công");
		}
		return error("Thêm lô thất bại");
	}

	@GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		Shipment shipment = shipmentService.selectShipmentWithGroupById(id);
		mmap.put("shipment", shipment);
        return prefix + "/edit";
	}
	
	@PostMapping("/editShipment")
    @ResponseBody
    public AjaxResult editShipment(Shipment shipment) {
		LogisticAccount user = getUser();
		shipment.setUpdateTime(new Date());
		shipment.setUpdateBy(user.getFullName());
		if (shipmentService.updateShipment(shipment) == 1) {
			return success("Chỉnh sửa lô thành công");
		}
		return error("Chỉnh sửa lô thất bại");
	}

	@PostMapping("/registerProcess")
	@ResponseBody
	public AjaxResult registerProcess(@RequestBody List<ShipmentDetail> shipmentDetails) {
		if (shipmentDetails != null) {
			LogisticAccount user = getUser();
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setCreateBy(user.getFullName());
				shipmentDetail.setCreateTime(new Date());
				shipmentDetail.setProcessStatus("Y");
				// unknow
				shipmentDetail.setRegisterNo("unknow");
				//
				if (shipmentDetailService.insertShipmentDetail(shipmentDetail) != 1) {
					return error("Đăng ký làm lệnh thất bại từ container: " + shipmentDetail.getContainerNo());
				}
			}
		}
		return success("Đăng ký làm lệnh thành công");
	}
}
