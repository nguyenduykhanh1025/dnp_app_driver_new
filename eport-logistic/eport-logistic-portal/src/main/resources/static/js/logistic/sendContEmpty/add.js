var prefix = ctx + "logistic/send-cont-empty";
var shipmentFileIds = [];

$(document).ready(function () {
    let previewTemplate = '<span data-dz-name></span>';

    myDropzone = new Dropzone("#dropzone", {
        url: prefix + "/file",
        method: "post",
        paramName: "file",
        maxFiles: 5,
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
                shipmentFileIds.push(response.shipmentFileId);
                let html = `<div class="preview-block">
                    <img src="` + ctx + `img/document.png" alt="Tài liệu" />
                    <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + response.shipmentFileId + `)" >
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

$("#form-add-shipment").validate({
    focusCleanup: true
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
            save(prefix + "/shipment");
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