package vn.com.irtech.eport.logistic.dto;

import java.io.Serializable;
import java.util.List;

public class ProcessJsonData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Long> shipmentDetailIds;
	
	private List<Long> prePickupContIds;

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
	
}
