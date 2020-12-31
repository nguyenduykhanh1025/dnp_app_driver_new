package vn.com.irtech.eport.system.mapper;

import java.util.List;
 
import vn.com.irtech.eport.system.domain.SysNotice;

/**
 * Notification form Mapper Interface
 * 
 * @author Trong Hieu
 * @date 2020-11-17
 */
public interface SysNoticeMapper 
{
    /**
     * Get Notification form
     * 
     * @param noticeId Notification formID
     * @return Notification form
     */
    public SysNotice selectSysNoticeById(Long noticeId);

    /**
     * Get Notification form List
     * 
     * @param sysNotice Notification form
     * @return Notification form List
     */
    public List<SysNotice> selectSysNoticeList(SysNotice sysNotice); 
    

    /**
     * Add Notification form
     * 
     * @param sysNotice Notification form
     * @return Result
     */
    public int insertSysNotice(SysNotice sysNotice);

    /**
     * Update Notification form
     * 
     * @param sysNotice Notification form
     * @return Result
     */
    public int updateSysNotice(SysNotice sysNotice);

    /**
     * Delete Notification form
     * 
     * @param noticeId Notification formID
     * @return result
     */
    public int deleteSysNoticeById(Long noticeId);

    /**
     * Batch Delete Notification form
     * 
     * @param noticeIds IDs
     * @return result
     */
    public int deleteSysNoticeByIds(String[] noticeIds);

	/**
	 * Select bulletin list
	 * 
	 * @param sysNotice
	 * @return List<SysNotice>
	 */
	public List<SysNotice> selectBulletinList(SysNotice sysNotice);
}
