const PREFIX = ctx + "system/robot";

// Init screen
$(document).ready(function () {
  loadListRobot();
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
        field: 'robotId',
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
        title: "Gửi container có hàng",
        align: "center",
        formatter: function (value, row, index) {
          return isSendContFullOrderFormater(value, row, index);
        },
      },
      {
        field: "isSendContEmptyOrder",
        title: "Gửi container rỗng",
        align: "center",
        formatter: function (value, row, index) {
          return isSendContEmptyOrderFormater(value, row, index);
        },
      },
      {
        title: "Hành động",
        align: "center",
        formatter: function (value, row, index) {
          var actions = [];
          actions.push(
            '<a class="btn btn-success btn-xs" href="javascript:void(0)" onclick="$.operate.edit(\'' +
              row.robotId +
              '\')"><i class="fa fa-edit"  ></i></a> '
          );
          actions.push(
            '<a class="btn btn-danger btn-xs" href="javascript:void(0)" onclick="$.operate.remove(\'' +
              row.robotId +
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
