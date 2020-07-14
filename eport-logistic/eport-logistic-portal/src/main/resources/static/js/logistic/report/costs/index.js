const PREFIX = "/logistic/report/costs";
var processBill = new Object();
//var fromDate, toDate;

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
            processBill.blNo = $('#searchAllInput').val().toUpperCase();
            processBill.bookingNo = $('#searchAllInput').val().toUpperCase();
            processBill.taxCode = $('#searchAllInput').val().toUpperCase();
            processBill.containerNo = $('#searchAllInput').val().toUpperCase();
            processBill.sztp = $('#searchAllInput').val().toUpperCase();
            processBill.referenceNo = $('#searchAllInput').val().toUpperCase();
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
                data: JSON.stringify({
                    pageNum: param.page,
                    pageSize: param.rows,
                    orderByColumn: param.sort,
                    isAsc: param.order,
                    data: processBill
                }),
                success: function (res) {
                    success(res.list);
                    $('#total').text(formatMoney(res.total)+" (VND)");
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
    processBill = new Object();
    loadTable();
}

function formatBlNo(value, row) {
    return row.processOrder.blNo;
}

function formatBookingNo(value, row) {
    return row.processOrder.bookingNo;
}

function formatTaxCode(value, row) {
    return row.processOrder.taxCode;
}

function formatPaymentStatus(value) {
    if ('Y' == value) {
        return 'Đã Thanh Toán';
    }
    return 'Chưa Thanh Toán';
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
    if (value != null && value != '') {
        return value.substring(8, 10)+'/'+value.substring(5, 7)+'/'+value.substring(0, 4)+value.substring(10, 19);
    }
    return value;
}

function formatMoney(value) {
    return value.format(2, 3, ',', '.');
}

Number.prototype.format = function(n, x, s, c) {
    var re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\D' : '$') + ')',
        num = this.toFixed(Math.max(0, ~~n));

    return (c ? num.replace('.', c) : num).replace(new RegExp(re, 'g'), '$&' + (s || ','));
};

function changeServiceType() {
    processBill.serviceType = $('#seviceTypeSelect').val();
    loadTable();
}

function changePaymentType() {
    processBill.payType = $('#paymentTypeSelect').val();
    loadTable();
}

function changePaymentStatus() {
    processBill.paymentStatus = $('#paymentStatusSelect').val();
    loadTable();
}

function changeFromDate() {
    let fromDate = stringToDate($('.from-date').val());
    if ($('.to-date').val() != '' && stringToDate($('.to-date').val()).getTime() < fromDate.getTime()) {
        $.modal.alertError('Quý khách không thể chọn từ ngày cao hơn đến ngày.')
        $('.from-date').val('');
    } else {
        processBill.fromDate = fromDate.getTime();
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
        processBill.toDate = toDate.getTime();
        loadTable();
    }
}

function stringToDate(dateStr) {
    let dateParts = dateStr.split('/');
    return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}