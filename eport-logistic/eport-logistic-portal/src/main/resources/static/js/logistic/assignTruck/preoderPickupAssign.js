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
        height: window.innerHeight - 170,
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
                        height: window.innerHeight - 170,
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