var prefix = ctx + "logistic/receiveContFull";
var driverIds = [];
var driverList;

function confirm() {
    if (driverIds.length > 0) {
        var ids = "";
        driverIds.forEach(function(id) {
            ids += id + ",";
        });
        if (pickCont) {
            if (driverIds.length == 1) {
                parent.finishPickTruck($("#plateNumber"+driverIds[0]).html(), ids.substring(0, ids.length-1));
            } else {
                parent.finishPickTruck("Đội xe chỉ định trước", ids.substring(0, ids.length-1));
            }
            $.modal.close();
        } else {
            $.ajax({
                url: prefix + "/pickTruck",
                method: "post",
                data: {
                    shipmentId: shipmentId,
                    driverIds: ids.substring(0, ids.length-1)
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
    } else {
        $.modal.alertError("Quý khách chưa đăng ký xe lấy container.");
    }
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
            var tableRow = '<tr id="transport'+ data[i].id +'"><td width="108px">' + data[i].plateNumber + '</td><td width="108px">' + data[i].mobileNumber + '</td><td width="100px"><button onclick="transferInToOut('+ i +', this)">Chuyển</button></td></tr>';
            $("#transportList").append(tableRow);
        }
    }
});

function transferInToOut(index, tr) {
    driverIds.push(driverList[index].id);
    $("#transport"+driverList[index].id).remove();
    var tableRow = '<tr id="pickedTransport'+ driverList[index].id +'"><td width="100px"><button onclick="transferOutToIn('+ index +', this)">Chuyển</button></td><td id="plateNumber'+ driverList[index].id +'" width="108px">' + driverList[index].plateNumber + '</td><td width="108px">' + driverList[index].mobileNumber + '</td></tr>';
    $("#pickedTransportList").append(tableRow);
}

function transferOutToIn(index, tr) {
    driverIds.splice($(tr).parents("tr")[0].sectionRowIndex, 1);
    $("#pickedTransport"+driverList[index].id).remove();
    var tableRow = '<tr id="transport'+ driverList[index].id +'"><td width="108px">' + driverList[index].plateNumber + '</td><td width="108px">' + driverList[index].mobileNumber + '</td><td width="100px"><button onclick="transferInToOut('+ index +', this)">Chuyển</button></td></tr>';
    $("#transportList").append(tableRow);
}

