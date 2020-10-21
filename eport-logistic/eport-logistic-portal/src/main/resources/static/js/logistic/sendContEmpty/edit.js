var prefix = ctx + "logistic/send-cont-empty";
var currentBill = shipment.blNo;
var shipmentFileIds = [];
// Map data to form
if (shipment != null) {
    $("#id").val(shipment.id);
    $("#blNo").val(shipment.blNo);
    $("#shipmentCode").val(shipment.id);
    $("#containerAmount").val(shipment.containerAmount);
    $("#remark").val(shipment.remark);
    $('#opeCode').val(shipment.opeCode);
    if (shipment.sendContEmptyType == '0') {
        $('#sendContPort').prop('checked', true);
    } else {
        $('#sendContCarrier').prop('checked', true);
    }
    if (shipment.status > 1) {
        $('#opeCode').prop('disabled', true);
        $('#sendContPort').prop('disabled', true);
        $('#sendContCarrier').prop('disabled', true);
    }
}

$("#form-edit-shipment").validate({
    focusCleanup: true
});

async function submitHandler() {
    if ($.validate.form()) {
        if ($("#opeCode option:selected").text() == 'Chọn OPR') {
            $.modal.alertWarning("Mã OPR là bắt buộc.");
        } else if (!$("#containerAmount").val()) {
            $.modal.alertWarning("Số lượng container không hợp lệ.");
        } else if ($("#blNo").val() && $("#blNo").val() != currentBill) {
            let res = await getBillNoUnique();
            if (res.code == 500) {
                $.modal.alertError(res.msg);
                $("#blNo").addClass("error-input");
            } else {
                $("#blNo").removeClass("error-input");
                edit(prefix + "/shipment/" + $('#id').val());
            }
        } else {
            edit(prefix + "/shipment/" + $('#id').val());
        }
    } 
}
function getBillNoUnique() {
    return $.ajax({
        url: prefix + "/blNo/bl-no",
        method: "post",
        contentType: "application/json",
        data: JSON.stringify({"blNo": $("#blNo").val()}),
    });
}
function checkBlNoUnique() {
    if ($("#blNo").val() != null && $("#blNo").val() != '' && $("#blNo").val() != currentBill) {
        //check bill unique
        $.ajax({
            url: prefix + "/blNo/bl-no",
            method: "post",
            contentType: "application/json",
            data: JSON.stringify({"blNo": $("#blNo").val()}),
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
function edit(url) {
    let shipmentUpdate = new Object();
    shipmentUpdate.id = shipment.id;
    shipmentUpdate.blNo = $('#blNo').val();
    shipmentUpdate.opeCode = $('#opeCode').val();
    shipmentUpdate.containerAmount = $('#containerAmount').val();
    shipmentUpdate.sendContEmptyType = $('input[name="sendContEmptyType"]:checked').val();
    shipmentUpdate.params = new Object();
    shipmentUpdate.params.ids = shipmentFileIds.join();
    $.ajax({
        url: url,
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(shipmentUpdate),
        beforeSend: function () {
            $.modal.loading("Đang xử lý, vui lòng chờ...");
        },
        success: function(result) {
            $.modal.closeLoading();
            if (result.code == 0) {
                parent.loadTable(result.msg);
                $.modal.close();
            } else {
                $.modal.alertError(result.msg);
            }
            $.modal.enable();
        }
    });
}

$(document).ready(function () {
    let maxFile = 5;
    if (shipmentFiles != null) {
        maxFile -= shipmentFiles.length;
        let htmlInit = '';
        shipmentFiles.forEach(function (element, index) {
            shipmentFileIds.push(element.id);
            htmlInit = `<div class="preview-block">
                    <img src="` + ctx + `img/document.png" alt="Tài liệu" />
                    <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)">
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>`;
            $('.preview-container').append(htmlInit);
        });
    }

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
                $.modal.alertWarning("Số lượng tệp đính kèm vượt quá số lượng cho phép.");
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
                $.modal.alertWarning("Đính kèm tệp thất bại, vui lòng thử lại sau.");
            }
        }
    });
});

function removeImage(element, fileIndex) {
    if (shipment.status > 2) {
        $.modal.alertWarning("Lô có container đang hoặc đã làm lệnh, không thể xóa tệp đã đính kèm.");
    } else {
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
                            $.modal.alertWarning("Xóa tệp thất bại.");
                        }
                    }
                });
                return false;
            }
        });
    }
}