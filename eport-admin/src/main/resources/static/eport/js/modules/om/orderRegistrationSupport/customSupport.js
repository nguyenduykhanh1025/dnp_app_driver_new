"use strict";
const PREFIX = ctx + "om/support";

$(document).ready(function () {
  loadTable();
});

function loadTable() {
  $("#dg").datagrid({
    height: $(document).height() - 100,
    singleSelect: true,
    clientPaging: false,
    pagination: false,
    rownumbers: true,
    nowrap: false,
    striped: true,
    loader: function (param, success, error) {
      success(shipmentDetails);
    },
  });
}

function formatCustomStatus(value, row, index) {
  if (value == 'R') {
    return "<span class='label label-primary'>Đã thông quan</span>";
  }
  return "<span class='label label-danger'>Chưa thông quan</span>";
}

function closeForm() {
  $.modal.close();
}

function syncCustomStatus() {
  $.modal.loading("Đang xử lý...");
  $.ajax({
    url: PREFIX + "/custom",
    method: "POST",
    contentType: "application/json",
    data: JSON.stringify(shipmentDetails)
  }).done(function (res) {
    $.modal.closeLoading();
    if (res.code == 0) {
      $("#dg").datagrid({
        height: $(document).height() - 100,
        singleSelect: true,
        clientPaging: false,
        pagination: false,
        rownumbers: true,
        nowrap: false,
        striped: true,
        loader: function (param, success, error) {
          success(res.shipmentDetails);
        },
      });
      $.modal.alertSuccess(res.msg);
    } else {
      $.modal.alertError("Có lỗi xảy ra trong quá trình đồng bộ dữ liệu!");
    }
  });
}
