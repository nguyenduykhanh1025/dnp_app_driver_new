package vn.com.irtech.eport.logistic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.dto.HouseBillRes;
import vn.com.irtech.eport.carrier.dto.HouseBillSearchReq;
import vn.com.irtech.eport.carrier.dto.SeparateHouseBillReq;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.carrier.dto.EdoWithoutHouseBillReq;
import vn.com.irtech.eport.logistic.domain.EdoHouseBill;
import vn.com.irtech.eport.logistic.service.IEdoHouseBillService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/logistic/shipmentSeparating")
public class LogisticShipmentSeparatingController extends LogisticBaseController {

  private final String PREFIX = "logistic/shipmentSeparating";

  @Autowired
  private IEdoService edoService;

  @Autowired
  private IEdoHouseBillService edoHouseBillService;

  @GetMapping()
  public String getShipmentSeparatingView() {
    return PREFIX + "/index";
  }

  @GetMapping("/separate")
  public String separate() {
    return PREFIX + "/separate";
  }

  @GetMapping("/addCont/{houseBillNo}")
  public String addCont(@PathVariable("houseBillNo") String houseBillNo, ModelMap mmap) {
    EdoHouseBill query = new EdoHouseBill();
    query.setHouseBillNo(houseBillNo);
    query.setLogisticGroupId(getGroup().getId());

    EdoHouseBill edoHouseBill = edoHouseBillService.selectFirstEdoHouseBill(query);

    if (edoHouseBill == null){
      edoHouseBill = new EdoHouseBill();
    }

    mmap.put("houseBill", edoHouseBill);

    return PREFIX + "/addCont";
  }

  @PostMapping("/separate/search")
  @ResponseBody
  public TableDataInfo searchEdo(@RequestBody PageAble<EdoWithoutHouseBillReq> param) {
    List<Edo> dataList = new ArrayList<>();
    startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
    EdoWithoutHouseBillReq dataSearch = param.getData();
    if (dataSearch == null) {
      return getDataTable(dataList);
    }
    dataList = edoService.selectListEdoWithoutHouseBillId(dataSearch);
    return getDataTable(dataList);
  }

  @PostMapping("/separate/execute")
  @ResponseBody
  @Transactional
  public AjaxResult separate(@RequestBody SeparateHouseBillReq req) {

    // Check exists house bill
    EdoHouseBill edoHouseBill = new EdoHouseBill();
    edoHouseBill.setHouseBillNo(req.getHouseBill());
    edoHouseBill.setHouseBillNo2(req.getHouseBill());
    edoHouseBill.setLogisticGroupId(getUser().getGroupId());

    if (edoHouseBillService.checkExistHouseBill(edoHouseBill) > 0){
      return error("House bill đã tồn tại, vui lòng nhập giá trị khác!");
    }

    // Get list edo
    List<Edo> edos = new ArrayList<>();
    for (Long edoId : req.getEdoIds()){
      Edo edo = edoService.selectEdoById(edoId);
      if (edo == null){
        return error("Có lỗi hệ thống xảy ra, vui lòng liên hệ người phụ trách!");
      }
      edos.add(edo);
    }

    // Insert data to edo house bill
    edoHouseBillService.insertListEdoHouseBill(edos, req.getHouseBill(), req.getConsignee2(), getUser());

    return success();
  }

  @PostMapping("/houseBill/list")
  @ResponseBody
  public TableDataInfo listHouseBill(@RequestBody PageAble<HouseBillSearchReq> param) {
    startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
    List<HouseBillRes> dataList = edoHouseBillService.selectListHouseBillRes(param.getData());
    return getDataTable(dataList);
  }

  @PostMapping("/houseBill/detail")
  @ResponseBody
  public TableDataInfo detailHouseBill(@RequestBody PageAble<String> param) {
    startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());

    EdoHouseBill edoHouseBill = new EdoHouseBill();
    edoHouseBill.setHouseBillNo(param.getData());

    List<EdoHouseBill> dataList = edoHouseBillService.selectEdoHouseBillList(edoHouseBill);
    return getDataTable(dataList);
  }

}
