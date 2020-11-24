const PREFIX = ctx + "logistic/receive-cont-full";
 
 
//var shipmentFileIds = [];// validate file
 
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

$('#wgt').val(shipmentDetail.wgt); // trọng lượng
$('#wgtQK').val(shipmentDetail.wgt); // trọng lượng quá khổ 
$('#wgtNH').val(shipmentDetail.wgt); // trọng lượng Nguy hiểm  
$('#temperature').val(shipmentDetail.temperature);// nhiệt độ 

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

$('#powerDrawDate').val(shipmentDetail.powerDrawDate);//  





// cont nguy hiểm 
$('#dangerousImo').val(shipmentDetail.dangerousImo);// 
$('#dangerousUnno').val(shipmentDetail.dangerousUnno);//  
$('#dangerous').val(shipmentDetail.dangerous);//

$("#form-detail-add").validate({
	  onkeyup: false,
	  focusCleanup: true,
	});

/*console.log("shipmentDetail.dangerous" + shipmentDetail.dangerous);*/
 

$("#datetimepicker1").datetimepicker({
	  format: "dd/mm/yyyy",
	  language: "vi_VN",
	  minView: "month",
	  autoclose: true
});



/*function formatDate(data) {
	  if (data) {
	    let result = "";
	    let arrDate = data.toString().split("/");
	    let temp = arrDate[0];
	    arrDate[0] = arrDate[1];
	    arrDate[1] = temp;
	    return arrDate.join("/");
	  }
	  return "";
}*/
 
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

console.log("fileType");// khi lưu tạm thời
console.log(fileType);
console.log("shipmentFiles");// file đã insert vào
console.log(shipmentFiles);


var shipmentFileTotal  = [];

shipmentFileTotal.push(fileType); 
shipmentFileTotal.push(shipmentFiles); 
console.log("shipmentFileTotal");
console.log(shipmentFileTotal);
 
// confirm
function confirm() {
	 $.ajax( 
		    	{
		    		url: prefix + "/saveDate", 
		    		method: "POST",
		    		data: { 
		    			shipmentDetailId : shipmentDetail.id,
		    			shipmentId: shipmentDetail.shipmentId,
		    			shipmentSztp : shipmentDetail.sztp, 
		    			powerDrawDate : $("#powerDrawDate").val()
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
 

//confirm
function insertDate() {  
	//console.log("powerdrawdate" + $("#powerDrawDate").val());
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
	//console.log(shipmentFiles);
	
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
            	//url: prefix + "/booking/file",
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
 

