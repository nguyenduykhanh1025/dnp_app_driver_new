var prefix = ctx + "logistic/vessel-changing";
var interval, currentPercent, timeout;
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected, shipmentDetails, shipmentDetailIds, sourceData;
var conts = '';
var allChecked = false;
var checkList = [];
var rowAmount = 0;
var shipmentSearch = new Object;

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
    //DEFAULT SEARCH FOLLOW DATE

    loadTable();
    $(".left-side").css("height", $(document).height());
    $("#btn-collapse").click(function () {
        handleCollapse(true);
    });
    $("#btn-uncollapse").click(function () {
        handleCollapse(false);
    });
});

document.getElementById("bookingSearch").addEventListener("keyup", function (event) {
    event.preventDefault();
    if (event.keyCode === 13) {
    shipmentSearch.bookingNo = $('#bookingSearch').val().toUpperCase();
    loadTable();
    }
});
function handleCollapse(status) {
    if (status) {
        $(".left-side").css("width", "0.5%");
        $(".left-side").children().hide();
        $("#btn-collapse").hide();
        $("#btn-uncollapse").show();
        $(".right-side").css("width", "99%");
        setTimeout(function () {
            hot.render();
        }, 500);
        return;
    }
    $(".left-side").css("width", "33%");
    $(".left-side").children().show();
    $("#btn-collapse").show();
    $("#btn-uncollapse").hide();
    $(".right-side").css("width", "67%");
    setTimeout(function () {
        hot.render();
    }, 500);
}

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
        $("#bookingNo").text(row.bookingNo);
        loadShipmentDetail(row.id);
    }
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
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle").addClass("htCenter");
    $(td).html(value);
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
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle");
    $(td).html(value);
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}

function temperatureRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle").addClass("htRight");
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
function cargoTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).addClass("htMiddle").addClass("htCenter");
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
                    var txt = "<input type='checkbox' class='checker' ";
                    txt += "onclick='checkAll()' ";
                    txt += ">";
                    return txt;
                case 1:
                    return 'Container No';
                case 2:
                    return 'Kích Thước';
                case 3:
                    return 'Chủ Hàng';
                case 4:
                    return 'Hãng Tàu';
                case 5:
                    return 'Tàu và Chuyến';
                case 6:
                    return "Nhiệt Độ";
                case 7:
                    return 'Trọng Lượng';
                case 8:
                    return 'Loại Hàng';
                case 9:
                    return 'Cảng Dỡ Hàng';
                case 10:
                    return "Ghi Chú";
            }
        },
        colWidths: [50, 100, 200, 200, 150, 220, 100, 100, 150, 150, 200],
        filter: "true",
        columns: [
            {
                data: "active",
                type: "checkbox",
                className: "htCenter",
                renderer: checkBoxRenderer
            },
            {
                data: "containerNo",
                renderer: containerNoRenderer,
            },
            {
                data: "sztp",
                renderer: sizeRenderer
            },
            {
                data: "consignee",
                renderer: consigneeRenderer
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
                data: "temperature",
                renderer: temperatureRenderer
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
            },
        ]
    };
}
configHandson();

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

// TRIGGER CHECK ALL SHIPMENT DETAIL
function checkAll() {
    if (!allChecked) {
        allChecked = true
        checkList = Array(rowAmount).fill(0);
        for (let i=0; i<checkList.length; i++) {
            if (hot.getDataAtCell(i, 1) == null) {
                break;
            }
            checkList[i] = 1;
            $('#check'+i).prop('checked', true);
        }
    } else {
        allChecked = false;
        checkList = Array(rowAmount).fill(0);
        for (let i=0; i<checkList.length; i++) {
            $('#check'+i).prop('checked', false);
        }
    }
    let tempCheck = allChecked;
    hot.render();
    allChecked = tempCheck;
    $('.checker').prop('checked', allChecked);
}
function check(id) {
    if (sourceData[id].id != null) {
        if (checkList[id] == 0) {
            $('#check'+id).prop('checked', true);
            checkList[id] = 1;
        } else {
            $('#check'+id).prop('checked', false);
            checkList[id] = 0;
        }
        hot.render();
    }
}


// LOAD SHIPMENT DETAIL LIST
function loadShipmentDetail(id) {
	$.modal.loading("Đang xử lý ...");
    $.ajax({
        url: prefix + "/shipment/" + id + "/shipment-detail",
        method: "GET",
        success: function (data) {
        	$.modal.closeLoading();
            if (data.code == 0) {
                sourceData = data.shipmentDetails;
                rowAmount = sourceData.length;
                checkList = Array(rowAmount).fill(0);
                allChecked = false;
                hot.destroy();
                configHandson();
                hot = new Handsontable(dogrid, config);
                hot.loadData(sourceData);
                hot.render();
                if (rowAmount == null || rowAmount == 0) {
                    $.modal.alertWarning("Hiện tại không có container nào để đổi tàu.")
                }
            }
        },
        error: function (data) {
            $.modal.closeLoading();
        }
    });
}

function reloadShipmentDetail() {
    checkList = Array(rowAmount).fill(0);
    allChecked = false;
    $('.checker').prop('checked', false);
    for (let i=0; i<checkList.length; i++) {
        $('#check'+i).prop('checked', false);
    }
    loadShipmentDetail(shipmentSelected.id);
}

// GET CHECKED SHIPMENT DETAIL LIST
function getDataSelectedFromTable() {
    let myTableData = hot.getSourceData();
    let errorFlg = false;
    let cleanedGridData = [];
    for (let i=0; i<checkList.length; i++) {
        if (checkList[i] == 1 && Object.keys(myTableData[i]).length > 0) {
            cleanedGridData.push(myTableData[i]);
        }
    }

    shipmentDetailIds = '';
    $.each(cleanedGridData, function (index, object) {
        shipmentDetailIds += object["id"] + ",";
    });

    // Get result in "selectedList" letiable
    if (shipmentDetailIds.length == 0) {
        $.modal.alert("Bạn chưa chọn container.");
        errorFlg = true;
    } else {
        shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length - 1);
    }
    if (errorFlg) {
        return false;
    }
}

function finishForm(result) {
    if (result.code == 0) {
        $.modal.alertSuccess(result.msg);
    } else {
        $.modal.alertError(result.msg);
    }
    reloadShipmentDetail();
}

function changeVessel() {
    if (getDataSelectedFromTable()) {
        $.modal.openCustomForm("Đổi tàu", prefix + "/shipment-detail-ids/" + shipmentDetailIds + "/vessel-changing", 400, 400);
    }
}



