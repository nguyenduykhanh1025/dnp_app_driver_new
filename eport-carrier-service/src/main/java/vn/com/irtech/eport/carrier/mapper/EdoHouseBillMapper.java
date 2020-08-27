package vn.com.irtech.eport.carrier.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import vn.com.irtech.eport.carrier.domain.EdoHouseBill;
import vn.com.irtech.eport.carrier.dto.HouseBillRes;
import vn.com.irtech.eport.carrier.dto.HouseBillSearchReq;

/**
 * Master BillMapper Interface
 * 
 * @author irtech
 * @date 2020-08-10
 */
public interface EdoHouseBillMapper 
{
    /**
     * Get Master Bill
     * 
     * @param id Master BillID
     * @return Master Bill
     */
    public EdoHouseBill selectEdoHouseBillById(Long id);

    /**
     * Select first edo house bill
     * @param edoHouseBill
     * @return
     */
    public EdoHouseBill selectFirstEdoHouseBill(EdoHouseBill edoHouseBill);

    /**
     * Get Master Bill List
     * 
     * @param edoHouseBill Master Bill
     * @return Master Bill List
     */
    public List<EdoHouseBill> selectEdoHouseBillList(EdoHouseBill edoHouseBill);

    /**
     * Add Master Bill
     * 
     * @param edoHouseBill Master Bill
     * @return Result
     */
    public int insertEdoHouseBill(EdoHouseBill edoHouseBill);

    /**
     * Update Master Bill
     * 
     * @param edoHouseBill Master Bill
     * @return Result
     */
    public int updateEdoHouseBill(EdoHouseBill edoHouseBill);

    /**
     * Delete Master Bill
     * 
     * @param id Master BillID
     * @return result
     */
    public int deleteEdoHouseBillById(Long id);

    /**
     * Batch Delete Master Bill
     * 
     * @param ids IDs
     * @return result
     */
    public int deleteEdoHouseBillByIds(String[] ids);

    /**
     * Check house bill is exist
     * @param edoHouseBill
     * @return
     */
    public Integer checkExistHouseBill(EdoHouseBill edoHouseBill);

    /**
     * select list house bill response
     * @param houseBillSearchReq
     * @return
     */
    public List<HouseBillRes> selectListHouseBillRes(HouseBillSearchReq houseBillSearchReq);
    
    /**
     * Get Edo House Bill By Bl No
     * 
     * @param blNo
     * @return EdoHouseBill
     */
    public EdoHouseBill getEdoHouseBillByBlNo(String blNo);
    
    /**
     * Get container amount with order number
     * 
     * @param blNo
     * @param orderNumber
     * @return String
     */
    public int getContainerAmountWithOrderNumber(@Param("blNo") String blNo, @Param("orderNumber") String orderNumber);
    
    /**
     * Select house bill for shipment
     * 
     * @param blNo
     * @return List<EdoHouseBill>
     */
    public List<EdoHouseBill> selectHouseBillForShipment(String blNo);
}
