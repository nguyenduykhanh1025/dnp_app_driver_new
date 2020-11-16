package vn.com.irtech.eport.web.controller.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.constant.UserConstants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.system.domain.SysRobot;
import vn.com.irtech.eport.system.service.ISysRobotService;

/**
 * Robot management controller
 * 
 * @author BaoHV
 *
 */

@Controller
@RequestMapping("/system/robot")
public class SysRobotController extends BaseController {

	private static final String PREFIX = "system/robot";

	@Autowired
	private ISysRobotService robotService;

	@GetMapping("/index")
	public String robot() {
		return PREFIX + "/robot";
	}

	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(SysRobot robot) {
		startPage();
		SysRobot robotParam = new SysRobot();
		robotParam.setUuId(robot.getUuId());
		robotParam.setIpAddress(robot.getIpAddress());
		robotParam.setStatus(robot.getStatus());
		if (robot.getServiceType() !=null ) {
			switch (robot.getServiceType()) {
			case EportConstants.SERVICE_PICKUP_FULL:
					robotParam.setIsReceiveContFullOrder(true);
					break;
				case EportConstants.SERVICE_DROP_EMPTY:
					robotParam.setIsSendContEmptyOrder(true);
					break;
				case EportConstants.SERVICE_PICKUP_EMPTY:
					robotParam.setIsReceiveContEmptyOrder(true);
					break;
				case EportConstants.SERVICE_DROP_FULL:
					robotParam.setIsSendContFullOrder(true);
					break;
				case EportConstants.SERVICE_SHIFTING:
					robotParam.setIsShiftingContOrder(true);
					break;
				case EportConstants.SERVICE_CHANGE_VESSEL:
					robotParam.setIsChangeVesselOrder(true);
					break;
				case EportConstants.SERVICE_CREATE_BOOKING:
					robotParam.setIsCreateBookingOrder(true);
					break;
				case EportConstants.SERVICE_GATE_IN:
					robotParam.setIsGateInOrder(true);
					break;
				case EportConstants.SERVICE_EXTEND_DATE:
					robotParam.setIsExtensionDateOrder(true);
					break;
				case EportConstants.SERVICE_TERMINAL_CUSTOM_HOLD:
					robotParam.setIsChangeTerminalCustomHold(true);
					break;
				case EportConstants.SERVICE_CANCEL_DROP_FULL:
					robotParam.setIsCancelSendContFullOrder(true);
					break;
				case EportConstants.SERVICE_CANCEL_PICKUP_EMPTY:
					robotParam.setIsCancelReceiveContEmptyOrder(true);
					break;
				case EportConstants.SERVICE_EXPORT_RECEIPT:
					robotParam.setIsExportReceipt(true);
					break;
				case EportConstants.SERVICE_EXTEND_DET:
					robotParam.setIsExtensionDetOrder(true);
					break;
				default:
					break;
			}
		}
		List<SysRobot> list = robotService.selectRobotList(robotParam);
		return getDataTable(list);
	}

	@GetMapping("/add")
	public String add() {
		return PREFIX + "/add";
	}

	@PostMapping("/checkUuidRobotUnique")
	@ResponseBody
	public String checkUuidRobotUnique(SysRobot robot) {
		return robotService.checkUuidRobotUnique(robot.getUuId());
	}

	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(@Validated SysRobot robot) {
		if (UserConstants.CONFIG_KEY_NOT_UNIQUE.equals(robotService.checkUuidRobotUnique(robot.getUuId()))) {
			return error("UuId đã tồn tại!");
		}
		robot.setCreateBy(ShiroUtils.getLoginName());
		return toAjax(robotService.insertRobot(robot));
	}

	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(String ids) {
		return toAjax(robotService.deleteRobotByIds(ids));
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long robotId, ModelMap mmap) {
		mmap.put("robot", robotService.selectRobotById(robotId));
		return PREFIX + "/edit";
	}

	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(@Validated SysRobot robot) {
		robot.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(robotService.updateRobot(robot));
	}
	
	@PostMapping("/disabled/change")
	@ResponseBody
	public AjaxResult changeDisabledStatus(SysRobot robot) {
		robot.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(robotService.updateRobot(robot));
	}
}
