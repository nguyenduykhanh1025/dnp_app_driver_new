package vn.com.irtech.eport.logistic.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
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
    public String assignTruck(ModelMap mmap) {
		PickupAssign pickupAssign = new PickupAssign();
		pickupAssign.setLogisticGroupId(getUser().getGroupId());
		pickupAssign.setExternalFlg(1L);
		mmap.put("driverOwnerList", pickupAssignService.getDriverOwners(pickupAssign));
    	return PREFIX + "/assignTruck";
	}

	@PostMapping("/listShipment")
	@ResponseBody
	public TableDataInfo listShipment(@RequestBody PageAble<Shipment> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		LogisticAccount user = getUser();
		Shipment shipment = param.getData();
		shipment.setLogisticGroupId(user.getGroupId());
		//List<Shipment> shipments = shipmentService.selectShipmentList(shipment);
		List<Shipment> shipments = shipmentService.getShipmentListForAssign(shipment);
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
	
    @GetMapping("/listDriverAccount")
    @ResponseBody
    public List<DriverAccount> listDriver(Long shipmentId, @RequestParam(value = "pickedIds[]", required = false)  Long[] pickedIds)
    {
		DriverAccount driverAccount = new DriverAccount();
    	driverAccount.setLogisticGroupId(getUser().getGroupId());
		driverAccount.setDelFlag(false);
		driverAccount.setStatus("0");//khóa
		if(pickedIds == null){
			return driverAccountService.selectDriverAccountList(driverAccount);
		}
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
        } else {//TH:default assign ca doi xe , ko co cont chi dinh, ko thue ngoai
			return driverAccountService.selectDriverAccountList(driverAccount);
		}
        return driverList;
	}
	
	@PostMapping("/savePickupAssignFollowBatch")
	@Transactional
	@ResponseBody
	public AjaxResult savePickupAssignFollowBatch(@RequestBody List<PickupAssign> pickupAssigns){
		if(pickupAssigns.size() == 0){
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
		if(accountNumber > 0){// co driver trong cty moi check
			int check = driverAccountService.checkDriverOfLogisticGroup(pickupAssigns);
			if(check != accountNumber){
				return error("Có tài xế không tồn tại.");
			}
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
		PickupAssign pickupAssign = new PickupAssign();
		pickupAssign.setLogisticGroupId(getUser().getGroupId());
		pickupAssign.setExternalFlg(1L);
		mmap.put("driverOwnerList", pickupAssignService.getDriverOwners(pickupAssign));
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
		if(pickupAssigns.size() == 0){
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
		if(accountNumber > 0){// co driver trong cty moi check
			int check = driverAccountService.checkDriverOfLogisticGroup(pickupAssigns);
			if(check != accountNumber){
				return error("Có tài xế không tồn tại.");
			}
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
	public AjaxResult getOutSourceListForBatch(@PathVariable Long shipmentId){
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

	@GetMapping("/out-source/container/{shipmentDetailId}")
	@ResponseBody
	public AjaxResult getOutSourceListForContainer(@PathVariable Long shipmentDetailId){
		AjaxResult ajaxResult = success();
		PickupAssign pickupAssign = new PickupAssign();
		pickupAssign.setExternalFlg(1L);
		pickupAssign.setLogisticGroupId(getUser().getGroupId());
		pickupAssign.setShipmentDetailId(shipmentDetailId);
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

	@GetMapping("/owner/{owner}/driver-phone-list")
	@ResponseBody
	public AjaxResult getDriverPhoneByOwner(@PathVariable String owner) {
		AjaxResult ajaxResult = AjaxResult.success();
		PickupAssign pickupAssign = new PickupAssign();
		pickupAssign.setLogisticGroupId(getUser().getGroupId());
		pickupAssign.setExternalFlg(1L);
		pickupAssign.setDriverOwner(owner);
		ajaxResult.put("driverPhoneList", pickupAssignService.getPhoneNumbersByDriverOwner(pickupAssign));
		return ajaxResult;
	}

	@GetMapping("/driver-phone/{driverPhone}/infor")
	@ResponseBody
	public AjaxResult getInforByDriverPhone(@PathVariable String driverPhone) {
		AjaxResult ajaxResult = AjaxResult.success();
		PickupAssign pickupAssign = new PickupAssign();
		pickupAssign.setLogisticGroupId(getUser().getGroupId());
		pickupAssign.setExternalFlg(1L);
		pickupAssign.setPhoneNumber(driverPhone);
		ajaxResult.put("pickupAssign", pickupAssignService.getInforOutSourceByPhoneNumber(pickupAssign));
		return ajaxResult;
	}

	@GetMapping("/remark/batch/{shipmentId}")
	@ResponseBody
	public AjaxResult getRemarkFollowBatch(@PathVariable Long shipmentId){
		if(shipmentId == null){
			return error();
		}
		PickupAssign pickupAssign = new PickupAssign();
		pickupAssign.setShipmentId(shipmentId);
		pickupAssign.setLogisticGroupId(getUser().getGroupId());
		String remark = pickupAssignService.getRemarkFollowBatchByShipmentId(pickupAssign);
		AjaxResult ajaxResult = success();
		ajaxResult.put("remark", remark);
		return ajaxResult;
	}

	
	@GetMapping("/remark/container/{shipmentDetailId}")
	@ResponseBody
	public AjaxResult getRemarkFollowContainer(@PathVariable Long shipmentDetailId){
		if(shipmentDetailId == null){
			return error();
		}
		PickupAssign pickupAssign = new PickupAssign();
		pickupAssign.setShipmentDetailId(shipmentDetailId);
		pickupAssign.setLogisticGroupId(getUser().getGroupId());
		String remark = pickupAssignService.getRemarkFollowContainerByShipmentDetailId(pickupAssign);
		AjaxResult ajaxResult = success();
		ajaxResult.put("remark", remark);
		return ajaxResult;
	}
	
	@GetMapping("/jasper-report/view/{shipmentId}")
	public void jasperReport(@PathVariable("shipmentId") Long shipmentId, HttpServletResponse response) {
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", String.format("attachment; filename=\"report.pdf\""));
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setShipmentId(shipmentId);
		shipmentDetail.setPaymentStatus("Y");
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
        try {
        	OutputStream out = response.getOutputStream();
            createPdfReport(shipmentDetails, out);
            
        } catch (final Exception e) {
            e.printStackTrace();
        }
	}
	private void createPdfReport(final List<ShipmentDetail> shipmentDetails, OutputStream out) throws JRException {
		// Fetching the .jrxml file from the resources folder.
        final InputStream stream = this.getClass().getResourceAsStream("/order_report.jrxml");
        // Compile the Jasper report from .jrxml to .japser
        final JasperReport report = JasperCompileManager.compileReport(stream);
        
        // Fetching the shipmentDetails from the data source.
        final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(shipmentDetails);
        
        // Adding the additional parameters to the pdf.
//        final Map<String, Object> parameters = new HashMap<>();
//        parameters.put("createdBy", "javacodegeek.com");
        
        // Filling the report with the shipmentDetail data and additional parameters information.
        final JasperPrint print = JasperFillManager.fillReport(report, null, source);
        
        //final String filePath = "I:/";
        // Export the report to a PDF file.
        //JasperExportManager.exportReportToPdfFile(print, filePath + "order_report.pdf");
        JasperExportManager.exportReportToPdfStream(print, out);
	}
}
