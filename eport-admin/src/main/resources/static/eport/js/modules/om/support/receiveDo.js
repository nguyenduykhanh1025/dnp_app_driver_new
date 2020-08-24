const PREFIX = ctx + "om/support";
var bill;
var processOrder = new Object();
var shipmentDetails = new Object();
var currentLeftWidth = $(".table-left").width();
var currentRightWidth = $(".table-right").width();

$(document).ready(function () {
  $("#btn-collapse").click(function () {
    handleCollapse(true);
  });
  $("#btn-uncollapse").click(function () {
    handleCollapse(false);
  });
  loadTable(processOrder);
  $('#checkDoStatus').attr("disabled", true);
  
  $("#searchInfoProcessOrder").keyup(function (event) {
    if (event.keyCode == 13) {
      blNo = $("#searchInfoProcessOrder").val().toUpperCase();
      if (blNo == "") {
        loadTable(processOrder);
      }
      processOrder.blNo = blNo;
      loadTable(processOrder);
    }
  });
  
});

function handleCollapse(status) {
  if (status) {
    $(".left").css("width", "0.5%");
    $(".left").children().hide();
    $("#btn-collapse").hide();
    $("#btn-uncollapse").show();
    $(".right").css("width", "99%");
    return;
  }
  $(".left").css("width", "25%");
  $(".left").children().show();
  $("#btn-collapse").show();
  $("#btn-uncollapse").hide();
  $(".right").css("width", "74%");
  return;
}

function loadTable(processOrder) {
  $("#dg").datagrid({
    url: PREFIX + "/orders",
    method: "POST",
    singleSelect: true,
    height: $(document).height() - 60,
    clientPaging: true,
    collapsible: true,
    pagination: true,
    pageSize: 20,
    rownumbers: true,
    onClickRow: function () {
      getSelectedRow();
    },
    nowrap: false,
    striped: true,
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
          data: processOrder,
        }),
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
loadTableByContainer();
function loadTableByContainer(shipmentDetails) {
  $("#dgContainer").datagrid({
    url: PREFIX + "/shipmentDetails",
    method: "POST",
    height: $(document).height() - 60,
    singleSelect: true,
    clientPaging: true,
    pagination: true,
    pageSize: 20,
    nowrap: false,
    striped: true,
    rownumbers: true,
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
      // if (shipmentDetails.processOrderId == null) {
      //   return false;
      // }
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
          data: shipmentDetails,
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

function getSelectedRow() {
  var row = $("#dg").datagrid("getSelected");
  if (row) {
    shipmentDetails.processOrderId = row.id;
    $('#checkDoStatus').attr("disabled", false);
    loadTableByContainer(shipmentDetails);
  }
}

function formatBlBooking(value, row) {
  if (row.blNo) {
    return row.blNo;
  }
  if (row.bookingNo) {
    return row.bookingNo;
  }
  return "";
}

function formatLogistic(value, row, index) {
  return '<a onclick="logisticInfo(' + row.logisticGroupId + "," + "'" + value + "')\"> " + value + "</a>";
}

function logisticInfo(id, logistics) {
  $.modal.openLogisticInfo("Thông tin liên lạc " + logistics, ctx + "om/support/logistics/" + id + "/info", null, 470, function() {
    $.modal.close();
  });
}

function formatCustomStatus(value, row) {
  switch (value) {
    case 'R':
      return '<span class="badge badge-success">Yes</span>'; 
    case 'Y':
      return '<span class="badge badge-danger">No</span>';
    case 'N':
      return '<span class="badge badge-danger">No</span>';
    default:
      return '-';
  }
}

// FORMATTER
function formatStatus(value, row) {
  switch (value) {
    case 'R':
      return '<span class="badge badge-success">Yes</span>'; 
    case 'Y':
      return '<span class="badge badge-danger">No</span>';
    case 'N':
      return '<span class="badge badge-danger">No</span>';
    default:
      return '-';
  }
}


function checkCustomStatusByProcessOrderId(processOrderId) {
  processOrderId =  shipmentDetails.processOrderId;
  if(processOrderId == null || processOrder == undefined)
  {
    $.modal.alertWarning("Bạn chưa chọn lệnh <br> Vui lòng kiểm tra dữ liệu.");
  }
  console.log("checkCustomStatusByProcessOrderId -> processOrderId", processOrderId)
  layer.confirm("Xác nhận đồng bộ dữ liệu thông quan với <br>catos.", {
    icon: 3,
    title: "Xác Nhận",
    btn: ['Đồng Ý', 'Hủy Bỏ']
  }, function () {
    layer.close(layer.index);
    $.modal.loading("Đang xử lý...");
    $.ajax({
      url: PREFIX + "/customStatus",
      method: "POST",
      data: {
        processOrderId: processOrderId
      },
      success: function (data) {
        $.modal.closeLoading();
        if (data.code == 0) {
          $.modal.alertSuccess("Đồng bộ dữ liệu thành công.");
          loadTableByContainer();
        } else {
          $.modal.alertWarning("Đồng bộ dữ liệu thất bại.");
        }
      },
      error: function (data) {
        $.modal.alertWarning("Đồng bộ dữ liệu thất bại.");
        $.modal.closeLoading();
      }
    });
  }, function () {
    // DO NOTHING
  });
}

function formatVessel(value, row) {
  return row.vslNm + " - " + row.vslName + " - " + row.voyNo;
}

$('#logistic').change(function() {
  if (0 != $('#logistic option:selected').val()) {
    processOrder.logisticGroupId = $('#logistic option:selected').val();
  } else {
    processOrder.logisticGroupId = '';
  }
  loadTable(processOrder);
});

function formatDoStatus(value, row) {
    if ('Y' == value) {
      return '<span class="badge badge-success">Yes</span>';
    }
    return '<span class="badge badge-danger">No</span>';
  }

  function checkDoStatus(processOrderId) {
    processOrderId =  shipmentDetails.processOrderId;
    if(processOrderId == null || processOrder == undefined)
    {
      $.modal.alertWarning("Bạn chưa chọn lệnh <br> Vui lòng kiểm tra dữ liệu.");
    }
    layer.confirm("Xác nhận đã nhận đủ giấy tờ.", {
      icon: 3,
      title: "Xác Nhận",
      btn: ['Đồng Ý', 'Hủy Bỏ']
    }, function () {
      layer.close(layer.index);
      $.modal.loading("Đang xử lý...");
      $.ajax({
        url: PREFIX + "/doStatus",
        method: "POST",
        data: {
          processOrderId: processOrderId
        },
        success: function (data) {
          $.modal.closeLoading();
          if (data.code == 0) {
            $.modal.alertSuccess("Cập nhật dữ liệu thành công.");
          } else {
            $.modal.alertWarning("Cập nhật dữ liệu thất bại.");
          }
        },
        error: function (data) {
          $.modal.alertWarning("Cập nhật dữ liệu thất bại.");
          $.modal.closeLoading();
        }
      });
    }, function () {
      // DO NOTHING
    });
  }
