package vn.com.irtech.eport.carrier.mapper;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.Booking;

/**
 * BookingMapper Interface
 * 
 * @author Irtech
 * @date 2020-09-04
 */
public interface BookingMapper 
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
     * @return Result
     */
    public int insertBooking(Booking booking);

    /**
     * Update Booking
     * 
     * @param booking Booking
     * @return Result
     */
    public int updateBooking(Booking booking);

    /**
     * Delete Booking
     * 
     * @param id BookingID
     * @return result
     */
    public int deleteBookingById(Long id);

    /**
     * Batch Delete Booking
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteBookingByIds(String[] ids);
}
