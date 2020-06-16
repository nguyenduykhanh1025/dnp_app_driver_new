var isChange = true;
var prefix = ctx + "logistic/receiveContEmpty";
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected;
var shipmentDetails;
var shipmentDetailIds;
var checked = false;
var allChecked = true;
var isIterate = false;
var isSaved = false;
var opeCodeList = ["MCM", "NIC", "BMW"];
var sizeList = ["22G0", "23G0"];
var vslNmList = ["Vessel 1", "Vessel 2", "Vessel 3"];
var voyNoList = ["Voyage 1", "Voyage 2", "Voyage 3"];
var LoadingPortList = ["Cảng nguồn 1", "Cảng nguồn 2"];
var DischargePortList = ["Cảng đích 1", "Cảng đích 2"];
var selectedRow = null;
var rowAmount = 0;

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
        nowrap: false,
        striped: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            var opts = $(this).datagrid("options");
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

// FORMAT REMARK FOR SHIPMENT LIST
function formatRemark(value) {
    return '<div class="easyui-tooltip" title="'+ ((value!=null&&value!="")?value:"không có ghi chú") +'" style="width: 80; text-align: center;"><span>'+ (value!=null?(value.substring(0, 5) + "..."):"...") +'</span></div>';
}

// FORMAT DATE FOR SHIPMENT LIST
function formatDate(value) {
    var date = new Date(value);
    var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    var month = date.getMonth() + 1;
    var monthText = month < 10 ? "0" + month : month;
    return day + "/" + monthText + "/" + date.getFullYear();
}

// Handle add
$(function () {
    var options = {
        createUrl: prefix + "/addShipmentForm",
        updateUrl: "0",
        modalName: " Lô"
    };
    $.table.init(options);
});

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
        loadShipmentDetail(row.id);
    }
}

