const PREFIX = ctx + "om/order/support";
const DOCUMENT_HEIGHT = $(document).height();

$(document).ready(function () {
  loadTable();

  if (shipments != null && shipments.length > 0) {
    $('.batch-code').text(shipments[0].id);
    switch (shipments[0].serviceType) {
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
    $('.group-name').text(shipments[0].logisticName);
  }
});

function loadTable() {
  $("#dgOrder table").datagrid({
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
      success(pickupHistories);
    },
  });
}

function formatSize(value, row) {
  return row.shipmentDetail.sztp;
}

function formatDriverName(value, row) {
  return row.pickupAssign.fullName;
}

function formatPhoneNumber(value, row) {
  return row.pickupAssign.phoneNumber;
}

function closeForm() {
  $.modal.close();
}