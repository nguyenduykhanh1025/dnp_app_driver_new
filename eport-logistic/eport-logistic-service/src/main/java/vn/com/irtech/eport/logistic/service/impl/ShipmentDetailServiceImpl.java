package vn.com.irtech.eport.logistic.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;
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
	public List<String> getVesselCodeList() {
		List<String> list = new ArrayList<>();
		list.add("VESSEL1");
		list.add("VESSEL2");
		list.add("VESSEL3");
		list.add("VESSEL4");
		return list;
    }
    
    @Override
	public List<String> getConsigneeList() {
		List<String> list = new ArrayList<>();
		list.add("HAHPHU");
		list.add("VINAL");
		list.add("MAVI");
		list.add("BACHYSOLY");
		list.add("SUNPAPER");
		list.add("DIENXANH");
		return list;
    }
    
    @Override
	public List<String> getTruckCoList(){
		List<String> list = new ArrayList<>();
		list.add("0120314052601 : CTY TNHH HAHPHU");
		list.add("013105020130 : CTY CP VINAL");
		list.add("010201011023: MAVI");
		return list;
    }
    
    @Override
	public List<String> getVoyageList(){
		List<String> list = new ArrayList<>();
		list.add("0101");
		list.add("0102");
		list.add("0103");
		list.add("0120");
		list.add("0130");
		list.add("0210");
		return list;
    }
    
    @Override
	public List<String> getOperatorCodeList(){
		List<String> list = new ArrayList<>();
		list.add("SIT");
		list.add("COS");
		list.add("MSC");
		list.add("OWN");
		list.add("MSL");
		list.add("CMA");
		return list;
    }
    
    @Override
	public List<String> getFeList(){
		List<String> list = new ArrayList<>();
		list.add("F");
		list.add("E");
		return list;
    }
    
    @Override
	public List<String> getCargoTypeList(){
		List<String> list = new ArrayList<>();
		list.add("MT");
		list.add("DR");
		list.add("RF");
		list.add("GP");
		return list;
    }
    
    @Override
	public List<String> getPODList(){
		List<String> list = new ArrayList<>();
		list.add("VNDAD");
		list.add("CMTVN");
		list.add("HKHKG");
		list.add("TCCVN");
		list.add("TWTXG");
		list.add("CNTAO");
		return list;
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

    class BayComparator implements Comparator<ShipmentDetail> {
		public int compare(ShipmentDetail shipmentDetail1, ShipmentDetail shipmentDetail2) {
			// In the following line you set the criterion,
			// which is the name of Contact in my example scenario
			return shipmentDetail1.getBay().compareTo(shipmentDetail2.getBay());
		}
	}
}
