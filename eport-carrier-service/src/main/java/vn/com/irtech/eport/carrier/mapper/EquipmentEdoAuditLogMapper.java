package vn.com.irtech.eport.carrier.mapper;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.EquipmentEdoAuditLog;

/**
 * eDO Audit Trail LogMapper Interface
 * 
 * @author ruoyi
 * @date 2020-07-31
 */
public interface EquipmentEdoAuditLogMapper 
{
    /**
     * Get eDO Audit Trail Log
     * 
     * @param id eDO Audit Trail LogID
     * @return eDO Audit Trail Log
     */
    public EquipmentEdoAuditLog selectEquipmentEdoAuditLogById(Long id);

    /**
     * Get eDO Audit Trail Log List
     * 
     * @param equipmentEdoAuditLog eDO Audit Trail Log
     * @return eDO Audit Trail Log List
     */
    public List<EquipmentEdoAuditLog> selectEquipmentEdoAuditLogList(EquipmentEdoAuditLog equipmentEdoAuditLog);

    /**
     * Add eDO Audit Trail Log
     * 
     * @param equipmentEdoAuditLog eDO Audit Trail Log
     * @return Result
     */
    public int insertEquipmentEdoAuditLog(EquipmentEdoAuditLog equipmentEdoAuditLog);

    /**
     * Update eDO Audit Trail Log
     * 
     * @param equipmentEdoAuditLog eDO Audit Trail Log
     * @return Result
     */
    public int updateEquipmentEdoAuditLog(EquipmentEdoAuditLog equipmentEdoAuditLog);

    /**
     * Delete eDO Audit Trail Log
     * 
     * @param id eDO Audit Trail LogID
     * @return result
     */
    public int deleteEquipmentEdoAuditLogById(Long id);

    /**
     * Batch Delete eDO Audit Trail Log
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteEquipmentEdoAuditLogByIds(String[] ids);

    public long getSeqNo(Long id);

    public EquipmentEdoAuditLog selectEdoAuditLogByEdo(EquipmentEdoAuditLog edoAuditLog);

    public int insertEdoAuditLogExpiredDem(EquipmentEdoAuditLog edoAuditLog);

    public int insertEdoAuditLogDetFreeTime(EquipmentEdoAuditLog edoAuditLog);

    public String selectEdoAuditLogByEdoId(Long edoId);
}
