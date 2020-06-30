package vn.com.irtech.eport.logistic.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import vn.com.irtech.eport.common.annotation.Excel;
import vn.com.irtech.eport.common.core.domain.BaseEntity;

/**
 * Pickup history Object
 * 
 * @author baohv
 * @date 2020-06-27
 */
public class PickupHistory extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** id */
	private Long id;

	/** Logistic Group */
	@Excel(name = "Logistic Group")
	private Long logisticGroupId;

	/** Shipment id */
	@Excel(name = "Shipment id")
	private Long shipmentId;

	/** Shipment detail id 1 */
	@Excel(name = "Shipment detail id 1")
	private Long shipmentDetailId1;

	/** Shipment detail id 2 */
	@Excel(name = "Shipment detail id 2")
	private Long shipmentDetailId2;

	/** Driver id */
	@Excel(name = "Driver id")
	private Long driverId;

	/** Assign ID */
	@Excel(name = "Assign ID")
	private Long pickupAssignId;

	/** Container no 1 */
	@Excel(name = "Container no 1")
	private String containerNo1;

	/** Container no 2 */
	@Excel(name = "Container no 2")
	private String containerNo2;

	/** Truck no */
	@Excel(name = "Truck no")
	private String truckNo;

	/** Chassis no */
	@Excel(name = "Chassis no")
	private String chassisNo;

	/** Yard position 1 */
	@Excel(name = "Yard position 1")
	private String yardPosition1;

	/** Yard position 2 */
	@Excel(name = "Yard position 2")
	private String yardPosition2;

	/** Status */
	@Excel(name = "Status")
	private Integer status;

	/** Receipt date */
	@Excel(name = "Receipt date", width = 30, dateFormat = "yyyy-MM-dd")
	private Date receiptDate;

	/** Gatein date */
	@Excel(name = "Gatein date", width = 30, dateFormat = "yyyy-MM-dd")
	private Date gateinDate;

	/** Gateout date */
	@Excel(name = "Gateout date", width = 30, dateFormat = "yyyy-MM-dd")
	private Date gateoutDate;

	/** cancel Receipt Date */
	@Excel(name = "cancel Receipt Date", width = 30, dateFormat = "yyyy-MM-dd")
	private Date cancelDeceiptDate;

	private Shipment shipment;

	private ShipmentDetail shipmentDetail1;

	private ShipmentDetail shipmentDetail2;

	private LogisticGroup logisticGroup;

	private DriverAccount driver;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setLogisticGroupId(Long logisticGroupId) {
		this.logisticGroupId = logisticGroupId;
	}

	public Long getLogisticGroupId() {
		return logisticGroupId;
	}

	public void setShipmentId(Long shipmentId) {
		this.shipmentId = shipmentId;
	}

	public Long getShipmentId() {
		return shipmentId;
	}

	public Long getShipmentDetailId1() {
		return shipmentDetailId1;
	}

	public void setShipmentDetailId1(Long shipmentDetailId1) {
		this.shipmentDetailId1 = shipmentDetailId1;
	}

	public Long getShipmentDetailId2() {
		return shipmentDetailId2;
	}

	public void setShipmentDetailId2(Long shipmentDetailId2) {
		this.shipmentDetailId2 = shipmentDetailId2;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setPickupAssignId(Long pickupAssignId) {
		this.pickupAssignId = pickupAssignId;
	}

	public Long getPickupAssignId() {
		return pickupAssignId;
	}

	public String getContainerNo1() {
		return containerNo1;
	}

	public void setContainerNo1(String containerNo1) {
		this.containerNo1 = containerNo1;
	}

	public String getContainerNo2() {
		return containerNo2;
	}

	public void setContainerNo2(String containerNo2) {
		this.containerNo2 = containerNo2;
	}

	public void setTruckNo(String truckNo) {
		this.truckNo = truckNo;
	}

	public String getTruckNo() {
		return truckNo;
	}

	public void setChassisNo(String chassisNo) {
		this.chassisNo = chassisNo;
	}

	public String getChassisNo() {
		return chassisNo;
	}

	public String getYardPosition1() {
		return yardPosition1;
	}

	public void setYardPosition1(String yardPosition1) {
		this.yardPosition1 = yardPosition1;
	}

	public String getYardPosition2() {
		return yardPosition2;
	}

	public void setYardPosition2(String yardPosition2) {
		this.yardPosition2 = yardPosition2;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setGateinDate(Date gateinDate) {
		this.gateinDate = gateinDate;
	}

	public Date getGateinDate() {
		return gateinDate;
	}

	public void setGateoutDate(Date gateoutDate) {
		this.gateoutDate = gateoutDate;
	}

	public Date getGateoutDate() {
		return gateoutDate;
	}

	public void setCancelDeceiptDate(Date cancelDeceiptDate) {
		this.cancelDeceiptDate = cancelDeceiptDate;
	}

	public Date getCancelDeceiptDate() {
		return cancelDeceiptDate;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public ShipmentDetail getShipmentDetail1() {
		return shipmentDetail1;
	}

	public void setShipmentDetail1(ShipmentDetail shipmentDetail1) {
		this.shipmentDetail1 = shipmentDetail1;
	}

	public ShipmentDetail getShipmentDetail2() {
		return shipmentDetail2;
	}

	public void setShipmentDetail2(ShipmentDetail shipmentDetail2) {
		this.shipmentDetail2 = shipmentDetail2;
	}

	public LogisticGroup getLogisticGroup() {
		return logisticGroup;
	}

	public void setLogisticGroup(LogisticGroup logisticGroup) {
		this.logisticGroup = logisticGroup;
	}

	public DriverAccount getDriver() {
		return driver;
	}

	public void setDriver(DriverAccount driver) {
		this.driver = driver;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId())
				.append("logisticGroupId", getLogisticGroupId()).append("shipmentId", getShipmentId())
				.append("shipmentDetailId1", getShipmentDetailId1()).append("shipmentDetailId2", getShipmentDetailId2())
				.append("driverId", getDriverId()).append("pickupAssignId", getPickupAssignId())
				.append("containerNo1", getContainerNo1()).append("containerNo2", getContainerNo2())
				.append("truckNo", getTruckNo()).append("chassisNo", getChassisNo())
				.append("yardPosition1", getYardPosition1()).append("yardPosition2", getYardPosition2())
				.append("status", getStatus()).append("receiptDate", getReceiptDate())
				.append("gateinDate", getGateinDate()).append("gateoutDate", getGateoutDate())
				.append("cancelDeceiptDate", getCancelDeceiptDate()).toString();
	}
}