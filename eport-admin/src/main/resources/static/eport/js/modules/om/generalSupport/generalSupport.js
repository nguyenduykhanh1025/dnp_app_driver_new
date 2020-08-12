const PREFIX = ctx + "om/support";
var shipment = new Object();
var conf = {
  options: {
    fitColumns: true,
    columns: [
      [
        { field: "company", title: "Company Name", width: 200 },
        { field: "contact", title: "Contact Name", width: 200 },
        { field: "country", title: "Country", width: 200 },
      ],
    ],
    data: [{ company: "Speed Info", contact: "Minna John", country: "Sweden" }],
  },
  subgrid: {
    options: {
      fitColumns: true,
      foreignField: "companyid",
      columns: [
        [
          { field: "orderdate", title: "Order Date", width: 200 },
          { field: "shippeddate", title: "Shipped Date", width: 200 },
          { field: "freight", title: "Freight", width: 200 },
        ],
      ],
      data: [{ orderdate: "08/23/2012", shippeddate: "12/25/2012", freight: 9734 }],
    },
  },
};

$(document).ready(function () {
  "use strict";
  $(".left").css("height", $(document).height() - 100);
  $(".right").css("height", $(document).height() - 100);
  $("#btn-collapse").click(function () {
    handleCollapse(true);
  });
  $("#btn-uncollapse").click(function () {
    handleCollapse(false);
  });
  $("#dg").height($(document).height() - 100);
  $("#dgContainer").height($(document).height() - 100);

  $(".c-search-box-vessel").select2({
    theme: "bootstrap",
    placeholder: "Vessel",
    allowClear: true,
  });

  $(".c-search-box-vessel-code").select2({
    theme: "bootstrap",
    placeholder: "Vessel Code",
    allowClear: true,
  });

  $(".c-search-box-voy-no").select2({
    theme: "bootstrap",
    placeholder: "Voy No",
    allowClear: true,
  });
  $(".c-search-opr-code").select2({
    theme: "bootstrap",
    placeholder: "OPR Code",
    allowClear: true,
  });
  loadTable();
  loadRightTable();
});

function loadRightTable() {
  $("#dg-right")
    .datagrid({
      width: document.getElementsByClassName("right").width,
      height: document.documentElement.clientHeight - 70,
    })
    .datagrid("subgrid", conf);
}

function loadTable() {
  $("#dg").datagrid({
    url: PREFIX + "/shipments",
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

function handleCollapse(status) {
  if (status) {
    $(".left").css("width", "0.5%");
    $(".left").children().hide();
    $("#btn-collapse").hide();
    $("#btn-uncollapse").show();
    $(".right").css("width", "99%");
    loadRightTable();
    return;
  }
  $(".left").css("width", "50%");
  $(".left").children().show();
  $("#btn-collapse").show();
  $("#btn-uncollapse").hide();
  $(".right").css("width", "50%");
  loadRightTable();
  return;
}

function formatLogistic(value, row, index) {
  return '<a onclick="logisticInfo(' + row.logisticGroupId + "," + "'" + value + "')\"> " + value + "</a>";
}

function formatBlBooking(value, row) {
  if (row.blNo) {
    return row.blNo;
  }
  if (row.bookingNo) {
    return row.bookingNo;
  }
  return "";
}

function formatServiceType(value) {
  switch (value) {
    case 1:
      return "Bốc Hàng";
    case 2:
      return "Hạ Rỗng";
    case 3:
      return "Bốc Rỗng";
    case 4:
      return "Hạ Hàng";
  }
}

function formatDate(value) {
  return value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4) + value.substring(10, 19);
}

function formatText(value) {
  return '<div class="easyui-tooltip" title="' + (value != null ? value : "Trống") + '" style="width: 80; text-align: center;"><span>' + (value != null ? value : "") + "</span></div>";
}
