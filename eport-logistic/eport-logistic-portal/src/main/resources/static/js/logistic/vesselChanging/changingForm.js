var prefix = ctx + "logistic/vessel-changing";

$('#opeCodeList').change(function() {
    let vesselOption = '<option value="" selected>Chọn tàu/chuyến</option>';
    if ("Chọn" != $('#opeCodeList').val()) {
        $.modal.loading("Đang xử lý ...");
        $.ajax({
            url: prefix + "/berthplan/ope-code/"+ $("#opeCodeList").val().split(":")[0] +"/vessel-voyage/list",
            method: "GET",
            success: function (data) {
                $.modal.closeLoading();
                if (data.code == 0) {
                    $.each(data.vesselAndVoyages , function(index, value) {
                        vesselOption += '<option value="' + value + '" selected>' + value + '</option>';
                    });
                    $("#vesselList").html(vesselOption);
                } else if (data.code == 301) {
                    $.modal.alertWarning(data.msg);
                }
            },
            error: function() {
                $.modal.closeLoading();
                $.modal.alertError("Có lỗi xảy ra trong quá trình truy xuất dữ liệu, vui lòng liên hệ với admin.");
            }
        });
    }
});

function closeForm() {
    parent.reloadShipmentDetail();
    $.modal.close();
}

function confirm() {
    if ("Chọn" == $('#opeCodeList').val()) {
        $('#opeCodeList').addClass("error-input");
        $.modal.alertError("Quý khách chưa chọn hãng tàu.");
    } else if ("" == $("#vesselList").val()) {
        $("#vesselList").addClass("error-input");
        $.modal.alertError("Quý khách chựa chọn tàu/chuyến.");
    } else {
        $.modal.close();
        parent.otp($("#vesselList").val());
    }
}

function removeOpeCodeError() {
    $('#opeCodeList').removeClass("error-input");
}

function removeVesselError() {
    $('#vesselList').removeClass("error-input");
}