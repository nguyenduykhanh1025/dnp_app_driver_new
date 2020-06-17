package vn.com.irtech.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.api.common.utils.R;
import vn.com.irtech.api.dao.SelectFieldDao;

@RestController
@RequestMapping("/api")
public class ApiSelectFieldController {

	@Autowired
	public SelectFieldDao selectFieldDao;
	
	@GetMapping("/selectField/selectVesselCode")
	public R selectVesselCode() {
		List<String> vesselCodeList = selectFieldDao.selectVesselCodeField();
		return R.ok().put("data", vesselCodeList);
	}
}
