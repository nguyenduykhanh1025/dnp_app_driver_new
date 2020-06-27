$(function () {
    loadTable();
  });

  var prefix = "/edo/manage"
  function loadTable() {
    $("#dg").datagrid({
      url: prefix + "/getHistory",
      method: "GET",
      singleSelect: true,
      height: document.documentElement.clientHeight - 70,
      clientPaging: false,
      pagination: true,
      rownumbers: true,
      pageSize: 50,
      nowrap: false,
      striped: true,
      loadMsg: " Đang xử lý...",
      loader: function (param, success, error) {
        var opts = $(this).datagrid("options");
        if (!opts.url) return false;
        $.ajax({
          type: opts.method,
          url: opts.url,
          data: {
            
          },
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
  function formatAction(value, row, index) {
    var actions = [];
      actions.push('<a class="btn btn-success btn-xs" onclick="viewEdiFile(\'' + row.id + '\')"><i class="fa fa-view"></i>Xem file</a> ');
      return actions.join('');
  }
  function viewEdiFile(id)
  {
     alert("OK  "+id);
  }

  function showViewEdi()
  {
      
  }
  
