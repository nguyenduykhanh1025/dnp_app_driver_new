package vn.com.irtech.eport.logistic.service;

import java.util.List;

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
}
