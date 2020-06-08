var isChange = true;
var prefix = ctx + "logistic/receiveContFull";
var fromDate = "";
var toDate = "";
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected;
var shipmentDetails;
var shipmentDetailIds;
var originStatus;
var billNo;
var simpleCustom;
var contList = [];
var checked = false;
var allChecked = true;
var isIterate = false;
$(document).ready(function () {
  // $(".colHeader > input[type=checkbox]").click(function() {
  //   console.log("inini")
  // })
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
          $("#dg").datagrid("hideColumn", "id");
          $("#dg").datagrid("hideColumn", "edoFlg");
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
  $(".left-side").css("width", "33%");
  $(".left-side").children().show();
  $("#btn-collapse").show();
  $("#btn-uncollapse").hide();
  $(".right-side").css("width", "67%");
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
  colHeaders: function (col) {
    switch (col) {
      case 0:
        return "id";
      case 1:
        var txt = "<input type='checkbox' class='checker' ";
        txt += "onclick='checkAll()' ";
        txt += ">";
        return txt;
      case 2:
        return '<span>Container No</span><span style="color: red;">(*)</span>';
      case 3:
        return "T.T Hải Quan";
      case 4:
        return "T.T Thanh Toán";
      case 5:
        return "T.T Làm Lệnh";
      case 6:
        return "T.T DO Gốc";
      case 7:
        return "T.T Nhận Cont";
      case 8:
        return '<span>Chủ Khai Thác</span><span style="color: red;">(*)</span>';
      case 9:
        return "Kích Thước";
      case 10:
        return "F/E";
      case 11:
        return "Chủ hàng";
      case 12:
        return "Seal No";
      case 13:
        return "Hạn Lệnh";
      case 14:
        return "Trọng tải";
      case 15:
        return "Tàu";
      case 16:
        return "Chuyến";
      case 17:
        return "Cảng Nguồn";
      case 18:
        return "Cảng Đích";
      case 19:
        return "Phương Tiện";
      case 20:
        return "Nơi Hạ Vỏ";
      case 21:
        return "Ghi Chú";
      case 22:
        return "T.T Bốc Container Chỉ Định";
    }
  },
  colWidths: [0.01, 50, 100, 150, 150, 150, 150, 150, 120, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 200],
  filter: "true",
  columns: [
    {
      data: "id",
      editor: false
    },
    {
      data: "active",
      type: "checkbox",
      className: "htCenter",
    },
    {
      data: "containerNo",
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
      editor: false,
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
      data: "remark",
    },
  ],
  afterChange: function (changes, src) {
    //Get data change in cell to render another column
    if (src !== "loadData") {
      var verifyStatus = false;
      var paymentStatus = false;
      var notVerify = false;
      changes.forEach(function interate(row) {
        var containerNo;
        if (row[1] == "active" && !isIterate) {
          getDataSelectedFromTable(false, false);
          if (allChecked) {
            $(".checker").prop("checked", true);
            checked = true;
          } else {
            $(".checker").prop("checked", false);
            checked = false;
          }
          if (shipmentDetails.length > 0) {
            var status = 2;
            for (var i=0; i<shipmentDetails.length; i++) {
              if (shipmentDetails[i].customStatus == "Chưa thông quan") {
                status = 1;
                break;
              } else if (shipmentDetails[i].paymentStatus == "Đã thanh toán") {
                if (verifyStatus || notVerify) {
                  status = 1;
                } else {
                  status = 4;
                  paymentStatus = true;
                }
              } else if (shipmentDetails[i].processStatus == "Đã làm lệnh") {
                if (paymentStatus || notVerify) {
                  status = 1;
                } else {
                  status = 3;
                  verifyStatus = true;
                }
              } else {
                if (verifyStatus || paymentStatus) {
                  status = 1;
                } else {
                  status = 2;
                }
                notVerify = true;
              }
            }
            switch (status) {
              case 1:
                setLayoutCustomStatus(simpleCustom);
                break;
              case 2:
                setLayoutVerifyUser();
                break;
              case 3:
                setLayoutPaymentStatus();
                break;
              case 4:
                setLayoutPickTruck();
                break;
            }
          } else {
            switch (originStatus) {
              case 1:
                setLayoutRegisterStatus();
                break;
              case 2:
                setLayoutCustomStatus(simpleCustom);
                break;
            }
          }
        }
        if (row[1] == "containerNo") {
          containerNo = hot.getDataAtRow(row[0])[2];
          isChange = true;
        } else {
          isChange = false;
        }
        if (containerNo != null && isChange && shipmentSelected.edoFlg == "0" && /[A-Z]{4}[0-9]{7}/g.test(containerNo)) {
          // Call data to auto-fill
          $.ajax({
            url: prefix + "/getContInfo",
            type: "post",
            data: {
              containerNo: containerNo,
              blNo: shipmentSelected.blNo
            }
          }).done(function (shipmentDetail) {
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
                case "R":
                  customStatus = "Đã thông quan";
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
              if (shipmentDetail.status != 5) {
                status = "Chưa nhận công";
              } else {
                status = "Đã nhận công";
              }
              hot.setDataAtCell(row[0], 3, customStatus); //customStatus
              hot.setDataAtCell(row[0], 4, paymentStatus); //paymentStatus
              hot.setDataAtCell(row[0], 5, processStatus); //processStatus
              hot.setDataAtCell(row[0], 6, doStatus); //doStatus
              hot.setDataAtCell(row[0], 7, status); //status
              hot.setDataAtCell(row[0], 8, shipmentDetail.opeCode); //opeCode
              hot.setDataAtCell(row[0], 9, shipmentDetail.sztp); //sztp
              hot.setDataAtCell(row[0], 10, shipmentDetail.fe); //fe
              hot.setDataAtCell(row[0], 11, shipmentDetail.consignee); //consignee
              hot.setDataAtCell(row[0], 12, shipmentDetail.sealNo); //sealNo
              hot.setDataAtCell(row[0], 13, expiredDem); //expiredem
              hot.setDataAtCell(row[0], 14, shipmentDetail.wgt); //wgt
              hot.setDataAtCell(row[0], 15, shipmentDetail.vslNm); //vslNm
              hot.setDataAtCell(row[0], 16, shipmentDetail.voyNo); //voyNo
              hot.setDataAtCell(row[0], 17, shipmentDetail.loadingPort); //loadingPort
              hot.setDataAtCell(row[0], 18, shipmentDetail.dischargePort); //dischargePort
              hot.setDataAtCell(row[0], 19, shipmentDetail.transportType); //transportType
              hot.setDataAtCell(row[0], 20, shipmentDetail.emptyDepot); //emptyDepot
              hot.setDataAtCell(row[0], 21, shipmentDetail.remark); //remark
            }
          });
        }
      });
    }
  },
};

