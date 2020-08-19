package vn.com.irtech.eport.carrier.dto;

import java.io.Serializable;

public class EdoWithoutHouseBillReq implements Serializable {

  private static final long serialVersionUID = 1L;

  private String billOfLading;

  private String orderNumber;

  private String carrierCode;

  private String expiredDem;

  public String getBillOfLading() {
    return billOfLading;
  }

  public void setBillOfLading(String billOfLading) {
    this.billOfLading = billOfLading;
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  public String getCarrierCode() {
    return carrierCode;
  }

  public void setCarrierCode(String carrierCode) {
    this.carrierCode = carrierCode;
  }

  public String getExpiredDem() {
    return expiredDem;
  }

  public void setExpiredDem(String expiredDem) {
    this.expiredDem = expiredDem;
  }
}
