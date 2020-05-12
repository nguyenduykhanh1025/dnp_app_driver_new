package vn.com.irtech.eport.framework.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.alibaba.druid.pool.DruidDataSource;

/**
 * druid configuration properties
 * 
 * @author admin
 */
@Configuration
public class DruidProperties
{
    @Value("${spring.datasource.druid.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.druid.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.druid.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.druid.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.maxEvictableIdleTimeMillis}")
    private int maxEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;

    public DruidDataSource dataSource(DruidDataSource datasource)
    {
    	/** Configure initialization size, minimum and maximum */
        datasource.setInitialSize(initialSize);
        datasource.setMaxActive(maxActive);
        datasource.setMinIdle(minIdle);

        /** Configure the time to wait for a connection to time out */
        datasource.setMaxWait(maxWait);

        /** How often to configure the interval before performing a test to detect idle connections that need to be closed, in milliseconds */
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        /** Configure the minimum and maximum survival time of a connection in the pool, in milliseconds */
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);

        /**
         * The sql used to detect whether the connection is valid, the requirement is a query statement, commonly used to select 'x'. If the validationQuery is null, testOnBorrow, testOnReturn, testWhileIdle will not work.
         */
        datasource.setValidationQuery(validationQuery);
        /** The recommended configuration is true, which does not affect performance and guarantees security. Check when applying for a connection. If the idle time is greater than timeBetweenEvictionRunsMillis, execute a validationQuery to check whether the connection is valid. */
        datasource.setTestWhileIdle(testWhileIdle);
        /** When applying for a connection, perform a validationQuery to check whether the connection is valid. Doing this configuration will reduce performance。 */
        datasource.setTestOnBorrow(testOnBorrow);
        /** When returning the connection, perform a validationQuery to check whether the connection is valid. Doing this configuration will reduce performance. */
        datasource.setTestOnReturn(testOnReturn);
        return datasource;
    }
}
