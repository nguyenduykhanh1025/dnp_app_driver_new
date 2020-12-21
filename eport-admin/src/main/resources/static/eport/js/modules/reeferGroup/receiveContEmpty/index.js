const PREFIX = ctx + "reefer-group/receive-cont-empty";
const greenBlackColor = "rgb(104 241 131)";
var rowAmount = 0;
var allChecked = false;
var checkList = [];
var shipment = new Object();
shipment.params = new Object();
shipment.params.sztp = "R";
var shipmentSelected;
var shipmentDetailIds;
var sourceData, isChange;

var shipmentDetailSearch = new Object();
shipmentDetailSearch.sztp = "R";
shipmentDetailSearch.contSupplyStatus = "R";

const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var dogrid = document.getElementById("container-grid"), hot;

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
	// loadTable();
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

	$("#batchStatus").combobox({
		onChange: function (serviceType) {
			shipment.contSupplyStatus = serviceType.value;
			search();
		}
	});
	getSelectedForShipmentDetails();
});

function search() {
	shipmentDetailSearch.bookingNo = $("#blNo").textbox('getText').toUpperCase();
	shipmentDetailSearch.containerNo = $("#containerNo").textbox('getText').toUpperCase();
	shipmentDetailSearch.contSupplyStatus = $('#batchStatus').combobox('getValue');
	// loadTable();
	loadShipmentDetails();
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
	// loadTable();
}

function clearInput() {
	$("#blNo").textbox('setText', '');
	shipment = new Object();
	shipment.params = new Object();
	// loadTable();
}

function getSelectedForShipmentDetails(index, row) {
	loadShipmentDetails();
	// loadListComment();
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

// FORMAT HANDSONTABLE COLUMN
function checkBoxRenderer(instance, td, row, col, prop, value, cellProperties) {
	let content = '';
	if (checkList[row] == 1) {
		content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')" checked></div>';
	} else {
		content += '<div><input type="checkbox" id="check' + row + '" onclick="check(' + row + ')"></div>';
	}
	$(td).attr('id', 'checkbox' + row).addClass("htCenter").addClass("htMiddle").html(content);
	return td;
}
function statusIconsRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'statusIcon' + row).addClass("htCenter").addClass("htMiddle");
	if (sourceData[row] && sourceData[row].contSupplyStatus && sourceData[row].processStatus && sourceData[row].paymentStatus && sourceData[row].finishStatus) {
		// Command container supply status
		let contSupply = '<i id="contSupply" class="fa fa-check easyui-tooltip" title="Chưa yêu cầu cấp container" aria-hidden="true" style="font-size: 15px; color: #666"></i>';
		switch (sourceData[row].contSupplyStatus) {
			case 'R':
				contSupply = '<i id="contSupply" class="fa fa-check easyui-tooltip" title="Đang chờ cấp container" aria-hidden="true" style="font-size: 15px; color : #f8ac59;"></i>';
				break;
			case 'Y':
				contSupply = '<i id="contSupply" class="fa fa-check easyui-tooltip" title="Đã cấp container" aria-hidden="true" style="font-size: 15px; color: #1ab394;"></i>';
				break;
		}
		// Command process status
		let process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Chưa xác nhận" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';
		switch (sourceData[row].processStatus) {
			case 'E':
				process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ kết quả" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
				break;
			case 'Y':
				process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đã làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
				break;
			case 'N':
				if (value > 1 && sourceData[row].contSupplyStatus == 'Y') {
					process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Có thể làm lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
				}
				break;
			case 'D':
				process = '<i id="verify" class="fa fa-windows easyui-tooltip" title="Đang chờ hủy lệnh" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #f93838;"></i>';
				break;
		}
		// Payment status
		let payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chưa Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #666"></i>';
		switch (sourceData[row].paymentStatus) {
			case 'E':
				payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Lỗi Thanh Toán" aria-hidden="true" style="margin-left: 8px; color : #ed5565;"></i>';
				break;
			case 'Y':
				payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Đã Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
				break;
			case 'N':
				if (value > 2) {
					payment = '<i id="payment" class="fa fa-credit-card-alt easyui-tooltip" title="Chờ Thanh Toán" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
				}
				break;
		}
		// released status
		let released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Chưa thể nhận container" aria-hidden="true" style="margin-left: 8px; color: #666;"></i>';
		switch (sourceData[row].finishStatus) {
			case 'Y':
				released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Đã Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #1ab394;"></i>';
				break;
			case 'N':
				if (sourceData[row].paymentStatus == 'Y') {
					released = '<i id="finish" class="fa fa-truck fa-flip-horizontal easyui-tooltip" title="Có Thể Nhận Container" aria-hidden="true" style="margin-left: 8px; color: #3498db;"></i>';
				}
				break;
		}
		// Return the content
		let content = contSupply + process + payment + released;
		if (!value) {
			value = '';
		}
		$(td).attr('id', 'statusIcons' + row).addClass("htMiddle").addClass("htCenter");
		$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + content + '</div>');
	}
	return td;
}
function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
	if (!value) {
		value = '';
	}
	$(td).attr('id', 'containerNo' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}

function edoFlagRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'filePath' + row).addClass("htMiddle").addClass("htCenter");
	$.ajax({
		type: "GET",
		url: PREFIX + "/shipments/" + sourceData[row].shipmentId,
		contentType: "application/json",
		success: function (data) {
			if (data.code == 0) {
				if (data.shipment) {
					let html = '';
					if (data.shipment.edoFlg == "0") {
						html += "DO"
					} else {
						html += "eDO"
					}
					$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + html + '</div>');
					return td;
				}
			}
		}
	});
}

function filePathRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'filePath' + row).addClass("htMiddle").addClass("htCenter");
	$.ajax({
		type: "GET",
		url: PREFIX + "/shipments/" + sourceData[row].shipmentId + "/shipment-images",
		contentType: "application/json",
		success: function (data) {
			if (data.code == 0) {
				if (data.shipmentFiles != null && data.shipmentFiles.length > 0) {
					let html = '';
					data.shipmentFiles.forEach(function (element, index) {
						html += ' <a href="' + element.path + '" target="_blank"><i class="fa fa-paperclip" style="font-size: 18px;"></i> ' + (index + 1) + '</a>';
					});
					// $('#attachFile').html(html);
					$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + html + '</div>');
					return td;
				}
			}
		}
	});
}
function contSupplyRemarkRenderer(instance, td, row, col, prop, value, cellProperties) {
	if (!value) {
		value = '';
	}
	$(td).attr('id', 'contSupplyRemark' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
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

function sztpRenderer(instance, td, row, col, prop, value, cellProperties) {
	if (!value) {
		value = '';
	}
	$(td).attr('id', 'sztp' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function locationRenderer(instance, td, row, col, prop, value, cellProperties) {
	let backgroundColor = "";
	if (row % 2 == 1) {
		backgroundColor = greenBlackColor;
	} else {
		backgroundColor = "#C6EFCE";
	}
	if (!value) {
		value = '';
		backgroundColor = "rgb(232, 232, 232)";
	}
	cellProperties.readOnly = 'true';
	$(td).css("background-color", backgroundColor);
	$(td).attr('id', 'location' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function containerRemarkRenderer(instance, td, row, col, prop, value, cellProperties) {
	let backgroundColor = "";
	if (row % 2 == 1) {
		backgroundColor = greenBlackColor;
	} else {
		backgroundColor = "#C6EFCE";
	}
	if (!value) {
		value = '';
		backgroundColor = "rgb(232, 232, 232)";
	}
	cellProperties.readOnly = 'true';
	$(td).css("background-color", backgroundColor);
	$(td).attr('id', 'containerRemark' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function opeCodeRenderer(instance, td, row, col, prop, value, cellProperties) {
	if (!value) {
		value = '';
	}
	$(td).attr('id', 'opeCode' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function planningDateRenderer(instance, td, row, col, prop, value, cellProperties) {
	if (value != null && value != '') {
		if (value.substring(2, 3) != "/") {
			value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
		}
	}
	if (!value) {
		value = '';
	}
	$(td).attr('id', 'planningDate' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function cargoTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
	if (!value) {
		value = '';
	}
	$(td).attr('id', 'cargoType' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function qualityRequirementRenderer(instance, td, row, col, prop, value, cellProperties) {
	if (!value) {
		value = '';
	}
	$(td).attr('id', 'qualityRequirement' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
	if (!value) {
		value = '';
	}
	$(td).attr('id', 'consignee' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
	if (!value) {
		value = '';
	}
	$(td).attr('id', 'vslNm' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function voyNoRenderer(instance, td, row, col, prop, value, cellProperties) {
	if (!value) {
		value = '';
	}
	$(td).attr('id', 'voyNo' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
	if (!value) {
		value = '';
	}
	$(td).attr('id', 'dischargePort' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
	if (!value) {
		value = '';
	}
	$(td).attr('id', 'remark' + row).addClass("htMiddle").addClass("htCenter");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
// CONFIGURATE HANDSONTABLE
function configHandson() {
	config = {
		stretchH: "all",
		height: $('#right-side__main-table').height() - 40,
		minRows: rowAmount,
		maxRows: rowAmount,
		width: "100%",
		minSpareRows: 0,
		rowHeights: 30,
		fixedColumnsLeft: 3,
		manualColumnResize: true,
		manualRowResize: false,
		renderAllRows: true,
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
					return "Trạng Thái";
				case 2:
					return "Booking no";
				case 3:
					return "Loại Lệnh";
				case 4:
					return "Đính Kèm Tệp";
				case 5:
					return "Số Container";
				case 6:
					return "Ghi Chú <br>Cấp/Duyệt Container";
				case 7:
					return "Hãng Tàu";
				case 8:
					return "Nhiệt Độ (c)";
				case 9:
					return "Độ Ẩm (%)";
				case 10:
					return "Thông Gió (%)";
				case 11:
					return "Sztp";
				case 12:
					return "Vị Trí";
				case 13:
					return "Tình Trạng Container";
				case 14:
					return "Thời Gian <br>Dự Kiến Bốc";
				case 15:
					return "Loại Hàng";
				case 16:
					return "Yêu Cầu Chất <br>Lượng Vỏ";
				case 17:
					return "Chủ hàng";
				case 18:
					return "Tên Tàu";
				case 19:
					return "Chuyến";
				case 20:
					return "Cảng Dỡ";
				case 21:
					return "Ghi Chú (K/H)";
			}
		},
		colWidths: [21, 91, 100, 100, 100, 100, 150, 80, 80, 80, 100, 100, 150, 150, 82, 95, 100, 100, 60, 58, 66, 150],
		filter: "true",
		columns: [
			{
				data: "active",
				type: "checkbox",
				className: "htCenter",
				renderer: checkBoxRenderer
			},
			{
				data: "status",
				readOnly: true,
				renderer: statusIconsRenderer
			},
			{
				data: "bookingNo",
				renderer: containerNoRenderer
			},
			{
				data: "edoFlag",
				renderer: edoFlagRenderer
			},
			{
				data: "filePaths",
				renderer: filePathRenderer
			},
			{
				data: "containerNo",
				renderer: containerNoRenderer
			},
			{
				data: "contSupplyRemark",
				renderer: contSupplyRemarkRenderer
			},
			{
				data: "opeCode",
				renderer: opeCodeRenderer
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
				data: "sztp",
				renderer: sztpRenderer
			},
			{
				data: "location",
				renderer: locationRenderer
			},
			{
				data: "containerRemark",
				renderer: containerRemarkRenderer
			},
			{
				data: "planningDate",
				renderer: planningDateRenderer
			},
			{
				data: "cargoType",
				renderer: cargoTypeRenderer
			},
			{
				data: "qualityRequirement",
				renderer: qualityRequirementRenderer
			},
			{
				data: "consignee",
				renderer: consigneeRenderer
			},
			{
				data: "vslNm",
				renderer: vslNmRenderer
			},
			{
				data: "voyNo",
				renderer: voyNoRenderer
			},
			{
				data: "dischargePort",
				renderer: dischargePortRenderer
			},
			{
				data: "remark",
				renderer: remarkRenderer
			}
		],
		afterChange: function (changes, src) {
			//Get data change in cell to render another column
			if (!changes) {
				return;
			}
			let containerOrderList = '';
			changes.forEach(function (change) {
				if (change[1] == "containerNo") {
					if (change[3] && /[A-Z]{4}[0-9]{7}/g.test(change[3])) {
						$.modal.loading("Đang xử lý...");

						// Call data to auto-fill
						$.ajax({
							url: PREFIX + "/shipment-detail/cont/info",
							type: "POST",
							contentType: "application/json",
							data: JSON.stringify({
								containerNo: change[3]
							})
						}).done(function (res) {
							if (res.code == 0) {
								let shipmentDetail = res.shipmentDetailResult;
								hot.setDataAtCell(change[0], 12, shipmentDetail.location); // location
								hot.setDataAtCell(change[0], 13, shipmentDetail.containerRemark); // container remark

								if (res.isOrder) {
									containerOrderList += change[3] + ',';
								}
							}
						});
					} else {
						hot.setDataAtCell(change[0], 12, ''); // location
						hot.setDataAtCell(change[0], 13, ''); // container remark
					}
				}
			});
			$.modal.closeLoading();
			if (containerOrderList.length > 0) {
				$.modal.alertWarning("Container " + containerOrderList.substring(0, containerOrderList.length - 1) + " đã được làm lệnh trên eport, vui lòng yêu cầu hủy lệnh cho container này trước khi cấp cho booking khác.");
			}
		},
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
					if (selected[3] == 15) {
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
	$.ajax({
		url: PREFIX + "/shipment/" + id + "/shipment-detail",
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

function saveInput() {
	if (getDataFromTable()) {
		let contAmount = 0;
		let containers = '';
		$.each(shipmentDetails, function (index, object) {
			if (object['contSupplyStatus'] == 'S') {
				contAmount++;
				containers += object['containerNo'] + ',';
			}
		});
		if (contAmount > 0) {
			containers = containers.substring(0, containers.length - 1);
			$.modal.confirm("Xác nhận cấp " + contAmount + " container: " + containers + "?", function () {
				saveData()
			});
		} else {
			$.modal.confirm("Xác nhận lưu thông tin, chưa chỉ định <br>container nào cho logistic?", function () {
				saveData()
			});
		}
	}
}

function logisticInfo(id, logistics) {
	$.modal.openLogisticInfo("Thông tin liên lạc " + logistics, ctx + "om/support/logistics/" + id + "/info", null, 470, function () {
		$.modal.close();
	});
}

function saveData() {
	$.modal.loading("Đang xử lý...");
	let shipmentDetailChooses = [];
	for (let i = 0; i < checkList.length; ++i) {
		if (checkList[i] == 1) {
			shipmentDetailChooses.push(shipmentDetails[i]);
		}
	}
	$.ajax({
		url: PREFIX + "/confirm",
		method: "post",
		contentType: "application/json",
		accept: 'text/plain',
		data: JSON.stringify(shipmentDetailChooses),
		dataType: 'text',
		success: function (data) {
			var result = JSON.parse(data);
			if (result.code == 0) {
				$.modal.msgSuccess(result.msg);
				// loadShipmentDetail(shipmentSelected.id);
				loadShipmentDetails();
			} else {
				$.modal.msgError(result.msg);
			}
			$.modal.closeLoading();
		},
		error: function (result) {
			$.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau.");
			$.modal.closeLoading();
		},
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

function addComment() {
	let topic = $('#topic').textbox('getText');
	let content = $('.summernote').summernote('code');// get editor content
	let errorFlg = false;
	if (!topic) {
		errorFlg = true;
		$.modal.alertWarning('Vui lòng nhập chủ đề.');
	} else if (!content) {
		errorFlg = true;
		$.modal.alertWarning('Vui lòng nhập nội dung.');
	}
	if (!errorFlg) {
		let req = {
			topic: topic,
			content: content,
			shipmentId: shipmentSelected.id,
			logisticGroupId: shipmentSelected.logisticGroupId
		};
		$.ajax({
			url: PREFIX + "/shipment/comment",
			type: "post",
			contentType: "application/json",
			data: JSON.stringify(req),
			beforeSend: function () {
				$.modal.loading("Đang xử lý, vui lòng chờ...");
			},
			success: function (result) {
				$.modal.closeLoading();
				if (result.code == 0) {
					loadListComment(result.shipmentCommentId);
					$.modal.msgSuccess("Gửi thành công.");
					$('#topic').textbox('setText', '');
					$('.summernote').summernote('code', '');
				} else {
					$.modal.msgError("Gửi thất bại.");
				}
			},
			error: function (error) {
				$.modal.closeLoading();
				$.modal.msgError("Gửi thất bại.");
			}
		});
	}
}

function rejectSupply() {
	if (getDataSelectedFromTable()) {
		layer.open({
			type: 2,
			area: [600 + 'px', 240 + 'px'],
			fix: true,
			maxmin: true,
			shade: 0.3,
			title: 'Xác nhận từ chối yêu cầu cấp container',
			content: PREFIX + "/confirmation",
			btn: ["Xác Nhận", "Hủy"],
			shadeClose: false,
			yes: function (index, layero) {
				rejectSupplyReq(index, layero);
			},
			cancel: function (index) {
				return true;
			}
		});
	}
}

function deleteSupply() {
	if (getDataSelectedFromTable()) {
		layer.open({
			type: 2,
			area: [600 + 'px', 240 + 'px'],
			fix: true,
			maxmin: true,
			shade: 0.3,
			title: 'Xác nhận xóa yếu cầu cấp container',
			content: PREFIX + "/confirmation",
			btn: ["Xác Nhận", "Hủy"],
			shadeClose: false,
			yes: function (index, layero) {
				deleteSupplyReq(index, layero);
			},
			cancel: function (index) {
				return true;
			}
		});
	}
}

function rejectSupplyReq(index, layero) {

	let childLayer = layero.find("iframe")[0].contentWindow.document;
	$.modal.loading("Đang xử lý...");
	const payload = [];
	for (let i = 0; i < checkList.length; ++i) {
		if (checkList[i] == 1) {
			payload.push(sourceData[i]);
		}
	}
	$.ajax({
		url: PREFIX + "/reject",
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify(payload),
		success: function (data) {
			layer.close(index);
			loadShipmentDetails();
			$.modal.closeLoading();

			rejectSupplyReqSendInformation(index, layero);
		},
		error: function (result) {
			$.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau.");
			$.modal.closeLoading();
		},
	});

}

function rejectSupplyReqSendInformation(index, layero) {
	let childLayer = layero.find("iframe")[0].contentWindow.document;
	$.ajax({
		url: PREFIX + "/reject-information",
		method: "POST",
		data: {
			content: $(childLayer).find("#message").val(),
			shipmentDetailIds: shipmentDetailIds,
		},
		success: function (res) {
			$.modal.alertSuccess("Xác nhận không thể cấp container thành công.");
		},
		error: function (data) {
		}
	});

}

function deleteSupplyReq(index, layero) {
	let childLayer = layero.find("iframe")[0].contentWindow.document;
	$.modal.loading("Đang xử lý ...");
	$.ajax({
		url: PREFIX + "/delete",
		method: "POST",
		data: {
			content: $(childLayer).find("#message").val(),
			shipmentDetailIds: shipmentDetailIds,
		},
		success: function (res) {
			$.modal.closeLoading();
			layer.close(index);
			loadShipmentDetails();
			if (res.code == 0) {
				$.modal.alertSuccess(res.msg);
			} else {
				$.modal.alertError(res.msg);
			}
		},
		error: function (data) {
			layer.close(index);
			loadShipmentDetails();
			// $.modal.closeLoading();
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

function loadShipmentDetails() {

	$.ajax({
		url: PREFIX + "/shipment-details",
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify(shipmentDetailSearch),
		success: function (data) {
			if (data.code == 0) {
				rowAmount = data.shipmentDetails.length;
				checkList = Array(rowAmount).fill(data.shipmentDetails.length);
				allChecked = false;

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

function openDetail(id, containerNo, sztp, row, cargoType) {
	if (!id) {
		$.modal.alertWarning(
			"Container chưa được lưu. Vui lòng lưu khai báo trước."
		);
	}
	else {
		$.modal.openCustomForm(
			"Khai báo chi tiết",
			`${PREFIX}/shipment-detail/${id}/cont/${containerNo}/sztp/${sztp}/detail`,
			800,
			460
		);
	}
}