var prefix = ctx + "logistic/sendContEmpty";
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected, shipmentDetails, shipmentDetailIds, sourceData, processOrderIds;
var contList = [];
var conts = '';
var allChecked = false;
var checkList = [];
var rowAmount = 0;
var sizeList = [
    "22G0: Cont 20 feet khô", 
    "22P0: Cont 20 feet flat rack - quá khổ", 
    "22R0: Cont 20 feet lạnh", 
    "22T0: Cont 20 feet tank - cont bồn",
    "22U0: Cont 20 feet open top", 
    "42G0: Cont 40 feet thấp khô", 
    "42P0: Cont 40 feet thấp flat rack - quá khổ",
    "42R0: Cont 40 feet thấp lạnh", 
    "42T0: Cont 40 feet thấp tank - cont bồn", 
    "42U0: Cont 40 feet thấp open top",
    "45G0: Cont 40 feet cao khô", 
    "45P0: Cont 40 feet cao flat rack - quá khổ", 
    "45R0: Cont 40 feet cao lạnh",
    "45T0: Cont 40 feet cao tank - cont bồn",
    "45U0: Cont 40 feet cao open top", 
    "L4G0: Cont 45 feet khô", 
    "L4P0: Cont 45 feet flat rack - quá khổ", 
    "L4R0: Cont 45 feet lạnh",
    "L4T0: Cont 45 feet tank - cont bồn",
    "L4U0: Cont 45 feet open top"
];
var consigneeList, opeCodeList, dischargePortList, vslNmList, currentProcessId, currentSubscription;

