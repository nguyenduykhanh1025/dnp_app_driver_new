var prefix = ctx + "logistic/special-service";
var shipmentFileIds = [];

$(document).ready(function () {
    let maxFile = 10;

    if (files != null) {
        maxFile -= files.length;
        let htmlInit = '';
        files.forEach(function (element, index) {
            shipmentFileIds.push(element.id);
            htmlInit = `<div class="preview-block">
                    <a href="${element.path}" target="_blank"><img src="` + ctx + `img/document.png" style="width: 68px; height: 70px;" alt="Tài liệu" /></a>
                    <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)" disabled>
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>`;
            $('.preview-container').append(htmlInit);
        });
    }

    let previewTemplate = '<span data-dz-name></span>';

    myDropzone = new Dropzone("#dropzone", {
        url: prefix + "/shipment-detail/" + shipmentDetailId + "/file",
        method: "post",
        paramName: "file",
        maxFiles: maxFile,
        maxFilesize: 10, //MB
        // autoProcessQueue: false,
        previewTemplate: previewTemplate,
        previewsContainer: ".preview-container", // Define the container to display the previews
        clickable: "#attachButton", // Define the element that should be used as click trigger to select files.
        init: function () {
            this.on("maxfilesexceeded", function (file) {
                $.modal.alertError("Số lượng tệp đính kèm vượt số lượng cho phép.");
                this.removeFile(file);
            });
        },
        success: function (file, response) {
            if (response.code == 0) {
                $.modal.msgSuccess("Đính kèm tệp thành công.");
                
                shipmentFileIds.push(response.shipmentFile.id);
                
                let html = `<div class="preview-block">
                    <a href="${response.shipmentFile.path}" target="_blank"><img src="` + ctx + `img/document.png" style="width: 68px; height: 70px;" alt="Tài liệu" /></a>
                    <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + response.shipmentFile.id + `)" >
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>`
                $('.preview-container').append(html);
            } else {
                $.modal.alertError("Đính kèm tệp thất bại, vui lòng thử lại sau.");
            }
        }
    });
});

function removeImage(element, fileIndex) {
    shipmentFileIds.forEach(function (value, index) {
        if (value == fileIndex) {
            $.ajax({
                url: prefix + "/file",
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
                        $.modal.msgError("Xóa tệp thất bại.");
                    }
                }
            });
            return false;
        }
    });
}

function closeForm() {
    parent.reloadShipmentDetail();
    $.modal.close();
}





