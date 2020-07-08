package vn.com.irtech.eport.carrier.controller;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import vn.com.irtech.eport.carrier.domain.CarrierAccount;
import vn.com.irtech.eport.carrier.domain.CarrierGroup;
import vn.com.irtech.eport.carrier.dto.EdiDataReq;
import vn.com.irtech.eport.carrier.dto.EdiHashcodeReq;
import vn.com.irtech.eport.carrier.dto.EdiReq;
import vn.com.irtech.eport.carrier.dto.EdiRes;
import vn.com.irtech.eport.carrier.service.ICarrierAccountService;
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
	private ICarrierAccountService carrierAccountService;

	@Autowired
	private IEdiService ediService;

	@PostMapping("/sendarrayedidata")
	@ResponseBody
	public ResponseEntity<EdiRes> sendArrayEdiData(@RequestBody EdiReq ediReq) {
		String transactionId = RandomStringUtils.randomAlphabetic(10);

		// Validate
		if (ediReq.getSiteId() == null) {
			throw new EdiApiException(EdiRes.error(HttpServletResponse.SC_PRECONDITION_FAILED,
					"SiteId must not be null.", transactionId));
		}

		if (ediReq.getPartnerCode() == null) {
			throw new EdiApiException(EdiRes.error(HttpServletResponse.SC_PRECONDITION_FAILED,
					"Partner code must not be null.", transactionId));
		}

		if (ediReq.getHashCode() == null) {
			throw new EdiApiException(EdiRes.error(HttpServletResponse.SC_PRECONDITION_FAILED,
					"Hash code must not be null.", transactionId));
		}

		if (CollectionUtils.isEmpty(ediReq.getData())) {
			throw new EdiApiException(
					EdiRes.error(HttpServletResponse.SC_PRECONDITION_FAILED, "Data must not be null.", transactionId));
		}

		// Get carrier group by site id
		CarrierGroup carrierGroup = carrierGroupService.selectCarrierGroupByGroupCode(ediReq.getSiteId());

		if (carrierGroup == null) {
			throw new EdiApiException(EdiRes.error(5002, "Site not found.", transactionId));
		}

		// Get carrier account by partner code
		CarrierAccount carrierAccount = carrierAccountService.selectByEmail(ediReq.getPartnerCode());

		if (carrierAccount == null) {
			throw new EdiApiException(EdiRes.error(5002, "Partner not found.", transactionId));
		}

		// Authentication
		Gson gson = new Gson();
		String plainText = gson.toJson(ediReq.getData());
		PublicKey publicKey = SignatureUtils.getPublicKey(carrierGroup.getApiPublicKey());

		if (publicKey == null || !SignatureUtils.verify(plainText, ediReq.getHashCode(), publicKey)) {
			throw new EdiApiException(EdiRes.error(5001, "The Edi secure key is wrong", transactionId));
		}

		List<EdiDataReq> ediDataReqsSuccess = ediService.executeListEdi(ediReq.getData(), carrierAccount,
				transactionId);

		EdiRes ediRes = EdiRes.success("", transactionId, ediReq.getData());
		ediRes.setData(ediDataReqsSuccess);
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
}