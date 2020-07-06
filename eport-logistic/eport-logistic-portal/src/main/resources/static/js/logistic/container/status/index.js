const PREFIX = "/logistic/container/status";

$(function () {
    loadTable();
});

function loadTable() {
    $("#dg").datagrid({
        url: PREFIX + "/list",
        method: "GET",
        singleSelect: true,
        height: document.documentElement.clientHeight - 70,
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
                data: {},
                dataType: "json",
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


function formatStatus(value)
{
    console.log(value);
    switch(value)
    {
        case 1:
            return "<span class='label label-success'>Trạng thái 1</span>";
            break;
        case 2:
            return "<span class='label label-success'>Trạng thái 2</span>";
            break;
        case 3:
            return "<span class='label label-success'>Trạng thái 2</span>";
            break;
        case 4:
            return "<span class='label label-success'>Trạng thái 2</span>";
            break;
        default:
            return "<span class='label label-warning'>Không xác định</span>";

    }
}