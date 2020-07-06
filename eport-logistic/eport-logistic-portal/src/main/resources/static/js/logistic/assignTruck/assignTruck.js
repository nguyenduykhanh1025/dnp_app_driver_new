var prefix = ctx + "logistic/assignTruck";
var shipmentType = 1;
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
            $("#dgShipmentDetail").datagrid('resize');
            $("#driverTable").datagrid('resize');
            $("#pickedDriverTable").datagrid('resize');
        }, 500);
        return;
    }
    $(".left-side").css("width", "33%");
    $(".left-side").children().show();
    $("#btn-collapse").show();
    $("#btn-uncollapse").hide();
    $(".right-side").css("width", "67%");
    setTimeout(function () {
        $("#dgShipmentDetail").datagrid('resize');
        $("#driverTable").datagrid('resize');
        $("#pickedDriverTable").datagrid('resize');
    }, 500);
}
function assignFollowBatchTab() {
    $(".assignFollowBatch").show();
    $("#batchBtn").css({"background-color": "#6c9dc7"});
    $(".assignFollowContainer").hide();
    $("#containerBtn").css({"background-color": "#c7c1c1"});
    let row = $("#dg").datagrid("getSelected");
    if(row){
        loadDriver(row.id);
    }

}

function assignFollowContainerTab() {
    $(".assignFollowContainer").css("display","flex");;
    $("#containerBtn").css({"background-color": "#6c9dc7"});
    $(".assignFollowBatch").hide();
    $("#batchBtn").css({"background-color": "#c7c1c1"});
    let row = $("#dg").datagrid("getSelected");
    if(row){
        loadShipmentDetail(row.id);
    }
}
// LOAD SHIPMENT LIST
function loadTable() {
	//shipment
    $("#dg").datagrid({
        url: prefix + "/listShipment",
        height: window.innerHeight - 70,
        singleSelect: true,
        collapsible: true,
        clientPaging: false,
        pagination: true,
        onClickRow: function () {
            getSelectedShipment();
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
                    serviceId: $('#shipmentType').val()
                },
                dataType: "json",
                success: function (data) {
                    success(data);
                    if ($('#shipmentType').val() == 1) {
                        $("#dg").datagrid("hideColumn", "bookingNo");
                        $("#dg").datagrid("showColumn", "blNo");
                        $("#bookingNoDiv").hide();
                        $("#blNoDiv").show();
                    } else if ($('#shipmentType').val() == 3) {
                        $("#dg").datagrid("hideColumn", "blNo");
                        $("#dg").datagrid("showColumn", "bookingNo");
                        $("#bookingNoDiv").show();
                        $("#blNoDiv").hide();
                    } else {
                        $("#dg").datagrid("hideColumn", "blNo");
                        $("#dg").datagrid("hideColumn", "bookingNo");
                        $("#bookingNoDiv").hide();
                        $("#blNoDiv").hide();
                    }
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

// HANDLE WHEN SELECT A SHIPMENT
function getSelectedShipment() {
    let row = $("#dg").datagrid("getSelected");
    if (row) {
        shipmentSelected = row;
        $("#batchCode").text(row.id);
        $("#taxCode").text(row.taxCode);
        $("#blNo").text(row.blNo);
        $("#bookingNo").text(row.bookingNo);
        $("#edoFlg").text(row.edoFlg == 1 ? "eDO" : "DO");
        loadShipmentDetail(row.id);
        loadDriver(row.id);
    }
}

function loadShipmentDetail(id) {
    $("#dgShipmentDetail").datagrid({
        url: prefix + "/getShipmentDetail",
        height: window.innerHeight - 110,
        singleSelect: true,
        collapsible: true,
        rownumbers:true,
        clientPaging: false,
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
                    shipmentId: id
                },
                dataType: "json",
                success: function (data) {
                    success(data);
                    $("#dgShipmentDetail").datagrid("hideColumn", "id");
                    $("#quantity").text(data.total);
                    if ($('#shipmentType').val() == 1) {
                        $("#dgShipmentDetail").datagrid("showColumn", "preorderPickup");
                    } else {
                        $("#dgShipmentDetail").datagrid("hideColumn", "preorderPickup");
                    }
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });
}

// HANDLE WHEN SELECT A SHIPMENTDETAIL
// function getSelectedShipmentDetail() {
//     let row = $("#dgShipmentDetail").datagrid("getSelected");
//     if (row) {
//         shipmentSelected = row;
//         $("#batchCode").text(row.id);
//         $("#taxCode").text(row.taxCode);
//         $("#blNo").text(row.blNo);
//         $("#bookingNo").text(row.bookingNo);
//         loadShipmentDetail(row.id);
//     }
// }

function formatPickup(value) {
    if (value == "Y") {
        return "<span class='label label-success'>Có</span>"
    } else{
        return "<span class='label label-default'>Không</span>"
    }
}

// function pickTruckForAll() {
//     if ($("#quantity").text() != "0") {
//         $.modal.openFullPickTruck("Điều xe", prefix + "/pickTruckForm/" + shipmentSelected.id + "/" + false + "/" + "0");
//     } else {
//         $.modal.alertError("Lô này hiện đang trống!");
//     }
// }

// function pickTruckForChosenList() {
//     if ($("#quantity").text() != "0") {
//         $.modal.openFullPickTruck("Điều xe", prefix + "/pickTruckForm/" + shipmentSelected.id + "/" + false + "/" + "0");
//     } else {
//         $.modal.alertError("Quý khách chưa chọn container!");
//     }
// }
///////////////////////ASSIGN TRUCK ////////////////////////////////////
var pickedIds = [];
function loadDriver(shipmentId){
    pickedIds = [];
    //pickedDriverList
    $("#pickedDriverTable").datagrid({
        url: prefix + "/assignedDriverAccountList",
        height: window.innerHeight - 170,
        collapsible: true,
        clientPaging: false,
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
                    shipmentId:shipmentId
                },
                dataType: "json",
                success: function (data) {
                    success(data);
                    //driverList
                    let records =  $('#pickedDriverTable').datagrid('getRows');
                    if(records){
                        for(let i = 0; i < records.length; i++){
                            pickedIds.push(records[i].id);
                        }
                    }
                    $("#driverTable").datagrid({
                        url: prefix + "/listDriverAccount",
                        height: window.innerHeight - 170,
                        collapsible: true,
                        clientPaging: false,
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
                                    shipmentId: shipmentId,
                                    pickedIds: pickedIds
                                },
                                dataType: "json",
                                success: function (data) {
                                    success(data);
                                },
                                error: function () {
                                    error.apply(this, arguments);
                                },
                            });
                        },
                    });
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });

}
function transferInToOut() {
    let rows = $('#driverTable').datagrid('getSelections');
    if(rows){
        for(let i=0; i< rows.length;i++){
            let index = $('#driverTable').datagrid('getRowIndex', rows[i]);
            $('#driverTable').datagrid('deleteRow', index);
            $('#pickedDriverTable').datagrid('appendRow', rows[i]);
        }
    }
}
function transferOutToIn() {
    let rows = $('#pickedDriverTable').datagrid('getSelections');
    if(rows){
        for(let i=0; i< rows.length;i++){
            let index = $('#pickedDriverTable').datagrid('getRowIndex', rows[i]);
            $('#pickedDriverTable').datagrid('deleteRow', index);
            $('#driverTable').datagrid('appendRow', rows[i]);
        }
    }
}

