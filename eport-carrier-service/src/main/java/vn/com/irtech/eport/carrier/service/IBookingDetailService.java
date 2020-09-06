package vn.com.irtech.eport.carrier.service;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.BookingDetail;

/**
 * Booking DetailService Interface
 * 
 * @author IRTech
 * @date 2020-09-04
 */
public interface IBookingDetailService 
{
    /**
     * Get Booking Detail
     * 
     * @param id Booking DetailID
     * @return Booking Detail
     */
    public BookingDetail selectBookingDetailById(Long id);

    /**
     * Get Booking Detail List
     * 
     * @param bookingDetail Booking Detail
     * @return Booking Detail List
     */
    public List<BookingDetail> selectBookingDetailList(BookingDetail bookingDetail);

    /**
     * Add Booking Detail
     * 
     * @param bookingDetail Booking Detail
     * @return result
     */
    public int insertBookingDetail(BookingDetail bookingDetail);

    /**
     * Update Booking Detail
     * 
     * @param bookingDetail Booking Detail
     * @return result
     */
    public int updateBookingDetail(BookingDetail bookingDetail);

    /**
     * Batch Delete Booking Detail
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteBookingDetailByIds(String ids);

    /**
     * Delete Booking Detail
     * 
     * @param id Booking DetailID
     * @return result
     */
    public int deleteBookingDetailById(Long id);

    public BookingDetail selectBookingDetailByBookingNo(Long bookingId);
}
