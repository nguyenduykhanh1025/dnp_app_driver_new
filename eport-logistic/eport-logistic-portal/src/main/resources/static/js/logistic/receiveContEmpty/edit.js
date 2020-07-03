var prefix = ctx + "logistic/receive-cont-empty";
var currentBooking = shipment.bookingNo;

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

// Map data to form
if (shipment != null) {
    $("#id").val(shipment.id);
    $("#shipmentCode").val(shipment.id);
    $("#bookingNo").val(shipment.bookingNo);
    $("#taxCode").val(shipment.taxCode);
    $("#containerAmount").val(shipment.containerAmount);
    $("#remark").val(shipment.remark);
    loadGroupName();
}

$("#form-edit-shipment").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        if ($("#bookingNo").val() != currentBooking) {
            $.ajax({
                url: prefix + "/unique/booking-no/" + $("#bookingNo").val(),
                method: "GET",
            }).done(function (result) {
                if (result.code == 0) {
                    $("#bookingNo").removeClass("error-input");
                    $.operate.save(prefix + "/editShipment", $('#form-edit-shipment').serialize());
                    parent.loadTable();
                } else {
                    $.modal.msgError("Số book đã tồn tại!");
                    $("#bookingNo").addClass("error-input");
                }
            });
        } else {
            $.operate.save(prefix + "/editShipment", $('#form-edit-shipment').serialize());
            parent.loadTable();
        }
    }
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
                $.modal.msgError("Số book đã tồn tại!");
                $("#bookingNo").addClass("error-input");
            }
        });
    }
}