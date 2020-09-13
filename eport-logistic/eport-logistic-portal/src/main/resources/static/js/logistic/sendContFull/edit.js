var prefix = ctx + "logistic/send-cont-full";
var currentBooking = shipment.bookingNo;

// Map data to form
if (shipment != null) {
    $("#id").val(shipment.id);
    $("#shipmentCode").val(shipment.id);
    $("#bookingNo").val(shipment.bookingNo);
    $("#opeCode").val(shipment.opeCode);
    $("#containerAmount").val(shipment.containerAmount);
    $("#remark").val(shipment.remark);
    if (shipment.status > 1) {
        $("#bookingNo").prop('disabled', true);
    }
    if (shipment.status > 2) {
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
        } else if (!$("#containerAmount").val()) {
            $.modal.alertWarning("Số lượng container cập nhật không hợp lệ.");
        } else {
            if ($("#bookingNo").val() != currentBooking) {
                let res = await getBookingNoUnique();
                if (res.code == 0) {
                    edit(prefix + "/shipment/" + $('#id').val(), $('#form-edit-shipment').serialize());
                }
            } else {
                edit(prefix + "/shipment/" + $('#id').val(), $('#form-edit-shipment').serialize());
            }
        }
    }
}

function getBookingNoUnique() {
    return $.ajax({
        url: prefix + "/unique/booking-no/" + $("#bookingNo").val(),
        method: "GET",
    })
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
                $.modal.alertError(result.msg);
            }
            $.modal.enable();
        }
    })
}