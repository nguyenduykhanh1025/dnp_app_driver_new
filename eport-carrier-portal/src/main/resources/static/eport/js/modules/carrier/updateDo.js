var PREFIX = ctx + "carrier/do";
var doList;
var hot;
var consigneeList = [];
var emptyDepotList = [];
var vesselList = [];
getOptionsColumn()
$("#billNo").html("No: " + firstDo.billOfLading)
$("#billNumber").html(firstDo.billOfLading)
$("#carrier").html(firstDo.carrierCode)
var date = new Date(firstDo.expiredDem)
var status = firstDo.status == 0 ? "<span class='label label-warning'>Chưa làm lệnh</span>" : "<span class='label label-success'>Đã làm lệnh</span>"
if (firstDo.status == 1) {
  $("#updateExpriredBtn").show();
} else {
  $("#updateExpriredBtn").hide();
}
$("#dostatus").html(status)
$("#expiredDem").html(
  date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear()
)
loadTable();

function loadTable() {
  $.ajax({
    url: PREFIX + "/getInfoBl",
    method: "get",
    data: {
      blNo: firstDo.billOfLading,
    },
    success: function (result) {
      doList = result
      var example = document.getElementById("showDemo")
      hot = new Handsontable(example, {
        data: doList,
        stretchH: "all",
        width: "100%",
        rowHeights: 30,
        manualColumnResize: true,
        className: "htMiddle",
        //manualRowResize: true,
        minSpareRows: 1,
        fillHandle: {
          autoInsertRow: true,
        },
        height: document.documentElement.clientHeight - 70,
        colHeaders: [
          "Hãng tàu <br>OPR Code",
          "Số vận đơn <br> B/L No",
          "Số Container <br>Container No.",
          "Tên khách hàng <br>Consignee",
          "Hạn lệnh <i class='red'>(*)</i><br> Valid to date",
          "Nơi hạ vỏ <i class='red'>(*)</i> <br> Empty depot",
          "Ngày miễn lưu <i class='red'>(*)</i><br> DET Freetime",
          "Tên tàu<br>Vessel",
          "Chuyến<br>Voyage",
          "Trọng tải <br> Weight",
          "Số seal <br> Seal No",
          "Lịch sử",
          "Ghi chú",
          "ID",
        ],
        columns: [
          {
            data: "carrierCode",
            editor: false,
            readOnly : true
          },
          {
            data: "billOfLading",
            editor: false,
            readOnly : true
          },
          {
            data: "containerNumber",
            type: "text",
            readOnly : true
          },
          {
            data: "consignee",
            type: "autocomplete",
            source: consigneeList,
            strict: false,
            readOnly : true
          },
          {
            data: "expiredDem",
            type: "date",
            dateFormat: "DD/MM/YYYY",
            correctFormat: true,
          },
          {
            data: "emptyContainerDepot",
            type: "autocomplete",
            source: emptyDepotList,
            strict: false,
          },
          {
            data: "detFreeTime",
            type: "date",
            dateFormat: "DD/MM/YYYY",
            correctFormat: true,
            type: "numeric",
          },
          {
            data: "vessel",
            type: "autocomplete",
            source: vesselList,
            strict: false,
            readOnly : true
          },
          {
            data: "voyNo",
            type: "text",
            readOnly : true
          },
          {
            data: "weight",
            type: "text",
            readOnly : true
          },
          {
            data: "sealNo",
            type: "text",
            readOnly : true
          },
          {
            data: "id",
            type: "text",
            renderer: historyRenderer,
            readOnly : true
          },
          {
            data: "remark",
            type: "text",
          },
          {
            data: "id",
            // readOnly: true,
          },
        ],
        rowHeaders: true,
        columnSorting: {
          indicator: true,
        },
        colWidths: [70, 70, 80, 160, 90, 140, 110, 80, 80, 80, 80, 80, 150, 0.1],
        manualColumnMove: true,
      })
      hot.validateCells()
      if (firstDo.status == 1) {
        $("#saveDoBtn").hide();
        $("#tips").hide();
        hot.updateSettings({
          cells: (row, col, prop) => {
            var cellProperties = {};
            cellProperties.editor = false
            return cellProperties
          },
        })
      }
    },
  })
}


function getOptionsColumn() {
  $.ajax({
    url: PREFIX + "/getListOptions",
    method: "get",
  }).done(function (result) {
    var list1 = result.consigneeList
    var list2 = result.emptyDepotList
    var list3 = result.vesselList
    for (var i = 0; i < list1.length; i++) {
      if (list1[i] != null) {
        consigneeList.push(list1[i])
      }
    }
    for (var i = 0; i < list2.length; i++) {
      if (list1[i] != null) {
        emptyDepotList.push(list2[i])
      }
    }
    for (var i = 0; i < list3.length; i++) {
      if (list1[i] != null) {
        vesselList.push(list3[i])
      }
    }
  })
}

function isGoodDate(dt) {
  var reGoodDate = /^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|(([1][26]|[2468][048]|[3579][26])00))))$/g
  return reGoodDate.test(dt)
}

