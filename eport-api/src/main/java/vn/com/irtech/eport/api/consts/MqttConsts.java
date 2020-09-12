package vn.com.irtech.eport.api.consts;

public class MqttConsts {

	public static String BASE_TOPIC = "eport";

	public static String SMART_GATE_REQ_TOPIC = BASE_TOPIC + "/gate/+/request";

	public static String SMART_GATE_RES_TOPIC = BASE_TOPIC + "/gate/+/response";

	public static String DRIVER_RES_TOPIC = BASE_TOPIC + "/driver/+/res";
	
	public static String MC_REQ_TOPIC = BASE_TOPIC + "/mc/plan/request";
	
	public static String GATE_ROBOT_REQ_TOPIC = BASE_TOPIC + "/robot/gate/+/request";
	
	public static String GATE_ROBOT_RES_TOPIC = BASE_TOPIC + "/robot/gate/+/response";
	
	public static final String NOTIFICATION_OM_TOPIC = "eport/notification/om";
	
	public static final String NOTIFICATION_IT_TOPIC = "eport/notification/it";
	
	public static final String NOTIFICATION_CONT_TOPIC = "eport/notification/cont";
	
	public static final String NOTIFICATION_MC_TOPIC = "eport/notification/mc";
	
	public static final String NOTIFICATION_GATE_TOPIC = "eport/notification/gate";
	
	public static final String NOTIFICATION_GATE_RES_TOPIC = "eport/notification/gate/response";
}
