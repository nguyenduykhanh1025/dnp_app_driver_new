var prefix = ctx + "logistic/receive-cont-empty";

$("#form-add-shipment").validate({
    focusCleanup: true
});

$('#taxCode').val(taxCode).prop('readonly', true);
loadGroupName();

$('input:radio[name="taxCodeDefault"]').change(function() {
    if ($(this).val() == '1') {
        $('#taxCode').val(taxCode).prop('readonly', true);
        loadGroupName();
    } else {
        $('#taxCode').val('').prop('readonly', false);
        $("#taxCode").removeClass("error-input");
    }
});

async function submitHandler() {
    if ($.validate.form()) {
        if ($("#groupName").val() != null && $("#groupName").val() != '') {
            let res = await getBookingNoUnique();
            if (res.code == 0) {
                save(prefix + "/shipment",$('#form-add-shipment').serialize());
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
                $.modal.alertError("Không tìm ra mã số thuế!<br>Quý khách vui lòng liên hệ đến bộ phận chăm sóc khách hàng 0933.157.159.");
                $("#taxCode").addClass("error-input");
                $("#groupName").val('');
            }
        });
    } else {
        $("#groupName").val('');
    }
}

function save(url, data) {
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