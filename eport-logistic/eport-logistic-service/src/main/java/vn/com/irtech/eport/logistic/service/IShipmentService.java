package vn.com.irtech.eport.logistic.service;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.form.ShipmentForm;

/**
 * ShipmentService Interface
 * 
 * @author admin
 * @date 2020-05-07
 */
public interface IShipmentService {
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
	 * @return result
	 */
	public int insertShipment(Shipment shipment);

	/**
	 * Update Shipment
	 * 
	 * @param shipment Shipment
	 * @return result
	 */
	public int updateShipment(Shipment shipment);

	/**
	 * Batch Delete Shipment
	 * 
	 * @param ids Entity Ids
	 * @return result
	 */
	public int deleteShipmentByIds(String ids);

	/**
	 * Delete Shipment
	 * 
	 * @param id ShipmentID
	 * @return result
	 */
	public int deleteShipmentById(Long id);

	public int checkBillBookingNoUnique(Shipment shipment);

	public Shipment getOpeCodeCatosByBlNo(String blNo);

	public List<Shipment> selectShipmentListForOm(Shipment shipment);

	public List<Shipment> getShipmentListForAssign(Shipment shipment);
	   /**
		 * Select Shipment List For Driver App
		 * 
		 * @param serviceType
		 * @param driverId
		 * @return List<ShipmentForm>
		 */
		public List<ShipmentForm> selectShipmentListForDriver(Integer serviceType, Long driverId);
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
}
