var prefix = ctx + "logistic/unloading-cargo-yard";
var shipmentDetailIds = "";

async function confirm() {
    let isSendContEmpty = false;
    if (sendContEmpty && $('#yes').prop('checked')) {
        isSendContEmpty = true;
    }
    if ($("#groupName").val() && $("#taxCode").val()) {
        if ($('#credit').prop('checked')) {
            let res = await getPaymentPermission();
            if (res.code == 0) {
                parent.verifyOtp(shipmentDetailIds.substring(0, shipmentDetailIds.length-1), $("#taxCode").val(), $('#credit').prop('checked'), isSendContEmpty);
                $.modal.close();
            } else {
                $.modal.alertWarning("Quý khách không có quyền trả sau cho mã số thuế đã chọn.");
            }
        } else {
        	$.modal.alertWarning("Chưa hỗ trợ thanh toán trả trước (cash).");
            //parent.verifyOtp(shipmentDetailIds.substring(0, shipmentDetailIds.length-1), $("#taxCode").val(), $('#credit').prop('checked'), isSendContEmpty);
            //$.modal.close();
        }
        
    }
    if ($("#taxCode").val() == '') {
        $.modal.alertWarning("Quý khách vui lòng nhập mã số thuế trước <br>khi bấm xác nhận.");
    }
}

function closeForm() {
    parent.reloadShipmentDetail();
    $.modal.close();
}

$("#contTable").datagrid({
    singleSelect: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
        var index = 1;
        shipmentDetails.forEach(function(shipmentDetail) {
            shipmentDetailIds += shipmentDetail.id + ",";
            shipmentDetail.id = index++;
        })
        success(shipmentDetails);
    },
});

if ('0' == creditFlag) {
    $('#credit').hide();
    $('#creditLabel').hide();
} else {
    $('#credit').prop('checked', true);
}

$('#taxCode').val(taxCode).prop('readonly', true);
loadGroupName();

function loadGroupName() {
    if ($("#taxCode").val() != null && $("#taxCode").val() != '') {
        $.ajax({
            url: ctx + "logistic/company/" + $("#taxCode").val(),
            method: "GET",
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

$('input:radio[name="taxCodeDefault"]').change(function() {
    if ($(this).val() == '1') {
        $('#taxCode').val(taxCode).prop('readonly', true);
        loadGroupName();
    } else {
        $('#taxCode').val('').prop('readonly', false);
        $("#taxCode").removeClass("error-input");
    }
});

$('.confirm-send-empty').hide();
// if (!sendContEmpty) {
//     $('.confirm-send-empty').hide();
// }


function getPaymentPermission() {
    return $.ajax({
        url: ctx + "logistic/taxCode/" + $("#taxCode").val() + "/delegate/payment/permission",
        method: "GET",
    });
}

