var prefix = ctx + "logistic/logisticTruck";
$(function () {
    loadTable();
});
function loadTable() {
    $("#dg").datagrid({
      url: prefix + "/list",
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
			plateNumber: $('#plateNumber').val() == null ? "" : $('#plateNumber').val(),
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
    let row = $("#dg").datagrid("getSelected");
    if (row) {
      let selected = row.id;
      $(function() {
        var options = {
          createUrl: prefix + "/add",
          updateUrl: prefix + "/edit/" + selected,
          modalName: " Đội Xe"
        };
        $.table.init(options);
      });
    }
}
//format
function formatDate(value) {
	if(value != null){
	    let date = new Date(value);
	    let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	    let month = date.getMonth() + 1;
	    let monthText = month < 10 ? "0" + month : month;
	    return day + "/" + monthText + "/" + date.getFullYear();
	}
	return null;
}
function formatType(value) {
    if (value == '0') {
      return "Đầu kéo";
    }
    return "Rơ mooc";
}
function formatAction(value, row, index) {
	var actions = [];
    actions.push('<a class="btn btn-success btn-xs" onclick="editt(\'' + row.id + '\')"><i class="fa fa-edit"></i>Sửa</a> ');
    actions.push('<a class="btn btn-danger btn-xs " onclick="remove(\'' + row.id + '\')"><i class="fa fa-remove"></i>Xóa</a>');
    return actions.join('');
}
//
function remove(id){
    console.log(id)
	$.modal.confirmTruck("Xác nhận thực hiện xóa thông tin  ?", function() {
	    $.ajax({
	        url: prefix + '/remove/',
	        type: 'POST',
	        data: {
	            ids: id,
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
function addTruck(id) {
    $(function() {
	      var options = {
	        createUrl: prefix + "/add",
	        modalName: "Đội Xe"
	      };
	      $.table.init(options);
	    });
	$.operate.addTruck();
}
function reset(){
	$('#plateNumber').val("");
}
document.getElementById("plateNumber").addEventListener("keyup", function (event) {
	  event.preventDefault();
	  if (event.keyCode === 13) {
	    $("#searchBtn").click();
	  }
	});