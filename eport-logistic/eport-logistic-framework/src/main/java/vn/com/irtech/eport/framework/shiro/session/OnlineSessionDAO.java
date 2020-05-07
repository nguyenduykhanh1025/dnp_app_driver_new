package vn.com.irtech.eport.framework.shiro.session;

import java.io.Serializable;
import java.util.Date;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import vn.com.irtech.eport.common.enums.OnlineStatus;
import vn.com.irtech.eport.framework.manager.AsyncManager;
import vn.com.irtech.eport.framework.manager.factory.AsyncFactory;
import vn.com.irtech.eport.framework.shiro.service.SysShiroService;

/**
 * @author admin
 */
public class OnlineSessionDAO extends EnterpriseCacheSessionDAO
{
    @Value("${shiro.session.dbSyncPeriod}")
    private int dbSyncPeriod;

    private static final String LAST_SYNC_DB_TIMESTAMP = OnlineSessionDAO.class.getName() + "LAST_SYNC_DB_TIMESTAMP";

    @Autowired
    private SysShiroService sysShiroService;

    public OnlineSessionDAO()
    {
        super();
    }

    // public OnlineSessionDAO(long expireTime)
    // {
    //     super();
    // }

    @Override
    protected Session doReadSession(Serializable sessionId)
    {
        return sysShiroService.getSession(sessionId);
    }

    @Override
    public void update(Session session) throws UnknownSessionException
    {
        super.update(session);
    }

    public void syncToDb(OnlineSession onlineSession)
    {
        Date lastSyncTimestamp = (Date) onlineSession.getAttribute(LAST_SYNC_DB_TIMESTAMP);
        if (lastSyncTimestamp != null)
        {
            boolean needSync = true;
            long deltaTime = onlineSession.getLastAccessTime().getTime() - lastSyncTimestamp.getTime();
            if (deltaTime < dbSyncPeriod * 60 * 1000)
            {
                needSync = false;
            }
            boolean isGuest = onlineSession.getUserId() == null || onlineSession.getUserId() == 0L;

            if (!isGuest == false && onlineSession.isAttributeChanged())
            {
                needSync = true;
            }

            if (!needSync)
            {
                return;
            }
        }
        onlineSession.setAttribute(LAST_SYNC_DB_TIMESTAMP, onlineSession.getLastAccessTime());
        if (onlineSession.isAttributeChanged())
        {
            onlineSession.resetAttributeChanged();
        }
        AsyncManager.me().execute(AsyncFactory.syncSessionToDb(onlineSession));
    }

    @Override
    protected void doDelete(Session session)
    {
        OnlineSession onlineSession = (OnlineSession) session;
        if (null == onlineSession)
        {
            return;
        }
        onlineSession.setStatus(OnlineStatus.off_line);
        sysShiroService.deleteSession(onlineSession);
    }
}
