package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
		shipment.setLogisticGroupId(user.getGroupId());
		List<Shipment> shipments = shipmentService.selectShipmentList(shipment);
		return getDataTable(shipments);
	}

	@RequestMapping("/listShipmentDetail")
	@ResponseBody
	public List<ShipmentDetail> listShipmentDetail(ShipmentDetail shipmentDetail) {
		Shipment shipment = shipmentService.selectShipmentById(shipmentDetail.getShipmentId());
		if (verifyPermission(shipment.getLogisticGroupId())) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			if (shipment.getEdoFlg().equals("1") && shipmentDetails.size() == 0) { 
				shipmentDetails = new ArrayList<>();
				for (int i=0; i<10; i++) {
					ShipmentDetail shipmentDetail2 = new ShipmentDetail();
					shipmentDetail2.setBlNo(shipment.getBlNo());
					shipmentDetail2.setContainerNo("CONT123456"+i);
					shipmentDetail2.setOpeCode("CMC");
					shipmentDetail2.setSztp("22G0");
					shipmentDetail2.setFe("F");
					shipmentDetail2.setConsignee("VINCOSHIP");
					shipmentDetail2.setSealNo("G8331306");
					shipmentDetail2.setExpiredDem(new Date());
					shipmentDetail2.setWgt(11l);
					shipmentDetail2.setVslNm("Vessel");
					shipmentDetail2.setVoyNo("Voyage");
					shipmentDetail2.setLoadingPort("LoadingPort");
					shipmentDetail2.setDischargePort("dischargePort");
					shipmentDetail2.setTransportType("Truck");
					shipmentDetail2.setEmptyDepot("emptyDepot");
					shipmentDetail2.setCustomStatus("N");
					shipmentDetail2.setPaymentStatus("N");
					shipmentDetail2.setProcessStatus("N");
					shipmentDetail2.setDoStatus("N");
					shipmentDetail2.setUserVerifyStatus("N");
					shipmentDetail2.setStatus(1);
					shipmentDetails.add(shipmentDetail2);
				}
			}
			return shipmentDetails;
		}
		return null;
	}

	@GetMapping("/addShipmentForm")
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

	@GetMapping("/editShipmentForm/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
		Shipment shipment = shipmentService.selectShipmentWithGroupById(id);
		if (verifyPermission(shipment.getLogisticGroupId())) {
			mmap.put("shipment", shipment);
		}
        return prefix + "/edit";
	}
	
	@PostMapping("/editShipment")
    @ResponseBody
    public AjaxResult editShipment(Shipment shipment) {
		LogisticAccount user = getUser();
		Shipment referenceShipment = shipmentService.selectShipmentById(shipment.getId());
		if (verifyPermission(referenceShipment.getLogisticGroupId())) {
			shipment.setUpdateTime(new Date());
			shipment.setUpdateBy(user.getFullName());
			if (shipmentService.updateShipment(shipment) == 1) {
				return success("Chỉnh sửa lô thành công");
			}
		}
		return error("Chỉnh sửa lô thất bại");
	}

	@PostMapping("/saveShipmentDetail")
	@ResponseBody
	public AjaxResult saveShipmentDetail(@RequestBody List<ShipmentDetail> shipmentDetails) {
		if (shipmentDetails != null) {
			LogisticAccount user = getUser();
			int index = 0;
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				index++;
				if (shipmentDetail.getId() != null && shipmentDetail.getStatus() == 1) {
					if (shipmentDetail.getContainerNo() == null || shipmentDetail.getContainerNo().equals("")) {
						shipmentDetailService.deleteShipmentDetailById(shipmentDetail.getId());
					} else {
						shipmentDetail.setUpdateBy(user.getFullName());
						shipmentDetail.setUpdateTime(new Date());
						if (shipmentDetailService.updateShipmentDetail(shipmentDetail) != 1) {
							return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
						}
					}
				} else if (shipmentDetail.getId() == null) {
					shipmentDetail.setLogisticGroupId(user.getGroupId());
					shipmentDetail.setCreateBy(user.getFullName());
					shipmentDetail.setCreateTime(new Date());
					shipmentDetail.setRegisterNo(shipmentDetail.getShipmentId().toString()+index);
					shipmentDetail.setStatus(1);
					shipmentDetail.setCustomStatus("N");
					shipmentDetail.setPaymentStatus("N");
					shipmentDetail.setProcessStatus("N");
					shipmentDetail.setDoStatus("N");
					shipmentDetail.setPreorderPickup("N");
					if (shipmentDetailService.insertShipmentDetail(shipmentDetail) != 1) {
						return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
					}
				}
			}
			return success("Lưu khai báo thành công");
		}
		return error("Lưu khai báo thất bại");
	}

	@PostMapping("/getContInfo")
	@ResponseBody
	public ShipmentDetail getContInfo(ShipmentDetail shipmentDetail) {
		if (shipmentDetail.getBlNo() != null && shipmentDetail.getContainerNo() != null) {
			shipmentDetail.setOpeCode("CMC");
			shipmentDetail.setSztp("22G0");
			shipmentDetail.setFe("F");
			shipmentDetail.setConsignee("VINCOSHIP");
			shipmentDetail.setSealNo("G8331306");
			shipmentDetail.setExpiredDem(new Date());
			shipmentDetail.setWgt(11l);
			shipmentDetail.setVslNm("Vessel");
			shipmentDetail.setVoyNo("Voyage");
			shipmentDetail.setLoadingPort("LoadingPort");
			shipmentDetail.setDischargePort("dischargePort");
			shipmentDetail.setTransportType("Truck");
			shipmentDetail.setEmptyDepot("emptyDepot");
			shipmentDetail.setCustomStatus("N");
			shipmentDetail.setPaymentStatus("N");
			shipmentDetail.setProcessStatus("N");
			shipmentDetail.setDoStatus("N");
			shipmentDetail.setUserVerifyStatus("N");
			shipmentDetail.setStatus(1);
			shipmentDetail.setRemark("Ghi chu");
			return shipmentDetail;
		} else {
			return null;
		}
	}

	@GetMapping("checkCustomStatusForm/{shipmentId}")
	public String checkCustomStatus(@PathVariable("shipmentId") Long shipmentId, ModelMap mmap) {
		mmap.put("shipmentId", shipmentId);
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		shipmentDetail.setStatus(1);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		if (shipmentDetails.size() > 0) {
			if (verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
				mmap.put("contList", shipmentDetails);
			}
		}
		return prefix + "/checkCustomStatus";
	}

	@PostMapping("/checkCustomStatus")
	@ResponseBody
	public List<ShipmentDetail> checkCustomStatus(@RequestParam(value="declareNoList[]") String[] declareNoList, String shipmentDetailIds) {
		if (declareNoList != null) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
			if (shipmentDetails.size() > 0) {
				if (verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
					Random random = new Random();
					for (ShipmentDetail shipmentDetail : shipmentDetails) {
						if (random.nextBoolean()) {
							shipmentDetail.setStatus(2);
							shipmentDetail.setCustomStatus("R");
							shipmentDetailService.updateShipmentDetail(shipmentDetail);
						}
					}
					return shipmentDetails;
				}
			}
		}
		return null;
	}

	@PostMapping("/updateShipmentDetailStatus")
	@ResponseBody
	public AjaxResult updateShipmentDetailStatus(ShipmentDetail shipmentDetail) {
		shipmentDetail.setUpdateBy(getUser().getFullName());
		shipmentDetail.setUpdateTime(new Date());
		if (shipmentDetailService.updateShipmentDetailStatus(shipmentDetail) == 1) {
			return success("Cập nhật trạng thái thành công");
		}
		return error("Cập nhật trạng thái thất bại");
	}

	@GetMapping("checkContListBeforeVerify/{shipmentDetailIds}")
	public String checkContListBeforeVerify(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		return prefix + "/checkContListBeforeVerify";
	}

	@RequestMapping("/listShipmentDetailByIds")
	@ResponseBody
	public List<ShipmentDetail> listShipmentDetailByIds(String shipmentDetailIds) {
		return 	shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
	}

	@GetMapping("verifyOtpForm/{shipmentDetailIds}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("numberPhone", "0912312312");
		return prefix + "/verifyOtp";
	}

	@PostMapping("/verifyOtp")
	@ResponseBody
	public AjaxResult verifyOtp(String shipmentDetailIds, String otp) {
		if (otp.equals("1234")) {
			try {
				String[] ids = shipmentDetailIds.split(",");
				for (String id : ids) {
					ShipmentDetail shipmentDetail = new ShipmentDetail();
					shipmentDetail.setId(Long.parseLong(id));
					shipmentDetail.setUserVerifyStatus("Y");
					shipmentDetail.setStatus(3);
					shipmentDetail.setProcessStatus("Y");
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
				}
				return success("Xác thực OTP thành công");
			} catch (Exception e) {
				return error("cõ lỗi xảy ra trong hệ thống!");
			}
		}
		return error("Mã OTP không chính xác!");
	}

	@GetMapping("pickContOnDemand/{billNo}/{shipmentId}")
	public String pickContOnDemand(@PathVariable("billNo") String billNo, @PathVariable("shipmentId") long shipmentId, ModelMap mmap) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		shipmentDetail.setBlNo(billNo);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		int shipmentDetailSize = shipmentDetails.size();
		ShipmentDetail[][] shipmentDetailMatrix = new ShipmentDetail[5][7];
		int index = 0;
		if (shipmentDetailSize > 0) {
			for (int col=0; col<7; col++) {
				for (int row=0; row<5; row++) {
					if (index <= (shipmentDetailSize-1)) {
						shipmentDetails.get(index).setRow(col);
						shipmentDetails.get(index).setTier(row);
						shipmentDetailMatrix[row][col] = shipmentDetails.get(index);
						index++;
					}
				}
			}
		}
		mmap.put("containerList", shipmentDetailMatrix);
		mmap.put("unitCosts", 20000);
		mmap.put("billNo", billNo);
		mmap.put("shipmentId", shipmentId);
		return prefix + "/pickContOnDemand";
	}

	@PostMapping("/setMovingContPrice")
	@ResponseBody
	public AjaxResult setMovingContPrice(@RequestParam(value="preorderPickupContIds[]") int[] preorderPickupContIds, String billNo, long shipmentId) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setBlNo(billNo);
		shipmentDetail.setShipmentId(shipmentId);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		if (shipmentDetails.size() > 0) {
			for (ShipmentDetail shipmentDetail2 : shipmentDetails) {
				for(int id : preorderPickupContIds) {
					if (id == shipmentDetail2.getId()) {
						shipmentDetail2.setPreorderPickup("Y");
						break;
					}
				}
				shipmentDetail2.setStatus(4);
				if (shipmentDetailService.updateShipmentDetail(shipmentDetail2) != 1) {
					return error("Có lỗi xảy ra trong quá trình bốc container chỉ định!");
				}
			}
		}
		return success("Bốc container chỉ định thành công");
	}

	@GetMapping("paymentForm/{shipmentId}")
	public String paymentForm(@PathVariable("shipmentId") long shipmentId, ModelMap mmap) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		shipmentDetail.setStatus(4);
		shipmentDetail.setPreorderPickup("Y");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		if (shipmentDetails.size() > 0) {
			mmap.put("moveContAmount", shipmentDetails.size());
		} else {
			mmap.put("moveContAmount", 0);
		}
		mmap.put("totalCosts", 100000000l);
		mmap.put("unitCosts", 20000);
		mmap.put("shipmentId", shipmentId);
		return prefix + "/paymentForm";
	}

	@PostMapping("/payment")
	@ResponseBody
	public AjaxResult payment(long shipmentId) {
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		shipmentDetail.setStatus(5);
		shipmentDetail.setPaymentStatus("Y");
		updateShipmentDetailStatus(shipmentDetail);
		return success("Thanh toán thành công");
	}

	@GetMapping("pickTruckForm/{shipmentId}/{pickCont}")
	public String pickTruckForm(@PathVariable("shipmentId") long shipmentId, @PathVariable("pickCont") boolean pickCont, ModelMap mmap) {
		mmap.put("shipmentId", shipmentId);
		mmap.put("pickCont", pickCont);
		return prefix + "/pickTruckForm";
	}

	@PostMapping("/pickTruck")
	@ResponseBody
	public AjaxResult pickTruck(long shipmentId, @RequestParam(value="driverIds[]") int[] driverIds) {
		String ids = "";
		for (int i : driverIds) {
			ids += i+",";
		}
		ids = ids.substring(0, ids.length()-1);
		return success("Điều xe thành công");
	}
}
