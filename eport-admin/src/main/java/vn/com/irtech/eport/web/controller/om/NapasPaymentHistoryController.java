package vn.com.irtech.eport.web.controller.om;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.logistic.domain.PaymentHistory;
import vn.com.irtech.eport.logistic.service.IPaymentHistoryService;
import vn.com.irtech.eport.web.controller.AdminBaseController;

@Controller
@RequestMapping("/om/paymentHistory")
public class NapasPaymentHistoryController extends AdminBaseController {
	private final String PREFIX = "om/paymentHistory";

	@Autowired
	private IPaymentHistoryService paymentHistoryService;
	
	@GetMapping()
	public String getViewPaymentHistory() {
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
