package vn.com.irtech.eport.carrier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.Booking;
import vn.com.irtech.eport.carrier.service.IBookingDetailService;
import vn.com.irtech.eport.carrier.service.IBookingService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;

/**
 * BookingController
 * 
 * @author Irtech
 * @date 2020-09-04
 */
@Controller
@RequestMapping("/carrier/booking")
public class BookingController extends CarrierBaseController
{
    private String prefix = "carrier/booking";


    @Autowired
    private IBookingDetailService bookingDetailService;

    @Autowired
    private IBookingService bookingService;

    @GetMapping()
    public String booking()
    {
        return prefix + "/booking";
    }

    /**
     * Get Booking List
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(@RequestBody PageAble<Booking> param)
    {
        startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
		Booking booking = param.getData();
		if (booking == null) {
			booking = new Booking();
		}
        List<Booking> list = bookingService.selectBookingList(booking);
        return getDataTable(list);
    }

    /**
     * Export Booking List
     */
    @Log(title = "Booking", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Booking booking)
    {
        List<Booking> list = bookingService.selectBookingList(booking);
        ExcelUtil<Booking> util = new ExcelUtil<Booking>(Booking.class);
        return util.exportExcel(list, "booking");
    }

    /**
     * Add Booking
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    @Log(title = "Booking", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Booking booking)
    {   
        if(bookingService.selectBookingByBookingNo(booking.getBookingNo()) != null)
        {
            return error("Booking "+ booking.getBookingNo() + " đã tồn tại");
        }
        booking.setCarrierAccountId(super.getUserId());
        booking.setCarrierGroupId(super.getUserGroup().getId());
        booking.setOpr(super.getUserGroup().getGroupCode());
        booking.setBookStatus('H');
        return toAjax(bookingService.insertBooking(booking));
    }

    @Log(title = "releaseBooking", businessType = BusinessType.UPDATE)
    @PostMapping("/releaseBooking")
    @ResponseBody
    public AjaxResult releaseBooking(Long id)
    {  
        if(bookingDetailService.selectBookingDetailByBookingNo(id) == null)
        {
            return error("Vui lòng điền đầy đủ thông tin booking <br> và lưu booking trước khi phát hành!");
        }
        if(!bookingService.selectBookingById(id).getCarrierAccountId().equals(super.getUserId()))
        {
            return error("Bạn không có quyền phát hành Booking này <br> vui lòng kiểm tra lại dữ liệu!");
        }
        Booking booking = new Booking();
        booking.setId(id);
        booking.setBookStatus('R');
        bookingService.updateBooking(booking);
        return success();
    }

    /**
     * Update Booking
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        
        Booking booking = bookingService.selectBookingById(id);
        mmap.put("booking", booking);
        return prefix + "/edit";
    }

    /**
     * Update Save Booking
     */
    @Log(title = "Booking", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Booking booking)
    {
        booking.setCarrierAccountId(super.getUserId());
        booking.setCarrierGroupId(super.getUserGroup().getId());
        booking.setOpr(super.getUserGroup().getGroupCode());
        booking.setBookStatus('1');
        return toAjax(bookingService.updateBooking(booking));
    }

    /**
     * Delete Booking
     */
    @Log(title = "Booking", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(bookingService.deleteBookingByIds(ids));
    }

    @GetMapping("/pickupContainer")
    public String pickupContainer()
    {
        return prefix + "/pickupContainer";
    }
}
