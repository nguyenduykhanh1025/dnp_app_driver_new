package vn.com.irtech.eport.carrier.service;

import java.util.List;

import vn.com.irtech.eport.carrier.domain.EquipmentEdoAuditLog;
import vn.com.irtech.eport.equipment.domain.EquipmentDo;

/**
 * eDO Audit Trail LogService Interface
 * 
 * @author ruoyi
 * @date 2020-07-31
 */
public interface IEquipmentEdoAuditLogService 
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
     * @return result
     */
    public int insertEquipmentEdoAuditLog(EquipmentEdoAuditLog equipmentEdoAuditLog);

    /**
     * Update eDO Audit Trail Log
     * 
     * @param equipmentEdoAuditLog eDO Audit Trail Log
     * @return result
     */
    public int updateEquipmentEdoAuditLog(EquipmentEdoAuditLog equipmentEdoAuditLog);

    /**
     * Batch Delete eDO Audit Trail Log
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteEquipmentEdoAuditLogByIds(String ids);

    /**
     * Delete eDO Audit Trail Log
     * 
     * @param id eDO Audit Trail LogID
     * @return result
     */
    public int deleteEquipmentEdoAuditLogById(Long id);

    public long getSeqNo(Long id);

    public EquipmentEdoAuditLog selectEdoAuditLogByEdo(EquipmentEdoAuditLog edoAuditLog);

    public int insertEdoAuditLogExpiredDem(EquipmentEdoAuditLog edoAuditLog);

    public int insertEdoAuditLogDetFreeTime(EquipmentEdoAuditLog edoAuditLog);

    public String selectEdoAuditLogByEdoId(Long id);

    public boolean addAuditLogFirst(EquipmentDo edo);

    public boolean updateAuditLog(EquipmentDo edo);
}
