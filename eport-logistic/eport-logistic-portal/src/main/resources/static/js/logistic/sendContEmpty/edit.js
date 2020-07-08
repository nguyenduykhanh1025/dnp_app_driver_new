var prefix = ctx + "logistic/send-cont-empty";

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

// Map data to form
if (shipment != null) {
    $("#id").val(shipment.id);
    $("#shipmentCode").val(shipment.id);
    $("#taxCode").val(shipment.taxCode);
    $("#containerAmount").val(shipment.containerAmount);
    $("#groupName").val(shipment.groupName);
    $("#remark").val(shipment.remark);
    if (shipment.status > 2) {
        $("#taxCode").prop('disabled', true);
        $("#containerAmount").prop('disabled', true);
    }
}

$("#form-edit-shipment").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        if ($("#groupName").val() != null && $("#groupName").val() != '') {
            $.operate.save(prefix + "/shipment/" + $('#id').val(), $('#form-edit-shipment').serialize());
            parent.loadTable();
        }
    } else {
        $.modal.msgError("Không tìm thấy mã số thuế!");
    }
}