const PREFIX = "/history/robot";
$(function () {
  loadTable();
});

function formatDate(value) {
    if (value == null) return "";
    var date = new Date(value);
    return formatNumber(date.getDate()) 
            + "/" + formatNumber(date.getMonth() + 1) 
            + "/" + date.getFullYear()
            + " " + formatNumber(date.getHours())
            + ":" + formatNumber(date.getMinutes());
}

function formatNumber(number) {
    return number < 10 ? "0" + number : number;
}

function formatResult(value) {
    return value == "S" ? "<span class='label label-success'>Thành công</span>" : "<span class='label label-danger'>Thất bại</span>";
}

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
    nowrap: false,
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
          console.log(data);
          success(data);
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}
