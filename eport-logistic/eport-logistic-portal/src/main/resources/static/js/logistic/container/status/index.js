const PREFIX = ctx + "logistic/container/status";
var shipmentDetail = new Object();

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
            shipmentDetail.blNo = $('#searchAllInput').val().toUpperCase();
            shipmentDetail.bookingNo = $('#searchAllInput').val().toUpperCase();
            shipmentDetail.containerNo = $('#searchAllInput').val().toUpperCase();
            shipmentDetail.sztp = $('#searchAllInput').val().toUpperCase();
            shipmentDetail.vslNm = $('#searchAllInput').val().toUpperCase();
            shipmentDetail.voyNo = $('#searchAllInput').val().toUpperCase();
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
                dataType: 'text',
                data: JSON.stringify({
                    pageNum: param.page,
                    pageSize: param.rows,
                    orderByColumn: param.sort,
                    isAsc: param.order,
                    data: shipmentDetail
                }),
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
    $('#paymentTypeSelect').val('');
    $('#paymentStatusSelect').val('');
    $('#fromDate').val('');
    $('#toDate').val('');
    shipmentDetail = new Object();
    loadTable();
}

function formatServiceType(value) {
    switch (value) {
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

function formatDate(value) {
    return value.substring(8, 10)+'/'+value.substring(5, 7)+'/'+value.substring(0, 4)+value.substring(10, 19);
}
function changeServiceType() {
    shipmentDetail.serviceType = $('#seviceTypeSelect').val();
    loadTable();
}

function changePaymentType() {
    shipmentDetail.payType = $('#paymentTypeSelect').val();
    loadTable();
}

function changePaymentStatus() {
    shipmentDetail.paymentStatus = $('#paymentStatusSelect').val();
    loadTable();
}

function changeFromDate() {
    let fromDate = stringToDate($('.from-date').val());
    if ($('.to-date').val() != '' && stringToDate($('.to-date').val()).getTime() < fromDate.getTime()) {
        $.modal.alertError('Quý khách không thể chọn từ ngày cao hơn đến ngày.')
        $('.from-date').val('');
    } else {
        shipmentDetail.fromDate = fromDate.getTime();
        loadTable();
    }
}

function changeToDate() {
    let toDate = stringToDate($('.to-date').val());
    if ($('.from-date').val() != '' && stringToDate($('.from-date').val()).getTime() > toDate.getTime()) {
        $.modal.alertError('Quý khách không thể chọn đến ngày thấp hơn từ ngày.')
        $('.to-date').val('');
    } else {
        toDate.setHours(23, 59, 59);
        shipmentDetail.toDate = toDate.getTime();
        loadTable();
    }
}

function stringToDate(dateStr) {
    let dateParts = dateStr.split('/');
    return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}
