const PREFIX = ctx + "container/supplier";
var rowAmount = 0;
var allChecked = false;
var checkList = [];
var shipment = new Object();
var shipmentSelected;
var sourceData;

const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var dogrid = document.getElementById("container-grid"), hot;

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
    loadTable();
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
    })

    $(".left-side__collapse").click(function () {
        $('#main-layout').layout('collapse', 'west');
    })

    $("#attachIcon").on("click", function () {
        let shipmentId = $(this).data("shipment-id");
        if (!shipmentId) {
            return;
        }
        let url = $(this).data("url");
        try {
            $.modal.openTab(`Đính kèm - Cont [${shipmentId}]`, url.replace("{shipmentId}", shipmentId));
        }
        catch (e)
        {
            window.open(url.replace("{shipmentId}", shipmentId));
        }
        
    });
    $("#blNo").textbox('textbox').bind('keydown', function(e) {
        // enter key
        if (e.keyCode == 13) {
          shipment.bookingNo = $("#blNo").textbox('getText').toUpperCase();
          loadTable();
        }
      });
});

function search() {
    shipment.bookingNo = $("#blNo").textbox('getText').toUpperCase();
    shipment.contSupplyStatus = $('#batchStatus').combobox('getValue');
    loadTable();
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
function loadTable() {
    $("#dg").datagrid({
        url: PREFIX + '/shipments',
        height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
        method: 'POST',
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
// FORMATTER
function formatLogistic(value, row, index) {
    return '<a onclick="logisticInfo(' + row.logisticGroupId + "," + "'" + value + "')\"> " + value + "</a>";
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

function formatSpecificContFlg(value) {
    switch (value) {
        case 1:
            return 'Hãng Tàu Cấp';
        case 0:
            return 'Cảng Cấp';
    }
}

function handleRefresh() {
    loadTable();
}

function clearInput() {
    $("#blNo").textbox('setText', '');
    shipment = new Object();
    loadTable();
  }
  

// HANDLE WHEN SELECT A SHIPMENT
function getSelected() {
    var row = $("#dg").datagrid("getSelected");
    if (row) {
        $("#loCode").text(row.id);
        $("#quantity").text(row.containerAmount);
        $("#bookingNo").text(row.bookingNo);
        rowAmount = row.containerAmount;
        shipmentSelected = row;
        checkList = Array(rowAmount).fill(0);
        allChecked = false;
        if (row.specificContFlg == 0) {
            $("#saveShipmentDetailBtn").html("Xác Nhận Cấp Container");
        } else {
            $("#saveShipmentDetailBtn").html("Duyệt Cấp Container");
        }
        loadShipmentDetail(row.id);
        toggleAttachIcon(row.id);
    }
}

function toggleAttachIcon(shipmentId) {
    $.ajax({
        type: "GET",
        url: PREFIX + "/shipments/" + shipmentId + "/shipment-images/count",
        contentType: "application/json",
        success: function (data) {
            let $attachIcon = $("a#attachIcon");
            if (data.numberOfShipmentImage && data.numberOfShipmentImage > 0) {
                $attachIcon.data("shipment-id", shipmentId);
                $attachIcon.removeClass("hidden");
            } else {
                $attachIcon.removeData("shipment-id");
                $attachIcon.addClass("hidden");
            }
        }
    });
}



$("#batchStatus").combobox({
    onSelect: function (serviceType) {
        shipment.contSupplyStatus = serviceType.value;
        loadTable();
    }
  });



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
    $(td).attr('id', 'containerNo' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function contSupplyRemarkRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'contSupplyRemark' + row).addClass("htMiddle");
    $(td).html(value);
    return td;
}
function sztpRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'sztp' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
    $(td).html(value);
    return td;
}
function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'opeCode' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
    $(td).html(value);
    return td;
}
function planningDateRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'planningDate' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
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
function cargoTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'cargoType' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
    $(td).html(value);
    return td;
}
function qualityRequirementRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'qualityRequirement' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
    $(td).html(value);
    return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
    cellProperties.readOnly = 'true';
    $(td).attr('id', 'consignee' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
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

// function formatUpdateTime(instance, td, row, col, prop, value, cellProperties) {
//     cellProperties.readOnly = 'true';
//     $(td).attr('id', 'remark' + row).addClass("htMiddle").css("background-color", "rgb(232, 232, 232)");
//     let updateTime = new Date(value);
//     let now = new Date();
//     let offset = now.getTime() - updateTime.getTime();
//     let totalMinutes = Math.round(offset / 1000 / 60);
//     value = totalMinutes;
//     $(td).html(value);
//     return td;
//   }

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
                    var txt = "<input type='checkbox' class='checker' ";
                    txt += "onclick='checkAll()' ";
                    txt += ">";
                    return txt;
                case 1:
                    return "Số Container";
                case 2:
                    return "Ghi Chú <br>Cấp/Duyệt Container";
                case 3:
                    return "Sztp";
                case 4:
                    return "Hãng Tàu";
                case 5:
                    return "Thời Gian <br>Dự Kiến Bốc";
                case 6:
                    return "Loại Hàng";
                case 7:
                    return "Yêu Cầu Chất <br>Lượng Vỏ";
                case 8:
                    return "Chủ hàng";
                case 9:
                    return "Tên Tàu";
                case 10:
                    return "Chuyến";
                case 11:
                    return "Cảng Dỡ";
                case 12:
                    return "Logistics Ghi Chú";
            }
        },
        // colWidths: [ 100, 100, 100, 120, 100, 100, 100, 150, 100, 120, 100, 80, 80, 80, 150],
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
                renderer: containerNoRenderer
            },
            {
                data: "contSupplyRemark",
                renderer: contSupplyRemarkRenderer
            },
            {
                data: "sztp",
                renderer: sztpRenderer
            },
            {
                data: "opeCode",
                renderer: opeCodeRenderer
            },
            {
                data: "planningDate",
                renderer: planningDateRenderer
            },
            {
                data: "cargoType",
                renderer: cargoTypeRenderer
            },
            {
                data: "qualityRequirement",
                renderer: qualityRequirementRenderer
            },
            {
                data: "consignee",
                renderer: consigneeRenderer
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
            }
        ],
        beforeKeyDown: function (e) {
            let selected = hot.getSelected()[0];
            switch (e.keyCode) {
                // Arrow Left
                case 37:
                    if (selected[3] == 0) {
                        e.stopImmediatePropagation();
                    }
                    break;
                // Arrow Up
                case 38:
                    if (selected[2] == 0) {
                        e.stopImmediatePropagation();
                    }
                    break;
                // Arrow Right
                case 39:
                    if (selected[3] == 12) {
                        e.stopImmediatePropagation();
                    }
                    break
                // Arrow Down
                case 40:
                    if (selected[2] == rowAmount - 1) {
                        e.stopImmediatePropagation();
                    }
                    break
                default:
                    break;
            }
        },
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
    $('.checker').prop('checked', tempCheck);
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
    console.log(checkList);
    for (let i = 0; i < myTableData.length; i++) {
        if (myTableData[i].id != null) {
            if (checkList[i] == 1) {
                myTableData[i].contSupplyStatus = 'S';
            }
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
        shipmentDetail.contSupplyRemark = object["contSupplyRemark"];
        shipmentDetail.contSupplyStatus = object["contSupplyStatus"];
        shipmentDetail.id = object["id"];
        shipmentDetails.push(shipmentDetail);
    });

    if (!errorFlg) {
        contList.sort();
        let contTemp = "";
        $.each(contList, function (index, cont) {
            if (cont && cont == contTemp) {
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
        let contAmount = 0;
        let containers = '';
        console.log(shipmentDetails);
        $.each(shipmentDetails, function (index, object) {
            if (object['contSupplyStatus'] == 'S') {
                contAmount++;
                containers += object['containerNo'] + ',';
            }
        });
        if (contAmount > 0) {
            containers = containers.substring(0, containers.length-1);
            $.modal.confirm("Xác nhận cấp " + contAmount + " container: " + containers + "?", function() {
                saveData()
            });
        } else {
            $.modal.confirm("Xác nhận lưu thông tin, chưa chỉ định <br>container nào cho logistic?", function() {
                saveData()
            });
        }
    }
}

function logisticInfo(id, logistics) {
    $.modal.openLogisticInfo("Thông tin liên lạc " + logistics, ctx + "om/support/logistics/" + id + "/info", null, 470, function() {
        $.modal.close();
    });
}

function saveData() {
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
}



    