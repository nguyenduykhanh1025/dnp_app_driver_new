package vn.com.irtech.eport.logistic.dto;

import java.io.Serializable;
import java.util.List;

public class ShipmentWaitExec implements Serializable {

	private static final long serialVersionUID = 1L;

	private String process;

	private Long blNo;

	private String ship;

	private String processStatus;

	private List<Long> shipmentDetailIds;

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public Long getBlNo() {
		return blNo;
	}

	public void setBlNo(Long blNo) {
		this.blNo = blNo;
	}

	public String getShip() {
		return ship;
	}

	public void setShip(String ship) {
		this.ship = ship;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public List<Long> getShipmentDetailIds() {
		return shipmentDetailIds;
	}

	public void setShipmentDetailIds(List<Long> shipmentDetailIds) {
		this.shipmentDetailIds = shipmentDetailIds;
	}

}
