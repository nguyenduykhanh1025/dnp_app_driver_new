package vn.com.irtech.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vn.com.irtech.api.entity.TokenEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenDao extends BaseMapper<TokenEntity> {
	
}
