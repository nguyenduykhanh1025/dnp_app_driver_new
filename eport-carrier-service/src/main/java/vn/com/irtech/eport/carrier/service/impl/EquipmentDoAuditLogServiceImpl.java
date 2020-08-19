package vn.com.irtech.eport.carrier.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.irtech.eport.carrier.domain.EquipmentDoAuditLog;
import vn.com.irtech.eport.carrier.mapper.EquipmentDoAuditLogMapper;
import vn.com.irtech.eport.carrier.service.IEquipmentDoAuditLogService;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.equipment.domain.EquipmentDo;

/**
 * Do Audit Trail LogService Business Processing
 * 
 * @author minhtc
 * @date 2020-07-31
 */
@Service
public class EquipmentDoAuditLogServiceImpl implements IEquipmentDoAuditLogService 
{
    @Autowired
    private EquipmentDoAuditLogMapper equipmentDoAuditLogMapper;

    /**
     * Get Do Audit Trail Log
     * 
     * @param id Do Audit Trail LogID
     * @return Do Audit Trail Log
     */
    @Override
    public EquipmentDoAuditLog selectEquipmentDoAuditLogById(Long id)
    {
        return equipmentDoAuditLogMapper.selectEquipmentDoAuditLogById(id);
    }

    /**
     * Get Do Audit Trail Log List
     * 
     * @param equipmentDoAuditLog Do Audit Trail Log
     * @return Do Audit Trail Log
     */
    @Override
    public List<EquipmentDoAuditLog> selectEquipmentDoAuditLogList(EquipmentDoAuditLog equipmentDoAuditLog)
    {
        return equipmentDoAuditLogMapper.selectEquipmentDoAuditLogList(equipmentDoAuditLog);
    }

    /**
     * Add Do Audit Trail Log
     * 
     * @param equipmentDoAuditLog Do Audit Trail Log
     * @return result
     */
    @Override
    public int insertEquipmentDoAuditLog(EquipmentDoAuditLog equipmentDoAuditLog)
    {
        equipmentDoAuditLog.setCreateTime(DateUtils.getNowDate());
        return equipmentDoAuditLogMapper.insertEquipmentDoAuditLog(equipmentDoAuditLog);
    }

    /**
     * Update Do Audit Trail Log
     * 
     * @param equipmentDoAuditLog Do Audit Trail Log
     * @return result
     */
    @Override
    public int updateEquipmentDoAuditLog(EquipmentDoAuditLog equipmentDoAuditLog)
    {
        equipmentDoAuditLog.setUpdateTime(DateUtils.getNowDate());
        return equipmentDoAuditLogMapper.updateEquipmentDoAuditLog(equipmentDoAuditLog);
    }

    /**
     * Delete Do Audit Trail Log By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteEquipmentDoAuditLogByIds(String ids)
    {
        return equipmentDoAuditLogMapper.deleteEquipmentDoAuditLogByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Do Audit Trail Log
     * 
     * @param id Do Audit Trail LogID
     * @return result
     */
    @Override
    public int deleteEquipmentDoAuditLogById(Long id)
    {
        return equipmentDoAuditLogMapper.deleteEquipmentDoAuditLogById(id);
    }

    @Override
    public long getSeqNo(Long id)
    {
        return equipmentDoAuditLogMapper.getSeqNo(id);
    }

    @Override
    public EquipmentDoAuditLog selectDoAuditLogByDo(EquipmentDoAuditLog doAuditLog)
    {
        return equipmentDoAuditLogMapper.selectDoAuditLogByDo(doAuditLog);
    }

    @Override
    public int insertDoAuditLogExpiredDem(EquipmentDoAuditLog doAuditLog)
    {
        return equipmentDoAuditLogMapper.insertDoAuditLogExpiredDem(doAuditLog);
    }

    @Override
    public int insertDoAuditLogDetFreeTime(EquipmentDoAuditLog doAuditLog)
    {
        return equipmentDoAuditLogMapper.insertDoAuditLogDetFreeTime(doAuditLog);
    }

    @Override
    public String selectDoAuditLogByDoId(Long doId)
    {
        return equipmentDoAuditLogMapper.selectDoAuditLogByDoId(doId);
    }

