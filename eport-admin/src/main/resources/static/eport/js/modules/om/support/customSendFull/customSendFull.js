const PREFIX = ctx + "om/support/custom-send-full";
const HIST_PREFIX = ctx + "om/controlling";
const containerCol = 3;
const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var currentHeight = $(document).innerHeight() - 150;
var bill, checkList, allChecked, shipmentDetailIds;
var shipment = new Object();
shipment.serviceType = 4;
var shipmentDetails;
var currentLeftWidth = $(".table-left").width();
var currentRightWidth = $(".table-right").width();
var dogrid = document.getElementById("container-grid"), hot;
var rowAmount = 0;
var shipmentSelected;
var sourceData;

$(document).ready(function () {
  $(".main-body").layout();

  $(".collapse").click(function () {
    $(".main-body__search-wrapper").hide();
    $(".main-body__search-wrapper--container").hide();
    $(this).hide();
    $(".uncollapse").show();
    currentHeight = $(document).innerHeight() - 40;
    $("#dg").datagrid("resize", {
      height: currentHeight,
    });
    setTimeout(() => {
      hot.updateSettings({ height: currentHeight });
      hot.render();
    }, 200);
  });

  $(".uncollapse").click(function () {
    $(".main-body__search-wrapper").show();
    $(".main-body__search-wrapper--container").show();
    $(this).hide();
    $(".collapse").show();
    currentHeight = $(document).innerHeight() - 70;
    $("#dg").datagrid("resize", {
      height: currentHeight,
    });
    setTimeout(() => {
      hot.updateSettings({ height: currentHeight });
      hot.render();
    }, 200);
  });

  $(".left-side__collapse").click(function () {
    $('#main-layout').layout('collapse', 'west');
  });

  $(".right-side__collapse").click(function () {
    $('#right-layout').layout('collapse', 'south');
    setTimeout(() => {
      hot.updateSettings({ height: $('#right-side__main-table').height() - 35 });
      hot.render();
    }, 200);
  });

  $('#right-layout').layout({
    onExpand: function (region) {
      if (region == "south") {
        hot.updateSettings({ height: $('#right-side__main-table').height() - 35 });
        hot.render();
      }
    }
  });

  $('#right-layout').layout('collapse', 'south');
  setTimeout(() => {
    hot.updateSettings({ height: $('#right-side__main-table').height() - 35 });
    hot.render();
  }, 200);

  loadTable(shipment);
  $('#notifyResult').attr("disabled", true);

  $("#blNo").textbox('textbox').bind('keydown', function (e) {
    // enter key
    if (e.keyCode == 13) {
      blNo = $("#blNo").textbox('getText').toUpperCase();
      if (blNo == "") {
        loadTable(shipment);
      }
      shipment.blNo = blNo;
      loadTable(shipment);
    }
  });

});

function handleCollapse(status) {
  if (status) {
    $(".left").css("width", "0.5%");
    $(".left").children().hide();
    $("#btn-collapse").hide();
    $("#btn-uncollapse").show();
    $(".right").css("width", "99%");
    return;
  }
  $(".left").css("width", "25%");
  $(".left").children().show();
  $("#btn-collapse").show();
  $("#btn-uncollapse").hide();
  $(".right").css("width", "74%");
  return;
}

function loadTable(shipment) {
  $("#dg").datagrid({
    url: PREFIX + "/shipments",
    method: "POST",
    singleSelect: true,
    height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
    clientPaging: true,
    collapsible: true,
    pagination: true,
    pageSize: 20,
    rownumbers: true,
    onBeforeSelect: function (index, row) {
      getSelectedRow(index, row);
    },
    nowrap: false,
    striped: true,
    loader: function (param, success, error) {
      var opts = $(this).datagrid("options");
      if (!opts.url) return false;
      $.ajax({
        type: opts.method,
        url: opts.url,
        contentType: "application/json",
        accept: "text/plain",
        dataType: "text",
        data: JSON.stringify({
          pageNum: param.page,
          pageSize: param.rows,
          orderByColumn: param.sort,
          isAsc: param.order,
          data: shipment,
        }),
        dataType: "json",
        success: function (data) {
          success(data);
          $("#dg").datagrid("selectRow", 0);
          parent.updateReportNumberOm();
        },
        error: function () {
          error.apply(this, arguments);
        },
      });
    },
  });
}
loadTableByContainer();

//FORMAT HANDSONTABLE COLUMN
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
function historyRenderer(instance, td, row, col, prop, value, cellProperties) {
  let historyIcon = '<a id="custom" onclick="openHistoryFormCatos(' + row + ')" class="fa fa-window-restore easyui-tooltip" title="Lịch Sử Catos" aria-hidden="true" style="color: #3498db;"></a>';
  $(td).addClass("htCenter").addClass("htMiddle").html(historyIcon);
}

