var prefix_main = ctx + "edo/separated-bill";
var houseBillSelected;
var houseBillSearch = new Object();

$(".main-body").layout();
$(".collapse").click(function () {
  $(".main-body__search-wrapper").hide();
  $(".main-body__search-wrapper--container").hide();
  $(this).hide();
  $(".uncollapse").show();
});

$(".uncollapse").click(function () {
  $(".main-body__search-wrapper").show();
  $(".main-body__search-wrapper--container").show();
  $(this).hide();
  $(".collapse").show();
});

$(".left-side__collapse").click(function () {
  $("#main-layout").layout("collapse", "west");
  setTimeout(() => {
    hot.render();
  }, 200);
});

$("#main-layout").layout({
  onExpand: function (region) {
    if (region == "west") {
      hot.render();
    }
  },
});

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
  $("#masterBill").textbox('textbox').bind('keydown', function (e) {
    // enter key
    if (e.keyCode == 13) {
      houseBillSearch.masterBill = $("#masterBill").textbox('getText').toUpperCase();
      loadTable();
    }
  });

  $("#houseBill").textbox('textbox').bind('keydown', function (e) {
    // enter key
    if (e.keyCode == 13) {
      houseBillSearch.houseBill = $("#houseBill").textbox('getText').toUpperCase();
      loadTable();
    }
  });

  loadTable();
  $("#dg2").datagrid({
    height: $('.main-body').height() - 82
  });
});

