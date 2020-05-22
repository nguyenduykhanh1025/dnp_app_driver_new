var prefix = ctx + "logistic/receiveContFull";
var moveContAmount = 0;
var preorderPickupContIds = [];

$("#unitCosts").html(unitCosts);

function confirm() {
    $.ajax({
        url: prefix + "/setMovingContPrice",
        method: "post",
        data: {
            preorderPickupContIds: preorderPickupContIds,
            billNo: billNo,
            shipmentId: shipmentId
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

var str = "";
for (var col=0; col<7; col++) {
    str += '<div class="columnDiv">';
    for (var row=4; row>=0; row--) {
        if (containerList[row][col] != null) {
            str += '<div id="cell'+ containerList[row][col].id +'" class="cellDiv" onclick="pickCont('+ containerList[row][col].id +', '+ row +','+ col +')">'+ containerList[row][col].containerNo +'</div>';
        } else {
            str += '<div class="cellDivDisable"></div>';
        }
    }
    str += "</div>";
}
$(".contListPosition").html(str);

function pickCont(id, row, col) {
    
    if (containerList[row][col].preorderPickup == "N") {
        containerList[row][col].preorderPickup = "Y";
        $('#cell'+ id).css("background-color", "#bfe5bf");
        var tableRow = '<tr id="row'+ id +'"><td width="60px">' + containerList[row][col].id + '</td><td width="250px">' + containerList[row][col].containerNo + '</td><td width="250px"></td><td width="200px"><button onclick="pickTruck()">Điều xe</button></td></tr>';
        $("#pickedContList").append(tableRow);
    } else {
        containerList[row][col].preorderPickup = "N";
        $('#cell'+ id).css("background-color", "#ffffff");
        $("#row" + id).remove();
    }
    var moveContAmountTemp = 0;
    preorderPickupContIds = [];
    moveContAmount = 0;
    for (var j=0; j<7; j++) {
        for (var i=4; i>=0; i--) {
            if (containerList[i][j] != null) {
                if (containerList[i][j].preorderPickup == "Y") {
                    preorderPickupContIds.push(containerList[i][j].id);
                    moveContAmount += moveContAmountTemp;
                    moveContAmountTemp = 0;
                } else {
                    moveContAmountTemp++;
                }
            }
        }
    }
    $("#pickedContAmount").html(moveContAmount);
    $("#totalCosts").html(moveContAmount*unitCosts);
}
