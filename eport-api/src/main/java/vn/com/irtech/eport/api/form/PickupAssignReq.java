package vn.com.irtech.eport.api.form;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

public class PickupAssignReq  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotBlank()
	private String driverIds;
	
	private String deliveryAddress;
	
	private String deliveryMobileNumber;
	
	private String deliveryRemark;

	public String getDriverIds() {
		return driverIds;
	}

	public void setDriverIds(String driverIds) {
		this.driverIds = driverIds;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDeliveryMobileNumber() {
		return deliveryMobileNumber;
	}

	public void setDeliveryMobileNumber(String deliveryMobileNumber) {
		this.deliveryMobileNumber = deliveryMobileNumber;
	}

	public String getDeliveryRemark() {
		return deliveryRemark;
	}

	public void setDeliveryRemark(String deliveryRemark) {
		this.deliveryRemark = deliveryRemark;
	}
	
}
