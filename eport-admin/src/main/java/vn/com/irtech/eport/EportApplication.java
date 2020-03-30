package vn.com.irtech.eport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author admin
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class EportApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(EportApplication.class, args);
    }
}