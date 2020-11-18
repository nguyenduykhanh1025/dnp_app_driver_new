package vn.com.irtech.eport.common.constant;

/**
 * Constants for the ePort system
 * 
 * @author GiapHD
 *
 */
public interface EportConstants {

	/** Boc cont hang */
	public static final int SERVICE_PICKUP_FULL = 1;
	/** Boc cont rong */
	public static final int SERVICE_PICKUP_EMPTY = 3;
	/** Ha cont hang */
	public static final int SERVICE_DROP_FULL = 4;
	/** Ha cont rong */
	public static final int SERVICE_DROP_EMPTY = 2;
	/** Dich chuyen */
	public static final int SERVICE_SHIFTING = 5;
	/** Doi tau chuyen */
	public static final int SERVICE_CHANGE_VESSEL = 6;
	/** Tao Booking */
	public static final int SERVICE_CREATE_BOOKING = 7;
	/** Gate in */
	public static final int SERVICE_GATE_IN = 8;
	/** Gia han lenh */
	public static final int SERVICE_EXTEND_DATE = 9;
	/** Terminal/Custom hold */
	public static final int SERVICE_TERMINAL_CUSTOM_HOLD = 10;
	/** Huy lenh ha hang */
	public static final int SERVICE_CANCEL_DROP_FULL = 11;
	/** Huy lenh boc rong */
	public static final int SERVICE_CANCEL_PICKUP_EMPTY = 12;
	/** Xuat hoa don */
	public static final int SERVICE_EXPORT_RECEIPT = 13;
	/** Gia han ngay mien luu vo */
	public static final int SERVICE_EXTEND_DET = 14;
	/** Update booking */
	public static final int BOOKING_UPDATE = 1;
	/** Update booking */
	public static final int BOOKING_CREATE = 2;

	public static final String DROP_EMPTY_TO_VESSEL = "1";

	public static final String DROP_EMPTY_TO_DEPORT = "0";

	// Shipment status
	/** Lo duoc khoi tao */
	public static final String SHIPMENT_STATUS_INIT = "1";
	/** Lo duoc khai bao chi tiet */
	public static final String SHIPMENT_STATUS_SAVE = "2";
	/** Lo dang trong qua trinh lam lenh */
	public static final String SHIPMENT_STATUS_PROCESSING = "3";
	/** Lo da hoan thanh toan bo container */
	public static final String SHIPMENT_STATUS_FINISH = "4";

	// Shipment detail status

	// Robot Status
	/** Robot is available */
	public static final String ROBOT_STATUS_AVAILABLE = "0";
	/** Robot is busy */
	public static final String ROBOT_STATUS_BUSY = "1";
	/** Robot is offline */
	public static final String ROBOT_STATUS_OFFLINE = "2";

	// Process order status
	public static final int PROCESS_ORDER_STATUS_NEW = 0;
	public static final int PROCESS_ORDER_STATUS_PROCESSING = 1;
	public static final int PROCESS_ORDER_STATUS_FINISHED = 2;
	// Process order result
	public static final String PROCESS_ORDER_RESULT_FAILED = "F";
	public static final String PROCESS_ORDER_RESULT_SUCCESS = "S";
	public static final String PROCESS_ORDER_RESULT_WAITING = "W";

	// process history
	public static final int PROCESS_HISTORY_STATUS_START = 1;
	public static final int PROCESS_HISTORY_STATUS_FINISHED = 2;
	// process history result
	public static final String PROCESS_HISTORY_RESULT_FAILED = "F";
	public static final String PROCESS_HISTORY_RESULT_SUCCESS = "S";

	// Delegate Permission
	public static final String DELEGATE_PERMISSION_PROCESS = "P";
	public static final String DELEGATE_PERMISSION_PAYMENT = "M";

	// Shipment supply status
	/** Shipment supply status waiting for supply cont */
	public static final Integer SHIPMENT_SUPPLY_STATUS_WAITING = 0;
	/** Finish supply container */
	public static final Integer SHIPMENT_SUPPLY_STATUS_FINISH = 1;

	// Container supply status
	/** Container status init */
	public static final Integer CONTAINER_STATUS_INIT = 0;
	/** Container status saved */
	public static final Integer CONTAINER_STATUS_SAVED = 1;
	/** Container status process order */
	public static final Integer CONTAINER_STATUS_PROCESSING = 2;
	/** Container status process order */
//	public static final Integer CONTAINER_STATUS_PAYMENT = 3;
//	/** Container status process order */
//	public static final Integer CONTAINER_STATUS_CUSTOM = 4;
//	/** Container status process order */
//	public static final Integer CONTAINER_STATUS_FINISH = 5;

	// Container supply status
	/** Container supply status init */
	public static final String CONTAINER_SUPPLY_STATUS_HOLD = "N";
	/** Container supply status request */
	public static final String CONTAINER_SUPPLY_STATUS_REQ = "R";
	/** Container supply status request accepted */
	public static final String CONTAINER_SUPPLY_STATUS_FINISH = "Y";

