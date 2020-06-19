const PREFIX = ctx + "system/robot";

function edit() {
  let data = $("#formEditRobot").serialize();
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
