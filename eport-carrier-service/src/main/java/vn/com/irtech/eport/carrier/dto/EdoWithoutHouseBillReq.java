package vn.com.irtech.eport.carrier.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class EdoWithoutHouseBillReq implements Serializable {

  private static final long serialVersionUID = 1L;

  private String billOfLading;

  private String consignee;

  private String carrierCode;

  private String expiredDem;

  public String getBillOfLading() {
    return billOfLading;
  }

  public void setBillOfLading(String billOfLading) {
    this.billOfLading = billOfLading;
  }

  public String getConsignee() {
    return consignee;
  }

  public void setConsignee(String consignee) {
    this.consignee = consignee;
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
