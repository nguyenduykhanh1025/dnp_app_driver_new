"use strict";
const PREFIX = ctx + "om/support";
const DOCUMENT_HEIGHT = $(document).height();
var dogrid = document.getElementById("container-grid"), hot;

$(document).ready(function () {
  if (shipmentDetails != null && shipmentDetails.length > 0) {
    let date = new Date();
    date.setHours(0,0,0);
    if ('Y' == shipmentDetails[0].doStatus) {
      if (new Date(shipmentDetails[0].expiredDem).getTime() < date.getTime()) {
        $('.do-status').prop('disabled', true);
      } else {
        $('.do-status').prop('disabled', true);
        $('.expired-dem').hide();
      }
    } else {
      $('.expired-dem').hide();
    }
  }

  if (shipment) {
    if (shipment.blNo) {
      $('.bl-no').text(shipment.blNo);
    } else {
      $('.bl-no-div').hide();
    }
    if (shipment.bookingNo) {
      $('.booking-no').text(shipment.bookingNo);
    } else {
      $('.booking-no-div').hide();
    }
    $('.batch-code').text(shipment.id);
    switch (shipment.serviceType) {
      case 1:
        $('.service-type').text('Bốc Hàng');
        break;
      case 2:
        $('.service-type').text('Hạ Rỗng');
        break;
      case 3:
        $('.service-type').text('Bốc Rỗng');
        break;
      case 4:
        $('.service-type').text('Hạ Hàng');
        break;
    }
  }
});




function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  if (value) {
    $(td).addClass("htMiddle").html(value);
  }
  return td;
}

function sztpRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  if (value) {
    $(td).addClass("htMiddle").html(value);
  }
  return td;
}

function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  if (value) {
    $(td).addClass("htMiddle").html(value);
  }
  return td;
}

function feRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  if (value) {
    $(td).addClass("htMiddle").html(value);
  }
  return td;
}

function sealNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  if (value) {
    $(td).addClass("htMiddle").html(value);
  }
  return td;
}

function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  if (value) {
    $(td).addClass("htMiddle").html(value);
  }
  return td;
}

function expiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
  if (value) {
    value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
    $(td).addClass("htMiddle").html(value);
  }
  return td;
}

function opeCodeNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  if (value) {
    $(td).addClass("htMiddle").html(value);
  }
  return td;
}

function vslNmNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  if (value) {
    $(td).addClass("htMiddle").html(value);
  }
  return td;
}

function voyNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  if (value) {
    $(td).addClass("htMiddle").html(value);
  }
  return td;
}

function loadingPortRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  if (value) {
    $(td).addClass("htMiddle").html(value);
  }
  return td;
}

function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).css("background-color", "rgb(232, 232, 232)");
  if (value) {
    $(td).addClass("htMiddle").html(value);
  }
  return td;
}

var config = {
  stretchH: "all",
  minRows: shipmentDetails.length,
  maxRows: shipmentDetails.length,
  width: "100%",
  minSpareRows: 0,
  rowHeights: 30,
  fixedColumnsLeft: 0,
  manualColumnResize: true,
  manualRowResize: true,
  renderAllRows: true,
  rowHeaders: true,
  className: "htMiddle",
  colHeaders: function (col) {
    switch (col) {
      case 0:
        return 'Container No.';
      case 1:
        return 'Kích Thước';
      case 2:
        return "Trọng Lượng";
      case 3:
        return 'FE';
      case 4:
        return 'Seal No.';
      case 5:
        return 'Chủ Hàng';
      case 6:
        return 'Hạn Lệnh';
      case 7:
        return 'Hãng Tàu';
      case 8:
        return 'Tàu';
      case 9:
        return 'Chuyến';
      case 10:
        return 'Cảng Xếp Hàng';
      case 11:
        return 'Cảng Dỡ Hàng';
    }
  },
  colWidths: [120, 100, 130, 50, 100, 150, 130, 120, 120, 100, 150, 150],
  columns: [
    {
      data: "containerNo",
      readOnly: true,
      renderer: containerNoRenderer
    },
    {
      data: "sztp",
      readOnly: true,
      renderer: sztpRenderer
    },
    {
      data: "wgt",
      readOnly: true,
      renderer: wgtRenderer
    },
    {
      data: "fe",
      readOnly: true,
      renderer: feRenderer
    },
    {
      data: "sealNo",
      readOnly: true,
      renderer: sealNoRenderer
    },
    {
      data: "consignee",
      readOnly: true,
      renderer: consigneeRenderer
    },
    {
      data: "expiredDem",
      type: "date",
      dateFormat: "YYYY-MM-DD",
      defaultDate: new Date(),
      renderer: expiredDemRenderer
    },
    {
      data: "opeCode",
      readOnly: true,
      renderer: opeCodeNoRenderer
    },
    {
      data: "vslNm",
      readOnly: true,
      renderer: vslNmNoRenderer
    },
    {
      data: "voyNo",
      readOnly: true,
      renderer: voyNoRenderer
    },
    {
      data: "loadingPort",
      readOnly: true,
      renderer: loadingPortRenderer
    },
    {
      data: "dischargePort",
      readOnly: true,
      renderer: dischargePortRenderer
    }
  ],
};
hot = new Handsontable(dogrid, config);
hot.loadData(shipmentDetails);
hot.render();






// function loadTable() {
//   $("#dg").datagrid({
//     singleSelect: true,
//     height: DOCUMENT_HEIGHT - 170,
//     clientPaging: false,
//     pagination: true,
//     rownumbers: true,
//     pageSize: 50,
//     nowrap: false,
//     striped: true,
//     loadMsg: " Đang xử lý...",
//     loader: function (param, success, error) {
//       success(shipmentDetails);
//     },
//   });
// }

// function formatDate(value) {
//   if (value) {
//     return value.substring(8, 10) + '/' + value.substring(5, 7) + '/' + value.substring(0, 4);
//   }
//   return '';
// }

function confirm() {
  layer.confirm("Xác nhận đã nhận đủ giấy tờ.", {
    icon: 3,
    title: "Xác Nhận",
    btn: ['Đồng Ý', 'Hủy Bỏ']
  }, function () {
    layer.close(layer.index);
    $.modal.loading("Đang xử lý...");
    // CANCEL DOING PROCESS ORDER
    getDataFromTable();
    $.ajax({
      url: PREFIX + "/do",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify(shipmentDetails)
    }).done(function (res) {
      $.modal.closeLoading();
      if (res.code == 0) {
        $.modal.alertSuccess(res.msg);
      } else {
        $.modal.alertError("Lỗi hệ thống, vui lòng liên hệ với admin.");
      }
    });
  }, function () {
    // DO NOTHING
  });
}

function closeForm() {
  $.modal.closeTab();
}

function getDataFromTable(isValidate) {
  let myTableData = hot.getSourceData();
  shipmentDetails = [];
  $.each(myTableData, function (index, object) {
    let shipmentDetail = new Object();
    let expiredDem = new Date(object["expiredDem"].substring(0, 4) + "/" + object["expiredDem"].substring(5, 7) + "/" + object["expiredDem"].substring(8, 10));
    shipmentDetail.expiredDem = expiredDem.getTime();
    shipmentDetail.id = object["id"];
    shipmentDetails.push(shipmentDetail);
  });
}