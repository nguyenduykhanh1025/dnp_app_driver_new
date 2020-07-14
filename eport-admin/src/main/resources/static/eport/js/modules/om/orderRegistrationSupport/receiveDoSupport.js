"use strict";
const PREFIX = ctx + "om/order/support";
var notification = new Object();

$(document).ready(function () {
  $("#toggle-status").bootstrapToggle();
  loadTable();
});

function loadTable() {
  $("#dg").datagrid({
    url: "/notifications" + "/list",
    method: "POST",
    singleSelect: true,
    height: $(document).height() - 170,
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
        contentType: "application/json",
        accept: "text/plain",
        dataType: "text",
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: notification,
        }),
        success: function (data) {
          success(JSON.parse(data));
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

$("#toggle-status").change(function(e) {
  console.log($(this).prop('checked'));
});
