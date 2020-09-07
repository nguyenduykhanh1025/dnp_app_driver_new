const PREFIX = ctx + "om/document";
const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected, checkList, allChecked, sourceData, rowAmount = 0, shipmentDetailIds;
var shipment = new Object();
shipment.params = new Object();

$( document ).ready(function() {
  $(".main-body").layout();

  $(".collapse").click(function () {
    $(".main-body__search-wrapper").height(15);
    $(".main-body__search-wrapper--container").hide();
    $(this).hide();
    $(".uncollapse").show();
  })

  $(".uncollapse").click(function () {
    $(".main-body__search-wrapper").height(SEARCH_HEIGHT);
    $(".main-body__search-wrapper--container").show();
    $(this).hide();
    $(".collapse").show();
  })

  $(".left-side__collapse").click(function () {
    $('#main-layout').layout('collapse', 'west');
  })

  $("#logisticGroups").combobox({
    valueField: 'id',
    textField: 'groupName',
    data: logisticGroups,
    onSelect: function (logisticGroup) {
      if (logisticGroup.id != 0) {
        shipment.logisticGroupId = logisticGroup.id;
      } else {
        shipment.logisticGroupId = null;
      }
      $('#vesselAndVoyages').combobox('select', 'Chọn tàu chuyến');
      $('#doStatus').combobox('select', 'null');
      $("#containerNo").textbox('setText', '');
      loadTable();
    }
  });

  $("#blNo").textbox('textbox').bind('keydown', function(e) {
    // enter key
    if (e.keyCode == 13) {
      shipment.blNo = $("#blNo").textbox('getText').toUpperCase();
      loadTable();
    }
  });

  $("#containerNo").textbox('textbox').bind('keydown', function(e) {
    // enter key
    if (e.keyCode == 13) {
      shipment.params.containerNo = $("#containerNo").textbox('getText').toUpperCase();
      loadTable();
    }
  });

  vesselAndVoyages[0].selected = true;
  $("#vesselAndVoyages").combobox({
    valueField: 'vslAndVoy',
    textField: 'vslAndVoy',
    data: vesselAndVoyages,
    onSelect: function (vesselAndVoyage) {
      if (vesselAndVoyage.vslAndVoy != 'Chọn tàu chuyến') {
        let vslAndVoyArr = vesselAndVoyage.vslAndVoy.split(" - ");
        shipment.params.vslNm = vslAndVoyArr[0];
        shipment.params.voyNo = vslAndVoyArr[2];
      } else {
        shipment.params.vslNm = null;
        shipment.params.voyNo = null;
      }
      loadTable();
    }
  });
  // $('#vesselAndVoyages').combobox('select', 'Chọn tàu chuyến');

  $("#doStatus").combobox({
    valueField: 'alias',
    textField: 'text',
    data: [{
      "alias": 'null',
      "text": "Trạng thái",
      "selected": true
    }, {
      "alias": 'N',
      "text": "Chưa thu"
    }, {
      "alias": 'Y',
      "text": "Đã thu"
    }],
    onSelect: function (doStatus) {
      if (doStatus.alias != 'null') {
        shipment.params.doStatus = doStatus.alias;
      } else {
        shipment.params.doStatus = null;
      }
      loadTable();
    }
  });
  // $('#doStatus').combobox('select', 'null');

  loadTable();
});


