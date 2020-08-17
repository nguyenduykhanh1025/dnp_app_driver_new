package vn.com.irtech.eport.carrier.dto;

import vn.com.irtech.eport.carrier.domain.Edo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

public class SeparateHouseBillReq implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotBlank
  private String houseBill;

  @NotBlank
  private String consignee2;

  @NotEmpty
  private List<Long> edoIds;

  public String getHouseBill() {
    return houseBill;
  }

  public void setHouseBill(String houseBill) {
    this.houseBill = houseBill;
  }

  public String getConsignee2() {
    return consignee2;
  }

  public void setConsignee2(String consignee2) {
    this.consignee2 = consignee2;
  }

  public List<Long> getEdoIds() {
    return edoIds;
  }

  public void setEdoIds(List<Long> edoIds) {
    this.edoIds = edoIds;
  }
}
