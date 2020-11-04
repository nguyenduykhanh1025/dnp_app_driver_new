const PREFIX = ctx + "logistic/send-cont-full";

$("#containerNo").val(containerNo);
$("#sztp").val(sztp);

$("#form-detail-add").validate({
  onkeyup: false,
});

function submitHandler() {
  if ($.validate.form()) {
    var data = $("#form-detail-add").serializeArray();
    data = { ...covertSerializeArrayToObject(data) };
    parent.submitDataFromDetailModal(data);
    onCloseModel();
  }
}

function onCloseModel() {
  $.modal.close();
}

function covertSerializeArrayToObject(data) {
  var rv = {};
  for (var i = 0; i < data.length; ++i) rv[data[i].name] = data[i].value;
  return rv;
}
