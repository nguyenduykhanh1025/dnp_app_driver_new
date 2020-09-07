package vn.com.irtech.eport.web.controller.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.firebase.messaging.FirebaseMessagingException;

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.framework.firebase.service.FirebaseService;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.logistic.domain.DriverAccount;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.service.IDriverAccountService;
import vn.com.irtech.eport.logistic.service.ILogisticAccountService;
import vn.com.irtech.eport.system.domain.SysNotification;
import vn.com.irtech.eport.system.domain.SysNotificationReceiver;
import vn.com.irtech.eport.system.service.ISysNotificationReceiverService;
import vn.com.irtech.eport.system.service.ISysNotificationService;
import vn.com.irtech.eport.system.service.ISysUserTokenService;

@Controller
@RequestMapping("/notifications")
public class NotificationController extends BaseController {
	private final static String PREFIX = "notification";

	@Autowired
	private ISysNotificationReceiverService sysNotificationReceiverService;

	@Autowired
	private ISysNotificationService sysNotificationService;;

	@Autowired
	private ISysUserTokenService sysUserTokenService;
	
	@Autowired
	private IDriverAccountService driverAccountService;
	
	@Autowired
	private ILogisticAccountService logisticAccountService;

	@Autowired
	private FirebaseService firebaseService;

	@GetMapping()
	private String getList() {
		return PREFIX + "/list";
	}

	@PostMapping("/list")
	@ResponseBody
	private TableDataInfo getALl(@RequestBody PageAble<SysNotificationReceiver> param) {
//		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
//		Notifications notifications = param.getData();
//		if (notifications == null) {
//			notifications = new Notifications();
//		}
//		List<Notifications> notificationsList = notificationService.selectNotificationsDetailList(notifications);
//		return getDataTable(notificationsList);
		return null;
	}

	@GetMapping("/add")
	private String getNotification() {
		return PREFIX + "/add";
	}

	@PostMapping()
	@ResponseBody
	@Transactional
	private AjaxResult sendNotification(String receiverGroups, String title, String content) {
		// Create info notification
		SysNotification sysNotification = new SysNotification();
		sysNotification.setTitle(title);
		sysNotification.setNotifyLevel(EportConstants.NOTIFICATION_LEVEL_GENERAL);
		sysNotification.setContent(content);
		sysNotification.setNotifyLink("");
		sysNotification.setStatus(EportConstants.NOTIFICATION_STATUS_ACTIVE);
		sysNotificationService.insertSysNotification(sysNotification);
		
		String[] userTypeArr = receiverGroups.split(",");
		for (int i=0; i<userTypeArr.length; i++) {
			if (EportConstants.USER_TYPE_DRIVER.equals(Long.parseLong(userTypeArr[i]))) {
				DriverAccount driverAccountParam = new DriverAccount();
				driverAccountParam.setDelFlag(false);
				List<DriverAccount> driverAccounts = driverAccountService.selectDriverAccountList(driverAccountParam);
				if (CollectionUtils.isNotEmpty(driverAccounts)) {
					for (DriverAccount driver : driverAccounts) {
						SysNotificationReceiver sysNotificationReceiver = new SysNotificationReceiver();
						sysNotificationReceiver.setUserId(driver.getId());;
						sysNotificationReceiver.setNotificationId(sysNotification.getId());
						sysNotificationReceiver.setUserType(EportConstants.USER_TYPE_DRIVER);
						sysNotificationReceiver.setSentFlg(false);
						sysNotificationReceiverService.insertSysNotificationReceiver(sysNotificationReceiver);
					}
				}
			} else if (EportConstants.USER_TYPE_LOGISTIC.equals(Long.parseLong(userTypeArr[i]))) {
				LogisticAccount logisticAccountParam = new LogisticAccount();
				logisticAccountParam.setDelFlag("0");
				List<LogisticAccount> logisticAccounts = logisticAccountService.selectLogisticAccountList(logisticAccountParam);
				if (CollectionUtils.isNotEmpty(logisticAccounts)) {
					for (LogisticAccount logisticAccount : logisticAccounts) {
						SysNotificationReceiver sysNotificationReceiver = new SysNotificationReceiver();
						sysNotificationReceiver.setUserId(logisticAccount.getId());;
						sysNotificationReceiver.setNotificationId(sysNotification.getId());
						sysNotificationReceiver.setUserType(EportConstants.USER_TYPE_LOGISTIC);
						sysNotificationReceiver.setSentFlg(false);
						sysNotificationReceiverService.insertSysNotificationReceiver(sysNotificationReceiver);
					}
				}
			} else if (EportConstants.USER_TYPE_ADMIN.equals(Long.parseLong(userTypeArr[i]))) {
				// TODO : create notification for admin
			}
		}
		
		return success();
	}
}
