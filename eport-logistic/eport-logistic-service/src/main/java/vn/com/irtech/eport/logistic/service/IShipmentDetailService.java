package vn.com.irtech.eport.logistic.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceRobotReq;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.dto.ShipmentWaitExec;

/**
 * Shipment DetailsService Interface
 * 
 * @author admin
 * @date 2020-05-07
 */
public interface IShipmentDetailService 
{
    /**
     * Get Shipment Details
     * 
     * @param id Shipment DetailsID
     * @return Shipment Details
     */
    public ShipmentDetail selectShipmentDetailById(Long id);

    /**
     * Get Shipment Details List
     * 
     * @param shipmentDetail Shipment Details
     * @return Shipment Details List
     */
    public List<ShipmentDetail> selectShipmentDetailList(ShipmentDetail shipmentDetail);

    /**
     * Add Shipment Details
     * 
     * @param shipmentDetail Shipment Details
     * @return result
     */
    public int insertShipmentDetail(ShipmentDetail shipmentDetail);

    /**
     * Update Shipment Details
     * 
     * @param shipmentDetail Shipment Details
     * @return result
     */
    public int updateShipmentDetail(ShipmentDetail shipmentDetail);

    /**
     * Batch Delete Shipment Details
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteShipmentDetailByIds(String ids);

    /**
     * Delete Shipment Details
     * 
     * @param id Shipment DetailsID
     * @return result
     */
    public int deleteShipmentDetailById(Long id);

    public List<ShipmentDetail> selectShipmentDetailByIds(String ids);

    public List<ShipmentDetail> selectShipmentDetailByBlno(String Blno);

    public List<String> getBlListByDoStatus(String keyString);

    public List<String> getBlLists(String keyString);

    public List<String> getBlListByPaymentStatus(String keyString);
    
    public long countShipmentDetailList(ShipmentDetail shipmentDetail);
    
    /**
     * Select list shipment detail wait robot execute or robot can't be execute, group by shipment id
     * @return result
     */
    public List<ShipmentWaitExec> selectListShipmentWaitExec();

    public List<ShipmentDetail[][]> getContPosition(List<LinkedHashMap> coordinateOfList, List<ShipmentDetail> shipmentDetails);
    
    public boolean calculateMovingCont(List<LinkedHashMap> coordinateOfList, List<ShipmentDetail> preorderPickupConts, List<ShipmentDetail> shipmentDetails);

    public List<ServiceSendFullRobotReq> makeOrderReceiveContFull(List<ShipmentDetail> shipmentDetails, Shipment shipment, boolean creditFlag);

    public List<ProcessOrder> makeOrderReceiveContEmpty(List<ShipmentDetail> shipmentDetails);

    public ProcessOrder makeOrderSendCont(List<ShipmentDetail> shipmentDetails, Shipment shipment, boolean creditFlag);
    
    public void updateProcessStatus(List<ShipmentDetail> shipmentDetail, String status, String invoiceNo);

    public boolean checkCustomStatus(String userVoy,String cntrNo) throws IOException;

    public String getGroupNameByTaxCode(String taxCode) throws Exception;

    public ProcessOrder getYearBeforeAfter(String vessel, String voyage);
    
    public List<String> checkContainerReserved(String containerNos);
    
    public Integer getCountContByBlNo(String blNo);
}
