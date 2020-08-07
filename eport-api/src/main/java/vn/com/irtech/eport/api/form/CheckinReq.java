package vn.com.irtech.eport.api.form;

import java.io.Serializable;
import java.util.List;

public class CheckinReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sessionId;

	private List<PickupHistoryDataRes> data;
	
	private List<MeasurementDataReq> input;
	
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

	public List<MeasurementDataReq> getInput() {
		return input;
	}

	public void setInput(List<MeasurementDataReq> input) {
		this.input = input;
	}
}
