package vn.com.irtech.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.api.common.utils.R;
import vn.com.irtech.api.dao.UnitBillDao;
import vn.com.irtech.api.entity.UnitBillEntity;

@RestController
@RequestMapping("/api")
public class ApiUnitBillController {
	@Autowired
	public UnitBillDao unitBillDao;
	
	@GetMapping("/unitBill/list/{invNo}")
	public R getUnitBillList(@PathVariable String invNo) {
		List<UnitBillEntity> list = unitBillDao.selectUnitBillByInvNo(invNo);
		return R.ok().put("data", list);
	}

}