function loadTable() {
  $("#dg").datagrid({
    url: PREFIX + '/shipments',
    height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
    method: 'POST',
    singleSelect: true,
    collapsible: true,
    clientPaging: false,
    pagination: true,
    rownumbers: true,
    onClickRow: function () {
      getSelected();
    },
    pageSize: 50,
    nowrap: true,
    striped: true,
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
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
          data: shipment
        }),
        success: function (res) {
          if (res.code == 0) {
            success(res.shipments);
          } else {
            success([]);
          }
          if (res.shipments.length > 0) {
            $("#dg").datagrid('checkRow', 0);
            shipmentSelected = res.shipments[0];
            rowAmount = shipmentSelected.containerAmount;
            shipmentDetail.shipmentId = shipmentSelected.id;
            loadShipmentDetails(shipmentSelected.id);
          }
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}

// FORMATTER
// Format logistic name for clickable show link 
function formatLogistic(value, row, index) {
  return '<a onclick="logisticInfo(' + row.logisticGroupId + "," + "'" + value + "')\"> " + value + "</a>";
}
// FORMAT DATE FOR date time format dd/mm/yyyy
function formatDate(value) {
  let date = new Date(value);
  let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
  let month = date.getMonth() + 1;
  let monthText = month < 10 ? "0" + month : month;
  return day + "/" + monthText + "/" + date.getFullYear();
}

// Trigger when click a row in easy ui data grid on the left screen
function getSelected() {
  let row = $("#dg").datagrid("getSelected");
  if (row) {
    shipmentSelected = row;
    rowAmount = shipmentSelected.containerAmount;
    checkList = Array(rowAmount).fill(0);
    allChecked = false;
  }
  loadShipmentDetails(shipmentSelected.id);
}

// FORMAT HANDSONTABLE COLUMN
function checkBoxRenderer(instance, td, row, col, prop, value, cellProperties) {
  let content = '';
  if (checkList[row] == 1) {
      content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')" checked></div>';
  } else {
      content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')"></div>';
  }
  $(td).attr('id', 'checkbox' + row).addClass("htCenter").addClass("htMiddle").html(content);
  return td;
}
function statusIconsRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).attr('id', 'statusIcon' + row).addClass("htCenter").addClass("htMiddle");
  if (sourceData[row] && sourceData[row].dischargePort && sourceData[row].processStatus && sourceData[row].paymentStatus && sourceData[row].customStatus && sourceData[row].finishStatus) {
    // Customs Status
    let customs = '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chờ Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
    switch (sourceData[row].customStatus) {
      case 'R':
        customs = '<i id="custom" class="fa fa-shield easyui-tooltip" title="Đã Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
      case 'Y':
        customs = '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chưa Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #ed5565;"></i>';
        break;
      case 'N':
        customs = '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chờ Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
        break;
    }
    // Command process status
    let process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Chưa xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';
    switch (sourceData[row].processStatus) {
      case 'E':
        process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ kết quả" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
        break;
    case 'Y':
      process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đã làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
        break;
    case 'N':
      if(value > 1) {
        process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Có thể làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
      }
        break;
    }
    // Payment status
    let payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
    switch (sourceData[row].paymentStatus) {
      case 'E':
        payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Lỗi Thanh Toán" aria-hidden="true" style="margin-left: 8px; color : #ed5565;"></i>';
        break;
    case 'Y':
      payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
    case 'N':
      if(value > 2) {
        payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chờ Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
      }
        break;
    }
    // Do status
    let doStatus = '<i id="do" class="fa fa-file-text easyui-tooltip" title="Chưa Thu Chứng TỪ" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
    if (sourceData[row].doStatus == 'Y') {
      doStatus = '<i id="do" class="fa fa-file-text easyui-tooltip" title="Đã Thu Chứng TỪ" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
    }
    // released status
    let released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Chưa thể nhận container" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
    switch (sourceData[row].finishStatus) {
    case 'Y':
      released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Đã Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
        break;
    case 'N':
      if(sourceData[row].paymentStatus == 'Y') {
        released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Có Thể Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
      }
        break;
    }
    // Return the content
    let content = '<div>';
    // Domestic cont: VN --> not show
    if(sourceData[row].loadingPort.substring(0,2) != 'VN') {
      content += customs;
    }
    content += process + payment + doStatus + released + '</div>';
    $(td).html(content);
  }
  return td;
}
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  return td;
}
function sztpRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  return td;
}
function expiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  if (value != null && value != '') {
    if (value.substring(2, 3) != "/") {
      value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
    }
  } else {
    $(td).html('');
  }
  return td;
  return td;
}
function emptyDepotRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  return td;
}
function detFreeTimeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  if (sourceData[row] && sourceData[row].vslNm && sourceData[row].voyNo) {
    $(td).html('<div style="width: 100%;">' + sourceData[row].vslNm + ' - ' + sourceData[row].voyNo + '</div>');
  }
  return td;
}
function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  return td;
}
function cargoTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  return td;
}
function loadingPortRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  return td;
}
function payTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  return td;
}
function payerRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  return td;
}
function orderNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  return td;
}
function invoiceNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
  $(td).addClass("htMiddle").addClass("htCenter");
  $(td).html('<div style="width: 100%;">' + value + '</div>');
  return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
  config = {
    stretchH: "all",
    height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
    minRows: rowAmount,
    maxRows: rowAmount,
    width: "100%",
    minSpareRows: 0,
    rowHeights: 30,
    fixedColumnsLeft: 4,
    manualColumnResize: true,
    manualRowResize: true,
    renderAllRows: true,
    rowHeaders: true,
    className: "htMiddle",
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
          return "Số Container";
        case 3:
          return "Sztp";
        case 4:
          return "Hạn Lệnh";
        case 5:
          return "Nơi Hạ Vỏ";
        case 6:
          return "Ngày Miễn";
        case 7:
          return "Chủ Hàng";
        case 8:
          return "Tàu - Chuyến";
        case 9:
          return "Trọng Lượng";
        case 10:
          return "Loại Hàng";
        case 11:
          return "Cảng Xếp Hàng";
        case 12:
          return "P.T.T.T";
        case 13:
          return "Payer";
        case 14:
          return "Số Tham Chiếu";
        case 15:
          return "Số Invoice";
        case 16:
          return "Ghi Chú";
      }
    },
    colWidths: [ 50, 120, 100, 60, 100, 100, 100, 150, 100, 100, 100, 100, 100, 100, 100, 100, 100],
    filter: "true",
    columns: [
      {
        data: "active",
        type: "checkbox",
        renderer: checkBoxRenderer
      },
      {
        data: "status",
        readOnly: true,
        renderer: statusIconsRenderer
      },
      {
        data: "containerNo",
        renderer: containerNoRenderer
      },
      {
        data: "sztp",
        renderer: sztpRenderer
      },
      {
        data: "expiredDem",
        type: "date",
        dateFormat: "-YYYY-MM-DD",
        defaultDate: new Date(),
        renderer: expiredDemRenderer
      },
      {
        data: "emptyDepot",
        renderer: emptyDepotRenderer
      },
      {
        data: "detFreeTime",
        renderer: detFreeTimeRenderer
      },
      {
        data: "consignee",
        renderer: consigneeRenderer
      },
      {
        data: "vslNm",
        renderer: vslNmRenderer
      },
      {
        data: "wgt",
        renderer: wgtRenderer
      },
      {
        data: "cargoType",
        renderer: cargoTypeRenderer
      },
      {
        data: "loadingPort",
        renderer: loadingPortRenderer
      },
      {
        data: "payType",
        renderer: payTypeRenderer
      },
      {
        data: "payer",
        renderer: payerRenderer
      },
      {
        data: "orderNo",
        renderer: orderNoRenderer
      },
      {
        data: "invoiceNo",
        renderer: invoiceNoRenderer
      },
      {
        data: "remark",
        renderer: remarkRenderer
      }
    ],
    beforeKeyDown: function (e) {
      let selected = hot.getSelected()[0];
      switch (e.keyCode) {
        // Arrow Left
        case 37:
          if (selected[3] == 0) {
            e.stopImmediatePropagation();
          }
          break;
        // Arrow Up
        case 38:
          if (selected[2] == 0) {
            e.stopImmediatePropagation();
          }
          break;
        // Arrow Right
        case 39:
          if (selected[3] == 17) {
            e.stopImmediatePropagation();
          }
          break
        // Arrow Down
        case 40:
          if (selected[2] == shipmentSelected.containerAmount - 1) {
            e.stopImmediatePropagation();
          }
          break
        default:
          break;
      }
    },
  };
}
configHandson();
hot = new Handsontable(dogrid, config);