function transferAllToOut(){
    let rows =  $('#driverTable').datagrid('getRows');
    if(rows){
        for(let i=0; i< rows.length;i++){
            $('#pickedDriverTable').datagrid('appendRow', rows[i]);
        }
        $('#driverTable').datagrid('loadData', {"total":0,"rows":[]});
    }
}

function transferAllToIn(){
    let rows =  $('#pickedDriverTable').datagrid('getRows');
    if(rows){
        for(let i=0; i< rows.length;i++){
            $('#driverTable').datagrid('appendRow', rows[i]);
        }
        $('#pickedDriverTable').datagrid('loadData', {"total":0,"rows":[]});
    }
}

function save(){
    let pickedIdDriverArray = [];
    let shipmentId;
    let rows = $('#pickedDriverTable').datagrid('getRows');
    let shipmentRow = $("#dg").datagrid("getSelected");
    if(shipmentRow){
        shipmentId = shipmentRow.id;
    }
    if(rows){
        for(let i=0; i< rows.length;i++){
            pickedIdDriverArray.push(rows[i].id);
        }
        $.ajax({
            url: prefix + "/savePickupAssignFollowBatch",
            method: "post",
            data:{
                pickedIdDriverArray:pickedIdDriverArray,
                shipmentId: shipmentId
            },
            success: function(result){
                if(result.code == 0){
                    $.modal.msgSuccess(result.msg);
                }else{
                    $.modal.msgError(result.msg);
                }
            }
        })
    }
}

function formatAction(value, row, index) {
	let actions = [];
    actions.push('<a class="btn btn-primary btn-xs" onclick="editDriver(\'' + row.id + '\')"><i class="fa fa-edit"></i>Sửa</a> ');
    return actions.join('');
}

function formatActionAssign(value, row, index) {
    let button = '';
    if(row.preorderPickup == "Y"){
        button += '<button class="btn btn-primary btn-xs" onclick="assignFollowContainer(\'' + row.id + '\')"><i class="fa fa-edit"></i>Điều xe</button> ';
    }else{
        button += '<button class="btn btn-primary btn-xs" onclick="assignFollowContainer(\'' + row.id + '\')" disabled><i class="fa fa-edit"></i>Điều xe</button> ';
    }
    return button;
}

function editDriver(id){
    $.modal.open("Thông tin Tài xế ", prefix +"/edit/driver/"+id);
}

function assignFollowContainer(id){
    $.modal.openTab("Điều xe theo Container", prefix + "/preoderPickupAssign/" + id);
}

function addTruck(){
    $.modal.open("Thêm xe mới", "/logistic/logisticTruck/add");
}
function addDriver(){
    $.modal.open("Thêm xe mới", "/logistic/transport/add");
}

function finishAssignTruck(msg) {
    $.modal.msgSuccess(msg);
}