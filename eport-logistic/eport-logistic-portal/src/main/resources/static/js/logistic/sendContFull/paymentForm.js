var prefix = ctx + "logistic/sendContFull";

function confirm() {
    $.ajax({
        url: prefix + "/payment",
        method: "post",
        data: {
            shipmentDetailIds: shipmentDetailIds
        },
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

$("#moveContAmount").html(0);
$("#unitCosts").html(0);
$("#moveContPrice").html(0);
$("#total").html(0);
