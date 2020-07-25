const PREFIX= ctx + "edo";
var edo = new Object();

$(function () {
  loadTable(edo)
});
function loadTable(edo)
{
  $("#dg").datagrid({
    url: PREFIX + "/edoReport",
    method: "POST",
    height: $(document).height() - 70,
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
      edo.containerNumber = $('#searchAll').val().toUpperCase();
      edo.consignee = $('#searchAll').val().toUpperCase();
      edo.vessel = $('#searchAll').val().toUpperCase();
      edo.voyNo = $('#searchAll').val().toUpperCase();
      loadTable(edo)
  }

});

function searchInfoEdo() {
  edo.fromDate = stringToDate($("#fromDate").val()).getTime();
  let toDate = stringToDate($("#toDate").val());
  if ($("#fromDate").val() != "" && stringToDate($("#fromDate").val()).getTime() > toDate.getTime()) {
    $.modal.alertError("Quý khách không thể chọn đến ngày thấp hơn từ ngày.");
    $("#toDate").val("");
  } else {
    toDate.setHours(23, 59, 59);
    edo.toDate = toDate.getTime();
    loadTable(edo)
  };
  edo.containerNumber = $('#searchAll').val().toUpperCase();
  edo.consignee = $('#searchAll').val().toUpperCase();
  edo.vessel = $('#searchAll').val().toUpperCase();
  edo.voyNo = $('#searchAll').val().toUpperCase();
  loadTable(edo)
}

function stringToDate(dateStr) {
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
$(".c-search-box-vessel").select2({
  placeholder: "Vessel",
  allowClear: true,
  minimumInputLength: 2,
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

$(".c-search-box-vessel-code").select2({
  placeholder: "Vessel Code",
  allowClear: true,
  minimumInputLength: 2,
  ajax: {
    url: PREFIX + "/getVesselCode",
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

$(".c-search-box-voy-no").select2({
  placeholder: "Voy No",
  allowClear: true,
  minimumInputLength: 2,
  ajax: {
    url: PREFIX + "/getVoyNo",
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
// For submit search
$(".c-search-box-vessel").change(function () {
  edo = new Object();
  edo.vessel = $(this).text().trim();
  $(this).text(null);
  loadTable(edo);
});
$(".c-search-box-vessel-code").change(function () {
  edo = new Object();
  edo.vesselNo = $(this).text().trim();
  $(this).text(null);
  loadTable(edo);
});
$(".c-search-box-voy-no").change(function () {
  edo = new Object();
  edo.voyNo = $(this).text().trim();
  $(this).text(null);
  loadTable(edo);
});
