package vn.com.irtech.eport.carrier.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.irtech.eport.carrier.mapper.EdoAuditLogMapper;
import vn.com.irtech.eport.carrier.domain.Edo;
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

    @Override
    @Transactional
    public boolean addAuditLogFirst(Edo edo)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date timeNow = new Date();
        int edoSeg = 1;
        EdoAuditLog edoAuditLog = new EdoAuditLog();
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
        if(edo.getVesselNo() != null)
        {
            edoAuditLog.setSeqNo((long) edoSeg);
            edoAuditLog.setFieldName("Vessel No");
            edoAuditLog.setNewValue(edo.getVesselNo().toString());
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
        return true;

    }
    @Override
    @Transactional
    public boolean updateAuditLog(Edo edo) 
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date timeNow = new Date();
        int segNo = 1;
        EdoAuditLog edoAuditLog = new EdoAuditLog();
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
            EdoAuditLog edoAuditLogCheck = selectEdoAuditLogByEdo(edoAuditLog);
            // check not default value
            if(edoAuditLogCheck != null)
            {
                edoAuditLog.setOldValue(edoAuditLogCheck.getNewValue());
            }
            edoAuditLog.setSeqNo(Long.parseLong(maxSegNo) + segNo);
            edoAuditLog.setNewValue(formatter.format(edo.getExpiredDem()).toString());
            insertEdoAuditLogExpiredDem(edoAuditLog);
            segNo += 1;
        }
        if(edo.getDetFreeTime() != null)
        {
            edoAuditLog.setFieldName("Det Free Time");
            EdoAuditLog edoAuditLogCheck = selectEdoAuditLogByEdo(edoAuditLog);
            // check not default value
            if(edoAuditLogCheck != null )
            {
                edoAuditLog.setOldValue(edoAuditLogCheck.getNewValue());
            }
            edoAuditLog.setSeqNo(Long.parseLong(maxSegNo) + segNo);
            edoAuditLog.setNewValue(edo.getDetFreeTime().toString());
            insertEdoAuditLogDetFreeTime(edoAuditLog);
            segNo += 1;
        }
        if(edo.getEmptyContainerDepot() != null && !edo.getEmptyContainerDepot().equals(""))
        {
            edoAuditLog.setFieldName("Empty Container Depot");
            EdoAuditLog edoAuditLogCheck = selectEdoAuditLogByEdo(edoAuditLog);
            if(edoAuditLogCheck != null) 
            {
                edoAuditLog.setOldValue(edoAuditLogCheck.getNewValue());
            }
            edoAuditLog.setSeqNo(Long.parseLong(maxSegNo) + segNo);
            edoAuditLog.setNewValue(edo.getEmptyContainerDepot().toString());
            insertEdoAuditLogDetFreeTime(edoAuditLog);
        }
        return true;
    }


}