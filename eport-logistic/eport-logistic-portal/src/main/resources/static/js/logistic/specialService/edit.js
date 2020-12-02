var prefix = ctx + "logistic/send-cont-full";
var currentBooking = shipment.bookingNo;
var shipmentFileIds = [];

// Map data to form
if (shipment != null) {
    $("#id").val(shipment.id);
    $("#shipmentCode").val(shipment.id);
    $("#bookingNo").val(shipment.bookingNo);
    $("#opeCode").val(shipment.opeCode + ":");
    $("#containerAmount").val(shipment.containerAmount);
    $("#remark").val(shipment.remark);
    if (shipment.status > 1) {
        $("#bookingNo").prop('disabled', true);
    }
    if (shipment.status > 2) {
        $("#opeCode").prop('disabled', true);
    }
}

$("#form-edit-shipment").validate({
    focusCleanup: true
});

async function submitHandler() {
    if ($.validate.form()) {
        if ($("#opeCode option:selected").text() == 'Chọn OPR') {
            $.modal.alertWarning("Quý khách chưa chọn mã OPR.");
        } else if (!$("#containerAmount").val()) {
            $.modal.alertWarning("Số lượng container cập nhật không hợp lệ.");
        } else {
            if ($("#bookingNo").val() != currentBooking) {
                let res = await getBookingNoUnique();
                if (res.code == 0) {
                    edit(prefix + "/shipment/" + $('#id').val());
                }
            } else {
                edit(prefix + "/shipment/" + $('#id').val());
            }
        }
    }
}

function getBookingNoUnique() {
    return $.ajax({
        url: prefix + "/unique/booking-no",
        method: "post",
        contentType: "application/json",
        data: JSON.stringify({ "bookingNo": $("#bookingNo").val() }),
    })
}

function checkBookingNoUnique() {
    if ($("#bookingNo").val() != null && $("#bookingNo").val() != '' && $("#bookingNo").val() != currentBooking) {
        $.ajax({
            url: prefix + "/unique/booking-no",
            method: "post",
            contentType: "application/json",
            data: JSON.stringify({ "bookingNo": $("#bookingNo").val() }),
        }).done(function (result) {
            if (result.code == 0) {
                $("#bookingNo").removeClass("error-input");
            } else {
                $.modal.alertError(result.msg);
                $("#bookingNo").addClass("error-input");
            }
        });
    }
}
function edit(url) {
    let shipmentUpdate = new Object();
    shipmentUpdate.id = shipment.id;
    shipmentUpdate.bookingNo = $('#bookingNo').val();
    shipmentUpdate.opeCode = $('#opeCode').val().split(": ")[0].replace(":", "");
    shipmentUpdate.sendContEmptyType = $('.sendContEmptyType').val();
    shipmentUpdate.containerAmount = $('#containerAmount').val();
    shipmentUpdate.params = new Object();
    shipmentUpdate.params.ids = shipmentFileIds.join();
    $.ajax({
        url: url,
        method: "post",
        contentType: "application/json",
        data: JSON.stringify(shipmentUpdate),
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
            $.modal.enable();
        }
    });
}

$(document).ready(function () {

    // Check opr code need to attach booking
    $('#opeCode').change(function () {
        if (oprListBookingCheck.includes($('#opeCode option:selected').val().split(":")[0].replace(":",""))) {
            $('#dropzone').show();
            bookingAttach = true;
        } else {
            $('#dropzone').hide();
            bookingAttach = false;
        }
    });

    if (!oprListBookingCheck.includes(shipment.opeCode)) {
        $('#dropzone').hide();
    }

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
    }
}