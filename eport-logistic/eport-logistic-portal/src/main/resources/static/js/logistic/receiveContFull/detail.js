const PREFIX = ctx + "logistic/receive-cont-full";
 
 
var shipmentFileIds = [];// validate file
 
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

/*console.log("1" + shipmentDetail.osHeight);
console.log("2" + shipmentDetail.osPort);
console.log("3" + shipmentDetail.osStbd);
console.log("4" + shipmentDetail.ovAft);
console.log("5" + shipmentDetail.ovFore);
console.log("6" + shipmentDetail.ovHeight);
console.log("7" + shipmentDetail.ovPort);
console.log("8" + shipmentDetail.ovStbd);*/

// cont nguy hiểm 
$('#dangerousImo').val(shipmentDetail.dangerousImo);// 
$('#dangerousUnno').val(shipmentDetail.dangerousUnno);//  
$('#dangerous').val(shipmentDetail.dangerous);//

$("#form-detail-add").validate({
	  onkeyup: false,
	  focusCleanup: true,
	});

/*console.log("shipmentDetail.dangerous" + shipmentDetail.dangerous);*/
 
/*$("#datetimepicker1").datetimepicker({
	  format: "dd/mm/yyyy",
	  language: "vi_VN",
	  minView: "month",
	});*/


/*console.log(fileType);*/ 

 
let contD = false;// nguy hiem 
let contR = false;// lanh
let contO = false;// qua kho 

let typeD = false;// nguy hiem 
let typeR = false;// lanh
let typeO = false;// qua kho


function confirm() { 
	if ( shipmentDetail.dangerous == "1" ||
		 shipmentDetail.dangerous == "3" ||
		 shipmentDetail.contSpecialStatus == "1" ||
		 shipmentDetail.contSpecialStatus == "3" ){
	    $.modal.alertWarning( "Container đang hoặc đã yêu cầu xác nhận, không thể thêm tệp đã đính kèm.");
	  }
	else {
		// nếu 
	if(fileType){ 
		fileType.forEach(function (elementType, index) {
			//alert("vao for 1");  
				if(elementType == "O"){// cont quá khổ oversize
					typeO = true;
				 }
				if(elementType == "D"){// con nguy hiểm Dangerous
					typeD = true;
				 }
				if(elementType == "R"){// cont lạnh SZTP = R
					typeR = true;
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
			if(elementcont.fileType == "R"){// fileType lạnh
				 contR = true;
			 } 
		});
		
		if(contD == false && typeD == false){ 
			$.modal.alertWarning( "Chưa đính kèm tệp cho container nguy hiểm. Vui lòng đính kèm file.");
		}
		else if(contR == false && typeR == false){
			$.modal.alertWarning( "Chưa đính kèm tệp cho container lạnh. Vui lòng đính kèm file.");
		}
		else if(contO == false && typeO == false){
			$.modal.alertWarning( "Chưa đính kèm tệp cho container quá khổ. Vui lòng đính kèm file.");
		}
		else{ 
			saveFile();
		} 
	}
  }

}
   
function saveFile() {  
	    $.ajax(
	    	{
	    		url: prefix + "/uploadFile", 
	    		method: "POST",
	    		data: {
	    			filePaths: shipmentFilePath,
	    			shipmentDetailId : shipmentDetail.id,
	    			shipmentId: shipmentDetail.shipmentId,
	    			shipmentSztp : shipmentDetail.sztp,
	    			shipmentDangerous : shipmentDetail.dangerous,  
	    			oversizeTop : shipmentDetail.oversizeTop,
	    			oversizeRight : shipmentDetail.oversizeRight,
	    			oversizeLeft : shipmentDetail.shipmentDetail,
	    			oversizeFront : shipmentDetail.oversizeFront,
	    			oversizeBack : shipmentDetail.oversizeBack, 
	    			fileType : fileType 
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

/*function removeImage(element, fileIndex) {
    shipmentFileIds.forEach(function (value, index) {
        if (value == fileIndex) {
            $.ajax({
            	url: prefix + "/booking/file",
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

}*/
 

