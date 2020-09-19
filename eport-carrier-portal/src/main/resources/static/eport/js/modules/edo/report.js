const PREFIX = ctx + "edo";
var edo = new Object();

$(function () {
  loadTable(edo);
  $("#searchAll").textbox('textbox').bind('keydown', function (e) {
    // enter key
    if (e.keyCode == 13) {
      edo.billOfLading = $("#searchAll").textbox('getText').toUpperCase();
      edo.containerNumber = $("#searchAll").textbox('getText').toUpperCase();
      edo.consignee = $("#searchAll").textbox('getText').toUpperCase();
      loadTable(edo);
    }
  });

  $('#fromDate').datebox({
    onSelect: function (date) {
      date.setHours(0, 0, 0);
      fromDate = date;
      if (toDate != null && date.getTime() > toDate.getTime()) {
        $.modal.alertWarning("Từ ngày không được lớn hơn đến ngày.");
      } else {
        fromDate.setHours(23, 59, 59);
        edo.fromDate = fromDate.getTime();
        edo.billOfLading = $("#searchBillNo").textbox('getText').toUpperCase();
        edo.containerNumber = $("#searchContNo").textbox('getText').toUpperCase();
        loadTable(edo);
      }
      return date;
    }
  });

  $('#toDate').datebox({
    onSelect: function (date) {
      date.setHours(23, 59, 59);
      toDate = date;
      if (fromDate != null && date.getTime() < fromDate.getTime()) {
        $.modal.alertWarning("Đến ngày không được thấp hơn từ ngày.");
      } else {
        toDate.setHours(23, 59, 59);
        edo.toDate = toDate.getTime();
        edo.billOfLading = $("#searchBillNo").textbox('getText').toUpperCase();
        edo.containerNumber = $("#searchContNo").textbox('getText').toUpperCase();
        loadTable(edo);
      }
    }
  });
});


$("#vessel2").combobox({
  valueField: 'vessel',
  textField: 'vessel',
  url: PREFIX + "/getVessel",
  onSelect: function (vessel) {
    $("#fromDate").datebox('setValue', '');
    $("#toDate").datebox('setValue', '');
    $("#searchAll").textbox('setText', '');
    edo = new Object();
    edo.vessel = vessel.vessel
    edo.voyNo = null;
    loadTable(edo);
    var url = PREFIX + '/getVoyNo/' + vessel.vessel;
    $('#voyNo').combobox({
      valueField: 'voyNo',
      textField: 'voyNo',
      url: url,
      onSelect: function (voyNo) {
        edo.voyNo = voyNo.voyNo
        loadTable(edo);
      }
    });
  }
});

function loadTable(edo) {
  $("#dg").datagrid({
    url: PREFIX + "/edoReport",
    method: "POST",
    height: $(document).height() - 55,
    clientPaging: true,
    pagination: true,
    singleSelect: true,
    pageSize: 20,
    nowrap: false,
    striped: true,
    rownumbers: true,
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
          data: edo
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

$('#searchAll').keyup(function (event) {
  if (event.keyCode == 13) {
    edo = new Object();
    edo.containerNumber = $('#searchAll').val().toUpperCase();
    edo.consignee = $('#searchAll').val().toUpperCase();
    edo.billOfLading = $('#searchAll').val().toUpperCase();
    loadTable(edo)
  }

});

function searchInfoEdo() {
  edo = new Object();
  edo.fromDate = stringToDate($("#fromDate").val()).getTime();
  let toDate = stringToDate($("#toDate").val());
  if ($("#fromDate").val() != "" && stringToDate($("#fromDate").val()).getTime() > toDate.getTime()) {
    $.modal.alertError("Quý khách không thể chọn đến ngày thấp hơn từ ngày.");
    $("#toDate").val("");
  } else {
    toDate.setHours(23, 59, 59);
    edo.toDate = toDate.getTime();
  };
  edo.containerNumber = $('#searchAll').val().toUpperCase();
  edo.consignee = $('#searchAll').val().toUpperCase();
  edo.billOfLading = $('#searchAll').val().toUpperCase();
  edo.vessel = $('.c-search-box-vessel').text().trim();
  edo.voyNo = $(".c-search-box-voy-no").text().trim();
  loadTable(edo)
}

function stringToDate(dateStr) {
  if (dateStr == null || dateStr == undefined) {
    return;
  }
  let dateParts = dateStr.split("/");
  return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}

laydate.render({
  elem: '#toDate',
  format: 'dd/MM/yyyy'
});
laydate.render({
  elem: '#fromDate',
  format: 'dd/MM/yyyy'
});


function formatStatus(value) {
  switch (value) {
    case '1':
      return "<span class='label label-success'>Chưa làm lệnh</span>";
    case '2':
      return "<span class='label label-success'>Đã làm lệnh</span>";
    case '3':
      return "<span class='label label-success'>Gate-in</span>";
  }
}

function dateformatter(date) {
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  var d = date.getDate();
  return (d < 10 ? ('0' + d) : d) + '/' + (m < 10 ? ('0' + m) : m) + '/' + y;
}

function dateparser(s) {
  var ss = (s.split('\.'));
  var d = parseInt(ss[0], 10);
  var m = parseInt(ss[1], 10);
  var y = parseInt(ss[2], 10);
  if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
    return new Date(y, m - 1, d);
  }
}

function searchDo() {
  edo.billOfLading = $("#searchAll").textbox('getText').toUpperCase();
  edo.containerNumber = $("#searchAll").textbox('getText').toUpperCase();
  edo.consignee = $("#searchAll").textbox('getText').toUpperCase();
  edo.fromDate = stringToDate($("#fromDate").val()).getTime();
  let toDate = stringToDate($("#toDate").val());
  if ($("#fromDate").val() != "" && stringToDate($("#fromDate").val()).getTime() > toDate.getTime()) {
    $.modal.alertError("Quý khách không thể chọn đến ngày thấp hơn từ ngày.");
    $("#toDate").val("");
  } else {
    toDate.setHours(23, 59, 59);
    edo.toDate = toDate.getTime();
    loadTable(edo);
  };
  loadTable(edo);
}

function clearInput() {
  edo = new Object();
  $("#fromDate").datebox('setValue', '');
  $("#toDate").datebox('setValue', '');
  $("#searchBillNo").textbox('setText', '');
  $("#searchContNo").textbox('setText', '');
  $("#vessel2").combobox('setText', '');
  $("#voyNo").combobox('setText', '');
  loadTable(edo);
}