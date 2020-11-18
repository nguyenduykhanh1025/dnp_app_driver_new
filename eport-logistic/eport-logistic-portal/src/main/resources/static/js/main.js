$(".main-body").layout();

const PREFIX = ctx + 'logistic/bulletin';
var bulletinSearch = new Object();
bulletinSearch.params = new Object();
var fromDate, toDate;

$(document).ready(function() {
    $("#noticeTitle").textbox('textbox').bind('keydown', function (e) {
        // enter key
        if (e.keyCode == 13) {
            bulletinSearch.noticeTitle = $("#noticeTitle").textbox('getText').toUpperCase();
            loadBulletins();
        }
    });

    $('#fromDate').datebox({
        onSelect: function (date) {
            date.setHours(0, 0, 0);
            fromDate = date;
            if (toDate != null && date.getTime() > toDate.getTime()) {
                $.modal.alertWarning("Từ ngày không được lớn hơn đến ngày.");
            } else {
                bulletinSearch.params.fromDate = dateToString(date);
                loadBulletins();
            }
            return date;
        }
    });

    $('#toDate').datebox({
        onSelect: function (date) {
            date.setHours(23, 59, 59);
            toDate = date;
            if (fromDate != null && date.getTime() < fromDate.getTime()) {
                $.modal.alertWarning("Đến ngày không được thấp hơn từ ngày.");
            } else {
                bulletinSearch.params.toDate = dateToString(date);
                loadBulletins();
            }
            return date;
        }
    });

    loadBulletins();
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

function search() {
    bulletinSearch.noticeTitle = $("#noticeTitle").textbox('getText').toUpperCase();
    loadBulletins();
}

function clearInput() {
    $("#noticeTitle").textbox('setText', '');
    $('#fromDate').datebox('setValue', '');
    $('#toDate').datebox('setValue', '');
    bulletinSearch = new Object();
    bulletinSearch.params = new Object();
    fromDate = null;
    toDate = null;
    loadBulletins();
}

function loadBulletins() {
    $("#dg").datagrid({
        url: PREFIX + "/list",
        height: $('.main-body').height() - 40,
        method: 'post',
        pagination: true,
        pageSize: 20,
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
                    data: bulletinSearch
                }),
                success: function (res) {
                    let html = '';
                    if (res.code == 0) {
                        success(res.bulletins);
                        res.bulletins.rows.forEach(notice => {
                            html += `<div class="notice-box">
                                <div class="notice-box-title">
                                <b><span class="notice-date">` 
                                + dateToString(new Date(notice.dateStart)) +
                                `:</span></b>&nbsp;&nbsp;&nbsp;&nbsp;<span>`
                                + notice.noticeTitle +
                                `</span></div><div class="notice-box-content"><span>`
                                + notice.noticeContent +
                                `</span></div></div><hr style="margin: 0;">`;
                        });
                    } else {
                        success([]);
                    }
                    $('.datagrid-body').html(html);
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });
}

function dateToString(date) {
    return ("0" + date.getDate()).slice(-2) + "/" + ("0" + (date.getMonth() + 1)).slice(-2) + "/" + date.getFullYear();
}