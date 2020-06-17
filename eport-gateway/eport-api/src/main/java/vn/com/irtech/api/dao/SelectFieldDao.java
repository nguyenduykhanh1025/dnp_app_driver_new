package vn.com.irtech.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SelectFieldDao{

	public List<String> selectVesselCodeField();
}
