package vn.com.irtech.eport.logistic.domain;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@Excel(name = "Shipment detail id")
	private Long shipmentDetailId;

	/** Driver id */
	@Excel(name = "Driver id")
	private Long driverId;

	/** Assign ID */
	@Excel(name = "Assign ID")
	@NotEmpty
	private Long pickupAssignId;

	/** Container no */
	@Excel(name = "Container no")
	private String containerNo;

	/** Truck no */
	@Excel(name = "Truck no")
	@NotEmpty
	private String truckNo;

	/** Chassis no */
	@Excel(name = "Chassis no")
	@NotEmpty
	private String chassisNo;

	/** Area */
	@Excel(name = "Area")
	private String area;

	/** Bay */
	@Excel(name = "Bay")
	private String bay;

	/** Block */
	@Excel(name = "Block")
	private String block;

	/** Line */
	@Excel(name = "Line")
	private String line;

	/** Tier */
	@Excel(name = "Tier")
	private String tier;

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

	/** planning Date */
	@Excel(name = "planning Date", width = 30, dateFormat = "yyyy-MM-dd")
	private Date planningDate;

	/** Process Order Id */
	@Excel(name = "Process Order Id")
	private Long processOrderId;

	/** Gate Pass */
	@Excel(name = "Gate Pass")
	private String gatePass;

	private Shipment shipment;

	private ShipmentDetail shipmentDetail;

	private LogisticGroup logisticGroup;

	private DriverAccount driver;

	private PickupAssign pickupAssign;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date fromDate;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date toDate;

	private Integer serviceType;

	private String blNo;

	private String bookingNo;

	private String sztp;

	private Double distance;
	
	private Date updateLocationTime;
	
	private String vslNm;

	private String voyNo;

	private String driverName;

	private String driverPhoneNumber;

	private String logisticGroupName;
	
	private Double x;
	
	private Double y;
	
	private String jobOrderNo;
	
	private Boolean jobOrderFlg;
	
	private Integer loadableWgt;

	private Boolean locationUpdate;

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

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public ShipmentDetail getShipmentDetail() {
		return shipmentDetail;
	}

	public void setShipmentDetail(ShipmentDetail shipmentDetail) {
		this.shipmentDetail = shipmentDetail;
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

	public void setPickupAssign(PickupAssign pickupAssign) {
		this.pickupAssign = pickupAssign;
	}

	public PickupAssign getPickupAssign() {
		return pickupAssign;
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
	
	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Date getUpdateLocationTime() {
		return updateLocationTime;
	}

	public void setUpdateLocationTime(Date updateLocationTime) {
		this.updateLocationTime = updateLocationTime;
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

	public String getGatePass() {
		return this.gatePass;
	}

	public void setGatePass(String gatePass) {
		this.gatePass = gatePass;
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

	public String getJobOrderNo() {
		return jobOrderNo;
	}

	public void setJobOrderNo(String jobOrderNo) {
		this.jobOrderNo = jobOrderNo;
	}

	public Boolean getJobOrderFlg() {
		return jobOrderFlg;
	}

	public void setJobOrderFlg(Boolean jobOrderFlg) {
		this.jobOrderFlg = jobOrderFlg;
	}

	public Integer getLoadableWgt() {
		return loadableWgt;
	}

	public void setLoadableWgt(Integer loadableWgt) {
		this.loadableWgt = loadableWgt;
	}

	public Boolean getLocationUpdate() {
		return locationUpdate;
	}

	public void setLocationUpdate(Boolean locationUpdate) {
		this.locationUpdate = locationUpdate;
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
				.append("bookingNo", getBookingNo()).append("sztp", getSztp())
				.append("distance", getDistance()).append("updateLocationTime", getUpdateLocationTime())
				.append("vslNm", getVslNm()).append("voyNo", getVoyNo()).append("driverName", getDriverName())
				.append("driverPhoneNumber", getDriverPhoneNumber()).append("logisticGroupName", getLogisticGroupName())
				.append("processOrderId", getProcessOrderId()).append("gatePass",getGatePass())
				.append("jobOrderNo", getJobOrderNo()).append("jobOrderFlg",getJobOrderFlg())
				.append("loadableWgt", getLoadableWgt())
				.toString();
	}
}