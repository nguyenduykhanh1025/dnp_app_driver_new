var prefix = ctx + "logistic/unloading-cargo-warehouse";
var currentBill;
if (shipment.houseBill) {
    currentBill = shipment.houseBill;
} else {
    currentBill = shipment.blNo;
}

// Map data to form
if (shipment != null) {
    $("#id").val(shipment.id);
    $("#shipmentCode").val(shipment.id);
    $("#containerAmount").val(shipment.containerAmount);
    $("#remark").val(shipment.remark);
    if (shipment.edoFlg == "0") {
        $('#edoFlg').val("0").text("Lệnh giao hàng (DO)");
        $('#edoFlgInput').val(shipment.edoFlg);
    } else {
        $('#edoFlg').val("1").text("Lệnh giao hàng điện tử (eDO)");
        $('#edoFlgInput').val(shipment.edoFlg);
    }
    $('#opeCode').val(shipment.opeCode);
    if (shipment.houseBill) {
        $("#blNoTemp").val(shipment.houseBill);
        $("#orderNumber").val(shipment.orderNumber);
        $("#blNoTemp").prop('disabled', true);
        $('#orderNumberDiv').show();
    } else {
        $("#blNoTemp").val(shipment.blNo);
    }

    if (shipment.status > 1) {
        $("#blNoTemp").prop('disabled', true);
    }
}

$("#form-edit-shipment").validate({
    focusCleanup: true
});

async function submitHandler() {
    if ($.validate.form()) {
        if ($("#blNoTemp").val() != currentBill) {
            let res = await getBillNoUnique();
            if (res.code == 500) {
                $.modal.alertError(result.msg);
                $("#blNoTemp").addClass("error-input");
                $('#opeCode').val("");
                $('#containerAmount').val("");
                $('#edoFlg').val(null).text("");
            } else {
                $("#blNoTemp").removeClass("error-input");
                $('#opeCode').val(res.shipment.opeCode);
                $('#containerAmount').val(res.shipment.containerAmount);
                if (res.shipment.edoFlg == "1") {
                    $('#edoFlg').val(res.shipment.edoFlg).text("Lệnh giao hàng điện tử (eDO)");
                    $('#edoFlgInput').val(res.shipment.edoFlg);
                } else {
                    $('#edoFlg').val(res.shipment.edoFlg).text("Lệnh giao hàng (DO)");
                    $('#edoFlgInput').val(res.shipment.edoFlg);
                    $('#blNo').val($('#blNoTemp').val());
                }
                edit(prefix + "/shipment/" + $("#id").val(), $('#form-edit-shipment').serialize())
            }
        } else {
            edit(prefix + "/shipment/" + $("#id").val(), $('#form-edit-shipment').serialize());
        }
    }
}

function getBillNoUnique() {
    return $.ajax({
        url: prefix + "/shipment/bl-no",
        method: "post",
        contentType: "application/json",
        data: JSON.stringify({ "blNo": $("#blNoTemp").val() }),
    });
}

function checkBlNoUnique() {
    if ($("#blNoTemp").val() != null && $("#blNoTemp").val() != '' && $("#blNoTemp").val() != currentBill) {
        //check bill unique, opeCode,edoFlag, containerAmount trong db edo, catos
        $.ajax({
            url: prefix + "/shipment/bl-no",
            method: "post",
            contentType: "application/json",
            data: JSON.stringify({ "blNo": $("#blNoTemp").val() }),
        }).done(function (result) {
            if (result.code == 500) {
                $.modal.alertError(result.msg);
                $("#blNoTemp").addClass("error-input");
                $('#opeCode').val("");
                $('#containerAmount').val("");
                $('#edoFlg').val(null).text("");
            } else {
                $("#blNoTemp").removeClass("error-input");
                $('#opeCode').val(result.shipment.opeCode);
                $('#containerAmount').val(result.shipment.containerAmount);
                if (result.shipment.edoFlg == "1") {
                    $('#edoFlg').val(result.shipment.edoFlg).text("Lệnh giao hàng điện tử (eDO)");
                    $('#edoFlgInput').val(result.shipment.edoFlg);
                } else {
                    $('#edoFlg').val(result.shipment.edoFlg).text("Lệnh giao hàng (DO)");
                    $('#edoFlgInput').val(result.shipment.edoFlg);
                }
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
        success: function (result) {
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
