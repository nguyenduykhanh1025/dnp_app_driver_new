package vn.com.irtech.eport.logistic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.form.ShipmentForm;

/**
 * ShipmentMapper Interface
 * 
 * @author admin
 * @date 2020-05-07
 */
public interface ShipmentMapper 
{
    /**
     * Get Shipment
     * 
     * @param id ShipmentID
     * @return Shipment
     */
    public Shipment selectShipmentById(Long id);

    /**
     * Get Shipment List
     * 
     * @param shipment Shipment
     * @return Shipment List
     */
    public List<Shipment> selectShipmentList(Shipment shipment);

    /**
     * Add Shipment
     * 
     * @param shipment Shipment
     * @return Result
     */
    public int insertShipment(Shipment shipment);

    /**
     * Update Shipment
     * 
     * @param shipment Shipment
     * @return Result
     */
    public int updateShipment(Shipment shipment);

    /**
     * Delete Shipment
     * 
     * @param id ShipmentID
     * @return result
     */
    public int deleteShipmentById(Long id);

    /**
     * Batch Delete Shipment
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteShipmentByIds(String[] ids);

    public int checkBillBookingNoUnique(Shipment shipment);

    public List<Shipment> selectShipmentListForOm(Shipment shipment);

    public List<Shipment> getShipmentListForAssign(Shipment shipment);

    /**
     * Select Shipment List For Driver App
     * 
     * @param serviceType
     * @param logisticGroupId
     * @return List<ShipmentForm>
     */
    public List<ShipmentForm> selectShipmentListForDriver(@Param("serviceType") Integer serviceType, @Param("driverId") Long driverId);
    
    /**
     * Select Shipment List For Register
     */
    public List<Shipment> selectShipmentListForRegister(Shipment shipment);
    
    /**
     * Get shipment list with logistic name for cont supplier
     * 
     * @param shipment
     * @return List shipment
     */
    public List<Shipment> getShipmentListForContSupply(Shipment shipment);
    
    /**
     * Select shipment list with searching field form both shipment and shipment detail
     * including vslnm, voyno, container no , do status,... from shipment detail
     * 
     * @param shipment
     * @return List shipment object
     */
    public List<Shipment> selectShipmentListByWithShipmentDetailFilter(Shipment shipment);
    
    
    public List<Shipment> selectShipmentListByWithShipmentDetailFilterApply(Shipment shipment);
    
    public List<Shipment> selectShipmentListByWithShipmentDetailDangerous(Shipment shipment);
    
    public List<Shipment> selectShipmentListByWithShipmentOverSize(Shipment shipment);
    
    
    
    /**
     * input: serviceType(bat buoc)
     * getShipmentsForSupportCustom in OM SupportCustomReceiveFull, SupportCustomSendFull
     */
    public List<Shipment> getShipmentsForSupportCustom(Shipment shipment);
    
    /**
     * Select list shipment where shipment detail is exists with condition for extension date
     * 
     * @param shipment
     * @return List shipment object
     */
    public List<Shipment> selectShipmentListForExtensionDate(Shipment shipment);
}
