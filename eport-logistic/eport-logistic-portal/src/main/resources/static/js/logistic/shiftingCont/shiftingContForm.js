var prefix = ctx + "logistic/shifting-cont";
var moveContAmount = 0;
var shipmentDetailIds;
var shipmentId;
var orders = [], orderNumber;

function confirm() {
    if (moveContAmount > 0) {
        parent.otp(shipmentDetailIds, $('#credit').prop('checked')?"1":"0");
        $.modal.close();
    } else {
        $.modal.alertWarning("Không phát sinh dịch chuyển container.");
    }
}

function closeForm() {
    parent.reloadShipmentDetail();
    $.modal.close();
}

var index = 0;
console.log(bayList);
bayList.forEach(function(bay) {
    let level = 1;
    let str = '<div class="bayPosition">';
    let bayName = "";
    for (let col=0; col<bay[0].length; col++) {
        if (col > 5 && level == 1) {
            level++;
            str += '</div><div style="margin-bottom: 10px;"><b>'+ bayName +'</b></div>';
            if (bayName) {
                $(".contListPosition").append(str);
            }
            str = '<div class="bayPosition">';
            bayName = "";
        }
        str += '<div class="columnDiv">';
        for (let row=4; row>=0; row--) {
            if (bay[row][col] != null) {
                bay[row][col].expiredDem = null;
                bayName = bay[row][col].block + "-" + bay[row][col].bay;
                shipmentId = bay[row][col].shipmentId;

                // Position is empty
                if (bay[row][col].containerNo == null) {
                    str += '<div id="cell'+ bay[row][col].id +'" class="cellDiv" style="background-color: #dbcfcf;" onclick="pickCont('+ bay[row][col].id +', '+ row +','+ col + ',' + index + ',' + false +')">CONT</div>';
                    
                    // Container must be make into an order
                } else if (bay[row][col].status > 2) {

                    // Container has already been pre-order pickup
                    if (bay[row][col].preorderPickup == "Y") {
                        str += '<div id="cell'+ bay[row][col].id +'" style="background-color: #72ecea;" class="cellDiv" onclick="pickCont('+ bay[row][col].id +', '+ row +','+ col + ',' + index + ',' + true +')">'+ bay[row][col].containerNo +'</div>';
                        // let tableRow = '<tr id="row'+ bay[row][col].id +'"><td width="330px">' + bay[row][col].containerNo + '</td><td width="165px">' + bay[row][col].sztp + '</td><td width="165px">' + bay[row][col].block + "-" + bay[row][col].bay + "-" + bay[row][col].row + "-" + bay[row][col].tier + '</td></tr>';
                        // $("#pickedContList").append(tableRow);
                    
                    // Container can be ready to pre-order pickup
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
});
calculateMovingCont();

function pickCont(id, row, col, index, isPicked) {
    if (bayList[index][row][col].containerNo == null) {
        $.modal.alertError("Container này không nằm trong lô của quý<br>khách.");
    } else if (bayList[index][row][col].status > 2) {
        if (isPicked) {
            $.modal.alertError("Container này đã được chỉ định.");
        } else {
            if (bayList[index][row][col].preorderPickup == "N") {
                bayList[index][row][col].preorderPickup = "Y";
                $('#cell'+ id).css("background-color", "#bfe5bf");
                let tableRow = '<tr id="row'+ id +'"><td width="330px">' + bayList[index][row][col].containerNo + '</td><td width="165px">' + bayList[index][row][col].sztp + '</td><td width="165px">' + bayList[index][row][col].block + "-" + bayList[index][row][col].bay + "-" + bayList[index][row][col].row + "-" + bayList[index][row][col].tier +'</td></tr>';
                $("#pickedContList").append(tableRow);
            } else {
                bayList[index][row][col].preorderPickup = "N";
                bayList[index][row][col].transportIds = null;
                $('#cell'+ id).css("background-color", "#ffffff");
                $("#row" + id).remove();
            }
            calculateMovingCont();
        }
    } else if (bayList[index][row][col].status == 2) {
        $.modal.alertError("Container này chưa được làm lệnh.");
    } else {
        $.modal.alertError("Container này chưa thông quan.");
    }
}

function calculateMovingCont() {
    shipmentDetailIds = "";
    moveContAmount = 0;
    for (let b=0; b<bayList.length; b++) {
        let moveContAmountTemp = 0;
        let level = 1;
        for (let j=0; j<bayList[b][0].length; j++) {
            if (j > 5 && level == 1) {
                level++;
                moveContAmountTemp = 0;
            }
            for (let i=4; i>=0; i--) {
                if (bayList[b][i][j] != null) {
                    if (bayList[b][i][j].preorderPickup == "Y" && "N" !=  bayList[b][i][j].prePickupPaymentStatus) {
                        shipmentDetailIds += bayList[b][i][j].id + ",";
                        moveContAmount += moveContAmountTemp;
                        moveContAmountTemp = 0;
                    } else {
                        moveContAmountTemp++;
                    }
                }
            }
            moveContAmountTemp = 0;
        }
    }
    if (shipmentDetailIds.length > 0) {
        shipmentDetailIds.substring(0, shipmentDetailIds.length-1);
    }
    $('#shiftingContAmount').html(moveContAmount+".");
}

if (!isCredit) {
    $('#credit').hide();
    $('#creditLabel').hide();
} else {
    $('#credit').prop('checked', true);
}


