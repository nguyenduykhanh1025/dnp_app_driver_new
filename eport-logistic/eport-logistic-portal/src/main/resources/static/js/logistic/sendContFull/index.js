var isChange = true;
var prefix = ctx + "logistic/sendContFull";
var fromDate = "";
var toDate = "";
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected;
var shipmentDetails;
var shipmentDetailIds;
var originStatus;
var billNo;
var simpleCustom;
var contList = [];
var checked = false;
var allChecked = false;
var isIterate = false;
var selectedRow;
var customStatus;
var rowAmount = 0;
var opeCodeList = ["CMC", "AVS", "QEW", "CNC"];
var vslNmList = ["SDF", "SDA", "EQW", "HABE"];
var voyNoList = ["2342", "3221", "1542", "0235"];
var sizeList = ["20G0", "22G0", "40G0", "45G0"];
var dischargePortList = [
    "VNDAD",
    "CMTVN",
    "CNSHA",
    "HKHKG",
    "KRINC",
    "KRPUS",
    "MYKUA",
    "MYPKG",
    "MYTPP",
    "SGSIN",
    "TCCVN",
    "TWKEL",
    "VNHCM",
    "VNHPH"
];
var cargoTypeList = [
    "AK",
    "BB",
    "BN",
    "DG",
    "DR",
    "DE",
    "FR",
    "GP",
    "MT",
    "RF"
];
var checkList = [];
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

    connectToWebsocketServer();
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
                    // fromDate: fromDate,
                    // toDate: toDate,
                    // voyageNo: $("#voyageNo").val() == null ? "" : $("#voyageNo").val(),
                    // blNo: $("#blNo").val() == null ? "" : $("#blNo").val(),
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
    return '<div class="easyui-tooltip" title="' + ((value != null && value != "") ? value : "không có ghi chú") + '" style="width: 80; text-align: center;"><span>' + (value != null ? (value.substring(0, 5) + "...") : "...") + '</span></div>';
}

