var prefix = ctx + "logistic/send-cont-empty";

$("#form-add-shipment").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        if ($("#groupName").val() != null && $("#groupName").val() != '') {
            $.operate.save(prefix + "/shipment", $('#form-add-shipment').serialize());
            parent.loadTable();
        } else {
            $.modal.msgError("Không tìm thấy mã số thuế!");
        }
    }
}

function loadGroupName() {
    if ($("#taxCode").val() != null && $("#taxCode").val() != '') {
        $.ajax({
            url: "/logistic/company/" + $("#taxCode").val(),
            method: "get"
        }).done(function (result) {
            if (result.code == 0) {
                $("#groupName").val(result.groupName);
                $("#taxCode").removeClass("error-input");
            } else {
                $.modal.msgError("Không tìm ra mã số thuế!");
                $("#taxCode").addClass("error-input");
                $("#groupName").val('');
            }
        });
    } else {
        $("#groupName").val('');
    }
}