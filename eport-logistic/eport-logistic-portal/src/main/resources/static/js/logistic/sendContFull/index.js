const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
const regexRemoveHtml = /(<([^>]+)>)/gi;
var prefix = ctx + "logistic/send-cont-full";
var interval, currentPercent, timeout;
var dogrid = document.getElementById("container-grid"),
  hot,
  isDestroy = false;
var shipmentSelected,
  shipmentDetails,
  shipmentDetailIds,
  sourceData,
  processOrderIds;
var contList = [],
  temperatureDisable = [],
  sztpListDisable = [];
var conts = "";
var allChecked = false;
var checkList = [];
var rowAmount = 0;
var shipmentSearch = new Object();
shipmentSearch.params = new Object();
shipmentSearch.serviceType = 4;
var sizeList = [];
var berthplanList; // get infor
var onChangeFlg = false,
  currentIndexRow,
  rejectChange = false,
  dischargePortList = [],
  currentVesselVoyage = "",
  currentEta;
var fromDate, toDate;
var myDropzone;

var detailInformationForContainerSpecial = {
  data: [],
  indexSelected: -1,
};

var consigneeList, vslNmList, currentProcessId, currentSubscription;

const CONT_SPECIAL_STATUS = {
  INIT: "I", // cont đã được lưu
  REQ: "R", // cont đã được yêu cầu xác nhận
  YES: "Y", // cont đã được phê duyệt yêu cầu xác nhận
  CANCEL: "C", // cont đã bị từ chối yêu cầu xác nhận
};

const CONT_SZTP = {
  DANGER: "Dangerous", // cont nguy hiểm  
};

//dictionary sizeList
$.ajax({
  type: "GET",
  url: ctx + "logistic/size/container/list",
  success(data) {
    if (data.code == 0) {
      data.data.forEach((element) => {
        sizeList.push(element["dictLabel"]);
      });
    }
  },
});

$.ajax({
  url: ctx + "logistic/source/consignee",
  method: "GET",
  success: function (data) {
    if (data.code == 0) {
      consigneeList = data.consigneeList;
    }
  },
});

$.ajax({
  url: prefix + "/berthplan/vessel-voyage/list",
  method: "GET",
  success: function (data) {
    if (data.code == 0) {
      berthplanList = data.berthplanList;
      vslNmList = data.vesselAndVoyages;
    }
  },
});
var cargoTypeList = [
  "AK:Over Dimension",
  "BB:Break Bulk",
  "BN:Bundle",
  "DG:Dangerous",
  "DR:Reefer & DG",
  "DE:Dangerous Empty",
  "FR:Fragile",
  "GP:General",
  "MT:Empty",
  "RF:Reefer",
];

var toolbar = [
  {
    text:
      '<button class="btn btn-sm btn-default"><i class="fa fa-plus text-success"></i> Thêm</button>',
    handler: function () {
      $.operate.addShipment();
    },
  },
  {
    text:
      '<button class="btn btn-sm btn-default" ><i class="fa fa-edit text-warning"></i> Sửa</button>',
    handler: function () {
      $.operate.editShipment();
    },
  },
  {
    text:
      '<button class="btn btn-sm btn-default"><i class="fa fa-remove text-danger"></i> Xóa</button>',
    handler: function () {
      removeShipment();
    },
  },
  {
    text:
      '<button class="btn btn-sm btn-default"><i class="fa fa-refresh text-success"></i></button>',
    handler: function () {
      handleRefresh();
    },
  },
];

$(".main-body").layout();

$(".collapse").click(function () {
  $(".main-body__search-wrapper").hide();
  $(".main-body__search-wrapper--container").hide();
  $(this).hide();
  $(".uncollapse").show();
});

$(".uncollapse").click(function () {
  $(".main-body__search-wrapper").show();
  $(".main-body__search-wrapper--container").show();
  $(this).hide();
  $(".collapse").show();
});

$(".left-side__collapse").click(function () {
  $("#main-layout").layout("collapse", "west");
  setTimeout(() => {
    hot.render();
  }, 200);
});

$("#main-layout").layout({
  onExpand: function (region) {
    if (region == "west") {
      hot.render();
    }
  },
});

$(".right-side__collapse").click(function () {
  $("#right-layout").layout("collapse", "south");
  setTimeout(() => {
    hot.updateSettings({ height: $("#right-side__main-table").height() - 35 });
    hot.render();
  }, 200);
});

$("#right-layout").layout({
  onExpand: function (region) {
    if (region == "south") {
      hot.updateSettings({
        height: $("#right-side__main-table").height() - 35,
      });
      hot.render();
      let req = {
        shipmentId: shipmentSelected.id,
      };
      $.ajax({
        url: ctx + "logistic/comment/update",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(req),
        success: function (res) {
          if (res.code == 0) {
            let commentTitle =
              '<span>Hỗ Trợ</span>&nbsp;&nbsp;<span class="round-notify-count">0</span>';
            $("#right-layout")
              .layout("panel", "expandSouth")
              .panel("setTitle", commentTitle);
          }
        },
      });
    }
  },
});

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
  // DROP ZONE configuration
  // Get the template HTML and remove it from the doumenthe template HTML and remove it from the doument

  let previewTemplate = "<span data-dz-name></span>";

  if (shipmentSelected == null) {
    shipmentSelected = new Object();
  }

  myDropzone = new Dropzone("#dropzone", {
    url: ctx + "logistic/shipment/" + shipmentSelected.id + "/file/attach",
    method: "post",
    paramName: "file",
    maxFiles: 5,
    maxFilesize: 10, //MB
    // autoProcessQueue: false,
    previewTemplate: previewTemplate,
    previewsContainer: "#previews", // Define the container to display the previews
    clickable: "#attachButton", // Define the element that should be used as click trigger to select files.
    init: function () {
      this.on("maxfilesexceeded", function (file) {
        $.modal.alertWarning(
          "Số lượng tệp đính kèm vượt giới hạn cho phép, quý khách vui lòng đính kèm tệp trong lần comment tiếp theo."
        );
        this.removeFile(file);
      });
    },
    success: function (file, response) {
      if (response.code == 0) {
        $.modal.msgSuccess("Đính kèm tệp thành công.");
        let content =
          '<p><a href="' +
          response.fileLink +
          '" target="_blank">' +
          file.upload.filename +
          "</a></p>";
        $("#content").summernote("editor.pasteHTML", content);
      } else {
        $.modal.msgError(
          "Đính kèm tệp thất bại, quý khách vui lòng thử lại sau."
        );
      }
    },
  });

  $("#right-layout").layout("collapse", "south");
  setTimeout(() => {
    hot.updateSettings({ height: $("#right-side__main-table").height() - 35 });
    hot.render();
  }, 200);

  $("#shipmentStatus").combobox({
    onSelect: function (option) {
      shipmentSearch.status = option.value;
      loadTable();
    },
  });

  $("#bookingNo")
    .textbox("textbox")
    .bind("keydown", function (e) {
      // enter key
      if (e.keyCode == 13) {
        shipmentSearch.bookingNo = $("#bookingNo")
          .textbox("getText")
          .toUpperCase();
        loadTable();
      }
    });

  if (sId != null) {
    shipmentSearch.id = sId;
  }

  $("#containerNo")
    .textbox("textbox")
    .bind("keydown", function (e) {
      // enter key
      if (e.keyCode == 13) {
        shipmentSearch.params.containerNo = $("#containerNo")
          .textbox("getText")
          .toUpperCase();
        loadTable();
      }
    });

  $("#consignee")
    .textbox("textbox")
    .bind("keydown", function (e) {
      // enter key
      if (e.keyCode == 13) {
        shipmentSearch.params.consignee = $("#consignee")
          .textbox("getText")
          .toUpperCase();
        loadTable();
      }
    });

  // let now = new Date();
  // now = new Date(now.getFullYear(), now.getMonth(), 1);
  // let nowStr = ("0" + now.getDate()).slice(-2) + "/" + ("0" + (now.getMonth() + 1)).slice(-2) + "/" + now.getFullYear();

  $("#fromDate").datebox({
    onSelect: function (date) {
      date.setHours(0, 0, 0);
      fromDate = date;
      if (toDate != null && date.getTime() > toDate.getTime()) {
        $.modal.alertWarning("Từ ngày không được lớn hơn đến ngày.");
      } else {
        shipmentSearch.params.fromDate = dateToString(date);
        loadTable();
      }
      return date;
    },
  });

  // $('#fromDate').datebox('setValue', nowStr);
  // shipmentSearch.params.fromDate = dateToString(now);

  $("#toDate").datebox({
    onSelect: function (date) {
      date.setHours(23, 59, 59);
      toDate = date;
      if (fromDate != null && date.getTime() < fromDate.getTime()) {
        $.modal.alertWarning("Đến ngày không được thấp hơn từ ngày.");
      } else {
        shipmentSearch.params.toDate = dateToString(date);
        loadTable();
      }
    },
  });

  // Handle add
  $(function () {
    let options = {
      createUrl: prefix + "/shipment/add",
      updateUrl: "0",
      modalName: " Lô",
    };
    $.table.init(options);
  });

  loadTable();
});

function dateformatter(date) {
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  var d = date.getDate();
  return (d < 10 ? "0" + d : d) + "/" + (m < 10 ? "0" + m : m) + "/" + y;
}
function dateparser(s) {
  var ss = s.split(".");
  var d = parseInt(ss[0], 10);
  var m = parseInt(ss[1], 10);
  var y = parseInt(ss[2], 10);
  if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
    return new Date(y, m - 1, d);
  }
}

