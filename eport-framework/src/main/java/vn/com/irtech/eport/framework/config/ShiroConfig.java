package vn.com.irtech.eport.framework.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.com.irtech.eport.common.utils.StringUtils;
import vn.com.irtech.eport.common.utils.spring.SpringUtils;
import vn.com.irtech.eport.framework.shiro.realm.UserRealm;
import vn.com.irtech.eport.framework.shiro.session.OnlineSessionDAO;
import vn.com.irtech.eport.framework.shiro.session.OnlineSessionFactory;
import vn.com.irtech.eport.framework.shiro.web.filter.LogoutFilter;
import vn.com.irtech.eport.framework.shiro.web.filter.captcha.CaptchaValidateFilter;
import vn.com.irtech.eport.framework.shiro.web.filter.kickout.KickoutSessionFilter;
import vn.com.irtech.eport.framework.shiro.web.filter.online.OnlineSessionFilter;
import vn.com.irtech.eport.framework.shiro.web.filter.sync.SyncOnlineSessionFilter;
import vn.com.irtech.eport.framework.shiro.web.session.OnlineWebSessionManager;
import vn.com.irtech.eport.framework.shiro.web.session.SpringSessionValidationScheduler;
import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

/**
 * Permission configuration loading
 * 
 * @author admin
 */
@Configuration
public class ShiroConfig
{
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    // Session timeout time, in milliseconds (default 30 minutes)
    @Value("${shiro.session.expireTime}")
    private int expireTime;

    // How often to check the validity of a session, in milliseconds, the default is 10 minutes
    @Value("${shiro.session.validationInterval}")
    private int validationInterval;

    // Maximum sessions of the same user
    @Value("${shiro.session.maxSession}")
    private int maxSession;

    // Kick out previously logged in / post logged in users, kick out previously logged in users by default
    @Value("${shiro.session.kickoutAfter}")
    private boolean kickoutAfter;

    // Verification code switch
    @Value("${shiro.user.captchaEnabled}")
    private boolean captchaEnabled;

    // Verification code type
    @Value("${shiro.user.captchaType}")
    private String captchaType;

    // Set Cookie Domain Name
    @Value("${shiro.cookie.domain}")
    private String domain;

    // Set effective access path for cookies
    @Value("${shiro.cookie.path}")
    private String path;

    // Set HttpOnly property
    @Value("${shiro.cookie.httpOnly}")
    private boolean httpOnly;

    // Set the cookie expiration time, in seconds
    @Value("${shiro.cookie.maxAge}")
    private int maxAge;

    // Login address
    @Value("${shiro.user.loginUrl}")
    private String loginUrl;

    // Authorization failed address
    @Value("${shiro.user.unauthorizedUrl}")
    private String unauthorizedUrl;

    /**
     * Cache manager using Ehcache
     */
    @Bean
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
     * Return to the configuration file flow to prevent the ehcache configuration file from being occupied all the time, and the project cannot be completely destroyed and redeployed
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
     * Custom Realm
     */
    @Bean
    public UserRealm userRealm(EhCacheManager cacheManager)
    {
        UserRealm userRealm = new UserRealm();
        userRealm.setCacheManager(cacheManager);
        return userRealm;
    }

    /**
     * Custom sessionDAO session
     */
    @Bean
    public OnlineSessionDAO sessionDAO()
    {
        OnlineSessionDAO sessionDAO = new OnlineSessionDAO();
        return sessionDAO;
    }

    /**
     * Custom sessionFactory session
     */
    @Bean
    public OnlineSessionFactory sessionFactory()
    {
        OnlineSessionFactory sessionFactory = new OnlineSessionFactory();
        return sessionFactory;
    }

    /**
     * Session manager
     */
    @Bean
    public OnlineWebSessionManager sessionManager()
    {
        OnlineWebSessionManager manager = new OnlineWebSessionManager();
        // Join the cache manager
        manager.setCacheManager(getEhCacheManager());
        // Delete expired session
        manager.setDeleteInvalidSessions(true);
        // Set global session timeout
        manager.setGlobalSessionTimeout(expireTime * 60 * 1000);
        // Remove JSESSIONID
        manager.setSessionIdUrlRewritingEnabled(false);
        // Define the invalid session timing scheduler to use
        manager.setSessionValidationScheduler(SpringUtils.getBean(SpringSessionValidationScheduler.class));
        // Whether to check the session regularly
        manager.setSessionValidationSchedulerEnabled(true);
        // Custom SessionDao
        manager.setSessionDAO(sessionDAO());
        // Custom sessionFactory
        manager.setSessionFactory(sessionFactory());
        return manager;
    }

