package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;
import vn.com.irtech.eport.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.com.irtech.eport.logistic.mapper.ShipmentMapper;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.carrier.mapper.CarrierGroupMapper;
import vn.com.irtech.eport.carrier.mapper.EdoMapper;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.core.text.Convert;

/**
 * ShipmentService Business Processing
 * 
 * @author admin
 * @date 2020-05-07
 */
@Service
public class ShipmentServiceImpl implements IShipmentService {
    @Autowired
    private ShipmentMapper shipmentMapper;

    @Autowired
    private EdoMapper edoMapper;

    @Autowired
    private CarrierGroupMapper carrierGroupMapper;

    /**
     * Get Shipment
     * 
     * @param id ShipmentID
     * @return Shipment
     */
    @Override
    public Shipment selectShipmentById(Long id) {
        return shipmentMapper.selectShipmentById(id);
    }

    /**
     * Get Shipment List
     * 
     * @param shipment Shipment
     * @return Shipment
     */
    @Override
    public List<Shipment> selectShipmentList(Shipment shipment) {
        return shipmentMapper.selectShipmentList(shipment);
    }

    /**
     * Add Shipment
     * 
     * @param shipment Shipment
     * @return result
     */
    @Override
    public int insertShipment(Shipment shipment) {
        shipment.setCreateTime(DateUtils.getNowDate());
        return shipmentMapper.insertShipment(shipment);
    }

    /**
     * Update Shipment
     * 
     * @param shipment Shipment
     * @return result
     */
    @Override
    public int updateShipment(Shipment shipment) {
        shipment.setUpdateTime(DateUtils.getNowDate());
        return shipmentMapper.updateShipment(shipment);
    }

    /**
     * Delete Shipment By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteShipmentByIds(String ids) {
        return shipmentMapper.deleteShipmentByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Shipment
     * 
     * @param id ShipmentID
     * @return result
     */
    @Override
    public int deleteShipmentById(Long id) {
        return shipmentMapper.deleteShipmentById(id);
    }

    @Override
    public int checkBillBookingNoUnique(Shipment shipment) {
        return shipmentMapper.checkBillBookingNoUnique(shipment);
    }

    @Override
    public String getOpeCodeByBlNo(String blNo) {
        return edoMapper.getOpeCodeByBlNo(blNo);
    }

    @Override
    public Long getCountContainerAmountByBlNo(String blNo) {
        return edoMapper.getCountContainerAmountByBlNo(blNo);
    }

    @Override
    public Shipment getOpeCodeCatosByBlNo(String blNo) {
        String url = Global.getApiUrl() + "/shipmentDetail/getOpeCodeCatosByBlNo/" + blNo;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, Shipment.class);
    }

    @Override
    public String getEdoFlgByOpeCode(String opeCode) {
        return carrierGroupMapper.getDoTypeByOpeCode(opeCode);
    }

    @Override
    public List<Shipment> selectShipmentListForOm(Shipment shipment) {
        return shipmentMapper.selectShipmentListForOm(shipment);
    }

    @Override
    public List<Shipment> getShipmentListForAssign(Shipment shipment) {
        return shipmentMapper.getShipmentListForAssign(shipment);
    }

}
