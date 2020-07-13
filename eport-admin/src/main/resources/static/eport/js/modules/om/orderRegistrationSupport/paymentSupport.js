"use strict";
const PREFIX = ctx + "om/order/support";

$(document).ready(function () {
  $("#dgOrder").datagrid({
    height: $(document).height()/2 - 70,
    singleSelect: true,
    clientPaging: false,
    pagination: false,
    rownumbers: true,
    nowrap: false,
    striped: true,
    loader: function (param, success, error) {
      success(orderList);
    },
  });
});

