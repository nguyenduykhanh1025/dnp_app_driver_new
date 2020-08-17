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

  $("#formSeparate").hide();
});

function loadTable(dataSearch) {
  if (dataSearch == null) {
    $("#dg").datagrid({});
  } else {
    $("#dg").datagrid({
      url: PREFIX + "/separate/search",
      height: document.documentElement.clientHeight - 230,
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
              $("#formSeparate").hide();
            } else {
              $("#formSeparate").show();
            }
            success(data);
            $("#dg").datagrid("hideColumn", "id");
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

$("#btnSearch").on("click", function () {
  let dataSearch = {
    billOfLading: $("input[name='billOfLading']").val(),
    consignee: $("input[name='consignee']").val(),
    carrierCode: $("input[name='carrierCode']").val(),
    expiredDem: $("input[name='expiredDem']").val(),
  };
  loadTable(dataSearch);
});

function formatStatus(value) {
  if (parseInt(value) <= 1) {
    return '<span class="badge badge-primary">Chưa làm lệnh</span>';
  } else {
    return '<span class="badge badge-success">Đã làm lệnh</span>';
  }
}

function submitHandler(index, layer, dg) {
  let rows = $("#dg").datagrid("getSelections");
  if (rows == null || rows.length == 0) {
    $.modal.alertWarning("Không có cont để tách, vui lòng kiểm tra lại.");
    return false;
  }

  let edoIds = rows.map((e) => e.id);

  if ($.validate.form()) {
    let reqData = {
      houseBill: $("input[name='houseBill']").val(),
      consignee2: $("input[name='consignee2']").val(),
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
    return false;
  }
}
