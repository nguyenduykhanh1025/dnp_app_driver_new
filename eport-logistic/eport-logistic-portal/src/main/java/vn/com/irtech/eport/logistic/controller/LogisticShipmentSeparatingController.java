package vn.com.irtech.eport.logistic.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoHouseBill;
import vn.com.irtech.eport.carrier.dto.EdoWithoutHouseBillReq;
import vn.com.irtech.eport.carrier.dto.HouseBillRes;
import vn.com.irtech.eport.carrier.dto.HouseBillSearchReq;
import vn.com.irtech.eport.carrier.dto.SeparateHouseBillReq;
import vn.com.irtech.eport.carrier.service.IEdoHouseBillService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.system.dto.PartnerInfoDto;

@Controller
@RequestMapping("/logistic/shipmentSeparating")
public class LogisticShipmentSeparatingController extends LogisticBaseController {

	private final String PREFIX = "logistic/shipmentSeparating";

	@Autowired
	private IEdoService edoService;

	@Autowired
	private IEdoHouseBillService edoHouseBillService;

//	@Autowired
//	private ICarrierGroupService carrierGroupService;

	@Autowired
	private ICatosApiService catosApiService;

	@GetMapping()
	public String getShipmentSeparatingView() {
		return PREFIX + "/index";
	}

	@GetMapping("/separate")
	public String separate(ModelMap mmap) {
		List<String> oprCodeList = catosApiService.getOprCodeList();
		mmap.put("carrierCodes", oprCodeList);
		return PREFIX + "/separate";
	}

	@GetMapping("/addCont/{houseBillNo}")
	public String addCont(@PathVariable("houseBillNo") String houseBillNo, ModelMap mmap) {
		EdoHouseBill query = new EdoHouseBill();
		query.setHouseBillNo(houseBillNo);
		query.setLogisticGroupId(getGroup().getId());

		EdoHouseBill edoHouseBill = edoHouseBillService.selectFirstEdoHouseBill(query);

		if (edoHouseBill == null) {
			edoHouseBill = new EdoHouseBill();
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Map<String, Object> map = new HashMap<>();
		map.put("expiredDem", simpleDateFormat.format(edoHouseBill.getEdo().getExpiredDem()));
		edoHouseBill.setParams(map);

		mmap.put("houseBill", edoHouseBill);

		return PREFIX + "/addCont";
	}

	@PostMapping("/separate/search")
	@ResponseBody
	public TableDataInfo searchEdo(@RequestBody PageAble<EdoWithoutHouseBillReq> param) {
		List<Edo> dataList = null;
		// startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		EdoWithoutHouseBillReq dataSearch = param.getData();
		if (dataSearch == null) {
			return getDataTable(new ArrayList<>());
		}
		dataList = edoService.selectListEdoWithoutHouseBillId(dataSearch);
		if (CollectionUtils.isEmpty(dataList)) {
			dataList = edoService.selectListEdoWithHouseBill(dataSearch);
		}
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

		PartnerInfoDto partner = catosApiService.getGroupNameByTaxCode(req.getConsignee2TaxCode());
		if (partner == null || partner.getGroupName() == null) {
			return error("Mã số thuế không tồn tại.");
		}
		req.setConsignee2(partner.getGroupName());

		if (edoHouseBillService.checkExistHouseBill(edoHouseBill) > 0) {
			return error("House bill đã tồn tại, vui lòng nhập giá trị khác!");
		}

		// Get list edo
		String edoIds = StringUtils.join(req.getEdoIds(), ",");
		List<Edo> edos = edoService.selectEdoByIds(edoIds);

		// Insert data to edo house bill
		edoHouseBillService.insertListEdoHouseBill(edos, req.getHouseBill(), req.getConsignee2(),
				req.getConsignee2TaxCode(), req.getOrderNumber(), getUser().getGroupId(), getUser().getId(),
				getUser().getUserName());

		return success();
	}

	@PostMapping("/separate/add")
	@ResponseBody
	@Transactional
	public AjaxResult addContainer(@RequestBody SeparateHouseBillReq req) {

		// Check exists house bill
		EdoHouseBill edoHouseBill = new EdoHouseBill();
		edoHouseBill.setHouseBillNo(req.getHouseBill());
		edoHouseBill.setHouseBillNo2(req.getHouseBill());
		edoHouseBill.setLogisticGroupId(getUser().getGroupId());

		PartnerInfoDto partner = catosApiService.getGroupNameByTaxCode(req.getConsignee2TaxCode());
		if (partner == null || partner.getGroupName() == null) {
			return error("Mã số thuế không tồn tại.");
		}
		req.setConsignee2(partner.getGroupName());

		// Get list edo
		String edoIds = StringUtils.join(req.getEdoIds(), ",");
		List<Edo> edos = edoService.selectEdoByIds(edoIds);

		// Insert data to edo house bill
		edoHouseBillService.insertListEdoHouseBill(edos, req.getHouseBill(), req.getConsignee2(),
				req.getConsignee2TaxCode(), req.getOrderNumber(), getUser().getGroupId(), getUser().getId(),
				getUser().getUserName());

		return success();
	}

	@PostMapping("/houseBill/list")
	@ResponseBody
	public TableDataInfo listHouseBill(@RequestBody PageAble<HouseBillSearchReq> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		HouseBillSearchReq houseBillSearchReq = param.getData();
		if (houseBillSearchReq == null) {
			houseBillSearchReq = new HouseBillSearchReq();
		}
		houseBillSearchReq.setLogisticGroupId(getUser().getGroupId());
		List<HouseBillRes> dataList = edoHouseBillService.selectListHouseBillRes(houseBillSearchReq);
		return getDataTable(dataList);
	}

	@PostMapping("/houseBill/detail")
	@ResponseBody
	public AjaxResult detailHouseBill(String houseBillNo) {

		if (StringUtils.isEmpty(houseBillNo)) {
			return error("Quý khách chưa chọn house bill no.");
		}

		EdoHouseBill edoHouseBill = new EdoHouseBill();
		edoHouseBill.setHouseBillNo(houseBillNo);

		List<EdoHouseBill> dataList = edoHouseBillService.selectEdoHouseBillList(edoHouseBill);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("houseBillDetail", getDataTable(dataList));
		return ajaxResult;
	}

	@GetMapping("/consignees")
	@ResponseBody
	public List<JSONObject> getConsignees(String searchKey) {
		PartnerInfoDto partnerReq = new PartnerInfoDto();
		partnerReq.setGroupName(searchKey);
		List<PartnerInfoDto> partners = catosApiService.getListConsigneeWithTaxCode(partnerReq);
		List<JSONObject> jsonObjects = new ArrayList<>();
		for (PartnerInfoDto partner : partners) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", partner.getTaxCode());
			jsonObject.put("text", partner.getGroupName());
			jsonObjects.add(jsonObject);
		}
		return jsonObjects;
	}

