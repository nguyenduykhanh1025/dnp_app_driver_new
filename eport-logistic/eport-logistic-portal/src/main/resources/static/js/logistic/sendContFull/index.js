var isChange = true;
var prefix = ctx + "logistic/sendContFull";
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected;
var shipmentDetails;
var shipmentDetailIds;
var checked = false;
var allChecked = true;
var isIterate = false;
var isSaved = false;
var contList = [];
var opeCodeList = ["MCM", "NIC", "BMW"];
var sizeList = ["22G0", "23G0"];
var vslNmList = ["Vessel 1", "Vessel 2", "Vessel 3"];
var voyNoList = ["Voyage 1", "Voyage 2", "Voyage 3"];
var LoadingPortList = ["Cảng nguồn 1", "Cảng nguồn 2"];
var DischargePortList = ["Cảng đích 1", "Cảng đích 2"];
var transportTypeList = ["Truck 1", "Truck 1"];
var selectedRow = null;

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
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'containerNo' + row).html(value).addClass("htMiddle");
    } else {
        $(td).html('');
    }
    return td;
}
function sealNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'sealNo' + row).html(value).addClass("htMiddle");
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
function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'wgt' + row).html(value).addClass("htMiddle");
    } else {
        $(td).html('');
    }
    return td;
}
function vgmPersonInfoRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'vgmPersonInfo' + row).html(value).addClass("htMiddle");
    } else {
        $(td).html('');
    }
    return td;
}
function vgmRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'vgm' + row).html(value).addClass("htMiddle");
    } else {
        $(td).html('');
    }
    return td;
}
function transportTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'transportType' + row).html(value).addClass("htMiddle");
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
config = {
    stretchH: "all",
    height: document.documentElement.clientHeight - 100,
    minRows: 100,
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
                return "T.T Làm Lệnh";
            case 3:
                return "T.T Thanh Toán";
            case 4:
                return "T.T Nhận Cont";
            case 5:
                return '<span>Hãng Tàu/Đại Lý</span><span style="color: red;">(*)</span>';
            case 6:
                return '<span>Số Book</span><span style="color: red;">(*)</span>';
            case 7:
                return '<span>Tàu</span><span style="color: red;">(*)</span>';
            case 8:
                return '<span>Chuyến</span><span style="color: red;">(*)</span>';
            case 9:
                return '<span>Cảng Ch.Tải</span><span style="color: red;">(*)</span>';
            case 10:
                return 'Cảng Đích';
            case 11:
                return '<span>Số Cont</span><span style="color: red;">(*)</span>';
            case 12:
                return "Số Seal";
            case 13:
                return '<span>Kích Cỡ</span><span style="color: red;">(*)</span>';
            case 14:
                return '<span>Trọng Lượng</span><span style="color: red;">(*)</span>';
            case 15:
                return "VGM";
            case 16:
                return "Đơn Vị Kiểm Định";
            case 17:
                return "Max Gross(Tấn)";
            case 18:
                return '<span>Phương Tiện</span><span style="color: red;">(*)</span>';
            case 19:
                return "Ghi Chú";
        }
    },
    colWidths: [50, 100, 150, 150, 150, 130, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 120, 100, 100, 200],
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
            data: "containerNo",
            strict: true,
            renderer: containerNoRenderer
        },
        {
            data: "sealNo",
            strict: true,
            renderer: sealNoRenderer
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
            strict: true,
            renderer: wgtRenderer
        },
        {
            data: "vgmChk",
            type: "checkbox",
            className: "htCenter",
        },
        {
            data: "vgmPersonInfo",
            strict: true,
            renderer: vgmPersonInfoRenderer
        },
        {
            data: "vgm",
            strict: true,
            renderer: vgmRenderer
        },
        {
            data: "transportType",
            type: "autocomplete",
            source: transportTypeList,
            strict: true,
            renderer: transportTypeRenderer
        },
        {
            data: "remark",
            renderer: remarkRenderer
        },
    ],
    afterRenderer: function (TD, row, column, prop, value, cellProperties) {
        if (row == 2) {
            switch (column) {
                case 1:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 10 || $(TD).attr("id").substring(0, 10) != "registerNo")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 2:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 7 || $(TD).attr("id").substring(0, 7) != "process")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 3:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 7 || $(TD).attr("id").substring(0, 7) != "payment")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 4:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 6 || $(TD).attr("id").substring(0, 6) != "status")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 5:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 7 || $(TD).attr("id").substring(0, 7) != "opeCode")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 6:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 9 || $(TD).attr("id").substring(0, 9) != "bookingNo")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 7:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 5 || $(TD).attr("id").substring(0, 5) != "vslNm")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 8:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 5 || $(TD).attr("id").substring(0, 5) != "voyNo")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 9:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 11 || $(TD).attr("id").substring(0, 11) != "loadingPort")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 10:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 13 || $(TD).attr("id").substring(0, 13) != "dischargePort")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 11:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 11 || $(TD).attr("id").substring(0, 11) != "containerNo")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 12:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 6 || $(TD).attr("id").substring(0, 6) != "sealNo")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 13:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 4 || $(TD).attr("id").substring(0, 4) != "size")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 14:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 3 || $(TD).attr("id").substring(0, 3) != "wgt")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 16:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 13 || $(TD).attr("id").substring(0, 13) != "vgmPersonInfo")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 17:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 3 || $(TD).attr("id").substring(0, 3) != "vgm")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 18:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 13 || $(TD).attr("id").substring(0, 13) != "transportType")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 19:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 6 || $(TD).attr("id").substring(0, 6) != "remark")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                default:
                    break;
            }
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
                                    status = 4;
                                    paymentStatus = true;
                                }
                            } else if (shipmentDetails[i].processStatus == "Y") {
                                if (paymentStatus || notVerify) {
                                    status = 1;
                                } else {
                                    status = 3;
                                    verifyStatus = true;
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
                                setLayoutPaymentStatus();
                                break;
                            case 4:
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
        if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"]) && isValidate) {
            $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
            errorFlg = true;
        }
        shipmentDetail.containerNo = object["containerNo"];
        shipmentDetail.processStatus = object["processStatus"];
        shipmentDetail.paymentStatus = object["paymentStatus"];
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
    contList = [];
    $.each(cleanedGridData, function (index, object) {
        var shipmentDetail = new Object();
        if (isValidate && object["delFlag"] == null) {
            if(object["containerNo"] == null || object["containerNo"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập số container!");
                errorFlg = true;
            } else if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"])) {
                $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
                errorFlg = true;
            } else if (object["opeCode"] == null || object["opeCode"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn hãng tàu/chủ đại lý!");
                errorFlg = true;
            } else if (object["sztp"] == null || object["sztp"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn kích thước!");
                errorFlg = true;
            } else if (object["bookingNo"] == null || object["bookingNo"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập số book!");
                errorFlg = true;
            } else if (object["vslNm"] == null || object["vslNm"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập tàu!");
                errorFlg = true;
            } else if (object["voyNo"] == null || object["voyNo"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập chuyến!");
                errorFlg = true;
            } else if (object["loadingPort"] == null || object["loadingPort"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập cảng chuyển<br>tải!");
                errorFlg = true;
            } else if (object["wgt"] == null || object["wgt"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập trọng lượng!");
                errorFlg = true;
            } else if (object["vgmChk"]) {
                if (object["vgmPersonInfo"] == null || object["vgmPersonInfo"] == "") {
                    $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập đơn vị kiểm định!");
                    errorFlg = true;
                } else if (object["vgm"] == null || object["vgm"] == "") {
                    $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập Max Gross!");
                    errorFlg = true;
                }
            } else if (object["transportType"] == null || object["transportType"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập phương tiện!");
                errorFlg = true;
            }
        }
        shipmentDetail.containerNo = object["containerNo"];
        shipmentDetail.sztp = object["sztp"];
        shipmentDetail.opeCode = object["opeCode"];
        shipmentDetail.loadingPort = object["loadingPort"];
        shipmentDetail.dischargePort = object["dischargePort"];
        shipmentDetail.remark = object["remark"];
        shipmentDetail.voyNo = object["voyNo"];
        shipmentDetail.vslNm = object["vslNm"];
        shipmentDetail.bookingNo = object["bookingNo"];
        shipmentDetail.registerNo = object["registerNo"];
        shipmentDetail.sealNo = object["sealNo"];
        shipmentDetail.wgt = object["wgt"];
        shipmentDetail.vgmChk = object["vgmChk"];
        shipmentDetail.vgmPersonInfo = object["vgmPersonInfo"];
        shipmentDetail.vgm = object["vgm"];
        shipmentDetail.transportType = object["transportType"];
        shipmentDetail.shipmentId = shipmentSelected.id;
        shipmentDetail.id = object["id"];
        shipmentDetails.push(shipmentDetail);
    });

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
    if (shipmentDetails.length == 0) {
        $.modal.alert("Quý khách chưa nhập thông tin.");
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



