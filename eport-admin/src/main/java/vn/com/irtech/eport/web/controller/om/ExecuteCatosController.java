package vn.com.irtech.eport.web.controller.om;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ShipmentWaitExec;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;

@Controller
@RequestMapping("/om/executeCatos")
public class ExecuteCatosController extends BaseController {
	private String prefix = "om/executeCatos";

	@Autowired
	private IShipmentDetailService shipmentDetailService;

	@GetMapping("/index")
	public String getViewexEcuteCatos() {
		return prefix + "/executeCatos";
	}

	/**
	 * Select list shipment detail wait robot execute or robot can't be execute,
	 * group by shipment id
	 * 
	 * @return
	 */
	@RequestMapping("/listWaitExec")
	@ResponseBody
	public TableDataInfo listShipment() {
		startPage();
		List<ShipmentWaitExec> result = shipmentDetailService.selectListShipmentWaitExec();
		return getDataTable(result);
	}

	/**
	 * Update list shipment detail
	 * 
	 * @param shipmentId
	 * @param processStatus
	 * @param mmap
	 * @return
	 */
	@GetMapping("/edit/{shipmentDetailIds}")
	public String edit(@PathVariable("shipmentDetailIds") Long[] shipmentDetailIds, ModelMap mmap) {
		mmap.put("shipmentDetailIds", shipmentDetailIds);
		List<ShipmentDetail> shipmentDetails = new ArrayList<ShipmentDetail>();
		for (Long id : shipmentDetailIds) {
			ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(id);
			if (shipmentDetail != null) {
				shipmentDetails.add(shipmentDetail);
			}
		}
		mmap.put("shipmentDetail", shipmentDetails);
		return prefix + "/edit";
	}

	/**
	 * Update process status of shipment details to "Y"
	 * 
	 * @param shipmentDetailIds
	 * @return
	 */
	@Log(title = "Đã làm lệnh", businessType = BusinessType.UPDATE)
	@PostMapping("/edit")
	@ResponseBody
	@Transactional
	public AjaxResult execCatos(@RequestParam("shipmentDetailIds[]") List<Long> shipmentDetailIds) {
		final String EXECUTED = "Y";
		if (CollectionUtils.isEmpty(shipmentDetailIds)) {
			return toAjax(true);
		}

		for (Long id : shipmentDetailIds) {
			ShipmentDetail shipmentDetail = shipmentDetailService.selectShipmentDetailById(id);
			if (shipmentDetail != null) {
				if (EXECUTED.equals(shipmentDetail.getUserVerifyStatus())
						&& !(EXECUTED.equals(shipmentDetail.getProcessStatus()))) {
					shipmentDetail.setProcessStatus(EXECUTED);
					shipmentDetailService.updateShipmentDetail(shipmentDetail);
				}
			}
		}
		return toAjax(true);
	}
}