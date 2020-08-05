package vn.com.irtech.eport.logistic.service;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;

public interface ICatosApiService {

	public Shipment getOpeCodeCatosByBlNo(String blNo);
	
	public Shipment getGroupNameByTaxCode(String taxCode);
	
	public ProcessOrder getYearBeforeAfter(String vessel, String voyage);
	
	public List<String> checkContainerReserved(String containerNos);
	
	public List<String> getPODList(ShipmentDetail shipmentDetail);
	
	public List<String> getVesselCodeList();
	
	public List<String> getConsigneeList();
	
	public List<String> getOpeCodeList();
	
	public List<String> getVoyageNoList(String vesselCode);
	
	public int getCountContByBlNo(String blNo);
	
	public Boolean checkCustomStatus(String containerNo, String voyNo);
	
	public List<ShipmentDetail> getCoordinateOfContainers(String blNo);
	
	public List<ShipmentDetail> selectShipmentDetailsByBLNo(String blNo);
	
	public ShipmentDetail selectShipmentDetailByContNo(String blNo, String containerNo);
	
	public List<String> selectVesselCodeBerthPlan(String opeCode);
	
	public String getYearByVslCodeAndVoyNo(String vslCode, String voyNo);
	
	public List<String> selectOpeCodeListInBerthPlan();
	
	public List<ShipmentDetail> selectVesselVoyageBerthPlan(String opeCode);
	
	public List<ProcessBill> getUnitBillByShipmentDetailsForReserve(ShipmentDetail shipmentDetail);
	
	public List<ProcessBill> getUnitBillByShipmentDetailsForInventory(ShipmentDetail shipmentDetail);
	
	public List<ProcessBill> getUnitBillByShipmentDetailsForReceiveSSR(ShipmentDetail shipmentDetail);
	
	public List<ProcessBill> getUnitBillByShipmentDetailsForSendSSR(ShipmentDetail shipmentDetail);
	
	public Integer checkBookingNoForSendFReceiveE(String bookingNo, String fe);
	
	public ShipmentDetail getInforSendFReceiveE(ShipmentDetail shipmentDetail);
}
