var prefix = ctx + "logistic/receiveContFull";
var number = 0;
var shipmentDetailIds;
function checkCustomStatus() {
    if (number == 0) {
        $.modal.alertError("Bạn chưa nhập số lượng tờ khai!");
    } else {
        var declareNoList = [];
        var completeInput = true;
        for (var i=0; i<number; i++) {
            if ($("#declareNo" + i).val() == null || $("#declareNo" + i).val() == "") {
                completeInput = false;
                break;
            } else {
                declareNoList.push($("#declareNo" + i).val());
            }
        }
        if (completeInput) {
            $.ajax({
                url: prefix + "/checkCustomStatus",
                method: "post",
                data: {
                    declareNoList: declareNoList,
                    shipmentDetailIds: shipmentDetailIds.substring(0, shipmentDetailIds.length-1)
                },
                success: function (data) {
                    $("#contTable").datagrid({
                        loadMsg: " Đang xử lý...",
                        loader: function (param, success, error) {
                            var opts = $(this).datagrid("options");
                            success(data);
                        },
                    });
                },
                error: function (result) {
                    $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
                }
            });
        } else {
            $.modal.alertError("Bạn chưa nhập đủ số tờ khai!");
        }
    }
}

function closeForm() {
    $.modal.close();
}

$("#contTable").datagrid({
    loadMsg: " Đang xử lý...",
    loader: function (param, success, error) {
        shipmentDetailIds = "";
        var index = 0;
        contList.forEach(function(cont) {
            shipmentDetailIds += cont.id + ",";
            cont.id = ++index;
        });
        success(contList);
    },
});

$("#declareNoAmount").keypress(function (event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if (keycode == '13') {
        event.preventDefault();
        number = parseInt($("#declareNoAmount").val(), 10);
        var declareNoForm = '';
        for (var i=0; i<number; i++) {
            declareNoForm += ' <input id="declareNo' + i + '" class="declareNoInput" type="text" placeholder="Tờ khai ' + (i+1) + '"/>';
        }
        $("#declareNoForm").html(declareNoForm);
    }
});

function formatStatus(value) {
	switch (value) {
        case "Y":
            return "Đã thông quan";
        case "N":
            return "Chưa thông quan";
    }
}