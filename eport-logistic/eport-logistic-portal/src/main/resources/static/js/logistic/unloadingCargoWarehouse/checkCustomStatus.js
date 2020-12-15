var prefix = ctx + "logistic/unloading-cargo-warehouse";
// Number of custom declare no need to input, default by 1.
var rowAmount = 1;
// Handsontable instance
var dogrid = document.getElementById("container-grid"), hot, config;
// String shipment detail id list separated by comma.
var shipmentDetailIds;

// To check before and after scan custom declare no, asked = true when complete scan custom
var asked = false;

// The number of cont need to scan
var contAmount = 0;
// Array list object shipmentDetail return from web socket, when complete use this array to reload easy ui datagrid on the left
var contResult = [];
// List custom declare no separated by comma
var declareNoList;

$(document).ready(function () {
    loadData();
});

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
        if (getDataFromTable()) {
            // Begin scan custom declare nos
            contAmount = contList.length;
            connectToWebsocketServer();
            openLoading("Đang kiểm tra trạng thái thông quan: 0/" + contList.length);
            setTimeout(() => {
                asked = true;
                $.ajax({
                    url: prefix + "/custom-status/shipment-detail",
                    method: "post",
                    data: {
                        declareNos: declareNoList,
                        shipmentDetailIds: shipmentDetailIds.substring(0, shipmentDetailIds.length - 1)
                    },
                    success: function (data) {
                    },
                    error: function (result) {
                        $("#checkBtn").html("Kết thúc");
                        $.modal.closeLoading();
                        $.modal.alertError("Có lỗi trong quá trình xử lý dữ liệu, vui lòng thử lại sau.");
                    }
                });
            }, 2000);
        }
    }
}

function closeForm() {
    parent.reloadShipmentDetail();
    $.modal.close();
}
function loadData() {
    $("#contTable").datagrid({
        singleSelect: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            shipmentDetailIds = "";
            var index = 0;
            let customNoReference = contList[0].customsNo;
            let customNoSavedCheck = true;
            contList.forEach(function (cont) {
                shipmentDetailIds += cont.id + ",";
                cont.id = ++index;
                if (!cont.customsNo || cont.customsNo != customNoReference) {
                    customNoSavedCheck = false;
                }
            });
            success(contList);
            let customNoList = [];
            if (customNoSavedCheck) {
                let customNoArr = customNoReference.split(",");
                customNoArr.forEach(function (customNo, index) {
                    customNoList.push({ customNo: customNo });
                });
                $('#declareNoAmount').val(customNoArr.length);
                rowAmount = customNoArr.length;
            }
            configHandson();
            hot = new Handsontable(dogrid, config);
            hot.loadData(customNoList);
            hot.render();
        },
    });
}

function formatStatus(value) {
    switch (value) {
        case "R":
            return '<span class="label label-success">Đã thông quan</span>';
        case "N":
            return '<span class="label label-warning">Đang chờ</span>';
        case "Y":
            return '<span class="label label-danger">Chưa thông quan</span>';
        default:
            return '';
    }
}

function onFocus(element) {
    element.classList.remove("errorInput");
}

function connectToWebsocketServer() {
    // Connect to WebSocket Server.
    $.websocket.connect({}, onConnected, onError);
}

function onConnected() {
    console.log('Connect socket.')
    for (let i = 0; i < contList.length; i++) {
        $.websocket.subscribe(contList[i].containerNo + '/response', onMessageReceived);
    }
}

