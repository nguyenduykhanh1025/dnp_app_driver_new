const PREFIX = ctx + "container/supplier";
var dogrid = document.getElementById("container-grid"), hot;
var rowAmount = 0;
var shipment = new Object();
var shipmentSelected;

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
        url: PREFIX + '/shipments',
        height: window.innerHeight - 70,
        method: 'POST',
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
            var opts = $(this).datagrid("options");
            if (!opts.url) return false;
            $.ajax({
                type: opts.method,
                url: opts.url,
                contentType: "application/json",
                accept: 'text/plain',
                dataType: 'text',
                data: JSON.stringify({
                    pageNum: param.page,
                    pageSize: param.rows,
                    orderByColumn: param.sort,
                    isAsc: param.order,
                    data: shipment
                }),
                success: function (data) {
                    success(JSON.parse(data));
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
    return '<div class="easyui-tooltip" title="'+ ((value!=null&&value!="")?value:"không có ghi chú") +'" style="width: 80; text-align: center;"><span>'+ (value!=null?value:"") +'</span></div>';
}

// FORMAT DATE FOR SHIPMENT LIST
function formatDate(value) {
    var date = new Date(value);
    var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    var month = date.getMonth() + 1;
    var monthText = month < 10 ? "0" + month : month;
    return day + "/" + monthText + "/" + date.getFullYear();
}

function handleRefresh() {
    loadTable();
}

// HANDLE WHEN SELECT A SHIPMENT
function getSelected() {
    var row = $("#dg").datagrid("getSelected");
    if (row) {
        $("#loCode").text(row.id);
        $("#taxCode").text(row.taxCode);
        $("#quantity").text(row.containerAmount);
        $("#bookingNo").text(row.bookingNo);
        rowAmount = row.containerAmount;
        shipmentSelected = row;
        loadShipmentDetail(row.id);
    }
}

function changeBatchStatus() {
    shipment.contSupplyStatus = $('#batchStatus').val();
    loadTable();
}

// FORMAT HANDSONTABLE COLUMN
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'containerNo' + row).addClass("htMiddle");
    $(td).html(value);
    // if (value != null && value != '') {
    //     if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
    //         cellProperties.readOnly = 'true';
    //         $(td).css("background-color", "rgb(232, 232, 232)");
    //     }
    // }
    return td;
}
function expiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'expiredDem' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
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
function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'opeCode' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
    $(td).html(value);
    return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'vslNm' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
    $(td).html(value);
    return td;
}
function voyNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'voyNo' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
    $(td).html(value);
    return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'sztp' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
    $(td).html(value);
    return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'dischargePort' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
    $(td).html(value);
    return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'remark' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
    $(td).html(value);
    return td;
}

function formatUpdateTime(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'remark' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
    let updateTime = new Date(value);
    let now = new Date();
    let offset = now.getTime() - updateTime.getTime();
    let totalMinutes = Math.round(offset / 1000 / 60);
    value = totalMinutes;
    $(td).html(value);
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
                    return "Container No";
                case 1:
                    return "Số phút";
                case 2:
                    return "Hãng tàu";
                case 3:
                    return "Size / Type";
                case 4:
                    return "Loại hàng";
                case 5:
                    return "Số book";
                case 6:
                    return "'Yêu cầu chất <br> lượng vỏ";
                case 7:
                    return "Chủ hàng";
                case 8:
                    return "SĐT liên hệ";
                case 9:
                    return "Dự kiến <br> Thời gian bốc";
                case 10:
                    return "Ngày yêu cầu";
                case 11:
                    return "Tên tàu";
                case 12:
                    return "Chuyến";
                case 13:
                    return "Cảng dỡ";
                case 14:
                    return "Ghi chú";
            }
        },
        colWidths: [ 100, 100, 100, 120, 100, 100, 100, 150, 100, 120, 100, 80, 80, 80, 150],
        filter: "true",
        columns: [
            {
                data: "containerNo",
                renderer: containerNoRenderer
            },
            {
                data: "updateTime",
                renderer: formatUpdateTime
            },
            {
                data: "opeCode",
                renderer: opeCodeRenderer
            },
            {
                data: "sztp",
                renderer: sizeRenderer
            },
            {
                data: "cargoType",
            },
            {
                data: "bookingNo",
            },
            {
                data: "shellQuality", // chat luong vo
            },
            {
                data: "consignee",
            },
            {
                data: "phoneNumber", // Số điện thoại liên hệ
            },
            {
                data: "takeTime", // Thời gian dự kiến bốc
            },
            {
                data: "createTime", // Ngày yêu cầu
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
                data: "dischargePort",
                renderer: dischargePortRenderer
            },
            {
                data: "remark",
                renderer: remarkRenderer
            },
        ],
    };
}
configHandson();

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

function loadShipmentDetail(id) {
    $.ajax({
        url: PREFIX + "/shipment/" + id + "/shipment-detail",
        method: "GET",
        success: function (data) {
            if (data.code == 0) {
                sourceData = data.shipmentDetails;
                hot.destroy();
                configHandson();
                hot = new Handsontable(dogrid, config);
                hot.loadData(sourceData);
                hot.render();
            }
        }
    });
}

function getDataFromTable() {
    let myTableData = hot.getSourceData();
    let errorFlg = false;
    let cleanedGridData = [];
    for (let i = 0; i < myTableData.length; i++) {
        if (myTableData[i].id != null) {
            cleanedGridData.push(myTableData[i]);
        }
    }
    shipmentDetails = [];
    contList = [];
    $.each(cleanedGridData, function (index, object) {
        var shipmentDetail = new Object();
        if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"])) {
            $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
            errorFlg = true;
        }
        shipmentDetail.containerNo = object["containerNo"];
        contList.push(object["containerNo"]);
        shipmentDetail.id = object["id"];
        shipmentDetails.push(shipmentDetail);
    });

    if (!errorFlg) {
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

    if (errorFlg) {
        return false;
    } else {
        return true;
    }
}

function saveInput() {
    if (getDataFromTable()) {
        if (shipmentDetails.length == shipmentSelected.containerAmount) {
            $.modal.loading("Đang xử lý...");
            $.ajax({
                url: PREFIX + "/shipment/" + shipmentSelected.id + "/shipment-detail",
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
        } else {
            $.modal.alertError("Bạn chưa nhập đủ container yêu cầu.");
        }
    }
}



    