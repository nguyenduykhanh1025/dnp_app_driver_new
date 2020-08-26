package vn.com.irtech.eport.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author admin
 */
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("vn.com.irtech.eport.**.mapper")
public class ApplicationConfig
{

}
