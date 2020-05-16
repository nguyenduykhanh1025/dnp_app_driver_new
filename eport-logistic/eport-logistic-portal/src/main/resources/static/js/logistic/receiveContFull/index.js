var prefix = ctx + "logistic/receiveContFull";
var fromDate = "";
var toDate = "";
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected = 0;
var shipmentDetails;
$(document).ready(function () {
  loadTable();
  $(".left-side").css("height", $(document).height());
  $("#btn-collapse").click(function () {
    handleCollapse(true);
  });
  $("#btn-uncollapse").click(function () {
    handleCollapse(false);
  });
});

function loadTable() {
  $("#dg").datagrid({
    url: ctx + "logistic/receiveContFull/listShipment",
    height: heightInfo,
    singleSelect: true,
    collapsible: true,
    clientPaging: false,
    pagination: true,
    onClickRow: function () {
      getSelected();
    },
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
        data: {
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          // fromDate: fromDate,
          // toDate: toDate,
          // voyageNo: $("#voyageNo").val() == null ? "" : $("#voyageNo").val(),
          // blNo: $("#blNo").val() == null ? "" : $("#blNo").val(),
        },
        dataType: "json",
        success: function (data) {
          success(data);
          // $("#dg").datagrid("hideColumn", "id");
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
  $(".left-side").css("width", "20%");
  $(".left-side").children().show();
  $("#btn-collapse").show();
  $("#btn-uncollapse").hide();
  $(".right-side").css("width", "80%");
  setTimeout(function () {
    hot.render();
  }, 500);
}

config = {
  stretchH: "all",
  height: document.documentElement.clientHeight - 70,
  minRows: 100,
  width: "100%",
  minSpareRows: 1,
  rowHeights: 30,
  manualColumnMove: false,
  rowHeaders: true,
  className: "htMiddle",
  colHeaders: [
    "<input type='checkbox' id='parent-checkbox'/>",
    "Hãng Tàu",
    "B/L No",
    "Container No",
    "Kích Thước",
    "F/E",
    "Chủ hàng",
    "Seal No",
    "Hạn Lệnh",
    "Tàu",
    "Chuyến",
    "Cảng Nguồn",
    "Cảng Đích",
    "VGM",
    "Phương Tiện",
    "Nơi Hạ Vỏ",
    "T.T Hải Quan",
    "T.T Thanh Toán",
    "T.T Làm Lệnh",
    "T.T DO Gốc",
    "Ghi Chú",
  ],
  // colWidths: [7, 8, 8, 20, 8, 15, 8, 8, 8, 15],
  filter: "true",
  columns: [
    {
      data: "selected",
      type: "checkbox",
      className: "htCenter",
    },
    {
      data: "carrierCode",
    },
    {
      data: "blNo",
    },
    {
      data: "containerNo",
    },
    {
      data: "sztp",
    },
    {
      data: "fe",
    },
    {
      data: "consignee"
    },
    {
      data: "sealNo",
    },
    {
      data: "expiredDem",
      type: "date",
      dateFormat: "DD/MM/YYYY",
      correctFormat: true,
      defaultDate: new Date(),
    },
    {
      data: "vslNm",
      type: "autocomplete",
    },
    {
      data: "voyNo",
    },
    {
      data: "loadingPort",
    },
    {
      data: "dischargePort",
    },
    {
      data: "vgm",
    },
    {
      data: "transportType",
    },
    {
      data: "emptyDepot",
    },
    {
      data: "customStatus",
    },
    {
      data: "paymentStatus",
    },
    {
      data: "processStatus",
    },
    {
      data: "",
    },
    {
      data: "remark",
    },
  ],
  afterChange: function (changes, src) {
    //Get data change in cell to render another column
    if (src !== "loadData" && (changes[0][3] != "")) {
      // console.log(changes);
      // console.log("Row change: " + changes[0][0])
      // console.log("cell values: " + changes[0][3])
    }
  },
};

hot = new Handsontable(dogrid, config);

function loadShipmentDetail(id) {
  $.ajax({
    url: prefix + "/listShipmentDetail",
    method: "GET",
    data: {
      shipmentId: id
    },
    success: function(result) {
      hot.loadData(result);
      hot.render();
    }
  });
}

function formatDate(value) {
  var date = new Date(value);
  var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  var month = date.getMonth() + 1;
  var monthText = month < 10 ? "0" + month : month;
  return day + "/" + monthText + "/" + date.getFullYear();
}

// Handle add
$(function() {
  var options = {
    createUrl: prefix + "/add",
    updateUrl: "0",
    modalName: " Lô"
  };
  $.table.init(options);
});

function handleRefresh() {
  loadTable();
}

function getSelected() {
  var row = $("#dg").datagrid("getSelected");
  if (row) {
    shipmentSelected = row.id;
    $(function() {
      var options = {
        createUrl: prefix + "/add",
        updateUrl: prefix + "/edit/" + shipmentSelected,
        modalName: " Lô"
      };
      $.table.init(options);
    });
    $("#loCode").text(row.id);
    $("#taxCode").text(row.taxCode);
    $("#quantity").text(row.containerAmount);
    loadShipmentDetail(row.id);
  }
}

function getDataSelectedFromTable() {
  var myTableData = hot.getSourceData();
  var errorFlg = false;
  if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
    hot.alter("remove_row", parseInt(myTableData.length - 1), (keepEmptyRows = false));
  }
  var cleanedGridData = [];
  $.each(myTableData, function (rowKey, object) {
    if (!hot.isEmptyRow(rowKey) && object["selected"]) {
      cleanedGridData.push(object);
    }
  });
  shipmentDetails = [];
  $.each(cleanedGridData, function (index, object) {
    var shipmentDetail = new Object();
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.shipmentId = shipmentSelected;
    shipmentDetails.push(shipmentDetail);
  });
  // Get result in "selectedList" variable
  if (shipmentDetails.length == 0) {
    $.modal.alert("Bạn chưa chọn container.");
    errorFlg = true;
  }

  if (errorFlg) {
    return false;
  }
}

function getDataFromTable() {
  var myTableData = hot.getSourceData();
  var errorFlg = false;
  if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
    hot.alter("remove_row", parseInt(myTableData.length - 1), (keepEmptyRows = false));
  }
  var cleanedGridData = [];
  $.each(myTableData, function (rowKey, object) {
    if (!hot.isEmptyRow(rowKey)) {
      cleanedGridData.push(object);
    }
  });
  shipmentDetails = [];
  $.each(cleanedGridData, function (index, object) {
    var shipmentDetail = new Object();
    shipmentDetail.carrierCode = object["carrierCode"];
    shipmentDetail.blNo = object["blNo"];
    shipmentDetail.containerNo = object["containerNo"];
    shipmentDetail.sztp = object["sztp"];
    shipmentDetail.fe = object["fe"];
    shipmentDetail.consignee = object["consignee"];
    shipmentDetail.sealNo = object["sealNo"];
    shipmentDetail.expiredDem = new Date().getTime();
    shipmentDetail.vslNm = object["vslNm"];
    shipmentDetail.voyNo = object["voyNo"];
    shipmentDetail.loadingPort = object["loadingPort"];
    shipmentDetail.dischargePort = object["dischargePort"];
    shipmentDetail.vgm = object["vgm"];
    shipmentDetail.transportType = object["transportType"];
    shipmentDetail.emptyDepot = object["emptyDepot"];
    shipmentDetail.customStatus = object["customStatus"];
    shipmentDetail.paymentStatus = object["paymentStatus"];
    shipmentDetail.processStatus = object["processStatus"];
    shipmentDetail.doStatus = object["doStatus"];
    shipmentDetail.remark = object["remark"];
    shipmentDetail.shipmentId = shipmentSelected;
    shipmentDetails.push(shipmentDetail);
  });
  // Get result in "selectedList" variable
  if (shipmentDetails.length == 0) {
    $.modal.alert("Bạn chưa nhập thông tin.");
    errorFlg = true;
  }

  if (errorFlg) {
    return false;
  }
}

function registerProcess() {
  if (shipmentSelected == 0) {
    $.modal.msgError("Bạn cần chọn lô trước");
    return;
  } else {
    getDataFromTable();
    if (shipmentDetails.length > 0) {
      $.ajax({
        url: prefix + "/registerProcess",
        method: "post",
        contentType : "application/json",
        accept: 'text/plain',
        data: JSON.stringify(shipmentDetails),
        dataType: 'text',
        success: function (data) {
          var result = JSON.parse(data);
          if(result.code == 0) {
            $.modal.msgSuccess(result.msg);
          } else {
            $.modal.msgError(result.msg);
          }
        },
        error: function (result) {
          $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
        },
      });
    }
  }
}

function checkCustomStatus() {

}

function authentic() {

}

function pay() {

}

function pickTruck() {

}

function pickContOnDemand() {

}

function exportBill() {

}

