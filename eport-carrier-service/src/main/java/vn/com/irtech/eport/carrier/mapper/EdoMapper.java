package vn.com.irtech.eport.carrier.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.dto.EdoWithoutHouseBillReq;

/**
 * Exchange Delivery OrderMapper Interface
 * 
 * @author irtech
 * @date 2020-06-26
 */
public interface EdoMapper 
{
    /**
     * Get Exchange Delivery Order
     * 
     * @param id Exchange Delivery OrderID
     * @return Exchange Delivery Order
     */
    public Edo selectEdoById(Long id);
    
    /**
     * Get first Exchange Delivery Order
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
     * @return Result
     */
    public int insertEdo(Edo edo);

    /**
     * Update Exchange Delivery Order
     * 
     * @param edo Exchange Delivery Order
     * @return Result
     */
    public int updateEdo(Edo edo);

    /**
     * Delete Exchange Delivery Order
     * 
     * @param id Exchange Delivery OrderID
     * @return result
     */
    public int deleteEdoById(Long id);

    /**
     * Batch Delete Exchange Delivery Order
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteEdoByIds(String[] ids);

    public Edo checkContainerAvailable(@Param("container") String cont,@Param("billNo") String billNo);
    
    public String getOpeCodeByBlNo(String blNo);
    
    public Long getCountContainerAmountByBlNo(String blNo);
    
    public List<Edo> selectEdoListByBlNo(String blNo);

    public List<Edo> selectEdoListByBillNo(Edo edo);

    public List<String> selectVesselNo(Edo edo);

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
    public int getContainerAmountWithOrderNumber(@Param("blNo") String blNo, @Param("orderNumber") String orderNumber);
    
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

    public Edo getBillOfLadingInfo(String blNo);
}
