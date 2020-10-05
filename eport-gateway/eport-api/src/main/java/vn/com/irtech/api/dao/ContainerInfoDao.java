package vn.com.irtech.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import vn.com.irtech.api.dto.ContainerHistoryDto;
import vn.com.irtech.api.entity.ContainerInfoEntity;

@Mapper
public interface ContainerInfoDao extends BaseMapper<ContainerInfoEntity> {

	List<ContainerInfoEntity> selectContainerInfoListInOut(ContainerInfoEntity containerInfo);

	int countContainerInfoInOut(ContainerInfoEntity containerInfo);

	List<ContainerInfoEntity> selectContainerInfoListFull(ContainerInfoEntity containerInfo);

	int countContainerInfoFull(ContainerInfoEntity containerInfo);

	List<ContainerInfoEntity> selectContainerInfoListEmpty(ContainerInfoEntity containerInfo);

	int countContainerInfoEmpty(ContainerInfoEntity containerInfo);

	/**
	 * Get container history for container.
	 * 
	 * @param history
	 * @return
	 */
	List<ContainerHistoryDto> getContainerHistory(ContainerHistoryDto history);

}
