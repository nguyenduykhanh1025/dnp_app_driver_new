var prefix = ctx + "logistic/assignTruck";
var driverList;
var internalTransport = [];
var externalTransport = [];
var externalDriverList = [];
var isCheckAllInternal = false;
var isCheckAllExternal = false;
var recentPlateNumber = '';
var number = 0;
// Variable width table field
var width = $('#truckList').width();
//var height = $('.interDeliveryTeam').height();
$('.tbody-custom').css({'height': $('.right-side').innerHeight});
// console.log(height);
//$('.tbody-custom').css('height',height);
var checkField = width *10 / 100;
var plateNumberField = width *25 / 100;
var mobileNumberField = width *25/ 100;
var fullNameField = width *40/ 100;
// left
var shipmentSelected;
$(document).ready(function () {
	// $(".colHeader > input[type=checkbox]").click(function() {
	// console.log("inini")
	// })
	loadTable();
	$(".left-side").css("height", $(document).height());
	$("#btn-collapse").click(function () {
		handleCollapse(true);
	});
	$("#btn-uncollapse").click(function () {
		handleCollapse(false);
	});
	Init();
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
			if (!opts.url)
				return false;
			$.ajax({
				type: opts.method,
				url: opts.url,
				data: {
					pageNum: param.page,
					pageSize: param.rows,
					orderByColumn: param.sort,
					isAsc: param.order,
					// fromDate: fromDate,
					// toDate: toDate,
					// voyageNo: $("#voyageNo").val() == null ? "" :
					// $("#voyageNo").val(),
					// blNo: $("#blNo").val() == null ? "" : $("#blNo").val(),
				},
				dataType: "json",
				success: function (data) {
					success(data);
					// $("#dg").datagrid("hideColumn", "id");
					// $("#dg").datagrid("hideColumn", "edoFlg");
				},
				error: function () {
					error.apply(this, arguments);
				},
			});
		},
	});
}
function handleCollapse(status) {
	if (status) {
		$(".left-side").css("width", "0.5%");
		$(".left-side").children().hide();
		$("#btn-collapse").hide();
		$("#btn-uncollapse").show();
		$(".right-side").css("width", "99%");
	} else {
		$(".left-side").css("width", "33%");
		$(".left-side").children().show();
		$("#btn-collapse").show();
		$("#btn-uncollapse").hide();
		$(".right-side").css("width", "67%");
	}
	setTimeout(() => {
		width = $('#truckList').width();
		checkField = width *10 / 100;
		plateNumberField = width *25 / 100;
		mobileNumberField = width *25/ 100;
		fullNameField = width *40/ 100;
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

		$("#transportList").html('');
		internalTransport.forEach(function(transport) {
			var tableRow = '<tr id="transport'
				+ transport.id
				+ '"><td width="' + checkField + '"><input type="checkbox" id="internalCheckbox'
				+ transport.id
				+ '" onclick="internalCheck()"/></td><td width="' + plateNumberField + '">'
				+ transport.plateNumber
				+ '</td><td width="' + mobileNumberField + '">'
				+ transport.mobileNumber
				+ '</td><td width="' + fullNameField + '">'
				+ transport.fullName + '</td></tr>';
			$("#transportList").append(tableRow);
		});

		$("#pickedTransportList").html('');
		externalTransport.forEach(function(transport) {
			var tableRow = '<tr id="pickedTransport'
				+ transport.id
				+ '"><td width="' + checkField + '"><input type="checkbox" id="externalCheckbox'
				+ transport.id
				+ '" onclick="internalCheck()"/></td><td width="' + plateNumberField + '">'
				+ transport.plateNumber
				+ '</td><td width="' + mobileNumberField + '">'
				+ transport.mobileNumber
				+ '</td><td width="' + fullNameField + '">'
				+ transport.fullName + '</td></tr>';
			$("#pickedTransportList").append(tableRow);
		});
	}, 100);


}
function getSelected() {
	var row = $("#dg").datagrid("getSelected");
	if (row) {
		shipmentSelected = row;
		// $(function () {
		// var options = {
		// createUrl: prefix + "/addShipmentForm",
		// updateUrl: prefix + "/editShipmentForm/" + shipmentSelected.id,
		// modalName: " Lô"
		// };
		// $.table.init(options);
		// });
		// $("#loCode").text(row.id);
		// $("#taxCode").text(row.taxCode);
		// $("#quantity").text(row.containerAmount);
		// if (row.edoFlg == "0") {
		// $("#dotype").text("DO giấy");
		// } else {
		// $("#dotype").text("EDO");
		// }
		// $("#blNo").text(row.blNo);
		// loadShipmentDetail(row.id);
		$('#loCode').text(row.id);
		$('#taxCode').text(row.taxCode);
		$('#quantity').text(row.containerAmount);
		$('#serviceType').text(row.serviceId);
	}
}
function formatDate(value) {
	var date = new Date(value);
	var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	var month = date.getMonth() + 1;
	var monthText = month < 10 ? "0" + month : month;
	return day + "/" + monthText + "/" + date.getFullYear();
}
// Right
function Init() {
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

	$.ajax({
		url: "/logistic/transport/listTransportAccount",
		method: "get",
	}).done(function (data) {
		if (data != null) {
			driverList = data;
			for (var i = 0; i < data.length; i++) {
				internalTransport.push(data[i]);
				var tableRow = '<tr id="transport'
					+ data[i].id
					+ '"><td width="' + checkField + '"><input type="checkbox" id="internalCheckbox'
					+ data[i].id
					+ '" onclick="internalCheck()"/></td><td width="' + plateNumberField + '">'
					+ data[i].plateNumber
					+ '</td><td width="' + mobileNumberField + '">'
					+ data[i].mobileNumber
					+ '</td><td width="' + fullNameField + '">'
					+ data[i].fullName + '</td></tr>';
				$("#transportList").append(tableRow);
			}
		}
	});
}
function confirm() {
	if (number == 0) {
		pickTruckWithoutExternal();
	} else {
		pickTruckWithExternal();
	}
}