// LOAD SHIPMENT LIST
function loadTable() {
  $("#dg").datagrid({
    url: ctx + "logistic/shipments",
    height: $(".main-body").height() - 75,
    method: "post",
    singleSelect: true,
    collapsible: true,
    clientPaging: false,
    toolbar: toolbar,
    pagination: true,
    rownumbers: true,
    onBeforeSelect: function (index, row) {
      getSelected(index, row);
    },
    pageSize: 50,
    nowrap: false,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      let opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        url: opts.url,
        contentType: "application/json",
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: shipmentSearch,
        }),
        success: function (data) {
          success(data);
          $("#dg").datagrid("hideColumn", "id");
          $("#dg").datagrid("selectRow", 0);
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

// FORMAT DATE FOR SHIPMENT LIST
function formatDate(value) {
  let date = new Date(value);
  let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  let month = date.getMonth() + 1;
  let monthText = month < 10 ? "0" + month : month;
  let hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
  let minutes =
    date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
  return (
    day +
    "/" +
    monthText +
    "/" +
    date.getFullYear() +
    " " +
    hours +
    ":" +
    minutes
  );
}

// FORMAT REMARK FOR SHIPMENT LIST
function formatRemark(value) {
  return (
    '<div class="easyui-tooltip" title="' +
    (value != null ? value : "Trống") +
    '" style="width: 80; text-align: center;"><span>' +
    (value != null ? value : "") +
    "</span></div>"
  );
}

function handleRefresh() {
  loadTable();
}

// HANDLE WHEN SELECT A SHIPMENT
function getSelected(index, row) {
  if (rejectChange) {
    rejectChange = false;
    return true;
  } else {
    if (onChangeFlg && currentIndexRow != index) {
      layer.confirm(
        "Thông tin khái báo chưa được lưu, quý khách có muốn di chuyển qua trang khác?",
        {
          icon: 3,
          title: "Xác Nhận",
          btn: ["Đồng Ý", "Hủy Bỏ"],
        },
        function () {
          layer.close(layer.index);
          currentIndexRow = index;
          if (row) {
            shipmentSelected = row;
            $(function () {
              let options = {
                createUrl: prefix + "/shipment/add",
                updateUrl: prefix + "/shipment/" + shipmentSelected.id,
                modalName: " Lô",
              };
              $.table.init(options);
            });
            let title = "";
            title += "Mã Lô: " + row.id + " - ";
            title += "SL: " + row.containerAmount + " - ";
            title += "Booking No: ";
            if (row.bookingNo != null) {
              title += row.bookingNo;
            } else {
              title += "Trống";
            }
            $("#right-layout")
              .layout("panel", "center")
              .panel("setTitle", title);
            rowAmount = row.containerAmount;
            checkList = Array(rowAmount).fill(0);
            temperatureDisable = Array(rowAmount).fill(1);
            sztpListDisable = Array(rowAmount).fill(0);
            allChecked = false;
            if (
              oprListBookingCheck != null &&
              oprListBookingCheck.includes(row.opeCode)
            ) {
              toggleAttachIcon(shipmentSelected.id);
            }
            loadShipmentDetail(row.id);
            onChangeFlg = false;
            currentIndexRow = index;
            loadListComment();

            // Update dropzone url with the shipment id selected
            myDropzone.options.url =
              ctx + "logistic/shipment/" + shipmentSelected.id + "/file/attach";
          }
          return true;
        },
        function () {
          layer.close(layer.index);
          rejectChange = true;
          $("#dg").datagrid("selectRow", currentIndexRow);
          return false;
        }
      );
    } else {
      currentIndexRow = index;
      if (row) {
        shipmentSelected = row;
        $(function () {
          let options = {
            createUrl: prefix + "/shipment/add",
            updateUrl: prefix + "/shipment/" + shipmentSelected.id,
            modalName: " Lô",
          };
          $.table.init(options);
        });
        let title = "";
        title += "Mã Lô: " + row.id + " - ";
        title += "SL: " + row.containerAmount + " - ";
        title += "Booking No: ";
        if (row.bookingNo != null) {
          title += row.bookingNo;
        } else {
          title += "Trống";
        }
        title += '<span id="attachFile"></span>';
        $("#right-layout").layout("panel", "center").panel("setTitle", title);
        rowAmount = row.containerAmount;
        checkList = Array(rowAmount).fill(0);
        temperatureDisable = Array(rowAmount).fill(1);
        sztpListDisable = Array(rowAmount).fill(0);
        allChecked = false;
        loadShipmentDetail(row.id);
        if (
          oprListBookingCheck != null &&
          oprListBookingCheck.includes(row.opeCode)
        ) {
          toggleAttachIcon(shipmentSelected.id);
        }
        loadShipmentDetail(row.id);
        onChangeFlg = false;
        currentIndexRow = index;
        loadListComment();

        // Update dropzone url with the shipment id selected
        myDropzone.options.url =
          ctx + "logistic/shipment/" + shipmentSelected.id + "/file/attach";
      }
      return true;
    }
  }
}

function toggleAttachIcon(shipmentId) {
  $.ajax({
    type: "GET",
    url: prefix + "/shipments/" + shipmentId + "/shipment-images",
    contentType: "application/json",
    success: function (data) {
      if (data.code == 0) {
        if (data.shipmentFiles != null && data.shipmentFiles.length > 0) {
          let html = "";
          data.shipmentFiles.forEach(function (element, index) {
            html +=
              ' <a href="' +
              element.path +
              '" target="_blank"><i class="fa fa-paperclip" style="font-size: 18px;"></i> ' +
              (index + 1) +
              "</a>";
          });
          $("#attachFile").html(html);
        }
      }
    },
  });
}

// FORMAT HANDSONTABLE COLUMN
function checkBoxRenderer(instance, td, row, col, prop, value, cellProperties) {
  let content = "";
  if (checkList[row] == 1) {
    content +=
      '<div><input type="checkbox" id="check' +
      row +
      '" onclick="check(' +
      row +
      ')" checked></div>';
  } else {
    content +=
      '<div><input type="checkbox" id="check' +
      row +
      '" onclick="check(' +
      row +
      ')"></div>';
  }
  $(td)
    .attr("id", "checkbox" + row)
    .addClass("htCenter")
    .addClass("htMiddle")
    .html(content);
  return td;
}

function statusIconsRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "statusIcon" + row)
    .addClass("htCenter")
    .addClass("htMiddle");
  if (
    sourceData[row] &&
    sourceData[row].dischargePort &&
    sourceData[row].processStatus &&
    sourceData[row].paymentStatus &&
    sourceData[row].customStatus &&
    sourceData[row].finishStatus
  ) {
    // Command process status
    let process =
      '<i id="verify" class="fa fa-windows easyui-tooltip" title="Chưa xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';

    if (
      !getStatusContFollowIndex(row) ||
      getStatusContFollowIndex(row) == CONT_SPECIAL_STATUS.YES
    ) {
      switch (sourceData[row].processStatus) {
        case "E":
          process =
            '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ kết quả" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
          break;
        case "Y":
          process =
            '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đã làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
          break;
        case "N":
          process =
            '<i id="verify" class="fa fa-windows easyui-tooltip" title="Có thể làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
          break;
        case "D":
          process =
            '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ hủy lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #f93838;"></i>';
          break;
      }
    }

    // Payment status
    let payment =
      '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
    switch (sourceData[row].paymentStatus) {
      case "E":
        payment =
          '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Lỗi Thanh Toán" aria-hidden="true" style="margin-left: 8px; color : #ed5565;"></i>';
        break;
      case "Y":
        payment =
          '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
      case "N":
        if (value > 1) {
          payment =
            '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chờ Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
        }
        break;
    }
    // Customs Status
    let customs =
      '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chờ Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
    switch (sourceData[row].customStatus) {
      case "R":
        customs =
          '<i id="custom" class="fa fa-shield easyui-tooltip" title="Đã Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
      case "Y":
        customs =
          '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chưa Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #ed5565;"></i>';
        break;
      case "N":
        customs =
          '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chờ Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
        break;
    }
    // released status
    let released =
      '<i id="finish" class="fa fa-ship easyui-tooltip" title="Chưa Thể Giao Container" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
    switch (sourceData[row].finishStatus) {
      case "Y":
        released =
          '<i id="finish" class="fa fa-ship easyui-tooltip" title="Đã Giao Container" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
      case "N":
        if (sourceData[row].paymentStatus == "Y") {
          released =
            '<i id="finish" class="fa fa-ship easyui-tooltip" title="Có Thể Giao Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
        }
        break;
    }
    // Return the content
    let content = '<div style="text-align: right">';

    content += getConfigIconSztp(row);

    content += getRequestConfigIcon(row);

    content += process + payment;
    // Domestic cont: VN --> not show
    if (sourceData[row].dischargePort.substring(0, 2) != "VN") {
      content += customs;
    }
    content += released + "</div>";
    $(td).html(content);
  }
  return td;
}

function getConfigIconSztp(row) {
  const sztpResult = getIconContFollowIndex(row);

  if (sztpResult == null) {
    return "";
  } if (sztpResult == CONT_SZTP.DANGER) {
    return '<i id="verify" class="fa fa-exclamation-triangle" title="Là cont nguy hiểm" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: red"></i>';
  }
}
function getIconContFollowIndex(index) {
  if (
    sourceData[index].sztp.substring(2, 3) != "G"
  ) {
    return null;
  }
  if (sourceData[index].sztp.substring(2, 3) == "G" && sourceData[index].cargoType == "DG") {
    return CONT_SZTP.DANGER;
  }

}


function getRequestConfigIcon(row) {
  const statusResult = getStatusContFollowIndex(row);

  if (!statusResult) {
    return "";
  } else if (statusResult == CONT_SPECIAL_STATUS.YES) {
    return '<i id="verify" class="fa fa-check easyui-tooltip" title="Yêu cầu xác nhật đã được duyệt" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394"></i>';
  } else if (statusResult == CONT_SPECIAL_STATUS.CANCEL) {
    return getRequestConfigIconIsCancel(row);
  } else if (statusResult == CONT_SPECIAL_STATUS.REQ) {
    return getRequestConfigIconIsRequest(row);
  } else if (statusResult == CONT_SPECIAL_STATUS.INIT) {
    return '<i id="verify" class="fa fa-check easyui-tooltip" title="Có thể yêu cầu xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db"></i>';
  }
}

/**
 * @author Khanh
 * @description Ket qua tra ve hien cont dang cho yeu cau request cua to nao?
 * @returns {String}
 */
function getRequestConfigIconIsRequest(row) {
  let statusResult = " ";
  if (
    sourceData[row].dangerous &&
    sourceData[row].dangerous === CONT_SPECIAL_STATUS.REQ &&
    sourceData[row].oversize &&
    sourceData[row].oversize === CONT_SPECIAL_STATUS.REQ &&
    sourceData[row].frozenStatus &&
    sourceData[row].frozenStatus === CONT_SPECIAL_STATUS.REQ
  ) {
    return '<i id="verify" class="fa fa-check easyui-tooltip" title="Đang chờ yêu cầu xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #f8ac59"></i>';
  }
  if (
    sourceData[row].dangerous &&
    sourceData[row].dangerous == CONT_SPECIAL_STATUS.REQ
  ) {
    statusResult += "Tổ Nguy Hiểm, ";
  }
  if (
    sourceData[row].oversize &&
    sourceData[row].oversize == CONT_SPECIAL_STATUS.REQ
  ) {
    statusResult += "Tổ Quá Khổ, ";
  }
  if (
    sourceData[row].frozenStatus &&
    sourceData[row].frozenStatus == CONT_SPECIAL_STATUS.REQ
  ) {
    statusResult += "Tổ Lạnh, ";
  }
  return `<i id="verify" class="fa fa-check easyui-tooltip" title="Đang chờ yêu cầu xác nhận từ ${statusResult.slice(
    0,
    statusResult.length - 2
  )}" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #f8ac59"></i>`;
}

/**
 * @author Khanh
 * @description Ket qua tra ve hien cont đã bị từ chối bởi to nao?
 * @returns {String}
 */
