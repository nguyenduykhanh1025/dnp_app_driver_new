var isChange = true;
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
    "Phương Tiện",
    "Nơi Hạ Vỏ",
    "T.T Hải Quan",
    "T.T Thanh Toán",
    "T.T Làm Lệnh",
    "T.T DO Gốc",
    "T.T Nhận Cont",
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
      data: "opeCode",
      editor: false
    },
    {
      data: "blNo",
    },
    {
      data: "containerNo",
    },
    {
      data: "sztp",
      editor: false
    },
    {
      data: "fe",
      editor: false
    },
    {
      data: "consignee"
    },
    {
      data: "sealNo",
      editor: false
    },
    {
      data: "expiredDem",
      type: "date",
      dateFormat: "DD/MM/YYYY",
      correctFormat: true,
      defaultDate: new Date(),
      editor: false
    },
    {
      data: "vslNm",
      type: "autocomplete",
      editor: false
    },
    {
      data: "voyNo",
      editor: false
    },
    {
      data: "loadingPort",
      editor: false
    },
    {
      data: "dischargePort",
      editor: false
    },
    {
      data: "transportType",
    },
    {
      data: "emptyDepot",
      editor: false
    },
    {
      data: "customStatus"=="N"?"Chưa thông quan":"Thông quan",
      editor: false
    },
    {
      data: "paymentStatus",
      editor: false
    },
    {
      data: "processStatus",
      editor: false
    },
    {
      data: "doStatus",
      editor: false
    },
    {
      data: "status",
      editor: false
    },
    {
      data: "remark",
    },
  ],
  afterChange: function (changes, src) {
    //Get data change in cell to render another column
    if (src !== "loadData") {
      changes.forEach(function interate(row) {
        var blNo;
        var containerNo;
        if (row[1] == "blNo" || row[1] == "containerNo") {
          blNo = hot.getDataAtRow(row[0])[2];
          containerNo = hot.getDataAtRow(row[0])[3];
          isChange = true;
        } else {
          isChange = false;
        }
        if (blNo != null && containerNo != null && isChange) {
          // Call data to auto-fill
          hot.setDataAtCell(row[0], 1, "CMC"); //opeCode
          hot.setDataAtCell(row[0], 4, "22G0"); //sztp
          hot.setDataAtCell(row[0], 5, "F"); //fe
          hot.setDataAtCell(row[0], 6, "VINCOSHIP"); //consignee
          hot.setDataAtCell(row[0], 7, "G8331306"); //sealNo
          hot.setDataAtCell(row[0], 8, "19/05/2020"); //expiredem
          hot.setDataAtCell(row[0], 9, "Vessel"); //vslNm
          hot.setDataAtCell(row[0], 10, "Voyage"); //voyNo
          hot.setDataAtCell(row[0], 11, "LoadingPort"); //loadingPort
          hot.setDataAtCell(row[0], 12, "dischargePort"); //dischargePort
          hot.setDataAtCell(row[0], 13, "Truck"); //transportType
          hot.setDataAtCell(row[0], 14, "emptyDepot"); //emptyDepot
          hot.setDataAtCell(row[0], 15, "Chưa thông quan"); //customStatus
          hot.setDataAtCell(row[0], 16, "Chưa thanh toán"); //paymentStatus
          hot.setDataAtCell(row[0], 17, "Chưa làm lệnh"); //processStatus
          hot.setDataAtCell(row[0], 18, "Chưa nhận DO gốc"); //doStatus
          hot.setDataAtCell(row[0], 19, "Chưa nhận container"); //status
          hot.setDataAtCell(row[0], 20, "Ghi chu"); //remark
        }
      });
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
    shipmentDetail.opeCode = object["opeCode"];
    shipmentDetail.blNo = object["blNo"];
    shipmentDetail.containerNo = object["containerNo"];
    shipmentDetail.sztp = object["sztp"];
    shipmentDetail.fe = object["fe"];
    shipmentDetail.consignee = object["consignee"];
    shipmentDetail.sealNo = object["sealNo"];
    //shipmentDetail.expiredDem = object["expiredDem"];
    shipmentDetail.vslNm = object["vslNm"];
    shipmentDetail.voyNo = object["voyNo"];
    shipmentDetail.loadingPort = object["loadingPort"];
    shipmentDetail.dischargePort = object["dischargePort"];
    shipmentDetail.transportType = object["transportType"];
    shipmentDetail.emptyDepot = object["emptyDepot"];
    shipmentDetail.customStatus = object["customStatus"];
    shipmentDetail.paymentStatus = object["paymentStatus"];
    shipmentDetail.processStatus = object["processStatus"];
    shipmentDetail.doStatus = object["doStatus"];
    shipmentDetail.status = object["status"];
    shipmentDetail.remark = object["remark"];
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

