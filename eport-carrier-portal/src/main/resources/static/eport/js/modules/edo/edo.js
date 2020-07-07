const PREFIX = "/edo";
$(function () {
  $("#dg").height($(document).height() - 100);
  $("#dgContainer").height($(document).height() - 100);
  currentLeftTableWidth = $(".left-table").width();
  currentRightTableWidth = $(".right-table").width();
  $.ajax({
    type: "GET",
    url: PREFIX + "/carrierCode",
    success(data) {
      data.forEach((element) => {
        $("#carrierCode").append(`<option value="${element}"> ${element}</option>`);
      });
    },
  });
  loadTable();
  loadTableByContainer();
});

function loadTable(containerNumber, billOfLading, fromDate, toDate) {
  $("#dg").datagrid({
    url: PREFIX + "/billNo",
    method: "GET",
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
        data: {
          containerNumber: containerNumber,
          billOfLading: billOfLading,
          fromDate: fromDate,
          toDate: toDate,
        },
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

function formatAction(value, row, index) {
  var actions = [];
  actions.push('<a class="btn btn-success btn-xs btn-action mt5" onclick="viewUpdateCont(\'' + row.id + '\')"><i class="fa fa-view"></i>Cập nhật</a> ');
  actions.push('<a class="btn btn-success btn-xs btn-action mt5 mb5" onclick="viewHistoryCont(\'' + row.id + '\')"><i class="fa fa-view"></i>Xem lịch sử</a> ');
  return actions.join("");
}

function viewHistoryCont(id) {
  $.modal.open("History Container", PREFIX + "/history/" + id, 800, 500);
}

function viewUpdateCont(id) {
  $.modal.openOption("Update Container", PREFIX + "/update/" + id, 800, 500);
}

function loadTableByContainer(billOfLading) {
  $("#dgContainer").datagrid({
    url: PREFIX + "/edo",
    method: "GET",
    singleSelect: true,
    clientPaging: true,
    pagination: true,
    pageSize: 20,
    nowrap: false,
    striped: true,
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
      if (billOfLading == null) {
        return false;
      }
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        url: opts.url,
        data: {
          billOfLading: billOfLading,
        },
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

function getSelectedRow() {
  var row = $("#dg").datagrid("getSelected");
  if (row) {
    console.log(row.billOfLading);
    loadTableByContainer(row.billOfLading);
  }
}

$(".btn-collapse").click(function () {
  if ($(".left-table").width() == 0) {
    $(".left-table").width("0%");
    $(".right-table").width("78%");
    $(".left-table").css("border-color", "#a9a9a9");
    $(this).css({ transform: "rotate(360deg)" });
    return;
  }
  $(".left-table").width(0);
  $(".right-table").width("98%");
  $(".left-table").css("border-color", "transparent");
  $(this).css({ transform: "rotate(180deg)" });
});
