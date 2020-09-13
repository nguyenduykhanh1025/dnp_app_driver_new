var prefix = ctx + "logistic/send-cont-empty";
var currentBill = shipment.blNo;
// Map data to form
if (shipment != null) {
    $("#id").val(shipment.id);
    $("#blNo").val(shipment.blNo);
    $("#shipmentCode").val(shipment.id);
    $("#containerAmount").val(shipment.containerAmount);
    $("#remark").val(shipment.remark);
    $('#opeCode').val(shipment.opeCode);
    if (shipment.sendContEmptyType == '0') {
        $('#sendContPort').prop('checked', true);
    } else {
        $('#sendContCarrier').prop('checked', true);
    }
    if (shipment.status > 1) {
        $('#opeCode').prop('disabled', true);
        $('#sendContPort').prop('disabled', true);
        $('#sendContCarrier').prop('disabled', true);
    }
}

$("#form-edit-shipment").validate({
    focusCleanup: true
});

async function submitHandler() {
    if ($.validate.form()) {
        if ($("#opeCode option:selected").text() == 'Chọn OPR') {
            $.modal.alertWarning("Quý khách chưa chọn mã OPR.");
        } else if (!$("#containerAmount").val() || $("#containerAmount").val() < shipment.containerAmount) {
            $.modal.alertWarning("Số lượng container quý khách muốn cập nhật không hợp lệ.");
        } else if ($("#blNo").val() != currentBill) {
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
}
function getBillNoUnique() {
    return $.ajax({
        url: prefix + "/blNo/bl-no",
        method: "post",
        contentType: "application/json",
        data: JSON.stringify({"blNo": $("#blNo").val()}),
    });
}
function checkBlNoUnique() {
    if ($("#blNo").val() != null && $("#blNo").val() != '' && $("#blNo").val() != currentBill) {
        //check bill unique
        $.ajax({
            url: prefix + "/blNo/bl-no",
            method: "post",
            contentType: "application/json",
            data: JSON.stringify({"blNo": $("#blNo").val()}),
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