// LOAD SHIPMENT LIST
function loadTable() {
  $("#dg").datagrid({
    url: prefix_main + "/houseBill/list",
    height: $('.main-body').height() - 84,
    method: "post",
    singleSelect: true,
    collapsible: true,
    clientPaging: false,
    pagination: true,
    rownumbers: true,
    onClickRow: function (index, row) {
      getSelected(index, row);
    },
    pageSize: 50,
    nowrap: true,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      let opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        url: opts.url,
        contentType: "application/json",
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: houseBillSearch,
        }),
        success: function (data) {
          success(data);
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
  $("#dg2").datagrid({
    height: $('.main-body').height() - 82
  });
}

// FORMAT DATE FOR SHIPMENT LIST
function formatDate(value) {
  let date = new Date(value);
  let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  let month = date.getMonth() + 1;
  let monthText = month < 10 ? "0" + month : month;
  let hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
  let minutes =
    date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
  let seconds =
    date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
  return (
    day +
    "/" +
    monthText +
    "/" +
    date.getFullYear() +
    " " +
    hours +
    ":" +
    minutes +
    ":" +
    seconds
  );
}

// HANDLE WHEN SELECT A SHIPMENT
function getSelected(index, row) {
  if (row) {
    houseBillSelected = row;
    $("#masterBillNo").text(row.masterBillNo);
    $("#consignee").text(row.consignee2);
    loadHouseBillDetail(row.houseBillNo);
  }
}

function loadHouseBillDetail(houseBillNo) {
  $("#dg2").datagrid({
    url: prefix_main + "/houseBill/detail",
    height: $('.main-body').height() - 82,
    method: "post",
    collapsible: true,
    clientPaging: false,
    pagination: true,
    rownumbers: true,
    pageSize: 20,
    nowrap: true,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      let opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        contentType: "application/json",
        url: opts.url,
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: houseBillNo
        }),
        success: function (data) {
          if (data.code == 0) {
            success(data.houseBillDetail);
          }
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

function formatStatusContainer(value, row) {
  // Stack status 
  let stacking = '<i id="stacking" class="fa fa-ship easyui-tooltip" title="Container chưa xuống bãi" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666;"></i>';
  if (row.params.status != null && (row.params.status == 'Stacking' || row.params.status == 'Delivered')) {
    stacking = '<i id="stacking" class="fa fa-ship easyui-tooltip" title="Container đã xuống bãi" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
  }

  // Command process status
  let process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Container chưa được khai báo" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666;"></i>';
  if (row.status) {
    process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Container đã được khai báo" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
  }
  if (row.params.jobOrderNo) {
    process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Container đã được làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
  }

  // released status
  let released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Container chưa được bốc đi" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
  if (row.params.status != null && row.params.status == 'Delivered') {
    released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Container đã được bốc đi" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
  }
  if (row.params.gateOutDate) {
    released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Container đã được bốc đi" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
  }

  // Return the content
  let content = '<div>';
  content += stacking + process + released;
  content += '</div>';
  return content;
}

function formatToYDMHMS(date) {
  if (date == null || date == undefined) {
    return "-";
  }
  let temp = date.substring(0, 10);
  return temp.split("-").reverse().join("/") + date.substring(10, 19);
}

function formatAction(value, row, index) {
  var actions = [];
  actions.push('<a class="btn btn btn-info btn-xs" onclick="viewHistoryCont(\'' + row.id + '\')"><i class="fa fa-history"></i> Lịch sử</a> ');
  return actions.join("");
}

function viewHistoryCont(id) {
  $.modal.openWithOneButton('Lịch sử cập nhật', ctx + "edo/manage/history/" + id, 1000, 400);
}

function formatStatus(value, row, index) {
  if (parseInt(row.status) <= 1) {
    return '<span class="badge badge-primary">Chưa làm lệnh</span>';
  } else {
    return '<span class="badge badge-success">Đã làm lệnh</span>';
  }
}

function formatExpiredDem(value, row, index) {
  return formatDate(row.expiredDem);
}

function printHouseBill() {
  if (houseBillSelected == null) {
    $.modal.alertError("Bạn chưa chọn house bill!");
    return;
  }
  $.modal.openTab("In House Bill", ctx + "edo/print/house-bill/" + houseBillSelected.houseBillNo);
}

function search() {
  shipmentSearch.masterBill = $("#masterBill").textbox('getValue');
  shipmentSearch.houseBill = $("#houseBill").textbox('getValue');
  loadTable();
}

function clearInput() {
  shipmentSearch = new Object();
  $("#masterBill").textbox('setText', '');
  $("#houseBill").textbox('setText', '');
  loadTable();
}

function reload() {
  loadHouseBillDetail(houseBillSelected.houseBillNo);
}

function lockEdo() {
  // Get selected rows in datagrid right
  let rows = $('#dg2').datagrid('getSelections');
  // Check if any row is selected
  if (!rows.length) {
    $.modal.alertWarning("Bạn chưa chọn container.");
  } else {
    layer.confirm("Xác nhận đã làm lệnh cho các container đã <br>chọn. Thực hiện khóa thay đổi dữ liệu trên <br>ePort.", {
      icon: 3,
      title: "Xác Nhận",
      btn: ['Đồng Ý', 'Hủy Bỏ']
    }, function () {
      let edoIds = '';
      rows.forEach(function (row, index) {
        edoIds += row.id + ",";
      });
      $.ajax({
        url: prefix_main + "/order/lock",
        method: "POST",
        data: {
          edoIds: edoIds.substring(0, edoIds.length - 1),
        },
        success: function (res) {
          layer.close(layer.index);
          if (res.code == 0) {
            $.modal.alertSuccess(res.msg);
          } else {
            $.modal.alertError(res.msg);
          }
          reload();
        },
        error: function (err) {
          console.log(err);
          $.modal.alertError("Lỗi hệ thống, vui lòng liên hệ admin.");
          layer.close(layer.index);
        }
      });
    }, function () {
      // Close form and do nothing
    });
  }
}

function unlockEdo() {
  // Get selected rows in datagrid right
  let rows = $('#dg2').datagrid('getSelections');
  // Check if any row is selected
  if (!rows.length) {
    $.modal.alertWarning("Bạn chưa chọn container.");
  } else {
    layer.confirm("Xác nhận đã hủy lệnh cho các container đã <br>chọn.", {
      icon: 3,
      title: "Xác Nhận",
      btn: ['Đồng Ý', 'Hủy Bỏ']
    }, function () {
      let edoIds = '';
      rows.forEach(function (row, index) {
        edoIds += row.id + ",";
      });
      $.ajax({
        url: prefix_main + "/order/unlock",
        method: "POST",
        data: {
          edoIds: edoIds.substring(0, edoIds.length - 1),
        },
        success: function (res) {
          layer.close(layer.index);
          if (res.code == 0) {
            $.modal.alertSuccess(res.msg);
          } else {
            $.modal.alertError(res.msg);
          }
          reload();
        },
        error: function (err) {
          console.log(err);
          $.modal.alertError("Lỗi hệ thống, vui lòng liên hệ admin.");
          layer.close(layer.index);
        }
      });
    }, function () {
      // Close form and do nothing
    });
  }
}