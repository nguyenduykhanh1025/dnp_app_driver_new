//package vn.com.irtech.eport.web.controller.notification;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import vn.com.irtech.eport.common.core.controller.BaseController;
//import vn.com.irtech.eport.common.core.domain.AjaxResult;
//import vn.com.irtech.eport.common.core.page.PageAble;
//import vn.com.irtech.eport.common.core.page.TableDataInfo;
//import vn.com.irtech.eport.framework.firebase.service.FirebaseService;
//import vn.com.irtech.eport.framework.util.ShiroUtils;
//import vn.com.irtech.eport.system.domain.NotificationReceiver;
//import vn.com.irtech.eport.system.domain.Notifications;
//import vn.com.irtech.eport.system.domain.UserDevices;
//import vn.com.irtech.eport.system.service.INotificationReceiverService;
//import vn.com.irtech.eport.system.service.INotificationsService;
//import vn.com.irtech.eport.system.service.IUserDevicesService;
//
//@Controller
//@RequestMapping("/notifications")
//public class NotificationController extends BaseController {
//	private final static String PREFIX = "notification";
//
//	@Autowired
//	private INotificationsService notificationService;
//
//	@Autowired
//	private INotificationReceiverService notificationReceiverService;
//
//	@Autowired
//	private IUserDevicesService userDevicesService;
//
//	@Autowired
//	private FirebaseService firebaseService;
//
//	@GetMapping()
//	private String getList() {
//		return PREFIX + "/list";
//	}
//
//	@PostMapping("/list")
//	@ResponseBody
//	private TableDataInfo getALl(@RequestBody PageAble<Notifications> param) {
//		startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
//		Notifications notifications = param.getData();
//		if (notifications == null) {
//			notifications = new Notifications();
//		}
//		List<Notifications> notificationsList = notificationService.selectNotificationsDetailList(notifications);
//		return getDataTable(notificationsList);
//	}
//
//	@GetMapping("/add")
//	private String getNotification() {
//		return PREFIX + "/add";
//	}
//
//	@PostMapping()
//	@ResponseBody
//	@Transactional
//	private AjaxResult sendNotification(String receiverGroups, String title, String content) {
//		Notifications noti = new Notifications();
//		noti.setTitle(title);
//		noti.setContent(content);
//		noti.setCreateTime(new Date());
//		noti.setCreateBy(ShiroUtils.getSysUser().getUserName());
//		notificationService.insertNotifications(noti);
//
//		List<UserDevices> receivers = userDevicesService.selectDeviceTokenList(receiverGroups);
//		List<String> receiverTokens = new ArrayList<>();
//    
//		for (UserDevices userDevices : receivers) {
//				NotificationReceiver receiverDetail = new NotificationReceiver();
//				receiverDetail.setUserDeviceId(userDevices.getId());
//				receiverDetail.setNotificationId(noti.getId());
//				notificationReceiverService.insertNotificationReceiver(receiverDetail);
//				receiverTokens.add(userDevices.getDeviceToken());
//			}
//		return AjaxResult.success(receiverTokens);
//	}
//}
