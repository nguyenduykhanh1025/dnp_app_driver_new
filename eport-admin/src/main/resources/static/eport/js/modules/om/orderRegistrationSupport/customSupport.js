"use strict";
const PREFIX = ctx + "om/order/support";

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
  if (value == 'Y') {
    return "<span class='label label-primary'>Đã thông quan</span>";
  }
  return "<span class='label label-danger'>Chưa thông quan</span>";
}

function closeForm() {
  $.modal.close();
}
