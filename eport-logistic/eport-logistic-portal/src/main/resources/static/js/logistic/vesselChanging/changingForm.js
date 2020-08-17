var prefix = ctx + "logistic/vessel-changing";
var berthplanList;

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
                    berthplanList = data.berthplanList;
                    $.each(data.vesselAndVoyages, function(index, value) {
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
        let vslNm, voyNo, vslName, voyCarrier;
        $.each(berthplanList, function(index, value) {
            if ($("#vesselList").val() == value.vslAndVoy) {
                vslNm = value.vslNm;
                voyNo = value.voyNo;
                vslName = value.vslAndVoy.split(" - ")[1];
                voyCarrier = value.voyCarrier;
                return false;
            }
        });
        $.modal.close();
        parent.otp(vslNm + "," + voyNo + "," +vslName + "," + voyCarrier);
    }
}

function removeOpeCodeError() {
    $('#opeCodeList').removeClass("error-input");
}

function removeVesselError() {
    $('#vesselList').removeClass("error-input");
}