const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var delegated = new Object();
var toolbar = [
  {
    text: '<a href="#" class="btn btn-sm btn-default"><i class="fa fa-plus text-success"></i> Thêm</a>',
    handler: function () {
      alert("them");
    },
  },
  {
    text: '<a href="#" class="btn btn-sm btn-default"><i class="fa fa-trash text-danger"></i> Xóa</a>',
    handler: function () {
      alert("sua");
    },
  },
  {
    text: '<a href="#" class="btn btn-sm btn-default"><i class="fa fa-refresh text-success"></i> Làm mới</a>',
    handler: function () {
      alert("xoa");
    },
  },
];


$(".main-body").layout();

loadTable();
$(".collapse").click(function () {
  $(".main-body__search-wrapper").height(15);
  $(".main-body__search-wrapper--container").hide();
  $(this).hide();
  $(".uncollapse").show();
})

$(".uncollapse").click(function() {
  $(".main-body__search-wrapper").height(SEARCH_HEIGHT);
  $(".main-body__search-wrapper--container").show();
  $(this).hide();
  $(".collapse").show();
})
$(function(){
  $("#searchAll").textbox('textbox').bind('keydown', function (e) {
    // enter key
    if (e.keyCode == 13) {
      delegated.delegateTaxCode = $("#searchAll").textbox('getText').toUpperCase();
      delegated.delegateCompany = $("#searchAll").textbox('getText').toUpperCase();
      loadTable();
    }
  });
})
function loadTable(msg) {
  if (msg) {
    $.modal.msgSuccess(msg);
  }
  $("#dg").datagrid({
    url: ctx + "logistic/profile/delegated/list",
    height: $(document).height() - $(".main-body__search-wrapper").height() - 50,
    clientPaging: true,
    pagination: true,
    pageSize: 20,
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

function formatDate(value) {
  var date = new Date(value);
  var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  var month = date.getMonth() + 1;
  var monthText = month < 10 ? "0" + month : month;
  return day + "/" + monthText + "/" + date.getFullYear();
}

function formatStatus(value) {
  if (value == 'P') {
    return "<span class='label label-success'>Thanh Toán</span>";
  }
  return "<span class='label label-default'>Làm lệnh</span>";
}
function formatExternalRent(value) {
  if (value != 0) {
    return "<span class='label label-success'>Có</span>";
  }
  return "<span class='label label-default'>Không</span>";
}
function formatAction(value, row, index) {
  var actions = [];
  actions.push('<a class="btn btn-default btn-xs " onclick="assignTruck(\'' + row.id + '\')"><i class="fa fa-eyedropper"></i>Chỉ định xe</a>');
  return actions.join("");
}

$("#delegateType").combobox({
  onSelect: function (val) {
      delegated.delegateType =  val.value;
      loadTable();
  }
});
