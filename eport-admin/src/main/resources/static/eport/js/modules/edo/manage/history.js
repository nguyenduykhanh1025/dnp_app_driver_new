var PREFIX = "/edo/manage"
$(function() {
    loadTable();
});


function loadTable() {
    $("#dg").datagrid({
        url: PREFIX + "/auditLog/" + edoId,
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


function formatToYDMHMS(date) {
    let temp = date.substring(0,10);
    return temp.split("-").reverse().join("/") + date.substring(10,19);
  }