$.ajax({
    url: prefix + "/getField",
    method: "GET",
    success: function (data) {
        if (data.code == 0) {
            dischargePortList = data.dischargePortList;
            opeCodeList = data.opeCodeList;
            vslNmList = data.vslNmList;
            consigneeList = data.consigneeList;
        }
    }
});
var cargoTypeList = ["AK:Over Dimension", "BB:Break Bulk", "BN:Bundle", "DG:Dangerous", "DR:Reefer & DG", "DE:Dangerous Empty", "FR:Fragile", "GP:General", "MT:Empty", "RF:Reefer"];

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
    loadTable();
    $(".left-side").css("height", $(document).height());
    $("#btn-collapse").click(function () {
        handleCollapse(true);
    });
    $("#btn-uncollapse").click(function () {
        handleCollapse(false);
    });

    // Handle add
    $(function () {
        let options = {
            createUrl: prefix + "/addShipmentForm",
            updateUrl: "0",
            modalName: " Lô"
        };
        $.table.init(options);
    });
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
function loadTable() {
    $("#dg").datagrid({
        url: prefix + "/listShipment",
        height: window.innerHeight - 70,
        singleSelect: true,
        collapsible: true,
        clientPaging: false,
        pagination: true,
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
                data: {
                    pageNum: param.page,
                    pageSize: param.rows,
                    orderByColumn: param.sort,
                    isAsc: param.order,
                },
                dataType: "json",
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
    return day + "/" + monthText + "/" + date.getFullYear();
}

// FORMAT REMARK FOR SHIPMENT LIST
function formatRemark(value) {
    return '<div class="easyui-tooltip" title="' + ((value != null && value != "") ? value : "không có ghi chú") + '" style="width: 80; text-align: center;"><span>' + (value != null && value != '' ? value : "...") + '</span></div>';
}

function handleRefresh() {
    loadTable();
}

// HANDLE WHEN SELECT A SHIPMENT
function getSelected() {
    var row = $("#dg").datagrid("getSelected");
    if (row) {
        shipmentSelected = row;
        $(function () {
            var options = {
                createUrl: prefix + "/addShipmentForm",
                updateUrl: prefix + "/editShipmentForm/" + shipmentSelected.id,
                modalName: " Lô"
            };
            $.table.init(options);
        });
        $("#loCode").text(row.id);
        $("#taxCode").text(row.taxCode);
        $("#quantity").text(row.containerAmount);
        rowAmount = row.containerAmount;
        checkList = Array(rowAmount).fill(0);
        allChecked = false;
        loadShipmentDetail(row.id);
    }
}

// FORMAT HANDSONTABLE COLUMN
function checkBoxRenderer(instance, td, row, col, prop, value, cellProperties) {
    let content = '';
    if (checkList[row] == 1 || value) {
        content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')" checked></div>';
    } else {
        content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')"></div>';
    }
    $(td).attr('id', 'checkbox' + row).addClass("htCenter").addClass("htMiddle").html(content);
    return td;
}
function statusIconsRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'statusIcon' + row).addClass("htCenter").addClass("htMiddle");
    let content = '';
        switch (value) {
            case 1:
                content += '<div><i id="verify" class="fa fa-mobile easyui-tooltip" title="Chưa Xác Nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: rgb(5, 148, 148);"></i>';
                content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px;"></i>';
                content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Hạ Container" aria-hidden="true" style="margin-left: 8px;"></i></div>';
                break;
            case 2:
                content += '<div><i id="verify" class="fa fa-mobile easyui-tooltip" title="Đã Xác Thực" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
                content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: rgb(5, 148, 148);"></i>';
                content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Hạ Container" aria-hidden="true" style="margin-left: 8px;"></i></div>';
                break;
            case 3:
                content += '<div><i id="verify" class="fa fa-mobile easyui-tooltip" title="Chưa Xác Thực" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
                content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Hạ Container" aria-hidden="true" style="margin-left: 8px; color: rgb(5, 148, 148);"></i></div>';
                break;
            case 4:
                content += '<div><i id="verify" class="fa fa-mobile easyui-tooltip" title="Chưa Xác Thực" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
                content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Đã Hạ Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i></div>';
                break;
            default:
                break;
        }
        $(td).html(content);
    return td;
}
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'containerNo' + row).addClass("htMiddle");
    $(td).html(value);
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    return td;
}
function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'opeCode' + row).addClass("htMiddle");
    $(td).html(value);
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    return td;
}
function expiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'expiredDem' + row).addClass("htMiddle");
    $(td).html(value);
    if (value != null && value != '') {
        if (value.substring(2, 3) != "/") {
            value = value.substring(8, 10)+"/"+value.substring(5, 7)+"/"+value.substring(0,4);
        }
        $(td).html(value);
    } else {
        $(td).html('');
    }
    return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'vslNm' + row).addClass("htMiddle");
    $(td).html(value);
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    return td;
}
function voyNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'voyNo' + row).addClass("htMiddle");
    $(td).html(value);
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'sztp' + row).addClass("htMiddle");
    if (value != null && value != '') {
        value = value.split(':')[0];
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    $(td).html(value);
    return td;
}
function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'wgt' + row).addClass("htMiddle");
    $(td).html(value);
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'dischargePort' + row).addClass("htMiddle");
    if (value != null && value != '') {
        value = value.split(':')[0];
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    $(td).html(value);
    return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'remark' + row).addClass("htMiddle");
    $(td).html(value);
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
        fixedColumnsLeft: 3,
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
                    return '<span>Container No</span><span style="color: red;">(*)</span>';
                case 3:
                    return '<span>Hãng Tàu</span><span style="color: red;">(*)</span>';
                case 4:
                    return '<span>Hạn Lệnh</span><span style="color: red;">(*)</span>';
                case 5:
                    return '<span>Tàu</span><span style="color: red;">(*)</span>';
                case 6:
                    return '<span>Chuyến</span><span style="color: red;">(*)</span>';
                case 7:
                    return '<span>Kích Thước</span><span style="color: red;">(*)</span>';
                case 8:
                    return '<span>Trọng Tải</span><span style="color: red;">(*)</span>';
                case 9:
                    return '<span>Cảng Dỡ Hàng</span><span style="color: red;">(*)</span>';
                case 10:
                    return "Ghi Chú";
            }
        },
        colWidths: [50, 100, 100, 120, 100, 100, 100, 100, 100, 150, 200],
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
                strict: true,
                renderer: containerNoRenderer,
            },
            {
                data: "opeCode",
                type: "autocomplete",
                source: opeCodeList,
                strict: true,
                renderer: opeCodeRenderer
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
                data: "vslNm",
                type: "autocomplete",
                source: vslNmList,
                strict: true,
                renderer: vslNmRenderer
            },
            {
                data: "voyNo",
                strict: true,
                renderer: voyNoRenderer
            },
            {
                data: "sztp",
                type: "autocomplete",
                source: sizeList,
                strict: true,
                renderer: sizeRenderer
            },
            {
                data: "wgt",
                type: "numeric",
                strict: true,
                renderer: wgtRenderer
            },
            {
                data: "dischargePort",
                strict: true,
                type: "autocomplete",
                source: dischargePortList,
                renderer: dischargePortRenderer
            },
            {
                data: "remark",
                renderer: remarkRenderer
            },
        ],
        beforeOnCellMouseDown: function restrictSelectionToWholeRowColumn(event, coords) {
            if(coords.col == 0) event.stopImmediatePropagation();
        }
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
    updateLayout();
    hot.render();
    $('.checker').prop('checked', allChecked);
}
function check(id) {
    if (checkList[id] == 0) {
        $('#check'+id).prop('checked', true);
        checkList[id] = 1;
    } else {
        $('#check'+id).prop('checked', false);
        checkList[id] = 0;
    }
    hot.render();
    updateLayout();
}
function updateLayout() {
    let disposable = true, status = 1, diff = false, check = false, verify = false;
    allChecked = true;
    for (let i=0; i<checkList.length; i++) {
        let cellStatus = hot.getDataAtCell(i, 1);
        if (cellStatus != null) {
            if (checkList[i] == 1) {
                if(cellStatus == 1 && 'Y' == sourceData[i].userVerifyStatus) {
                    verify = true;
                }
                check = true;
                if (cellStatus > 1) {
                    disposable = false;
                }
                if (status != 1 && status != cellStatus) {
                    diff = true;
                } else {
                    status = cellStatus;
                }
            } else {
                allChecked = false;
            }
        }
    }
    $('.checker').prop('checked', allChecked);
    if (disposable) {
        $("#deleteBtn").prop("disabled", false);
    } else {
        $("#deleteBtn").prop("disabled", true);
    }
    if (diff) {
        status = 1;
    } else {
        status++;
    }
    if (!check) {
        $("#deleteBtn").prop("disabled", true);
        status = 1;
    }
    switch (status) {
        case 1:
            setLayoutRegisterStatus();
            break;
        case 2:
            setLayoutVerifyUserStatus();
            if (verify) {
                $("#verifyBtn").prop("disabled", true);
                $("#deleteBtn").prop("disabled", true);
            }
            break;
        case 3:
            setLayoutPaymentStatus();
            break;
        case 4:
            setLayoutFinishStatus();
            break;
        default:
            break;
    }
}

