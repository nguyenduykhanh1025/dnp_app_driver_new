var prefix = ctx + "om/executeCatos";

// Init screen
$(document).ready(function () {
  loadTableShipment();
});

// Init data for table list shipment wait execute
function loadTableShipment() {
  $("#dg").datagrid({
    url: prefix + "/listWaitExec",
    height: document.documentElement.clientHeight - 90,
    singleSelect: true,
    collapsible: true,
    clientPaging: false,
    pagination: true,
    fitColumns: true,
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
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
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

// Format for column "Lệnh" of table shipment wait execute
function formatProcess(value) {
  return "Bốc cont hàng ra khỏi cảng";
}

// Format  for column "B/L No" of table shipment wait execute
function formatBLNo(value) {
  return "ABC123";
}

// Format for column "Tàu" of table shipment wait execute
function formatShip(value) {
  return "ABCDEF";
}

// Format for column "Trạng thái" of table shipment wait execute
function formatProcessStatus(value) {
  if (value === "N") {
    return '<div class="text-warning">Chờ làm lệnh</div>';
  }

  if (value === "E") {
    return '<div class="text-danger">Lỗi làm lệnh</div>';
  }
}

// Format for column "Action" of table shipment wait execute
function formatAction(value, row, index) {
  return (
    '<button type="button" class="btn btn-primary btn-xs" onclick="update(\'' +
    row.shipmentDetailIds +
    "')\">làm lệnh</button>"
  );
}

// Open modal update process status of shipment
function update(shipmentDetailIds) {
  const options = {
    url: prefix + "/edit/" + shipmentDetailIds,
    title: "Thông tin Lô",
    skin: "custom-modal",
    btn: ["Copy Data", "Đã làm lệnh", "Đóng"],
    yes: function (index, layero) {
      let iframeWin = layero.find("iframe")[0];
      iframeWin.contentWindow.submitHandler(index, layero);
    },
    btn2: function (index, layero) {
      let url = prefix + "/edit";
      let data = {shipmentDetailIds: shipmentDetailIds.split(",").map(Number)};
      let callback = function(result) {
        if (result.code == web_status.SUCCESS) {
              $.modal.alertSuccess(result.msg)
          } else if (result.code == web_status.WARNING) {
              $.modal.alertWarning(result.msg)
          } else {
            $.modal.alertError(result.msg);
          }
        $.modal.closeLoading();
        $("#dg").datagrid('reload');
      };
      $.operate.saveModal(url, data, callback);
      return true;
    }
  };
  $.modal.openOptions(options);
}
