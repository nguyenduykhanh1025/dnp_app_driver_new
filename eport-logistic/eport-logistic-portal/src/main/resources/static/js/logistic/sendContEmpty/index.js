var prefix = ctx + "logistic/send-cont-empty";
var interval, currentPercent, timeout;
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected, shipmentDetails, shipmentDetailIds, sourceData, processOrderIds;
var contList = [];
var conts = '';
var allChecked = false;
var checkList = [];
var rowAmount = 0;
var shipmentSearch = new Object;
shipmentSearch.serviceType = 2;
var sizeList = [];
//dictionary sizeList
$.ajax({
	  type: "GET",
	  url: ctx + "logistic/size/container/list",
	  success(data) {
		  if(data.code == 0){
		      data.data.forEach(element => {
		    	  sizeList.push(element['dictLabel'])
		      })
		  }
	  }
})
var consigneeList, opeCodeList, vslNmList, currentProcessId, currentSubscription;

$.ajax({
    url: ctx + "logistic/source/option",
    method: "GET",
    success: function (data) {
        if (data.code == 0) {
            opeCodeList = data.opeCodeList;
            vslNmList = data.vslNmList;
            consigneeList = data.consigneeList;
        }
    }
});
var cargoTypeList = ["AK:Over Dimension", "BB:Break Bulk", "BN:Bundle", "DG:Dangerous", "DR:Reefer & DG", "DE:Dangerous Empty", "FR:Fragile", "GP:General", "MT:Empty", "RF:Reefer"];

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
    //DEFAULT SEARCH FOLLOW DATE
    let fromMonth = (new Date().getMonth() < 10) ? "0" + (new Date().getMonth()) : new Date().getMonth();
    let toMonth = (new Date().getMonth() +2 < 10) ? "0" + (new Date().getMonth() +2 ): new Date().getMonth() +2;
    $('#fromDate').val("01/"+ fromMonth + "/" + new Date().getFullYear());
    $('#toDate').val("01/"+ (toMonth > 12 ? "01" +"/"+ (new Date().getFullYear()+1)  : toMonth + "/" + new Date().getFullYear()));
    let fromDate = stringToDate($('#fromDate').val());
    let toDate =  stringToDate($('#toDate').val());
    fromDate.setHours(0,0,0);
    toDate.setHours(23, 59, 59);
    shipmentSearch.fromDate = fromDate.getTime();
    shipmentSearch.toDate = toDate.getTime();

    loadTable();
    $(".left-side").css("height", $(document).height());
    $("#btn-collapse").click(function () {
        handleCollapse(true);
    });
    $("#btn-uncollapse").click(function () {
        handleCollapse(false);
    });
    //find date
    $('.from-date').datetimepicker({
        language: 'en',
        format: 'dd/mm/yyyy',
        autoclose: true,
        todayBtn: true,
        todayHighlight: true,
        pickTime: false,
        minView: 2
    });
    $('.to-date').datetimepicker({
        language: 'en',
        format: 'dd/mm/yyyy',
        autoclose: true,
        todayBtn: true,
        todayHighlight: true,
        pickTime: false,
        minView: 2
    });
    // Handle add
    $(function () {
        let options = {
            createUrl: prefix + "/shipment/add",
            updateUrl: "0",
            modalName: " Lô"
        };
        $.table.init(options);
    });
});
//search date
function changeFromDate() {
    let fromDate = stringToDate($('#fromDate').val());
    if ($('#toDate').val() != '' && stringToDate($('#toDate').val()).getTime() < fromDate.getTime()) {
        $.modal.alertError('Quý khách không thể chọn từ ngày cao hơn đến ngày.')
        $('#fromDate').val('');
    } else {
        shipmentSearch.fromDate = fromDate.getTime();
        loadTable();
    }
}
  
