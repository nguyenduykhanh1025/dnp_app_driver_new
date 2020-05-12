package vn.com.irtech.eport.framework.shiro.web.filter.sync;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import vn.com.irtech.eport.common.constant.ShiroConstants;
import vn.com.irtech.eport.framework.shiro.session.OnlineSession;
import vn.com.irtech.eport.framework.shiro.session.OnlineSessionDAO;

/**
 * Synchronize session data to Db
 * 
 * @author admin
 */
public class SyncOnlineSessionFilter extends PathMatchingFilter
{
    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    /**
     * Synchronize session data to DB One request can be synchronized at most once to prevent excessive processing. Need to be placed before Shiro filter
     */
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception
    {
        OnlineSession session = (OnlineSession) request.getAttribute(ShiroConstants.ONLINE_SESSION);
        // If the session is stopped, it will not be synchronized
        // session stop time, if stopTimestamp is not null, it means it has been stopped
        if (session != null && session.getUserId() != null && session.getStopTimestamp() == null)
        {
            onlineSessionDAO.syncToDb(session);
        }
        return true;
    }
}
