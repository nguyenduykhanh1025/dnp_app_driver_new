var prefix = ctx + "logistic/shifting-cont";
var interval, currentPercent, timeout;
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected, currentProcessId, orders, orderNumber = 0;
var rowAmount = 0;
var shipmentSearch = new Object;

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
    //DEFAULT SEARCH FOLLOW DATE

    loadTable();
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
});

document.getElementById("blSearch").addEventListener("keyup", function (event) {
    event.preventDefault();
    if (event.keyCode === 13) {
        shipmentSearch.blNo = $('#blSearch').val().toUpperCase();
        loadTable();
    }
});

// LOAD SHIPMENT LIST
function loadTable(msg) {
    if (msg) {
        $.modal.alertSuccess(msg);
    }
    $("#dg").datagrid({
        url: prefix + "/shipments",
        height: window.innerHeight - 110,
        method: 'post',
        singleSelect: true,
        collapsible: true,
        clientPaging: false,
        pagination: true,
        rownumbers:true,
        onClickRow: function () {
            getSelected();
        },
        pageSize: 50,
        nowrap: true,
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
                  data: shipmentSearch
                }),
                success: function (data) {
                    success(data);
                    $("#dg").datagrid("hideColumn", "id");
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
    let minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
    let seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
    return day + "/" + monthText + "/" + date.getFullYear() + " " + hours + ":" + minutes + ":" + seconds;
}

// FORMAT REMARK FOR SHIPMENT LIST
function formatRemark(value) {
    return '<div class="easyui-tooltip" title="' + (value != null ? value : "Trống") + '" style="width: 80; text-align: center;"><span>' + (value != null ? value : "") + '</span></div>';
}

function handleRefresh() {
    loadTable();
}

// HANDLE WHEN SELECT A SHIPMENT
function getSelected() {
    let row = $("#dg").datagrid("getSelected");
    if (row) {
        shipmentSelected = row;
        $("#loCode").text(row.id);
        $("#taxCode").text(row.taxCode);
        $("#quantity").text(row.containerAmount);
        $("#blNo").text(row.blNo);
        if (row.edoFlg == "0") {
            $("#dotype").text("DO");
        } else {
            $("#dotype").text("eDO");
        }
        loadShipmentDetail(row.id);
    }
}

// FORMAT HANDSONTABLE COLUMN
function containerStatusRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle").addClass("htCenter");
    if (value == "Y") {
        value = '<span class="badge badge-success">Có</span>';
    } else {
        value = '<span class="badge badge-warning">Không</span>';
    }
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle").addClass("htCenter");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function detFreeTimeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle").addClass("htCenter");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function expiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle").addClass("htCenter");
    if (value) {
        $(td).html(value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4));
    }
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function emptyDepotRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function voyNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle").addClass("htCenter");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function sealNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle").addClass("htRight");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function loadingPortRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
    config = {
        stretchH: "all",
        height: document.documentElement.clientHeight - 105,
        minRows: rowAmount,
        maxRows: rowAmount,
        width: "100%",
        minSpareRows: 0,
        rowHeights: 30,
        fixedColumnsLeft: 1,
        manualColumnResize: true,
        manualRowResize: true,
        renderAllRows: true,
        rowHeaders: true,
        className: "htMiddle",
        colHeaders: function (col) {
            switch (col) {
                case 0:
                    return 'Container Chỉ Định';
                case 1:
                    return 'Container No';
                case 2:
                    return 'Hạn Lệnh';
                case 3:
                    return 'Số Ngày Miễn Lưu Vỏ';
                case 4:
                    return 'Chủ Hàng';
                case 5:
                    return 'Nơi Hạ Vỏ';
                case 6:
                    return 'Hãng Tàu';
                case 7:
                    return 'Tàu';
                case 8:
                    return 'Chuyến';
                case 9:
                    return "Kích Thước";
                case 10:
                    return "Seal No";
                case 11:
                    return "Trọng Tải";
                case 12:
                    return 'Cảng Xếp Hàng';
                case 13:
                    return "Cảng Dỡ Hàng";
                case 14:
                    return "Ghi Chú";
            }
        },
        // colWidths: [150, 100, 100, 150, 150, 100, 150, 200, 100, 100, 100, 100, 120, 100, 200],
        filter: "true",
        columns: [
            {
                data: "preorderPickup",
                renderer: containerStatusRenderer,
            },
            {
                data: "containerNo",
                renderer: containerNoRenderer,
            },
            {
                data: "expiredDem",
                renderer: expiredDemRenderer
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
                data: "emptyDepot",
                renderer: emptyDepotRenderer
            },
            {
                data: "opeCode",
                renderer: opeCodeRenderer
            },
            {
                data: "vslNm",
                renderer: vslNmRenderer
            },
            {
                data: "voyNo",
                renderer: voyNoRenderer
            },
            {
                data: "sztp",
                renderer: sizeRenderer
            },
            {
                data: "sealNo",
                renderer: sealNoRenderer
            },
            {
                data: "wgt",
                renderer: wgtRenderer
            },
            {
                data: "loadingPort",
                renderer: loadingPortRenderer
            },
            {
                data: "dischargePort",
                renderer: dischargePortRenderer
            },
            {
                data: "remark",
                renderer: remarkRenderer
            },
        ]
    };
}
configHandson();

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