function changeToDate() {
    let toDate = stringToDate($('.to-date').val());
    if ($('.from-date').val() != '' && stringToDate($('.from-date').val()).getTime() > toDate.getTime()) {
        $.modal.alertError('Quý khách không thể chọn đến ngày thấp hơn từ ngày.')
        $('.to-date').val('');
    } else {
        toDate.setHours(23, 59, 59);
        shipmentSearch.toDate = toDate.getTime();
        loadTable();
    }
}
  
function stringToDate(dateStr) {
    let dateParts = dateStr.split('/');
    return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}
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
        url: ctx + 'logistic/shipments',
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
    return '<div class="easyui-tooltip" title="' + (value != null ? value : "không có ghi chú") + '" style="width: 80; text-align: center;"><span>' + (value != null ? value : "") + '</span></div>';
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
                createUrl: prefix + "/shipment/add",
                updateUrl: prefix + "/shipment/" + shipmentSelected.id,
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
    if (sourceData[row] && sourceData[row].processStatus && sourceData[row].paymentStatus && sourceData[row].finishStatus) {
        // Command process status
        let process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Chưa xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';
        switch (sourceData[row].processStatus) {
            case 'E':
                process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ kết quả" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
                break;
            case 'Y':
                process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đã làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
                break;
            case 'N':
                process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Có thể làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
                break;
        }
        // Payment status
        let payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
        switch (sourceData[row].paymentStatus) {
            case 'E':
                payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Lỗi Thanh Toán" aria-hidden="true" style="margin-left: 8px; color : #ed5565;"></i>';
                break;
            case 'Y':
                payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
                break;
            case 'N':
                if(value > 1) {
                    payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chờ Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                }
                break;
        }
        // released status
        let released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Chưa thể nhận container" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
        switch (sourceData[row].finishStatus) {
            case 'Y':
                released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Đã Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
                break;
            case 'N':
                if(sourceData[row].paymentStatus == 'Y') {
                    released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Có Thể Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
                }
                break;
        }
        // Return the content
        let content = '<div>' + process + payment + released + '</div>';
        $(td).html(content);
    }
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
//function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
//    $(td).attr('id', 'vslNm' + row).addClass("htMiddle");
//    $(td).html(value);
//    if (value != null && value != '') {
//        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
//            cellProperties.readOnly = 'true';
//            $(td).css("background-color", "rgb(232, 232, 232)");
//        }
//    }
//    return td;
//}
//function voyNoRenderer(instance, td, row, col, prop, value, cellProperties) {
//    $(td).attr('id', 'voyNo' + row).addClass("htMiddle");
//    $(td).html(value);
//    if (value != null && value != '') {
//        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
//            cellProperties.readOnly = 'true';
//            $(td).css("background-color", "rgb(232, 232, 232)");
//        }
//    }
//    return td;
//}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'sztp' + row).addClass("htMiddle");
    if (value != null && value != '') {
        if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
            cellProperties.readOnly = 'true';
            $(td).css("background-color", "rgb(232, 232, 232)");
        }
    }
    $(td).html(value);
    return td;
}
// function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
//     $(td).attr('id', 'wgt' + row).addClass("htMiddle");
//     $(td).html(value);
//     if (value != null && value != '') {
//         if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
//             cellProperties.readOnly = 'true';
//             $(td).css("background-color", "rgb(232, 232, 232)");
//         }
//     }
//     return td;
// }
// function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
//     $(td).attr('id', 'dischargePort' + row).addClass("htMiddle");
//     if (value != null && value != '') {
//         value = value.split(':')[0];
//         if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
//             cellProperties.readOnly = 'true';
//             $(td).css("background-color", "rgb(232, 232, 232)");
//         }
//     }
//     $(td).html(value);
//     return td;
// }
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
                    return '<span>Container No</span><span style="color: red;">(*)</span>';
                case 3:
                    return '<span>Kích Thước</span><span style="color: red;">(*)</span>';
                case 4:
                    return '<span>Hãng Tàu</span><span style="color: red;">(*)</span>';
                case 5:
                    if (shipmentSelected && shipmentSelected.blNo) {
                        return '<span>Hạn Lệnh</span><span style="color: red;">(*)</span>';
                    }
                    return 'Hạn Lệnh';
