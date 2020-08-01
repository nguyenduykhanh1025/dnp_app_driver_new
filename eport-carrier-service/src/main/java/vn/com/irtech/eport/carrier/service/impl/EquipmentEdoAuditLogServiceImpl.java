package vn.com.irtech.eport.carrier.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.equipment.domain.EquipmentDo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.irtech.eport.carrier.mapper.EquipmentEdoAuditLogMapper;
import vn.com.irtech.eport.carrier.domain.EquipmentEdoAuditLog;
import vn.com.irtech.eport.carrier.service.IEquipmentEdoAuditLogService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * eDO Audit Trail LogService Business Processing
 * 
 * @author minhtc
 * @date 2020-07-31
 */
@Service
public class EquipmentEdoAuditLogServiceImpl implements IEquipmentEdoAuditLogService 
{
    @Autowired
    private EquipmentEdoAuditLogMapper equipmentEdoAuditLogMapper;

    /**
     * Get eDO Audit Trail Log
     * 
     * @param id eDO Audit Trail LogID
     * @return eDO Audit Trail Log
     */
    @Override
    public EquipmentEdoAuditLog selectEquipmentEdoAuditLogById(Long id)
    {
        return equipmentEdoAuditLogMapper.selectEquipmentEdoAuditLogById(id);
    }

    /**
     * Get eDO Audit Trail Log List
     * 
     * @param equipmentEdoAuditLog eDO Audit Trail Log
     * @return eDO Audit Trail Log
     */
    @Override
    public List<EquipmentEdoAuditLog> selectEquipmentEdoAuditLogList(EquipmentEdoAuditLog equipmentEdoAuditLog)
    {
        return equipmentEdoAuditLogMapper.selectEquipmentEdoAuditLogList(equipmentEdoAuditLog);
    }

    /**
     * Add eDO Audit Trail Log
     * 
     * @param equipmentEdoAuditLog eDO Audit Trail Log
     * @return result
     */
    @Override
    public int insertEquipmentEdoAuditLog(EquipmentEdoAuditLog equipmentEdoAuditLog)
    {
        equipmentEdoAuditLog.setCreateTime(DateUtils.getNowDate());
        return equipmentEdoAuditLogMapper.insertEquipmentEdoAuditLog(equipmentEdoAuditLog);
    }

    /**
     * Update eDO Audit Trail Log
     * 
     * @param equipmentEdoAuditLog eDO Audit Trail Log
     * @return result
     */
    @Override
    public int updateEquipmentEdoAuditLog(EquipmentEdoAuditLog equipmentEdoAuditLog)
    {
        equipmentEdoAuditLog.setUpdateTime(DateUtils.getNowDate());
        return equipmentEdoAuditLogMapper.updateEquipmentEdoAuditLog(equipmentEdoAuditLog);
    }

    /**
     * Delete eDO Audit Trail Log By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteEquipmentEdoAuditLogByIds(String ids)
    {
        return equipmentEdoAuditLogMapper.deleteEquipmentEdoAuditLogByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete eDO Audit Trail Log
     * 
     * @param id eDO Audit Trail LogID
     * @return result
     */
    @Override
    public int deleteEquipmentEdoAuditLogById(Long id)
    {
        return equipmentEdoAuditLogMapper.deleteEquipmentEdoAuditLogById(id);
    }

    @Override
    public long getSeqNo(Long id)
    {
        return equipmentEdoAuditLogMapper.getSeqNo(id);
    }

    @Override
    public EquipmentEdoAuditLog selectEdoAuditLogByEdo(EquipmentEdoAuditLog edoAuditLog)
    {
        return equipmentEdoAuditLogMapper.selectEdoAuditLogByEdo(edoAuditLog);
    }

    @Override
    public int insertEdoAuditLogExpiredDem(EquipmentEdoAuditLog edoAuditLog)
    {
        return equipmentEdoAuditLogMapper.insertEdoAuditLogExpiredDem(edoAuditLog);
    }

    @Override
    public int insertEdoAuditLogDetFreeTime(EquipmentEdoAuditLog edoAuditLog)
    {
        return equipmentEdoAuditLogMapper.insertEdoAuditLogDetFreeTime(edoAuditLog);
    }

    @Override
    public String selectEdoAuditLogByEdoId(Long edoId)
    {
        return equipmentEdoAuditLogMapper.selectEdoAuditLogByEdoId(edoId);
    }

