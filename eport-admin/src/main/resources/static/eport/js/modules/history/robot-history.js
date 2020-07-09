const PREFIX = ctx + "history/robot";
var processHistory = new Object();

$(document).ready(function () {
  loadTable();

  $('#fromDate').datetimepicker({
    language: 'en',
    format: 'dd/mm/yyyy',
    autoclose: true,
    todayBtn: true,
    todayHighlight: true,
    pickTime: false,
    minView: 2
  });

  $('#toDate').datetimepicker({
    language: 'en',
    format: 'dd/mm/yyyy',
    autoclose: true,
    todayBtn: true,
    todayHighlight: true,
    pickTime: false,
    minView: 2
  });

  $('#searchAllInput').keyup(function (event) {
    if (event.keyCode == 13) {
      processHistory.robotUuid = $('#searchAllInput').val().toUpperCase();
      loadTable();
    }
  });
});

function formatDate(value) {
  if (value == null) return "";
  var date = new Date(value);
  return formatNumber(date.getDate())
    + "/" + formatNumber(date.getMonth() + 1)
    + "/" + date.getFullYear()
    + " " + formatNumber(date.getHours())
    + ":" + formatNumber(date.getMinutes());
}

function formatNumber(number) {
    return number < 10 ? "0" + number : number;
}

function formatServiceType(value, row) {
  console.log(row);
  switch (row.processOrder.serviceType) {
    case 1:
      return 'Bốc Hàng';
    case 2:
      return 'Hạ Rỗng';
    case 3:
      return 'Bốc Rỗng';
    case 4:
      return 'Hạ Hàng';
  }
}

function formatStatus(value, row) {
  switch(row.status) {
    case 1:
      return 'Bắt đầu';
    case 2:
      return 'Kết thúc';
  }
}

function formatResult(value) {
    return value == "S" ? "<span class='label label-success'>Thành công</span>" : "<span class='label label-danger'>Thất bại</span>";
}

function loadTable() {
  $("#dg").datagrid({
    url: PREFIX + "/list",
    method: "POST",
    singleSelect: true,
    height: document.documentElement.clientHeight - 70,
    clientPaging: false,
    pagination: true,
    rownumbers: true,
    pageSize: 50,
    nowrap: false,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
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
          data: processHistory
        }),
        success: function (data) {
          success(JSON.parse(data));
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

function refresh() {
  $('#searchAllInput').val('');
  $('#seviceTypeSelect').val('');
  $('#resultSelect').val('');
  $('#fromDate').val('');
  $('#toDate').val('');
  processHistory = new Object();
  loadTable();
}

function changeServiceType() {
  processHistory.serviceType = $('#seviceTypeSelect').val();
  loadTable();
}

function changeResult() {
  processHistory.result = $('#resultSelect').val();
  loadTable();
}

function changeFromDate() {
  let fromDate = stringToDate($('.from-date').val());
  if ($('.to-date').val() != '' && stringToDate($('.to-date').val()).getTime() < fromDate.getTime()) {
    $.modal.alertError('Quý khách không thể chọn từ ngày cao hơn đến ngày.')
    $('.from-date').val('');
  } else {
    processHistory.fromDate = fromDate.getTime();
    loadTable();
  }
}

function changeToDate() {
  let toDate = stringToDate($('.to-date').val());
  if ($('.from-date').val() != '' && stringToDate($('.from-date').val()).getTime() > toDate.getTime()) {
    $.modal.alertError('Quý khách không thể chọn đến ngày thấp hơn từ ngày.')
    $('.to-date').val('');
  } else {
    toDate.setHours(23, 59, 59);
    processHistory.toDate = toDate.getTime();
    loadTable();
  }
}

function stringToDate(dateStr) {
  let dateParts = dateStr.split('/');
  return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}
