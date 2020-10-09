const PREFIX = ctx + "depo";
const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var edo = new Object();
edo.params = new Object();
var fromDate, toDate;
var edoSelected;


$(function () {
  $(".main-body").layout();
  $(".collapse").click(function () {
    $(".main-body__search-wrapper").height(15);
    $(".main-body__search-wrapper--container").hide();
    $(this).hide();
    $(".uncollapse").show();
  });

  $(".uncollapse").click(function () {
    $(".main-body__search-wrapper").height(SEARCH_HEIGHT + 20);
    $(".main-body__search-wrapper--container").show();
    $(this).hide();
    $(".collapse").show();
  });

  $(".left-side__collapse").click(function () {
    $('#main-layout').layout('collapse', 'west');
  });

  $("#blNo").textbox('textbox').bind('keydown', function (e) {
    // enter key
    if (e.keyCode == 13) {
      edo.billOfLading = $("#blNo").textbox('getText').toUpperCase();
      loadTable();
    }
  });

  $("#containerNo").textbox('textbox').bind('keydown', function (e) {
    // enter key
    if (e.keyCode == 13) {
      edo.containerNumber = $("#containerNo").textbox('getText').toUpperCase();
      loadTable();
    }
  });

  $('#fromDate').datebox({
    onSelect: function (date) {
      date.setHours(0, 0, 0);
      fromDate = date;
      if (toDate != null && date.getTime() > toDate.getTime()) {
        $.modal.alertWarning("Từ ngày không được lớn hơn đến ngày.");
      } else {
        fromDate.setHours(0, 0, 0);
        edo.params.fromDate = dateToString(fromDate);
        loadTable();
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
        edo.params.toDate = dateToString(toDate);
        loadTable();
      }
      return date;
    }
  });

  loadTable();
});

// Convert date to string
function dateToString(date) {
  return ("0" + date.getDate()).slice(-2) + "/" + ("0" + (date.getMonth() + 1)).slice(-2) + "/" + date.getFullYear()
    + " " + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2) + ":" + ("0" + date.getSeconds()).slice(-2);
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

function loadTable() {
  $("#dg").datagrid({
    url: PREFIX + "/blNo",
    method: "POST",
    singleSelect: true,
    height: $(document).height() - $(".main-body__search-wrapper").height() - 45,
    clientPaging: true,
    pagination: true,
    pageSize: 20,
    onBeforeSelect: function (index, row) {
      getSelectedRow(index, row);
    },
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
        accept: "text/plain",
        dataType: "text",
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: edo,
        }),
        dataType: "json",
        success: function (data) {
          edoSelected = null;
          loadTableByContainer();
          if (data != null) {
            success(data);
            $("#dg").datagrid("selectRow", 0);
          }
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
    error: function () {
      error.apply(this, arguments);
    },
  });
}

function formatDateTime(value) {
  if (value == null) return "";
  let date = new Date(value);
  return formatNumber(date.getDate())
    + "/" + formatNumber(date.getMonth() + 1)
    + "/" + date.getFullYear()
    + " " + formatNumber(date.getHours())
    + ":" + formatNumber(date.getMinutes());
}

function formatNumber(number) {
  return number < 10 ? "0" + number : number;
}

function getSelectedRow(index, row) {
  edoSelected = row;
  loadTableByContainer();
}

function searchDo() {
  edo.billOfLading = $('#blNo').val().toUpperCase();
  edo.containerNumber = $('#containerNo').val().toUpperCase();
  loadTable();
}

function clearInput() {
  $("#blNo").textbox('setText', '');
  $("#containerNo").textbox('setText', '');
  $('#fromDate').datebox('setValue', '');
  $('#toDate').datebox('setValue', '');
  edo = new Object();
  loadTable();
}

function loadTableByContainer() {
  if (edoSelected != null) {
    let containerEdoEeq = new Object();
    containerEdoEeq.params = {
      billOfLading: edoSelected.billOfLading
    };
    $("#container-grid").datagrid({
      url: PREFIX + "/blNo/containers",
      method: "POST",
      singleSelect: true,
      clientPaging: false,
      height: $(document).height() - $(".main-body__search-wrapper").height() - 45,
      pagination: true,
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
          data: JSON.stringify({
            pageNum: param.page,
            pageSize: param.rows,
            orderByColumn: param.sort,
            isAsc: param.order,
            data: containerEdoEeq
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
  } else {
    $("#container-grid").datagrid({
      singleSelect: true,
      clientPaging: false,
      height: $(document).height() - $(".main-body__search-wrapper").height() - 45,
      pagination: true,
      pageSize: 20,
      nowrap: false,
      striped: true,
      rownumbers: true,
      loader: function (param, success, error) {
        success([]);
      },
    });
  }
}


