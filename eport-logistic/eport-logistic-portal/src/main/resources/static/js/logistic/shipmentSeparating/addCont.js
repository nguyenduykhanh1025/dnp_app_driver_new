

$(document).ready(function () {
  let dataSearch = {
    billOfLading: houseBill.masterBillNo
  }
  console.log(dataSearch);
  loadTable(dataSearch);
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