const PREFIX = ctx + "edo";
var edo = new Object();

$(function () {
  loadTable(edo)
});

function loadTable(edo) {
  $("#dg").datagrid({
    url: PREFIX + "/edoReport",
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
    edo = new Object();
    edo.containerNumber = $('#searchAll').val().toUpperCase();
    edo.consignee = $('#searchAll').val().toUpperCase();
    edo.billOfLading = $('#searchAll').val().toUpperCase();
    loadTable(edo)
  }

});

function searchInfoEdo() {
  edo = new Object();
  edo.fromDate = stringToDate($("#fromDate").val()).getTime();
  let toDate = stringToDate($("#toDate").val());
  if ($("#fromDate").val() != "" && stringToDate($("#fromDate").val()).getTime() > toDate.getTime()) {
    $.modal.alertError("Quý khách không thể chọn đến ngày thấp hơn từ ngày.");
    $("#toDate").val("");
  } else {
    toDate.setHours(23, 59, 59);
    edo.toDate = toDate.getTime();
  };
  edo.containerNumber = $('#searchAll').val().toUpperCase();
  edo.consignee = $('#searchAll').val().toUpperCase();
  edo.billOfLading = $('#searchAll').val().toUpperCase();
  edo.vessel = $('.c-search-box-vessel').text().trim();
  edo.voyNo = $(".c-search-box-voy-no").text().trim();
  loadTable(edo)
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


// SEARCH INFO VESSEL AREA
$('.c-search-box-vessel').on("select2:opening", function(e) {
  $('.c-search-box-vessel').text(null);
  $('.c-search-box-voy-no').text(null);
  edo = new Object();
  loadTable(edo);
});
$(".c-search-box-vessel").select2({
  theme: "bootstrap",
  placeholder: "Vessel",
  allowClear: true,
  ajax: {
    url: PREFIX + "/getVessel",
    dataType: "json",
    method: "GET",
    data: function (params) {
      return {
        keyString: params.term,
      };
    },
    processResults: function (data) {
      let results = []
      data.forEach(function (element, i) {
        let obj = {};
        obj.id = i;
        obj.text = element;
        results.push(obj);
      })
      return {
        results: results,
      };
    },
  },
});

$('.c-search-box-voy-no').on("select2:opening", function(e) {
  edo = new Object();
  $(this).text(null);
  edo.vessel = $('.c-search-box-vessel').text().trim();
  loadTable(edo);
});
$(".c-search-box-voy-no").select2({
  theme: "bootstrap",
  placeholder: "Voy No",
  allowClear: true,
  ajax: {
    url: PREFIX + "/getVoyNo",
    dataType: "json",
    method: "GET",
    data: function (params) {
      return {
        keyString: params.term,
        vessel : edo.vessel
      };
    },
    processResults: function (data) {
      let results = []
      data.forEach(function (element, i) {
        let obj = {};
        obj.id = i;
        obj.text = element;
        results.push(obj);

      })
      return {
        results: results,
      };
    },
  },
});
// For submit search
$(".c-search-box-vessel").change(function () {
  edo = new Object();
  edo.vessel = $(this).text().trim();
  loadTable(edo);
});
$(".c-search-box-voy-no").change(function () {
  edo = new Object();
  edo.voyNo = $(this).text().trim();
  loadTable(edo);
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