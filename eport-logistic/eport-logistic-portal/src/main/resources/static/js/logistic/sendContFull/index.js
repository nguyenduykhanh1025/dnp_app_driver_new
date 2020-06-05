var isChange = true;
var prefix = ctx + "logistic/sendContFull";
var fromDate = "";
var toDate = "";
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected;
var shipmentDetails;
var shipmentDetailIds;
var contList = [];
var checked = false;
var allChecked = true;
var isIterate = false;
$(document).ready(function () {
    // $(".colHeader > input[type=checkbox]").click(function() {
    //   console.log("inini")
    // })
    loadTable();
    $(".left-side").css("height", $(document).height());
    $("#btn-collapse").click(function () {
        handleCollapse(true);
    });
    $("#btn-uncollapse").click(function () {
        handleCollapse(false);
    });
});

function loadTable() {
    $("#dg").datagrid({
        url: prefix + "/listShipment",
        height: window.innerHeight - 70,
        singleSelect: true,
        collapsible: true,
        clientPaging: false,
        pagination: true,
        onClickRow: function () {
            getSelected();
        },
        pageSize: 50,
        nowrap: false,
        striped: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            var opts = $(this).datagrid("options");
            if (!opts.url) return false;
            $.ajax({
                type: opts.method,
                url: opts.url,
                data: {
                    pageNum: param.page,
                    pageSize: param.rows,
                    orderByColumn: param.sort,
                    isAsc: param.order,
                },
                dataType: "json",
                success: function (data) {
                    success(data);
                    $("#dg").datagrid("hideColumn", "id");
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });
}

function formatRemark(value) {
    return '<div class="easyui-tooltip" title="'+ ((value!=null&&value!="")?value:"không có ghi chú") +'" style="width: 80; text-align: center;"><span>'+ (value!=null?(value.substring(0, 5) + "..."):"...") +'</span></div>';
  }

function handleCollapse(status) {
    if (status) {
        $(".left-side").css("width", "0.5%");
        $(".left-side").children().hide();
        $("#btn-collapse").hide();
        $("#btn-uncollapse").show();
        $(".right-side").css("width", "99%");
        setTimeout(function () {
            hot.render();
        }, 500);
        return;
    }
    $(".left-side").css("width", "33%");
    $(".left-side").children().show();
    $("#btn-collapse").show();
    $("#btn-uncollapse").hide();
    $(".right-side").css("width", "67%");
    setTimeout(function () {
        hot.render();
    }, 500);
}

config = {
    stretchH: "all",
    height: document.documentElement.clientHeight - 100,
    minRows: 100,
    width: "100%",
    minSpareRows: 1,
    rowHeights: 30,
    manualColumnMove: false,
    rowHeaders: true,
    className: "htMiddle",
    colHeaders: function (col) {
        switch (col) {
            case 0:
                var txt = "<input type='checkbox' class='checker' ";
                txt += "onclick='checkAll()' ";
                txt += ">";
                return txt;
            case 1:
                return "id";
            case 2:
                return "Container No";
            case 3:
                return "T.T Thanh Toán";
            case 4:
                return "T.T Làm Lệnh";
            case 5:
                return "T.T Hạ Cont";
            case 6:
                return "Kích Thước";
            case 7:
                return "Chủ hàng";
            case 8:
                return "Hạn Lệnh";
            case 9:
                return "Cảng Nguồn";
            case 10:
                return "Cảng Đích";
            case 11:
                return "Phương Tiện";
            case 12:
                return "Nơi Hạ Vỏ";
            case 13:
                return "Ghi Chú";
        }
    },
    colWidths: [50, 0.01, 100, 150, 150, 150, 100, 100, 100, 100, 100, 100, 100, 200],
    filter: "true",
    columns: [
        {
            data: "active",
            type: "checkbox",
            className: "htCenter",
        },
        {
            data: "id",
            editor: false
        },
        {
            data: "containerNo",
        },
        {
            data: "paymentStatus",
            editor: false
        },
        {
            data: "processStatus",
            editor: false
        },
        {
            data: "status",
            editor: false,
        },
        {
            data: "sztp",
        },
        {
            data: "consignee"
        },
        {
            data: "expiredDem",
            type: "date",
            dateFormat: "DD/MM/YYYY",
            correctFormat: true,
            defaultDate: new Date(),
        },
        {
            data: "loadingPort",
        },
        {
            data: "dischargePort",
        },
        {
            data: "transportType",
        },
        {
            data: "emptyDepot",
        },
        {
            data: "remark",
        },
    ],
    afterChange: function (changes, src) {
        //Get data change in cell to render another column
        if (src !== "loadData") {
            var verifyStatus = false;
            var paymentStatus = false;
            var notVerify = false;
            changes.forEach(function interate(row) {
                if (row[1] == "active" && !isIterate) {
                    getDataSelectedFromTable(false);
                    if (allChecked) {
                        $(".checker").prop("checked", true);
                        checked = true;
                    } else {
                        $(".checker").prop("checked", false);
                        checked = false;
                    }
                    if (shipmentDetails.length > 0) {
                        var status = 1;
                        for (var i = 0; i < shipmentDetails.length; i++) {
                            if (shipmentDetails[i].paymentStatus == "Đã thanh toán") {
                                if (verifyStatus || notVerify) {
                                    status = 1;
                                } else {
                                    status = 4;
                                    paymentStatus = true;
                                }
                            } else if (shipmentDetails[i].processStatus == "Đã làm lệnh") {
                                if (paymentStatus || notVerify) {
                                    status = 1;
                                } else {
                                    status = 3;
                                    verifyStatus = true;
                                }
                            } else {
                                if (verifyStatus || paymentStatus) {
                                    status = 1;
                                } else {
                                    status = 2;
                                }
                                notVerify = true;
                            }
                        }
                        switch (status) {
                            case 1:
                                setLayoutRegisterStatus();
                                break;
                            case 2:
                                setLayoutVerifyUser();
                                break;
                            case 3:
                                setLayoutPaymentStatus();
                                break;
                            case 4:
                                setLayoutFinish();
                                break;
                        }
                    } else {
                        setLayoutRegisterStatus();
                    }
                }
            });
        }
    },
};

function checkAll() {
    getDataFromTable(false);
    isIterate = true;
    if (checked) {
        for (var i = 0; i < shipmentDetails.length; i++) {
            hot.setDataAtCell(i, 0, false);
            if (i == shipmentDetails.length - 2) {
                isIterate = false;
            }
        }
        $(".checker").prop("checked", false);
        checked = false;
    } else {
        for (var i = 0; i < shipmentDetails.length; i++) {
            hot.setDataAtCell(i, 0, true);
            if (i == shipmentDetails.length - 2) {
                isIterate = false;
            }
        }
        $(".checker").prop("checked", true);
        checked = true;
    }
}

hot = new Handsontable(dogrid, config);

function loadShipmentDetail(id) {
    $.ajax({
        url: prefix + "/listShipmentDetail",
        method: "GET",
        data: {
            shipmentId: id
        },
        success: function (result) {
            result.forEach(function iterate(shipmentDetail) {
                if (shipmentDetail.expiredDem != null && shipmentDetail.expiredDem != '') {
                    shipmentDetail.expiredDem = shipmentDetail.expiredDem.substring(8, 10) + "/" + shipmentDetail.expiredDem.substring(5, 7) + "/" + shipmentDetail.expiredDem.substring(0, 4);
                }
                switch (shipmentDetail.paymentStatus) {
                    case "N":
                        shipmentDetail.paymentStatus = "Chưa thanh toán";
                        break;
                    case "Y":
                        shipmentDetail.paymentStatus = "Đã thanh toán";
                        break;
                    default:
                        break;
                }
                switch (shipmentDetail.processStatus) {
                    case "N":
                        shipmentDetail.processStatus = "Chưa làm lệnh";
                        break;
                    case "Y":
                        shipmentDetail.processStatus = "Đã làm lệnh";
                        break;
                    default:
                        break;
                }
                if (shipmentDetail.status != 4) {
                    shipmentDetail.status = "Chưa hạ container";
                } else {
                    shipmentDetail.status = "Đã hạ container";
                }
            });
            hot.loadData(result);
            hot.render();
            setLayoutRegisterStatus();
        }
    });
}

function formatDate(value) {
    var date = new Date(value);
    var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    var month = date.getMonth() + 1;
    var monthText = month < 10 ? "0" + month : month;
    return day + "/" + monthText + "/" + date.getFullYear();
}

// Handle add
$(function () {
    var options = {
        createUrl: prefix + "/addShipmentForm",
        updateUrl: "0",
        modalName: " Lô"
    };
    $.table.init(options);
});

function handleRefresh() {
    loadTable();
}

function getSelected() {
    var row = $("#dg").datagrid("getSelected");
    if (row) {
        shipmentSelected = row;
        $(function () {
            var options = {
                createUrl: prefix + "/addShipmentForm",
                updateUrl: prefix + "/editShipmentForm/" + shipmentSelected.id,
                modalName: " Lô"
            };
            $.table.init(options);
        });
        $("#loCode").text(row.id);
        $("#taxCode").text(row.taxCode);
        $("#quantity").text(row.containerAmount);
        if (row.edoFlg == "0") {
            $("#dotype").text("DO giấy");
        } else {
            $("#dotype").text("EDO");
        }
        $("#blNo").text(row.blNo);
        loadShipmentDetail(row.id);
    }
}

function getDataSelectedFromTable(isValidate) {
    var myTableData = hot.getSourceData();
    var errorFlg = false;
    if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
        hot.alter("remove_row", parseInt(myTableData.length - 1), (keepEmptyRows = false));
    }
    var cleanedGridData = [];
    allChecked = true;
    $.each(myTableData, function (rowKey, object) {
        if (!hot.isEmptyRow(rowKey)) {
            if (object["active"]) {
                cleanedGridData.push(object);
            } else {
                allChecked = false;
            }
        }
    });
    shipmentDetailIds = "";
    shipmentDetails = [];
    $.each(cleanedGridData, function (index, object) {
        var shipmentDetail = new Object();
        if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"]) && isValidate) {
            $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
            errorFlg = true;
        }
        shipmentDetail.containerNo = object["containerNo"];
        shipmentDetail.processStatus = object["processStatus"];
        shipmentDetail.paymentStatus = object["paymentStatus"];
        shipmentDetail.shipmentId = shipmentSelected.id;
        shipmentDetail.id = object["id"];
        shipmentDetails.push(shipmentDetail);
        shipmentDetailIds += object["id"] + ",";
    });

    // Get result in "selectedList" variable
    if (shipmentDetails.length == 0 && isValidate) {
        $.modal.alert("Bạn chưa chọn container.");
        errorFlg = true;
    } else {
        shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length - 1);
    }
    if (errorFlg) {
        return false;
    }
}

