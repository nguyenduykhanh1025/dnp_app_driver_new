"use strict";
const PREFIX = ctx + "om/paymentHistory";
var paymentHistory = new Object();

$(document).ready(function () {
  loadTable();

  laydate.render({
    elem: '#fromDate',
    format: "dd/MM/yyyy"
  });

  laydate.render({
    elem: '#toDate',
    format: "dd/MM/yyyy"
  });

  $("#searchBox").keyup(function (event) {
    if (event.keyCode == 13) {
      paymentHistory.orderId = $("#searchBox").val().toUpperCase();
      paymentHistory.blNo = $("#searchBox").val().toUpperCase();
      paymentHistory.bookingNo = $("#searchBox").val().toUpperCase();
      loadTable();
    }
  });

});

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
    nowrap: true,
    striped: true,
    loadMsg: " Đang xử lý...",
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
          data: paymentHistory,
        }),
        success: function (data) {
          success(JSON.parse(data));
          $("#dg").datagrid("hideColumn", "id");
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

function formatLogistic(value, row, index) {
  return '<a onclick="logisticInfo(' + row.logisticGroupId + ',' + '\'' + value + '\')"> '+ value + '</a>'
}

function formatBlBooking(value, row, index) {
  if (row.blNo) {
    return row.blNo;
  }
  if (row.bookingNo) {
    return row.bookingNo;
  }
  return '';
}

function formatStatus(value, row, index) {
  if ('1' == value) {
    return 'Đã thanh toán';
  }
  return 'Chưa thanh toán';
}

function formatServiceType(value) {
  switch (value) {
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

function formatDate(value) {
  return value.substring(8, 10)+'/'+value.substring(5, 7)+'/'+value.substring(0, 4)+value.substring(10, 19);
}

function formatMoney(value) {
  return value.format(2, 3, ',', '.');
}

Number.prototype.format = function (n, x, s, c) {
  var re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\D' : '$') + ')',
    num = this.toFixed(Math.max(0, ~~n));

  return (c ? num.replace('.', c) : num).replace(new RegExp(re, 'g'), '$&' + (s || ','));
};

function changeServiceType() {
  paymentHistory.serviceType = $('#serviceType').val();
  loadTable();
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

$("#fromDate").on("inputchange", function () {
  let fromDate = stringToDate($('#fromDate').val());
  if ($('#toDate').val() != '' && stringToDate($('#toDate').val()).getTime() < fromDate.getTime()) {
    $.modal.alertError('Quý khách không thể chọn từ ngày cao hơn đến ngày.')
    $('#fromDate').val('');
  } else {
    paymentHistory.fromDate = fromDate.getTime();
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
    paymentHistory.toDate = toDate.getTime();
    loadTable();
  }
});

function logisticInfo(id, logistics) {
  $.modal.openLogisticInfo("Thông tin liên lạc " + logistics, ctx + "om/support/logistics/" + id + "/info", null, 470, function() {
    $.modal.close();
  });
}

$('#logistic').change(function() {
  if (0 != $('#logistic option:selected').val()) {
    paymentHistory.logisticGroupId = $('#logistic option:selected').val();
  } else {
    paymentHistory.logisticGroupId = '';
  }
  loadTable();
});