const PREFIX = ctx + "logistic/container/status";
var shipmentDetail = new Object();
shipmentDetail.params = new Object();

$(document).ready(function() {

    let date = new Date();
    let firstDay = new Date(date.getFullYear(), date.getMonth(), 1); 
    let lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
    lastDay.setHours(23, 59, 59);

    shipmentDetail.params.fromDate = ("0" + firstDay.getDate()).slice(-2) + "/" + ("0"+(firstDay.getMonth()+1)).slice(-2) + "/" + firstDay.getFullYear()
    + " " + ("0" + firstDay.getHours()).slice(-2) + ":" + ("0" + firstDay.getMinutes()).slice(-2) + ":" + ("0" + firstDay.getSeconds()).slice(-2);

    shipmentDetail.params.toDate = ("0" + lastDay.getDate()).slice(-2) + "/" + ("0"+(lastDay.getMonth()+1)).slice(-2) + "/" + lastDay.getFullYear()
    + " " + ("0" + lastDay.getHours()).slice(-2) + ":" + ("0" + lastDay.getMinutes()).slice(-2) + ":" + ("0" + lastDay.getSeconds()).slice(-2);

    $('.from-date').val(("0" + firstDay.getDate()).slice(-2) + "/" + ("0"+(firstDay.getMonth()+1)).slice(-2) + "/" + firstDay.getFullYear());

    $('.to-date').val(("0" + lastDay.getDate()).slice(-2) + "/" + ("0"+(lastDay.getMonth()+1)).slice(-2) + "/" + lastDay.getFullYear());

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
        height: currentHeight,
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
    if (value) {
        return value.substring(8, 10)+'/'+value.substring(5, 7)+'/'+value.substring(0, 4)+value.substring(10, 19);
    }
    return value;
}

function formatPayment(value) {
    if (value) {
        return 'Y'==value?'Đã thanh toán':'Chưa thanh toán';
    }
    return value;
}

function formatDo(value) {
    if (value) {
        return 'Y'==value?'Đã nhận':'Chưa nhận';
    }
    return "";
}

function formatBlBooking(value, row) {
    if (row.blNo) {
        return row.blNo;
    }
    if (row.bookingNo) {
        return row.bookingNo;
    }
    return '';
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
        shipmentDetail.params.fromDate = ("0" + fromDate.getDate()).slice(-2) + "/" + ("0"+(fromDate.getMonth()+1)).slice(-2) + "/" + fromDate.getFullYear()
        + " " + ("0" + fromDate.getHours()).slice(-2) + ":" + ("0" + fromDate.getMinutes()).slice(-2) + ":" + ("0" + fromDate.getSeconds()).slice(-2);
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
        shipmentDetail.params.toDate = ("0" + toDate.getDate()).slice(-2) + "/" + ("0"+(toDate.getMonth()+1)).slice(-2) + "/" + toDate.getFullYear()
        + " " + ("0" + toDate.getHours()).slice(-2) + ":" + ("0" + toDate.getMinutes()).slice(-2) + ":" + ("0" + toDate.getSeconds()).slice(-2);
        loadTable();
    }
}

function stringToDate(dateStr) {
    let dateParts = dateStr.split('/');
    return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}
