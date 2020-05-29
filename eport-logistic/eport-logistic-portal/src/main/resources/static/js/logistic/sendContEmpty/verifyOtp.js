var prefix = ctx + "logistic/sendContEmpty";
var interval;

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

function getOtp() {

    $.modal.msgSuccess("Tạo OTP thành công.");

    if (interval != null) {
        clearInterval(interval);
        
    } else {
        $("#indicateTime").show();
        $("#indicateAction").hide();
    }
    // Get today's date and time
    var now = new Date();

    // Set the date we're counting down to
    var countDownDate = new Date(now.getTime() + (5*60000));

    // Update the count down every 1 second
    interval = setInterval(function() {
        
        now = new Date();

        // Find the distance between now and the count down date
        var distance = countDownDate.getTime() - now.getTime();

        var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((distance % (1000 * 60)) / 1000);

        // Display the result in the element with id="demo"
        $("#timer").html(minutes + " phút " + seconds + " giây")

        // If the count down is finished, write some text
        if (distance < 0) {
            clearInterval(x);
            $("#timer").html("0 phút 0 giây"); 
        }
    }, 1000);
}

$("#p1").html("Mã OTP sẽ được gửi đến số điện thoại " + numberPhone + ".");