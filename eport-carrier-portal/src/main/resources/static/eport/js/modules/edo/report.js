const PREFIX= ctx + "edo";
var edo = new Object();
var firtarrierCode;
if (contFE == "F") {
  $('#toDate').remove();
  $('#fromDate').remove();
}
if (contFE == "E") {
  $('#toDate').remove();
  $('#fromDate').remove();
}

$(function () {
//   $('#toDate').val(getCurrentDayForLoadPage());
//   $('#fromDate').val(getYesterdayForLoadGage());
//   $.ajax({
//     type: "GET",
//     url: PREFIX+ "/listCarrierCode",
//     success(data) {
//       data.forEach(element => {
//         $('#carrierCode').append(`<option value="${element}"> 
//                                                   ${element} 
//                                                 </option>`);
//       });

//     }
//   })
  $("#dg").datagrid({
    url: PREFIX + "/edoReport",
    method: "POST",
        singleSelect: false,
        clientPaging: true,
        pagination: true,
        pageSize: 20,
        nowrap: false,
        striped: true,
        rownumbers:true,
        loader: function (param, success, error) {
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
                    data: edo
                }),
                success: function (data) {
                    success(JSON.parse(data));
                    // let dataTotal = JSON.parse(data);
                    // console.log(dataTotal);
                },

        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
});

function searchContainer() {
  var toDate = $("#toDate").val() == null ? "" : $("#toDate").val()
  var fromDate = $("#fromDate").val() == null ? "" : $("#fromDate").val()

  var validationDate = validateDateSearch(fromDate, toDate);
  if (validationDate == -1) {
    $.modal.alertWarning("Vui lòng chọn ngày bắt đầu là ngày <br> trước ngày kết thúc");
    return;
  }
  if (validationDate == 0) {
    $.modal.alertWarning("Vui lòng chọn khoảng thời gian <br> không quá 40 ngày!");
    return;
  }
}

function formatDateForSearch(value) {
  if (value == null) {
    return;
  }
  var date = new Date(value)

  var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  var month = date.getMonth() + 1;
  var monthText = month < 10 ? "0" + month : month;

  return date.getFullYear() + "-" + monthText + "-" + day;
}

function formatDate(value) {
  if (value == null) {
    return;
  }
  var date = new Date(value);
  var InDate = new Date($("#toDate").val());
  var OutDate = new Date($("#fromDate").val());
  var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  var month = date.getMonth() + 1;
  var monthText = month < 10 ? "0" + month : month;
  var hour = date.getHours();
  hour = hour < 10 ? "0" + hour : hour;
  var minute = date.getMinutes();
  minute = minute < 10 ? "0" + minute : minute;
  var seconds = date.getSeconds();
  seconds = seconds < 10 ? "0" + seconds : seconds;
  if (date > InDate || date < OutDate) {
    return '<span style="color:darkgray;">' + day + "/" + monthText + "/" + date.getFullYear() + " " + hour +
      ":" + minute + ":" + seconds + '</span>';
  }
  return day + "/" + monthText + "/" + date.getFullYear() + " " + hour + ":" + minute + ":" + seconds;
}

function exportExcel() {
  var toDate = $("#toDate").val() == null ? "" : $("#toDate").val()
  var fromDate = $("#fromDate").val() == null ? "" : $("#fromDate").val()
  var validationDate = validateDateSearch(fromDate, toDate);
  searchContainer();
  if (validationDate == -1) {
    $.modal.alertWarning("Vui lòng chọn ngày bắt đầu là ngày <br> trước ngày kết thúc");
    return;
  }
  if (validationDate == 0) {
    $.modal.alertWarning("Vui lòng chọn khoảng thời gian <br> không quá 40 ngày!");
    return;
  }
  $.modal.loading("Đang xử lý, vui lòng chờ...");
  $.ajax({
    type: "POST",
    url: PREFIX+ "/export",
    data: {
      toDate: toDate,
      fromDate: fromDate,
      contFE: contFE,
      cntrNo: $("#cntrNo").val() == null ? "" : $("#cntrNo").val(),
      carrierCode: $("#carrierCode").children("option:selected").val(),
    },
    success: function (result) {
      var nameFile;
      var date = new Date();
      var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      var month = date.getMonth() + 1;
      var monthText = month < 10 ? "0" + month : month;
      var year = date.getFullYear();
      nameFile = "Container_GateInGateOut_report"
      if (contFE == "E") {
        nameFile = "Container_empty_report"
      }
      if (contFE == "F") {
        nameFile = "Container_full_report"
      }
      nameFile = nameFile + "_" + day + monthText + year + "_";

      if (result.code == web_status.SUCCESS) {
        window.location.href = ctx + "common/download/" + nameFile + "?fileName=" + encodeURI(result.msg) +
          "&delete=" +
          true;
      } else if (result.code == web_status.WARNING) {
        $.modal.alertWarning(result.msg)
      } else {
        $.modal.alertError(result.msg);
      }
      $.modal.closeLoading();
    },
  })
}

function cleanRemark(value) {
  if (value == null) {
    return;
  }
  return value.replace("[Remark]", "");
}

function getCurrentDayForLoadPage() {
  var today = new Date();
  var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate() + ' ' + '07:00:00';
  return date;
}

function getYesterdayForLoadGage() {
  var today = new Date();
  var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + (today.getDate() - 1) + ' ' + '07:00:00';
  return date;
}

function formatYardPosition(value) {
  if (value == null) {
    return;
  }
  return value.replace("---", "");
}

function validateDateSearch(fromDate, toDate) {

  if (fromDate == "" || toDate == "") {
    return 1;
  }
  var formatDate1 = new Date(fromDate);
  var toDate1 = new Date(toDate);
  var offset = toDate1.getTime() - formatDate1.getTime();
  var totalDays = Math.round(offset / 1000 / 60 / 60 / 24);
  if (totalDays < 0) {
    return -1;
  } else if (totalDays <= 40) {
    return 1;
  }
  return 0;

}
laydate.render({
  elem: '#fromDate',
  theme: 'grid',
  type: 'datetime',
});

laydate.render({
  elem: '#toDate',
  theme: 'grid',
  type: 'datetime'
});
document.getElementById("cntrNo").addEventListener("keyup", function (event) {
  event.preventDefault();
  if (event.keyCode === 13) {
    $("#searchBtn").click();
  }
});