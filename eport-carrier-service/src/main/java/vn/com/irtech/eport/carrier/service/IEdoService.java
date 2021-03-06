package vn.com.irtech.eport.carrier.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.dto.EdoWithoutHouseBillReq;

/**
 * Exchange Delivery OrderService Interface
 * 
 * @author irtech
 * @date 2020-06-26
 */
public interface IEdoService {
	/**
	 * Get Exchange Delivery Order
	 * 
	 * @param id Exchange Delivery OrderID
	 * @return Exchange Delivery Order
	 */
	public Edo selectEdoById(Long id);

	/**
	 * Get first Exchange Delivery Order
	 * 
	 * @param orderNumber
	 * @return
	 */
	public Edo selectFirstEdo(Edo edo);

	/**
	 * Get Exchange Delivery Order List
	 * 
	 * @param edo Exchange Delivery Order
	 * @return Exchange Delivery Order List
	 */
	public List<Edo> selectEdoList(Edo edo);

	/**
	 * Add Exchange Delivery Order
	 * 
	 * @param edo Exchange Delivery Order
	 * @return result
	 */
	public int insertEdo(Edo edo);

	/**
	 * Update Exchange Delivery Order
	 * 
	 * @param edo Exchange Delivery Order
	 * @return result
	 */
	public int updateEdo(Edo edo);

	/**
	 * Batch Delete Exchange Delivery Order
	 * 
	 * @param ids Entity Ids
	 * @return result
	 */
	public int deleteEdoByIds(String ids);

	/**
	 * Delete Exchange Delivery Order
	 * 
	 * @param id Exchange Delivery OrderID
	 * @return result
	 */
	public int deleteEdoById(Long id);

	public Edo checkContainerAvailable(@Param("container") String cont, @Param("billNo") String billNo);

	public String getOpeCodeByBlNo(String blNo);

	public List<Edo> readEdi(String[] text);

	public Long getCountContainerAmountByBlNo(String blNo);

	public File getFolderUploadByTime(String folderLoad);

	// Get Bill No DISTINCT
	public List<Edo> selectEdoListByBillNo(Edo edo);

	public List<Edo> selectOprCode(Edo edo);

	public List<Edo> selectVoyNos(Edo edo);

	public List<Edo> selectVessels(Edo edo);

	/**
     * Update edo by bill of lading and container no
     * 
     * @param edo
     * @return
     */
    public int updateEdoByBlCont(Edo edo);

	/**
	 * Get list Edo without house bill id
	 * @param edo
	 * @return
	 */
	public List<Edo> selectListEdoWithoutHouseBillId(EdoWithoutHouseBillReq edo);
	
	/**
     * Get container amount with order number
     * 
     * @param blNo
     * @param orderNumber
     * @return String
     */
    public int getContainerAmountWithOrderNumber(String blNo, String orderNumber);
    
    /**
     * Get bill of lading by house bill id
     * 
     * @param houseBillId
     * @return String
     */
    public String getBlNoByHouseBillId(Long houseBillId);

	public List<Edo> selectEdoListForReport(Edo edo);
	
	/**
	 * Select list edo with house bill req
	 * 
	 * @param edo
	 * @return List<Edo>
	 */
	public List<Edo> selectListEdoWithHouseBill(EdoWithoutHouseBillReq edo);  

	public Map<String, String> getReportByCarrierGroup(String[] codes);

	public Edo getBillOfLadingInfo(Edo edo);
	
	/**
     * Select list edo by ids
     * 
     * @param ids
     * @return List<Edo>
     */
    public List<Edo> selectEdoByIds(String ids);
    
    /**
     * Update edo by condition
     * 
     * @param edo
     * @return int
     */
    public int updateEdoByCondition(Edo edo);

	/**
	 * Select edo list by house bill
	 * 
	 * @param houseBill
	 * @return List<Edo>
	 */
	public List<Edo> selectEdoListByHouseBill(String houseBill);
}
