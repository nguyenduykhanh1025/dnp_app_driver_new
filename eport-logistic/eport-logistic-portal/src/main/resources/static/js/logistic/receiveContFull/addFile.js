const prefix = ctx + "logistic/receive-cont-full"
var shipmentFileIds = [];
//var abc = [];
var shipmentFilePath  = [];
var shipmentDetailId =[];
var shipmentId = []; 
var fileType = []; 

const DANGEROUS_STATUS = {
		  yet: "T", // là cont dangerous
		  pending: "1", // là cont danger đang chờ xét duyết
		  approve: "3", // là cont danger đã đc xét duyết
		  reject: "4", // là cont danger đã bị từ chối
		  NOT: "F", // không phải là cont danger
	};
	const SPECIAL_STATUS = {
		  yet: "1",
		  pending: "2",
		  approve: "3",
		  reject: "4",
	};


$(document).ready(function () {
  
	
    let previewTemplate = '<span data-dz-name></span>';
    
//////////////// cont lạnh/////
    myDropzone = new Dropzone("#dropzoneL", {
        url: prefix + "/file/file-type/R",
        method: "post",
        paramName: "file",
        maxFiles: 5,
        maxFilesize: 10, //MB
        // autoProcessQueue: false,
        previewTemplate: previewTemplate,
        previewsContainer: ".preview-containerL", // Define the container to display the previews
        clickable: "#attachButtonL", // Define the element that should be used as click trigger to select files.
        init: function () {
            this.on("maxfilesexceeded", function (file) {
                $.modal.alertError("Số lượng tệp đính kèm vượt số lượng cho phép.");
                this.removeFile(file);
            });
        },
        success: function (file, response) {
            if (response.code == 0) { 
                $.modal.msgSuccess("Đính kèm tệp thành công."); 
                
                shipmentFileIds.push(response.shipmentFileId);
                
                console.log("eeeeeeeeeeeeeeeeeeeee" + response);
                 
                //  them
                shipmentFilePath.push(response.file); 
                shipmentDetailId.push(response.id);  
                
                console.log("aaaaaaaaaaaaaaaaa" + response.fileType); 
                /*fileType += response.fileType;*/
                fileType.push(response.fileType);  
                console.log("bbbbbbbbbb" + fileType); 
                
                
              
                
                //shipmentId.push(response.shipmentId); 
                let html = `<div class="preview-block">
                    <img src="` + ctx + `img/document.png" alt="Tài liệu" />
                    <button type="button" class="close" aria-label="Close" onclick="removeImage1(this, '${response.file}')" >
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>`
                $('.preview-containerL').append(html);
            } else {
                $.modal.alertError("Đính kèm tệp thất bại, vui lòng thử lại sau.");
            }
        }
    });
//////////////// cont quá khổ//////
    myDropzone = new Dropzone("#dropzoneQK", {
        url: prefix + "/file/file-type/O",
        method: "post",
        paramName: "file",
        maxFiles: 5,
        maxFilesize: 10, //MB
        // autoProcessQueue: false,
        previewTemplate: previewTemplate,
        previewsContainer: ".preview-containerQK", // Define the container to display the previews
        clickable: "#attachButtonQK", // Define the element that should be used as click trigger to select files.
        init: function () {
            this.on("maxfilesexceeded", function (file) {
                $.modal.alertError("Số lượng tệp đính kèm vượt số lượng cho phép.");
                this.removeFile(file);
            });
        },
        success: function (file, response) {
            if (response.code == 0) { 
                $.modal.msgSuccess("Đính kèm tệp thành công."); 
                
                shipmentFileIds.push(response.shipmentFileId);
                console.log("IIIIIIII" + response); 
                console.log("sssss" + response.file); 
                //fileType += response.fileType;
                fileType.push(response.fileType);  
                //  them
                shipmentFilePath.push(response.file); 
                shipmentDetailId.push(response.id); 
                //shipmentId.push(response.shipmentId); 
                let html = `<div class="preview-block">
                    <img src="` + ctx + `img/document.png" alt="Tài liệu" />
                    <button type="button" class="close" aria-label="Close" onclick="removeImage1(this, '${response.file}')" >
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>`
                $('.preview-containerQK').append(html);
            } else {
                $.modal.alertError("Đính kèm tệp thất bại, vui lòng thử lại sau.");
            }
        }
    });
    
///////// cont nguy hiểm//////////////////
    myDropzone = new Dropzone("#dropzoneNH", {
        url: prefix + "/file/file-type/D",
        method: "post",
        paramName: "file",
        maxFiles: 5,
        maxFilesize: 10, //MB
        // autoProcessQueue: false,
        previewTemplate: previewTemplate,
        previewsContainer: ".preview-containerNH", // Define the container to display the previews
        clickable: "#attachButtonNH", // Define the element that should be used as click trigger to select files.
        init: function () {
            this.on("maxfilesexceeded", function (file) {
                $.modal.alertError("Số lượng tệp đính kèm vượt số lượng cho phép.");
                this.removeFile(file);
            });
        },
        success: function (file, response) {
            if (response.code == 0) { 
                $.modal.msgSuccess("Đính kèm tệp thành công."); 
                
                shipmentFileIds.push(response.shipmentFileId);
                console.log("IIIIIIII" + response); 
                console.log("sssss" + response.file); 
                //  them
                shipmentFilePath.push(response.file); 
                
                shipmentDetailId.push(response.id); 
                fileType.push(response.fileType);  
                //fileType += response.fileType;
                //shipmentId.push(response.shipmentId); 
                let html = `<div class="preview-block">
                    <img src="` + ctx + `img/document.png" alt="Tài liệu" />
                    <button type="button" class="close" aria-label="Close" onclick="removeImage1(this, '${response.file}')" >
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>`
                $('.preview-containerNH').append(html);
            } else {
                $.modal.alertError("Đính kèm tệp thất bại, vui lòng thử lại sau.");
            }
        }
    });
});

