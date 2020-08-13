package vn.com.irtech.eport.carrier.service;

import java.util.List;

import vn.com.irtech.eport.carrier.domain.EquipmentDoAuditLog;
import vn.com.irtech.eport.equipment.domain.EquipmentDo;

/**
 * Do Audit Trail LogService Interface
 * 
 * @author irtech
 * @date 2020-07-31
 */
public interface IEquipmentDoAuditLogService 
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
     * @return result
     */
    public int insertEquipmentDoAuditLog(EquipmentDoAuditLog equipmentDoAuditLog);

    /**
     * Update Do Audit Trail Log
     * 
     * @param equipmentDoAuditLog Do Audit Trail Log
     * @return result
     */
    public int updateEquipmentDoAuditLog(EquipmentDoAuditLog equipmentDoAuditLog);

    /**
     * Batch Delete Do Audit Trail Log
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteEquipmentDoAuditLogByIds(String ids);

    /**
     * Delete Do Audit Trail Log
     * 
     * @param id Do Audit Trail LogID
     * @return result
     */
    public int deleteEquipmentDoAuditLogById(Long id);

    public long getSeqNo(Long id);

    public EquipmentDoAuditLog selectDoAuditLogByDo(EquipmentDoAuditLog doAuditLog);

    public int insertDoAuditLogExpiredDem(EquipmentDoAuditLog doAuditLog);

    public int insertDoAuditLogDetFreeTime(EquipmentDoAuditLog doAuditLog);

    public String selectDoAuditLogByDoId(Long id);

    public boolean addAuditLogFirst(EquipmentDo doItem);

    public boolean updateAuditLog(EquipmentDo doItem);

}
