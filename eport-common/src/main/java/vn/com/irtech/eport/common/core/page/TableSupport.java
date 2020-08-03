package vn.com.irtech.eport.common.core.page;

import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.utils.ServletUtils;

/**
 * Form data processing
 * 
 * @author admin
 */
public class TableSupport
{
    /**
     * Encapsulate the paging object
     */
    public static PageDomain getPageDomain()
    {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(ServletUtils.getParameterToInt(Constants.PAGE_NUM));
        pageDomain.setPageSize(ServletUtils.getParameterToInt(Constants.PAGE_SIZE));
        pageDomain.setOrderByColumn(ServletUtils.getParameter(Constants.ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtils.getParameter(Constants.IS_ASC));
        return pageDomain;
    }

    public static PageDomain buildPageRequest()
    {
        return getPageDomain();
    }
}
