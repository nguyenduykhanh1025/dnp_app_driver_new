package vn.com.irtech.eport.logistic.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import vn.com.irtech.eport.carrier.domain.Edo;
import vn.com.irtech.eport.carrier.mapper.EdoMapper;
import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.constant.Constants;
import vn.com.irtech.eport.common.core.text.Convert;
import vn.com.irtech.eport.common.utils.DateUtils;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.ServiceSendFullRobotReq;
import vn.com.irtech.eport.logistic.dto.ShipmentWaitExec;
import vn.com.irtech.eport.logistic.form.PickupAssignForm;
import vn.com.irtech.eport.logistic.mapper.ShipmentDetailMapper;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.logistic.service.IProcessOrderService;
import vn.com.irtech.eport.logistic.service.IShipmentDetailService;
import vn.com.irtech.eport.logistic.service.IShipmentService;
import vn.com.irtech.eport.system.domain.SysDictData;
import vn.com.irtech.eport.system.service.ISysConfigService;
import vn.com.irtech.eport.system.service.ISysDictTypeService;


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
    private EdoMapper edoMapper;

    @Autowired
    private IProcessOrderService processOrderService;

    @Autowired
    private ISysConfigService configService;
    
    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private ICatosApiService catosApiService;

    @Autowired
    private IShipmentService shipmentService;
    
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
    
    class OrderNoComparator implements Comparator<ShipmentDetail> {
        public int compare(ShipmentDetail shipmentDetail1, ShipmentDetail shipmentDetail2) {
            // In the following line you set the criterion,
            // which is the name of Contact in my example scenario
            return shipmentDetail1.getOrderNo().compareTo(shipmentDetail2.getOrderNo());
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
    public List<ShipmentDetail> selectShipmentDetailByIds(String ids, Long logisticGroupId) {
        return shipmentDetailMapper.selectShipmentDetailByIds(Convert.toStrArray(ids), logisticGroupId);
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
    public List<ShipmentDetail[][]> getContPosition(List<ShipmentDetail> coordinateOfList,
            List<ShipmentDetail> shipmentDetails) {
        // simulating the location of container in da nang port, mapping to matrix
        List<ShipmentDetail[][]> bayList = new ArrayList<>();
        for (ShipmentDetail shipmentDetail : shipmentDetails) {
            for (int i = 0; i < coordinateOfList.size(); i++) {
                if (shipmentDetail.getContainerNo().equals(coordinateOfList.get(i).getContainerNo())) {
                    shipmentDetail.setBlock(coordinateOfList.get(i).getBlock());
                    shipmentDetail.setBay(coordinateOfList.get(i).getBay());
                    shipmentDetail.setRow(coordinateOfList.get(i).getRow());
                    shipmentDetail.setTier(coordinateOfList.get(i).getTier());
                    coordinateOfList.remove(i);
                    i--;
                }
            }
        }

        for (int i = 0; i < coordinateOfList.size(); i++) {
            ShipmentDetail shipmentDetail2 = new ShipmentDetail();
            shipmentDetail2.setBlock(coordinateOfList.get(i).getBlock());
            shipmentDetail2.setBay(coordinateOfList.get(i).getBay());
            shipmentDetail2.setRow(coordinateOfList.get(i).getRow());
            shipmentDetail2.setTier(coordinateOfList.get(i).getTier());
            shipmentDetails.add(shipmentDetail2);
        }

        // Mapping container to matrix by location row, tier, bay
        Collections.sort(shipmentDetails, new BayComparator());
        ShipmentDetail[][] shipmentDetailMatrix = new ShipmentDetail[5][7];
        String currentBay = shipmentDetails.get(0).getBay();
        for (ShipmentDetail shipmentDetail : shipmentDetails) {
            if (currentBay.equals(shipmentDetail.getBay())) {
                shipmentDetailMatrix[shipmentDetail.getTier() - 1][shipmentDetail.getRow() - 1] = shipmentDetail;
            } else {
                bayList.add(shipmentDetailMatrix);
                currentBay = shipmentDetail.getBay();
                shipmentDetailMatrix = new ShipmentDetail[5][7];
                shipmentDetailMatrix[shipmentDetail.getTier() - 1][shipmentDetail.getRow() - 1] = shipmentDetail;
            }
        }
        bayList.add(shipmentDetailMatrix);

        return bayList;
    }

    @Override
    @Transactional
    public List<ServiceSendFullRobotReq> calculateMovingCont(List<ShipmentDetail> coordinateOfList, List<ShipmentDetail> preorderPickupConts,
            List<ShipmentDetail> shipmentDetails, Shipment shipment, Boolean isCredit) {
        try {

            // Apply yard position for shipment detail list
            List<ShipmentDetail[][]> bayList = new ArrayList<>();
            for (ShipmentDetail shipmentDetail : shipmentDetails) {
                for (int i = 0; i < coordinateOfList.size(); i++) {
                    if (shipmentDetail.getContainerNo().equals(coordinateOfList.get(i).getContainerNo())) {
                        shipmentDetail.setBay(coordinateOfList.get(i).getBay());
                        shipmentDetail.setRow(coordinateOfList.get(i).getRow());
                        shipmentDetail.setTier(coordinateOfList.get(i).getTier());
                        coordinateOfList.remove(i);
                        i--;
                    }
                }
            }

            // 
            for (int i = 0; i < coordinateOfList.size(); i++) {
                ShipmentDetail shipmentDetail2 = new ShipmentDetail();
                shipmentDetail2.setBay(coordinateOfList.get(i).getBay());
                shipmentDetail2.setRow(coordinateOfList.get(i).getRow());
                shipmentDetail2.setTier(coordinateOfList.get(i).getTier());
                shipmentDetails.add(shipmentDetail2);
            }

            // Mapping container to matrix by location row, tier, bay
            Collections.sort(shipmentDetails, new BayComparator());
            ShipmentDetail[][] shipmentDetailMatrix = new ShipmentDetail[5][7];
            String currentBay = shipmentDetails.get(0).getBay();
            for (ShipmentDetail shipmentDetail : shipmentDetails) {
                if (currentBay.equals(shipmentDetail.getBay())) {
                    shipmentDetailMatrix[shipmentDetail.getTier() - 1][shipmentDetail.getRow() - 1] = shipmentDetail;
                } else {
                    bayList.add(shipmentDetailMatrix);
                    currentBay = shipmentDetail.getBay();
                    shipmentDetailMatrix = new ShipmentDetail[5][7];
                    shipmentDetailMatrix[shipmentDetail.getTier() - 1][shipmentDetail.getRow() - 1] = shipmentDetail;
                }
            }
            bayList.add(shipmentDetailMatrix);

            // Collect list cont need to shifting
            List<ShipmentDetail> shiftingContList = new ArrayList<>();
            for (int b = 0; b < bayList.size(); b++) {
                List<ShipmentDetail> tempShiftingContList = new ArrayList<>();
                for (int row = 0; row < 7; row++) {
                    for (int tier = 4; tier >= 0; tier--) {
                        if (bayList.get(b)[tier][row] != null) {
                            Boolean needMoving = true;
                            for (ShipmentDetail shipmentDetail : preorderPickupConts) {
                                if (Objects.equals(bayList.get(b)[tier][row].getId(), shipmentDetail.getId())) {
                                    shiftingContList.addAll(tempShiftingContList);
                                    tempShiftingContList.clear();
                                    needMoving = false;
                                    break;
                                }
                            }
                            if ("N".equals(bayList.get(b)[tier][row].getPreorderPickup()) && needMoving) {
                                tempShiftingContList.add(bayList.get(b)[tier][row]);
                            }
                        }
                    }
                    tempShiftingContList.clear();
                }
            }

            // Seperate cont with different sztp
            List<ServiceSendFullRobotReq> serviceRobotReq = new ArrayList<>();
            Collections.sort(shiftingContList, new SztpComparator());
            String sztp = shiftingContList.get(0).getSztp();
            List<ShipmentDetail> shipmentOrderList = new ArrayList<>();
            for (ShipmentDetail shipmentDetail : shiftingContList) {
                if (!sztp.equals(shipmentDetail.getSztp())) {
                    serviceRobotReq.add(groupShipmentDetailByShiftingContOrder(shipmentOrderList, shipment, isCredit));
                    shipmentOrderList = new ArrayList<>();
                }
                Integer index = catosApiService.getIndexContForSsrByContainerNo(shipmentDetail.getContainerNo());
                if (index == null) {
                    index = 1;
                }
                shipmentDetail.setIndex(index);
                shipmentOrderList.add(shipmentDetail);
            }
            serviceRobotReq.add(groupShipmentDetailByShiftingContOrder(shipmentOrderList, shipment, isCredit));

            return serviceRobotReq;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ServiceSendFullRobotReq groupShipmentDetailByShiftingContOrder(List<ShipmentDetail> shipmentDetails, Shipment shipment, Boolean isCredit) {
        ProcessOrder processOrder = new ProcessOrder();
        processOrder.setTaxCode(shipment.getTaxCode());
        processOrder.setShipmentId(shipment.getId());
        processOrder.setLogisticGroupId(shipment.getLogisticGroupId());
        processOrder.setServiceType(5);
        if (isCredit) {
            processOrder.setPayType("Credit");
        } else {
            processOrder.setPayType("Cash");
        }
        processOrder.setContNumber(shipmentDetails.size());
        processOrder.setSsrCode(getSSR(shipmentDetails.get(0).getSztp()));
        processOrderService.insertProcessOrder(processOrder);
        for (ShipmentDetail shipmentDetail : shipmentDetails) {
            shipmentDetail.setPreorderPickup("Y");
            shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
        }
        return new ServiceSendFullRobotReq(processOrder, shipmentDetails);
    }

    public List<ServiceSendFullRobotReq> makeOrderReceiveContFull(List<ShipmentDetail> shipmentDetails,
            Shipment shipment, boolean creditFlag) {
        if (shipmentDetails.size() > 0) {
            List<ServiceSendFullRobotReq> serviceRobotReq = new ArrayList<>();
            if (checkMakeOrderByBl(shipment.getBlNo(), shipmentDetails.size(),
                    "1".equalsIgnoreCase(shipment.getEdoFlg()))) {
                serviceRobotReq.add(groupShipmentDetailByReceiveContFullOrder(shipmentDetails.get(0).getId(),
                        shipmentDetails, shipment, creditFlag, true));
            } else {
                Collections.sort(shipmentDetails, new SztpComparator());
                String sztp = shipmentDetails.get(0).getSztp();
                List<ShipmentDetail> shipmentOrderList = new ArrayList<>();
                for (ShipmentDetail shipmentDetail : shipmentDetails) {
                    if (!sztp.equals(shipmentDetail.getSztp())) {
                        serviceRobotReq.add(groupShipmentDetailByReceiveContFullOrder(shipmentDetails.get(0).getId(),
                                shipmentOrderList, shipment, creditFlag, false));
                        shipmentOrderList = new ArrayList<>();
                    }
                    shipmentOrderList.add(shipmentDetail);
                }
                serviceRobotReq.add(groupShipmentDetailByReceiveContFullOrder(shipmentDetails.get(0).getId(),
                        shipmentOrderList, shipment, creditFlag, false));
            }
            return serviceRobotReq;
        }
        return null;
    }

    @Transactional
    private ServiceSendFullRobotReq groupShipmentDetailByReceiveContFullOrder(Long registerNo,
            List<ShipmentDetail> shipmentDetails, Shipment shipment, boolean creditFlag, boolean orderByBl) {
        ProcessOrder processOrder = new ProcessOrder();
        if (orderByBl) {
            processOrder.setModee("Pickup Order By BL");
        } else {
            processOrder.setModee("Truck Out");
        }
        processOrder.setConsignee(shipmentDetails.get(0).getConsignee());
        processOrder.setLogisticGroupId(shipment.getLogisticGroupId());
        try {
            processOrder.setTruckCo(shipment.getTaxCode() + " : " + getGroupNameByTaxCode(shipment.getTaxCode()).getGroupName());
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
    public List<ServiceSendFullRobotReq> makeOrderReceiveContEmpty(List<ShipmentDetail> shipmentDetails, Shipment shipment, boolean creditFlag) {
        if (shipmentDetails.size() > 0) {
            List<ServiceSendFullRobotReq> serviceRobotReq = new ArrayList<>();
            Collections.sort(shipmentDetails, new SztpComparator());
            String sztp = shipmentDetails.get(0).getSztp();
            List<ShipmentDetail> shipmentOrderList = new ArrayList<>();
            for (ShipmentDetail shipmentDetail : shipmentDetails) {
                if (!sztp.equals(shipmentDetail.getSztp())) {
                    serviceRobotReq.add(groupShipmentDetailByReceiveContEmptyOrder(shipmentDetails.get(0).getId(), shipmentOrderList, shipment, creditFlag));
                    shipmentOrderList = new ArrayList<>();
                }
                shipmentOrderList.add(shipmentDetail);
            }
            serviceRobotReq.add(groupShipmentDetailByReceiveContEmptyOrder(shipmentDetails.get(0).getId(), shipmentOrderList, shipment, creditFlag));
            return serviceRobotReq;
        }
        return null;
    }

    @Transactional
    private ServiceSendFullRobotReq groupShipmentDetailByReceiveContEmptyOrder(Long registerNo, List<ShipmentDetail> shipmentDetails, Shipment shipment, boolean creditFlag) {
        ProcessOrder processOrder = new ProcessOrder();
        processOrder.setModee("Pickup By Booking");
        processOrder.setConsignee(shipmentDetails.get(0).getConsignee());
        processOrder.setLogisticGroupId(shipment.getLogisticGroupId());
        processOrder.setTruckCo(shipment.getTaxCode()+" : "+shipment.getGroupName());
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
        processOrder.setBookingNo(shipmentDetails.get(0).getBookingNo());
        processOrder.setPickupDate(shipmentDetails.get(0).getExpiredDem());
        processOrder.setVessel(shipmentDetails.get(0).getVslNm());
        processOrder.setVoyage(shipmentDetails.get(0).getVoyNo());
        processOrder.setSztp(shipmentDetails.get(0).getSztp());
        processOrder.setContNumber(shipmentDetails.size());
        processOrder.setShipmentId(shipment.getId());
        processOrder.setServiceType(3);
        processOrderService.insertProcessOrder(processOrder);
        for (ShipmentDetail shipmentDetail : shipmentDetails) {
            shipmentDetail.setProcessOrderId(processOrder.getId());
            shipmentDetail.setRegisterNo(registerNo.toString());
            shipmentDetail.setUserVerifyStatus("Y");
            shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
        }
        return new ServiceSendFullRobotReq(processOrder, shipmentDetails);
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
    public void updateProcessStatus(List<ShipmentDetail> shipmentDetails, String status, String invoiceNo,
            ProcessOrder processOrder) {
        for (ShipmentDetail shipmentDetail : shipmentDetails) {
            shipmentDetail.setProcessStatus(status);
            shipmentDetail.setOrderNo(processOrder.getOrderNo());
            if ("Y".equalsIgnoreCase(status)) {
                if ("Credit".equalsIgnoreCase(processOrder.getPayType())) {
                    shipmentDetail.setStatus(shipmentDetail.getStatus() + 1);
                    shipmentDetail.setPaymentStatus("Y");
                    if (4 == processOrder.getServiceType()
                            && "VN".equals(shipmentDetail.getDischargePort().substring(0, 2))) {
                        shipmentDetail.setStatus(shipmentDetail.getStatus() + 1);
                        shipmentDetail.setCustomStatus("R");
                    }
                }
                shipmentDetail.setStatus(shipmentDetail.getStatus() + 1);
            }
            shipmentDetailMapper.updateShipmentDetail(shipmentDetail);
        }
    }

    @Override
    public boolean checkCustomStatus(String userVoy, String cntrNo) {
        try {
            // "http://192.168.0.36:8060/ACCIS-Web/rest/v1/eportcontroller/getCustomsStatus/"
            String uri = configService.selectConfigByKey("acciss.api.uri");
            // URI uri = new URI(uri);

            String requestJson = "{\"RequestCntrStatus\": {\"UserVoy\": \"" + userVoy + "\",\"CntrNo\": \"" + cntrNo
                    + "\"}}";
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
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Shipment getGroupNameByTaxCode(String taxCode) throws Exception {
        // String apiUrl = "https://thongtindoanhnghiep.co/api/company/";
        // String methodName = "GET";
        // String readLine = null;
        // HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl + "/" +
        // taxCode).openConnection();
        // connection.setRequestMethod(methodName);
        // connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1;
        // WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95
        // Safari/537.11");
        //
        // int responseCode = connection.getResponseCode();
        // StringBuffer response = new StringBuffer();
        // if (responseCode == HttpURLConnection.HTTP_OK) {
        // BufferedReader in = new BufferedReader(new
        // InputStreamReader(connection.getInputStream()));
        // while ((readLine = in.readLine()) != null) {
        // response.append(readLine);
        // };
        // in.close();
        // } else {
        // String error = responseCode + " : " + methodName + " NOT WORKED";
        // return error;
        // }
        // String str = response.toString();
        // JsonObject convertedObject = new Gson().fromJson(str, JsonObject.class);
        // if(convertedObject.get("Title").toString().equals("null"))
        // {
        // return null;
        // }
        // return convertedObject.get("Title").toString().replace("\"", "");
        String url = Global.getApiUrl() + "/shipmentDetail/getGroupNameByTaxCode/" + taxCode;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, Shipment.class);
    }

    @Override
    public ProcessOrder getYearBeforeAfter(String vessel, String voyage) {
        String url = Global.getApiUrl() + "/processOrder/getYearBeforeAfter/" + vessel + "/" + voyage;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, ProcessOrder.class);
    }

    @Override
    public List<String> checkContainerReserved(String containerNos) {
        String url = Global.getApiUrl() + "/shipmentDetail/checkContReserved/" + containerNos;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<String>>() {
                });
        List<String> listCont = response.getBody();
        return listCont;
    }

    @Override
    public List<String> getPODList() {
        String url = Global.getApiUrl() + "/shipmentDetail/getPODList";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<String>>() {
                });
        List<String> pods = response.getBody();
        return pods;
    }

    @Override
    public List<String> getVesselCodeList() {
        String url = Global.getApiUrl() + "/shipmentDetail/getVesselCodeList";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<String>>() {
                });
        List<String> vslNms = response.getBody();
        return vslNms;
    }

    @Override
    public List<String> getConsigneeList() {
        String url = Global.getApiUrl() + "/shipmentDetail/getConsigneeList";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<String>>() {
                });
        List<String> consignees = response.getBody();
        return consignees;
    }

    @Override
    public List<String> getOpeCodeList() {
        String url = Global.getApiUrl() + "/shipmentDetail/getOpeCodeList";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<String>>() {
                });
        List<String> opeCodes = response.getBody();
        return opeCodes;
    }

    @Override
    public List<String> getVoyageNoList(String vesselCode) {
        String url = Global.getApiUrl() + "/shipmentDetail/getVoyageNoList/" + vesselCode;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<String>>() {
                });
        List<String> voyages = response.getBody();
        return voyages;
    }

    @Override
    public int getCountContByBlNo(String blNo) {
        String url = Global.getApiUrl() + "/shipmentDetail/getCountContByBlNo/" + blNo;
        RestTemplate restTemplate = new RestTemplate();
        Integer count = restTemplate.getForObject(url, Integer.class);
        return count.intValue();
    }

    @Override
    public List<ShipmentDetail> selectShipmentDetailByProcessIds(String processOrderIds) {
        return shipmentDetailMapper.selectShipmentDetailByProcessIds(Convert.toStrArray(processOrderIds));
    }

    @Override
    public List<ShipmentDetail> getShipmentDetailsFromEDIByBlNo(String blNo) {
        List<Edo> listEdo = edoMapper.selectEdoListByBlNo(blNo);
        List<ShipmentDetail> listShip = new ArrayList<ShipmentDetail>();
        if (listEdo != null) {
            for (Edo i : listEdo) {
                ShipmentDetail ship = new ShipmentDetail();
                ship.setContainerNo(i.getContainerNumber());
                ship.setExpiredDem(i.getExpiredDem());
                ship.setDetFreeTime(i.getDetFreeTime());
                ship.setEmptyDepot(i.getEmptyContainerDepot());
                ship.setOpeCode(i.getCarrierCode());
                ship.setVslNm(i.getVesselNo()+": "+i.getVessel());
                ship.setVslName(i.getVessel());
                ship.setVoyNo(i.getVoyNo());
                ship.setSztp(i.getSztp());
                listShip.add(ship);
            }
            return listShip;
        }
        return null;
    }

    @Override
    public List<ShipmentDetail> getShipmentDetailListForAssign(ShipmentDetail shipmentDetail) {
        return shipmentDetailMapper.getShipmentDetailListForAssign(shipmentDetail);
    }
    
    
    // @Override
    // public List<ShipmentDetail> selectSendEmptyShipmentDetailByListCont(@Param("conts") String conts, @Param("shipmentId") Long shipmentId) {
    //     return shipmentDetailMapper.selectSendEmptyShipmentDetailByListCont(Convert.toStrArray(conts), shipmentId);
    // }

    @Override
    public List <ShipmentDetail> selectContainerStatusList(ShipmentDetail shipmentDetail) {
        return shipmentDetailMapper.selectContainerStatusList(shipmentDetail);
    }

	@Override
	public List<ShipmentDetail> getShipmentDetailList(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.getShipmentDetailList(shipmentDetail);
	}
    
    /**
     * Count number of legal container
     * 
     * @param shipmentDetails
     * @param logisticGroupId
     * @return Integer
     */
    @Override
    public Integer countNumberOfLegalCont(List<ShipmentDetail> shipmentDetails, Long logisticGroupId) {
        return shipmentDetailMapper.countNumberOfLegalCont(shipmentDetails, logisticGroupId);
    }

    @Override
	public String getSSR(String sztp)
	{
		List<SysDictData> dictDatas = dictTypeService.selectDictDataByType("sys_special_service_request");
		for(SysDictData i : dictDatas) {
			if(i.getDictValue().equals(sztp)) {
				return i.getDictLabel();
			}
		}
		return null;
	}

	@Override
	public List<ShipmentDetail> getShipmentDetailListForSendFReceiveE(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.getShipmentDetailListForSendFReceiveE(shipmentDetail);
	}

	@Override
	public List<Long> getCommandListInBatch(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.getCommandListInBatch(shipmentDetail);
	}

	@Override
	public List<ShipmentDetail> getShipmentDetailForPrint(ShipmentDetail shipmentDetail) {
		return shipmentDetailMapper.getShipmentDetailForPrint(shipmentDetail);
	} 
    
    /**
     * Get container with yard position
     * 
     * @param shipmentId
     * @return ShipmentDetail
     */
    @Override
    public ShipmentDetail getContainerWithYardPosition(Long shipmentId) {
        // Get Shipment
        Shipment shipment = shipmentService.selectShipmentById(shipmentId);

        // Get shipment detail by shipment id
        ShipmentDetail shipmentDetail = new ShipmentDetail();
        shipmentDetail.setShipmentId(shipmentId);
        List<ShipmentDetail> shipmentDetails = shipmentDetailMapper.selectShipmentDetailList(shipmentDetail);

        // Get coordinate by bill
        List<ShipmentDetail> coordinateList = catosApiService.getCoordinateOfContainers(shipmentDetails.get(0).getBlNo());
        List<ShipmentDetail[][]> bayList = new ArrayList<>();
        bayList = getContPosition(coordinateList, shipmentDetails);

        // Get container from top to bottom
        for (int b = 0; b < bayList.size(); b++) {
            for (int row = 0; row < 3; row++) {
                boolean stack1 = false;
                boolean stack2 = false;
                for (int tier = 4; tier >= 0; tier--) {
                    ShipmentDetail shipmentDetail1 = bayList.get(b)[tier][row];
                    // validate
                    if (shipmentDetail1 != null) {
                    	if (validateAutoPickupCont(shipmentDetail1, stack1)) {
                            return shipmentDetail1;
                        }
                    	stack1 = true;
                    }
                    
                    ShipmentDetail shipmentDetail2 = bayList.get(b)[tier][5-row];
                    if (shipmentDetail2 != null) {
                    	if (validateAutoPickupCont(shipmentDetail2, stack2)) {
                            return shipmentDetail2;
                        }
                    	stack2 = true;
                    }
                }
            }
        }
        return null;
    }

    private Boolean validateAutoPickupCont (ShipmentDetail shipmentDetail, boolean stack) {	
        if (shipmentDetail.getId() == null) {
            return false;
        } 

        // Not received DO
        if (!"Y".equals(shipmentDetail.getDoStatus())) {
            return false;
        }
        
        // Exceed expired dem
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (shipmentDetail.getExpiredDem().compareTo(now) < 0) {
            return false;
        }

        if ("N".equals(shipmentDetail.getPaymentStatus())) {
            return false;
        }

        if (stack && "N".equals(shipmentDetail.getPrePickupPaymentStatus())) {
            return false;
        }

        return true;
    }

    /**
     * Select shipment detail for driver shipment assign
     * 
     * @param shipmentId
     * @param driverId
     * @return List<PickupAssignForm>
     */
    public List<PickupAssignForm> selectShipmentDetailForDriverShipmentAssign(Long shipmentId, Long driverId) {
        return shipmentDetailMapper.selectShipmentDetailForDriverShipmentAssign(shipmentId, driverId);
    }
    
    /**
     * Make change vessel order
     * 
     * @param shipmentDetails
     * @param vessel
     * @param voyage
     * @param groupId
     * @return ServiceSendFullRobotReq
     */
    @Override
    public ServiceSendFullRobotReq makeChangeVesselOrder(List<ShipmentDetail>shipmentDetails, String vessel, String voyage, Long groupId) {
    	ProcessOrder processOrder = new ProcessOrder();
    	if (CollectionUtils.isNotEmpty(shipmentDetails)) {
    		ShipmentDetail shipmentDt = shipmentDetails.get(0);
    		processOrder.setServiceType(Constants.CHANGE_VESSEL);
    		processOrder.setShipmentId(shipmentDt.getShipmentId());
    		processOrder.setBookingNo(shipmentDt.getBookingNo());
    		processOrder.setStatus(0);
    		ProcessOrder tempProcessOrder = getYearBeforeAfter(processOrder.getVessel(), processOrder.getVoyage());
            if (tempProcessOrder != null) {
                processOrder.setYear(tempProcessOrder.getYear());
                processOrder.setBeforeAfter(tempProcessOrder.getBeforeAfter());
            } else {
                processOrder.setYear(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
                processOrder.setBeforeAfter("Before");
            }
    		processOrder.setOldVessel(shipmentDt.getVslNm());
    		processOrder.setOldVoyAge(shipmentDt.getVoyNo());
    		processOrder.setVessel(vessel);
    		processOrder.setVoyage(voyage);
    		List<Long> shipmentDetailIds = new ArrayList<>();
    		for (ShipmentDetail shipmentDetail : shipmentDetails) {
    			shipmentDetail.setVslNm(vessel);
    			shipmentDetail.setVoyNo(voyage);
    			shipmentDetailIds.add(shipmentDetail.getId());
    		}
    		Map<String, Object> map = new HashMap<>();
    		map.put("shipmentDetailIds", shipmentDetailIds);
    		processOrder.setProcessData(new Gson().toJson(map));
    		processOrder.setLogisticGroupId(groupId);
    		processOrderService.insertProcessOrder(processOrder);
    		ServiceSendFullRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
    		return serviceRobotReq;
    	}
    	return null;
    }

    /**
     * Make extension date order
     * 
     * @param shipmentDetails
     * @param expiredDem
     * @param groupId
     * @return ServiceSendFullRobotReq
     */
    @Override
    public List<ServiceSendFullRobotReq> makeExtensionDateOrder(List<ShipmentDetail> shipmentDetails, Date expiredDem, Long groupId) {
    	Collections.sort(shipmentDetails, new OrderNoComparator());
    	List<ServiceSendFullRobotReq> serviceRobotReqs = new ArrayList<>();
    	String currentOrderNo = shipmentDetails.get(0).getOrderNo();
    	List<ShipmentDetail> shipmentDetailList = new ArrayList<>();
    	for (ShipmentDetail shipmentDetail : shipmentDetails) {
    		if (!currentOrderNo.equals(shipmentDetail.getOrderNo())) {
    			shipmentDetailList.add(shipmentDetail);
    		} else {
    			serviceRobotReqs.add(separateExtensionOrderByOrderNo(shipmentDetailList, expiredDem, groupId));
    			shipmentDetailList = new ArrayList<>();
    			shipmentDetailList.add(shipmentDetail);
    			currentOrderNo = shipmentDetail.getOrderNo();
    		}
    	}
    	serviceRobotReqs.add(separateExtensionOrderByOrderNo(shipmentDetailList, expiredDem, groupId));
    	return serviceRobotReqs;
    }
    
    private ServiceSendFullRobotReq separateExtensionOrderByOrderNo(List<ShipmentDetail> shipmentDetails, Date expiredDem, Long groupId) {
    	ProcessOrder processOrder = new ProcessOrder();
    	processOrder.setPickupDate(expiredDem);
    	processOrder.setServiceType(Constants.EXTENSION_DATE_ORDER);
    	processOrder.setLogisticGroupId(groupId);
    	processOrder.setShipmentId(shipmentDetails.get(0).getShipmentId());
    	processOrder.setOrderNo(shipmentDetails.get(0).getOrderNo());
    	List<Long> shipmentDetailIds = new ArrayList<>();
    	for (ShipmentDetail shipmentDetail : shipmentDetails) {
    		shipmentDetailIds.add(shipmentDetail.getId());
    	}
    	processOrder.setProcessData(new Gson().toJson(shipmentDetailIds));
    	processOrderService.insertProcessOrder(processOrder);
    	ServiceSendFullRobotReq serviceRobotReq = new ServiceSendFullRobotReq(processOrder, shipmentDetails);
    	return serviceRobotReq;
    }
}
