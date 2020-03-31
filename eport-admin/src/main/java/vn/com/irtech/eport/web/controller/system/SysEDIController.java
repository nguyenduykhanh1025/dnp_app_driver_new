package vn.com.irtech.eport.web.controller.system;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.utils.poi.ExcelUtil;
import vn.com.irtech.eport.system.domain.SysEdi;
import vn.com.irtech.eport.system.service.ISysEdiService;


@Controller
@RequestMapping("/EDI")
public class SysEDIController extends BaseController
{

	@Autowired
	private ISysEdiService sysEdiService;
	

	
	@GetMapping("/index")
    public String test()
    {
        return "EDI/index";
    }
    
    @RequestMapping(value = "/file",method = { RequestMethod.POST })
	public @ResponseBody Object upload(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException {
		if (file.isEmpty()) {
			System.out.println("File empty");
        }
        String data = "";
		try {
			  String fileName = file.getOriginalFilename();
		      File filenew = new File(this.getFolderUpload(), fileName);
		      file.transferTo(filenew);
		      File myObj = new File(this.getFolderUpload()+"/"+fileName);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
				data += myReader.nextLine();
		      }
              myReader.close();
              String[] text = data.split("'");
              this.ReadEDI(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getSuccessMessage().toString();
    }
    public File getFolderUpload() {
        File folderUpload = new File(System.getProperty("user.home") + "/EDI");
        if (!folderUpload.exists()) {
          folderUpload.mkdirs();
        }
        return folderUpload;
      }

	private JSONObject getSuccessMessage() {
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	@PostMapping("/datalist")
    @RequiresPermissions("system:edi:list")
    @ResponseBody
    public TableDataInfo list(SysEdi sysEdi)
    {
        startPage();
        List<SysEdi> list = sysEdiService.selectSysEdiList(sysEdi);
        return getDataTable(list);
    }
    @GetMapping("/list")
    public String viewList()
    {
        return "EDI/List";
    }
    public AjaxResult addEDI(SysEdi edi)
    {
       
        return toAjax(sysEdiService.insertSysEdi(edi));
    }


    private boolean ReadEDI(String[] text)
	{
        SysEdi edi = new SysEdi();
        ZoneId defaultZoneId = ZoneId.systemDefault();
		for(String s : text)
		{
			//buildNo
			if(s.contains("RFF+BM"))
			{
				int numberIndex = s.length();
                s = s.substring(8, numberIndex);
                edi.setBuildNo(s);
			}
			//businessunit
			if(s.contains("UNB+UNOA"))
			{
  				String[] businessUnit = s.split("\\+");
                edi.setBusinessUnit(businessUnit[2]);		
			}
			//ContNo
			if(s.contains("EQD+CN"))
			{
			
				String[] contNo = s.split("\\+");
                edi.setContNo(contNo[2]);
			}
			//OrderNo
			if(s.contains("RFF+AAJ"))	
			{
				int numberIndex = s.length();
				s = s.substring(9, numberIndex);
				edi.setOrderNo(s);
			}
			//releaseTo
			if(s.contains("NAD+BJ"))
			{
				String[] releaseTo = s.split("\\+");
				releaseTo[3] = releaseTo[3].substring(0, releaseTo[3].length() - 1);
                edi.setReleaseTo(releaseTo[3]);
			}
			//validToDay
			if(s.contains("DTM+400"))
			{
				String[] validToDay = s.split("\\:");
                validToDay[1] = validToDay[1].substring(0, validToDay[1].length() - 4);
                LocalDate date = LocalDate.parse(validToDay[1], DateTimeFormatter.BASIC_ISO_DATE);
                Date date2 = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
				edi.setValidtoDay(date2);
			}                                                                                                                                                                                                                                                                                                                                                                                           
			//emptyContDepot
			if(s.contains("LOC+99"))
			{
				String[] emptyContDepot = s.split("\\+");
				emptyContDepot[3] = emptyContDepot[3].substring(0, emptyContDepot[3].length());
                edi.setEmptycontDepot(emptyContDepot[3]);		
			}
			if(s.contains("FTX+AAI"))
			{
				String[] haulage = s.split("\\+");
				haulage[4] = haulage[4].substring(0, haulage[4].length());
				
                if(!haulage[4].isEmpty()){
                    Long i = Long.parseLong(haulage[4]);
					edi.setHaulage(i);
				}  
				sysEdiService.insertSysEdi(edi);    
			}
        }
		return true;
	}
    
}