function getRequestConfigIconIsCancel(row) {
  let statusResult = " ";
  if (
    sourceData[row].dangerous &&
    sourceData[row].dangerous === CONT_SPECIAL_STATUS.CANCEL &&
    sourceData[row].oversize &&
    sourceData[row].oversize === CONT_SPECIAL_STATUS.CANCEL &&
    sourceData[row].frozenStatus &&
    sourceData[row].frozenStatus === CONT_SPECIAL_STATUS.CANCEL
  ) {
    return '<i id="verify" class="fa fa-check easyui-tooltip" title="Yêu cầu xác nhận bị từ chối" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #ff0000"></i>';
  }
  if (
    sourceData[row].dangerous &&
    sourceData[row].dangerous == CONT_SPECIAL_STATUS.CANCEL
  ) {
    statusResult += "Tổ Nguy Hiểm, ";
  }
  if (
    sourceData[row].oversize &&
    sourceData[row].oversize == CONT_SPECIAL_STATUS.CANCEL
  ) {
    statusResult += "Tổ Quá Khổ, ";
  }
  if (
    sourceData[row].frozenStatus &&
    sourceData[row].frozenStatus == CONT_SPECIAL_STATUS.CANCEL
  ) {
    statusResult += "Tổ Lạnh, ";
  }
  return `<i id="verify" class="fa fa-check easyui-tooltip" title="Yêu cầu xác nhận bị từ chối từ ${statusResult.slice(
    0,
    statusResult.length - 2
  )}" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #ff0000"></i>`;
}

function containerNoRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "containerNo" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
    if (!checkContainerNo(value)) {
      cellProperties.comment = {
        value:
          "Số container không đúng tiêu chuẩn ISO, có thể bạn đang nhập sai, vui lòng kiểm tra lại",
      };
      value = '<span style="color: red;">' + value + "</span>";
    } else {
      cellProperties.comment = null;
    }
  }
  if (!value) {
    value = "";
    cellProperties.comment = null;
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function expiredDemRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "expiredDem" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  $(td).html(value);
  if (value != null && value != "") {
    if (value.substring(2, 3) != "/") {
      value =
        value.substring(8, 10) +
        "/" +
        value.substring(5, 7) +
        "/" +
        value.substring(0, 4);
    }
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function consigneeRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "consignee" + row)
    .addClass("htMiddle");
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "vslNm" + row)
    .addClass("htMiddle");
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (!value) {
    value = "";
  }

  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function etaRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "eta" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  if (value != null && value != "") {
    if (value.substring(2, 3) != "/") {
      value =
        value.substring(8, 10) +
        "/" +
        value.substring(5, 7) +
        "/" +
        value.substring(0, 4);
    }
  } else {
    value = "";
  }
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "sztp" + row)
    .addClass("htMiddle");
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (sztpListDisable[row] == 1) {
    cellProperties.readOnly = "true";
    $(td).css("background-color", "rgb(232, 232, 232)");
  }
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function sealNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "sealNo" + row)
    .addClass("htMiddle");
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function temperatureRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "temperature" + row)
    .addClass("htMiddle")
    .addClass("htRight");
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (temperatureDisable[row] == 1) {
    $(td).html("");
    cellProperties.readOnly = "true";
    $(td).css("background-color", "rgb(232, 232, 232)");
  }

  if (value === null || value === "") {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

/**
 * Render button
 */
function btnDetailRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "wgt" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  let containerNo, sztp;
  if (!isDestroy) {
    containerNo = hot.getDataAtCell(row, 2);
    sztp = hot.getDataAtCell(row, 3);
    cargoType = hot.getDataAtCell(row, 9);
  }
  
  const iElement = '<i style="margin-right: 3px;" class="fa fa-book"></i>';
  if (sourceData && sourceData.length > 0) {
    if (sourceData.length > row && sourceData[row].id) {
      if (sourceData[row].cargoType == "DG") {
        value = `<button style="width: 85%;" class="btn btn-success btn-xs" onclick="openDetail('${sourceData[row].id}', '${containerNo}', '${sztp}', '${row}','${sourceData[row].cargoType}')"><i style="margin-right: 3px;" class="fa fa-book"></i> Nguy hiểm</button>`;
      }
      else if (sourceData[row].sztp.substring(2, 3) == "R") {
        value = `<button style="width: 85%;" class="btn btn-success btn-xs" onclick="openDetail('${sourceData[row].id}', '${containerNo}', '${sztp}', '${row}','${sourceData[row].cargoType}')"><i style="margin-right: 3px;" class="fa fa-book"></i> Lạnh</button>`;
      }
      //else if(sourceData[row].oversizeTop || sourceData[row].oversizeRight || sourceData[row].oversizeLeft){
      else if (sourceData[row].sztp.substring(2, 3) == "P" || sourceData[row].sztp.substring(2, 3) == "U") {
        value = `<button style="width: 85%;" class="btn btn-success btn-xs" onclick="openDetail('${sourceData[row].id}', '${containerNo}', '${sztp}', '${row}','${sourceData[row].cargoType}')">${iElement}Quá khổ</button>`;
      }
    } else if (containerNo && sztp) {
      value = `<button style="width: 85%;" class="btn btn-success btn-xs" onclick="openDetail('${""}', '${containerNo}', '${sztp}', '${row}','${sourceData[row].cargoType}')"><i style="margin-right: 3px;" class="fa fa-book"></i> Đặc biệt</button>`;
    }
    //  else if (containerNo && sztp) {
    //   value = `<button style="width: 69%;" class="btn btn-success btn-xs" onclick="openDetail('${""}', '${containerNo}', '${sztp}', '${row}','${sourceData[row].cargoType}')">${iElement}Đặc biệt</button>`;
    // } 
  }
  $(td).html(value);
  cellProperties.readOnly = "true";
  return td;
}

function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "wgt" + row)
    .addClass("htMiddle")
    .addClass("htRight");
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
    if (value > 99999) {
      layer.msg("Trọng lượng (kg) quá lớn (hơn 100 tấn).", {
        icon: $.modal.icon(modal_status.FAIL),
        time: 2000,
        shift: 5,
      });
      $(td).css("text-color", "red");
    } else if (value < 1000) {
      layer.msg("Trọng lượng (kg) quá nhỏ (nhỏ hơn 1 tấn).", {
        icon: $.modal.icon(modal_status.FAIL),
        time: 2000,
        shift: 5,
      });
      $(td).css("text-color", "red");
    }
    value = formatMoney(value);
  }
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function formatMoney(value) {
  return value.format(0, 3, ",", ".");
}
Number.prototype.format = function (n, x, s, c) {
  var re = "\\d(?=(\\d{" + (x || 3) + "})+" + (n > 0 ? "\\D" : "$") + ")",
    num = this.toFixed(Math.max(0, ~~n));

  return (c ? num.replace(".", c) : num).replace(
    new RegExp(re, "g"),
    "$&" + (s || ",")
  );
};

function cargoTypeRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "cargoType" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  if (value != null && value != "") {
    value = value.split(":")[0];
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function commodityRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "commodity" + row)
    .addClass("htMiddle");
  if (value != null && value != "") {
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}
function dischargePortRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "dischargePort" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  if (value != null && value != "") {
    value = value.split(":")[0];
    if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
      cellProperties.readOnly = "true";
      $(td).css("background-color", "rgb(232, 232, 232)");
    }
  }
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

function payTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "payType" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}

function payerRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "payer" + row)
    .addClass("htMiddle")
    .addClass("htCenter");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}

function payerNameRenderer(
  instance,
  td,
  row,
  col,
  prop,
  value,
  cellProperties
) {
  $(td)
    .attr("id", "payerNamer" + row)
    .addClass("htMiddle");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  cellProperties.readOnly = "true";
  $(td).css("background-color", "rgb(232, 232, 232)");
  return td;
}

