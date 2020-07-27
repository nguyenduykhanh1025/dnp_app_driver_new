package vn.com.irtech.eport.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.json.JSON;
import vn.com.irtech.eport.common.json.JSONObject;
import vn.com.irtech.eport.common.utils.http.HttpUtils;

/**
 * 获取地址类
 * 
 * @author admin
 */
public class AddressUtils
{
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IP地址查询
    public static final String IP_URL = "http://ip-api.com/json/";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip)
    {
        String address = UNKNOWN;
        // 内网不查询
        if (IpUtils.internalIp(ip))
        {
            return "Intranet IP";
        }
        if (Global.isAddressEnabled())
        {

            try
            {
                String rspStr = HttpUtils.sendGet(IP_URL + "/" + ip, "", Constants.GBK);
                if (StringUtils.isEmpty(rspStr))
                {
                    log.error("Get location error {}", ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSONObject.parseObject(rspStr);
                String region = obj.getString("country");
                String city = obj.getString("city");
                return String.format("%s, %s", region, city);
            }
            catch (Exception e)
            {
                log.error("Get geolocation exception{}", ip);
            }
        }
        return address;
    }
}
