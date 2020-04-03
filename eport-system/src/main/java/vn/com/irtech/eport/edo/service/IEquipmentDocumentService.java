package vn.com.irtech.eport.edo.service;

import java.util.List;
import vn.com.irtech.eport.edo.domain.EquipmentDocument;

/**
 * Equipment attached documentService Interface
 * 
 * @author irtech
 * @date 2020-04-03
 */
public interface IEquipmentDocumentService 
{
    /**
     * Get Equipment attached document
     * 
     * @param id Equipment attached documentID
     * @return Equipment attached document
     */
    public EquipmentDocument selectEquipmentDocumentById(Long id);

    /**
     * Get Equipment attached document List
     * 
     * @param equipmentDocument Equipment attached document
     * @return Equipment attached document List
     */
    public List<EquipmentDocument> selectEquipmentDocumentList(EquipmentDocument equipmentDocument);

    /**
     * Add Equipment attached document
     * 
     * @param equipmentDocument Equipment attached document
     * @return result
     */
    public int insertEquipmentDocument(EquipmentDocument equipmentDocument);

    /**
     * Update Equipment attached document
     * 
     * @param equipmentDocument Equipment attached document
     * @return result
     */
    public int updateEquipmentDocument(EquipmentDocument equipmentDocument);

    /**
     * Batch Delete Equipment attached document
     * 
     * @param ids Entity Ids
     * @return result
     */
    public int deleteEquipmentDocumentByIds(String ids);

    /**
     * Delete Equipment attached document
     * 
     * @param id Equipment attached documentID
     * @return result
     */
    public int deleteEquipmentDocumentById(Long id);
}
