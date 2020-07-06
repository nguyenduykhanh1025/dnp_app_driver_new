package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;

/**
 * Process order historyService Interface
 * 
 * @author HieuNT
 * @date 2020-06-27
 */
public interface IProcessHistoryService 
{
    /**
     * Get Process order history
     * 
     * @param id Process order historyID
     * @return Process order history
     */
    public ProcessHistory selectProcessHistoryById(Long id);

    /**
     * Get Process order history List
     * 
     * @param processHistory Process order history
     * @return Process order history List
     */
    public List<ProcessHistory> selectProcessHistoryList(ProcessHistory processHistory);

    /**
     * Add Process order history
     * 
     * @param processHistory Process order history
     * @return result
     */
    public int insertProcessHistory(ProcessHistory processHistory);

    /**
     * Update Process order history
     * 
     * @param processHistory Process order history
     * @return result
     */
    public int updateProcessHistory(ProcessHistory processHistory);

    /**
     * Batch Delete Process order history
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteProcessHistoryByIds(String ids);

    /**
     * Delete Process order history
     * 
     * @param id Process order historyID
     * @return result
     */
    public int deleteProcessHistoryById(Long id);

    public List<ProcessHistory> selectRobotHistory(ProcessHistory processHistory);
}
