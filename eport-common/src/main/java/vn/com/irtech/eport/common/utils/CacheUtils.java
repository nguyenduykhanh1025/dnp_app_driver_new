package vn.com.irtech.eport.common.utils;

import java.util.Iterator;
import java.util.Set;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.com.irtech.eport.common.utils.spring.SpringUtils;

/**
 * Cache tools
 * 
 * @author admin
 */
public class CacheUtils
{
    private static Logger logger = LoggerFactory.getLogger(CacheUtils.class);

    private static CacheManager cacheManager = SpringUtils.getBean(CacheManager.class);

    private static final String SYS_CACHE = "sys-cache";

    /**
     * Get SYS_CACHE cache
     * 
     * @param key
     * @return
     */
    public static Object get(String key)
    {
        return get(SYS_CACHE, key);
    }

    /**
     * Get SYS_CACHE cache
     * 
     * @param key
     * @param defaultValue
     * @return
     */
    public static Object get(String key, Object defaultValue)
    {
        Object value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Write to SYS_CACHE cache
     * 
     * @param key
     * @return
     */
    public static void put(String key, Object value)
    {
        put(SYS_CACHE, key, value);
    }

    /**
     * Remove from SYS_CACHE cache
     * 
     * @param key
     * @return
     */
    public static void remove(String key)
    {
        remove(SYS_CACHE, key);
    }

    /**
     * Get cache
     * 
     * @param cacheName
     * @param key
     * @return
     */
    public static Object get(String cacheName, String key)
    {
        return getCache(cacheName).get(getKey(key));
    }

    /**
     * Get cache
     * 
     * @param cacheName
     * @param key
     * @param defaultValue
     * @return
     */
    public static Object get(String cacheName, String key, Object defaultValue)
    {
        Object value = get(cacheName, getKey(key));
        return value != null ? value : defaultValue;
    }

    /**
     * Write cache
     * 
     * @param cacheName
     * @param key
     * @param value
     */
    public static void put(String cacheName, String key, Object value)
    {
    	 Cache<String, Object> cache = getCache(cacheName);
    	 if (cache != null) {
    		 cache.put(getKey(key), value);
    	 }
    }

    /**
     * Remove from cache
     * 
     * @param cacheName
     * @param key
     */
    public static void remove(String cacheName, String key)
    {
        getCache(cacheName).remove(getKey(key));
    }

    /**
     * Remove all from cache
     * 
     * @param cacheName
     */
    public static void removeAll(String cacheName)
    {
        Cache<String, Object> cache = getCache(cacheName);
        Set<String> keys = cache.keys();
        for (Iterator<String> it = keys.iterator(); it.hasNext();)
        {
            cache.remove(it.next());
        }
        logger.info("Clear cache： {} => {}", cacheName, keys);
    }

    /**
     * Remove the specified key from the cache
     * 
     * @param keys
     */
    public static void removeByKeys(Set<String> keys)
    {
        removeByKeys(SYS_CACHE, keys);
    }

    /**
     * Remove the specified key from the cache
     * 
     * @param cacheName
     * @param keys
     */
    public static void removeByKeys(String cacheName, Set<String> keys)
    {
        for (Iterator<String> it = keys.iterator(); it.hasNext();)
        {
            remove(it.next());
        }
        logger.info("Clear cache： {} => {}", cacheName, keys);
    }

    /**
     * Get the cache key name
     * 
     * @param key
     * @return
     */
    private static String getKey(String key)
    {
        return key;
    }

    /**
     * Get a Cache, if not, display the log.
     * 
     * @param cacheName
     * @return
     */
    private static Cache<String, Object> getCache(String cacheName)
    {
    	if (cacheManager == null) {
			return null;
		}
        Cache<String, Object> cache = cacheManager.getCache(cacheName);
        if (cache == null)
        {
            throw new RuntimeException("Cache “" + cacheName + "” not exist.");
        }
        return cache;
    }

}
