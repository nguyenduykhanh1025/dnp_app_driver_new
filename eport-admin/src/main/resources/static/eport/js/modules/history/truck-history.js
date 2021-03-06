const PREFIX = ctx + "history/truck";
var pickupHistory = new Object();

$(document).ready(function () {

  //DEFAULT SEARCH FOLLOW DATE
  let fromMonth = (new Date().getMonth()+1 < 10) ? "0" + (new Date().getMonth()+1) : new Date().getMonth()+1;
  let toMonth = (new Date().getMonth() +2 < 10) ? "0" + (new Date().getMonth() +2 ): new Date().getMonth() +2;
  $('#fromDate').val("01/"+ fromMonth + "/" + new Date().getFullYear());
  $('#toDate').val("01/"+ (toMonth > 12 ? "01" +"/"+ (new Date().getFullYear()+1)  : toMonth + "/" + new Date().getFullYear()));
  let fromDate = stringToDate($('#fromDate').val());
  let toDate =  stringToDate($('#toDate').val());
  fromDate.setHours(0,0,0);
  toDate.setHours(23, 59, 59);
  pickupHistory.fromDate = fromDate.getTime();
  pickupHistory.toDate = toDate.getTime();

  loadTable();

  laydate.render({
    elem: '#fromDate',
    format: "dd/MM/yyyy"
  });

  laydate.render({
    elem: '#toDate',
    format: "dd/MM/yyyy"
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
      pickupHistory.logisticGroupName = $('#searchAllInput').val().toUpperCase();
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
          data: pickupHistory
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
  $('#fromDate').val('');
  $('#toDate').val('');
  pickupHistory = new Object();
  loadTable();
}

function formatBillBooking(value, row) {
  if (row.shipment.bookingNo) {
    return row.shipment.bookingNo;
  }
  if (row.shipment.blNo) {
    return row.shipment.blNo;
  }
  return '';
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

$("#fromDate").on("inputchange", function () {
  let fromDate = stringToDate($('#fromDate').val());
  if ($('#toDate').val() != '' && stringToDate($('#toDate').val()).getTime() < fromDate.getTime()) {
    $.modal.alertError('Quý khách không thể chọn từ ngày cao hơn đến ngày.')
    $('#fromDate').val('');
  } else {
    pickupHistory.fromDate = fromDate.getTime();
    loadTable();
  }
});

$("#toDate").on("inputchange", function () {
  let toDate = stringToDate($("#toDate").val());
  if ($("#fromDate").val() != "" && stringToDate($("#fromDate").val()).getTime() > toDate.getTime()) {
    $.modal.alertError("Quý khách không thể chọn đến ngày thấp hơn từ ngày.");
    $("#toDate").val("");
  } else {
    toDate.setHours(23, 59, 59);
    pickupHistory.toDate = toDate.getTime();
    loadTable();
  }
});

function stringToDate(dateStr) {
  let dateParts = dateStr.split('/');
  return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}
