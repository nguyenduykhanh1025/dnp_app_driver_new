const PREFIX = ctx + "reefer-group/extend-draw-date";
var dogrid = document.getElementById("container-grid"), hot;
var checkList = [];
var sourceData = reeferInfos;
const PAYMENT_STATUS = {
  success: "S",
  process: "P"
}
const PAYER_TYPE = {
  customer: 'Customer',
  carriers: 'Carriers'
}

const PAY_TYPE = {
  credit: 'Credit',
  cash: 'Cash'
}
const objectPaymentList = ["Hãng tàu", "Chủ hàng trả trước", "Chủ hàng trả sau"];

$(document).ready(function () {
  initElement();
  initTabReefer();
  initTabOversize();
  initTableAddRegisterTime();
});

function initElement() {
  if (shipmentDetail.sztp.includes("R")) {
    $('#reeferContainer').css('display', 'block');
    $("#tab-1").prop('checked', true);
  } else if (oversizeTop || oversizeRight || oversizeLeft || oversizeFront || oversizeBack) {
    $('#oversizeContainer').css('display', 'block');
    $("#tab-2").prop('checked', true);
  }

}

function initTabReefer() {

  $('#containerNo').val(containerNo);// container no
  $('#sztp').val(sztp);//  
  $('#abc').val(shipmentDetail.id);
  $('#numberContainer').val(shipmentDetail.containerNo);
  $('#sizeCont').val(shipmentDetail.sztp);// kich thuoc
  $('#expiredDem').val(shipmentDetail.expiredDem); // han lenh
  $('#consignee').val(shipmentDetail.consignee); // Chu hang
  $('#sealNo').val(shipmentDetail.sealNo); // sealNo Lạnh
  $('#sealNoQK').val(shipmentDetail.sealNo); // sealNo Quá khổ
  $('#sealNoNH').val(shipmentDetail.sealNo); // sealNo Nguy hiểm
  $('#temperature').val(shipmentDetail.temperature);// nhiệt độ 
  $('#temperature').val(shipmentDetail.humidity);
  $('#temperature').val(shipmentDetail.ventilation);

  if (shipment.edoFlg != "0") {
    $("#file-container").css("display", "none");
  }

  initFileReefer();
}

function initFileReefer() {
  if (shipmentFiles != null) {// hiển thị hình ảnh
    let htmlInit = '';
    shipmentFiles.forEach(function (element, index) {
      shipmentFiles.push(element.id);
      if (element.fileType == "R" || element.fileType == "r") {
        htmlInit = `<div class="preview-block" style="float: left;">
                <a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" /></a>
            </div>`;
        $('.preview-container-reefer').append(htmlInit);
      }
    });
  }
}
function initTabOversize() {


  $('#wgt').val(shipmentDetail.wgt); // trọng lượng
  $('#wgtQK').val(shipmentDetail.wgt); // trọng lượng quá khổ 
  $('#wgtNH').val(shipmentDetail.wgt); // trọng lượng Nguy hiểm  

  $('#oversizeTop').val(shipmentDetail.oversizeTop);//  
  $('#oversizeRight').val(shipmentDetail.oversizeRight);//  
  $('#oversizeLeft').val(shipmentDetail.oversizeLeft);//  
  $('#oversizeFront').val(shipmentDetail.oversizeFront);//  
  $('#oversizeBack').val(shipmentDetail.oversizeBack);//   


  // cont quá khổ
  $('#osHeight').val(shipmentDetail.osHeight);// 
  $('#osPort').val(shipmentDetail.osPort);//  
  $('#osStbd').val(shipmentDetail.osStbd);// 
  $('#ovAft').val(shipmentDetail.ovAft);// 
  $('#ovFore').val(shipmentDetail.ovFore);// 
  $('#ovHeight').val(shipmentDetail.ovHeight);//
  $('#ovPort').val(shipmentDetail.ovPort);// 
  $('#ovStbd').val(shipmentDetail.ovStbd);//  

  $('#chassisNo').val(shipmentDetail.chassisNo);// 
  $('#truckNo').val(shipmentDetail.truckNo);// 
}

function initTableAddRegisterTime() {
  configHandson();
  hot = new Handsontable(dogrid, config);
  hot.loadData(sourceData);
}
function initTabDangerous() {
  $('#dangerousContainer').css('display', 'block !important');
  // cont nguy hiểm 
  $('#dangerousImo').val(shipmentDetail.dangerousImo);// 
  $('#dangerousUnno').val(shipmentDetail.dangerousUnno);//  
  $('#dangerous').val(shipmentDetail.dangerous);// 
  $("#form-detail-add").validate({
    onkeyup: false,
    focusCleanup: true,
  });
}

