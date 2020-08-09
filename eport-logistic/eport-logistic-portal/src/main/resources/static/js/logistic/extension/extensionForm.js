var prefix = ctx + "logistic/vessel-changing";


$('#expiredDem').datetimepicker({
    language: 'en',
    format: 'dd/mm/yyyy',
    autoclose: true,
    todayBtn: true,
    todayHighlight: true,
    pickTime: false,
    minView: 2
});


$('#expiredDem').change(function() {
    let date = new Date();
    let expiredDem = stringToDate($('#expiredDem').val());
    date.setHours(0, 0, 0, 0);
    if (date.getTime() > expiredDem.getTime()) {
        $.modal.alertError("Hạn lệnh không được trong quá khứ.");
        $('#expiredDem').addClass("error-input");
        $('#expiredDem').val('')
    }
});

function closeForm() {
    parent.reloadShipmentDetail();
    $.modal.close();
}

function confirm() {
    if ("" == $('#expiredDem').val()) {
        $('#expiredDem').addClass("error-input");
        $.modal.alertError("Quý khách chưa nhập hạn lệnh mới.");
    } else {
        $.modal.close();
        parent.otp(stringToDate($("#expiredDem").val()).getTime());
    }
}

function removeExpiredDemError() {
    $('#expiredDem').removeClass("error-input");
}

function stringToDate(dateStr) {
    let dateParts = dateStr.split('/');
    return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}