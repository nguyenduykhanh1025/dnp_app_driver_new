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
	
	@PostMapping("/container/list")
	public R listContainer(@RequestBody ContainerInfoEntity query) {
		if(query.getFe() == null)
		{
			List<ContainerInfoEntity> data = containerInfo.selectContainerInfoListInOut(query);
			
			int total = containerInfo.countContainerInfoInOut(query);
			return R.ok().put("data", data).put("total", total);
		}
		if(query.getFe().equals("F"))
		{
			List<ContainerInfoEntity> data = containerInfo.selectContainerInfoListFull(query);
			int total = containerInfo.countContainerInfoFull(query);
			return R.ok().put("data", data).put("total", total);
		}
		if (query.getFe().equals("E"))
		{
			List<ContainerInfoEntity> data = containerInfo.selectContainerInfoListEmpty(query);
			int total = containerInfo.countContainerInfoEmpty(query);
			return R.ok().put("data", data).put("total", total);
		}
		return null;
	
	}
	
	@PostMapping("/container/export")
	public R listForExport(@RequestBody ContainerInfoEntity query) {
		Map<String, Object> pageInfo = new HashMap<>();
		pageInfo = query.getParams();
        
		if(query.getFe() == null)
		{
			int total = containerInfo.countContainerInfoInOut(query);
			pageInfo.put("pageNum",0);
			pageInfo.put("pageSize", total);
			query.setParams(pageInfo);
			List<ContainerInfoEntity> data = containerInfo.selectContainerInfoListInOut(query);
			return R.ok().put("data", data).put("total", total);
		}
		if(query.getFe().equals("F"))
		{
			int total = containerInfo.countContainerInfoFull(query);
			pageInfo.put("pageNum",0);
			pageInfo.put("pageSize", total);
			query.setParams(pageInfo);
			List<ContainerInfoEntity> data = containerInfo.selectContainerInfoListFull(query);
			return R.ok().put("data", data).put("total", total);
		}
		if (query.getFe().equals("E"))
		{
			int total = containerInfo.countContainerInfoEmpty(query);
			pageInfo.put("pageNum",0);
			pageInfo.put("pageSize", total);
			query.setParams(pageInfo);
			List<ContainerInfoEntity> data = containerInfo.selectContainerInfoListEmpty(query);
			return R.ok().put("data", data).put("total", total);
		}
		return null;
	}

}
