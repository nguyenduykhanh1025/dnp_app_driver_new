package vn.com.irtech.eport.carrier.service;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.EdoAuditLog;

/**
 * eDO Audit Trail LogService Interface
 * 
 * @author ruoyi
 * @date 2020-07-07
 */
public interface IEdoAuditLogService 
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
     * @return result
     */
    public int insertEdoAuditLog(EdoAuditLog edoAuditLog);

    /**
     * Update eDO Audit Trail Log
     * 
     * @param edoAuditLog eDO Audit Trail Log
     * @return result
     */
    public int updateEdoAuditLog(EdoAuditLog edoAuditLog);

    /**
     * Batch Delete eDO Audit Trail Log
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteEdoAuditLogByIds(String ids);

    /**
     * Delete eDO Audit Trail Log
     * 
     * @param id eDO Audit Trail LogID
     * @return result
     */
    public int deleteEdoAuditLogById(Long id);

    public long getSeqNo(Long id);

    public EdoAuditLog selectEdoAuditLogByEdo(EdoAuditLog edoAuditLog);

    public int insertEdoAuditLogExpiredDem(EdoAuditLog edoAuditLog);

    public int insertEdoAuditLogDetFreeTime(EdoAuditLog edoAuditLog);

    public String selectEdoAuditLogByEdoId(Long id);

}
