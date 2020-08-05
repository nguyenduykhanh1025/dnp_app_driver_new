var prefix = ctx + "logistic/send-cont-empty";
var currentBill = shipment.blNo;
// Map data to form
if (shipment != null) {
    $("#id").val(shipment.id);
    $("#blNo").val(shipment.blNo);
    $("#shipmentCode").val(shipment.id);
    if (shipment.taxCode == taxCode) {
        $('#taxCode').val(taxCode).prop('readonly', true);
    } else {
        $('#taxCodeNotDefault').prop('checked', true);
        $('#taxCode').val(shipment.taxCode);
    }
    $("#containerAmount").val(shipment.containerAmount);
    $("#groupName").val(shipment.groupName);
    $("#address").val(shipment.address);
    $("#remark").val(shipment.remark);
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
        $("#address").val(shipment.address);
        $("#taxCode").removeClass("error-input");
    }
});

$("#form-edit-shipment").validate({
    focusCleanup: true
});

function loadGroupName() {
    if ($("#taxCode").val() != null && $("#taxCode").val() != '') {
        $.ajax({
            url: ctx + "logistic/company/" + $("#taxCode").val(),
            method: "get"
        }).done(function (result) {
            if (result.code == 0) {
                $("#groupName").val(result.groupName);
                $("#address").val(result.address);
                $("#taxCode").removeClass("error-input");
            } else {
                $.modal.alertError("Không tìm ra mã số thuế!<br>Quý khách vui lòng liên hệ đến bộ phận chăm sóc khách hàng 0933.157.159.");
                $("#taxCode").addClass("error-input");
                $("#groupName").val('');
                $("#address").val('');
            }
        });
    } else {
        $("#groupName").val('');
        $("#address").val('');
    }
}

async function submitHandler() {
    if ($.validate.form()) {
        if ($("#groupName").val() != null && $("#groupName").val() != '') {
            if ($("#blNo").val() != currentBill) {
                let res = await getBillNoUnique();
                if (res.code == 500) {
                    $.modal.alertError(res.msg);
                    $("#blNo").addClass("error-input");
                } else {
            	    $("#blNo").removeClass("error-input");
                    edit(prefix + "/shipment/" + $('#id').val(), $('#form-edit-shipment').serialize());
                }
            } else {
            	edit(prefix + "/shipment/" + $('#id').val(), $('#form-edit-shipment').serialize());
            }
        }
    } else {
        $.modal.alertError("Không tìm ra mã số thuế!<br>Quý khách vui lòng liên hệ đến bộ phận chăm sóc khách hàng 0933.157.159.");
    }
}
function getBillNoUnique() {
    return $.ajax({
        url: prefix + "/shipment/bl-no/" + $("#blNo").val(),
        method: "GET",
    });
}
function checkBlNoUnique() {
    if ($("#blNo").val() != null && $("#blNo").val() != '' && $("#blNo").val() != currentBill) {
        //check bill unique
        $.ajax({
            url: prefix + "/shipment/bl-no/" + $("#blNo").val(),
            method: "GET",
        }).done(function (result) {
            if (result.code == 500) {
                $.modal.alertError(result.msg);
                $("#blNo").addClass("error-input");
            } else {
            	$("#blNo").removeClass("error-input");
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
        }
    })
}