function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td)
    .attr("id", "remark" + row)
    .addClass("htMiddle");
  if (!value) {
    value = "";
  }
  $(td).html(
    '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
    value +
    "</div>"
  );
  return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
  config = {
    stretchH: "all",
    height: $("#right-side__main-table").height() - 35,
    minRows: rowAmount,
    maxRows: rowAmount,
    width: "100%",
    minSpareRows: 0,
    rowHeights: 30,
    fixedColumnsLeft: 3,
    trimDropdown: false,
    manualColumnResize: true,
    manualRowResize: true,
    rowHeaders: true,
    comments: true,
    className: "htMiddle htCenter",
    colHeaders: function (col) {
      switch (col) {
        case 0:
          var txt = "<input type='checkbox' class='checker' ";
          txt += "onclick='checkAll()' ";
          txt += ">";
          return txt;
        case 1:
          return "Trạng Thái";
        case 2:
          return '<span class="required">Container No</span>';
        case 3:
          return '<span class="required">Kích Thước</span>';
        case 4:
          return '<span class="required">Chủ Hàng</span>';
        case 5:
          return '<span class="required">Tàu và Chuyến</span>';
        case 6:
          return "Ngày tàu đến";
        case 7:
          return '<span class="required">Cảng Dỡ Hàng</span>';
        case 8:
          return '<span class="required">Trọng Lượng (kg)</span>';
        case 9:
          return '<span class="required">Loại Hàng</span>';
        case 10:
          return "Tên Hàng";
        case 11:
          return "Số Seal";
        case 12:
          return "Nhiệt Độ (c)";
        case 13:
          return '<span>Chi Tiết Container</span>';
        case 14:
          return "PTTT";
        case 15:
          return "Mã Số Thuế";
        case 16:
          return "Người Thanh Toán";
        case 17:
          return "Ghi Chú";
      }
    },
    colWidths: [
      40,
      120,
      100,
      150,
      150,
      150,
      130,
      100,
      120,
      120,
      80,
      100,
      80,
      130,
      80,
      100,
      130,
      200,
    ],
    filter: "true",
    columns: [
      {
        data: "active",
        type: "checkbox",
        className: "htCenter",
        renderer: checkBoxRenderer,
      },
      {
        data: "status",
        readOnly: true,
        renderer: statusIconsRenderer,
      },
      {
        data: "containerNo",
        strict: true,
        renderer: containerNoRenderer,
      },

      {
        data: "sztp",
        type: "autocomplete",
        source: sizeList,
        strict: true,
        renderer: sizeRenderer,
      },

      {
        data: "consignee",
        strict: true,
        type: "autocomplete",
        source: consigneeList,
        renderer: consigneeRenderer,
      },
      {
        data: "vslNm",
        type: "autocomplete",
        source: vslNmList,
        strict: true,
        renderer: vslNmRenderer,
      },
      {
        data: "eta",
        renderer: etaRenderer,
      },
      {
        data: "dischargePort",
        strict: true,
        type: "autocomplete",
        renderer: dischargePortRenderer,
      },
      {
        data: "wgt",
        type: "numeric",
        strict: true,
        renderer: wgtRenderer,
      },
      {
        data: "cargoType",
        strict: true,
        type: "autocomplete",
        source: cargoTypeList,
        renderer: cargoTypeRenderer,
      },
      {
        data: "commodity",
        renderer: commodityRenderer,
      },
      {
        data: "sealNo",
        renderer: sealNoRenderer,
      },

      {
        data: "temperature",
        type: "numeric",
        strict: true,
        readonly: true,
        renderer: temperatureRenderer,
      },
      {
        data: "btnInformationContainer",
        renderer: btnDetailRenderer,
      },
      {
        data: "payType",
        renderer: payTypeRenderer,
      },
      {
        data: "payer",
        renderer: payerRenderer,
      },
      {
        data: "payerName",
        renderer: payerNameRenderer,
      },
      {
        data: "remark",
        renderer: remarkRenderer,
      },
    ],
    beforeKeyDown: function (e) {
      let selected;
      switch (e.keyCode) {
        // Arrow Left
        case 37:
          selected = hot.getSelected()[0];
          if (selected[3] == 0) {
            e.stopImmediatePropagation();
          }
          break;
        // Arrow Up
        case 38:
          selected = hot.getSelected()[0];
          if (selected[2] == 0) {
            e.stopImmediatePropagation();
          }
          break;
        // Arrow Right
        case 39:
          selected = hot.getSelected()[0];
          if (selected[3] == 17) {
            e.stopImmediatePropagation();
          }
          break;
        // Arrow Down
        case 40:
          selected = hot.getSelected()[0];
          if (selected[2] == rowAmount - 1) {
            e.stopImmediatePropagation();
          }
          break;
        default:
          break;
      }
    },
    afterChange: onChange,
  };
}
configHandson();
function onChange(changes, source) {
  if (!changes) {
    return;
  }
  onChangeFlg = true;
  changes.forEach(function (change) {
    // Trigger when vessel-voyage no change, get list discharge port by vessel, voy no
    if (change[1] == "vslNm" && change[3] != null && change[3] != "") {
      let vesselAndVoy = hot.getDataAtCell(change[0], 5);
      //hot.setDataAtCell(change[0], 10, ''); // dischargePort reset
      if (vesselAndVoy) {
        if (currentVesselVoyage != vesselAndVoy) {
          currentVesselVoyage = vesselAndVoy;
          let shipmentDetail = new Object();
          for (let i = 0; i < berthplanList.length; i++) {
            if (vesselAndVoy == berthplanList[i].vslAndVoy) {
              currentEta = berthplanList[i].eta;
              shipmentDetail.vslNm = berthplanList[i].vslNm;
              shipmentDetail.voyNo = berthplanList[i].voyNo;
              shipmentDetail.year = berthplanList[i].year;
              $.modal.loading("Đang xử lý ...");
              $.ajax({
                url: ctx + "/logistic/pods",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(shipmentDetail),
                success: function (data) {
                  $.modal.closeLoading();
                  if (data.code == 0) {
                    hot.updateSettings({
                      cells: function (row, col, prop) {
                        if (col == 7) {
                          let cellProperties = {};
                          dischargePortList = data.dischargePorts;
                          cellProperties.source = dischargePortList;
                          return cellProperties;
                        }
                      },
                    });
                  }
                },
              });
            }
          }
        }
        hot.setDataAtCell(change[0], 6, currentEta);
      }
      // check to input temperature
    } else if (change[1] == "sztp") {
      if (change[3] && hot.getDataAtCell(change[0], 2)) {
        $("#detailBtn" + change[0]).prop("disabled", false);
      } else {
        $("#detailBtn" + change[0]).prop("disabled", true);
      }

      if (
        change[3] &&
        change[3].length > 3 &&
        change[3].substring(0, 4).includes("R")
      ) {
        temperatureDisable[change[0]] = 0;
        hot.updateSettings({
          cells: function (row, col, prop) {
            if (row == change[0] && col == 12) {
              let cellProperties = {};
              cellProperties.readOnly = false;
              return cellProperties;
            }
          },
        });
      } else {
        temperatureDisable[change[0]] = 1;
        hot.updateSettings({
          cells: function (row, col, prop) {
            if (row == change[0] && col == 12) {
              let cellProperties = {};
              cellProperties.readOnly = true;
              $("#temperature" + row).css(
                "background-color",
                "rgb(232, 232, 232)"
              );
              return cellProperties;
            }
          },
        });
      }
    } else if (change[1] == "containerNo") {
      if (!change[3]) {
        sztpListDisable[change[0]] = 0;
        cleanCell(change[0], 3, sizeList);
      } else {
        if (checkContainerNo(change[3])) {
          $.ajax({
            url: prefix + "/containerNo/" + change[3] + "/sztp",
            method: "GET",
            success: function (data) {
              if (data.code == 0) {
                if (data.sztp && data.sztp[0] != "{") {
                  sizeList.forEach((element) => {
                    if (data.sztp == element.substring(0, 4)) {
                      data.sztp = element;
                      return false;
                    }
                  });
                  sztpListDisable[change[0]] = 1;
                  hot.setDataAtCell(change[0], 3, data.sztp);
                } else {
                  sztpListDisable[change[0]] = 0;
                  cleanCell(change[0], 3, sizeList);
                }
              } else {
                sztpListDisable[change[0]] = 0;
                cleanCell(change[0], 3, sizeList);
              }
            },
            error: function (err) {
              sztpListDisable[change[0]] = 0;
              cleanCell(change[0], 3, sizeList);
            },
          });
        } else {
          sztpListDisable[change[0]] = 0;
          cleanCell(change[0], 3, sizeList);
        }
      }
      if (change[3] && hot.getDataAtCell(change[0], 3)) {
        $("#detailBtn" + change[0]).prop("disabled", false);
      } else {
        $("#detailBtn" + change[0]).prop("disabled", true);
      }
    }
  });
}

function cleanCell(roww, coll, src) {
  hot.setDataAtCell(roww, coll, "");
  hot.updateSettings({
    cells: function (row, col, prop) {
      if (row == roww && col == coll) {
        let cellProperties = {};
        cellProperties.source = src;
        return cellProperties;
      }
    },
  });
}

// Check container valid
function checkContainerNo(containerNo) {
  if (!containerNo || containerNo == "" || containerNo.length != 11) {
    return false;
  }
  containerNo = containerNo.toUpperCase();
  let re = /^[A-Z]{4}\d{7}/;
  if (re.test(containerNo)) {
    let sum = 0;
    for (i = 0; i < 10; i++) {
      let n = containerNo.substr(i, 1);
      if (i < 4) {
        n = "0123456789A?BCDEFGHIJK?LMNOPQRSTU?VWXYZ".indexOf(
          containerNo.substr(i, 1)
        );
      }
      n *= Math.pow(2, i);
      sum += n;
    }
    if (containerNo.substr(0, 4) == "HLCU") {
      sum -= 2;
    }
    sum %= 11;
    sum %= 10;
    return sum == containerNo.substr(10);
  } else {
    return false;
  }
}

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

// TRIGGER CHECK ALL SHIPMENT DETAIL
function checkAll() {
  if (!allChecked) {
    allChecked = true;
    checkList = Array(rowAmount).fill(0);
    for (let i = 0; i < checkList.length; i++) {
      if (hot.getDataAtCell(i, 1) == null) {
        break;
      }
      checkList[i] = 1;
      $("#check" + i).prop("checked", true);
    }
  } else {
    allChecked = false;
    checkList = Array(rowAmount).fill(0);
    for (let i = 0; i < checkList.length; i++) {
      $("#check" + i).prop("checked", false);
    }
  }
  let tempCheck = allChecked;
  updateLayout();
  hot.render();
  allChecked = tempCheck;
  $(".checker").prop("checked", allChecked);
}
function check(id) {
  if (sourceData[id].id != null) {
    if (checkList[id] == 0) {
      $("#check" + id).prop("checked", true);
      checkList[id] = 1;
    } else {
      $("#check" + id).prop("checked", false);
      checkList[id] = 0;
    }
    hot.render();
    updateLayout();
  }
}
function updateLayout() {
  let disposable = true,
    status = 1,
    diff = false,
    check = false,
    verify = false;
  allChecked = true;

  /**
   * Set active item in colunm status
   */
  for (let i = 0; i < checkList.length; i++) {
    let cellStatus = hot.getDataAtCell(i, 1);

    if (cellStatus != null) {
      if (checkList[i] == 1) {
        if (cellStatus == 1 && "Y" == sourceData[i].userVerifyStatus) {
          verify = true;
        }
        check = true;
        if (cellStatus > 1) {
          disposable = false;
        }
        if (status != 1 && status != cellStatus) {
          diff = true;
        } else {
          status = cellStatus;
        }
      } else {
        allChecked = false;
      }
    }
  }

  $(".checker").prop("checked", allChecked);
  if (disposable) {
    $("#deleteBtn").prop("disabled", false);
  } else {
    $("#deleteBtn").prop("disabled", true);
  }

  if (diff) {
    status = 1;
  } else {
    status++;
  }
  if (!check) {
    $("#deleteBtn").prop("disabled", true);
    status = 1;
  }
  switch (status) {
    case 1:
      setLayoutRegisterStatus();
      break;
    case 2:
      setLayoutVerifyUserStatus();
      if (verify) {
        $("#verifyBtn").prop("disabled", true);
        $("#deleteBtn").prop("disabled", true);
      }
      break;
    case 3:
      setLayoutPaymentStatus();
      break;
    case 4:
      setLayoutCustomStatus();
      break;
    case 5:
      setLayoutFinishStatus();
      break;
    default:
      break;
  }
  setLayoutConfirmRequestContSpecial();
}

/**
 * @param {none}
 * @author Khanh
 * @description set status for btn request
 */
function setLayoutConfirmRequestContSpecial() {
  $("#requestShipmentDetailBtn").prop("disabled", isDisableBtnRequestConfirm());
}

function isDisableBtnRequestConfirm() {
  let result = false; // true: enable btn || false: disable btn
  for (let i = 0; i < checkList.length; ++i) {
    if (checkList[i] == 1) {
      const statusResult = getStatusContFollowIndex(i);
      if (
        !statusResult ||
        statusResult == CONT_SPECIAL_STATUS.REQ ||
        statusResult == CONT_SPECIAL_STATUS.YES
      ) {
        result = true;
      }
    }
  }

  return result;
}

