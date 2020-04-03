package vn.com.irtech.eport.edo.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.irtech.eport.edo.mapper.EquipmentDocumentMapper;
import vn.com.irtech.eport.edo.domain.EquipmentDocument;
import vn.com.irtech.eport.edo.service.IEquipmentDocumentService;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * Equipment attached documentService Business Processing
 * 
 * @author irtech
 * @date 2020-04-03
 */
@Service
public class EquipmentDocumentServiceImpl implements IEquipmentDocumentService 
{
    @Autowired
    private EquipmentDocumentMapper equipmentDocumentMapper;

    /**
     * Get Equipment attached document
     * 
     * @param id Equipment attached documentID
     * @return Equipment attached document
     */
    @Override
    public EquipmentDocument selectEquipmentDocumentById(Long id)
    {
        return equipmentDocumentMapper.selectEquipmentDocumentById(id);
    }

    /**
     * Get Equipment attached document List
     * 
     * @param equipmentDocument Equipment attached document
     * @return Equipment attached document
     */
    @Override
    public List<EquipmentDocument> selectEquipmentDocumentList(EquipmentDocument equipmentDocument)
    {
        return equipmentDocumentMapper.selectEquipmentDocumentList(equipmentDocument);
    }

    /**
     * Add Equipment attached document
     * 
     * @param equipmentDocument Equipment attached document
     * @return result
     */
    @Override
    public int insertEquipmentDocument(EquipmentDocument equipmentDocument)
    {
        equipmentDocument.setCreateTime(DateUtils.getNowDate());
        return equipmentDocumentMapper.insertEquipmentDocument(equipmentDocument);
    }

    /**
     * Update Equipment attached document
     * 
     * @param equipmentDocument Equipment attached document
     * @return result
     */
    @Override
    public int updateEquipmentDocument(EquipmentDocument equipmentDocument)
    {
        equipmentDocument.setUpdateTime(DateUtils.getNowDate());
        return equipmentDocumentMapper.updateEquipmentDocument(equipmentDocument);
    }

    /**
     * Delete Equipment attached document By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteEquipmentDocumentByIds(String ids)
    {
        return equipmentDocumentMapper.deleteEquipmentDocumentByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Equipment attached document
     * 
     * @param id Equipment attached documentID
     * @return result
     */
    @Override
    public int deleteEquipmentDocumentById(Long id)
    {
        return equipmentDocumentMapper.deleteEquipmentDocumentById(id);
    }
}
