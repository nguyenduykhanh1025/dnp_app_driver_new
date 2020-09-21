const PREFIX = ctx + "do/manage";
var bill;
var edo = new Object();

$(document).ready(function () {

  loadTable();
  loadTableByContainer();

  $('#searchAll').keyup(function (event) {
    if (event.keyCode == 13) {
      edo.containerNumber = $('#searchAll').val().toUpperCase();
      edo.consignee = $('#searchAll').val().toUpperCase();
      loadTableByContainer(bill);
    }

  });

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
        fromDate.setHours(23, 59, 59);
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
        edo.vessel = vessel.vessel
        edo.voyNo = null;
        loadTable(edo);
        var url = PREFIX + '/getVoyNo/' + carrierCode.carrierCode + '/' + vessel.vessel;
        $('#voyNo').combobox({
          valueField: 'voyNo',
          textField: 'voyNo',
          url: url,
          onSelect: function (voyNo) {
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
    height: $(document).height() - 60,
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

function loadTableByContainer(billOfLading) {
  edo.billOfLading = billOfLading;
  $("#container-grid").datagrid({
    url: PREFIX + "/edo",
    method: "POST",
    singleSelect: true,
    height: $(document).height() - 60,
    clientPaging: true,
    pagination: true,
    pageSize: 20,
    nowrap: false,
    striped: true,
    rownumbers: true,
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
      if (billOfLading == null) {
        return false;
      }
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
        success: function (data) {
          success(JSON.parse(data));
          edo.billOfLading = null;
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
  $.modal.openWithOneButton('Lịch sử thay đổi thông tin', PREFIX + "/history/" + id, 1000, 400);
}

function getSelectedRow() {
  var row = $("#dg").datagrid("getSelected");
  if (row) {
    bill = row.billOfLading;
    loadTableByContainer(row.billOfLading);
  }
}

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

function searchInfoEdo() {
  edo.fromDate = stringToDate($("#fromDate").val()).getTime();
  let toDate = stringToDate($("#toDate").val());
  if ($("#fromDate").val() != "" && stringToDate($("#fromDate").val()).getTime() > toDate.getTime()) {
    $.modal.alertError("Quý khách không thể chọn đến ngày thấp hơn từ ngày.");
    $("#toDate").val("");
  } else {
    toDate.setHours(23, 59, 59);
    edo.toDate = toDate.getTime();
    loadTableByContainer(bill);
  };
  edo.containerNumber = $('#searchAll').val().toUpperCase();
  edo.consignee = $('#searchAll').val().toUpperCase();
  edo.vessel = $('#searchAll').val().toUpperCase();
  edo.voyNo = $('#searchAll').val().toUpperCase();
  loadTableByContainer(bill);
}


function formatToYDMHMS(date) {
  if (date == null || date == undefined) {
    return "-";
  }
  let temp = date.substring(0, 10);
  return temp.split("-").reverse().join("/") + date.substring(10, 19);
}

function formatStatus(value) {
  switch (value) {
    case '1':
      return "<span class='label label-success'>Chưa làm lệnh</span>";
    case '2':
      return "<span class='label label-success'>Đã làm lệnh</span>";
    case '3':
      return "<span class='label label-success'>Gate-in</span>";
  }
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
  $.modal.alertWarning("Chức năng này đang được bảo trì !");
  return;
  // if (!bill) {
  //   $.modal.alertError("Bạn chưa chọn Lô!");
  //   return
  // }
  // $.modal.openTab("In phiếu", ctx + "edo/print/bill/" + bill);
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