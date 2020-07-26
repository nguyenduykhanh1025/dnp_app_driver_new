package vn.com.irtech.eport.api.form;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

public class CheckinReq implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private List<Long> pickupHistoryIds;

	public List<Long> getPickupHistoryIds() {
		return pickupHistoryIds;
	}

	public void setPickupHistoryIds(List<Long> pickupHistoryIds) {
		this.pickupHistoryIds = pickupHistoryIds;
	}
}
