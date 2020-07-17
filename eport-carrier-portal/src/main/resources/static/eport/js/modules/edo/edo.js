const PREFIX = ctx + "edo";
var bill;
var edo = new Object();
$(function () {
  $(".left").css("height", $(document).height());
  $(".right").css("height", $(document).height());
  $("#btn-collapse").click(function () {
    handleCollapse(true);
  });
  $("#btn-uncollapse").click(function () {
    handleCollapse(false);
  });
    $("#dg").height($(document).height() - 60);
    $("#dgContainer").height($(document).height() - 60);
    loadTable();
    loadTableByContainer();

    $('#searchAll').keyup(function (event) {
        if (event.keyCode == 13) {
            edo.containerNumber = $('#searchAll').val().toUpperCase();
            edo.consignee = $('#searchAll').val().toUpperCase();
            edo.vessel = $('#searchAll').val().toUpperCase();
            edo.voyNo = $('#searchAll').val().toUpperCase();
            loadTableByContainer(bill);
        }
   
    });
    $('#searchBillNo').keyup(function (event) {
        if (event.keyCode == 13) {
            billOfLading = $('#searchBillNo').val().toUpperCase();
            if(billOfLading == "")
            {
              loadTable(edo);
            }
            edo = new Object();
            edo.billOfLading = billOfLading;
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
        error: function () {
          error.apply(this, arguments);
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
  return date.split("/").reverse().join("/");
}

function formatToYDMHMS(date) {
    let temp = date.substring(0,10);
    return temp.split("-").reverse().join("/") + date.substring(10,19);
}

function formatAction(value, row, index) {
  var actions = [];
  actions.push('<a class="btn btn-success btn-xs btn-action mt5" onclick="viewUpdateCont(\'' + row.id + '\')"><i class="fa fa-pencil-square-o"></i> Cập nhật</a> '+'<br>');
  actions.push('<a class="btn btn-info btn-xs btn-xs btn-action mt5 mb5" onclick="viewHistoryCont(\'' + row.id + '\')"><i class="fa fa-history"></i> Lịch sử</a> ');
  return actions.join("");
}

function viewHistoryCont(id) {
  $.modal.openWithOneButton('Lịch sử thay đổi thông tin',PREFIX + "/history/" + id,1000,400);
}

function viewUpdateCont(id) {
  $.modal.openOption("Cập nhật container", PREFIX + "/update/" + id, 600, 400);
}

function loadTableByContainer(billOfLading) {
    edo.billOfLading = billOfLading
    $("#dgContainer").datagrid({
        url: PREFIX + "/edo",
        method: "POST",
        singleSelect: false,
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
                    // let dataTotal = JSON.parse(data);
                    // console.log(dataTotal);
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


function formatStatus(value)
{
    switch(value)
    {
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



function multiUpdateEdo()
{
  let ids = [];
  let rows = $('#dgContainer').datagrid('getSelections');
  if(rows.length === 0)
  {
      $.modal.alertWarning("Bạn chưa chọn container để update, vui lòng kiểm tra lại !");
      return;
  }
  for(let i=0; i<rows.length; i++){
    let row = rows[i];
       ids.push(row.id);
  }
  $.modal.openOption("Cập nhật container", PREFIX + "/multiUpdate/" + ids, 600, 400);
}

// $("#vesselNo").change(function() {
//   let edo = new Object();
//   edo.vesselNo = this.value;
//   loadTable(edo);
// });
// $("#voyNo").change(function() {
//   let edo = new Object();
//   edo.voyNo = this.value;
//   loadTable(edo);
// });


// SEARCH INFO VESSEL AREA
$(".c-search-box-vessel").select2({
  placeholder: "Vessel",
  allowClear: true,
  minimumInputLength: 2,
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

$(".c-search-box-vessel-code").select2({
  placeholder: "Vessel Code",
  allowClear: true,
  minimumInputLength: 2,
  ajax: {
    url: PREFIX + "/getVesselCode",
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

$(".c-search-box-voy-no").select2({
  placeholder: "Voy No",
  allowClear: true,
  minimumInputLength: 2,
  ajax: {
    url: PREFIX + "/getVoyNo",
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
$(".c-search-box-vessel").change(function () {
  edo = new Object();
  edo.vessel = $(this).text().trim();
  $(this).text(null);
  loadTable(edo);
});
$(".c-search-box-vessel-code").change(function () {
  edo = new Object();
  edo.vesselNo = $(this).text().trim();
  $(this).text(null);
  loadTable(edo);
});
$(".c-search-box-voy-no").change(function () {
  edo = new Object();
  edo.voyNo = $(this).text().trim();
  $(this).text(null);
  loadTable(edo);
});

