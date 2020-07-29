"use strict";
const PREFIX = ctx + "om/order/support";
const DOCUMENT_HEIGHT = $(document).height();
var notification = new Object();

$(document).ready(function () {
  if (shipmentDetails != null && shipmentDetails.length > 0) {
    if ('Y' == shipmentDetails[0].doStatus) {
      $("#toggle-status").prop('checked', true).change();
    }
  }

  loadTable();

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

function loadTable() {
  $("#dg").datagrid({
    singleSelect: true,
    height: DOCUMENT_HEIGHT - 170,
    clientPaging: false,
    pagination: true,
    rownumbers: true,
    pageSize: 50,
    nowrap: false,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      success(shipmentDetails);
    },
  });
}

function formatDate(value) {
  if (value) {
    return value.substring(8, 10) + '/' + value.substring(5, 7) + '/' + value.substring(0, 4);
  }
  return '';
}

function confirm() {
  if ($("#toggle-status").prop('checked')) {
    layer.confirm("Xác nhận đã nhận DO gốc.", {
      icon: 3,
      title: "Xác Nhận",
      btn: ['Đồng Ý', 'Hủy Bỏ']
    }, function () {

      // CANCEL DOING PROCESS ORDER
      $.ajax({
        url: PREFIX + "/do",
        method: "POST",
        data: {
          shipmentId: shipment.id
        }
      }).done(function (res) {
        if (res.code == 0) {
          parent.finishForm(res);
          layer.close(layer.index);
          $.modal.close();
        }
      });
    }, function () {
      // DO NOTHING
    });
  } else {
    $.modal.alertError("Bạn chưa đánh dấu đã nhận DO gốc.");
  }
}

$("#toggle-status").change(function(e) {
  console.log($(this).prop('checked'));
});
