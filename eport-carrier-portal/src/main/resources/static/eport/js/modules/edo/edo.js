var prefix = ctx + "edo"
var bill;
var edo = new Object();
$(function() {
    $.ajax({
        type: "GET",
        url: prefix + "/carrierCode",
        success(data) {
            data.forEach(element => {
                $('#carrierCode').append(`<option value="${element}"> 
                                                  ${element} 
                                                </option>`);
            });

        }
    })
    loadTable();
    loadTableByContainer();

    $('#searchAll').keyup(function(event) {
        if (event.keyCode == 13) {
            edo.containerNumber = $('#searchAll').val().toUpperCase();
            edo.consignee = $('#searchAll').val().toUpperCase();
            edo.vessel = $('#searchAll').val().toUpperCase();
            edo.voyNo = $('#searchAll').val().toUpperCase();
            loadTableByContainer(bill);
        }
    });
});

function loadTable(containerNumber, billOfLading, fromDate, toDate) {
    $("#dg").datagrid({
        url: prefix + "/billNo",
        method: "GET",
        singleSelect: true,
        clientPaging: true,
        pagination: true,
        pageSize: 20,
        onClickRow: function () {
            getSelectedRow();
        },
        nowrap: false,
        striped: true,
        loader: function(param, success, error) {
            var opts = $(this).datagrid("options");
            if (!opts.url) return false;
            $.ajax({
                type: opts.method,
                url: opts.url,
                data: {
                    containerNumber: containerNumber,
                    billOfLading: billOfLading,
                    fromDate: fromDate,
                    toDate: toDate
                },
                dataType: "json",
                success: function(data) {
                    success(data);
                },
                error: function() {
                    error.apply(this, arguments);
                },
            });
        },
    });
}

function searchDo() {
    // setTimeout($.modal.loading("Đang xử lý"), 100);
    // $.modal.closeLoading()
    let containerNumber = $("#containerNumber").val() == null ? "" : $("#containerNumber").val();
    let billOfLading = $("#billOfLading").val() == null ? "" : $("#billOfLading").val();
    let fromDate = formatToYDM($("#fromDate").val() == null ? "" : $("#fromDate").val());
    let toDate = formatToYDM($("#toDate").val() == null ? "" : $("#toDate").val());
    loadTable(containerNumber, billOfLading, fromDate, toDate);
}

function formatToYDM(date) {
    return date.split("/").reverse().join("/");
}

function formatAction(value, row, index) {
    var actions = [];
    actions.push('<a class="btn btn-success btn-xs" onclick="viewUpdateCont(\'' + row.id + '\')"><i class="fa fa-view"></i>Cập nhật</a> ');
    actions.push('<a class="btn btn-success btn-xs" onclick="viewHistoryCont(\'' + row.id + '\')"><i class="fa fa-view"></i>Xem lịch sử</a> ');
    return actions.join('');
}

function viewHistoryCont(id) {
    $.modal.open("History Container", prefix + "/history/" + id, 800, 500);
}

function viewUpdateCont(id) {
    $.modal.openOption('Update Container', prefix + '/update/' + id, 800, 500);
}


function loadTableByContainer( billOfLading) {
    edo.billOfLading = billOfLading
    $("#dgContainer").datagrid({
        url: prefix + "/edo",
        method: "POST",
        singleSelect: true,
        clientPaging: true,
        pagination: true,
        pageSize: 20,
        nowrap: false,
        striped: true,
        loader: function(param, success, error) {
            var opts = $(this).datagrid("options");
            if(billOfLading == null)
            {
                return false;
            }
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
                    data: edo
                }),
                success: function(data) {
                    success(JSON.parse(data));
                },
                error: function() {
                    error.apply(this, arguments);
                },
            });
        },
    });
}


function getSelectedRow()
{
    var row = $('#dg').datagrid('getSelected');
    if (row){
        bill = row.billOfLading;
        loadTableByContainer(row.billOfLading);
    }
}

function stringToDate(dateStr) {
    let dateParts = dateStr.split('/');
    return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}

$.event.special.inputchange = {
    setup: function () {
        var self = this,
            val;
        $.data(
            this,
            "timer",
            window.setInterval(function () {
                val = self.value;
                if ($.data(self, "cache") != val) {
                    $.data(self, "cache", val);
                    $(self).trigger("inputchange");
                }
            }, 20)
        );
    },
    teardown: function () {
        window.clearInterval($.data(this, "timer"));
    },
    add: function () {
        $.data(this, "cache", this.value);
    },
};

$("#fromDate").on("inputchange", function () {
    edo.fromDate = stringToDate($('#fromDate').val()).getTime();
    loadTableByContainer(bill);
});

$("#toDate").on("inputchange", function () {
    let toDate = stringToDate($('#toDate').val());
    if ($('#fromDate').val() != '' && stringToDate($('#fromDate').val()).getTime() > toDate.getTime()) {
        $.modal.alertError('Quý khách không thể chọn đến ngày thấp hơn từ ngày.')
        $('#toDate').val('');
    } else {
        toDate.setHours(23, 59, 59);
        edo.toDate = toDate.getTime();
        loadTableByContainer(bill);
    }
});

