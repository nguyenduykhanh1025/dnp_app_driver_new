package vn.com.irtech.eport.edo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.dto.HouseBillRes;
import vn.com.irtech.eport.carrier.dto.HouseBillSearchReq;
import vn.com.irtech.eport.carrier.service.IEdoHouseBillService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;

@Controller
@RequestMapping("/edo/separated-bill")
public class SeparatedBillManagementController extends BaseController {

	private final String PREFIX = "edo/separatedBill";

	@Autowired
	private IEdoService edoService;

	@Autowired
	private IEdoHouseBillService edoHouseBillService;

	@Autowired
	private ICatosApiService catosApiService;

	@GetMapping()
	public String getShipmentSeparatingView() {
		return PREFIX + "/index";
	}

	@PostMapping("/houseBill/list")
	@ResponseBody
	public TableDataInfo listHouseBill(@RequestBody PageAble<HouseBillSearchReq> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		HouseBillSearchReq houseBillSearchReq = param.getData();
		if (houseBillSearchReq == null) {
			houseBillSearchReq = new HouseBillSearchReq();
		}
		houseBillSearchReq.setReleaseFlg(true);
		List<HouseBillRes> dataList = edoHouseBillService.selectListHouseBillRes(houseBillSearchReq);
		return getDataTable(dataList);
	}

	@PostMapping("/houseBill/detail")
	@ResponseBody
	public AjaxResult detailHouseBill(@RequestBody PageAble<String> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		String houseBill = param.getData();
		if (StringUtils.isEmpty(houseBill)) {
			return error("Bạn chưa chọn house bill cần nhập.");
		}
		List<Edo> dataList = edoService.selectEdoListByHouseBill(houseBill);
		if (CollectionUtils.isEmpty(dataList)) {
			return error("Không tìm thấy thông tin house bill.");
		}
		Map<String, ContainerInfoDto> cntrInfoMap = getContainerInfoMap(dataList.get(0).getBillOfLading());
		for (Edo edo : dataList) {
			// Get container info from catos mapping by container no
			ContainerInfoDto cntrInfo = cntrInfoMap.get(edo.getContainerNumber());
			Map<String, Object> extraData = new HashMap<>();
			if (cntrInfo != null) {
				extraData.put("jobOrderNo", cntrInfo.getJobOdrNo2());
				extraData.put("gateOutDate", cntrInfo.getOutDate());
				extraData.put("status", cntrInfo.getCntrState());
			}
			edo.setParams(extraData);
		}

		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("houseBillDetail", getDataTable(dataList));
		return ajaxResult;
	}

	private Map<String, ContainerInfoDto> getContainerInfoMap(String blNo) {
		List<ContainerInfoDto> cntrInfos = catosApiService.getContainerInfoListByBlNo(blNo);
		// List<ContainerInfoDto> cntrInfos =
		// catosApiService.getContainerInfoListByBlNo(blNo);
		// Map object store container info data by key container no
		Map<String, ContainerInfoDto> cntrInfoMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(cntrInfos)) {
			for (ContainerInfoDto cntrInfo : cntrInfos) {
				cntrInfoMap.put(cntrInfo.getCntrNo(), cntrInfo);
			}
		}
		return cntrInfoMap;
	}

	@PostMapping("/order/lock")
	@ResponseBody
	public AjaxResult lockEdo(String edoIds) {
		if (StringUtils.isEmpty(edoIds)) {
			return error("Bạn chưa chọn container.");
		}
		Edo edo = new Edo();
		edo.setReleaseStatus("Y");
		Map<String, Object> params = new HashMap<>();
		params.put("edoIds", Convert.toStrArray(edoIds));
		edo.setParams(params);
		edoService.updateEdoByCondition(edo);
		return success("Xác nhận khoá làm lệnh thành công.");
	}

	@PostMapping("/order/unlock")
	@ResponseBody
	public AjaxResult unlockEdo(String edoIds) {
		if (StringUtils.isEmpty(edoIds)) {
			return error("Bạn chưa chọn container.");
		}
		Edo edo = new Edo();
		edo.setReleaseStatus("N");
		Map<String, Object> params = new HashMap<>();
		params.put("edoIds", Convert.toStrArray(edoIds));
		edo.setParams(params);
		edoService.updateEdoByCondition(edo);
		return success("Xác nhận mở khoá thành công.");
	}
}