// LOAD SHIPMENT DETAIL LIST
function loadShipmentDetail(id) {
	$.modal.loading("Đang xử lý ...");
    $.ajax({
        url: prefix + "/shipment/" + id + "/shipment-detail",
        method: "GET",
        success: function (data) {
        	$.modal.closeLoading();
            if (data.code == 0) {
                rowAmount = data.shipmentDetails.length;
                hot.destroy();
                configHandson();
                hot = new Handsontable(dogrid, config);
                hot.loadData(data.shipmentDetails);
                hot.render();
                if (rowAmount == null || rowAmount == 0) {
                    $.modal.alertWarning("Hiện tại không có container nào có thể áp dụng bốc container chỉ định.")
                } else {
                    $('#pickContBtn').prop('disabled', false);
                    $('#paymentCont').prop('disabled', false);
                }
            }
        },
        error: function (data) {
            $.modal.closeLoading();
        }
    });
}

function reloadShipmentDetail() {
    loadShipmentDetail(shipmentSelected.id);
}

function finishForm(result) {
    if (result.code == 0) {
        $.modal.alertSuccess(result.msg);
    } else {
        $.modal.alertError(result.msg);
    }
    reloadShipmentDetail();
}

function pickContForm() {
    $.modal.openCustomForm("Bốc container chỉ định", prefix + "/cont-list/yard-position/" + shipmentSelected.blNo, 710, 565);
}

function otp(shipmentDetailIds, isCredit) {
    $.modal.openCustomForm("Xác thực OTP", prefix + "/otp/shipment-detail-ids/" + shipmentDetailIds + "/credit/" + isCredit, 600, 350);
}

function finishVerifyForm(result) {
    if (result.code == 0 || result.code == 301){
        //$.modal.loading(result.msg);
        orders = result.processIds;
        orderNumber = result.orderNumber;
        // CONNECT WEB SOCKET
        connectToWebsocketServer();
        showProgress("Đang xử lý ...");
        timeout = setTimeout(() => {
            setTimeout(() => {
                hideProgress();
                reloadShipmentDetail();
                $.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");
            }, 1000);
        }, 200000);
    } else {
        reloadShipmentDetail();
        $.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");
    }
}

function connectToWebsocketServer() {
    // Connect to WebSocket Server.
    $.websocket.connect({}, onConnected, onError);
}

function onConnected() {
    for (let i = 0; i < orders.length; i++) {
        $.websocket.subscribe(orders[i] + '/response', onMessageReceived);
    }
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    if (message.code != 0) {

        clearTimeout(timeout);

        setProgressPercent(currentPercent = 100);
        setTimeout(() => {
            hideProgress();

            reloadShipmentDetail();

            $.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");

            // Close loading
            //$.modal.closeLoading();

            // Close websocket connection 
            $.websocket.disconnect(onDisconnected);
        }, 1000);
    } else {
        orderNumber--;
        if (orderNumber == 0) {

            clearTimeout(timeout);

            setProgressPercent(currentPercent = 100);
            setTimeout(() => {
                hideProgress();

                reloadShipmentDetail();

                $.modal.alertSuccess(message.msg);

                // Close loading
                //$.modal.closeLoading();

                // Close websocket connection 
                $.websocket.disconnect(onDisconnected);
            }, 1000);
        }
    }
}

function onDisconnected() {
    console.log('Disconnected socket.');
}

function onError(error) {
    //console.error('Could not connect to WebSocket server. Please refresh this page to try again!');
    setTimeout(() => {
        hideProgress();
        $.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");
    }, 1000);
}

function showProgress(title) {
    $('.progress-wrapper').show();
    $('.dim-bg').show();
    $('#titleProgress').text(title);
    $('.percent-text').text("0%");
    currentPercent = 0;
    interval = setInterval(function () {
        if (currentPercent <=99) {
            setProgressPercent(++currentPercent);
        }
        if (currentPercent >= 99) {
            clearInterval(interval);
        }
    }, 1000);
}

function setProgressPercent(percent) {
    $('#progressBar').prop('aria-valuenow', percent)
    $('#progressBar').css('width', percent + "%")
    $('.percent-text').text(percent + "%");
}

function hideProgress() {
    $('.progress-wrapper').hide();
    $('.dim-bg').hide();
    currentPercent = 0;
    $('.percent-text').text("0%");
    setProgressPercent(0);
}

function napasPaymentFormForShifting() {
    $.modal.openFullWithoutButton("Cổng Thanh Toán", ctx + "logistic/shipment/" + shipmentSelected.id + "/napas");
}

function payment() {
    $.modal.openCustomForm("Thanh toán phí dịch chuyển", prefix + "/shipment/" + shipmentSelected.id + "/payment/shifting", 800, 400);
}


