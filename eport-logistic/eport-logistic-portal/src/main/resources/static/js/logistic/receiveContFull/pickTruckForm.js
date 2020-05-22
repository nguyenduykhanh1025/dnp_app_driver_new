var prefix = ctx + "logistic/receiveContFull";
var driverIds = "";
var driverList;

function confirm() {
    $.ajax({
        url: prefix + "/pickTruck",
        method: "post",
        data: {
            shipmentId: shipmentId,
            driverIds: driverIds
        },
        success: function (data) {
            if (data.code != 0) {
                $.modal.msgError(data.msg);
            } else {
                parent.finishForm(data);
                $.modal.close();
            }
        },
        error: function (result) {
            $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
        }
    });
}

function closeForm() {
    $.modal.close();
}

function interDeliveryTab() {
    $(".interDeliveryTeam").show();
    $("#interBtn").css({"background-color": "#72c072"});
    $(".exterDeliveryTeam").hide();
    $("#exterBtn").css({"background-color": "#c7c1c1"});

}

function exterDeliveryTab() {
    $(".exterDeliveryTeam").show();
    $("#exterBtn").css({"background-color": "#72c072"});
    $(".interDeliveryTeam").hide();
    $("#interBtn").css({"background-color": "#c7c1c1"});
}

$.ajax({
    url: "/logistic/transport/listTransportAccount",
    method: "get",
}).done(function(data) {
    if (data != null) {
        driverList = data;
        for (var i=0; i<data.length; i++) {
            var tableRow = '<tr id="transport'+ data[i].id +'"><td width="108px">' + data[i].plateNumber + '</td><td width="108px">' + data[i].mobileNumber + '</td><td width="100px"><button onclick="transferInToOut('+ i +')">Chuyển</button></td></tr>';
            $("#transportList").append(tableRow);
        }
    }
});

function transferInToOut(index) {
    $("#transport"+driverList[index].id).remove();
    var tableRow = '<tr id="pickedTransport'+ driverList[index].id +'"><td width="100px"><button onclick="transferOutToIn('+ index +')">Chuyển</button></td><td width="108px">' + driverList[index].plateNumber + '</td><td width="108px">' + driverList[index].mobileNumber + '</td></tr>';
    $("#pickedTransportList").append(tableRow);
}

function transferOutToIn(index) {
    $("#pickedTransport"+driverList[index].id).remove();
    var tableRow = '<tr id="transport'+ driverList[index].id +'"><td width="108px">' + driverList[index].plateNumber + '</td><td width="108px">' + driverList[index].mobileNumber + '</td><td width="100px"><button onclick="transferInToOut('+ index +')">Chuyển</button></td></tr>';
    $("#transportList").append(tableRow);
}

