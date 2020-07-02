package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.domain.PickupAssign;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.IDriverAccountService;
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
		//shipment.setServiceId(1);
		List<Shipment> shipments = shipmentService.selectShipmentList(shipment);
		return getDataTable(shipments);
	}

	@RequestMapping("/getShipmentDetail")
	@ResponseBody
	public TableDataInfo getShipmentDetail(ShipmentDetail shipmentDetail) {
		LogisticAccount user = getUser();
		shipmentDetail.setLogisticGroupId(user.getGroupId());
		shipmentDetail.setProcessStatus("Y");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
		return getDataTable(shipmentDetails);
	}

	@GetMapping("pickTruckForm/{shipmentId}/{pickCont}/{shipmentDetailId}")
	public String pickTruckForm(@PathVariable("shipmentId") long shipmentId, @PathVariable("pickCont") boolean pickCont,@PathVariable("shipmentDetailId") Integer shipmentDetailId, ModelMap mmap) {
//		mmap.put("shipmentId", shipmentId);
//		mmap.put("pickCont", pickCont);
//		mmap.put("shipmentDetailId", shipmentDetailId);
//		String transportId = "";
//		String shipmentIds = "";
//		if (!pickCont) {
//			ShipmentDetail shipmentDetail = new ShipmentDetail();
//			shipmentDetail.setShipmentId(shipmentId);
//			shipmentDetail.setLogisticGroupId(getUser().getGroupId());
//			List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
//			for (ShipmentDetail shipmentDetail2 : shipmentDetails) {
//				if (shipmentDetail2.getPreorderPickup() == null || !shipmentDetail2.getPreorderPickup().equals("Y")) {
//					shipmentIds += shipmentDetail2.getId() + ",";
//					if (shipmentDetail2.getTransportIds() != null && transportId.length() == 0) {
//						transportId = shipmentDetail2.getTransportIds();
//					}
//				}
//			}
//		}
//		mmap.put("transportIds", transportId);
//		mmap.put("shipmentDetailIds", shipmentIds);
		return PREFIX + "/pickTruckForm";
	}

	@PostMapping("/pickTruck")
	@Transactional
	@ResponseBody
	public AjaxResult pickTruck(String shipmentDetailIds, String driverIds) {
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIds);
		if (shipmentDetails.size() > 0 && verifyPermission(shipmentDetails.get(0).getLogisticGroupId())) {
			for (ShipmentDetail shipmentDetail : shipmentDetails) {
				//shipmentDetail.setTransportIds(driverIds);
				shipmentDetailService.updateShipmentDetail(shipmentDetail);
			}
			return success("Điều xe thành công");
		}
		return error("Xảy ra lỗi trong quá trình điều xe.");
	}
	
    @GetMapping("/listDriverAccount")
    @ResponseBody
    public List<DriverAccount> listDriver(Long shipmentId, @RequestParam(value = "pickedIds[]", required = false)  Long[] pickedIds)
    {
		DriverAccount driverAccount = new DriverAccount();
    	driverAccount.setLogisticGroupId(getUser().getGroupId());
        driverAccount.setDelFlag(false);
		List<DriverAccount> driverList = driverAccountService.selectDriverAccountList(driverAccount);
		if(pickedIds != null){
			for(Long i :pickedIds) {
				for(int j = 0; j < driverList.size(); j++){
					if(driverList.get(j).getId() == i){
						driverList.remove(j);
					}
				}
			}
		}
        return driverList;
    }
    @GetMapping("/assignedDriverAccountList")
    @ResponseBody
    public List<DriverAccount> listDriverAccount(Long shipmentId)
    {
		List<DriverAccount> driverList = new ArrayList<DriverAccount>();
		DriverAccount driverAccount = new DriverAccount();
        driverAccount.setLogisticGroupId(getUser().getGroupId());
        driverAccount.setDelFlag(false);
        PickupAssign pickupAssign = new PickupAssign();
        pickupAssign.setShipmentId(shipmentId);
        List<PickupAssign> assignList = pickupAssignService.selectPickupAssignList(pickupAssign);
        if(assignList.size() != 0) {
        	for(PickupAssign i : assignList) {
        		//TH: default assign ca doi xe
        		if(i.getDriverId() == null) {
        			return driverAccountService.selectDriverAccountList(driverAccount);
        		}
        		//TH: con lai
        		driverList.add(driverAccountService.selectDriverAccountById(i.getDriverId()));
        	}
        }
        return driverList;
	}
	
	@PostMapping("/savePickupAssignFollowBatch")
	@Transactional
	@ResponseBody
	public AjaxResult savePickupAssignFollowBatch(@RequestParam( value = "pickedIdDriverArray[]", required = false) Long[] pickedIdDriverArray, Long shipmentId){
		AjaxResult ajaxResult = new AjaxResult();
		if(shipmentId == null || pickedIdDriverArray == null){
			ajaxResult = error();
			return ajaxResult;
		}
		Shipment shipment  = shipmentService.selectShipmentById(shipmentId);
		System.out.println(getUser().getId() + "XXXXX");
		//check shipment of current user
		if(shipment.getLogisticAccountId().equals(getUser().getId())){
			//delete default assign follow batch or last assign
			PickupAssign assignBatch = new PickupAssign();
			assignBatch.setShipmentId(shipmentId);
			List<PickupAssign> assignlist = pickupAssignService.selectPickupAssignList(assignBatch);
			if(assignlist.size() != 0 ){
				for(PickupAssign i : assignlist){
					if(i.getDriverId() == null || i.getShipmentDetailId() == null){
						pickupAssignService.deletePickupAssignById(i.getId());
					}
				}
			}
			// add custom assign follow batch
			for(Long i : pickedIdDriverArray){
				PickupAssign pickupAssign = new PickupAssign();
				pickupAssign.setDriverId(i);
				pickupAssign.setLogisticGroupId(shipment.getLogisticGroupId());
				pickupAssign.setShipmentId(shipmentId);
				pickupAssign.setExternalFlg(0L);
				pickupAssignService.insertPickupAssign(pickupAssign);
			}
		}
		ajaxResult = success();
		return ajaxResult;
	}
}
