const PREFIX = ctx + "system/robot";

function submitHandler() {
  if ($.validate.form()) {
    $.operate.save(PREFIX + "/add", $("#formAddRobot").serialize());
  }
}
