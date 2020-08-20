var prefix = ctx + "logistic/receive-cont-full";
var number = 0;
var shipmentDetailIds;
var asked = false;
var contAmount = 0;
var contResult = [];

if (contList == null) {
    var result = new Object();
    result.code = 1;
    result.msg = "Không có container nào cần khai hải quan.";
    parent.finishForm(result);
    $.modal.close();
}
function checkCustomStatus() {
    if (asked) {
        parent.reloadShipmentDetail();
        $.modal.close();
    } else {

        if (number == 0) {
            $.modal.alertError("Bạn chưa nhập số lượng tờ khai!");
        } else {
            let declareNoList = '';
            let completeInput = true;
            for (var i = 0; i < number; i++) {
                if ($("#declareNo" + i).val() == null || $("#declareNo" + i).val() == "") {
                    completeInput = false;
                    break;
                } else {
                    declareNoList += $("#declareNo" + i).val() + ',';
                }
            }
            if (!completeInput) {
                $.modal.alertError("Bạn chưa nhập đủ số tờ khai!");
            } else {
                for (var i = 0; i < number; i++) {
                    if (!/^[0-9]{12}$/g.test($("#declareNo" + i).val())) {
                        completeInput = false;
                        $("#declareNo" + i).addClass("errorInput");
                        completeInput = false;
                    }
                }
                if (completeInput) {
                    contAmount = contList.length;
                    connectToWebsocketServer();
                    //$.modal.loading("Đang kiểm tra trạng thái thông quan: 0/"+contList.length);
                    openLoading("Đang kiểm tra trạng thái thông quan: 0/"+contList.length);
                    setTimeout(() => {
                        asked = true;
                        $.ajax({
                            url: prefix + "/custom-status/shipment-detail/" + shipmentDetailIds.substring(0, shipmentDetailIds.length - 1),
                            method: "post",
                            data: {
                                declareNos: declareNoList.substring(0, declareNoList.length-1),
                            },
                                success: function (data) {
                            },
                            error: function (result) {
                                $("#checkBtn").html("Kết thúc");
                                $.modal.closeLoading();
                                $.modal.alertError("Có lỗi trong quá trình xử lý dữ liệu, vui lòng liên hệ admin.");
                            }
                        });
                    }, 2000);
                } else {
                    $.modal.alertError("Số tờ khai không hợp lệ.");
                }
            }
        }
    }
}

loadData() 
function closeForm() {
    parent.reloadShipmentDetail();
    $.modal.close();
}
function loadData() 
{
    $("#contTable").datagrid({
        singleSelect: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            shipmentDetailIds = "";
            var index = 0;
            contList.forEach(function (cont) {
                shipmentDetailIds += cont.id + ",";
                cont.id = ++index;
            });
            success(contList);
        },
    });
}


$("#declareNoAmount").keypress(function (event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if (keycode == '13') {
        event.preventDefault();
        number = parseInt($("#declareNoAmount").val(), 10);
        var declareNoForm = '';
        for (var i = 0; i < number; i++) {
            declareNoForm += ' <input id="declareNo' + i + '" class="declareNoInput" onfocus="onFocus(this)" type="text" placeholder="Tờ khai ' + (i + 1) + '"/>';
        }
        $("#declareNoForm").html(declareNoForm);
    }
});

function formatStatus(value) {
    switch (value) {
        case "R":
            return '<span class="label label-success">Đã thông quan</span>';
        case "N":
            return '<span class="label label-warning">Đang chờ</span>';
        case "Y":
            return '<span class="label label-danger">Chưa thông quan</span>';
        default :
            return'';
    }
}

function onFocus(element) {
    element.classList.remove("errorInput");
}

function connectToWebsocketServer(){
    // Connect to WebSocket Server.
    $.websocket.connect({}, onConnected, onError);
}

function onConnected() {
    console.log('Connect socket.')
    for (let i=0; i<contList.length; i++) {
        $.websocket.subscribe(contList[i].containerNo + '/response', onMessageReceived);
    }
}

function onError(error) {
    console.log(error);
    $.modal.alertError('Could not connect to WebSocket server. Please refresh this page to try again!');
    $.modal.closeLoading();
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    if (message.code == 0) {
        contResult.push(message.shipmentDetail);
        contAmount--;
        //$.modal.closeLoading();
        //$.modal.loading("Đang kiểm tra trạng thái thông quan: "+(contList.length-contAmount)+"/"+contList.length);
        changeTextLoading("Đang kiểm tra trạng thái thông quan: "+(contList.length-contAmount)+"/"+contList.length);
        if (contAmount == 0) {
            //$.modal.closeLoading();
            let shipmentId;
            contResult.forEach(function(value) {
                if (value.customStatus != 'R') {
                    shipmentId = value.shipmentId;
                    return false;
                }
            });
            if (shipmentId) {
                $.ajax({
                    url: prefix + "/shipment/" + shipmentId + "/custom/notification",
                    method: "GET"
                }).done(function(res) {
                    // done
                });
            }
            closeLoading();
            $("#contTable").datagrid({
                loadMsg: " Đang xử lý...",
                loader: function (param, success, error) {
                    success(contResult);
                },
            });
            $.modal.alertSuccess(message.msg);
            
            // loadData() 
            $("#checkBtn").html("Kết thúc");

            // Close websocket connection 
            $.websocket.disconnect(onDisconnected);
        }
    } else{
        $.modal.alertError(message.msg);
    }

    
}

function onDisconnected(){
    console.log('Disconnected socket.');
}

function openLoading(text) {
    $('.loader').show();
    $('#loading-text').text(text);
}

function closeLoading() {
    $('.loader').hide();
}

function changeTextLoading(text) {
    $('#loading-text').text(text);
}