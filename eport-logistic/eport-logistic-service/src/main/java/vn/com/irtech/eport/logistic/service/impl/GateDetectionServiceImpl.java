package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.GateDetectionMapper;
import vn.com.irtech.eport.logistic.domain.GateDetection;
import vn.com.irtech.eport.logistic.service.IGateDetectionService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Gate DetectionService Business Processing
 * 
 * @author Irtech
 * @date 2020-10-10
 */
@Service
public class GateDetectionServiceImpl implements IGateDetectionService 
{
    @Autowired
    private GateDetectionMapper gateDetectionMapper;

    /**
     * Get Gate Detection
     * 
     * @param id Gate DetectionID
     * @return Gate Detection
     */
    @Override
    public GateDetection selectGateDetectionById(Long id)
    {
        return gateDetectionMapper.selectGateDetectionById(id);
    }

    /**
     * Get Gate Detection List
     * 
     * @param gateDetection Gate Detection
     * @return Gate Detection
     */
    @Override
    public List<GateDetection> selectGateDetectionList(GateDetection gateDetection)
    {
        return gateDetectionMapper.selectGateDetectionList(gateDetection);
    }

    /**
     * Add Gate Detection
     * 
     * @param gateDetection Gate Detection
     * @return result
     */
    @Override
    public int insertGateDetection(GateDetection gateDetection)
    {
        gateDetection.setCreateTime(DateUtils.getNowDate());
        return gateDetectionMapper.insertGateDetection(gateDetection);
    }

    /**
     * Update Gate Detection
     * 
     * @param gateDetection Gate Detection
     * @return result
     */
    @Override
    public int updateGateDetection(GateDetection gateDetection)
    {
        gateDetection.setUpdateTime(DateUtils.getNowDate());
        return gateDetectionMapper.updateGateDetection(gateDetection);
    }

    /**
     * Delete Gate Detection By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteGateDetectionByIds(String ids)
    {
        return gateDetectionMapper.deleteGateDetectionByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Gate Detection
     * 
     * @param id Gate DetectionID
     * @return result
     */
    @Override
    public int deleteGateDetectionById(Long id)
    {
        return gateDetectionMapper.deleteGateDetectionById(id);
    }
}
