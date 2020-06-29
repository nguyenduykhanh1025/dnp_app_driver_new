package vn.com.irtech.eport.carrier.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.page.TableDataInfo;

@Controller
@RequestMapping("/edo")
public class CarrierEdoController extends CarrierBaseController {
    private final String PREFIX = "edo";


	@Autowired
	private IEdoService edoService;

    @GetMapping("/index")
	public String EquipmentDo() {
		if (!hasDoPermission()) {
			return "error/404";
		}
		return PREFIX + "/edo";
	}


	@GetMapping("/edo")
	@ResponseBody
	public TableDataInfo edo(Edo edo,String fromDate,String toDate)
	{
		startPage();
		Map<String, Object> searchDay = new HashMap<>();
		searchDay.put("fromDate", fromDate);
		searchDay.put("toDate", toDate);
		edo.setParams(searchDay);
		List<Edo> dataList = edoService.selectEdoList(edo);
		return getDataTable(dataList);
	}

	@GetMapping("/listCarrierCode")
	@ResponseBody
    public List<String> lisCarrierCode()
    {
        return super.getGroupCodes();
    }


}