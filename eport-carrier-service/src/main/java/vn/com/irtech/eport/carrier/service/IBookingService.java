package vn.com.irtech.eport.carrier.service;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.Booking;

/**
 * BookingService Interface
 * 
 * @author Irtech
 * @date 2020-09-04
 */
public interface IBookingService 
{
    /**
     * Get Booking
     * 
     * @param id BookingID
     * @return Booking
     */
    public Booking selectBookingById(Long id);

    /**
     * Get Booking List
     * 
     * @param booking Booking
     * @return Booking List
     */
    public List<Booking> selectBookingList(Booking booking);

    /**
     * Add Booking
     * 
     * @param booking Booking
     * @return result
     */
    public int insertBooking(Booking booking);

    /**
     * Update Booking
     * 
     * @param booking Booking
     * @return result
     */
    public int updateBooking(Booking booking);

    /**
     * Batch Delete Booking
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteBookingByIds(String ids);

    /**
     * Delete Booking
     * 
     * @param id BookingID
     * @return result
     */
    public int deleteBookingById(Long id);

    public Booking selectBookingByBookingNo(String bookingNo);
}
