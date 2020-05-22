var prefix = ctx + "logistic/receiveContFull";

function confirm() {
    if ($("#otpInput").val() !=null && $("#otpInput").val() != "") {
        $.ajax({
            url: prefix + "/verifyOtp",
            method: "post",
            data: {
                otp: $("#otpInput").val(),
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
    } else {
        $.modal.alertError("Quý khách chưa nhập mã OTP!");
    }
    
}

function closeForm() {
    $.modal.close();
}

$("#p1").html("Mã OTP sẽ được gửi đến số điện thoại " + numberPhone + ".");