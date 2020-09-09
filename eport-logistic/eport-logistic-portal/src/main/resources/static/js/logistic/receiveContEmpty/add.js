var prefix = ctx + "logistic/receive-cont-empty";

$("#form-add-shipment").validate({
    focusCleanup: true
});

async function submitHandler() {
    if ($.validate.form()) {
        if ($("#opeCode option:selected").text() == 'Chọn OPR') {
            $.modal.alertWarning("Quý khách chưa chọn mã OPR.");
        } else if (Array.from($("input#uploadFiles")[0].files).length > 0) {
            let res = await getBookingNoUnique();
            if (res.code == 0) {
                save(prefix + "/shipment", new FormData($('#form-add-shipment')[0]));
            }
        } else {
            $.modal.alertWarning("Quý khách chưa đính kèm hình ảnh booking hoặc lệnh cấp vỏ container.");
        }
    }
}

function getBookingNoUnique() {
    return $.ajax({
        url: prefix + "/unique/booking-no/" + $("#bookingNo").val(),
        method: "GET",
    });
}

function checkBookingNoUnique() {
    if ($("#bookingNo").val() != null && $("#bookingNo").val() != '') {
        $.ajax({
            url: prefix + "/unique/booking-no/" + $("#bookingNo").val(),
            method: "GET",
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

function save(url, data) {
    $.ajax({
        url: url,
        type: "post",
        dataType: "json",
        data: data,
        processData: false,
        contentType: false,
        beforeSend: function () {
            $.modal.loading("Đang xử lý, vui lòng chờ...");
            $.modal.disable();
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

$("#uploadFiles").change(function () {
    previewImages(this);
});

function previewImages($input) {
    if (!$input.files) {
        return;
    }

    if (!isValidFiles($input.files)) {
        return;
    }

    const $parent = $("div.preview-container");
    $parent.empty();

    $.each($input.files, function (index, file) {
        const $previewContainer = $(`<div class="preview-block">
                                        <img src="#" alt="Preview image" />
                                        <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ${index})" >
                                          <span aria-hidden="true">&times;</span>
                                        </button>
                                     </div>`);
        $parent.append($previewContainer);

        const reader = new FileReader();
        reader.onload = function (e) {
            $previewContainer.find("img").attr("src", e.target.result);
        };

        // Convert to base64 string
        reader.readAsDataURL(file);
    });
}

function isValidFiles(files) {
    if (files.length > 5) {
        $.modal.alertError("Chỉ được phép đính kèm tối đa 5 hình ảnh!");
        return false;
    }

    let totalFileSize = 0;
    $.each(files, function (index, file) {
        if (!/\.(jpe?g|png|gif|bmp)$/i.test(file.name)) {
            $.modal.alertError("Vui lòng chọn hình ảnh có định dạng (.bmp/.gif/.jpg/.jpeg/.png)!");
        }

        totalFileSize += file.size;
    });

    // Check total file size larger than 25MB or not ?
    if (totalFileSize > 2000000) {
        $.modal.alertError("Tổng dung lượng ảnh đính kèm chỉ được phép dưới 20MB!");
        return false;
    }

    return true;
}

function removeImage(element, imageIndex) {
    $(element).parent("div.preview-block").remove();
    Array.from($("input#uploadFiles")[0].files).splice(imageIndex, 1);
}
