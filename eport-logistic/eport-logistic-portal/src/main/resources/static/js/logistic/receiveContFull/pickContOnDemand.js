var prefix = ctx + "logistic/receiveContFull";
var moveContAmount = 0;

$("#unitCosts").html(unitCosts);

function confirm() {
    parent.finishForm(data);
    $.modal.close();
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
    var moveContAmountTemp = 0;
    for (var j=0; j<7; j++) {
        for (var i=4; i>=0; i--) {
            if (containerList[i][j] != null) {
                if (i == row && j == col) {
                    break;
                } else {
                    moveContAmountTemp++;
                }
            }
        }
    }
    if (containerList[row][col].preorderPickup == "N") {
        containerList[row][col].preorderPickup = "Y";
        $('#cell'+ id).css("background-color", "#bfe5bf");
        var tableRow = '<tr id="row'+ id +'"><td width="50px">' + containerList[row][col].id + '</td><td width="150px">' + containerList[row][col].containerNo + '</td><td width="150px"></td><td width="100px"><button onclick="pickTruck()">Điều xe</button></td></tr>';
        $("#pickedContList").append(tableRow);
        moveContAmount += moveContAmountTemp;
    } else {
        containerList[row][col].preorderPickup = "N";
        $('#cell'+ id).css("background-color", "#ffffff");
        $("#row" + id).remove();
        moveContAmount -= moveContAmountTemp;
    }
    $("#pickedContAmount").html(moveContAmount);
    $("#totalCosts").html(moveContAmount*unitCosts);
}
