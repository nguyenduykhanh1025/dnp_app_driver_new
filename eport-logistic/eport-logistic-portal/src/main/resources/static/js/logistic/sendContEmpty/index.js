var isChange = true;
var prefix = ctx + "logistic/sendContEmpty";
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected;
var shipmentDetails;
var shipmentDetailIds;
var contList = [];
var checked = false;
var allChecked = true;
var isIterate = false;
var isSaved = false;
var opeCodeList = ["CMC", "AVS", "QEW"];
var vslNmList = ["SDF", "SDA", "EQW"];
var voyNoList = ["2342", "3221", "1542"];
var sizeList = ["20G0", "22G0", "40G0", "45G0"];
var dischargePortList = [
    "VNDAD:Da Nang",
    "CMTVN:CAI MEP",
    "CNSHA:Shanghai",
    "HKHKG:Hong Kong",
    "KRINC:Inchon",
    "KRPUS:Pusan",
    "MYKUA:Kuantan",
    "MYPKG:Port Kelang",
    "MYTPP:Tanjong Pelepas",
    "SGSIN:Singapore",
    "TCCVN:TCCVN",
    "TWKEL:Keelung",
    "VNHCM:Ho Chin Minh",
    "VNHPH:Haiphong"];
var selectedRow = null;
var rowAmount = 0;
var sourceData;
// GET RESOURCE DATA LIST
// $.ajax({
//     url: prefix + "/getFieldList",
//     method: "get",
//     success: function (data) {
//         if (data.code == 0) {
//             opeCodeList = data.opeCodeList;
//             vslNmList = data.vslNmList;
//             voyNoList = data.voyNoList;
//         }
//     },
//     error: function (result) {
//         console.log("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
//     },
// });

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
        rowAmount = row.containerAmount;
        loadShipmentDetail(row.id);
    }
}

