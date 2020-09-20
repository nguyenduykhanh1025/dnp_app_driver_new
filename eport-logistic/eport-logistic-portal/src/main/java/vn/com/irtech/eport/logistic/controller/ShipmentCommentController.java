//package vn.com.irtech.eport.logistic.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import vn.com.irtech.eport.common.annotation.Log;
//import vn.com.irtech.eport.common.core.controller.BaseController;
//import vn.com.irtech.eport.common.core.domain.AjaxResult;
//import vn.com.irtech.eport.common.core.page.TableDataInfo;
//import vn.com.irtech.eport.common.enums.BusinessType;
//import vn.com.irtech.eport.common.enums.OperatorType;
//import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
//import vn.com.irtech.eport.logistic.domain.ShipmentComment;
//import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
//
///**
// * Shipment CommentController
// * 
// * @author IRTech
// * @date 2020-09-06
// */
//@Controller
//@RequestMapping("/shipment/comment")
//public class ShipmentCommentController extends BaseController
//{
//    private String prefix = "logistic/comment";
//
//    @Autowired
//    private IShipmentCommentService shipmentCommentService;
//
//    @GetMapping()
//    public String comment()
//    {
//        return prefix + "/comment";
//    }
//
//    /**
//     * Get Shipment Comment List
//     */
//    @PostMapping("/list")
//    @ResponseBody
//    public TableDataInfo list(ShipmentComment shipmentComment)
//    {
//        startPage();
//        List<ShipmentComment> list = shipmentCommentService.selectShipmentCommentList(shipmentComment);
//        return getDataTable(list);
//    }
//
//    /**
//     * Export Shipment Comment List
//     */
//    @Log(title = "Shipment Comment", businessType = BusinessType.EXPORT, operatorType = OperatorType.LOGISTIC)
//    @PostMapping("/export")
//    @ResponseBody
//    public AjaxResult export(ShipmentComment shipmentComment)
//    {
//        List<ShipmentComment> list = shipmentCommentService.selectShipmentCommentList(shipmentComment);
//        ExcelUtil<ShipmentComment> util = new ExcelUtil<ShipmentComment>(ShipmentComment.class);
//        return util.exportExcel(list, "comment");
//    }
//
//    /**
//     * Add Shipment Comment
//     */
//    @GetMapping("/add")
//    public String add()
//    {
//        return prefix + "/add";
//    }
//
//    @Log(title = "Shipment Comment", businessType = BusinessType.INSERT, operatorType = OperatorType.LOGISTIC)
//    @PostMapping("/add")
//    @ResponseBody
//    public AjaxResult addSave(ShipmentComment shipmentComment)
//    {
//        return toAjax(shipmentCommentService.insertShipmentComment(shipmentComment));
//    }
//
//    /**
//     * Update Shipment Comment
//     */
//    @GetMapping("/edit/{id}")
//    public String edit(@PathVariable("id") Long id, ModelMap mmap)
//    {
//        ShipmentComment shipmentComment = shipmentCommentService.selectShipmentCommentById(id);
//        mmap.put("shipmentComment", shipmentComment);
//        return prefix + "/edit";
//    }
//
//    /**
//     * Update Save Shipment Comment
//     */
//    @Log(title = "Shipment Comment", businessType = BusinessType.UPDATE, operatorType = OperatorType.LOGISTIC)
//    @PostMapping("/edit")
//    @ResponseBody
//    public AjaxResult editSave(ShipmentComment shipmentComment)
//    {
//        return toAjax(shipmentCommentService.updateShipmentComment(shipmentComment));
//    }
//
//    /**
//     * Delete Shipment Comment
//     */
//    @Log(title = "Shipment Comment", businessType = BusinessType.DELETE, operatorType = OperatorType.LOGISTIC)
//    @PostMapping( "/remove")
//    @ResponseBody
//    public AjaxResult remove(String ids)
//    {
//        return toAjax(shipmentCommentService.deleteShipmentCommentByIds(ids));
//    }
//}
