const PREFIX = ctx + "logistic/eir-gate";
var eirGate = new Object();
eirGate.params = new Object();
var fromDate, toDate;
var ready = false;

$(document).ready(function () {

    let date = new Date();
    fromDate = new Date(date.getFullYear(), date.getMonth(), 1);
    fromDate.setHours(0, 0, 0);
    toDate = new Date(date.getFullYear(), date.getMonth() + 1, 0);
    toDate.setHours(23, 59, 59);
    eirGate.params.variableStart = dateToString(fromDate);
    eirGate.params.variableEnd = dateToString(toDate);

    $('#fromDate').datebox({
        onSelect: function (date) {
            date.setHours(0, 0, 0);
            let fortyDays = 40 * 24 *60 * 60 * 1000;
            if (toDate != null && date.getTime() > toDate.getTime()) {
                $.modal.alertWarning("Từ ngày không được lớn hơn đến ngày.");
            } else if (Math.abs(toDate.getTime() - date.getTime()) > fortyDays) {
                $.modal.alertWarning("Không thể lọc báo cáo trong khoảng lớn hơn 40 ngày.");
            } else {
                fromDate = date;
                eirGate.params.variableStart = dateToString(date);
                if (ready) {
                    loadTable();
                }
            }
        }
    });


    $('#toDate').datebox({
        onSelect: function (date) {
            date.setHours(23, 59, 59);
            let fortyDays = 40 * 24 *60 * 60 * 1000;
            if (fromDate != null && date.getTime() < fromDate.getTime()) {
                $.modal.alertWarning("Đến ngày không được thấp hơn từ ngày.");
            } else if (Math.abs(date.getTime() - fromDate.getTime()) > fortyDays) {
                $.modal.alertWarning("Không thể lọc báo cáo trong khoảng lớn hơn 40 ngày.");
            } else {
                toDate = date;
                eirGate.params.variableEnd = dateToString(date);
                if (ready) {
                    loadTable();
                }
            }
        }
    });

    let currentYear = new Date().getFullYear();
    $("#callYear").combobox({
        valueField: 'yearValue',
        textField: 'yearKey',
        data: [
            {
                "yearValue": currentYear,
                "yearKey": currentYear,
                "selected": true
            },
            {
                "yearValue": currentYear - 1,
                "yearKey": currentYear - 1
            }],
        onSelect: function (callYear) {
            eirGate.params.variableYear = callYear.yearValue;
            if (ready) {
                loadTable();
            }
        }
    });

    $('#fromDate').datebox('setValue', dateToStringDate(fromDate));
    $('#toDate').datebox('setValue', dateToStringDate(toDate));

    $("#containerNo").textbox('textbox').bind('keydown', function (e) {
        // enter key
        if (e.keyCode == 13) {
            eirGate.containerNo = $("#containerNo").textbox('getText').toUpperCase();
            loadTable();
        }
    });

    loadTable();
    ready = true;
});

function dateformatter(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    return (d < 10 ? ('0' + d) : d) + '/' + (m < 10 ? ('0' + m) : m) + '/' + y;
}
function dateparser(s) {
    var ss = (s.split('/'));
    var d = parseInt(ss[0], 10);
    var m = parseInt(ss[1], 10);
    var y = parseInt(ss[2], 10);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
        return new Date(y, m - 1, d);
    }
}

function dateToString(date) {
    return ("0" + date.getDate()).slice(-2) + "/" + ("0" + (date.getMonth() + 1)).slice(-2) + "/" + date.getFullYear()
        + " " + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2) + ":" + ("0" + date.getSeconds()).slice(-2);
}

function dateToStringDate(date) {
    return ("0" + date.getDate()).slice(-2) + "/" + ("0" + (date.getMonth() + 1)).slice(-2) + "/" + date.getFullYear();
}

function loadTable() {
    $("#dg").datagrid({
        url: PREFIX + "/report",
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
                data: JSON.stringify({
                    pageNum: param.page,
                    pageSize: param.rows,
                    orderByColumn: param.sort,
                    isAsc: param.order,
                    data: eirGate
                }),
                success: function (data) {
                    success(data);
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });
}

function refresh() {
    $('#containerNo').textbox('setText', '');
    eirGate = new Object();
    eirGate.params = new Object();
    let date = new Date();
    fromDate = new Date(date.getFullYear(), date.getMonth(), 1);
    fromDate.setHours(0, 0, 0);
    toDate = new Date(date.getFullYear(), date.getMonth() + 1, 0);
    toDate.setHours(23, 59, 59);
    eirGate.params.variableStart = dateToString(fromDate);
    eirGate.params.variableEnd = dateToString(toDate);
    $('#fromDate').datebox('setValue', dateToStringDate(fromDate));
    $('#toDate').datebox('setValue', dateToStringDate(toDate));
    loadTable();
}

function formatDate(value) {
    if (value != null && value != '') {
        return value.substring(8, 10) + '/' + value.substring(5, 7) + '/' + value.substring(0, 4) + value.substring(10, 19).replace('T', ' ');
    }
    return value;
}

function exportExcel() {
    $.modal.loading("Đang xử lý, vui lòng chờ...");
    $.ajax({
        type: "POST",
        url: PREFIX + "/export",
        contentType: "application/json",
        data: JSON.stringify(eirGate),
        success: function (result) {
            if (result.code == web_status.SUCCESS) {
                window.location.href = ctx + "common/download?fileName=" + encodeURI(result.msg) + "&delete=" + true;
            } else if (result.code == web_status.WARNING) {
                $.modal.alertWarning(result.msg);
            } else {
                $.modal.alertError(result.msg);
            }
            $.modal.closeLoading();
        },
    });
}