// Handle add
$(function () {
    let options = {
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
    let row = $("#dg").datagrid("getSelected");
    if (row) {
        shipmentSelected = row;
        $(function () {
            let options = {
                createUrl: prefix + "/addShipmentForm",
                updateUrl: prefix + "/editShipmentForm/" + shipmentSelected.id,
                modalName: " Lô"
            };
            $.table.init(options);
        });
        $("#loCode").text(row.id);
        $("#taxCode").text(row.taxCode);
        $("#quantity").text(row.containerAmount);
        $("#bookingNo").text(row.bookingNo);
        rowAmount = row.containerAmount;
        checkList = Array(rowAmount).fill(0);
        allChecked = false;
        loadShipmentDetail(row.id);
    }
}

// FORMAT HANDSONTABLE COLUMN
function checkBoxRenderer(instance, td, row, col, prop, value, cellProperties) {
    //Handsontable.renderers.CheckboxRenderer.apply(this, arguments);
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
    let content = '';
        switch (value) {
            case 1:
                content += '<div><i id="verify" class="fa fa-mobile easyui-tooltip" title="Chưa Xác Nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: rgb(5, 148, 148);"></i>';
                content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px;"></i>';
                content += '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chưa Thông Quan" aria-hidden="true" style="margin-left: 8px;"></i>';
                content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Hạ Container" aria-hidden="true" style="margin-left: 8px;"></i></div>';
                break;
            case 2:
                content += '<div><i id="verify" class="fa fa-mobile easyui-tooltip" title="Đã Xác Thực" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
                content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: rgb(5, 148, 148);"></i>';
                content += '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chưa Thông Quan" aria-hidden="true" style="margin-left: 8px;"></i>';
                content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Hạ Container" aria-hidden="true" style="margin-left: 8px;"></i></div>';
                break;
            case 3:
                content += '<div><i id="verify" class="fa fa-mobile easyui-tooltip" title="Chưa Xác Thực" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
                content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                content += '<i id="custom" class="fa fa-shield easyui-tooltip" title="Chưa Thông Quan" aria-hidden="true" style="margin-left: 8px; color: rgb(5, 148, 148);"></i>';
                content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Hạ Container" aria-hidden="true" style="margin-left: 8px;"></i></div>';
                break;
            case 4:
                content += '<div><i id="verify" class="fa fa-mobile easyui-tooltip" title="Chưa Xác Thực" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
                content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                content += '<i id="custom" class="fa fa-shield easyui-tooltip" title="Đã Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Hạ Container" aria-hidden="true" style="margin-left: 8px; color: rgb(5, 148, 148);"></i></div>';
                break;
            case 5:
                content += '<div><i id="verify" class="fa fa-mobile easyui-tooltip" title="Chưa Xác Thực" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
                content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                content += '<i id="custom" class="fa fa-shield easyui-tooltip" title="Đã Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Đã Hạ Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i></div>';
                break;
            default:
                break;
        }
        // if (content == '' && value != null && value != '') {
        //     content += '<div><i id="verify" class="fa fa-mobile easyui-tooltip" title="Chưa Xác Thực" aria-hidden="true" style="margin-left: 8px; font-size: 15px;"></i>';
        //     content += '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px;"></i>';
        //     content += '<i id="custom" class="fa fa-shield easyui-tooltip" title="Đã Thông Quan" aria-hidden="true" style="margin-left: 8px;"></i>';
        //     content += '<i id="finish" class="fa fa-check-square-o easyui-tooltip" title="Chưa Hạ Container" aria-hidden="true" style="margin-left: 8px;"></i></div>';
        // }
        $(td).html(content);
    return td;
}
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'containerNo' + row).addClass("htMiddle");
    $(td).html(value);
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'consignee' + row).addClass("htMiddle");
    $(td).html(value);
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
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
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'vslNm' + row).addClass("htMiddle");
    $(td).html(value);
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
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
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'sztp' + row).addClass("htMiddle");
    $(td).html(value);
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    return td;
}
function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'wgt' + row).addClass("htMiddle");
    $(td).html(value);
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    return td;
}
function cargoTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'cargoType' + row).addClass("htMiddle");
    $(td).html(value);
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'dischargePort' + row).addClass("htMiddle");
    $(td).html(value);
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
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
        minSpareRows: 1,
        rowHeights: 30,
        manualColumnMove: false,
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
                    return '<span>Chủ Hàng</span><span style="color: red;">(*)</span>';
                case 4:
                    return '<span>Hãng Tàu</span><span style="color: red;">(*)</span>';
                case 5:
                    return '<span>Tàu</span><span style="color: red;">(*)</span>';
                case 6:
                    return '<span>Chuyến</span><span style="color: red;">(*)</span>';
                case 7:
                    return '<span>Kích Thước</span><span style="color: red;">(*)</span>';
                case 8:
                    return '<span>Trọng Lượng</span><span style="color: red;">(*)</span>';
                case 9:
                    return '<span>Loại Hàng</span><span style="color: red;">(*)</span>';
                case 10:
                    return '<span>Cảng Dỡ Hàng</span><span style="color: red;">(*)</span>';
                case 11:
                    return "Ghi Chú";
            }
        },
        colWidths: [50, 110, 100, 100, 100, 100, 100, 100, 100, 150, 150, 200],
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
                validator: Handsontable.validators.NumericValidator
            },
            {
                data: "consignee",
                strict: true,
                renderer: consigneeRenderer
              },
            {
                data: "opeCode",
                type: "autocomplete",
                source: opeCodeList,
                strict: true,
                renderer: opeCodeRenderer
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
                data: "cargoType",
                strict: true,
                type: "autocomplete",
                source: cargoTypeList,
                renderer: cargoTypeRenderer
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
        // afterChange: function (changes, src) {
        //     //Get data change in cell to render another column
        //     if (src !== "loadData") {
        //         let verifyStatus = false;
        //         let paymentStatus = false;
        //         let makeOrder = false;
        //         let notVerify = false;
        //         changes.forEach(function interate(row) {
        //             if (row[1] == "active" && !isIterate) {
        //                 getDataSelectedFromTable(false, false);
        //                 if (allChecked) {
        //                     //$(".checker").prop("checked", true);
        //                     checked = true;
        //                 } else {
        //                     //$(".checker").prop("checked", false);
        //                     checked = false;
        //                 }
        //                 if (shipmentDetails.length > 0) {
        //                     let status = 1;
        //                     for (let i = 0; i < shipmentDetails.length; i++) {
        //                         switch (shipmentDetails[i].status) {
        //                             case 1:
        //                                 status = 1;
        //                                 break;
        //                             case 2:
        //                                 if (shipmentDetails[i].userVerifyStatus == "Y") {
        //                                     if (notVerify || makeOrder || paymentStatus) {
        //                                         status = 1;
        //                                     } else {
        //                                         verifyStatus = true;
        //                                         status = 3;
        //                                     }
        //                                 } else {
        //                                     if (verifyStatus || makeOrder || paymentStatus) {
        //                                         status = 1;
        //                                     } else {
        //                                         status = 2;
        //                                         notVerify = true;
        //                                     }
        //                                 }
        //                                 break;
        //                             case 3:
        //                                 if (verifyStatus || notVerify || paymentStatus) {
        //                                     status = 1;
        //                                 } else {
        //                                     status = 4;
        //                                     makeOrder = true;
        //                                 }
        //                                 break;
        //                             case 4:
        //                                 if (verifyStatus || notVerify || makeOrder) {
        //                                     status = 1;
        //                                 } else {
        //                                     status = 5;
        //                                     paymentStatus = true;
        //                                 }
        //                                 break;
        //                         }
        //                     }
        //                     switch (status) {
        //                         case 1:
        //                             setLayoutCustomStatus(simpleCustom);
        //                             if (!paymentStatus && !notVerify && !verifyStatus) {
        //                                 $("#deleteBtn").prop("disabled", false);
        //                             }
        //                             break;
        //                         case 2:
        //                             setLayoutVerifyUserStatus();
        //                             break;
        //                         case 3:
        //                             setLayoutVerifyUserStatus();
        //                             $("#verifyBtn").prop("disabled", true);
        //                             break;
        //                         case 4:
        //                             setLayoutPaymentStatus();
        //                             break;
        //                         case 5:
        //                             setLayoutFinishStatus();
        //                             break;
        //                     }
        //                 } else {
        //                     setLayoutCustomStatus();
        //                 }
        //             }
        //         });
        //     }
        // },
        // afterSelectionEnd: function (r, c, r2, c2) {
        //     selectedRow = null;
        //     if (c == 0 && c2 == 12) {
        //         selectedRow = r;
        //     }
        // },
        // beforeKeyDown: function (e) {
        //     if (e.keyCode == 8) {
        //         e.stopImmediatePropagation();
        //     }
        // },
    };
}
configHandson();

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

