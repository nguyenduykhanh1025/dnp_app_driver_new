const PREFIX = ctx + "carrier/do";
var bill;
var edo = new Object();
$(function () {
  $("#updateEdo").attr("disabled", true);
  var onCheck = 0;
  $("#btn-collapse").click(function () {
    handleCollapse(true);
  });
  $("#btn-uncollapse").click(function () {
    handleCollapse(false);
  });
  loadTable();
  loadTableByContainer();

  $('#searchAll').keyup(function (event) {
    if (event.keyCode == 13) {
      edo.containerNumber = $('#searchAll').val().toUpperCase();
      edo.consignee = $('#searchAll').val().toUpperCase();
      loadTableByContainer(bill);
    }

  });
  $('#searchBillNo').keyup(function (event) {
    if (event.keyCode == 13) {
      billOfLading = $('#searchBillNo').val().toUpperCase();
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
    clientPaging: true,
    height: $(document).height() - 60,
    pagination: true,
    pageSize: 20,
    onClickRow: function () {
      getSelectedRow();
    },
    nowrap: false,
    striped: true,
    rownumbers: true,
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
    error: function () {
      error.apply(this, arguments);
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
  if(date == null || date == undefined)
  {
      return;
  }
  return date.split("/").reverse().join("/");
}

function formatToYDMHMS(date) {
  if(date == null || date == undefined)
  {
      return;
  }
  let temp = date.substring(0, 10);
  return temp.split("-").reverse().join("/") + date.substring(10, 19);
}

function formatAction(value, row, index) {
  let actions = [];
  let disabled = "";
  if (row.status == '3') {
    disabled = "disabled";
  }
  actions.push('<button ' + disabled + ' class="btn btn-success btn-xs btn-action mt5 mb5" id="updateEdo" onclick="viewUpdateCont(\'' + row.id + '\')"><i class="fa fa-pencil-square-o"></i> Cập Nhật</button>' + '<br>');
  actions.push('<a class="btn btn-info btn-xs btn-xs btn-action mt5 mb5" onclick="viewHistoryCont(\'' + row.id + '\')"><i class="fa fa-history"></i> Lịch Sử</a> ');
  return actions.join("");
}

function viewHistoryCont(id) {
  $.modal.openWithOneButton('Lịch sử thay đổi thông tin', PREFIX + "/history/" + id, 1000, 400);
}

function viewUpdateCont(id) {
  $.modal.openOption("Cập nhật container", PREFIX + "/update/" + id, 500, 380);
}

function loadTableByContainer(billOfLading) {
  edo.billOfLading = billOfLading
  $("#dgContainer").datagrid({
    url: PREFIX + "/equipmentDo",
    method: "POST",
    singleSelect: false,
    clientPaging: true,
    height: $(document).height() - 60,
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
        accept: 'text/plain',
        dataType: 'text',
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: edo
        }),
        success: function (data) {
          success(JSON.parse(data));
          edo = new Object();
        },

        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

function getSelectedRow() {
  onCheck = 0;
  $("#updateEdo").attr("disabled", true);
  var row = $("#dg").datagrid("getSelected");
  if (row) {
    bill = row.billOfLading;
    edo = new Object();
    loadTableByContainer(row.billOfLading);
  }
}

function stringToDate(dateStr) {
  if(dateStr == null || dateStr == undefined)
  {
      return;
  }
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

$('#searchAll').keyup(function (event) {
  if (event.keyCode == 13) {
    edo.containerNumber = $('#searchAll').val().toUpperCase();
    edo.consignee = $('#searchAll').val().toUpperCase();
    edo.vessel = $('#searchAll').val().toUpperCase();
    edo.voyNo = $('#searchAll').val().toUpperCase();
    loadTableByContainer(bill);
  }
});

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



function multiUpdateEdo() {
  let ids = [];
  let rows = $('#dgContainer').datagrid('getSelections');
  if (rows.length === 0) {
    $.modal.alertWarning("Quý khách chưa chọn container để update, vui lòng kiểm tra lại !");
    return;
  }
  for (let i = 0; i < rows.length; i++) {
    let row = rows[i];
    if (row.status == '3') {
      $.modal.alertError("Quý khách đã chọn container đã GATE-IN ra khỏi cảng, vui lòng kiểm tra lại dữ liệu!");
      return;
    }
    ids.push(row.id);
  }
  $.modal.openOption("Cập nhật container", PREFIX + "/multiUpdate/" + ids, 500, 320);
}



// SEARCH INFO VESSEL AREA

$('.c-search-box-vessel').on("select2:opening", function(e) {
  $('.c-search-box-vessel').text(null);
  $('.c-search-box-voy-no').text(null);
  edo = new Object();
  loadTable(edo);
});
$(".c-search-box-vessel").select2({
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
        vessel: edo.vessel,
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
$(".c-search-box-vessel").change(function () {
  edo = new Object();
  edo.vessel = $(this).text().trim();
  loadTable(edo);
});

$(".c-search-box-voy-no").change(function () {
  edo.voyNo = $(this).text().trim();
  loadTable(edo);
});

$(".pagination-info").hide();

// add full size do
function addDo(id) {
  $.modal.openTab("Phát Hành DO", ctx + "carrier/do/add");
}
$('#btnRefresh').click(function(){
  $(".c-search-box-vessel").text(null);
  $(".c-search-box-voy-no").text(null);
  $("#fromDate").val(null);
  $("#toDate").val(null)
  $("#searchBillNo").val(null);
  $("#searchContNo").val(null);
  edo.vessel = null;
  loadTable();
});

$('#dgContainer').datagrid({
  onCheck: function(){
    onCheck += 1;
    $("#updateEdo").attr("disabled", false);
  },
  onCheckAll: function(index){
    onCheck = index.length;
    $("#updateEdo").attr("disabled", false);
  },
  onUncheck: function(){
    onCheck = onCheck - 1;
    if(onCheck == 0)
    {
      $("#updateEdo").attr("disabled", true);
    }
  },
  onUncheckAll: function(){
    onCheck = 0;
    $("#updateEdo").attr("disabled", true);
  },
})