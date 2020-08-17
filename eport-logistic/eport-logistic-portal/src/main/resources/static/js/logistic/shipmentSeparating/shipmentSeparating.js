var prefix = ctx + "logistic/send-cont-full";
var prefix_main = ctx + "logistic/shipmentSeparating";
var interval, currentPercent, timeout;
var dogrid = document.getElementById("container-grid"),
  hot;
var shipmentSelected,
  shipmentDetails,
  shipmentDetailIds,
  sourceData,
  processOrderIds;
var contList = [],
  temperatureDisable = [];
var conts = "";
var allChecked = false;
var checkList = [];
var rowAmount = 0;
var shipmentSearch = new Object();
shipmentSearch.serviceType = 4;
var sizeList = [];
var berthplanList; // get infor
//dictionary sizeList
$.ajax({
  type: "GET",
  url: ctx + "logistic/size/container/list",
  success(data) {
    if (data.code == 0) {
      data.data.forEach((element) => {
        sizeList.push(element["dictLabel"]);
      });
    }
  },
});
var consigneeList,
  opeCodeList,
  vslNmList,
  currentProcessId,
  currentSubscription;

$.ajax({
  url: ctx + "logistic/source/option",
  method: "GET",
  success: function (data) {
    if (data.code == 0) {
      //            opeCodeList = data.opeCodeList;
      //            vslNmList = data.vslNmList;
      consigneeList = data.consigneeList;
    }
  },
});
//get opeCodeList BerthPlan
$.ajax({
  url: prefix + "/berthplan/ope-code/list",
  method: "GET",
  success: function (data) {
    if (data.code == 0) {
      opeCodeList = data.opeCodeList;
    }
  },
});
var cargoTypeList = [
  "AK:Over Dimension",
  "BB:Break Bulk",
  "BN:Bundle",
  "DG:Dangerous",
  "DR:Reefer & DG",
  "DE:Dangerous Empty",
  "FR:Fragile",
  "GP:General",
  "MT:Empty",
  "RF:Reefer",
];

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
  //DEFAULT SEARCH FOLLOW DATE
  let fromMonth =
    new Date().getMonth() < 10
      ? "0" + new Date().getMonth()
      : new Date().getMonth();
  let toMonth =
    new Date().getMonth() + 2 < 10
      ? "0" + (new Date().getMonth() + 2)
      : new Date().getMonth() + 2;
  $("#fromDate").val("01/" + fromMonth + "/" + new Date().getFullYear());
  $("#toDate").val(
    "01/" +
      (toMonth > 12
        ? "01" + "/" + (new Date().getFullYear() + 1)
        : toMonth + "/" + new Date().getFullYear())
  );
  let fromDate = stringToDate($("#fromDate").val());
  let toDate = stringToDate($("#toDate").val());
  fromDate.setHours(0, 0, 0);
  toDate.setHours(23, 59, 59);
  shipmentSearch.fromDate = fromDate.getTime();
  shipmentSearch.toDate = toDate.getTime();

  loadTable();
  $(".left-side").css("height", $(document).height());
  $("#btn-collapse").click(function () {
    handleCollapse(true);
  });
  $("#btn-uncollapse").click(function () {
    handleCollapse(false);
  });
  //find date
  $(".from-date").datetimepicker({
    language: "en",
    format: "dd/mm/yyyy",
    autoclose: true,
    todayBtn: true,
    todayHighlight: true,
    pickTime: false,
    minView: 2,
  });
  $(".to-date").datetimepicker({
    language: "en",
    format: "dd/mm/yyyy",
    autoclose: true,
    todayBtn: true,
    todayHighlight: true,
    pickTime: false,
    minView: 2,
  });
});
//search date
function changeFromDate() {
  let fromDate = stringToDate($("#fromDate").val());
  if (
    $("#toDate").val() != "" &&
    stringToDate($("#toDate").val()).getTime() < fromDate.getTime()
  ) {
    $.modal.alertError("Quý khách không thể chọn từ ngày cao hơn đến ngày.");
    $("#fromDate").val("");
  } else {
    shipmentSearch.fromDate = fromDate.getTime();
    loadTable();
  }
}

function changeToDate() {
  let toDate = stringToDate($(".to-date").val());
  if (
    $(".from-date").val() != "" &&
    stringToDate($(".from-date").val()).getTime() > toDate.getTime()
  ) {
    $.modal.alertError("Quý khách không thể chọn đến ngày thấp hơn từ ngày.");
    $(".to-date").val("");
  } else {
    toDate.setHours(23, 59, 59);
    shipmentSearch.toDate = toDate.getTime();
    loadTable();
  }
}

