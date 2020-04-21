package vn.com.irtech.api.controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.api.R;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api")
@Api(tags="Login API")
public class ApiPublicTestController {
	
	@GetMapping("/testAPI")
	public R testAPI()
	{
		
		return R.ok("testAPI");
		
	}
	
	public R ()
}
