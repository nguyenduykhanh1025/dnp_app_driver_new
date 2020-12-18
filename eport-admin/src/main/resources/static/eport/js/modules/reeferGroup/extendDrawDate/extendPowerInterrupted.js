const PREFIX = ctx + "reefer-group/extend-draw-date";
const objectPaymentList = ["Hãng tàu", "Chủ hàng trả trước", "Chủ hàng trả sau"];
const PAYER_TYPE = {
	customer: 'Customer',
	carriers: 'Carriers'
}
const PAY_TYPE = {
	credit: 'Credit',
	cash: 'Cash'
}

$(document).ready(function () {
	initSelection();
	initDateTimePicker('datetimepickerPowerDrawDate');
	initDateTimePicker('datetimepickerPowerSetupDate');
});

function initSelection() {
	for (let i = 0; i < objectPaymentList.length; ++i) {
		$("#slPaymentInformation").append(new Option(objectPaymentList[i], objectPaymentList[i]));
	}
}

function initDateTimePicker(idElement) {
	$(`#${idElement}`).datetimepicker({
		format: 'dd/mm/yyyy hh:ii',
		language: "vi_VN",
		//minView: "month",
		autoclose: true,
		minuteStep: 30,
		todayBtn: true,
		startDate: new Date()
	});
}

function confirm() {
	let valuePaymentInformation = $('#slPaymentInformation').val();
	let dateDraw = tranferValidatedDate($('#powerDrawDate').val());
	let dateSetup = tranferValidatedDate($('#powerSetupDate').val());
	let payType = getPayType(valuePaymentInformation);
	let payerType = getPayerType(valuePaymentInformation);

	//validate
	if (!valuePaymentInformation || !$('#powerDrawDate').val() || !$('#powerDrawDate').val()) {
		$.modal.alertError("Vui lòng điền đầy đủ các trường bắt buộc.");
		return;
	}

	const payload = {
		dateSetPower: dateSetup.getTime(),
		dateGetPower: dateDraw.getTime(),
		payType: payType,
		payerType: payerType,
	}

	$.ajax({
		url: PREFIX + "/shipment-detail/" + shipmentDetailId + "/extend-power-interrupted",
		method: "post",
		contentType: "application/json",
		accept: "text/plain",
		data: JSON.stringify(payload),
		dataType: "text",
		success: function (result) {
			if (JSON.parse(result).code == 500) {
				$.modal.alertError(JSON.parse(result).msg);
			} else {
				parent.submitDataFromExtendPowerInterruptedModal(result);
				$.modal.close();
			}
		},
	});
}

function closeForm() {
	$.modal.close();
}

function tranferValidatedDate(dateFromInput) {
	const dataArr = dateFromInput.split("/");
	let day = dataArr[0];
	let month = dataArr[1];
	let year = dataArr[2];
	return new Date(`${month}-${day}-${year}`);
}
function getPayType(data) {
	if (data == objectPaymentList[0]) {
		// hãng tàu thanh toán
		return null;
	} else if (data == objectPaymentList[1]) {
		// là chủ hàng trả rước
		return "Credit";
	} else {
		// là chủ hàng trả sau
		return "Cash"
	}
}

function getPayerType(data) {
	if (data == objectPaymentList[0]) {
		// hãng tàu thanh toán
		return PAYER_TYPE.carriers;
	} else {
		return PAYER_TYPE.customer;
	}
}