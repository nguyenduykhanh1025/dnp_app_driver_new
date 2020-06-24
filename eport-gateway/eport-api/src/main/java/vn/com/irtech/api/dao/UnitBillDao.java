package vn.com.irtech.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import vn.com.irtech.api.entity.UnitBillEntity;

@Mapper
public interface UnitBillDao extends BaseMapper<UnitBillDao>{

	public List<UnitBillEntity> selectUnitBillByInvNo(String invNo);
}
