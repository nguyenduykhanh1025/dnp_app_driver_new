const PREFIX = ctx + "support-request/reefer";
var dogrid = document.getElementById("container-grid"), hot;
var checkList = [];
var sourceData = powerDropDate.map(item => {
  return {
    id: item.id,
    registerDate: item.newValue
  }
})
$(document).ready(function () {
  initTabReefer();
  // initTabOversize();
  initTableAddRegisterTime();
});

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

let dayDrop = new Date(shipmentDetail.powerDrawDate);
let daySetup = new Date(shipmentDetail.daySetupTemperature);
$("#datetimepicker1").datetimepicker('setDate', dayDrop);
$("#datetimepicker2").datetimepicker('setDate', daySetup);

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
  $("#numberHours").val(getDifferenceBetween(dayDrop, daySetup))
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
  let date = $("#datetimepicker1").datetimepicker('getDate').getTime();
  let shipmentDetailId = shipmentDetail.id;
  let truckNo = $("#truckNo").val();
  let chassisNo = $("#chassisNo").val();
  const detail = {
    id: shipmentDetail.id,
    shipmentId: shipmentDetail.shipmentId,
    sztp: shipmentDetail.sztp,
    powerDrawDate: date,
    truckNo: truckNo,
    chassisNo: chassisNo
  }
  $.ajax(
    {
      url: prefix + "/saveDate",
      method: "post",
      contentType: "application/json",
      accept: "text/plain",
      data: JSON.stringify(detail),
      dataType: "text",
      success: function (result) {
        if (result.code == 0) {
          //$.modal.alertError(result.msg);
          $.modal.close();
          //insertFile();  
        } else {
          $.modal.close();
        }
      }
    });
}



function confirm() {
  var lengthTemp = shipmentFilePath;
  if (lengthTemp == null || lengthTemp.length == 0) {
    insertCont();
  }
  if (lengthTemp != null || lengthTemp.length != 0) {// nếu có thì vào đây
    saveFile();
  }
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
    height: $(document).height() - 140,
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
          return "Ngày gia hạn rút điện";

      }
    },
    colWidths: [80, 200],
    columns: [

      {
        data: "status",
        readOnly: true,
        renderer: statusIconRenderer
      },
      {
        data: "registerDate",
        type: "date",
        dateFormat: "DD/MM/YYYY",
        correctFormat: true,
        defaultDate: new Date(),
        renderer: registerDateRenderer
      },
    ],
  };

}

function statusIconRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).html('');
  cellProperties.readOnly = 'true';
  $(td).attr('id', 'statusIcon' + row).addClass("htCenter").addClass("htMiddle");
  let status = "";
  if (row == 0) {
    if (shipmentDetail.powerDrawDateStatus == "P") {
      status = '<i id="status" class="fa fa-clock-o fa-flip-horizontal easyui-tooltip" title="Container đang chờ xét duyệt yêu cầu gia hạn rút điện" aria-hidden="true" style="color: #f8ac59;"></i>';
    } else if (shipmentDetail.powerDrawDateStatus === "S") {
      status = '<i id="status" class="fa fa-clock-o fa-flip-horizontal easyui-tooltip" title="Container đã được xác nhận gia hạn rút điện" aria-hidden="true" style="color: #1ab394;"></i>';
    }
  } else {
    // status
    status = '<i id="status" class="fa fa-clock-ofa-flip-horizontal easyui-tooltip" title="Container đã được xét duyệt yêu cầu gia hạn rút điện" aria-hidden="true" style="color: #1ab394;"></i>';
    if (value && value == 'L') {
      status = '<i id="finish" class="fa fa-clock-o fa-flip-horizontal easyui-tooltip" title="Container đã bị khóa không thể chỉnh sửa" aria-hidden="true" style="color: #f8ac59;"></i>';
    }
  }

  // Return the content
  let content = '<div style="font-size: 25px">' + status + '</div>';
  if (sourceData && sourceData.length >= (row + 1) && sourceData[row].id) {
    $(td).html(content);
  }
  value = '';
  return td;
}
function registerDateRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'dateReceipt' + row).addClass("htMiddle").addClass("htCenter");
  if (value != null && value != '') {
    let dateArray = value.split(" ");
    let resultDate = new Date(dateArray[0]);
    if (dateArray[2] === "PM") {
      resultDate.setHours(parseInt(dateArray[1].split(".")[0]) + 12);

    }
    else {
      resultDate.setHours(parseInt(dateArray[1].split(".")[0]));
    }
    resultDate.setMinutes(dateArray[1].split(".")[1]);
    let month = resultDate.getMonth() == 12 ? 0 : resultDate.getMonth() + 1;
    value = `${resultDate.getDate()}/${month}/${resultDate.getFullYear()} ${resultDate.getHours()}:${resultDate.getMinutes()}`;
  } else {
    value = '';
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

function getDifferenceBetween(date1, date2) {
  const diffTime = Math.abs(date2 - date1);
  console.log(diffTime);
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60));
  return diffDays;
}