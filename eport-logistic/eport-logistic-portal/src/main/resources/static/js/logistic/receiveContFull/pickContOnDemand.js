var prefix = ctx + "logistic/receive-cont-full";
var moveContAmount = 0;
var preorderPickupConts = [];
var currentPickedBay, currentPickedRow, currentPickedTier, currentPickedId;
var shipmentId;

$("#unitCosts").html(unitCosts);

function confirm() {
    if (preorderPickupConts.length > 0) {
        $.modal.confirm("Xác nhận bốc container chỉ định (Quý khách<br>không thể hủy chỉ định cho container đã được chỉ định).", function() {
            $.ajax({
                url: prefix + "/shipment-detail/pickup-cont",
                method: "post",
                contentType: "application/json",
                accept: 'text/plain',
                data: JSON.stringify(preorderPickupConts),
                dataType: 'text',
                success: function (data) {
                    let result = JSON.parse(data);
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
    parent.reloadShipmentDetail();
    $.modal.close();
}

var index = 0;
bayList.forEach(function(bay) {
    let str = '<div class="bayPosition">';
    let bayName = "";
    for (let col=0; col<6; col++) {
        str += '<div class="columnDiv">';
        for (let row=4; row>=0; row--) {
            if (bay[row][col] != null) {
                bay[row][col].expiredDem = null;
                bayName = bay[row][col].block + "-" + bay[row][col].bay;
                shipmentId = bay[row][col].shipmentId;
                if (bay[row][col].containerNo == null) {
                    str += '<div id="cell'+ bay[row][col].id +'" class="cellDiv" style="background-color: #dbcfcf;" onclick="pickCont('+ bay[row][col].id +', '+ row +','+ col + ',' + index + ',' + false +')">CONT</div>';
                } else if (bay[row][col].status > 1 && bay[row][col].status < 4) {
                    if (bay[row][col].preorderPickup == "Y") {
                        str += '<div id="cell'+ bay[row][col].id +'" style="background-color: #72ecea;" class="cellDiv" onclick="pickCont('+ bay[row][col].id +', '+ row +','+ col + ',' + index + ',' + true +')">'+ bay[row][col].containerNo +'</div>';
                        let tableRow = '<tr id="row'+ bay[row][col].id +'"><td width="330px">' + bay[row][col].containerNo + '</td><td width="330px">' + bay[row][col].sztp + '</td></tr>';
                        $("#pickedContList").append(tableRow);
                    } else {
                        str += '<div id="cell'+ bay[row][col].id +'" class="cellDiv" onclick="pickCont('+ bay[row][col].id +', '+ row +','+ col + ',' + index + ',' + false +')">'+ bay[row][col].containerNo +'</div>';
                    }
                } else if (bay[row][col].status > 3) {
                    str += '<div id="cell'+ bay[row][col].id +'" class="cellDiv" style="background-color: #dbcfcf;" onclick="pickCont('+ bay[row][col].id +', '+ row +','+ col + ',' + index + ',' + false +')">'+ bay[row][col].containerNo +'</div>';
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
    } else if (bayList[index][row][col].status > 3) {
        $.modal.msgError("Container này đã được thanh toán.");
    } else if (bayList[index][row][col].status > 1) {
        if (isPicked) {
            $.modal.msgError("Container này đã được chỉ định.");
        } else {
            if (bayList[index][row][col].preorderPickup == "N") {
                bayList[index][row][col].preorderPickup = "Y";
                $('#cell'+ id).css("background-color", "#bfe5bf");
                let tableRow = '<tr id="row'+ id +'"><td width="330px">' + bayList[index][row][col].containerNo + '</td><td width="330px">' + bayList[index][row][col].sztp + '</td></tr>';
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

function calculateMovingCont() {
    preorderPickupConts = [];
    moveContAmount = 0;
    let moveContCol = 0;
    for (let b=0; b<bayList.length; b++) {
        let moveContAmountTemp = 0;
        for (let j=0; j<6; j++) {
            for (let i=4; i>=0; i--) {
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
