const PREFIX = ctx + "system/robot";

// Init screen
$(document).ready(function () {
  loadListRobot();

  $("input").keyup(function (event) {
    if (event.keyCode == 13) {
      $.table.search();
      event.preventDefault();
    }
  });

});

function loadListRobot() {
  const OPTIONS = {
    url: PREFIX + "/list",
    createUrl: PREFIX + "/add",
    updateUrl: PREFIX + "/edit/{id}",
    removeUrl: PREFIX + "/remove",
    modalName: "Robot",
    columns: [
      {
        checkbox: true,
      },
      {
        field: 'id',
        title: "Id",
        visible: false,
      },
      {
        field: "uuId",
        title: "UUID",
      },
      {
        field: "ipAddress",
        title: "Địa chỉ IP",
        sortable: true,
      },
      {
        field: "status",
        title: "Trạng thái",
        align: "left",
        formatter: function (value, row, index) {
          return statusFormatter(value, row, index);
        },
      },
      {
        title: 'Disabled',
        align: 'center',
        formatter: function (value, row, index) {
          return disabledFormatter(value, row, index);
        }
      },
      {
        field: "isReceiveContFullOrder",
        title: "Nhận container có hàng",
        align: "center",
        formatter: function (value, row, index) {
          return isReceiveContFullOrderFormater(value, row, index);
        },
      },
      {
        field: "isReceiveContEmptyOrder",
        title: "Nhận container rỗng",
        align: "center",
        formatter: function (value, row, index) {
          return isReceiveContEmptyOrderFormater(value, row, index);
        },
      },
      {
        field: "isSendContFullOrder",
        title: "Giao container có hàng",
        align: "center",
        formatter: function (value, row, index) {
          return isSendContFullOrderFormater(value, row, index);
        },
      },
      {
        field: "isSendContEmptyOrder",
        title: "Giao container rỗng",
        align: "center",
        formatter: function (value, row, index) {
          return isSendContEmptyOrderFormater(value, row, index);
        },
      },
      {
        field: "isShiftingContOrder",
        title: "Dịch chuyển container",
        align: "center",
        formatter: function (value, row, index) {
          return isShiftingContOrderFormater(value, row, index);
        },
      },
      {
        field: "isChangeVesselOrder",
        title: "Đổi tàu/chuyến",
        align: "center",
        formatter: function (value, row, index) {
          return isChangeVesselOrderFormater(value, row, index);
        },
      },
      {
        field: "isExtensionDateOrder",
        title: "Gia hạn lệnh",
        align: "center",
        formatter: function (value, row, index) {
          return isExtensionDateOrderFormater(value, row, index);
        },
      },
      {
        field: "isCreateBookingOrder",
        title: "Tạo Booking",
        align: "center",
        formatter: function (value, row, index) {
          return isCreateBookingOrderFormater(value, row, index);
        },
      },
      {
        field: "isGateInOrder",
        title: "Gate in",
        align: "center",
        formatter: function (value, row, index) {
          return isGateInFormater(value, row, index);
        },
      },
      {
        field: "isChangeTerminalCustomHold",
        title: "Terminal/Custom Hold",
        align: "center",
        formatter: function (value, row, index) {
          return isChangeTerminalCustomHoldFormater(value, row, index);
        },
      },
      {
        field: "isCancelSendContFullOrder",
        title: "Hủy hạ hàng",
        align: "center",
        formatter: function (value, row, index) {
          return isCancelSendContFullOrderFormater(value, row, index);
        },
      },
      {
        field: "isCancelReceiveContEmptyOrder",
        title: "Hủy bốc rỗng",
        align: "center",
        formatter: function (value, row, index) {
          return isCancelReceiveContEmptyOrderFormater(value, row, index);
        },
      },
      {
        field: "isExportReceipt",
        title: "Xuất hóa đơn",
        align: "center",
        formatter: function (value, row, index) {
          return isExportReceiptFormater(value, row, index);
        },
      },
      {
        title: "Hành động",
        align: "center",
        formatter: function (value, row, index) {
          var actions = [];
          actions.push(
            '<a class="btn btn-success btn-xs" href="javascript:void(0)" onclick="$.operate.edit(\'' +
            row.id +
            '\')"><i class="fa fa-edit"  ></i></a> '
          );
          actions.push(
            '<a class="btn btn-danger btn-xs" href="javascript:void(0)" onclick="$.operate.remove(\'' +
            row.id +
            '\')"><i class="fa fa-remove"></i></a> '
          );
          return actions.join("");
        },
      },
    ],
  };
  $.table.init(OPTIONS);
}

/* formatter for status column */
function statusFormatter(value, row, index) {
  if (row.status == 0) {
    return '<span class="text-info"><i class="fa fa-circle"></i> Available</span>';
  } else if (row.status == 1) {
    return '<span class="text-warning"><i class="fa fa-circle"></i> Busy</span>';
  } else {
    return '<span class="text-danger"><i class="fa fa-circle"></i> Offline</span>';
  }
}

