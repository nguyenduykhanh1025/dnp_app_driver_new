var prefix = ctx + "logistic/send-cont-empty";

// Map data to form
if (shipment != null) {
    $("#id").val(shipment.id);
    $("#shipmentCode").val(shipment.id);
    if (shipment.taxCode == taxCode) {
        $('#taxCode').val(taxCode).prop('readonly', true);
    } else {
        $('#taxCodeNotDefault').prop('checked', true);
        $('#taxCode').val(shipment.taxCode);
    }
    $("#containerAmount").val(shipment.containerAmount);
    $("#groupName").val(shipment.groupName);
    $("#remark").val(shipment.remark);
    if (shipment.status > 2) {
        $('input:radio[name="taxCodeDefault"]').prop('disabled', true);
        $("#taxCode").prop('disabled', true);
        $("#containerAmount").prop('disabled', true);
    }
}

$('input:radio[name="taxCodeDefault"]').change(function() {
    if ($(this).val() == '1') {
        $('#taxCode').val(taxCode).prop('readonly', true);
        loadGroupName();
    } else {
        $('#taxCode').val(shipment.taxCode).prop('readonly', false);
        $("#groupName").val(shipment.groupName);
        $("#taxCode").removeClass("error-input");
    }
});

$("#form-edit-shipment").validate({
    focusCleanup: true
});

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
                $.modal.alertError("Không tìm ra mã số thuế!<br>Quý khách vui lòng liên hệ đến bộ phận chăm sóc khách hàng 0933.157.159.");
                $("#taxCode").addClass("error-input");
                $("#groupName").val('');
            }
        });
    } else {
        $("#groupName").val('');
    }
}

async function submitHandler() {
    if ($.validate.form()) {
        if ($("#groupName").val() != null && $("#groupName").val() != '') {
            $.operate.save(prefix + "/shipment/" + $('#id').val(), $('#form-edit-shipment').serialize());
            parent.loadTable();
        }
    } else {
        $.modal.alertError("Không tìm ra mã số thuế!<br>Quý khách vui lòng liên hệ đến bộ phận chăm sóc khách hàng 0933.157.159.");
    }
}