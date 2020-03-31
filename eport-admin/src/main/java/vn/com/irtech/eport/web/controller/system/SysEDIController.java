package vn.com.irtech.eport.web.controller.system;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;
import vn.com.irtech.eport.common.core.controller.BaseController;


@Controller
@RequestMapping("/EDI")
public class SysEDIController extends BaseController
{
	@GetMapping("/index")
    public String test()
    {
        return "EDI/index";
    }
    
    @RequestMapping(value = "/file",
	        method = { RequestMethod.POST })
	public @ResponseBody Object upload(
	        @RequestParam("file") MultipartFile file,
	        HttpServletRequest request) throws IOException {
    	
		if (file.isEmpty()) {
			System.out.println("File empty");
		}
		try {
			String fileName = file.getOriginalFilename();
		      File filenew = new File(this.getFolderUpload(), fileName);
		      file.transferTo(filenew);
		      File myObj = new File(this.getFolderUpload()+"/"+fileName);
		      Scanner myReader = new Scanner(myObj);
		      while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //Xử lý data ở đây
		        System.out.println(data);
		      }
		      myReader.close();
		      System.out.print(this.getFolderUpload()+fileName);
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
       
    
    
}