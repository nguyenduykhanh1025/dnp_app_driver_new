package vn.com.irtech.eport.api.controller.edo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.dto.EdoPublicRes;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.common.utils.bean.BeanUtils;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;

/**
 * @author Trong Hieu
 *
 */
@Controller
@RequestMapping("/edo/lookup")
public class EdoLookupController extends BaseController {

	@Autowired
	private ICatosApiService catosApiService;

	@Autowired
	private IEdoService edoService;

	@PostMapping()
	@CrossOrigin("*")
	@ResponseBody
	public AjaxResult getEdoByBillOrContainer(@RequestBody PageAble<Edo> param) {

		// Pagination
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());

		Edo edoInput = param.getData();
		if (edoInput == null) {
			return error("Không tìm thấy edo theo bill/container đã nhập.");
		}

		if (StringUtils.isEmpty(edoInput.getBillOfLading()) && StringUtils.isEmpty(edoInput.getContainerNumber())) {
			return error("Không tìm thấy edo theo bill/container đã nhập.");
		}

		AjaxResult ajaxResult = AjaxResult.success();
		// Edo object passing bl no and container no from client to search
		Edo edoParam = new Edo();
		Map<String, Object> params = new HashMap<>();
		params.put("blNo", edoInput.getBillOfLading());
		params.put("containerNo", edoInput.getContainerNumber());
		edoParam.setParams(params);
		// Query List edos by bill of lading and container no
		List<Edo> edos = edoService.selectEdoListForReport(edoParam);
		// return data table empty to client if result query = 0
		if (CollectionUtils.isEmpty(edos)) {
			ajaxResult.put("edos", getDataTable(new ArrayList<>()));
			return ajaxResult;
		}

		String blNo = edos.get(0).getBillOfLading(); // it's always the same => get first bill for default
		Map<String, ContainerInfoDto> cntrInfoMap = getContainerInfoMap(blNo);

		// Add extra data from catos into edo list and filter data return to client
		List<EdoPublicRes> edoListResult = new ArrayList<>();
		for (Edo edo : edos) {
			EdoPublicRes edoPublicRes = new EdoPublicRes();
			BeanUtils.copyBeanProp(edoPublicRes, edo);
			// Get container info from catos mapping by container no
			ContainerInfoDto cntrInfo = cntrInfoMap.get(edoPublicRes.getContainerNumber());
			// Check if cntr info not null ==> set gate out date and remark from catos into
			// edo public res
			if (cntrInfo != null) {
				edoPublicRes.setRemark(cntrInfo.getRemark()); // edo remark
				edoPublicRes.setGateOutDate(cntrInfo.getOutDate());
				edoPublicRes.setGateInDate(cntrInfo.getInDate());
				edoPublicRes.setCntrState(cntrInfo.getCntrState());
				edoPublicRes.setLocation(cntrInfo.getLocation());
			}
			edoListResult.add(edoPublicRes);
		}
		ajaxResult.put("edos", getDataTable(edoListResult));
		return ajaxResult;
	}

	private Map<String, ContainerInfoDto> getContainerInfoMap(String blNo) {
		List<ContainerInfoDto> cntrInfos = catosApiService.getContainerInfoListByBlNo(blNo);
		// List<ContainerInfoDto> cntrInfos =
		// catosApiService.getContainerInfoListByBlNo(blNo);
		// Map oject store container info data by key container no
		Map<String, ContainerInfoDto> cntrInfoMap = new HashMap<>();
		if (CollectionUtils.isNotEmpty(cntrInfos)) {
			for (ContainerInfoDto cntrInfo : cntrInfos) {
				cntrInfoMap.put(cntrInfo.getCntrNo(), cntrInfo);
			}
		}
		return cntrInfoMap;
	}
}
