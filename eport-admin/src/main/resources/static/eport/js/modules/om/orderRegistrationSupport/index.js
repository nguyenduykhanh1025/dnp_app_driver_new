"use strict";
const PREFIX = ctx + "om/order/support";
var notification = new Object();

$(document).ready(function () {
    loadTable();
});

function loadTable() {
  $("#dg").datagrid({
    url: "/notifications" + "/list",
    method: "POST",
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

function formatAction(value, row, index) {
  var actions = [];
  actions.push('<a class="btn btn-primary btn-xs" onclick="openVerificationSupport(\'' + row.id + '\')"><i class="fa fa-view"></i>Hải quan</a> ');
  actions.push('<a class="btn btn-success btn-xs" onclick="openCustomSupport(\'' + row.id + '\')"><i class="fa fa-view"></i>Làm lệnh</a> ');
  actions.push('<a class="btn btn-default btn-xs" onclick="openPaymentSupport(\'' + row.id + '\')"><i class="fa fa-view"></i>Thanh toán</a> ');
  actions.push('<a class="btn btn-danger btn-xs" onclick="openReceiverDOSupport(\'' + row.id + '\')"><i class="fa fa-view"></i>Nộp DO gốc</a> ');
  actions.push('<a class="btn btn-warning btn-xs" onclick="openDriverSupport(\'' + row.id + '\')"><i class="fa fa-view"></i>Vận tải</a> ');
  return actions.join("");
}

function openVerificationSupport(id) {
  $.modal.openWithOneButton("Hải quan", PREFIX + "/verification/");
}

function openPaymentSupport(id) {
  $.modal.open("Thanh toán", PREFIX + "/payment/");
}

function openCustomSupport(id) {
  $.modal.openWithOneButton("Làm lệnh", PREFIX + "/custom/", null, null, null, "Xác nhận");
}

function openReceiverDOSupport(id) {
  $.modal.open("DO gốc", PREFIX + "/do/");
}

function openDriverSupport(id) {
  $.modal.open("Tài xế", PREFIX + "/driver/");
}