// LOAD SHIPMENT DETAIL LIST
function loadShipmentDetail(id) {
  $.modal.loading("Đang xử lý ...");
  $.ajax({
    url: prefix + "/shipment/" + id + "/shipment-detail",
    method: "GET",
    success: function (data) {
      $.modal.closeLoading();
      if (data.code == 0) {
        sourceData = data.shipmentDetails;
        if (rowAmount < sourceData.length) {
          sourceData = sourceData.slice(0, rowAmount);
        }
        sourceData.forEach(function (element, index) {
          if (
            element.sztp &&
            element.sztp.length > 3 &&
            element.sztp.substring(0, 4).includes("R")
          ) {
            temperatureDisable[index] = 0;
          }
        });
        hot.destroy();
        isDestroy = true;
        configHandson();
        hot = new Handsontable(dogrid, config);
        hot.loadData(sourceData);
        isDestroy = false;
        hot.render();
        onChangeFlg = false;
        if (sourceData.length > 0) {
          currentVesselVoyage = sourceData[0].vslNm;
          let shipmentDetail = new Object();
          for (let i = 0; i < berthplanList.length; i++) {
            if (currentVesselVoyage == berthplanList[i].vslAndVoy) {
              currentEta = berthplanList[i].eta;
              shipmentDetail.vslNm = berthplanList[i].vslNm;
              shipmentDetail.voyNo = berthplanList[i].voyNo;
              shipmentDetail.year = berthplanList[i].year;
              $.modal.loading("Đang xử lý ...");
              $.ajax({
                url: ctx + "/logistic/pods",
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(shipmentDetail),
                success: function (data) {
                  $.modal.closeLoading();
                  if (data.code == 0) {
                    hot.updateSettings({
                      cells: function (row, col, prop) {
                        if (col == 7) {
                          let cellProperties = {};
                          dischargePortList = data.dischargePorts;
                          cellProperties.source = dischargePortList;
                          return cellProperties;
                        }
                      },
                    });
                  }
                },
              });
            }
          }
        }
      }
    },
    error: function (data) {
      $.modal.closeLoading();
    },
  });
}

function reloadShipmentDetail() {
  checkList = Array(rowAmount).fill(0);
  temperatureDisable = Array(rowAmount).fill(1);
  sztpListDisable = Array(rowAmount).fill(0);
  allChecked = false;
  $(".checker").prop("checked", false);
  for (let i = 0; i < checkList.length; i++) {
    $("#check" + i).prop("checked", false);
  }
  $("#deleteBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#customBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
  $("#exportReceiptBtn").prop("disabled", true);
  $("#exportPackingListBtn").prop("disabled", true);
  setLayoutRegisterStatus();
  loadShipmentDetail(shipmentSelected.id);
}

// GET CHECKED SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = truex
function getDataSelectedFromTable(isValidate) {
  let myTableData = hot.getSourceData();
  let errorFlg = false;
  let cleanedGridData = [];
  for (let i = 0; i < checkList.length; i++) {
    if (checkList[i] == 1 && Object.keys(myTableData[i]).length > 0) {
      cleanedGridData.push(myTableData[i]);
    }
  }
  shipmentDetailIds = "";
  shipmentDetails = [];
  processOrderIds = "";
  let temProcessOrderIds = [];
  $.each(cleanedGridData, function (index, object) {
    let shipmentDetail = new Object();
    shipmentDetail.bookingNo = shipmentSelected.bookingNo;
    shipmentDetail.containerNo = object["containerNo"];
    shipmentDetail.customStatus = object["customStatus"];
    shipmentDetail.processStatus = object["processStatus"];
    shipmentDetail.paymentStatus = object["paymentStatus"];
    shipmentDetail.userVerifyStatus = object["userVerifyStatus"];

    shipmentDetail.status = object["status"];
    shipmentDetail.shipmentId = shipmentSelected.id;
    shipmentDetail.id = object["id"];
    shipmentDetails.push(shipmentDetail);
    if (
      object["processOrderId"] != null &&
      !temProcessOrderIds.includes(object["processOrderId"])
    ) {
      temProcessOrderIds.push(object["processOrderId"]);
      processOrderIds += object["processOrderId"] + ",";
    }
    shipmentDetailIds += object["id"] + ",";
  });

  if (processOrderIds != "") {
    processOrderIds = processOrderIds.substring(0, processOrderIds.length - 1);
  }

  // Get result in "selectedList" letiable
  if (shipmentDetails.length == 0 && isValidate) {
    $.modal.alert("Bạn chưa chọn container.");
    errorFlg = true;
  } else {
    shipmentDetailIds = shipmentDetailIds.substring(
      0,
      shipmentDetailIds.length - 1
    );
  }
  if (errorFlg) {
    return false;
  } else {
    return true;
  }
}

// GET SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataFromTable(isValidate) {
  let myTableData = hot.getSourceData();
  let errorFlg = false;
  let cleanedGridData = [];
  for (let i = 0; i < checkList.length; i++) {
    if (Object.keys(myTableData[i]).length > 0) {
      if (
        myTableData[i].containerNo ||
        myTableData[i].consignee ||
        myTableData[i].opeCode ||
        myTableData[i].vslNm ||
        myTableData[i].voyNo ||
        myTableData[i].sztp ||
        myTableData[i].temperature ||
        myTableData[i].wgt ||
        myTableData[i].cargoType ||
        myTableData[i].dischargePort ||
        myTableData[i].remark
      ) {
        cleanedGridData.push(myTableData[i]);
      }
    }
  }
  shipmentDetails = [];
  contList = [];
  conts = "";
  let consignee, opecode, vessel, voyage, pod;
  if (cleanedGridData.length > 0) {
    consignee = cleanedGridData[0].consignee;
    vessel = cleanedGridData[0].vslNm;
    voyage = cleanedGridData[0].voyNo;
    pod = cleanedGridData[0].dischargePort;
  }
  $.each(cleanedGridData, function (index, object) {
    let shipmentDetail = new Object();
    if (isValidate) {
      if (!object["containerNo"]) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Vui lòng nhập số container!"
        );
        errorFlg = true;
        return false;
      } else if (!/^[A-Z]{4}[0-9]{7}$/g.test(object["containerNo"])) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Số container không hợp lệ!"
        );
        errorFlg = true;
        return false;
      } else if (!object["consignee"]) {
        $.modal.alertError("Hàng " + (index + 1) + ":Vui lòng chọn chủ hàng!");
        errorFlg = true;
        return false;
      } else if (!object["vslNm"]) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Vui lòng chọn tàu - chuyến!"
        );
        errorFlg = true;
        return false;
      } else if (!object["sztp"]) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Vui lòng chọn kích thước!"
        );
        errorFlg = true;
        return false;
      } else if (!object["wgt"]) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Vui lòng nhập trọng lượng (kg)!"
        );
        errorFlg = true;
        return false;
      } else if (object["wgt"] < 1000) {
        $.modal.alertError(
          "Hàng " +
          (index + 1) +
          ": Trọng lượng (tính bằng kg) quá nhỏ, vui lòng kiểm tra lại!"
        );
        errorFlg = true;
        return false;
      } else if (object["wgt"] > 99999) {
        $.modal.alertError(
          "Hàng " +
          (index + 1) +
          ": Trọng lượng quá lớn (hơn 100 tấn), vui lòng kiểm tra lại!"
        );
        errorFlg = true;
        return false;
      } else if (!object["dischargePort"]) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Hãy chọn cảng dỡ hàng từ danh sách!"
        );
        errorFlg = true;
        return false;
      } else if (!object["cargoType"]) {
        $.modal.alertError(
          "Hàng " + (index + 1) + ": Hãy chọn loại hàng từ danh sách!"
        );
        errorFlg = true;
        return false;
      } else if (consignee != object["consignee"]) {
        $.modal.alertError("Tên chủ hàng không được khác nhau!");
        errorFlg = true;
        return false;
        //            } else if (consignee != object["consignee"]) {
        //                $.modal.alertError("Tên chủ hàng không được khác nhau!");
        //                errorFlg = true;
        //                return false;
        //            } else if (vessel != object["vslNm"]) {
        //                $.modal.alertError("Tàu và Chuyến không được khác nhau!");
        //                errorFlg = true;
        //                return false;
      } else if (pod.split(": ")[0] != object["dischargePort"].split(": ")[0]) {
        $.modal.alertError("Cảng dỡ hàng không được khác nhau!");
        errorFlg = true;
        return false;
      }
    }
    consignee = object["consignee"];
    vessel = object["vslNm"];
    pod = object["dischargePort"];
    shipmentDetail.bookingNo = shipmentSelected.bookingNo;
    shipmentDetail.containerNo = object["containerNo"];
    if (object["status"] == 1 || object["status"] == null) {
      conts += object["containerNo"] + ",";
    }
    contList.push(object["containerNo"]);
    let sizeType = object["sztp"].split(": ");
    if (
      sizeType[0] &&
      sizeType[0].length > 3 &&
      sizeType[0].substring(0, 4).includes("R") &&
      (object["temperature"] == null || object["temperature"] === "")
    ) {
      $.modal.alertError(
        "Hàng " + (index + 1) + ": Vui lòng nhập nhiệt độ cho container lạnh!"
      );
      errorFlg = true;
      return false;
    }
    shipmentDetail.sztp = sizeType[0];
    shipmentDetail.sztpDefine = sizeType[1];
    shipmentDetail.temperature = object["temperature"];
    shipmentDetail.consignee = object["consignee"];
    shipmentDetail.eta = object["eta"];
    shipmentDetail.wgt = object["wgt"];

    /**
     * add information detail of container special
     */

    // shipmentDetail = {
    //   ...sourceData[index],
    //   ...shipmentDetail,
    //   ...detailInformationForContainerSpecial.data[index],
    // };

    if (detailInformationForContainerSpecial.data[index]) {
      var dataModal = detailInformationForContainerSpecial.data[index];
      shipmentDetail.dangerousImo = dataModal.dangerousImo;
      shipmentDetail.dangerousNameProduct = dataModal.dangerousNameProduct;
      shipmentDetail.dangerousPacking = dataModal.dangerousPacking;
      shipmentDetail.dangerousUnno = dataModal.dangerousUnno;
      shipmentDetail.humidity = dataModal.humidity;
      shipmentDetail.oversizeLeft = dataModal.oversizeLeft;
      shipmentDetail.oversizeRight = dataModal.oversizeRight;
      shipmentDetail.oversizeTop = dataModal.oversizeTop;
      shipmentDetail.oversizeLeft = dataModal.oversizeLeft;
      shipmentDetail.temperature = dataModal.temperature;
      shipmentDetail.ventilation = dataModal.ventilation;
    }

    // console.log(detailInformationForContainerSpecial.data[index]);

    if (berthplanList) {
      for (let i = 0; i < berthplanList.length; i++) {
        if (object["vslNm"] == berthplanList[i].vslAndVoy) {
          shipmentDetail.vslNm = berthplanList[i].vslNm;
          shipmentDetail.voyNo = berthplanList[i].voyNo;
          shipmentDetail.year = berthplanList[i].year;
          shipmentDetail.vslName = berthplanList[i].vslAndVoy.split(" - ")[1];
          shipmentDetail.voyCarrier = berthplanList[i].voyCarrier;
          shipmentDetail.etd = berthplanList[i].etd;
        }
      }
    }
    shipmentDetail.commodity = object["commodity"];
    shipmentDetail.dischargePort = object["dischargePort"].split(": ")[0];
    shipmentDetail.cargoType = object["cargoType"].substring(0, 2);
    shipmentDetail.remark = object["remark"];
    shipmentDetail.sealNo = object["sealNo"];
    shipmentDetail.shipmentId = shipmentSelected.id;
    shipmentDetail.id = object["id"];
    shipmentDetails.push(shipmentDetail);
  });

  conts.substring(0, conts.length - 1);

  if (isValidate) {
    contList.sort();
    let contTemp = "";
    $.each(contList, function (index, cont) {
      if (cont != "" && cont == contTemp) {
        $.modal.alertError(
          "Số container " + cont + " bị trùng, vui lòng kiểm tra lại!"
        );
        errorFlg = true;
        return false;
      }
      contTemp = cont;
    });
  }

  // Get result in "selectedList" variable
  if (shipmentDetails.length == 0 && !errorFlg) {
    $.modal.alert("Vui lòng nhập thông tin cho các container đã chọn.");
    errorFlg = true;
  }

  if (errorFlg) {
    return false;
  } else {
    return true;
  }
}

