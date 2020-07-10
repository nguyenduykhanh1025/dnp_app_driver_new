var prefix = ctx + "logistic/receive-cont-empty";

$("#form-add-shipment").validate({
    focusCleanup: true
});

async function submitHandler() {
    if ($.validate.form()) {
        if ($("#groupName").val() != null && $("#groupName").val() != '') {
            let res = await getBookingNoUnique();
            if (res.code == 0) {
                await $.operate.save(prefix + "/shipment", $('#form-add-shipment').serialize());
                parent.loadTable();
            }
        } else {
            $.modal.msgError("Không tìm thấy mã số thuế!");
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
                $("#groupName").val('');
            }
        });
    } else {
        $("#groupName").val('');
    }
}