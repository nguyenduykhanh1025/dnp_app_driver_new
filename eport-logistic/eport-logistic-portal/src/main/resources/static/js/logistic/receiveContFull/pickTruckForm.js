var prefix = ctx + "logistic/receiveContFull";
var driverList;
var internalTransport = [];
var externalTransport = [];
var externalDriverList = [];
var isCheckAllInternal = false;
var isCheckAllExternal = false;
var recentPlateNumber = '';
var number = 0;
// Variable width table field
var width;
var checkField;
var plateNumberField;
var mobileNumberField;
var fullNameField;
var widthExternal;
var sttExternalField;
var plateNumberExternalField;
var mobileNumberExternalField;
var fullNameExternalField;
var pwdExternalField;
setTimeout(() => {
    width = $('#truckList').width();
    widthExternal = $('.interDeliveryTeam').width();
    checkField = width *10 / 100;
    plateNumberField = width *25 / 100;
    mobileNumberField = width *25/ 100;
    fullNameField = width *40/ 100;

    sttExternalField = widthExternal *10 / 100;
    plateNumberExternalField = widthExternal * 30/100;
    mobileNumberExternalField = widthExternal *20/100;
    fullNameExternalField = widthExternal *20/100;
    pwdExternalField = widthExternal *20/100;

    $.ajax({
        url: "/logistic/transport/listTransportAccount",
        method: "get",
    }).done(function(data) {
        if (data != null) {
            driverList = data;
            var transportArr;
            if (transportIds.length != 0) {
                transportArr = transportIds.split(",");
                for (var i=0; i<data.length; i++) {
                    var check = true;
                    for (var j=0; j<transportArr.length; j++) {
                        if(data[i].id == transportArr[j]) {
                            externalTransport.push(data[i]);
                            check = false;
                            var tableRow = '<tr id="pickedTransport'+ data[i].id +'"><td width="' + checkField + '"><input type="checkbox" id="externalCheckbox' + data[i].id +'" onclick="externalCheck()"/></td><td width="' + plateNumberField + '">' + data[i].plateNumber + '</td><td width="' + mobileNumberField + '">' + data[i].mobileNumber + '</td><td width="' + fullNameField + '">' + data[i].fullName + '</td></tr>';
                            $("#pickedTruckList").append(tableRow);
                            break;
                        }
                    }
                    if (check) {
                        internalTransport.push(data[i]);
                        var tableRow = '<tr id="transport'+ data[i].id +'"><td width="' + checkField + '"><input type="checkbox" id="internalCheckbox' + data[i].id +'" onclick="internalCheck()"/></td><td width="' + plateNumberField + '">' + data[i].plateNumber + '</td><td width="' + mobileNumberField + '">' + data[i].mobileNumber + '</td><td width="' + fullNameField + '">' + data[i].fullName + '</td></tr>';
                        $("#transportList").append(tableRow);
                    }
                }
            } else {
                for (var i=0; i<data.length; i++) {
                    externalTransport.push(data[i]);
                    var tableRow = '<tr id="pickedTransport'+ data[i].id +'"><td width="' + checkField + '"><input type="checkbox" id="externalCheckbox' + data[i].id +'" onclick="externalCheck()"/></td><td width="' + plateNumberField + '">' + data[i].plateNumber + '</td><td width="' + mobileNumberField + '">' + data[i].mobileNumber + '</td><td width="' + fullNameField + '">' + data[i].fullName + '</td></tr>';
                    $("#pickedTruckList").append(tableRow);
                }
            }
        }
    });
    Init();
}, 200);
function confirm() {
    if (number == 0) {
        pickTruckWithoutExternal();
    } else {
        pickTruckWithExternal();
    }
}
function Init(){
    // In
	$('#checkInField').css("width", checkField);
	$('#plateNumberInField').css("width", plateNumberField);
	$('#mobileNumberInField').css("width", mobileNumberField);
	$('#fullNameInField').css("width", fullNameField);
	// Out
	$('#checkOutField').css("width", checkField);
	$('#plateNumberOutField').css("width", plateNumberField);
	$('#mobileNumberOutField').css("width", mobileNumberField);
    $('#fullNameOutField').css("width", fullNameField);
    //External
    $('#sttExternalField').css("width", sttExternalField);
	$('#plateNumberExternalField').css("width", plateNumberExternalField);
	$('#mobileNumberExternalField').css("width", mobileNumberExternalField);
    $('#fullNameExternalField').css("width", fullNameExternalField);
    $('#pwdExternalField').css("width", pwdExternalField);
}
function pickTruckWithoutExternal() {
    if (externalTransport.length > 0) {
        var ids = "";
        externalTransport.forEach(function(driver) {
            ids += driver.id + ",";
        });
        if (pickCont) {
            if (externalTransport.length == 1) {
                parent.finishPickTruck(recentPlateNumber, ids.substring(0, ids.length-1));
            } else {
                parent.finishPickTruck("Đội xe chỉ định trước", ids.substring(0, ids.length-1));
            }
            console.log(ids);
            $.modal.close();
        } else {
            $.ajax({
                url: prefix + "/pickTruck",
                method: "post",
                data: {
                    shipmentDetailIds: shipmentDetailIds,
                    driverIds: ids.substring(0, ids.length-1)
                },
                success: function (data) {
                    if (data.code != 0) {
                        $.modal.msgError(data.msg);
                    } else {
                        parent.finishForm(data);
                        $.modal.close();
                    }
                },
                error: function (result) {
                    $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
                }
            });
            
        }
    } else {
        $.modal.alertError("Quý khách chưa đăng ký xe lấy container.");
    }
}

