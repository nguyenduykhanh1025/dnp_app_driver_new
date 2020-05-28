var prefix = ctx + "logistic/receiveContFull";

function confirm() {
    parent.verifyOtp(shipmentDetailIds);
    $.modal.close();
}

function closeForm() {
    $.modal.close();
}

$("#contTable").datagrid({
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
        var index = 1;
        shipmentDetails.forEach(function(shipmentDetail) {
            shipmentDetail.id = index++;
        })
        success(shipmentDetails);
    },
});