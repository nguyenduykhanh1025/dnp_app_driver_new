const PREFIX = ctx + "reefer-group/send-cont-reefer";
const greenBlackColor = "rgb(104 241 131)";
var rowAmount = 0;
var allChecked = false;
var checkList = [];
var shipment = new Object();
shipment.params = new Object();
shipment.params.sztp = "R";
shipment.params.serviceArray = [4];
shipment.params.supportStatus = 'P';
var shipmentSelected;
var shipmentDetailIds;
var sourceData, isChange;

const CONT_SPECIAL_STATUS = {
	INIT: "I", // cont đã được lưu
	REQ: "R", // cont đã được yêu cầu xác nhận
	YES: "Y", // cont đã được phê duyệt yêu cầu xác nhận
	CANCEL: "C", // cont đã bị từ chối yêu cầu xác nhận
};

const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var dogrid = document.getElementById("container-grid"), hot;

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
	loadTable();
	$(".main-body").layout();

	$(".collapse").click(function () {
		$(".main-body__search-wrapper").height(15);
		$(".main-body__search-wrapper--container").hide();
		$(this).hide();
		$(".uncollapse").show();
	});

	$(".uncollapse").click(function () {
		$(".main-body__search-wrapper").height(SEARCH_HEIGHT);
		$(".main-body__search-wrapper--container").show();
		$(this).hide();
		$(".collapse").show();
	});

	$(".left-side__collapse").click(function () {
		$('#main-layout').layout('collapse', 'west');
	});

	$(".right-side__collapse").click(function () {
		$('#right-layout').layout('collapse', 'south');
		setTimeout(() => {
			hot.updateSettings({ height: $('#right-side__main-table').height() - 35 });
			hot.render();
		}, 200);
	});

	$('#right-layout').layout({
		onExpand: function (region) {
			if (region == "south") {
				hot.updateSettings({ height: $('#right-side__main-table').height() - 35 });
				hot.render();
			}
		}
	});

	$('#right-layout').layout('collapse', 'south');
	setTimeout(() => {
		hot.updateSettings({ height: $('#right-side__main-table').height() - 35 });
		hot.render();
	}, 200);

	$("#attachIcon").on("click", function () {
		let shipmentId = $(this).data("shipment-id");
		if (!shipmentId) {
			return;
		}
		let url = $(this).data("url");
		try {
			$.modal.openTab(`Đính kèm - Cont [${shipmentId}]`, url.replace("{shipmentId}", shipmentId));
		}
		catch (e) {
			window.open(url.replace("{shipmentId}", shipmentId));
		}
	});

	$("#containerNo").textbox('textbox').bind('keydown', function (e) {
		// enter key
		if (e.keyCode == 13) {
			search();
		}
	});

	$("#blNo").textbox('textbox').bind('keydown', function (e) {
		// enter key
		if (e.keyCode == 13) {
			search();
		}
	});
});

