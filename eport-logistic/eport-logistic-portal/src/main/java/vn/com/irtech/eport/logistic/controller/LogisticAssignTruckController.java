package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.domain.DriverTruck;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.LogisticTruck;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IDriverAccountService;
import vn.com.irtech.eport.logistic.service.IDriverTruckService;
import vn.com.irtech.eport.logistic.service.ILogisticTruckService;
import vn.com.irtech.eport.logistic.service.IPickupAssignService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;

@Controller
@RequestMapping("/logistic/assignTruck")
public class LogisticAssignTruckController extends LogisticBaseController{

	private final String PREFIX = "logistic/assignTruck";
	
	@Autowired
	private IShipmentService shipmentService;

	@Autowired
	private IShipmentDetailService shipmentDetailService;
	
	@Autowired
	private IDriverAccountService driverAccountService;
	
	@Autowired
	private IPickupAssignService pickupAssignService;

	@Autowired
	private IDriverTruckService driverTruckService;
	
	@Autowired
	private ILogisticTruckService logisticTruckService;

	@GetMapping
    public String assignTruck() {
    	return PREFIX + "/assignTruck";
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

	@RequestMapping("/getShipmentDetail")
	@ResponseBody
	public TableDataInfo getShipmentDetail(ShipmentDetail shipmentDetail) {
		LogisticAccount user = getUser();
		shipmentDetail.setLogisticGroupId(user.getGroupId());
		shipmentDetail.setProcessStatus("Y");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.getShipmentDetailListForAssign(shipmentDetail);
		return getDataTable(shipmentDetails);
	}

	// @GetMapping("pickTruckForm/{shipmentId}/{pickCont}/{shipmentDetailId}")
	// public String pickTruckForm(@PathVariable("shipmentId") long shipmentId, @PathVariable("pickCont") boolean pickCont,@PathVariable("shipmentDetailId") Integer shipmentDetailId, ModelMap mmap) {
	// 	mmap.put("shipmentId", shipmentId);
	// 	mmap.put("pickCont", pickCont);
	// 	mmap.put("shipmentDetailId", shipmentDetailId);
	// 	String transportId = "";
	// 	String shipmentIds = "";
	// 	if (!pickCont) {
	// 		ShipmentDetail shipmentDetail = new ShipmentDetail();
	// 		shipmentDetail.setShipmentId(shipmentId);
	// 		shipmentDetail.setLogisticGroupId(getUser().getGroupId());
	// 		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
	// 		for (ShipmentDetail shipmentDetail2 : shipmentDetails) {
	// 			if (shipmentDetail2.getPreorderPickup() == null || !shipmentDetail2.getPreorderPickup().equals("Y")) {
	// 				shipmentIds += shipmentDetail2.getId() + ",";
	// 				if (shipmentDetail2.getTransportIds() != null && transportId.length() == 0) {
	// 					transportId = shipmentDetail2.getTransportIds();
	// 				}
	// 			}
	// 		}
	// 	}
	// 	mmap.put("transportIds", transportId);
	// 	mmap.put("shipmentDetailIds", shipmentIds);
	// 	return PREFIX + "/pickTruckForm";
	// }

	// @PostMapping("/pickTruck")
	// @Transactional
	// @ResponseBody
	// public AjaxResult pickTruck(String shipmentDetailIds, String driverIds) {
	// 	List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
	// 	if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
	// 		for (ShipmentDetail shipmentDetail : shipmentDetails) {
	// 			//shipmentDetail.setTransportIds(driverIds);
	// 			shipmentDetailService.updateShipmentDetail(shipmentDetail);
	// 		}
	// 		return success("Điều xe thành công");
	// 	}
	// 	return error("Xảy ra lỗi trong quá trình điều xe.");
	// }
	
    @GetMapping("/listDriverAccount")
    @ResponseBody
    public List<DriverAccount> listDriver(Long shipmentId, @RequestParam(value = "pickedIds[]", required = false)  Long[] pickedIds)
    {
		DriverAccount driverAccount = new DriverAccount();
    	driverAccount.setLogisticGroupId(getUser().getGroupId());
		driverAccount.setDelFlag(false);
		driverAccount.setStatus("0");//khóa
		List<DriverAccount> driverList = driverAccountService.selectDriverAccountList(driverAccount);
		List<DriverAccount> assignedDriverList = driverAccountService.getAssignedDrivers(pickedIds);
		driverList.removeAll(assignedDriverList);
        return driverList;
    }
    @GetMapping("/assignedDriverAccountList")
    @ResponseBody
    public List<DriverAccount> assignedDriverAccountList(Long shipmentId)
    {
		List<DriverAccount> driverList = new ArrayList<DriverAccount>();
		DriverAccount driverAccount = new DriverAccount();
        driverAccount.setLogisticGroupId(getUser().getGroupId());
		driverAccount.setDelFlag(false);
		driverAccount.setStatus("0");
        PickupAssign pickupAssign = new PickupAssign();
        pickupAssign.setShipmentId(shipmentId);
        List<PickupAssign> assignList = pickupAssignService.selectPickupAssignList(pickupAssign);
        if(assignList.size() != 0) {
        	for(PickupAssign i : assignList) {
				//TH: custom theo batch
				if(i.getShipmentDetailId() == null && i.getDriverId() != null){
					driverList.add(driverAccountService.selectDriverAccountById(i.getDriverId()));
				}
			}
			//TH:default assign ca doi xe ma co cont chi dinh
			if(driverList.size() == 0 ){
				return driverAccountService.selectDriverAccountList(driverAccount);
			}
        } else {//TH:default assign ca doi xe , ko co cont chi dinh
			return driverAccountService.selectDriverAccountList(driverAccount);
		}
        return driverList;
	}
	
	@PostMapping("/savePickupAssignFollowBatch")
	@Transactional
	@ResponseBody
	public AjaxResult savePickupAssignFollowBatch(@RequestBody List<PickupAssign> pickupAssigns){
		if(pickupAssigns == null){
			return error();
		}
		int accountNumber = 0;
		for(PickupAssign i :pickupAssigns){
			if (i.getDriverId() != null) {
				accountNumber++;
			} else {
				if (i.getPhoneNumber() == null || "".equals(i.getPhoneNumber().trim())) {
					return error("Số điện thoại không được trống");
				}
				if (i.getFullName() == null || "".equals(i.getFullName().trim())) {
					return error("Họ tên không được trống");
				}
				if (i.getTruckNo() == null || "".equals(i.getTruckNo().trim())) {
					return error("Biển số xe đầu kéo không được trống");
				}
				if (i.getChassisNo() == null || "".equals(i.getChassisNo().trim())) {
					return error("Biển số xe đầu rơ mooc không được trống");
				}
			}
			i.setLogisticGroupId(getUser().getGroupId());
		}
		//check driverId of current logistic
		int check = driverAccountService.checkDriverOfLogisticGroup(pickupAssigns);
		if(check != accountNumber){
			return error("Có tài xế không tồn tại.");
		}
		//check shipmentId of current logistic
		Shipment shipment = shipmentService.selectShipmentById(pickupAssigns.get(0).getShipmentId());
		if(shipment != null && shipment.getLogisticAccountId().equals(getUser().getId())){
			//delete last assign follow batch
			PickupAssign assignBatch = new PickupAssign();
			assignBatch.setShipmentId(shipment.getId());
			List<PickupAssign> assignlist = pickupAssignService.selectPickupAssignList(assignBatch);
			if(assignlist.size() != 0 ){
				for(PickupAssign i : assignlist){
					if(i.getShipmentDetailId() == null){
						pickupAssignService.deletePickupAssignById(i.getId());
					}
				}
			}
			// add custom assign follow batch
			for(int i = 0 ; i< pickupAssigns.size(); i ++){
				pickupAssigns.get(i).setCreateBy(getUser().getFullName());
				pickupAssigns.get(i).setCreateTime(new Date());
				pickupAssignService.insertPickupAssign(pickupAssigns.get(i));
			}
			return success();
		}
		return error();
	}

	@GetMapping("preoderPickupAssign/{shipmentDetailId}")
	public String preoderPickupAssign(@PathVariable("shipmentDetailId") Long shipmentDetailId, ModelMap mmap){
		ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(shipmentDetailId);
		Shipment shipment = shipmentService.selectShipmentById(shipmentDetail.getShipmentId());
		mmap.put("shipment", shipment);
		mmap.put("shipmentDetail", shipmentDetail);
		return PREFIX + "/preoderPickupAssign";
	}
	@GetMapping("/assignedDriverAccountListForPreoderPickup")
	@ResponseBody
    public List<DriverAccount> assignedDriverAccountListForPreoderPickup(PickupAssign pickupAssign)
    {
		List<DriverAccount> driverList = new ArrayList<DriverAccount>();
		//check preoderPickup of ShipmentDetail
		ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(pickupAssign.getShipmentDetailId());
		Shipment shipment = shipmentService.selectShipmentById(pickupAssign.getShipmentId());
		if(shipment.getServiceType().equals(Constants.RECEIVE_CONT_FULL)){
			if(shipmentDetail.getPreorderPickup().equals("N")){
				return driverList;
			}
		}
		DriverAccount driverAccount = new DriverAccount();
		driverAccount.setLogisticGroupId(getUser().getGroupId());
		driverAccount.setDelFlag(false);
		driverAccount.setStatus("0");
        List<PickupAssign> assignList = pickupAssignService.selectPickupAssignList(pickupAssign);
        if(assignList.size() != 0) {
        	for(PickupAssign i : assignList) {
				//TH: assign theo container
				if(i.getShipmentDetailId() != null && i.getDriverId() != null){
					driverList.add(driverAccountService.selectDriverAccountById(i.getDriverId()));
				}
			}
			if(driverList.size() == 0){
				//TH: chua assign
					//return driverAccountService.selectDriverAccountList(driverAccount);
					return driverList;
			}
        } else {
			//TH: chua assign theo cont, batch
			return driverList;
		}
        return driverList;
	}
	@GetMapping("/listDriverAccountPreorderPickup")
    @ResponseBody
    public List<DriverAccount> listDriverAccountPreorderPickup(PickupAssign pickupAssign, @RequestParam(value = "pickedIds[]", required = false)  Long[] pickedIds)
    {
		DriverAccount driverAccount = new DriverAccount();
    	driverAccount.setLogisticGroupId(getUser().getGroupId());
		driverAccount.setDelFlag(false);
		driverAccount.setStatus("0");
		List<DriverAccount> driverList = driverAccountService.selectDriverAccountList(driverAccount);
		if(pickedIds == null){
			return driverList;
		}
		List<DriverAccount> assignedDriverList = driverAccountService.getAssignedDrivers(pickedIds);
		driverList.removeAll(assignedDriverList);
        return driverList;
	}
	
	@PostMapping("/savePickupAssignFollowContainer")
	@Transactional
	@ResponseBody
	public AjaxResult savePickupAssignFollowContainer(@RequestBody List<PickupAssign> pickupAssigns){
		if(pickupAssigns == null){
			return error();
		}
		int accountNumber = 0; // so driver trong cty
		for(PickupAssign i :pickupAssigns){
			if (i.getDriverId() != null) {
				accountNumber++;
			} else {
				if (i.getPhoneNumber() == null || "".equals(i.getPhoneNumber().trim())) {
					return error("Số điện thoại không được trống");
				}
				if (i.getFullName() == null || "".equals(i.getFullName().trim())) {
					return error("Họ tên không được trống");
				}
				if (i.getTruckNo() == null || "".equals(i.getTruckNo().trim())) {
					return error("Biển số xe đầu kéo không được trống");
				}
				if (i.getChassisNo() == null || "".equals(i.getChassisNo().trim())) {
					return error("Biển số xe đầu rơ mooc không được trống");
				}
			}
			i.setLogisticGroupId(getUser().getGroupId());
		}
		//check driverId of current logistic
		int check = driverAccountService.checkDriverOfLogisticGroup(pickupAssigns);
		if(check != accountNumber){
			return error("Có tài xế không tồn tại.");
		}
		//check shipmentId of current logistic
		Shipment shipment  = shipmentService.selectShipmentById(pickupAssigns.get(0).getShipmentId());
		if(shipment != null && shipment.getLogisticAccountId().equals(getUser().getId())){
			//delete last assign follow container
			PickupAssign assignContainer = new PickupAssign();
			assignContainer.setShipmentId(shipment.getId());
			assignContainer.setShipmentDetailId(pickupAssigns.get(0).getShipmentDetailId());
			List<PickupAssign> assignlist = pickupAssignService.selectPickupAssignList(assignContainer);
			if(assignlist.size() != 0 ){
				for(PickupAssign i : assignlist){
						pickupAssignService.deletePickupAssignById(i.getId());
				}
			}
			// add  assign follow container (preoderPickup: receiveContFull)
			for(int i = 0; i < pickupAssigns.size(); i ++){
				pickupAssigns.get(i).setCreateBy(getUser().getFullName());
				pickupAssigns.get(i).setCreateTime(new Date());
				pickupAssignService.insertPickupAssign(pickupAssigns.get(i));
			}
			return success();
		}
		return error();
		
	}

	@GetMapping("edit/driver/{id}")
	public String editDriver(@PathVariable("id") Long id, ModelMap mmap)
    {
        DriverAccount driverAccount = driverAccountService.selectDriverAccountById(id);
        mmap.put("driverAccount", driverAccount);
        return PREFIX + "/driverInfor";
	}
	/**
     * Update Save Driver login info
     */
    @Log(title = "Driver login info", businessType = BusinessType.UPDATE)
    @PostMapping("/edit/driver")
    @ResponseBody
    public AjaxResult editSave(DriverAccount driverAccount)
    {
        if(driverAccountService.checkPhoneUnique(driverAccount.getMobileNumber()) > 1) {
        	return error("PhoneNumber này đã tồn tại!");
        }
        if(!Pattern.matches(PHONE_PATTERN, driverAccount.getMobileNumber())){
        	return error("PhoneNumber này phải từ 10 đến 11 số!");
        }
        return toAjax(driverAccountService.updateDriverAccount(driverAccount));
	}
	/**
	 * Load table truck assigned follow driver
	*/
	@RequestMapping("/driver/truck/list")
	@ResponseBody
	public List<LogisticTruck> getDriverTruckList(DriverTruck driverTruck){
		List<LogisticTruck> logisticTrucks  = new ArrayList<LogisticTruck>();
		DriverAccount driverAccount = driverAccountService.selectDriverAccountById(driverTruck.getDriverId());
		//check driver of current logisticGroup
		if(driverAccount.getLogisticGroupId().equals(getUser().getGroupId())){
			//get ds xe theo driverId (table mapping)
			List<DriverTruck> tractors = driverTruckService.selectTractorByDriverId(driverTruck.getDriverId());
			List<DriverTruck> trailers = driverTruckService.selectTrailerByDriverId(driverTruck.getDriverId());
			if(tractors.size() != 0){
				for(DriverTruck i : tractors){
					logisticTrucks.add(logisticTruckService.selectLogisticTruckById(i.getTruckId()));
				}
			}
			if(trailers.size() != 0){
				for(DriverTruck i :trailers){
					logisticTrucks.add(logisticTruckService.selectLogisticTruckById(i.getTruckId()));
				}
			}
		}
		return logisticTrucks;
	}
	/**
	 * Load table truck not assigned follow driver
	*/
	@GetMapping("/trucks/not-picked")
	@ResponseBody
	public List<LogisticTruck> getTrucks(@RequestParam (value = "truckIds[]", required = false) Long[] truckIds){
		LogisticTruck logisticTruck = new LogisticTruck();
		logisticTruck.setLogisticGroupId(getUser().getGroupId());
		List<LogisticTruck> trucks = logisticTruckService.selectLogisticTruckList(logisticTruck);
		if(truckIds != null && trucks.size() > 0){
			for(Long i : truckIds){
				for(int j=0; j< trucks.size(); j++){
					if(trucks.get(j).getId() == i){
						trucks.remove(j);
					}
				}
			}
		}
		return trucks;
	}

	/**
	 * Save assign truck for driver
	 * @param truckIds
	 * @param driverId
	 * @return
	 */
	@PostMapping("/truck/assign/save")
	@ResponseBody
	@Transactional
	public AjaxResult saveAssignTruck(@RequestParam(value = "truckIds[]", required = false)  Long[] truckIds, Long driverId){
		//check this driver is of current logisticGoup
		DriverAccount driverAccount = driverAccountService.selectDriverAccountById(driverId);
		if(! driverAccount.getLogisticGroupId().equals(getUser().getGroupId())){
			return error();
		}
        if(truckIds != null){
			//truckIds is of current logisticGroup
			LogisticTruck logisticTruck = new LogisticTruck();
			logisticTruck.setLogisticGroupId(getUser().getGroupId());
			List<LogisticTruck> logisticTrucks = logisticTruckService.selectLogisticTruckList(logisticTruck);
			if(logisticTrucks.size() > 0 ){
				int count = 0;
				for(Long i : truckIds){
					for(int j = 0; j< logisticTrucks.size(); j++){
						if(logisticTrucks.get(j).getId().equals(i)){
							count++;
						}
					}
				}
				if(count != truckIds.length){
					//TH: a truckId isn't of current logisticGroup
					return error();
				}
			}else{
				//TH:current logisticGroup hasn't truck
				return error();
			}
			driverTruckService.deleteDriverTruckById(driverId);
            for (Long i : truckIds) {
                DriverTruck driverTruck = new DriverTruck();
                driverTruck.setDriverId(driverId);
                driverTruck.setTruckId(i);
                driverTruckService.insertDriverTruck(driverTruck);
            }
        }
        return success();
	}

	@GetMapping("/out-source/batch/{shipmentId}")
	@ResponseBody
	public AjaxResult getOutSourcListForBatch(@PathVariable Long shipmentId){
		AjaxResult ajaxResult = success();
		PickupAssign pickupAssign = new PickupAssign();
		pickupAssign.setExternalFlg(1L);
		pickupAssign.setLogisticGroupId(getUser().getGroupId());
		pickupAssign.setShipmentId(shipmentId);
		List<PickupAssign> pickupAssigns = pickupAssignService.selectPickupAssignList(pickupAssign);
		List<PickupAssign> outSourceForBatch = new ArrayList<PickupAssign>();
		if(pickupAssigns.size() > 0){
			for(PickupAssign i: pickupAssigns){
				if(i.getShipmentDetailId() == null) {
					outSourceForBatch.add(i);
				}
			}
		}
		ajaxResult.put("outSourceList", outSourceForBatch);
		return ajaxResult;
	}

	@GetMapping("/out-source/container/{shipmentId}")
	@ResponseBody
	public AjaxResult getOutSourcListForContainer(@PathVariable Long shipmentId){
		AjaxResult ajaxResult = success();
		PickupAssign pickupAssign = new PickupAssign();
		pickupAssign.setExternalFlg(1L);
		pickupAssign.setLogisticGroupId(getUser().getGroupId());
		pickupAssign.setShipmentId(shipmentId);
		List<PickupAssign> pickupAssigns = pickupAssignService.selectPickupAssignList(pickupAssign);
		List<PickupAssign> outSourceForContainer = new ArrayList<PickupAssign>();
		if(pickupAssigns.size() > 0){
			for(PickupAssign i: pickupAssigns){
				if(i.getShipmentDetailId() != null) {
					outSourceForContainer.add(i);
				}
			}
		}
		ajaxResult.put("outSourceList", outSourceForContainer);
		return ajaxResult;
	}
}
