package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.dto.HouseBillRes;
import vn.com.irtech.eport.carrier.dto.HouseBillSearchReq;
import vn.com.irtech.eport.carrier.dto.SeparateHouseBillReq;
import vn.com.irtech.eport.carrier.mapper.EdoMapper;
import vn.com.irtech.eport.carrier.service.IEdoService;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.domain.LogisticAccount;
import vn.com.irtech.eport.logistic.mapper.EdoHouseBillMapper;
import vn.com.irtech.eport.logistic.domain.EdoHouseBill;
import vn.com.irtech.eport.logistic.service.IEdoHouseBillService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Master BillService Business Processing
 * 
 * @author irtech
 * @date 2020-08-10
 */
@Service
public class EdoHouseBillServiceImpl implements IEdoHouseBillService 
{
    @Autowired
    private EdoHouseBillMapper edoHouseBillMapper;

    @Autowired
    private EdoMapper edoMapper;

    /**
     * Get Master Bill
     * 
     * @param id Master BillID
     * @return Master Bill
     */
    @Override
    public EdoHouseBill selectEdoHouseBillById(Long id)
    {
        return edoHouseBillMapper.selectEdoHouseBillById(id);
    }

    /**
     * Select first edo house bill
     * @param edoHouseBill
     * @return
     */
    @Override
    public EdoHouseBill selectFirstEdoHouseBill(EdoHouseBill edoHouseBill) {
        return edoHouseBillMapper.selectFirstEdoHouseBill(edoHouseBill);
    }

    /**
     * Get Master Bill List
     * 
     * @param edoHouseBill Master Bill
     * @return Master Bill
     */
    @Override
    public List<EdoHouseBill> selectEdoHouseBillList(EdoHouseBill edoHouseBill)
    {
        return edoHouseBillMapper.selectEdoHouseBillList(edoHouseBill);
    }

    /**
     * Add Master Bill
     * 
     * @param edoHouseBill Master Bill
     * @return result
     */
    @Override
    public int insertEdoHouseBill(EdoHouseBill edoHouseBill)
    {
        edoHouseBill.setCreateTime(DateUtils.getNowDate());
        return edoHouseBillMapper.insertEdoHouseBill(edoHouseBill);
    }

    /**
     * Update Master Bill
     * 
     * @param edoHouseBill Master Bill
     * @return result
     */
    @Override
    public int updateEdoHouseBill(EdoHouseBill edoHouseBill)
    {
        edoHouseBill.setUpdateTime(DateUtils.getNowDate());
        return edoHouseBillMapper.updateEdoHouseBill(edoHouseBill);
    }

    /**
     * Delete Master Bill By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteEdoHouseBillByIds(String ids)
    {
        return edoHouseBillMapper.deleteEdoHouseBillByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Master Bill
     * 
     * @param id Master BillID
     * @return result
     */
    @Override
    public int deleteEdoHouseBillById(Long id)
    {
        return edoHouseBillMapper.deleteEdoHouseBillById(id);
    }

    /**
     * Check house bill is exist
     * @param edoHouseBill
     * @return
     */
    @Override
    public Integer checkExistHouseBill(EdoHouseBill edoHouseBill) {
        return edoHouseBillMapper.checkExistHouseBill(edoHouseBill);
    }

    /**
     * insert edo  house bill
     * @param edos
     * @param houseBill
     * @param consignee2
     * @param user
     * @return
     */
    @Override
    public int insertListEdoHouseBill(List<Edo> edos, String houseBill, String consignee2, LogisticAccount user) {
        if (edos == null){
            return 0;
        }

        for (Edo edo : edos){
            EdoHouseBill edoHouseBill = new EdoHouseBill();
            edoHouseBill.setLogisticGroupId(user.getGroupId());
            edoHouseBill.setLogisticAccountId(user.getId());
            edoHouseBill.setEdoId(edo.getId());
            edoHouseBill.setBillOfLading(edo.getBillOfLading());
            edoHouseBill.setOrderNumber(edo.getOrderNumber());
            edoHouseBill.setMasterBillNo(edo.getBillOfLading());
            edoHouseBill.setConsignee(edo.getConsignee());
            edoHouseBill.setHouseBillNo(houseBill);
            edoHouseBill.setConsignee2(consignee2);
            edoHouseBill.setContainerNumber(edo.getContainerNumber());
            edoHouseBill.setSztp(edo.getSztp());
            edoHouseBill.setVessel(edo.getVessel());
            edoHouseBill.setVoyNo(edo.getVoyNo());
            edoHouseBill.setCreateBy(user.getUserName());
            edoHouseBill.setCarrierCode(edo.getCarrierCode());

            edoHouseBillMapper.insertEdoHouseBill(edoHouseBill);

            Edo edoUpdate = new Edo();
            edoUpdate.setId(edo.getId());
            edoUpdate.setUpdateBy(user.getUserName());
            edoUpdate.setHouseBillId(edoHouseBill.getId());
            edoMapper.updateEdo(edoUpdate);
        }
        return 1;
    }

    /**
     * select list house bill response
     * @param houseBillSearchReq
     * @return
     */
    @Override
    public List<HouseBillRes> selectListHouseBillRes(HouseBillSearchReq houseBillSearchReq) {
        return edoHouseBillMapper.selectListHouseBillRes(houseBillSearchReq);
    }

}
