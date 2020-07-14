"use strict";
const PREFIX = ctx + "om/executeCatos";
var dogrid = document.getElementById("container-grid"), hot;
var toggleTrigger = true, countEvent = 2, processOrder;
var check = false;

$(document).ready(function () {
  $("#toggle-status").bootstrapToggle({
    width: '160px',
  });

  if (orderList != null && orderList.length > 0) {
    processOrder = orderList[0];
    switch (processOrder.serviceType) {
      case 1:
        $('#serviceType').text("Bốc Hàng");
        break;
      case 2:
        $('#serviceType').text("Hạ Rỗng");
        break;
      case 3:
        $('#serviceType').text("Bốc Rỗng");
        break;
      case 4:
        $('#serviceType').text("Hạ Hàng");
        break;
    }
    $('#consignee').text(processOrder.consignee);
    $('#taxCode').text(processOrder.taxCode);
    $('#blNo').text(processOrder.blNo);
    $('#bookingNo').text(processOrder.bookingNo);
    $('#payType').text(processOrder.payType);
    let pickupDate = processOrder.pickupDate;
    if (pickupDate) {
      $('#pickupDate').text(pickupDate.substring(8, 10)+'/'+pickupDate.substring(5, 7)+'/'+pickupDate.substring(0, 4));
    }
    $('#vessel').text(processOrder.vessel);
    $('#voyage').text(processOrder.voyage);

    if (processOrder.status != 0) {
      $("#toggle-status").bootstrapToggle({
        on:"<i class='fa fa-cog fa-spin fa-fw'></i>&nbsp;Đang làm lệnh",
        off:"Không",
        onstyle:"success",
        offstyle:"default"
      });
      $("#toggle-status").bootstrapToggle('on');
      $("#toggle-status").prop('disabled', true);
      $('#invoiceNo').val(processOrder.referenceNo).prop('readonly', true);
    } else {
      $("#toggle-status").bootstrapToggle({
        on:"<i class='fa fa-cog fa-spin fa-fw'></i>&nbsp;Đang làm lệnh",
        off:"Không",
        onstyle:"success",
        offstyle:"default"
      });
      $("#toggle-status").bootstrapToggle('off');
    }
  }
});

function statusRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).addClass("htMiddle");
  switch (value) {
    case 0:
      $(td).html('Đang chờ');
      break;
    case 1:
      $(td).html('Đang làm');
      break;
    case 2:
      $(td).html('Đã làm');
      break;
  }
  return td;
}

function idRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  if (orderList[row].id) {
    $(td).addClass("htMiddle").html(orderList[row].id);
  }
  return td;
}

function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  if (orderList[row].shipmentDetail.containerNo) {
    $(td).addClass("htMiddle").html(orderList[row].shipmentDetail.containerNo);
  }
  return td;
}

function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  if (orderList[row].shipmentDetail.opeCode) {
    $(td).addClass("htMiddle").html(orderList[row].shipmentDetail.opeCode);
  }
  return td;
}

function sztpRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  if (orderList[row].shipmentDetail.sztp) {
    $(td).addClass("htMiddle").html(orderList[row].shipmentDetail.sztp);
  }
  return td;
}

function feRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  if (orderList[row].shipmentDetail.fe) {
    $(td).addClass("htMiddle").html(orderList[row].shipmentDetail.fe);
  }
  return td;
}

function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  if (orderList[row].shipmentDetail.wgt) {
    $(td).addClass("htMiddle").html(orderList[row].shipmentDetail.wgt);
  }
  return td;
}

function cargoTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  if (orderList[row].shipmentDetail.cargoType) {
    $(td).addClass("htMiddle").html(orderList[row].shipmentDetail.cargoType);
  }
  return td;
}

function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  if (orderList[row].shipmentDetail.dischargePort) {
    $(td).addClass("htMiddle").html(orderList[row].shipmentDetail.dischargePort);
  }
  return td;
}