function pickTruckWithExternal() {
    if(validateInput()) {
        $.ajax({
            url: "/logistic/transport/saveExternalTransportAccount",
            method: "post",
            contentType: "application/json",
            accept: 'text/plain',
            data: JSON.stringify(externalDriverList),
            dataType: 'text',
            success: function (data) {
                var result = JSON.parse(data);
                if (result.length > 0) {
                    var ids = "";
                    result.forEach(function(driver) {
                        ids += driver.id + ",";
                    });
                    if (externalTransport.length > 0) {
                        externalTransport.forEach(function(driver) {
                            ids += driver.id + ",";
                        });
                    }
                    if (pickCont) {
                        if (externalTransport.length == 0 && externalDriverList.length == 1) {
                            parent.finishPickTruck($("#plateNumber"+externalTransport[0].id).html(), ids.substring(0, ids.length-1));
                        } else {
                            parent.finishPickTruck("Đội xe chỉ định trước", ids.substring(0, ids.length-1));
                        }
                        $.modal.close();
                    } else {
                        $.ajax({
                            url: prefix + "/pickTruck",
                            method: "post",
                            data: {
                                shipmentDetailIds: shipmentDetailIds,
                                driverIds: ids.substring(0, ids.length-1)
                            },
                            success: function (data) {
                                if (data.code != 0) {
                                    $.modal.msgError(data.msg);
                                } else {
                                    parent.finishForm(data);
                                    $.modal.close();
                                }
                            },
                            error: function (result) {
                                $.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
                            }
                        });
                    }
                } else {
                    $.modal.alertError("Có lỗi trong quá trình xử lý dữ liệu, vui lòng liên hệ admin.");
                }
                $.modal.closeLoading();
            },
            error: function (result) {
                $.modal.alertError("Có lỗi trong quá trình xử lý dữ liệu, vui lòng liên hệ admin.");
                $.modal.closeLoading();
            },
        });
    } 
}

function validateInput() {
    var error = false;
    externalDriverList = [];
    for (var i=0; i<number; i++) {
        if ($("#numberPlate" + i).val() == "" || $("#numberPhone" + i).val() == "" || $("#name" + i).val() == "" || $("#pass" + i).val() == "") {
            $.modal.alertError("Quý khách chưa nhập đủ thông tin cho xe<br>thuê ngoài.");
            error = true;
            break;
        }
        if (!/[0-9]{10}/g.test($("#numberPhone" + i).val())) {
            $.modal.alertError("Dòng "+ (i+1) +": Số điện thoại không hợp lệ.");
            error = true;
            break;
        }
        if ($("#pass" + i).val().length < 6) {
            $.modal.alertError("Dòng "+ (i+1) +": Độ dài mật khẩu quá ngắn.");
            error = true;
            break;
        }
        var object = new Object();
        object.plateNumber = $("#numberPlate" + i).val();
        object.mobileNumber = $("#numberPhone" + i).val();
        object.fullName = $("#name" + i).val();
        object.password = $("#pass" + i).val();
        var now = new Date();
        object.validDate = now.setDate(now.getDate() + 7);
        externalDriverList.push(object);
    }
    if (error) {
        return false;
    } else {
        return true;
    }
}

function closeForm() {
    $.modal.close();
}

function interDeliveryTab() {
    $(".interDeliveryTeam").show();
    $("#interBtn").css({"background-color": "#72c072"});
    $(".exterDeliveryTeam").hide();
    $("#exterBtn").css({"background-color": "#c7c1c1"});

}

function exterDeliveryTab() {
    $(".exterDeliveryTeam").css("display","flex");;
    $("#exterBtn").css({"background-color": "#72c072"});
    $(".interDeliveryTeam").hide();
    $("#interBtn").css({"background-color": "#c7c1c1"});
}

