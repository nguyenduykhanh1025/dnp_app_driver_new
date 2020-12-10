const PREFIX = ctx + "logistic/receive-cont-full";
var dogrid = document.getElementById("container-grid"), hot;
var checkList = [];
sourceData = reeferInfos;

const PAYMENT_STATUS = {
  success: "S",
  process: "P"
}

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
  } 
  // check theo trường hợp chỉ cần P với U là hiển thị cont lên
  else if(shipmentDetail.sztp.includes("P") || shipmentDetail.sztp.includes("U")){
       $('#oversizeContainer').css('display', 'block');
       $("#tab-2").prop('checked', true);
  }
  // check theo trường hợp cụ thể. nếu có thì mới hiển thị ra
  // else if (oversizeTop || oversizeRight || oversizeLeft || oversizeFront || oversizeBack) {
  //   $('#oversizeContainer').css('display', 'block');
  //   $("#tab-2").prop('checked', true);
  // }
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
  $('#humidity').val(shipmentDetail.humidity);
  $('#ventilation').val(shipmentDetail.ventilation);

  if (!$('#powerDrawDate').val()) {
    $('#extendPowerDrawDateContainer').css('display', 'none');
    $('#tableExtendDateContainer').css('display', 'none');
  }

  console.log('ssssssssssssssssssssss', shipmentDetail.frozenStatus);
  if (shipmentDetail.frozenStatus == "S") {
    console.log('iiiiiiiiiiiisii');
    $("#powerDrawDate").attr('disabled', 'disabled');
    $("#btnPowerDrawDate").css('display', 'none');
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
  startDate: new Date(shipmentDetail.daySetupTemperature)
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
 
function confirm() {
  var truckNo = $("#truckNo").val();
  var chassisNo = $("#chassisNo").val(); 
  
  if(shipmentFiles.length <1 && fileIds.length < 1 && !shipmentDetail.sztp.includes("R")){ 
 $.modal.alertWarning("Vui lòng đính kèm file"); 
  } 
 else  
if(oversizeTop || oversizeRight || oversizeLeft && !shipmentDetail.sztp.includes("R")){
    if(truckNo == null || truckNo == ""){
      $.modal.alertWarning("Vui lòng nhập vào biển số xe đầu kéo rồi thử lại");
    }
    else if(chassisNo == null || chassisNo == "" && !shipmentDetail.sztp.includes("R")){
      $.modal.alertWarning("Vui lòng nhập vào biển số xe rơ móc rồi thử lại");
    }
    else{ 
      insertCont();
      saveFile(); 
    } 
  //saveFile();
} 
// if(oversizeTop || oversizeRight || oversizeLeft && !shipmentDetail.sztp.includes("R")){ 
//   insertCont();
//   saveFile();
// }
 
  else{
    var lengthTemp = shipmentFilePath;
     if (lengthTemp) {// nếu k có thì vào đây
       insertCont();
     }
    if (!shipmentDetail.sztp.includes("R") && (!lengthTemp)) {// nếu có thì vào đây
       saveFile();
    }

    // var lengthTemp = shipmentFilePath;
    // if (lengthTemp == null || lengthTemp.length == 0) {// nếu k có thì vào đây
    //   insertCont();
    // }
  
    // if (!shipmentDetail.sztp.includes("R") && (lengthTemp != null || lengthTemp.length != 0)) {// nếu có thì vào đây
    //   saveFile();
    // }

  }

 
}


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
 
function saveFile() {
//console.log("saveFile");
  //console.log(fileIds);
  $.ajax(
    {
      url: prefix + "/saveFileImage",
      method: "POST",
      data: {

        fileIds : fileIds,

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
          return "Hình Thức Thanh Toán";
        case 6:
          return "Action";

      }
    },
    colWidths: [60, 100, 100, 60, 90, 140, 100],
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
        data: "moneyTypeNumber",
        renderer: paymentTypeRenderer,
        readOnly: true
      },
      {
        data: "btnAction",
        renderer: btnActionRenderer,
        readOnly: true
      },
    ],
  };

}
/*function abc() {*/
// cont nguy hiểm: trường dangerous khác null
// cont quá khổ: trường oversize khác null

//if(oversize && dangerous) bắt cả 2 đều có file
//if(oversize) bắt oversize có file
//if(dangerous)bắt dangerous có file

//shipmentFiles  fileType

/*if(oversize && dangerous){
  if(fileType){ 
    fileType.forEach(function (elementType, index) { 
        if(elementType == "O"){// cont quá khổ oversize
          typeO = true;
         }
        if(elementType == "D"){// con nguy hiểm Dangerous
          typeD = true;
         } 
    });
  	
    shipmentFiles.forEach(function (elementcont, index) {// kết quả sau khi lưu file type 
      //alert("vao for 2");
      if(elementcont.fileType == "O"){// filetype quá khổ
         contO = true;
       }
      if(elementcont.fileType == "D"){// filetype nguy hiểm
         contD = true;
       } 
    });
  	
    if(contD == false && typeD == false){ 
      $.modal.alertWarning( "Chưa đính kèm tệp cho container nguy hiểm. Vui lòng đính kèm file.");
    } 
    else if(contO == false && typeO == false){
      $.modal.alertWarning( "Chưa đính kèm tệp cho container quá khổ. Vui lòng đính kèm file.");
    }
    else{ 
      saveFile();
    } 
  } 
  	
}*/