function onError(error) {
    console.log(error);
    $.modal.alertError('Không thể kết nối đến server kết quả. Vui lòng thử lại sau!');
    $.modal.closeLoading();
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    if (message.code == 0) {
        contResult.push(message.shipmentDetail);
        contAmount--;
        changeTextLoading("Đang kiểm tra trạng thái thông quan: " + (contList.length - contAmount) + "/" + contList.length);
        if (contAmount == 0) {
            //$.modal.closeLoading();
            let shipmentId;
            contResult.forEach(function (value) {
                if (value.customStatus != 'R') {
                    shipmentId = value.shipmentId;
                    return false;
                }
            });
            if (shipmentId) {
                $.ajax({
                    url: prefix + "/shipment/" + shipmentId + "/custom/notification",
                    method: "GET"
                }).done(function (res) {
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
            $.modal.alertSuccess("Hoàn tất quét tờ khai.");

            $("#checkBtn").html("Kết thúc");

            // Close websocket connection 
            $.websocket.disconnect(onDisconnected);
        }
    } else {
        $.modal.alertError(message.msg);
    }


}

function onDisconnected() {
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

function customNoRenderer(instance, td, row, col, prop, value, cellProperties) {
    $(td).attr('id', 'customNo' + row).addClass("htMiddle").addClass("htCenter");
    if (value) {
        if (!/^[0-9]{12}$/g.test(value)) {
            $(td).css("background-color", "red");
        }
    }
    $(td).html(value);
    return td;
}
function configHandson() {
    config = {
        stretchH: "all",
        height: $('#declareNoForm').height(),
        minRows: rowAmount,
        maxRows: rowAmount,
        width: $('#declareNoForm').width(),
        minSpareRows: 0,
        rowHeights: 30,
        trimDropdown: false,
        manualColumnResize: true,
        manualRowResize: true,
        renderAllRows: true,
        rowHeaders: true,
        className: "htCenter",
        colHeaders: function (col) {
            switch (col) {
                case 0:
                    return "Số Tờ Khai";
            }
        },
        colWidths: [40],
        columns: [
            {
                data: "customNo",
                renderer: customNoRenderer
            },
        ],
        beforeKeyDown: function (e) {
            let selected = hot.getSelected()[0];
            switch (e.keyCode) {
                // Arrow Left
                case 37:
                    if (selected[3] == 0) {
                        e.stopImmediatePropagation();
                    }
                    break;
                // Arrow Up
                case 38:
                    if (selected[2] == 0) {
                        e.stopImmediatePropagation();
                    }
                    break;
                // Arrow Right
                case 39:
                    if (selected[3] == 1) {
                        e.stopImmediatePropagation();
                    }
                    break
                // Arrow Down
                case 40:
                    if (selected[2] == rowAmount - 1) {
                        e.stopImmediatePropagation();
                    }
                    break
                default:
                    break;
            }
        },
    };
}

$("#declareNoAmount").on("input", function () {
    if ($("#declareNoAmount").val()) {
        try {
            let oldSource = hot.getSourceData();
            rowAmount = parseInt($("#declareNoAmount").val(), 10);
            if (rowAmount <= 100) {
                hot.destroy();
                configHandson();
                hot = new Handsontable(dogrid, config);
                hot.loadData(oldSource);
                hot.render();
            } else {
                rowAmount = 100;
                hot.destroy();
                configHandson();
                hot = new Handsontable(dogrid, config);
                hot.loadData(oldSource);
                hot.render();
                $.modal.msgError("Số lượng số tờ khai có thể nhập không được vượt qua 100.");
            }
        } catch (err) {
            $.modal.alertWarning("Ký tự bạn nhập vào không phải là số.");
        }
    }
});

function getDataFromTable(isValidate) {
    let myTableData = hot.getSourceData();
    myTableData.splice(rowAmount, myTableData.length);
    let errorFlg = false;

    declareNoList = '';
    let count = 0;
    $.each(myTableData, function (index, object) {
        if (object["customNo"]) {
            if (declareNoList.includes(object["customNo"])) {
                $.modal.alertWarning("Hàng " + (index + 1) + ": bị trùng số tờ khai " + object["customNo"] + ".");
                errorFlg = true;
                return false;
            }
            declareNoList += object["customNo"] + ",";
            count++;
        }
    });

    if (count != rowAmount && !errorFlg) {
        errorFlg = true;
        $.modal.alertWarning("Chưa nhập đủ số tờ khai.<br>Đã nhập: " + count + "/" + rowAmount);
    }

    if (declareNoList.length == 0 && !errorFlg) {
        errorFlg = true;
    } else {
        declareNoList = declareNoList.substring(0, declareNoList.length - 1);
    }

    if (errorFlg) {
        return false;
    } else {
        return true;
    }
}
