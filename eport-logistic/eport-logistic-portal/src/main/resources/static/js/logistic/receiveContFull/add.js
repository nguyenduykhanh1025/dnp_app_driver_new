var prefix = ctx + "logistic/receive-cont-full";
var shipment;

$("#form-add-shipment").validate({
    focusCleanup: true
});

// $('#taxCode').val(taxCode).prop('readonly', true);
// loadGroupName();

// $('input:radio[name="taxCodeDefault"]').change(function() {
//     if ($(this).val() == '1') {
//         $('#taxCode').val(taxCode).prop('readonly', true);
//         loadGroupName();
//     } else {
//         $('#taxCode').val('').prop('readonly', false);
//         $("#taxCode").removeClass("error-input");
//     }
// });

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
        // if ($("#groupName").val() != null && $("#groupName").val() != '') {
        //     let res = await getBillNoUnique();
        //     if (res.code == 500) {
        //         $.modal.alertError(result.msg);
        //         $("#blNoTemp").addClass("error-input");
        //         $('#opeCode').val("");
        //         $('#containerAmount').val("");
        //         $('#edoFlg').val(null).text("");
        //         $('#orderNumberDiv').hide();
        //         $('#orderNumber').val('');
        //         shipment = null;
        //     } else {
        //         $("#blNoTemp").removeClass("error-input");
        //         $('#opeCode').val(res.shipment.opeCode);
        //         shipment = res.shipment;
        //         if (res.shipment.edoFlg == "1") {
        //             $('#edoFlg').val(res.shipment.edoFlg).text("Lệnh giao hàng điện tử (eDO)");
        //             $('#edoFlgInput').val(res.shipment.edoFlg);
        //             $('#orderNumberDiv').show();
        //         } else {
        //             $('#edoFlg').val(res.shipment.edoFlg).text("Lệnh giao hàng (DO)");
        //             $('#edoFlgInput').val(res.shipment.edoFlg);
        //             $('#containerAmount').val(res.shipment.containerAmount);
        //             $('#orderNumberDiv').hide();
        //             $('#orderNumber').val('');
        //         }
        //         if (res.shipment.houseBill) {
        //             $('#houseBill').val(res.shipment.houseBill);
        //         }
        //         $('#blNo').val(res.shipment.blNo);
        //         save(prefix + "/shipment", $('#form-add-shipment').serialize());
        //     }
        // } else {
        //     $.modal.alertError("Không tìm ra mã số thuế!<br>Quý khách vui lòng liên hệ đến bộ phận chăm sóc khách hàng 0933.157.159.");
        // }
    }
}

function getBillNoUnique() {
    return $.ajax({
        url: prefix + "/shipment/bl-no/" + $("#blNoTemp").val(),
        method: "GET",
    });
}

function checkBlNoUnique() {
    if ($("#blNoTemp").val() != null && $("#blNoTemp").val() != '') {
        //check bill unique, opeCode,edoFlag, containerAmount trong db edo, catos
        $.ajax({
            url: prefix + "/shipment/bl-no/" + $("#blNoTemp").val(),
            method: "GET",
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

// function loadGroupName() {
//     if ($("#taxCode").val() != null && $("#taxCode").val() != '') {
//         $.ajax({
//             url: ctx + "logistic/company/" + $("#taxCode").val(),
//             method: "get"
//         }).done(function (result) {
//             if (result.code == 0) {
//                 $("#groupName").val(result.groupName);
//                 $("#address").val(result.address);
//                 $("#taxCode").removeClass("error-input");
//             } else {
//                 $.modal.alertError("Không tìm ra mã số thuế!<br>Quý khách vui lòng liên hệ đến bộ phận chăm sóc khách hàng 0933.157.159.");
//                 $("#taxCode").addClass("error-input");
//                 $("#groupName").val('');
//                 $("#address").val('');
//             }
//         });
//     } else {
//         $("#groupName").val('');
//         $("#address").val('');
//     }
// }

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