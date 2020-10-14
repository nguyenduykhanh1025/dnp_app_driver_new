const PREFIX = ctx + "history/container";

$(document).ready(function () {
    let req = {
        callSeq: callSeq,
        vslCd: vslCd,
        cntrNo: cntrNo
    }
    loadTable(req);
});

function loadTable(req) {
    $("#dg").datagrid({
        url: PREFIX + '/catos',
        method: 'POST',
        singleSelect: true,
        collapsible: true,
        clientPaging: false,
        pagination: false,
        rownumbers: true,
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
                data: JSON.stringify(req),
                success: function (res) {
                    if (res.code == 0) {
                        success(res.containerHistories);
                    } else {
                        success([]);
                    }
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });
}

function formatVesselInfo(value, row) {
    if (row) {
        return row.vslCd + " / " + row.callYear + " / " + row.callSeq;
    }
    return '';
}

function formatDate(value) {
    let date = new Date(value);
    let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    let month = date.getMonth() + 1;
    let monthText = month < 10 ? "0" + month : month;
    let hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
    let minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
    let second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
    return day + "/" + monthText + "/" + date.getFullYear() + " " + hour + ":" + minute + ":" + second;
}