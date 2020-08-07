$("#dg").datagrid({
  url: ctx + 'logistic/shipments',
  height: '300px',
  method: 'post',
  singleSelect: true,
  collapsible: true,
  clientPaging: false,
  pagination: true,
  rownumbers:true,
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
            data: shipmentSearch
          }),
          success: function (data) {
              success(data);
              $("#dg").datagrid("hideColumn", "id");
          },
          error: function () {
              error.apply(this, arguments);
          },
      });
  },
});