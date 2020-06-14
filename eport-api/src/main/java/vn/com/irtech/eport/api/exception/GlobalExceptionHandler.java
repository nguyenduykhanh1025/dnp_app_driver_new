package vn.com.irtech.eport.api.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import vn.com.irtech.eport.api.consts.MessageConsts;
import vn.com.irtech.eport.api.message.MessageHelper;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.exception.BusinessException;

/**
 * Global exception handler
 * 
 * @author admin
 */
@ControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 权Limit verification failed If the request returns json for ajax, the normal request jumps to the page
     */
    @ExceptionHandler(AuthorizationException.class)
    public ModelAndView handleAuthorizationException(HttpServletRequest request, AuthorizationException e)
    {
    	AjaxResult ajaxResult = AjaxResult.error(MessageHelper.getMessage(MessageConsts.E0002));
    	ajaxResult.put("errorCode", HttpServletResponse.SC_UNAUTHORIZED);
    	return processException(ajaxResult);
    }

    /**
     * Request method is not supported
     */
    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public ModelAndView handleException(HttpRequestMethodNotSupportedException e)
    {
        log.error(e.getMessage(), e);
        AjaxResult ajaxResult = AjaxResult.error(MessageHelper.getMessage(MessageConsts.E0001));
    	ajaxResult.put("errorCode", HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    	return processException(ajaxResult);
    }

    /**
     * Intercepting unknown runtime exceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView notFount(RuntimeException e)
    {
        log.error("Runtime Exception:", e);
        AjaxResult ajaxResult = AjaxResult.error(MessageHelper.getMessage(MessageConsts.E0001));
    	ajaxResult.put("errorCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    	return processException(ajaxResult);
    }

    /**
     * System exception
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e)
    {
        log.error(e.getMessage(), e);
        AjaxResult ajaxResult = AjaxResult.error(MessageHelper.getMessage(MessageConsts.E0001));
    	ajaxResult.put("errorCode", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    	return processException(ajaxResult);
    }

    /**
     * Business abnormal
     */
    @ExceptionHandler(BusinessException.class)
    public ModelAndView businessException(HttpServletRequest request, BusinessException e)
    {
        log.error(e.getMessage(), e);
        AjaxResult ajaxResult = AjaxResult.error(e.getMessage());
    	ajaxResult.put("errorCode", HttpServletResponse.SC_BAD_REQUEST);

    	return processException(ajaxResult);
    }

    /**
     * Custom validation exception
     */
    @ExceptionHandler(BindException.class)
    public ModelAndView validatedBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        AjaxResult ajaxResult = AjaxResult.error(message);
    	ajaxResult.put("errorCode", HttpServletResponse.SC_PRECONDITION_FAILED);
    	return processException(ajaxResult);
    }
    
    private ModelAndView processException(AjaxResult ajaxResult) {
    	ModelAndView view = new ModelAndView(new MappingJackson2JsonView());
    	for (Map.Entry<String, Object> entry : ajaxResult.entrySet()) {
    		view.addObject(entry.getKey(), entry.getValue());
    	}
    	return view;
    }
}