function historyEportRenderer(instance, td, row, col, prop, value, cellProperties) {
  let historyIcon = '<a id="custom" onclick="openHistoryFormEport(' + row + ')" class="fa fa-history easyui-tooltip" title="Lịch Sử Eport" aria-hidden="true" style="color: #3498db;"></a>';
  $(td).addClass("htCenter").addClass("htMiddle").html(historyIcon);
}
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).attr('id', 'containerNo' + row).addClass("htMiddle");
  $(td).html(value);
  return td;
}
function sztpRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).attr('id', 'sztp' + row).addClass("htMiddle");
  $(td).html(value);
  return td;
}
function customsNoRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).attr('id', 'customsNo' + row).addClass("htMiddle");
  $(td).html(value);
  return td;
}
function customStatusRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).attr('id', 'customStatus' + row).addClass("htMiddle");
  $(td).html(value);
  return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).attr('id', 'consignee' + row).addClass("htMiddle");
  $(td).html(value);
  return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).attr('id', 'vslNm' + row).addClass("htMiddle");
  $(td).html(value);
  return td;
}
function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).attr('id', 'wgt' + row).addClass("htMiddle");
  $(td).html(value);
  return td;
}
function cargoTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).attr('id', 'cargoType' + row).addClass("htMiddle");
  $(td).html(value);
  return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).attr('id', 'dischargePort' + row).addClass("htMiddle");
  $(td).html(value);
  return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
  cellProperties.readOnly = 'true';
  $(td).attr('id', 'remark' + row).addClass("htMiddle");
  $(td).html(value);
  return td;
}
//CONFIGURATE HANDSONTABLE
function configHandson() {
  config = {
    stretchH: "all",
    height: $('#right-side__main-table').height() - 35,
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
          return '<a class="fa fa-window-restore easyui-tooltip" title="Lịch Sử Catos" aria-hidden="true" style="color: #3498db;"></a>';
        case 2:
          return '<a class="fa fa-history easyui-tooltip" title="Lịch Sử Catos" aria-hidden="true" style="color: #3498db;"></a>';
        case 3:
          return "Số Container";
        case 4:
          return "Sztp";
        case 5:
          return "Số Tờ Khai HQ";
        case 6:
          return "T.T.T.Quan";
        case 7:
          return "Chủ hàng";
        case 8:
          return "Tàu - Chuyến";
        case 9:
          return "Trọng lượng";
        case 10:
          return "Loại hàng";
        case 11:
          return "Cảng Dở Hàng";
        case 12:
          return "Ghi Chú";
      }
    },
    colWidths: [23, 21, 21, 100, 50, 100, 100, 200, 250, 100, 100, 100, 150],
    filter: "true",
    columns: [
      {
        data: "active",
        type: "checkbox",
        renderer: checkBoxRenderer
      },
      {
        data: "historyCatos",
        readOnly: true,
        renderer: historyRenderer
      },
      {
        data: "historyEport",
        readOnly: true,
        renderer: historyEportRenderer
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
        data: "customsNo",
        renderer: customsNoRenderer
      },
      {
        data: "customStatus",
        renderer: customStatusRenderer
      },
      {
        data: "consignee",
        renderer: consigneeRenderer
      },
      //vslNm = vslNm + "-" + "voyCarrier"
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
        data: "dischargePort",
        renderer: dischargePortRenderer
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
          if (selected[3] == 12) {
            e.stopImmediatePropagation();
          }
          break
        // Arrow Down
        case 40:
          if (selected[2] == rowAmount - 1) {
            e.stopImmediatePropagation();
          }
          break
        default:
          break;
      }
    },
    beforeCopy: beforeCopy
  };
}
configHandson();

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

function beforeCopy(data, coords) {
  if (coords[0].startCol == containerCol && coords[0].endCol == containerCol) {
    if (data.length > 1) {
      for (let i=0; i<data.length-1; i++) {
        data[i][0] = data[i][0] + ',';
      }
    }
  }
}

