var prefix_main = ctx + "logistic/shipmentSeparating";
var houseBillSelected;
var houseBillSearch = new Object();
var toolbar = [
  {
    text: '<button type="submit" class="btn btn-w-m btn-rounder btn-success"><i class="fa fa-plus"></i> Tách lô từ Master Bill</button>',
    handler: function () {
      openFormSeparate();
    },
  },
  {
    text: '<button type="submit" class="btn btn-w-m btn-rounder btn-danger"><i class="fa fa-trash"></i> Xóa lô</button>',
    handler: function () {
      //alert("sua");
    },
  },
];

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
    height: $('.main-body').height() - 112
  });
});

// LOAD SHIPMENT LIST
function loadTable() {
  $("#dg").datagrid({
    url: prefix_main + "/houseBill/list",
    height: $('.main-body').height() - 85,
    method: "post",
    toolbar: toolbar,
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
    url: ctx + "logistic/shipmentSeparating/houseBill/detail",
    height: $('.main-body').height() - 112,
    method: "post",
    collapsible: true,
    clientPaging: false,
    pagination: false,
    rownumbers: true,
    nowrap: true,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      let opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        url: opts.url,
        data: {
          houseBillNo: houseBillNo
        },
        success: function (data) {
          if (data.code == 0) {
            success(data.houseBillDetail);
            $("#dg2").datagrid("hideColumn", "id");
            if (data.houseBillDetail.rows != null || data.houseBillDetail.rows.length > 0) {
              if (data.houseBillDetail.rows[0].releaseFlg) {
                $('#btnReleaseHouseBill').prop('disabled', true);
                $('#btnAddContToHouseBill').prop('disabled', true);
                $('#deleteBtn').prop('disabled', true);
                $('#printHouseBill').prop('disabled', false);
              } else {
                $('#btnReleaseHouseBill').prop('disabled', false);
                $('#btnAddContToHouseBill').prop('disabled', false);
                $('#deleteBtn').prop('disabled', false);
                $('#printHouseBill').prop('disabled', true);
              }
            }
          }
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

function openFormSeparate() {
  const options = {
    url: prefix_main + "/separate",
    title: "Tách Lô Từ Master Bill",
    width: '1200',
    skin: "custom-modal",
    btn: ["Xác Nhận Tách Bill", "Đóng"],
    yes: function (index, layero) {
      let iframeWin = layero.find("iframe")[0];
      iframeWin.contentWindow.submitHandler(index, layer, $("#dg"));
    },
  };
  $.modal.openOptions(options);
}

function openFormAddCont() {
  let row = $("#dg").datagrid("getSelected");
  if (row) {
    const options = {
      url: prefix_main + "/addCont/" + row.houseBillNo,
      title: "Thêm cont vào House Bill",
      skin: "custom-modal",
      btn: ["Xác Nhận Thêm Container", "Đóng"],
      yes: function (index, layero) {
        let iframeWin = layero.find("iframe")[0];
        iframeWin.contentWindow.submitHandler(index, layer, $("#dg2"), houseBillSelected);
      },
    };
    $.modal.openOptions(options);
  }
}

function formatStatus(value, row, index) {
  if (parseInt(row.edo.status) <= 1) {
    return '<span class="badge badge-primary">Chưa làm lệnh</span>';
  } else {
    return '<span class="badge badge-success">Đã làm lệnh</span>';
  }
}

function formatExpiredDem(value, row, index) {
  return formatDate(row.edo.expiredDem);
}

function printHouseBill() {
  if (!houseBillSelected) {
    $.modal.alertError("Bạn chưa chọn House Bill!");
    return
  }
  $.modal.openTab("In House Bill", ctx + "logistic/print/house-bill/" + houseBillSelected.houseBillNo);
}

function releaseHouseBill() {
  if (houseBillSelected == null) {
    $.modal.alertWarning("Quý khách chưa chọn house bill.");
  } else {
    layer.confirm("Không thể thêm hay xóa container sau khi <br>phát hành house bill. Xác nhận phát hành <br>house bill?", {
      icon: 3,
      title: "Xác Nhận",
      btn: ['Đồng Ý', 'Hủy Bỏ']
    }, function () {

      // UPDATE PROCESS ORDER TO DOING STATUS
      $.ajax({
        url: prefix_main + "/houseBill/release",
        method: "post",
        data: {
          houseBillNo: houseBillSelected.houseBillNo
        }
      }).done(function (res) {
        if (res.code == 0) {
          $.modal.alertSuccess("Phát hành house bill thành công.");
          loadHouseBillDetail(houseBillSelected.houseBillNo);
        } else {
          $.modal.alertError("Phát hành house bill thất bại, vui lòng thử lại sau.");
        }
        layer.close(layer.index);
      });
    }, function () {
      // close form do nothing
    });
  }
}

function deleteContainer() {
  let rows = $("#dg2").datagrid("getSelections");
  if (rows == null || rows.length == 0) {
    $.modal.alertWarning("Quý khách chưa chọn container cần xóa.");
    return false;
  } else {
    layer.confirm("Xác nhận xóa house bill?", {
      icon: 3,
      title: "Xác Nhận",
      btn: ['Đồng Ý', 'Hủy Bỏ']
    }, function () {
      let houseBillIds = rows.map((e) => e.id);

      $.ajax({
        url: prefix_main + "/houseBill/container",
        method: "DELETE",
        data: {
          houseBillIds: houseBillIds.join(",")
        }
      }).done(function (res) {
        if (res.code == 0) {
          $.modal.alertSuccess("Xóa container thành công.");
          loadHouseBillDetail(houseBillSelected.houseBillNo);
        } else {
          $.modal.alertError("Xóa container thất bại, vui lòng thử lại sau.");
        }
        layer.close(layer.index);
      });
    }, function () {
      // close form do nothing
    });
  }
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