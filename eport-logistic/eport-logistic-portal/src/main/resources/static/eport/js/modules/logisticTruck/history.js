const PREFIX = ctx + "logistic/truck/history";
var pickupHistory = new Object();

$(document).ready(function () {
  
  let date = new Date();
  let firstDay = new Date(date.getFullYear(), date.getMonth(), 1); 
  let lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
  lastDay.setHours(23, 59, 59);

  pickupHistory.fromDate = firstDay.getTime();
  pickupHistory.toDate = lastDay.getTime();

  $('.from-date').val(("0" + firstDay.getDate()).slice(-2) + "/" + ("0"+(firstDay.getMonth()+1)).slice(-2) + "/" + firstDay.getFullYear());

  $('.to-date').val(("0" + lastDay.getDate()).slice(-2) + "/" + ("0"+(lastDay.getMonth()+1)).slice(-2) + "/" + lastDay.getFullYear());

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
      pickupHistory.containerNo = $('#searchAllInput').val().toUpperCase();
      pickupHistory.truckNo = $('#searchAllInput').val().toUpperCase();
      pickupHistory.chassisNo = $('#searchAllInput').val().toUpperCase();
      pickupHistory.driverName = $('#searchAllInput').val().toUpperCase();
      pickupHistory.driverPhoneNumber = $('#searchAllInput').val().toUpperCase();
      pickupHistory.blNo = $('#searchAllInput').val().toUpperCase();
      pickupHistory.bookingNo = $('#searchAllInput').val().toUpperCase();
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
    + ":" + formatNumber(date.getMinutes())
    + ":" + formatNumber(date.getSeconds());
}

function formatNumber(number) {
  return number < 10 ? "0" + number : number;
}

function formatBillBook(value, row) {
  if (row.shipment.bookingNo) {
    return row.shipment.bookingNo
  } else if (row.shipment.blNo) {
    return row.shipment.blNo;
  }
  return '';
}

function loadTable() {
  $("#dg").datagrid({
    url: PREFIX + "/list",
    method: "POST",
    singleSelect: true,
    height: currentHeight,
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
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: pickupHistory
        }),
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
  let fromDate = stringToDate($('.from-date').val());
  if ($('.to-date').val() != '' && stringToDate($('.to-date').val()).getTime() < fromDate.getTime()) {
    $.modal.alertError('Quý khách không thể chọn từ ngày cao hơn đến ngày.')
    $('.from-date').val('');
  } else {
    pickupHistory.fromDate = fromDate.getTime();
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
    pickupHistory.toDate = toDate.getTime();
    loadTable();
  }
}

function stringToDate(dateStr) {
  let dateParts = dateStr.split('/');
  return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}
