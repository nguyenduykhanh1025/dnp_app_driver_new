package vn.com.irtech.eport.logistic.service;

import com.alibaba.fastjson.JSONObject;

public interface INapasApiService {
    
	/**
	 * 
	 * @return
	 */
    public JSONObject getAccessToken();
    
    /**
     * 
     * @param clienIp
     * @param deviceId
     * @param orderId
     * @param amount
     * @return
     */
    public JSONObject getDataKey(String clienIp, String deviceId, String orderId, Long amount, String token);
    
}