$("#datetimepicker1").datetimepicker({
  format: 'dd/mm/yyyy hh:ii',
  language: "vi_VN",
  //minView: "month",
  autoclose: true,
  minuteStep: 30,
  todayBtn: true,
  startDate: new Date()
});

$("#datetimepicker2").datetimepicker({
  format: 'dd/mm/yyyy hh:ii',
  language: "vi_VN",
  //minView: "month",
  autoclose: true,
  minuteStep: 30,
  todayBtn: true,
  startDate: new Date()
});

//$("#datetimepicker1").datetimepicker('getDate').getTime();


let daySetup = new Date(shipmentDetail.powerDrawDate);
$("#datetimepicker1").datetimepicker('setDate', daySetup);

var dangerousIMO = shipmentDetail.dangerousImo;
var dangerousUNNO = shipmentDetail.dangerousUnno;
var contsztp = shipmentDetail.sztp;
var cont = contsztp.substring(2, 3);// lấy mã cont cắt ra 
var oversizeTop = shipmentDetail.oversizeTop;
var oversizeRight = shipmentDetail.oversizeRight;
var oversizeLeft = shipmentDetail.oversizeLeft;
var oversizeFront = shipmentDetail.oversizeFront;
var oversizeBack = shipmentDetail.oversizeBack;
var frozenStatus = shipmentDetail.frozenStatus;
var oversize = shipmentDetail.oversize;
var dangerous = shipmentDetail.dangerous;


let contD = true;// nguy hiem 
let contR = true;// lanh
let contO = true;// qua kho  
let typeD = true;// nguy hiem 
let typeR = true;// lanh
let typeO = true;// qua kho

function getPayType(data) {
  if (data.paymentTypeRenderer == objectPaymentList[0]) {
    // hãng tàu thanh toán
    return null;
  } else if (data.paymentTypeRenderer == objectPaymentList[1]) {
    // là chủ hàng trả rước
    return "Credit";
  } else {
    // là chủ hàng trả sau
    return "Cash"
  }
}

function getPayerType(data) {
  if (data.paymentTypeRenderer == objectPaymentList[0]) {
    // hãng tàu thanh toán
    return PAYER_TYPE.carriers;
  } else {
    return PAYER_TYPE.customer;
  }
}
// confirm
function insertCont() {
  const payload = sourceData.map(item => {
    if (item.paymentTypeRenderer) {
      return {
        id: item.id,
        payType: getPayType(item),
        payerType: getPayerType(item)
      }
    }
  })
  $.ajax(
    {
      url: PREFIX + "/save-reefer-info",
      method: "post",
      contentType: "application/json",
      accept: "text/plain",
      data: JSON.stringify(payload),
      dataType: "text",
      success: function (result) {
        $.modal.close();
      }
    });
}



function confirm() {
  insertCont();
}


function saveFile() {
  $.ajax(
    {
      url: prefix + "/saveFileImage",
      method: "POST",
      data: {
        filePaths: shipmentFilePath,
        shipmentDetailId: shipmentDetail.id,
        shipmentId: shipmentDetail.shipmentId,
        shipmentSztp: shipmentDetail.sztp,
        fileType: fileType
      },
      success: function (result) {
        if (result.code == 0) {
          //$.modal.alertError(result.msg);
          //$.modal.close(); 
          insertCont();
        } else {
          $.modal.close();
        }
      }
    });
}

function closeForm() {
  $.modal.close();
}