/**
 * Save when click btn id="saveShipmentDetailBtn"
 */
function saveShipmentDetail() {
  if (getDataFromTable(true)) {
    if (
      shipmentDetails.length > 0 &&
      shipmentDetails.length <= shipmentSelected.containerAmount
    ) {
      shipmentDetails[0].processStatus = conts;

      const payload = checkList.includes(1) ? getDataFromListChecked() : shipmentDetails;

      $.modal.loading("Đang xử lý...");
      $.ajax({
        url: prefix + "/" + shipmentSelected.id + "/shipment-detail",
        method: "post",
        contentType: "application/json",
        accept: "text/plain",
        data: JSON.stringify(payload),
        dataType: "text",
        success: function (data) {
          var result = JSON.parse(data);
          if (result.code == 0) {
            $.modal.msgSuccess(result.msg);

            reloadShipmentDetail();
          } else {

            if (result.conts != null) {
              $.modal.alertError(
                "Các container sau đã được thực hiện lệnh nâng/hạ trong hệ thống của Cảng. Xin vui lòng kiểm tra lại dữ liệu.<br>" +
                result.conts
              );
            } else {
              $.modal.alertError(result.msg);
            }
          }
          $.modal.closeLoading();
        },
        error: function (result) {
          $.modal.alertError(
            "Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau."
          );
          $.modal.closeLoading();
        },
      });
    } else if (shipmentDetails.length > shipmentSelected.containerAmount) {
      $.modal.alertError(
        "Số container nhập vào vượt quá số container khai báo của lô."
      );
    } else {
      $.modal.alertError("Hãy nhập thông tin chi tiết lô.");
    }
  }
}


function saveShipmentDetailFollowIndexNew(index) {
  if (getDataFromTable(true)) {
    if (
      shipmentDetails.length > 0 &&
      shipmentDetails.length <= shipmentSelected.containerAmount
    ) {
      shipmentDetails[0].processStatus = conts;

      $.modal.loading("Đang xử lý...");
      $.ajax({
        url: prefix + "/" + shipmentSelected.id + "/shipment-detail-special",
        method: "post",
        contentType: "application/json",
        accept: "text/plain",
        data: JSON.stringify([shipmentDetails[index]]),
        dataType: "text",
        success: function (data) {
          var result = JSON.parse(data);
          if (result.code == 0) {
            $.modal.msgSuccess(result.msg);
            reloadShipmentDetail();
          } else {
            if (result.conts != null) {
              $.modal.alertError(
                "Các container sau đã được thực hiện lệnh nâng/hạ trong hệ thống của Cảng. Xin vui lòng kiểm tra lại dữ liệu.<br>" +
                result.conts
              );
            } else {
              $.modal.alertError(result.msg);
            }
          }
          $.modal.closeLoading();
        },
        error: function (result) {
          $.modal.alertError(
            "Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau."
          );
          $.modal.closeLoading();
        },
      });
    } else if (shipmentDetails.length > shipmentSelected.containerAmount) {
      $.modal.alertError(
        "Số container nhập vào vượt quá số container khai báo của lô."
      );
    } else {
      $.modal.alertError("Hãy nhập thông tin chi tiết lô.");
    }
  }
}



function saveShipmentDetailFollowIndex(index) {
  if (getDataFromTable(true)) {
    if (
      shipmentDetails.length > 0 &&
      shipmentDetails.length <= shipmentSelected.containerAmount
    ) {
      shipmentDetails[0].processStatus = conts;

      $.modal.loading("Đang xử lý...");
      $.ajax({
        url: prefix + "/" + shipmentSelected.id + "/shipment-detail",
        method: "post",
        contentType: "application/json",
        accept: "text/plain",
        data: JSON.stringify([shipmentDetails[index]]),
        dataType: "text",
        success: function (data) {
          var result = JSON.parse(data);
          if (result.code == 0) {
            $.modal.msgSuccess(result.msg);
            reloadShipmentDetail();
          } else {
            if (result.conts != null) {
              $.modal.alertError(
                "Các container sau đã được thực hiện lệnh nâng/hạ trong hệ thống của Cảng. Xin vui lòng kiểm tra lại dữ liệu.<br>" +
                result.conts
              );
            } else {
              $.modal.alertError(result.msg);
            }
          }
          $.modal.closeLoading();
        },
        error: function (result) {
          $.modal.alertError(
            "Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau."
          );
          $.modal.closeLoading();
        },
      });
    } else if (shipmentDetails.length > shipmentSelected.containerAmount) {
      $.modal.alertError(
        "Số container nhập vào vượt quá số container khai báo của lô."
      );
    } else {
      $.modal.alertError("Hãy nhập thông tin chi tiết lô.");
    }
  }
}

function getDataFromListChecked() {
  let result = [];
  for (let checkIndex = 0; checkIndex < checkList.length; ++checkIndex) {
    if (checkList[checkIndex] == 1) {
      result.push(shipmentDetails[checkIndex]);
    }
  }
  return result;
}

// DELETE SHIPMENT DETAIL
function deleteShipmentDetail() {
  if (getDataSelectedFromTable(true) && shipmentDetails.length > 0) {
    $.modal.confirmShipment("Xác nhận xóa khai báo container ?", function () {
      $.modal.loading("Đang xử lý...");
      $.ajax({
        url: prefix + "/" + shipmentSelected.id + "/shipment-detail/delete",
        method: "post",
        contentType: "application/json",
        data: JSON.stringify({ ids: shipmentDetailIds }),
        success: function (result) {
          if (result.code == 0) {
            $.modal.msgSuccess(result.msg);
            reloadShipmentDetail();
          } else {
            $.modal.alertError(result.msg);
          }
          $.modal.closeLoading();
        },
        error: function (result) {
          $.modal.alertError(
            "Có lỗi trong quá trình xóa dữ liệu, vui lòng thử lại."
          );
          $.modal.closeLoading();
        },
      });
    });
  }
}

// Handling logic
function verify() {
  getDataSelectedFromTable(true);
  if (shipmentDetails.length > 0) {
    let isCanVerify = true;
    for (let i = 0; i < checkList.length; ++i) {
      if (checkList[i] == 1) {
        if (!(getStatusContFollowIndex(i) == CONT_SPECIAL_STATUS.YES)) {
          isCanVerify = false;
        }
      }
    }

    $.ajax({
      url: prefix + "/shipment-detail/validation",
      method: "POST",
      data: {
        shipmentDetailIds: shipmentDetailIds,
      },
      success: function (res) {
        if (res.code != 0) {
          $.modal.alertWarning(res.msg);
        } else {
          $.modal.openCustomForm(
            "Xác nhận làm lệnh",
            prefix + "/otp/cont-list/confirmation/" + shipmentDetailIds,
            700,
            500
          );
        }
      },
      error: function (err) {
        $.modal.alertWarning("Lỗi hệ thống, quý khách vui lòng thử lại sau.");
      },
    });
  }
}

function verifyOtp(shipmentDtIds, taxCode, creditFlag) {
  getDataSelectedFromTable(true);
  if (shipmentDetails.length > 0) {
    $.modal.openCustomForm(
      "Xác thực OTP",
      prefix +
      "/otp/verification/" +
      shipmentDtIds +
      "/" +
      creditFlag +
      "/" +
      taxCode +
      "/" +
      shipmentSelected.id,
      600,
      350
    );
  }
}

function pay() {
  getDataSelectedFromTable(true);
  if (shipmentDetails.length > 0) {
    $.modal.openCustomForm(
      "Thanh toán",
      prefix + "/payment/" + processOrderIds,
      800,
      400
    );
  }
}

function checkCustomStatus() {
  getDataSelectedFromTable(true);
  if (shipmentDetails.length > 0) {
    $.modal.openCustomForm(
      "Khai báo hải quan",
      prefix + "/custom-status/" + shipmentDetailIds,
      720,
      500
    );
  }
}

function exportBill() { }

// Handling UI STATUS
function setLayoutRegisterStatus() {
  $("#registerStatus").removeClass("label-primary disable").addClass("active");
  $("#verifyStatus").removeClass("label-primary active").addClass("disable");
  $("#paymentStatus").removeClass("label-primary active").addClass("disable");
  $("#customStatus").removeClass("label-primary active").addClass("disable");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
  $("#verifyBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#customBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
  $("#exportReceiptBtn").prop("disabled", true);
  $("#exportPackingListBtn").prop("disabled", true);
}

function setLayoutVerifyUserStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("label-primary disable").addClass("active");
  $("#paymentStatus").removeClass("active label-primary").addClass("disable");
  $("#customStatus").removeClass("active label-primary").addClass("disable");
  $("#finishStatus").removeClass("active label-primary").addClass("disable");
  $("#verifyBtn").prop("disabled", false);
  $("#payBtn").prop("disabled", true);
  $("#customBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
  $("#exportReceiptBtn").prop("disabled", true);
  $("#exportPackingListBtn").prop("disabled", false);
}

function setLayoutPaymentStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("active disable").addClass("label-primary");
  $("#paymentStatus").removeClass("label-primary disable").addClass("active");
  $("#customStatus").removeClass("active label-primary").addClass("disable");
  $("#finishStatus").removeClass("active label-primary").addClass("disable");
  $("#deleteBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", false);
  $("#customBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", true);
  $("#exportReceiptBtn").prop("disabled", true);
  $("#exportPackingListBtn").prop("disabled", false);
}

function setLayoutCustomStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("active disable").addClass("label-primary");
  $("#paymentStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("label-primary disable").addClass("active");
  $("#finishStatus").removeClass("label-primary active").addClass("disable");
  $("#verifyBtn").prop("disabled", true);
  $("#deleteBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#customBtn").prop("disabled", false);
  $("#exportBillBtn").prop("disabled", false);
  $("#exportReceiptBtn").prop("disabled", false);
  $("#exportPackingListBtn").prop("disabled", false);
}

