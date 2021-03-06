var prefix = ctx + "logistic/send-cont-empty";
var interval;
var minutes = 0, seconds = 0;

function confirm() {
    if ($("#otpInput").val() !=null && $("#otpInput").val() != "") {
        $.ajax({
            url: prefix + "/otp/" + $("#otpInput").val() + "/verification",
            method: "post",
            data: {
                shipmentDetailIds: shipmentDetailIds,
                creditFlag: creditFlag,
                taxCode: taxCode
            },
            success: function (data) {
                if (data.code != 0 && data.code != 301) {
                    $.modal.alertError(data.msg);
                } else {
                    parent.finishVerifyForm(data);
                    $.modal.close();
                }
            },
            error: function (result) {
                $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau.");
            }
        });
    } else {
        $.modal.alertError("Quý khách chưa nhập mã OTP!");
    }
    
}

function closeForm() {
    parent.reloadShipmentDetail();
    $.modal.close();
}

function getOtp() {
    if (minutes < 4 || (minutes == 4 && seconds < 30)) {
        $.ajax({
            url: ctx + "logistic/otp/" + shipmentDetailIds,
            method: "GET",
            success: function (data) {
                if (data.code != 0) {
                    $.modal.alertSuccess("Đã gửi OTP.");
                } 
            },
            error: function (result) {
                $.modal.alertError("Có lỗi trong quá trình xử lý dữ liệu, vui lòng thử lại sau.");
            }
        });
        
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
    
            minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            seconds = Math.floor((distance % (1000 * 60)) / 1000);
    
            // Display the result in the element with id="demo"
            $("#timer").html(minutes + " phút " + seconds + " giây")
    
            // If the count down is finished, write some text
            if (distance < 0) {
                clearInterval(interval);
                $("#timer").html("0 phút 0 giây"); 
            }
        }, 1000);
    } else {
        $.modal.msgError("Quý khách vui lòng đợi " + (seconds - 30) + " giây trước khi yêu cầu gửi lại OTP");
    }
}

$(function() {
	$("#p1").html("Mã OTP sẽ được gửi đến số điện thoại " + numberPhone + ".");
	$("#sid").html("<b>" + sid + "</b>");
	getOtp();
});