const PREFIX = ctx + "support-request/reefer";
var dogrid = document.getElementById("container-grid"), hot;
var checkList = [];
const objectPaymentList = ["Chủ hàng trả trước", "Chủ hàng trả sau", "Hãng tàu"];
const PAYER_TYPE = {
  customer: 'Customer',
  carriers: 'Carriers'
}
const PAY_TYPE = {
  credit: 'Credit',
  cash: 'Cash'
}

$(document).ready(function () {
  initSelection();
  initTabReefer();
  initDateTime();
});
$("#datetimepickerSet").datetimepicker({
  format: 'dd/mm/yyyy hh:ii',
  language: "vi_VN",
  //minView: "month",
  autoclose: true,
  minuteStep: 30,
  todayBtn: true,
  endDate : new Date(shipmentDetail.powerDrawDate)
});

function initSelection() {
  let data = '';
  if (reeferInfos[0].payerType == PAYER_TYPE.carriers) {
    data += 'Hãng tàu';
  } else {
    data += 'Chủ hàng';
    if (reeferInfos[0].payType == PAY_TYPE.credit) {
      data += ' trả trước';
    } else {
      data += ' trả sau';
    }
  }

  for (let i = 0; i < objectPaymentList.length; ++i) {
    $("#slPaymentInformation").append(new Option(objectPaymentList[i], objectPaymentList[i]));
    $('#slPaymentInformation option').each(function () {
      if ($(this).val() == data) {
        $(this).prop("selected", true);
      }
    });
  }
}
function initDateTime() {
  let dayDrop = new Date(shipmentDetail.powerDrawDate);
  let daySetup = new Date(shipmentDetail.daySetupTemperature);

  if (daySetup.getFullYear() != 1970) {
    $("#daySetupTemperature").val(formatDate(daySetup));
  } else {
    $("#daySetupTemperature").val(null);
  }
  $("#powerDrawDate").val(formatDate(dayDrop));

}

function initTabReefer() {
  if (shipmentDetail.sztp.includes("R")) {
    $('#reeferContainer').css('display', 'block');
    $("#tab-1").prop('checked', true);
  } else {
    $('#reeferContainer').css('display', 'none');
    $('.tab-label-1').css('display', 'none');
  }

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
  $('#humidity').val(shipmentDetail.humidity);
  $('#ventilation').val(shipmentDetail.ventilation);

  $('#numberHours').val(getBetweenTwoDateInSourceData());
  $('#moneyNumber').val(getCountNumber());

  initFileReefer();
}

function initTabOversize() {
  if (oversizeTop || oversizeRight || oversizeLeft || oversizeFront || oversizeBack) {
    $('#oversizeContainer').css('display', 'block');
    $("#tab-2").prop('checked', true);
  } else {
    $('#oversizeContainer').css('display', 'none');
    $('.tab-label-2').css('display', 'none');
  }

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

// confirm
function insertCont() {
  let validate = true;
  let valuePaymentInformation = $('#slPaymentInformation').val();
  let payType = getPayType(valuePaymentInformation);
  let payerType = getPayerType(valuePaymentInformation);
  let dateSetup = new Date(formatDateToServer($("#daySetupTemperature").val())).getTime();
  let dateDrop = new Date(formatDateToServer($("#powerDrawDate").val())).getTime();
  //validate
  if (dateSetup > dateDrop) {
    validate = false;
    $.modal.alertError("Ngày gia hạn tiếp theo không thể nhỏ hơn ngày rút điện hiện tại.");
  }
  // chỉ update reeferInfos đầu tiên
  if (validate) {
    const payload = {
      id: reeferInfos[0].id,
      payType: payType,
      payerType: payerType,
      dateSetPower: dateSetup,
      shipmentDetailId: reeferInfos[0].shipmentDetailId
    }

    $.ajax(
      {
        url: PREFIX + "/save-reefer",
        method: "post",
        contentType: "application/json",
        accept: "text/plain",
        data: JSON.stringify(payload),
        dataType: "text",
        success: function (result) {
          parent.submitFromDetailModal();
          $.modal.close();
        }
      });
  }

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

function getDifferenceBetween(date1, date2) {
  const diffTime = Math.abs(date2 - date1);
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60));
  return diffDays;
}

function formatDate(data) {
  let date = new Date(data);
  const month = date.getMonth() == 12 ? '00' : date.getMonth() + 1;
  return `${getTwoDigitFormat(date.getDate())}/${getTwoDigitFormat(month)}/${date.getFullYear()} ${getTwoDigitFormat(date.getHours())}:${getTwoDigitFormat(date.getMinutes())}`
}

function formatDateToServer(data) {
  let dateArr = data.split("/");
  return `${dateArr[1]}/${dateArr[0]}/${dateArr[2]}`;
}
function numberWithCommas(x) {
  return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function numberNotWithCommas(x) {
  return parseInt(x.split(",").join(""));
}

function getBetweenTwoDate(date1, date2) {
  const diffTime = Math.abs(date2 - date1);
  return Math.ceil(diffTime / (1000 * 60 * 60));
}

function getBetweenTwoDateInSourceData() {
  let result = '';
  if (shipmentDetail && shipmentDetail.daySetupTemperature && shipmentDetail.powerDrawDate) {
    result = getBetweenTwoDate(new Date(shipmentDetail.powerDrawDate), new Date(shipmentDetail.daySetupTemperature));
  }
  return result;
}

function numberWithCommas(x) {
  return x.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
}

function getCountNumber() {
  let billNumber = 0;
  for (let i = 0; i < billPowers.length; ++i) {
    if (shipmentDetail.sztp.substring(0, 2) == billPowers[i].dictLabel) {
      billNumber = billPowers[i].dictValue;
      break;
    }
  }
  return data = numberWithCommas(billNumber * getBetweenTwoDateInSourceData());
}

function getTwoDigitFormat(data) {
  return ("0" + data).slice(-2);
}

function getPayType(data) {
  if (data == objectPaymentList[2]) {
    // hãng tàu thanh toán
    return null;
  } else if (data == objectPaymentList[0]) {
    // là chủ hàng trả rước
    return "Credit";
  } else {
    // là chủ hàng trả sau
    return "Cash"
  }
}

function getPayerType(data) {
  if (data == objectPaymentList[2]) {
    // hãng tàu thanh toán
    return PAYER_TYPE.carriers;
  } else {
    return PAYER_TYPE.customer;
  }
}