const PREFIX = ctx + "om/generalSupport";
var shipment = new Object();
var processOrder = new Object();
var currentShipment;
var conf;

$(document).ready(function () {
  "use strict";
  $(".left").css("height", $(document).height() - 100);
  $(".right").css("height", $(document).height() - 100);
  $("#btn-collapse").click(function () {
    handleCollapse(true);
  });
  $("#btn-uncollapse").click(function () {
    handleCollapse(false);
  });
  $("#dg").height($(document).height() - 100);
  $("#dgContainer").height($(document).height() - 100);

  // $(".c-search-box-vessel").select2({
  //   theme: "bootstrap",
  //   placeholder: "Vessel",
  //   allowClear: true,
  // });

  // $(".c-search-box-vessel-code").select2({
  //   theme: "bootstrap",
  //   placeholder: "Vessel Code",
  //   allowClear: true,
  // });

  // $(".c-search-box-voy-no").select2({
  //   theme: "bootstrap",
  //   placeholder: "Voy No",
  //   allowClear: true,
  // });
  // $(".c-search-opr-code").select2({
  //   theme: "bootstrap",
  //   placeholder: "OPR Code",
  //   allowClear: true,
  // });

  laydate.render({
    elem: '#fromDate',
    format: "dd/MM/yyyy"
  });

  laydate.render({
    elem: '#toDate',
    format: "dd/MM/yyyy"
  });

  $("#searchBox").keyup(function (event) {
    if (event.keyCode == 13) {
      shipment.blNo = $("#searchBox").val().toUpperCase();
      shipment.bookingNo = $("#searchBox").val().toUpperCase();
      shipment.taxCode = $("#searchBox").val().toUpperCase();
      loadTable();
    }
  });
  loadTable();
});

function handleCollapse(status) {
  if (status) {
    $(".left").css("width", "0.5%");
    $(".left").children().hide();
    $("#btn-collapse").hide();
    $("#btn-uncollapse").show();
    $(".right").css("width", "99%");
    $('#dg-right').datagrid('resize',{
      width: document.getElementsByClassName("right").width,
      height: document.documentElement.clientHeight - 70
    });
    return;
  }
  $(".left").css("width", "30%");
  $(".left").children().show();
  $("#btn-collapse").show();
  $("#btn-uncollapse").hide();
  $(".right").css("width", "70%");
  $('#dg-right').datagrid('resize',{
    width: document.getElementsByClassName("right").width,
    height: document.documentElement.clientHeight - 70
  });
  return;
}

// SEARCH

function stringToDate(dateStr) {
  let dateParts = dateStr.split("/");
  return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}

$.event.special.inputchange = {
  setup: function () {
    var self = this,
      val;
    $.data(
      this,
      "timer",
      window.setInterval(function () {
        val = self.value;
        if ($.data(self, "cache") != val) {
          $.data(self, "cache", val);
          $(self).trigger("inputchange");
        }
      }, 20)
    );
  },
  teardown: function () {
    window.clearInterval($.data(this, "timer"));
  },
  add: function () {
    $.data(this, "cache", this.value);
  },
};

$("#fromDate").on("inputchange", function () {
  let fromDate = stringToDate($('#fromDate').val());
  if ($('#toDate').val() != '' && stringToDate($('#toDate').val()).getTime() < fromDate.getTime()) {
    $.modal.alertError('Quý khách không thể chọn từ ngày cao hơn đến ngày.')
    $('#fromDate').val('');
  } else {
    shipment.fromDate = fromDate.getTime();
    loadTable();
  }
});

$("#toDate").on("inputchange", function () {
  let toDate = stringToDate($("#toDate").val());
  if ($("#fromDate").val() != "" && stringToDate($("#fromDate").val()).getTime() > toDate.getTime()) {
    $.modal.alertError("Quý khách không thể chọn đến ngày thấp hơn từ ngày.");
    $("#toDate").val("");
  } else {
    toDate.setHours(23, 59, 59);
    shipment.toDate = toDate.getTime();
    loadTable();
  }
});


