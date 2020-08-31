const PREFIX = ctx + "edo/manage";
$(function() {
    loadTable();
});

function closeForm()
{
   $.modal.close();
}

function loadTable() {
    $("#dg").datagrid({
        url: PREFIX + "/auditLog/" + edoId,
        method: "GET",
        singleSelect: true,
        clientPaging: true,
        height: $(document).height() - 65,
        pagination: true,
        pageSize: 20,
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
	if(date == null || date == undefined) {
		return "-";
	}
    let temp = date.substring(0,10);
    return temp.split("-").reverse().join("/") + date.substring(10,19);
}

function formatField(value) {
  return "<span class='label label-success'>"+value+"</span>";
}