function getDataFromTable(isValidate) {
    var myTableData = hot.getSourceData();
    var errorFlg = false;
    if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
        hot.alter("remove_row", parseInt(myTableData.length - 1), (keepEmptyRows = false));
    }
    var cleanedGridData = [];
    $.each(myTableData, function (rowKey, object) {
        if (!hot.isEmptyRow(rowKey)) {
            cleanedGridData.push(object);
        }
    });
    shipmentDetails = [];
    contList = [];
    $.each(cleanedGridData, function (index, object) {
        var shipmentDetail = new Object();
        if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"]) && isValidate) {
            $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
            errorFlg = true;
        }
        var expiredDem = new Date(object["expiredDem"].substring(6, 10) + "/" + object["expiredDem"].substring(3, 5) + "/" + object["expiredDem"].substring(0, 2));
        shipmentDetail.containerNo = object["containerNo"];
        contList.push(object["containerNo"]);
        shipmentDetail.sztp = object["sztp"];
        shipmentDetail.consignee = object["consignee"];
        shipmentDetail.expiredDem = expiredDem.getTime();
        shipmentDetail.loadingPort = object["loadingPort"];
        shipmentDetail.dischargePort = object["dischargePort"];
        shipmentDetail.transportType = object["transportType"];
        shipmentDetail.emptyDepot = object["emptyDepot"];
        shipmentDetail.remark = object["remark"];
        shipmentDetail.shipmentId = shipmentSelected.id;
        shipmentDetail.id = object["id"];
        shipmentDetails.push(shipmentDetail);
        var now = new Date();
        now.setHours(0, 0, 0);
        expiredDem.setHours(23, 59, 59);
        if (expiredDem.getTime() < now.getTime() && isValidate) {
            errorFlg = true;
            $.modal.alertError("Hàng " + (index + 1) + ": Hạn lệnh không được trong quá khứ!")
        }
    });

    if (isValidate) {
        contList.sort();
        var contTemp = "";
        $.each(contList, function (index, cont) {
            if (cont != "" && cont == contTemp) {
                $.modal.alertError("Số container không được giống nhau!");
                errorFlg = true;
                return false;
            }
            contTemp = cont;
        });
    }

    // Get result in "selectedList" variable
    if (shipmentDetails.length == 0) {
        $.modal.alert("Bạn chưa nhập thông tin.");
        errorFlg = true;
    }

    if (errorFlg) {
        return false;
    } else {
        return true;
    }
}

