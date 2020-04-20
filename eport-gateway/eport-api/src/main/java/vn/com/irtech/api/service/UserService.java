package vn.com.irtech.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vn.com.irtech.api.entity.UserEntity;
import vn.com.irtech.api.form.LoginForm;

import java.util.Map;

public interface UserService extends IService<UserEntity> {

	UserEntity queryByMobile(String mobile);

	Map<String, Object> login(LoginForm form);
}
