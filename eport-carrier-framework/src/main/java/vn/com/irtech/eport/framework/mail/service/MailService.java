package vn.com.irtech.eport.framework.mail.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService {
  @Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	TemplateEngine templateEngine;
 
    public void prepareAndSend(String subject, String recipient, String ccEmail, Map<String, Object> variables, String template) throws MessagingException {
        // Prepare the evaluation context
        Context ctx = new Context();
        ctx.setVariables(variables);
        String htmlContent = templateEngine.process(template, ctx);
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());
        try {
			helper.setFrom("admin_erp@danangport.com","Cảng Đà Nẵng");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        helper.setSubject(subject);
        helper.setTo(InternetAddress.parse(recipient)); //send multiple recipients
        helper.setText(htmlContent, true);
        if (ccEmail.length() != 0) {
          helper.setCc(InternetAddress.parse(ccEmail));
        }    
        // Send email
        mailSender.send(mimeMessage);
    }
}