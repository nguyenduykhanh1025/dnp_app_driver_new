package vn.com.irtech.eport.carrier.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.BookingDetail;
import vn.com.irtech.eport.carrier.service.IBookingDetailService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;

/**
 * Booking DetailController
 * 
 * @author IRTech
 * @date 2020-09-04
 */
@Controller
@RequestMapping("/carrier/booking/detail")
public class BookingDetailController extends CarrierBaseController
{
    private String prefix = "carrier/booking/detail";

    @Autowired
    private IBookingDetailService bookingDetailService;

    @GetMapping()
    public String detail()
    {
        return prefix + "/detail";
    }

    /**
     * Get Booking Detail List
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BookingDetail bookingDetail)
    {
        startPage();
        List<BookingDetail> list = bookingDetailService.selectBookingDetailList(bookingDetail);
        return getDataTable(list);
    }

    /**
     * Export Booking Detail List
     */
    @Log(title = "Booking Detail", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BookingDetail bookingDetail)
    {
        List<BookingDetail> list = bookingDetailService.selectBookingDetailList(bookingDetail);
        ExcelUtil<BookingDetail> util = new ExcelUtil<BookingDetail>(BookingDetail.class);
        return util.exportExcel(list, "booking/detail");
    }

    /**
     * Add Booking Detail
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存Booking Detail
     */
    @Log(title = "Booking Detail", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @Transactional
    public AjaxResult addSave(@RequestBody List<BookingDetail> bookingDetails)
    {
        if (bookingDetails != null) {
			for (BookingDetail bookingDetail : bookingDetails) {
                bookingDetail.setCarrierGroupId(super.getUserGroup().getId());
                if(bookingDetail.getId() == null)
                {
                    bookingDetailService.insertBookingDetail(bookingDetail);
                } else {
                    bookingDetailService.updateBookingDetail(bookingDetail);
                }
			}
			return success("Lưu khai báo thành công");
		}
		return error("Lưu khai báo thất bại");
    }

    /**
     * Update Booking Detail
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        BookingDetail bookingDetail = bookingDetailService.selectBookingDetailById(id);
        mmap.put("bookingDetail", bookingDetail);
        return prefix + "/edit";
    }

    /**
     * Update Save Booking Detail
     */
    @Log(title = "Booking Detail", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BookingDetail bookingDetail)
    {
        return toAjax(bookingDetailService.updateBookingDetail(bookingDetail));
    }

    /**
     * Delete Booking Detail
     */
    @Log(title = "Booking Detail", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    @Transactional
    public AjaxResult remove(String ids)
    {
        return toAjax(bookingDetailService.deleteBookingDetailByIds(ids));
    }

    @GetMapping("/pickupContainer")
    public String pickupContainer()
    {
        return prefix + "/pickupContainer";
    }
}