function pickTruckWithoutExternal() {
	if (externalTransport.length > 0) {
		console.log(externalTransport);
		var ids = "";
		externalTransport.forEach(function (driver) {
			ids += driver.id + ",";
		});
		if (pickCont) {
			if (externalTransport.length == 1) {
				parent.finishPickTruck(recentPlateNumber, ids.substring(0,
					ids.length - 1));
			} else {
				parent.finishPickTruck("Đội xe chỉ định trước", ids.substring(
					0, ids.length - 1));
			}
			console.log(ids);
			$.modal.close();
		} else {
			$.ajax({
				url: prefix + "/pickTruck",
				method: "post",
				data: {
					shipmentDetailIds: shipmentDetailIds,
					driverIds: ids.substring(0, ids.length - 1)
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
	if (validateInput()) {
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
					result.forEach(function (driver) {
						ids += driver.id + ",";
					});
					if (externalTransport.length > 0) {
						externalTransport.forEach(function (driver) {
							ids += driver.id + ",";
						});
					}
					if (pickCont) {
						if (externalTransport.length == 0
							&& externalDriverList.length == 1) {
							parent.finishPickTruck($(
								"#plateNumber"
								+ externalTransport[0].id)
								.html(), ids.substring(0,
									ids.length - 1));
						} else {
							parent.finishPickTruck(
								"Đội xe chỉ định trước", ids
									.substring(0,
										ids.length - 1));
						}
						$.modal.close();
					} else {
						$.ajax({
							url: prefix + "/pickTruck",
							method: "post",
							data: {
								shipmentDetailIds: shipmentDetailIds,
								driverIds: ids.substring(0,
									ids.length - 1)
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
	for (var i = 0; i < number; i++) {
		if ($("#numberPlate" + i).val() == ""
			|| $("#numberPhone" + i).val() == ""
			|| $("#name" + i).val() == "" || $("#pass" + i).val() == "") {
			$.modal.alertError("Quý khách chưa nhập đủ thông tin cho xe<br>thuê ngoài.");
			error = true;
			break;
		}
		if (!/[0-9]{10}/g.test($("#numberPhone" + i).val())) {
			$.modal.alertError("Dòng " + (i + 1)
				+ ": Số điện thoại không hợp lệ.");
			error = true;
			break;
		}
		if ($("#pass" + i).val().length < 6) {
			$.modal.alertError("Dòng " + (i + 1)
				+ ": Độ dài mật khẩu quá ngắn.");
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
	$("#interBtn").css({
		"background-color": "#72c072"
	});
	$(".exterDeliveryTeam").hide();
	$("#exterBtn").css({
		"background-color": "#c7c1c1"
	});

}

function exterDeliveryTab() {
	$(".exterDeliveryTeam").show();
	$("#exterBtn").css({
		"background-color": "#72c072"
	});
	$(".interDeliveryTeam").hide();
	$("#interBtn").css({
		"background-color": "#c7c1c1"
	});
}

function transferInToOut() {
	var size = internalTransport.length;
	var i = 0;
	while (i < size) {
		if ($("#internalCheckbox" + internalTransport[i].id).prop("checked")) {
			externalTransport.push(internalTransport[i]);
			$("#transport" + internalTransport[i].id).remove();
			var tableRow = '<tr id="pickedTransport'
				+ internalTransport[i].id
				+ '"><td width="' + checkField + '"><input type="checkbox" id="externalCheckbox'
				+ internalTransport[i].id
				+ '" onclick="internalCheck()"/></td><td width="' + plateNumberField + '">'
				+ internalTransport[i].plateNumber
				+ '</td><td width="' + mobileNumberField + '">'
				+ internalTransport[i].mobileNumber
				+ '</td><td width="' + fullNameField + '">' + internalTransport[i].fullName
				+ '</td></tr>';
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
			$("#pickedTransport" + externalTransport[i].id).remove();
			var tableRow = '<tr id="transport'
				+ externalTransport[i].id
				+ '"><td width="' + checkField + '"><input type="checkbox" id="internalCheckbox'
				+ externalTransport[i].id
				+ '" onclick="externalCheck()"/></td><td width="' + plateNumberField + '">'
				+ externalTransport[i].plateNumber
				+ '</td><td width="' + mobileNumberField + '">'
				+ externalTransport[i].mobileNumber
				+ '</td><td width="' + fullNameField + '">' + externalTransport[i].fullName
				+ '</td></tr>';
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
		for (var i = 0; i < internalTransport.length; i++) {
			$("#internalCheckbox" + internalTransport[i].id).prop("checked",
				false);
		}
	} else {
		isCheckAllInternal = true;
		for (var i = 0; i < internalTransport.length; i++) {
			$("#internalCheckbox" + internalTransport[i].id).prop("checked",
				true);
		}
	}
}

function checkAllExternal() {
	if (isCheckAllExternal) {
		isCheckAllExternal = false;
		for (var i = 0; i < externalTransport.length; i++) {
			$("#externalCheckbox" + externalTransport[i].id).prop("checked",
				false);
		}
	} else {
		isCheckAllExternal = true;
		for (var i = 0; i < externalTransport.length; i++) {
			$("#externalCheckbox" + externalTransport[i].id).prop("checked",
				true);
		}
	}
}

function internalCheck() {
	isCheckAllInternal = true;
	for (var i = 0; i < internalTransport.length; i++) {
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
	for (var i = 0; i < externalTransport.length; i++) {
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

$("#inputExternalRentNumber").keypress(
	function (event) {
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == '13') {
			event.preventDefault();
			number = parseInt($("#inputExternalRentNumber").val(),
				10);
			var externalDriver = '';
			for (var i = 0; i < number; i++) {
				externalDriver += '<tr><td width="40px" style="padding: 0;">'
					+ i
					+ '</td><td width="140px" style="padding: 0;"><input id="numberPlate'
					+ i
					+ '"type="text" style="width: 100%; box-sizing: border-box;"/></td><td width="135px" style="padding: 0;"><input id="numberPhone'
					+ i
					+ '"type="text" style="width: 100%; box-sizing: border-box;"/></td><td width="150px" style="padding: 0;"><input id="name'
					+ i
					+ '"type="text" style="width: 100%; box-sizing: border-box;"/></td><td width="150px" style="padding: 0;"><input id="pass'
					+ i
					+ '"type="text" style="width: 100%; box-sizing: border-box;"/></td></tr>'
			}
			$("#externalInputList").html(externalDriver);
		}
	});
