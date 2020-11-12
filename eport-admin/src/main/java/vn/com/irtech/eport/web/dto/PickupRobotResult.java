package vn.com.irtech.eport.web.dto;

import java.io.Serializable;

public class PickupRobotResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String containerNo;
	
	private String yardPosition;
	
	private String result;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getYardPosition() {
		return yardPosition;
	}

	public void setYardPosition(String yardPosition) {
		this.yardPosition = yardPosition;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
