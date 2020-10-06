package vn.com.irtech.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import vn.com.irtech.api.entity.ContainerHistoryEntity;

@Mapper
public interface ContainerHistoryDao extends BaseMapper<ContainerHistoryEntity> {
	
	/**
	 * Get container history by container no
	 * 
	 * @param containerHistory
	 * @return List<containerHistoryEntity>
	 */
	public List<ContainerHistoryEntity> getContainerHistoryByContainerNo(ContainerHistoryEntity containerHistory);
}

