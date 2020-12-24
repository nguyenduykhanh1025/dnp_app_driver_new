package vn.com.irtech.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.api.dao.EirGateDao;
import vn.com.irtech.api.entity.EirGateEntity;

@RestController
@RequestMapping("/api")
public class ApiEirGateController {
	
	@Autowired
	private EirGateDao eirGateDao;
	
	/**
	 * Get eir gate report
	 * 
	 * @param eirGate
	 * @return List<EirGateEntity>
	 */
	@PostMapping("/eir-gate/report")
	public List<EirGateEntity> getEirGateReport(@RequestBody EirGateEntity eirGate) {
		return eirGateDao.getEirGateReport(eirGate);
	}

	/**
	 * Get eir gate report total
	 * 
	 * @param eirGate
	 * @return Long
	 */
	@PostMapping("/eir-gate/report/total")
	public Long getEirGateReportTotal(@RequestBody EirGateEntity eirGate) {
		return eirGateDao.totalEirGateReport(eirGate);
	}
}
