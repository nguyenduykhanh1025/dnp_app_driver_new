var prefix = ctx + "logistic/transport";
var fromDate = $("#fromDate").val() == null ? "" : $("#fromDate").val()
fromDate = formatDateForSearch(fromDate);
var toDate = $("#toDate").val() == null ? "" : $("#toDate").val()
toDate = formatDateForSearch(toDate);
$(function () {
loadTable();
});
function loadTable() {
	  $("#dg").datagrid({
	    url: ctx + "logistic/transport/list",
	    height: heightInfo,
	    singleSelect: true,
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
	          // fromDate: fromDate,
	          // toDate: toDate,
	          // voyageNo: $("#voyageNo").val() == null ? "" : $("#voyageNo").val(),
	          // blNo: $("#blNo").val() == null ? "" : $("#blNo").val(),
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
// **********************************
function getSelected() {
	  var row = $("#dg").datagrid("getSelected");
	  if (row) {
	    transportSelected = row.id;
	    $(function() {
	      var options = {
	        createUrl: prefix + "/add",
	        updateUrl: prefix + "/edit/" + transportSelected,
	        modalName: " Đội Xe"
	      };
	      $.table.init(options);
	    });
//	    $("#loCode").text(row.id);
//	    $("#taxCode").text(row.taxCode);
//	    $("#quantity").text(row.containerAmount);
//	    loadShipmentDetail(row.id);
	  }
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
    return "<span class='label label-success'>Disable</span>"
  }
  return "<span class='label label-default'>Nomal</span>"
}
function formatAction(value, row, index) {
    var actions = [];
    actions.push('<a class="btn btn-success btn-xs" href="javascript:void(0)" onclick="$.operate.edit(\'' + row.id + '\')"><i class="fa fa-edit"></i>Sửa</a> ');
    actions.push('<a class="btn btn-danger btn-xs " href="javascript:void(0)" onclick="remove(\'' + row.id + '\')"><i class="fa fa-remove"></i>Xóa</a>');
    actions.push("<a class='btn btn-default btn-xs' href='javascript:void(0)' onclick='resetPwd(" + row.id + ")'><i class='fa fa-key'></i>Đặt lại mật khẩu</a> ");
    return actions.join('');
}
function remove(id){
    $.ajax({
        url: prefix + '/remove',
        type: 'POST',
        dataType: 'html',
        data: {
            id: id,
        }
    }).done(function(rs) {
        
    });
}
//function formatDocumentStatus(value) {
//  if (value != 0) {
//    return "<span class='label label-success'>Đã nhận DO gốc</span>"
//  }
//  return "<span class='label label-default'>Chưa nhận DO gốc</span>"
//}

//function formatBL(value) {
//  return "<a onclick='viewBL(\"" + value + "\")'>" + value + "</a>";
//}

function searchDo() {
  var fromDate = $("#fromDate").val() == null ? "" : $("#fromDate").val()
  fromDate = formatDateForSearch(fromDate);
  var toDate = $("#toDate").val() == null ? "" : $("#toDate").val()
  toDate = formatDateForSearch(toDate);
  var dg = $("#dg").datagrid({
    url: ctx + "carrier/do/list",
    singleSelection: true,
    clientPaging: false,
    pagination: true,
    rownumbers: true,
    pageSize: 50,
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
          fromDate: fromDate,
          toDate: toDate,
          vessel: $("#vessel").val() == null ? "" : $("#vessel").val(),
          blNo: $("#blNo").val() == null ? "" : $("#blNo").val(),
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

document.getElementById("vessel").addEventListener("keyup", function (event) {
  event.preventDefault();
  if (event.keyCode === 13) {
    $("#searchBtn").click();
  }
});
document.getElementById("blNo").addEventListener("keyup", function (event) {
  event.preventDefault();
  if (event.keyCode === 13) {
    $("#searchBtn").click();
  }
});

function viewBL(value) {
  $.modal.openTab("BL#" + value, "/carrier/do/viewbl/" + value);
}

// add full size do
function addTransport(id) {
    $(function() {
	      var options = {
	        createUrl: prefix + "/add",
	        modalName: "Đội Xe"
	      };
	      $.table.init(options);
	    });
	$.operate.addTransportAccount();
}

//function addChangeExpired(id) {
//  table.set();
//  var url = $.common.isEmpty(id) ? table.options.createUrl : table.options.createUrl.replace("{id}", id);
//  $.modal.openDo("Thay đổi hạn lệnh", url, 600, 400);
//}

function formatDateForSearch(value) {
  if (value == null) {
    return;
  }
  var newdate = value.split("/").reverse();
  var date = new Date(newdate)
  var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  var month = date.getMonth() + 1;
  var monthText = month < 10 ? "0" + month : month;
  return date.getFullYear() + "-" + monthText + "-" + day;
}
function resetPwd(id) {
  var url = prefix + '/resetPwd/' + id;
  $.modal.open("Đặt lại mật khẩu", url, '500', '350');
}