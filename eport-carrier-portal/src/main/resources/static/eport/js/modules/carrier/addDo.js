var carrierCodeList = [];
var consigneeList = [];
var emptyDepotList = [];
var vesselList = [];

var prefix = ctx + "carrier/do";
function submitHandler() {
  // add condition to validate before submit add do
  if (true) {
    $.operate.save(prefix + "/add", $("#form-do-add").serialize());
  }
}
getOptionsColumn();
var dogrid = document.getElementById("dogrid"),
  hot,
  $hooksList,
  hooks;
var groupName = null;
var config;
// Empty row and format date
dateValidator = function (value, callback) {
  if (!value || 0 === value.length) {
    callback(false);
  } else {
    if (isGoodDate(value)) {
      callback(true);
    } else {
      callback(false);
    }
  }
};
// Empty row
emptyValidator = function (value, callback) {
  if (!value || 0 === value.length) {
    callback(false);
  } else {
    callback(true);
  }
};

var doRenderer = function (instance, td, row, col, prop, value, cellProperties) {
  if ($("#dogrid").handsontable("getDataAtCell", row, errorFlagCol) == "") {
    $(td).parent().css("background-color", "#205199");
  } else {
    $(td).parent().css("background-color", "#FFFFFF");
  }
  return td;
};

config = {
  stretchH: "all",
  height: document.documentElement.clientHeight - 70,
  minRows: 100,
  width: "100%",
  minSpareRows: 1,
  rowHeights: 30,
  manualColumnMove: false,
  rowHeaders: true,
  className: "htMiddle",
  colHeaders: [
    "Hãng tàu <i class='red'>(*)</i><br>OPR Code",
    "Số vận đơn <i class='red'>(*)</i><br>B/L No.",
    "Số container <i class='red'>(*)</i><br> Container No.",
    "Tên khách hàng <i class='red'>(*)</i><br> Consignee",
    "Hạn lệnh <i class='red'>(*)</i><br> Valid to date",
    "Nơi hạ vỏ <br> Empty depot",
    "Ngày miễn lưu <br> DET free time",
    "Tên tàu <br> Vessel",
    "Chuyến <br> Voyage",
    "Trọng tải <br> Weight",
    "Số seal <br> Seal No",
    "Ghi chú",
  ],
  colWidths: [7, 8, 8, 20, 8, 15, 8, 8, 8, 10, 10, 15],
  filter: "true",
  columns: [
    {
      data: "carrierCode",
      type: "autocomplete",
      source: carrierCodeList,
      strict: true,
    },
    {
      data: "blNo",
      validator: emptyValidator,
    },
    {
      data: "containerNo",
      validator: emptyValidator,
    },
    {
      data: "consignee",
      type: "autocomplete",
      source: consigneeList,
      strict: false
    },
    {
      data: "expiredDem",
      type: "date",
      dateFormat: "DD/MM/YYYY",
      correctFormat: true,
      defaultDate: new Date(),
      validator: dateValidator,
    },
    {
      data: "emptyDepot",
      type: "autocomplete",
      source: emptyDepotList,
      strict: false
    },
    {
      data: "detFreetime",
      type: "numeric",
    },
    {
      data: "vessel",
      type: "autocomplete",
      source: vesselList,
      strict: false
    },
    {
      data: "voyage",
    },
    {
      data: "weight",
    },
    {
      data: "sealNo",
    },
    {
      data: "remark",
    },
  ],
};

function getOptionsColumn() {
  $.ajax({
    url: prefix + "/getOperateCode",
    method: "get",
  }).done(function (result) {
    for (var i = 0; i < result.length; i++) {
      carrierCodeList.push(result[i]);
    }
  });

  $.ajax({
    url: prefix + "/getListOptions",
    method: "get",
  }).done(function (result) {
    var list1 = result.consigneeList;
    var list2 = result.emptyDepotList;
    var list3 = result.vesselList;
    for (var i = 0; i < list1.length; i++) {
      if (list1[i] != null) {
        consigneeList.push(list1[i]);
      }
    }
    for (var i = 0; i< list2.length; i++) {
      if (list1[i] != null) {
        emptyDepotList.push(list2[i]);
      }
    }
    for (var i = 0; i< list3.length; i++) {
      if (list1[i] != null) {
        vesselList.push(list3[i]);
      }
    }
  });
}
// Load table
document.addEventListener("DOMContentLoaded", function () {
  setTimeout(function () {
    hot = new Handsontable(dogrid, config);
    hot.updateSettings({
      cells: function (row, col) {
        var cellProp = {};
        if (col === 5 && isGoodDate(hot.getDataAtCell(row, col))) {
        } else if (col === 5) {
          cellProp.className = " not-date";
        }
        return cellProp;
      },
    });
  }, 200);
});

function isGoodDate(dt) {
  var reGoodDate = /^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|(([1][26]|[2468][048]|[3579][26])00))))$/g;
  return reGoodDate.test(dt);
}

