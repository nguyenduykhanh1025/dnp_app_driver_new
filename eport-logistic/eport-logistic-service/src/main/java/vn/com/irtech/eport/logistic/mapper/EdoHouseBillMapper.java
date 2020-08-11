package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.EdoHouseBill;

/**
 * Master BillMapper Interface
 * 
 * @author irtech
 * @date 2020-08-10
 */
public interface EdoHouseBillMapper 
{
    /**
     * Get Master Bill
     * 
     * @param id Master BillID
     * @return Master Bill
     */
    public EdoHouseBill selectEdoHouseBillById(Long id);

    /**
     * Get Master Bill List
     * 
     * @param edoHouseBill Master Bill
     * @return Master Bill List
     */
    public List<EdoHouseBill> selectEdoHouseBillList(EdoHouseBill edoHouseBill);

    /**
     * Add Master Bill
     * 
     * @param edoHouseBill Master Bill
     * @return Result
     */
    public int insertEdoHouseBill(EdoHouseBill edoHouseBill);

    /**
     * Update Master Bill
     * 
     * @param edoHouseBill Master Bill
     * @return Result
     */
    public int updateEdoHouseBill(EdoHouseBill edoHouseBill);

    /**
     * Delete Master Bill
     * 
     * @param id Master BillID
     * @return result
     */
    public int deleteEdoHouseBillById(Long id);

    /**
     * Batch Delete Master Bill
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteEdoHouseBillByIds(String[] ids);
}
