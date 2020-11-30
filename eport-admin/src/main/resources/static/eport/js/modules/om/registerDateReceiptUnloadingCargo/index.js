const PREFIX = ctx + "om/register-date-receipt-unloading-cargo";
const HIST_PREFIX = ctx + "om/controlling";
const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected, checkList, allChecked, sourceData, rowAmount = 0, shipmentDetailIds;
const greenBlackColor = "rgb(104 241 131)";
var shipment = new Object();
shipment.params = new Object();

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
    let content = "";
    if (checkList[row] == 1) {
        content +=
            '<div><input type="checkbox" id="check' +
            row +
            '" onclick="check(' +
            row +
            ')" checked></div>';
    } else {
        content +=
            '<div><input type="checkbox" id="check' +
            row +
            '" onclick="check(' +
            row +
            ')"></div>';
    }
    $(td)
        .attr("id", "checkbox" + row)
        .addClass("htCenter")
        .addClass("htMiddle")
        .html(content);
    return td;
}

function statusIconsRenderer(
    instance,
    td,
    row,
    col,
    prop,
    value,
    cellProperties
) {
    $(td)
        .attr("id", "statusIcon" + row)
        .addClass("htCenter")
        .addClass("htMiddle");
    if (
        sourceData[row] &&
        sourceData[row].id &&
        sourceData[row].dischargePort &&
        sourceData[row].processStatus &&
        sourceData[row].finishStatus
    ) {
        // Customs Status
        let customs =
            '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chờ Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
        switch (sourceData[row].customStatus) {
            case "R":
                customs =
                    '<i id="custom" class="fa fa-shield easyui-tooltip" title="Đã Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
                break;
            case "Y":
                customs =
                    '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chưa Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #ed5565;"></i>';
                break;
            case "N":
                customs =
                    '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chờ Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                break;
        }
        // Command process status
        let process =
            '<i id="verify" class="fa fa-windows easyui-tooltip" title="Chưa xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';
        switch (sourceData[row].processStatus) {
            case "W":
                process =
                    '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ kết quả" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
                break;
            case "Y":
                process =
                    '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đã làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
                break;
            case "N":
                if (value > 1) {
                    process =
                        '<i id="verify" class="fa fa-windows easyui-tooltip" title="Có thể làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
                }
                break;
            case "D":
                process =
                    '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ hủy lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #f93838;"></i>';
                break;
            case "E":
                payment =
                    '<i id="verify" class="fa fa-windows easyui-tooltip" title="Lỗi làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #ed5565;"></i>';
                break;
        }
        // Payment status
        let payment =
            '<i id="payment" class="fa fa-clock-o easyui-tooltip" title="Chưa Đăng Kí Ngày Rút Hàng" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
        switch (sourceData[row].dateReceiptStatus) {
            case "E":
                payment =
                    '<i id="payment" class="fa fa-clock-o easyui-tooltip" title="Lỗi Đăng Kí Ngày Rút Hàng" aria-hidden="true" style="margin-left: 8px; color : #ed5565;"></i>';
                break;
            case "S":
                payment =
                    '<i id="payment" class="fa fa-clock-o easyui-tooltip" title="Đã Đăng Kí Ngày Rút Hàng" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
                break;
            case "P":
                payment =
                    '<i id="payment" class="fa fa-clock-o easyui-tooltip" title="Chờ Đăng Kí Ngày Rút Hàng" aria-hidden="true" style="margin-left: 8px; color: #f8ac59;"></i>';
                break;

            case null:
                if (value > 2) {
                    payment = '<i id="payment" class="fa fa-clock-o easyui-tooltip" title="Có thể Đăng Kí Ngày Rút Hàng" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                }
                break;
            case "W":
                if (value > 2) {
                    payment = '<i id="payment" class="fa fa-clock-o easyui-tooltip" title="Có thể Đăng Kí Ngày Rút Hàng" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                }
                break;
        }

        // Return the content
        let content = "<div>";
        // Domestic cont: VN --> not show
        if (sourceData[row].loadingPort.substring(0, 2) != "VN") {
            content += customs;
        }
        content += process + payment;
        content += "</div>";
        $(td).html(content);
    }
    return td;
}

function containerNoRenderer(
    instance,
    td,
    row,
    col,
    prop,
    value,
    cellProperties
) {
    if (value != null && value != "") {
        if (hot.getDataAtCell(row, 1) != null) {
            cellProperties.readOnly = "true";
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    if (!value) {
        value = "";
    }
    $(td)
        .attr("id", "containerNo" + row)
        .addClass("htMiddle")
        .addClass("htCenter");
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    return td;
}

function houseBillBtnRenderer(
    instance,
    td,
    row,
    col,
    prop,
    value,
    cellProperties
) {
    $(td)
        .attr("id", "houseBillBtn" + row)
        .addClass("htMiddle")
        .addClass("htCenter");
    let shipmentDetailId;
    if (sourceData && sourceData.length > row) {
        shipmentDetailId = sourceData[row].id;
    }
    if (shipmentDetailId) {
        value =
            '<button class="btn btn-success btn-xs" id="detailBtn ' +
            row +
            '" onclick="openHouseBillForm(' +
            shipmentDetailId +
            ')"><i class="fa fa-check-circle"></i>Khai báo</button>';
    }

    $(td).html(value);
    cellProperties.readOnly = "true";
    return td;
}

function expiredDemRenderer(
    instance,
    td,
    row,
    col,
    prop,
    value,
    cellProperties
) {
    if (shipmentSelected.edoFlg == "1") {
        cellProperties.readOnly = "true";
        $(td).css("background-color", "rgb(232, 232, 232)");
    }
    if (value != null && value != "") {
        if (value.substring(2, 3) != "/") {
            value =
                value.substring(8, 10) +
                "/" +
                value.substring(5, 7) +
                "/" +
                value.substring(0, 4);
        }
        $(td)
            .attr("id", "expiredDem" + row)
            .addClass("htMiddle")
            .addClass("htCenter");
        $(td).html(
            '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
            value +
            "</div>"
        );
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = "true";
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    } else {
        $(td).html(
            '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;"></div>'
        );
    }
    return td;
}

function consigneeRenderer(
    instance,
    td,
    row,
    col,
    prop,
    value,
    cellProperties
) {
    $(td)
        .attr("id", "consignee" + row)
        .addClass("htMiddle");
    if (value != null && value != "") {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = "true";
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    if (!value) {
        value = "";
    }
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    return td;
}

function dateReceiptRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'receiptDem' + row).addClass("htMiddle").addClass("htCenter");
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

function emptyDepotRenderer(
    instance,
    td,
    row,
    col,
    prop,
    value,
    cellProperties
) {
    $(td)
        .attr("id", "emptyDepot" + row)
        .addClass("htMiddle");
    if (value != null && value != "") {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = "true";
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    if (!value) {
        value = "";
    }
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    return td;
}

function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (!value) {
        value = "";
    }
    cellProperties.readOnly = "true";
    let backgroundColor = "";
    if (row % 2 == 1) {
        backgroundColor = greenBlackColor;
    } else {
        backgroundColor = "#C6EFCE";
    }
    $(td).css("background-color", backgroundColor);
    $(td).css("color", "#006100");
    $(td)
        .attr("id", "opeCode" + row)
        .addClass("htMiddle");
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    return td;
}

function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (!value) {
        value = "";
    }
    cellProperties.readOnly = "true";
    let backgroundColor = "";
    if (row % 2 == 1) {
        backgroundColor = greenBlackColor;
    } else {
        backgroundColor = "#C6EFCE";
    }
    $(td).css("background-color", backgroundColor);
    $(td).css("color", "#006100");
    $(td)
        .attr("id", "vslNm" + row)
        .addClass("htMiddle");
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    return td;
}

function voyNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (!value) {
        value = "";
    }
    cellProperties.readOnly = "true";
    let backgroundColor = "";
    if (row % 2 == 1) {
        backgroundColor = greenBlackColor;
    } else {
        backgroundColor = "#C6EFCE";
    }
    $(td).css("background-color", backgroundColor);
    $(td).css("color", "#006100");
    $(td)
        .attr("id", "voyNo" + row)
        .addClass("htMiddle")
        .addClass("htCenter");
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    return td;
}

function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (!value) {
        value = "";
    }
    cellProperties.readOnly = "true";
    let backgroundColor = "";
    if (row % 2 == 1) {
        backgroundColor = greenBlackColor;
    } else {
        backgroundColor = "#C6EFCE";
    }
    $(td).css("background-color", backgroundColor);
    $(td).css("color", "#006100");
    $(td)
        .attr("id", "sztp" + row)
        .addClass("htMiddle")
        .addClass("htCenter");
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    return td;
}

function sealNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (!value) {
        value = "";
    }
    cellProperties.readOnly = "true";
    let backgroundColor = "";
    if (row % 2 == 1) {
        backgroundColor = greenBlackColor;
    } else {
        backgroundColor = "#C6EFCE";
    }
    $(td).css("background-color", backgroundColor);
    $(td).css("color", "#006100");
    $(td)
        .attr("id", "sztp" + row)
        .addClass("htMiddle")
        .addClass("htCenter");
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    return td;
}