var config = {
  stretchH: "all",
  minRows: orderList.length,
  maxRows: orderList.length,
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
        return 'Trạng Thái';
      case 1:
        return 'Mã Lệnh';
      case 2:
        return "Container No.";
      case 3:
        return 'Hãng Tàu';
      case 4:
        return 'Kích Thước';
      case 5:
        return 'FE';
      case 6:
        return 'Trọng Lượng';
      case 7:
        return 'Loại Hàng';
      case 8:
        return 'Cảng Dỡ Hàng';
    }
  },
  colWidths: [100, 90, 120, 100, 100, 50, 110, 100, 120],
  columns: [
    {
      data: "status",
      readOnly: true,
      renderer: statusRenderer
    },
    {
      data: "id",
      readOnly: true,
      renderer: idRenderer
    },
    {
      data: "containerNo",
      readOnly: true,
      renderer: containerNoRenderer
    },
    {
      data: "opeCode",
      readOnly: true,
      renderer: opeCodeRenderer
    },
    {
      data: "sztp",
      readOnly: true,
      renderer: sztpRenderer
    },
    {
      data: "fe",
      readOnly: true,
      renderer: feRenderer
    },
    {
      data: "wgt",
      readOnly: true,
      renderer: wgtRenderer
    },
    {
      data: "cargoType",
      readOnly: true,
      renderer: cargoTypeRenderer
    },
    {
      data: "dischargePort",
      readOnly: true,
      renderer: dischargePortRenderer
    },
  ],
};
hot = new Handsontable(dogrid, config);
hot.loadData(orderList);
hot.render();
$("section.content").css("overflow", "auto");

$("#toggle-status").change(function (e) {
  if (!check) {
    check = true;
    return;
  }
  if (toggleTrigger && countEvent == 2) {
    if ($("#toggle-status").prop('checked')) {

      // HANDLE WHEN TOGGLE TURN ON
      layer.confirm("Xác nhận làm lệnh này.", {
        icon: 3,
        title: "Xác Nhận",
        btn: ['Đồng Ý', 'Hủy Bỏ']
      }, function () {

        // UPDATE PROCESS ORDER TO DOING STATUS
        $.ajax({
          url: PREFIX + "/process-order/doing",
          method: "Get",
          data: {
            processOrderId: processOrder.id
          }
        }).done(function (res) {
          if (res.code != 0) {
            $("#toggle-status").prop('checked', false).change();
            $.modal.alertError(res.msg);
          }
          layer.close(layer.index);
        });
      }, function () {

        // CANCEL REQUEST MAKE ORDER
        toggleTrigger = false;
        countEvent = 0;
        $("#toggle-status").prop('checked', false).change();
      });
    } else {

      // HANDLE WHEN TOGGLE TURN OFF
      layer.confirm("Hủy làm lệnh này.", {
        icon: 3,
        title: "Xác Nhận",
        btn: ['Đồng Ý', 'Hủy Bỏ']
      }, function () {

        // CANCEL DOING PROCESS ORDER
        $.ajax({
          url: PREFIX + "/process-order/canceling",
          method: "Get",
          data: {
            processOrderId: processOrder.id
          }
        }).done(function (res) {
          if (res.code != 0) {
            $("#toggle-status").prop('checked', true).change();
            $.modal.alertError(res.msg);
          }
          layer.close(layer.index);
        });
      }, function () {

        // CANCEL ACTION 
        toggleTrigger = false;
        countEvent = 0;
        $("#toggle-status").prop('checked', true).change();
      });
    }
  } else {
    toggleTrigger = true;
    countEvent++;
  }
});

function confirm() {
  let error = false;
  if ($('#invoiceNo').val()) {
    processOrder.referenceNo = $('#invoiceNo').val();
  } else {
    error = true;
  }
  if (error) {
    $.modal.alertError("Bạn chưa nhập đủ số invoice no.");
  } else {
    $.modal.loading("Đang xử lý...");
    $.ajax({
      url: PREFIX + "/invoice-no",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify(processOrder),
      success: function (res) {
        if (res.code == 0) {
          parent.finishForm(res);
          $.modal.close();
        }
      },
      error: function () {
        $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
        $.modal.closeLoading();
      },
    });
  }
}