const PREFIX = ctx + "system/robot";

function edit() {
  let status;
  if ($('#availableRadio').prop('checked')) {
    status = 0;
  } else if ($('#busyRadio').prop('checked')) {
    status = 1;
  } else {
    status = 2;
  }
  let data = {
    id: robot.id,
    uuId: robot.uuId,
    ipAddress: $("input[name='ipAddress']").val(),
    status: status,
    isReceiveContFullOrder: $("input[name='isReceiveContFullOrder']").is(':checked') == true,
    isReceiveContEmptyOrder: $("input[name='isReceiveContEmptyOrder']").is(':checked') == true,
    isSendContFullOrder: $("input[name='isSendContFullOrder']").is(':checked') == true,
    isSendContEmptyOrder: $("input[name='isSendContEmptyOrder']").is(':checked') == true,
    isShiftingContOrder: $("input[name='isShiftingContOrder']").is(':checked') == true,
    isChangeVesselOrder: $("input[name='isChangeVesselOrder']").is(':checked') == true,
    isExtensionDateOrder: $("input[name='isExtensionDateOrder']").is(':checked') == true,
    isCreateBookingOrder: $("input[name='isCreateBookingOrder']").is(':checked') == true,
    isGateInOrder: $("input[name='isGateInOrder']").is(':checked') == true,
    isChangeTerminalCustomHold: $("input[name='isChangeTerminalCustomHold']").is(':checked') == true,
    isCancelSendContFullOrder: $("input[name='isCancelSendContFullOrder']").is(':checked') == true,
    isCancelReceiveContEmptyOrder: $("input[name='isCancelReceiveContEmptyOrder']").is(':checked') == true,
    isExportReceipt: $("input[name='isExportReceipt']").is(':checked') == true,
    remark: $("textarea[name='remark']").val()
  }
  $.ajax({
    cache: true,
    type: "POST",
    url: PREFIX + "/edit",
    data: data,
    async: false,
    error: function (request) {
      $.modal.alertError("System error");
    },
    success: function (data) {
      $.operate.successCallback(data);
    },
  });
}

function submitHandler() {
  if ($.validate.form()) {
    edit();
  }
}
