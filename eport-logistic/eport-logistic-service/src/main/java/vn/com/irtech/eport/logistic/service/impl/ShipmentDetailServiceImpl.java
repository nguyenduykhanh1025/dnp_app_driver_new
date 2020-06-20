package vn.com.irtech.eport.logistic.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.collections4.QueueUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.logistic.domain.QueueOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ShipmentWaitExec;
import vn.com.irtech.eport.logistic.mapper.ShipmentDetailMapper;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;

/**
 * Shipment DetailsService Business Processing
 * 
 * @author admin
 * @date 2020-05-07
 */
@Service
public class ShipmentDetailServiceImpl implements IShipmentDetailService 
{
    @Autowired
    private ShipmentDetailMapper shipmentDetailMapper;

    class BayComparator implements Comparator<ShipmentDetail> {
		public int compare(ShipmentDetail shipmentDetail1, ShipmentDetail shipmentDetail2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return shipmentDetail1.getBay().compareTo(shipmentDetail2.getBay());
		}
    }

    class SztpComparator implements Comparator<ShipmentDetail> {
		public int compare(ShipmentDetail shipmentDetail1, ShipmentDetail shipmentDetail2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return shipmentDetail1.getSztp().compareTo(shipmentDetail2.getSztp());
		}
    }

    /**
     * Get Shipment Details
     * 
     * @param id Shipment DetailsID
     * @return Shipment Details
     */
    @Override
    public ShipmentDetail selectShipmentDetailById(Long id)
    {
        return shipmentDetailMapper.selectShipmentDetailById(id);
    }

    /**
     * Get Shipment Details List
     * 
     * @param shipmentDetail Shipment Details
     * @return Shipment Details
     */
    @Override
    public List<ShipmentDetail> selectShipmentDetailList(ShipmentDetail shipmentDetail)
    {
        return shipmentDetailMapper.selectShipmentDetailList(shipmentDetail);
    }

    /**
     * Add Shipment Details
     * 
     * @param shipmentDetail Shipment Details
     * @return result
     */
    @Override
    public int insertShipmentDetail(ShipmentDetail shipmentDetail)
    {
        shipmentDetail.setCreateTime(DateUtils.getNowDate());
        return shipmentDetailMapper.insertShipmentDetail(shipmentDetail);
    }

    /**
     * Update Shipment Details
     * 
     * @param shipmentDetail Shipment Details
     * @return result
     */
    @Override
    public int updateShipmentDetail(ShipmentDetail shipmentDetail)
    {
        shipmentDetail.setUpdateTime(DateUtils.getNowDate());
        return shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
    }