//                case 5:
//                    return '<span>Tàu</span><span style="color: red;">(*)</span>';
//                case 6:
//                    return '<span>Chuyến</span><span style="color: red;">(*)</span>';
                // case 8:
                //     return '<span>Trọng Tải</span><span style="color: red;">(*)</span>';
                // case 9:
                //     return '<span>Cảng Dỡ Hàng</span><span style="color: red;">(*)</span>';
                case 6:
                    return "Ghi Chú";
            }
        },
        // colWidths: [50, 100, 100, 100, 120, 100, 200],
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
                data: "sztp",
                type: "autocomplete",
                source: sizeList,
                strict: true,
                renderer: sizeRenderer
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
//            {
//                data: "vslNm",
//                type: "autocomplete",
//                source: vslNmList,
//                strict: true,
//                renderer: vslNmRenderer
//            },
//            {
//                data: "voyNo",
//                type: "autocomplete",
//                strict: true,
//                renderer: voyNoRenderer
//            },
            // {
            //     data: "wgt",
            //     type: "numeric",
            //     strict: true,
            //     renderer: wgtRenderer
            // },
            // {
            //     data: "dischargePort",
            //     strict: true,
            //     type: "autocomplete",
            //     source: dischargePortList,
            //     renderer: dischargePortRenderer
            // },
            {
                data: "remark",
                renderer: remarkRenderer
            },
        ],
        // beforeOnCellMouseDown: function restrictSelectionToWholeRowColumn(event, coords) {
        //     if(coords.col == 0) event.stopImmediatePropagation();
        // },
        afterChange: onChange
    };
}
configHandson();

