const PREFIX = ctx + "mc/plan";

//var block = $("input[name='block']");
//var bay = $("input[name='bay']");
//var row = $("input[name='row']");
//var tier = $("input[name='tier']");
//var area = $("input[name='area']");
var block = $("#block").val();
var bay = $("#bay").val();
var row = $("#row").val();
var tier = $("#tier").val();
var area = $("#area").val();
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
    // init block select 2
    $('.block').select2({
        ajax: {
            url: PREFIX + '/block/list',
            method: 'get',
            delay: 250,
            data: function (params) {
                return {
                    keyword: params.term == null ? "" : params.term,
                }
            },
            processResults: function (data, params) {
                return {
                    results: data
                }
            }
        },
        placeholder: 'Chọn',
        minimumInputLength: 0,
    });
    // init bay select 2
    $('.bay').select2({
        ajax: {
            url: PREFIX + '/bay/list',
            method: 'get',
            delay: 250,
            data: function (params) {
                return {
                    keyword: params.term == null ? "" : params.term,
                    sztp: pickupHistory.shipmentDetail.sztp
                }
            },
            processResults: function (data, params) {
                return {
                    results: data
                }
            }
        },
        placeholder: 'Chọn',
        minimumInputLength: 0,
    });
    // init row select 2
    $('.row').select2({
        ajax: {
            url: PREFIX + '/row/list',
            method: 'get',
            delay: 250,
            data: function (params) {
                return {
                    keyword: params.term == null ? "" : params.term,
                }
            },
            processResults: function (data, params) {
                return {
                    results: data
                }
            }
        },
        placeholder: 'Chọn',
        minimumInputLength: 0,
    });
    // init tier select 2
    $('.tier').select2({
        ajax: {
            url: PREFIX + '/tier/list',
            method: 'get',
            delay: 250,
            data: function (params) {
                return {
                    keyword: params.term == null ? "" : params.term,
                }
            },
            processResults: function (data, params) {
                return {
                    results: data
                }
            }
        },
        placeholder: 'Chọn',
        minimumInputLength: 0,
    });
    // init area select 2
    $('.area').select2({
        ajax: {
            url: PREFIX + '/area/list',
            method: 'get',
            delay: 250,
            data: function (params) {
                return {
                    keyword: params.term == null ? "" : params.term,
                }
            },
            processResults: function (data, params) {
                return {
                    results: data
                }
            }
        },
        placeholder: 'Chọn',
        minimumInputLength: 0,
    });
    // when select option Block, bay, row, tier, area
    $('#block').change(function () {
        block = $("#block").val();
    });
    $('#bay').change(function () {
        bay = $("#bay").val();
    });
    $('#row').change(function () {
        row = $("#row").val();
    });
    $('#tier').change(function () {
        tier = $("#tier").val();
    });
    $('#area').change(function () {
        area = $("#area").val();
    });
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
        block: block,
        bay: bay,
        line: row,
        tier: tier,
        area: area,
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
