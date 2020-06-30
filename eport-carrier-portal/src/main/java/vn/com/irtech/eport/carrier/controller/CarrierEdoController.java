package vn.com.irtech.eport.carrier.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


	@GetMapping("/edo")
	@ResponseBody
	public TableDataInfo edo(Edo edo,String fromDate,String toDate)
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
	public  Object upload(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException {
		if (file.isEmpty()) {
			System.out.println("File empty");
        }
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
			  for(Edo Edo : edo)
            	{
                Edo.setCarrierCode("MCA");
				Edo.setCarrierId((long) 1);
				Edo.setSecureCode("web");
				Edo edoCheck = new Edo();
				edoCheck = edoService.checkContainerAvailable(Edo.getContainerNumber(),Edo.getBillOfLading());
                if(edoCheck != null)
                {
					Edo.setId(edoCheck.getId());
                    edoService.updateEdo(Edo);
                    edoHistory.setBillOfLading(Edo.getBillOfLading());
                    edoHistory.setCarrierCode(Edo.getCarrierCode());
                    edoHistory.setCarrierId(ShiroUtils.getGroupId());
                    edoHistory.setEdoId(edoCheck.getId());
                    edoHistory.setContainerNumber(Edo.getContainerNumber());
                    edoHistory.setAction("update");
                    edoHistory.setEdiContent(content);
                    edoHistoryService.insertEdoHistory(edoHistory);
                }else {
                    edoService.insertEdo(Edo);
                    edoHistory.setBillOfLading(Edo.getBillOfLading());
                    edoHistory.setCarrierCode(Edo.getCarrierCode());
                    edoHistory.setCarrierId(ShiroUtils.getGroupId());
                    edoHistory.setEdoId(Edo.getId());
                    edoHistory.setContainerNumber(Edo.getContainerNumber());
                    edoHistory.setAction("add");
                    edoHistory.setEdiContent(content);
                    edoHistoryService.insertEdoHistory(edoHistory);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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

	@GetMapping("/history/{containerNumber}")
	public String getHistory(@PathVariable("containerNumber") String containerNumber,ModelMap map) {
		map.put("containerNumber",containerNumber);
		return PREFIX + "/history";
	}

	@GetMapping("/getHistory")
	@ResponseBody
	public TableDataInfo getHistory(EdoHistory edoHistory,String containerNumber)
	{
		edoHistory.setContainerNumber(containerNumber);
		List<EdoHistory> edoHistories = edoHistoryService.selectEdoHistoryList(edoHistory);
		return getDataTable(edoHistories);
	}
	


}