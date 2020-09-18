var prefix_main = ctx + "logistic/shipmentSeparating";
var houseBillSelected;
var toolbar = [
  {
    text: '<button type="submit" class="btn btn-w-m btn-rounder btn-success"><i class="fa fa-plus"></i> Tách lô từ Master Bill</button>',
    handler: function () {
      openFormSeparate();
    },
  },
  {
    text: '<button type="submit" class="btn btn-w-m btn-rounder btn-danger"><i class="fa fa-trash"></i> Xóa lô</button>',
    handler: function () {
      //alert("sua");
    },
  },
];

$(".main-body").layout();
$(".collapse").click(function () {
  $(".main-body__search-wrapper").hide();
  $(".main-body__search-wrapper--container").hide();
  $(this).hide();
  $(".uncollapse").show();
});

$(".uncollapse").click(function () {
  $(".main-body__search-wrapper").show();
  $(".main-body__search-wrapper--container").show();
  $(this).hide();
  $(".collapse").show();
});

$(".left-side__collapse").click(function () {
  $("#main-layout").layout("collapse", "west");
  setTimeout(() => {
    hot.render();
  }, 200);
});

$("#main-layout").layout({
  onExpand: function (region) {
    if (region == "west") {
      hot.render();
    }
  },
});

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {

  loadTable();
  $("#dg2").datagrid({
    height: $('.main-body').height() - 110
  });
});

// LOAD SHIPMENT LIST
function loadTable() {
  $("#dg").datagrid({
    url: prefix_main + "/houseBill/list",
    height: $('.main-body').height() - 85,
    method: "post",
    toolbar: toolbar,
    singleSelect: true,
    collapsible: true,
    clientPaging: false,
    pagination: true,
    rownumbers: true,
    onClickRow: function () {
      getSelected();
    },
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
          data: {},
        }),
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

// FORMAT DATE FOR SHIPMENT LIST
function formatDate(value) {
  let date = new Date(value);
  let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  let month = date.getMonth() + 1;
  let monthText = month < 10 ? "0" + month : month;
  let hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
  let minutes =
    date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
  let seconds =
    date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
  return (
    day +
    "/" +
    monthText +
    "/" +
    date.getFullYear() +
    " " +
    hours +
    ":" +
    minutes +
    ":" +
    seconds
  );
}

// HANDLE WHEN SELECT A SHIPMENT
function getSelected() {
  let row = $("#dg").datagrid("getSelected");
  if (row) {
    houseBillSelected = row;
    $("#masterBillNo").text(row.masterBillNo);
    $("#consignee").text(row.consignee2);
    loadHouseBillDetail(row.houseBillNo);
  }
}

function loadHouseBillDetail(houseBillNo) {
  $("#dg2").datagrid({
    url: ctx + "logistic/shipmentSeparating/houseBill/detail",
    height: $('.main-body').height() - 85,
    method: "post",
    collapsible: true,
    clientPaging: false,
    pagination: false,
    rownumbers: true,
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
          data: houseBillNo,
        }),
        success: function (data) {
          success(data);
          $("#dg2").datagrid("hideColumn", "id");
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

function openFormSeparate() {
  const options = {
    url: prefix_main + "/separate",
    title: "Tách Lô Từ Master Bill",
    width: '1200',
    skin: "custom-modal",
    btn: ["Xác Nhận Tách Bill", "Đóng"],
    yes: function (index, layero) {
      let iframeWin = layero.find("iframe")[0];
      iframeWin.contentWindow.submitHandler(index, layer, $("#dg"));
    },
  };
  $.modal.openOptions(options);
}

function openFormAddCont() {
  let row = $("#dg").datagrid("getSelected");
  if (row) {
    const options = {
      url: prefix_main + "/addCont/" + row.houseBillNo,
      title: "Thêm cont vào House Bill",
      skin: "custom-modal",
      btn: ["Xác Nhận Thêm Cont", "Đóng"],
      yes: function (index, layero) {
        let iframeWin = layero.find("iframe")[0];
        iframeWin.contentWindow.submitHandler(index, layer, $("#dg2"));
      },
    };
    $.modal.openOptions(options);
  }
}

function formatStatus(value, row, index) {
  if (parseInt(row.edo.status) <= 1) {
    return '<span class="badge badge-primary">Chưa làm lệnh</span>';
  } else {
    return '<span class="badge badge-success">Đã làm lệnh</span>';
  }
}

function formatExpiredDem(value, row, index) {
  return formatDate(row.edo.expiredDem);
}

function printHouseBill(){
	if(!houseBillSelected){
		console.log(houseBillSelected)
		$.modal.alertError("Bạn chưa chọn House Bill!");
		return
	}
	$.modal.openTab("In House Bill", ctx +"logistic/print/house-bill/" + houseBillSelected.houseBillNo);
}