    @Override
    @Transactional
    public boolean addAuditLogFirst(EquipmentDo edo)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date timeNow = new Date();
        int edoSeg = 1;
        EquipmentEdoAuditLog edoAuditLog = new EquipmentEdoAuditLog();
        edoAuditLog.setCarrierId(edo.getCarrierId());
        edoAuditLog.setCarrierCode(edo.getCarrierCode());
        edoAuditLog.setCreateBy(edo.getCarrierCode());
        edoAuditLog.setEdoId(edo.getId());
        if(edo.getExpiredDem() != null)
        {
            edoAuditLog.setSeqNo((long) edoSeg);
            edoAuditLog.setCreateTime(timeNow);
            edoAuditLog.setFieldName("Expired Dem");
            edoAuditLog.setNewValue(formatter.format(edo.getExpiredDem()).toString());
            insertEdoAuditLogExpiredDem(edoAuditLog);
            edoSeg +=1;
        }
        if(edo.getDetFreeTime() != null)
        {
            edoAuditLog.setSeqNo((long) edoSeg);
            edoAuditLog.setFieldName("Det Free Time");
            edoAuditLog.setNewValue(edo.getDetFreeTime().toString());
            insertEdoAuditLogDetFreeTime(edoAuditLog);
            edoSeg +=1;
        }
        if(edo.getEmptyContainerDepot() != null)
        {   
            edoAuditLog.setSeqNo((long) edoSeg);
            edoAuditLog.setFieldName("Empty Container Depot");
            edoAuditLog.setNewValue(edo.getEmptyContainerDepot().toString());
            insertEdoAuditLogDetFreeTime(edoAuditLog);
            edoSeg +=1;
        }
        if(edo.getConsignee() != null)
        {
            edoAuditLog.setSeqNo((long) edoSeg);
            edoAuditLog.setFieldName("Consignee");
            edoAuditLog.setNewValue(edo.getConsignee().toString());
            insertEdoAuditLogDetFreeTime(edoAuditLog);
            edoSeg +=1;
        }
        if(edo.getVessel() != null)
        {
            edoAuditLog.setSeqNo((long) edoSeg);
            edoAuditLog.setFieldName("Vessel");
            edoAuditLog.setNewValue(edo.getVessel().toString());
            insertEdoAuditLogDetFreeTime(edoAuditLog);
            edoSeg +=1;
        }
        if(edo.getVoyNo() != null) 
        {
            edoAuditLog.setSeqNo((long) edoSeg);
            edoAuditLog.setFieldName("Voy No");
            edoAuditLog.setNewValue(edo.getVoyNo().toString());
            insertEdoAuditLogDetFreeTime(edoAuditLog);
            edoSeg +=1;
        }
        if(edo.getWeight() != null) 
        {
            edoAuditLog.setSeqNo((long) edoSeg);
            edoAuditLog.setFieldName("Weight ");
            edoAuditLog.setNewValue(edo.getWeight().toString());
            insertEdoAuditLogDetFreeTime(edoAuditLog);
            edoSeg +=1;
        }
        if(edo.getSealNo() != null) 
        {
            edoAuditLog.setSeqNo((long) edoSeg);
            edoAuditLog.setFieldName("Seal No");
            edoAuditLog.setNewValue(edo.getSealNo().toString());
            insertEdoAuditLogDetFreeTime(edoAuditLog);
            edoSeg +=1;
        }
        return true;

    }
    @Override
    @Transactional
    public boolean updateAuditLog(EquipmentDo edo) 
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date timeNow = new Date();
        int segNo = 1;
        EquipmentEdoAuditLog edoAuditLog = new EquipmentEdoAuditLog();
        edoAuditLog.setCarrierId(edo.getCarrierId());
        edoAuditLog.setCarrierCode(edo.getCarrierCode());
        edoAuditLog.setCreateBy(edo.getCarrierCode());
        edoAuditLog.setEdoId(edo.getId());
        edoAuditLog.setCreateTime(timeNow);
        String maxSegNo = selectEdoAuditLogByEdoId(edo.getId());
        if(edo.getExpiredDem() != null)
        {
            Date setTimeUpdatExpicedDem = edo.getExpiredDem();
			setTimeUpdatExpicedDem.setHours(23);
			setTimeUpdatExpicedDem.setMinutes(59);
			setTimeUpdatExpicedDem.setSeconds(59);
			edo.setExpiredDem(setTimeUpdatExpicedDem);
            edoAuditLog.setFieldName("Expired Dem");
            EquipmentEdoAuditLog edoAuditLogCheck = selectEdoAuditLogByEdo(edoAuditLog);
            // check not default value
            if(edoAuditLogCheck != null && !formatter.format(setTimeUpdatExpicedDem).toString().equals(edoAuditLogCheck.getNewValue()))
            {
                edoAuditLog.setOldValue(edoAuditLogCheck.getNewValue());
                edoAuditLog.setSeqNo(Long.parseLong(maxSegNo) + segNo);
                edoAuditLog.setNewValue(formatter.format(edo.getExpiredDem()).toString());
                insertEdoAuditLogExpiredDem(edoAuditLog);
                segNo += 1;
            }
        }
        if(edo.getDetFreeTime() != null)
        {
            edoAuditLog.setFieldName("Det Free Time");
            EquipmentEdoAuditLog edoAuditLogCheck = selectEdoAuditLogByEdo(edoAuditLog);
            // check not default value
            if(edoAuditLogCheck != null && !edoAuditLogCheck.getNewValue().equals(edo.getDetFreeTime().toString()))
            {
                edoAuditLog.setOldValue(edoAuditLogCheck.getNewValue());
                edoAuditLog.setSeqNo(Long.parseLong(maxSegNo) + segNo);
                edoAuditLog.setNewValue(edo.getDetFreeTime().toString());
                insertEdoAuditLogDetFreeTime(edoAuditLog);
                segNo += 1;
            }
        }
        if(edo.getEmptyContainerDepot() != null && !edo.getEmptyContainerDepot().equals(""))
        {
            edoAuditLog.setFieldName("Empty Container Depot");
            EquipmentEdoAuditLog edoAuditLogCheck = selectEdoAuditLogByEdo(edoAuditLog);
            if(edoAuditLogCheck != null && !edo.getEmptyContainerDepot().toString().equals(edoAuditLogCheck.getNewValue())) 
            {
                edoAuditLog.setOldValue(edoAuditLogCheck.getNewValue());
                edoAuditLog.setSeqNo(Long.parseLong(maxSegNo) + segNo);
                edoAuditLog.setNewValue(edo.getEmptyContainerDepot().toString());
                insertEdoAuditLogDetFreeTime(edoAuditLog);
            }
        }
        return true;
    }


}
