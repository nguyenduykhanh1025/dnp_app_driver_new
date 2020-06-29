package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.ProcessHistoryMapper;
import vn.com.irtech.eport.logistic.domain.ProcessHistory;
import vn.com.irtech.eport.logistic.service.IProcessHistoryService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Process order historyService Business Processing
 * 
 * @author HieuNT
 * @date 2020-06-27
 */
@Service
public class ProcessHistoryServiceImpl implements IProcessHistoryService 
{
    @Autowired
    private ProcessHistoryMapper processHistoryMapper;

    /**
     * Get Process order history
     * 
     * @param id Process order historyID
     * @return Process order history
     */
    @Override
    public ProcessHistory selectProcessHistoryById(Long id)
    {
        return processHistoryMapper.selectProcessHistoryById(id);
    }

    /**
     * Get Process order history List
     * 
     * @param processHistory Process order history
     * @return Process order history
     */
    @Override
    public List<ProcessHistory> selectProcessHistoryList(ProcessHistory processHistory)
    {
        return processHistoryMapper.selectProcessHistoryList(processHistory);
    }

    /**
     * Add Process order history
     * 
     * @param processHistory Process order history
     * @return result
     */
    @Override
    public int insertProcessHistory(ProcessHistory processHistory)
    {
        processHistory.setCreateTime(DateUtils.getNowDate());
        return processHistoryMapper.insertProcessHistory(processHistory);
    }

    /**
     * Update Process order history
     * 
     * @param processHistory Process order history
     * @return result
     */
    @Override
    public int updateProcessHistory(ProcessHistory processHistory)
    {
        processHistory.setUpdateTime(DateUtils.getNowDate());
        return processHistoryMapper.updateProcessHistory(processHistory);
    }

    /**
     * Delete Process order history By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteProcessHistoryByIds(String ids)
    {
        return processHistoryMapper.deleteProcessHistoryByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Process order history
     * 
     * @param id Process order historyID
     * @return result
     */
    @Override
    public int deleteProcessHistoryById(Long id)
    {
        return processHistoryMapper.deleteProcessHistoryById(id);
    }
}
