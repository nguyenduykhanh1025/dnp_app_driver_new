package vn.com.irtech.api.entity;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * Pickup history Object
 * 
 * @author baohv
 * @date 2020-06-27
 */
public class PickupHistoryEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/** id */
	private Long id;

	/** Logistic Group */
	private Long logisticGroupId;

	/** Shipment id */
	private Long shipmentId;

	/** Shipment detail id 1 */
	private Long shipmentDetailId;

	/** Driver id */
	private Long driverId;

	/** Assign ID */
	@NotEmpty
	private Long pickupAssignId;

	/** Container no */
	private String containerNo;

	/** Truck no */
	@NotEmpty
	private String truckNo;

	/** Chassis no */
	@NotEmpty
	private String chassisNo;

	/** Area */
	private String area;

	/** Bay */
	private String bay;

	/** Block */
	private String block;

	/** Line */
	private String line;

	/** Tier */
	private String tier;

	/** Status */
	private Integer status;

	/** Receipt date */
	private Date receiptDate;

	/** Gatein date */
	private Date gateinDate;

	/** Gateout date */
	private Date gateoutDate;

	/** cancel Receipt Date */
	private Date cancelDeceiptDate;

	/** planning Date */
	private Date planningDate;

	/** Process Order Id */
	private Long processOrderId;


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fromDate;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date toDate;

	private Integer serviceType;

	private String blNo;

	private String bookingNo;

	private String sztp;

	private String vslNm;

	private String voyNo;

	private String driverName;

	private String driverPhoneNumber;

	private String logisticGroupName;
	
	private Double x;
	
	private Double y;

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

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBookingNo(String bookingNo) {
		this.bookingNo = bookingNo;
	}

	public String getBookingNo() {
		return bookingNo;
	}

	public void setSztp(String sztp) {
		this.sztp = sztp;
	}

	public String getSztp() {
		return sztp;
	}

	public void setVslNm(String vslNm) {
		this.vslNm = vslNm;
	}

	public String getVslNm() {
		return vslNm;
	}

	public void setVoyNo(String voyNo) {
		this.voyNo = voyNo;
	}

	public String getVoyNo() {
		return voyNo;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverPhoneNumber(String driverPhoneNumber) {
		this.driverPhoneNumber = driverPhoneNumber;
	}

	public String getDriverPhoneNumber() {
		return driverPhoneNumber;
	}

	public void setLogisticGroupName(String logisticGroupName) {
		this.logisticGroupName = logisticGroupName;
	}

	public String getLogisticGroupName() {
		return logisticGroupName;
	}

	public Long getShipmentDetailId() {
		return shipmentDetailId;
	}

	public void setShipmentDetailId(Long shipmentDetailId) {
		this.shipmentDetailId = shipmentDetailId;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getBay() {
		return bay;
	}

	public void setBay(String bay) {
		this.bay = bay;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public Date getPlanningDate() {
		return planningDate;
	}

	public void setPlanningDate(Date planningDate) {
		this.planningDate = planningDate;
	}

	public Long getProcessOrderId() {
		return this.processOrderId;
	}

	public void setProcessOrderId(Long processOrderId) {
		this.processOrderId = processOrderId;
	}
	
	public void setX(Double x) {
		this.x = x;
	}
	
	public Double getX() {
		return x;
	}
	
	public void setY(Double y) {
		this.y = y;
	}
	
	public Double getY() {
		return y;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", getId())
				.append("logisticGroupId", getLogisticGroupId()).append("shipmentId", getShipmentId())
				.append("shipmentDetailId", getShipmentDetailId()).append("driverId", getDriverId())
				.append("pickupAssignId", getPickupAssignId()).append("containerNo", getContainerNo())
				.append("truckNo", getTruckNo()).append("chassisNo", getChassisNo()).append("area", getArea())
				.append("status", getStatus()).append("receiptDate", getReceiptDate())
				.append("gateinDate", getGateinDate()).append("gateoutDate", getGateoutDate())
				.append("cancelDeceiptDate", getCancelDeceiptDate()).append("fromDate", getFromDate())
				.append("toDate", getToDate()).append("serviceType", getServiceType()).append("blNo", getBlNo())
				.append("bookingNo", getBookingNo()).append("sztp", getSztp()).append("vslNm", getVslNm())
				.append("voyNo", getVoyNo()).append("driverName", getDriverName())
				.append("driverPhoneNumber", getDriverPhoneNumber()).append("logisticGroupName", getLogisticGroupName())
				.append("processOrderId", getProcessOrderId())
				.toString();
	}
}