package vn.com.irtech.eport.carrier.mapper;

import java.util.List;
import vn.com.irtech.eport.carrier.domain.ContainerInfo;

/**
 * Container InfomationMapper Interface
 * 
 * @author Admin
 * @date 2020-04-16
 */
public interface ContainerInfoMapper 
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
     * @return Result
     */
    public int insertContainerInfo(ContainerInfo containerInfo);

    /**
     * Update Container Infomation
     * 
     * @param containerInfo Container Infomation
     * @return Result
     */
    public int updateContainerInfo(ContainerInfo containerInfo);

    /**
     * Delete Container Infomation
     * 
     * @param cntrId Container InfomationID
     * @return result
     */
    public int deleteContainerInfoById(Long cntrId);

    /**
     * Batch Delete Container Infomation
     * 
     * @param cntrIds IDs
     * @return result
     */
    public int deleteContainerInfoByIds(String[] cntrIds);
}
