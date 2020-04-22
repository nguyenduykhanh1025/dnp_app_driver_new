package vn.com.irtech.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import vn.com.irtech.api.entity.ContainerInfoEntity;

@Mapper
public interface ContainerInfoDao extends BaseMapper<ContainerInfoEntity> {
	
	List<ContainerInfoEntity> selectContainerInfoList(ContainerInfoEntity containerInfo);

	int countContainerInfoList(ContainerInfoEntity containerInfo);

}

