package vn.com.irtech.eport.logistic.dto;

import java.io.Serializable;
import java.util.List;

public class ProcessJsonData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** Array Long shipment detail id container need to shifting */
	private List<Long> shipmentDetailIds;
	
	/** Array Long shipment detail id container need to pick on demand */
	private List<Long> prePickupContIds;
	
	private String vslName;
	
	private String voyCarrier;
	
	private String containers;
	
	/** Number of times process retry send to robot after error */
	private Integer retryCount;

	public List<Long> getShipmentDetailIds() {
		return shipmentDetailIds;
	}

	public void setShipmentDetailIds(List<Long> shipmentDetailIds) {
		this.shipmentDetailIds = shipmentDetailIds;
	}

	public List<Long> getPrePickupContIds() {
		return prePickupContIds;
	}

	public void setPrePickupContIds(List<Long> prePickupContIds) {
		this.prePickupContIds = prePickupContIds;
	}

	public String getVslName() {
		return vslName;
	}

	public void setVslName(String vslName) {
		this.vslName = vslName;
	}

	public String getVoyCarrier() {
		return voyCarrier;
	}

	public void setVoyCarrier(String voyCarrier) {
		this.voyCarrier = voyCarrier;
	}

	public String getContainers() {
		return containers;
	}

	public void setContainers(String containers) {
		this.containers = containers;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
}