	// Result status for gate in order
	/** Status success for gate in order */
	public static final String GATE_RESULT_SUCCESS = "success";
	/** Status failed for gate in order */
	public static final String GATE_RESULT_FAIL = "fail";

	// Pickup history status
	/** Pickup history status waiting to gate in */
	public static final Integer PICKUP_HISTORY_STATUS_WAITING = 0;
	/** Pickup history status gate in to gate in */
	public static final Integer PICKUP_HISTORY_STATUS_GATE_IN = 1;
	/** Pickup history status finish to gate in */
	public static final Integer PICKUP_HISTORY_STATUS_FINISH = 2;

	// Notification status
	/** Notification status draft */
	public static final Long NOTIFICATION_STATUS_DRAFT = 0L;
	/** Notification status active */
	public static final Long NOTIFICATION_STATUS_ACTIVE = 1L;

	// Notification user type for receive notification */
	/** User type logistic */
	public static final Long USER_TYPE_LOGISTIC = 1L;
	/** User type driver */
	public static final Long USER_TYPE_DRIVER = 2L;
	/** User type admin */
	public static final Long USER_TYPE_ADMIN = 3L;

	// Notification level
	/** Notification general with time delay */
	public static final Long NOTIFICATION_LEVEL_GENERAL = 1L;
	/** Notification instant with time instant */
	public static final Long NOTIIFCATION_LEVEL_INSTANT = 2L;

	// User type for shipment comment
	/** DNP Staff commentor */
	public static final String COMMENTOR_DNP_STAFF = "S";
	/** Logistics commentor */
	public static final String COMMENTOR_LOGISTIC = "L";
	/** Carrier commentor */
	public static final String COMMENTOR_CARRIER = "C";
	/** Driver commentor */
	public static final String COMMENTOR_DRIVER = "D";

	// Topic for shipment comment
	/** Topic comment container supplier */
	public static final String TOPIC_COMMENT_CONT_SUPPLIER = "Cấp container rỗng";
	/** Topic comment container supplier reject */
	public static final String TOPIC_COMMENT_CONT_SUPPLIER_REJECT = "Từ chối cấp container rỗng";
	/** Topic comment container supplier delete */
	public static final String TOPIC_COMMENT_CONT_SUPPLIER_DELETE = "Hủy yêu cầu cấp container rỗng";
	/** Topic comment om support document */
	public static final String TOPIC_COMMENT_OM_DOCUMENT = "Xác nhận giấy tờ thủ tục";
	/** Topic comment om support booking for carrier payment controlling */
	public static final String TOPIC_COMMENT_OM_BOOKING = "Xác nhận Booking";
	/** Topic comment req cancel order pickup empty */
	public static final String TOPIC_COMMENT_CANCEL_PICKUP_EMPTY = "Yêu cầu hủy lệnh nhận container rỗng";
	/** Topic comment req cancel order pickup full */
	public static final String TOPIC_COMMENT_CANCEL_PICKUP_FULL = "Yêu cầu hủy lệnh nhận container hàng";
	/** Topic comment req cancel order drop empty */
	public static final String TOPIC_COMMENT_CANCEL_DROP_EMPTY = "Yêu cầu hủy lệnh giao container rỗng";
	/** Topic comment req cancel order drop full */
	public static final String TOPIC_COMMENT_CANCEL_DROP_FULL = "Yêu cầu hủy lệnh giao container hàng";

	// User type on notification app window
	/** User type om in app notification */
	public static final String APP_USER_TYPE_OM = "om";
	/** User type om in app notification */
	public static final String APP_USER_TYPE_MC = "mc";
	/** User type om in app notification */
	public static final String APP_USER_TYPE_IT = "it";
	/** User type om in app notification */
	public static final String APP_USER_TYPE_KT = "kt";
	/** User type om in app notification */
	public static final String APP_USER_TYPE_CUSTOM = "hq";
	/** User type om in app notification */
	public static final String APP_USER_TYPE_GATE = "gate";
	/** User type cont supplier in app notification */
	public static final String APP_USER_TYPE_CONT = "cont";

