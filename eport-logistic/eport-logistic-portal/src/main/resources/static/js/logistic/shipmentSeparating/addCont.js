const PREFIX = ctx + "logistic/shipmentSeparating";

$(document).ready(function () {
  let dataSearch = {
    billOfLading: houseBill.masterBillNo
  }
  loadTable(dataSearch);
});

function loadTable(dataSearch) {
  if (dataSearch == null) {
    $("#dg").datagrid({});
  } else {
    $("#dg").datagrid({
      url: PREFIX + "/separate/search",
      height: document.documentElement.clientHeight - 125,
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

function formatStatus(value) {
  if (parseInt(value) <= 1) {
    return '<span class="badge badge-primary">Chưa làm lệnh</span>';
  } else {
    return '<span class="badge badge-success">Đã làm lệnh</span>';
  }
}

function formatExpiredDem(value) {
  if (value) {
    return value.substring(8, 10) + '/' + value.substring(5, 7) + '/' + value.substring(0, 4) + value.substring(10, 19);
  }
  return '';
}

function submitHandler(index, layer, dg, edoHouseBill) {
  let rows = $("#dg").datagrid("getSelections");
  if (rows == null || rows.length == 0) {
    $.modal.alertWarning("Không có cont để tách, vui lòng kiểm tra lại.");
    return false;
  }

  let edoIds = rows.map((e) => e.id);

  let reqData = {
    houseBill: edoHouseBill.houseBillNo,
    orderNumber: edoHouseBill.orderNumber,
    consignee2TaxCode: edoHouseBill.consignee2TaxCode,
    edoIds: edoIds,
  };
  $.ajax({
    url: PREFIX + "/separate/add",
    method: "POST",
    contentType: "application/json",
    data: JSON.stringify(reqData),
    async: false,
    error: function (request) {
      $.modal.alertError("System error");
    },
    success: function (result) {
      if (result.code == web_status.SUCCESS) {
        $.modal.alert("Thành công!");
        parent.reload();
        layer.close(index);
      } else if (result.code == web_status.WARNING) {
        $.modal.alertWarning(result.msg);
      } else {
        $.modal.alertError(result.msg);
      }
    },
  });
}
