var prefix = ctx + "logistic/receiveContFull";
var shipmentDetailIds = "";
function confirm() {
    parent.verifyOtp(shipmentDetailIds.substring(0, shipmentDetailIds.length-1), $('#credit').prop('checked'));
    $.modal.close();
}

function closeForm() {
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
}