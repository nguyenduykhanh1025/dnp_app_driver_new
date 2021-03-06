package vn.com.irtech.eport.web.controller.om;

import vn.com.irtech.eport.common.core.controller.BaseController;
import vn.com.irtech.eport.framework.util.ShiroUtils;
import vn.com.irtech.eport.system.domain.SysUser;

public class OmBaseController extends BaseController {

    public SysUser getUser() {
        return ShiroUtils.getSysUser();
    }

    public Long getUserId() {
        return ShiroUtils.getSysUser().getUserId();
    }
    
}