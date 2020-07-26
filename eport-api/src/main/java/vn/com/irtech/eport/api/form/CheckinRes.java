package vn.com.irtech.eport.api.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckinRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sessionId;

	private List<PickupHistoryDataRes> data;
	
	public CheckinRes() {
		data = new ArrayList<PickupHistoryDataRes>();
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public List<PickupHistoryDataRes> getData() {
		return data;
	}

	public void setData(List<PickupHistoryDataRes> data) {
		this.data = data;
	}

}
