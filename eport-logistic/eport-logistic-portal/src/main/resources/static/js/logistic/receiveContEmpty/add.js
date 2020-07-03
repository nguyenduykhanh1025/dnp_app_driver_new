var prefix = ctx + "logistic/receive-cont-empty";

$("#form-add-shipment").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.ajax({
            url: prefix + "/unique/booking-no/" + $("#bookingNo").val(),
            method: "GET",
        }).done(function (result) {
            if (result.code == 0) {
                $("#bookingNo").removeClass("error-input");
                $.operate.save(prefix + "/addShipment", $('#form-add-shipment').serialize());
                parent.loadTable();
            } else {
                $.modal.msgError("Số book đã tồn tại!");
                $("#bookingNo").addClass("error-input");
            }
        });
    }
}

function checkBookingNoUnique() {
    if ($("#bookingNo").val() != null && $("#bookingNo").val() != '') {
        $.ajax({
            url: prefix + "/unique/booking-no/" + $("#bookingNo").val(),
            method: "GET",
        }).done(function (result) {
            if (result.code == 0) {
                $("#bookingNo").removeClass("error-input");
            } else {
                $.modal.msgError("Số book đã tồn tại!");
                $("#bookingNo").addClass("error-input");
            }
        });
    }
}

function loadGroupName() {
    if ($("#taxCode").val() != null && $("#taxCode").val() != '') {
        $.ajax({
            url: "/logistic/company/" + $("#taxCode").val(),
            method: "GET",
        }).done(function (result) {
            if (result.code == 0) {
                $("#groupName").val(result.groupName);
                $("#taxCode").removeClass("error-input");
            } else {
                $.modal.msgError("Không tìm ra mã số thuế!");
                $("#taxCode").addClass("error-input");
            }
        });
    } else {
        $("#groupName").val('');
    }
}