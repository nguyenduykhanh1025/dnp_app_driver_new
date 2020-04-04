package vn.com.irtech.eport.web.controller.system;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.system.domain.SysEdi;
import vn.com.irtech.eport.system.service.ISysEdiService;


@Controller
@RequestMapping("/edi")
public class SysEDIController extends BaseController
{

	@Autowired
	private ISysEdiService sysEdiService;
	
	
	@GetMapping("/index")
    public String test()
    {
        return "edi/index";
    }

	@GetMapping("/add")
    public String add()
    {
        return "edi/add";
	}
	@GetMapping("/hanson")
    public String hanson()
    {
        return "edi/add";
    }
	@RequestMapping(value = "/file",method = { RequestMethod.POST })
	public @ResponseBody Object upload(@RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException {
		if (file.isEmpty()) {
			System.out.println("File empty");
        }
		String data = "";
		List<JSONObject> obj = new ArrayList<>();
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
              obj = this.ReadEDI(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
    }
    public File getFolderUpload() {
        File folderUpload = new File("D:/eport/upload");
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
	@GetMapping("/viewdatatestlist")
	public String viewdatatestlist()
	{
		return ("/edi/viewdatatestlist");
	}
	@GetMapping("/datatestlist")
    @RequiresPermissions("system:edi:list")
    @ResponseBody
    public TableDataInfo listtest(SysEdi sysEdi)
    {
        startPage();
        List<SysEdi> list = sysEdiService.selectSysEdiList(sysEdi);
        return getDataTable(list);
    }
    @GetMapping("/list")
    public String viewList()
    {
        return "edi/list";
    }
    public AjaxResult addEDI(SysEdi edi)
    {
       
        return toAjax(sysEdiService.insertSysEdi(edi));
    }


    private List<JSONObject> ReadEDI(String[] text)
	{
        SysEdi edi = new SysEdi();
		ZoneId defaultZoneId = ZoneId.systemDefault();
		JSONObject obj = new JSONObject();
		List<JSONObject> listObj = new ArrayList<>();
		for(String s : text)
		{
			//buildNo
			
			if(s.contains("RFF+BM"))
			{
				int numberIndex = s.length();
				if(!s.isEmpty())
				{
					s = s.substring(8, numberIndex);
					obj.put("buildNo", s);
					edi.setBuildNo(s);
				}
			}
			//businessUnit
			if(s.contains("UNB+UNOA"))
			{
				String[] businessUnit = s.split("\\+");
				if(!s.isEmpty())
				{
					obj.put("businessUnit", businessUnit[2]);
					edi.setBusinessUnit(businessUnit[2]);
				}
			}
			//contNo
			if(s.contains("EQD+CN"))
			{
			
				String[] contNo = s.split("\\+");
				if(!s.isEmpty())
				{
					obj.put("contNo",contNo[2]);
                	edi.setContNo(contNo[2]);
				}
				
			}
			//orderNo
			if(s.contains("RFF+AAJ"))	
			{
				int numberIndex = s.length();
				if(!s.isEmpty())
				{
					s = s.substring(8, numberIndex);
					obj.put("orderNo", s);
					edi.setOrderNo(s);
				}
			}
			//releaseTo
			if(s.contains("NAD+BJ"))
			{
				String[] releaseTo = s.split("\\+");
				if(!releaseTo[3].isEmpty())
				{
					releaseTo[3] = releaseTo[3].substring(0, releaseTo[3].length() - 1);
					obj.put("releaseTo", releaseTo[3]);
					edi.setReleaseTo(releaseTo[3]);
				}
			}
			//validToDay
			if(s.contains("DTM+400"))
			{
				String[] validToDay = s.split("\\:");
				if(!validToDay[1].isEmpty())
				{
					validToDay[1] = validToDay[1].substring(0, validToDay[1].length() - 4);
					LocalDate date = LocalDate.parse(validToDay[1], DateTimeFormatter.BASIC_ISO_DATE);
					Date date2 = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
					obj.put("validToDay", date2);
					edi.setValidtoDay(date2);
				}
			}                                                                                                                                                                                                                                                                                                                                                                                           
			//emptyContDepot
			if(s.contains("LOC+99"))
			{
				String[] emptyContDepotA = s.split("\\+");
				if(!emptyContDepotA[3].isEmpty()){
					String[] emptyContDepot = emptyContDepotA[3].split(":");
					obj.put("emptyContDepot", emptyContDepot[0]);
					edi.setEmptycontDepot(emptyContDepot[0]);
				}
						
			}
			//haulage
			if(s.contains("FTX+AAI"))
			{
				String[] haulage = s.split("\\+");
				haulage[4] = haulage[4].substring(0, haulage[4].length());
				
                if(!haulage[4].isEmpty()){
                    Long i = Long.parseLong(haulage[4]);
					edi.setHaulage(i);
					obj.put("haulage", haulage[4]);
				}
				// edi.setStatus(2);
				sysEdiService.insertSysEdi(edi);  
				listObj.add(obj);
				obj = new JSONObject();
			}
        }
		return listObj;
	}
    
}