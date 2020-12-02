const PREFIX = ctx + "logistic/receive-cont-full";

$(document).ready(function () {
	initTabReefer();
	initTabOversize();
});

function initTabReefer() {
	if(shipmentDetail.sztp.includes("R")) {
		$('#reeferContainer').css('display', 'block');
		$("#tab-1").prop('checked', true);
	}else {
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
	$('#temperature').val(shipmentDetail.humidity);
	$('#temperature').val(shipmentDetail.ventilation);
}

function initTabOversize() {
	if(oversizeTop || oversizeRight || oversizeLeft || oversizeFront || oversizeBack) {
		$('#oversizeContainer').css('display', 'block');
		$("#tab-2").prop('checked', true);
	}else {
		console.log('cppp');
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

$("#datetimepicker1").datetimepicker({
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
var cont = contsztp.substring(2,3);// lấy mã cont cắt ra 
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
function confirm() {
	let date = $("#datetimepicker1").datetimepicker('getDate').getTime();
	let shipmentDetailId = shipmentDetail.id;
	
	let truckNo = $("#truckNo").val();
	let chassisNo = $("#chassisNo").val();
	console.log("HHHH" + truckNo);
	console.log(shipmentDetailId);
	const detail = { 
			id : shipmentDetail.id,
			shipmentId: shipmentDetail.shipmentId,
			sztp : shipmentDetail.sztp, 
			powerDrawDate: date, 
			truckNo : truckNo,
			chassisNo : chassisNo
	}
	 $.ajax( 
		    	{
		    		url: prefix + "/saveDate", 
		    		method: "post",
		            contentType: "application/json",
		            accept: "text/plain",
		    		data: JSON.stringify(detail),
		    		dataType: "text",
		    		success: function(result){
	    			if (result.code == 0) {
	                    //$.modal.alertError(result.msg);
	    				insertDate();  
	                } else {
	                    $.modal.close();
	                } 
		    }}); 
}


/*function confirm() {  
	let myDate = $("#datetimepicker1").datetimepicker('getDate').getTime();
    $.ajax( 
    	{
    		url: prefix + "/saveDate", 
    		method: "POST",
    		data: { 
    			shipmentDetailId : shipmentDetail.id,
    			shipmentId : shipmentDetail.shipmentId,
    			shipmentSztp : shipmentDetail.sztp,
    			powerDrawDate : $("#datetimepicker1").datetimepicker('getDate').getTime() 
    		},
    		success: function(result){
			if (result.code == 0) {
                //$.modal.alertError(result.msg);
				insertDate(); 
				 
            } else {
                $.modal.close();
            } 
    }});  
}
  */
//confirm
function insertDate() {   
	    $.ajax( 
	    	{
	    		url: prefix + "/saveFileImage", 
	    		method: "POST",
	    		data: {
	    			filePaths: shipmentFilePath,
	    			shipmentDetailId : shipmentDetail.id,
	    			shipmentId: shipmentDetail.shipmentId,
	    			shipmentSztp : shipmentDetail.sztp,
	    			shipmentDangerous : shipmentDetail.dangerous,  
	    			fileType : fileType, 
	    		
	    		},
	    		success: function(result){
    			if (result.code == 0) {
                    //$.modal.alertError(result.msg);
                    $.modal.close(); 
    				 
                } else {
                    $.modal.close();
                } 
	    }});  
}
  
function closeForm() {
    $.modal.close();
}

$( document ).ready(function() {  
	if (shipmentFiles != null) {// hiển thị hình ảnh
        let htmlInit = '';
        shipmentFiles.forEach(function (element, index) {  
        	shipmentFiles.push(element.id);
        	if(element.fileType == "R" || element.fileType == "r"){
        		htmlInit = `<div class="preview-block">
                <a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" /></a>
                <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)" >  
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
            	$('.preview-containerL').append(htmlInit); 
        	}
        	if(element.fileType == "D" || element.fileType == "d"){
        		htmlInit = `<div class="preview-block">
        		<a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" /></a>
                <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)" >
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
            	$('.preview-containerNH').append(htmlInit); 
        	}
        	if(element.fileType == "O" || element.fileType == "o"){
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
 
/*function abc() {*/
	// cont nguy hiểm: trường dangerous khác null
	// cont quá khổ: trường oversize khác null
	
	//if(oversize && dangerous) bắt cả 2 đều có file
	//if(oversize) bắt oversize có file
	//if(dangerous)bắt dangerous có file
	
	
	
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
}

function checkSave(){
	let result = false;
	
	 
	
}*/
 

