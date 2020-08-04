/**
 * 
 */
package vn.com.irtech.eport.api.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.io.ResourceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vn.com.irtech.eport.common.config.cache.RedisCacheManager;
import vn.com.irtech.eport.common.utils.StringUtils;

/**
 * @author GiapHD
 *
 */
@Configuration
public class CacheConfig {
	
	// redis cache switch
    @Value ( "${spring.redis.enabled}" )
    private  boolean  redisEnabled  =  false ;
    

    /**
     * Cache manager implemented using Ehcache
     */
//    @Bean
    public EhCacheManager getEhCacheManager()
    {
        net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.getCacheManager("eport");
        EhCacheManager em = new EhCacheManager();
        if (StringUtils.isNull(cacheManager))
        {
            em.setCacheManager(new net.sf.ehcache.CacheManager(getCacheManagerConfigFileInputStream()));
            return em;
        }
        else
        {
            em.setCacheManager(cacheManager);
            return em;
        }
    }

    /**
     * Return to the configuration file stream to prevent the ehcache configuration file from being occupied all the time and unable to completely destroy the project and redeploy
     */
    protected InputStream getCacheManagerConfigFileInputStream()
    {
        String configFile = "classpath:ehcache/ehcache-shiro.xml";
        InputStream inputStream = null;
        try
        {
            inputStream = ResourceUtils.getInputStreamForPath(configFile);
            byte[] b = IOUtils.toByteArray(inputStream);
            InputStream in = new ByteArrayInputStream(b);
            return in;
        }
        catch (IOException e)
        {
            throw new ConfigurationException(
                    "Unable to obtain input stream for cacheManagerConfigFile [" + configFile + "]", e);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }
    
    /**
     * Cache manager is implemented using redis
     *
     * @return
     */
//    @Bean
//    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisCacheManager getRedisCacheManager()
    {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        return redisCacheManager;
    }

    @Bean
    public CacheManager cacheManager (){
        if (redisEnabled){
            return  getRedisCacheManager ();
        } else  {
            return  getEhCacheManager ();
        }
    }
}
