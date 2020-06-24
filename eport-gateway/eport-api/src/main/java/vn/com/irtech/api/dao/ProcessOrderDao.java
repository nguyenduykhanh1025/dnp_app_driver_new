package vn.com.irtech.api.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import vn.com.irtech.api.entity.ProcessOrderEntity;

@Mapper
public interface ProcessOrderDao extends BaseMapper<ProcessOrderEntity>{

	public ProcessOrderEntity getYearBeforeAfter(@Param("vesselCode") String vesselCode, @Param("voyageNo") String voyageNo);
}
