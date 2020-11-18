//const PREFIX = ctx + "logistic/receive-cont-full"

const PREFIX = ctx + "system/checkContP"; //om/booking
 
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
 
function closeForm() {
    $.modal.close();
}

$( document ).ready(function() {  
	if (shipmentFiles != null) { 
        let htmlInit = '';
        shipmentFiles.forEach(function (element, index) { 
        	shipmentFiles.push(element.id);
        	if(element.fileType == "r" || element.fileType == "R"){
        		htmlInit = `<div class="preview-block">
                <a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" /></a>
                <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)" disabled>
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
            	$('.preview-containerL').append(htmlInit); 
        	}
        	if(element.fileType == "d" || element.fileType == "D"){
        		htmlInit = `<div class="preview-block">
        		<a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" /></a>
                <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)" disabled>
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
            	$('.preview-containerNH').append(htmlInit); 
        	}
        	if(element.fileType == "o" || element.fileType == "O"){
        		htmlInit = `<div class="preview-block">
        		<a href=${element.path} target="_blank"><img src="` + ctx + `img/document.png" alt="Tài liệu" /></a>
                <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)" disabled>
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
            	$('.preview-containerQK').append(htmlInit); 
        	}
        	 
        });

    }
});

