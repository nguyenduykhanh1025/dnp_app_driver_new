package vn.com.irtech.eport.carrier.mapper;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.EquipmentDoAuditLog;

/**
 * Do Audit Trail LogMapper Interface
 * 
 * @author admin
 * @date 2020-07-31
 */
public interface EquipmentDoAuditLogMapper 
{
    /**
     * Get Do Audit Trail Log
     * 
     * @param id Do Audit Trail LogID
     * @return Do Audit Trail Log
     */
    public EquipmentDoAuditLog selectEquipmentDoAuditLogById(Long id);

    /**
     * Get Do Audit Trail Log List
     * 
     * @param equipmentDoAuditLog Do Audit Trail Log
     * @return Do Audit Trail Log List
     */
    public List<EquipmentDoAuditLog> selectEquipmentDoAuditLogList(EquipmentDoAuditLog equipmentDoAuditLog);

    /**
     * Add Do Audit Trail Log
     * 
     * @param equipmentDoAuditLog Do Audit Trail Log
     * @return Result
     */
    public int insertEquipmentDoAuditLog(EquipmentDoAuditLog equipmentDoAuditLog);

    /**
     * Update Do Audit Trail Log
     * 
     * @param equipmentDoAuditLog Do Audit Trail Log
     * @return Result
     */
    public int updateEquipmentDoAuditLog(EquipmentDoAuditLog equipmentDoAuditLog);

    /**
     * Delete Do Audit Trail Log
     * 
     * @param id Do Audit Trail LogID
     * @return result
     */
    public int deleteEquipmentDoAuditLogById(Long id);

    /**
     * Batch Delete Do Audit Trail Log
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteEquipmentDoAuditLogByIds(String[] ids);

    public long getSeqNo(Long id);

    public EquipmentDoAuditLog selectDoAuditLogByDo(EquipmentDoAuditLog doAuditLog);

    public int insertDoAuditLogExpiredDem(EquipmentDoAuditLog doAuditLog);

    public int insertDoAuditLogDetFreeTime(EquipmentDoAuditLog doAuditLog);

    public String selectDoAuditLogByDoId(Long doId);
}
