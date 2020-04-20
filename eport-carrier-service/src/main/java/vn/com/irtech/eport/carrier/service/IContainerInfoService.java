package vn.com.irtech.eport.carrier.service;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.ContainerInfo;

/**
 * Container InfomationService Interface
 * 
 * @author Admin
 * @date 2020-04-16
 */
public interface IContainerInfoService 
{
    /**
     * Get Container Infomation
     * 
     * @param cntrId Container InfomationID
     * @return Container Infomation
     */
    public ContainerInfo selectContainerInfoById(Long cntrId);

    /**
     * Get Container Infomation List
     * 
     * @param containerInfo Container Infomation
     * @return Container Infomation List
     */
    public List<ContainerInfo> selectContainerInfoList(ContainerInfo containerInfo);

    /**
     * Add Container Infomation
     * 
     * @param containerInfo Container Infomation
     * @return result
     */
    public int insertContainerInfo(ContainerInfo containerInfo);

    /**
     * Update Container Infomation
     * 
     * @param containerInfo Container Infomation
     * @return result
     */
    public int updateContainerInfo(ContainerInfo containerInfo);

    /**
     * Batch Delete Container Infomation
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteContainerInfoByIds(String ids);

    /**
     * Delete Container Infomation
     * 
     * @param cntrId Container InfomationID
     * @return result
     */
    public int deleteContainerInfoById(Long cntrId);

    
}
