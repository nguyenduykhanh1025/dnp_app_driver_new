package vn.com.irtech.eport.carrier.controller;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import vn.com.irtech.eport.carrier.domain.CarrierApi;
import vn.com.irtech.eport.carrier.dto.EdiHashcodeReq;
import vn.com.irtech.eport.carrier.dto.EdiReq;
import vn.com.irtech.eport.carrier.dto.EdiRes;
import vn.com.irtech.eport.carrier.service.ICarrierApiService;
import vn.com.irtech.eport.carrier.service.IEdiService;
import vn.com.irtech.eport.common.annotation.Log;
import vn.com.irtech.eport.common.enums.BusinessType;
import vn.com.irtech.eport.common.enums.OperatorType;
import vn.com.irtech.eport.common.utils.SignatureUtils;
import vn.com.irtech.eport.framework.web.exception.EdiApiException;

@Controller
@RequestMapping("/api/v1/edi")
public class CarrierEdiController {
	
	private static final Logger logger = LoggerFactory.getLogger(CarrierEdiController.class);
	
	private static final String DATE_FORMAT_GSON = "yyyy-MM-dd HH:mm:ss";
	
	@Autowired
	private ICarrierApiService carrierApiService;

	@Autowired
	private IEdiService ediService;

	@PostMapping("/sendarrayedidata")
	@ResponseBody
	@Transactional
	@Log(title = "EDI API", businessType = BusinessType.INSERT, operatorType = OperatorType.SHIPPINGLINE)
	public ResponseEntity<EdiRes> sendArrayEdiData(@RequestBody EdiReq ediReq) {
		String transactionId = RandomStringUtils.randomAlphabetic(10);
		// Validate
		this.validateRequest(ediReq, transactionId);
		// Get carrier group by partnerCode
		CarrierApi carrierApi = carrierApiService.selectCarrierApiByOprCode(ediReq.getPartnerCode());

		// check if carrier exist or API is enabled for this carrier
		if (carrierApi == null || carrierApi.getBlockedFlg() != 0) {
			throw new EdiApiException(EdiRes.error(HttpServletResponse.SC_PRECONDITION_FAILED, "Partner code is not exist", transactionId, ediReq.getData()));
		}
		// Authentication
		Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT_GSON).create();
		String plainText = gson.toJson(ediReq.getData());
		PublicKey publicKey = SignatureUtils.getPublicKey(carrierApi.getApiPublicKey());
		
		logger.debug("Received EDI API: " + gson.toJson(ediReq));
		// Get public key
		if (publicKey == null) {
			logger.debug("EDI API KEY is NULL");
			throw new EdiApiException(EdiRes.error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The Edi key is not created", transactionId, ediReq.getData()));
		}
		if (!SignatureUtils.verify(plainText, ediReq.getHashCode(), publicKey)) {
			logger.debug("EDI Hashcode Failed: " + ediReq.getHashCode());
			throw new EdiApiException(EdiRes.error(HttpServletResponse.SC_UNAUTHORIZED, "The Edi secure key is wrong", transactionId, ediReq.getData()));
		}

		try {
			ediService.executeListEdi(ediReq.getData(), ediReq.getPartnerCode(), carrierApi.getGroupId(), transactionId);
		} catch (Exception e) {
			logger.error("Error while call EDI API", e);
			throw new EdiApiException(EdiRes.error(HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), transactionId, ediReq.getData()));
		}

		EdiRes ediRes = EdiRes.success("", transactionId, ediReq.getData());
		ediRes.setData(ediReq.getData());
		return ResponseEntity.status(HttpStatus.OK).body(ediRes);
	}


	private void validateRequest(EdiReq ediReq, String transactionId) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<EdiReq>> ediReqViolations = validator.validate(ediReq);

		if (!CollectionUtils.isEmpty(ediReqViolations)) {
			for (ConstraintViolation<EdiReq> violation : ediReqViolations) {
				throw new EdiApiException(EdiRes.error(HttpServletResponse.SC_PRECONDITION_FAILED,
						violation.getPropertyPath() + " " + violation.getMessage(), transactionId, ediReq.getData()));
			}
		}
	}

	/**
	 * Create hashcode for testing only
	 * 
	 * @param ediHashcodeReq
	 * @return
	 */
	@PostMapping("/gethashcode")
	@ResponseBody
	public ResponseEntity<String> getHashcode(@RequestBody EdiHashcodeReq ediHashcodeReq) {
		String hashCode = null;
		Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT_GSON).create();
		String plainText = gson.toJson(ediHashcodeReq.getData());
		PrivateKey pk = SignatureUtils.getPrivateKey(ediHashcodeReq.getPrivateKey());
		hashCode = SignatureUtils.sign(plainText, pk);
		return ResponseEntity.status(HttpStatus.OK).body(hashCode);
	}
}