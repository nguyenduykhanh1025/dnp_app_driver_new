package vn.com.irtech.eport.logistic.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.json.JSONObject;
import vn.com.irtech.eport.common.utils.CacheUtils;
import vn.com.irtech.eport.logistic.domain.PickupHistory;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.dto.BerthPlanInfo;
import vn.com.irtech.eport.logistic.dto.ContainerHistoryDto;
import vn.com.irtech.eport.logistic.dto.ContainerHoldInfo;
import vn.com.irtech.eport.logistic.form.BookingInfo;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
import vn.com.irtech.eport.system.dto.ContainerInfoDto;
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
		String url = Global.getApiUrl() + "/shipmentDetail/getGroupNameByTaxCode/" + taxCode;
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		PartnerInfoDto result = restTemplate.getForObject(url, PartnerInfoDto.class);
		return result;
	}

	@Override
	public ProcessOrder getYearBeforeAfter(String vessel, String voyage) {
		String url = Global.getApiUrl() + "/processOrder/getYearBeforeAfter/" + vessel + "/" + voyage;
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, ProcessOrder.class);
	}

	@Override
	public List<String> checkContainerReserved(String containerNos) {
		String url = Global.getApiUrl() + "/shipmentDetail/checkContReserved/" + containerNos;
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<String>>() {
				});
		List<String> listCont = response.getBody();
		return listCont;
	}

	@Override
	public List<ContainerInfoDto> getContainerPickup(String containerNos, String userVoy) {
		String url = Global.getApiUrl() + "/shipmentDetail/getContainerPickup";
		logger.debug("Call CATOS API :{}", url);
		// create request object
		JSONObject containerReq = new JSONObject();
		containerReq.put("containerNos", containerNos);
		containerReq.put("userVoy", userVoy);

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(containerReq);
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<List<String>>() {
				});

		List<String> listCont = response.getBody();
		if (listCont == null) {
			return new ArrayList<ContainerInfoDto>();
		}
		// split string by /
		List<ContainerInfoDto> containerInfoList = new ArrayList<>();
		ContainerInfoDto dto = null;
		String[] contArr = null;
		for (String cont : listCont) {
			// CNTR_NO / IX_CD / USER_VOY / PTNR_CODE / JOB_ODR_NO2
			contArr = cont.split("/");
			if (contArr.length > 0) {
				dto = new ContainerInfoDto();
				dto.setCntrNo(contArr[0]);
				dto.setIxCd(contArr[1]);
				dto.setUserVoy(contArr[2]);
				dto.setPtnrCode(contArr[3]);
				dto.setJobOdrNo2(contArr[4]);
				containerInfoList.add(dto);
			}
		}

		return containerInfoList;
	}

	@Override
	public List<String> getPODList(ShipmentDetail shipmentDetail) {
		String url = Global.getApiUrl() + "/shipmentDetail/getPODList";
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<ShipmentDetail> httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
				new ParameterizedTypeReference<List<String>>() {
				});
		List<String> pods = response.getBody();
		return pods;
	}

	@Override
	public List<String> getOPRList(ShipmentDetail shipmentDetail) {
		String url = Global.getApiUrl() + "/shipmentDetail/getOPRList";
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<ShipmentDetail> httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<String>>() { });
		List<String> pods = response.getBody();
		return pods;
	}

	@Override
	public List<String> getVesselCodeList() {
		String url = Global.getApiUrl() + "/shipmentDetail/getVesselCodeList";
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<String>>() {
				});
		List<String> listVessel = response.getBody();
		return listVessel;
	}

	@Override
	public List<String> getConsigneeList() {
		String url = Global.getApiUrl() + "/shipmentDetail/getConsigneeList";
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<String>>() {
				});
		List<String> listConsignee = response.getBody();
		return listConsignee;
	}

	@Override
	public List<String> getOpeCodeList() {
		String url = Global.getApiUrl() + "/shipmentDetail/getOpeCodeList";
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<String>>() {
				});
		List<String> listOpeCode = response.getBody();
		return listOpeCode;
	}

	@Override
	public List<String> getVoyageNoList(String vesselCode) {
		String url = Global.getApiUrl() + "/shipmentDetail/getVoyageNoList/" + vesselCode;
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<String>>() {
				});
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
			ResponseEntity<List<ShipmentDetail>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<ShipmentDetail>>() {
					});
			List<ShipmentDetail> coordinates = response.getBody();
			return coordinates;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while call CATOS Api", e);
			return null;
		}
	}

	@Override
	public List<ContainerInfoDto> selectShipmentDetailsByBLNo(String blNo) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/list";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();

			JSONObject reqEntity = new JSONObject();
			reqEntity.put("blNo", blNo);
			HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(reqEntity);

			ResponseEntity<List<ContainerInfoDto>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<ContainerInfoDto>>() {
					});

			List<ContainerInfoDto> shipmentDetails = response.getBody();
			return shipmentDetails;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while call CATOS Api", e);
			return null;
		}

	}

	@Override
	public List<ContainerInfoDto> getContainerInfoListByBlNo(String blNo) {
		try {
			String url = Global.getApiUrl() + "/bl-no/containers";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();

			JSONObject reqEntity = new JSONObject();
			reqEntity.put("blNo", blNo);
			HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(reqEntity);

			ResponseEntity<List<ContainerInfoDto>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<ContainerInfoDto>>() {
					});

			List<ContainerInfoDto> shipmentDetails = response.getBody();
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
			ResponseEntity<ShipmentDetail> responseEntity = restTemplate.postForEntity(url, shipmentDetail,
					ShipmentDetail.class);
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
			String url = Global.getApiUrl() + "/shipmentDetail/berthplan/vessel-code/list/ope-code/" + opeCode;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<String>>() {
					});
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
			String url = Global.getApiUrl() + "/shipmentDetail/vessel-code/" + vslCode + "/voyage/" + voyNo + "/year";
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
			String url = Global.getApiUrl() + "/shipmentDetail/berthplan/ope-code/list";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<String>>() {
					});
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
			String url = Global.getApiUrl() + "/shipmentDetail/berthplan/ope-code/" + opeCode + "/vessel-voyage/list";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<ShipmentDetail>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<ShipmentDetail>>() {
					});
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
			HttpEntity<ShipmentDetail> httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<ProcessBill>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<ProcessBill>>() {
					});
			List<ProcessBill> bills = response.getBody();
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
			HttpEntity<ShipmentDetail> httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<ProcessBill>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<ProcessBill>>() {
					});
			List<ProcessBill> bills = response.getBody();
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
			HttpEntity<ShipmentDetail> httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<ProcessBill>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<ProcessBill>>() {
					});
			List<ProcessBill> bills = response.getBody();
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
			HttpEntity<ShipmentDetail> httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<ProcessBill>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<ProcessBill>>() {
					});
			List<ProcessBill> bills = response.getBody();
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
			String url = Global.getApiUrl() + "/shipmentDetail/check/booking-no/" + bookingNo + "/fe/" + fe;
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
	 * 
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
			String url = Global.getApiUrl() + "/shipmentDetail/location/bl-no/" + blNo + "/container-no/" + containerNo;
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ShipmentDetail location = restTemplate.getForObject(url, ShipmentDetail.class);
			if (location != null) {
				pickupHistory.setBlock(location.getBlock());
				pickupHistory.setBay(location.getBay());
				pickupHistory.setLine(String.valueOf(location.getRoww()));
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
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<String>>() {
					});
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
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<String>>() {
					});
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
			String url = Global.getApiUrl() + "/shipmentDetail/send-cont-full/container-amount/booking-no/" + bookingNo
					+ "/sztp/" + sztp + "/check/not-ordered";
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
	 * @return List<shipment>
	 */
	@Override
	public List<PartnerInfoDto> getListConsigneeWithTaxCode(PartnerInfoDto partnerReq) {
		try {
			String url = Global.getApiUrl() + "/consignee/list";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<PartnerInfoDto> httpEntity = new HttpEntity<PartnerInfoDto>(partnerReq);
			ResponseEntity<List<PartnerInfoDto>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<PartnerInfoDto>>() {
					});
			List<PartnerInfoDto> partners = response.getBody();
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
	public List<ContainerInfoDto> selectShipmentDetailByJobOrder(String jobOrder) {
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
		String url = Global.getApiUrl() + "/shipmentDetail/getConsigneeNameByTaxCode/" + taxCode;
		logger.debug("Call CATOS API :{}", url);
		RestTemplate restTemplate = new RestTemplate();
		PartnerInfoDto partner = restTemplate.getForObject(url, PartnerInfoDto.class);
		return partner;
	}

	/**
	 * Get opr code list
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getOprCodeList() {
		List<String> oprCodeList = (List<String>) CacheUtils.get("oprCodeList");
		if (oprCodeList == null) {
			String url = Global.getApiUrl() + "/opr/list";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<String>>() {
					});
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
			String url = Global.getApiUrl() + "/shipmentDetail/berthplan/vessel-voyage/list";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<ShipmentDetail>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<ShipmentDetail>>() {
					});
			List<ShipmentDetail> vesselAndVoyages = response.getBody();
			return vesselAndVoyages;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api", e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get booking info from catos by booking no and user voy (ope code + voy no)
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
			ResponseEntity<List<BookingInfo>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<BookingInfo>>() {
					});
			List<BookingInfo> bookingInfos = response.getBody();
			return bookingInfos;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get booking info: ", e);
			return null;
		}
	}

	/**
	 * Get orderNo in Inventory from catos by shipmentDetail for OM support
	 * orderRegister
	 */
	@Override
	public String getOrderNoInInventoryByShipmentDetail(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/inventory/order-no";
			logger.debug("Call CATOS API get orderNo:{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String orderNo = restTemplate.postForObject(url, shipmentDetail, String.class);
			return orderNo;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get OrderNo Inventory: ", e);
			return null;
		}
	}

	/**
	 * Get orderNo in Reserve from catos by shipmentDetail for OM support
	 * orderRegister
	 */
	@Override
	public String getOrderNoInReserveByShipmentDetail(ShipmentDetail shipmentDetail) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/reserve/order-no";
			logger.debug("Call CATOS API get orderNo:{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String orderNo = restTemplate.postForObject(url, shipmentDetail, String.class);
			return orderNo;
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
			String url = Global.getApiUrl() + "/order-no/" + orderNo + "/get/invoice-no";
			logger.debug("Call CATOS API get invoiceNo:{}", url);
			RestTemplate restTemplate = new RestTemplate();
			String invoiceNo = restTemplate.getForObject(url, String.class);
			return invoiceNo;
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
			HttpEntity<ShipmentDetail> httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<ShipmentDetail>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<ShipmentDetail>>() {
					});
			List<ShipmentDetail> coordinates = response.getBody();
			return coordinates;
		} catch (Exception e) {
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
			HttpEntity<ShipmentDetail> httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<String>>() {
					});
			List<String> blocks = response.getBody();
			return blocks;
		} catch (Exception e) {
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
			HttpEntity<ShipmentDetail> httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<String>>() {
					});
			List<String> bays = response.getBody();
			return bays;
		} catch (Exception e) {
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
			return trucker;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get trucker: ", e);
			return null;
		}
	}

	/**
	 * Get list container no not hold terminal
	 * 
	 * @param containers
	 * @return List<String>
	 */
	@Override
	public List<String> getContainerListHoldRelease(ContainerHoldInfo containerHoldInfo) {
		try {
			String url = Global.getApiUrl() + "/hold-check/containers";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<ContainerHoldInfo> httpEntity = new HttpEntity<ContainerHoldInfo>(containerHoldInfo);
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<String>>() {
					});
			List<String> containerList = response.getBody();
			return containerList;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get list container to hold or release", e);
			return null;
		}
	}

	/**
	 * Check if consignee is exist in catos
	 * 
	 * @param consignee
	 * @return number of consignee record in catos
	 */
	@Override
	public Integer checkConsigneeExistInCatos(String consignee) {
		try {
			String url = Global.getApiUrl() + "/consignee/" + consignee + "/exist";
			logger.debug("Call CATOS API check consignee exist", url);
			RestTemplate restTemplate = new RestTemplate();
			Integer count = restTemplate.getForObject(url, Integer.class);
			return count;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api check consignee exist: ", e);
			return 0;
		}
	}

	/**
	 * Check if pod (discharge port) exist in catos
	 * 
	 * @param pod
	 * @return number of pod record in catos
	 */
	@Override
	public Integer checkPodExistIncatos(String pod) {
		try {
			String url = Global.getApiUrl() + "/pod/" + pod + "/exist";
			logger.debug("Call CATOS API check pod exist", url);
			RestTemplate restTemplate = new RestTemplate();
			Integer count = restTemplate.getForObject(url, Integer.class);
			return count;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api check pod exist: ", e);
			return 0;
		}
	}

	/**
	 * Get container info by cont no list separated by comma
	 * 
	 * @param containerNo
	 * @return List<ContaienrInfoDto>
	 */
	@Override
	public List<ContainerInfoDto> getContainerInfoDtoByContNos(String containerNos) {
		try {
			String url = Global.getApiUrl() + "/container/info";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			Map<String, Object> map = new HashMap<>();
			map.put("containerNos", containerNos);
			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);
			ResponseEntity<List<ContainerInfoDto>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<ContainerInfoDto>>() {
					});
			List<ContainerInfoDto> containerInfoDtos = response.getBody();
			return containerInfoDtos;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get container info: ", e);
			return null;
		}
	}

	/**
	 * Get container info by cont no list separated by comma
	 * 
	 * @param containerNo
	 * @return List<ContaienrInfoDto>
	 */
	@Override
	public List<ContainerInfoDto> getAllContainerInfoDtoByContNos(String containerNos) {
		try {
			String url = Global.getApiUrl() + "/container/info/all";
			logger.debug("Call CATOS API :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			Map<String, Object> map = new HashMap<>();
			map.put("containerNos", containerNos);
			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);
			ResponseEntity<List<ContainerInfoDto>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<ContainerInfoDto>>() {
					});
			List<ContainerInfoDto> containerInfoDtos = response.getBody();
			return containerInfoDtos;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get container info: ", e);
			return null;
		}
	}
	/**
	 * Get container history info
	 * 
	 * @param containerHistory
	 * @return List<ContainerHistoryDto>
	 */
	@Override
	public List<ContainerHistoryDto> getContainerHistory(ContainerHistoryDto containerHistory) {
		try {
			String url = Global.getApiUrl() + "/container/history";
			logger.debug("Call CATOS API get container history :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<ContainerHistoryDto> httpEntity = new HttpEntity<ContainerHistoryDto>(containerHistory);
			ResponseEntity<List<ContainerHistoryDto>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<ContainerHistoryDto>>() {
					});
			List<ContainerHistoryDto> containerHistories = response.getBody();
			return containerHistories;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get container history", e);
			return new ArrayList<>();
		}
	}

	/**
	 * Get berth plan information
	 * 
	 * @param berthPlanInfo
	 * @return BerthPlanInfo
	 */
	@Override
	public BerthPlanInfo getBerthPlanInfo(BerthPlanInfo berthPlanInfo) {
		try {
			String url = Global.getApiUrl() + "/berth-plan/info";
			logger.debug("Call CATOS API get berth plan info :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<BerthPlanInfo> response = restTemplate.postForEntity(url, berthPlanInfo,
					BerthPlanInfo.class);
			BerthPlanInfo berthPlanInfoRes = response.getBody();
			return berthPlanInfoRes;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get berth plan info", e);
			return null;
		}
	}

	/**
	 * Get container info by ContainerInfoDto object
	 * 
	 * @param containerInfoDto
	 * @return List<ContainerInfoDto>
	 */
	@Override
	public List<ContainerInfoDto> getContainerInfoListByCondition(ContainerInfoDto containerInfoDto) {
		try {
			String url = Global.getApiUrl() + "/container-full/list";
			logger.debug("Call CATOS API get container info :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity<ContainerInfoDto> httpEntity = new HttpEntity<ContainerInfoDto>(containerInfoDto);
			ResponseEntity<List<ContainerInfoDto>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity,
					new ParameterizedTypeReference<List<ContainerInfoDto>>() {
					});
			List<ContainerInfoDto> containerInfoDtos = response.getBody();
			return containerInfoDtos;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get container info", e);
			return null;
		}
	}

	/**
	 * Get partner info
	 * 
	 * @param PartnerInfoDto partnerInfoParam
	 * @return PartnerInfoDto
	 */
	@Override
	public PartnerInfoDto getPartnerInfo(PartnerInfoDto partnerInfoParam) {
		try {
			String url = Global.getApiUrl() + "/partner-info";
			logger.debug("Call CATOS API get partner info :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<PartnerInfoDto> response = restTemplate.postForEntity(url, partnerInfoParam,
					PartnerInfoDto.class);
			PartnerInfoDto partnerInfoDto = response.getBody();
			return partnerInfoDto;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get berth plan info", e);
			return null;
		}
	}

	/**
	 * Get container info from table reserve catos
	 * 
	 * @param String containerNos
	 * @return List<ContainerInfoDto>
	 */
	@Override
	public List<ContainerInfoDto> getContainerInfoReserve(String containerNos) {
		try {
			String url = Global.getApiUrl() + "/reserve/cntr-info";
			logger.debug("Call CATOS API get container info :{}", url);
			RestTemplate restTemplate = new RestTemplate();
			Map<String, Object> map = new HashMap<>();
			map.put("containerNos", containerNos);
			HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(map);
			ResponseEntity<List<ContainerInfoDto>> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity,
					new ParameterizedTypeReference<List<ContainerInfoDto>>() {
					});
			List<ContainerInfoDto> containerInfoDtos = response.getBody();
			return containerInfoDtos;
		} catch (Exception e) {
			logger.error("Error while call CATOS Api get container info from reserve table", e);
			return null;
		}
	}
}
