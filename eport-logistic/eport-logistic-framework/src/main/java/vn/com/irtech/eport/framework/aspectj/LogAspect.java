//package vn.com.irtech.eport.framework.aspectj;
//
//import java.lang.reflect.Method;
//import java.util.Map;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import vn.com.irtech.eport.common.annotation.Log;
//import vn.com.irtech.eport.common.enums.BusinessStatus;
//import vn.com.irtech.eport.common.json.JSON;
//import vn.com.irtech.eport.common.utils.ServletUtils;
//import vn.com.irtech.eport.common.utils.StringUtils;
//import vn.com.irtech.eport.framework.manager.AsyncManager;
//import vn.com.irtech.eport.framework.manager.factory.AsyncFactory;
//import vn.com.irtech.eport.framework.util.ShiroUtils;
//import vn.com.irtech.eport.system.domain.SysOperLog;
//import vn.com.irtech.eport.system.domain.SysUser;
//
///**
// * @author admin
// */
//@Aspect
//@Component
//public class LogAspect
//{
//    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
//
//    @Pointcut("@annotation(vn.com.irtech.eport.common.annotation.Log)")
//    public void logPointCut()
//    {
//    }
//
//    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
//    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult)
//    {
//        handleLog(joinPoint, null, jsonResult);
//    }
//
//    @AfterThrowing(value = "logPointCut()", throwing = "e")
//    public void doAfterThrowing(JoinPoint joinPoint, Exception e)
//    {
//        handleLog(joinPoint, e, null);
//    }
//
//    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult)
//    {
//        try
//        {
//            Log controllerLog = getAnnotationLog(joinPoint);
//            if (controllerLog == null)
//            {
//                return;
//            }
//
//            SysUser currentUser = ShiroUtils.getSysUser();
//
//            // *========Database log=========*//
//            SysOperLog operLog = new SysOperLog();
//            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
//            // Requested address
//            String ip = ShiroUtils.getIp();
//            operLog.setOperIp(ip);
//            // Return parameter
//            operLog.setJsonResult(JSON.marshal(jsonResult));
//
//            operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
//            if (currentUser != null)
//            {
//                operLog.setOperName(currentUser.getLoginName());
//                if (StringUtils.isNotNull(currentUser.getDept())
//                        && StringUtils.isNotEmpty(currentUser.getDept().getDeptName()))
//                {
//                    operLog.setDeptName(currentUser.getDept().getDeptName());
//                }
//            }
//
//            if (e != null)
//            {
//                operLog.setStatus(BusinessStatus.FAIL.ordinal());
//                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
//            }
//            // Setting method name
//            String className = joinPoint.getTarget().getClass().getName();
//            String methodName = joinPoint.getSignature().getName();
//            operLog.setMethod(className + "." + methodName + "()");
//            // Set request method
//            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
//            // Handling parameters on setting annotations
//            getControllerMethodDescription(controllerLog, operLog);
//            // Save the database
//            AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
//        }
//        catch (Exception exp)
//        {
//            log.error("==Pre-notification exception==");
//            log.error("Exception information: {}", exp.getMessage());
//            exp.printStackTrace();
//        }
//    }
//
//    /**
//     * Get the description information of the method in the annotation Used for Controller layer annotation
//     * 
//     * @param log 日志
//     * @param operLog 操作日志
//     * @throws Exception
//     */
//    public void getControllerMethodDescription(Log log, SysOperLog operLog) throws Exception
//    {
//        // Set action
//        operLog.setBusinessType(log.businessType().ordinal());
//        // Set title
//        operLog.setTitle(log.title());
//        // Set operator category
//        operLog.setOperatorType(log.operatorType().ordinal());
//        // Do you need to save the request, parameters and values
//        if (log.isSaveRequestData())
//        {
//            // Obtain the parameter information and pass it into the database.
//            setRequestValue(operLog);
//        }
//    }
//
//    /**
//     * Get the parameters of the request and put it in the log
//     * 
//     * @param operLog Operation log
//     * @throws Exception abnormal
//     */
//    private void setRequestValue(SysOperLog operLog) throws Exception
//    {
//        Map<String, String[]> map = ServletUtils.getRequest().getParameterMap();
//        String params = JSON.marshal(map);
//        operLog.setOperParam(StringUtils.substring(params, 0, 2000));
//    }
//
//    /**
//     * Whether there is an annotation, if it exists, get it
//     */
//    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception
//    {
//        Signature signature = joinPoint.getSignature();
//        MethodSignature methodSignature = (MethodSignature) signature;
//        Method method = methodSignature.getMethod();
//
//        if (method != null)
//        {
//            return method.getAnnotation(Log.class);
//        }
//        return null;
//    }
//}
