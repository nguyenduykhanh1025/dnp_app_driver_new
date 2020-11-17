package vn.com.irtech.eport.web.controller.supportRequest;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.config.ServerConfig;
import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.exception.file.InvalidExtensionException;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.framework.web.service.DictService;
import vn.com.irtech.eport.logistic.domain.LogisticGroup;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentComment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.domain.ShipmentImage;
import vn.com.irtech.eport.logistic.service.ILogisticGroupService;
import vn.com.irtech.eport.logistic.service.IShipmentCommentService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentImageService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.SysDictData;
import vn.com.irtech.eport.system.domain.SysUser;
import vn.com.irtech.eport.web.controller.AdminBaseController;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/support-request/dangerous")
public class LogisticSendContFullDangerousSupportRequest extends AdminBaseController {
    private static final Logger logger = LoggerFactory.getLogger(LogisticSendContFullDangerousSupportRequest.class);

    private String PREFIX = "/supportRequest/dangerous";

    @Autowired
    private IShipmentService shipmentService;

    @Autowired
    private ILogisticGroupService logisticGroupService;

    @Autowired
    private IShipmentDetailService shipmentDetailService;

    @Autowired
    private IShipmentCommentService shipmentCommentService;

    @Autowired
    private DictService dictService;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private DictService dictDataService;
    
    @Autowired
    private IShipmentImageService shipmentImageService;
    
    @GetMapping
    public String getViewDocument(@RequestParam(required = false) Long sId, ModelMap mmap) {

        if (sId != null) {
            mmap.put("sId", sId);
        }
        mmap.put("domain", serverConfig.getUrl());
        // Get list logistic group
        LogisticGroup logisticGroup = new LogisticGroup();
        logisticGroup.setGroupName("Chọn đơn vị Logistics");
        logisticGroup.setId(0L);
        LogisticGroup logisticGroupParam = new LogisticGroup();
        logisticGroupParam.setDelFlag("0");
        List<LogisticGroup> logisticGroups = logisticGroupService.selectLogisticGroupList(logisticGroupParam);
        logisticGroups.add(0, logisticGroup);
        mmap.put("logisticGroups", logisticGroups);

        List<SysDictData> dictDatas = dictService.getType("opr_list_booking_check");
        if (CollectionUtils.isEmpty(dictDatas)) {
            dictDatas = new ArrayList<>();
        }
        SysDictData sysDictData = new SysDictData();
        sysDictData.setDictLabel("Chọn OPR");
        sysDictData.setDictValue("Chọn OPR");
        dictDatas.add(0, sysDictData);
        mmap.put("oprList", dictDatas);
        
        mmap.put("contCargoTypes", dictDataService.getType("cont_cargo_type"));
        mmap.put("contDangerousImos", dictDataService.getType("cont_dangerous_imo"));
        mmap.put("contDangerousUnnos", dictDataService.getType("cont_dangerous_unno"));
        
        return PREFIX + "/index";
    }

    @PostMapping("/shipments")
    @ResponseBody
    public AjaxResult getShipments(@RequestBody PageAble<Shipment> param) {
        startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
        AjaxResult ajaxResult = AjaxResult.success();
        Shipment shipment = param.getData();
        if (shipment == null) {
            shipment = new Shipment();
        }
        shipment.setServiceType(EportConstants.SERVICE_DROP_FULL);
        Map<String, Object> params = shipment.getParams();
        if (params == null) {
            params = new HashMap<>();
        }
        // get cont is cont dangerous
        params.put("dangerous", EportConstants.CONT_REQUEST_DANGEROUS_PENDING);
        params.put("doStatus", "");
        
        shipment.setParams(params);
        
        List<Shipment> shipments = shipmentService.selectShipmentListByWithShipmentDetailFilter(shipment);

        ajaxResult.put("shipments", getDataTable(shipments));
        return ajaxResult;
    }

    @GetMapping("/shipment/{shipmentId}/shipmentDetails")
    @ResponseBody
    public AjaxResult getShipmentDetails(@PathVariable("shipmentId") Long shipmentId) {
        AjaxResult ajaxResult = AjaxResult.success();
        ShipmentDetail shipmentDetail = new ShipmentDetail();
        shipmentDetail.setShipmentId(shipmentId);
        shipmentDetail.setDangerous(EportConstants.CONT_REQUEST_DANGEROUS_PENDING);
        List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailList(shipmentDetail);
        ajaxResult.put("shipmentDetails", shipmentDetails);
        return ajaxResult;
    }

    @GetMapping("/reject/shipment/{shipmentId}/logistic-group/{logisticGroupId}/shipment-detail/{shipmentDetailIds}")
    public String openRejectComment(@PathVariable("shipmentId") String shipmentId,
                                    @PathVariable("shipmentDetailIds") String shipmentDetailIds,
                                    @PathVariable("logisticGroupId") String logisticGroupId, ModelMap mmap) {
        mmap.put("shipmentId", shipmentId);
        mmap.put("logisticGroupId", logisticGroupId);
        mmap.put("shipmentDetailIds", shipmentDetailIds);
        return PREFIX + "/reject";
    }

