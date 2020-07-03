var prefix = ctx + "logistic/receive-cont-full";
var currentBill = shipment.blNo;

function loadGroupName() {
    if ($("#taxCode").val() != null && $("#taxCode").val() != '') {
        $.ajax({
            url: "/logistic/company/" + $("#taxCode").val(),
            method: "get"
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

// Map data to form
if (shipment != null) {
    $("#id").val(shipment.id);
    $("#shipmentCode").val(shipment.id);
    $("#taxCode").val(shipment.taxCode);
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
    $("#blNo").val(shipment.blNo);
    loadGroupName();
}

$("#form-edit-shipment").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        if ($("#groupName").val() != null && $("#groupName").val() != '') {
            if ($("#blNo").val() != currentBill) {
                $.ajax({
                    url: prefix + "/unique/bl-no/" + $("#blNo").val(),
                    method: "GET",
                }).done(function (result) {
                    if (result.code == 0) {
                        $("#blNo").removeClass("error-input");
                        $.operate.save(prefix + "/shipment/" + $("#id").val(), $('#form-edit-shipment').serialize());
                        parent.loadTable();
                    } else {
                        $.modal.msgError("Số bill đã tồn tại!");
                        $("#blNo").addClass("error-input");
                    }
                });
            } else {
                $.operate.save(prefix + "/shipment/" + $("#id").val(), $('#form-edit-shipment').serialize());
                parent.loadTable();
            }
        } else {
            $.modal.msgError("Không tìm thấy mã số thuế!");
        }
    }
}

function checkBlNoUnique() {
    if ($("#blNo").val() != null && $("#blNo").val() != '' && $("#blNo").val() != currentBill) {
        $.ajax({
            url: prefix + "/unique/bl-no/" + $("#blNo").val(),
            method: "GET",
        }).done(function (result) {
            if (result.code == 0) {
                $("#blNo").removeClass("error-input");
            } else {
                $.modal.msgError("Số bill đã tồn tại!");
                $("#blNo").addClass("error-input");
            }
        });
        //check opeCode,edoFlag, containerAmount trong db edo, catos
        $.ajax({
            url: prefix + "/shipment/bl-no/" + $("#blNo").val(),
            method: "GET",
        }).done(function (result) {
            if (result.code == 500) {
                $.modal.msgError(result.msg);
                $("#blNo").addClass("error-input");
                $('#opeCode').val("");
                $('#containerAmount').val("");
                $('#edoFlg').val(null).text("");
            } else {
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