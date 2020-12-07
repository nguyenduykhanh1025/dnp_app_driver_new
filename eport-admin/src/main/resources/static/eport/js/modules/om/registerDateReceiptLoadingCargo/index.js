const PREFIX = ctx + "om/register-date-receipt-loading-cargo";
const HIST_PREFIX = ctx + "om/controlling";
const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected, checkList, allChecked, sourceData, rowAmount = 0, shipmentDetailIds;
const greenBlackColor = "rgb(104 241 131)";
var shipment = new Object();
shipment.params = new Object();
var contList = [], sztpListDisable = [], currentEta;
const DATE_RECEIPT_STATUS = {
    NO: "N",
    PROGRESS: "P",
    SUCCESS: "S",
    ERROR: "E"
}
$(document).ready(function () {
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

    $("#dateReceiptStatus").combobox({
        panelHeight: 'auto',
        valueField: 'alias',
        editable: false,
        textField: 'text',
        data: [{
            "alias": '',
            "text": "Trạng thái"
        },
        {
            "alias": DATE_RECEIPT_STATUS.PROGRESS,
            "text": "Chưa kiểm tra",
            "selected": true
        },
        {
            "alias": DATE_RECEIPT_STATUS.SUCCESS,
            "text": "Đã kiểm tra"
        },
        {
            "alias": DATE_RECEIPT_STATUS.ERROR,
            "text": "Đã từ chối"
        }],
        onSelect: function (dateReceiptStatus) {
            if (dateReceiptStatus.alias != '') {
                shipment.params.dateReceiptStatus = dateReceiptStatus.alias;
            } else {
                shipment.params.dateReceiptStatus = null;
            }
            loadTable();
        }
    });

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
            $('#opr').combobox('select', 'Chọn OPR');
            $("#containerNo").textbox('setText', '');
            loadTable();
        }
    });

    $("#bookingNo").textbox('textbox').bind('keydown', function (e) {
        // enter key
        if (e.keyCode == 13) {
            shipment.bookingNo = $("#bookingNo").textbox('getText').toUpperCase();
            loadTable();
        }
    });

    $("#containerNo").textbox('textbox').bind('keydown', function (e) {
        // enter key
        if (e.keyCode == 13) {
            shipment.params.containerNo = $("#containerNo").textbox('getText').toUpperCase();
            loadTable();
        }
    });

    $("#opr").combobox({
        panelMaxHeight: 200,
        valueField: 'dictValue',
        textField: 'dictLabel',
        data: oprList,
        onSelect: function (opr) {
            if (opr.dictValue != 'Chọn OPR') {
                shipment.opeCode = opr.dictValue;
            } else {
                shipment.opeCode = null;
            }
            loadTable();
        }
    });
    $('#opr').combobox('select', 'Chọn OPR');

    // loadTable();
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
        onBeforeSelect: function (index, row) {
            getSelected(index, row);
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
                        $("#dg").datagrid("selectRow", 0);
                    } else {
                        success([]);
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

function formatBlBooking(value, row) {
    if (row) {
        if (row.blNo) {
            return row.blNo;
        } else if (row.bookingNo) {
            return row.bookingNo;
        }
    }
    return '';
}

function formatServiceType(value) {
    switch (value) {
        case 1:
            return 'Bốc Hàng';
        case 2:
            return 'Hạ Rỗng';
        case 3:
            return 'Bốc Rỗng';
        case 4:
            return 'Hạ Hàng';
        default:
            return ''
    }
}

// Trigger when click a row in easy ui data grid on the left screen
function getSelected(index, row) {
    if (row) {
        shipmentSelected = row;
        rowAmount = shipmentSelected.containerAmount;
        checkList = Array(rowAmount).fill(0);
        allChecked = false;
        let title = '';
        title += 'Mã Lô: ' + row.id + ' - ';
        title += 'SL: ' + row.containerAmount + ' - ';
        title += 'Booking No: ';
        if (row.bookingNo != null) {
            title += row.bookingNo;
        } else {
            title += 'Trống';
        }
        $.ajax({
            type: "GET",
            url: PREFIX + "/shipments/" + row.id + "/shipment-images",
            contentType: "application/json",
            success: function (data) {
                if (data.code == 0) {
                    if (data.shipmentFiles != null && data.shipmentFiles.length > 0) {
                        data.shipmentFiles.forEach(function (element, index) {
                            title += ' <a href="' + element.path + '" target="_blank"><i class="fa fa-paperclip" style="font-size: 18px;"></i> ' + (index + 1) + '</a>';
                        });
                    }
                }
                $('#shipmentInfo').html(title);
            },
            error: function (errr) {
                $('#shipmentInfo').html(title);
            }
        });
    }
    loadShipmentDetails(shipmentSelected.id);
    loadListComment();
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
    if (sourceData[row] && sourceData[row].contSupplyStatus && sourceData[row].processStatus && sourceData[row].paymentStatus && sourceData[row].finishStatus) {
        // Command container supply status
        let contSupply = '<i id="contSupply" class="fa fa-check easyui-tooltip" title="Chưa yêu cầu cấp container" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';
        switch (sourceData[row].contSupplyStatus) {
            case 'R':
                contSupply = '<i id="contSupply" class="fa fa-check easyui-tooltip" title="Đang chờ cấp container" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
                break;
            case 'Y':
                contSupply = '<i id="contSupply" class="fa fa-check easyui-tooltip" title="Đã cấp container" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
                break;
            case 'N':
                contSupply = '<i id="contSupply" class="fa fa-check easyui-tooltip" title="Có thể yêu cầu cấp container" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
                break;
        }
        // Command process status
        let process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Chưa xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';
        switch (sourceData[row].processStatus) {
            case 'W':
                process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ kết quả" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
                break;
            case 'Y':
                process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đã làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
                break;
            case 'N':
                if (value > 1 && sourceData[row].contSupplyStatus == 'Y') {
                    process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Có thể làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
                }
                break;
            case 'D':
                process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ hủy lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #f93838;"></i>';
                break;
        }

        // Date receipt status
        let dateReceipt = '<i id="dateReceiptRegister" class="fa fa-clock-o easyui-tooltip" title="Chưa đăng ký ngày đóng hàng" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
        switch (sourceData[row].dateReceiptStatus) {
            case 'N':
                dateReceipt = '<i id="dateReceiptRegister" class="fa fa-clock-o easyui-tooltip" title="Có thể đăng ký ngày đóng hàng" aria-hidden="true" style="margin-left: 8px; color: #3498db"></i>';
                break;
            case 'P':
                dateReceipt = '<i id="dateReceiptRegister" class="fa fa-clock-o easyui-tooltip" title="Ngày đăng ký đóng hàng đang được xét duyệt" aria-hidden="true" style="margin-left: 8px; color: #f8ac59"></i>';
                break;
            case 'S':
                dateReceipt = '<i id="dateReceiptRegister" class="fa fa-clock-o easyui-tooltip" title="Ngày đăng ký đóng hàng đã được chấp nhận" aria-hidden="true" style="margin-left: 8px; color: #1ab394"></i>';
                break;
            case 'E':
                dateReceipt = '<i id="dateReceiptRegister" class="fa fa-clock-o easyui-tooltip" title="Ngày đăng ký đóng hàng bị từ chối" aria-hidden="true" style="margin-left: 8px; color: #ed5565"></i>';
                break;
        }

        // released status
        let released = '<i id="finish" class="fa fa-ship easyui-tooltip" title="Chưa Thể Giao Container" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
        switch (sourceData[row].finishStatus) {
            case "Y":
                released =
                    '<i id="finish" class="fa fa-ship easyui-tooltip" title="Đã Giao Container" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
                break;
            case "N":
                if (sourceData[row].paymentStatus == "Y") {
                    released =
                        '<i id="finish" class="fa fa-ship easyui-tooltip" title="Có Thể Giao Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                }
                break;
        }

        // Return the content
        let content = '<div>' + contSupply + process + dateReceipt + released + '</div>';
        $(td).html(content);
    }
    return td;
}

function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'containerNo' + row).addClass("htMiddle").addClass("htCenter");

    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    if (shipmentSelected.specificContFlg == 0) {
        cellProperties.readOnly = 'true';
        $(td).css("background-color", "rgb(232, 232, 232)");
    }
    if (!value) {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    return td;
}

function houseBillBtnRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'houseBillBtn' + row).addClass("htMiddle").addClass("htCenter");
    let shipmentDetailId;
    if (sourceData && sourceData.length > row) {
        shipmentDetailId = sourceData[row].id;
    }
    value = '<button class="btn btn-success btn-xs" id="detailBtn ' + row + '" onclick="openHouseBillForm(' + shipmentDetailId + ')"><i class="fa fa-check-circle"></i>Khai báo</button>';
    $(td).html(value);
    cellProperties.readOnly = 'true';
    return td;
}

function dateReceiptRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'dateReceipt' + row).addClass("htMiddle").addClass("htCenter");
    if (value != null && value != '') {
        if (value.substring(2, 3) != "/") {
            value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
        }
    } else {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    return td;
}

function expiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'expiredDem' + row).addClass("htMiddle").addClass("htCenter");
    if (value != null && value != '') {
        if (value.substring(2, 3) != "/") {
            value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
        }
    } else {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'consignee' + row).addClass("htMiddle");
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    if (!value) {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    return td;
}
function planningDateRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'planningDate' + row).addClass("htMiddle").addClass("htCenter");
    if (value != null && value != '') {
        if (value.substring(2, 3) != "/") {
            value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
        }
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    } else {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    return td;
}
function cargoTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'cargoType' + row).addClass("htMiddle").addClass("htCenter");
    if (value != null && value != '') {
        value = value.split(':')[0];
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    if (!value) {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    return td;
}
function qualityRequirementRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'qualityRequirement' + row).addClass("htMiddle");
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    if (!value) {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'vslNm' + row).addClass("htMiddle");
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    if (!value) {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    return td;
}
function etaRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'eta' + row).addClass("htMiddle").addClass("htCenter");
    if (value != null && value != '') {
        if (value.substring(2, 3) != "/") {
            value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
        }
    } else {
        value = '';
    }
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'sztp' + row).addClass("htMiddle");
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    if (sztpListDisable[row] == 1) {
        cellProperties.readOnly = 'true';
        $(td).css("background-color", "rgb(232, 232, 232)");
    }
    if (!value) {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'dischargePort' + row).addClass("htMiddle").addClass("htCenter");
    if (value != null && value != '') {
        value = value.split(':')[0];
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    if (!value) {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    return td;
}
function contSupplyRemarkRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'remark' + row).addClass("htMiddle");
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    if (!value) {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    return td;
}

function payTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'payType' + row).addClass("htMiddle").addClass("htCenter");
    if (!value) {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}

function payerRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'payer' + row).addClass("htMiddle").addClass("htCenter");
    if (!value) {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}

function payerNameRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'payerNamer' + row).addClass("htMiddle");
    if (!value) {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    cellProperties.readOnly = 'true';
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}

function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'remark' + row).addClass("htMiddle");
    if (!value) {
        value = '';
    }
    $(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
    return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
    config = {
        stretchH: "all",
        height: $('#right-side__main-table').height() - 35,
        minRows: rowAmount,
        maxRows: rowAmount,
        width: "100%",
        minSpareRows: 0,
        rowHeights: 30,
        fixedColumnsLeft: 3,
        trimDropdown: false,
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
                    return "Container No";
                case 3:
                    return "House Bill";
                case 4:
                    return "Ngày Đóng Hàng";
                case 5:
                    return '<span class="required">Kích Thước</span>';
                case 6:
                    return '<span class="required">Hạn Lệnh</span>';
                case 7:
                    return '<span class="required">Chủ Hàng</span>';
                case 8:
                    return '<span class="required">Ngày Dự <br>Kiến Bốc</span>';
                case 9:
                    return '<span class="required">Loại Hàng</span>';
                case 10:
                    return 'Yêu Cầu <br>Chất Lượng';
                case 11:
                    return '<span class="required">Tàu và Chuyến</span>';
                case 12:
                    return "Ngày tàu đến";
                case 13:
                    return '<span class="required">Cảng Dỡ Hàng</span>';
                case 14:
                    return 'Cấp Container <br>Ghi Chú';
                case 15:
                    return 'PTTT';
                case 16:
                    return 'Mã Số Thuế';
                case 17:
                    return 'Người Thanh Toán';
                case 18:
                    return "Ghi Chú";
            }
        },
        colWidths: [40, 100, 100, 100, 120, 150, 100, 200, 100, 80, 150, 150, 100, 120, 150, 100, 130, 130, 200],
        filter: "true",
        columns: [
            {
                data: "active",
                type: "checkbox",
                className: "htCenter",
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
                data: "housebilBtn",
                renderer: houseBillBtnRenderer
            },
            {
                data: "dateReceipt",
                type: "date",
                dateFormat: "DD/MM/YYYY hh:mm",
                // correctFormat: true,
                defaultDate: new Date(),
                renderer: dateReceiptRenderer
            },
            {
                data: "sztp",
                type: "autocomplete",
                strict: true,
                renderer: sizeRenderer
            },
            {
                data: "expiredDem",
                type: "date",
                dateFormat: "DD/MM/YYYY",
                correctFormat: true,
                defaultDate: new Date(),
                renderer: expiredDemRenderer
            },
            {
                data: "consignee",
                strict: true,
                type: "autocomplete",
                renderer: consigneeRenderer
            },
            {
                data: "planningDate",
                type: "date",
                dateFormat: "DD/MM/YYYY",
                correctFormat: true,
                defaultDate: new Date(),
                renderer: planningDateRenderer
            },
            {
                data: "cargoType",
                type: "autocomplete",
                strict: true,
                renderer: cargoTypeRenderer
            },
            {
                data: "qualityRequirement",
                renderer: qualityRequirementRenderer
            },
            {
                data: "vslNm",
                type: "autocomplete",
                strict: true,
                renderer: vslNmRenderer
            },
            {
                data: "eta",
                renderer: etaRenderer
            },
            {
                data: "dischargePort",
                type: "autocomplete",
                strict: true,
                renderer: dischargePortRenderer
            },
            {
                data: "contSupplyRemark",
                renderer: contSupplyRemarkRenderer
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
                data: "payerName",
                renderer: payerNameRenderer
            },
            {
                data: "remark",
                renderer: remarkRenderer
            },
        ],
        beforeKeyDown: function (e) {
            let selected;
            switch (e.keyCode) {
                // Arrow Left
                case 37:
                    selected = hot.getSelected()[0];
                    if (selected[3] == 0) {
                        e.stopImmediatePropagation();
                    }
                    break;
                // Arrow Up
                case 38:
                    selected = hot.getSelected()[0];
                    if (selected[2] == 0) {
                        e.stopImmediatePropagation();
                    }
                    break;
                // Arrow Right
                case 39:
                    selected = hot.getSelected()[0];
                    if (selected[3] == 17) {
                        e.stopImmediatePropagation();
                    }
                    break
                // Arrow Down
                case 40:
                    selected = hot.getSelected()[0];
                    if (selected[2] == rowAmount - 1) {
                        e.stopImmediatePropagation();
                    }
                    break
                default:
                    break;
            }
        },
        afterChange: onChange
    };
}
configHandson();

