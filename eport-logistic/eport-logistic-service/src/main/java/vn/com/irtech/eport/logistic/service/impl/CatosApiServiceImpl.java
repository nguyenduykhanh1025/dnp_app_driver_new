package vn.com.irtech.eport.logistic.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.form.BookingInfo;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.system.dto.PartnerInfoDto;
@Service
public class CatosApiServiceImpl implements ICatosApiService {
	
	private static final Logger logger = LoggerFactory.getLogger(CatosApiServiceImpl.class);
	
	@Override
	public Shipment getOpeCodeCatosByBlNo(String blNo) {
		String url = Global.getApiUrl() + "/shipmentDetail/getOpeCodeCatosByBlNo";
		ShipmentDetail shipmentDetail = new ShipmentDetail();
		shipmentDetail.setBlNo(blNo);
		logger.debug("Call CATOS API :{}", url);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Shipment> responseEntity = restTemplate.postForEntity(url, shipmentDetail, Shipment.class);
		return responseEntity.getBody();
	}

	@Override
	public PartnerInfoDto getGroupNameByTaxCode(String taxCode) {
    	String url = Global.getApiUrl() + "/shipmentDetail/getGroupNameByTaxCode/"+taxCode;
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		PartnerInfoDto result = restTemplate.getForObject(url, PartnerInfoDto.class);
		return  result;
	}

