const PREFIX = ctx + "reefer-group/receive-cont-empty";
var shipmentDetail = new Object();

$(function () {
  loadTable(shipmentDetail)
});

function loadTable(shipmentDetail) {
  $("#dg").datagrid({
    url: PREFIX + "/supplierReport",
    method: "POST",
    height: $(document).height() - 55,
    clientPaging: true,
    pagination: true,
    singleSelect: true,
    pageSize: 20,
    nowrap: false,
    striped: true,
    rownumbers: true,
    loader: function (param, success, error) {
      shipmentDetail
      console.log("loadTable -> shipmentDetail", shipmentDetail)
      var opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        url: opts.url,
        contentType: "application/json",
        accept: 'text/plain',
        dataType: 'text',
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: shipmentDetail
        }),
        success: function (data) {
          success(JSON.parse(data));
        },

        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

$('#searchAll').keyup(function (event) {
  if (event.keyCode == 13) {
    shipmentDetail = new Object();
    shipmentDetail.containerNo = $('#searchAll').val().toUpperCase();
    shipmentDetail.blNo = $('#searchAll').val().toUpperCase();
    shipmentDetail.bookingNo = $('#searchAll').val().toUpperCase();
    loadTable(shipmentDetail)
  }

});

function searchInfoEdo() {
  shipmentDetail = new Object();
  shipmentDetail.fromDate = stringToDate($("#fromDate").val()).getTime();
  let toDate = stringToDate($("#toDate").val());
  if ($("#fromDate").val() != "" && stringToDate($("#fromDate").val()).getTime() > toDate.getTime()) {
    $.modal.alertError("Quý khách không thể chọn đến ngày thấp hơn từ ngày.");
    $("#toDate").val("");
  } else {
    toDate.setHours(23, 59, 59);
    shipmentDetail.toDate = toDate.getTime();
  };
  shipmentDetail.containerNo = $('#searchAll').val().toUpperCase();
  shipmentDetail.blNo = $('#searchAll').val().toUpperCase();
  shipmentDetail.bookingNo = $('#searchAll').val().toUpperCase();
  loadTable(shipmentDetail)
}

function stringToDate(dateStr) {
  if(dateStr == null || dateStr == undefined)
  {
      return;
  }
  let dateParts = dateStr.split("/");
  return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}

laydate.render({
  elem: '#toDate',
  format: 'dd/MM/yyyy'
});
laydate.render({
  elem: '#fromDate',
  format: 'dd/MM/yyyy'
});


function formatStatus(value) {
  switch (value) {
    case '1':
      return "<span class='label label-success'>Chưa làm lệnh</span>";
    case '2':
      return "<span class='label label-success'>Đã làm lệnh</span>";
    case '3':
      return "<span class='label label-success'>Gate-in</span>";
  }
}