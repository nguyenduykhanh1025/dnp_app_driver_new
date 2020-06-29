
var prefix = "/edo"
$(function () {
  $.ajax({
    type: "GET",
    url: prefix + "/listCarrierCode",
    success(data) {
      console.log(data);
      data.forEach(element => {
        $('#carrierCode').append(`<option value="${element}"> 
                                                  ${element} 
                                                </option>`);
      });

    }
  })
  loadTable();
});
function loadTable(containerNumber, billOfLading,fromDate, toDate) {
  $("#dg").datagrid({
    url: prefix + "/edo",
    method: "GET",
    singleSelect: true,
    height: document.documentElement.clientHeight - 70,
    clientPaging: false,
    pagination: true,
    rownumbers: true,
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
          containerNumber : containerNumber,
          billOfLading : billOfLading,
          fromDate : fromDate,
          toDate : toDate
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

function searchDo() {
  let containerNumber = $("#containerNumber").val() == null ? "" : $("#containerNumber").val();
  let billOfLading = $("#billOfLading").val() == null ? "" : $("#billOfLading").val();
  let fromDate = $("#fromDate").val() == null ? "" : $("#fromDate").val();
  let toDate = $("#toDate").val() == null ? "" : $("#toDate").val();
  loadTable(containerNumber, billOfLading, fromDate, toDate);
}