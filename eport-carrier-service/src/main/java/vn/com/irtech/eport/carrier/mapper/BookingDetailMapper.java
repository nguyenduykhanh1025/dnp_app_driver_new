package vn.com.irtech.eport.carrier.mapper;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.BookingDetail;

/**
 * Booking DetailMapper Interface
 * 
 * @author IRTech
 * @date 2020-09-04
 */
public interface BookingDetailMapper 
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
     * @return Result
     */
    public int insertBookingDetail(BookingDetail bookingDetail);

    /**
     * Update Booking Detail
     * 
     * @param bookingDetail Booking Detail
     * @return Result
     */
    public int updateBookingDetail(BookingDetail bookingDetail);

    /**
     * Delete Booking Detail
     * 
     * @param id Booking DetailID
     * @return result
     */
    public int deleteBookingDetailById(Long id);

    /**
     * Batch Delete Booking Detail
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteBookingDetailByIds(String[] ids);
}
