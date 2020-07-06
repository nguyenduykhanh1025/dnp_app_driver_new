package vn.com.irtech.eport.carrier.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoHistory;
import vn.com.irtech.eport.carrier.service.IEdoHistoryService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.framework.util.ShiroUtils;

@Controller
@RequestMapping("/edo")
public class CarrierEdoController extends CarrierBaseController {

    private final String PREFIX = "edo";
	

	@Autowired
	private IEdoService edoService;

	@Autowired
	private IEdoHistoryService edoHistoryService;

    @GetMapping("/index")
	public String EquipmentDo() {
		if (!hasDoPermission()) {
			return "error/404";
		}
		return PREFIX + "/edo";
	}

    //List
	@GetMapping("/billNo")
	@ResponseBody
	public TableDataInfo billNo(Edo edo, String fromDate, String toDate)
	{
		startPage();
		edo.setCarrierId(ShiroUtils.getGroupId());
		Map<String, Object> searchDate = new HashMap<>();
		searchDate.put("fromDate", fromDate);
		searchDate.put("toDate", toDate);
		edo.setParams(searchDate);
		List<Edo> dataList = edoService.selectEdoListByBillNo(edo);
		return getDataTable(dataList);
	}

	//List
	@GetMapping("/edo")
	@ResponseBody
	public TableDataInfo edo(Edo edo, String fromDate, String toDate)
	{
		startPage();
		edo.setCarrierId(ShiroUtils.getGroupId());
		Map<String, Object> searchDate = new HashMap<>();
		searchDate.put("fromDate", fromDate);
		searchDate.put("toDate", toDate);
		edo.setParams(searchDate);
		List<Edo> dataList = edoService.selectEdoList(edo);
		return getDataTable(dataList);
	}

	@GetMapping("/carrierCode")
	@ResponseBody
    public List<String> carrierCode()
    {
        return super.getGroupCodes();
	}
	
	@GetMapping("/update")
	public String update()
	{
		return PREFIX + "/update";
	}

	@RequestMapping(value = "/file",method = { RequestMethod.POST })
	@ResponseBody
	public Object upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
		String content = "";
		List<Edo> listEdo = new ArrayList<>();
		try {
			  String fileName = file.getOriginalFilename();
		      File fileNew = new File(edoService.getFolderUploadByTime(super.folderUpLoad()), fileName);
		      file.transferTo(fileNew);
		      File myObj = new File(edoService.getFolderUploadByTime(super.folderUpLoad())+File.separator+fileName);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
				content += myReader.nextLine();
		      }
              myReader.close();
			  String[] text = content.split("'");
			  listEdo = edoService.readEdi(text);
			  EdoHistory edoHistory = new EdoHistory();
			  edoHistory.setFileName(fileName);
			  edoHistory.setCreateSource("websiteDaNangPort");
			  Date timeNow = new Date();
			  for(Edo edo : listEdo)
            	{
					edo.setCarrierId(super.getUser().getGroupId());
					edo.setCreateSource("web");
					edoHistory.setBillOfLading(edo.getBillOfLading());
					edoHistory.setOrderNumber(edo.getOrderNumber());
					edoHistory.setCarrierCode(edo.getCarrierCode());
					edoHistory.setCarrierId(super.getUserId());
					edoHistory.setEdiContent(content);
					edoHistory.setContainerNumber(edo.getContainerNumber());
					edoHistory.setCreateBy(super.getUser().getFullName());
					Edo edoCheck = edoService.checkContainerAvailable(edo.getContainerNumber(),edo.getBillOfLading());
                if(edoCheck != null)
                {
					edo.setId(edoCheck.getId());
					edo.setUpdateTime(timeNow);
					edo.setUpdateBy(super.getUser().getFullName());
                    edoService.updateEdo(edo); //TODO
                    edoHistory.setEdoId(edo.getId());
                    edoHistory.setAction("update");
                    edoHistoryService.insertEdoHistory(edoHistory);
                }else {
					edo.setCreateTime(timeNow);
					edo.setCreateBy(super.getUser().getFullName());
                    edoService.insertEdo(edo);
                    edoHistory.setEdoId(edo.getId());
                    edoHistory.setAction("add");
                    edoHistoryService.insertEdoHistory(edoHistory);
				}
				}
		} catch (IOException e) {
			e.printStackTrace(); // transaction rollback?
		}
		return  listEdo;
    }

	@GetMapping("/history/{id}")
	public String getHistory(@PathVariable("id") Long id,ModelMap map) {
		map.put("id", id);
		return PREFIX + "/history";
	}

	@GetMapping("/update/{id}")
	public String getUpdate(@PathVariable("id") Long id,ModelMap map) {
		map.put("id", id);
		return PREFIX + "/update";
	}


	@PostMapping("/readEdiOnly")
	@ResponseBody
	public Object readEdi(String fileContent)
	{
		List<Edo> edo = new ArrayList<>();
		String[] text = fileContent.split("'");
		edo = edoService.readEdi(text);
		return edo;
	}
	


}