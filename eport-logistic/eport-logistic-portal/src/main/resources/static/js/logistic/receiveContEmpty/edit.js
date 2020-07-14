var prefix = ctx + "logistic/receive-cont-empty";
var currentBooking = shipment.bookingNo;

// Map data to form
if (shipment != null) {
    $("#id").val(shipment.id);
    $("#shipmentCode").val(shipment.id);
    $("#bookingNo").val(shipment.bookingNo);
    if (shipment.taxCode == taxCode) {
        $('#taxCode').val(taxCode).prop('readonly', true);
    } else {
        $('#taxCodeNotDefault').prop('checked', true);
        $('#taxCode').val(shipment.taxCode);
    }
    $("input[name='specificContFlg'][value='"+shipment.specificContFlg+"']").prop('checked', true);
    $("#containerAmount").val(shipment.containerAmount);
    $("#groupName").val(shipment.groupName);
    $("#remark").val(shipment.remark);
    if (shipment.status > 1) {
        $("#bookingNo").prop('disabled', true);
        $("input[name='specificContFlg']").prop('disabled', true);
    }
    if (shipment.status > 2) {
        $('input:radio[name="taxCodeDefault"]').prop('disabled', true);
        $("#taxCode").prop('disabled', true);
        $("#containerAmount").prop('disabled', true);
    }
}

$('input:radio[name="taxCodeDefault"]').change(function() {
    if ($(this).val() == '1') {
        $('#taxCode').val(taxCode).prop('readonly', true);
        loadGroupName();
    } else {
        $('#taxCode').val(shipment.taxCode).prop('readonly', false);
        $("#groupName").val(shipment.groupName);
        $("#taxCode").removeClass("error-input");
    }
});

$("#form-edit-shipment").validate({
    focusCleanup: true
});

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
                $.modal.alertError("Không tìm ra mã số thuế!<br>Quý khách vui lòng liên hệ đến bộ phận chăm sóc khách hàng 0933.157.159.");
                $("#taxCode").addClass("error-input");
                $("#groupName").val('');
            }
        });
    } else {
        $("#groupName").val('');
    }
}

async function submitHandler() {
    if ($.validate.form()) {
        if ($("#groupName").val() != null && $("#groupName").val() != '') {
            if ($("#bookingNo").val() != currentBooking) {
                let res = await getBookingNoUnique();
                if (res.code == 0) {
                    $.operate.save(prefix + "/shipment/" + shipment.id, $('#form-edit-shipment').serialize());
                    parent.loadTable();
                }
            } else {
                $.operate.save(prefix + "/shipment/" + shipment.id, $('#form-edit-shipment').serialize());
                parent.loadTable();
            }
        } else {
            $.modal.alertError("Không tìm ra mã số thuế!<br>Quý khách vui lòng liên hệ đến bộ phận chăm sóc khách hàng 0933.157.159.");
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
                $.modal.msgError("Số book đã tồn tại!");
                $("#bookingNo").addClass("error-input");
            }
        });
    }
}