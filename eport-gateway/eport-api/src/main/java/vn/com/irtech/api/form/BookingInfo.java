/**
 * 
 */
package vn.com.irtech.api.form;

import java.io.Serializable;

/**
 * @author GiapHD
 *
 */
public class BookingInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userVoy;
	private String bookingNo;
	private String sztp;
	private Integer bookQty;
	private Integer usedQty;

	public String getUserVoy() {
		return userVoy;
	}

	public void setUserVoy(String userVoy) {
		this.userVoy = userVoy;
	}

	public String getBookingNo() {
		return bookingNo;
	}

	public void setBookingNo(String bookingNo) {
		this.bookingNo = bookingNo;
	}

	public String getSztp() {
		return sztp;
	}

	public void setSztp(String sztp) {
		this.sztp = sztp;
	}

	public Integer getBookQty() {
		return bookQty;
	}

	public void setBookQty(Integer bookQty) {
		this.bookQty = bookQty;
	}

	public Integer getUsedQty() {
		return usedQty;
	}

	public void setUsedQty(Integer usedQty) {
		this.usedQty = usedQty;
	}

}