function updateDO() {
  var myTableData = hot.getSourceData()
  // If the last row is empty, remove it before validation
  if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
    // Remove the last row if it's empty
    hot.alter(
      "remove_row",
      parseInt(myTableData.length - 1),
      (keepEmptyRows = false)
    )
  }
  var cleanedGridData = []
  $.each(myTableData, function (rowKey, object) {
    if (!hot.isEmptyRow(rowKey)) {
      cleanedGridData.push(object)
    }
  })
  var doList = []
  var errorFlg = false
  $.each(cleanedGridData, function (index, item) {
    var doObj = new Object()
    if (!isGoodDate(item["expiredDem"]) || item["expiredDem"] == null) {
      $.modal.alertError(
        "Có lỗi tại hàng [" +
          (index + 1) +
          "].<br>Lỗi: Hạn lệnh đang để trống hoặc chưa đúng format."
      )
      errorFlg = true
      return
    }
    var date = new Date(
      item["expiredDem"].replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$2/$1/$3")
    )
    date.setHours(0, 0, 1, 1)
    var dateValidate = new Date()
    dateValidate.setHours(0, 0, 0, 0)
    if (date.getTime() < dateValidate.getTime()) {
      $.modal.alert(
        "Có lỗi tại hàng [" +
          (index + 1) +
          "].<br>Lỗi: Hạn lệnh không được nhỏ hơn ngày hiện tại."
      )
      errorFlg = true
      return
    }
    doObj.id = item["id"]
    doObj.carrierCode = firstDo.carrierCode
    doObj.billOfLading = firstDo.billOfLading
    doObj.containerNumber = item["containerNumber"]
    doObj.consignee = item["consignee"]
    doObj.expiredDem = date.getTime()
    doObj.detFreeTime = item["detFreeTime"]
    doObj.emptyContainerDepot = item["emptyContainerDepot"]
    doObj.voyNo = item["voyNo"]
    doObj.vessel = item["vessel"]
    doObj.remark = item["remark"]

    doList.push(doObj)
  })
  $.each(doList, function (index, item) {
    if (item["carrierCode"] == null) {
      $.modal.alertError(
        "Có lỗi tại hàng [" +
          (index + 1) +
          "].<br>Lỗi: Mã khách hàng không được trống."
      )
      errorFlg = true
      return false
    }
    if (item["billOfLading"] == null) {
      $.modal.alertError(
        "Có lỗi tại hàng [" +
          (index + 1) +
          "].<br>Lỗi: Số vận đơn không được trống."
      )
      errorFlg = true
      return false
    }

    if (item["containerNumber"] == null) {
      $.modal.alertError(
        "Có lỗi tại hàng [" +
          (index + 1) +
          "].<br>Lỗi: Số container không được trống."
      )
      errorFlg = true
      return false
    }

    if (item["consignee"] == null) {
      $.modal.alertError(
        "Có lỗi tại hàng [" +
          (index + 1) +
          "].<br>Lỗi: Tên khách hàng không được trống."
      )
      errorFlg = true
      return false
    }
    var regexNuber = /^[0-9]*$/
    if (item["detFreeTime"] != null) {
      if (!regexNuber.test(item["detFreeTime"])) {
        $.modal.alertError(
          "Có lỗi tại hàng [" +
            (index + 1) +
            "].<br>Lỗi: Số ngày miễn lưu vỏ phải là số."
        )
        errorFlg = true
        return false
      }
    }
  })
  if (errorFlg) {
    return
  }

  $.modal.confirm(
    "Bạn có chắc chắn muốn cập nhật DO không?",
    function () {
      $.ajax({
        url: ctx + "carrier/do/update/" + firstDo.billOfLading,
        method: "post",
        contentType: "application/json",
        accept: "text/plain",
        data: JSON.stringify(doList),
        dataType: "text",
        success: function (data) {
          var result = JSON.parse(data)
          if (result.code == 0) {
            $.modal.confirm("Cập nhật DO thành công!", function () {}, {
              title: "Thông báo",
              btn: ["Đồng Ý"],
            })
            reload()
          } else {
            $.modal.alertError(result.msg)
          }
        },
        error: function (result) {
          $.modal.alertError(
            "Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin."
          )
        },
      })
    },
    { title: "Xác nhận cập nhật DO", btn: ["Đồng Ý", "Hủy Bỏ"] }
  )
}

function search() {
  $.ajax({
    url: ctx + "carrier/do/searchCon",
    method: "get",
    data: {
      billOfLading: firstDo.billOfLading,
      contNo: $("#searchForm").val(),
    },
  }).done(function (result) {
    hot.loadData(result)
    hot.render()
    hot.validateCells()
  })
}
document.getElementById("searchForm").addEventListener("keyup", function (event) {
  event.preventDefault()
  if (event.keyCode === 13) {
    $("#searchBtn").click()
  }
})

function closeError(msg) {
  $.modal.alertError(msg)
  loadTable();
}

function closeSuccess(msg) {
  $.modal.alertSuccess(msg)
  loadTable();
}

function reload() {
  var parent = window.parent
  if (parent.table.options.type == table_type.bootstrapTable) {
    $.modal.close()
    parent.$.modal.msgSuccess(result.msg)
    parent.$.table.refresh()
  } else if (parent.table.options.type == table_type.bootstrapTreeTable) {
    $.modal.close()
    parent.$.modal.msgSuccess(result.msg)
    parent.$.treeTable.refresh()
  } else {
    $.modal.msgReload(
      "Lưu thành công! Vui lòng chờ trong khi refresh dữ liêu...",
      modal_status.SUCCESS
    )
  }
  $.modal.closeLoading()
  $.modal.enable()
}

function changeExpiredDate() {
  $.modal.openChangeExpired("Thay đổi hạn lệnh", "/carrier/do/changeExpiredDate/" + firstDo.billOfLading, 500, 380)
}

function historyRenderer(instance, td, row, col, prop, value, cellProperties) {
  let content;
  if(value == null)
  {
    content = '';
  }else {
    content = '<a class="btn btn-info btn-xs btn-xs btn-action mt5 mb5" onclick="viewHistoryCont(\'' + value + '\')"><i class="fa fa-history"></i> Lịch sử</a> ';
  }
  $(td).html(content);
  return td;
}

function viewHistoryCont(value)
{
  $.modal.openWithOneButton('Lịch sử thay đổi thông tin', PREFIX + "/history/" + value, 1000, 400);
}
