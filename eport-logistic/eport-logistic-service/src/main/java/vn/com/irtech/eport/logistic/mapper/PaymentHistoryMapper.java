package vn.com.irtech.eport.logistic.mapper;

import java.util.List;

import vn.com.irtech.eport.logistic.domain.PaymentHistory;


/**
 * Payment History Mapper Interface
 * 
 * @author HieuNT
 * @date 2020-07-22
 */
public interface PaymentHistoryMapper 
{
    /**
     * 
     * @param id
     * @return
     */
    public PaymentHistory selectPaymentHistoryById(Long id);

    /**
     * 
     * @param paymentHistory
     * @return
     */
    public List<PaymentHistory> selectPaymentHistoryList(PaymentHistory paymentHistory);

    /**
     * 
     * @param paymentHistory
     * @return
     */
    public int insertPaymentHistory(PaymentHistory paymentHistory);

    /**
     * 
     * @param paymentHistory
     * @return
     */
    public int updatePaymentHistory(PaymentHistory paymentHistory);

    /**
     * 
     * @param id
     * @return
     */
    public int deletePaymentHistoryById(Long id);
    
    /**
     * Select payment history list om
     * 
     * @param paymentHistory
     * @return List<PaymentHistory>
     */
    public List<PaymentHistory> selectPaymentHistoryListOm(PaymentHistory paymentHistory);
}
