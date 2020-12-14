var prefix = ctx + "logistic/unloading-cargo-warehouse";

var processOrderIds = '';

$(document).ready(function () {
    if (billList == null || billList.length == 0) {
        let res = new Object();
        res.code = 500;
        res.msg = "Quý khách không có container nào cần thanh toán phí dịch chuyển.";
        parent.finishForm(res);
        $.modal.close();
    }
    $("#billDatagrid").datagrid({
        singleSelect: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            success(billList);
        },
    });

    let total = 0;
    let processOrderArray = [];
    billList.forEach(element => {
        total += element.vatAfterFee;
        if (!processOrderIds.includes()) {
            processOrderArray.push(element.processOrderId);
            processOrderIds += element.processOrderId + ',';
        }
    });
    processOrderIds.substring(0, processOrderIds.length - 1);
    $("#total").html(total.format(2, 3, ',', '.'));
});

function formatMoney(value) {
    return value.format(2, 3, ',', '.');
}

Number.prototype.format = function(n, x, s, c) {
    var re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\D' : '$') + ')',
        num = this.toFixed(Math.max(0, ~~n));

    return (c ? num.replace('.', c) : num).replace(new RegExp(re, 'g'), '$&' + (s || ','));
};

function confirm() {
    parent.napasPaymentFormForShifting();
    $.modal.close();
}

function closeForm() {
    parent.reloadShipmentDetail();
    $.modal.close();
}
