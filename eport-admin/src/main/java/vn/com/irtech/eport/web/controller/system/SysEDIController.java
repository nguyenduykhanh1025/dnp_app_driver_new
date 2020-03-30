package vn.com.irtech.eport.web.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @PostMapping("/file")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

    
        String name = file.toString();
        System.out.println(name);
        
        redirectAttributes.addFlashAttribute("message",
        "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "EDI/ok";
    }
       
    
    
}