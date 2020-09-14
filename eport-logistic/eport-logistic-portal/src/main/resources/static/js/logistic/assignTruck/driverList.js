var prefix = ctx + "logistic/assignTruck";
$(document).ready(function () {
	loadDriver(shipmentId)
});
function loadDriver(shipmentId){
	//TH pickedIds rỗng
	if(pickedIds[0] == -1){
		pickedIds = [];
	}
    //ds tai xe chua dieu, da co truck
  var dg = $("#driverTable").datagrid({
  url: prefix + "/listDriverAccount",
  height: window.innerHeight,
  collapsible: true,
  clientPaging: false,
  nowrap: false,
  striped: true,
  loadMsg: " Đang xử lý...",
  loader: function (param, success, error) {
      let opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
          type: "GET",
          url: opts.url,
          data: {
              shipmentId: shipmentId,
              pickedIds: pickedIds
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
  dg.datagrid('enableFilter');
}
function submitHandler() {
    if ($.validate.form()) {
    	let rows = $('#driverTable').datagrid('getSelections');
        if(rows){
            parent.appendDriverList(rows);
            $.modal.close();
        }
    }
}
