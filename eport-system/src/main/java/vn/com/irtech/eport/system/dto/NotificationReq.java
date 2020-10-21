package vn.com.irtech.eport.system.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class NotificationReq implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String title;
	
	private String msg;
	
	private String type;
	
	private String link;
	
	private Integer priority;
	
	private List<Map<String, Object>> data;
	
	private Object gateData;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public Object getGateData() {
		return gateData;
	}

	public void setGateData(Object gateData) {
		this.gateData = gateData;
	}
	
}
