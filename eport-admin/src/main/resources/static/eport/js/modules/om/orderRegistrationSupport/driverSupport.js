const PREFIX = ctx + "om/order/support";
const DOCUMENT_HEIGHT = $(document).height();

$(document).ready(function () {
  loadTable();
});

function loadTable() {
  $("#dg").datagrid({
    height: DOCUMENT_HEIGHT - 70,
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