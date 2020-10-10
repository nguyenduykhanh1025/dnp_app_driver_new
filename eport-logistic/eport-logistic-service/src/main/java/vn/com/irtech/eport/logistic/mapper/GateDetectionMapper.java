package vn.com.irtech.eport.logistic.mapper;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.GateDetection;

/**
 * Gate DetectionMapper Interface
 * 
 * @author Irtech
 * @date 2020-10-10
 */
public interface GateDetectionMapper 
{
    /**
     * Get Gate Detection
     * 
     * @param id Gate DetectionID
     * @return Gate Detection
     */
    public GateDetection selectGateDetectionById(Long id);

    /**
     * Get Gate Detection List
     * 
     * @param gateDetection Gate Detection
     * @return Gate Detection List
     */
    public List<GateDetection> selectGateDetectionList(GateDetection gateDetection);

    /**
     * Add Gate Detection
     * 
     * @param gateDetection Gate Detection
     * @return Result
     */
    public int insertGateDetection(GateDetection gateDetection);

    /**
     * Update Gate Detection
     * 
     * @param gateDetection Gate Detection
     * @return Result
     */
    public int updateGateDetection(GateDetection gateDetection);

    /**
     * Delete Gate Detection
     * 
     * @param id Gate DetectionID
     * @return result
     */
    public int deleteGateDetectionById(Long id);

    /**
     * Batch Delete Gate Detection
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteGateDetectionByIds(String[] ids);
}
