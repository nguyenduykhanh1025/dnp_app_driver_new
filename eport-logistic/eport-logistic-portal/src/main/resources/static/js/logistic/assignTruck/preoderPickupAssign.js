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
});
function loadDriver(shipmentId){
    pickedIds = [];
    //pickedDriverList
    $("#pickedDriverTable").datagrid({
        url: prefix + "/assignedDriverAccountListForPreoderPickup",
        height: window.innerHeight/2 - 35,
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
                        height: window.innerHeight/2 - 35,
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
    let pickedIdDriverArray = [];
    let rows = $('#pickedDriverTable').datagrid('getRows');
    if(rows){
        for(let i=0; i< rows.length;i++){
            pickedIdDriverArray.push(rows[i].id);
        }
        $.ajax({
            url: prefix + "/savePickupAssignFollowContainer",
            method: "post",
            data:{
                pickedIdDriverArray:pickedIdDriverArray,
                shipmentId: shipment.id,
                shipmentDetailId: shipmentDetail.id
            },
            success: function(result){
                if(result.code == 0){
                    $.modal.msgSuccess(result.msg);
                    setTimeout(() => {
                        $.modal.closeTab();
                    }, 500);
                }else{
                    $.modal.msgError(result.msg);
                }
            }
        })
    }
}

function addTruck(){
    $.modal.open("Thêm xe mới", "/logistic/logisticTruck/add");
}

function addDriver(){
    $.modal.open("Thêm xe mới", "/logistic/transport/add");
}
 //---------------------------------THUE NGOAI------------------------------------------------
 var dogrid = document.getElementById("container-grid"), hot;
 var config;
 function loadOutSource(shipmentId) {
    $.ajax({
        url: prefix + "/out-source/batch" + shipmentId,
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
    minRows: 2,
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
                return "Họ và tên";
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
    // changes.forEach(function (change) {
    //     if (change[1] == "vslNm" && change[3] != null && change[3] != '') {
    //         $.ajax({
    //             url: "/logistic/vessel/" + change[3] + "/voyages",
    //             method: "GET",
    //             success: function (data) {
    //                 if (data.code == 0) {
    //                     hot.updateSettings({
    //                         cells: function (row, col, prop) {
    //                             if (row == change[0] && col == 6) {
    //                                 let cellProperties = {};
    //                                 cellProperties.source = data.voyages;
    //                                 return cellProperties;
    //                             }
    //                         }
    //                     });
    //                 }
    //             }
    //         });
    //     }
    // });
}
// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);