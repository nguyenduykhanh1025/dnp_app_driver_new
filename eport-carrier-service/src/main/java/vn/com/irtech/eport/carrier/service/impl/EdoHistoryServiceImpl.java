package vn.com.irtech.eport.carrier.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.carrier.mapper.EdoHistoryMapper;
import vn.com.irtech.eport.carrier.domain.EdoHistory;
import vn.com.irtech.eport.carrier.service.IEdoHistoryService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * eDO Action HistoryService Business Processing
 * 
 * @author irtech
 * @date 2020-06-26
 */
@Service
public class EdoHistoryServiceImpl implements IEdoHistoryService 
{
    @Autowired
    private EdoHistoryMapper edoHistoryMapper;

    /**
     * Get eDO Action History
     * 
     * @param id eDO Action HistoryID
     * @return eDO Action History
     */
    @Override
    public EdoHistory selectEdoHistoryById(Long id)
    {
        return edoHistoryMapper.selectEdoHistoryById(id);
    }

    /**
     * Get eDO Action History List
     * 
     * @param edoHistory eDO Action History
     * @return eDO Action History
     */
    @Override
    public List<EdoHistory> selectEdoHistoryList(EdoHistory edoHistory)
    {
        return edoHistoryMapper.selectEdoHistoryList(edoHistory);
    }

    /**
     * Add eDO Action History
     * 
     * @param edoHistory eDO Action History
     * @return result
     */
    @Override
    public int insertEdoHistory(EdoHistory edoHistory)
    {
        edoHistory.setCreateTime(DateUtils.getNowDate());
        return edoHistoryMapper.insertEdoHistory(edoHistory);
    }

    /**
     * Update eDO Action History
     * 
     * @param edoHistory eDO Action History
     * @return result
     */
    @Override
    public int updateEdoHistory(EdoHistory edoHistory)
    {
        edoHistory.setUpdateTime(DateUtils.getNowDate());
        return edoHistoryMapper.updateEdoHistory(edoHistory);
    }

    /**
     * Delete eDO Action History By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteEdoHistoryByIds(String ids)
    {
        return edoHistoryMapper.deleteEdoHistoryByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete eDO Action History
     * 
     * @param id eDO Action HistoryID
     * @return result
     */
    @Override
    public int deleteEdoHistoryById(Long id)
    {
        return edoHistoryMapper.deleteEdoHistoryById(id);
    }

    @Override
    public EdoHistory selectEdoHistoryByFileName(String fileName)
	{
		return edoHistoryMapper.selectEdoHistoryByFileName(fileName);
	}

}
