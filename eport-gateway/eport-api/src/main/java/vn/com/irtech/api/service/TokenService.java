package vn.com.irtech.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vn.com.irtech.api.entity.TokenEntity;

public interface TokenService extends IService<TokenEntity> {

	TokenEntity queryByToken(String token);

	TokenEntity createToken(long userId);

	void expireToken(long userId);

}