// LOAD SHIPMENT DETAIL LIST
function loadShipmentDetail(id) {
    $.ajax({
        url: prefix + "/listShipmentDetail",
        method: "GET",
        data: {
            shipmentId: id
        },
        success: function (data) {
            if (data.code == 0) {
                sourceData = data.shipmentDetails;
                if (rowAmount < sourceData.length) {
                    sourceData = sourceData.slice(0, rowAmount);
                }
                hot.destroy();
                configHandson();
                hot = new Handsontable(dogrid, config);
                hot.loadData(sourceData);
                hot.render();
            }
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

// GET CHECKED SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataSelectedFromTable(isValidate) {
    let myTableData = hot.getSourceData();
    let errorFlg = false;
    let cleanedGridData = [];
    for (let i=0; i<checkList.length; i++) {
        if (checkList[i] == 1 && Object.keys(myTableData[i]).length > 0) {
            cleanedGridData.push(myTableData[i]);
        }
    }
    shipmentDetailIds = "";
    shipmentDetails = [];
    processOrderIds = '';
    let temProcessOrderIds = [];
    $.each(cleanedGridData, function (index, object) {
        let shipmentDetail = new Object();
        shipmentDetail.bookingNo = shipmentSelected.bookingNo;
        shipmentDetail.containerNo = object["containerNo"];
        shipmentDetail.customStatus = object["customStatus"];
        shipmentDetail.processStatus = object["processStatus"];
        shipmentDetail.paymentStatus = object["paymentStatus"];
        shipmentDetail.userVerifyStatus = object["userVerifyStatus"];
        shipmentDetail.status = object["status"];
        shipmentDetail.shipmentId = shipmentSelected.id;
        shipmentDetail.id = object["id"];
        shipmentDetails.push(shipmentDetail);
        if (object["processOrderId"] != null && !temProcessOrderIds.includes(object["processOrderId"])) {
            temProcessOrderIds.push(object["processOrderId"]);
            processOrderIds += object["processOrderId"] + ',';
        }
        shipmentDetailIds += object["id"] + ",";
    });

    if (processOrderIds != '') {
        processOrderIds.substring(0, processOrderIds.length-1);
    }

    // Get result in "selectedList" letiable
    if (shipmentDetails.length == 0 && isValidate) {
        $.modal.alert("Bạn chưa chọn container.");
        errorFlg = true;
    } else {
        shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length - 1);
    }
    if (errorFlg) {
        return false;
    }
}

// GET SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataFromTable(isValidate) {
    let myTableData = hot.getSourceData();
    let errorFlg = false;
    let cleanedGridData = [];
    for (let i=0; i<checkList.length; i++) {
        if (Object.keys(myTableData[i]).length > 0) {
            cleanedGridData.push(myTableData[i]);
        }
    }
    shipmentDetails = [];
    contList = [];
    conts = '';
    $.each(cleanedGridData, function (index, object) {
        let shipmentDetail = new Object();
        if (isValidate) {
            if(object["containerNo"] == null || object["containerNo"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập số container!");
                errorFlg = true;
            } else if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"])) {
                $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
                errorFlg = true;
            } else if (object["opeCode"] == null || object["opeCode"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn chủ khai thác!");
                errorFlg = true;
            } else if (object["expiredDem"] == null || object["expiredDem"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập hạn lệnh!");
                errorFlg = true;
            } else if (object["vslNm"] == null || object["vslNm"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn tàu!");
                errorFlg = true;
            } else if (object["voyNo"] == null || object["voyNo"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn chuyến!");
                errorFlg = true;
            } else if (object["sztp"] == null || object["sztp"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn kích thước!");
                errorFlg = true;
            } else if (object["wgt"] == null || object["wgt"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn trọng tải!");
                errorFlg = true;
            } else if (object["dischargePort"] == null || object["dischargePort"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn cảng dỡ hàng!");
                errorFlg = true;
            }
        }
        var expiredDem = new Date(object["expiredDem"].substring(6, 10) + "/" + object["expiredDem"].substring(3, 5) + "/" + object["expiredDem"].substring(0, 2));
        shipmentDetail.containerNo = object["containerNo"];
        contList.push(object["containerNo"]);
        if (object["status"] == 1 || object["status"] == null) {
            conts += object["containerNo"] + ',';
        }
        shipmentDetail.opeCode = object["opeCode"];
        shipmentDetail.expiredDem = expiredDem.getTime();
        shipmentDetail.vslNm = object["vslNm"];
        shipmentDetail.voyNo = object["voyNo"];
        shipmentDetail.sztp = object["sztp"].split(":")[0];
        shipmentDetail.wgt = object["wgt"];
        shipmentDetail.dischargePort = object["dischargePort"].split(":")[0];
        shipmentDetail.remark = object["remark"];
        shipmentDetail.shipmentId = shipmentSelected.id;
        shipmentDetail.id = object["id"];
        shipmentDetails.push(shipmentDetail);
        var now = new Date();
        now.setHours(0, 0, 0);
        expiredDem.setHours(23, 59, 59);
        if (expiredDem.getTime() < now.getTime() && isValidate && object["delFlag"] == null) {
            errorFlg = true;
            $.modal.alertError("Hàng " + (index + 1) + ": Hạn lệnh không được trong quá khứ!")
        }
    });

    conts.substring(0, conts.length-1);

    if (isValidate) {
        contList.sort();
        var contTemp = "";
        $.each(contList, function (index, cont) {
            if (cont != "" && cont == contTemp) {
                $.modal.alertError("Số container không được giống nhau!");
                errorFlg = true;
                return false;
            }
            contTemp = cont;
        });
    }

    // Get result in "selectedList" variable
    if (shipmentDetails.length == 0 && !errorFlg) {
        $.modal.alert("Bạn chưa nhập thông tin.");
        errorFlg = true;
    }

    if (errorFlg) {
        return false;
    } else {
        return true;
    }
}

// SAVE/EDIT/DELETE SHIPMENT DETAIL
function saveShipmentDetail() {
    if (shipmentSelected == null) {
        $.modal.msgError("Bạn cần chọn lô trước");
        return;
    } else {
        if (getDataFromTable(true)) {
            if (shipmentDetails.length > 0 && shipmentDetails.length <= shipmentSelected.containerAmount) {
                shipmentDetails[0].processStatus = conts;
                $.modal.loading("Đang xử lý...");
                $.ajax({
                    url: prefix + "/saveShipmentDetail",
                    method: "post",
                    contentType: "application/json",
                    accept: 'text/plain',
                    data: JSON.stringify(shipmentDetails),
                    dataType: 'text',
                    success: function (data) {
                        var result = JSON.parse(data);
                        if (result.code == 0) {
                            $.modal.msgSuccess(result.msg);
                            loadShipmentDetail(shipmentSelected.id);
                        } else {
                            if (result.conts != null) {
                                $.modal.alertError("Không thể làm lệnh đối với các container: "+result.conts);
                            } else {
                                $.modal.msgError(result.msg);
                            }
                        }
                        $.modal.closeLoading();
                    },
                    error: function (result) {
                        $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
                        $.modal.closeLoading();
                    },
                });
            } else if (shipmentDetails.length > shipmentSelected.containerAmount) {
                $.modal.alertError("Số container nhập vào vượt quá số container<br>của lô.");
            } else {
                $.modal.alertError("Quý khách chưa nhập thông tin chi tiết lô.");
            }
        }
    }
}

// DELETE SHIPMENT DETAIL
function deleteShipmentDetail() {
    getDataSelectedFromTable(true);
    $.modal.loading("Đang xử lý...");
    $.ajax({
        url: prefix + "/deleteShipmentDetail",
        method: "post",
        data: {
            shipmentDetailIds: shipmentDetailIds
        },
        success: function (result) {
            if (result.code == 0) {
                $.modal.msgSuccess(result.msg);
                loadShipmentDetail(shipmentSelected.id);
            } else {
                $.modal.msgError(result.msg);
            }
            $.modal.closeLoading();
        },
        error: function (result) {
            $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
            $.modal.closeLoading();
        },
    });
}

// Handling logic
function verify() {
    getDataSelectedFromTable(true);
    if (shipmentDetails.length > 0) {
        $.modal.openCustomForm("Xác nhận làm lệnh", prefix + "/checkContListBeforeVerify/" + shipmentDetailIds, 600, 500);
    }
}

function verifyOtp(shipmentDtIds, creditFlag) {
    getDataSelectedFromTable(true);
    if (shipmentDetails.length > 0) {
        $.modal.openCustomForm("Xác thực OTP", prefix + "/verifyOtpForm/" + shipmentDtIds + "/" + creditFlag, 600, 350);
    }
}

function pay() {
    getDataSelectedFromTable(true);
    if (shipmentDetails.length > 0) {
        $.modal.openCustomForm("Thanh toán", prefix + "/paymentForm/" + processOrderIds, 800, 400);
    }
}

function exportBill() {

}

// Handling UI STATUS
function setLayoutRegisterStatus() {
    $("#registerStatus").removeClass("label-primary disable").addClass("active");
    $("#verifyStatus").removeClass("label-primary active").addClass("disable");
    $("#paymentStatus").removeClass("label-primary active").addClass("disable");
    $("#finishStatus").removeClass("label-primary active").addClass("disable");
    $("#verifyBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", true);
    $("#exportBillBtn").prop("disabled", true);
}

function setLayoutVerifyUserStatus() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("label-primary disable").addClass("active");
    $("#paymentStatus").removeClass("active label-primary").addClass("disable");
    $("#finishStatus").removeClass("active label-primary").addClass("disable");
    $("#verifyBtn").prop("disabled", false);
    $("#payBtn").prop("disabled", true);
    $("#exportBillBtn").prop("disabled", true);
}

function setLayoutPaymentStatus() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("active disable").addClass("label-primary");
    $("#paymentStatus").removeClass("label-primary disable").addClass("active");
    $("#finishStatus").removeClass("active label-primary").addClass("disable");
    $("#deleteBtn").prop("disabled", true);
    $("#verifyBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", false);
    $("#exportBillBtn").prop("disabled", true);
}

function setLayoutFinishStatus() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("active disable").addClass("label-primary");
    $("#paymentStatus").removeClass("active disable").addClass("label-primary");
    $("#finishStatus").removeClass("label-primary disable").addClass("active");
    $("#deleteBtn").prop("disabled", true);
    $("#verifyBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", true);
    $("#exportBillBtn").prop("disabled", false);
}

function finishForm(result) {
    if (result.code == 0) {
        $.modal.msgSuccess(result.msg);
    } else {
        $.modal.msgError(result.msg);
    }
    reloadShipmentDetail();
}

function finishVerifyForm(result) {
    if (result.code == 0 || result.code == 301){
        $.modal.loading(result.msg);
        currentProcessId = result.processId;
        // CONNECT WEB SOCKET
        connectToWebsocketServer();
       
    } else {
        $.modal.msgError(result.msg);
        reloadShipmentDetail();
    }
}

function napasPaymentForm() {
    $.modal.openTab("Cổng Thanh Toán NAPAS", prefix + "/napasPaymentForm");
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    if (message.code == 0){
        $.modal.alertSuccess(message.msg);
    }else{
        $.modal.alertError(message.msg);
    }
    $.modal.closeLoading();
    reloadShipmentDetail();
}

function connectToWebsocketServer(){
    // Connect to WebSocket Server.
    $.websocket.connect({}, onConnected, onError);
}

function onConnected() {
    console.log('Connect socket.')
    currentSubscription = $.websocket.subscribe(currentProcessId + '/response', onMessageReceived);
}

function onError(error) {
    console.log(error);
    $.modal.alertError('Could not connect to WebSocket server. Please refresh this page to try again!');
    $.modal.closeLoading();
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    if (message.code == 0){
        $.modal.alertSuccess(message.msg);
    }else{
        $.modal.alertError(message.msg);
    }

    // Close loading
    $.modal.closeLoading();

    // Unsubscribe destination
    if (currentSubscription){
        currentSubscription.unsubscribe();
    }

    // Close websocket connection 
    $.websocket.disconnect(onDisconnected);

    reloadShipmentDetail();
}

function onDisconnected(){
    console.log('Disconnected socket.');
}
  