function saveDO() {
  var myTableData = hot.getSourceData();
  if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
    hot.alter(
      "remove_row",
      parseInt(myTableData.length - 1),
      (keepEmptyRows = false)
    );
  }
  var cleanedGridData = [];
  $.each(myTableData, function (rowKey, object) {
    if (!hot.isEmptyRow(rowKey)) {
      cleanedGridData.push(object);
    }
  });
  //Create List DO Object
  var errorFlg = false;
  var doList = [];
  $.each(cleanedGridData, function (index, item) {
    var doObj = new Object();
    if (!isGoodDate(item["expiredDem"]) || item["expiredDem"] == null) {
      $.modal.alertError(
        "Có lỗi tại hàng [" +
          (index + 1) +
          "].<br>Lỗi: Hạn lệnh đang để trống hoặc chưa đúng format."
      );
      errorFlg = true;
      return;
    }
    var date = new Date(
      item["expiredDem"].replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$2/$1/$3")
    );
    date.setHours(0, 0, 1, 1);
    var dateValidate = new Date();
    dateValidate.setHours(0, 0, 0, 0);
    if (date.getTime() < dateValidate.getTime()) {
      $.modal.alertError(
        "Có lỗi tại hàng [" +
          (index + 1) +
          "].<br>Lỗi: Hạn lệnh không được nhỏ hơn ngày hiện tại."
      );
      errorFlg = true;
      return;
    }
    doObj.carrierCode = item["carrierCode"];
    doObj.billOfLading = item["blNo"];
    doObj.containerNumber = item["containerNo"];
    doObj.consignee = item["consignee"];
    doObj.expiredDem = date.getTime();
    doObj.detFreeTime = item["detFreetime"];
    doObj.emptyContainerDepot = item["emptyDepot"];
    doObj.voyNo = item["voyage"];
    doObj.vessel = item["vessel"];
    doObj.remark = item["remark"];
    doObj.weight = item["weight"];
    doObj.sealNo = item["sealNo"];
    doList.push(doObj);
  });

  $.each(doList, function (index, item) {
    if (item["carrierCode"] == null || item["carrierCode"] == "") {
      $.modal.alertError(
        "Có lỗi tại hàng [" +
          (index + 1) +
          "].<br>Lỗi: Hãng tàu (OPR Code) không được trống."
      );
      errorFlg = true;
      return false;
    }
    if (item["billOfLading"] == null || item["billOfLading"] == "") {
      $.modal.alertError(
        "Có lỗi tại hàng [" +
          (index + 1) +
          "].<br>Lỗi: Số vận đơn (B/L No) không được trống."
      );
      errorFlg = true;
      return false;
    }
    if (item["containerNumber"] == null || item["containerNumber"] == "") {
      $.modal.alertError(
        "Có lỗi tại hàng [" +
          (index + 1) +
          "].<br>Lỗi: Số container không được trống."
      );
      errorFlg = true;
      return false;
    }
    if (item["consignee"] == null || item["consignee"] == "") {
      $.modal.alertError(
        "Có lỗi tại hàng [" +
          (index + 1) +
          "].<br>Lỗi: Tên khách hàng không được trống."
      );
      errorFlg = true;
      return false;
    }
    var regexNuber = /^[0-9]*$/;
    if (item["detFreeTime"] != null && item["detFreeTime"] != "") {
      if (!regexNuber.test(item["detFreeTime"])) {
        $.modal.alertError(
          "Có lỗi tại hàng [" +
            (index + 1) +
            "].<br>Lỗi: Số ngày miễn lưu vỏ phải là số."
        );
        errorFlg = true;
        return false;
      }
    }
  });
  if (!errorFlg && doList.length == 0) {
    $.modal.alert("Bạn chưa nhập thông tin.");
    errorFlg = true;
  }
  if(!errorFlg && doList.length==0) {
    $.modal.alert("Bạn chưa nhập thông tin.");
      errorFlg = true;
  }
  if (errorFlg) {
    return;
  }
 
  if (!errorFlg) {
    $.modal.confirm("Bạn có chắc chắn cập nhật DO này lên Web Portal của Cảng Đà Nẵng không?", function() {
          $.ajax({
            url: prefix + "/add",
            method: "post",
            contentType : "application/json",
            accept: 'text/plain',
            data: JSON.stringify(doList),
            dataType: 'text',
            success: function (data) {
            var result = JSON.parse(data);
            if(result.code == 0) {
              $.modal.alert("Khai báo DO thành công!", function() { },{title:"Thông báo",btn:["Đồng Ý"]});
              reload();
            } else {
              $.modal.alertError(result.msg);
            }
            },
            error: function (result) {
              $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
            },
          });
    },
    {title:"Xác Nhận Gửi DO",btn:["Đồng Ý","Hủy Bỏ"]});
  }
}

function reload() {
  var parent = window.parent;
  if (parent.table.options.type == table_type.bootstrapTable) {
      $.modal.close();
      parent.$.modal.msgSuccess(result.msg);
      parent.$.table.refresh();
  } else if (parent.table.options.type == table_type.bootstrapTreeTable) {
      $.modal.close();
      parent.$.modal.msgSuccess(result.msg);
      parent.$.treeTable.refresh();
  } else {
      $.modal.msgReload("Lưu thành công! Vui lòng chờ trong khi refresh dữ liêu...", modal_status.SUCCESS);
  }
  $.modal.closeLoading();
  $.modal.enable();
}