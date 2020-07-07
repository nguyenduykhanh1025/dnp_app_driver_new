package vn.com.irtech.eport.carrier.controller;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import vn.com.irtech.eport.carrier.domain.CarrierGroup;
import vn.com.irtech.eport.carrier.dto.EdiDataRes;
import vn.com.irtech.eport.carrier.dto.EdiHashcodeReq;
import vn.com.irtech.eport.carrier.dto.EdiReq;
import vn.com.irtech.eport.carrier.dto.EdiRes;
import vn.com.irtech.eport.carrier.service.ICarrierGroupService;
import vn.com.irtech.eport.common.utils.SignatureUtils;
import vn.com.irtech.eport.framework.web.exception.EdiApiException;

@Controller
@RequestMapping("/edi")
public class CarrierEdiController {

	@Autowired
	private ICarrierGroupService carrierGroupService;

	@PostMapping("/sendarrayedidata")
	@ResponseBody
	public ResponseEntity<EdiRes> sendArrayEdiData(@RequestBody EdiReq ediReq) {
		String transactionId = null;

		// Validate
		if (ediReq.getSiteId() == null) {
			throw new EdiApiException(EdiRes.error(412, "SiteId must not be null.", transactionId));
		}

		if (ediReq.getPartnerCode() == null) {
			throw new EdiApiException(EdiRes.error(412, "Partner code must not be null.", transactionId));
		}

		if (ediReq.getHashCode() == null) {
			throw new EdiApiException(EdiRes.error(412, "Hash code must not be null.", transactionId));
		}

		if (ediReq.getData() == null) {
			throw new EdiApiException(EdiRes.error(412, "Data must not be null.", transactionId));
		}

		// Get carrier group by site id
		CarrierGroup carrierGroup = carrierGroupService.selectCarrierGroupByGroupCode(ediReq.getSiteId());

		if (carrierGroup == null) {
			throw new EdiApiException(EdiRes.error(5002, "Site not found.", transactionId));
		}

		// Authentication
		Gson gson = new Gson();
		String plainText = gson.toJson(ediReq.getData());
		PublicKey publicKey = SignatureUtils.getPublicKey(carrierGroup.getApiPublicKey());

		if (publicKey == null || !SignatureUtils.verify(plainText, ediReq.getHashCode(), publicKey)) {
			throw new EdiApiException(EdiRes.error(5001, "The Edi secure key is wrong", transactionId));
		}

		List<EdiDataRes> data = new ArrayList<EdiDataRes>();

		// TODO:
		data.add(new EdiDataRes());

		EdiRes ediRes = EdiRes.success("", transactionId, data);
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