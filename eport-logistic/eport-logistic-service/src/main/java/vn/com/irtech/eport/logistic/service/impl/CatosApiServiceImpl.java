package vn.com.irtech.eport.logistic.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;

import org.apache.poi.ss.usermodel.Header;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.com.irtech.eport.common.config.Global;
import vn.com.irtech.eport.common.json.JSONObject;
import vn.com.irtech.eport.logistic.domain.ProcessBill;
import vn.com.irtech.eport.logistic.domain.ProcessOrder;
import vn.com.irtech.eport.logistic.domain.Shipment;
import vn.com.irtech.eport.logistic.domain.ShipmentDetail;
import vn.com.irtech.eport.logistic.service.ICatosApiService;
@Service
public class CatosApiServiceImpl implements ICatosApiService {

	@Override
	public Shipment getOpeCodeCatosByBlNo(String blNo) {
		String url = Global.getApiUrl() + "/shipmentDetail/getOpeCodeCatosByBlNo/" + blNo;
        RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, Shipment.class);
	}

	@Override
	public Shipment getGroupNameByTaxCode(String taxCode) {
    	String url = Global.getApiUrl() + "/shipmentDetail/getGroupNameByTaxCode/"+taxCode;
		RestTemplate restTemplate = new RestTemplate();
		Shipment shipment = restTemplate.getForObject(url, Shipment.class);
		return  shipment;
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
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
		List<String> listCont = response.getBody();
		return listCont;
	}

	@Override
	public List<String> getPODList(ShipmentDetail shipmentDetail) {
		String url = Global.getApiUrl() + "/shipmentDetail/getPODList";
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity httpEntity = new HttpEntity<ShipmentDetail>(shipmentDetail);
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<String>>(){});
		List<String> pods = response.getBody();
		return pods;
	}

	@Override
	public List<String> getVesselCodeList() {
		String url = Global.getApiUrl() + "/shipmentDetail/getVesselCodeList";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
		List<String> listVessel = response.getBody();
		return listVessel;
	}

	@Override
	public List<String> getConsigneeList() {
		String url = Global.getApiUrl() + "/shipmentDetail/getConsigneeList";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
		List<String> listConsignee = response.getBody();
		return listConsignee;
	}

	@Override
	public List<String> getOpeCodeList() {
		String url = Global.getApiUrl() + "/shipmentDetail/getOpeCodeList";
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
		List<String> listOpeCode = response.getBody();
		return listOpeCode;
	}

	@Override
	public List<String> getVoyageNoList(String vesselCode) {
		String url = Global.getApiUrl() + "/shipmentDetail/getVoyageNoList/" + vesselCode;
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
		List<String> listVoyage = response.getBody();
		return listVoyage;
	}

	@Override
	public int getCountContByBlNo(String blNo) {
		String url = Global.getApiUrl() + "/shipmentDetail/getCountContByBlNo/" + blNo;
		RestTemplate restTemplate = new RestTemplate();
		Integer count = restTemplate.getForObject(url, Integer.class);
		return count.intValue();
	}

	@Override
	public Boolean checkCustomStatus(String containerNo, String voyNo) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/check/custom/" + containerNo + "/" + voyNo;
			RestTemplate restTemplate = new RestTemplate();
			Boolean rs = restTemplate.getForObject(url, Boolean.class);
			return rs;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<ShipmentDetail> getCoordinateOfContainers(String blNo) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/getCoordinateOfContainers/" + blNo;
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<ShipmentDetail>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ShipmentDetail>>() {});
			List<ShipmentDetail> coordinates = response.getBody();
			return coordinates;
		}catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}

	@Override
	public List<ShipmentDetail> selectShipmentDetailsByBLNo(String blNo) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/list/" + blNo;
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<ShipmentDetail>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ShipmentDetail>>() {});
			List<ShipmentDetail> shipmentDetails = response.getBody();
			return shipmentDetails;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}

	}

	@Override
	public ShipmentDetail selectShipmentDetailByContNo(String blNo, String containerNo) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/containerInfor/" + blNo + "/" + containerNo;
			RestTemplate restTemplate = new RestTemplate();
			ShipmentDetail shipmentDetail = restTemplate.getForObject(url, ShipmentDetail.class);
			return shipmentDetail;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}

	@Override
	public List<String> selectVesselCodeBerthPlan(String opeCode) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/berthplan/vessel-code/list/ope-code/" + opeCode ;
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
			List<String> vslCodes = response.getBody();
			return vslCodes;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
		
	}

	@Override
	public String getYearByVslCodeAndVoyNo(String vslCode, String voyNo) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/vessel-code/"+ vslCode +"/voyage/" + voyNo + "/year" ;
			RestTemplate restTemplate = new RestTemplate();
			String year = restTemplate.getForObject(url, String.class);
			return year;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}

	@Override
	public List<String> selectOpeCodeListInBerthPlan() {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/berthplan/ope-code/list" ;
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {});
			List<String> opeCodes = response.getBody();
			return opeCodes;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}

	@Override
	public List<ShipmentDetail> selectVesselVoyageBerthPlan(String opeCode) {
		try {
			String url = Global.getApiUrl() + "/shipmentDetail/berthplan/ope-code/"+ opeCode +"/vessel-voyage/list" ;
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<ShipmentDetail>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ShipmentDetail>>() {});
			List<ShipmentDetail> vesselAndVoyages= response.getBody();
			return vesselAndVoyages;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}

	@Override
	public List<ProcessBill> getUnitBillByShipmentDetailsForReserve(List<ShipmentDetail> shipmentDetails) {
		try {
			String url = Global.getApiUrl() + "/unit-bill/list/send-cont";
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity httpEntity = new HttpEntity<List<ShipmentDetail>>(shipmentDetails);
			ResponseEntity<List<ProcessBill>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<ProcessBill>>() {});
			List<ProcessBill> bills= response.getBody();
			return bills;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}

	@Override
	public List<ProcessBill> getUnitBillByShipmentDetailsForInventory(List<ShipmentDetail> shipmentDetails) {
		try {
			String url = Global.getApiUrl() + "/unit-bill/list/receive-cont";
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity httpEntity = new HttpEntity<List<ShipmentDetail>>(shipmentDetails);
			ResponseEntity<List<ProcessBill>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<ProcessBill>>() {});
			List<ProcessBill> bills= response.getBody();
			return bills;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}

	@Override
	public List<ProcessBill> getUnitBillByShipmentDetailsForReceiveSSR(List<ShipmentDetail> shipmentDetails) {
		try {
			String url = Global.getApiUrl() + "/unit-bill/list/receive-cont/ssr";
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity httpEntity = new HttpEntity<List<ShipmentDetail>>(shipmentDetails);
			ResponseEntity<List<ProcessBill>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<ProcessBill>>() {});
			List<ProcessBill> bills= response.getBody();
			return bills;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}

	@Override
	public List<ProcessBill> getUnitBillByShipmentDetailsForSendSSR(List<ShipmentDetail> shipmentDetails) {
		try {
			String url = Global.getApiUrl() + "/unit-bill/list/send-cont/ssr";
			RestTemplate restTemplate = new RestTemplate();
			HttpEntity httpEntity = new HttpEntity<List<ShipmentDetail>>(shipmentDetails);
			ResponseEntity<List<ProcessBill>> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<ProcessBill>>() {});
			List<ProcessBill> bills= response.getBody();
			return bills;
		} catch (Exception e) {
			e.getStackTrace();
			return null;
		}
	}
}
