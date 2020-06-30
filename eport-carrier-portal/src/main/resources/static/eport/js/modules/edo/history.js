var prefix = "/edo";
$(function() {
    loadTable(containerNumber);
});

function loadTable(containerNumber) {
    $("#dg").datagrid({
        url: prefix + "/getHistory",
        method: "GET",
        singleSelect: true,
        clientPaging: true,
        pagination: true,
        pageSize: 20,
        nowrap: true,
        striped: true,
        loader: function(param, success, error) {
            var opts = $(this).datagrid("options");
            if (!opts.url) return false;
            $.ajax({
                type: opts.method,
                url: opts.url,
                data: {
                    containerNumber: containerNumber
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

function statusFormat(value) {
    if (value == "add") {
        return '<span class="badge badge-primary">Add</span>';
    }
    return '<span class="badge badge-info">Update</span>';

}

function contentFormat(value) {
    return value;
}