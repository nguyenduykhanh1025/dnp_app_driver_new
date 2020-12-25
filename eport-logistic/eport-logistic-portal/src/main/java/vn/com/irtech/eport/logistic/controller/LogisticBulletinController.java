/**
 * 
 */
package vn.com.irtech.eport.logistic.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vn.com.irtech.eport.common.constant.EportConstants;
import vn.com.irtech.eport.common.core.domain.AjaxResult;
import vn.com.irtech.eport.common.core.page.PageAble;
import vn.com.irtech.eport.common.core.page.TableDataInfo;
import vn.com.irtech.eport.system.domain.SysNotice;
import vn.com.irtech.eport.system.service.ISysNoticeService;

/**
 * @author Trong Hieu
 *
 */
@Controller
@RequestMapping("/logistic/bulletin")
public class LogisticBulletinController extends LogisticBaseController {

	@Autowired
	private ISysNoticeService sysNoticeService;

	@PostMapping("/list")
	@ResponseBody
	public AjaxResult getBulletinList(@RequestBody PageAble<SysNotice> params) {
		startPage(params.getPageNum(), params.getPageSize(), params.getOrderBy());
		SysNotice sysNoticeParam = params.getData();
		if (sysNoticeParam == null) {
			sysNoticeParam = new SysNotice();
		}
		sysNoticeParam.setActive(EportConstants.BULLETIN_RELEASE);
		sysNoticeParam.setNoticeType(EportConstants.NOTICE_TYPE_LOGISTIC);
		List<SysNotice> sysNotices = sysNoticeService.selectBulletinList(sysNoticeParam);
		if (CollectionUtils.isEmpty(sysNotices)) {
			sysNotices = new ArrayList<>();
		}
		TableDataInfo tableDataInfo = getDataTable(sysNotices);
		AjaxResult ajaxResult = AjaxResult.success();
		ajaxResult.put("bulletins", tableDataInfo);
		return ajaxResult;
	}

}
