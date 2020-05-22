var prefix = ctx + "logistic/receiveContFull";

function confirm() {
    parent.verifyOtp(shipmentDetailIds);
    $.modal.close();
}

function closeForm() {
    $.modal.close();
}

$("#contTable").datagrid({
    url: prefix + "/listShipmentDetailByIds",
    //height: window.innerHeight - 70,
    nowrap: false,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
        var opts = $(this).datagrid("options");
        if (!opts.url) return false;
        $.ajax({
            type: opts.method,
            url: opts.url,
            data: {
                shipmentDetailIds: shipmentDetailIds
            },
            dataType: "json",
            success: function (data) {
                success(data);
                // $("#dg").datagrid("hideColumn", "id");
            },
            error: function () {
            error.apply(this, arguments);
            },
        });
    },
});