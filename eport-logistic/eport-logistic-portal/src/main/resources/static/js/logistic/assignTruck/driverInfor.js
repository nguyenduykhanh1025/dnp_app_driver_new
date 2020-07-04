prefix = ctx + "logistic/assignTruck";
var truckIds = [];
$(document).ready(function () {
    loadPickedTruckTable(driverAccount.id);
});
function loadPickedTruckTable(driverId){
    truckIds = [];
    $("#pickedTruckTable").datagrid({
        url: prefix + "/driver/truck/list",
        height: 270,
        rownumbers:true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            let opts = $(this).datagrid("options");
            if (!opts.url) return false;
            $.ajax({
                type: opts.method,
                url: opts.url,
                data: {
                    driverId: driverId
                },
                dataType: "json",
                success: function (data) {
                    success(data);
                    if(data){
                        for(let i=0; i< data.length;i++){
                            truckIds.push(data[i].id);
                        }
                    }
                    // getTrucks not picked
                    $.ajax({
                        url: prefix + "/trucks/not-picked",
                        method: "get",
                        data:{
                            truckIds:truckIds
                        },
                        success: function(result){
                            if(result){
                                //load combogrid
                                let g = $('#addTruckAssignTable').combogrid('grid'); 
                                g.datagrid('loadData', result);
                            } 
                        },
                        error: function () {
                            error.apply(this, arguments);
                        },
                    })
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });
}

function formatId(value, row, index){
    if(value){
        return value;
    }
    return row.id;
}

function formatPlateNumber(value, row, index){
    if(value){
        return value;
    }
    return row.plateNumber
}

function formatType(value, row, index){
    if(value){
        return value  == 0 ? "Đầu kéo" : "Rơ mooc";
    }
    return row.type == 0 ? "Đầu kéo" : "Rơ mooc";
}

function formatWgt(value, row, index){
    if(value){
        return value;
    }
    return row.wgt;
}

// function formatAction(value, row, index){
//     let button = '';
//         button += '<button class="btn btn-danger btn-xs " onclick="removeTruck()"><i class="fa fa-remove"></i>Xóa</button>'
//     return button;
// }
// function formatTruckType(value, row, index){
//     return row.type  == 0 ? "Đầu kéo" : "Rơ mooc";
// }

function removeTruck(){
    let rows = $('#pickedTruckTable').datagrid('getSelections');
    if(rows){
        for(let i=0; i< rows.length;i++){
            let index = $('#pickedTruckTable').datagrid('getRowIndex', rows[i]);
            $('#pickedTruckTable').datagrid('deleteRow', index);
            let g = $('#addTruckAssignTable').combogrid('grid');
            g.datagrid('appendRow', rows[i]);
        }
    }
}

function addTruck(){
    let g = $('#addTruckAssignTable').combogrid('grid'); 
    let r = g.datagrid('getSelected');
    if(r){
        //append ds assigned
        $('#pickedTruckTable').datagrid('appendRow', r);
        //delete r in combogrid
        let index = g.datagrid('getRowIndex', r);
        g.datagrid('deleteRow', index);
    }
}

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save("/logistic/transport/edit", $('#form-account-edit').serialize());

        // save assign truck for driver
        truckIds = [];
        let rows = $('#pickedTruckTable').datagrid('getRows');
        if(rows){
            for(let i=0; i< rows.length;i++){
                truckIds.push(rows[i].id);
            }
        }
        $.ajax({
            url: prefix + "/truck/assign/save",
            method: "post",
            data:{
                truckIds: truckIds,
                driverId: driverAccount.id
            },
            done: function(rs){
                if(rs.code == 0 ){
                    $.modal.msgSuccess(rs.msg);
                    $.modal.close();
                }else{
                    $.modal.msgError(rs.msg);
                }
            }
        })
        //reload 
        parent.getSelectedShipment()

    }
}