// LEFT TABLE
function loadTable() {
  $("#dg").datagrid({
    url: ctx + "om/support/shipments",
    method: "POST",
    singleSelect: true,
    height: document.documentElement.clientHeight - 70,
    clientPaging: false,
    pagination: true,
    rownumbers: true,
    onClickRow: function () {
      getSelected();
    },
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
        accept: "text/plain",
        dataType: "text",
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: shipment,
        }),
        success: function (data) {
          success(JSON.parse(data));
          $("#dg").datagrid("hideColumn", "id");
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

function getSelected() {
  var row = $("#dg").datagrid("getSelected");
  if (row) {
    currentShipment = row;
    processOrder.shipmentId = row.id
    loadRightTable();
  }
}

function logisticInfo(id, logistics) {
  $.modal.openLogisticInfo("Thông tin liên lạc " + logistics, ctx + "om/support/logistics/" + id + "/info", null, 470, function() {
    $.modal.close();
  });
}

$('#logistic').change(function() {
  if (0 != $('#logistic option:selected').val()) {
    shipment.logisticGroupId = $('#logistic option:selected').val();
  } else {
    shipment.logisticGroupId = '';
  }
  loadTable();
});

function changeServiceType() {
  shipment.serviceType = $('#serviceType option:selected').val();
  loadTable();
}

// FORMATTER
function formatLogistic(value, row, index) {
  return '<a onclick="logisticInfo(' + row.logisticGroupId + "," + "'" + value + "')\"> " + value + "</a>";
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

function formatServiceType(value) {
  switch (value) {
    case 1:
      return "Bốc Hàng";
    case 2:
      return "Hạ Rỗng";
    case 3:
      return "Bốc Rỗng";
    case 4:
      return "Hạ Hàng";
  }
}

function formatDate(value) {
  if (value) {
    return value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4) + value.substring(10, 19);
  }
  return '-';
}

function formatText(value) {
  return '<div class="easyui-tooltip" title="' + (value != null ? value : "Trống") + '" style="width: 80; text-align: center;"><span>' + (value != null ? value : "") + "</span></div>";
}

// RIGHT TABLE
function changeOrderStatus() {
  if ($('#orderStatus').val() == 3) {
    processOrder.status = 0;
    processOrder.robotUuid = 'blank';
  } else if ($('#orderStatus').val() != '') {
    processOrder.status = $('#orderStatus').val();
    processOrder.robotUuid = '';
  } else {
    processOrder.robotUuid = null;
  }
  loadRightTable();
}
loadRightTable();
function loadRightTable() {
  $.modal.loading("Đang xử lý ...");
  $.ajax({
    url: PREFIX + "/orders",
    method: "POST",
    data: JSON.stringify(processOrder),
    contentType: "application/json",
    success: function (data) {
      $.modal.closeLoading();
      if (data.code == 0) {
        $("#dg-right").datagrid({
          width: document.getElementsByClassName("right").width,
          height: document.documentElement.clientHeight - 70,
          loader: function (param, success, error) {
            success(data.processOrders);
          },
          onLoadSuccess:function(row) {
            // TODO
          },
          view: detailview,
          detailFormatter:function(index,row){
            return '<div style="padding:2px"><table class="ddv"></table></div>';
          },
          onExpandRow: function (index, row) {
            var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
            $.ajax({
              url: PREFIX + "/shipment/" + row.shipmentId + "/processOrder/"+ row.id + "/shipmentDetails",
              method: "GET",
              success: function (data) {
                $.modal.closeLoading();
                if (data.code == 0) {
                  switch (currentShipment.serviceType) {
                    // Receive cont full
                    case 1:
                      ddv.datagrid({
                        rownumbers: true,
                        columns:[[
                          {field:'containerNo',title:'Container No.',width:120, halign:'center', align:'center'},
                          {field:'sztp',title:'sztp',width:60, halign:'center', align:'center'},
                          {field:'consignee',title:'Chủ Hàng',width:100, halign:'center', align:'left'},
                          {field:'expiredDem',title:'Hạn Lệnh',width:100, halign:'center', align:'center', formatter: formatExpiredDem},
                          {field:'vessel',title:'Tàu/Chuyến',width:150, halign:'center', align:'left', formatter: formatVessel},
                          {field:'loadingPort',title:'POL',width:70, halign:'center', align:'left'},
                          {field:'dischargePort',title:'POD',width:70, halign:'center', align:'left'},
                          {field:'customStatus',title:'Hải Quan  <a class="btn btn-primary btn-xs" onclick="checkCustomStatusByProcessOrderId(\'' + row.id + '\')"><i class="fa fa-check-circle"></i>Đồng bộ</a>',width:150, halign:'center', align:'center', formatter: formatCustomStatus},
                          {field:'emptyDepot',title:'Nơi Hạ Vỏ',width:100, halign:'center', align:'right'},
                          {field:'detFreeTime',title:'Miễn Lưu',width:80, halign:'center', align:'right'},
                          {field:'isSendEmpty',title:'Hạ Rỗng',width:80, halign:'center', align:'center', formatter: formatIsSendEmpty},
                          {field:'info',title:'Chi tiết',width:100, halign:'center', align:'center', formatter: formatInfo}
                        ]],
                        loader: function (param, success, error) {
                          success(data.shipmentDetails);
                        },
                        onLoadSuccess:function(){
                          $('#dg-right').datagrid('fixDetailRowHeight',index);
                        }
                      });
                      break;
                    case 2:
                      // Send cont empty
                      ddv.datagrid({
                        rownumbers: true,
                        columns:[[
                          {field:'containerNo',title:'Container No.',width:120, halign:'center', align:'center'},
                          {field:'sztp',title:'sztp',width:60, halign:'center', align:'center'},
                          {field:'opeCode',title:'OPR',width:50, halign:'center', align:'center'},
                          {field:'expiredDem',title:'Hạn Lệnh',width:100, halign:'center', align:'center', formatter: formatExpiredDem},
                          {field:'emptyDepot',title:'Nơi Hạ Vỏ',width:100, halign:'center', align:'left'},
                          {field:'vessel',title:'Tàu/Chuyến',width:90, halign:'center', align:'center', formatter: formatVesselEmpty},
                          {field:'loadingPort',title:'POL',width:70, halign:'center', align:'left'},
                          {field:'dischargePort',title:'POD',width:70, halign:'center', align:'left'},
                          {field:'omRemark',title:'Ghi Chú (OM)',width:200, halign:'center', align:'left'}
                        ]],
                        loader: function (param, success, error) {
                          success(data.shipmentDetails);
                        },
                        onLoadSuccess:function(){
                          $('#dg-right').datagrid('fixDetailRowHeight',index);
                        }
                      });
                      break;
                    case 3:
                      // Receive cont empty
                      ddv.datagrid({
                        rownumbers: true,
                        columns:[[
                          {field:'containerNo',title:'Container No.',width:120, halign:'center', align:'center'},
                          {field:'sztp',title:'sztp',width:60, halign:'center', align:'center'},
                          {field:'opeCode',title:'OPR',width:50, halign:'center', align:'center'},
                          {field:'expiredDem',title:'Hạn Lệnh',width:100, halign:'center', align:'center', formatter: formatExpiredDem},
                          {field:'vessel',title:'Tàu/Chuyến',width:150, halign:'center', align:'left', formatter: formatVessel},
                          {field:'loadingPort',title:'POL',width:70, halign:'center', align:'left'},
                          {field:'dischargePort',title:'POD',width:70, halign:'center', align:'left'},
                          {field:'omRemark',title:'Ghi Chú (OM)',width:200, halign:'center', align:'left'}
                        ]],
                        loader: function (param, success, error) {
                          success(data.shipmentDetails);
                        },
                        onLoadSuccess:function(){
                          $('#dg-right').datagrid('fixDetailRowHeight',index);
                        }
                      });
                      break;
                    case 4:
                      // Send cont full
                      ddv.datagrid({
                        rownumbers: true,
                        columns:[[
                          {field:'containerNo',title:'Container No.',width:120, halign:'center', align:'center'},
                          {field:'sztp',title:'sztp',width:60, halign:'center', align:'center'},
                          {field:'consignee',title:'Chủ Hàng',width:100, halign:'center', align:'left'},
                          {field:'expiredDem',title:'Hạn Lệnh',width:100, halign:'center', align:'center', formatter: formatExpiredDem},
                          {field:'vessel',title:'Tàu/Chuyến',width:150, halign:'center', align:'left', formatter: formatVessel},
                          {field:'loadingPort',title:'POL',width:70, halign:'center', align:'left'},
                          {field:'dischargePort',title:'POD',width:70, halign:'center', align:'left'},
                          {field:'customStatus',title:'Hải Quan  <a class="btn btn-primary btn-xs" onclick="checkCustomStatusByProcessOrderId(\'' + row.id + '\')"><i class="fa fa-check-circle"></i>Đồng bộ</a>',width:150, halign:'center', align:'center', formatter: formatCustomStatus},
                          {field:'info',title:'Chi tiết',width:100, halign:'center', align:'center', formatter: formatInfo}
                        ]],
                        loader: function (param, success, error) {
                          success(data.shipmentDetails);
                        },
                        onLoadSuccess:function(){
                          $('#dg-right').datagrid('fixDetailRowHeight',index);
                        }
                      });
                      break;
                  }
                }
                $('#dg-right').datagrid('fixDetailRowHeight', index);
              },
              error: function (data) {
                $.modal.closeLoading();
              }
            });
          }
        });
      }
    },
    error: function (data) {
      $.modal.closeLoading();
    }
  });
}

// FORMATTER
function formatStatus(value, row) {
  switch (value) {
    case 0:
      if (row.robotUuid) {
        return '<span class="badge badge-danger">Bị lỗi</span>';
      }
      return '<span class="badge badge-warning">Đang chờ</span>';
    case 1:
      return '<span class="badge badge-primary">Đang làm</span>';
    case 2:
      return '<span class="badge badge-success">Đã hoàn thành</span>';
  }
  return '-';
}

function formatTaxcode(value, row) {
  if (currentShipment == null) return false;
  return currentShipment.taxCode + ': ' + currentShipment.groupName;
}

function formatPaymentStatus(value, row) {
  if ('Y' == value) {
    if (!row.invoiceNo) {
      return '<span class="badge badge-success">Yes</span>    <a class="btn btn-default btn-xs" disabled><i class="fa fa-view"></i>Thông tin</a>';
    }
    return '<span class="badge badge-success">Yes</span>    <a class="btn btn-default btn-xs" onclick="openPaymentSupport(\'' + row.id + '\')"><i class="fa fa-view"></i>Thông tin</a>'; 
  }
  if (!row.invoiceNo) {
    return '<span class="badge badge-success">Yes</span>    <a class="btn btn-default btn-xs" disabled><i class="fa fa-view"></i>Thanh toán</a>';
  }
  return '<span class="badge badge-danger">No</span>    <a class="btn btn-default btn-xs" onclick="openPaymentSupport(\'' + row.id + '\')"><i class="fa fa-view"></i>Thanh toán</a>';
}

function formatDoStatus(value, row) {
  if ('Y' == value || currentShipment.serviceType != 1) {
    return '<span class="badge badge-success">Yes</span>';
  }
  return '<span class="badge badge-danger">No</span>    <a class="btn btn-danger btn-xs" onclick="checkDoStatus(\'' + row.id + '\')"><i class="fa fa-check-circle"></i> Thu Chứng Từ</a>';
}

function formatVessel(value, row) {
  return row.vslNm + " - " + row.vslName + " - " + row.voyNo;
}

function formatIsSendEmpty(value, row) {
  if (row.emptyDepot == 'Cảng Tiên Sa') {
    return '<input type="checkbox" value="" checked>';
  }
  return '<input type="checkbox" value="">';
}

function formatInfo(value, row) {
  return '<a class="btn btn-success btn-xs" onclick="openDetailInfo(\'' + row.id + '\')"><i class="fa fa-check-circle"></i>Chi tiết</a>';
}

function formatExpiredDem(value) {
  if (value) {
    return value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
  }
  return '-';
}

function formatVesselEmpty() {
  return 'EMPTY';
}

function formatCustomStatus(value, row) {
  switch (value) {
    case 'R':
      return '<span class="badge badge-success">Yes</span>'; 
    case 'Y':
      return '<span class="badge badge-danger">No</span>    <a class="btn btn-primary btn-xs" onclick="checkCustomStatus(\'' + row.id + '\')"><i class="fa fa-check-circle"></i>Đồng bộ</a>';
    case 'N':
      return '<span class="badge badge-danger">No</span>    <a class="btn btn-primary btn-xs" onclick="checkCustomStatus(\'' + row.id + '\')"><i class="fa fa-check-circle"></i>Đồng bộ</a>';
    default:
      return '-';
  }
}

function openPaymentSupport(processOrderId) {
  $.modal.openCustomForm("Thanh toán", PREFIX + "/processOrderId/" + processOrderId + "/payment", 800, 400);
}

function checkDoStatus(processOrderId) {
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
          loadRightTable();
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

function checkCustomStatusByProcessOrderId(processOrderId) {
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
          loadRightTable();
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

function checkCustomStatus(shipmentDetailId) {
  layer.confirm("Xác nhận đồng bộ dữ liệu thông quan với <br>catos.", {
    icon: 3,
    title: "Xác Nhận",
    btn: ['Đồng Ý', 'Hủy Bỏ']
  }, function () {
    layer.close(layer.index);
    $.modal.loading("Đang xử lý...");
    $.ajax({
      url: PREFIX + "/customStatus/shipmentDetail/" + shipmentDetailId,
      method: "POST",
      success: function (data) {
        $.modal.closeLoading();
        if (data.code == 0) {
          $.modal.alertSuccess("Đồng bộ dữ liệu thành công.");
          loadRightTable();
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

function openDetailInfo(shipmentDetailId) {
  $.modal.alertWarning("Không tìm thấy thông tin chi tiết.");
}