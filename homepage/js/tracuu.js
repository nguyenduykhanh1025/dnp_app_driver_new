const PREFIX_URL = 'https://app.danangport.com/api';
$(document).ready(function (){
    $('input[name=billNo]').on('keypress',function(e) {
        if(e.which == 13) {
            searchEdo();
        }
    });

    $('input[name=containerNo]').on('keypress',function(e) {
        if(e.which == 13) {
            searchEdo();
        }
    });

    loadTable(null);
});

function searchEdo() {
    let blNo = $('input[name=billNo]').val().trim().toUpperCase();
    let containerNo = $('input[name=containerNo]').val().trim().toUpperCase();
    if (!blNo && !containerNo) {
        alert("Vui lòng nhập số Bill hoặc số Container để tìm kiếm.");
    } else {
        loadTable({ billOfLading: blNo, containerNumber: containerNo });
    }
}

function loadTable(requestData) {
    if (requestData == null) {
        $("#dg").datagrid({
            height: $(document).height() - 220,
            singleSelect: true,
            collapsible: true,
            clientPaging: false,
            pagination: true,
            rownumbers: true,
            pageSize: 50,
            nowrap: true,
            striped: true
        });
    } else {
        $("#dg").datagrid({
            height: $(document).height() - 220,
            url: PREFIX_URL + '/edo/lookup',
            method: 'POST',
            singleSelect: true,
            collapsible: true,
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
                    method: opts.method,
                    url: opts.url,
                    contentType: "application/json",
                    data: JSON.stringify({
                        pageNum: param.page,
                        pageSize: param.rows,
                        orderByColumn: param.sort,
                        isAsc: param.order,
                        data: requestData
                    }),
                    success: function (res) {
                        if (res.code == 0) {
                            success(res.edos);
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
}

// formatter
// format Date => fomat dd/MM/yyyy HH:mm
function formatDateTime(value, row) {
    if (value) {
        let date = new Date(value);
        let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        let month = date.getMonth() + 1;
        let monthText = month < 10 ? "0" + month : month;
        let hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
        let minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
        return day + "/" + monthText + "/" + date.getFullYear() + " " + hour + ":" + minute;
    }
    return '';
}

// format Date => format dd/MM/yyyy
function formatDate(value, row) {
    if (value) {
        let date = new Date(value);
        let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        let month = date.getMonth() + 1;
        let monthText = month < 10 ? "0" + month : month;
        return day + "/" + monthText + "/" + date.getFullYear();
    }
    return '';
}

function formatStatus(value, row) {
    return value;
}

