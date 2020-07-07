var prefix = "/edo"
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
    
    $("#dgContainer").datagrid({
        url: prefix + "/edo",
        method: "GET",
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
                data: {
                    billOfLading: billOfLading
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


function getSelectedRow()
{
    var row = $('#dg').datagrid('getSelected');
    if (row){
        console.log(row.billOfLading);
        loadTableByContainer(row.billOfLading);
    }
}