function onChange(changes, source) {
    if (!changes) {
        return;
    }
    changes.forEach(function (change) {
//        if (change[1] == "vslNm" && change[3] != null && change[3] != '') {
//            hot.setDataAtCell(change[0], 6, '');//voyNo reset
//            $.ajax({
//                url: "/logistic/vessel/" + change[3] + "/voyages",
//                method: "GET",
//                success: function (data) {
//                    if (data.code == 0) {
//                        hot.updateSettings({
//                            cells: function (row, col, prop) {
//                                if (row == change[0] && col == 6) {
//                                    let cellProperties = {};
//                                    cellProperties.source = data.voyages;
//                                    return cellProperties;
//                                }
//                            }
//                        });
//                    }
//                }
//            });
//        }
    });
}

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
    updateLayout();
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
        updateLayout();
    }
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
	$.modal.loading("Đang xử lý ...");
    $.ajax({
        url: prefix + "/shipment/" + id + "/shipment-detail",
        method: "GET",
        success: function (data) {
        	$.modal.closeLoading();
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
    $("#deleteBtn").prop("disabled", true);
    $("#verifyBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", true);
    $("#exportBillBtn").prop("disabled", true);
    $("#exportReceiptBtn").prop("disabled", true);
    setLayoutRegisterStatus();
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
    } else {
        return true;
    }
}

// GET SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataFromTable(isValidate) {
    let myTableData = hot.getSourceData();
    let errorFlg = false;
    let cleanedGridData = [];
    for (let i=0; i<checkList.length; i++) {
        if (Object.keys(myTableData[i]).length > 0) {
            if (myTableData[i].containerNo || myTableData[i].opeCode || myTableData[i].expiredDem ||
                myTableData[i].sztp || myTableData[i].remark) {
                cleanedGridData.push(myTableData[i]);
            }
        }
    }
    shipmentDetails = [];
    contList = [];
    conts = '';
    let opecode, vessel, voyage, pod;
    if (cleanedGridData.length > 0) {
        opecode = cleanedGridData[0].opeCode;
//        vessel = cleanedGridData[0].vslNm;
//        voyage = cleanedGridData[0].voyNo;
    }
    $.each(cleanedGridData, function (index, object) {
        let shipmentDetail = new Object();
        if (isValidate) {
            if(!object["containerNo"]) {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập số container!");
                errorFlg = true;
                return false;
            } else if (!/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"])) {
                $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
                errorFlg = true;
                return false;
            } else if (!object["opeCode"]) {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn Hãng tàu!");
                errorFlg = true;
                return false;
            } else if (!object["expiredDem"] && shipmentSelected.blNo) {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa nhập hạn lệnh!");
                errorFlg = true;
                return false;
//            } else if (!object["vslNm"]) {
//                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn tàu!");
//                errorFlg = true;
//                return false;
//            } else if (!object["voyNo"]) {
//                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn chuyến!");
//                errorFlg = true;
//                return false;
            } else if (!object["sztp"]) {
                $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn kích thước!");
                errorFlg = true;
                return false;
            } else if (opecode != object["opeCode"]) {
                $.modal.alertError("Hãng tàu không được khác nhau!");
                errorFlg = true;
                return false;
//            } else if (vessel != object["vslNm"]) {
//                $.modal.alertError("Tàu không được khác nhau!");
//                errorFlg = true;
//                return false;
//            } else if (voyage != object["voyNo"]) {
//                $.modal.alertError("Số chuyến không được khác nhau!");
//                errorFlg = true;
//                return false;
            }
        }
        opecode = object["opeCode"];
//        vessel = object["vslNm"];
//        voyage = object["voyNo"];
        let expiredDem;
        if (shipmentSelected.blNo || object["expiredDem"]) {
            expiredDem = new Date(object["expiredDem"].substring(6, 10) + "/" + object["expiredDem"].substring(3, 5) + "/" + object["expiredDem"].substring(0, 2));
            let now = new Date();
            now.setHours(0, 0, 0);
            expiredDem.setHours(23, 59, 59);
            if (expiredDem.getTime() < now.getTime() && isValidate && shipmentSelected.blNo) {
                errorFlg = true;
                $.modal.alertError("Hàng " + (index + 1) + ": Hạn lệnh không được trong quá khứ!")
            }
            shipmentDetail.expiredDem = expiredDem.getTime();
        }
        shipmentDetail.containerNo = object["containerNo"];
        contList.push(object["containerNo"]);
        if (object["status"] == 1 || object["status"] == null) {
            conts += object["containerNo"] + ',';
        }
        let carrier = object["opeCode"].split(": ");
        shipmentDetail.opeCode = carrier[0];
        shipmentDetail.carrierName = carrier[1];
//        shipmentDetail.vslNm = object["vslNm"];
//        shipmentDetail.voyNo = object["voyNo"];
        let sizeType = object["sztp"].split(": ");
        shipmentDetail.sztp = sizeType[0];
        shipmentDetail.sztpDefine = sizeType[1];
        shipmentDetail.remark = object["remark"];
        shipmentDetail.bookingNo = shipmentSelected.bookingNo;
        shipmentDetail.shipmentId = shipmentSelected.id;
        shipmentDetail.id = object["id"];
        shipmentDetails.push(shipmentDetail);
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
        $.modal.alertError("Bạn cần chọn lô trước");
        return;
    } else {
        if (getDataFromTable(true)) {
            if (shipmentDetails.length > 0 && shipmentDetails.length <= shipmentSelected.containerAmount) {
                shipmentDetails[0].processStatus = conts;
                $.modal.loading("Đang xử lý...");
                $.ajax({
                    url: prefix + "/shipment-detail",
                    method: "post",
                    contentType: "application/json",
                    accept: 'text/plain',
                    data: JSON.stringify(shipmentDetails),
                    dataType: 'text',
                    success: function (data) {
                        var result = JSON.parse(data);
                        if (result.code == 0) {
                            $.modal.alertSuccess(result.msg);
                            reloadShipmentDetail();
                        } else {
                            if (result.conts != null) {
                                $.modal.alertError("Không thể làm lệnh đối với các container: "+result.conts);
                            } else {
                                $.modal.alertError(result.msg);
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
    if (getDataSelectedFromTable(true)) {
        $.modal.loading("Đang xử lý...");
        $.ajax({
            url: prefix + "/shipment/" + shipmentSelected.id + "/shipment-detail/" + shipmentDetailIds,
            method: "delete",
            success: function (result) {
                if (result.code == 0) {
                    $.modal.alertSuccess(result.msg);
                    reloadShipmentDetail();
                } else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
            },
            error: function (result) {
                $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
                $.modal.closeLoading();
            },
        });
    }
}

// Handling logic
function verify() {
    getDataSelectedFromTable(true);
    if (shipmentDetails.length > 0) {
        $.modal.openCustomForm("Xác nhận làm lệnh", prefix + "/otp/cont-list/confirmation/" + shipmentDetailIds, 600, 500);
    }
}

function verifyOtp(shipmentDtIds, creditFlag) {
    getDataSelectedFromTable(true);
    if (shipmentDetails.length > 0) {
        $.modal.openCustomForm("Xác thực OTP", prefix + "/otp/verification/" + shipmentDtIds + "/" + creditFlag, 600, 350);
    }
}

function pay() {
    getDataSelectedFromTable(true);
    if (shipmentDetails.length > 0) {
        $.modal.openCustomForm("Thanh toán", prefix + "/payment/" + processOrderIds, 800, 400);
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
    $("#exportReceiptBtn").prop("disabled", true);
}

function setLayoutVerifyUserStatus() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("label-primary disable").addClass("active");
    $("#paymentStatus").removeClass("active label-primary").addClass("disable");
    $("#finishStatus").removeClass("active label-primary").addClass("disable");
    $("#verifyBtn").prop("disabled", false);
    $("#payBtn").prop("disabled", true);
    $("#exportBillBtn").prop("disabled", true);
    $("#exportReceiptBtn").prop("disabled", true);
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
    $("#exportReceiptBtn").prop("disabled", true);
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
    $("#exportReceiptBtn").prop("disabled", false);
}

function finishForm(result) {
    if (result.code == 0) {
        $.modal.alertSuccess(result.msg);
    } else {
        $.modal.alertError(result.msg);
    }
    reloadShipmentDetail();
}

function finishVerifyForm(result) {
    if (result.code == 0 || result.code == 301){
        //$.modal.loading(result.msg);
        currentProcessId = result.processId;
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

function napasPaymentForm() {
    $.modal.openFullWithoutButton("Cổng Thanh Toán", ctx + "logistic/payment/napas/" + processOrderIds);
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
    //$.modal.alertError('Could not connect to WebSocket server. Please refresh this page to try again!');
    setTimeout(() => {
        hideProgress();
        $.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");
    }, 1000);
    //$.modal.closeLoading();
}

function onMessageReceived(payload) {
    clearTimeout(timeout);
    setProgressPercent(currentPercent=100);
    setTimeout(() => {
        hideProgress();

        let message = JSON.parse(payload.body);

        reloadShipmentDetail();

        if (message.code == 0){
            $.modal.alertSuccess(message.msg);
        } else {
            $.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");
        }

        // Close loading
        //$.modal.closeLoading();

        // Unsubscribe destination
        if (currentSubscription){
            currentSubscription.unsubscribe();
        }

        // Close websocket connection 
        $.websocket.disconnect(onDisconnected);
    }, 1000);
}

function onDisconnected() {
    console.log('Disconnected socket.');
}     

function showProgress(title) {
    $('.progress-wrapper').show();
    $('.dim-bg').show();
    $('#titleProgress').text(title);
    $('.percent-text').text("0%");
    currentPercent = 0;
    interval = setInterval(function() {
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

function exportReceipt(){
	if(!shipmentSelected){
		$.modal.alertError("Bạn chưa chọn Lô!");
		return
	}
    $.modal.openTab("In Biên Nhận", ctx +"logistic/print/receipt/shipment/"+shipmentSelected.id);
}