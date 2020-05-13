package vn.com.irtech.eport.framework.shiro.web.filter.online;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.common.constant.ShiroConstants;
import vn.com.irtech.eport.common.enums.OnlineStatus;
import vn.com.irtech.eport.framework.shiro.session.OnlineSession;
import vn.com.irtech.eport.framework.shiro.session.OnlineSessionDAO;
import vn.com.irtech.eport.framework.util.ShiroUtils;

/**
 * Custom access control
 * 
 * @author admin
 */
public class OnlineSessionFilter extends AccessControlFilter
{
    /**
     * Redirected address after forced exit
     */
    @Value("${shiro.user.loginUrl}")
    private String loginUrl;

    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    /**
     * Indicates whether access is allowed; mappedValue is the part of the interceptor parameter in the [urls] configuration. If access is allowed, return true, otherwise false;
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception
    {
        Subject subject = getSubject(request, response);
        if (subject == null || subject.getSession() == null)
        {
            return true;
        }
        Session session = onlineSessionDAO.readSession(subject.getSession().getId());
        if (session != null && session instanceof OnlineSession)
        {
            OnlineSession onlineSession = (OnlineSession) session;
            request.setAttribute(ShiroConstants.ONLINE_SESSION, onlineSession);
            // Set the user object in
            boolean isGuest = onlineSession.getUserId() == null || onlineSession.getUserId() == 0L;
            if (isGuest == true)
            {
                LogisticAccount user = ShiroUtils.getSysUser();
                if (user != null)
                {
                    onlineSession.setUserId(user.getId());
                    onlineSession.setLoginName(user.getEmail());
					onlineSession.setAvatar("");
                    onlineSession.setDeptName("Logistic");
                    onlineSession.markAttributeChanged();
                }
            }

            if (onlineSession.getStatus() == OnlineStatus.off_line)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Indicates whether it has been processed when the access is denied; if it returns true, it needs to continue processing; if it returns false, it means that the interceptor instance has been processed, and it will simply return.
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        Subject subject = getSubject(request, response);
        if (subject != null)
        {
            subject.logout();
        }
        saveRequestAndRedirectToLogin(request, response);
        return false;
    }

    // Jump to login page
    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException
    {
        WebUtils.issueRedirect(request, response, loginUrl);
    }
}
