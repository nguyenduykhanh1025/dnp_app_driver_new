package vn.com.irtech.eport.web.controller.om;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.PaymentHistory;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IPaymentHistoryService;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/om/paymentHistory")
public class NapasPaymentHistoryController extends AdminBaseController {
	private final String PREFIX = "om/paymentHistory";

	@Autowired
	private IPaymentHistoryService paymentHistoryService;
	
	@Autowired
	private ILogisticGroupService logisticGroupService;
	
	@GetMapping()
	public String getViewPaymentHistory(ModelMap mmap) {
		LogisticGroup logisticGroup = new LogisticGroup();
	    logisticGroup.setGroupName("Chọn đơn vị Logistics");
	    logisticGroup.setId(0L);
	    LogisticGroup logisticGroupParam = new LogisticGroup();
	    logisticGroupParam.setDelFlag("0");
	    List<LogisticGroup> logisticGroups = logisticGroupService.selectLogisticGroupList(logisticGroupParam);
	    logisticGroups.add(0, logisticGroup);
	    mmap.put("logisticsGroups", logisticGroups);
		return PREFIX + "/paymentHistory";
	}
	
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo getList(@RequestBody PageAble<PaymentHistory> param) {
		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		PaymentHistory paymentHistory = param.getData();
		if (paymentHistory == null) {
			paymentHistory = new PaymentHistory();
		}
		List<PaymentHistory> paymentHistories = paymentHistoryService.selectPaymentHistoryListOm(paymentHistory);
		return getDataTable(paymentHistories);
	}
}