function checkAll() {
  getDataFromTable(false);
  isIterate = true;
  if (checked) {
    for (var i=0; i<shipmentDetails.length; i++) {
      hot.setDataAtCell(i, 1, false);
      if (i == shipmentDetails.length-2) {
        isIterate = false;
      }
    }
    $(".checker").prop("checked", false);
    checked = false;
  } else {
    for (var i=0; i<shipmentDetails.length; i++) {
      hot.setDataAtCell(i, 1, true);
      if (i == shipmentDetails.length-2) {
        isIterate = false;
      }
    }
    $(".checker").prop("checked", true);
    checked = true;
  }
}

hot = new Handsontable(dogrid, config);

function loadShipmentDetail(id) {
  $.ajax({
    url: prefix + "/listShipmentDetail",
    method: "GET",
    data: {
      shipmentId: id
    },
    success: function (result) {
      var saved = true;
      var customStatus = true;
      simpleCustom = false;
      result.forEach(function iterate(shipmentDetail) {
        if (shipmentDetail.id == null) {
          saved = false;
          setLayoutRegisterStatus();
          originStatus = 1;
        } else {
          if (shipmentDetail.status < 2) {
            customStatus = false;
          } else {
            simpleCustom = true;
          }
          setLayoutCustomStatus(simpleCustom);
          originStatus = 2;
        }
        if (shipmentDetail.expiredDem != null && shipmentDetail.expiredDem != '') {
          shipmentDetail.expiredDem = shipmentDetail.expiredDem.substring(8, 10) + "/" + shipmentDetail.expiredDem.substring(5, 7) + "/" + shipmentDetail.expiredDem.substring(0, 4);
        }
        switch (shipmentDetail.customStatus) {
          case "N":
            shipmentDetail.customStatus = "Chưa thông quan";
            break;
          case "R":
            shipmentDetail.customStatus = "Đã thông quan";
            break;
          default:
            break;
        }
        switch (shipmentDetail.paymentStatus) {
          case "N":
            shipmentDetail.paymentStatus = "Chưa thanh toán";
            break;
          case "Y":
            shipmentDetail.paymentStatus = "Đã thanh toán";
            break;
          default:
            break;
        }
        switch (shipmentDetail.processStatus) {
          case "N":
            shipmentDetail.processStatus = "Chưa làm lệnh";
            break;
          case "Y":
            shipmentDetail.processStatus = "Đã làm lệnh";
            break;
          default:
            break;
        }
        switch (shipmentDetail.doStatus) {
          case "N":
            shipmentDetail.doStatus = "Chưa nhận DO gốc";
            break;
          case "Y":
            shipmentDetail.doStatus = "Đã nhận DO gốc";
          default:
            break;
        }
        if (shipmentDetail.status != 5) {
          shipmentDetail.status = "Chưa nhận container";
        } else {
          shipmentDetail.status = "Đã nhận container";
        }
      });
      hot.loadData(result);
      hot.render();
      if (!saved) {
        $.modal.alert("Thông tin container đã được hệ thống tự<br>động điền, quý khách vui lòng kiểm tra lại<br>thông tin và lưu khai báo.");
        setLayoutRegisterStatus();
      } else if (customStatus) {
        setLayoutVerifyUser();
        $("#customBtn").prop("disabled", true);
      }
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

function formatRemark(value) {
  return '<div class="easyui-tooltip" title="'+ ((value!=null&&value!="")?value:"không có ghi chú") +'" style="width: 80; text-align: center;"><span>'+ (value!=null?(value.substring(0, 5) + "..."):"...") +'</span></div>';
}

// Handle add
$(function () {
  var options = {
    createUrl: prefix + "/addShipmentForm",
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
    shipmentSelected = row;
    $(function () {
      var options = {
        createUrl: prefix + "/addShipmentForm",
        updateUrl: prefix + "/editShipmentForm/" + shipmentSelected.id,
        modalName: " Lô"
      };
      $.table.init(options);
    });
    $("#loCode").text(row.id);
    $("#taxCode").text(row.taxCode);
    $("#quantity").text(row.containerAmount);
    if (row.edoFlg == "0") {
      $("#dotype").text("DO giấy");
    } else {
      $("#dotype").text("EDO");
    }
    $("#blNo").text(row.blNo);
    loadShipmentDetail(row.id);
  }
}

function getDataSelectedFromTable(isValidate, isNeedPickedCont) {
  var myTableData = hot.getSourceData();
  var errorFlg = false;
  if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
    hot.alter("remove_row", parseInt(myTableData.length - 1), (keepEmptyRows = false));
  }
  var cleanedGridData = [];
  allChecked = true;
  $.each(myTableData, function (rowKey, object) {
    if (!hot.isEmptyRow(rowKey)) {
      if (object["active"]) {
        cleanedGridData.push(object);
      } else {
        allChecked = false;
        if (object["preorderPickup"] == "Y" && isNeedPickedCont) {
          cleanedGridData.push(object);
        }
      }
    } 
  });
  shipmentDetailIds = "";
  shipmentDetails = [];
  $.each(cleanedGridData, function (index, object) {
    var shipmentDetail = new Object();
    if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"]) && isValidate) {
      $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
      errorFlg = true;
    }
    shipmentDetail.blNo = object["blNo"];
    shipmentDetail.containerNo = object["containerNo"];
    shipmentDetail.customStatus = object["customStatus"];
    shipmentDetail.processStatus = object["processStatus"];
    shipmentDetail.paymentStatus = object["paymentStatus"];
    shipmentDetail.shipmentId = shipmentSelected.id;
    shipmentDetail.id = object["id"];
    shipmentDetails.push(shipmentDetail);
    shipmentDetailIds += object["id"]+",";
  });

  // Get result in "selectedList" variable
  if (shipmentDetails.length == 0 && isValidate) {
    $.modal.alert("Bạn chưa chọn container.");
    errorFlg = true;
  } else {
    shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length-1);
  }
  if (errorFlg) {
    return false;
  }
}

function getDataFromTable(isValidate) {
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
  if (cleanedGridData.length > 0) {
    billNo = cleanedGridData[0]["blNo"];
  }
  contList = [];
  $.each(cleanedGridData, function (index, object) {
    var shipmentDetail = new Object();
    if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"]) && isValidate) {
      $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
      errorFlg = true;
    }
    var expiredDem = new Date(object["expiredDem"].substring(6, 10) + "/" + object["expiredDem"].substring(3, 5) + "/" + object["expiredDem"].substring(0, 2));
    shipmentDetail.blNo = shipmentSelected.blNo;
    shipmentDetail.containerNo = object["containerNo"];
    contList.push(object["containerNo"]);
    shipmentDetail.opeCode = object["opeCode"];
    shipmentDetail.sztp = object["sztp"];
    shipmentDetail.fe = object["fe"];
    shipmentDetail.consignee = object["consignee"];
    shipmentDetail.sealNo = object["sealNo"];
    shipmentDetail.expiredDem = expiredDem.getTime();
    shipmentDetail.wgt = object["wgt"];
    shipmentDetail.vslNm = object["vslNm"];
    shipmentDetail.voyNo = object["voyNo"];
    shipmentDetail.loadingPort = object["loadingPort"];
    shipmentDetail.dischargePort = object["dischargePort"];
    shipmentDetail.transportType = object["transportType"];
    shipmentDetail.emptyDepot = object["emptyDepot"];
    shipmentDetail.remark = object["remark"];
    shipmentDetail.shipmentId = shipmentSelected.id;
    shipmentDetail.id = object["id"];
    shipmentDetails.push(shipmentDetail);
    var now = new Date();
    now.setHours(0, 0, 0);
    expiredDem.setHours(23, 59, 59);
    if (expiredDem.getTime() < now.getTime() && isValidate) {
      errorFlg = true;
      $.modal.alertError("Hàng " + (index + 1) + ": Hạn lệnh không được trong quá khứ!")
    }
  });

  if (isValidate) {
    contList.sort();
    var contTemp = "";
    $.each(contList, function (index, cont) {
      if (cont != "" && cont == contTemp) {
        $.modal.alertError("Số container không được giống nhau!");
        errorFlg = true;
        return false;
      }
      contTemp = cont;
    });
  }

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
  if (shipmentSelected == null) {
    $.modal.msgError("Bạn cần chọn lô trước");
    return;
  } else {
    if (getDataFromTable(true) && shipmentDetails.length > 0 && shipmentDetails.length <= shipmentSelected.containerAmount) {
      $.modal.loading("Đang xử lý...");
      $.ajax({
        url: prefix + "/saveShipmentDetail",
        method: "post",
        contentType: "application/json",
        accept: 'text/plain',
        data: JSON.stringify(shipmentDetails),
        dataType: 'text',
        success: function (data) {
          var result = JSON.parse(data);
          if (result.code == 0) {
            $.modal.msgSuccess(result.msg);
            loadShipmentDetail(shipmentSelected.id);
          } else {
            $.modal.msgError(result.msg);
          }
          $.modal.closeLoading();
        },
        error: function (result) {
          $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
          $.modal.closeLoading();
        },
      });
    } else if (shipmentDetails.length > shipmentSelected.containerAmount) {
      $.modal.alertError("Số container nhập vào vượt quá số container<br>của lô.");
    } else {
      $.modal.alertError("Quý khách chưa nhập thông tin chi tiết lô.");
    }
  }
}

// Handling logic
function checkCustomStatus() {
  $.modal.openCustomForm("Khai báo hải quan", prefix + "/checkCustomStatusForm/" + shipmentSelected.id, 720, 500);
}

function verify() {
  getDataSelectedFromTable(true, true);
  if (shipmentDetails.length > 0) {
    $.modal.openCustomForm("Xác nhận làm lệnh", prefix + "/checkContListBeforeVerify/" + shipmentDetailIds, 600, 500);
  } 
}

function verifyOtp(shipmentDtIds) {
  $.modal.openCustomForm("Xác thực OTP", prefix + "/verifyOtpForm/" + shipmentDtIds, 600, 350);
}

function pay() {
  $.modal.openCustomForm("Thanh toán", prefix + "/paymentForm/" + shipmentDetailIds, 700, 300);
}

function pickTruck() {
  $.modal.openFullPickTruck("Điều xe", prefix + "/pickTruckForm/" + shipmentSelected.id + "/" + false + "/" + "0");
}

function pickContOnDemand() {
  getDataFromTable(false);
  $.modal.openCustomForm("Bốc container chỉ định", prefix + "/pickContOnDemandForm/" + billNo, 710, 565);
}

function exportBill() {

}

function reloadShipmentDetail() {
  loadShipmentDetail(shipmentSelected.id);
}

// Handling UI
function setLayoutRegisterStatus() {
  $("#registerStatus").removeClass("label-primary disable").addClass("active");
  $("#customStatus").removeClass("label-primary active").addClass("disable");
  $("#verifyStatus").removeClass("label-primary active").addClass("disable");
  $("#paymentStatus").removeClass("label-primary active").addClass("disable");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
  $("#saveShipmentDetailBtn").prop("disabled", false);
  $("#customBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  $("#pickContOnDemandBtn").prop("disabled", true);
  $("#pickTruckBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
}

function setLayoutCustomStatus(simpleCustoms) {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("label-primary disable").addClass("active");
  $("#verifyStatus").removeClass("label-primary active").addClass("disable");
  $("#paymentStatus").removeClass("label-primary active").addClass("disable");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
  $("#saveShipmentDetailBtn").prop("disabled", false);
  $("#customBtn").prop("disabled", false);
  $("#verifyBtn").prop("disabled", true);
  if (simpleCustoms) {
    $("#pickContOnDemandBtn").prop("disabled", false);
  } else {
    $("#pickContOnDemandBtn").prop("disabled", true);
  }
  $("#pickTruckBtn").prop("disabled", false);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
}

function setLayoutVerifyUser() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("label-primary disable").addClass("active");
  $("#paymentStatus").removeClass("label-primary active").addClass("disable");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
  // $("#saveShipmentDetailBtn").prop("disabled", false);
  // $("#customBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", false);
  // $("#pickContOnDemandBtn").prop("disabled", false);
  // $("#pickTruckBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
}

function setLayoutPaymentStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("active disable").addClass("label-primary");
  $("#paymentStatus").removeClass("label-primary disable").addClass("active");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
  // $("#saveShipmentDetailBtn").prop("disabled", true);
  // $("#customBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  // $("#pickContOnDemandBtn").prop("disabled", true);
  // $("#pickTruckBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", false);
  $("#exportBillBtn").prop("disabled", true);
}

function setLayoutPickTruck() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("active disable").addClass("label-primary");
  $("#paymentStatus").removeClass("active disable").addClass("label-primary");
  $("#finishStatus").removeClass("label-primary disable").addClass("active");
  // $("#saveShipmentDetailBtn").prop("disabled", true);
  // $("#customBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  // $("#pickContOnDemandBtn").prop("disabled", true);
  // $("#pickTruckBtn").prop("disabled", false);
  $("#payBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", false);
}

// function setLayoutPickCont() {
//   $("#registerStatus").removeClass("active disable").addClass("label-primary");
//   $("#customStatus").removeClass("active disable").addClass("label-primary");
//   $("#verifyStatus").removeClass("active disable").addClass("label-primary");
//   $("#paymentStatus").removeClass("label-primary disable").addClass("active");
//   $("#finishStatus").removeClass("label-primary active").addClass("disable");
//   $("#saveShipmentDetailBtn").prop("disabled", true);
//   $("#customBtn").prop("disabled", true);
//   $("#verifyBtn").prop("disabled", true);
//   $("#pickContOnDemandBtn").prop("disabled", false);
//   $("#pickTruckBtn").prop("disabled", true);
//   $("#payBtn").prop("disabled", true);
//   $("#exportBillBtn").prop("disabled", true);
// }

function finishForm(result) {
  if (result.code == 0) {
    $.modal.msgSuccess(result.msg);
  } else {
    $.modal.msgError(result.msg);
  }
  reloadShipmentDetail();
}