    @PostMapping("/confirmation")
    @ResponseBody
    @Transactional
    public AjaxResult acceptRequestContIce(String shipmentDetailIds, Long logisticGroupId) {
        ShipmentDetail shipmentDetail = new ShipmentDetail();
        shipmentDetail.setDangerous(EportConstants.CONT_REQUEST_DANGEROUS_APPROVE);
        shipmentDetail.setUpdateBy(getUser().getLoginName());
        shipmentDetailService.updateShipmentDetailByIds(shipmentDetailIds, shipmentDetail);
        
        return success("Xác nhận thành công.");
    }

    @PostMapping("/reject")
    @ResponseBody
    @Transactional
    public AjaxResult rejectRequestContIce(String shipmentDetailIds) {
        ShipmentDetail shipmentDetail = new ShipmentDetail();

        shipmentDetail.setDangerous(EportConstants.CONT_REQUEST_DANGEROUS_REJECT);
        shipmentDetail.setUpdateBy(getUser().getLoginName());

        shipmentDetailService.updateShipmentDetailByIds(shipmentDetailIds, shipmentDetail);

        return success("Đã từ chối yêu cầu.");
    }

    @PostMapping("/shipment/comment")
    @ResponseBody
    public AjaxResult addNewCommentToSend(@RequestBody ShipmentComment shipmentComment) {
        SysUser user = getUser();
        shipmentComment.setCreateBy(user.getUserName());
        shipmentComment.setUserId(user.getUserId());
        shipmentComment.setUserType(EportConstants.COMMENTOR_DNP_STAFF);
        shipmentComment.setUserAlias(user.getDept().getDeptName());
        shipmentComment.setUserName(user.getUserName());
        shipmentComment.setServiceType(EportConstants.SERVICE_PICKUP_EMPTY);
        shipmentComment.setCommentTime(new Date());
        shipmentComment.setResolvedFlg(true);
        shipmentCommentService.insertShipmentComment(shipmentComment);

        // Add id to make background grey (different from other comment)
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put("shipmentCommentId", shipmentComment.getId());
        return ajaxResult;
    }
    
    @GetMapping("/shipment-detail/{shipmentDetailId}/cont/{containerNo}/sztp/{sztp}/detail")
    public String getShipmentDetailInputForm(@PathVariable("shipmentDetailId") Long shipmentDetailId,
            @PathVariable("containerNo") String containerNo, @PathVariable("sztp") String sztp, ModelMap mmap) {
//        mmap.put("containerNo", containerNo);
//        mmap.put("sztp", sztp);
//        mmap.put("shipmentDetailId", shipmentDetailId);
//
//        mmap.put("contCargoTypes", dictDataService.getType("cont_cargo_type"));
//        mmap.put("contDangerousImos", dictDataService.getType("cont_dangerous_imo"));
//        mmap.put("contDangerousUnnos", dictDataService.getType("cont_dangerous_unno"));
//        mmap.put("shipmentDetail", this.shipmentDetailService.selectShipmentDetailById(shipmentDetailId));
//        
//        ShipmentImage shipmentImage = new ShipmentImage();
//		shipmentImage.setShipmentDetailId(shipmentDetailId.toString());
//		List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageList(shipmentImage);
//		for (ShipmentImage shipmentImage2 : shipmentImages) {
//			shipmentImage2.setPath(serverConfig.getUrl() + shipmentImage2.getPath());
//		}
//		mmap.put("shipmentFiles", shipmentImages);
//		
//        return PREFIX + "/detail";
    	mmap.put("containerNo", containerNo);
        mmap.put("sztp", sztp);
        mmap.put("shipmentDetailId", shipmentDetailId);

        mmap.put("contCargoTypes", dictDataService.getType("cont_cargo_type"));
        mmap.put("contDangerousImos", dictDataService.getType("cont_dangerous_imo"));
        mmap.put("contDangerousUnnos", dictDataService.getType("cont_dangerous_unno"));

        mmap.put("shipmentDetail", this.shipmentDetailService.selectShipmentDetailById(shipmentDetailId));
        
        ShipmentImage shipmentImage = new ShipmentImage();
		shipmentImage.setShipmentDetailId(shipmentDetailId.toString());
		List<ShipmentImage> shipmentImages = shipmentImageService.selectShipmentImageList(shipmentImage);
		for (ShipmentImage shipmentImage2 : shipmentImages) {
			shipmentImage2.setPath(serverConfig.getUrl() + shipmentImage2.getPath());
		}
		mmap.put("shipmentFiles", shipmentImages);
        return PREFIX + "/detail";
    }

    

}
