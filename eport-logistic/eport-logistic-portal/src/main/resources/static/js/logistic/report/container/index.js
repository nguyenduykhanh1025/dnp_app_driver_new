const PREFIX = "/logistic/report/container";
var pickupStory = new Object();

$(document).ready(function() {
    loadTable();

    $('.from-date').datetimepicker({
        language: 'en',
        format: 'dd/mm/yyyy',
        autoclose: true,
        todayBtn: true,
        todayHighlight: true,
        pickTime: false,
        minView: 2
    });
    $('.to-date').datetimepicker({
        language: 'en',
        format: 'dd/mm/yyyy',
        autoclose: true,
        todayBtn: true,
        todayHighlight: true,
        pickTime: false,
        minView: 2
    });

    $('#searchAllInput').keyup(function(event) {
        if (event.keyCode == 13) {
            pickupStory.blNo = $('#searchAllInput').val().toUpperCase();
            pickupStory.bookingNo = $('#searchAllInput').val().toUpperCase();
            pickupStory.containerNo1 = $('#searchAllInput').val().toUpperCase();
            pickupStory.containerNo2 = $('#searchAllInput').val().toUpperCase();
            pickupStory.truckNo = $('#searchAllInput').val().toUpperCase();
            pickupStory.chassisNo = $('#searchAllInput').val().toUpperCase();
            pickupStory.yardPosition1 = $('#searchAllInput').val().toUpperCase();
            pickupStory.yardPosition2 = $('#searchAllInput').val().toUpperCase();
            pickupStory.sztp = $('#searchAllInput').val().toUpperCase();
            pickupStory.vslNm = $('#searchAllInput').val().toUpperCase();
            pickupStory.voyNo = $('#searchAllInput').val().toUpperCase();
            loadTable();
        }
    });
});

function loadTable() {
    $("#dg").datagrid({
        url: PREFIX + "/list",
        method: "POST",
        singleSelect: true,
        height: document.documentElement.clientHeight - 110,
        clientPaging: false,
        pagination: true,
        rownumbers: true,
        pageSize: 50,
        nowrap: true,
        striped: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            var opts = $(this).datagrid("options");
            if (!opts.url) return false;
            $.ajax({
                type: opts.method,
                url: opts.url,
                contentType: "application/json",
                accept: 'text/plain',
                data: JSON.stringify(pickupStory),
                dataType: 'text',
                success: function (data) {
                    success(JSON.parse(data));
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
            $("#dg").datagrid("hideColumn", "id");
        },
    });
}

function refresh() {
    $('#searchAllInput').val('');
    $('#seviceTypeSelect').val('');
    $('#fromDate').val('');
    $('#toDate').val('');
    pickupStory = new Object();
    loadTable();
}

function formatBlNo(value, row) {
    return row.shipmentDetail1.blNo;
}

function formatBookingNo(value, row) {
    return row.shipmentDetail1.bookingNo;
}

function formatSztp(value, row) {
    return row.shipmentDetail1.sztp;
}

function formatServiceType(value, row) {
    console.log(row)
    switch (row.shipment.serviceType) {
        case 1:
            return 'Bốc Hàng';
        case 2:
            return 'Hạ Rỗng';
        case 3:
            return 'Bốc Rỗng';
        case 4:
            return 'Hạ Hàng';
    }
}

function formatVslNm(value, row) {
    return row.shipmentDetail1.vslNm;
}

function formatVoyNo(value, row) {
    return row.shipmentDetail1.voyNo;
}

function formatDate(value) {
    if (value != null && value != '') {
        return value.substring(8, 10)+'/'+value.substring(5, 7)+'/'+value.substring(0, 4)+value.substring(10, 19);
    }
    return value;
}

function changeServiceType() {
    pickupStory.serviceType = $('#seviceTypeSelect').val();
    loadTable();
}

function changeFromDate() {
    pickupStory.fromDate = stringToDate($('.from-date').val()).getTime();
    loadTable();
}

function changeToDate() {
    let toDate = stringToDate($('.to-date').val());
    if ($('.from-date').val() != '' && stringToDate($('.from-date').val()).getTime() > toDate.getTime()) {
        $.modal.alertError('Quý khách không thể chọn đến ngày thấp hơn từ ngày.')
        $('.to-date').val('');
    } else {
        toDate.setHours(23, 59, 59);
        pickupStory.toDate = toDate.getTime();
        loadTable();
    }
}

function stringToDate(dateStr) {
    let dateParts = dateStr.split('/');
    return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}