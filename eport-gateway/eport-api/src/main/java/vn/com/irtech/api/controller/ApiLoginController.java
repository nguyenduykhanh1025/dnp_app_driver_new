package vn.com.irtech.api.controller;


import vn.com.irtech.api.annotation.Login;
import vn.com.irtech.api.common.utils.R;
import vn.com.irtech.api.common.validator.ValidatorUtils;
import vn.com.irtech.api.form.LoginForm;
import vn.com.irtech.api.service.TokenService;
import vn.com.irtech.api.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(tags="Login API")
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
