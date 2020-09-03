const PREFIX = ctx + "do/manage";
var bill;
var edo = new Object();
var currentLeftWidth = $(".table-left").width();
var currentRightWidth = $(".table-right").width();

$(document).ready(function () {
  $("#btn-collapse").click(function () {
    handleCollapse(true);
  });
  $("#btn-uncollapse").click(function () {
    handleCollapse(false);
  });
  loadTable();
  loadTableByContainer();
  $("#searchAll").keyup(function (event) {
    if (event.keyCode == 13) {
      edo.containerNumber = $("#searchAll").val().toUpperCase();
      edo.consignee = $("#searchAll").val().toUpperCase();
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
  $("#searchContNo").keyup(function (event) {
    if (event.keyCode == 13) {
      containerNumber = $("#searchContNo").val().toUpperCase();
      if (containerNumber == "") {
        loadTable(edo);
      }
      edo = new Object();
      edo.containerNumber = containerNumber;
      loadTable(edo);
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
    loadTableByContainer();
    return;
  }
  $(".left").css("width", "25%");
  $(".left").children().show();
  $("#btn-collapse").show();
  $("#btn-uncollapse").hide();
  $(".right").css("width", "74%");
  loadTableByContainer();
  return;
}

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
  $("#dgContainer").datagrid({
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
  edo.vessel = $('.c-search-box-vessel').text().trim();
  edo.voyNo = $(".c-search-box-voy-no").text().trim();
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






$('.c-search-box-vessel').on("select2:opening", function(e) {
  $(".c-search-box-voy-no").text(null);
  $(this).text(null);
  edo.vessel = null;
  edo.voyNo = null;
  console.log("edo", edo)
});
$('.c-search-box-vessel').select2({
  theme: "bootstrap",
  placeholder: "Vessel",
  allowClear: true,
  ajax: {
    url: PREFIX + "/getVessel",
    dataType: "json",
    method: "GET",
    data: function (params) {
      return {
        keyString: params.term,
        oprCode : edo.carrierCode,
      };
    },
    processResults: function (data) {
      let results = []
      data.forEach(function (element, i) {
        let obj = {};
        obj.id = i;
        obj.text = element;
        results.push(obj);
      })
      return {
        results: results,
      };
    },
  },
});

$('.c-search-box-voy-no').on("select2:opening", function(e) {
  edo = new Object();
  $(this).text(null);
  edo.vessel = $('.c-search-box-vessel').text().trim();
  edo.carrierCode = $(".c-search-opr-code").text().trim();
  loadTable(edo);
});

$(".c-search-box-voy-no").select2({
  theme: "bootstrap",
  placeholder: "Voy No",
  allowClear: true,
  ajax: {
    url: PREFIX + "/getVoyNo",
    dataType: "json",
    method: "GET",
    data: function (params) {
      return {
        keyString: params.term,
        vessel : edo.vessel,
        oprCode : edo.carrierCode
      };
    },
    processResults: function (data) {
      let results = []
      data.forEach(function (element, i) {
        let obj = {};
        obj.id = i;
        obj.text = element;
        results.push(obj);

      })
      return {
        results: results,
      };
    },
  },
});

$(".c-search-opr-code").on('select2:open', function (e) {
  $('.c-search-box-vessel').text(null);
  $('.c-search-box-voy-no').text(null);
  $(this).text(null);
  edo = new Object();
  loadTable(edo);
});

$(".c-search-opr-code").select2({
  theme: "bootstrap",
  placeholder: "Mã vận hành",
  allowClear: true,
  ajax: {
    url: PREFIX + "/getOprCode",
    dataType: "json",
    method: "GET",
    data: function (params) {
      return {
        keyString: params.term,
      };
    },
    processResults: function (data) {
      let results = []
      data.forEach(function (element, i) {
        let obj = {};
        obj.id = i;
        obj.text = element;
        results.push(obj);

      })
      return {
        results: results,
      };
    },
  },
});


// For submit search
$('.c-search-box-vessel').change(function () {
  edo = new Object();
  edo.carrierCode = $(".c-search-opr-code").text().trim();
  edo.vessel = $(this).text().trim();
  loadTable(edo);
});

$(".c-search-box-voy-no").change(function () {
  edo.voyNo = $(this).text().trim();
  loadTable(edo);
});

$(".c-search-opr-code").change(function () {
  edo.carrierCode = $(this).text().trim();
  loadTable(edo);
});



function generatePDF() {
  if (!bill) {
    $.modal.alertError("Bạn chưa chọn Lô!");
    return
  }
  $.modal.openTab("In phiếu", ctx + "edo/print/bill/" + bill);
}

