var prefix = ctx + "logistic/receiveContFull";

function confirm() {
    $.ajax({
        url: prefix + "/payment",
        method: "post",
        data: {
            shipmentId: shipmentId
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

$("#moveContAmount").html(moveContAmount);
$("#unitCosts").html(unitCosts);
$("#moveContPrice").html(moveContAmount * unitCosts);
$("#total").html(moveContAmount * unitCosts);
