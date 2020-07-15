var prefix = ctx + "logistic/logisticTruck"
$("#form-truck-add").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
    	save(prefix + "/add", $('#form-truck-add').serialize())
    }
}

$("input[name='registryExpiryDate']").datetimepicker({
    format: "yyyy-mm-dd",
    minView: "month",
    language: 'en',
    autoclose: true
});

function save(url, data) {
    $.ajax({
        url: url,
        type: "post",
        dataType: "json",
        data: data,
        beforeSend: function () {
            $.modal.loading("Đang xử lý, vui lòng chờ...");
            $.modal.disable();
        },
        success: function(result) {
            $.modal.closeLoading();
            if (result.code == 0) {
                parent.loadTable(result.msg);
                $.modal.close();
            } else {
                $.modal.msgError(result.msg);
            }
        }
    })
}