var PREFIX = ctx + "accountant/support";

$(document).ready(function () {
    $("#billDatagrid").datagrid({
        singleSelect: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            console.log("processBills", processBills)
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

Number.prototype.format = function (n, x, s, c) {
    var re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\D' : '$') + ')',
        num = this.toFixed(Math.max(0, ~~n));

    return (c ? num.replace('.', c) : num).replace(new RegExp(re, 'g'), '$&' + (s || ','));
};

function confirm() {
    parent.napasPaymentForm();
    $.modal.close();
}

function closeForm() {
    $.modal.close();
}

function openPaymentSupport() {
      layer.confirm("Xác nhận bill này đã thanh toán.", {
        icon: 3,
        title: "Xác Nhận",
        btn: ['Đồng Ý', 'Hủy Bỏ']
      }, function () {
        $.modal.loading("Đang xử lý...");
        $.ajax({
          url: PREFIX + "/payment",
          method: "GET",
          data: {
            processOrderId: processOrderId
          }
        }).done(function (res) {
            parent.getSelectedRow();
          $.modal.closeLoading();
          $.modal.msgSuccess(res.msg);
        });
      }, function () {
        // DO NOTHING
      });
    }