$(document).ready(function () {
  if (shipmentFiles != null) {// hiển thị hình ảnh
    let htmlInit = '';
    shipmentFiles.forEach(function (element, index) {
      shipmentFiles.push(element.id);
      if (element.fileType == "R" || element.fileType == "r") {
        htmlInit = `<div class="preview-block">
                <a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" /></a>
                <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)" >  
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
        $('.preview-containerL').append(htmlInit);
      }
      if (element.fileType == "D" || element.fileType == "d") {
        htmlInit = `<div class="preview-block">
        		<a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" /></a>
                <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)" >
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
        $('.preview-containerNH').append(htmlInit);
      }
      if (element.fileType == "O" || element.fileType == "o") {
        htmlInit = `<div class="preview-block">
        		<a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" /></a>
                <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)" >
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
        $('.preview-containerQK').append(htmlInit);
      }
    });
  }
});

// xóa khi đã lưu có id 
function removeImage(element, fileIndex) {
  shipmentFiles.forEach(function (value, index) {
    if (value == fileIndex) {
      $.ajax({
        url: prefix + "/delete_file",
        method: "DELETE",
        data: {
          id: value
        },
        beforeSend: function () {
          $.modal.loading("Đang xử lý, vui lòng chờ...");
        },
        success: function (result) {
          $.modal.closeLoading();
          if (result.code == 0) {
            $.modal.msgSuccess("Xóa tệp thành công.");
            $(element).parent("div.preview-block").remove();
            shipmentFileIds.splice(index, 1);
          } else {
            $.modal.alertWarning("Xóa tệp thất bại.");
          }
        }
      });
      return false;
    }
  });

}


function configHandson() {
  config = {
    stretchH: "all",
    height: 200,
    minRows: 1,
    maxRows: 100,
    width: "100%",
    minSpareRows: 0,
    rowHeights: 30,
    trimDropdown: false,
    manualColumnResize: true,
    manualRowResize: true,
    renderAllRows: true,
    rowHeaders: true,
    className: "htMiddle",
    colHeaders: function (col) {
      switch (col) {
        case 0:
          return "Trạng Thái";
        case 1:
          return "Ngày Gia Hạn Cắm Điện";
        case 2:
          return "Ngày Gia Hạn Rút Điện";
        case 3:
          return "Số Giờ (h)";
        case 4:
          return "Thành Tiền (vnd)";
        case 5:
          return "Đối Tượng Thanh Toán";

      }
    },
    colWidths: [60, 100, 100, 60, 90, 140],
    columns: [

      {
        data: "status",
        readOnly: true,
        renderer: statusIconRenderer
      },
      {
        data: "dateSetPower",
        renderer: dateSetPower,
        readOnly: true
      },
      {
        data: "dateGetPower",
        renderer: dateGetPower,
        readOnly: true
      },
      {
        data: "hourNumber",
        renderer: numberHoursRenderer,
        readOnly: true
      },
      {
        data: "moneyNumber",
        renderer: paymentRenderer,
        readOnly: true
      },
      {
        data: "paymentTypeRenderer",
        type: "autocomplete",
        source: objectPaymentList,
        renderer: paymentTypeRenderer
      },

    ],
  };

}

function statusIconRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (row == reeferInfos.length) {
    return '';
  }
  $(td).html('');
  cellProperties.readOnly = 'true';
  $(td).attr('id', 'statusIcon' + row).addClass("htCenter").addClass("htMiddle");
  let status = "";
  // if (row == 0) {
  if (value == "P") {
    status = '<i id="status" class="fa fa-check easyui-tooltip" title="Container đang chờ xét duyệt yêu cầu gia hạn rút điện" aria-hidden="true" style="color: #f8ac59;"></i>';
  } else if (value === "S") {
    status = '<i id="status" class="fa fa-check easyui-tooltip" title="Container đã được xác nhận gia hạn rút điện" aria-hidden="true" style="color: #1ab394;"></i>';
  } else if (value === "E") {
    status = '<i id="status" class="fa fa-check easyui-tooltip" title="Container đã bị từ chối gia hạn rút điện" aria-hidden="true" style="color: #ef6776;"></i>';
  }
  // } else {
  //   // status
  //   status = '<i id="status" class="fa fa-clock-ofa-flip-horizontal easyui-tooltip" title="Container đã được xét duyệt yêu cầu gia hạn rút điện" aria-hidden="true" style="color: #1ab394;"></i>';
  //   if (value && value == 'L') {
  //     status = '<i id="finish" class="fa fa-clock-o fa-flip-horizontal easyui-tooltip" title="Container đã bị khóa không thể chỉnh sửa" aria-hidden="true" style="color: #f8ac59;"></i>';
  //   }
  // }

  // Return the content
  let content = '<div style="font-size: 25px">' + status + '</div>';
  if (sourceData && sourceData.length >= (row + 1) && sourceData[row].id) {
    $(td).html(content);
  }
  value = '';
  return td;
}

function dateSetPower(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    return '';
  }
  const dateResult = new Date(value);
  const month = dateResult.getMonth() == 12 ? '00' : dateResult.getMonth() + 1;
  const result = `${getTwoDigitFormat(dateResult.getDate())}/${getTwoDigitFormat(month)}/${dateResult.getFullYear()} ${getTwoDigitFormat(dateResult.getHours())}:${getTwoDigitFormat(dateResult.getMinutes())}`;
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: center;text-align: center;">' + result + '</div>');
  return td;
}

function dateGetPower(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    return '';
  }
  const dateResult = new Date(value);
  const month = dateResult.getMonth() == 12 ? '00' : dateResult.getMonth() + 1;
  const result = `${getTwoDigitFormat(dateResult.getDate())}/${getTwoDigitFormat(month)}/${dateResult.getFullYear()} ${getTwoDigitFormat(dateResult.getHours())}:${getTwoDigitFormat(dateResult.getMinutes())}`;
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: center;text-align: center;">' + result + '</div>');
  return td;
}
function numberHoursRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = '';
  }
  let result = getBetweenTwoDateInSourceData(row);
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: center;text-align: center;">' + result + '</div>');
  return td;
}

function paymentRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    value = '';
  }
  let billNumber = 0;
  for (let i = 0; i < billPowers.length; ++i) {
    if (contsztp.substring(0, 2) == billPowers[i].dictLabel) {
      billNumber = billPowers[i].dictValue;
      break;
    }
  }
  const data = numberWithCommas(billNumber * getBetweenTwoDateInSourceData(row));
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: center;text-align: center;">' + data + '</div>');
  return td;
}
function paymentTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'consignee' + row).addClass("htMiddle");

  if (!value) {
    value = '';
    if (!sourceData[row]) {
      return;
    }
    if (sourceData[row].payerType == PAYER_TYPE.carriers) {
      value += 'Hãng tàu';
    } else {
      value += 'Chủ hàng';
      if (sourceData[row].payType == PAY_TYPE.credit) {
        value += ' trả trước';
      } else {
        value += ' trả sau';
      }
    }
  } else {
    if (value != null && value != '') {
      if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
        cellProperties.readOnly = 'true';
        $(td).css("background-color", "rgb(232, 232, 232)");
      }
    }
    if (!value) {
      value = '';
    }
  }
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
  return td;

}

function extendPowerDrawDate() {

  let date = $('#extendPowerDrawDate').val();
  let arrDate = date.split("/");
  const tempDate = arrDate[1];
  arrDate[1] = arrDate[0];
  arrDate[0] = tempDate;

  const detail = {
    id: shipmentDetail.id,
    powerDrawDate: new Date(arrDate.join("/")).getTime()
  }
  $.ajax({
    url: prefix + "/extendPowerDrawDate",
    method: "POST",
    contentType: "application/json",
    accept: 'text/plain',
    data: JSON.stringify(detail),
    dataType: 'text',
    success: function (data) {
      data = JSON.parse(data);
      shipmentDetail.powerDrawDateStatus = "P";
      sourceData = data.data.map(item => {
        return {
          id: item.id,
          registerDate: item.newValue
        }
      })

      configHandson();
      hot = new Handsontable(dogrid, config);
      hot.loadData(sourceData);

      $.modal.alertSuccess("Gia hạn ngày rút điện thành công.");
    },
    error: function (result) {
      $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, xin vui lòng thử lại.");
      $.modal.closeLoading();
    },
  });
}

function getBetweenTwoDate(date1, date2) {
  const diffTime = Math.abs(date2 - date1);
  return (diffTime / (1000 * 60 * 60)).toFixed(1);
}

function getBetweenTwoDateInSourceData(row) {
  let result = '';
  if (sourceData[row] && sourceData[row].dateGetPower && sourceData[row].dateSetPower) {
    result = getBetweenTwoDate(new Date(sourceData[row].dateSetPower), new Date(sourceData[row].dateGetPower));
  }
  return result;
}

function numberWithCommas(x) {
  return x.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
}

function isBookingCheckPayment() {
  let result = false;
  for (let i = 0; i < oprlistBookingCheck.length; ++i) {
    if (shipmentDetail.opeCode == oprlistBookingCheck[i].dictValue) {
      result = true;
      break;
    }
  }
  return result;
}

function extendPowerDrawDateInterrupted() {
  $.modal.openCustomForm("Thêm thông tin gia hạn ngắt quãng", PREFIX + "/shipment-detail/" + shipmentDetailId + "/extend-power-interrupted", 600, 300);
}

function submitDataFromExtendPowerInterruptedModal(data) {
  data = JSON.parse(data);
  sourceData = data.data;
  configHandson();
  hot = new Handsontable(dogrid, config);
  hot.loadData(sourceData);

  $.modal.alertSuccess("Thêm gia hạn ngày rút điện thành công.");
}

function getTwoDigitFormat(data) {
  return ("0" + data).slice(-2);
}