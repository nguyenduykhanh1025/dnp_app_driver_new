package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.CfsHouseBill;

/**
 * CFS House BillMapper Interface
 * 
 * @author Trong Hieu
 * @date 2020-11-23
 */
public interface CfsHouseBillMapper 
{
    /**
     * Get CFS House Bill
     * 
     * @param id CFS House BillID
     * @return CFS House Bill
     */
    public CfsHouseBill selectCfsHouseBillById(Long id);

    /**
     * Get CFS House Bill List
     * 
     * @param cfsHouseBill CFS House Bill
     * @return CFS House Bill List
     */
    public List<CfsHouseBill> selectCfsHouseBillList(CfsHouseBill cfsHouseBill);

    /**
     * Add CFS House Bill
     * 
     * @param cfsHouseBill CFS House Bill
     * @return Result
     */
    public int insertCfsHouseBill(CfsHouseBill cfsHouseBill);

    /**
     * Update CFS House Bill
     * 
     * @param cfsHouseBill CFS House Bill
     * @return Result
     */
    public int updateCfsHouseBill(CfsHouseBill cfsHouseBill);

    /**
     * Delete CFS House Bill
     * 
     * @param id CFS House BillID
     * @return result
     */
    public int deleteCfsHouseBillById(Long id);

    /**
     * Batch Delete CFS House Bill
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteCfsHouseBillByIds(String[] ids);
}
