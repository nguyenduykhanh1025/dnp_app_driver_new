package vn.com.irtech.eport.carrier.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.druid.sql.visitor.functions.Now;
import com.google.gson.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import vn.com.irtech.eport.common.core.domain.AjaxResult;
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
		List<Edo> edo = new ArrayList<>();
		try {
			  String fileName = file.getOriginalFilename();
		      File fileNew = new File(this.getFolderUpload(), fileName);
		      file.transferTo(fileNew);
		      File myObj = new File(this.getFolderUpload()+"/"+fileName);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
				content += myReader.nextLine();
		      }
              myReader.close();
			  String[] text = content.split("'");
			  edo = edoService.readEdi(text);
			  EdoHistory edoHistory = new EdoHistory();
			  Date timeNow = new Date();
			  for(Edo Edo : edo)
            	{
				Edo.setCarrierId(super.getUser().getGroupId());
				Edo.setCreateSource("web");
				edoHistory.setBillOfLading(Edo.getBillOfLading());
				edoHistory.setOrderNumber(Edo.getOrderNumber());
				edoHistory.setCarrierCode(Edo.getCarrierCode());
				edoHistory.setCarrierId(super.getUserId());
				edoHistory.setEdiContent(content);
				edoHistory.setContainerNumber(Edo.getContainerNumber());
				edoHistory.setCreateBy(super.getUser().getFullName());
				Edo edoCheck = edoService.checkContainerAvailable(Edo.getContainerNumber(),Edo.getBillOfLading());
                if(edoCheck != null)
                {
					Edo.setId(edoCheck.getId());
					Edo.setUpdateTime(timeNow);
					Edo.setUpdateBy(super.getUser().getFullName());
                    edoService.updateEdo(Edo); //TODO
                    edoHistory.setEdoId(Edo.getId());
                    edoHistory.setAction("update");
                    edoHistoryService.insertEdoHistory(edoHistory);
                }else {
					Edo.setCreateTime(timeNow);
					Edo.setCreateBy(super.getUser().getFullName());
                    edoService.insertEdo(Edo);
                    edoHistory.setEdoId(Edo.getId());
                    edoHistory.setAction("add");
                    edoHistoryService.insertEdoHistory(edoHistory);
				}
			}
		} catch (IOException e) {
			e.printStackTrace(); // transaction rollback?
		}
		return  edo;
    }
    public File getFolderUpload() {
		LocalDate toDay = LocalDate.now();
		String year = Integer.toString(toDay.getYear());
		String month = Integer.toString(toDay.getMonthValue());
		String day = Integer.toString(toDay.getDayOfMonth());
        File folderUpload = new File(super.folderUpLoad()  + "/" +  year + "/" +  month + "/" +  day);
        if (!folderUpload.exists()) {
          folderUpload.mkdirs();
        }
        return folderUpload;
	}

	@GetMapping("/history/{id}")
	public String getHistory(@PathVariable("id") Long id,ModelMap map) {
		map.put("id",id);
		return PREFIX + "/history";
	}

	@GetMapping("/getHistory")
	@ResponseBody
	public TableDataInfo getHistory(EdoHistory edoHistory,Long id)
	{
		edoHistory.setEdoId(id);
		//checkCarrier code 
		List<EdoHistory> edoHistories = edoHistoryService.selectEdoHistoryList(edoHistory);
		return getDataTable(edoHistories);
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