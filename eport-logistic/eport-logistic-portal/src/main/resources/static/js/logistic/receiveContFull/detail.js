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
	    			shipmentSztp : shipmentDetail.sztp
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
	//console.log(shipmentFiles[0]);
	
	
	if (shipmentFiles != null) {
        //maxFile -= shipmentFiles.length;
		 
        let htmlInit = '';
        shipmentFiles.forEach(function (element, index) { 
        	shipmentFiles.push(element.id);
        	if(element.fileType == "t" || element.fileType == "T"){
        		htmlInit = `<div class="preview-block">
                <a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" /></a>
                /*<button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)" disabled>*/
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
            	$('.preview-containerL').append(htmlInit); 
        	}
        	if(element.fileType == "d" || element.fileType == "D"){
        		htmlInit = `<div class="preview-block">
        		<a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" />
                /*<button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)" disabled>*/
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
            	$('.preview-containerNH').append(htmlInit); 
        	}
        	if(element.fileType == "p" || element.fileType == "P"){
        		htmlInit = `<div class="preview-block">
        		<a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" />
                /*<button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)" disabled>*/
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
            	$('.preview-containerQK').append(htmlInit); 
        	}
        	
        	
            
        });

    }
});

