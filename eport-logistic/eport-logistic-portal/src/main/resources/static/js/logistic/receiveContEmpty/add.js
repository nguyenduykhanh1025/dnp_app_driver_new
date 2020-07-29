var prefix = ctx + "logistic/receive-cont-empty";

$("#form-add-shipment").validate({
    focusCleanup: true
});

$('#taxCode').val(taxCode).prop('readonly', true);
loadGroupName();

$('input:radio[name="taxCodeDefault"]').change(function () {
    if ($(this).val() == '1') {
        $('#taxCode').val(taxCode).prop('readonly', true);
        loadGroupName();
    } else {
        $('#taxCode').val('').prop('readonly', false);
        $("#taxCode").removeClass("error-input");
    }
});

async function submitHandler() {
    if ($.validate.form()) {
        if ($("#groupName").val() != null && $("#groupName").val() != '') {
            let res = await getBookingNoUnique();
            if (res.code == 0) {
                save(prefix + "/shipment", new FormData($('#form-add-shipment')[0]));
            }
        } else {
            $.modal.alertError("Không tìm ra mã số thuế!<br>Quý khách vui lòng liên hệ đến bộ phận chăm sóc khách hàng 0933.157.159.");
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
                $.modal.alertError("Số book đã tồn tại!");
                $("#bookingNo").addClass("error-input");
            }
        });
    }
}

function loadGroupName() {
    if ($("#taxCode").val() != null && $("#taxCode").val() != '') {
        $.ajax({
            url: "/logistic/company/" + $("#taxCode").val(),
            method: "GET",
        }).done(function (result) {
            if (result.code == 0) {
                $("#groupName").val(result.groupName);
                $("#address").val(result.address);
                $("#taxCode").removeClass("error-input");
            } else {
                $.modal.alertError("Không tìm ra mã số thuế!<br>Quý khách vui lòng liên hệ đến bộ phận chăm sóc khách hàng 0933.157.159.");
                $("#taxCode").addClass("error-input");
                $("#groupName").val('');
                $("#address").val('');
            }
        });
    } else {
        $("#groupName").val('');
        $("#address").val('');
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
        $.modal.msgError("Chỉ được phép đính kèm tối đa 5 hình ảnh!");
        return false;
    }

    let totalFileSize = 0;
    $.each(files, function (index, file) {
        totalFileSize += file.size;
    });

    // Check total file size larger than 25MB or not ?
    if (totalFileSize > 2000000) {
        $.modal.msgError("Tổng dung lượng ảnh đính kèm chỉ được phép dưới 20MB!");
        return false;
    }

    return true;
}

function removeImage(element, imageIndex) {
    $(element).parent("div.preview-block").remove();
    Array.from($("input#uploadFiles")[0].files).splice(imageIndex, 1);
}
