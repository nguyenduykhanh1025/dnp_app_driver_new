package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.ProcessBill;

/**
 * Process billingMapper Interface
 * 
 * @author admin
 * @date 2020-06-25
 */
public interface ProcessBillMapper 
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
     * @return Result
     */
    public int insertProcessBill(ProcessBill processBill);

    /**
     * Update Process billing
     * 
     * @param processBill Process billing
     * @return Result
     */
    public int updateProcessBill(ProcessBill processBill);

    /**
     * Delete Process billing
     * 
     * @param id Process billingID
     * @return result
     */
    public int deleteProcessBillById(Long id);

    /**
     * Batch Delete Process billing
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteProcessBillByIds(String[] ids);

    public List<ProcessBill> selectProcessBillListByProcessOrderIds(String[] processOrderIds);

    public List<ProcessBill> selectBillReportList(ProcessBill processBill);

    public Long sumOfBillList(ProcessBill processBill);

    public List<ProcessBill> getBillListByShipmentId(Long shipmentId);

    public int updateBillList(ProcessBill processBill);

    public Long getSumOfTotalBillList(String[] proccessOrderIds);
}