function saveShipmentDetail() {
    if (shipmentSelected == null) {
        $.modal.msgError("Bạn cần chọn lô trước");
        return;
    } else {
        if (getDataFromTable(true) && shipmentDetails.length > 0 && shipmentDetails.length <= shipmentSelected.containerAmount) {
            $.modal.loading("Đang xử lý...");
            $.ajax({
                url: prefix + "/saveShipmentDetail",
                method: "post",
                contentType: "application/json",
                accept: 'text/plain',
                data: JSON.stringify(shipmentDetails),
                dataType: 'text',
                success: function (data) {
                    var result = JSON.parse(data);
                    if (result.code == 0) {
                        $.modal.msgSuccess(result.msg);
                        loadShipmentDetail(shipmentSelected.id);
                    } else {
                        $.modal.msgError(result.msg);
                    }
                    $.modal.closeLoading();
                },
                error: function (result) {
                    $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
                    $.modal.closeLoading();
                },
            });
        } else if (shipmentDetails.length > shipmentSelected.containerAmount) {
            $.modal.alertError("Số container nhập vào vượt quá số container<br>của lô.");
        } else {
            $.modal.alertError("Quý khách chưa nhập thông tin chi tiết lô.");
        }
    }
}

// Handling logic
function verify() {
    getDataSelectedFromTable(true);
    if (shipmentDetails.length > 0) {
        $.modal.openCustomForm("Xác nhận làm lệnh", prefix + "/checkContListBeforeVerify/" + shipmentDetailIds, 600, 500);
    }
}

