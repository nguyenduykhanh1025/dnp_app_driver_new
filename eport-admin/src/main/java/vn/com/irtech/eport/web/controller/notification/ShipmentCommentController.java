/**
 * 
 */
package vn.com.irtech.eport.web.controller.notification;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;

/**
 * @author Trong Hieu
 *
 */
@Controller
@RequestMapping("/shipment-comment")
public class ShipmentCommentController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(ShipmentCommentController.class);
	
	@Autowired
	private IShipmentCommentService shipmentCommentService;
	
	@GetMapping("/amount")
	@ResponseBody
	public AjaxResult getNumberOfComment() {
		ShipmentComment shipmentComment = new ShipmentComment();
		shipmentComment.setUserType(EportConstants.COMMENTOR_LOGISTIC);
		shipmentComment.setSeenFlg(false);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentCommentAmount", shipmentCommentService.selectCountCommentListUnSeen(shipmentComment));
		return ajaxResult;
	}
	
	@GetMapping("/list")
	@ResponseBody
	public AjaxResult getListCommentShipmentForGeneral() {
		startPage();
		ShipmentComment shipmentComment = new ShipmentComment();
		shipmentComment.setUserType(EportConstants.COMMENTOR_LOGISTIC);
		List<ShipmentComment> shipmentComments = shipmentCommentService.selectShipmentCommentListForNotification(shipmentComment);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentComments", shipmentComments);
		Long total = new PageInfo(shipmentComments).getTotal();
		ajaxResult.put("total", total);
		return ajaxResult;
	}
	
	@GetMapping("/list/all")
	@ResponseBody
	public AjaxResult getFullListShipmentComment() {
		ShipmentComment shipmentComment = new ShipmentComment();
		shipmentComment.setUserType(EportConstants.COMMENTOR_LOGISTIC);
		List<ShipmentComment> shipmentComments = shipmentCommentService.selectShipmentCommentListForNotification(shipmentComment);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("shipmentComments", shipmentComments);
		return ajaxResult;
	}
}
