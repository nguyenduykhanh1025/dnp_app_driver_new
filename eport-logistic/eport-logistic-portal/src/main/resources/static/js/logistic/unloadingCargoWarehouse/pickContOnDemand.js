var prefix = ctx + "logistic/unloading-cargo-warehouse";
var moveContAmount = 0;
var preorderPickupConts = [];
var currentPickedBay, currentPickedRow, currentPickedTier, currentPickedId;
var shipmentId;
var orders = [], orderNumber;

function confirm() {
    if (preorderPickupConts.length > 0) {
        $.modal.loading("Đang xử lý ...");
        $.modal.confirm("Xác nhận bốc container chỉ định (Quý khách<br>không thể hủy chỉ định cho container đã được chỉ định).", function() {
            $.ajax({
                url: prefix + "/shipment-detail/pickup-cont/" + $('#credit').prop('checked'),
                method: "post",
                contentType: "application/json",
                accept: 'text/plain',
                data: JSON.stringify(
                    preorderPickupConts
                ),
                dataType: 'text',
                success: function (data) {
                    let result = JSON.parse(data);
                    if (result.code == 301) {
                        $.modal.closeLoading();
                        parent.finishForm(result);
                        $.modal.close();
                    }
                    if (result.code != 0) {
                        $.modal.alertError(result.alert);
                    } else {
                        orders = result.orderIds;
                        orderNumber = orders.length;
                        connectToWebsocketServer();
                    }
                },
                error: function (result) {
                    $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau.");
                }
            });
        }, "Xác nhận");
    } else {
        $.modal.alertError("Quý khách chưa chọn container chỉ định.");
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

                // Position is empty
                if (bay[row][col].containerNo == null) {
                    str += '<div id="cell'+ bay[row][col].id +'" class="cellDiv" style="background-color: #dbcfcf;" onclick="pickCont('+ bay[row][col].id +', '+ row +','+ col + ',' + index + ',' + false +')">CONT</div>';
                    
                    // Container must be make into an order
                } else if (bay[row][col].status > 2) {

                    // Container has already been pre-order pickup
                    if (bay[row][col].preorderPickup == "Y") {
                        str += '<div id="cell'+ bay[row][col].id +'" style="background-color: #72ecea;" class="cellDiv" onclick="pickCont('+ bay[row][col].id +', '+ row +','+ col + ',' + index + ',' + true +')">'+ bay[row][col].containerNo +'</div>';
                        let tableRow = '<tr id="row'+ bay[row][col].id +'"><td width="330px">' + bay[row][col].containerNo + '</td><td width="165px">' + bay[row][col].sztp + '</td><td width="165px">' + bay[row][col].block + "-" + bay[row][col].bay + "-" + bay[row][col].row + "-" + bay[row][col].tier + '</td></tr>';
                        $("#pickedContList").append(tableRow);
                    
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
    calculateMovingCont();
});

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
    preorderPickupConts = [];
    moveContAmount = 0;
    for (let b=0; b<bayList.length; b++) {
        let moveContAmountTemp = 0;
        for (let j=0; j<6; j++) {
            for (let i=4; i>=0; i--) {
                if (bayList[b][i][j] != null) {
                    if (bayList[b][i][j].preorderPickup == "Y") {
                        preorderPickupConts.push(bayList[b][i][j]);
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
}

if (!isCredit) {
    $('#credit').hide();
    $('#creditLabel').hide();
} else {
    $('#credit').prop('checked', true);
}

function connectToWebsocketServer() {
    // Connect to WebSocket Server.
    $.websocket.connect({}, onConnected, onError);
}

function onConnected() {
    for (let i = 0; i < orders.length; i++) {
        $.websocket.subscribe(orders[i] + '/response', onMessageReceived);
    }
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    if (message.code != 0) {
        parent.finishForm(message);

        // Close loading
        $.modal.closeLoading();

        $.modal.close();

        // Close websocket connection 
        $.websocket.disconnect(onDisconnected);
    } else {
        orderNumber--;
        if (orderNumber == 0) {

            parent.finishForm(message.msg);

            // Close loading
            $.modal.closeLoading();

            $.modal.close();

            // Close websocket connection 
            $.websocket.disconnect(onDisconnected);
        }
    }
}

function onDisconnected() {
    console.log('Disconnected socket.');
} 

function onError(error) {
    console.error('Could not connect to WebSocket server. Please refresh this page to try again!');
}
