const PREFIX = ctx + "logistic/receive-cont-full"
 
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

console.log("1" + shipmentDetail.osHeight);
console.log("2" + shipmentDetail.osPort);
console.log("3" + shipmentDetail.osStbd);
console.log("4" + shipmentDetail.ovAft);
console.log("5" + shipmentDetail.ovFore);
console.log("6" + shipmentDetail.ovHeight);
console.log("7" + shipmentDetail.ovPort);
console.log("8" + shipmentDetail.ovStbd);

// cont nguy hiểm 
$('#dangerousImo').val(shipmentDetail.dangerousImo);// 
$('#dangerousUnno').val(shipmentDetail.dangerousUnno);// 

$('#dangerous').val(shipmentDetail.dangerous);//

console.log("shipmentDetail.dangerous" + shipmentDetail.dangerous);
 
$("#datetimepicker1").datetimepicker({
	  format: "dd/mm/yyyy",
	  language: "vi_VN",
	  minView: "month",
	});
 
function confirm() {  
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
	    			oversizeBack : shipmentDetail.oversizeBack
	    			
	    		},
	    		success: function(result){
    			if (result.code == 0) {
                    $.modal.alertError(result.msg);
                } else {
                    $.modal.close();
                } 
	    }});  
}

function closeForm() {
    $.modal.close();
}

$( document ).ready(function() {
	if (shipmentFiles != null) {
        let htmlInit = '';
        shipmentFiles.forEach(function (element, index) { 
        	shipmentFiles.push(element.id);
        	if(element.fileType == "R" || element.fileType == "r"){
        		htmlInit = `<div class="preview-block">
                <a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" /></a>
                <button type="button" class="close" aria-label="Close" onclick="removeImage1(this, ` + element.id + `)" >  
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
            	$('.preview-containerL').append(htmlInit); 
        	}
        	if(element.fileType == "D" || element.fileType == "d"){
        		htmlInit = `<div class="preview-block">
        		<a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" /></a>
                <button type="button" class="close" aria-label="Close" onclick="removeImage1(this, ` + element.id + `)" >
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
            	$('.preview-containerNH').append(htmlInit); 
        	}
        	if(element.fileType == "O" || element.fileType == "o"){
        		htmlInit = `<div class="preview-block">
        		<a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" /></a>
                <button type="button" class="close" aria-label="Close" onclick="removeImage1(this, ` + element.id + `)" >
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
            	$('.preview-containerQK').append(htmlInit); 
        	}
        	
        	
            
        });

    }
});

 

function removeImage1(element, fileIndex) {  
$.modal.confirmShipment("Xác nhận xóa tệp đính kèm ?", function () {
	shipmentFiles.forEach(function (value, index) {
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
                        shipmentFiles.splice(index, 1);
                    } else {
                        $.modal.msgError("Xóa tệp thất bại.");
                    }
                }
            });
            return false;
        }
    }); 
});
}

