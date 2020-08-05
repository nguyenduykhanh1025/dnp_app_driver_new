"use strict";
const PREFIX = ctx + "om/support";
var shipment = new Object();

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
      shipment.blNo = $("#searchBox").val().toUpperCase();
      shipment.bookingNo = $("#searchBox").val().toUpperCase();
      shipment.vslNm = $("#searchBox").val().toUpperCase();
      shipment.voyNo = $("#searchBox").val().toUpperCase();
      shipment.taxCode = $("#searchBox").val().toUpperCase();
      loadTable();
    }
  });

});

function loadTable() {
  $("#dg").datagrid({
    url: PREFIX + "/shipments",
    method: "POST",
    singleSelect: true,
    height: document.documentElement.clientHeight - 150,
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
          data: shipment,
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

function formatCustom(value, row, index) {
  if (row.serviceType == 1 || row.serviceType == 4) {
    return '<a class="btn btn-primary btn-xs" onclick="openCustomSupport(\'' + row.id + '\')"><i class="fa fa-view"></i>Hải quan</a> ';
  }
  return '';
}


function formatPayment(value, row, index) {
  return '<a class="btn btn-default btn-xs" onclick="openPaymentSupport(\'' + row.id + '\')"><i class="fa fa-view"></i>Thanh toán</a> ';
}

function formatDo(value, row, index) {
  if (row.serviceType == 1) {
    return '<a class="btn btn-danger btn-xs" onclick="openReceiverDOSupport(\'' + row.id + '\')"><i class="fa fa-check-circle"></i> Thu Chứng Từ</a> ';
  }
  return '<span style="color: #333333; font-size: 12px;">Đầy đủ chứng từ</span>';
}

function formatDriver(value, row, index) {
  return '<a class="btn btn-warning btn-xs" onclick="openDriverSupport(\'' + row.id + '\')"><i class="fa fa-view"></i>Vận tải</a> ';
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

function formatText(value) {
  return '<div class="easyui-tooltip" title="' + (value != null ? value : "Trống") + '" style="width: 80; text-align: center;"><span>' + (value != null ? value : "") + '</span></div>';
}

function changeServiceType() {
  shipment.serviceType = $('#serviceType').val();
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
    shipment.fromDate = fromDate.getTime();
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
    shipment.toDate = toDate.getTime();
    loadTable();
  }
});

function openCustomSupport(id) {
  $.modal.openTab("Hỗ trợ Hải Quan cho [BillNo/Booking No]", PREFIX + "/custom/" + id, null, null);
}

function openPaymentSupport(id) {
  $.modal.openWithOneButton("Thanh toán", PREFIX + "/payment/" + id, null, null);
}

function openReceiverDOSupport(id) {
  $.modal.openWithOneButton("DO gốc", PREFIX + "/do/" + id);
}

function openDriverSupport(id) {
  $.modal.openWithOneButton("Tài xế", PREFIX + "/driver/" + id, null, null);
}


function finishForm(res) {
  if (res.code == 0) {
    $.modal.msgSuccess(res.msg);
  } else {
    $.modal.msgError(res.msg);
  }
  loadTable();
}