	@GetMapping("/taxcode/{taxCode}/consignee")
	@ResponseBody
	public AjaxResult getConsgineeByTaxCode(@PathVariable String taxCode) {
		PartnerInfoDto partner = catosApiService.getGroupNameByTaxCode(taxCode);
		if (partner != null && partner.getGroupName() != null) {
			AjaxResult ajaxResult = AjaxResult.success();
			ajaxResult.put("companyName", partner.getGroupName());
			return ajaxResult;
		}
		return error();
	}

	@PostMapping("/houseBill/release")
	@ResponseBody
	public AjaxResult releaseHouseBill(String houseBillNo) {

		if (StringUtils.isEmpty(houseBillNo)) {
			return error("Quý khách chưa chọn house bill no.");
		}

		EdoHouseBill edoHouseBill = new EdoHouseBill();
		edoHouseBill.setHouseBillNo(houseBillNo);
		edoHouseBill.setReleaseFlg(true);

		if (edoHouseBillService.updateEdoHouseBillByCondition(edoHouseBill) != 0) {
			return success();
		}
		return error();
	}

	@DeleteMapping("/houseBill/container")
	@ResponseBody
	public AjaxResult deleteContainer(String houseBillIds) {
		if (StringUtils.isEmpty(houseBillIds)) {
			return error("Quý khách chưa chọn container muốn xóa.");
		}

		List<EdoHouseBill> edoHouseBills = edoHouseBillService.selectEdoHouseBillByIds(houseBillIds,
				getUser().getGroupId());
		if (CollectionUtils.isEmpty(edoHouseBills)) {
			return error("Không tìm thấy container cần xóa trong hệ thống.");
		}

		String ids = "";
		String houseBillNos = "";
		for (EdoHouseBill edoHouseBill : edoHouseBills) {
			if (edoHouseBill.getReleaseFlg()) {
				return error("House bill đã được phát hành không thể xóa.");
			}
			ids += edoHouseBill.getId() + ",";
			houseBillNos += edoHouseBill.getHouseBillNo() + ",";
		}
		ids = ids.substring(0, ids.length() - 1);
		houseBillNos = houseBillNos.substring(0, houseBillNos.length() - 1);

		edoHouseBillService.deleteEdoHouseBillByIds(ids);

		// Get edo house bill previous
		Map<String, Object> paramHouseBill = new HashMap<>();
		paramHouseBill.put("houseBillNos", Convert.toStrArray(houseBillNos));
		EdoHouseBill edoHouseBillParam = new EdoHouseBill();
		edoHouseBillParam.setLogisticGroupId(getUser().getGroupId());
		edoHouseBillParam.setParams(paramHouseBill);
		List<EdoHouseBill> edoHouseBillsOld = edoHouseBillService.selectEdoHouseBillByHouseBillNo2s(edoHouseBillParam);

		// Check if previous bill is house bill
		if (CollectionUtils.isNotEmpty(edoHouseBillsOld)) {
			// Set house bill no 2 to blank
			for (EdoHouseBill edoHouseBill : edoHouseBillsOld) {
				edoHouseBill.setHouseBillNo2(null);
				edoHouseBillService.updateOldHouseBillToNewHouseBill(edoHouseBill);

				// Update Edo
				Edo edo = new Edo();
				edo.setId(edoHouseBill.getEdoId());
				edo.setHouseBillId(edoHouseBill.getId());
				edoService.updateEdo(edo);
			}
		} else {
			// Set house bill id of id to null
			Map<String, Object> paramEdo = new HashMap<>();
			paramEdo.put("houseBillIds", Convert.toStrArray(ids));
			paramEdo.put("houseBillIdNull", true);
			Edo edoParam = new Edo();
			edoParam.setParams(paramEdo);
			edoService.updateEdoByCondition(edoParam);
		}

		return success();
	}
}
