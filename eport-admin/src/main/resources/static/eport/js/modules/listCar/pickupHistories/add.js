const PREFIX = ctx + "listCar/pickupHistories";
const SERVICE_TYPE = {
	SERVICE_PICKUP_FULL: 1,
	SERVICE_PICKUP_EMPTY: 3,
	SERVICE_DROP_FULL: 4,
	SERVICE_DROP_EMPTY: 2
}

$(document).ready(function () {
	initInputElementHtml();
	initSelectLogisticGruops();
	handleChangeRadioServiceType();
});

function initInputElementHtml() {
	// default cheked pickup cont full
	const serviceType = $('input[name=serviceType]:checked', '#form-detail-add').val();
	if (serviceType == SERVICE_TYPE.SERVICE_PICKUP_FULL) {
		$("#in-container-no").prop('disabled', true);
		$("#in-bill-no").prop('disabled', false);
		$("#in-booking-no").prop('disabled', true);
		$("#in-job-oder").prop('disabled', false);
	} else if (serviceType == SERVICE_TYPE.SERVICE_PICKUP_EMPTY) {
		$("#in-container-no").prop('disabled', true);
		$("#in-bill-no").prop('disabled', true);
		$("#in-booking-no").prop('disabled', false);
		$("#in-job-oder").prop('disabled', false);
	} else if (serviceType == SERVICE_TYPE.SERVICE_DROP_EMPTY) {
		$("#in-container-no").prop('disabled', false);
		$("#in-bill-no").prop('disabled', false);
		$("#in-booking-no").prop('disabled', true);
		$("#in-job-oder").prop('disabled', false);
	} else {
		$("#in-container-no").prop('disabled', false);
		$("#in-bill-no").prop('disabled', true);
		$("#in-booking-no").prop('disabled', false);
		$("#in-job-oder").prop('disabled', false);
	}
}

function initSelectLogisticGruops() {
	if (logisticGroups) {
		logisticGroups.forEach(element => {
			$('#sel-logistic-group').append($('<option>', {
				value: element.id,
				text: element.groupName
			}));
		});

	}
}

function handleChangeRadioServiceType() {
	$('input[type=radio][name=serviceType]').change(function () {
		initInputElementHtml();
	});
}

function onClickSave() {

	$.ajax({
		url: PREFIX + "/add",
		contentType: "application/json",
		method: 'POST',
		data: JSON.stringify({
			serviceType: $('input[name=serviceType]:checked', '#form-detail-add').val(),
			containerNo: $('#in-container-no').val(),
			blNo: $('#in-bill-no').val(),
			bookingNo: $('#in-booking-no').val(),
			jobOrderNo: $('#in-job-oder').val(),
			truckNo: $('#in-truck-no').val(),
			chassisNo: $('#in-chassis-no').val(),
			logisticGroupId: $("select#sel-logistic-group option").filter(":selected").val(),
			userMobilePhone: $('#in-phone-number').val()
		}),
		success: function (res) {
			$.modal.closeLoading();
			if (res.code == 0) {
				$.modal.msgSuccess(res.msg);
				loadTable();
			} else {
				$.modal.msgError(res.msg);
			}
		},
		error: function (err) {
			$.modal.closeLoading();
			$.modal.msgError(err.msg);
		}
	});

}