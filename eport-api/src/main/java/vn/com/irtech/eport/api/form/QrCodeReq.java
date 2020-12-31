package vn.com.irtech.eport.api.form;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import vn.com.irtech.eport.logistic.domain.PickupHistory;

public class QrCodeReq implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private List<Long> pickupHistoryIds;

	@NotEmpty
	private List<PickupHistory> pickupHistories;

	public List<Long> getPickupHistoryIds() {
		return pickupHistoryIds;
	}

	public void setPickupHistoryIds(List<Long> pickupHistoryIds) {
		this.pickupHistoryIds = pickupHistoryIds;
	}

	public List<PickupHistory> getPickupHistories() {
		return pickupHistories;
	}

	public void setPickupHistories(List<PickupHistory> pickupHistories) {
		this.pickupHistories = pickupHistories;
	}
}