// TRIGGER CHECK ALL SHIPMENT DETAIL
function checkAll() {
    if (!allChecked) {
        allChecked = true
        checkList = Array(rowAmount).fill(1);
        for (let i=0; i<checkList.length; i++) {
            if (hot.getDataAtCell(i, 1) == null) {
                break;
            }
            $('#check'+i).prop('checked', true);
        }
    } else {
        allChecked = false;
        checkList = Array(rowAmount).fill(0);
        for (let i=0; i<checkList.length; i++) {
            if (hot.getDataAtCell(i, 1) == null) {
                break;
            }
            $('#check'+i).prop('checked', false);
        }
    }
}
function check(id) {
    if ($('#check'+id).prop('checked')) {
        checkList[id] = 1;
    } else {
        checkList[id] = 0;
    }
    let disposable = null;
    let status = 1;
    let diff = false;
    allChecked = true;
    for (let i=0; i<checkList.length; i++) {
        let cellStatus = hot.getDataAtCell(i, 1);
        if (checkList[i] == 1 && cellStatus != null) {
            if (disposable == null) {
                disposable = true;
            }
            if (cellStatus > 2) {
                disposable = false;
            }
            if (status != 1 && status != cellStatus) {
                diff = true;
            } else {
                status = cellStatus;
            }
        } else if (cellStatus != null) {
            allChecked = false;
            $('.checker').prop('checked', false);
        }
    }
    if (allChecked) {
        $('.checker').prop('checked', true);
    }
    if (disposable != null && disposable) {
        $("#deleteBtn").prop("disabled", false);
    } else {
        $("#deleteBtn").prop("disabled", true);
    }
    if (diff) {
        status = 1;
    }
    switch (status) {
        case 1:
            setLayoutRegisterStatus();
            break;
        case 2:
            setLayoutVerifyUserStatus();
            break;
        case 3:
            setLayoutPaymentStatus();
            break;
        case 4:
            setLayoutCustomStatus();
            break;
        case 5:
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
                let sourceData = data.shipmentDetails;
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
    loadShipmentDetail(shipmentSelected.id);
}

// GET CHECKED SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataSelectedFromTable(isValidate, isNeedPickedCont) {
    let myTableData = hot.getSourceData();
    let errorFlg = false;
    if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
        hot.alter("remove_row", parseInt(myTableData.length - 1), (keepEmptyRows = false));
    }
    let cleanedGridData = [];
    for (let i=0; i<checkList.length; i++) {
        if (checkList[i] == 1) {
            cleanedGridData.push(myTableData[i]);
        }
    }
    shipmentDetailIds = "";
    shipmentDetails = [];
    $.each(cleanedGridData, function (index, object) {
        let shipmentDetail = new Object();
        if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"]) && isValidate) {
            $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
            errorFlg = true;
        }
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
        shipmentDetailIds += object["id"] + ",";
    });

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
    if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
        hot.alter("remove_row", parseInt(myTableData.length - 1), (keepEmptyRows = false));
    }
    let cleanedGridData = [];
    $.each(myTableData, function (rowKey, object) {
        if (!hot.isEmptyRow(rowKey)) {
            cleanedGridData.push(object);
        }
    });
    shipmentDetails = [];
    if (cleanedGridData.length > 0) {
        billNo = cleanedGridData[0]["blNo"];
    }
    contList = [];
    $.each(cleanedGridData, function (index, object) {
        let shipmentDetail = new Object();
        if (isValidate && object["delFlag"] == null) {
            if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"])) {
                $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
                errorFlg = true;
                return false;
            } else if (object["consignee"] == null || object["consignee"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn chủ hàng!");
                errorFlg = true;
                return false;
            } else if (object["opeCode"] == null || object["opeCode"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn hãng tàu!");
                errorFlg = true;
                return false;
            } else if (object["vslNm"] == null || object["vslNm"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn tàu!");
                errorFlg = true;
                return false;
            } else if (object["voyNo"] == null || object["voyNo"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn chuyến!");
                errorFlg = true;
                return false;
            } else if (object["sztp"] == null || object["sztp"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn kích thước!");
                errorFlg = true;
                return false;
            } else if (object["wgt"] == null || object["wgt"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn trọng tải!");
                errorFlg = true;
                return false;
            } else if (object["dischargePort"] == null || object["dischargePort"] == "") {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn cảng dỡ hàng!");
                errorFlg = true;
                return false;
            } 
        }
        shipmentDetail.bookingNo = shipmentSelected.bookingNo;
        shipmentDetail.containerNo = object["containerNo"];
        contList.push(object["containerNo"]);
        shipmentDetail.opeCode = object["opeCode"];
        shipmentDetail.sztp = object["sztp"];
        shipmentDetail.consignee = object["consignee"];
        shipmentDetail.wgt = object["wgt"];
        shipmentDetail.vslNm = object["vslNm"];
        shipmentDetail.voyNo = object["voyNo"];
        shipmentDetail.dischargePort = object["dischargePort"];
        shipmentDetail.transportType = object["transportType"];
        shipmentDetail.cargoType = object["cargoType"];
        shipmentDetail.remark = object["remark"];
        shipmentDetail.shipmentId = shipmentSelected.id;
        shipmentDetail.id = object["id"];
        shipmentDetails.push(shipmentDetail);
    });

    if (isValidate) {
        contList.sort();
        let contTemp = "";
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

// Handling logic
function verify() {
    getDataSelectedFromTable(true, true);
    if (shipmentDetails.length > 0) {
        $.modal.openCustomForm("Xác nhận làm lệnh", prefix + "/checkContListBeforeVerify/" + shipmentDetailIds, 600, 500);
    }
}

function verifyOtp(shipmentDtIds) {
    $.modal.openCustomForm("Xác thực OTP", prefix + "/verifyOtpForm/" + shipmentDtIds, 600, 350);
}

function pay() {
    $.modal.openCustomForm("Thanh toán", prefix + "/paymentForm/" + shipmentDetailIds, 700, 300);
}

function checkCustomStatus() {
    $.modal.openCustomForm("Khai báo hải quan", prefix + "/checkCustomStatusForm/" + shipmentSelected.id, 720, 500);
}

function exportBill() {

}

// Handling UI STATUS
function setLayoutRegisterStatus() {
    $("#registerStatus").removeClass("label-primary disable").addClass("active");
    $("#verifyStatus").removeClass("label-primary active").addClass("disable");
    $("#paymentStatus").removeClass("label-primary active").addClass("disable");
    $("#customStatus").removeClass("label-primary active").addClass("disable");
    $("#finishStatus").removeClass("label-primary active").addClass("disable");
    $("#verifyBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", true);
    $("#customBtn").prop("disabled", true);
    $("#exportBillBtn").prop("disabled", true);
}

function setLayoutVerifyUserStatus() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("label-primary disable").addClass("active");
    $("#paymentStatus").removeClass("label-primary active").addClass("disable");
    $("#customStatus").removeClass("active disable").addClass("disable");
    $("#finishStatus").removeClass("label-primary active").addClass("disable");
    $("#verifyBtn").prop("disabled", false);
    $("#payBtn").prop("disabled", true);
    $("#customBtn").prop("disabled", true);
    $("#exportBillBtn").prop("disabled", true);
}

function setLayoutPaymentStatus() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("active disable").addClass("label-primary");
    $("#paymentStatus").removeClass("label-primary disable").addClass("active");
    $("#customStatus").removeClass("active disable").addClass("disable");
    $("#finishStatus").removeClass("label-primary active").addClass("disable");
    $("#deleteBtn").prop("disabled", true);
    $("#verifyBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", false);
    $("#customBtn").prop("disabled", true);
    $("#exportBillBtn").prop("disabled", true);
}

function setLayoutCustomStatus() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("label-primary active").addClass("label-primary");
    $("#paymentStatus").removeClass("label-primary active").addClass("label-primary");
    $("#customStatus").removeClass("label-primary disable").addClass("active");
    $("#finishStatus").removeClass("label-primary active").addClass("disable");
    $("#verifyBtn").prop("disabled", true);
    $("#deleteBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", true);
    $("#customBtn").prop("disabled", false);
    $("#exportBillBtn").prop("disabled", false);
}

function setLayoutFinishStatus() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("active disable").addClass("label-primary");
    $("#paymentStatus").removeClass("active disable").addClass("label-primary");
    $("#customStatus").removeClass("active disable").addClass("label-primary");
    $("#finishStatus").removeClass("label-primary disable").addClass("active");
    $("#deleteBtn").prop("disabled", true);
    $("#verifyBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", true);
    $("#customBtn").prop("disabled", true);
    $("#exportBillBtn").prop("disabled", false);
}

function finishForm(result) {
    if (result.code == 0 || result.code == 301){
        $.modal.loading("Đang xử lý, vui lòng chờ..");
        let processId = result.processId;
        $.websocket.subscribe('/eportTopic/' + processId + '/response', onMessageReceived);
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
}

function onError(error) {
    console.error('Could not connect to WebSocket server. Please refresh this page to try again!');
}
  
