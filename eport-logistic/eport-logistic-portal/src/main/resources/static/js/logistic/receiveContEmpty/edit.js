var prefix = ctx + "logistic/receive-cont-empty";
var currentBooking = shipment.bookingNo;

// Map data to form
if (shipment != null) {
    $("#id").val(shipment.id);
    $("#shipmentCode").val(shipment.id);
    $("#opeCode").val(shipment.opeCode);
    $("#bookingNo").val(shipment.bookingNo);
    $("input[name='specificContFlg'][value='"+shipment.specificContFlg+"']").prop('checked', true);
    $("#containerAmount").val(shipment.containerAmount);
    $("#remark").val(shipment.remark);
    if (shipment.status > 1) {
        $("#bookingNo").prop('disabled', true);
        $("input[name='specificContFlg']").prop('disabled', true);
    }
    if (shipment.status > 2) {
        $("#containerAmount").prop('disabled', true);
        $("#opeCode").prop('disabled', true);
    }
}

$("#form-edit-shipment").validate({
    focusCleanup: true
});

async function submitHandler() {
    if ($.validate.form()) {
        if ($("#opeCode option:selected").text() == 'Chọn OPR') {
            $.modal.alertWarning("Quý khách chưa chọn mã OPR.");
        } else if ($("#bookingNo").val() != currentBooking) {
            let res = await getBookingNoUnique();
            if (res.code == 0) {
                edit(prefix + "/shipment/" + shipment.id, $('#form-edit-shipment').serialize())
            }
        } else {
            edit(prefix + "/shipment/" + shipment.id, $('#form-edit-shipment').serialize());
        }
    }
}

function getBookingNoUnique() {
    return $.ajax({
        url: prefix + "/unique/booking-no/" + $("#bookingNo").val(),
        method: "GET",
    });
}

function checkBookingNoUnique() {
    if ($("#bookingNo").val() != null && $("#bookingNo").val() != '' && $("#bookingNo").val() != currentBooking) {
        $.ajax({
            url: prefix + "/unique/booking-no/" + $("#bookingNo").val(),
            method: "GET",
        }).done(function (result) {
            if (result.code == 0) {
                $("#bookingNo").removeClass("error-input");
            } else {
                $.modal.alertError(result.msg);
                $("#bookingNo").addClass("error-input");
            }
        });
    }
}

function edit(url, data) {
    $.ajax({
        url: url,
        type: "post",
        dataType: "json",
        data: data,
        beforeSend: function () {
            $.modal.loading("Đang xử lý, vui lòng chờ...");
            $.modal.disable();
        },
        success: function(result) {
            $.modal.closeLoading();
            if (result.code == 0) {
                parent.loadTable(result.msg);
                $.modal.close();
            } else {
                $.modal.msgError(result.msg);
            }
        }
    })
}