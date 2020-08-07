const PREFIX = ctx + "history/robot";
var processHistory = new Object();

$(document).ready(function () {
  let fromMonth = (new Date().getMonth()+1 < 10) ? "0" + (new Date().getMonth()+1) : new Date().getMonth()+1;
  let toMonth = (new Date().getMonth() +2 < 10) ? "0" + (new Date().getMonth() +2 ): new Date().getMonth() +2;
  $('#fromDate').val("01/"+ fromMonth + "/" + new Date().getFullYear());
  $('#toDate').val("01/"+ (toMonth > 12 ? "01" +"/"+ (new Date().getFullYear()+1)  : toMonth + "/" + new Date().getFullYear()));
  let fromDate = stringToDate($('#fromDate').val());
  let toDate =  stringToDate($('#toDate').val());
  fromDate.setHours(0,0,0);
  toDate.setHours(23, 59, 59);
  processHistory.fromDate = fromDate.getTime();
  processHistory.toDate = toDate.getTime();

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
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: processHistory
        }),
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
    processHistory.fromDate = fromDate.getTime();
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
    processHistory.toDate = toDate.getTime();
    loadTable();
  }
});

function stringToDate(dateStr) {
  let dateParts = dateStr.split('/');
  return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}