function stringToDate(dateStr) {
  let dateParts = dateStr.split("/");
  return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}

document
  .getElementById("bookingSearch")
  .addEventListener("keyup", function (event) {
    event.preventDefault();
    if (event.keyCode === 13) {
      shipmentSearch.bookingNo = $("#bookingSearch").val().toUpperCase();
      loadTable();
    }
  });

function handleCollapse(status) {
  if (status) {
    $(".left-side").css("width", "0.5%");
    $(".left-side").children().hide();
    $("#btn-collapse").hide();
    $("#btn-uncollapse").show();
    $(".right-side").css("width", "99%");
    setTimeout(function () {
      hot.render();
    }, 500);
    return;
  }
  $(".left-side").css("width", "33%");
  $(".left-side").children().show();
  $("#btn-collapse").show();
  $("#btn-uncollapse").hide();
  $(".right-side").css("width", "67%");
  setTimeout(function () {
    hot.render();
  }, 500);
}

// LOAD SHIPMENT LIST
function loadTable(msg) {
  if (msg) {
    $.modal.alertSuccess(msg);
  }
  $("#dg").datagrid({
    url: ctx + "logistic/shipmentSeparating/houseBill/list",
    height: window.innerHeight - 110,
    method: "post",
    singleSelect: true,
    collapsible: true,
    clientPaging: false,
    pagination: true,
    rownumbers: true,
    onClickRow: function () {
      getSelected();
    },
    pageSize: 50,
    nowrap: true,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      let opts = $(this).datagrid("options");
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
          data: {},
        }),
        success: function (data) {
          success(data);
          $("#dg").datagrid("hideColumn", "id");
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
  $("#dg2").datagrid({});
}

// FORMAT DATE FOR SHIPMENT LIST
function formatDate(value) {
  let date = new Date(value);
  let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  let month = date.getMonth() + 1;
  let monthText = month < 10 ? "0" + month : month;
  let hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
  let minutes =
    date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
  let seconds =
    date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
  return (
    day +
    "/" +
    monthText +
    "/" +
    date.getFullYear() +
    " " +
    hours +
    ":" +
    minutes +
    ":" +
    seconds
  );
}

// HANDLE WHEN SELECT A SHIPMENT
function getSelected() {
  let row = $("#dg").datagrid("getSelected");
  if (row) {
    houseBillSelected = row;
    $("#masterBillNo").text(row.masterBillNo);
    $("#consignee").text(row.consignee2);
    loadHouseBillDetail(row.houseBillNo);
  }
}

function loadHouseBillDetail(houseBillNo) {
  $("#dg2").datagrid({
    url: ctx + "logistic/shipmentSeparating/houseBill/detail",
    height: window.innerHeight - 110,
    method: "post",
    collapsible: true,
    clientPaging: false,
    pagination: true,
    rownumbers: true,
    onClickRow: function () {
      getSelected();
    },
    pageSize: 50,
    nowrap: true,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      let opts = $(this).datagrid("options");
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
          data: houseBillNo,
        }),
        success: function (data) {
          success(data);
          $("#dg2").datagrid("hideColumn", "id");
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

function openFormSeparate() {
  const options = {
    url: prefix_main + "/separate",
    title: "Tách Lô Từ Master Bill",
    skin: "custom-modal",
    btn: ["Xác Nhận Tách Bill", "Đóng"],
    yes: function (index, layero) {
      let iframeWin = layero.find("iframe")[0];
      iframeWin.contentWindow.submitHandler(index, layer, $("#dg"));
    },
  };
  $.modal.openOptions(options);
}

function openFormAddCont() {
  let row = $("#dg").datagrid("getSelected");
  if (row) {
    const options = {
      url: prefix_main + "/addCont/" + row.houseBillNo,
      title: "Thêm cont vào House Bill",
      skin: "custom-modal",
      btn: ["Xác Nhận Thêm Cont", "Đóng"],
      yes: function (index, layero) {
        let iframeWin = layero.find("iframe")[0];
        iframeWin.contentWindow.submitHandler(index, layer, $("#dg2"));
      },
    };
    $.modal.openOptions(options);
  }
}

function formatStatus(value, row, index) {
  if (parseInt(row.edo.status) <= 1) {
    return '<span class="badge badge-primary">Chưa làm lệnh</span>';
  } else {
    return '<span class="badge badge-success">Đã làm lệnh</span>';
  }
}

function formatExpiredDem(value, row, index) {
  return formatDate(row.edo.expiredDem);
}