// FORMAT HANDSONTABLE COLUMN
function registerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'registerNo' + row).css("background-color", "rgb(232, 232, 232)").html(value).addClass("htMiddle");
        cellProperties.readOnly = 'true';
    } else {
        $(td).html('');
    }
    return td;
}
function processRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        if (value == "Y") {
            $(td).css("background-color", "rgb(92, 255, 92)").html("Đã làm lệnh").addClass("htMiddle");
        } else {
            $(td).css("background-color", "rgb(225, 255, 34)").html("Chưa làm lệnh").addClass("htMiddle");
        }
        $(td).attr('id', 'process' + row);
    } else {
        $(td).html('');
    }
    return td;
}
function paymentRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        if (value == "Y") {
            $(td).css("background-color", "rgb(92, 255, 92)").html("Đã Thanh toán").addClass("htMiddle");
        } else {
            $(td).css("background-color", "rgb(225, 255, 34)").html("Chưa thanh toán").addClass("htMiddle");
        }
        $(td).attr('id', 'payment' + row);
    } else {
        $(td).html('');
    }
    return td;
}
function statusRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        if (value != 4) {
            $(td).css("background-color", "rgb(225, 255, 34)").html("Chưa hạ container").addClass("htMiddle");
        } else {
            $(td).css("background-color", "rgb(92, 255, 92)").html("Đã hạ container").addClass("htMiddle");
        }
        $(td).attr('id', 'status' + row);
    } else {
        $(td).html('');
    }
    return td;
}
function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'opeCode' + row).html(value).addClass("htMiddle");
    } else {
        $(td).html('');
    }
    return td;
}
function bookingNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'bookingNo' + row).html(value).addClass("htMiddle");
    } else {
        $(td).html('');
    }
    return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'size' + row).html(value).addClass("htMiddle");
    } else {
        $(td).html('');
    }
    return td;
}
function expiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        if (value.substring(2, 3) != "/") {
            value = value.substring(8, 10)+"/"+value.substring(5, 7)+"/"+value.substring(0,4);
        }
        $(td).attr('id', 'expiredDem' + row).html(value).addClass("htMiddle");
    } else {
        $(td).html('');
    }
    return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'vslNm' + row).html(value).addClass("htMiddle");
    } else {
        $(td).html('');
    }
    return td;
}
function voyNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'voyNo' + row).html(value).addClass("htMiddle");
    } else {
        $(td).html('');
    }
    return td;
}
function loadingPortRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'loadingPort' + row).html(value).addClass("htMiddle");
    } else {
        $(td).html('');
    }
    return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'dischargePort' + row).html(value).addClass("htMiddle");
    } else {
        $(td).html('');
    }
    return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'remark' + row).html(value).addClass("htMiddle");
    } else {
        $(td).html('');
    }
    return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
    config = {
        stretchH: "all",
        height: document.documentElement.clientHeight - 100,
        minRows: rowAmount,
        maxRows: rowAmount,
        width: "100%",
        minSpareRows: 1,
        rowHeights: 30,
        manualColumnMove: false,
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
                    return "Số Đăng Ký";
                case 2:
                    return '<span>Chủ khai thác</span><span style="color: red;">(*)</span>';
                case 3:
                    return '<span>Số Book</span><span style="color: red;">(*)</span>';
                case 4:
                    return '<span>Kích Thước</span><span style="color: red;">(*)</span>';
                case 5:
                    return '<span>Hạn Lệnh</span><span style="color: red;">(*)</span>';
                case 6:
                    return "Tàu";
                case 7:
                    return "Chuyến";
                case 8:
                    return "Cảng Xếp Hàng";
                case 9:
                    return "Cảng Dỡ Hàng";
                case 10:
                    return "T.T Làm Lệnh";
                case 11:
                    return "T.T Thanh Toán";
                case 12:
                    return "T.T Nhận Cont";
                case 13:
                    return "Ghi Chú";
            }
        },
        colWidths: [50, 100, 100, 100, 100, 100, 100, 100, 100, 100, 150, 150, 150, 200],
        filter: "true",
        columns: [
            {
                data: "active",
                type: "checkbox",
                className: "htCenter",
            },
            {
                data: "registerNo",
                readOnly: true,
                renderer: registerNoRenderer
            },
            {
                data: "opeCode",
                type: "autocomplete",
                source: opeCodeList,
                strict: true,
                renderer: opeCodeRenderer
            },
            {
                data: "bookingNo",
                strict: true,
                renderer: bookingNoRenderer
            },
            {
                data: "sztp",
                type: "autocomplete",
                source: sizeList,
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
                data: "vslNm",
                type: "autocomplete",
                source: vslNmList,
                strict: true,
                renderer: vslNmRenderer
            },
            {
                data: "voyNo",
                type: "autocomplete",
                source: voyNoList,
                strict: true,
                renderer: voyNoRenderer
            },
            {
                data: "loadingPort",
                type: "autocomplete",
                source: LoadingPortList,
                strict: true,
                renderer: loadingPortRenderer
            },
            {
                data: "dischargePort",
                type: "autocomplete",
                source: DischargePortList,
                strict: true,
                renderer: dischargePortRenderer
            },
            {
                data: "processStatus",
                readOnly: true,
                renderer: processRenderer
            },
            {
                data: "paymentStatus",
                readOnly: true,
                renderer: paymentRenderer
            },
            {
                data: "status",
                readOnly: true,
                renderer: statusRenderer
            },
            {
                data: "remark",
                renderer: remarkRenderer
            },
        ],
        afterRenderer: function (TD, row, column, prop, value, cellProperties) {
            switch (column) {
                case 1:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 10 || $(TD).attr("id").substring(0, 10) != "registerNo")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 2:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 7 || $(TD).attr("id").substring(0, 7) != "opeCode")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 3:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 9 || $(TD).attr("id").substring(0, 9) != "bookingNo")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 4:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 4 || $(TD).attr("id").substring(0, 4) != "size")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 5:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 10 || $(TD).attr("id").substring(0, 10) != "expiredDem")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 6:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 5 || $(TD).attr("id").substring(0, 5) != "vslNm")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 7:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 5 || $(TD).attr("id").substring(0, 5) != "voyNo")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 8:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 11 || $(TD).attr("id").substring(0, 11) != "loadingPort")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 9:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 13 || $(TD).attr("id").substring(0, 13) != "dischargePort")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 10:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 7 || $(TD).attr("id").substring(0, 7) != "process")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 11:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 7 || $(TD).attr("id").substring(0, 7) != "payment")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 12:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 6 || $(TD).attr("id").substring(0, 6) != "status")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 13:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 6 || $(TD).attr("id").substring(0, 6) != "remark")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                default:
                    break;
            }
        },
        afterChange: function (changes, src) {
            //Get data change in cell to render another column
            if (src !== "loadData") {
                var verifyStatus = false;
                var paymentStatus = false;
                var notVerify = false;
                changes.forEach(function interate(row) {
                    if (row[1] == "active" && !isIterate) {
                        getDataSelectedFromTable(false);
                        if (allChecked) {
                            $(".checker").prop("checked", true);
                            checked = true;
                        } else {
                            $(".checker").prop("checked", false);
                            checked = false;
                        }
                        if (shipmentDetails.length > 0) {
                            var status = 1;
                            for (var i = 0; i < shipmentDetails.length; i++) {
                                if (shipmentDetails[i].paymentStatus == "Y") {
                                    if (verifyStatus || notVerify) {
                                        status = 1;
                                    } else {
                                        status = 5;
                                        paymentStatus = true;
                                    }
                                } else if (shipmentDetails[i].userVerifyStatus == "Y") {
                                    if (shipmentDetails[i].processStatus == "Y") {
                                        if (paymentStatus || notVerify) {
                                            status = 1;
                                        } else {
                                            status = 4;
                                            verifyStatus = true;
                                        }
                                    } else {
                                        if (paymentStatus || notVerify) {
                                            status = 1;
                                        } else {
                                            status = 3;
                                            verifyStatus = true;
                                        }
                                    }
                                } else {
                                    if (verifyStatus || paymentStatus) {
                                        status = 1;
                                    } else {
                                        status = 2;
                                    }
                                    notVerify = true;
                                }
                            }
                            switch (status) {
                                case 1:
                                    setLayoutRegisterStatus();
                                    break;
                                case 2:
                                    setLayoutVerifyUser();
                                    break;
                                case 3:
                                    setLayoutVerifyUser();
                                    $("#deleteBtn").prop("disabled", true);
                                    $("#verifyBtn").prop("disabled", true);
                                    break;
                                case 4:
                                    setLayoutPaymentStatus();
                                    break;
                                case 5:
                                    setLayoutFinish();
                                    break;
                            }
                        } else {
                            setLayoutRegisterStatus();
                        }
                    }
                });
            }
        },
        afterSelectionEnd: function (r, c, r2, c2) {
            selectedRow = null;
            if (c == 0 && c2 == 12) {
                selectedRow = r;
            }
        },
        beforeKeyDown: function(e) {
            if (e.keyCode == 8) {
                e.stopImmediatePropagation();
            }
        },
    };
}
configHandson();

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

