package vn.com.irtech.eport.web.controller.edo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoHistory;
import vn.com.irtech.eport.carrier.service.IEdoHistoryService;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.core.controller.BaseController;

@Controller
@RequestMapping("/edo/ftp")
public class EdoFtpController extends BaseController{
    
    final String PREFIX = "/edo/ftp";
    @Autowired
    private IEdoService edoService;
    @Autowired
    private IEdoHistoryService edoHistoryService;
    
    @GetMapping("/index")
    public String index()
    {
        return PREFIX + "/index";
    }

    @GetMapping("/loadFileFromDisk")
	@ResponseBody
	public Object loadFileFromDisk() throws IOException {

        //Define folder receiver and folder moving file Edo before save
		final File folder = new File("D:/testReadFile");
        final String destinationFolder = "D:/moveFileToThisFolder/";
        
		List<Edo> edo = new ArrayList<>();
        //Edo edi = new Edo();
       
        edoService.selectEdoById((long) 6);
			for (final File fileEntry : folder.listFiles()) {
			String path = fileEntry.getAbsolutePath();
			String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            String[] text = content.split("'");
            EdoHistory edoHistory = new EdoHistory();
           
            EdoCommon common = new EdoCommon();
            edo = common.readEdi(text);
            for(Edo Edo : edo)
            {

                Edo.setCarrierCode("MCA");
				Edo.setCarrierId((long) 1);
                Edo.setSecureCode("web");
                if(edoService.checkContainerAvailable(Edo.getContainerNumber(),Edo.getBillOfLading()) != null)
                {
                    edoService.updateEdo(Edo);
                    edoHistory.setBillOfLading(Edo.getBillOfLading());
                    edoHistory.setCarrierCode(Edo.getCarrierCode());
                    edoHistory.setCarrierId((long) 1);
                    edoHistory.setEdoId((long) 1);
                    edoHistory.setContainerNumber(Edo.getContainerNumber());
                    edoHistory.setAction("update");
                    edoHistory.setEdiContent(content);
                    edoHistoryService.insertEdoHistory(edoHistory);
                }else {
                    edoService.insertEdo(Edo);
                    edoHistory.setBillOfLading(Edo.getBillOfLading());
                    edoHistory.setCarrierCode(Edo.getCarrierCode());
                    edoHistory.setCarrierId((long) 1);
                    edoHistory.setEdoId((long) 1);
                    edoHistory.setContainerNumber(Edo.getContainerNumber());
                    edoHistory.setAction("add");
                    edoHistory.setEdiContent(content);
                    edoHistoryService.insertEdoHistory(edoHistory);
                }
                
            }
            
			//Move file to distination folder
			fileEntry.renameTo(new File(destinationFolder + fileEntry.getName()));
			fileEntry.delete();
			}
		return edo;
    } 
    




}