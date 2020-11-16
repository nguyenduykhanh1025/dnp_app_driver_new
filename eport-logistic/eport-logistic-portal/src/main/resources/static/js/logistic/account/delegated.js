const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var delegated = new Object();
$(function(){
  loadTable();
  dgResize();
  $("#searchAll").textbox('textbox').bind('keydown', function (e) {
    // enter key
    if (e.keyCode == 13) {
      loadTable();
    }
  });
})

function loadTable(msg) {
    delegated.delegateTaxCode = $("#searchAll").textbox('getText').toUpperCase();
    delegated.delegateCompany = $("#searchAll").textbox('getText').toUpperCase();
  if (msg) {
    $.modal.msgSuccess(msg);
  }
  $("#dg").datagrid({
    url: ctx + "logistic/profile/delegated/list",
    height: $(document).height() - $(".main-body__search-wrapper").height() - 50,
    clientPaging: true,
    pagination: true,
    pageSize: 20,
    singleSelect: true,
    onClickRow: function () {
      getSelected();
    },
    nowrap: false,
    striped: true,
    rownumbers: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        url: opts.url,
        contentType: "application/json",
        accept: "text/plain",
        dataType: "text",
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data : delegated
        }),
        dataType: "json",
        success: function (data) {
          success(data);
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

function refresh() {
	$('#searchAll').textbox('setText', '');
    shipmentDetail = new Object();
    loadTable();
}
function formatDate(value) {
  var date = new Date(value);
  var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  var month = date.getMonth() + 1;
  var monthText = month < 10 ? "0" + month : month;
  return day + "/" + monthText + "/" + date.getFullYear();
}

function formatStatus(value) {
  if (value == 'P') {
    return "Làm lệnh";
  }
  return "Thanh Toán";
}

$("#delegateType").combobox({
  onSelect: function (val) {
      delegated.delegateType =  val.value;
      loadTable();
  }
});
