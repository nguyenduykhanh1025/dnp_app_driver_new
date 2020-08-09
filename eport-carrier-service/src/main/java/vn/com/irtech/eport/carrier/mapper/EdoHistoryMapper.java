package vn.com.irtech.eport.carrier.mapper;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.EdoHistory;

/**
 * eDO Action HistoryMapper Interface
 * 
 * @author irtech
 * @date 2020-06-26
 */
public interface EdoHistoryMapper 
{
    /**
     * Get eDO Action History
     * 
     * @param id eDO Action HistoryID
     * @return eDO Action History
     */
    public EdoHistory selectEdoHistoryById(Long id);

    /**
     * Get eDO Action History List
     * 
     * @param edoHistory eDO Action History
     * @return eDO Action History List
     */
    public List<EdoHistory> selectEdoHistoryList(EdoHistory edoHistory);

    /**
     * Add eDO Action History
     * 
     * @param edoHistory eDO Action History
     * @return Result
     */
    public int insertEdoHistory(EdoHistory edoHistory);

    /**
     * Update eDO Action History
     * 
     * @param edoHistory eDO Action History
     * @return Result
     */
    public int updateEdoHistory(EdoHistory edoHistory);

    /**
     * Delete eDO Action History
     * 
     * @param id eDO Action HistoryID
     * @return result
     */
    public int deleteEdoHistoryById(Long id);

    /**
     * Batch Delete eDO Action History
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteEdoHistoryByIds(String[] ids);

    public EdoHistory selectEdoHistoryByFileName(String fileName);
}
