const PREFIX = ctx + "gate/check";
const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var shipmentDetailIds = [];
var jobOrderNo;
$(document).ready(function () {

  $(".main-body").layout();

  $(".collapse").click(function () {
    $(".main-body__search-wrapper").height(15);
    $(".main-body__search-wrapper--container").hide();
    $(this).hide();
    $(".uncollapse").show();
  });

  $(".uncollapse").click(function () {
    $(".main-body__search-wrapper").height(SEARCH_HEIGHT + 20);
    $(".main-body__search-wrapper--container").show();
    $(this).hide();
    $(".collapse").show();
  });

  $(".left-side__collapse").click(function () {
    $('#main-layout').layout('collapse', 'west');
  });

  $("#jobOrderNo").textbox('textbox').bind('keydown', function (e) {
    // enter key
    if (e.keyCode == 13) {
      loadTable();
    }
  });
  loadTable();
});

function loadTable() {
  jobOrderNo = $('#jobOrderNo').textbox('getText');
  shipmentDetailIds = [];
  if (jobOrderNo) {
    $.modal.loading("Đang xử lý...");
    $.ajax({
      url: PREFIX + "/job/info",
      method: "GET",
      data: {
        jobOrderNo: jobOrderNo
      },
      success: function (res) {
        $.modal.closeLoading();
        if (res.code == 0) {
          loadTableLeft(res.shipmentDetails);
          loadTableRight(res.driverInfos);
        } else {
          loadTableLeft([]);
          loadTableRight(null);
        }
      },
      error: function (err) {
        $.modal.closeLoading();
        loadTableLeft([]);
        loadTableRight(null);
      }
    });
  } else {
    loadTableLeft([]);
    loadTableRight(null);
  }
}

function loadTableLeft(shipmentDetails) {
  $("#dg-left").datagrid({
    height: $(document).height() - $(".main-body__search-wrapper").height() - 58,
    multipleSelect: true,
    collapsible: true,
    clientPaging: false,
    rownumbers: true,
    nowrap: true,
    striped: true,
    onSelect: function (index, row) {
      selectRowLeftTable(index, row);
    },
    onUnselect: function (index, row) {
      unSelectRowLeftTable(index, row);
    },
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      success(shipmentDetails);
      loadTableRight();
    }
  });
}

function loadTableRight(shipmentId) {
  if (shipmentId == null) {
    $("#dg-right").datagrid({
      height: $(document).height() - $(".main-body__search-wrapper").height() - 58,
      singleSelect: true,
      collapsible: true,
      clientPaging: false,
      rownumbers: true,
      nowrap: true,
      striped: true,
      loadMsg: " Đang xử lý...",
      loader: function (param, success, error) {
        success([]);
      }
    });
  } else {
    $("#dg-right").datagrid({
      height: $(document).height() - $(".main-body__search-wrapper").height() - 58,
      singleSelect: true,
      collapsible: true,
      clientPaging: false,
      rownumbers: true,
      nowrap: true,
      striped: true,
      loadMsg: " Đang xử lý...",
      loader: function (param, success, error) {
        $.ajax({
          url: PREFIX + "/pickup-assign/list",
          method: "POST",
          data: {
            shipmentDetailIds: shipmentDetailIds.join(),
            shipmentId: shipmentId,
            jobOrderNo: jobOrderNo
          },
          success: function (res) {
            if (res.code == 0) {
              success(res.pickupAssigns);
            } else {
              success([]);
            }
          },
          error: function (err) {
            success([]);
          }
        });
      }
    });
  }
}

// FORMATTER
// FORMAT DATE FOR date time format dd/mm/yyyy
function formatDate(value) {
  let date = new Date(value);
  let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  let month = date.getMonth() + 1;
  let monthText = month < 10 ? "0" + month : month;
  return day + "/" + monthText + "/" + date.getFullYear();
}

// FORMAT BOOKING OR BL NO
function formatBlBooking(value, row) {
  if (row.bookingNo) {
    return row.bookingNo;
  } else if (row.blNo) {
    return row.blNo;
  }
  return '';
}

function formatVslVoy(value, row) {
  if (row) {
    return row.vslNm + ' - ' + row.voyNo;
  }
  return '';
}

function selectRowLeftTable(index, row) {
  shipmentDetailIds.push(row.id);
  loadTableRight(row.shipmentId);
}

function unSelectRowLeftTable(index, row) {
  for (let i = 0; i < shipmentDetailIds.length; i++) {
    if (row.id == shipmentDetailIds[i]) {
      shipmentDetailIds.splice(i, 1);
    }
  }
  loadTableRight(row.shipmentId);
}