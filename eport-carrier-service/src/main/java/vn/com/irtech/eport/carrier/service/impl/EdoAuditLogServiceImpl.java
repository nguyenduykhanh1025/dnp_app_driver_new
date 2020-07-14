package vn.com.irtech.eport.carrier.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.carrier.mapper.EdoAuditLogMapper;
import vn.com.irtech.eport.carrier.domain.EdoAuditLog;
import vn.com.irtech.eport.carrier.service.IEdoAuditLogService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * eDO Audit Trail LogService Business Processing
 * 
 * @author ruoyi
 * @date 2020-07-07
 */
@Service
public class EdoAuditLogServiceImpl implements IEdoAuditLogService 
{
    @Autowired
    private EdoAuditLogMapper edoAuditLogMapper;

    /**
     * Get eDO Audit Trail Log
     * 
     * @param id eDO Audit Trail LogID
     * @return eDO Audit Trail Log
     */
    @Override
    public EdoAuditLog selectEdoAuditLogById(Long id)
    {
        return edoAuditLogMapper.selectEdoAuditLogById(id);
    }

    /**
     * Get eDO Audit Trail Log List
     * 
     * @param edoAuditLog eDO Audit Trail Log
     * @return eDO Audit Trail Log
     */
    @Override
    public List<EdoAuditLog> selectEdoAuditLogList(EdoAuditLog edoAuditLog)
    {
        return edoAuditLogMapper.selectEdoAuditLogList(edoAuditLog);
    }

    /**
     * Add eDO Audit Trail Log
     * 
     * @param edoAuditLog eDO Audit Trail Log
     * @return result
     */
    @Override
    public int insertEdoAuditLog(EdoAuditLog edoAuditLog)
    {
        edoAuditLog.setCreateTime(DateUtils.getNowDate());
        return edoAuditLogMapper.insertEdoAuditLog(edoAuditLog);
    }

    /**
     * Update eDO Audit Trail Log
     * 
     * @param edoAuditLog eDO Audit Trail Log
     * @return result
     */
    @Override
    public int updateEdoAuditLog(EdoAuditLog edoAuditLog)
    {
        edoAuditLog.setUpdateTime(DateUtils.getNowDate());
        return edoAuditLogMapper.updateEdoAuditLog(edoAuditLog);
    }

    /**
     * Delete eDO Audit Trail Log By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteEdoAuditLogByIds(String ids)
    {
        return edoAuditLogMapper.deleteEdoAuditLogByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete eDO Audit Trail Log
     * 
     * @param id eDO Audit Trail LogID
     * @return result
     */
    @Override
    public int deleteEdoAuditLogById(Long id)
    {
        return edoAuditLogMapper.deleteEdoAuditLogById(id);
    }

    @Override
    public long getSeqNo(Long id)
    {
        return edoAuditLogMapper.getSeqNo(id);
    }

    @Override
    public EdoAuditLog selectEdoAuditLogByEdo(EdoAuditLog edoAuditLog)
    {
        return edoAuditLogMapper.selectEdoAuditLogByEdo(edoAuditLog);
    }

    @Override
    public int insertEdoAuditLogExpiredDem(EdoAuditLog edoAuditLog)
    {
        return edoAuditLogMapper.insertEdoAuditLogExpiredDem(edoAuditLog);
    }

    @Override
    public int insertEdoAuditLogDetFreeTime(EdoAuditLog edoAuditLog)
    {
        return edoAuditLogMapper.insertEdoAuditLogDetFreeTime(edoAuditLog);
    }

    @Override
    public String selectEdoAuditLogByEdoId(Long edoId)
    {
        return edoAuditLogMapper.selectEdoAuditLogByEdoId(edoId);
    }


}
