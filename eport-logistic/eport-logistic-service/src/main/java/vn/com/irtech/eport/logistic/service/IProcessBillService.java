package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.ProcessBill;

/**
 * Process billingService Interface
 * 
 * @author admin
 * @date 2020-06-25
 */
public interface IProcessBillService 
{
    /**
     * Get Process billing
     * 
     * @param id Process billingID
     * @return Process billing
     */
    public ProcessBill selectProcessBillById(Long id);

    /**
     * Get Process billing List
     * 
     * @param processBill Process billing
     * @return Process billing List
     */
    public List<ProcessBill> selectProcessBillList(ProcessBill processBill);

    /**
     * Add Process billing
     * 
     * @param processBill Process billing
     * @return result
     */
    public int insertProcessBill(ProcessBill processBill);

    /**
     * Update Process billing
     * 
     * @param processBill Process billing
     * @return result
     */
    public int updateProcessBill(ProcessBill processBill);

    /**
     * Batch Delete Process billing
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteProcessBillByIds(String ids);

    /**
     * Delete Process billing
     * 
     * @param id Process billingID
     * @return result
     */
    public int deleteProcessBillById(Long id);
    
    public List<ProcessBill> getUnitBillList(String invNo);
}
