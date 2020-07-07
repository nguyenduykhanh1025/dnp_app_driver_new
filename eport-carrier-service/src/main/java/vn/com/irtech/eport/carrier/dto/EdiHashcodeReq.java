package vn.com.irtech.eport.carrier.dto;

import java.io.Serializable;
import java.util.List;

public class EdiHashcodeReq implements Serializable {

	private static final long serialVersionUID = 1L;

	private String privateKey;

	private List<EdiDataReq> data;

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public List<EdiDataReq> getData() {
		return data;
	}

	public void setData(List<EdiDataReq> data) {
		this.data = data;
	}

}
