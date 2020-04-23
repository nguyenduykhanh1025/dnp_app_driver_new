package vn.com.irtech.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		
		ContainerInfoEntity query = new ContainerInfoEntity();
		List<ContainerInfoEntity> data = containerInfo.selectContainerInfoList(query);
		
		return R.ok().put("data", data);
	}
	
	@PostMapping("/container/list")
	public R listContainer(@RequestBody ContainerInfoEntity query) {
		
		System.out.println("PARA QUERY"+new Gson().toJson(query));
		List<ContainerInfoEntity> data = containerInfo.selectContainerInfoList(query);
		int total = containerInfo.countContainerInfoList(query);
		return R.ok().put("data", data).put("total", total);
	}
	
	@PostMapping("/container/export")
	public R listForExport(@RequestBody ContainerInfoEntity query) {
		Map<String, Object> pageInfo = new HashMap<>();
		pageInfo = query.getParams();
        int total = containerInfo.countContainerInfoList(query);
        pageInfo.put("pageNum",0);
        pageInfo.put("pageSize", total);
        query.setParams(pageInfo);
		List<ContainerInfoEntity> data = containerInfo.selectContainerInfoList(query);
		return R.ok().put("data", data);
	}

}