hot = new Handsontable(dogrid, config);


function onChange(changes, source) {
    if (!changes) {
        return;
    }
    onChangeFlg = true;
    changes.forEach(function (change) {
        // Trigger when vessel-voyage no change, get list discharge port by vessel, voy no
        if (change[1] == "vslNm" && change[3] != null && change[3] != '') {
            let vesselAndVoy = hot.getDataAtCell(change[0], 11);
            //hot.setDataAtCell(change[0], 10, ''); // dischargePort reset
            if (vesselAndVoy) {
                if (currentVesselVoyage != vesselAndVoy) {
                    currentVesselVoyage = vesselAndVoy;
                    let shipmentDetail = new Object();
                    for (let i = 0; i < berthplanList.length; i++) {
                        if (vesselAndVoy == berthplanList[i].vslAndVoy) {
                            currentEta = berthplanList[i].eta;
                            shipmentDetail.vslNm = berthplanList[i].vslNm;
                            shipmentDetail.voyNo = berthplanList[i].voyNo;
                            shipmentDetail.year = berthplanList[i].year;
                            $.modal.loading("Đang xử lý ...");
                            $.ajax({
                                url: ctx + "/logistic/pods",
                                method: "POST",
                                contentType: "application/json",
                                data: JSON.stringify(shipmentDetail),
                                success: function (data) {
                                    $.modal.closeLoading();
                                    if (data.code == 0) {
                                        hot.updateSettings({
                                            cells: function (row, col, prop) {
                                                if (col == 13) {
                                                    let cellProperties = {};
                                                    dischargePortList = data.dischargePorts;
                                                    cellProperties.source = dischargePortList;
                                                    return cellProperties;
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
                hot.setDataAtCell(change[0], 12, currentEta);
            }
        } else if (change[1] == "containerNo") {
            if (!change[3]) {
                sztpListDisable[change[0]] = 0;
                cleanCell(change[0], 5, sizeList);
            } else {
                $.ajax({
                    url: prefix + "/containerNo/" + change[3] + "/sztp",
                    method: "GET",
                    success: function (data) {
                        if (data.code == 0) {
                            if (data.sztp && data.sztp[0] != '{') {
                                sizeList.forEach(element => {
                                    if (data.sztp == element.substring(0, 4)) {
                                        data.sztp = element;
                                        return false;
                                    }
                                });
                                sztpListDisable[change[0]] = 1;
                                hot.setDataAtCell(change[0], 5, data.sztp);
                            } else {
                                sztpListDisable[change[0]] = 0;
                                cleanCell(change[0], 5, sizeList);
                            }
                        } else {
                            sztpListDisable[change[0]] = 0;
                            cleanCell(change[0], 5, sizeList);
                        }
                    },
                    error: function (err) {
                        sztpListDisable[change[0]] = 0;
                        cleanCell(change[0], 5, sizeList);
                    }
                });
            }
        }
    });
}


function loadShipmentDetails(id) {
    if (id) {
        $.modal.loading("Đang xử lý ...");
        $.ajax({
            url: PREFIX + "/shipment/" + id + "/shipmentDetails/dateReceiptStatus/" + shipment.params.dateReceiptStatus,
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
        allChecked = true;
        for (let i = 0; i < checkList.length; i++) {
            if (hot.getDataAtCell(i, 3) == null) {
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
        if (hot.getDataAtCell(i, 3) != null) {
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
            // if ('N' == object["paymentStatus"]) {
            //   errorFlg = true;
            //   $.modal.alertWarning("Không thể xác nhận chứng từ gốc cho container chưa thanh toán. Vui lòng kiểm tra lại.");
            //   return false;
            // }
            shipmentDetailIds += object["id"] + ",";
        });

        if (!errorFlg) {
            if (shipmentDetailIds.length == 0) {
                $.modal.alertWarning("Bạn chưa chọn container nào.")
                errorFlg = true;
            } else {
                shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length - 1);
            }
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
    loadTable();
}

function clearInput() {
    $('#opr').combobox('select', 'Chọn OPR');
    $('#logisticGroups').combobox('select', '0');
    $('#doStatus').combobox('select', '');
    $("#containerNo").textbox('setText', '');
    $("#bookingNo").textbox('setText', '');
    shipment = new Object();
    shipment.params = new Object();
    loadTable();
}

function confirmDocument() {
    if (getDataSelectedFromTable()) {
        layer.confirm("Xác nhận đăng kí ngày rút.", {
            icon: 3,
            title: "Xác Nhận",
            btn: ['Đồng Ý', 'Hủy Bỏ']
        }, function () {
            $.modal.loading("Đang xử lý ...");
            layer.close(layer.index);
            $.ajax({
                url: PREFIX + "/confirmation",
                method: "POST",
                data: {
                    shipmentDetailIds: shipmentDetailIds,
                    logisticGroupId: shipmentSelected.logisticGroupId
                },
                success: function (res) {
                    $.modal.closeLoading();
                    if (res.code == 0) {
                        $.modal.alertSuccess(res.msg);
                        loadTable();
                    } else {
                        $.modal.alertError(res.msg);
                    }
                },
                error: function (data) {
                    $.modal.closeLoading();
                }
            });
        }, function () {
            // close form
        });
    }
}

function rejectRequestDocument() {
    if (getDataSelectedFromTable()) {
        openReject();
    }

}

/**
 * @param {none}
 * @description open model reject.js
 * @author Khanh
 */
function openReject() {
    layer.open({
        type: 2,
        area: [600 + 'px', 300 + 'px'],
        fix: true,
        maxmin: true,
        shade: 0.3,
        title: 'Khai báo lí do từ chối',
        content: PREFIX + "/reject",
        btn: ["Xác Nhận Từ Chối", "Hủy"],
        shadeClose: false,
        yes: function (index, layero) {
            confirmReject(index, layero);
        },
        cancel: function (index) {
            return true;
        }
    });
}


function confirmReject(index, layero) {
    let childLayer = layero.find("iframe")[0].contentWindow.document;
    const containerNoCheckeds = getListContainerNoFromCheked().join(", ");

    $.modal.loading("Đang xử lý ...");
    layer.close(layer.index);
    $.ajax({
        url: PREFIX + "/reject",
        method: "POST",
        data: {
            shipmentDetailIds: shipmentDetailIds,
        },
        success: function (res) {
            const contentReject = $(childLayer).find("#contentReject").val();
            sendComment(contentReject, shipmentSelected.id, shipmentSelected.logisticGroupId, shipmentSelected.serviceType, containerNoCheckeds);
            if (res.code == 0) {
                $.modal.alertSuccess(res.msg);
                handleLoadTableFromModel();
            } else {
                $.modal.alertError(res.msg);
            }
        },
        error: function (data) {
            onCloseModel();
        },
    });
}

/**
 * @param {}
 * @description Call api to add comment to server
 * @author Khanh
 */
function sendComment(contentReject, shipmentId, logisticGroupId, serviceType, containerNos) {
    console.log(shipmentId);
    let req = {
        topic: "Lí do từ chối yêu cầu đăng kí ngày rút hàng container " + containerNos,
        content: contentReject,
        shipmentId: `${shipmentId}`,
        logisticGroupId: `${logisticGroupId}`,
        serviceType: `${serviceType}`,
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
                $.modal.msgSuccess("Gửi thành công.");
                $("#topic").textbox("setText", "");
                $(".summernote").summernote("code", "");
            } else {
                $.modal.msgError("Gửi thất bại.");
            }
        },
        error: function (error) {
            $.modal.closeLoading();
            $.modal.msgError("Gửi thất bại.");
        },
    });
}




function logisticInfo(id, logistics) {
    $.modal.openLogisticInfo("Thông tin liên lạc " + logistics, ctx + "om/support/logistics/" + id + "/info", null, 470, function () {
        $.modal.close();
    });
}

function loadListComment(shipmentCommentId) {
    let req = {
        serviceType: 1,
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

function openHouseBillForm(shipmentDetailId) {
    if (shipmentDetailId == null) {
        $.modal.alertWarning("Quý khách chưa khai báo container cần làm lệnh!");
        return;
    }
    $.modal.openCustomForm(
        "Khai báo house bill",
        PREFIX + "/shipment-detail/" + shipmentDetailId + "/house-bill"
    );
}

function getListContainerNoFromCheked() {
    let result = [];
    for (let i = 0; i < sourceData.length; ++i) {
        if (checkList[i] == 1) {
            result.push(sourceData[i].containerNo);
        }
    }
    return result;
}

/**
 * @param {none}
 * @description handel event load again table in reject.js to load Table
 * @author Khanh
 */
function handleLoadTableFromModel() {
    loadTable();
}

function saveDocument() {
    let payload = [];
    for (let i = 0; i < checkList.length; ++i) {
        if (checkList[i] == 1) {
            payload.push({ ...sourceData[i], dateReceipt: formatDateToSendServer(sourceData[i].dateReceipt) });
        }
    }
    $.ajax({
        url: PREFIX + "/shipment-detail",
        method: "POST",
        contentType: "application/json",
        accept: "text/plain",
        data: JSON.stringify(payload),
        dataType: "text",
        success: function (data) {
            var result = JSON.parse(data);
            if (result.code == 0) {
                $.modal.alertSuccess(result.msg);
                loadTable();
            } else {
                $.modal.alertError(result.msg);
            }
            $.modal.closeLoading();
        },
        error: function (result) {
            $.modal.alertError(
                "Có lỗi trong quá trình thêm dữ liệu, xin vui lòng thử lại."
            );
            $.modal.closeLoading();
        },
    });
}

function formatDateToSendServer(date) {
    if (new Date(date).getTime()) {
        return new Date(date).getTime();
    }
    let result;
    if (date) {
        let expiredDem = new Date(date.substring(6, 10) + "/" + date.substring(3, 5) + "/" + date.substring(0, 2));

        expiredDem.setHours(23, 59, 59);
        result = expiredDem.getTime();
    }
    return result;
}