function loadShipmentDetails(id) {
  if (id) {
    $.modal.loading("Đang xử lý ...");
    $.ajax({
      url: PREFIX + "/shipment/" + id + "/shipmentDetails",
      method: "GET",
      success: function (res) {
        $.modal.closeLoading();
        if (res.code == 0) {
          checkList = Array(rowAmount).fill(0);
          allChecked = false;
          $('.checker').prop('checked', false);
          for (let i = 0; i < checkList.length; i++) {
            $('#check' + i).prop('checked', false);
          }
          sourceData = res.shipmentDetails;
          hot.destroy();
          configHandson();
          hot = new Handsontable(dogrid, config);
          hot.loadData(sourceData);
          hot.render();
        }
      },
      error: function (data) {
        $.modal.closeLoading();
      }
    });
  }
}

// TRIGGER CHECK ALL SHIPMENT DETAIL
function checkAll() {
  if (!allChecked) {
    allChecked = true
    checkList = Array(rowAmount).fill(0);
    for (let i = 0; i < checkList.length; i++) {
      if (hot.getDataAtCell(i, 1) == null) {
        break;
      }
      checkList[i] = 1;
      $('#check' + i).prop('checked', true);
    }
  } else {
    allChecked = false;
    checkList = Array(rowAmount).fill(0);
    for (let i = 0; i < checkList.length; i++) {
      $('#check' + i).prop('checked', false);
    }
  }
  let tempCheck = allChecked;
  updateLayout();
  hot.render();
  allChecked = tempCheck;
  $('.checker').prop('checked', allChecked);
}
function check(id) {
  if (sourceData[id].id != null) {
    if (checkList[id] == 0) {
      $('#check' + id).prop('checked', true);
      checkList[id] = 1;
    } else {
      $('#check' + id).prop('checked', false);
      checkList[id] = 0;
    }
    hot.render();
    updateLayout();
  }
}
function updateLayout() {
  allChecked = true;
  for (let i = 0; i < checkList.length; i++) {
    if (hot.getDataAtCell(i, 1) != null) {
      if (checkList[i] != 1) {
        allChecked = false;
      } 
    }
  }
  $('.checker').prop('checked', allChecked);
}

