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
    url: prefix + "/listShipment",
    height: window.innerHeight - 70,
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
  height: document.documentElement.clientHeight - 100,
  minRows: 100,
  width: "100%",
  minSpareRows: 1,
  rowHeights: 30,
  manualColumnMove: false,
  rowHeaders: true,
  className: "htMiddle",
  colHeaders: [
    "<input type='checkbox' id='parent-checkbox'/>",
    "id",
    "B/L No",
    "Container No",
    "Hãng Tàu",
    "Kích Thước",
    "F/E",
    "Chủ hàng",
    "Seal No",
    "Hạn Lệnh",
    "Trọng tải",
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
  colWidths: [50, 0.01, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 150, 150, 150, 150, 150, 200],
  filter: "true",
  columns: [
    {
      data: "selected",
      type: "checkbox",
      className: "htCenter",
    },
    {
      data: "id",
      editor: false
    },
    {
      data: "blNo",
    },
    {
      data: "containerNo",
    },
    {
      data: "opeCode",
      editor: false
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
    },
    {
      data: "wgt",
      editor: false
    },
    {
      data: "vslNm",
      editor: false
    },
    {
      data: "voyNo",
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
      data: "customStatus",
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
          $.ajax({
            url: prefix + "/getContInfo",
            type: "post",
            data: {
              blNo: blNo, containerNo: containerNo
            } 
          }).done(function(shipmentDetail) {
            if (shipmentDetail != null) {
              var expiredDem = "";
              var customStatus = "";
              var paymentStatus = "";
              var processStatus = "";
              var doStatus = "";
              var status = "";
              if (shipmentDetail.expiredDem != null) {
                expiredDem = shipmentDetail.expiredDem//shipmentDetail.expiredDem.getDate()+"/"+shipmentDetail.expiredDem.getMonth()+"/"+shipmentDetail.expiredDem.getYear();
              }
              switch (shipmentDetail.customStatus) {
                case "N":
                  customStatus = "Chưa thông quan";
                  break;
                default:
                  break;
              }
              switch (shipmentDetail.paymentStatus) {
                case "N":
                  paymentStatus = "Chưa thanh toán";
                  break;
                default:
                  break;
              }
              switch (shipmentDetail.processStatus) {
                case "N":
                  processStatus = "Chưa làm lệnh";
                  break;
                default:
                  break;
              }
              switch (shipmentDetail.doStatus) {
                case "N":
                  doStatus = "Chưa nhận DO gốc";
                  break;
                default:
                  break;
              }
              switch (shipmentDetail.status) {
                case 1:
                  status = "Chưa nhận công";
                  break;
                default:
                  break;
              }
              hot.setDataAtCell(row[0], 4, shipmentDetail.opeCode); //opeCode
              hot.setDataAtCell(row[0], 5, shipmentDetail.sztp); //sztp
              hot.setDataAtCell(row[0], 6, shipmentDetail.fe); //fe
              hot.setDataAtCell(row[0], 7, shipmentDetail.consignee); //consignee
              hot.setDataAtCell(row[0], 8, shipmentDetail.sealNo); //sealNo
              hot.setDataAtCell(row[0], 9, expiredDem); //expiredem
              hot.setDataAtCell(row[0], 10, shipmentDetail.wgt); //wgt
              hot.setDataAtCell(row[0], 11, shipmentDetail.vslNm); //vslNm
              hot.setDataAtCell(row[0], 12, shipmentDetail.voyNo); //voyNo
              hot.setDataAtCell(row[0], 13, shipmentDetail.loadingPort); //loadingPort
              hot.setDataAtCell(row[0], 14, shipmentDetail.dischargePort); //dischargePort
              hot.setDataAtCell(row[0], 15, shipmentDetail.transportType); //transportType
              hot.setDataAtCell(row[0], 16, shipmentDetail.emptyDepot); //emptyDepot
              hot.setDataAtCell(row[0], 17, customStatus); //customStatus
              hot.setDataAtCell(row[0], 18, paymentStatus); //paymentStatus
              hot.setDataAtCell(row[0], 19, processStatus); //processStatus
              hot.setDataAtCell(row[0], 20, doStatus); //doStatus
              hot.setDataAtCell(row[0], 21, status); //status
              hot.setDataAtCell(row[0], 22, shipmentDetail.remark); //remark
            }
          });
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
      result.forEach(function iterate(shipmentDetail) {
        if (shipmentDetail.expiredDem != null && shipmentDetail.expiredDem != '') {
          shipmentDetail.expiredDem = shipmentDetail.expiredDem.substring(8, 10)+"/"+shipmentDetail.expiredDem.substring(5, 7)+"/"+shipmentDetail.expiredDem.substring(0, 4);
        }
        switch (shipmentDetail.customStatus) {
          case "N":
            shipmentDetail.customStatus = "Chưa thông quan";
            break;
          default:
            break;
        }
        switch (shipmentDetail.paymentStatus) {
          case "N":
            shipmentDetail.paymentStatus = "Chưa thanh toán";
            break;
          default:
            break;
        }
        switch (shipmentDetail.processStatus) {
          case "N":
            shipmentDetail.processStatus = "Chưa làm lệnh";
            break;
          default:
            break;
        }
        switch (shipmentDetail.doStatus) {
          case "N":
            shipmentDetail.doStatus = "Chưa nhận DO gốc";
            break;
          default:
            break;
        }
        switch (shipmentDetail.status) {
          case 1:
            shipmentDetail.status = "Chưa nhận công";
            break;
          default:
            break;
        }
      });
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

// function getDataSelectedFromTable() {
//   var myTableData = hot.getSourceData();
//   var errorFlg = false;
//   if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
//     hot.alter("remove_row", parseInt(myTableData.length - 1), (keepEmptyRows = false));
//   }
//   var cleanedGridData = [];
//   $.each(myTableData, function (rowKey, object) {
//     if (!hot.isEmptyRow(rowKey) && object["selected"]) {
//       cleanedGridData.push(object);
//     }
//   });
//   shipmentDetails = [];
//   $.each(cleanedGridData, function (index, object) {
//     console.log(object["expiredDem"].substring(6, 10)+"/"+object["expiredDem"].substring(3, 5)+"/"+object["expiredDem"].substring(0,2));
//     var shipmentDetail = new Object();
//     shipmentDetail.opeCode = object["opeCode"];
//     shipmentDetail.blNo = object["blNo"];
//     shipmentDetail.containerNo = object["containerNo"];
//     shipmentDetail.sztp = object["sztp"];
//     shipmentDetail.fe = object["fe"];
//     shipmentDetail.consignee = object["consignee"];
//     shipmentDetail.sealNo = object["sealNo"];
//     shipmentDetail.expiredDem = new Date().getTime();
//     shipmentDetail.wgt = object["wgt"];
//     shipmentDetail.vslNm = object["vslNm"];
//     shipmentDetail.voyNo = object["voyNo"];
//     shipmentDetail.loadingPort = object["loadingPort"];
//     shipmentDetail.dischargePort = object["dischargePort"];
//     shipmentDetail.transportType = object["transportType"];
//     shipmentDetail.emptyDepot = object["emptyDepot"];
//     shipmentDetail.customStatus = "N";
//     shipmentDetail.paymentStatus = "N";
//     shipmentDetail.processStatus = "N";
//     shipmentDetail.doStatus = "N";
//     shipmentDetail.status = 1
//     shipmentDetail.remark = object["remark"];
//     shipmentDetail.shipmentId = shipmentSelected;
//     shipmentDetails.push(shipmentDetail);
//   });
//   // Get result in "selectedList" variable
//   if (shipmentDetails.length == 0) {
//     $.modal.alert("Bạn chưa chọn container.");
//     errorFlg = true;
//   }

//   if (errorFlg) {
//     return false;
//   }
// }

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
    if (object["blNo"] == null || object["blNo"] == "") {
      $.modal.alertError("Hàng " + (index+1) + ": Số bill không được trống!");
      errorFlg = true;
    } else if(object["containerNo"] == null || object["containerNo"] == "") {
      $.modal.alertError("Hàng " + (index+1) + ": Số container không được trống!");
      errorFlg = true;
    } else {
      shipmentDetail.blNo = object["blNo"];
      shipmentDetail.containerNo = object["containerNo"];
      shipmentDetail.opeCode = object["opeCode"];
      shipmentDetail.sztp = object["sztp"];
      shipmentDetail.fe = object["fe"];
      shipmentDetail.consignee = object["consignee"];
      shipmentDetail.sealNo = object["sealNo"];
      shipmentDetail.expiredDem = new Date(object["expiredDem"].substring(6, 10)+"/"+object["expiredDem"].substring(3, 5)+"/"+object["expiredDem"].substring(0,2)).getTime();
      shipmentDetail.wgt = object["wgt"];
      shipmentDetail.vslNm = object["vslNm"];
      shipmentDetail.voyNo = object["voyNo"];
      shipmentDetail.loadingPort = object["loadingPort"];
      shipmentDetail.dischargePort = object["dischargePort"];
      shipmentDetail.transportType = object["transportType"];
      shipmentDetail.emptyDepot = object["emptyDepot"];
      shipmentDetail.customStatus = "N";
      shipmentDetail.paymentStatus = "N";
      shipmentDetail.processStatus = "N";
      shipmentDetail.doStatus = "N";
      shipmentDetail.status = 1
      shipmentDetail.remark = object["remark"];
      shipmentDetail.shipmentId = shipmentSelected;
      shipmentDetail.id = object["id"];
      shipmentDetails.push(shipmentDetail);
    }
  });
  // Get result in "selectedList" variable
  if (shipmentDetails.length == 0) {
    $.modal.alert("Bạn chưa nhập thông tin.");
    errorFlg = true;
  }

  if (errorFlg) {
    return false;
  } else {
    return true;
  }
}

function saveShipmentDetail() {
  if (shipmentSelected == 0) {
    $.modal.msgError("Bạn cần chọn lô trước");
    return;
  } else {
    if (getDataFromTable() && shipmentDetails.length > 0) {
      $.ajax({
        url: prefix + "/saveShipmentDetail",
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

