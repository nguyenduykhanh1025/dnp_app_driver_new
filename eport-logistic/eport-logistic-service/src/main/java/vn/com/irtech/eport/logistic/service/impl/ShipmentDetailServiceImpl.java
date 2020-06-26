package vn.com.irtech.eport.logistic.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.dto.ShipmentWaitExec;
import vn.com.irtech.eport.logistic.mapper.ShipmentDetailMapper;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;

/**
 * Shipment DetailsService Business Processing
 * 
 * @author admin
 * @date 2020-05-07
 */
@Service
public class ShipmentDetailServiceImpl implements IShipmentDetailService {
    @Autowired
    private ShipmentDetailMapper shipmentDetailMapper;

    @Autowired
    private IProcessOrderService processOrderService;

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
    public ShipmentDetail selectShipmentDetailById(Long id) {
        return shipmentDetailMapper.selectShipmentDetailById(id);
    }

    /**
     * Get Shipment Details List
     * 
     * @param shipmentDetail Shipment Details
     * @return Shipment Details
     */
    @Override
    public List<ShipmentDetail> selectShipmentDetailList(ShipmentDetail shipmentDetail) {
        return shipmentDetailMapper.selectShipmentDetailList(shipmentDetail);
    }

    /**
     * Add Shipment Details
     * 
     * @param shipmentDetail Shipment Details
     * @return result
     */
    @Override
    public int insertShipmentDetail(ShipmentDetail shipmentDetail) {
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
    public int updateShipmentDetail(ShipmentDetail shipmentDetail) {
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
    public int deleteShipmentDetailByIds(String ids) {
        return shipmentDetailMapper.deleteShipmentDetailByIds(Convert.toStrArray(ids));
    }

    /**
     * Delete Shipment Details
     * 
     * @param id Shipment DetailsID
     * @return result
     */
    @Override
    public int deleteShipmentDetailById(Long id) {
        return shipmentDetailMapper.deleteShipmentDetailById(id);
    }

    @Override
    public List<ShipmentDetail> selectShipmentDetailByIds(String ids) {
        return shipmentDetailMapper.selectShipmentDetailByIds(Convert.toStrArray(ids));
    }

    @Override
    public List<ShipmentDetail> selectShipmentDetailByBlno(String Blno) {
        return shipmentDetailMapper.selectShipmentDetailByBlno(Blno);
    }

    @Override
    public List<String> getBlListByDoStatus(String keyString) {
        return shipmentDetailMapper.getBlListByDoStatus(keyString);
    }

    @Override
    public List<String> getBlListByPaymentStatus(String keyString) {
        return shipmentDetailMapper.getBlListByPaymentStatus(keyString);
    }

    @Override
    public List<String> getBlLists(String keyString) {
        return shipmentDetailMapper.getBlLists(keyString);
    }

    public long countShipmentDetailList(ShipmentDetail shipmentDetail) {
        return shipmentDetailMapper.countShipmentDetailList(shipmentDetail);
    }

    /**
     * Select list shipment detail wait robot execute or robot can't be execute,
     * group by shipment id
     * 
     * @return result
     */
    @Override
    public List<ShipmentWaitExec> selectListShipmentWaitExec() {
        return shipmentDetailMapper.selectListShipmentWaitExec();
    }

    @Override
    public List<ShipmentDetail[][]> getContPosition(List<LinkedHashMap> coordinateOfList,
            List<ShipmentDetail> shipmentDetails) {
        // simulating the location of container in da nang port, mapping to matrix
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

        return bayList;
    }

    @Override
    @Transactional
    public boolean calculateMovingCont(List<LinkedHashMap> coordinateOfList, List<ShipmentDetail> preorderPickupConts,
            List<ShipmentDetail> shipmentDetails) {
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
                shipmentDetail.setShiftingContNumber(movingContAmount);
                shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<ServiceSendFullRobotReq> makeOrderReceiveContFull(List<ShipmentDetail> shipmentDetails, Shipment shipment, boolean creditFlag) {
        if (shipmentDetails.size() > 0) {
            List<ServiceSendFullRobotReq> serviceRobotReq = new ArrayList<>();
            if (checkMakeOrderByBl(shipment.getBlNo(), shipmentDetails.size() , "1".equalsIgnoreCase(shipment.getEdoFlg()))) {
                serviceRobotReq.add(groupShipmentDetailByReceiveContFullOrder(shipmentDetails.get(0).getId(), shipmentDetails, shipment, creditFlag, true));
            } else {
                Collections.sort(shipmentDetails, new SztpComparator());
                String sztp = shipmentDetails.get(0).getSztp();
                List<ShipmentDetail> shipmentOrderList = new ArrayList<>();
                for (ShipmentDetail shipmentDetail : shipmentDetails) {
                    if (!sztp.equals(shipmentDetail.getSztp())) {
                        serviceRobotReq.add(groupShipmentDetailByReceiveContFullOrder(shipmentDetails.get(0).getId(), shipmentOrderList, shipment, creditFlag, false));
                        shipmentOrderList = new ArrayList<>();
                    }
                    shipmentOrderList.add(shipmentDetail);
                }
                serviceRobotReq.add(groupShipmentDetailByReceiveContFullOrder(shipmentDetails.get(0).getId(), shipmentOrderList, shipment, creditFlag, false));
            }
            return serviceRobotReq;
        }
        return null;
    }

    @Transactional
    private ServiceSendFullRobotReq groupShipmentDetailByReceiveContFullOrder(Long registerNo, List<ShipmentDetail> shipmentDetails, Shipment shipment, boolean creditFlag, boolean orderByBl) {
        ProcessOrder processOrder = new ProcessOrder();
        if (orderByBl) {
            processOrder.setMode("Pickup Order by BL");
        } else {
            processOrder.setMode("Truck Out");
        }
        processOrder.setConsignee(shipmentDetails.get(0).getConsignee());
        processOrder.setLogisticGroupId(shipment.getLogisticGroupId());
        try {
            processOrder.setTruckCo(shipment.getTaxCode()+" : "+getGroupNameByTaxCode(shipment.getTaxCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        processOrder.setTaxCode(shipment.getTaxCode());
        if (creditFlag) {
            processOrder.setPayType("Credit");
        } else {
            processOrder.setPayType("Cash");
        }
        ProcessOrder tempProcessOrder = getYearBeforeAfter(processOrder.getVessel(), processOrder.getVoyage());
        if (tempProcessOrder != null) {
            processOrder.setYear(tempProcessOrder.getYear());
            processOrder.setBeforeAfter(tempProcessOrder.getBeforeAfter());
        } else {
            processOrder.setYear(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
            processOrder.setBeforeAfter("Before");
        }
        processOrder.setBlNo(shipmentDetails.get(0).getBlNo());
        processOrder.setPickupDate(shipmentDetails.get(0).getExpiredDem());
        processOrder.setVessel(shipmentDetails.get(0).getVslNm());
        processOrder.setVoyage(shipmentDetails.get(0).getVoyNo());
        processOrder.setSztp(shipmentDetails.get(0).getSztp());
        processOrder.setContNumber(shipmentDetails.size());
        processOrder.setShipmentId(shipment.getId());
        processOrder.setServiceType(1);
        processOrderService.insertProcessOrder(processOrder);
        for (ShipmentDetail shipmentDetail : shipmentDetails) {
            shipmentDetail.setProcessOrderId(processOrder.getId());
            shipmentDetail.setRegisterNo(registerNo.toString());
            shipmentDetail.setUserVerifyStatus("Y");
            shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
        }
        return new ServiceSendFullRobotReq(processOrder, shipmentDetails);
    }

    private boolean checkMakeOrderByBl(String blNo, int size, boolean edoFlag) {
        if (edoFlag) {
            if (getCountContByBlNo(blNo) == size) {
                return true;
            }
        } else {
            if (getCountContByBlNo(blNo) == size) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public List<ProcessOrder> makeOrderReceiveContEmpty(List<ShipmentDetail> shipmentDetails) {
        if (shipmentDetails.size() > 0) {
            Collections.sort(shipmentDetails, new SztpComparator());
            String sztp = shipmentDetails.get(0).getSztp();
            List<ShipmentDetail> shipmentOrderList = new ArrayList<>();
            List<ProcessOrder> processOrders = new ArrayList<>();
            ProcessOrder processOrder = new ProcessOrder();
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
            return processOrders;
        }
        return null;
    }

    @Override
    public ProcessOrder makeOrderSendCont(List<ShipmentDetail> shipmentDetails, Shipment shipment, boolean creditFlag) {
        ProcessOrder processOrder = new ProcessOrder();
        processOrder.setTaxCode(shipment.getTaxCode());
        processOrder.setContNumber(shipmentDetails.size());
        processOrder.setVessel(shipmentDetails.get(0).getVslNm());
        processOrder.setVoyage(shipmentDetails.get(0).getVoyNo());
        processOrder.setLogisticGroupId(shipment.getLogisticGroupId());
        ProcessOrder tempProcessOrder = getYearBeforeAfter(processOrder.getVessel(), processOrder.getVoyage());
        if (tempProcessOrder != null) {
            processOrder.setYear(tempProcessOrder.getYear());
            processOrder.setBeforeAfter(tempProcessOrder.getBeforeAfter());
        } else {
            processOrder.setYear(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
            processOrder.setBeforeAfter("Before");
        }
        processOrder.setShipmentId(shipment.getId());
        if ("F".equalsIgnoreCase(shipmentDetails.get(0).getFe())) {
            processOrder.setServiceType(4);
        } else {
            processOrder.setServiceType(2);
        }
        if (creditFlag) {
            processOrder.setPayType("Credit");
        } else {
            processOrder.setPayType("Cash");
        }
        processOrderService.insertProcessOrder(processOrder);
        for (ShipmentDetail shipmentDetail : shipmentDetails) {
            shipmentDetail.setProcessOrderId(processOrder.getId());
            shipmentDetail.setRegisterNo(shipmentDetails.get(0).getId().toString());
            shipmentDetail.setUserVerifyStatus("Y");
            shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
        }
        return processOrder;
    }

    @Override
    @Transactional
    public void updateProcessStatus(List<ShipmentDetail> shipmentDetails, String status, String invoiceNo, ProcessOrder processOrder) {
        for (ShipmentDetail shipmentDetail : shipmentDetails) {
            shipmentDetail.setProcessStatus(status);
            if ("Y".equalsIgnoreCase(status)) {
                if ("Credit".equalsIgnoreCase(processOrder.getPayType())) {
                    shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
                    shipmentDetail.setPaymentStatus("Y");
                    if (4 == processOrder.getServiceType() && "VN".equals(shipmentDetail.getDischargePort().substring(0, 2))) {
                        shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
                        shipmentDetail.setCustomStatus("R");
                    }
                }
                shipmentDetail.setStatus(shipmentDetail.getStatus()+1);
                shipmentDetail.setRegisterNo(invoiceNo);
            }
            shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
        }
    }

    @Override
    public boolean checkCustomStatus(String userVoy, String cntrNo) throws IOException {

        String uri = "http://192.168.0.36:8060/ACCIS-Web/rest/v1/eportcontroller/getCustomsStatus/";
        // URI uri = new URI(uri);
        
    
        String requestJson = "{\"RequestCntrStatus\": {\"UserVoy\": \""+userVoy+"\",\"CntrNo\": \""+cntrNo+"\"}}";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Basic RVBPUlQ6MTEx");
        RestTemplate restTemplate = new RestTemplate();
        // String result = restTemplate.getForObject(uri,HttpMethod.GET,
        // String.class,headers);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        String stringJson = result.getBody();
        JsonObject convertedObject = new Gson().fromJson(stringJson, JsonObject.class);
        convertedObject = convertedObject.getAsJsonObject("response");
        JsonArray jarray = convertedObject.getAsJsonArray("data");
        if (jarray.size() > 0) {
            convertedObject = jarray.get(0).getAsJsonObject();
            String rs = convertedObject.get("customsStatus").toString();
            System.out.print(rs);
            if ("TQ".equals(rs.substring(1, rs.length() - 1))) {
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    @Override
    public String getGroupNameByTaxCode(String taxCode) throws Exception {
//        String apiUrl = "https://thongtindoanhnghiep.co/api/company/";
//        String methodName = "GET";
//        String readLine = null;
//        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl + "/" + taxCode).openConnection();
//        connection.setRequestMethod(methodName);
//        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
//
//        int responseCode = connection.getResponseCode();
//        StringBuffer response = new StringBuffer();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            while ((readLine = in.readLine()) != null) {
//                response.append(readLine);
//            };
//            in.close();
//        } else {
//        	String error = responseCode + " : " + methodName + " NOT WORKED";
//            return error;
//        }
//        String str = response.toString();
//        JsonObject convertedObject = new Gson().fromJson(str, JsonObject.class);
//        if(convertedObject.get("Title").toString().equals("null"))
//        {
//            return null;
//        }
//        return convertedObject.get("Title").toString().replace("\"", "");
    	String url = Global.getApiUrl() + "/shipmentDetail/getGroupNameByTaxCode/"+taxCode;
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, String.class);
    }

    @Override
    public ProcessOrder getYearBeforeAfter(String vessel, String voyage) {
        String url = Global.getApiUrl() + "/processOrder/getYearBeforeAfter/"+vessel+"/"+voyage;
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, ProcessOrder.class);
    }

	@Override
	public List<String> checkContainerReserved(String containerNos) {
		String url = Global.getApiUrl() + "/shipmentDetail/checkContReserved/" + containerNos;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate
				.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
				});
		List<String> listCont = response.getBody();
		return listCont;
	}

	@Override
	public int getCountContByBlNo(String blNo) {
		String url = Global.getApiUrl() + "/shipmentDetail/getCountContByBlNo/" + blNo;
		RestTemplate restTemplate = new RestTemplate();
		Integer count = restTemplate.getForObject(url, Integer.class);
		return count.intValue();
	}
    
    @Override
    public List<ShipmentDetail> selectShipmentDetailByProcessIds (String processOrderIds) {
        return shipmentDetailMapper.selectShipmentDetailByProcessIds(Convert.toStrArray(processOrderIds));
    }
    
}
