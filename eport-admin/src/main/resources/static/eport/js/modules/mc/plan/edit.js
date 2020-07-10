const PREFIX = ctx + "mc/plan";

var block = $("input[name='block']");
var bay = $("input[name='bay']");
var row = $("input[name='row']");
var tier = $("input[name='tier']");
var area = $("input[name='area']");

// Clear data and toggle disable attribute of area
function toggleDisableArea() {
  let isDisabled =
    !isNullOrEmpty(block.val()) ||
    !isNullOrEmpty(bay.val()) ||
    !isNullOrEmpty(row.val()) ||
    !isNullOrEmpty(tier.val());
  area.val(null);
  area.prop("disabled", isDisabled);

  if (isDisabled) {
    area.removeClass("error");
  }
}

// Init screen
$(document).ready(function () {
  // Display yard position if current pickup has been planned
  if (pickupHistory.status > 0) {
    if (pickupHistory.block) {
      block.val(pickupHistory.block);
      bay.val(pickupHistory.bay);
      row.val(pickupHistory.line);
      tier.val(pickupHistory.tier);
      area.prop("disabled", true);;
    } else {
      area.val(pickupHistory.area);
      block.prop("disabled", true);
      bay.prop("disabled", true);
      row.prop("disabled", true);
      tier.prop("disabled", true);
    }
  }

  block.change(toggleDisableArea);

  bay.change(toggleDisableArea);

  row.change(toggleDisableArea);

  tier.change(toggleDisableArea);

  area.change(function () {
    let isDisabled = !isNullOrEmpty(area.val());
    block.val(null);
    block.prop("disabled", isDisabled);
    bay.val(null);
    bay.prop("disabled", isDisabled);
    row.val(null);
    row.prop("disabled", isDisabled);
    tier.val(null);
    tier.prop("disabled", isDisabled);
    if (isDisabled) {
      block.removeClass("error");
      bay.removeClass("error");
      row.removeClass("error");
      tier.removeClass("error");
    }
  });
});

function submitHandler(index, layero) {
  if ($.validate.form()) {
    $.ajax({
      cache: true,
      type: "POST",
      url: PREFIX + "/edit",
      data: {
        id: pickupHistory.id,
        block: block.val(),
        bay: bay.val(),
        line: row.val(),
        tier: tier.val(),
        area: area.val(),
      },
      async: false,
      error: function (request) {
        $.modal.alertError("System error");
      },
      success: function (data) {
        $.operate.successCallback(data);
        layer.close(index);
        $("#dg").datagrid("reload");
      },
    });
  } else {
    return false;
  }
}
