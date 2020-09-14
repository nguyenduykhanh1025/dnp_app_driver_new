var prefix = ctx + "logistic/logisticTruck"
$("#form-truck-add").validate({
    focusCleanup: true
});
function submitHandler() { 
    if(!validatePlateNumber())
    {
        return false;
    }
    if ($.validate.form()) {
        $.ajax({
            url: prefix + "/unique/plate/" + $('input[name=plateNumber]').val(),
            method: 'GET'
        }).done(function(res) {
            if (res.code == 0) {
                save(prefix + "/add", $('#form-truck-add').serialize())
            } else {
                $.modal.msgError(res.msg);
            }
        });
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

$( "#type" ).change(function() {
    if($( "#type" ).val() == 1)
    {
        $( "#wgt" ).attr("type", "number");
        $("#wgtTitle").removeClass("hidden");
        $("#gatepassTitle").addClass("hidden");
        $("#gatepass").prop( "disabled", true );
    }else {
        $( "#wgt" ).attr("type", "hidden");
        $("#wgtTitle").addClass("hidden");
        $("#gatepassTitle").removeClass("hidden");
        $("#gatepass").prop( "disabled", false );
    }
});
var plateNumberRgx = /[0-9][0-9][a-z][0-9]{4,5}$/i;
function validatePlateNumber()
{
    var check = plateNumberRgx.test($('#plateNumber').val().trim())
    if(!check)
    {
        $.modal.alertError("Vui lòng nhập đúng định dạng biển số xe!");
        return false;
    }
    setGatePass();
    return true;
}

function setGatePass() {
    let gatePass = $("#plateNumber").val().trim();
    if(gatePass.length > 7 && $( "#type" ).val() == 0)
    {
        gatePass = gatePass.substring(gatePass.length-5,gatePass.length)
        $("#gatepass").val(gatePass);
    }
}