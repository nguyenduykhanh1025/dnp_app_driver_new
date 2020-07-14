const PREFIX = ctx + "edo/manage";
var bill;
var edo = new Object();
var currentLeftWidth = $(".table-left").width();
var currentRightWidth = $(".table-right").width();

$(document).ready(function () {
  $("#dg").height($(document).height() - 100);
  $("#dgContainer").height($(document).height() - 100);
  loadTable();
  loadTableByContainer();
  $("#searchAll").keyup(function (event) {
    if (event.keyCode == 13) {
      edo.containerNumber = $("#searchAll").val().toUpperCase();
      edo.consignee = $("#searchAll").val().toUpperCase();
      edo.vessel = $("#searchAll").val().toUpperCase();
      edo.voyNo = $("#searchAll").val().toUpperCase();
      loadTableByContainer(bill);
    }
  });
  $("#searchBillNo").keyup(function (event) {
    if (event.keyCode == 13) {
      billOfLading = $("#searchBillNo").val().toUpperCase();
      if (billOfLading == "") {
        loadTable(edo);
      }
      edo = new Object();
      edo.billOfLading = billOfLading;
      loadTable(edo);
    }
  });
  $(".btn-collapse").click(function (event) {
    let leftTable = $(".table-left");
    let rightTable = $(".table-right");
    let leftWidth = leftTable.width();
    if (leftWidth !== 0) {
      leftTable.width(0);
      rightTable.width(currentRightWidth + currentLeftWidth);
      loadTableByContainer();
      leftTable.css("border-color","transparent");
      $(this).css({'transform' : 'rotate('+ 180 +'deg)'});
      return;
    }
    leftTable.width(currentLeftWidth);
    rightTable.width(currentRightWidth);
    leftTable.css("border-color","darkgrey");
    $(this).css({'transform' : 'rotate('+ 360 +'deg)'});
    return;
  });

  $.ajax({
    type: "GET",
    url: PREFIX + "/getCarrierCode",
    success(data) {
      data.forEach(element => {
        $('#carrierCode').append(`<option value="${element}"> 
                                                  ${element} 
                                                </option>`);
      });

    }
  })
});

function loadTable(edo) {
  $("#dg").datagrid({
    url: PREFIX + "/billNo",
    method: "POST",
    singleSelect: true,
    clientPaging: true,
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
  $("#dgContainer").datagrid({
    url: PREFIX + "/edo",
    method: "POST",
    singleSelect: true,
    clientPaging: true,
    pagination: true,
    pageSize: 20,
    nowrap: false,
    striped: true,
    rownumbers:true,
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
          let dataTotal = JSON.parse(data);
          $("#countContainer").text("Số lượng container : " + dataTotal.total);
          $("#showBillNo").text("Bill No : " + bill);
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

function searchDo() {
  let containerNumber = $("#containerNumber").val() == null ? "" : $("#containerNumber").val();
  let billOfLading = $("#billOfLading").val() == null ? "" : $("#billOfLading").val();
  let fromDate = formatToYDM($("#fromDate").val() == null ? "" : $("#fromDate").val());
  let toDate = formatToYDM($("#toDate").val() == null ? "" : $("#toDate").val());
  loadTable(containerNumber, billOfLading, fromDate, toDate);
}

function formatToYDM(date) {
  return date.split("-").reverse().join("-");
}

function formatAction(value, row, index) {
  var actions = [];
  actions.push('<a class="btn btn-success btn-xs" onclick="viewHistoryCont(\'' + row.id + '\')"><i class="fa fa-view"></i>Xem lịch sử</a> ');
  return actions.join("");
}

function viewHistoryCont(id) {
  $.modal.openWithOneButton('Lịch sử thay đổi thông tin',PREFIX + "/history/" + id,1000,400);
}

function getSelectedRow() {
  var row = $("#dg").datagrid("getSelected");
  if (row) {
    bill = row.billOfLading;
    edo = new Object();
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
  let temp = date.substring(0, 10);
  return temp.split("-").reverse().join("/") + date.substring(10, 19);
}

function formatStatus(value) {
  switch (value) {
    case 0:
      return "<span class='label label-success'>Trạng thái 0</span>";
    case 1:
      return "<span class='label label-success'>Trạng thái 1</span>";
    case 2:
      return "<span class='label label-success'>Trạng thái 2</span>";
    case 3:
      return "<span class='label label-success'>Trạng thái 2</span>";
    case 4:
      return "<span class='label label-success'>Trạng thái 2</span>";
    default:
      return "<span class='label label-warning'>Chờ làm lệnh</span>";
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


$("#carrierCode").change(function() {
   let edo = new Object();
   edo.carrierCode = this.value;
   loadTable(edo);
});
