package vn.com.irtech.eport.web.controller.history;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.logistic.dto.ContainerHistoryDto;
import vn.com.irtech.eport.logistic.service.ICatosApiService;

@Controller
@RequestMapping("/history/container")
public class ContainerHistoryController extends BaseController {

    @Autowired
    private ICatosApiService catosApiService;;
    
    @PostMapping("/catos")
    @ResponseBody
    public AjaxResult getContainerHistoryCatos(@RequestBody ContainerHistoryDto containerHistoryDto) {
    	List<ContainerHistoryDto> containerHistoryDtos = catosApiService.getContainerHistory(containerHistoryDto);
    	AjaxResult ajaxResult = AjaxResult.success();
    	ajaxResult.put("containerHistories", getDataTable(containerHistoryDtos));
    	return ajaxResult;
    }
}