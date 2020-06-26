const PREFIX = ctx + "system/robot";

function edit() {
  let data = {
    id: robot.id,
    uuId: robot.uuId,
    ipAddress: $("input[name='ipAddress']").val(),
    status: $("input[name='status']").val(),
    isReceiveContFullOrder: $("input[name='isReceiveContFullOrder']").is(':checked') == true,
    isReceiveContEmptyOrder: $("input[name='isReceiveContEmptyOrder']").is(':checked') == true,
    isSendContFullOrder: $("input[name='isSendContFullOrder']").is(':checked') == true,
    isSendContEmptyOrder: $("input[name='isSendContEmptyOrder']").is(':checked') == true,
    isGateInOrder: $("input[name='isGateInOrder']").is(':checked') == true,
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
