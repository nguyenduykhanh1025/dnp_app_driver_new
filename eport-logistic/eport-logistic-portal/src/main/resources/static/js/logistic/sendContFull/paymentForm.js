var prefix = ctx + "logistic/send-cont-full";

$(document).ready(function () {
    $("#billDatagrid").datagrid({
        singleSelect: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            success(processBills);
        },
    });

    let total = 0;
    processBills.forEach(element => {
        total += element.vatAfterFee;
    });
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
    parent.napasPaymentForm();
    $.ajax({
        url: prefix + "/payment/" + shipmentDetailIds,
        method: "post",
        success: function (data) {
            if (data.code != 0) {
                $.modal.msgError(data.msg);
            } else {
                parent.finishForm(data);
                $.modal.close();
            }
        },
        error: function (result) {
            $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
        }
    });
}

function closeForm() {
    $.modal.close();
}