function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (!value) {
        value = "";
    }
    cellProperties.readOnly = "true";
    let backgroundColor = "";
    if (row % 2 == 1) {
        backgroundColor = greenBlackColor;
    } else {
        backgroundColor = "#C6EFCE";
    }
    $(td).css("background-color", backgroundColor);
    $(td).css("color", "#006100");
    $(td)
        .attr("id", "wgt" + row)
        .addClass("htMiddle")
        .addClass("htRight");
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    return td;
}

function loadingPortRenderer(
    instance,
    td,
    row,
    col,
    prop,
    value,
    cellProperties
) {
    if (!value) {
        value = "";
    }
    cellProperties.readOnly = "true";
    let backgroundColor = "";
    if (row % 2 == 1) {
        backgroundColor = greenBlackColor;
    } else {
        backgroundColor = "#C6EFCE";
    }
    $(td).css("background-color", backgroundColor);
    $(td).css("color", "#006100");
    $(td)
        .attr("id", "loadingPort" + row)
        .addClass("htMiddle")
        .addClass("htCenter");
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    return td;
}

function dischargePortRenderer(
    instance,
    td,
    row,
    col,
    prop,
    value,
    cellProperties
) {
    if (!value) {
        value = "";
    }
    cellProperties.readOnly = "true";
    let backgroundColor = "";
    if (row % 2 == 1) {
        backgroundColor = greenBlackColor;
    } else {
        backgroundColor = "#C6EFCE";
    }
    $(td).css("background-color", backgroundColor);
    $(td).css("color", "#006100");
    $(td)
        .attr("id", "dischargePort" + row)
        .addClass("htMiddle")
        .addClass("htCenter");
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    return td;
}

function payTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (!value) {
        value = "";
    }
    $(td)
        .attr("id", "payType" + row)
        .addClass("htMiddle")
        .addClass("htCenter");
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    cellProperties.readOnly = "true";
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}

function payerRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (!value) {
        value = "";
    }
    $(td)
        .attr("id", "payer" + row)
        .addClass("htMiddle")
        .addClass("htCenter");
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    cellProperties.readOnly = "true";
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}

function payerNameRenderer(
    instance,
    td,
    row,
    col,
    prop,
    value,
    cellProperties
) {
    if (!value) {
        value = "";
    }
    $(td)
        .attr("id", "payerNamer" + row)
        .addClass("htMiddle");
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    cellProperties.readOnly = "true";
    $(td).css("background-color", "rgb(232, 232, 232)");
    return td;
}

function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td)
        .attr("id", "remark" + row)
        .addClass("htMiddle");
    if (!value) {
        value = "";
    }
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    return td;
}

function detFreeTimeRenderer(
    instance,
    td,
    row,
    col,
    prop,
    value,
    cellProperties
) {
    if (shipmentSelected.edoFlg == "1") {
        cellProperties.readOnly = "true";
        $(td).css("background-color", "rgb(232, 232, 232)");
    }
    if (value != null && value != "") {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = "true";
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    $(td)
        .attr("id", "detFreeTime" + row)
        .addClass("htMiddle")
        .addClass("htRight");
    if (!value) {
        value = "";
    }
    $(td).html(
        '<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
        value +
        "</div>"
    );
    return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
    config = {
        stretchH: "all",
        height: $("#right-side__main-table").height() - 35,
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
        className: "htCenter",
        colHeaders: function (col) {
            switch (col) {
                case 0:
                    let txt = "<input type='checkbox' class='checker' ";
                    txt += "onclick='checkAll()' ";
                    txt += ">";
                    return txt;
                case 1:
                    return "Trạng Thái";
                case 2:
                    return '<span class="required">Container No</span>';
                case 3:
                    return "House Bill";
                case 4:
                    return '<span class="required">Hạn Lệnh</span>';
                case 5:
                    return "Ngày Miễn<br>Lưu Bãi";
                case 6:
                    return '<span class="required">Chủ Hàng</span>';
                case 7:
                    return 'Ngày Rút Hàng';
                case 8:
                    return '<span class="required">Nơi Hạ Vỏ</span>';
                case 9:
                    return "Kích Thước";
                case 10:
                    return '<span class="required">Hãng Tàu</span>';
                case 11:
                    return '<span class="required">Tàu</span>';
                case 12:
                    return '<span class="required">Chuyến</span>';
                case 13:
                    return "Seal No";
                case 14:
                    return "Trọng Lượng (kg)";
                case 15:
                    return '<span class="required">Cảng Xếp Hàng</span>';
                case 16:
                    return "Cảng Dỡ Hàng";
                case 17:
                    return "PTTT";
                case 18:
                    return "Mã Số Thuế";
                case 19:
                    return "Người Thanh Toán";
                case 20:
                    return "Ghi Chú";
            }
        },
        colWidths: [
            40,
            120,
            100,
            100,
            100,
            80,
            150,
            100,
            100,
            80,
            100,
            120,
            70,
            80,
            120,
            120,
            100,
            100,
            130,
            130,
            200,
        ],
        filter: "true",
        columns: [{
            data: "active",
            type: "checkbox",
            className: "htCenter",
            renderer: checkBoxRenderer,
        },
        {
            data: "status",
            readOnly: true,
            renderer: statusIconsRenderer,
        },
        {
            data: "containerNo",
            strict: true,
            renderer: containerNoRenderer,
        },
        {
            data: "housebilBtn",
            renderer: houseBillBtnRenderer,
        },
        {
            data: "expiredDem",
            type: "date",
            dateFormat: "YYYY-MM-DD",
            defaultDate: new Date(),
            renderer: expiredDemRenderer,
        },
        {
            data: "detFreeTime",
            type: "numeric",
            renderer: detFreeTimeRenderer,
        },
        {
            data: "consignee",
            type: "autocomplete",
            strict: true,
            renderer: consigneeRenderer,
        },
        {
            data: "dateReceipt",
            type: "date",
            dateFormat: "DD/MM/YYYY",
            correctFormat: true,
            defaultDate: new Date(),
            renderer: dateReceiptRenderer,
        },
        {
            data: "emptyDepot",
            type: "autocomplete",
            strict: true,
            renderer: emptyDepotRenderer,
        },
        {
            data: "sztp",
            type: "autocomplete",
            strict: true,
            renderer: sizeRenderer,
        },
        {
            data: "opeCode",
            type: "autocomplete",
            strict: true,
            renderer: opeCodeRenderer,
        },
        {
            data: "vslNm",
            type: "autocomplete",
            strict: true,
            renderer: vslNmRenderer,
        },
        {
            data: "voyNo",
            type: "autocomplete",
            strict: true,
            renderer: voyNoRenderer,
        },
        {
            data: "sealNo",
            renderer: sealNoRenderer,
        },
        {
            data: "wgt",
            renderer: wgtRenderer,
        },
        {
            data: "loadingPort",
            type: "autocomplete",
            renderer: loadingPortRenderer,
        },
        {
            data: "dischargePort",
            type: "autocomplete",
            renderer: dischargePortRenderer,
        },
        {
            data: "payType",
            renderer: payTypeRenderer,
        },
        {
            data: "payer",
            renderer: payerRenderer,
        },
        {
            data: "payerName",
            renderer: payerNameRenderer,
        },
        {
            data: "remark",
            renderer: remarkRenderer,
        },
        ],
        afterChange: function (changes, src) {
            //Get data change in cell to render another column
            if (!changes) {
                return;
            }
            onChangeFlg = true;
            if (src !== "loadData") {
                changes.forEach(function interate(change) {
                    if (change[1] == "vslNm" && change[3] != null && change[3] != "") {
                        $.ajax({
                            url: ctx + "logistic/vessel/" + change[3] + "/voyages",
                            method: "GET",
                            success: function (data) {
                                if (data.code == 0) {
                                    hot.updateSettings({
                                        cells: function (row, col, prop) {
                                            if (row == change[0] && col == 10) {
                                                let cellProperties = {};
                                                cellProperties.source = data.voyages;
                                                return cellProperties;
                                            }
                                        },
                                    });
                                }
                            },
                        });
                    } else {
                        let containerNo;
                        if (change[1] == "containerNo") {
                            containerNo = hot.getDataAtRow(change[0])[2];
                            isChange = true;
                        } else {
                            isChange = false;
                        }
                        if (
                            containerNo != null &&
                            isChange &&
                            shipmentSelected.edoFlg == "0" &&
                            /[A-Z]{4}[0-9]{7}/g.test(containerNo)
                        ) {
                            $.modal.loading("Đang xử lý...");
                            // CLEAR DATA
                            hot.setDataAtCell(change[0], 5, ""); //consignee
                            hot.setDataAtCell(change[0], 7, ""); //sztp
                            hot.setDataAtCell(change[0], 8, ""); //opeCode
                            hot.setDataAtCell(change[0], 9, ""); //vslNm
                            hot.setDataAtCell(change[0], 10, ""); //voyNo
                            hot.setDataAtCell(change[0], 11, ""); //sealNo
                            hot.setDataAtCell(change[0], 12, ""); //wgt
                            hot.setDataAtCell(change[0], 13, ""); //loadingPort
                            hot.setDataAtCell(change[0], 14, ""); //dischargePort
                            containerRemarkArr[change[0]] = ""; // container remark from catos
                            locations[change[0]] = ""; // yard position from catos

                            // Call data to auto-fill
                            $.ajax({
                                url: prefix + "/shipment-detail/bl-no/cont/info",
                                type: "POST",
                                contentType: "application/json",
                                data: JSON.stringify({
                                    blNo: shipmentSelected.blNo,
                                    containerNo: containerNo,
                                }),
                            }).done(function (shipmentDetail) {
                                if (shipmentDetail != null) {
                                    hot.setDataAtCell(change[0], 5, shipmentDetail.consignee); //consignee
                                    hot.setDataAtCell(change[0], 7, shipmentDetail.sztp); //sztp
                                    hot.setDataAtCell(change[0], 8, shipmentDetail.opeCode); //opeCode
                                    hot.setDataAtCell(change[0], 9, shipmentDetail.vslNm); //vslNm
                                    hot.setDataAtCell(change[0], 10, shipmentDetail.voyNo); //voyNo
                                    hot.setDataAtCell(change[0], 11, shipmentDetail.sealNo); //sealNo
                                    hot.setDataAtCell(change[0], 12, shipmentDetail.wgt); //wgt
                                    hot.setDataAtCell(change[0], 13, shipmentDetail.loadingPort); //loadingPort
                                    hot.setDataAtCell(
                                        change[0],
                                        14,
                                        shipmentDetail.dischargePort
                                    ); //dischargePort
                                    containerRemarkArr[change[0]] =
                                        shipmentDetail.containerRemark; // container remark from catos
                                    locations[change[0]] = shipmentDetail.location; // yard position from catos
                                    voyCarrier = shipmentDetail.voyCarrier;
                                }
                            });
                        }
                    }
                });
                $.modal.closeLoading();
            }
        },
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
                    if (selected[3] == 18) {
                        e.stopImmediatePropagation();
                    }
                    break;
                // Arrow Down
                case 40:
                    selected = hot.getSelected()[0];
                    if (selected[2] == rowAmount - 1) {
                        e.stopImmediatePropagation();
                    }
                    break;
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