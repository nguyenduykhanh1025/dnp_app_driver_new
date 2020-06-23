var prefix = ctx + "logistic/sendContFull";
var number = 0;
var shipmentDetailIds;
var asked = false;

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
            var declareNoList = [];
            var completeInput = true;
            for (var i = 0; i < number; i++) {
                if ($("#declareNo" + i).val() == null || $("#declareNo" + i).val() == "") {
                    completeInput = false;
                    break;
                } else {
                    declareNoList.push($("#declareNo" + i).val());
                }
            }
            if (!completeInput) {
                $.modal.alertError("Bạn chưa nhập đủ số tờ khai!");
            } else {
                for (var i = 0; i < number; i++) {
                    if (!/[0-9]{12}/g.test($("#declareNo" + i).val())) {
                        completeInput = false;
                        $("#declareNo" + i).addClass("errorInput");
                        completeInput = false;
                    }
                }
                if (completeInput) {
                    asked = true;
                    $(".loaderbox").css('display','block');
                    $.ajax({
                        url: prefix + "/checkCustomStatus",
                        method: "post",
                        data: {
                            declareNoList: declareNoList,
                            shipmentDetailIds: shipmentDetailIds.substring(0, shipmentDetailIds.length - 1)
                        },
                        success: function (data) {
                            // $.modal.closeLoading();
                            // if (data != null) {
                            //     $("#contTable").datagrid({
                            //         loadMsg: " Đang xử lý...",
                            //         loader: function (param, success, error) {
                            //             success(data);
                            //         },
                            //     });
                            // } else {
                            //     $.modal.msgError("Có lỗi xảy ra trong quá trình khai hải quan.");
                            // }
                            $(".loaderbox").css('display','none');
                            loadData() 
                            $("#checkBtn").html("Kết thúc");
                        },
                        error: function (result) {
                            $("#checkBtn").html("Kết thúc");
                            $.modal.closeLoading();
                            $.modal.alertError("Có lỗi trong quá trình xử lý dữ liệu, vui lòng liên hệ admin.");
                        }
                    });
                } else {
                    $.modal.msgError("Số tờ khai không hợp lệ.");
                }
            }
        }
    }
}

loadData() 
function closeForm() {
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
        case "TQ":
            return "Đã thông quan";
        case "CTQ":
            return "Chưa thông quan";
        default :
            return' <span class="label label-success">Đang chờ</span>';
    }
}

function onFocus(element) {
    element.classList.remove("errorInput");
}