    /**
     * Delete Shipment Details By ID
     * 
     * @param ids Entity ID
     * @return result
     */
    @Override
    public int deleteShipmentDetailByIds(String ids)
    {
        return shipmentDetailMapper.deleteShipmentDetailByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Shipment Details
     * 
     * @param id Shipment DetailsID
     * @return result
     */
    @Override
    public int deleteShipmentDetailById(Long id)
    {
        return shipmentDetailMapper.deleteShipmentDetailById(id);
    }

    @Override
    public List<ShipmentDetail> selectShipmentDetailByIds(String ids) {
        return shipmentDetailMapper.selectShipmentDetailByIds(Convert.toStrArray(ids));
    }

    @Override
    public List<ShipmentDetail> selectShipmentDetailByBlno(String Blno)
    {
        return shipmentDetailMapper.selectShipmentDetailByBlno(Blno);
    }

    @Override
    public List<String> getBlListByDoStatus(String keyString)
    {
        return shipmentDetailMapper.getBlListByDoStatus(keyString);
    }

    @Override
    public List<String> getBlListByPaymentStatus(String keyString)
    {
        return shipmentDetailMapper.getBlListByPaymentStatus(keyString);
    }

    @Override
    public List<String> getBlLists(String keyString)
    {
        return shipmentDetailMapper.getBlLists(keyString);
    }

    public long countShipmentDetailList(ShipmentDetail shipmentDetail)
    {
        return shipmentDetailMapper.countShipmentDetailList(shipmentDetail);
    }

    public int updateStatusShipmentDetail(ShipmentDetail shipmentDetail)
    {
        return shipmentDetailMapper.updateStatusShipmentDetail(shipmentDetail);
    }
    /**
     * Select list shipment detail wait robot execute or robot can't be execute, group by shipment id
     * @return result
     */
	@Override
	public List<ShipmentWaitExec> selectListShipmentWaitExec() {
		return shipmentDetailMapper.selectListShipmentWaitExec();
    }
    
    @Override
    public List<ShipmentDetail[][]> getContPosition(List<LinkedHashMap> coordinateOfList, List<ShipmentDetail> shipmentDetails) {
        // simulating the location of container in da nang port, mapping to matrix
        List<ShipmentDetail[][]> bayList = new ArrayList<>();
        for (ShipmentDetail shipmentDetail : shipmentDetails) {
            for (int i=0; i<coordinateOfList.size(); i++) {
                if (shipmentDetail.getContainerNo().equals(coordinateOfList.get(i).get("containerNo"))) {
                    shipmentDetail.setBay(coordinateOfList.get(i).get("bay").toString());
                    shipmentDetail.setRow(Integer.parseInt(coordinateOfList.get(i).get("row").toString()));
                    shipmentDetail.setTier(Integer.parseInt(coordinateOfList.get(i).get("tier").toString()));
                    coordinateOfList.remove(i);
                    i--;
                }
            }
        }

        for (int i=0; i<coordinateOfList.size(); i++) {
            ShipmentDetail shipmentDetail2 = new ShipmentDetail();
            shipmentDetail2.setBay(coordinateOfList.get(i).get("bay").toString());
            shipmentDetail2.setRow(Integer.parseInt(coordinateOfList.get(i).get("row").toString()));
            shipmentDetail2.setTier(Integer.parseInt(coordinateOfList.get(i).get("tier").toString()));
            shipmentDetails.add(shipmentDetail2);
        }

        // Mapping container to matrix by location row, tier, bay
        Collections.sort(shipmentDetails, new BayComparator());
        ShipmentDetail[][] shipmentDetailMatrix = new ShipmentDetail[5][6];
        String currentBay = shipmentDetails.get(0).getBay();
        for (ShipmentDetail shipmentDetail : shipmentDetails) {
            if (currentBay.equals(shipmentDetail.getBay())) {
                shipmentDetailMatrix[shipmentDetail.getTier() - 1][shipmentDetail.getRow() - 1] = shipmentDetail;
            } else {
                bayList.add(shipmentDetailMatrix);
                currentBay = shipmentDetail.getBay();
                shipmentDetailMatrix = new ShipmentDetail[5][6];
                shipmentDetailMatrix[shipmentDetail.getTier() - 1][shipmentDetail.getRow() - 1] = shipmentDetail;
            }
        }
        bayList.add(shipmentDetailMatrix);

        return bayList;
    }
    
    @Override
    @Transactional
    public boolean calculateMovingCont(List<LinkedHashMap> coordinateOfList, List<ShipmentDetail> preorderPickupConts, List<ShipmentDetail> shipmentDetails) {
        try {
            List<ShipmentDetail[][]> bayList = new ArrayList<>();
            for (ShipmentDetail shipmentDetail : shipmentDetails) {
                for (int i = 0; i < coordinateOfList.size(); i++) {
                    if (shipmentDetail.getContainerNo().equals(coordinateOfList.get(i).get("containerNo"))) {
                        shipmentDetail.setBay(coordinateOfList.get(i).get("bay").toString());
                        shipmentDetail.setRow(Integer.parseInt(coordinateOfList.get(i).get("row").toString()));
                        shipmentDetail.setTier(Integer.parseInt(coordinateOfList.get(i).get("tier").toString()));
                        coordinateOfList.remove(i);
                        i--;
                    }
                }
            }

            for (int i = 0; i < coordinateOfList.size(); i++) {
                ShipmentDetail shipmentDetail2 = new ShipmentDetail();
                shipmentDetail2.setBay(coordinateOfList.get(i).get("bay").toString());
                shipmentDetail2.setRow(Integer.parseInt(coordinateOfList.get(i).get("row").toString()));
                shipmentDetail2.setTier(Integer.parseInt(coordinateOfList.get(i).get("tier").toString()));
                shipmentDetails.add(shipmentDetail2);
            }

            // Mapping container to matrix by location row, tier, bay
            Collections.sort(shipmentDetails, new BayComparator());
            ShipmentDetail[][] shipmentDetailMatrix = new ShipmentDetail[5][6];
            String currentBay = shipmentDetails.get(0).getBay();
            for (ShipmentDetail shipmentDetail : shipmentDetails) {
                if (currentBay.equals(shipmentDetail.getBay())) {
                    shipmentDetailMatrix[shipmentDetail.getTier() - 1][shipmentDetail.getRow() - 1] = shipmentDetail;
                } else {
                    bayList.add(shipmentDetailMatrix);
                    currentBay = shipmentDetail.getBay();
                    shipmentDetailMatrix = new ShipmentDetail[5][6];
                    shipmentDetailMatrix[shipmentDetail.getTier() - 1][shipmentDetail.getRow() - 1] = shipmentDetail;
                }
            }
            bayList.add(shipmentDetailMatrix);

            int movingContAmount = 0;
            int movingContCol = 0;
            for (int b = 0; b < bayList.size(); b++) {
                int movingContAmountTemp = 0;
                for (int row = 0; row < 6; row++) {
                    for (int tier = 4; tier >= 0; tier--) {
                        if (bayList.get(b)[tier][row] != null) {
                            for (ShipmentDetail shipmentDetail : preorderPickupConts) {
                                if (bayList.get(b)[tier][row].getId() == shipmentDetail.getId()) {
                                    movingContCol = movingContAmountTemp;
                                    break;
                                }
                            }
                            movingContAmountTemp++;
                        }
                    }
                    movingContAmount += movingContCol;
                    movingContCol = 0;
                    movingContAmountTemp = 0;
                }
            }

            for (ShipmentDetail shipmentDetail : preorderPickupConts) {
                shipmentDetail.setMovingContAmount(movingContAmount);
                shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Transactional
    public List<QueueOrder> makeOrderReceiveContFull(List<ShipmentDetail> shipmentDetails, Shipment shipment, String isCredit) {
        if (shipmentDetails.size() > 0) {
            Collections.sort(shipmentDetails, new SztpComparator());
            String sztp = shipmentDetails.get(0).getSztp();
            List<ShipmentDetail> shipmentOrderList = new ArrayList<>();
            List<QueueOrder> queueOrders = new ArrayList<>();
            QueueOrder queueOrder = new QueueOrder();
            for (ShipmentDetail shipmentDetail : shipmentDetails) {
                if (sztp.equals(shipmentDetail.getSztp())) {
                    shipmentOrderList.add(shipmentDetail);
                } else {
                    queueOrder.setMode("Truck Out");
                    queueOrder.setConsignee(shipmentOrderList.get(0).getConsignee());
                    queueOrder.setTruckCo("0100100110 : VIỆN NGHIÊN CỨU CƠ KHÍ");
                    queueOrder.setTaxCode(shipment.getTaxCode());
                    if ("0".equals(isCredit)) {
                        queueOrder.setPayType("Cash");
                    } else {
                        queueOrder.setPayType("Credit");
                    }
                    queueOrder.setBlNo(shipmentDetails.get(0).getBlNo());
                    queueOrder.setPickupDate(new Date());
                    queueOrder.setVessel(shipmentDetails.get(0).getVslNm());
                    queueOrder.setVessel(shipmentDetails.get(0).getVoyNo());
                    queueOrder.setYear("2020");
                    queueOrder.setSizeType(shipmentDetails.get(0).getSztp());
                    queueOrder.setBeforeAfter("Before");
                    queueOrder.setContNumber(shipmentDetails.size());
                    queueOrder.setId(shipmentDetails.get(0).getId());
                    queueOrders.add(queueOrder);
                    queueOrder = new QueueOrder();
                    for (ShipmentDetail shipmentDetail2 : shipmentOrderList) {
                        
                        shipmentDetail2.setRegisterNo(shipmentOrderList.get(0).getId().toString());
                        shipmentDetail2.setUserVerifyStatus("Y");
                        shipmentDetailMapper.updateShipmentDetail(shipmentDetail2);
                    }
                }
            }
            queueOrder.setMode("Truck Out");
            queueOrder.setConsignee(shipmentOrderList.get(0).getConsignee());
            queueOrder.setTruckCo("0100100110 : VIỆN NGHIÊN CỨU CƠ KHÍ");
            queueOrder.setTaxCode(shipment.getTaxCode());
            if ("0".equals(isCredit)) {
                queueOrder.setPayType("Cash");
            } else {
                queueOrder.setPayType("Credit");
            }
            queueOrder.setBlNo(shipmentDetails.get(0).getBlNo());
            queueOrder.setPickupDate(new Date());
            queueOrder.setVessel(shipmentDetails.get(0).getVslNm());
            queueOrder.setVessel(shipmentDetails.get(0).getVoyNo());
            queueOrder.setYear("2020");
            queueOrder.setSizeType(shipmentDetails.get(0).getSztp());
            queueOrder.setBeforeAfter("Before");
            queueOrder.setContNumber(shipmentDetails.size());
            queueOrder.setId(shipmentDetails.get(0).getId());
            queueOrder.setServiceId("1");
            queueOrders.add(queueOrder);
            for (ShipmentDetail shipmentDetail2 : shipmentOrderList) {
                shipmentDetail2.setRegisterNo(shipmentOrderList.get(0).getId().toString());
                shipmentDetail2.setUserVerifyStatus("Y");
                shipmentDetailMapper.updateShipmentDetail(shipmentDetail2);
            }
            return queueOrders;
        }
        return null;
    }

    @Transactional
    public QueueOrder makeOrderSendContEmpty(List<ShipmentDetail> shipmentDetails, Shipment shipment, String isCredit) {
        if (shipmentDetails.size() > 0) {
            QueueOrder queueOrder = new QueueOrder();
            queueOrder.setTaxCode(shipment.getTaxCode());
            if ("0".equals(isCredit)) {
                queueOrder.setPayType("Cash");
                queueOrder.setInvoiceTemplate("Dịch vụ hạ container - Cash");
            } else {
                queueOrder.setPayType("Credit");
                queueOrder.setInvoiceTemplate("Dịch vụ hạ container - Credit");
            }
            queueOrder.setVessel(shipmentDetails.get(0).getVslNm());
            queueOrder.setVoyage(shipmentDetails.get(0).getVoyNo());
            queueOrder.setYear("2020");
            queueOrder.setBeforeAfter("Before");
            queueOrder.setInvoiceType("200");
            queueOrder.setContNumber(shipmentDetails.size());
            queueOrder.setId(shipmentDetails.get(0).getId());
            queueOrder.setServiceId("2");
            for (ShipmentDetail shipmentDetail : shipmentDetails) {
                shipmentDetail.setRegisterNo(shipmentDetails.get(0).getId().toString());
                shipmentDetail.setUserVerifyStatus("Y");
                shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
            }
            return queueOrder;
        }
        return null;
    }

    @Transactional
    public List<QueueOrder> makeOrderReceiveContEmpty(List<ShipmentDetail> shipmentDetails) {
        if (shipmentDetails.size() > 0) {
            Collections.sort(shipmentDetails, new SztpComparator());
            String sztp = shipmentDetails.get(0).getSztp();
            List<ShipmentDetail> shipmentOrderList = new ArrayList<>();
            List<QueueOrder> queueOrders = new ArrayList<>();
            QueueOrder queueOrder = new QueueOrder();
            for (ShipmentDetail shipmentDetail : shipmentDetails) {
                if (sztp.equals(shipmentDetail.getSztp())) {
                    shipmentOrderList.add(shipmentDetail);
                } else {
                    for (ShipmentDetail shipmentDetail2 : shipmentOrderList) {
                        shipmentDetail2.setRegisterNo(shipmentOrderList.get(0).getId().toString());
                        shipmentDetail2.setUserVerifyStatus("Y");
                        shipmentDetailMapper.updateShipmentDetail(shipmentDetail2);
                    }
                }
            }
            for (ShipmentDetail shipmentDetail2 : shipmentOrderList) {
                shipmentDetail2.setRegisterNo(shipmentOrderList.get(0).getId().toString());
                shipmentDetail2.setUserVerifyStatus("Y");
                shipmentDetailMapper.updateShipmentDetail(shipmentDetail2);
            }
            return queueOrders;
        }
        return null;
    }

    @Override
    public QueueOrder makeOrderSendContFull(List<ShipmentDetail> shipmentDetails, Shipment shipment, String isCredit) {
        if (shipmentDetails.size() > 0) {
            QueueOrder queueOrder = new QueueOrder();
            queueOrder.setTaxCode(shipment.getTaxCode());
            if ("0".equals(isCredit)) {
                queueOrder.setPayType("Cash");
                queueOrder.setInvoiceType("200");
                queueOrder.setInvoiceTemplate("Dịch vụ hạ container - CASH");
                queueOrder.setContNumber(shipmentDetails.size());
                queueOrder.setVessel(shipmentDetails.get(0).getVslNm());
                queueOrder.setVoyage(shipmentDetails.get(0).getVoyNo());
                queueOrder.setYear("2020");
                queueOrder.setBeforeAfter("Before");
                queueOrder.setId(shipmentDetails.get(0).getId());
            }
            for (ShipmentDetail shipmentDetail : shipmentDetails) {
                shipmentDetail.setRegisterNo(shipmentDetails.get(0).getId().toString());
                shipmentDetail.setUserVerifyStatus("Y");
                // shipmentDetail.setProcessStatus("Y");
                // shipmentDetail.setStatus(3);
                shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
            }
            return queueOrder;
        }
        return null;
    }


    @Override
    public String getGroupNameByTaxCode(String taxCode) {
        String uri = "https://thongtindoanhnghiep.co/api/company/" + taxCode;

        // RestTemplate restTemplate = new RestTemplate();
        // String result = restTemplate.getForObject(uri, String.class);
        return "Công ty abc";
    }
    
    @Override
    @Transactional
    public void updateProcessStatus(List<ShipmentDetail> shipmentDetails, String status) {
    	for (ShipmentDetail shipmentDetail : shipmentDetails) {
    		shipmentDetail.setProcessStatus(status);
    		if ("Y".equalsIgnoreCase(status)) {
    			shipmentDetail.setStatus(2);
    		} 		
    		shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
    	}
    }
}
