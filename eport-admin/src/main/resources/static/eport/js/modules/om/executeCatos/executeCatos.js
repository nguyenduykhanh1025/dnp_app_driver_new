var prefix = ctx + "om/executeCatos";
var processOrder = new Object();

// Init screen
$(document).ready(function () {
  processOrder.status = 0;

  loadTable();

  $('#searchAllInput').keyup(function (event) {
    if (event.keyCode == 13) {
      processOrder.taxCode = $('#searchAllInput').val().toUpperCase();
      processOrder.vessel = $('#searchAllInput').val().toUpperCase();
      processOrder.voyage = $('#searchAllInput').val().toUpperCase();
      loadTable();
    }
  });
});

// Init data for table list shipment wait execute
function loadTable() {
  $("#dg").datagrid({
    url: prefix + "/listWaitExec",
    method: "POST",
    singleSelect: true,
    height: document.documentElement.clientHeight - 110,
    clientPaging: false,
    pagination: true,
    rownumbers: true,
    pageSize: 50,
    nowrap: true,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        url: opts.url,
        contentType: "application/json",
        accept: 'text/plain',
        dataType: 'text',
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: processOrder
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

function formatServiceType(value) {
  switch (value) {
    case 1:
      return 'Bốc hàng';
    case 2:
      return 'Hạ rỗng';
    case 3:
      return 'Bốc rỗng';
    case 4:
      return 'Hạ hàng';
  }
}

function formatStatus(value) {
  switch (value) {
    case 0:
      return 'Đang chờ';
    case 1:
      return 'Đang làm';
    case 2:
      return 'Đã hoàn thành';
  }
}

function changeServiceType() {
  processOrder.serviceType = $('#seviceTypeSelect').val();
  loadTable();
}

function changeStatus() {
  processOrder.status = $('#status').val();
  loadTable();
}

// Format for column "Action" of table shipment wait execute
function formatAction(value, row, index) {
  return (
    '<button type="button" class="btn btn-primary btn-xs" onclick="openMakeOrderForm(\'' +
    row.id +
    "')\">Làm Lệnh</button>"
  );
}

function openMakeOrderForm(id) {
  $.modal.openWithOneButton("Làm lệnh", prefix + "/detail/" + id, null, null);
}

function finishForm(res) {
  if (res.code == 0) {
    $.modal.msgSuccess(res.msg);
  } else {
    $.modal.msgError(res.msg);
  }
  loadTable();
}

// Open modal update process status of shipment
// function update(shipmentDetailIds) {
//   const options = {
//     url: prefix + "/edit/" + shipmentDetailIds,
//     title: "Thông tin Lô",
//     skin: "custom-modal",
//     btn: ["Copy Data", "Đã làm lệnh", "Đóng"],
//     yes: function (index, layero) {
//       let iframeWin = layero.find("iframe")[0];
//       iframeWin.contentWindow.submitHandler(index, layero);
//     },
//     btn2: function (index, layero) {
//       let url = prefix + "/edit";
//       let data = {shipmentDetailIds: shipmentDetailIds.split(",").map(Number)};
//       let callback = function(result) {
//         if (result.code == web_status.SUCCESS) {
//               $.modal.alertSuccess(result.msg)
//           } else if (result.code == web_status.WARNING) {
//               $.modal.alertWarning(result.msg)
//           } else {
//             $.modal.alertError(result.msg);
//           }
//         $.modal.closeLoading();
//         $("#dg").datagrid('reload');
//       };
//       $.operate.saveModal(url, data, callback);
//       return true;
//     }
//   };
//   $.modal.openOptions(options);
// }