    /**
     * Security manager
     */
    @Bean
    public SecurityManager securityManager(UserRealm userRealm)
    {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // Set up realm.
        securityManager.setRealm(userRealm);
        // remember me
        securityManager.setRememberMeManager(rememberMeManager());
        // Inject into the cache manager;
        securityManager.setCacheManager(getEhCacheManager());
        // session manager
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * Exit filter
     */
    public LogoutFilter logoutFilter()
    {
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setCacheManager(getEhCacheManager());
        logoutFilter.setLoginUrl(loginUrl);
        return logoutFilter;
    }

    /**
     * Shiro Filter configuration
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager)
    {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // Shiro's core security interface, this attribute is required
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // If authentication fails, jump to the configuration of the login page
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        // Permission authentication fails, jump to the specified page
        shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
        // Shiro connection constraint configuration, namely the definition of filter chain
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // Set anonymous access to static resources
        filterChainDefinitionMap.put("/favicon.ico**", "anon");
        filterChainDefinitionMap.put("/logo.png**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/docs/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/ajax/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/eport/**", "anon");
        filterChainDefinitionMap.put("/captcha/captchaImage**", "anon");
        // Exit logout address, shiro to clear the session
        filterChainDefinitionMap.put("/logout", "logout");
        // Access that does not need to be blocked
        filterChainDefinitionMap.put("/login", "anon,captchaValidate");
        // Registration related
        filterChainDefinitionMap.put("/register", "anon,captchaValidate");
        // System permissions list
        // filterChainDefinitionMap.putAll(SpringUtils.getBean(IMenuService.class).selectPermsAll());

        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        filters.put("onlineSession", onlineSessionFilter());
        filters.put("syncOnlineSession", syncOnlineSessionFilter());
        filters.put("captchaValidate", captchaValidateFilter());
        filters.put("kickout", kickoutSessionFilter());
        // If the logout is successful, jump to the specified page
        filters.put("logout", logoutFilter());
        shiroFilterFactoryBean.setFilters(filters);

        // All requests require authentication
        filterChainDefinitionMap.put("/**", "user,kickout,onlineSession,syncOnlineSession");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    /**
     * Custom online user processing filters
     */
    @Bean
    public OnlineSessionFilter onlineSessionFilter()
    {
        OnlineSessionFilter onlineSessionFilter = new OnlineSessionFilter();
        onlineSessionFilter.setLoginUrl(loginUrl);
        return onlineSessionFilter;
    }

    /**
     * Custom online user synchronization filter
     */
    @Bean
    public SyncOnlineSessionFilter syncOnlineSessionFilter()
    {
        SyncOnlineSessionFilter syncOnlineSessionFilter = new SyncOnlineSessionFilter();
        return syncOnlineSessionFilter;
    }

    /**
     * Custom verification code filter
     */
    @Bean
    public CaptchaValidateFilter captchaValidateFilter()
    {
        CaptchaValidateFilter captchaValidateFilter = new CaptchaValidateFilter();
        captchaValidateFilter.setCaptchaEnabled(captchaEnabled);
        captchaValidateFilter.setCaptchaType(captchaType);
        return captchaValidateFilter;
    }

    /**
     * cookie Property setting
     */
    public SimpleCookie rememberMeCookie()
    {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(maxAge * 24 * 60 * 60);
        return cookie;
    }

    /**
     * remember me
     */
    public CookieRememberMeManager rememberMeManager()
    {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("fCq+/xW488hMTCD+cmJ3aQ=="));
        return cookieRememberMeManager;
    }

    /**
     * Restrictions on multiple device logins for the same user
     */
    public KickoutSessionFilter kickoutSessionFilter()
    {
        KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter();
        kickoutSessionFilter.setCacheManager(getEhCacheManager());
        kickoutSessionFilter.setSessionManager(sessionManager());
        // The maximum number of sessions for the same user, the default is -1 unlimited; for example, 2 means that the same user is allowed to log in at most two people at the same time
        kickoutSessionFilter.setMaxSession(maxSession);
        // Whether to log out later, the default is false; that is, the user who logs in the latter kicks out the user who logs in the former; the order of kicking out
        kickoutSessionFilter.setKickoutAfter(kickoutAfter);
        // The address to redirect to after being kicked out;
        kickoutSessionFilter.setKickoutUrl("/login?kickout=1");
        return kickoutSessionFilter;
    }

    /**
     * Integration of thymeleaf template engine and shiro framework
     */
    @Bean
    public ShiroDialect shiroDialect()
    {
        return new ShiroDialect();
    }

    /**
     * Open the Shiro annotation notifier
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            @Qualifier("securityManager") SecurityManager securityManager)
    {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
