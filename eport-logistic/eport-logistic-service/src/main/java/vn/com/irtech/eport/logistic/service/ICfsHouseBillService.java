package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.CfsHouseBill;

/**
 * CFS House BillService Interface
 * 
 * @author Trong Hieu
 * @date 2020-11-23
 */
public interface ICfsHouseBillService 
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
     * @return result
     */
    public int insertCfsHouseBill(CfsHouseBill cfsHouseBill);

    /**
     * Update CFS House Bill
     * 
     * @param cfsHouseBill CFS House Bill
     * @return result
     */
    public int updateCfsHouseBill(CfsHouseBill cfsHouseBill);

    /**
     * Batch Delete CFS House Bill
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteCfsHouseBillByIds(String ids);

    /**
     * Delete CFS House Bill
     * 
     * @param id CFS House BillID
     * @return result
     */
    public int deleteCfsHouseBillById(Long id);
}