function transferInToOut() {
    var size = internalTransport.length;
    var i = 0;
    while (i < size) {
        if ($("#internalCheckbox" + internalTransport[i].id).prop("checked")) {
            externalTransport.push(internalTransport[i]);
            $("#transport"+internalTransport[i].id).remove();
            var tableRow = '<tr id="pickedTransport'+ internalTransport[i].id +'"><td width="' + checkField + '"><input type="checkbox" id="externalCheckbox' + internalTransport[i].id +'" onclick="internalCheck()"/></td><td width="' + plateNumberField + '">' + internalTransport[i].plateNumber + '</td><td width="' + mobileNumberField +'">' + internalTransport[i].mobileNumber + '</td><td width="' + fullNameField +'">' + internalTransport[i].fullName + '</td></tr>';
            $("#pickedTruckList").append(tableRow);
            internalTransport.splice(i, 1);
            size--;
        } else {
            i++;
        }
    }
    $("#headerInternalCheckbox").prop("checked", false);
    isCheckAllInternal = false;
    recentPlateNumber = externalTransport[0].plateNumber;
}

function transferOutToIn() {
    var size = externalTransport.length;
    var i = 0;
    while (i < size) {
        if ($("#externalCheckbox" + externalTransport[i].id).prop("checked")) {
            internalTransport.push(externalTransport[i]);
            $("#pickedTransport"+externalTransport[i].id).remove();
            var tableRow = '<tr id="transport'+ externalTransport[i].id +'"><td width="' + checkField + '"><input type="checkbox" id="internalCheckbox' + externalTransport[i].id +'" onclick="externalCheck()"/></td><td width="' + plateNumberField + '">' + externalTransport[i].plateNumber + '</td><td width="' + mobileNumberField +'">' + externalTransport[i].mobileNumber + '</td><td width="' + fullNameField +'">' + internalTransport[i].fullName + '</td></tr>';
            $("#truckList").append(tableRow);
            externalTransport.splice(i, 1);
            size--;
        } else {
            i++;
        }
    }
    $("#headerExternalCheckbox").prop("checked", false);
    isCheckAllExternal = false;
}

function checkAllInternal() {
    if (isCheckAllInternal) {
        isCheckAllInternal = false;
        for (var i=0; i<internalTransport.length; i++) {
            $("#internalCheckbox" + internalTransport[i].id).prop("checked", false);
        }
    } else {
        isCheckAllInternal = true;
        for (var i=0; i<internalTransport.length; i++) {
            $("#internalCheckbox" + internalTransport[i].id).prop("checked", true);
        }
    }
}

function checkAllExternal() {
    if (isCheckAllExternal) {
        isCheckAllExternal = false;
        for (var i=0; i<externalTransport.length; i++) {
            $("#externalCheckbox" + externalTransport[i].id).prop("checked", false);
        }
    } else {
        isCheckAllExternal = true;
        for (var i=0; i<externalTransport.length; i++) {
            $("#externalCheckbox" + externalTransport[i].id).prop("checked", true);
        }
    }
}

function internalCheck() {
    isCheckAllInternal = true;
    for (var i=0; i<internalTransport.length; i++) {
        if (!$("#internalCheckbox" + internalTransport[i].id).prop("checked")) {
            isCheckAllInternal = false;
            break;
        }
    }
    if (isCheckAllInternal) {
        $("#headerInternalCheckbox").prop("checked", true);
    } else {
        $("#headerInternalCheckbox").prop("checked", false);
    }
}

function externalCheck() {
    isCheckAllExternal = true;
    for (var i=0; i<externalTransport.length; i++) {
        if (!$("#externalCheckbox" + externalTransport[i].id).prop("checked")) {
            isCheckAllExternal = false;
            break;
        }
    }
    if (isCheckAllExternal) {
        $("#headerExternalCheckbox").prop("checked", true);
    } else {
        $("#headerExternalCheckbox").prop("checked", false);
    }
}

$("#inputExternalRentNumber").keypress(function (event) {
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if (keycode == '13') {
        event.preventDefault();
        number = parseInt($("#inputExternalRentNumber").val(), 10);
        var externalDriver = '';
        for (var i = 0; i < number; i++) {
            externalDriver += '<tr><td width="' + sttExternalField + '" style="padding: 0;">'+i+'</td><td width="' + plateNumberExternalField + '" style="padding: 0;"><input id="numberPlate' + i + '"type="text" style="width: 100%; box-sizing: border-box;"/></td><td width="' + mobileNumberExternalField + '" style="padding: 0;"><input id="numberPhone' + i + '"type="text" style="width: 100%; box-sizing: border-box;"/></td><td width="' + fullNameExternalField + '" style="padding: 0;"><input id="name' + i + '"type="text" style="width: 100%; box-sizing: border-box;"/></td><td width="' + pwdExternalField + '" style="padding: 0;"><input id="pass' + i + '"type="text" style="width: 100%; box-sizing: border-box;"/></td></tr>'
        }
        $("#externalInputList").html(externalDriver);
    }
});

