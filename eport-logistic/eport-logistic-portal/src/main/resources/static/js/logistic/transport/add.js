var prefix = ctx + "logistic/transport"
$("#status").val(0);
document.getElementById("toggle").innerHTML = '<span class="fa fa-toggle-off text-info fa-2x" onclick="clickToggle()"></span>';
$("#form-account-add").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        save(prefix + "/add", $('#form-account-add').serialize());
    }
}

$("input[name='validDate']").datetimepicker({
    format: "yyyy-mm-dd",
    minView: "month",
    language: 'en',
    autoclose: true
});
function clickToggle() {
    if ($("#status").val() == 0) {
        document.getElementById("toggle").innerHTML = '<span class="fa fa-toggle-on text-info fa-2x" onclick="clickToggle()"></span>';
        $("#status").val(1);
    } else {
        document.getElementById("toggle").innerHTML = '<span class="fa fa-toggle-off text-info fa-2x" onclick="clickToggle()"></span>';
        $("#status").val(0);
    }
}
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