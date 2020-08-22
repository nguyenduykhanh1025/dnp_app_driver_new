const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var toolbar = [
  {
    text: "Thêm",
    handler: function () {
      alert("them");
    },
  },
  "-",
  {
    text: "Sửa",
    handler: function () {
      alert("sua");
    },
  },
  "-",
  {
    text: "Xóa",
    handler: function () {
      alert("xoa");
    },
  },
  "-",
  {
    text: "Đặt lại mật khẩu",
    handle: function () {
        alert("reset");
    }
  }
];


$(".main-body").layout();

loadTable();
$(".collapse").click(function () {
    $(".main-body__search-wrapper").height(5);
    $(".main-body__search-wrapper form").hide();
    $(".main-body__search-wrapper--btn-group").hide();
    $(this).hide();
    $(".uncollapse").show();
})

$(".uncollapse").click(function() {
    $(".main-body__search-wrapper").height(SEARCH_HEIGHT);
    $(".main-body__search-wrapper form").show();
    $(".main-body__search-wrapper--btn-group").show();
    $(this).hide();
    $(".collapse").show();
})

function loadTable(msg) {
  if (msg) {
    $.modal.msgSuccess(msg);
  }
  $("#dg").datagrid({
    url: ctx + "logistic/transport/list",
    height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
    singleSelect: true,
    toolbar: toolbar,
    collapsible: true,
    clientPaging: false,
    pagination: true,
    onClickRow: function () {
      getSelected();
    },
    pageSize: 50,
    nowrap: false,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        url: opts.url,
        data: {
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          groupName: "",
          fullName: "",
          mobileNumber: "",
          validDate: "",
        },
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
  if (value != 0) {
    return "<span class='label label-success'>Khóa</span>";
  }
  return "<span class='label label-default'>Không</span>";
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
