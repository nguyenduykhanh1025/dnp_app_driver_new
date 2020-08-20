package vn.com.irtech.eport.logistic.service;

import vn.com.irtech.eport.logistic.dto.CustomsCheckResultDto;

/**
 * Custom Check Service (using API)
 * 
 * @author GiapHD
 *
 */
public interface ICustomCheckService {

	/**
	 * Check Customer status for container using ACCISS
	 * 
	 * @param userVoy Voyage No
	 * @param cntrNo  Container No
	 * @return Custom Number List
	 */
	public CustomsCheckResultDto checkCustomStatus(String userVoy, String cntrNo);

}
