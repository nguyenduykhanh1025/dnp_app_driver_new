package vn.com.irtech.eport.logistic.service;

import java.util.List;
import vn.com.irtech.eport.logistic.domain.GateDetection;

/**
 * Gate DetectionService Interface
 * 
 * @author Irtech
 * @date 2020-10-10
 */
public interface IGateDetectionService 
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
     * @return result
     */
    public int insertGateDetection(GateDetection gateDetection);

    /**
     * Update Gate Detection
     * 
     * @param gateDetection Gate Detection
     * @return result
     */
    public int updateGateDetection(GateDetection gateDetection);

    /**
     * Batch Delete Gate Detection
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteGateDetectionByIds(String ids);

    /**
     * Delete Gate Detection
     * 
     * @param id Gate DetectionID
     * @return result
     */
    public int deleteGateDetectionById(Long id);
}