function verifyOtp(shipmentDtIds) {
    $.modal.openCustomForm("Xác thực OTP", prefix + "/verifyOtpForm/" + shipmentDtIds, 600, 350);
}

function pay() {
    $.modal.openCustomForm("Thanh toán", prefix + "/paymentForm/" + shipmentDetailIds, 700, 300);
}

function exportBill() {

}

function reloadShipmentDetail() {
    loadShipmentDetail(shipmentSelected.id);
}

// Handling UI
function setLayoutRegisterStatus() {
    $("#registerStatus").removeClass("label-primary disable").addClass("active");
    $("#verifyStatus").removeClass("label-primary active").addClass("disable");
    $("#paymentStatus").removeClass("label-primary active").addClass("disable");
    $("#finishStatus").removeClass("label-primary active").addClass("disable");
    $("#saveShipmentDetailBtn").prop("disabled", false);
    $("#verifyBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", true);
    $("#exportBillBtn").prop("disabled", true);
}

function setLayoutVerifyUser() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("label-primary disable").addClass("active");
    $("#paymentStatus").removeClass("label-primary active").addClass("disable");
    $("#finishStatus").removeClass("label-primary active").addClass("disable");
    // $("#saveShipmentDetailBtn").prop("disabled", false);
    $("#verifyBtn").prop("disabled", false);
    $("#payBtn").prop("disabled", true);
    $("#exportBillBtn").prop("disabled", true);
}

function setLayoutPaymentStatus() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("active disable").addClass("label-primary");
    $("#paymentStatus").removeClass("label-primary disable").addClass("active");
    $("#finishStatus").removeClass("label-primary active").addClass("disable");
    // $("#saveShipmentDetailBtn").prop("disabled", true);
    $("#verifyBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", false);
    $("#exportBillBtn").prop("disabled", true);
}

function setLayoutFinish() {
    $("#registerStatus").removeClass("active disable").addClass("label-primary");
    $("#verifyStatus").removeClass("active disable").addClass("label-primary");
    $("#paymentStatus").removeClass("label-primary disable").addClass("label-primary");
    $("#finishStatus").removeClass("label-primary disable").addClass("active");
    // $("#saveShipmentDetailBtn").prop("disabled", true);
    $("#verifyBtn").prop("disabled", true);
    $("#payBtn").prop("disabled", true);
    $("#exportBillBtn").prop("disabled", false);
}

function finishForm(result) {
    if (result.code == 0) {
        $.modal.msgSuccess(result.msg);
    } else {
        $.modal.msgError(result.msg);
    }
    reloadShipmentDetail();
}
