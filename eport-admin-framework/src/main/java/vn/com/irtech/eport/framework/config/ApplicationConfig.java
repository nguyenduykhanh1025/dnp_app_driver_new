package vn.com.irtech.eport.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Program annotation configuration
 *
 * @author admin
 */
@Configuration
// Indicates that the proxy object is exposed through the aop framework, and AopContext can access
@EnableAspectJAutoProxy(exposeProxy = true)
// Specify the path of the package of the Mapper class to be scanned
@MapperScan("vn.com.irtech.eport.**.mapper")
public class ApplicationConfig
{

}