function search() {
	shipment.bookingNo = $("#blNo").textbox('getText').toUpperCase();
	shipment.params.containerNo = $("#containerNo").textbox('getText').toUpperCase();
	shipment.params.supportStatus = $('#batchStatus').combobox('getValue');
	loadTable();
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

// LOAD SHIPMENT LIST
function loadTable() {
	$("#dg").datagrid({
		url: PREFIX + '/shipments',
		height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
		method: 'POST',
		singleSelect: true,
		collapsible: true,
		clientPaging: false,
		pagination: true,
		rownumbers: true,
		onClickRow: function () {
			getSelected();
		},
		pageSize: 50,
		nowrap: true,
		striped: true,
		loadMsg: " Đang xử lý...",
		loader: function (param, success, error) {
			var opts = $(this).datagrid("options");
			if (!opts.url) return false;
			$.ajax({
				type: opts.method,
				url: opts.url,
				contentType: "application/json",
				accept: 'text/plain',
				dataType: 'text',
				data: JSON.stringify({
					pageNum: param.page,
					pageSize: param.rows,
					orderByColumn: param.sort,
					isAsc: param.order,
					data: shipment
				}),
				success: function (data) {
					success(JSON.parse(data));
					$("#dg").datagrid("hideColumn", "id");
					$("#dg").datagrid("selectRow", 0);
					if (shipmentSelected) {
						loadShipmentDetail(shipmentSelected.id);
					} else {
						loadShipmentDetail(JSON.parse(data).rows[0].id);
					}
				},
				error: function () {
					error.apply(this, arguments);
				},
			});
		},
	});
}
// FORMATTER
function formatLogistic(value, row, index) {
	return '<a onclick="logisticInfo(' + row.logisticGroupId + "," + "'" + value + "')\"> " + value + "</a>";
}
// FORMAT REMARK FOR SHIPMENT LIST
function formatRemark(value) {
	return '<div class="easyui-tooltip" title="' + ((value != null && value != "") ? value : "không có ghi chú") + '" style="width: 80; text-align: center;"><span>' + (value != null ? value : "") + '</span></div>';
}

// FORMAT DATE FOR SHIPMENT LIST
function formatDate(value) {
	var date = new Date(value);
	var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	var month = date.getMonth() + 1;
	var monthText = month < 10 ? "0" + month : month;
	return day + "/" + monthText + "/" + date.getFullYear();
}

function formatSpecificContFlg(value) {
	switch (value) {
		case 1:
			return 'Hãng Tàu Cấp';
		case 0:
			return 'Cảng Cấp';
	}
}

function handleRefresh() {
	loadTable();
}

function clearInput() {
	$("#blNo").textbox('setText', '');
	shipment = new Object();
	shipment.params = new Object();
	loadTable();
}


// HANDLE WHEN SELECT A SHIPMENT
function getSelected() {
	var row = $("#dg").datagrid("getSelected");
	if (row) {
		$("#loCode").text(row.id);
		$("#quantity").text(row.containerAmount);
		$("#bookingNo").text(row.bookingNo);

		if (row.edoFlg == "0") {
			$("#edoFlg").text("DO");
		} else {
			$("#edoFlg").text("eDO");
		}

		rowAmount = row.containerAmount;
		shipmentSelected = row;
		checkList = Array(rowAmount).fill(0);
		allChecked = false;
		if (row.specificContFlg == 0) {
			$("#saveShipmentDetailBtn").html("Xác Nhận Cấp Container");
		} else {
			$("#saveShipmentDetailBtn").html("Duyệt Cấp Container");
		}
		loadShipmentDetail(row.id);
		toggleAttachIcon(row.id);
		loadListComment();
	}
}

function toggleAttachIcon(shipmentId) {
	$.ajax({
		type: "GET",
		url: PREFIX + "/shipments/" + shipmentId + "/shipment-images",
		contentType: "application/json",
		success: function (data) {
			if (data.code == 0) {
				if (data.shipmentFiles != null && data.shipmentFiles.length > 0) {
					let html = '';
					data.shipmentFiles.forEach(function (element, index) {
						html += ' <a href="' + element.path + '" target="_blank"><i class="fa fa-paperclip" style="font-size: 18px;"></i> ' + (index + 1) + '</a>';
					});
					$('#attachIcon').html(html);
				}
			}
		}
	});
}

$("#batchStatus").combobox({
	onChange: function (serviceType) {
		shipment.params.supportStatus = serviceType.value;
		search();
	}
});

// FORMAT HANDSONTABLE COLUMN

function checkBoxRenderer(instance, td, row, col, prop, value, cellProperties) {
	let content = "";
	if (checkList[row] == 1) {
		content +=
			'<div><input type="checkbox" id="check' +
			row +
			'" onclick="check(' +
			row +
			')" checked></div>';
	} else {
		content +=
			'<div><input type="checkbox" id="check' +
			row +
			'" onclick="check(' +
			row +
			')"></div>';
	}
	$(td)
		.attr("id", "checkbox" + row)
		.addClass("htCenter")
		.addClass("htMiddle")
		.html(content);
	return td;
}

function statusIconsRenderer(
	instance,
	td,
	row,
	col,
	prop,
	value,
	cellProperties
) {
	$(td)
		.attr("id", "statusIcon" + row)
		.addClass("htCenter")
		.addClass("htMiddle");
	if (
		sourceData[row] &&
		sourceData[row].dischargePort &&
		sourceData[row].processStatus &&
		sourceData[row].paymentStatus &&
		sourceData[row].customStatus &&
		sourceData[row].finishStatus
	) {
		// Command process status
		let process =
			'<i id="verify" class="fa fa-windows easyui-tooltip" title="Chưa xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';


		switch (sourceData[row].processStatus) {
			case "E":
				process =
					'<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ kết quả" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
				break;
			case "Y":
				process =
					'<i id="verify" class="fa fa-windows easyui-tooltip" title="Đã làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
				break;
			case "N":
				process =
					'<i id="verify" class="fa fa-windows easyui-tooltip" title="Có thể làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
				break;
			case "D":
				process =
					'<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ hủy lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #f93838;"></i>';
				break;
		}


		// Payment status
		let payment =
			'<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
		switch (sourceData[row].paymentStatus) {
			case "E":
				payment =
					'<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Lỗi Thanh Toán" aria-hidden="true" style="margin-left: 8px; color : #ed5565;"></i>';
				break;
			case "Y":
				payment =
					'<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
				break;
			case "N":
				if (value > 1) {
					payment =
						'<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chờ Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
				}
				break;
		}
		// Customs Status
		let customs =
			'<i id="custom" class="fa fa-shield easyui-tooltip" title="Chờ Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
		switch (sourceData[row].customStatus) {
			case "R":
				customs =
					'<i id="custom" class="fa fa-shield easyui-tooltip" title="Đã Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
				break;
			case "Y":
				customs =
					'<i id="custom" class="fa fa-shield easyui-tooltip" title="Chưa Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #ed5565;"></i>';
				break;
			case "N":
				customs =
					'<i id="custom" class="fa fa-shield easyui-tooltip" title="Chờ Thông Quan" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
				break;
		}
		// released status
		let released =
			'<i id="finish" class="fa fa-ship easyui-tooltip" title="Chưa Thể Giao Container" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
		switch (sourceData[row].finishStatus) {
			case "Y":
				released =
					'<i id="finish" class="fa fa-ship easyui-tooltip" title="Đã Giao Container" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
				break;
			case "N":
				if (sourceData[row].paymentStatus == "Y") {
					released =
						'<i id="finish" class="fa fa-ship easyui-tooltip" title="Có Thể Giao Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
				}
				break;
		}
		// Return the content
		let content = '<div style="text-align: right">';

		content += getRequestConfigIcon(row);

		content += process + payment;
		// Domestic cont: VN --> not show
		if (sourceData[row].dischargePort.substring(0, 2) != "VN") {
			content += customs;
		}
		content += released + "</div>";
		$(td).html(content);
	}
	return td;
}

function containerNoRenderer(
	instance,
	td,
	row,
	col,
	prop,
	value,
	cellProperties
) {
	$(td)
		.attr("id", "containerNo" + row)
		.addClass("htMiddle")
		.addClass("htCenter");
	if (value != null && value != "") {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
			cellProperties.readOnly = "true";
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (!value) {
		value = "";
		cellProperties.comment = null;
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	return td;
}

function expiredDemRenderer(
	instance,
	td,
	row,
	col,
	prop,
	value,
	cellProperties
) {
	$(td)
		.attr("id", "expiredDem" + row)
		.addClass("htMiddle")
		.addClass("htCenter");
	$(td).html(value);
	if (value != null && value != "") {
		if (value.substring(2, 3) != "/") {
			value =
				value.substring(8, 10) +
				"/" +
				value.substring(5, 7) +
				"/" +
				value.substring(0, 4);
		}
		value = "";
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	return td;
}
function consigneeRenderer(
	instance,
	td,
	row,
	col,
	prop,
	value,
	cellProperties
) {
	$(td)
		.attr("id", "consignee" + row)
		.addClass("htMiddle");
	if (value != null && value != "") {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
			cellProperties.readOnly = "true";
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (!value) {
		value = "";
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td)
		.attr("id", "vslNm" + row)
		.addClass("htMiddle");
	if (value != null && value != "") {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
			cellProperties.readOnly = "true";
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (!value) {
		value = "";
	}

	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	return td;
}
function etaRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td)
		.attr("id", "eta" + row)
		.addClass("htMiddle")
		.addClass("htCenter");
	if (value != null && value != "") {
		if (value.substring(2, 3) != "/") {
			value =
				value.substring(8, 10) +
				"/" +
				value.substring(5, 7) +
				"/" +
				value.substring(0, 4);
		}
	} else {
		value = "";
	}
	cellProperties.readOnly = "true";
	$(td).css("background-color", "rgb(232, 232, 232)");
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td)
		.attr("id", "sztp" + row)
		.addClass("htMiddle");
	if (value != null && value != "") {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
			cellProperties.readOnly = "true";
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	// if (sztpListDisable[row] == 1) {
	//   cellProperties.readOnly = "true";
	//   $(td).css("background-color", "rgb(232, 232, 232)");
	// }
	if (!value) {
		value = "";
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	return td;
}
function sealNoRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td)
		.attr("id", "sealNo" + row)
		.addClass("htMiddle");
	if (value != null && value != "") {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
			cellProperties.readOnly = "true";
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (!value) {
		value = "";
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	return td;
}
function temperatureRenderer(
	instance,
	td,
	row,
	col,
	prop,
	value,
	cellProperties
) {
	$(td)
		.attr("id", "temperature" + row)
		.addClass("htMiddle")
		.addClass("htRight");
	if (value != null && value != "") {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
			cellProperties.readOnly = "true";
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	// if (temperatureDisable[row] == 1) {
	//   $(td).html("");
	//   cellProperties.readOnly = "true";
	//   $(td).css("background-color", "rgb(232, 232, 232)");
	// }

	if (value === null || value === "") {
		value = "";
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	return td;
}

/**
 * Render button
 */
function btnDetailRenderer(
	instance,
	td,
	row,
	col,
	prop,
	value,
	cellProperties
) {
	$(td)
		.attr("id", "wgt" + row)
		.addClass("htMiddle")
		.addClass("htCenter");
	let containerNo, sztp;
	// if (!isDestroy) {
	// containerNo = hot.getDataAtCell(row, 2);
	// sztp = hot.getDataAtCell(row, 3); 
	// cargoType = hot.getDataAtCell(row, 9);  
	// }

	if (sourceData && sourceData.length > 0) {
		if (sourceData.length > row && sourceData[row].id) {
			value = `<button class="btn btn-success btn-xs" onclick="openDetail('${sourceData[row].id}', '${containerNo}', '${sztp}', '${row}','${sourceData[row].cargoType}')"><i class="fa fa-book"></i>Cont đặc biệt</button>`;
		} else if (containerNo && sztp) {
			value = `<button class="btn btn-success btn-xs" onclick="openDetail('${""}', '${containerNo}', '${sztp}', '${row}','${sourceData[row].cargoType}')"><i class="fa fa-book"></i>Cont đặc biệt</button>`;
		}
	}

	/*  if (sourceData && sourceData.length > 0) {  
		 if (sourceData.length > row && sourceData[row].id) { 
			 if(sourceData[row].cargoType == "DG"){
					value = `<button class="btn btn-success btn-xs" onclick="openDetail('${sourceData[row].id}', '${containerNo}', '${sztp}', '${row}','${sourceData[row].cargoType}')"><i class="fa fa-book"></i>Cont nguy hiểm</button>`;
			 }
			 else if(sourceData[row].sztp.substring (2,3) == "R"){
			 value = `<button class="btn btn-success btn-xs" onclick="openDetail('${sourceData[row].id}', '${containerNo}', '${sztp}', '${row}','${sourceData[row].cargoType}')"><i class="fa fa-book"></i>Cont lạnh</button>`;
			 }
			 else if(sourceData[row].oversizeTop || sourceData[row].oversizeRight || sourceData[row].oversizeLeft){
					value = `<button class="btn btn-success btn-xs" onclick="openDetail('${sourceData[row].id}', '${containerNo}', '${sztp}', '${row}','${sourceData[row].cargoType}')"><i class="fa fa-book"></i>Cont quá khổ</button>`;
			 } 
			} else if (containerNo && sztp) {
				value = `<button class="btn btn-success btn-xs" onclick="openDetail('${""}', '${containerNo}', '${sztp}', '${row}','${sourceData[row].cargoType}')"><i class="fa fa-book"></i>Cont đặc biệt</button>`;
			} 
		}*/
	$(td).html(value);
	cellProperties.readOnly = "true";
	return td;
}

function wgtRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td)
		.attr("id", "wgt" + row)
		.addClass("htMiddle")
		.addClass("htRight");
	if (value != null && value != "") {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
			cellProperties.readOnly = "true";
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
		if (value > 99999) {
			layer.msg("Trọng lượng (kg) quá lớn (hơn 100 tấn).", {
				icon: $.modal.icon(modal_status.FAIL),
				time: 2000,
				shift: 5,
			});
			$(td).css("text-color", "red");
		} else if (value < 1000) {
			layer.msg("Trọng lượng (kg) quá nhỏ (nhỏ hơn 1 tấn).", {
				icon: $.modal.icon(modal_status.FAIL),
				time: 2000,
				shift: 5,
			});
			$(td).css("text-color", "red");
		}
		value = formatMoney(value);
	}
	if (!value) {
		value = "";
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	return td;
}
function formatMoney(value) {
	return value.format(0, 3, ",", ".");
}
Number.prototype.format = function (n, x, s, c) {
	var re = "\\d(?=(\\d{" + (x || 3) + "})+" + (n > 0 ? "\\D" : "$") + ")",
		num = this.toFixed(Math.max(0, ~~n));

	return (c ? num.replace(".", c) : num).replace(
		new RegExp(re, "g"),
		"$&" + (s || ",")
	);
};

function cargoTypeRenderer(
	instance,
	td,
	row,
	col,
	prop,
	value,
	cellProperties
) {
	$(td)
		.attr("id", "cargoType" + row)
		.addClass("htMiddle")
		.addClass("htCenter");
	if (value != null && value != "") {
		value = value.split(":")[0];
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
			cellProperties.readOnly = "true";
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (!value) {
		value = "";
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	return td;
}
function commodityRenderer(
	instance,
	td,
	row,
	col,
	prop,
	value,
	cellProperties
) {
	$(td)
		.attr("id", "commodity" + row)
		.addClass("htMiddle");
	if (value != null && value != "") {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
			cellProperties.readOnly = "true";
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (!value) {
		value = "";
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	return td;
}
function dischargePortRenderer(
	instance,
	td,
	row,
	col,
	prop,
	value,
	cellProperties
) {
	$(td)
		.attr("id", "dischargePort" + row)
		.addClass("htMiddle")
		.addClass("htCenter");
	if (value != null && value != "") {
		value = value.split(":")[0];
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
			cellProperties.readOnly = "true";
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (!value) {
		value = "";
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	return td;
}

function payTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td)
		.attr("id", "payType" + row)
		.addClass("htMiddle")
		.addClass("htCenter");
	if (!value) {
		value = "";
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	cellProperties.readOnly = "true";
	$(td).css("background-color", "rgb(232, 232, 232)");
	return td;
}

function payerRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td)
		.attr("id", "payer" + row)
		.addClass("htMiddle")
		.addClass("htCenter");
	if (!value) {
		value = "";
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	cellProperties.readOnly = "true";
	$(td).css("background-color", "rgb(232, 232, 232)");
	return td;
}

function payerNameRenderer(
	instance,
	td,
	row,
	col,
	prop,
	value,
	cellProperties
) {
	$(td)
		.attr("id", "payerNamer" + row)
		.addClass("htMiddle");
	if (!value) {
		value = "";
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	cellProperties.readOnly = "true";
	$(td).css("background-color", "rgb(232, 232, 232)");
	return td;
}

function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td)
		.attr("id", "remark" + row)
		.addClass("htMiddle");
	if (!value) {
		value = "";
	}
	$(td).html(
		'<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' +
		value +
		"</div>"
	);
	return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
	config = {
		stretchH: "all",
		height: $("#right-side__main-table").height() - 35,
		minRows: rowAmount,
		maxRows: rowAmount,
		width: "100%",
		minSpareRows: 0,
		rowHeights: 30,
		fixedColumnsLeft: 3,
		trimDropdown: false,
		manualColumnResize: true,
		manualRowResize: true,
		rowHeaders: true,
		comments: true,
		className: "htMiddle htCenter",
		colHeaders: function (col) {
			switch (col) {
				case 0:
					var txt = "<input type='checkbox' class='checker' ";
					txt += "onclick='checkAll()' ";
					txt += ">";
					return txt;
				case 1:
					return "Trạng Thái";
				case 2:
					return '<span class="required">Container No</span>';
				case 3:
					return '<span class="required">Kích Thước</span>';
				case 4:
					return '<span class="required">Chủ Hàng</span>';
				case 5:
					return '<span class="required">Tàu và Chuyến</span>';
				case 6:
					return "Ngày tàu đến";
				case 7:
					return "Nhiệt Độ (c)";
				case 8:
					return "Độ Ẩm (%)";
				case 9:
					return "Thông Gió (%)";
				case 10:
					return '<span class="required">Cảng Dỡ Hàng</span>';
				case 11:
					return '<span class="required">Trọng Lượng (kg)</span>';
				case 12:
					return '<span class="required">Loại Hàng</span>';
				case 13:
					return "Tên Hàng";
				case 14:
					return "Số Seal";
				// case 15:
				// 	return '<span>Chi Tiết Container</span>';
				case 15:
					return "PTTT";
				case 16:
					return "Mã Số Thuế";
				case 17:
					return "Người Thanh Toán";
				case 18:
					return "Ghi Chú";
			}
		},
		colWidths: [
			40,
			120,
			100,
			150,
			150,
			150,
			80,
			80,
			80,
			130,
			100,
			120,
			120,
			80,
			100,
			// 130,
			80,
			100,
			130,
			200,
		],
		filter: "true",
		columns: [
			{
				data: "active",
				type: "checkbox",
				className: "htCenter",
				renderer: checkBoxRenderer,
			},
			{
				data: "status",
				readOnly: true,
				renderer: statusIconsRenderer,
			},
			{
				data: "containerNo",
				strict: true,
				renderer: containerNoRenderer,
			},

			{
				data: "sztp",
				type: "autocomplete",
				strict: true,
				renderer: sizeRenderer,
			},

			{
				data: "consignee",
				strict: true,
				type: "autocomplete",
				renderer: consigneeRenderer,
			},
			{
				data: "vslNm",
				type: "autocomplete",
				strict: true,
				renderer: vslNmRenderer,
			},
			{
				data: "eta",
				renderer: etaRenderer,
			},
			{
				data: "temperature",
				type: "numeric",
				strict: true,
				readonly: true,
				renderer: temperatureRenderer,
			},
			{
				data: "humidity",
				type: "numeric",
				strict: true,
				readonly: true,
				renderer: temperatureRenderer,
			},
			{
				data: "ventilation",
				type: "numeric",
				strict: true,
				readonly: true,
				renderer: temperatureRenderer,
			},
			{
				data: "dischargePort",
				strict: true,
				type: "autocomplete",
				renderer: dischargePortRenderer,
			},
			{
				data: "wgt",
				type: "numeric",
				strict: true,
				renderer: wgtRenderer,
			},
			{
				data: "cargoType",
				strict: true,
				type: "autocomplete",
				renderer: cargoTypeRenderer,
			},
			{
				data: "commodity",
				renderer: commodityRenderer,
			},
			{
				data: "sealNo",
				renderer: sealNoRenderer,
			},
			// {
			// 	data: "btnInformationContainer",
			// 	renderer: btnDetailRenderer,
			// },
			{
				data: "payType",
				renderer: payTypeRenderer,
			},
			{
				data: "payer",
				renderer: payerRenderer,
			},
			{
				data: "payerName",
				renderer: payerNameRenderer,
			},
			{
				data: "remark",
				renderer: remarkRenderer,
			},
		],
		beforeKeyDown: function (e) {
			let selected;
			switch (e.keyCode) {
				// Arrow Left
				case 37:
					selected = hot.getSelected()[0];
					if (selected[3] == 0) {
						e.stopImmediatePropagation();
					}
					break;
				// Arrow Up
				case 38:
					selected = hot.getSelected()[0];
					if (selected[2] == 0) {
						e.stopImmediatePropagation();
					}
					break;
				// Arrow Right
				case 39:
					selected = hot.getSelected()[0];
					if (selected[3] == 17) {
						e.stopImmediatePropagation();
					}
					break;
				// Arrow Down
				case 40:
					selected = hot.getSelected()[0];
					if (selected[2] == rowAmount - 1) {
						e.stopImmediatePropagation();
					}
					break;
				default:
					break;
			}
		},
	};
}
configHandson();

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

// TRIGGER CHECK ALL SHIPMENT DETAIL
function checkAll() {
	if (!allChecked) {
		allChecked = true
		checkList = Array(rowAmount).fill(0);
		for (let i = 0; i < checkList.length; i++) {
			checkList[i] = 1;
			$('#check' + i).prop('checked', true);
		}
	} else {
		allChecked = false;
		checkList = Array(rowAmount).fill(0);
		for (let i = 0; i < checkList.length; i++) {
			$('#check' + i).prop('checked', false);
		}
	}
	let tempCheck = allChecked;
	hot.render();
	allChecked = tempCheck;
	$('.checker').prop('checked', tempCheck);
}
function check(id) {
	if (sourceData[id].id != null) {
		if (checkList[id] == 0) {
			$('#check' + id).prop('checked', true);
			checkList[id] = 1;
		} else {
			$('#check' + id).prop('checked', false);
			checkList[id] = 0;
		}
		hot.render();
		updateLayout();
	}
}
function updateLayout() {
	allChecked = true;
	for (let i = 0; i < checkList.length; i++) {
		let cellStatus = hot.getDataAtCell(i, 1);
		if (cellStatus != null) {
			if (checkList[i] != 1) {
				allChecked = false;
			}
		}
	}
	$('.checker').prop('checked', allChecked);
}

function loadShipmentDetail(id) {
	const status = shipment.params.supportStatus;
	$.ajax({
		url: PREFIX + "/shipment/" + id + "/shipment-detail" + "/status/" + status,
		method: "GET",
		success: function (data) {
			if (data.code == 0) {
				sourceData = data.shipmentDetails;
				hot.destroy();
				configHandson();
				hot = new Handsontable(dogrid, config);
				hot.loadData(sourceData);
				hot.render();
			}
		}
	});
}

// GET CHECKED SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataSelectedFromTable() {
	let myTableData = hot.getSourceData();
	let errorFlg = false;
	let cleanedGridData = [];
	for (let i = 0; i < myTableData.length; i++) {
		if (myTableData[i].id != null) {
			if (checkList[i] == 1) {
				cleanedGridData.push(myTableData[i]);
			}
		}
	}
	shipmentDetails = [];
	shipmentDetailIds = '';
	$.each(cleanedGridData, function (index, object) {
		var shipmentDetail = new Object();
		shipmentDetail.containerNo = object["containerNo"];
		shipmentDetail.contSupplyRemark = object["contSupplyRemark"];
		shipmentDetail.contSupplyStatus = object["contSupplyStatus"];
		shipmentDetail.location = object["location"];
		shipmentDetail.containerRemark = object["containerRemark"];
		shipmentDetail.id = object["id"];
		shipmentDetailIds += object["id"] + ',';
		shipmentDetails.push(shipmentDetail);
	});

	if (shipmentDetails.length == 0) {
		$.modal.alertWarning("Bạn chưa chọn container.");
		errorFlg = true;
	} else {
		shipmentDetailIds = shipmentDetailIds.substring(0, shipmentDetailIds.length - 1);
	}

	if (errorFlg) {
		return false;
	} else {
		return true;
	}
}

function getDataFromTable() {
	let myTableData = hot.getSourceData();
	let errorFlg = false;
	let cleanedGridData = [];
	for (let i = 0; i < myTableData.length; i++) {
		if (myTableData[i].id != null) {
			if (checkList[i] == 1) {
				myTableData[i].contSupplyStatus = 'S';
			}
			cleanedGridData.push(myTableData[i]);
		}
	}
	shipmentDetails = [];
	contList = [];
	$.each(cleanedGridData, function (index, object) {
		var shipmentDetail = new Object();
		if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"])) {
			$.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
			errorFlg = true;
		}
		shipmentDetail.containerNo = object["containerNo"];
		contList.push(object["containerNo"]);
		shipmentDetail.contSupplyRemark = object["contSupplyRemark"];
		shipmentDetail.contSupplyStatus = object["contSupplyStatus"];
		shipmentDetail.location = object["location"];
		shipmentDetail.containerRemark = object["containerRemark"];
		shipmentDetail.id = object["id"];
		shipmentDetails.push(shipmentDetail);
	});

	if (!errorFlg) {
		contList.sort();
		let contTemp = "";
		$.each(contList, function (index, cont) {
			if (cont && cont == contTemp) {
				$.modal.alertError("Số container không được giống nhau!");
				errorFlg = true;
				return false;
			}
			contTemp = cont;
		});
	}

	if (errorFlg) {
		return false;
	} else {
		return true;
	}
}


function logisticInfo(id, logistics) {
	$.modal.openLogisticInfo("Thông tin liên lạc " + logistics, ctx + "om/support/logistics/" + id + "/info", null, 470, function () {
		$.modal.close();
	});
}


function loadListComment(shipmentCommentId) {
	let req = {
		serviceType: 3,
		shipmentId: shipmentSelected.id
	};
	$.ajax({
		url: ctx + "shipment-comment/shipment/list",
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify(req),
		success: function (data) {
			if (data.code == 0) {
				let html = '';
				// set title for panel comment
				let commentTitle = '<span style="color: black">Hỗ Trợ<span>';
				let commentNumber = 0;
				if (data.shipmentComments != null) {
					data.shipmentComments.forEach(function (element, index) {
						let createTime = element.createTime;
						let date = '';
						let time = '';
						if (createTime) {
							date = createTime.substring(8, 10) + "/" + createTime.substring(5, 7) + "/" + createTime.substring(0, 4);
							time = createTime.substring(10, 19);
						}

						let resolvedBackground = '';
						if ((shipmentCommentId && shipmentCommentId == element.id) || !element.resolvedFlg) {
							resolvedBackground = 'style="background-color: #ececec;"';
							commentNumber++;
						}

						html += '<div ' + resolvedBackground + '>';
						// User name comment and date time comment
						html += '<div><i style="font-size: 15px; color: #015198;" class="fa fa-user-circle" aria-hidden="true"></i><span> <a>' + element.userName + ' (' + element.userAlias + ')</a>: <i>' + date + ' at ' + time + '</i></span></div>';
						// Topic comment
						html += '<div><span><strong>Yêu cầu:</strong> ' + element.topic + '</span></div>';
						// Content comment
						html += '<div><span>' + element.content.replaceAll("#{domain}", domain) + '</span></div>';
						html += '</div>';
						html += '<hr>';
					});
				}
				commentTitle += ' <span class="round-notify-count">' + commentNumber + '</span>';
				$('#right-layout').layout('panel', 'expandSouth').panel('setTitle', commentTitle);
				$('#commentList').html(html);
				// $("#comment-div").animate({ scrollTop: $("#comment-div")[0].scrollHeight}, 1000);
			}
		}
	});
}

function reloadShipmentDetail() {
	checkList = Array(rowAmount).fill(0);
	allChecked = false;
	$('.checker').prop('checked', false);
	for (let i = 0; i < checkList.length; i++) {
		$('#check' + i).prop('checked', false);
	}
	// loadTable();
	loadShipmentDetail(shipmentSelected.id);
}

function formatServiceType(value, row) {
	switch (value) {
		case 1:
			return "Bốc Hàng";
		case 2:
			return "Hạ Rỗng";
		case 3:
			return "Bốc Rỗng";
		case 4:
			return "Hạ Hàng";
		default:
			return "";
	}
}

function getRequestConfigIcon(row) {
	if (sourceData[row].supportStatus == 'Y') {
		return '<i id="verify" class="fa fa-check easyui-tooltip" title="Yêu cầu xác nhật đã được duyệt" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394"></i>';
	}
	return `<i id="verify" class="fa fa-check easyui-tooltip" title="Đang chờ yêu cầu xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #f8ac59"></i>`;
}

function confirmDocument() {
	let checkIds = [];
	for (let i = 0; i < checkList.length; ++i) {
		if (checkList[i] == 1) {
			checkIds.push(sourceData[i].id);
		}
	}
	$.ajax({
		url: PREFIX + "/confirm",
		type: "post",
		data: {
			checkIds: checkIds.join(",")
		},
		success: function (res) {
			$.modal.msgSuccess("Xác nhận thành công.");
			// loadShipmentDetail(shipmentSelected.id);
			loadTable();
			// loadShipmentDetail(shipmentSelected.id);
			// reloadShipmentDetail();
		},
	});
}