    @Override
    @Transactional
    public boolean addAuditLogFirst(EquipmentDo doItem)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date timeNow = new Date();
        int doSeg = 1;
        EquipmentDoAuditLog doAuditLog = new EquipmentDoAuditLog();
        doAuditLog.setCarrierId(doItem.getCarrierId());
        doAuditLog.setCarrierCode(doItem.getCarrierCode());
        doAuditLog.setCreateBy(doItem.getCarrierCode());
        doAuditLog.setDoId(doItem.getId());
        if(doItem.getExpiredDem() != null)
        {
            doAuditLog.setSeqNo((long) doSeg);
            doAuditLog.setCreateTime(timeNow);
            doAuditLog.setFieldName("Expired Dem");
            doAuditLog.setNewValue(formatter.format(doItem.getExpiredDem()).toString());
            insertDoAuditLogExpiredDem(doAuditLog);
            doSeg +=1;
        }
        if(doItem.getDetFreeTime() != null)
        {
            doAuditLog.setSeqNo((long) doSeg);
            doAuditLog.setFieldName("Det Free Time");
            doAuditLog.setNewValue(doItem.getDetFreeTime().toString());
            insertDoAuditLogDetFreeTime(doAuditLog);
            doSeg +=1;
        }
        if(doItem.getEmptyContainerDepot() != null)
        {   
            doAuditLog.setSeqNo((long) doSeg);
            doAuditLog.setFieldName("Empty Container Depot");
            doAuditLog.setNewValue(doItem.getEmptyContainerDepot().toString());
            insertDoAuditLogDetFreeTime(doAuditLog);
            doSeg +=1;
        }
        if(doItem.getConsignee() != null)
        {
            doAuditLog.setSeqNo((long) doSeg);
            doAuditLog.setFieldName("Consignee");
            doAuditLog.setNewValue(doItem.getConsignee().toString());
            insertDoAuditLogDetFreeTime(doAuditLog);
            doSeg +=1;
        }
        if(doItem.getVessel() != null)
        {
            doAuditLog.setSeqNo((long) doSeg);
            doAuditLog.setFieldName("Vessel");
            doAuditLog.setNewValue(doItem.getVessel().toString());
            insertDoAuditLogDetFreeTime(doAuditLog);
            doSeg +=1;
        }
        if(doItem.getVoyNo() != null) 
        {
            doAuditLog.setSeqNo((long) doSeg);
            doAuditLog.setFieldName("Voy No");
            doAuditLog.setNewValue(doItem.getVoyNo().toString());
            insertDoAuditLogDetFreeTime(doAuditLog);
            doSeg +=1;
        }
        if(doItem.getWeight() != null) 
        {
            doAuditLog.setSeqNo((long) doSeg);
            doAuditLog.setFieldName("Weight ");
            doAuditLog.setNewValue(doItem.getWeight().toString());
            insertDoAuditLogDetFreeTime(doAuditLog);
            doSeg +=1;
        }
        if(doItem.getSealNo() != null) 
        {
            doAuditLog.setSeqNo((long) doSeg);
            doAuditLog.setFieldName("Seal No");
            doAuditLog.setNewValue(doItem.getSealNo().toString());
            insertDoAuditLogDetFreeTime(doAuditLog);
            doSeg +=1;
        }
        return true;

    }
    @Override
    @Transactional
    public boolean updateAuditLog(EquipmentDo doItem) 
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date timeNow = new Date();
        int segNo = 1;
        EquipmentDoAuditLog doAuditLog = new EquipmentDoAuditLog();
        doAuditLog.setCarrierId(doItem.getCarrierId());
        doAuditLog.setCarrierCode(doItem.getCarrierCode());
        doAuditLog.setCreateBy(doItem.getCreateBy());
        doAuditLog.setDoId(doItem.getId());
        doAuditLog.setCreateTime(timeNow);
        String maxSegNo = selectDoAuditLogByDoId(doItem.getId());
        if(doItem.getExpiredDem() != null)
        {
            Date setTimeUpdatExpicedDem = doItem.getExpiredDem();
			setTimeUpdatExpicedDem.setHours(23);
			setTimeUpdatExpicedDem.setMinutes(59);
			setTimeUpdatExpicedDem.setSeconds(59);
			doItem.setExpiredDem(setTimeUpdatExpicedDem);
            doAuditLog.setFieldName("Expired Dem");
            EquipmentDoAuditLog doAuditLogCheck = selectDoAuditLogByDo(doAuditLog);
            // check not default value
            if(doAuditLogCheck != null && !formatter.format(setTimeUpdatExpicedDem).toString().equals(doAuditLogCheck.getNewValue()))
            {
                doAuditLog.setOldValue(doAuditLogCheck.getNewValue());
                doAuditLog.setSeqNo(Long.parseLong(maxSegNo) + segNo);
                doAuditLog.setNewValue(formatter.format(doItem.getExpiredDem()).toString());
                insertDoAuditLogExpiredDem(doAuditLog);
                segNo += 1;
            }
        }
        if(doItem.getDetFreeTime() != null)
        {
            doAuditLog.setFieldName("Det Free Time");
            EquipmentDoAuditLog doAuditLogCheck = selectDoAuditLogByDo(doAuditLog);
            // check not default value
            if(doAuditLogCheck != null && !doAuditLogCheck.getNewValue().equals(doItem.getDetFreeTime().toString()))
            {
                doAuditLog.setOldValue(doAuditLogCheck.getNewValue());
                doAuditLog.setSeqNo(Long.parseLong(maxSegNo) + segNo);
                doAuditLog.setNewValue(doItem.getDetFreeTime().toString());
                insertDoAuditLogDetFreeTime(doAuditLog);
                segNo += 1;
            }
        }
        if(doItem.getEmptyContainerDepot() != null && !doItem.getEmptyContainerDepot().equals(""))
        {
            doAuditLog.setFieldName("Empty Container Depot");
            EquipmentDoAuditLog doAuditLogCheck = selectDoAuditLogByDo(doAuditLog);
            if(doAuditLogCheck != null && !doItem.getEmptyContainerDepot().toString().equals(doAuditLogCheck.getNewValue())) 
            {
                doAuditLog.setOldValue(doAuditLogCheck.getNewValue());
                doAuditLog.setSeqNo(Long.parseLong(maxSegNo) + segNo);
                doAuditLog.setNewValue(doItem.getEmptyContainerDepot().toString());
                insertDoAuditLogDetFreeTime(doAuditLog);
            }
        }
        return true;
    }

    //

    


}
