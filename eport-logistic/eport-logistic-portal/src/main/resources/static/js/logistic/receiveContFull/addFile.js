const prefix = ctx + "logistic/receive-cont-full"
var shipmentFileIds = [];
//var abc = [];
var shipmentFilePath  = [];
var shipmentDetailId =[];
var shipmentId = []; 
var fileType = []; 

const SPECIAL_STATUS = {
	  yet: "1",
	  pending: "2",
	  approve: "3",
	  reject: "4",
}; 
$(document).ready(function () { 
    let previewTemplate = '<span data-dz-name></span>'; 
//////////////// frozen/////
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
                shipmentFilePath.push(response.file); 
                shipmentDetailId.push(response.id);  
                fileType.push(response.fileType);     
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
//////////////// oversize//////
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
                fileType.push(response.fileType);   
                shipmentFilePath.push(response.file); 
                shipmentDetailId.push(response.id);   
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
    
///////// dangerous//////////////////
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
                shipmentFilePath.push(response.file);  
                shipmentDetailId.push(response.id); 
                fileType.push(response.fileType);   
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

async function submitHandler() { 
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
  
// xóa khi chưa lưu, chưa có id. lấy filepath xóa trong lưu tạm
function removeImage1(element, fileIndex) { 
	shipmentFilePath.forEach(function (value, index) {   
        if (value == fileIndex) { 
            $.ajax({ 
            	url: prefix + "/delete_file", 
                method: "DELETE",
                data: {
                    id: "",
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

 