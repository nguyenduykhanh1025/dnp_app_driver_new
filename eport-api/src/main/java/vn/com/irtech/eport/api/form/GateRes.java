package vn.com.irtech.eport.api.form;

import java.io.Serializable;

public class GateRes implements Serializable {

	private static final long serialVersionUID = 1L;

	private String status;

	private String result;

	private String msg;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
