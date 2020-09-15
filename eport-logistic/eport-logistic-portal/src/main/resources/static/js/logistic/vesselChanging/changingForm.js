var prefix = ctx + "logistic/vessel-changing";
var podList;

$('#vesselList').change(function() {
    let podOption = '<option value="" selected>Chọn cảng dỡ</option>';
    if ("Chọn" != $('#vesselList').val()) {
        $.modal.loading("Đang xử lý ...");
        let shipmentDetail = new Object();
        for (let i = 0; i < berthplanList.length; i++) {
            if ($('#vesselList option:selected').text() == berthplanList[i].vslAndVoy) {
                shipmentDetail.vslNm = berthplanList[i].vslNm;
                shipmentDetail.voyNo = berthplanList[i].voyNo;
                shipmentDetail.year = berthplanList[i].year;
                $.modal.loading("Đang xử lý ...");
                $.ajax({
                    url: ctx + "/logistic/pods",
                    method: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(shipmentDetail),
                    success: function (data) {
                        $.modal.closeLoading();
                        if (data.code == 0) {
                            podList = data.dischargePorts;
                            $.each(podList, function(index, value) {
                                podOption += '<option value="' + value + '">' + value + '</option>';
                            });
                            $("#podList").html(podOption);
                        }
                    },
                    error: function() {
                        $.modal.closeLoading();
                        $.modal.alertError("Có lỗi xảy ra trong quá trình truy xuất dữ liệu, vui lòng liên hệ với admin.");
                    }
                });
            }
        }
    }
});

function closeForm() {
    parent.reloadShipmentDetail();
    $.modal.close();
}

function confirm() {
    if ("" == $("#vesselList").val()) {
        $("#vesselList").addClass("error-input");
        $.modal.alertError("Quý khách chựa chọn tàu/chuyến.");
    } else {
        let vslNm, voyNo, vslName, voyCarrier;
        $.each(berthplanList, function(index, value) {
            if ($('#vesselList option:selected').text() == value.vslAndVoy) {
                console.log(value);
                vslNm = value.vslNm;
                voyNo = value.voyNo;
                vslName = value.vslAndVoy.split(" - ")[1];
                voyCarrier = value.voyCarrier;
                return false;
            }
        });
        parent.otp(vslNm, voyNo, vslName, voyCarrier);
        $.modal.close();
    }
}

function removeVesselError() {
    $('#vesselList').removeClass("error-input");
}

function removePod() {
    $('#podList').removeClass("error-input");
}