// LOAD SHIPMENT DETAIL LIST
function loadShipmentDetail(id) {
    $.ajax({
        url: prefix + "/listShipmentDetail",
        method: "GET",
        data: {
            shipmentId: id
        },
        success: function (result) {
            isSaved = false;
            if(result.length > 0 && result[0].id != null) {
                isSaved = true;
            }
            hot.destroy();
            configHandson();
            hot = new Handsontable(dogrid, config);
            hot.loadData(result);
            hot.render();
            setLayoutRegisterStatus();
        }
    });
}

function reloadShipmentDetail() {
    loadShipmentDetail(shipmentSelected.id);
}

// GET CHECKED SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataSelectedFromTable(isValidate) {
    var myTableData = hot.getSourceData();
    var errorFlg = false;
    if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
        hot.alter("remove_row", parseInt(myTableData.length - 1), (keepEmptyRows = false));
    }
    var cleanedGridData = [];
    allChecked = true;
    $.each(myTableData, function (rowKey, object) {
        if (!hot.isEmptyRow(rowKey)) {
            if (object["active"]) {
                cleanedGridData.push(object);
            } else {
                allChecked = false;
            }
        }
    });
    shipmentDetailIds = "";
    shipmentDetails = [];
    $.each(cleanedGridData, function (index, object) {
        var shipmentDetail = new Object();
        shipmentDetail.processStatus = object["processStatus"];
        shipmentDetail.paymentStatus = object["paymentStatus"];
        shipmentDetail.userVerifyStatus = object["userVerifyStatus"];
        shipmentDetail.shipmentId = shipmentSelected.id;
        shipmentDetail.id = object["id"];
        shipmentDetails.push(shipmentDetail);
        shipmentDetailIds += object["id"] + ",";
    });

    // Get result in "selectedList" variable
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
    var myTableData = hot.getSourceData();
    var errorFlg = false;
    if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
        hot.alter("remove_row", parseInt(myTableData.length - 1), (keepEmptyRows = false));
    }
    var cleanedGridData = [];
    $.each(myTableData, function (rowKey, object) {
        if (!hot.isEmptyRow(rowKey)) {
            cleanedGridData.push(object);
        }
    });
    shipmentDetails = [];
    $.each(cleanedGridData, function (index, object) {
        var shipmentDetail = new Object();
        if (isValidate && object["delFlag"] == null) {
            if (object["expiredDem"] == null || object["expiredDem"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập hạn lệnh!");
                errorFlg = true;
            } else if (object["opeCode"] == null || object["opeCode"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn chủ khai thác!");
                errorFlg = true;
            } else if (object["sztp"] == null || object["sztp"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn kích thước!");
                errorFlg = true;
            } else if (object["bookingNo"] == null || object["bookingNo"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập số book!");
                errorFlg = true;
            }
        }
        var expiredDem = new Date(object["expiredDem"].substring(6, 10) + "/" + object["expiredDem"].substring(3, 5) + "/" + object["expiredDem"].substring(0, 2));
        shipmentDetail.sztp = object["sztp"];
        shipmentDetail.opeCode = object["opeCode"];
        shipmentDetail.expiredDem = expiredDem.getTime();
        shipmentDetail.loadingPort = object["loadingPort"];
        shipmentDetail.dischargePort = object["dischargePort"];
        shipmentDetail.remark = object["remark"];
        shipmentDetail.voyNo = object["voyNo"];
        shipmentDetail.vslNm = object["vslNm"];
        shipmentDetail.bookingNo = object["bookingNo"];
        shipmentDetail.registerNo = object["registerNo"];
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

    // Get result in "selectedList" variable
    if (shipmentDetails.length == 0) {
        $.modal.alert("Bạn chưa nhập thông tin.");
        errorFlg = true;
    }

    if (errorFlg) {
        return false;
    } else {
        return true;
    }
}

// EVENT WHEN DELETE SELECTED SHIPMENT DETAIL WITH BACKSPACE BUTTON
document.addEventListener("keyup", function(e){
    if (e.keyCode == 8) {
        if (selectedRow != null && $("#process" + selectedRow).html() != "Đã làm lệnh") {
            var myTableData = hot.getSourceData();
            myTableData[selectedRow].registerNo = '';
            myTableData[selectedRow].containerNo = '';
            myTableData[selectedRow].process = '';
            myTableData[selectedRow].payment = '';
            myTableData[selectedRow].status = '';
            myTableData[selectedRow].opeCode = '';
            myTableData[selectedRow].expiredDem = '';
            myTableData[selectedRow].size = '';
            myTableData[selectedRow].loadingPort = '';
            myTableData[selectedRow].dischargePort = '';
            myTableData[selectedRow].remark = '';
            myTableData[selectedRow].delFlag = true;
            hot.loadData(myTableData);
            $("#registerNo"+selectedRow).html('');
            $("#containerNo"+selectedRow).html('');
            $("#process"+selectedRow).html('');
            $("#payment"+selectedRow).html('');
            $("#status"+selectedRow).html('');
            $("#opeCode"+selectedRow).html('');
            $("#expiredDem"+selectedRow).html('');
            $("#size"+selectedRow).html('');
            $("#loadingPort"+selectedRow).html('');
            $("#dischargePort"+selectedRow).html('');
            $("#remark"+selectedRow).html('');
        }
    }
});

// SAVE/EDIT/DELETE SHIPMENT DETAIL
function saveShipmentDetail() {
    if (shipmentSelected == null) {
        $.modal.msgError("Bạn cần chọn lô trước");
        return;
    } else {
        if (getDataFromTable(true) && shipmentDetails.length > 0 && shipmentDetails.length <= shipmentSelected.containerAmount) {
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
                        $.modal.msgError(result.msg);
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
        }
    }
}

// DELETE SHIPMENT DETAIL
function deleteShipmentDetail() {
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

// TRIGGER CHECK ALL SHIPMENT DETAIL
function checkAll() {
    getDataFromTable(false);
    isIterate = true;
    if (checked) {
        for (var i = 0; i < shipmentDetails.length; i++) {
            hot.setDataAtCell(i, 0, false);
            if (i == shipmentDetails.length - 2) {
                isIterate = false;
            }
        }
        $(".checker").prop("checked", false);
        checked = false;
    } else {
        for (var i = 0; i < shipmentDetails.length; i++) {
            hot.setDataAtCell(i, 0, true);
            if (i == shipmentDetails.length - 2) {
                isIterate = false;
            }
        }
        $(".checker").prop("checked", true);
        checked = true;
    }
}

// Handling logic
function verify() {
    getDataSelectedFromTable(true);
    if (shipmentDetails.length > 0) {
        $.modal.openCustomForm("Xác nhận làm lệnh", prefix + "/checkContListBeforeVerify/" + shipmentDetailIds, 600, 400);
    }
}

function verifyOtp(shipmentDtIds) {
    $.modal.openCustomForm("Xác thực OTP", prefix + "/verifyOtpForm/" + shipmentDtIds, 600, 350);
}

function pay() {
    $.modal.openCustomForm("Thanh toán", prefix + "/paymentForm/" + shipmentDetailIds, 700, 300);
}

function pickTruck() {
    $.modal.openFullPickTruck("Điều xe", prefix + "/pickTruckForm/" + shipmentSelected.id);
}

function exportBill() {

}

// Handling UI STATUS
function setLayoutRegisterStatus() {
    $("#registerStatus").removeClass("label-primary disable").addClass("active");
    $("#verifyStatus").removeClass("label-primary active").addClass("disable");
    $("#paymentStatus").removeClass("label-primary active").addClass("disable");
    $("#finishStatus").removeClass("label-primary active").addClass("disable");
    $("#saveShipmentDetailBtn").prop("disabled", false);
    $("#deleteBtn").prop("disabled", true);
    $("#verifyBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", true);
    if (isSaved) {
        $("#pickTruckBtn").prop("disabled", false);
    } else {
        $("#pickTruckBtn").prop("disabled", true);
    }
    $("#exportBillBtn").prop("disabled", true);
}
function setLayoutVerifyUser() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("label-primary disable").addClass("active");
    $("#paymentStatus").removeClass("label-primary active").addClass("disable");
    $("#finishStatus").removeClass("label-primary active").addClass("disable");
    $("#verifyBtn").prop("disabled", false);
    $("#deleteBtn").prop("disabled", false);
    $("#payBtn").prop("disabled", true);
    $("#exportBillBtn").prop("disabled", true);
}
function setLayoutPaymentStatus() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("active disable").addClass("label-primary");
    $("#paymentStatus").removeClass("label-primary disable").addClass("active");
    $("#finishStatus").removeClass("label-primary active").addClass("disable");
    $("#deleteBtn").prop("disabled", true);
    $("#verifyBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", false);
    $("#exportBillBtn").prop("disabled", true);
}
function setLayoutFinish() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("active disable").addClass("label-primary");
    $("#paymentStatus").removeClass("label-primary disable").addClass("label-primary");
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



