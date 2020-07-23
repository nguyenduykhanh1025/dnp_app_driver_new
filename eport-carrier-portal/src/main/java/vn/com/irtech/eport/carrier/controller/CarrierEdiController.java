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

import vn.com.irtech.eport.carrier.domain.CarrierGroup;
import vn.com.irtech.eport.carrier.dto.EdiHashcodeReq;
import vn.com.irtech.eport.carrier.dto.EdiReq;
import vn.com.irtech.eport.carrier.dto.EdiRes;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.carrier.service.IEdiService;
import vn.com.irtech.eport.common.utils.SignatureUtils;
import vn.com.irtech.eport.framework.web.exception.EdiApiException;

@Controller
@RequestMapping("/edi")
public class CarrierEdiController {

	@Autowired
	private ICarrierGroupService carrierGroupService;

	@Autowired
	private IEdiService ediService;

	@PostMapping("/sendarrayedidata")
	@ResponseBody
	@Transactional
	public ResponseEntity<EdiRes> sendArrayEdiData(@RequestBody EdiReq ediReq) {
		String transactionId = RandomStringUtils.randomAlphabetic(10);

		// Validate
		this.validateRequest(ediReq, transactionId);

		// Get carrier group by partnerCode
		CarrierGroup carrierGroup = carrierGroupService.selectCarrierGroupByGroupCode(ediReq.getPartnerCode());

		if (carrierGroup == null) {
			throw new EdiApiException(EdiRes.error(HttpServletResponse.SC_PRECONDITION_FAILED, "Partner code is not exist", transactionId, ediReq.getData()));
		}

		// Authentication
		Gson gson = new Gson();
		String plainText = gson.toJson(ediReq.getData());
		PublicKey publicKey = SignatureUtils.getPublicKey(carrierGroup.getApiPublicKey());

		if (publicKey == null || !SignatureUtils.verify(plainText, ediReq.getHashCode(), publicKey)) {
			throw new EdiApiException(
					EdiRes.error(HttpServletResponse.SC_UNAUTHORIZED, "The Edi secure key is wrong", transactionId, ediReq.getData()));
		}

		try {
			ediService.executeListEdi(ediReq.getData(), ediReq.getPartnerCode(), transactionId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new EdiApiException(
					EdiRes.error(HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), transactionId, ediReq.getData()));
		}

		EdiRes ediRes = EdiRes.success("", transactionId, ediReq.getData());
		ediRes.setData(ediReq.getData());
		return ResponseEntity.status(HttpStatus.OK).body(ediRes);
	}

	@PostMapping("/gethashcode")
	@ResponseBody
	public ResponseEntity<String> getHashcode(@RequestBody EdiHashcodeReq ediHashcodeReq) {
		String hashCode = null;
		Gson gson = new Gson();
		String plainText = gson.toJson(ediHashcodeReq.getData());
		PrivateKey pk = SignatureUtils.getPrivateKey(ediHashcodeReq.getPrivateKey());
		hashCode = SignatureUtils.sign(plainText, pk);
		return ResponseEntity.status(HttpStatus.OK).body(hashCode);
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
}