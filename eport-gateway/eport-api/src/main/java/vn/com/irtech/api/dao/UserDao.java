package vn.com.irtech.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vn.com.irtech.api.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

}
