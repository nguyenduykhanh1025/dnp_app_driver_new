
$(function() {
    validateRule();
    $('.imgcode').click(function() {
		var url = ctx + "captcha/captchaImage?type=" + captchaType + "&s=" + Math.random();
		$(".imgcode").attr("src", url);
	});
});

$.validator.setDefaults({
    submitHandler: function() {
    	register();
    }
});

function register() {
	$.modal.loading($("#btnSubmit").data("loading"));
	var username = $.common.trim($("input[name='username']").val());
    var password = $.common.trim($("input[name='password']").val());
    var validateCode = $("input[name='validateCode']").val();
    $.ajax({
        type: "post",
        url: ctx + "register",
        data: {
            "loginName": username,
            "password": password,
            "validateCode": validateCode
        },
        success: function(r) {
            if (r.code == 0) {
            	layer.alert("<font color='red'>Xin chúc mừng, tài khoản " + username + "  đã tạo thành công !</font>", {
        	        icon: 1,
        	        title: "Hệ Thống"
        	    },
        	    function(index) {
        	        layer.close(index);
        	        location.href = ctx + 'login';
        	    });
            } else {
            	$.modal.closeLoading();
            	$('.imgcode').click();
            	$(".code").val("");
            	$.modal.msg(r.msg);
            }
        }
    });
}

function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
    $("#registerForm").validate({
        rules: {
            username: {
                required: true,
                minlength: 2
            },
            password: {
                required: true,
                minlength: 5
            },
            confirmPassword: {
                required: true,
                equalTo: "[name='password']"
            }
        },
        messages: {
            username: {
                required: icon + "Hãy nhập tên đăng nhập",
                minlength: icon + "Tên đăng nhập tối thiểu 2 ký tự"
            },
            password: {
            	required: icon + "Hãy nhập mật khẩu",
                minlength: icon + "Mật khẩu tối thiểu 5 ký tự",
            },
            confirmPassword: {
                required: icon + "Hãy nhập xác nhận mật khẩu",
                equalTo: icon + "Xác nhận mật khẩu không trùng"
            }
        }
    })
}
