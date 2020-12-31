package vn.com.irtech.eport.system.service;

import java.util.List;
 
import vn.com.irtech.eport.system.domain.SysNotice;

/**
 * Notification form Service Interface
 * 
 * @author Trong Hieu
 * @date 2020-11-17
 */
public interface ISysNoticeService 
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
     * @return result
     */
    public int insertSysNotice(SysNotice sysNotice);

    /**
     * Update Notification form
     * 
     * @param sysNotice Notification form
     * @return result
     */
    public int updateSysNotice(SysNotice sysNotice);

    /**
     * Batch Delete Notification form
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteSysNoticeByIds(String ids);

    /**
     * Delete Notification form
     * 
     * @param noticeId Notification formID
     * @return result
     */
    public int deleteSysNoticeById(Long noticeId);

	/**
	 * Select bulletin list
	 * 
	 * @param sysNotice
	 * @return List<SysNotice>
	 */
	public List<SysNotice> selectBulletinList(SysNotice sysNotice);
}
