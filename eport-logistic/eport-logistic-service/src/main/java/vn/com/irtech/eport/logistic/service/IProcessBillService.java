package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;

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
    
    public boolean saveProcessBillByInvoiceNo(ProcessOrder processOrder);

    public List<ProcessBill> selectProcessBillListByProcessOrderIds(String processOrderIds);

    public List<ProcessBill> selectBillReportList(ProcessBill processBill);

    public boolean saveProcessBillWithCredit(List<ShipmentDetail> shipmentDetails, ProcessOrder processOrder);

    public Long sumOfBillList(ProcessBill processBill);

    public List<ProcessBill> getBillListByShipmentId(Long shipmentId);

    public int updateBillList(ProcessBill processBill);

    /**
     * Get sum of total list bill
     * 
     * @param proccessOrderIds
     * @return
     */
    public Long getSumOfTotalBillList(String[] proccessOrderIds);

    /**
     * Update payment status for bill list by process order ids
     * 
     * @param processOrderIds
     * @return
     */
    public int updateBillListByProcessOrderIds(String processOrderIds);

    /**
     * Get Bill Shifting Cont By Shipment Id
     * 
     * @param shipmentId
     * @param logisticGroupId
     * @return  List<ProcessBill>
     */
    public List<ProcessBill> getBillShiftingContByShipmentId(Long shipmentId, Long logisticGroupId);
    
    /***
     * Get bill
     * @param shipmentDetail
     * @return
     */
    public List<ProcessBill> getBillByShipmentDetail(ShipmentDetail shipmentDetail);
    /***
     * Get bill for SSR
     * @param shipmentDetail
     * @return
     */
    public List<ProcessBill> getBillByShipmentDetailForSSR(ShipmentDetail shipmentDetail);
}