/*$("#form-add-shipment").validate({
    focusCleanup: true
});*/

async function submitHandler() {
	//alert("vao submitHandler");
    if ($.validate.form()) {
        if ($("#opeCode option:selected").text() == 'Chọn OPR') {
            $.modal.alertWarning("Quý khách chưa chọn mã OPR.");
        } else if ($("#blNo").val()) {
            let res = await getBillNoUnique();
            if (res.code == 500) {
                $.modal.alertError(res.msg);
                $("#blNo").addClass("error-input"); 
            } else {
                $("#blNo").removeClass("error-input"); 
                save(prefix + "/shipment");
            }
        } else {
            //save(prefix + "/shipment");
            $.modal.alertError("Hãy đính kèm tệp");
        }
    }
}

 
function getBillNoUnique() {
    return $.ajax({
        url: prefix + "/unique/bl-no",
        method: "post",
        contentType: "application/json",
        data: JSON.stringify({ "blNo": $("#blNo").val() }),
    });
}

function checkBlNoUnique() {
    if ($("#blNo").val() != null && $("#blNo").val() != '') {
        //check bill unique
        $.ajax({
            url: prefix + "/unique/bl-no",
            method: "post",
            contentType: "application/json",
            data: JSON.stringify({ "blNo": $("#blNo").val() }),
        }).done(function (result) {
            if (result.code == 500) {
                $.modal.alertError(result.msg);
                $("#blNo").addClass("error-input");
            } else {
                $("#blNo").removeClass("error-input");
            }
        });
    }
}

function save(url) {
    let shipment = new Object();
    shipment.blNo = $('#blNo').val();
    shipment.opeCode = $('#opeCode').val().split(": ")[0].replace(":", "");
    shipment.containerAmount = $('#containerAmount').val();
    shipment.sendContEmptyType = $('input[name="sendContEmptyType"]:checked').val();
    shipment.remark = $('#remark').val();
    shipment.params = new Object();
    shipment.params.ids = shipmentFileIds.join();
    $.ajax({
        url: url,
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(shipment),
        beforeSend: function () {
            $.modal.loading("Đang xử lý, vui lòng chờ...");
        },
        success: function (result) {
            $.modal.closeLoading();
            if (result.code == 0) {
                parent.loadTable(result.msg);
                $.modal.close();
            } else {
                $.modal.alertError(result.msg);
            }
        }
    })
}
/*function removeImage(element, fileIndex) {
	
	if ( shipmentDetail.dangerous == DANGEROUS_STATUS.pending ||
			 shipmentDetail.dangerous == DANGEROUS_STATUS.approve ||
			 shipmentDetail.contSpecialStatus == SPECIAL_STATUS.yet ||
			 shipmentDetail.contSpecialStatus == SPECIAL_STATUS.approve ){
	    $.modal.alertWarning( "Container đang hoặc đã yêu cầu xác nhận, không thể xóa tệp đã đính kèm.");
	  } 
	else{
	if (!fileIndex) {
	    $.modal.msgSuccess("Xóa tệp thành công.");
	    $(element).parent("div.preview-block").remove();
	    let indexIsClick = $(".close").index(element);
	    shipmentFileIds.splice(indexIsClick, 1);
	    //shipmentFilePaths[`${element.className.split(" ")[1]}`].splice(indexIsClick, 1 );
	  } else {
		  shipmentFileIds.forEach(function (value, index) {
	      if (value.id == fileIndex) {
	        $.ajax({
	          url: prefix + "/booking/file",
	          method: "DELETE",
	          data: {
	            id: value,
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
	              //shipmentFilePaths[ `${getKeyFormByKeyType(value.fileType)}`
	              //].splice(index, 1);
	            } else {
	              $.modal.alertWarning("Xóa tệp thất bại.");
	            }
	          },
	        });
	        return false;
	      }
	    });
	  }
	//}
	
}*/






// xóa khi chưa lưu, chưa có id. lấy filepath xóa trong lưu tạm
function removeImage1(element, fileIndex) {
	 
	shipmentFilePath.forEach(function (value, index) {
	 
		
        if (value == fileIndex) { 
            $.ajax({
            	url: prefix + "/booking/file",
                method: "DELETE",
                data: {
                    id: index,
                    filePath: fileIndex
                },
                beforeSend: function () {
                    $.modal.loading("Đang xử lý, vui lòng chờ...");
                },
                success: function (result) {
                    $.modal.closeLoading();
                    if (result.code == 0) {
                        $.modal.msgSuccess("Xóa tệp thành công.");
                        $(element).parent("div.preview-block").remove();
                        shipmentFilePath.splice(index, 1);
                    } else {
                        $.modal.alertWarning("Xóa tệp thất bại.");
                    }
                }
            });
            return false;
        }
    });
   
}

 