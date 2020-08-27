package vn.com.irtech.eport.carrier.service;

import java.util.List;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.domain.EdoHouseBill;
import vn.com.irtech.eport.carrier.dto.HouseBillRes;
import vn.com.irtech.eport.carrier.dto.HouseBillSearchReq;

/**
 * Master BillService Interface
 * 
 * @author irtech
 * @date 2020-08-10
 */
public interface IEdoHouseBillService 
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
     * @return result
     */
    public int insertEdoHouseBill(EdoHouseBill edoHouseBill);

    /**
     * Update Master Bill
     * 
     * @param edoHouseBill Master Bill
     * @return result
     */
    public int updateEdoHouseBill(EdoHouseBill edoHouseBill);

    /**
     * Batch Delete Master Bill
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteEdoHouseBillByIds(String ids);

    /**
     * Delete Master Bill
     * 
     * @param id Master BillID
     * @return result
     */
    public int deleteEdoHouseBillById(Long id);

    /**
     * Check house bill is exist
     * @param edoHouseBill
     * @return
     */
    public Integer checkExistHouseBill(EdoHouseBill edoHouseBill);

    /**
     * insert edo  house bill
     * @param edos
     * @param houseBill
     * @param consignee2
     * @param user
     * @return
     */
    public int insertListEdoHouseBill(List<Edo> edos, String houseBill, String consignee2, String taxCode, String orderNumber, Long logisticGroupId, Long logisticAccountId, String creator);

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
     * @param shipment
     * @return String
     */
    public int getContainerAmountWithOrderNumber(String blNo, String orderNumber);
    
    /**
     * Select house bill for shipment
     * 
     * @param blNo
     * @return List<EdoHouseBill>
     */
    public List<EdoHouseBill> selectHouseBillForShipment(String blNo);
}
