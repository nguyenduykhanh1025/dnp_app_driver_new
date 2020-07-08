var prefix = ctx + "logistic/receive-cont-full";

$("#form-add-shipment").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        if ($("#groupName").val() != null && $("#groupName").val() != '') {
            $.ajax({
                url: prefix + "/unique/bl-no/" + $("#blNo").val(),
                method: "GET",
            }).done(function (result) {
                if (result.code == 0) {
                    $("#blNo").removeClass("error-input");
                    $.operate.save(prefix + "/shipment", $('#form-add-shipment').serialize());
                    parent.loadTable();
                } else {
                    $.modal.msgError("Số bill đã tồn tại!");
                    $("#blNo").addClass("error-input");
                }
            });
        } else {
            $.modal.msgError("Không tìm thấy mã số thuế!");
        }
    }
}

function checkBlNoUnique() {
    if ($("#blNo").val() != null && $("#blNo").val() != '') {
        // $.ajax({
        //     url: prefix + "/unique/bl-no/" + $("#blNo").val(),
        //     method: "GET",
        // }).done(function (result) {
        //     if (result.code == 0) {
        //         $("#blNo").removeClass("error-input");
        //     } else {
        //         $.modal.msgError("Số bill đã tồn tại!");
        //         $("#blNo").addClass("error-input");
        //     }
        // });
        
        //check bill unique, opeCode,edoFlag, containerAmount trong db edo, catos
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
            	$("#blNo").removeClass("error-input");
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