package vn.com.irtech.eport.carrier.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.carrier.mapper.ContainerInfoMapper;
import vn.com.irtech.eport.carrier.domain.ContainerInfo;
import vn.com.irtech.eport.carrier.service.IContainerInfoService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Container InfomationService Business Processing
 * 
 * @author Admin
 * @date 2020-04-16
 */
@Service
public class ContainerInfoServiceImpl implements IContainerInfoService 
{
    @Autowired
    private ContainerInfoMapper containerInfoMapper;

    /**
     * Get Container Infomation
     * 
     * @param cntrId Container InfomationID
     * @return Container Infomation
     */
    @Override
    public ContainerInfo selectContainerInfoById(Long cntrId)
    {
        return containerInfoMapper.selectContainerInfoById(cntrId);
    }

    /**
     * Get Container Infomation List
     * 
     * @param containerInfo Container Infomation
     * @return Container Infomation
     */
    @Override
    public List<ContainerInfo> selectContainerInfoList(ContainerInfo containerInfo)
    {
        return containerInfoMapper.selectContainerInfoList(containerInfo);
    }

    /**
     * Add Container Infomation
     * 
     * @param containerInfo Container Infomation
     * @return result
     */
    @Override
    public int insertContainerInfo(ContainerInfo containerInfo)
    {
        return containerInfoMapper.insertContainerInfo(containerInfo);
    }

    /**
     * Update Container Infomation
     * 
     * @param containerInfo Container Infomation
     * @return result
     */
    @Override
    public int updateContainerInfo(ContainerInfo containerInfo)
    {
        return containerInfoMapper.updateContainerInfo(containerInfo);
    }

    /**
     * Delete Container Infomation By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteContainerInfoByIds(String ids)
    {
        return containerInfoMapper.deleteContainerInfoByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Container Infomation
     * 
     * @param cntrId Container InfomationID
     * @return result
     */
    @Override
    public int deleteContainerInfoById(Long cntrId)
    {
        return containerInfoMapper.deleteContainerInfoById(cntrId);
    }
}