function setLayoutFinishStatus() {
  $("#registerStatus").removeClass("active disable").addClass("label-primary");
  $("#verifyStatus").removeClass("active disable").addClass("label-primary");
  $("#paymentStatus").removeClass("active disable").addClass("label-primary");
  $("#customStatus").removeClass("active disable").addClass("label-primary");
  $("#finishStatus").removeClass("label-primary disable").addClass("active");
  $("#deleteBtn").prop("disabled", true);
  $("#verifyBtn").prop("disabled", true);
  $("#payBtn").prop("disabled", true);
  $("#customBtn").prop("disabled", true);
  $("#exportBillBtn").prop("disabled", false);
  $("#exportReceiptBtn").prop("disabled", false);
  $("#exportPackingListBtn").prop("disabled", false);
}

function finishForm(result) {
  if (result.code == 0) {
    $.modal.alertSuccess(result.msg);
  } else {
    $.modal.alertError(result.msg);
  }
  reloadShipmentDetail();
}

function finishVerifyForm(result) {
  if (result.code == 0 || result.code == 301) {
    //$.modal.loading(result.msg);
    currentProcessId = result.processId;
    // CONNECT WEB SOCKET
    connectToWebsocketServer();

    showProgress("Đang xử lý ...");
    timeout = setTimeout(() => {
      setTimeout(() => {
        hideProgress();
        reloadShipmentDetail();
        $.modal.alertWarning(
          "Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi."
        );
      }, 1000);
    }, 200000);
  } else {
    reloadShipmentDetail();
    $.modal.alertWarning(
      "Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi."
    );
  }
}

function napasPaymentForm() {
  //$.modal.openFullWithoutButton("Cổng Thanh Toán", );
  window.open(ctx + "logistic/payment/napas/" + processOrderIds, "_blank");
}

function connectToWebsocketServer() {
  // Connect to WebSocket Server.
  $.websocket.connect({}, onConnected, onError);
}

function onConnected() {
  console.log("Connect socket.");
  currentSubscription = $.websocket.subscribe(
    currentProcessId + "/response",
    onMessageReceived
  );
}

function onError(error) {
  console.log(error);
  $.modal.alertError(
    "Could not connect to WebSocket server. Please refresh this page to try again!"
  );
  setTimeout(() => {
    hideProgress();
    $.modal.alertWarning(
      "Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi."
    );
  }, 1000);
  //$.modal.closeLoading();
}

function onMessageReceived(payload) {
  clearTimeout(timeout);
  setProgressPercent((currentPercent = 100));
  setTimeout(() => {
    hideProgress();

    let message = JSON.parse(payload.body);

    reloadShipmentDetail();

    if (message.code == 0) {
      $.modal.alertSuccess(message.msg);
    } else {
      $.modal.alertWarning(
        "Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi."
      );
    }

    // Close loading
    //$.modal.closeLoading();

    // Unsubscribe destination
    if (currentSubscription) {
      currentSubscription.unsubscribe();
    }

    // Close websocket connection
    $.websocket.disconnect(onDisconnected);
  }, 1000);
}

function onDisconnected() {
  console.log("Disconnected socket.");
}

function showProgress(title) {
  $(".progress-wrapper").show();
  $(".dim-bg").show();
  $("#titleProgress").text(title);
  $(".percent-text").text("0%");
  currentPercent = 0;
  interval = setInterval(function () {
    if (currentPercent <= 99) {
      setProgressPercent(++currentPercent);
    }
    if (currentPercent >= 99) {
      clearInterval(interval);
    }
  }, 1000);
}

function setProgressPercent(percent) {
  $("#progressBar").prop("aria-valuenow", percent);
  $("#progressBar").css("width", percent + "%");
  $(".percent-text").text(percent + "%");
}

function hideProgress() {
  $(".progress-wrapper").hide();
  $(".dim-bg").hide();
  currentPercent = 0;
  $(".percent-text").text("0%");
  setProgressPercent(0);
}

function exportReceipt() {
  if (!shipmentSelected) {
    $.modal.alertError("Bạn chưa chọn Lô!");
    return;
  }
  $.modal.openTab(
    "In Biên Nhận",
    ctx + "logistic/print/receipt/shipment/" + shipmentSelected.id
  );
}

function exportPackingList() {
  if (!shipmentSelected) {
    $.modal.alertError("Bạn chưa chọn Lô!");
    return;
  }
  $.modal.openTab(
    "In Packing List",
    ctx + "logistic/print/shipment/" + shipmentSelected.id + "/packing-list"
  );
}
//,${sourceData[row].cargoType}'

function openDetail(id, containerNo, sztp, row, cargoType) {
  if (!id) {
    $.modal.alertWarning(
      "Container chưa được lưu. Vui lòng lưu khai báo trước."
    );
  }

  /*  if (sztp.substring(2, 3) == "G" && cargoType != "DG") {
      $.modal.alertWarning(
        "Loại hàng không phải là cont nguy hiểm. Vui lòng nhập loại hàng là cont nguy hiểm và thử lại!"
      );
    }*/

  /*if(sztp.substring(2,3) == "G" && cargoType != "DG"){
    $.modal.alertWarning("Loại hàng không phải là cont nguy hiểm. Vui lòng nhập loại hàng là cont nguy hiểm và thử lại!");
  }*/

  else {
    detailInformationForContainerSpecial.indexSelected = row;
    $.modal.openCustomForm(
      "Khai báo chi tiết",
      `${prefix}/shipment-detail/${id}/cont/${containerNo}/sztp/${sztp}/detail`,
      800,
      460
    );
  }
}

function removeShipment() {
  if (!shipmentSelected) {
    $.modal.alertError("Bạn chưa chọn Lô!");
    return;
  } else {
    //1- chua khai bao cont, 2- khai bao nhung chua lam cac buoc tiep theo
    if (shipmentSelected.status == "1" || shipmentSelected.status == "2") {
      $.modal.confirmShipment(
        "Xác nhận thực hiện xóa Lô " + shipmentSelected.id + "  ?",
        function () {
          $.modal.loading("Đang xử lý...");
          $.ajax({
            url: ctx + "logistic/shipment/remove",
            type: "POST",
            data: {
              id: shipmentSelected.id,
            },
          }).done(function (rs) {
            $.modal.closeLoading();
            if (rs.code == 0) {
              $.modal.msgSuccess(rs.msg);
              loadTable();
            } else {
              $.modal.msgError(rs.msg);
            }
          });
        }
      );
    } else {
      $.modal.msgError("Không thể xóa Lô " + shipmentSelected.id);
    }
  }
}

function dateToString(date) {
  return (
    ("0" + date.getDate()).slice(-2) +
    "/" +
    ("0" + (date.getMonth() + 1)).slice(-2) +
    "/" +
    date.getFullYear() +
    " " +
    ("0" + date.getHours()).slice(-2) +
    ":" +
    ("0" + date.getMinutes()).slice(-2) +
    ":" +
    ("0" + date.getSeconds()).slice(-2)
  );
}

function search() {
  shipmentSearch.bookingNo = $("#bookingNo").textbox("getText");
  shipmentSearch.params.containerNo = $("#containerNo").textbox("getValue");
  shipmentSearch.params.consignee = $("#consignee").textbox("getValue");
  shipmentSearch.params.fromDate = $("#fromDate").datebox("getValue");
  shipmentSearch.params.toDate = $("#toDate").datebox("getValue");
  loadTable();
}

function clearInput() {
  $("#bookingNo").textbox("setText", "");
  $("#containerNo").textbox("setText", "");
  $("#consignee").textbox("setText", "");
  $("#fromDate").datebox("setValue", "");
  $("#toDate").datebox("setValue", "");
  shipmentSearch = new Object();
  shipmentSearch.params = new Object();
  shipmentSearch.serviceType = 4;
  fromDate = null;
  toDate = null;
  loadTable();
}

function loadListComment(shipmentCommentId) {
  let req = {
    serviceType: 4,
    shipmentId: shipmentSelected.id,
  };
  $.ajax({
    url: ctx + "logistic/comment/list",
    method: "POST",
    contentType: "application/json",
    data: JSON.stringify(req),
    success: function (data) {
      if (data.code == 0) {
        let html = "";
        // set title for panel comment
        let commentTitle = "<span>Hỗ Trợ<span>";
        let commentNumber = 0;
        if (data.shipmentComments != null) {
          data.shipmentComments.forEach(function (element, index) {
            let createTime = element.createTime;
            let date = "";
            let time = "";
            if (createTime) {
              date =
                createTime.substring(8, 10) +
                "/" +
                createTime.substring(5, 7) +
                "/" +
                createTime.substring(0, 4);
              time = createTime.substring(10, 19);
            }

            let seenBackground = "";
            if (
              (shipmentCommentId && shipmentCommentId == element.id) ||
              !element.seenFlg
            ) {
              seenBackground = 'style="background-color: #ececec;"';
              commentNumber++;
            }

            html += "<div " + seenBackground + ">";
            // User name comment and date time comment
            html +=
              '<div><i style="font-size: 15px; color: #015198;" class="fa fa-user-circle" aria-hidden="true"></i><span> <a>' +
              element.userName +
              " (" +
              element.userAlias +
              ")</a>: <i>" +
              date +
              " at " +
              time +
              "</i></span></div>";
            // Topic comment
            html +=
              "<div><span><strong>Yêu cầu:</strong> " +
              element.topic +
              "</span></div>";
            // Content comment
            html +=
              "<div><span>" +
              element.content.replaceAll("#{domain}", domain) +
              "</span></div>";
            html += "</div>";
            html += "<hr>";
          });
        }
        commentTitle +=
          ' <span class="round-notify-count">' + commentNumber + "</span>";
        $("#right-layout")
          .layout("panel", "expandSouth")
          .panel("setTitle", commentTitle);
        $("#commentList").html(html);
        // $("#comment-div").animate({ scrollTop: $("#comment-div")[0].scrollHeight}, 1000);
        if (sId != null) {
          $("#right-layout").layout("expand", "south");
          shipmentSearch.id = null;
          sId = null;
        }
      }
    },
  });
}

function addComment() {
  let topic = $("#topic").textbox("getText").trim();
  let content = $(".summernote").summernote("code"); // get editor content
  let errorFlg = false;
  let contentTemp = content
    .replace(regexRemoveHtml, "")
    .replaceAll("&nbsp;", "")
    .trim();
  if (!topic) {
    errorFlg = true;
    $.modal.alertWarning("Vui lòng nhập chủ đề.");
  } else if (!contentTemp) {
    errorFlg = true;
    $.modal.alertWarning("Vui lòng nhập nội dung.");
  }
  if (!errorFlg) {
    let req = {
      topic: topic,
      content: content.replaceAll(domain, "#{domain}"),
      shipmentId: shipmentSelected.id,
    };
    $.ajax({
      url: prefix + "/shipment/comment",
      type: "post",
      contentType: "application/json",
      data: JSON.stringify(req),
      beforeSend: function () {
        $.modal.loading("Đang xử lý, vui lòng chờ...");
      },
      success: function (result) {
        $.modal.closeLoading();
        if (result.code == 0) {
          loadListComment(result.shipmentCommentId);
          $.modal.msgSuccess("Gửi thành công.");
          $("#topic").textbox("setText", "");
          // reset editor content
          $(".summernote").summernote("code", "");
        } else {
          $.modal.msgError("Gửi thất bại.");
        }
      },
      error: function (error) {
        $.modal.closeLoading();
        $.modal.msgError("Gửi thất bại.");
      },
    });
  }
}

