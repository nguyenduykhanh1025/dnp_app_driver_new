const PREFIX = ctx + "logistic/shipmentSeparating";

$(document).ready(function () {
  $("#expiredDem").datetimepicker({
    language: "en",
    format: "dd/mm/yyyy",
    autoclose: true,
    todayBtn: true,
    todayHighlight: true,
    pickTime: false,
    minView: 2,
  });

  loadTable(null);

  $("select[name='carrierCode']").select2({
    closeOnSelect: true,
    placeholder: 'Chọn hãng tàu',
    allowClear: true
  });
  $("select[name='carrierCode']").val(null).trigger("change");

  // $("select[name='consignee2']").select2({
  //   closeOnSelect: true,
  //   placeholder :'Chọn chủ hàng',
  //   allowClear: true
  // });
});

function loadTable(dataSearch) {
  if (dataSearch == null) {
    $("#dg").datagrid({
      height: document.documentElement.clientHeight - 270,
      collapsible: true,
      clientPaging: false,
      pagination: true,
      rownumbers: true,
      pageSize: 50,
      nowrap: true,
      striped: true,
      loadMsg: " Đang xử lý...",
      loader: function (param, success, error) {
        success([]);
      }
    });
  } else {
    $("#dg").datagrid({
      url: PREFIX + "/separate/search",
      height: document.documentElement.clientHeight - 270,
      method: "post",
      collapsible: true,
      clientPaging: false,
      pagination: true,
      rownumbers: true,
      pageSize: 50,
      nowrap: true,
      striped: true,
      loadMsg: " Đang xử lý...",
      loader: function (param, success, error) {
        let opts = $(this).datagrid("options");
        if (!opts.url) return false;
        $.ajax({
          type: opts.method,
          url: opts.url,
          contentType: "application/json",
          data: JSON.stringify({
            pageNum: param.page,
            pageSize: param.rows,
            orderByColumn: param.sort,
            isAsc: param.order,
            data: dataSearch,
          }),
          success: function (data) {
            if (data.total == 0) {
              $.modal.alertWarning(
                "Không tìm thấy data, vui lòng kiểm tra lại"
              );
            }
            success(data);
            // $("#dg").datagrid("hideColumn", "id");
          },
          error: function () {
            $("#formSeparate").hide();
            error.apply(this, arguments);
          },
        });
      },
    });
  }
}

function checkTaxCodeExists() {
  if ($("input[name='taxCode']").val()) {
    $.ajax({
      url: PREFIX + "/taxcode/" + $("input[name='taxCode']").val() + "/consignee",
      method: "get",
      success: function (res) {
        if (res.code == 0) {
          $("input[name='companyName']").val(res.companyName);
          return true;
        } else {
          $.modal.alertWarning("Mã số thuế không tồn tại.");
          return false;
        }
      },
      error: function (err) {
        $.modal.msgError("Có lỗi xảy ra.");
        return false;
      }
    });
  }
}

function checkTaxCodeExistsRes() {
  return $.ajax({
    url: PREFIX + "/taxcode/" + $("input[name='taxCode']").val() + "/consignee",
    method: "get"
  });
}

function removeError(element) {
  $(element).removeClass("error-input");
}

$("#btnSearch").on("click", function () {
  let dataSearch = {
    billOfLading: $("input[name='billOfLading']").val(),
    orderNumber: $("input[name='orderNumber']").val(),
    carrierCode: $("select[name='carrierCode']").val(),
    expiredDem: $("input[name='expiredDem']").val(),
  };
  let errorFlg = false;
  if (!$("input[name='billOfLading']").val()) {
    errorFlg = true;
    $("input[name='billOfLading']").addClass("error-input");
  }
  if (!$("input[name='orderNumber']").val()) {
    errorFlg = true;
    $("input[name='orderNumber']").addClass("error-input");
  }
  if (!$("select[name='carrierCode']").val()) {
    errorFlg = true;
    // $($("select[name='carrierCode']").select2("container")).addClass("error-input").trigger("change");
  }
  if (!$("input[name='expiredDem']").val()) {
    errorFlg = true;
    $("input[name='expiredDem']").addClass("error-input");
  }
  if (!errorFlg) {
    loadTable(dataSearch);
  }
});

function formatStatus(value) {
  if (parseInt(value) <= 1) {
    return '<span class="badge badge-primary">Chưa làm lệnh</span>';
  } else {
    return '<span class="badge badge-success">Đã làm lệnh</span>';
  }
}

async function submitHandler(index, layer, dg) {
  let rows = $("#dg").datagrid("getSelections");
  if (rows == null || rows.length == 0) {
    $.modal.alertWarning("Không có cont để tách, vui lòng kiểm tra lại.");
    return false;
  }

  let edoIds = rows.map((e) => e.id);

  if ($.validate.form()) {
    let res = await checkTaxCodeExistsRes();
    if (res.code == 0) {
      $("input[name='taxCode']").val(res.companyName);
      let reqData = {
        houseBill: $("input[name='houseBill']").val(),
        orderNumber: $("input[name='orderNumberRegister']").val(),
        consignee2TaxCode: $("input[name='taxCode']").val(),
        edoIds: edoIds,
      };
      $.ajax({
        cache: true,
        type: "POST",
        url: PREFIX + "/separate/execute",
        contentType: "application/json",
        data: JSON.stringify(reqData),
        async: false,
        error: function (request) {
          $.modal.alertError("System error");
        },
        success: function (result) {
          if (result.code == web_status.SUCCESS) {
            $.modal.alert("Thành công!");
            layer.close(index);
            dg.datagrid("reload");
          } else if (result.code == web_status.WARNING) {
            $.modal.alertWarning(result.msg);
          } else {
            $.modal.alertError(result.msg);
          }
        },
      });
    } else {
      $.modal.alertWarning("Mã số thuế không tồn tại.");
    }
  } else {
    return false;
  }
}
