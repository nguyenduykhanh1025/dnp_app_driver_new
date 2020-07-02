var prefix = "/edo";
$(function() {
    loadTable(id);
});

function loadTable(id) {
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
                    id: id
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
    var actions = [];
    var formBlob = new Blob([value], { type: 'text/plain' });
    actions.push(' <a class="btn btn-warning btn-xs "  onclick="viewHistoryFileContent(\'' + value + '\')"><i class="fa fa-search"></i> Content File</a> ');
    return actions.join('');
}

function viewHistoryFileContent(formBlob) {

    // someLink.href = window.URL.createObjectURL(formBlob);
    var element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(formBlob));
    element.setAttribute('download', "filename");
  
    element.style.display = 'none';
    document.body.appendChild(element);
  
    element.click();
  
    document.body.removeChild(element);
}