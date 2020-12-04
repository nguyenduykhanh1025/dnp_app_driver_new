package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.logistic.mapper.CfsHouseBillMapper;
import vn.com.irtech.eport.logistic.domain.CfsHouseBill;
import vn.com.irtech.eport.logistic.service.ICfsHouseBillService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * CFS House BillService Business Processing
 * 
 * @author Trong Hieu
 * @date 2020-11-23
 */
@Service
public class CfsHouseBillServiceImpl implements ICfsHouseBillService 
{
    @Autowired
    private CfsHouseBillMapper cfsHouseBillMapper;

    /**
     * Get CFS House Bill
     * 
     * @param id CFS House BillID
     * @return CFS House Bill
     */
    @Override
    public CfsHouseBill selectCfsHouseBillById(Long id)
    {
        return cfsHouseBillMapper.selectCfsHouseBillById(id);
    }

    /**
     * Get CFS House Bill List
     * 
     * @param cfsHouseBill CFS House Bill
     * @return CFS House Bill
     */
    @Override
    public List<CfsHouseBill> selectCfsHouseBillList(CfsHouseBill cfsHouseBill)
    {
        return cfsHouseBillMapper.selectCfsHouseBillList(cfsHouseBill);
    }

    /**
     * Add CFS House Bill
     * 
     * @param cfsHouseBill CFS House Bill
     * @return result
     */
    @Override
    public int insertCfsHouseBill(CfsHouseBill cfsHouseBill)
    {
        cfsHouseBill.setCreateTime(DateUtils.getNowDate());
        return cfsHouseBillMapper.insertCfsHouseBill(cfsHouseBill);
    }

    /**
     * Update CFS House Bill
     * 
     * @param cfsHouseBill CFS House Bill
     * @return result
     */
    @Override
    public int updateCfsHouseBill(CfsHouseBill cfsHouseBill)
    {
        cfsHouseBill.setUpdateTime(DateUtils.getNowDate());
        return cfsHouseBillMapper.updateCfsHouseBill(cfsHouseBill);
    }

    /**
     * Delete CFS House Bill By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteCfsHouseBillByIds(String ids)
    {
        return cfsHouseBillMapper.deleteCfsHouseBillByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete CFS House Bill
     * 
     * @param id CFS House BillID
     * @return result
     */
    @Override
    public int deleteCfsHouseBillById(Long id)
    {
        return cfsHouseBillMapper.deleteCfsHouseBillById(id);
    }
}
