var prefix = ctx + "logistic/receiveContFull";
var shipmentDetailIds = "";

function confirm() {
    let isSendContEmpty = false;
    if (sendContEmpty && $('#yes').prop('checked')) {
        isSendContEmpty = true;
    }
    parent.verifyOtp(shipmentDetailIds.substring(0, shipmentDetailIds.length-1), $('#credit').prop('checked'), isSendContEmpty);
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

$('.confirm-send-empty').hide();
// if (!sendContEmpty) {
//     $('.confirm-send-empty').hide();
// }

