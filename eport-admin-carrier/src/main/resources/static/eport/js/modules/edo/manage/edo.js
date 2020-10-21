const PREFIX = ctx + "edo/manage";
var bill;
var edo = new Object();
var billOfLadingFresh;
var fromDate, toDate;

$(document).ready(function () {
  loadTable();
  loadTableByContainer();
  $("#searchBillNo").textbox('textbox').bind('keydown', function (e) {
    // enter key
    if (e.keyCode == 13) {
      edo.billOfLading = $("#searchBillNo").textbox('getText').toUpperCase();
      loadTable(edo);
    }
  });

  $("#searchContNo").textbox('textbox').bind('keydown', function (e) {
    // enter key
    if (e.keyCode == 13) {
      edo.containerNumber = $("#searchContNo").textbox('getText').toUpperCase();
      loadTable(edo);
    }
  });
  $('#fromDate').datebox({
    onSelect: function (date) {
      date.setHours(0, 0, 0);
      fromDate = date;
      if (toDate != null && date.getTime() > toDate.getTime()) {
        $.modal.alertWarning("Từ ngày không được lớn hơn đến ngày.");
      } else {
        fromDate.setHours(0, 0, 0);
        edo.fromDate = fromDate.getTime();
        edo.billOfLading = $("#searchBillNo").textbox('getText').toUpperCase();
        edo.containerNumber = $("#searchContNo").textbox('getText').toUpperCase();
        loadTable(edo);
      }
      return date;
    }
  });

  $('#toDate').datebox({
    onSelect: function (date) {
      date.setHours(23, 59, 59);
      toDate = date;
      if (fromDate != null && date.getTime() < fromDate.getTime()) {
        $.modal.alertWarning("Đến ngày không được thấp hơn từ ngày.");
      } else {
        toDate.setHours(23, 59, 59);
        edo.toDate = toDate.getTime();
        edo.billOfLading = $("#searchBillNo").textbox('getText').toUpperCase();
        edo.containerNumber = $("#searchContNo").textbox('getText').toUpperCase();
        loadTable(edo);
      }
    }
  });
});

$("#opr").combobox({
  valueField: 'carrierCode',
  textField: 'carrierCode',
  url: PREFIX + "/getOprCode",
  onSelect: function (carrierCode) {
    $("#voyNo").combobox('setText', '');
    edo = new Object();
    edo.carrierCode = carrierCode.carrierCode;
    loadTable(edo);
    $("#vessel").combobox({
      valueField: 'vessel',
      textField: 'vessel',
      url: PREFIX + '/getVessel/' + carrierCode.carrierCode,
      onSelect: function (vessel) {
        $("#fromDate").datebox('setValue', '');
        $("#toDate").datebox('setValue', '');
        $("#searchBillNo").textbox('setText', '');
        $("#searchContNo").textbox('setText', '');
        edo.billOfLading = null;
        edo.vessel = vessel.vessel
        edo.voyNo = null;
        loadTable(edo);
        var url = PREFIX + '/getVoyNo/' + carrierCode.carrierCode + '/' + vessel.vessel;
        $('#voyNo').combobox({
          valueField: 'voyNo',
          textField: 'voyNo',
          url: url,
          onSelect: function (voyNo) {
            edo.billOfLading = null;
            edo.voyNo = voyNo.voyNo
            loadTable(edo);
          }
        });
      }
    });

  }
});



