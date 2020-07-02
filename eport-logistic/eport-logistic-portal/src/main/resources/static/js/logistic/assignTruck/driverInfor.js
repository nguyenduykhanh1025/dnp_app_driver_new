prefix = ctx + "logistic/assignTruck";
$(document).ready(function () {
    loadPickedTruckTable(driverAccount.id);
});
function loadPickedTruckTable(driverId){
    //console.log($('.container').height());
    $("#pickedTruckTable").datagrid({
        url: prefix + "/driver/truck/list",
        height: 270,
        singleSelect: true,
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
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });
}
function formatPlateNumber(value, row, index){
    return row.logisticTruck.plateNumber;
}

function formatType(value, row, index){
    return row.logisticTruck.type  == 0 ? "Đầu kéo" : "Rơ mooc";
}

function formatWgt(value, row, index){
    return row.logisticTruck.wgt;
}

function formatAction(value, row, index){
    let button = '';
    if(row){
        button += '<button class="btn btn-danger btn-xs " onclick="remove(\'' + row.id + '\')"><i class="fa fa-remove"></i>Xóa</button>'
    }
    return button;
}
function remove(truckId){

}