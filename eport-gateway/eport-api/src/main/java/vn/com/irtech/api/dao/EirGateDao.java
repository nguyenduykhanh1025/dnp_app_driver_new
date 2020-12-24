package vn.com.irtech.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import vn.com.irtech.api.entity.EirGateEntity;

@Mapper
public interface EirGateDao extends BaseMapper<EirGateEntity> {
	
	/**
	 * Get Eir Gate Report
	 * 
	 * @param eirGate
	 * @return List<EirGateEntity>
	 */
	public List<EirGateEntity> getEirGateReport(EirGateEntity eirGate);

	public Long totalEirGateReport(EirGateEntity eirGate);
}

