package vn.com.irtech.eport.logistic.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import vn.com.irtech.eport.common.utils.DateUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import vn.com.irtech.eport.logistic.mapper.ProcessBillMapper;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Process billingService Business Processing
 * 
 * @author admin
 * @date 2020-06-25
 */
@Service
public class ProcessBillServiceImpl implements IProcessBillService 
{
    @Autowired
    private ProcessBillMapper processBillMapper;
    
    @Autowired
    private ICatosApiService catosApiService;
    
    @Autowired
    private IShipmentDetailService shipmentDetailService;

    /**
     * Get Process billing
     * 
     * @param id Process billingID
     * @return Process billing
     */
    @Override
    public ProcessBill selectProcessBillById(Long id)
    {
        return processBillMapper.selectProcessBillById(id);
    }

    /**
     * Get Process billing List
     * 
     * @param processBill Process billing
     * @return Process billing
     */
    @Override
    public List<ProcessBill> selectProcessBillList(ProcessBill processBill)
    {
        return processBillMapper.selectProcessBillList(processBill);
    }

    /**
     * Add Process billing
     * 
     * @param processBill Process billing
     * @return result
     */
    @Override
    public int insertProcessBill(ProcessBill processBill)
    {
        processBill.setCreateTime(DateUtils.getNowDate());
        return processBillMapper.insertProcessBill(processBill);
    }

    /**
     * Update Process billing
     * 
     * @param processBill Process billing
     * @return result
     */
    @Override
    public int updateProcessBill(ProcessBill processBill)
    {
        processBill.setUpdateTime(DateUtils.getNowDate());
        return processBillMapper.updateProcessBill(processBill);
    }

    /**
     * Delete Process billing By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteProcessBillByIds(String ids)
    {
        return processBillMapper.deleteProcessBillByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Process billing
     * 
     * @param id Process billingID
     * @return result
     */
    @Override
    public int deleteProcessBillById(Long id)
    {
        return processBillMapper.deleteProcessBillById(id);
    }