	// Domain web eport
	/** Url to mc provide yard position */
	public static final String URL_POSITION_MC = "/mc/plan/request/index";
	/** url to om support receive full */
	public static final String URL_OM_RECEIVE_F_SUPPORT = "/om/support/receive-full/view";
	/** url to om support receive empty */
	public static final String URL_OM_RECEIVE_E_SUPPORT = "/om/support/receive-empty/view";
	/** url to om support send full */
	public static final String URL_OM_SEND_F_SUPPORT = "/om/support/send-full/view";
	/** url to om support send empty */
	public static final String URL_OM_SEND_E_SUPPORT = "/om/support/send-empty/view";
	/** url to om support receive custom */
	public static final String URL_OM_CUSTOM_RECEIVE_SUPPORT = "/om/support/custom-receive-full/view";
	/** url to om support send custom */
	public static final String URL_OM_CUSTOM_SEND_SUPPORT = "/om/support/custom-send-full/view";
	/** url to cont supplier */
	public static final String URL_CONT_SUPPLIER = "/container/supplier";
	/** url to cancel order req support */
	public static final String URL_CANCEL_ORDER_SUPPORT = "/om/support/order-cancel";
	/** url to gate support */
	public static final String URL_GATE = "/gate/support";

	// Priority notification app window
	/** High priority notificaiton app window */
	public static final Integer NOTIFICATION_PRIORITY_HIGH = 1;
	/** Medium priority notificaiton app window */
	public static final Integer NOTIFICATION_PRIORITY_MEDIUM = 3;
	/** Low priority notificaiton app window */
	public static final Integer NOTIFICATION_PRIORITY_LOW = 2;

	// plate number type for logistic truck type
	/** loai xe dau keo */
	public static final String TRUCK_TYPE_TRUCK_NO = "0";
	/** loai xe ro mooc */
	public static final String TRUCK_TYPE_CHASSIS_NO = "1";

	// Process status of shipment detail
	public static final String PROCESS_STATUS_SHIPMENT_DETAIL_NO = "N";
	public static final String PROCESS_STATUS_SHIPMENT_DETAIL_YES = "Y";
	public static final String PROCESS_STATUS_SHIPMENT_DETAIL_ERROR = "E";
	public static final String PROCESS_STATUS_SHIPMENT_DETAIL_DELETE = "D";

	// DO type carriger group
	/** Do type DO of carrier group */
	public static final String DO_TYPE_CARRIER_DO = "0";
	/** Do type eDO of carrier group */
	public static final String DO_TYPE_CARRIER_EDO = "1";

	// Mode check hold
	/** terminal hold mode */
	public static final String MODE_TERMINAL_HOLD = "Terminal Hold";
	/** custom hold mode */
	public static final String MODE_CUSTOM_HOLD = "Custom Hold";

	// Payment history status
	/** Payment history success status */
	public static final String PAYMENT_HISTORY_SUCCESS = "1";

	// Hold type catos
	/** Terminal hold catos */
	public static final String HOLD_TYPE_TERMINAL = "T";
	/** Custom hold catos */
	public static final String HOLD_TYPE_CUSTOM = "C";

	// Hold code catos
	/** Terminal hold code DO */
	public static final String HOLD_CODE_DO = "DO";

	// Default user name
	/** User name default system */
	public static final String USER_NAME_SYSTEM = "SYSTEM";

	/** Robot Retry Times */
	public static final Integer ROBOT_RETRY_TIMES_LIMIT = 4;

	// Catos container status
	/** Catos container status stacking */
	public static final String CATOS_CONT_STACKING = "Stacking";
	/** Catos container status delivered */
	public static final String CATOS_CONT_DELIVERED = "Delivered";

	// Key name to get data from cache
	/** Key name for gate detection info saved in cache */
	public static final String CACHE_GATE_DETECTION_KEY = "gateDetectionInfo";

	// Synchronize queue type
	/** Sync queue type expired dem */
	public static final String SYNC_QUEUE_DEM = "DEM";
	/** Sync queue type det free time */
	public static final String SYNC_QUEUE_DET = "DET";

	// Synchronize queue status
	/** Sync queue status waiting */
	public static final String SYNC_QUEUE_STATUS_WAITING = "W";
	/** Sync queue status progress */
	public static final String SYNC_QUEUE_STATUS_PROGRESS = "P";
	/** Sync queue status success */
	public static final String SYNC_QUEUE_STATUS_SUCCESS = "S";
	/** Sync queue status error */
	public static final String SYNC_QUEUE_STATUS_ERROR = "E";

	// Type of gate in form data (Begin or continue)
	/** Gate in form data type beginning */
	public static final String GATE_REQ_TYPE_BEGIN = "B";
	/** Gate in form data type continuing */
	public static final String GATE_REQ_TYPE_CONTINUE = "C";

	// Type of partner code in catos
	/** partner type consignee */
	public static final String PTNR_TYPE_CONSIGNEE = "CNS";
	/** partner type shipping line */
	public static final String PTNR_TYPE_SHIPPING_LINE = "SHP";
	/** partner type trucker */
	public static final String PTNR_TYPE_TRUCKER = "TRK";
	/** partner type forwarder */
	public static final String PTNR_TYPE_FORWARDER = "FWD";

	// Bulletin active status
	/** Bulletin draft status */
	public static final Integer BULLETIN_DRAFT = 0;
	/** Bulletin release status */
	public static final Integer BULLETIN_RELEASE = 1;
}
