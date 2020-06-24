package vn.com.irtech.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.api.dao.ProcessOrderDao;
import vn.com.irtech.api.entity.ProcessOrderEntity;
@RestController
@RequestMapping("/api")
public class ApiProcessOrderController {
	@Autowired
	private ProcessOrderDao processOrderDao;
	
	@GetMapping("/processOrder/getYearBeforeAfter/{vesselCode}/{voyageNo}")
	public ProcessOrderEntity getYearBeforeAfter(@PathVariable String vesselCode, @PathVariable String voyageNo) {
		ProcessOrderEntity po = processOrderDao.getYearBeforeAfter(vesselCode, voyageNo);
		return po;
	}
}
