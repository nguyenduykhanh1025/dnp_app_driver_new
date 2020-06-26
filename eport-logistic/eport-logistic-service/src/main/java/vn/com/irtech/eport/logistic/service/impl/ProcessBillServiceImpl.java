package vn.com.irtech.eport.logistic.service.impl;

import java.util.Date;
import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
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
import vn.com.irtech.eport.logistic.service.IProcessBillService;
import vn.com.irtech.eport.common.config.Global;
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
    	if (processOrder.getReferenceNo() != null) {
    		List<ProcessBill> processBills = getUnitBillList(processOrder.getReferenceNo());
        	if (processBills != null) {
        		for (ProcessBill processBill : processBills) {
        			processBill.setProcessOrderId(processOrder.getId());
        			processBill.setServiceType(processOrder.getServiceType());
        			processBill.setShipmentId(processOrder.getShipmentId());
        			processBill.setCreateTime(new Date());
        			processBillMapper.insertProcessBill(processBill);
        		}
        	}
        	return true;
    	}
    	return false;
    }

    @Override
    public List<ProcessBill> selectProcessBillListByProcessOrderIds(String processOrderIds) {
        return processBillMapper.selectProcessBillListByProcessOrderIds(Convert.toStrArray(processOrderIds));
    }
}
