var prefix = "/edo"
$(function() {
    loadTable();
});

function closeForm()
{
   $.modal.close();
}
function loadTable() {
    $("#dg").datagrid({
        url: prefix + "/auditLog/" + edoId,
        method: "GET",
        height: $(document).height() - 70,
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

function formatField(value)
{
  return "<span class='label label-success'>"+value+"</span>";
}

function formatToYDMHMS(date) {
    let temp = date.substring(0,10);
    return temp.split("-").reverse().join("/") +  "</br>" + date.substring(10,19);
}