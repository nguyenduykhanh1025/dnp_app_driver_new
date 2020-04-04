package vn.com.irtech.eport.carrier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author admin
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class EportCarrierApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(EportCarrierApplication.class, args);
    }
}