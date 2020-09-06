const PREFIX = ctx + "om/support";
var shipmentDetail = new Object;
$(document).ready(function () {
	shipmentDetail.shipmentId = shipmentId;
	loadTableByContainer(shipmentDetail);
});
function loadTableByContainer(shipmentDetail) {
  $("#dg").datagrid({
    url: PREFIX + "/shipmentDetails",
    method: "POST",
    height: $(document).height() - 60,
    singleSelect: true,
    clientPaging: true,
    pagination: true,
    pageSize: 20,
    nowrap: false,
    striped: true,
    rownumbers: true,
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
      if (shipmentDetail == null) {
        return false;
      }
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
          data: shipmentDetail,
        }),
        success: function (data) {
        	let rs =JSON.parse(data);
        	$("#bookingNo").text(rs.rows[0].bookingNo);
        	$("#vslNm").text(rs.rows[0].vslNm + " - " + rs.rows[0].vslName + " - " + rs.rows[0].voyCarrier);
        	$("#opeCode").text(rs.rows[0].opeCode);
        	$("#consignee").text(rs.rows[0].consignee);
          success(JSON.parse(data));
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}