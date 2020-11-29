var prefix = ctx + "logistic/unloading-cargo";
var shipment;

$("#form-add-shipment").validate({
    focusCleanup: true
});
async function submitHandler() {
    if ($.validate.form()) {
        let res = await getBillNoUnique();
        if (res.code == 500) {
            $.modal.alertError(result.msg);
            $("#blNoTemp").addClass("error-input");
            $('#opeCode').val("");
            $('#containerAmount').val("");
            $('#edoFlg').val(null).text("");
            $('#orderNumberDiv').hide();
            $('#orderNumber').val('');
            shipment = null;
        } else {
            $("#blNoTemp").removeClass("error-input");
            $('#opeCode').val(res.shipment.opeCode);
            shipment = res.shipment;
            if (res.shipment.edoFlg == "1") {
                $('#edoFlg').val(res.shipment.edoFlg).text("Lệnh giao hàng điện tử (eDO)");
                $('#edoFlgInput').val(res.shipment.edoFlg);
                $('#orderNumberDiv').show();
            } else {
                $('#edoFlg').val(res.shipment.edoFlg).text("Lệnh giao hàng (DO)");
                $('#edoFlgInput').val(res.shipment.edoFlg);
                $('#containerAmount').val(res.shipment.containerAmount);
                $('#orderNumberDiv').hide();
                $('#orderNumber').val('');
            }
            if (res.shipment.houseBill) {
                $('#houseBill').val(res.shipment.houseBill);
            }
            $('#blNo').val(res.shipment.blNo);
            save(prefix + "/shipment", $('#form-add-shipment').serialize());
        }
    }
}

function getBillNoUnique() {
    return $.ajax({
        url: prefix + "/shipment/bl-no",
        method: "post",
        contentType: "application/json",
        data: JSON.stringify({"blNo": $("#blNoTemp").val()}),
    });
}

function checkBlNoUnique() {
    if ($("#blNoTemp").val() != null && $("#blNoTemp").val() != '') {
        //check bill unique, opeCode,edoFlag, containerAmount trong db edo, catos
        $.ajax({
            url: prefix + "/shipment/bl-no",
            method: "post",
            contentType: "application/json",
            data: JSON.stringify({"blNo": $("#blNoTemp").val()}),
        }).done(function (result) {
            if (result.code == 500) {
                $.modal.alertError(result.msg);
                $("#blNoTemp").addClass("error-input");
                $('#opeCode').val("");
                $('#containerAmount').val("");
                $('#edoFlg').val(null).text("");
                $('#orderNumberDiv').hide();
                $('#orderNumber').val('');
                shipment = null;
            } else {
            	$("#blNoTemp").removeClass("error-input");
                $('#opeCode').val(result.shipment.opeCode);
                shipment = result.shipment;
                if (result.shipment.edoFlg == "1") {
                    $('#edoFlg').val(result.shipment.edoFlg).text("Lệnh giao hàng điện tử (eDO)");
                    $('#edoFlgInput').val(result.shipment.edoFlg);
                    $('#orderNumberDiv').show();
                } else {
                    $('#edoFlg').val(result.shipment.edoFlg).text("Lệnh giao hàng (DO)");
                    $('#edoFlgInput').val(result.shipment.edoFlg);
                    $('#containerAmount').val(result.shipment.containerAmount);
                    $('#orderNumberDiv').hide();
                    $('#orderNumber').val('');
                }
            }
        });
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
                $.modal.alertError(result.msg);
            }
        }
    });
}

function checkOrderNumber() {
    if ($('#orderNumber').val()) {
        shipment.orderNumber = $('#orderNumber').val();
        $.modal.loading("Đang xử lý...");
        $.ajax({
            url: prefix + "/orderNumber/check",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify(shipment),
            success: function(result) {
                $.modal.closeLoading();
                if (result.code == 0) {
                    $('#containerAmount').val(result.containerAmount);
                } else {
                    $('#containerAmount').val('');
                    $.modal.alertError(result.msg);
                }
            },
            error: function(err) {
                $.modal.alertError("Có lỗi xảy ra.");
            }
        });
    }
}