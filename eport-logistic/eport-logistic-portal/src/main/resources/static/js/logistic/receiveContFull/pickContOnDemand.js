var prefix = ctx + "logistic/receiveContFull";
var moveContAmount = 0;
var preorderPickupConts = [];
var currentPickedBay, currentPickedRow, currentPickedTier, currentPickedId;
var shipmentId;

$("#unitCosts").html(unitCosts);

function confirm() {
    if (preorderPickupConts.length > 0) {
        $.modal.confirm("Xác nhận bốc container chỉ định (Quý khách<br>không thể hủy chỉ định cho container đã được chỉ định).", function() {
            $.ajax({
                url: prefix + "/pickContOnDemand",
                method: "post",
                contentType: "application/json",
                accept: 'text/plain',
                data: JSON.stringify(preorderPickupConts),
                dataType: 'text',
                success: function (data) {
                    var result = JSON.parse(data);
                    if (result.code != 0) {
                        $.modal.msgError(result.msg);
                    } else {
                        parent.finishForm(result);
                        $.modal.close();
                    }
                },
                error: function (result) {
                    $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
                }
            });
        }, "Xác nhận");
    } else {
        $.modal.msgError("Quý khách chưa chọn container chỉ định.");
    }
}

function closeForm() {
    $.modal.close();
}

var index = 0;
bayList.forEach(function(bay) {
    var str = '<div class="bayPosition">';
    var bayName = "";
    for (var col=0; col<6; col++) {
        str += '<div class="columnDiv">';
        for (var row=4; row>=0; row--) {
            if (bay[row][col] != null) {
                bay[row][col].expiredDem = null;
                bayName = bay[row][col].bay;
                shipmentId = bay[row][col].shipmentId;
                if (bay[row][col].containerNo == null) {
                    str += '<div id="cell'+ bay[row][col].id +'" class="cellDiv" style="background-color: #dbcfcf;" onclick="pickCont('+ bay[row][col].id +', '+ row +','+ col + ',' + index + ',' + false +')">CONT</div>';
                } else if (bay[row][col].status > 1) {
                    if (bay[row][col].preorderPickup == "Y") {
                        str += '<div id="cell'+ bay[row][col].id +'" style="background-color: #72ecea;" class="cellDiv" onclick="pickCont('+ bay[row][col].id +', '+ row +','+ col + ',' + index + ',' + true +')">'+ bay[row][col].containerNo +'</div>';
                        var tableRow = '<tr id="row'+ bay[row][col].id +'"><td width="240px">' + bay[row][col].containerNo + '</td><td id="tdTransport'+ bay[row][col].id +'" width="240px">Đã chỉ định</td><td width="180px"><button onclick="pickTruck('+ bay[row][col].id + ',' + index + ',' + col + ',' + row +')">Điều xe</button></td></tr>';
                        $("#pickedContList").append(tableRow);
                    } else {
                        str += '<div id="cell'+ bay[row][col].id +'" class="cellDiv" onclick="pickCont('+ bay[row][col].id +', '+ row +','+ col + ',' + index + ',' + false +')">'+ bay[row][col].containerNo +'</div>';
                    }
                } else {
                    str += '<div id="cell'+ bay[row][col].id +'" class="cellDiv" style="background-color: #dbcfcf;" onclick="pickCont('+ bay[row][col].id +', '+ row +','+ col + ',' + index + ',' + false +')">'+ bay[row][col].containerNo +'</div>';
                }
            } else {
                str += '<div class="cellDivDisable"></div>';
            }
        }
        str += '</div>';
    }
    str += '</div><div style="margin-bottom: 10px;"><b>'+ bayName +'</b></div>';
    $(".contListPosition").append(str);
    index++;
    calculateMovingCont();
});

function pickCont(id, row, col, index, isPicked) {
    if (bayList[index][row][col].containerNo == null) {
        $.modal.msgError("Container này không nằm trong lô của quý<br>khách.");
    } else if (bayList[index][row][col].status > 1) {
        if (isPicked) {
            $.modal.msgError("Container này đã được chỉ định.");
        } else {
            if (bayList[index][row][col].preorderPickup == "N") {
                bayList[index][row][col].preorderPickup = "Y";
                $('#cell'+ id).css("background-color", "#bfe5bf");
                var tableRow = '<tr id="row'+ id +'"><td width="240px">' + bayList[index][row][col].containerNo + '</td><td id="tdTransport'+ id +'" width="240px"></td><td width="180px"><button onclick="pickTruck('+ id + ',' + index + ',' + col + ',' + row +')">Điều xe</button></td></tr>';
                $("#pickedContList").append(tableRow);
            } else {
                bayList[index][row][col].preorderPickup = "N";
                bayList[index][row][col].transportIds = null;
                $('#cell'+ id).css("background-color", "#ffffff");
                $("#row" + id).remove();
            }
            calculateMovingCont();
        }
    } else {
        $.modal.msgError("Container này chưa thông quan.");
    }
}

function pickTruck(id, bay, row, tier) {
    currentPickedId = id;
    currentPickedBay = bay;
    currentPickedRow = row;
    currentPickedTier = tier;
    $.modal.openCustomForm("Điều xe", prefix + "/pickTruckForm/" + shipmentId + "/" + true + "/" + id, 700, 400);
}

function finishPickTruck(plateNumber, driverIds) {
    $("#tdTransport" + currentPickedId).html(plateNumber);
    bayList[currentPickedBay][currentPickedTier][currentPickedRow].transportIds = driverIds;
    calculateMovingCont();
}

function calculateMovingCont() {
    preorderPickupConts = [];
    moveContAmount = 0;
    var moveContCol = 0;
    for (var b=0; b<bayList.length; b++) {
        var moveContAmountTemp = 0;
        for (var j=0; j<6; j++) {
            for (var i=4; i>=0; i--) {
                if (bayList[b][i][j] != null) {
                    if (bayList[b][i][j].preorderPickup == "Y") {
                        preorderPickupConts.push(bayList[b][i][j]);
                        moveContCol = moveContAmountTemp;
                    }
                    moveContAmountTemp++;
                }
            }
            moveContAmount += moveContCol;
            moveContCol = 0;
            moveContAmountTemp = 0;
        }
    }
    $("#pickedContAmount").html(moveContAmount);
    $("#totalCosts").html(moveContAmount*unitCosts);
}
