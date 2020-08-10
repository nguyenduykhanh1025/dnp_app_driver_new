package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
