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
    height: $(document).height() - 105,
    pageSize: 25,
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
        field: "uuid",
        title: "UUID",
        sortable: true,
        formatter: function (value, row, index) {
          return uuidFormatter(value, row, index);
        }
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
        sortable: true,
        formatter: function (value, row, index) {
          return statusFormatter(value, row, index);
        },
      },
      {
        field: 'disabled',
        title: 'Disabled',
        align: 'center',
        sortable: true,
        formatter: function (value, row, index) {
          return disabledFormatter(value, row, index);
        }
      },
      {
        field: "robotService",
        title: "Chức năng",
        align: "left",
        formatter: function (value, row, index) {
          return robotServiceFormater(value, row, index);
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
            '<a class="btn btn-primary btn-xs" href="javascript:void(0)" onclick="openHistory(\'' +
            row.id +
            '\')"><i class="fa fa-history"  ></i></a> '
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

function uuidFormatter(value, row, index) {
  return row.uuId;
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
    return '<i class=\"fa fa-toggle-on text-info fa-2x\" style="color: #ed5565;" onclick="enable(\'' + row.id + '\',\'' + row.uuId + '\')"></i> ';
  }
}

/* formatter for ReceiveContFullOrder column */
function robotServiceFormater(value, row, index) {
  let content = '';
  if (row.isReceiveContFullOrder) {
    content += '<span class="badge badge-primary">Nhận container có hàng</span><span> </span>';
  }
  if (row.isReceiveContEmptyOrder) {
    content += '<span class="badge badge-primary">Nhận container rỗng</span><span> </span>';
  }
  if (row.isSendContFullOrder) {
    content += '<span class="badge badge-primary">Giao container có hàng</span><span> </span>';
  }
  if (row.isSendContEmptyOrder) {
    content += '<span class="badge badge-primary">Giao container rỗng</span><span> </span>';
  }
  if (row.isShiftingContOrder) {
    content += '<span class="badge badge-primary">Dịch chuyển container</span><span> </span>';
  }
  if (row.isChangeVesselOrder) {
    content += '<span class="badge badge-primary">Đổi tàu/chuyến</span><span> </span>';
  }
  if (row.isExtensionDateOrder) {
    content += '<span class="badge badge-primary">Gia hạn lệnh</span><span> </span>';
  }
  if (row.isCreateBookingOrder) {
    content += '<span class="badge badge-primary">Tạo booking</span><span> </span>';
  }
  if (row.isGateInOrder) {
    content += '<span class="badge badge-primary">Gate in</span><span> </span>';
  }
  if (row.isChangeTerminalCustomHold) {
    content += '<span class="badge badge-primary">Terminal/Custom Hold</span><span> </span>';
  }
  if (row.isCancelSendContFullOrder) {
    content += '<span class="badge badge-primary">Hủy lệnh hạ hàng</span><span> </span>';
  }
  if (row.isCancelReceiveContEmptyOrder) {
    content += '<span class="badge badge-primary">Hủy Lệnh bốc rỗng</span><span> </span>';
  }
  if (row.isExportReceipt) {
    content += '<span class="badge badge-primary">Xuất hóa đơn</span><span> </span>';
  }
  if (row.isExtensionDetOrder) {
    content += '<span class="badge badge-primary">Gia hạn detention</span><span> </span>';
  }
  if (row.isOverSizeRemarkOrder) {
    content += '<span class="badge badge-primary">Remark cont quá khổ</span><span> </span>';
  }
  return content;
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

function openHistory(robotId) {
  $.modal.alertWarning('Coming soon...');
}
