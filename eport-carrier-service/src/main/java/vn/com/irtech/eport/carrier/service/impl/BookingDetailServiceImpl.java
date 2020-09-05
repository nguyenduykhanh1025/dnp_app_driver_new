package vn.com.irtech.eport.carrier.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.carrier.mapper.BookingDetailMapper;
import vn.com.irtech.eport.carrier.domain.BookingDetail;
import vn.com.irtech.eport.carrier.service.IBookingDetailService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Booking DetailService Business Processing
 * 
 * @author IRTech
 * @date 2020-09-04
 */
@Service
public class BookingDetailServiceImpl implements IBookingDetailService 
{
    @Autowired
    private BookingDetailMapper bookingDetailMapper;

    /**
     * Get Booking Detail
     * 
     * @param id Booking DetailID
     * @return Booking Detail
     */
    @Override
    public BookingDetail selectBookingDetailById(Long id)
    {
        return bookingDetailMapper.selectBookingDetailById(id);
    }

    /**
     * Get Booking Detail List
     * 
     * @param bookingDetail Booking Detail
     * @return Booking Detail
     */
    @Override
    public List<BookingDetail> selectBookingDetailList(BookingDetail bookingDetail)
    {
        return bookingDetailMapper.selectBookingDetailList(bookingDetail);
    }

    /**
     * Add Booking Detail
     * 
     * @param bookingDetail Booking Detail
     * @return result
     */
    @Override
    public int insertBookingDetail(BookingDetail bookingDetail)
    {
        bookingDetail.setCreateTime(DateUtils.getNowDate());
        return bookingDetailMapper.insertBookingDetail(bookingDetail);
    }

    /**
     * Update Booking Detail
     * 
     * @param bookingDetail Booking Detail
     * @return result
     */
    @Override
    public int updateBookingDetail(BookingDetail bookingDetail)
    {
        bookingDetail.setUpdateTime(DateUtils.getNowDate());
        return bookingDetailMapper.updateBookingDetail(bookingDetail);
    }

    /**
     * Delete Booking Detail By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteBookingDetailByIds(String ids)
    {
        return bookingDetailMapper.deleteBookingDetailByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Booking Detail
     * 
     * @param id Booking DetailID
     * @return result
     */
    @Override
    public int deleteBookingDetailById(Long id)
    {
        return bookingDetailMapper.deleteBookingDetailById(id);
    }
    @Override
    public BookingDetail selectBookingDetailByBookingNo(Long bookingId)
    {
        return bookingDetailMapper.selectBookingDetailByBookingNo(bookingId);
    }
}
