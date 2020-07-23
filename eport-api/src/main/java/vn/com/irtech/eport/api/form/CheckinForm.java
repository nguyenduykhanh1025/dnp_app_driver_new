package vn.com.irtech.eport.api.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

public class CheckinForm {
	
	@NotEmpty
	private List<Long> pickupHistoryIds;

	public List<Long> getPickupHistoryIds() {
		return pickupHistoryIds;
	}

	public void setPickupHistoryIds(List<Long> pickupHistoryIds) {
		this.pickupHistoryIds = pickupHistoryIds;
	}
}