	@Override
	public List<ProcessBill> getUnitBillList(String invNo) {
		String url = Global.getApiUrl() + "/unitBill/list/" + invNo;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<ProcessBill>> response = restTemplate
				.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ProcessBill>>() {
				});
		return response.getBody();
	}
    
    @Override
    @Transactional
    public boolean saveProcessBillByInvoiceNo(ProcessOrder processOrder) {
    	if (processOrder.getInvoiceNo() != null) {
    		List<ProcessBill> processBills = getUnitBillList(processOrder.getInvoiceNo());
        	if (processBills != null) {
        		for (ProcessBill processBill : processBills) {
        			processBill.setProcessOrderId(processOrder.getId());
        			processBill.setServiceType(processOrder.getServiceType());
        			processBill.setShipmentId(processOrder.getShipmentId());
                    processBill.setCreateTime(new Date());
                    processBill.setLogisticGroupId(processOrder.getLogisticGroupId());
                    processBill.setPaymentStatus("N");
                    processBill.setPayType("Cash");
        			processBillMapper.insertProcessBill(processBill);
        		}
        	}
        	return true;
    	}
    	return false;
    }

    @Override
    @Transactional
    public boolean saveProcessBillWithCredit(List<ShipmentDetail> shipmentDetails, ProcessOrder processOrder) {
        if (shipmentDetails != null && shipmentDetails.size() > 0) {
            for (ShipmentDetail shipmentDetail: shipmentDetails) {
                ProcessBill processBill = new ProcessBill();
                processBill.setProcessOrderId(shipmentDetail.getProcessOrderId());
                processBill.setLogisticGroupId(shipmentDetail.getLogisticGroupId());
                processBill.setShipmentId(shipmentDetail.getShipmentId());
                processBill.setSztp(shipmentDetail.getSztp());
                processBill.setContainerNo(shipmentDetail.getContainerNo());
                processBill.setServiceType(processOrder.getServiceType());
                processBill.setPayType("Credit");
                processBill.setPaymentStatus("Y");
                processBill.setPaymentTime(new Date());
                processBill.setCreateTime(new Date());
            }
            return true;
        }
        return false;
    }

    @Override
    public List<ProcessBill> selectProcessBillListByProcessOrderIds(String processOrderIds) {
        return processBillMapper.selectProcessBillListByProcessOrderIds(Convert.toStrArray(processOrderIds));
    }

    @Override
    public List<ProcessBill> selectBillReportList(ProcessBill processBill) {
        return processBillMapper.selectBillReportList(processBill);
    }

    @Override
    public Long sumOfBillList(ProcessBill processBill) {
        return processBillMapper.sumOfBillList(processBill);
    }

    @Override
    public List<ProcessBill> getBillListByShipmentId(Long shipmentId) {
        return processBillMapper.getBillListByShipmentId(shipmentId);
    }

    @Override
    public int updateBillList(ProcessBill processBill) {
        return processBillMapper.updateBillList(processBill);
    }

    @Override
    public Long getSumOfTotalBillList(String[] proccessOrderIds) {
        return processBillMapper.getSumOfTotalBillList(proccessOrderIds);
    }

    @Override
    public int updateBillListByProcessOrderIds(String processOrderIds) {
        return processBillMapper.updateBillListByProcessOrderIds(Convert.toStrArray(processOrderIds));
    }

    /**
     * Get Bill Shifting Cont By Shipment Id
     * 
     * @param shipmentId
     * @param logisticGroupId
     * @return  List<ProcessBill>
     */
    @Override
    public List<ProcessBill> getBillShiftingContByShipmentId(Long shipmentId, Long logisticGroupId) {
        return processBillMapper.getBillShiftingContByShipmentId(shipmentId, logisticGroupId);
    }
	@Override
	public List<ProcessBill> getBillByShipmentDetail(ShipmentDetail shipmentDetail) {
		if(shipmentDetail != null) {
			if(shipmentDetail.getServiceType().equals(Constants.SEND_CONT_FULL)) {
				return catosApiService.getUnitBillByShipmentDetailsForReserve(shipmentDetail);
			}
			if(shipmentDetail.getServiceType().equals(Constants.SEND_CONT_EMPTY)) {
				return catosApiService.getUnitBillByShipmentDetailsForReserve(shipmentDetail);
			}
			if(shipmentDetail.getServiceType().equals(Constants.RECEIVE_CONT_FULL)) {
				return catosApiService.getUnitBillByShipmentDetailsForInventory(shipmentDetail);
			}
			if(shipmentDetail.getServiceType().equals(Constants.RECEIVE_CONT_EMPTY)) {
				return catosApiService.getUnitBillByShipmentDetailsForInventory(shipmentDetail);
			}
		}
		return null;
	}

	@Override
	public List<ProcessBill> getBillByShipmentDetailForSSR(ShipmentDetail shipmentDetail) {
		if(shipmentDetail != null) {
			if(shipmentDetail.getServiceType().equals(Constants.SEND_CONT_FULL)) {
				return catosApiService.getUnitBillByShipmentDetailsForSendSSR(shipmentDetail);
			}
			if(shipmentDetail.getServiceType().equals(Constants.SEND_CONT_EMPTY)) {
				return catosApiService.getUnitBillByShipmentDetailsForSendSSR(shipmentDetail);
			}
			if(shipmentDetail.getServiceType().equals(Constants.RECEIVE_CONT_FULL)) {
				return catosApiService.getUnitBillByShipmentDetailsForReceiveSSR(shipmentDetail);
			}
			if(shipmentDetail.getServiceType().equals(Constants.RECEIVE_CONT_EMPTY)) {
				return catosApiService.getUnitBillByShipmentDetailsForReceiveSSR(shipmentDetail);
			}
		}
		return null;
	} 
	
	@Override
	public void saveShiftingBillWithCredit(List<Long> shipmentDetailIds, ProcessOrder processOrder) {
		String shipmentDetailIdsStr = shipmentDetailIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		List<ShipmentDetail> shipmentDetails = shipmentDetailService.selectShipmentDetailByIds(shipmentDetailIdsStr, null);
		if (CollectionUtils.isNotEmpty(shipmentDetails)) {
            for (ShipmentDetail shipmentDetail: shipmentDetails) {
                ProcessBill processBill = new ProcessBill();
                processBill.setProcessOrderId(shipmentDetail.getProcessOrderId());
                processBill.setLogisticGroupId(shipmentDetail.getLogisticGroupId());
                processBill.setShipmentId(shipmentDetail.getShipmentId());
                processBill.setSztp(shipmentDetail.getSztp());
                processBill.setContainerNo(shipmentDetail.getContainerNo());
                processBill.setServiceType(processOrder.getServiceType());
                processBill.setPayType("Credit");
                processBill.setPaymentStatus("Y");
                processBill.setPaymentTime(new Date());
                processBill.setCreateTime(new Date());
            }
        }
	}
}
