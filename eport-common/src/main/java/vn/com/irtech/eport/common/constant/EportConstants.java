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
	/** Boc cont rong*/
	public static final int SERVICE_PICKUP_EMPTY = 3;
	/** Ha cont hang */
	public static final int SERVICE_DROP_FULL = 4;
	/** Ha cont rong*/
	public static final int SERVICE_DROP_EMPTY = 2;
	/** Dich chuyen*/
	public static final int SERVICE_SHIFTING = 5;
	/** Doi tau chuyen*/
	public static final int SERVICE_CHANGE_VESSEL = 6;
	/** Tao Booking*/
	public static final int SERVICE_CREATE_BOOKING = 7;
	/** Gate in*/
	public static final int SERVICE_GATE_IN = 8;
	/** Gia han lenh*/
	public static final int SERVICE_EXTEND_DATE = 9;
	/** Update booking*/
	public static final int BOOKING_UPDATE = 1;
	/** Update booking*/ 
	public static final int BOOKING_CREATE = 2;
	
	// Shipment status
	/** Lo duoc khoi tao*/
	public static final String SHIPMENT_STATUS_DECLARE = "1";
	/** Lo duoc khai bao chi tiet*/
	public static final String SHIPMENT_STATUS_SAVE = "2";
	/** Lo dang trong qua trinh lam lenh*/
	public static final String SHIPMENT_STATUS_PROCESSING = "3";
	/** Lo da hoan thanh toan bo container*/
	public static final String SHIPMENT_STATUS_FINISH = "4";
	
	
	// Robot Status
	/** Robot is available */
	public static final String ROBOT_STATUS_AVAILABLE = "0";
	/** Robot is busy */
	public static final String ROBOT_STATUS_BUSY = "1";
	/** Robot is offline */
	public static final String ROBOT_STATUS_OFFLINE = "2";
}
