const PREFIX = ctx + "support-request/reefer";
var dogrid = document.getElementById("container-grid"), hot;
var checkList = [];
$(document).ready(function () {
  initTabReefer();
  initDateTime();
});
console.log(billPowers);
function initDateTime() {
  let dayDrop = new Date(shipmentDetail.powerDrawDate);
  let daySetup = new Date(shipmentDetail.daySetupTemperature);


  $("#powerDrawDate").val(formatDate(dayDrop));
  $("#daySetupTemperature").val(formatDate(daySetup));
}

console.log(reeferInfos);
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
  const payload = reeferInfos.map(item => {
    return {
      id: item.id,
      hourNumber: $('#numberHours').val(),
      moneyNumber: $('#moneyNumber').val(),
      shipmentDetailId: item.shipmentDetailId,
      // payType: getPayType(),
      // payerType: getPayerType()
    }
  })
  $.ajax(
    {
      url: PREFIX + "/save-reefer",
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

function getPayType() {
  const data = $('input[name="optradio"]:checked').val();
  console.log(data);
  let result = '';
  if (data == 'paymentType_1') {
    result = 'B';
  } else if (data == 'paymentType_2') {
    result = 'A';
  }
  return result;
}

function getPayerType() {
  const data = $('#optradio').val();
  let result = '';
  if (data == 'paymentType_0') {
    result = 'P';
  } else {
    result = 'Q';
  }
  return result;
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

function getDifferenceBetween(date1, date2) {
  const diffTime = Math.abs(date2 - date1);
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60));
  return diffDays;
}

function formatDate(data) {
  let date = new Date(data);
  const month = date.getMonth() == 12 ? '00' : date.getMonth() + 1;
  return `${date.getDate()}/${month}/${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}`
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