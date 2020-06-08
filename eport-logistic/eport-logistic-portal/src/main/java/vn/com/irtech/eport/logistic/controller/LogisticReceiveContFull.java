package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.springframework.web.client.RestTemplate;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.logistic.utils.R;

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
		shipment.setServiceId(1);
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
				// for (int i=0; i<10; i++) {
				// 	ShipmentDetail shipmentDetail2 = new ShipmentDetail();
				// 	shipmentDetail2.setBlNo(shipment.getBlNo());
				// 	shipmentDetail2.setContainerNo("CONT123456"+i);
				// 	shipmentDetail2.setOpeCode("CMC");
				// 	shipmentDetail2.setSztp("22G0");
				// 	shipmentDetail2.setFe("F");
				// 	shipmentDetail2.setConsignee("VINCOSHIP");
				// 	shipmentDetail2.setSealNo("G8331306");
				// 	shipmentDetail2.setExpiredDem(new Date());
				// 	shipmentDetail2.setWgt(11l);
				// 	shipmentDetail2.setVslNm("Vessel");
				// 	shipmentDetail2.setVoyNo("Voyage");
				// 	shipmentDetail2.setLoadingPort("LoadingPort");
				// 	shipmentDetail2.setDischargePort("dischargePort");
				// 	shipmentDetail2.setTransportType("Truck");
				// 	shipmentDetail2.setEmptyDepot("emptyDepot");
				// 	shipmentDetail2.setCustomStatus("N");
				// 	shipmentDetail2.setPaymentStatus("N");
				// 	shipmentDetail2.setProcessStatus("N");
				// 	shipmentDetail2.setDoStatus("N");
				// 	shipmentDetail2.setUserVerifyStatus("N");
				// 	shipmentDetail2.setStatus(1);
				// 	shipmentDetails.add(shipmentDetail2);
				// }
				final String url = Global.getApiUrl() + "/shipmentDetail/list";
				ShipmentDetail shipDetail = new ShipmentDetail();
				shipDetail.setBlNo(shipment.getBlNo());
				RestTemplate restTemplate = new RestTemplate();
				R r = restTemplate.postForObject( url, shipDetail, R.class);
				shipmentDetails = (List<ShipmentDetail>) r.get("data");
				//return shipmentDetails;
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
				if (shipmentDetail.getId() != null) {
					if (shipmentDetail.getContainerNo() == null || shipmentDetail.getContainerNo().equals("")) {
						shipmentDetailService.deleteShipmentDetailById(shipmentDetail.getId());
					} else {
						shipmentDetail.setUpdateBy(user.getFullName());
						shipmentDetail.setUpdateTime(new Date());
						if (shipmentDetailService.updateShipmentDetail(shipmentDetail) != 1) {
							return error("Lưu khai báo thất bại từ container: " + shipmentDetail.getContainerNo());
						}
					}
				} else {
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
			// shipmentDetail.setOpeCode("CMC");
			// shipmentDetail.setSztp("22G0");
			// shipmentDetail.setFe("F");
			// shipmentDetail.setConsignee("VINCOSHIP");
			// shipmentDetail.setSealNo("G8331306");
			// shipmentDetail.setExpiredDem(new Date());
			// shipmentDetail.setWgt(11l);
			// shipmentDetail.setVslNm("Vessel");
			// shipmentDetail.setVoyNo("Voyage");
			// shipmentDetail.setLoadingPort("LoadingPort");
			// shipmentDetail.setDischargePort("dischargePort");
			// shipmentDetail.setTransportType("Truck");
			// shipmentDetail.setEmptyDepot("emptyDepot");
			// shipmentDetail.setCustomStatus("N");
			// shipmentDetail.setPaymentStatus("N");
			// shipmentDetail.setProcessStatus("N");
			// shipmentDetail.setDoStatus("N");
			// shipmentDetail.setUserVerifyStatus("N");
			// shipmentDetail.setStatus(1);
			// shipmentDetail.setRemark("Ghi chu");
			String url = Global.getApiUrl() + "/shipmentDetail/containerInfor";
			RestTemplate restTemplate = new RestTemplate();
			R r = restTemplate.postForObject(url, shipmentDetail, R.class);
			// List<ShipmentDetail> l = (List<ShipmentDetail>) r.get("data");
			ObjectMapper mapper = new ObjectMapper();
			ShipmentDetail aShipmentDetail = mapper.convertValue(r.get("data"), ShipmentDetail.class);
			return aShipmentDetail;
			
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

	@GetMapping("checkContListBeforeVerify/{shipmentDetailIds}")
	public String checkContListBeforeVerify(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			mmap.put("shipmentDetails", shipmentDetails);
		}
		return prefix + "/checkContListBeforeVerify";
	}

	// @RequestMapping("/listShipmentDetailByIds")
	// @ResponseBody
	// public List<ShipmentDetail> listShipmentDetailByIds(String shipmentDetailIds) {
	// 	return 	shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
	// }

	@GetMapping("verifyOtpForm/{shipmentDetailIds}")
	public String verifyOtpForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		mmap.put("numberPhone", getGroup().getMobilePhone());
		return prefix + "/verifyOtp";
	}

	@PostMapping("/verifyOtp")
	@ResponseBody
	public AjaxResult verifyOtp(String shipmentDetailIds, String otp) {
		if (otp.equals("1234")) {
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
			if (shipmentDetails.size() >0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
				for (ShipmentDetail shipmentDetail : shipmentDetails) {
					shipmentDetail.setUserVerifyStatus("Y");
					shipmentDetail.setStatus(3);
					shipmentDetail.setProcessStatus("Y");
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
				}
				return success("Xác thực OTP thành công");
			}
		}
		return error("Mã OTP không chính xác!");
	}

	@GetMapping("pickContOnDemandForm/{blNo}")
	public String pickContOnDemand(@PathVariable("blNo") String blNo, ModelMap mmap) {
		ShipmentDetail shipmentDt = new ShipmentDetail();
		shipmentDt.setBlNo(blNo);
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDt);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			// simulating the location of container in da nang port, mapping to matrix
			List<ShipmentDetail[][]> bayList= new ArrayList<>();
			String bay1 = "AB123";
			int row1 = 1;
			int tier1 = 1;
			String bay2 = "AB124";
			int row2 = 1;
			int tier2 = 1;
			boolean next = true;
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if (next) {
					if (tier1 == 6) {
						tier1 = 1;
						row1++;
					}
					shipmentDetail.setBay(bay1);
					shipmentDetail.setRow(row1);
					shipmentDetail.setTier(tier1++);
					next = false;
				} else {
					if (tier2 == 6) {
						tier2 = 1;
						row2++;
					}
					shipmentDetail.setBay(bay2);
					shipmentDetail.setRow(row2);
					shipmentDetail.setTier(tier2++);
					next = true;
				}
			}

			// Mapping container to matrix by location row, tier, bay
			Collections.sort(shipmentDetails, new BayComparator());
			ShipmentDetail[][] shipmentDetailMatrix = new ShipmentDetail[5][6];
			String currentBay = shipmentDetails.get(0).getBay();
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				if (currentBay.equals(shipmentDetail.getBay())) {
					shipmentDetailMatrix[shipmentDetail.getTier()-1][shipmentDetail.getRow()-1] = shipmentDetail;
				} else {
					bayList.add(shipmentDetailMatrix);
					currentBay = shipmentDetail.getBay();
					shipmentDetailMatrix = new ShipmentDetail[5][6];
					shipmentDetailMatrix[shipmentDetail.getTier()-1][shipmentDetail.getRow()-1] = shipmentDetail;
				}
			}
			bayList.add(shipmentDetailMatrix);

			mmap.put("bayList", bayList);
			mmap.put("unitCosts", 20000);
		}
		return prefix + "/pickContOnDemand";
	}

	@PostMapping("/pickContOnDemand")
	@ResponseBody
	public AjaxResult pickContOnDemand(@RequestBody List<ShipmentDetail> preorderPickupConts) {
		if (preorderPickupConts.size() > 0) {
			ShipmentDetail shipmentDt = new ShipmentDetail();
			shipmentDt.setBlNo(preorderPickupConts.get(0).getBlNo());
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDt);
			if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
				// simulating the location of container in da nang port
				List<ShipmentDetail[][]> bayList= new ArrayList<>();
				String bay1 = "AB123";
				int row1 = 1;
				int tier1 = 1;
				String bay2 = "AB124";
				int row2 = 1;
				int tier2 = 1;
				boolean next = true;
				for (ShipmentDetail shipmentDetail : shipmentDetails) {
					if (next) {
						if (tier1 == 6) {
							tier1 = 1;
							row1++;
						}
						shipmentDetail.setBay(bay1);
						shipmentDetail.setRow(row1);
						shipmentDetail.setTier(tier1++);
						next = false;
					} else {
						if (tier2 == 6) {
							tier2 = 1;
							row2++;
						}
						shipmentDetail.setBay(bay2);
						shipmentDetail.setRow(row2);
						shipmentDetail.setTier(tier2++);
						next = true;
					}
				}

				// Mapping container to matrix by location row, tier, bay
				Collections.sort(shipmentDetails, new BayComparator());
				ShipmentDetail[][] shipmentDetailMatrix = new ShipmentDetail[5][6];
				String currentBay = shipmentDetails.get(0).getBay();
				for (ShipmentDetail shipmentDetail : shipmentDetails) {
					if (currentBay.equals(shipmentDetail.getBay())) {
						shipmentDetailMatrix[shipmentDetail.getTier()-1][shipmentDetail.getRow()-1] = shipmentDetail;
					} else {
						bayList.add(shipmentDetailMatrix);
						currentBay = shipmentDetail.getBay();
						shipmentDetailMatrix = new ShipmentDetail[5][6];
						shipmentDetailMatrix[shipmentDetail.getTier()-1][shipmentDetail.getRow()-1] = shipmentDetail;
					}
				}
				bayList.add(shipmentDetailMatrix);

				int movingContAmount = 0;
				for (int b=0; b<bayList.size(); b++) {
					int movingContAmountTemp = 0;
					for (int row=0; row<6; row++) {
						for (int tier=4; tier>=0; tier--) {
							if (bayList.get(b)[tier][row] != null) {
								for (ShipmentDetail shipmentDetail : preorderPickupConts) {
									if (bayList.get(b)[tier][row].getId() == shipmentDetail.getId()) {
										movingContAmount += movingContAmountTemp;
										movingContAmountTemp = 0;
										break;
									}
								}
								movingContAmountTemp++;
							}
						}
					}
				}

				for (ShipmentDetail shipmentDetail : preorderPickupConts) {
					shipmentDetail.setMovingContAmount(movingContAmount);
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
				}

				return success("Bốc container chỉ định thành công.");
			}
		}
		return error("Có lỗi xảy ra trong quá trình bốc container chỉ định!");
	}

	@GetMapping("paymentForm/{shipmentDetailIds}")
	public String paymentForm(@PathVariable("shipmentDetailIds") String shipmentDetailIds, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		return prefix + "/paymentForm";
	}

	@PostMapping("/payment")
	@ResponseBody
	public AjaxResult payment(String shipmentDetailIds) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setStatus(4);
				shipmentDetail.setPaymentStatus("Y");
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			return success("Thanh toán thành công");
		}
		return error("Có lỗi xảy ra trong quá trình thanh toán.");
	}

	@GetMapping("pickTruckForm/{shipmentId}/{pickCont}/{shipmentDetailId}")
	public String pickTruckForm(@PathVariable("shipmentId") long shipmentId, @PathVariable("pickCont") boolean pickCont,@PathVariable("shipmentDetailId") Integer shipmentDetailId, ModelMap mmap) {
		mmap.put("shipmentId", shipmentId);
		mmap.put("pickCont", pickCont);
		mmap.put("shipmentDetailId", shipmentDetailId);
		String transportId = "";
		String shipmentIds = "";
		if (!pickCont) {
			ShipmentDetail shipmentDetail = new ShipmentDetail();
			shipmentDetail.setShipmentId(shipmentId);
			shipmentDetail.setLogisticGroupId(getUser().getGroupId());
			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
			for (ShipmentDetail shipmentDetail2 : shipmentDetails) {
				if (shipmentDetail2.getPreorderPickup() == null || !shipmentDetail2.getPreorderPickup().equals("Y")) {
					shipmentIds += shipmentDetail2.getId() + ",";
					if (shipmentDetail2.getTransportIds() != null && transportId.length() == 0) {
						transportId = shipmentDetail2.getTransportIds();
					}
				}
			}
		}
		mmap.put("transportIds", transportId);
		mmap.put("shipmentDetailIds", shipmentIds);
		return prefix + "/pickTruckForm";
	}

	@PostMapping("/pickTruck")
	@ResponseBody
	public AjaxResult pickTruck(String shipmentDetailIds, String driverIds) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				shipmentDetail.setTransportIds(driverIds);
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			return success("Điều xe thành công");
		}
		return error("Xảy ra lỗi trong quá trình điều xe.");
	}

	class BayComparator implements Comparator<ShipmentDetail> {
		public int compare(ShipmentDetail shipmentDetail1, ShipmentDetail shipmentDetail2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return shipmentDetail1.getBay().compareTo(shipmentDetail2.getBay());
		}
	}
}

