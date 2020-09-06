package vn.com.irtech.eport.carrier.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.carrier.mapper.BookingMapper;
import vn.com.irtech.eport.carrier.domain.Booking;
import vn.com.irtech.eport.carrier.service.IBookingService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * BookingService Business Processing
 * 
 * @author Irtech
 * @date 2020-09-04
 */
@Service
public class BookingServiceImpl implements IBookingService 
{
    @Autowired
    private BookingMapper bookingMapper;

    /**
     * Get Booking
     * 
     * @param id BookingID
     * @return Booking
     */
    @Override
    public Booking selectBookingById(Long id)
    {
        return bookingMapper.selectBookingById(id);
    }

    /**
     * Get Booking List
     * 
     * @param booking Booking
     * @return Booking
     */
    @Override
    public List<Booking> selectBookingList(Booking booking)
    {
        return bookingMapper.selectBookingList(booking);
    }

    /**
     * Add Booking
     * 
     * @param booking Booking
     * @return result
     */
    @Override
    public int insertBooking(Booking booking)
    {
        booking.setCreateTime(DateUtils.getNowDate());
        return bookingMapper.insertBooking(booking);
    }

    /**
     * Update Booking
     * 
     * @param booking Booking
     * @return result
     */
    @Override
    public int updateBooking(Booking booking)
    {
        booking.setUpdateTime(DateUtils.getNowDate());
        return bookingMapper.updateBooking(booking);
    }

    /**
     * Delete Booking By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteBookingByIds(String ids)
    {
        return bookingMapper.deleteBookingByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Booking
     * 
     * @param id BookingID
     * @return result
     */
    @Override
    public int deleteBookingById(Long id)
    {
        return bookingMapper.deleteBookingById(id);
    }

    @Override
    public Booking selectBookingByBookingNo(String bookingNo)
    {
        return bookingMapper.selectBookingByBookingNo(bookingNo);
    }
}
