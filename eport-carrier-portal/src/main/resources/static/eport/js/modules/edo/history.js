var PREFIX = ctx + "edo";
var originalValue;
$(function () {
    loadTable();
});

function closeForm() {
    $.modal.close();
}

function loadTable() {
    $("#dg").datagrid({
        url: PREFIX + "/auditLog/" + edoId,
        method: "GET",
        height: $(document).height() - 65,
        singleSelect: true,
        clientPaging: true,
        pagination: true,
        pageSize: 20,
        nowrap: false,
        striped: true,
        loader: function (param, success, error) {
            var opts = $(this).datagrid("options");
            if (!opts.url) return false;
            $.ajax({
                type: opts.method,
                url: opts.url,
                dataType: "json",
                success: function (data) {
                    let lenghtRow = data.rows.length;
                    originalValue = data.rows[lenghtRow-1].newValue
                    success(data);
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });
}

function formatField(value) {
    return "<span class='label label-success'>" + value + "</span>";
}

function formatToYDMHMS(date) {
    if(date == null || date == undefined)
    {
        return;
    }
    let temp = date.substring(0, 10);
    return temp.split("-").reverse().join("/") + "</br>" + date.substring(10, 19);
}

function formatOldValue(val,row)
{
    if(val == 'ORIGINAL VALUE')
    {
        let originalArray = originalValue.split(',');
        for(let i = 0 ;i < originalArray.length ; i++)
        {
           let checkOldValue = originalArray[i].split(':');
           if(checkOldValue.length > 2)
           {
            return checkOldValue[1] + ":" + checkOldValue[2] + ":" + checkOldValue[3]; 
           }
           if(checkOldValue[0].trim() == row.fieldName)
           {
                return checkOldValue[1]; 
           }
        }
    }
    return val;
}