/*if(oversize && dangerous){
  if(fileType){ 
     fileType.forEach(function (elementType, index) { 
         if(elementType == "O"){// cont quá khổ oversize
           typeO = true;
          }
         if(elementType == "D"){// con nguy hiểm Dangerous
           typeD = true;
          } 
     });
   	
     shipmentFiles.forEach(function (elementcont, index) {// kết quả sau khi lưu file type 
       //alert("vao for 2");
       if(elementcont.fileType == "O"){// filetype quá khổ
          contO = true;
        }
       if(elementcont.fileType == "D"){// filetype nguy hiểm
          contD = true;
        } 
     });
   	
     if(contD == false && typeD == false){ 
       $.modal.alertWarning( "Chưa đính kèm tệp cho container nguy hiểm. Vui lòng đính kèm file.");
     } 
     else if(contO == false && typeO == false){
       $.modal.alertWarning( "Chưa đính kèm tệp cho container quá khổ. Vui lòng đính kèm file.");
     }
     else{
       checkSave(); 
       //saveFile();
     } 
   }
  
}
  shipmentFiles fileType
if(oversize){
  if(fileType){  
     fileType.forEach(function (elementType, index) { 
         if(elementType == "O"){// cont quá khổ oversize
           typeO = true;
          }  
     });
   	
     shipmentFiles.forEach(function (elementcont, index) {// kết quả sau khi lưu file type 
       //alert("vao for 2");
       if(elementcont.fileType == "O"){// filetype quá khổ
          contO = true;
        }
        
     });
   	
      if(contO == false && typeO == false){
       $.modal.alertWarning( "Chưa đính kèm tệp cho container quá khổ. Vui lòng đính kèm file.");
     }
     else{
       checkSave(); 
       //saveFile();
     } 
   }
}
 
 if(dangerous){
   if(fileType){ 
      fileType.forEach(function (elementType, index) { 
          if(elementType == "D"){// cont quá khổ oversize
            typeD = true;
           }  
      });
    	
      shipmentFiles.forEach(function (elementcont, index) {// kết quả sau khi lưu file type 
        //alert("vao for 2");
        if(elementcont.fileType == "D"){// filetype quá khổ
           contD = true;
         }
         
      });
    	
       if(contD == false && typeD == false){
        $.modal.alertWarning( "Chưa đính kèm tệp cho container nguy hiểm. Vui lòng đính kèm file.");
      }
      else{
        checkSave(); 
        //saveFile();
      } 
    }
   
 }  	
 */

function statusIconRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (row == reeferInfos.length) {
    return '';
  }
  $(td).html('');
  cellProperties.readOnly = 'true';
  $(td).attr('id', 'statusIcon' + row).addClass("htCenter").addClass("htMiddle");
  let status = "";
  if (row == 0) {
    value = shipmentDetail.powerDrawDateStatus;
    if (value == "P") {
      status = '<i id="status" class="fa fa-clock-o fa-flip-horizontal easyui-tooltip" title="Container đang chờ xét duyệt yêu cầu gia hạn rút điện" aria-hidden="true" style="color: #f8ac59;"></i>';
    } else if (value === "S") {
      status = '<i id="status" class="fa fa-clock-o fa-flip-horizontal easyui-tooltip" title="Container đã được xác nhận gia hạn rút điện" aria-hidden="true" style="color: #1ab394;"></i>';
    } else if (value === "E") {
      status = '<i id="status" class="fa fa-clock-o fa-flip-horizontal easyui-tooltip" title="Container đã được xác nhận gia hạn rút điện" aria-hidden="true" style="color: #ef6776;"></i>';
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

function dateSetPower(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    return '';
  }
  const dateResult = new Date(value);
  const month = dateResult.getMonth() == 12 ? '01' : dateResult.getMonth() + 1;
  const result = `${dateResult.getDate()}/${month}/${dateResult.getFullYear()} ${dateResult.getHours()}:${dateResult.getMinutes()}`;
  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: center;text-align: center;">' + result + '</div>');
  return td;
}

function dateGetPower(instance, td, row, col, prop, value, cellProperties) {
  if (!value) {
    return '';
  }
  const dateResult = new Date(value);
  const month = dateResult.getMonth() == 12 ? '00' : dateResult.getMonth() + 1;
  const result = `${dateResult.getDate()}/${month}/${dateResult.getFullYear()} ${dateResult.getHours()}:${dateResult.getMinutes()}`;
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
  $(td).addClass("htMiddle").addClass("htCenter");
  console.log('sssssssssssssssssss');
  if (!value) {
    value = "";
  }
  //isBookingCheckPayment
  if (sourceData[row] && sourceData[row].id) {
    if (isBookingCheckPayment()) {
      value = "Hãng tàu thanh toán";
    } else {
      if ('0' == creditFlag) {
        value = "Khách hàng trả trước";
      } else {
        value = "Khách hàng trả sau";
      }
    }
  }

  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function btnActionRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (!sourceData[row]) {
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: center;text-align: center;">' + '' + '</div>');
    return td;
  }
  let result = '';
  const btnPayment = `
  <td>
                          <a href="javascript:;" class="l-btn l-btn-small l-btn-plain" group="" id="">
                            <span class="l-btn-left"
                              ><span class="l-btn-text">
                                <button id="saveShipmentDetailBtn" onclick="paymentDateDrop()" class="btn btn-sm btn-primary"><i class="fa fa-credit-card text-primary"></i>Thanh toán</button></span
                              ></span
                            >
                          </a>
                        </td> 
  `;

  const btnCancel = `
  <td>
    <a href="javascript:;" class="l-btn l-btn-small l-btn-plain" group="" id="">
    <span class="l-btn-left"
      ><span class="l-btn-text">
        <button id="acceptBtn" onclick="cancelDateDrop(${sourceData[row].id})"  class="btn btn-sm btn-primary" style="background-color: #ef6776;width: 87%;">
          <i class="fa fa-close text-primary"></i>Hủy</button></span
      ></span
    >
    </a>
  </td>
  `;
  if (shipmentDetail.powerDrawDateStatus == "S" && PAYMENT_STATUS.process == sourceData[row].paymentStatus) {
    result += btnPayment;
  } else if (!sourceData[row].id || PAYMENT_STATUS.success == sourceData[row].paymentStatus) {
    result += 'Đã thanh toán';
  } else {
    result += btnCancel;
  }



  $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: center;text-align: center;">' + result + '</div>');
  return td;
}

function extendPowerDrawDate() {
  let len = sourceData.length - 1;

  if (!$('#extendPowerDrawDate').val()) {
    $.modal.alertError("Quý khách vui lòng điền thông tin gia hạn.");
  }
  else if (!$('#powerDrawDate').val()) {
    $.modal.alertError("Chưa có dữ liệu ngày rút điện.");
  }
  else if ($('#extendPowerDrawDate').val() < $('#powerDrawDate').val()) {
    $.modal.alertError("Ngày gia hạn tiếp theo không thể nhỏ hơn ngày rút điện hiện tại.");
  }
  else if (shipmentDetail.frozenStatus != 'S' || shipmentDetail.powerDrawDateStatus != 'S' || (sourceData[len].status && sourceData[len].paymentStatus != PAYMENT_STATUS.success)) {
    $.modal.alertError("Cont chưa thể gia hạn ngày rút điện");
  } else {

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
        sourceData = data.data;
        shipmentDetail.powerDrawDateStatus = "P";
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
}

function paymentDateDrop() {
  layer.confirm("Bạn có muốn thanh toán?", {
    icon: 3,
    title: "Xác Nhận",
    btn: ['Đồng Ý', 'Hủy Bỏ']
  }, function () {
    layer.close(layer.index);
    $.modal.openCustomForm("Thanh toán", prefix + "/paymentPowerDropForm/" + 69, 800, 400);
    return true;
  }, function () {
    layer.close(layer.index);;
    return false;
  });
}

function cancelDateDrop(id) {
  const detail = {
    id: id,
    shipmentDetailId: shipmentDetail.id
  }

  layer.confirm("Bạn có muốn hủy yêu cầu gia hạn ngày cắm điện?", {
    icon: 3,
    title: "Xác Nhận",
    btn: ['Đồng Ý', 'Hủy Bỏ']
  }, function () {
    layer.close(layer.index);

    $.ajax({
      url: prefix + "/cancelDateDrop",
      method: "POST",
      contentType: "application/json",
      accept: 'text/plain',
      data: JSON.stringify(detail),
      dataType: 'text',
      success: function (data) {

        data = JSON.parse(data);
        sourceData = data.data;
        shipmentDetail.powerDrawDateStatus = "S";
        configHandson();
        hot = new Handsontable(dogrid, config);
        hot.loadData(sourceData);
        $.modal.msgSuccess("Hủy gia hạn thành công.");
      },
      error: function (result) {
        $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, xin vui lòng thử lại.");
        $.modal.closeLoading();
      },
    });
    return true;
  }, function () {
    layer.close(layer.index);;
    return false;
  });
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


function getBetweenTwoDate(date1, date2) {
  const diffTime = Math.abs(date2 - date1);
  return Math.ceil(diffTime / (1000 * 60 * 60));
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