function requestCancelOrder() {
  getDataSelectedFromTable(true);
  if (shipmentDetails.length > 0) {
    // Check if list cont exists cont has been process
    let containers = "";
    shipmentDetails.forEach(function (element) {
      if (element.processStatus != "Y") {
        containers += element.containerNo + ",";
      }
    });
    if (containers.length > 0) {
      containers = containers.substring(0, containers.length - 1);
      $.modal.alertWarning(
        "Các contaienr quý khách chọn chưa được thực hiện làm lệnh, quý khách không thể yêu cầu hủy lệnh cho những container này."
      );
    } else {
      openFormRemarkBeforeReqCancelOrder();
    }
  }
}

function openFormRemarkBeforeReqCancelOrder() {
  // Form confirm req supply cont
  layer.open({
    type: 2,
    area: [500 + "px", 230 + "px"],
    fix: true,
    maxmin: true,
    shade: 0.3,
    title: "Xác Nhận",
    content: prefix + "/req/cancel/confirmation",
    btn: ["Xác Nhận", "Hủy"],
    shadeClose: false,
    yes: function (index, layero) {
      let childLayer = layero.find("iframe")[0].contentWindow.document;
      $.modal.loading("Đang xử lý ...");
      $.ajax({
        url: prefix + "/order-cancel/shipment-detail",
        method: "POST",
        data: {
          shipmentDetailIds: shipmentDetailIds,
          contReqRemark: $(childLayer).find("#message").val(),
        },
        success: function (result) {
          if (result.code == 0) {
            $.modal.alertSuccess(result.msg);
            reloadShipmentDetail();
          } else {
            $.modal.alertError(result.msg);
          }
          $.modal.closeLoading();
          layer.close(index);
        },
        error: function (result) {
          $.modal.alertError(
            "Có lỗi trong quá trình xử lý dữ liệu, vui lòng thử lại sau."
          );
          $.modal.closeLoading();
        },
      });
    },
    cancel: function (index) {
      return true;
    },
  });
}

/**
 * @param {}
 * @author Khanh
 * @description handle click btn cancel request confirm for cont special
 */
function requestConfirmContSpecialCancelOrder() {
  const CHECKED = 1;
  if (!checkList.includes(CHECKED)) {
    $.modal.alertWarning("Quý khách chưa chọn container.");
  } else {
    let isNeedRequest = true;
    for (let i = 0; i < checkList.length; ++i) {
      let dataColunmSizeCont = getCodeSizeContFromDataTableHandsonFollowIndex(
        i
      );
      // status is checked
      if (checkList[i] == 1) {
        if (!(getStatusContFollowIndex(i) == CONT_SPECIAL_STATUS.REQ)) {
          isNeedRequest = false;
        }
      }
    }

    if (!isNeedRequest) {
      $.modal.alertWarning(
        "Các contaienr quý khách chọn chưa được thực hiện yêu cầu xác nhận, quý khách không thể yêu cầu hủy xác nhận cho những container này."
      );
    } else {
      openFormRemarkBeforeReqSpecialCancelOrder();
    }
  }
}

/**
 * @param {none}
 * @author Khanh
 * @description Call api conform cancel request cont special
 */
function openFormRemarkBeforeReqSpecialCancelOrder() {
  // Form confirm req supply cont
  getDataSelectedFromTable(true);
  layer.open({
    type: 2,
    area: [500 + "px", 230 + "px"],
    fix: true,
    maxmin: true,
    shade: 0.3,
    title: "Xác Nhận",
    content: prefix + "/req/cancel/confirmation",
    btn: ["Xác Nhận", "Hủy"],
    shadeClose: false,
    yes: function (index, layero) {
      let childLayer = layero.find("iframe")[0].contentWindow.document;
      $.modal.loading("Đang xử lý ...");

      $.ajax({
        url: prefix + "/request-special-cancel/shipment-detail",
        method: "POST",
        data: {
          shipmentDetailIds: shipmentDetailIds,
          contReqRemark: $(childLayer).find("#message").val(),
        },
        success: function (result) {
          if (result.code == 0) {
            $.modal.alertSuccess(result.msg);
            reloadShipmentDetail();
          } else {
            $.modal.alertError(result.msg);
          }
          $.modal.closeLoading();
          layer.close(index);
        },
        error: function (result) {
          $.modal.alertError(
            "Có lỗi trong quá trình xử lý dữ liệu, vui lòng thử lại sau."
          );
          $.modal.closeLoading();
        },
      });
    },
    cancel: function (index) {
      return true;
    },
  });
}

/**
 * @param {} data
 * @author Khanh
 * @description data get from detail.js
 */
function submitDataFromDetailModal(data) {

  const { indexSelected } = detailInformationForContainerSpecial;
  detailInformationForContainerSpecial.data[indexSelected] = data;
  //saveShipmentDetailFollowIndex(indexSelected);
  saveShipmentDetailFollowIndexNew(indexSelected);

}

/**
 * @param {} none
 * @author Khanh
 * @description Gửi request yêu cầu xác nhận cont để admin xét duyệt
 */
function requestConfirmShipmentDetail() {
  if (shipmentSelected == null) {
    $.modal.alertError("Chưa chọn lô! Vui lòng chọn lô trước khi thao tác.");
    return;
  } else {
    $.modal.confirmShipment(
      "Quý khách muốn yêu cầu xác nhận container " +
      getListContainerNoFromCheked().join(", ") +
      "  ?",
      function () {
        if (getDataFromTable(true)) {
          if (
            shipmentDetails.length > 0 &&
            shipmentDetails.length <= shipmentSelected.containerAmount
          ) {
            shipmentDetails[0].processStatus = conts;
            for (let i = 0; i < sourceData.length; ++i) {
              const data = shipmentDetails[i];
              // shipmentDetails[i] = {
              //   dangerous: sourceData[i].dangerous,
              //   oversize: sourceData[i].oversize,
              //   ...data,
              // };
              shipmentDetails[i].dangerous = sourceData[i].dangerous;
              shipmentDetails[i].oversize = sourceData[i].oversize;
            }

            let dataResult = [];
            for (let i = 0; i < checkList.length; ++i) {
              if (checkList[i] == 1) {
                dataResult.push(shipmentDetails[i]);
              }
            }
            $.modal.loading("Đang xử lý...");
            $.ajax({
              url:
                prefix +
                "/" +
                shipmentSelected.id +
                "/shipment-detail/request-confirm",
              method: "post",
              contentType: "application/json",
              accept: "text/plain",
              data: JSON.stringify(dataResult),
              dataType: "text",
              success: function (data) {
                var result = JSON.parse(data);
                if (result.code == 0) {
                  $.modal.msgSuccess(result.msg);
                  reloadShipmentDetail();
                } else {
                  if (result.conts != null) {
                    $.modal.alertError(
                      "Các container sau đã được thực hiện lệnh nâng/hạ trong hệ thống của Cảng. Xin vui lòng kiểm tra lại dữ liệu.<br>" +
                      result.conts
                    );
                  } else {
                    $.modal.alertError(result.msg);
                  }
                }
                $.modal.closeLoading();
              },
              error: function (result) {
                $.modal.alertError(
                  "Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau."
                );
                $.modal.closeLoading();
              },
            });
          } else if (
            shipmentDetails.length > shipmentSelected.containerAmount
          ) {
            $.modal.alertError(
              "Số container nhập vào vượt quá số container khai báo của lô."
            );
          } else {
            $.modal.alertError("Hãy nhập thông tin chi tiết lô.");
          }
        }
      }
    );
  }
}

function getListContainerNoFromCheked() {
  let result = [];
  for (let i = 0; i < sourceData.length; ++i) {
    if (checkList[i] == 1) {
      result.push(sourceData[i].containerNo);
    }
  }
  return result;
}

/**
 * @param {number} index
 * @author Khanh
 * @description get code size cont fron table in colunm 3
 * @returns {String} "22P0" || "22GO"
 */

function getCodeSizeContFromDataTableHandsonFollowIndex(index) {
  if (hot && hot.getDataAtCell(index, 3)) {
    return hot.getDataAtCell(index, 3).substring(0, 4);
  }
  return "";
}

function getStatusContFollowIndex(index) {
  if (
    !sourceData[index].oversize &&
    !sourceData[index].dangerous &&
    !sourceData[index].frozenStatus
  ) {
    return null;
  } else if (
    sourceData[index].dangerous == CONT_SPECIAL_STATUS.CANCEL ||
    sourceData[index].oversize == CONT_SPECIAL_STATUS.CANCEL ||
    sourceData[index].frozenStatus == CONT_SPECIAL_STATUS.CANCEL
  ) {
    // là cont bị từ chối
    return CONT_SPECIAL_STATUS.CANCEL;
  } else if (
    sourceData[index].dangerous == CONT_SPECIAL_STATUS.REQ ||
    sourceData[index].oversize == CONT_SPECIAL_STATUS.REQ ||
    sourceData[index].frozenStatus == CONT_SPECIAL_STATUS.REQ
  ) {
    // là cont đang chờ xác nhận
    return CONT_SPECIAL_STATUS.REQ;
  } else if (
    sourceData[index].dangerous == CONT_SPECIAL_STATUS.INIT ||
    sourceData[index].oversize == CONT_SPECIAL_STATUS.INIT ||
    sourceData[index].frozenStatus == CONT_SPECIAL_STATUS.INIT
  ) {
    // là cont đã được xét duyệt
    return CONT_SPECIAL_STATUS.INIT;
  } else {
    // là cont chỉ mới được tạo
    return CONT_SPECIAL_STATUS.YES;
  }
}
function getStatusContFollowIndex(index) {
  //console.log('co');
  return null;
  /*if (
    !sourceData[index].frozenStatus
  ) {
    return null;
  } else if (
    sourceData[index].frozenStatus == CONT_SPECIAL_STATUS.CANCEL
  ) {
    // là cont bị từ chối
    return CONT_SPECIAL_STATUS.CANCEL;
  } else if (
    sourceData[index].frozenStatus == CONT_SPECIAL_STATUS.REQ
  ) {
    // là cont đang chờ xác nhận
    return CONT_SPECIAL_STATUS.REQ;
  } else if (
    sourceData[index].frozenStatus == CONT_SPECIAL_STATUS.INIT
  ) {
    // là cont đã được xét duyệt
    return CONT_SPECIAL_STATUS.INIT;
  } else {
    // là cont chỉ mới được tạo
    return CONT_SPECIAL_STATUS.YES;
  }*/
}