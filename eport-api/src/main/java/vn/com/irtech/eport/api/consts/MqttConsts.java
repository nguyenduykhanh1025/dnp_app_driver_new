package vn.com.irtech.eport.api.consts;

import org.springframework.beans.factory.annotation.Value;

public class MqttConsts {

	public static String BASE_TOPIC = "eport";

	public static String SMART_GATE_REQ_TOPIC = BASE_TOPIC + "/gate/+/request";

	public static String SMART_GATE_RES_TOPIC = BASE_TOPIC + "/gate/+/response";

	public static String DRIVER_RES_TOPIC = BASE_TOPIC + "/driver/+/response";
	
	public static String MC_REQ_TOPIC = BASE_TOPIC + "/mc/plan/request";
}
