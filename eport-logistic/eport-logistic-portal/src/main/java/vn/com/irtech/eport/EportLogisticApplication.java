package vn.com.irtech.eport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author admin
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class EportLogisticApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(EportLogisticApplication.class, args);
    }
}