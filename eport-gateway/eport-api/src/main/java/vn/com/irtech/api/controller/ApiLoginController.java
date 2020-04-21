package vn.com.irtech.api.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import vn.com.irtech.api.annotation.Login;
import vn.com.irtech.api.common.utils.R;
import vn.com.irtech.api.common.validator.ValidatorUtils;
import vn.com.irtech.api.form.LoginForm;
import vn.com.irtech.api.service.TokenService;
import vn.com.irtech.api.service.UserService;

@RestController
@RequestMapping("/api")
public class ApiLoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;


    @PostMapping("login")
    @ApiOperation("Login")
    public R login(@RequestBody LoginForm form){
        ValidatorUtils.validateEntity(form);
        Map<String, Object> map = userService.login(form);
        return R.ok(map);
    }

    @Login
    @PostMapping("logout")
    @ApiOperation("Logout")
    public R logout(@ApiIgnore @RequestAttribute("userId") long userId){
        tokenService.expireToken(userId);
        return R.ok();
    }

}
