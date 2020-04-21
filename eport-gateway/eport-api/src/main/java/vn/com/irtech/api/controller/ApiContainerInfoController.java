package vn.com.irtech.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import vn.com.irtech.api.common.utils.R;
import vn.com.irtech.api.dao.ContainerInfoDao;
import vn.com.irtech.api.entity.ContainerInfoEntity;

@RestController
@RequestMapping("/api")
public class ApiContainerInfoController {
	@Autowired
	private ContainerInfoDao containerInfo;

	@RequestMapping("/container/list1")
	public R listContainer1() {
		// Filter theo OPR CODE, Paging, Search theo fromDate, toDay, OPR_CODE
		// TODO: Paging
		// TODO: Filter by PNTR_CODE
		// TODO: Search by fromDate, toDate
		// TODO: List for export
		List<ContainerInfoEntity> data = containerInfo.selectContainerInfoList(null);
		// Get total count for this query
		int total = 200;
		return R.ok().put("data", data).put("total", total);
	}
	
	@PostMapping("/container/list")
	public R listContainer(@RequestBody ContainerInfoEntity query) {
		// Filter theo OPR CODE, Paging, Search theo fromDate, toDay, OPR_CODE
		// TODO: Paging
		// TODO: Filter by PNTR_CODE
		// TODO: Search by fromDate, toDate
		// TODO: List for export
		System.out.println(new Gson().toJson(query));
		List<ContainerInfoEntity> data = containerInfo.selectContainerInfoList(query);
		// TODO: Get total count for this query
		int total = 200;
		return R.ok().put("data", data).put("total", total);
	}
	
	@PostMapping("/container/export")
	public R listForExport(@RequestBody ContainerInfoEntity query) {
		// TODO: Filter by PNTR_CODE
		// TODO: Search by fromDate, toDate
		// TODO List all record by search condition for export
		List<ContainerInfoEntity> data = containerInfo.selectContainerInfoList(query);
		return R.ok().put("data", data);
	}

}
