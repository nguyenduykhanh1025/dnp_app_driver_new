package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;

/**
 * Process order historyMapper Interface
 * 
 * @author HieuNT
 * @date 2020-06-27
 */
public interface ProcessHistoryMapper 
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
     * @return Result
     */
    public int insertProcessHistory(ProcessHistory processHistory);

    /**
     * Update Process order history
     * 
     * @param processHistory Process order history
     * @return Result
     */
    public int updateProcessHistory(ProcessHistory processHistory);

    /**
     * Delete Process order history
     * 
     * @param id Process order historyID
     * @return result
     */
    public int deleteProcessHistoryById(Long id);

    /**
     * Batch Delete Process order history
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteProcessHistoryByIds(String[] ids);

    public List<ProcessHistory> selectRobotHistory(ProcessHistory processHistory);
}
