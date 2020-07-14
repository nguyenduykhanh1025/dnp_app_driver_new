var prefix = ctx + "logistic/assignTruck";
var pickedIds = [];
$(document).ready(function () {
    $('#batchCode').text(shipment.id);
    $('#taxCode').text(shipment.taxCode);
    $('#blNo').text(shipment.blNo);
    $('#bookingNo').text(shipment.bookingNo);
    $('#edoFlg').text(shipment.edoFlg == 1 ? "eDO" : "DO");
    $('#containerNo').text(shipmentDetail.containerNo);
    loadDriver(shipmentDetail.shipmentId);
    loadOutSource(shipment.id);
});
function loadDriver(shipmentId){
    pickedIds = [];
    //pickedDriverList
    $("#pickedDriverTable").datagrid({
        url: prefix + "/assignedDriverAccountListForPreoderPickup",
        height: window.innerHeight/2 - 55,
        collapsible: true,
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
                    shipmentId:shipmentId,
                    shipmentDetailId: shipmentDetail.id
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
                        url: prefix + "/listDriverAccountPreorderPickup",
                        height: window.innerHeight/2 - 55,
                        collapsible: true,
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
                                    shipmentDetailId: shipmentDetail.id,
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
function formatAction(value, row, index) {
	let actions = [];
    actions.push('<a class="btn btn-primary btn-xs" onclick="editDriver(\'' + row.id + '\')"><i class="fa fa-edit"></i>Sửa</a> ');
    return actions.join('');
}

function editDriver(id){
    $.modal.open("Chỉnh Sửa Tài xế ", prefix +"/edit/driver/"+id);
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

function closeForm() {
    $.modal.closeTab();
}

function saveAssignPreorderPickup(){
    let rows = $('#pickedDriverTable').datagrid('getRows');
    let pickupAssigns = [];
    
    if(rows){
        for(let i=0; i< rows.length;i++){
            let object = new Object();
            object.driverId = rows[i].id;
            object.shipmentId = shipment.id;
            object.shipmentDetailId = shipmentDetail.id;
            pickupAssigns.push(object);
        }
    }
    if (getDataFromOutSource(true)){
        if(outsources.length > 0){
            for(let i=0;i < outsources.length; i++){
                pickupAssigns.push(outsources[i])
            }
        }
        $.modal.loading("Đang xử lý...");
        $.ajax({
            url: prefix + "/savePickupAssignFollowContainer",
            method: "post",
            contentType: "application/json",
            data: JSON.stringify(pickupAssigns),
            success: function(result){
                if(result.code == 0){
                    $.modal.msgSuccess(result.msg);
                    getSelectedShipment()
                }else{
                    $.modal.msgError(result.msg);
                }
                $.modal.closeLoading();
            },
            error: function (result) {
                $.modal.alertError("Có lỗi trong quá trình lưu dữ liệu, vui lòng liên hệ admin.");
                $.modal.closeLoading();
            },
        })
    }

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
 //---------------------------------THUE NGOAI------------------------------------------------
 var dogrid = document.getElementById("container-grid"), hot;
 var config;
 var outsources = [];
 function loadOutSource(shipmentId) {
    $.ajax({
        url: prefix + "/out-source/container/" + shipmentId,
        method: "GET",
        success: function (data) {
            if (data.code == 0) {
                hot.destroy();
                hot = new Handsontable(dogrid, config);
                hot.loadData(data.outSourceList);
                hot.render();
            }
        }
    });
}
// CONFIGURATE HANDSONTABLE
config = {
    stretchH: "all",
    height: "100%",
    minRows: 5,
    maxRows: 20,
    width: "100%",
    minSpareRows: 1,
    rowHeights: 30,
    fixedColumnsLeft: 0,
    manualColumnResize: true,
    manualRowResize: true,
    renderAllRows: true,
    rowHeaders: true,
    className: "htMiddle",
    colHeaders: function (col) {
        switch (col) {
            case 0:
                return "Đơn vị chủ quản";
            case 1:
                return '<span>Số điện thoại</span><span style="color: red;">(*)</span>';
            case 2:
                return '<span>Họ và tên</span><span style="color: red;">(*)</span>';
            case 3:
                return '<span>Xe đầu kéo</span><span style="color: red;">(*)</span>';
            case 4:
                return '<span>Xe rơ mooc</span><span style="color: red;">(*)</span>';
        }
    },
    colWidths: [200, 100, 150, 100, 100],
    filter: "true",
    columns: [
        {
            data: "driverOwner",
            type: "autocomplete",
            source: driverOwnerList,
            strict: true
        },
        {
            data: "phoneNumber",
            type: "autocomplete",
            strict: true,
        },
        {
            data: "fullName",
        },
        {
            data: "truckNo",
        },
        {
            data: "chassisNo",
        },
    ],
    afterChange: onChange
};

function onChange(changes, source) {
    if (!changes) {
        return;
    }
    changes.forEach(function (change) {
        if (change[1] == "driverOwner" && change[3] != null && change[3] != '') {
            $.ajax({
                url: prefix + "/owner/"+ change[3] +"/driver-phone-list",
                method: "GET",
                success: function (data) {
                    if (data.code == 0) {
                        hot.updateSettings({
                            cells: function (row, col, prop) {
                                if (row == change[0] && col == 1) {
                                    let cellProperties = {};
                                    cellProperties.source = data.driverPhoneList;
                                    return cellProperties;
                                }
                            }
                        });
                    }
                }
            });
        } else if (change[1] == "phoneNumber" && change[3] != null && change[3] != '') {
            $.ajax({
                url: prefix + "/driver-phone/" + change[3] + "/infor",
                method: "GET",
                success: function (data) {
                    if (data.code == 0) {
                        hot.setDataAtCell(change[0], 2, data.pickupAssign.fullName);
                        hot.setDataAtCell(change[0], 3, data.pickupAssign.truckNo);
                        hot.setDataAtCell(change[0], 4, data.pickupAssign.chassisNo);
                    }
                }
            });
        }
    });
}
// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);
//GET DATA FROM HANDSOME
function getDataFromOutSource(){
    outsources = [];
    let myTableData = hot.getSourceData();
    let cleanedGridData = [];
    let errorFlg = false;
    for (let i = 0; i < myTableData.length; i++) {
        if (Object.keys(myTableData[i]).length > 0) {
            if (myTableData[i].driverOwner ||myTableData[i].phoneNumber || myTableData[i].fullName || myTableData[i].truckNo || myTableData[i].chassisNo) {
                cleanedGridData.push(myTableData[i]);
            }
        }
    }
    $.each(cleanedGridData, function(index, object){
        let outsource = new Object();
        if(!object["phoneNumber"]){
            $.modal.alertError("Số điện thoại hàng:" + (index + 1) + " không được trống!");
            errorFlg = true;
            return false;
        }
        if(!object["fullName"]){
            $.modal.alertError("Họ tên hàng:" + (index + 1) + " không được trống!");
            errorFlg = true;
            return false;
        }
        if(!object["truckNo"]){
            $.modal.alertError("Biển số xe đầu kéo hàng:" + (index + 1) +" không được trống!");
            errorFlg = true;
            return false;
        }
        if(!object["chassisNo"]){
            $.modal.alertError("Biển số xe rơ mooc hàng:" + (index + 1) +" không được trống!");
            errorFlg = true;
            return false;
        }
        outsource.phoneNumber = object["phoneNumber"].trim();
        outsource.driverOwner = object["driverOwner"];
        outsource.truckNo = object["truckNo"].trim().toUpperCase();
        outsource.fullName = object["fullName"].trim();
        outsource.chassisNo = object["chassisNo"].trim().toUpperCase();
        outsource.shipmentId = shipment.id;
        outsource.shipmentDetailId = shipmentDetail.id;
        outsource.externalFlg = 1;
        outsources.push(outsource);
    })
    if (errorFlg) {
        return false;
    } else {
        return true;
    }
}