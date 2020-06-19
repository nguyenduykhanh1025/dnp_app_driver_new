var prefix = ctx + "logistic/transport";
$('#validDate').datetimepicker({
    format: "yyyy-mm-dd",
    minView: "month",
    language: 'en',
    autoclose: true
});
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
			  groupName: $('#groupName').val() == null ? "" : $('#groupName').val(),
			  fullName: $('#fullName').val() == null ? "" : $('#fullName').val(),
//			  plateNumber: $('#plateNumber').val() == null ? "" : $('#plateNumber').val(),
			  mobileNumber: $('#mobileNumber').val() == null ? "" : $('#mobileNumber').val(),
			  validDate: $('#validDate').val() == null ? "" : $('#validDate').val(),
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
    return "<span class='label label-success'>Khóa</span>"
  }
  return "<span class='label label-default'>Không</span>"
}
function formatExternalRent(value){
	  if (value != 0) {
		    return "<span class='label label-success'>Có</span>"
		  }
		  return "<span class='label label-default'>Không</span>"
}
function formatAction(value, row, index) {
	var actions = [];
    actions.push('<a class="btn btn-success btn-xs" onclick="editt(\'' + row.id + '\')"><i class="fa fa-edit"></i>Sửa</a> ');
	actions.push('<a class="btn btn-danger btn-xs " onclick="remove(\'' + row.id + '\')"><i class="fa fa-remove"></i>Xóa</a>');
	actions.push('<a class="btn btn-default btn-xs " onclick="assignTruck(\'' + row.id + '\')"><i class="fa fa-eyedropper"></i>Chỉ định xe</a>');
    actions.push("<a class='btn btn-default btn-xs' onclick='resetPwd(" + row.id + ")'><i class='fa fa-key'></i>Đặt lại mật khẩu</a> ");
    return actions.join('');
}
function assignTruck(id){
	$.modal.open("Chỉ định xe", prefix+"/driverTruck/"+id , 800, 300);
}
function remove(id){
	$.modal.confirmTransport("Xác nhận thực hiện xóa thông tin  ?", function() {
	    $.ajax({
	        url: prefix + '/remove',
	        type: 'POST',
	        data: {
	            id: id,
	        }
	    }).done(function(rs) {
	        if(rs.code  == 0){
	        	$.modal.msgSuccess(rs.msg);
	        	loadTable();
	        }
	        else{
	        	$.modal.msgError(rs.msg)
	        }
	    });
	});
}

function editt(id) {
	 $.modal.open("Chỉnh Sửa ", prefix+"/edit/"+id);
}
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

/*function formatGroup(value){
	return value.groupName
}*/
function resetPwd(id) {
  var url = prefix + '/resetPwd/' + id;
  $.modal.open("Đặt lại mật khẩu", url, '500', '350');
}
function reset(){
	$('#groupName').val("");
	$('#fullName').val("");
//	$('#plateNumber').val("");
	$('#mobileNumber').val("");
	$('#validDate').val("");
}
//document.getElementById("groupName").addEventListener("keyup", function (event) {
//	  event.preventDefault();
//	  if (event.keyCode === 13) {
//	    $("#searchBtn").click();
//	  }
//	});
document.getElementById("fullName").addEventListener("keyup", function (event) {
	  event.preventDefault();
	  if (event.keyCode === 13) {
	    $("#searchBtn").click();
	  }
	});
//document.getElementById("plateNumber").addEventListener("keyup", function (event) {
//	  event.preventDefault();
//	  if (event.keyCode === 13) {
//	    $("#searchBtn").click();
//	  }
//	});
document.getElementById("mobileNumber").addEventListener("keyup", function (event) {
	  event.preventDefault();
	  if (event.keyCode === 13) {
	    $("#searchBtn").click();
	  }
	});