function loadTable(edo) {
  $("#dg").datagrid({
    url: PREFIX + "/billNo",
    method: "POST",
    singleSelect: true,
    height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
    clientPaging: true,
    collapsible: true,
    pagination: true,
    pageSize: 20,
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
          data: edo,
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

function loadTableByContainer() {
  $("#container-grid").datagrid({
    url: PREFIX + "/edo",
    method: "POST",
    multipleSelect: true,
    height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
    clientPaging: true,
    pagination: true,
    pageSize: 20,
    nowrap: false,
    striped: true,
    rownumbers: true,
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
      if (!opts.url) return false;
      if (edo.billOfLading == null) {
        edo.billOfLading = billOfLadingFresh;
      }
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
          data: edo
        }),
        success: function (data) {
          if (data == null || data == '' || data == undefined) {
            error(data);
            return;
          }
          success(JSON.parse(data));
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

function searchDo() {
  edo.billOfLading = $('#searchBillNo').val().toUpperCase();
  edo.containerNumber = $('#searchContNo').val().toUpperCase();
  edo.fromDate = stringToDate($("#fromDate").val()).getTime();
  let toDate = stringToDate($("#toDate").val());
  if ($("#fromDate").val() != "" && stringToDate($("#fromDate").val()).getTime() > toDate.getTime()) {
    $.modal.alertError("Quý khách không thể chọn đến ngày thấp hơn từ ngày.");
    $("#toDate").val("");
  } else {
    toDate.setHours(23, 59, 59);
    edo.toDate = toDate.getTime();
    loadTable(edo);
  };
  loadTable(edo);
}

function formatToYDM(date) {
  if (date == null || date == undefined) {
    return "-";
  }
  return date.split("-").reverse().join("-");
}

function formatAction(value, row, index) {
  var actions = [];
  actions.push('<a class="btn btn btn-info btn-xs" onclick="viewHistoryCont(\'' + row.id + '\')"><i class="fa fa-history"></i> Lịch sử</a> ');
  return actions.join("");
}

function viewHistoryCont(id) {
  $.modal.openWithOneButton('Lịch sử cập nhật', PREFIX + "/history/" + id, 1000, 400);
}

function getSelectedRow() {
  var row = $("#dg").datagrid("getSelected");
  if (row) {
    edo = new Object();
    edo.billOfLading = row.billOfLading;
    billOfLadingFresh = row.billOfLading;
    loadTableByContainer();
  }
}

function stringToDate(dateStr) {
  let dateParts = dateStr.split("/");
  return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}

function searchInfoEdo() {
  edo.fromDate = stringToDate($("#fromDate").val()).getTime();
  let toDate = stringToDate($("#toDate").val());
  if ($("#fromDate").val() != "" && stringToDate($("#fromDate").val()).getTime() > toDate.getTime()) {
    $.modal.alertError("Quý khách không thể chọn đến ngày thấp hơn từ ngày.");
    $("#toDate").val("");
  } else {
    toDate.setHours(23, 59, 59);
    edo.toDate = toDate.getTime();
    loadTableByContainer();
  };
  edo.containerNumber = $('#searchAll').val().toUpperCase();
  edo.consignee = $('#searchAll').val().toUpperCase();
  edo.vessel = $('#searchAll').val().toUpperCase();
  edo.voyNo = $('#searchAll').val().toUpperCase();
  loadTableByContainer();
}


function formatToYDMHMS(date) {
  if (date == null || date == undefined) {
    return "-";
  }
  let temp = date.substring(0, 10);
  return temp.split("-").reverse().join("/") + date.substring(10, 19);
}

function formatStatus(value, row) {
  if (row && row.releaseStatus == 'Y') {
    return "<span class='label label-success'>Đã làm lệnh</span>";
  } else {
    if(value == null || value == '' || value == undefined) {
      return "<span class='label label-success'>Chưa làm lệnh</span>";
    }
    switch (value) {
      case '1':
        return "<span class='label label-success'>Đã khai báo</span>";
      case '2':
        return "<span class='label label-success'>Đã khai báo</span>";
      case '3':
        return "<span class='label label-success'>Đã khai báo</span>";
      case '5':
      return "<span class='label label-success'>Đã Gate - Out</span>";
    }
  }
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

laydate.render({
  elem: '#toDate',
  format: 'dd/MM/yyyy'
});
laydate.render({
  elem: '#fromDate',
  format: 'dd/MM/yyyy'
});

function generatePDF() {
  if (!edo.billOfLading) {
    $.modal.alertError("Bạn chưa chọn Lô!");
    return;
  }
  $.modal.openTab("In phiếu", ctx + "edo/print/bill/" + edo.billOfLading);
}

function dateformatter(date) {
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  var d = date.getDate();
  return (d < 10 ? ('0' + d) : d) + '/' + (m < 10 ? ('0' + m) : m) + '/' + y;
}

function dateparser(s) {
  var ss = (s.split('\.'));
  var d = parseInt(ss[0], 10);
  var m = parseInt(ss[1], 10);
  var y = parseInt(ss[2], 10);
  if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
    return new Date(y, m - 1, d);
  }
}

function clearInput() {
  edo = new Object();
  $("#fromDate").datebox('setValue', '');
  $("#toDate").datebox('setValue', '');
  $("#searchBillNo").textbox('setText', '');
  $("#searchContNo").textbox('setText', '');
  $("#opr").combobox('setText', '');
  $("#vessel").combobox('setText', '');
  $("#voyNo").combobox('setText', '');
  loadTable(edo);
}

function lockEdo() {
  // Get selected rows in datagrid right
  let rows = $('#container-grid').datagrid('getSelections');
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
        url: PREFIX + "/order/lock",
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
          loadTableByContainer();
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
  let rows = $('#container-grid').datagrid('getSelections');
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
        url: PREFIX + "/order/unlock",
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
          loadTableByContainer();
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