// FORMAT HANDSONTABLE COLUMN
function statusIconsRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        let content = '';
        switch (value) {
            case 1:
                if (sourceData[row].userVerifyStatus == "Y") {
                    content += '<i id="verify" class="fa fa-mobile easyui-tooltip" title="Đã Xác Thực" aria-hidden="true" style="margin-left: 8px; color: #3498db; font-size: 15px;"></i>';
                    content += '<i id="makeOrder" class="fa fa-keyboard-o easyui-tooltip" title="Chưa Làm Lệnh" aria-hidden="true" style="margin-left: 8px;  color: rgb(5, 148, 148);"></i>';
                } else {
                    content += '<i id="verify" class="fa fa-mobile easyui-tooltip" title="Chưa Xác Thực" aria-hidden="true" style="margin-left: 8px; color: rgb(5, 148, 148); font-size: 15px;"></i>';
                    content += '<i id="makeOrder" class="fa fa-keyboard-o easyui-tooltip" title="Chưa Làm Lệnh" aria-hidden="true" style="margin-left: 8px;"></i>';
                }
                content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px;"></i>';
                content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Hạ Container" aria-hidden="true" style="margin-left: 8px;"></i></div>';
                break;
            case 2:
                content += '<i id="verify" class="fa fa-mobile easyui-tooltip" title="Đã Xác Thực" aria-hidden="true" style="margin-left: 8px; color: #3498db; font-size: 15px;"></i>';
                content += '<i id="makeOrder" class="fa fa-keyboard-o easyui-tooltip" title="Đã Làm Lệnh" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: rgb(5, 148, 148);"></i>';
                content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Hạ Container" aria-hidden="true" style="margin-left: 8px;"></i></div>';
                break;
            case 3:
                content += '<i id="verify" class="fa fa-mobile easyui-tooltip" title="Đã Xác Thực" aria-hidden="true" style="margin-left: 8px; color: #3498db; font-size: 15px;"></i>';
                content += '<i id="makeOrder" class="fa fa-keyboard-o easyui-tooltip" title="Đã Làm Lệnh" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Hạ Container" aria-hidden="true" style="margin-left: 8px; color: rgb(5, 148, 148);"></i></div>';
                break;
            case 4:
                content += '<i id="verify" class="fa fa-mobile easyui-tooltip" title="Đã Xác Thực" aria-hidden="true" style="margin-left: 8px; color: #3498db; font-size: 15px;"></i>';
                content += '<i id="makeOrder" class="fa fa-keyboard-o easyui-tooltip" title="Đã Làm Lệnh" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Đã Hạ Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i></div>';
                break;
            default:
                break;
        }
        $(td).html(content).addClass("htMiddle");
    }
    return td;
}
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'containerNo' + row).html(value).addClass("htMiddle");
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    } else {
        $(td).html('');
    }
    return td;
}
function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'opeCode' + row).html(value).addClass("htMiddle");
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
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
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    } else {
        $(td).html('');
    }
    return td;
}
function voyNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'voyNo' + row).html(value).addClass("htMiddle");
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    } else {
        $(td).html('');
    }
    return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'sztp' + row).html(value).addClass("htMiddle");
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    } else {
        $(td).html('');
    }
    return td;
}
function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'wgt' + row).html(value).addClass("htMiddle");
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    } else {
        $(td).html('');
    }
    return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
    if (value != null && value != '') {
        $(td).attr('id', 'dischargePort' + row).html(value).addClass("htMiddle");
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
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

// CONFIGURATE HANDSONTABLE NOT EDO
function configHandson() {
    config = {
        stretchH: "all",
        height: document.documentElement.clientHeight - 100,
        minRows: rowAmount,
        maxRows: rowAmount,
        width: "100%",
        minSpareRows: 0,
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
                    return "Trạng Thái";
                case 2:
                    return '<span>Container No</span><span style="color: red;">(*)</span>';
                case 3:
                    return '<span>Chủ Khai Thác</span><span style="color: red;">(*)</span>';
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
                renderer: wgtRenderer
            },
            {
                data: "dischargePort",
                type: "autocomplete",
                source: dischargePortList,
                strict: true,
                renderer: dischargePortRenderer
            },
            {
                data: "remark",
                renderer: remarkRenderer
            },
        ],
        afterRenderer: function (TD, row, column, prop, value, cellProperties) {
            switch (column) {
                case 1:
                    if ($(TD).attr("id") != null) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 2:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 11 || $(TD).attr("id").substring(0, 11) != "containerNo")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 3:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 7 || $(TD).attr("id").substring(0, 7) != "opeCode")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 4:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 10 || $(TD).attr("id").substring(0, 10) != "expiredDem")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 5:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 5 || $(TD).attr("id").substring(0, 5) != "vslNm")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 6:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 5 || $(TD).attr("id").substring(0, 5) != "voyNo")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 7:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 4 || $(TD).attr("id").substring(0, 4) != "sztp")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 8:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 3 || $(TD).attr("id").substring(0, 3) != "wgt")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 9:
                    if (value != '' && $(TD).attr("id") != null && ($(TD).attr("id").length <= 13 || $(TD).attr("id").substring(0, 13) != "dischargePort")) {
                        hot.setDataAtCell(row, column, '');
                    }
                    break;
                case 10:
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
                            if (shipmentDetails[0].status != null) {
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
        success: function (data) {
            if (data.code == 0) {
                sourceData = data.shipmentDetails;
                if (rowAmount < sourceData.length) {
                    sourceData = sourceData.slice(0, rowAmount);
                }
                setLayoutRegisterStatus();
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
        shipmentDetail.userVerifyStatus = object["userVerifyStatus"];
        shipmentDetail.status = object["status"];
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
        shipmentDetail.opeCode = object["opeCode"];
        shipmentDetail.expiredDem = expiredDem.getTime();
        shipmentDetail.vslNm = object["vslNm"];
        shipmentDetail.voyNo = object["voyNo"];
        shipmentDetail.sztp = object["sztp"];
        shipmentDetail.wgt = object["wgt"];
        shipmentDetail.dischargePort = object["dischargePort"];
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
            myTableData[selectedRow].containerNo = '';
            myTableData[selectedRow].status = '';
            myTableData[selectedRow].opeCode = '';
            myTableData[selectedRow].expiredDem = '';
            myTableData[selectedRow].vslNm = '';
            myTableData[selectedRow].voyNo = '';
            myTableData[selectedRow].sztp = '';
            myTableData[selectedRow].wgt = '';
            myTableData[selectedRow].dischargePort = '';
            myTableData[selectedRow].remark = '';
            myTableData[selectedRow].delFlag = true;
            hot.loadData(myTableData);
            $("#containerNo"+selectedRow).html('');
            $("#status"+selectedRow).html('');
            $("#opeCode"+selectedRow).html('');
            $("#expiredDem"+selectedRow).html('');
            $("#vslNm"+selectedRow).html('');
            $("#voyNo"+selectedRow).html('');
            $("#sztp"+selectedRow).html('');
            $("#wgt"+selectedRow).html('');
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
        if (getDataFromTable(true)) {
            if (shipmentDetails.length > 0 && shipmentDetails.length <= shipmentSelected.containerAmount) {
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
            } else {
                $.modal.alertError("Quý khách chưa nhập thông tin chi tiết lô.");
            }
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
    $("#deleteBtn").prop("disabled", true);
    $("#saveShipmentDetailBtn").prop("disabled", false);
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
    $("#verifyBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", false);
    $("#exportBillBtn").prop("disabled", true);
}
function setLayoutFinish() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("active disable").addClass("label-primary");
    $("#paymentStatus").removeClass("label-primary disable").addClass("label-primary");
    $("#finishStatus").removeClass("label-primary disable").addClass("active");
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



