/**
 * 
 */
package vn.com.irtech.eport.web.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Trong Hieu
 *
 */
public class SensorResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String gateId;
	
	private List<Integer> sensors;

	public String getGateId() {
		return gateId;
	}

	public void setGateId(String gateId) {
		this.gateId = gateId;
	}

	public List<Integer> getSensors() {
		return sensors;
	}

	public void setSensors(List<Integer> sensors) {
		this.sensors = sensors;
	}

}
