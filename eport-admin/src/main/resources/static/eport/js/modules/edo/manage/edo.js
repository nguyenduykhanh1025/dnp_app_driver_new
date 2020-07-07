const PREFIX = "/edo/manage";

function loadTable(containerNumber, billOfLading, fromDate, toDate) {
  $("#dg").datagrid({
    url: PREFIX + "/getEdo",
    method: "GET",
    singleSelect: true,
    clientPaging: true,
    pagination: true,
    pageSize: 20,
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
loadTable();

function searchDo() {
  setTimeout($.modal.loading("Đang xử lý"), 100);
  $.modal.closeLoading();
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
  actions.push('<a class="btn btn-success btn-xs" onclick="viewHistoryEdiFile(\'' + row.containerNumber + '\')"><i class="fa fa-view"></i>History</a> ');
  return actions.join("");
}

function viewHistoryEdiFile(containerNumber) {
  $.modal.open("History File", prefix + "/history/" + containerNumber, 800, 500);
}