// GET CHECKED SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataSelectedFromTable() {
  let myTableData = hot.getSourceData();
  let errorFlg = false;
  if (myTableData && checkList) {
    let cleanedGridData = [];
    for (let i = 0; i < checkList.length; i++) {
      if (Object.keys(myTableData[i]).length > 0) {
        if (checkList[i] == 1) {
          cleanedGridData.push(myTableData[i]);
        }
      }
    }
    shipmentDetailIds = "";
    $.each(cleanedGridData, function (index, object) {
      shipmentDetailIds += object["id"] + ",";
    });
  
    if (shipmentDetailIds.length == 0) {
      $.modal.alertWarning("Bạn chưa chọn container nào.")
      errorFlg = true;
    } else {
      shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length-1);
    }
  } else {
    $.modal.alertWarning("Bạn chưa chọn lô.");
    errorFlg = true;
  }

  if (errorFlg) {
    return false;
  } else {
    return true;
  }
}

function search() {

}

function clearInput() {
  $('#vesselAndVoyages').combobox('select', 'Chọn tàu chuyến');
  $('#doStatus').combobox('select', 'null');
  $('#logisticGroups').combobox('select', '0');
  $("#containerNo").textbox('setText', '');
  $("#blNo").textbox('setText', '');
  shipment = new Object();
  shipment.params = new Object();
  loadTable();
}

function confirmDocument() {
  if (getDataSelectedFromTable()) {
    layer.open({
      type: 2,
      area: [600 + 'px', 300 + 'px'],
      fix: true,
      maxmin: true,
      shade: 0.3,
      title: 'Xác Nhận',
      content: PREFIX + "/confirmation",
      btn: ["Xác Nhận", "Hủy"],
      shadeClose: false,
      yes: function(index, layero) {
        confirm(index, layero);
      },
      cancel: function(index) {
        return true;
      }
    });
  }
}

function confirm(index, layero) {
  let childLayer = layero.find("iframe")[0].contentWindow.document;
  $.modal.loading("Đang xử lý ...");
  $.ajax({
    url: PREFIX + "/confirmation",
    method: "POST",
    data: {
      doStatus: $(childLayer).find('input[name="acceptFlg"]:checked').val(),
      content: $(childLayer).find("#message").val(),
      shipmentDetailIds: shipmentDetailIds 
    },
    success: function (res) {
      layer.close(index);
      $.modal.closeLoading();
      if (res.code == 0) {
        $.modal.alertSuccess(res.msg);
        loadShipmentDetails(shipmentSelected.id);
      } else {
        $.modal.alertError(res.msg);
      }
    },
    error: function (data) {
      layer.close(index);
      $.modal.closeLoading();
    }
  });
}