/* formatter for disabled status */
function disabledFormatter(value, row, index) {
  if (row.disabled == 0) {
    return '<i class=\"fa fa-toggle-off text-info fa-2x\" onclick="disable(\'' + row.id + '\',\'' + row.uuId + '\')"></i> ';
  } else {
    return '<i class=\"fa fa-toggle-on text-info fa-2x\" onclick="enable(\'' + row.id + '\',\'' + row.uuId + '\')"></i> ';
  }
}

/* formatter for ReceiveContFullOrder column */
function isReceiveContFullOrderFormater(value, row, index) {
  if (row.isReceiveContFullOrder == true) {
    return '<span class="badge badge-primary">Yes</span>';
  } else {
    return '<span class="badge badge-danger">No</span>';
  }
}

/* formatter for ReceiveContEmptyOrder column */
function isReceiveContEmptyOrderFormater(value, row, index) {
  if (row.isReceiveContEmptyOrder == true) {
    return '<span class="badge badge-primary">Yes</span>';
  } else {
    return '<span class="badge badge-danger">No</span>';
  }
}

/* formatter for SendContFullOrder column */
function isSendContFullOrderFormater(value, row, index) {
  if (row.isSendContFullOrder == true) {
    return '<span class="badge badge-primary">Yes</span>';
  } else {
    return '<span class="badge badge-danger">No</span>';
  }
}

/* formatter for SendContEmptyOrder column */
function isSendContEmptyOrderFormater(value, row, index) {
  if (row.isSendContEmptyOrder == true) {
    return '<span class="badge badge-primary">Yes</span>';
  } else {
    return '<span class="badge badge-danger">No</span>';
  }
}

/* formatter for shiftingContOrder column */
function isShiftingContOrderFormater(value, row, index) {
  if (row.isShiftingContOrder == true) {
    return '<span class="badge badge-primary">Yes</span>';
  } else {
    return '<span class="badge badge-danger">No</span>';
  }
}

/* formatter for shiftingContOrder column */
function isChangeVesselOrderFormater(value, row, index) {
  if (row.isChangeVesselOrder == true) {
    return '<span class="badge badge-primary">Yes</span>';
  } else {
    return '<span class="badge badge-danger">No</span>';
  }
}

/* formatter for isExtensionDateOrder column */
function isExtensionDateOrderFormater(value, row, index) {
  if (row.isExtensionDateOrder == true) {
    return '<span class="badge badge-primary">Yes</span>';
  } else {
    return '<span class="badge badge-danger">No</span>';
  }
}

/* formatter for shiftingContOrder column */
function isCreateBookingOrderFormater(value, row, index) {
  if (row.isCreateBookingOrder == true) {
    return '<span class="badge badge-primary">Yes</span>';
  } else {
    return '<span class="badge badge-danger">No</span>';
  }
}

/* formatter for Gate In column */
function isGateInFormater(value, row, index) {
  if (row.isGateInOrder == true) {
    return '<span class="badge badge-primary">Yes</span>';
  } else {
    return '<span class="badge badge-danger">No</span>';
  }
}

/* formatter for change terminal custom hold column */
function isChangeTerminalCustomHoldFormater(value, row, index) {
  if (row.isChangeTerminalCustomHold == true) {
    return '<span class="badge badge-primary">Yes</span>';
  } else {
    return '<span class="badge badge-danger">No</span>';
  }
}

/* formatter for send cont full order column */
function isCancelSendContFullOrderFormater(value, row, index) {
  if (row.isCancelSendContFullOrder == true) {
    return '<span class="badge badge-primary">Yes</span>';
  } else {
    return '<span class="badge badge-danger">No</span>';
  }
}

/* formatter for Cancel receive cont empty order column */
function isCancelReceiveContEmptyOrderFormater(value, row, index) {
  if (row.isCancelReceiveContEmptyOrder == true) {
    return '<span class="badge badge-primary">Yes</span>';
  } else {
    return '<span class="badge badge-danger">No</span>';
  }
}

/* formatter for export receipt column */
function isExportReceiptFormater(value, row, index) {
  if (row.isExportReceipt == true) {
    return '<span class="badge badge-primary">Yes</span>';
  } else {
    return '<span class="badge badge-danger">No</span>';
  }
}

/* disable robot */
function disable(id, uuid) {
  $.modal.confirm("Xác nhận vô hiệu hóa robot " + uuid + "？", function () {
    $.operate.post(PREFIX + "/disabled/change", { "id": id, "disabled": 1 });
  })
}

/* enable robot */
function enable(id, uuid) {
  $.modal.confirm("Xác nhận kích hoạt lại robot " + uuid + "？", function () {
    $.operate.post(PREFIX + "/disabled/change", { "id": id, "disabled": 0 });
  })
}
