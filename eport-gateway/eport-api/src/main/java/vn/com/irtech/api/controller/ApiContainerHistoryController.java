package vn.com.irtech.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.irtech.api.dao.ContainerHistoryDao;
import vn.com.irtech.api.entity.ContainerHistoryEntity;

@RestController
@RequestMapping("/api")
public class ApiContainerHistoryController {
	
	@Autowired
	private ContainerHistoryDao containerHistoryDao;
	
	@PostMapping("/container/history")
	public List<ContainerHistoryEntity> listContainerHistory(@RequestBody ContainerHistoryEntity containerHistory) {
		return containerHistoryDao.getContainerHistoryByContainerNo(containerHistory);
	}
}
