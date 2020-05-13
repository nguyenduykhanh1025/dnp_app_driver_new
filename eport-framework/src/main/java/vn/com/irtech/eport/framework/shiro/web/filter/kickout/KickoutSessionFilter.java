package vn.com.irtech.eport.framework.shiro.web.filter.kickout;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.com.irtech.eport.common.constant.ShiroConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.utils.ServletUtils;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.system.domain.SysUser;

/**
 * Login account control filter
 * 
 * @author admin
 */
public class KickoutSessionFilter extends AccessControlFilter
{
    private final static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Maximum sessions of the same user
     **/
    private int maxSession = -1;

    /**
     * Kick out previously logged in / post logged in user default false kick out previously logged in user
     **/
    private Boolean kickoutAfter = false;

    /**
     * Address after kicking
     **/
    private String kickoutUrl;

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o)
            throws Exception
    {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered() || maxSession == -1)
        {
            // If there is no login or the maximum number of user sessions is -1, proceed directly to the subsequent process
            return true;
        }
        try
        {
            Session session = subject.getSession();
            // Currently logged in user
            SysUser user = ShiroUtils.getSysUser();
            String loginName = user.getLoginName();
            Serializable sessionId = session.getId();

            // Read cache users and deposit without
            Deque<Serializable> deque = cache.get(loginName);
            if (deque == null)
            {
                // Initialize the queue
                deque = new ArrayDeque<Serializable>();
            }

            // If the sessionId is not in the queue and the user is not kicked out; put it in the queue
            if (!deque.contains(sessionId) && session.getAttribute("kickout") == null)
            {
                // Store sessionId in the queue
                deque.push(sessionId);
                // Cache the user's sessionId queue
                cache.put(loginName, deque);
            }

            // If the number of sessionId in the queue exceeds the maximum number of sessions, start kicking
            while (deque.size() > maxSession)
            {
                Serializable kickoutSessionId = null;
                // Whether to log out after logging in, the default is false; that is, the user logging in the latter kicks out the user logging in the former;
                if (kickoutAfter)
                {
                    // Kick the latter
                    kickoutSessionId = deque.removeFirst();
                }
                else
                {
                    // Kick out the former
                    kickoutSessionId = deque.removeLast();
                }
                // After kicking out, update the cache queue
                cache.put(loginName, deque);

                try
                {
                    // Get the session object of the kicked sessionId
                    Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                    if (null != kickoutSession)
                    {
                        // Setting the kickout attribute of the session means kicked out
                        kickoutSession.setAttribute("kickout", true);
                    }
                }
                catch (Exception e)
                {
                    // In the face of anomalies, we choose to ignore
                }
            }

            // If kicked out, (the former or the latter) exit directly and redirect to the address after kicked out
            if ((Boolean) session.getAttribute("kickout") != null && (Boolean) session.getAttribute("kickout") == true)
            {
                // sign out
                subject.logout();
                saveRequest(request);
                return isAjaxResponse(request, response);
            }
            return true;
        }
        catch (Exception e)
        {
            return isAjaxResponse(request, response);
        }
    }

    private boolean isAjaxResponse(ServletRequest request, ServletResponse response) throws IOException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (ServletUtils.isAjaxRequest(req))
        {
            AjaxResult ajaxResult = AjaxResult.error("You are logged in elsewhere, please change your password or log in again");
            ServletUtils.renderString(res, objectMapper.writeValueAsString(ajaxResult));
        }
        else
        {
            WebUtils.issueRedirect(request, response, kickoutUrl);
        }
        return false;
    }

    public void setMaxSession(int maxSession)
    {
        this.maxSession = maxSession;
    }

    public void setKickoutAfter(boolean kickoutAfter)
    {
        this.kickoutAfter = kickoutAfter;
    }

    public void setKickoutUrl(String kickoutUrl)
    {
        this.kickoutUrl = kickoutUrl;
    }

    public void setSessionManager(SessionManager sessionManager)
    {
        this.sessionManager = sessionManager;
    }

    // Set the cache key prefix
    public void setCacheManager(CacheManager cacheManager)
    {
        // Must be the same as the cache name in the ehcache cache configuration
        this.cache = cacheManager.getCache(ShiroConstants.SYS_USERCACHE);
    }
}