	@Override
	public ProcessOrder getYearBeforeAfter(String vessel, String voyage) {
        String url = Global.getApiUrl() + "/processOrder/getYearBeforeAfter/"+vessel+"/"+voyage;
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, ProcessOrder.class);
	}

	@Override
	public List<String> checkContainerReserved(String containerNos) {
		String url = Global.getApiUrl() + "/shipmentDetail/checkContReserved/" + containerNos;
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
		List<String> listCont = response.getBody();
		return listCont;
	}

	@Override
	public List<String> getPODList(ShipmentDetail shipmentDetail) {
		String url = Global.getApiUrl() + "/shipmentDetail/getPODList";
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<String>>(){});
		List<String> pods = response.getBody();
		return pods;
	}

	@Override
	public List<String> getVesselCodeList() {
		String url = Global.getApiUrl() + "/shipmentDetail/getVesselCodeList";
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
		List<String> listVessel = response.getBody();
		return listVessel;
	}

	@Override
	public List<String> getConsigneeList() {
		String url = Global.getApiUrl() + "/shipmentDetail/getConsigneeList";
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
		List<String> listConsignee = response.getBody();
		return listConsignee;
	}

	@Override
	public List<String> getOpeCodeList() {
		String url = Global.getApiUrl() + "/shipmentDetail/getOpeCodeList";
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
		List<String> listOpeCode = response.getBody();
		return listOpeCode;
	}

	@Override
	public List<String> getVoyageNoList(String vesselCode) {
		String url = Global.getApiUrl() + "/shipmentDetail/getVoyageNoList/" + vesselCode;
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
		List<String> listVoyage = response.getBody();
		return listVoyage;
	}

	@Override
	public int getCountContByBlNo(String blNo) {
		String url = Global.getApiUrl() + "/shipmentDetail/getCountContByBlNo/" + blNo;
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		Integer count = restTemplate.getForObject(url, Integer.class);
		return count.intValue();
	}

	@Override
	public Boolean checkCustomStatus(String containerNo, String voyNo) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/check/custom/" + containerNo + "/" + voyNo;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			Boolean rs = restTemplate.getForObject(url, Boolean.class);
			return rs;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			return false;
		}
	}

	@Override
	public List<ShipmentDetail> getCoordinateOfContainers(String blNo) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/getCoordinateOfContainers/" + blNo;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<ShipmentDetail>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ShipmentDetail>>() {});
			List<ShipmentDetail> coordinates = response.getBody();
			return coordinates;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while call CATOS Api", e);
			return null;
		}
	}

	@Override
	public List<ShipmentDetail> selectShipmentDetailsByBLNo(String blNo) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/list";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			Shipment shipment = new Shipment();
			shipment.setBlNo(blNo);
			HttpEntity<Shipment> httpEntity = new HttpEntity<Shipment>(shipment);
			ResponseEntity<List<ShipmentDetail>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<ShipmentDetail>>() {});
			List<ShipmentDetail> shipmentDetails = response.getBody();
			return shipmentDetails;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while call CATOS Api", e);
			return null;
		}

	}

	@Override
	public ShipmentDetail selectShipmentDetailByContNo(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/containerInfor";
			logger.debug("Call CATOS API: {}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<ShipmentDetail> responseEntity = restTemplate.postForEntity(url, shipmentDetail, ShipmentDetail.class);
			return responseEntity.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while call CATOS Api", e);
			return null;
		}
	}

	@Override
	public List<String> selectVesselCodeBerthPlan(String opeCode) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/berthplan/vessel-code/list/ope-code/" + opeCode ;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
			List<String> vslCodes = response.getBody();
			return vslCodes;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public String getYearByVslCodeAndVoyNo(String vslCode, String voyNo) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/vessel-code/"+ vslCode +"/voyage/" + voyNo + "/year" ;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String year = restTemplate.getForObject(url, String.class);
			return year;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> selectOpeCodeListInBerthPlan() {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/berthplan/ope-code/list" ;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
			List<String> opeCodes = response.getBody();
			return opeCodes;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ShipmentDetail> selectVesselVoyageBerthPlan(String opeCode) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/berthplan/ope-code/"+ opeCode +"/vessel-voyage/list" ;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<ShipmentDetail>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ShipmentDetail>>() {});
			List<ShipmentDetail> vesselAndVoyages = response.getBody();
			return vesselAndVoyages;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ProcessBill> getUnitBillByShipmentDetailsForReserve(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/unit-bill/list/send-cont";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<ProcessBill>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<ProcessBill>>() {});
			List<ProcessBill> bills= response.getBody();
			return bills;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ProcessBill> getUnitBillByShipmentDetailsForInventory(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/unit-bill/list/receive-cont";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<ProcessBill>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<ProcessBill>>() {});
			List<ProcessBill> bills= response.getBody();
			return bills;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ProcessBill> getUnitBillByShipmentDetailsForReceiveSSR(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/unit-bill/list/receive-cont/ssr";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<ProcessBill>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<ProcessBill>>() {});
			List<ProcessBill> bills= response.getBody();
			return bills;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<ProcessBill> getUnitBillByShipmentDetailsForSendSSR(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/unit-bill/list/send-cont/ssr";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<ProcessBill>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<ProcessBill>>() {});
			List<ProcessBill> bills= response.getBody();
			return bills;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Integer checkBookingNoForSendFReceiveE(String bookingNo, String fe) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/check/booking-no/"+ bookingNo + "/fe/" + fe;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			Integer result = restTemplate.getForObject(url, Integer.class);
			return result;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ShipmentDetail getInforSendFReceiveE(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/infor/send-full-receive-e";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ShipmentDetail result = restTemplate.postForObject(url, shipmentDetail, ShipmentDetail.class);
			return result;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Integer getIndexContForSsrByContainerNo(String containerNo) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/index/container-master-ssr/" + containerNo;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			Integer index = restTemplate.getForObject(url, Integer.class);
			return index;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}
	/***
	 * input: vslNm, voyNo, year, sztp,booking
	 * @param shipmentDetail
	 * @return
	 */
	@Override
	public Integer getIndexBooking(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/booking/index";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			Integer index = restTemplate.postForObject(url, shipmentDetail, Integer.class);
			if (index == null || index == 0) {
				index = 1;
			}
			return index;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}
	/***
	 * input: blNo, containerNo
	 */

	@Override
	public PickupHistory getLocationForReceiveF(PickupHistory pickupHistory) {
		try {
			String blNo = pickupHistory.getShipment().getBlNo();
			String containerNo = pickupHistory.getContainerNo();
			String url = Global.getApiUrl() + "/shipmentDetail/location/bl-no/" + blNo +"/container-no/" + containerNo;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ShipmentDetail location = restTemplate.getForObject(url, ShipmentDetail.class);
			if(location != null) {
				pickupHistory.setBlock(location.getBlock());
				pickupHistory.setBay(location.getBay());
				pickupHistory.setLine(String.valueOf(location.getRow()));
				pickupHistory.setTier(String.valueOf(location.getTier()));
			}
			return pickupHistory;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}
	/***
	 * input: containerNo, blNo, bookingNo, vslNm, voyNo, sztp, opeCode, fe
	 */

	@Override
	public String checkContainerStatus(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/container-status";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String containerStatus = restTemplate.postForObject(url, shipmentDetail, String.class);
			return containerStatus;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> getBlockList(String keyword) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/block/list/keyword/" + keyword;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
			List<String> blockList = response.getBody();
			return blockList;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> getAreaList(String keyword) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/area/list/keyword/" + keyword;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
			List<String> areaList = response.getBody();
			return areaList;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Boolean checkContReserved(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/check/reserved";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			Boolean rs = restTemplate.postForObject(url, shipmentDetail, Boolean.class);
			return rs;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Integer checkTheNumberOfContainersNotOrderedForSendContFull(String bookingNo, String sztp) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/send-cont-full/container-amount/booking-no/" + bookingNo + "/sztp/" + sztp + "/check/not-ordered";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			Integer rs = restTemplate.getForObject(url, Integer.class);
			return rs;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get list consignee with tax code
	 * 
	 * @param partnerReq
	 * @return	List<shipment>
	 */
	@Override
	public List<PartnerInfoDto> getListConsigneeWithTaxCode(PartnerInfoDto partnerReq) {
		try {
			String url = Global.getApiUrl() + "/consignee/list";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity httpEntity = new HttpEntity<PartnerInfoDto>(partnerReq);
			ResponseEntity<List<PartnerInfoDto>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<PartnerInfoDto>>() {});
			List<PartnerInfoDto> partners= response.getBody();
			return partners;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String getSztpByContainerNo(String containerNo) {
		try {
			String url = Global.getApiUrl() + "/containerNo/" + containerNo + "/sztp";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String rs = restTemplate.getForObject(url, String.class);
			return rs;
		} catch (Exception e) {
			logger.error("CATOS Api get sztp by container no error: ", e);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get tax code by snm group name
	 * 
	 * @param consignee
	 * @return String
	 */
	@Override
	public String getTaxCodeBySnmGroupName(String consignee) {
		try {
			String url = Global.getApiUrl() + "/consignee/" + consignee + "/taxCode";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String rs = restTemplate.getForObject(url, String.class);
			return rs;
		} catch (Exception e) {
			logger.error("CATOS Api get sztp by container no error: ", e);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get Coordinat Of Containers list by job order no
	 * 
	 * @param jobOrder
	 */
	public List<ShipmentDetail> getCoordinateOfContainersByJobOrderNo(String jobOrder) {
		try {
			String url = Global.getApiUrl() + "/jobOrder/" + jobOrder + "/blNo";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String blNo = restTemplate.getForObject(url, String.class);
			return getCoordinateOfContainers(blNo);
		} catch (Exception e) {
			logger.error("CATOS Api get coordinate by job order no: ", e);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Select shipment detail by job order
	 * 
	 * @param jobOrder
	 * @return List<ShipmentDetail>
	 */
	@Override
	public List<ShipmentDetail> selectShipmentDetailByJobOrder(String jobOrder) {
		try {
			String url = Global.getApiUrl() + "/jobOrder/" + jobOrder + "/blNo";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String blNo = restTemplate.getForObject(url, String.class);
			return selectShipmentDetailsByBLNo(blNo);
		} catch (Exception e) {
			logger.error("CATOS Api get shipment dettail by job order no: ", e);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get bl no by order job no
	 * 
	 * @param jobOrder
	 * @return
	 */
	@Override
	public String getBlNoByOrderJobNo(String jobOrder) {
		try {
			String url = Global.getApiUrl() + "/jobOrder/" + jobOrder + "/blNo";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String rs = restTemplate.getForObject(url, String.class);
			return rs;
		} catch (Exception e) {
			logger.error("CATOS Api get shipment dettail by job order no: ", e);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get consignee name by tax code
	 * 
	 * @param taxCode
	 * @return Shipment
	 */
	@Override
	public PartnerInfoDto getConsigneeNameByTaxCode(String taxCode) {
		String url = Global.getApiUrl() + "/shipmentDetail/getConsigneeNameByTaxCode/"+taxCode;
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		PartnerInfoDto partner = restTemplate.getForObject(url, PartnerInfoDto.class);
		return  partner;
	}
	
	/**
	 * Get opr code list
	 * 
	 * @return
	 */
	@Override
	public List<String> getOprCodeList() {
		List<String> oprCodeList = (List<String>) CacheUtils.get("oprCodeList");
		if (oprCodeList == null) {
			String url = Global.getApiUrl() + "/opr/list";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
			oprCodeList = response.getBody();
			// put to cache
			CacheUtils.put("oprCodeList", oprCodeList);
		}
		return oprCodeList;
	}
	
	/**
	 * Select vessel voyage berth plan without ope code
	 * 
	 * @return
	 */
	@Override
	public List<ShipmentDetail> selectVesselVoyageBerthPlanWithoutOpe() {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/berthplan/vessel-voyage/list" ;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<ShipmentDetail>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ShipmentDetail>>() {});
			List<ShipmentDetail> vesselAndVoyages = response.getBody();
			return vesselAndVoyages;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get booking info from catos by booking no
	 * and user voy (ope code + voy no)
	 * 
	 * @param bookingNo
	 * @param userVoy
	 * @return List Booking info object
	 */
	@Override
	public List<BookingInfo> getBookingInfo(String bookingNo, String userVoy) {
		try {
			String url = Global.getApiUrl() + "/booking-info/" + bookingNo + "/user-voy/" + userVoy;
			logger.debug("Call CATOS API get booking info:{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<BookingInfo>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<BookingInfo>>() {});
			List<BookingInfo> bookingInfos = response.getBody();
			return  bookingInfos;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get booking info: ", e);
			return null;
		}
	}
	/**
	 * Get orderNo in Inventory from catos by shipmentDetail for OM support orderRegister
	 */
	@Override
	public String getOrderNoInInventoryByShipmentDetail(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/inventory/order-no";
			logger.debug("Call CATOS API get orderNo:{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String orderNo = restTemplate.postForObject(url, shipmentDetail, String.class);
			return  orderNo;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get OrderNo Inventory: ", e);
			return null;
		}
	}
	/**
	 * Get orderNo in Reserve from catos by shipmentDetail for OM support orderRegister
	 */
	@Override
	public String getOrderNoInReserveByShipmentDetail(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/reserve/order-no";
			logger.debug("Call CATOS API get orderNo:{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String orderNo = restTemplate.postForObject(url, shipmentDetail, String.class);
			return  orderNo;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get OrderNo Reserve: ", e);
			return null;
		}
	}
	/**
	 * Get InvoiceNo by OrderNo for OM support orderRegister
	 */

	@Override
	public String getInvoiceNoByOrderNo(String orderNo) {
		try {
			String url = Global.getApiUrl() + "/order-no/" + orderNo +"/get/invoice-no";
			logger.debug("Call CATOS API get invoiceNo:{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String invoiceNo = restTemplate.getForObject(url, String.class);
			return  invoiceNo;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get invoiceNo: ", e);
			return null;
		}
	}
	/**
	 * 
	 * getCoordinateOfContainers for Carrier
	 */

	@Override
	public List<ShipmentDetail> selectCoordinateOfContainersByShipmentDetail(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetai/inventory/position";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<ShipmentDetail>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<ShipmentDetail>>() {});
			List<ShipmentDetail> coordinates = response.getBody();
			return coordinates;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while call CATOS Api", e);
			return null;
		}
	}
	
	/**
	 * Get block list for carrier where container of carrier is exist in depot
	 * 
	 * @param shipmentDetail
	 * @return List string block
	 */
	@Override
	public List<String> getBlocksForCarrier(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/carrier/blocks";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<String>>() {});
			List<String> blocks = response.getBody();
			return blocks;
		}catch (Exception e) {
			logger.error("Error while call CATOS Api get blocks", e);
			return null;
		}
	}
	
	/**
	 * Get bay list for carrier where container of carrier is exist in depot
	 * 
	 * @param shipmentDetail
	 * @return List string bay
	 */
	@Override
	public List<String> getBaysForCarrier(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/carrier/bays";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<String>>() {});
			List<String> bays = response.getBody();
			return bays;
		}catch (Exception e) {
			logger.error("Error while call CATOS Api get bays", e);
			return null;
		}
	}
	
	/**
	 * Get trucker from column ptnr_code in catos by tax code (reg_no)
	 * 
	 * @param taxCode
	 * @return String
	 */
	public String getTruckerByTaxCode(String taxCode) {
		try {
			String url = Global.getApiUrl() + "/taxCode/" + taxCode + "/trucker";
			logger.debug("Call CATOS API get trucker:{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String trucker = restTemplate.getForObject(url, String.class);
			return  trucker;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get trucker: ", e);
			return null;
		}
	}
}
