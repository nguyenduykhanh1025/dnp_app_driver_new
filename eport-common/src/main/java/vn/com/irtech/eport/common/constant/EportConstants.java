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
	
	
}
