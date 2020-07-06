var prefix = "/edo"
$(function() {
    loadTable();
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