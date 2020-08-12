package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.irtech.eport.logistic.domain.PaymentHistory;
import vn.com.irtech.eport.logistic.mapper.PaymentHistoryMapper;
import vn.com.irtech.eport.logistic.service.IPaymentHistoryService;

/**
 * Payment History Service Business Processing
 * 
 * @author HieuNT
 * @date 2020-07-22
 */
@Service
public class PaymentHistoryServiceImpl implements IPaymentHistoryService 
{

	@Autowired
	private PaymentHistoryMapper paymentHistoryMapper;
	
	@Override
    public PaymentHistory selectPaymentHistoryById(Long id) {
		return paymentHistoryMapper.selectPaymentHistoryById(id);
	}

	@Override
    public List<PaymentHistory> selectPaymentHistoryList(PaymentHistory paymentHistory) {
		return paymentHistoryMapper.selectPaymentHistoryList(paymentHistory);
	}

	@Override
    public int insertPaymentHistory(PaymentHistory paymentHistory) {
		return paymentHistoryMapper.insertPaymentHistory(paymentHistory);
	}

	@Override
    public int updatePaymentHistory(PaymentHistory paymentHistory) {
		return paymentHistoryMapper.updatePaymentHistory(paymentHistory);
	}

	@Override
    public int deletePaymentHistoryById(Long id) {
		return paymentHistoryMapper.deletePaymentHistoryById(id);
	}
	
	/**
     * Select payment history list om
     * 
     * @param paymentHistory
     * @return List<PaymentHistory>
     */
	@Override
    public List<PaymentHistory> selectPaymentHistoryListOm(PaymentHistory paymentHistory) {
		return paymentHistoryMapper.selectPaymentHistoryListOm(paymentHistory);
	}
}