function checkAll() {
  if (!allChecked) {
    allChecked = true;
    for (let i = 0; i < checkList.length; i++) {
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
  hot.render();
  allChecked = tempCheck;
  $('.checker').prop('checked', tempCheck);
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
    if (checkList[i] != 1) {
      allChecked = false;
    }
  }
  $('.checker').prop('checked', allChecked);
}

function loadTableByContainer(shipmentId) {
  $.modal.loading("Đang xử lý ...");
  $.ajax({
    url: PREFIX + "/shipmentId/" + shipmentId + "/shipmentDetails",
    method: "GET",
    success: function (data) {
      $.modal.closeLoading();
      if (data.code == 0) {
        checkList = Array(rowAmount).fill(0);
        allChecked = false;
        $('.checker').prop('checked', false);
        for (let i = 0; i < checkList.length; i++) {
          $('#check' + i).prop('checked', false);
        }
        sourceData = data.shipmentDetails;
        if (sourceData) {
          for (let i = 0; i < sourceData.length; i++) {
            sourceData[i].vslNm = sourceData[i].vslNm + " - " + sourceData[i].vslName + " - " + sourceData[i].voyCarrier;
          }
        }
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
function getSelectedRow(index, row) {
  if (row) {
    shipmentSelected = row;
    rowAmount = shipmentSelected.contAmount;
    $('#notifyResult').attr("disabled", false);
    $('#notifyResult').css("background-color", "#1C84C6");
    loadTableByContainer(row.id);
    loadListComment();
  }
}

function formatBlBooking(value, row) {
  if (row.blNo) {
    return row.blNo;
  }
  if (row.bookingNo) {
    return row.bookingNo;
  }
  return "";
}
function formatLogistic(value, row, index) {
  return '<a onclick="logisticInfo(' + row.logisticGroupId + "," + "'" + value + "')\"> " + value + "</a>";
}

function logisticInfo(id, logistics) {
  $.modal.openLogisticInfo("Thông tin: " + logistics, ctx + "om/support/logistics/" + id + "/info", null, 470, function () {
  });
}

function notifyResult() {
  $.modal.open("Xác nhận", PREFIX + "/confirm-result-notification/shipmentId/" + shipmentSelected.id, 430, 330);
}

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
      shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length - 1);
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

function syncCustomStatus() {
  if (getDataSelectedFromTable()) {
    layer.confirm("Xác nhận đồng bộ hải quan?", {
      icon: 3,
      title: "Xác Nhận",
      btn: ['Xác Nhận', 'Hủy Bỏ']
    }, function () {
      $.ajax({
        url: PREFIX + "/sync",
        method: "POST",
        data: {
          shipmentDetailIds: shipmentDetailIds,
          shipmentId: shipmentSelected.id
        },
        success: function (result) {
          if (result.code == 0) {
            $.modal.alertSuccess(result.msg);
            loadTable();
          } else {
            $.modal.alertError(result.msg);
          }
          $.modal.closeLoading();
        },
        error: function (result) {
          $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, xin vui lòng thử lại.");
          $.modal.closeLoading();
        },
      });
      layer.close(layer.index);
    }, function () {
      // do nothing
    });
  }
}

function msgSuccess(msg) {
  $.modal.alertSuccess(msg);
  loadTable(shipment);
}
function msgError(msg) {
  $.modal.alertError(msg);
}


//function formatVessel(value, row) {
//  return row.vslNm + " - " + row.vslName + " - " + row.voyNo;
//}


$("#logistic").combobox({
  onSelect: function (serviceType) {
    if (serviceType.value != 0) {
      shipment.logisticGroupId = serviceType.value;
    } else {
      shipment.logisticGroupId = '';
    }
    loadTable(shipment);
  }
});

function formatUpdateTime(value, row, index) {
  if (!row.customScanTime) {
    return null
  }
  let customScanTime = new Date(row.customScanTime);
  let now = new Date();
  let offset = now.getTime() - customScanTime.getTime();
  let totalMinutes = Math.round(offset / 1000 / 60);
  var toHHMMSS = (secs) => {
    var sec_num = parseInt(secs, 10)
    var hours = Math.floor(sec_num / 3600)
    var minutes = Math.floor(sec_num / 60) % 60
    var seconds = sec_num % 60

    return [hours, minutes]
      .map(v => v < 10 ? "0" + v : v)
      .filter((v, i) => v !== "00" || i > 0)
      .join(":")
  }
  return toHHMMSS(totalMinutes * 60);
}

function clearInput() {
  $("#blNo").textbox('setText', '');
  $('#logistic').combobox('setValue', "0");
  $('#logistic').combobox('setText', "Chọn đơn vị Logistics");
  shipment = new Object();
  loadTable(shipment);
}
function search() {
  shipment.blNo = $("#blNo").textbox('getText').toUpperCase();
  shipment.logisticGroupId = $('#logistic').combobox('getValue') == '0' ? '' : $('#logistic').combobox('getValue');
  loadTable(shipment);
}

function loadListComment(shipmentCommentId) {
  let req = {
    serviceType: 4,
    shipmentId: shipmentSelected.id
  };
  $.ajax({
    url: ctx + "shipment-comment/shipment/list",
    method: "POST",
    contentType: "application/json",
    data: JSON.stringify(req),
    success: function (data) {
      if (data.code == 0) {
        let html = '';
        // set title for panel comment
        let commentTitle = '<span style="color: black">Hỗ Trợ<span>';
        let commentNumber = 0;
        if (data.shipmentComments != null) {
          data.shipmentComments.forEach(function (element, index) {
            let createTime = element.createTime;
            let date = '';
            let time = '';
            if (createTime) {
              date = createTime.substring(8, 10) + "/" + createTime.substring(5, 7) + "/" + createTime.substring(0, 4);
              time = createTime.substring(10, 19);
            }

            let resolvedBackground = '';
            if ((shipmentCommentId && shipmentCommentId == element.id) || !element.resolvedFlg) {
              resolvedBackground = 'style="background-color: #ececec;"';
              commentNumber++;
            }

            html += '<div ' + resolvedBackground + '>';
            // User name comment and date time comment
            html += '<div><i style="font-size: 15px; color: #015198;" class="fa fa-user-circle" aria-hidden="true"></i><span> <a>' + element.userName + ' (' + element.userAlias + ')</a>: <i>' + date + ' at ' + time + '</i></span></div>';
            // Topic comment
            html += '<div><span><strong>Yêu cầu:</strong> ' + element.topic + '</span></div>';
            // Content comment
            html += '<div><span>' + element.content.replaceAll("#{domain}", domain) + '</span></div>';
            html += '</div>';
            html += '<hr>';
          });
        }
        commentTitle += ' <span class="round-notify-count">' + commentNumber + '</span>';
        $('#right-layout').layout('panel', 'expandSouth').panel('setTitle', commentTitle);
        $('#commentList').html(html);
        // $("#comment-div").animate({ scrollTop: $("#comment-div")[0].scrollHeight}, 1000);
      }
    }
  });
}

function addComment() {
  let topic = $('#topic').textbox('getText');
  let content = $('.summernote').summernote('code');// get editor content
  let errorFlg = false;
  if (!topic) {
    errorFlg = true;
    $.modal.alertWarning('Vui lòng nhập chủ đề.');
  } else if (!content) {
    errorFlg = true;
    $.modal.alertWarning('Vui lòng nhập nội dung.');
  }
  if (!errorFlg) {
    let req = {
      topic: topic,
      content: content,
      shipmentId: shipmentSelected.id,
      logisticGroupId: shipmentSelected.logisticGroupId
    };
    $.ajax({
      url: PREFIX + "/shipment/comment",
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
          $('#topic').textbox('setText', '');
          $('.summernote').summernote('code', '');
        } else {
          $.modal.msgError("Gửi thất bại.");
        }
      },
      error: function (error) {
        $.modal.closeLoading();
        $.modal.msgError("Gửi thất bại.");
      }
    });
  }
}

function openHistoryFormCatos(row) {
  let containerInfo = sourceData[row];
  let vslCd = '';
  if (containerInfo.vslNm) {
    vslCd = containerInfo.vslNm.split(" ")[0];
  }
  let voyNo = containerInfo.voyNo;
  let containerNo = containerInfo.containerNo;
  if (containerInfo == null || !containerNo || !vslCd || !voyNo) {
    $.modal.alertWarning("Container chưa được khai báo.");
  } else {
    layer.open({
      type: 2,
      area: [1002 + 'px', 500 + 'px'],
      fix: true,
      maxmin: true,
      shade: 0.3,
      title: 'Lịch Sử Container ' + containerNo + ' Catos',
      content: HIST_PREFIX + "/container/history/" + voyNo + "/" + vslCd + "/" + containerNo,
      btn: ["Đóng"],
      shadeClose: false,
      yes: function (index, layero) {
        layer.close(index);
      }
    });
  }
}

function openHistoryFormEport(row) {
  let containerInfo = sourceData[row];
  if (containerInfo == null || !containerInfo.id) {
    $.modal.alertWarning("Container chưa được khai báo.");
  } else {
    layer.open({
      type: 2,
      area: [967 + 'px', 500 + 'px'],
      fix: true,
      maxmin: true,
      shade: 0.3,
      title: 'Lịch Sử Container ' + (containerInfo.containerNo != null ? containerInfo.containerNo : '') + ' Eport',
      content: HIST_PREFIX + "/container/history/" + containerInfo.id,
      btn: ["Đóng"],
      shadeClose: false,
      yes: function (index, layero) {
        layer.close(index);
      }
    });
  }
}


