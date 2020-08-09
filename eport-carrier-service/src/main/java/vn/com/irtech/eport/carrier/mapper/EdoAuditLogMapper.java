package vn.com.irtech.eport.carrier.mapper;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.EdoAuditLog;

/**
 * eDO Audit Trail LogMapper Interface
 * 
 * @author irtech
 * @date 2020-07-07
 */
public interface EdoAuditLogMapper 
{
    /**
     * Get eDO Audit Trail Log
     * 
     * @param id eDO Audit Trail LogID
     * @return eDO Audit Trail Log
     */
    public EdoAuditLog selectEdoAuditLogById(Long id);

    /**
     * Get eDO Audit Trail Log List
     * 
     * @param edoAuditLog eDO Audit Trail Log
     * @return eDO Audit Trail Log List
     */
    public List<EdoAuditLog> selectEdoAuditLogList(EdoAuditLog edoAuditLog);

    /**
     * Add eDO Audit Trail Log
     * 
     * @param edoAuditLog eDO Audit Trail Log
     * @return Result
     */
    public int insertEdoAuditLog(EdoAuditLog edoAuditLog);

    /**
     * Update eDO Audit Trail Log
     * 
     * @param edoAuditLog eDO Audit Trail Log
     * @return Result
     */
    public int updateEdoAuditLog(EdoAuditLog edoAuditLog);

    /**
     * Delete eDO Audit Trail Log
     * 
     * @param id eDO Audit Trail LogID
     * @return result
     */
    public int deleteEdoAuditLogById(Long id);

    /**
     * Batch Delete eDO Audit Trail Log
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteEdoAuditLogByIds(String[] ids);

    public long getSeqNo(Long id);

    public EdoAuditLog selectEdoAuditLogByEdo(EdoAuditLog edoAuditLog);

    public int insertEdoAuditLogExpiredDem(EdoAuditLog edoAuditLog);

    public int insertEdoAuditLogDetFreeTime(EdoAuditLog edoAuditLog);

    public String selectEdoAuditLogByEdoId(Long edoId);
}
