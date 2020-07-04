const PREFIX = ctx + "history/truck";
var pickupHistory = new Object();

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
      pickupHistory.fullName = $('#searchAllInput').val().toUpperCase();
      pickupHistory.mobileNumber = $('#searchAllInput').val().toUpperCase();
      pickupHistory.containerNo1 = $('#searchAllInput').val().toUpperCase();
      pickupHistory.containerNo2 = $('#searchAllInput').val().toUpperCase();
      pickupHistory.truckNo = $('#searchAllInput').val().toUpperCase();
      pickupHistory.chassisNo = $('#searchAllInput').val().toUpperCase();
      pickupHistory.yardPosition1 = $('#searchAllInput').val().toUpperCase();
      pickupHistory.yardPosition2 = $('#searchAllInput').val().toUpperCase();
      pickupHistory.sztp = $('#searchAllInput').val().toUpperCase();
      pickupHistory.vslNm = $('#searchAllInput').val().toUpperCase();
      pickupHistory.voyNo = $('#searchAllInput').val().toUpperCase();
      pickupHistory.driverName = $('#searchAllInput').val().toUpperCase();
      pickupHistory.driverPhoneNumber = $('#searchAllInput').val().toUpperCase();
      pickupHistory.logisticGroupName = $('#searchAllInput').val().toUpperCase();
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
        data: JSON.stringify(pickupHistory),
        dataType: 'text',
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
  $('#fromDate').val('');
  $('#toDate').val('');
  pickupHistory = new Object();
  loadTable();
}

function changeFromDate() {
  pickupHistory.fromDate = stringToDate($('.from-date').val()).getTime();
  loadTable();
}

function changeToDate() {
  let toDate = stringToDate($('.to-date').val());
  if ($('.from-date').val() != '' && stringToDate($('.from-date').val()).getTime() > toDate.getTime()) {
    $.modal.alertError('Quý khách không thể chọn đến ngày thấp hơn từ ngày.')
    $('.to-date').val('');
  } else {
    toDate.setHours(23, 59, 59);
    pickupHistory.toDate = toDate.getTime();
    loadTable();
  }
}

function stringToDate(dateStr) {
  let dateParts = dateStr.split('/');
  return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}
