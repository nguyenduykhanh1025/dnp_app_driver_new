const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
const regexRemoveHtml = /(<([^>]+)>)/ig;
var prefix = ctx + "logistic/receive-cont-empty";
var interval, currentPercent, timeout;
var dogrid = document.getElementById("container-grid"), hot;
var shipmentSelected, shipmentDetails, shipmentDetailIds, sourceData, orderNumber = 0, currentVslNm;
var contList = [], orders = [], processOrderIds, sztpListDisable = [];
var conts = '';
var allChecked = false;
var checkList = [];
var vslNmList, consigneeList;
var rowAmount = 0;
var shipmentSearch = new Object();
shipmentSearch.params = new Object();
shipmentSearch.serviceType = 3;
var sizeList = [];
var berthplanList;
var onChangeFlg = false, currentIndexRow, rejectChange = false, dischargePortList = [], currentVesselVoyage = '';
var fromDate, toDate;
var myDropzone;
//dictionary sizeList
var cargoTypeList = ["AK:Over Dimension", "BB:Break Bulk", "BN:Bundle", "DG:Dangerous", "DR:Reefer & DG", "DE:Dangerous Empty", "FR:Fragile", "GP:General", "MT:Empty", "RF:Reefer"];
var temperatureDisable = [];
var detailInformationForContainerSpecial = {
	data: [],
	indexSelected: -1,
};

$.ajax({
	type: "GET",
	url: ctx + "logistic/size/container/list",
	success(data) {
		if (data.code == 0) {
			data.data.forEach(element => {
				sizeList.push(element['dictLabel'])
			})
		}
	}
});

$.ajax({
	url: ctx + "logistic/source/consignee",
	method: "GET",
	success: function (data) {
		if (data.code == 0) {
			consigneeList = data.consigneeList;
		}
	}
});

$.ajax({
	url: prefix + "/berthplan/vessel-voyage/list",
	method: "GET",
	success: function (data) {
		if (data.code == 0) {
			berthplanList = data.berthplanList;
			vslNmList = data.vesselAndVoyages;
		}
	}
});

var toolbar = [
	{
		text: '<button class="btn btn-sm btn-default"><i class="fa fa-plus text-success"></i> Thêm</button>',
		handler: function () {
			$.operate.addShipment();
		},
	},
	{
		text: '<button class="btn btn-sm btn-default" ><i class="fa fa-edit text-warning"></i> Sửa</button>',
		handler: function () {
			$.operate.editShipment();
		},
	},
	{
		text: '<button class="btn btn-sm btn-default"><i class="fa fa-remove text-danger"></i> Xóa</button>',
		handler: function () {
			removeShipment()
		},
	},
	{
		text: '<button class="btn btn-sm btn-default"><i class="fa fa-refresh text-success"></i></button>',
		handler: function () {
			handleRefresh();
		},
	},
];

$(".main-body").layout();

$(".collapse").click(function () {
	$(".main-body__search-wrapper").hide();
	$(".main-body__search-wrapper--container").hide();
	$(this).hide();
	$(".uncollapse").show();
});

$(".uncollapse").click(function () {
	$(".main-body__search-wrapper").show();
	$(".main-body__search-wrapper--container").show();
	$(this).hide();
	$(".collapse").show();
});

$(".left-side__collapse").click(function () {
	$("#main-layout").layout("collapse", "west");
	setTimeout(() => {
		hot.render();
	}, 200);
});

$('#main-layout').layout({
	onExpand: function (region) {
		if (region == "west") {
			hot.render();
		}
	}
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
			let req = {
				shipmentId: shipmentSelected.id
			}
			$.ajax({
				url: ctx + "logistic/comment/update",
				type: "post",
				contentType: "application/json",
				data: JSON.stringify(req),
				success: function (res) {
					if (res.code == 0) {
						let commentTitle = '<span>Hỗ Trợ</span> <span class="round-notify-count">0</span>';
						$('#right-layout').layout('panel', 'expandSouth').panel('setTitle', commentTitle);
					}
				}
			});
		}
	}
});

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {

	// DROP ZONE configuration
	// Get the template HTML and remove it from the doumenthe template HTML and remove it from the doument

	let previewTemplate = '<span data-dz-name></span>';

	if (shipmentSelected == null) {
		shipmentSelected = new Object();
	}

	myDropzone = new Dropzone("#dropzone", {
		url: ctx + "logistic/shipment/" + shipmentSelected.id + "/file/attach",
		method: "post",
		paramName: "file",
		maxFiles: 5,
		maxFilesize: 10, //MB
		// autoProcessQueue: false,
		previewTemplate: previewTemplate,
		previewsContainer: "#previews", // Define the container to display the previews
		clickable: "#attachButton", // Define the element that should be used as click trigger to select files.
		init: function () {
			this.on("maxfilesexceeded", function (file) {
				$.modal.alertWarning("Số lượng tệp đính kèm vượt giới hạn cho phép, quý khách vui lòng đính kèm tệp trong lần comment tiếp theo.");
				this.removeFile(file);
			});
		},
		success: function (file, response) {
			if (response.code == 0) {
				$.modal.msgSuccess("Đính kèm tệp thành công.");
				let content = '<p><a href="' + response.fileLink + '" target="_blank">' + file.upload.filename + '</a></p>';
				$('#content').summernote('editor.pasteHTML', content);
			} else {
				$.modal.msgError("Đính kèm tệp thất bại, quý khách vui lòng thử lại sau.");
			}
		}
	});

	$('#right-layout').layout('collapse', 'south');
	setTimeout(() => {
		hot.updateSettings({ height: $('#right-side__main-table').height() - 35 });
		hot.render();
	}, 200);

	$("#shipmentStatus").combobox({
		onSelect: function (option) {
			shipmentSearch.status = option.value;
			loadTable();
		}
	});

	if (sId != null) {
		shipmentSearch.id = sId;
	}

	$("#bookingNo").textbox('textbox').bind('keydown', function (e) {
		// enter key
		if (e.keyCode == 13) {
			shipmentSearch.bookingNo = $("#bookingNo").textbox('getText').toUpperCase();
			loadTable();
		}
	});

	$("#containerNo").textbox('textbox').bind('keydown', function (e) {
		// enter key
		if (e.keyCode == 13) {
			shipmentSearch.params.containerNo = $("#containerNo").textbox('getText').toUpperCase();
			loadTable();
		}
	});

	$("#consignee").textbox('textbox').bind('keydown', function (e) {
		// enter key
		if (e.keyCode == 13) {
			shipmentSearch.params.consignee = $("#consignee").textbox('getText').toUpperCase();
			loadTable();
		}
	});

	$('#fromDate').datebox({
		onSelect: function (date) {
			date.setHours(0, 0, 0);
			fromDate = date;
			if (toDate != null && date.getTime() > toDate.getTime()) {
				$.modal.alertWarning("Từ ngày không được lớn hơn đến ngày.");
			} else {
				shipmentSearch.params.fromDate = dateToString(date);
				loadTable();
			}
			return date;
		}
	});

	// let now = new Date();
	// now = new Date(now.getFullYear(), now.getMonth(), 1);
	// let nowStr = ("0" + now.getDate()).slice(-2) + "/" + ("0" + (now.getMonth() + 1)).slice(-2) + "/" + now.getFullYear();
	// $('#fromDate').datebox('setValue', nowStr);
	// shipmentSearch.params.fromDate = dateToString(now);

	$('#toDate').datebox({
		onSelect: function (date) {
			date.setHours(23, 59, 59);
			toDate = date;
			if (fromDate != null && date.getTime() < fromDate.getTime()) {
				$.modal.alertWarning("Đến ngày không được thấp hơn từ ngày.");
			} else {
				shipmentSearch.params.toDate = dateToString(date);
				loadTable();
			}
		}
	});

	$(function () {
		var options = {
			createUrl: prefix + "/shipment/add",
			updateUrl: "0",
			modalName: " Lô"
		};
		$.table.init(options);
	});

	loadTable();
});

function dateformatter(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return (d < 10 ? ('0' + d) : d) + '/' + (m < 10 ? ('0' + m) : m) + '/' + y;
}
function dateparser(s) {
	var ss = (s.split('\.'));
	var d = parseInt(ss[0], 10);
	var m = parseInt(ss[1], 10);
	var y = parseInt(ss[2], 10);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
		return new Date(y, m - 1, d);
	}
}

// LOAD SHIPMENT LIST
function loadTable() {
	$("#dg").datagrid({
		url: ctx + 'logistic/shipments',
		height: $('.main-body').height() - 75,
		method: 'post',
		singleSelect: true,
		collapsible: true,
		clientPaging: false,
		rownumbers: true,
		pagination: true,
		toolbar: toolbar,
		onBeforeSelect: function (index, row) {
			getSelected(index, row);
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
				contentType: "application/json",
				data: JSON.stringify({
					pageNum: param.page,
					pageSize: param.rows,
					orderByColumn: param.sort,
					isAsc: param.order,
					data: shipmentSearch
				}),
				success: function (data) {
					success(data);
					$("#dg").datagrid("hideColumn", "id");
					$("#dg").datagrid("selectRow", 0);
				},
				error: function () {
					error.apply(this, arguments);
				},
			});
		},
	});
}

// FORMAT REMARK FOR SHIPMENT LIST
function formatRemark(value) {
	return '<div class="easyui-tooltip" title="' + (value != null ? value : "Trống") + '" style="width: 80; text-align: center;"><span>' + (value != null ? value : "") + '</span></div>';
}

// FORMAT DATE FOR SHIPMENT LIST
function formatDate(value) {
	var date = new Date(value);
	var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	var month = date.getMonth() + 1;
	var monthText = month < 10 ? "0" + month : month;
	let hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
	let minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
	return day + "/" + monthText + "/" + date.getFullYear() + " " + hours + ":" + minutes;
}


function handleRefresh() {
	loadTable();
}

// HANDLE WHEN SELECT A SHIPMENT
function getSelected(index, row) {
	if (rejectChange) {
		rejectChange = false;
		return true;
	} else {
		if (onChangeFlg && currentIndexRow != index) {
			layer.confirm("Thông tin khái báo chưa được lưu, quý khách có muốn di chuyển qua trang khác?", {
				icon: 3,
				title: "Xác Nhận",
				btn: ['Đồng Ý', 'Hủy Bỏ']
			}, function () {
				layer.close(layer.index);
				currentIndexRow = index;
				if (row) {
					shipmentSelected = row;
					$(function () {
						var options = {
							createUrl: prefix + "/shipment/add",
							updateUrl: prefix + "/shipment/" + shipmentSelected.id,
							modalName: " Lô"
						};
						$.table.init(options);
					});
					let title = '';
					title += 'Mã Lô: ' + row.id + ' - ';
					title += 'SL: ' + row.containerAmount + ' - ';
					title += 'Booking No: ';
					if (row.bookingNo != null) {
						title += row.bookingNo;
					} else {
						title += 'Trống';
					}
					title += '<span id="attachFile"></span>';
					$('#right-layout').layout('panel', 'center').panel('setTitle', title);
					rowAmount = row.containerAmount;
					checkList = Array(rowAmount).fill(0);
					temperatureDisable = Array(rowAmount).fill(1);
					sztpListDisable = Array(rowAmount).fill(0);
					allChecked = false;
					loadShipmentDetail(row.id);
					toggleAttachIcon(shipmentSelected.id);
					onChangeFlg = false;
					currentIndexRow = index;
					loadListComment();

					// Update dropzone url with the shipment id selected
					myDropzone.options.url = ctx + "logistic/shipment/" + shipmentSelected.id + "/file/attach";
				}
				return true;
			}, function () {
				layer.close(layer.index);
				rejectChange = true;
				$('#dg').datagrid('selectRow', currentIndexRow);
				return false;
			});
		} else {
			currentIndexRow = index;
			if (row) {
				shipmentSelected = row;
				$(function () {
					var options = {
						createUrl: prefix + "/shipment/add",
						updateUrl: prefix + "/shipment/" + shipmentSelected.id,
						modalName: " Lô"
					};
					$.table.init(options);
				});
				let title = '';
				title += 'Mã Lô: ' + row.id + ' - ';
				title += 'SL: ' + row.containerAmount + ' - ';
				title += 'Booking No: ';
				if (row.bookingNo != null) {
					title += row.bookingNo;
				} else {
					title += 'Trống';
				}
				title += '<span id="attachFile"></span>';
				$('#right-layout').layout('panel', 'center').panel('setTitle', title);
				rowAmount = row.containerAmount;
				checkList = Array(rowAmount).fill(0);
				temperatureDisable = Array(rowAmount).fill(1);
				sztpListDisable = Array(rowAmount).fill(0);
				allChecked = false;
				loadShipmentDetail(row.id);
				toggleAttachIcon(shipmentSelected.id);
				onChangeFlg = false;
				currentIndexRow = index;
				loadListComment();

				if (sId != null) {
					$('#right-layout').layout('expand', 'south');
					shipmentSearch.id = null;
					sId = null;
				}

				// Update dropzone url with the shipment id selected
				myDropzone.options.url = ctx + "logistic/shipment/" + shipmentSelected.id + "/file/attach";
			}
			return true;
		}
	}
}

function toggleAttachIcon(shipmentId) {
	$.ajax({
		type: "GET",
		url: prefix + "/shipments/" + shipmentId + "/shipment-images",
		contentType: "application/json",
		success: function (data) {
			if (data.code == 0) {
				if (data.shipmentFiles != null && data.shipmentFiles.length > 0) {
					let html = '';
					data.shipmentFiles.forEach(function (element, index) {
						html += ' <a href="' + element.path + '" target="_blank"><i class="fa fa-paperclip" style="font-size: 18px;"></i> ' + (index + 1) + '</a>';
					});
					$('#attachFile').html(html);
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
		let contSupply = '<i id="contSupply" class="fa fa-check easyui-tooltip" title="Chưa yêu cầu cấp container" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #666"></i>';
		switch (sourceData[row].contSupplyStatus) {
			case 'R':
				contSupply = '<i id="contSupply" class="fa fa-check easyui-tooltip" title="Đang chờ cấp container" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color : #f8ac59;"></i>';
				break;
			case 'Y':
				contSupply = '<i id="contSupply" class="fa fa-check easyui-tooltip" title="Đã cấp container" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #1ab394;"></i>';
				break;
			case 'N':
				contSupply = '<i id="contSupply" class="fa fa-check easyui-tooltip" title="Có thể yêu cầu cấp container" aria-hidden="true" style="margin-left: 8px; font-size: 15px; color: #3498db;"></i>';
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
		let content = '<div>' + contSupply + process + payment + released + '</div>';
		$(td).html(content);
	}
	return td;
}

function containerNoRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'containerNo' + row).addClass("htMiddle").addClass("htCenter");

	if (value != null && value != '') {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
			cellProperties.readOnly = 'true';
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (shipmentSelected.specificContFlg == 0) {
		cellProperties.readOnly = 'true';
		$(td).css("background-color", "rgb(232, 232, 232)");
	}
	if (!value) {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function expiredDemRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'expiredDem' + row).addClass("htMiddle").addClass("htCenter");
	if (value != null && value != '') {
		if (value.substring(2, 3) != "/") {
			value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
		}
	} else {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function consigneeRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'consignee' + row).addClass("htMiddle");
	if (value != null && value != '') {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
			cellProperties.readOnly = 'true';
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (!value) {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function planningDateRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'planningDate' + row).addClass("htMiddle").addClass("htCenter");
	if (value != null && value != '') {
		if (value.substring(2, 3) != "/") {
			value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
		}
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
			cellProperties.readOnly = 'true';
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	} else {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function cargoTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'cargoType' + row).addClass("htMiddle").addClass("htCenter");
	if (value != null && value != '') {
		value = value.split(':')[0];
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
			cellProperties.readOnly = 'true';
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (!value) {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function qualityRequirementRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'qualityRequirement' + row).addClass("htMiddle");
	if (value != null && value != '') {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
			cellProperties.readOnly = 'true';
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (!value) {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function vslNmRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'vslNm' + row).addClass("htMiddle");
	if (value != null && value != '') {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
			cellProperties.readOnly = 'true';
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (!value) {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function etaRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'eta' + row).addClass("htMiddle").addClass("htCenter");
	if (value != null && value != '') {
		if (value.substring(2, 3) != "/") {
			value = value.substring(8, 10) + "/" + value.substring(5, 7) + "/" + value.substring(0, 4);
		}
	} else {
		value = '';
	}
	cellProperties.readOnly = 'true';
	$(td).css("background-color", "rgb(232, 232, 232)");
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function sizeRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'sztp' + row).addClass("htMiddle");
	if (value != null && value != '') {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
			cellProperties.readOnly = 'true';
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (sztpListDisable[row] == 1) {
		cellProperties.readOnly = 'true';
		$(td).css("background-color", "rgb(232, 232, 232)");
	}
	if (!value) {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}
function dischargePortRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'dischargePort' + row).addClass("htMiddle").addClass("htCenter");
	if (value != null && value != '') {
		value = value.split(':')[0];
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 2) {
			cellProperties.readOnly = 'true';
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (!value) {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}

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
	let containerNo, sztp = '';
	if (value != null && value != "") {
		if (hot.getDataAtCell(row, 2) && hot.getDataAtCell(row, 3)) {
			containerNo = hot.getDataAtCell(row, 2);
			sztp = hot.getDataAtCell(row, 3);
		}
	}

	if (sourceData && sourceData.length > 0) {
		if (sourceData.length > row && sourceData[row].id && sourceData[row].sztp.substring(0, 4).includes("R")) {
			sztp = sourceData[row].sztp;
			value = `<button class="btn btn-success btn-xs" style="width: 85%;" onclick="openDetail('${sourceData[row].id}', '${containerNo}', '${sztp}', '${row}','${sourceData[row].cargoType}')"><i class="fa fa-book" style="margin: 0 3px;"></i>Lạnh</button>`;
		} else if (containerNo && sztp) {
			value = `<button class="btn btn-success btn-xs" style="width: 85%;" onclick="openDetail('${""}', '${containerNo}', '${sztp}', '${row}','${sourceData[row].cargoType}')"><i class="fa fa-book" style="margin: 0 3px;"></i>Lạnh</button>`;
		}
	}
	$(td).html(value);
	cellProperties.readOnly = "true";
	return td;
}

function temperatureRenderer(instance, td, row, col, prop, value, cellProperties) {
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
	if (temperatureDisable[row] == 1) {
		$(td).html("");
		cellProperties.readOnly = "true";
		$(td).css("background-color", "rgb(232, 232, 232)");
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

function humidityRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td)
		.attr("id", "humidity" + row)
		.addClass("htMiddle")
		.addClass("htRight");
	if (value !== null && value !== "") {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
			cellProperties.readOnly = "true";
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (temperatureDisable[row] == 1) {
		$(td).html("");
		cellProperties.readOnly = "true";
		$(td).css("background-color", "rgb(232, 232, 232)");
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
function ventilationRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td)
		.attr("id", "ventilation" + row)
		.addClass("htMiddle")
		.addClass("htRight");
	if (value !== null && value !== "") {
		if (hot.getDataAtCell(row, 1) != null && hot.getDataAtCell(row, 1) > 1) {
			cellProperties.readOnly = "true";
			$(td).css("background-color", "rgb(232, 232, 232)");
		}
	}
	if (temperatureDisable[row] == 1) {
		$(td).html("");
		cellProperties.readOnly = "true";
		$(td).css("background-color", "rgb(232, 232, 232)");
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

function contSupplyRemarkRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'remark' + row).addClass("htMiddle");
	cellProperties.readOnly = 'true';
	$(td).css("background-color", "rgb(232, 232, 232)");
	if (!value) {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}

function payTypeRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'payType' + row).addClass("htMiddle").addClass("htCenter");
	if (!value) {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	cellProperties.readOnly = 'true';
	$(td).css("background-color", "rgb(232, 232, 232)");
	return td;
}

function payerRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'payer' + row).addClass("htMiddle").addClass("htCenter");
	if (!value) {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	cellProperties.readOnly = 'true';
	$(td).css("background-color", "rgb(232, 232, 232)");
	return td;
}

function payerNameRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'payerNamer' + row).addClass("htMiddle");
	if (!value) {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	cellProperties.readOnly = 'true';
	$(td).css("background-color", "rgb(232, 232, 232)");
	return td;
}

function remarkRenderer(instance, td, row, col, prop, value, cellProperties) {
	$(td).attr('id', 'remark' + row).addClass("htMiddle");
	if (!value) {
		value = '';
	}
	$(td).html('<div style="width: 100%; white-space: nowrap; text-overflow: ellipsis; text-overflow: ellipsis;">' + value + '</div>');
	return td;
}

// CONFIGURATE HANDSONTABLE
function configHandson() {
	config = {
		stretchH: "all",
		height: $('#right-side__main-table').height() - 35,
		minRows: rowAmount,
		maxRows: rowAmount,
		width: "100%",
		minSpareRows: 0,
		rowHeights: 30,
		fixedColumnsLeft: 3,
		trimDropdown: false,
		manualColumnResize: true,
		manualRowResize: true,
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
					return "Container No";
				case 3:
					return '<span class="required">Kích Thước</span>';
				case 4:
					return '<span class="required">Hạn Lệnh</span>';
				case 5:
					return '<span class="required">Chủ Hàng</span>';
				case 6:
					return '<span class="required">Ngày Dự <br>Kiến Bốc</span>';
				case 7:
					return '<span class="required">Loại Hàng</span>';
				case 8:
					return 'Yêu Cầu <br>Chất Lượng';
				case 9:
					return '<span class="required">Tàu và Chuyến</span>';
				case 10:
					return "Ngày tàu đến";
				case 11:
					return '<span class="required">Cảng Dỡ Hàng</span>';
				case 12:
					return 'Chi Tiết Container';
				case 13:
					return 'Cấp Container <br>Ghi Chú';
				case 14:
					return 'PTTT';
				case 15:
					return 'Mã Số Thuế';
				case 16:
					return 'Người Thanh Toán';
				case 17:
					return "Ghi Chú";
			}
		},
		colWidths: [40, 100, 100, 150, 100, 200, 100, 80, 150, 150, 100, 120, 130, 150, 100, 130, 130, 200],
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
				data: "containerNo",
				renderer: containerNoRenderer
			},
			{
				data: "sztp",
				type: "autocomplete",
				source: sizeList,
				strict: true,
				renderer: sizeRenderer
			},
			{
				data: "expiredDem",
				type: "date",
				dateFormat: "DD/MM/YYYY",
				correctFormat: true,
				defaultDate: new Date(),
				renderer: expiredDemRenderer
			},
			{
				data: "consignee",
				strict: true,
				type: "autocomplete",
				source: consigneeList,
				renderer: consigneeRenderer
			},
			{
				data: "planningDate",
				type: "date",
				dateFormat: "DD/MM/YYYY",
				correctFormat: true,
				defaultDate: new Date(),
				renderer: planningDateRenderer
			},
			{
				data: "cargoType",
				type: "autocomplete",
				source: cargoTypeList,
				strict: true,
				renderer: cargoTypeRenderer
			},
			{
				data: "qualityRequirement",
				renderer: qualityRequirementRenderer
			},
			{
				data: "vslNm",
				type: "autocomplete",
				source: vslNmList,
				strict: true,
				renderer: vslNmRenderer
			},
			{
				data: "eta",
				renderer: etaRenderer
			},
			{
				data: "dischargePort",
				type: "autocomplete",
				strict: true,
				renderer: dischargePortRenderer
			},
			// {
			// 	data: "temperature",
			// 	type: "numeric",
			// 	strict: true,
			// 	readonly: true,
			// 	renderer: temperatureRenderer
			// },
			// {
			// 	data: "humidity",
			// 	type: "numeric",
			// 	strict: true,
			// 	readonly: true,
			// 	renderer: humidityRenderer
			// },
			// {
			// 	data: "ventilation",
			// 	type: "numeric",
			// 	strict: true,
			// 	readonly: true,
			// 	renderer: ventilationRenderer
			// },
			{
				data: "btnInformationContainer",
				renderer: btnDetailRenderer,
			},
			{
				data: "contSupplyRemark",
				renderer: contSupplyRemarkRenderer
			},
			{
				data: "payType",
				renderer: payTypeRenderer
			},
			{
				data: "payer",
				renderer: payerRenderer
			},
			{
				data: "payerName",
				renderer: payerNameRenderer
			},
			{
				data: "remark",
				renderer: remarkRenderer
			},
		],
		// beforeOnCellMouseDown: function restrictSelectionToWholeRowColumn(event, coords) {
		//     if(coords.col == 0) event.stopImmediatePropagation();
		// },
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
					if (selected[3] == 16) {
						e.stopImmediatePropagation();
					}
					break
				// Arrow Down
				case 40:
					selected = hot.getSelected()[0];
					if (selected[2] == rowAmount - 1) {
						e.stopImmediatePropagation();
					}
					break
				default:
					break;
			}
		},
		afterChange: onChange
	};
}
configHandson();

function onChange(changes, source) {
	if (!changes) {
		return;
	}
	onChangeFlg = true;
	changes.forEach(function (change) {
		// Trigger when vessel-voyage no change, get list discharge port by vessel, voy no
		if (change[1] == "vslNm" && change[3] != null && change[3] != '') {
			let vesselAndVoy = hot.getDataAtCell(change[0], 9);
			//hot.setDataAtCell(change[0], 10, ''); // dischargePort reset
			if (vesselAndVoy) {
				if (currentVesselVoyage != vesselAndVoy) {
					currentVesselVoyage = vesselAndVoy;
					let shipmentDetail = new Object();
					for (let i = 0; i < berthplanList.length; i++) {
						if (vesselAndVoy == berthplanList[i].vslAndVoy) {
							currentEta = berthplanList[i].eta;
							shipmentDetail.vslNm = berthplanList[i].vslNm;
							shipmentDetail.voyNo = berthplanList[i].voyNo;
							shipmentDetail.year = berthplanList[i].year;
							$.modal.loading("Đang xử lý ...");
							$.ajax({
								url: ctx + "/logistic/pods",
								method: "POST",
								contentType: "application/json",
								data: JSON.stringify(shipmentDetail),
								success: function (data) {
									$.modal.closeLoading();
									if (data.code == 0) {
										hot.updateSettings({
											cells: function (row, col, prop) {
												if (col == 11) {
													let cellProperties = {};
													dischargePortList = data.dischargePorts;
													cellProperties.source = dischargePortList;
													return cellProperties;
												}
											}
										});
									}
								}
							});
						}
					}
				}
				hot.setDataAtCell(change[0], 10, currentEta);
			}
		} else if (change[1] == "containerNo") {
			if (!change[3]) {
				sztpListDisable[change[0]] = 0;
				cleanCell(change[0], 3, sizeList);
			} else {
				$.ajax({
					url: prefix + "/containerNo/" + change[3] + "/sztp",
					method: "GET",
					success: function (data) {
						if (data.code == 0) {
							if (data.sztp && data.sztp[0] != '{') {
								sizeList.forEach(element => {
									if (data.sztp == element.substring(0, 4)) {
										data.sztp = element;
										return false;
									}
								});
								sztpListDisable[change[0]] = 1;
								hot.setDataAtCell(change[0], 3, data.sztp);
							} else {
								sztpListDisable[change[0]] = 0;
								cleanCell(change[0], 3, sizeList);
							}
						} else {
							sztpListDisable[change[0]] = 0;
							cleanCell(change[0], 3, sizeList);
						}
					},
					error: function (err) {
						sztpListDisable[change[0]] = 0;
						cleanCell(change[0], 3, sizeList);
					}
				});
			}
		} else if (change[1] == "sztp") {
			if (change[3] && hot.getDataAtCell(change[0], 2)) {
				$("#detailBtn" + change[0]).prop("disabled", false);
			} else {
				$("#detailBtn" + change[0]).prop("disabled", true);
			}

			// if (
			// 	change[3] &&
			// 	change[3].length > 3 &&
			// 	change[3].substring(0, 4).includes("R")
			// ) {
			// 	temperatureDisable[change[0]] = 0;
			// 	hot.updateSettings({
			// 		cells: function (row, col, prop) {
			// 			if (row == change[0] && (col == 12 || col == 13 || col == 14)) {
			// 				let cellProperties = {};
			// 				cellProperties.readOnly = false;
			// 				return cellProperties;
			// 			}
			// 		},
			// 	});
			// } else {
			// 	temperatureDisable[change[0]] = 1;
			// 	hot.updateSettings({
			// 		cells: function (row, col, prop) {
			// 			if (row == change[0] && (col == 12 || col == 13 || col == 14)) {
			// 				let cellProperties = {};
			// 				cellProperties.readOnly = true;
			// 				$("#temperature" + row).css(
			// 					"background-color",
			// 					"rgb(232, 232, 232)"
			// 				);
			// 				$("#humidity" + row).css(
			// 					"background-color",
			// 					"rgb(232, 232, 232)"
			// 				);
			// 				$("#ventilation" + row).css(
			// 					"background-color",
			// 					"rgb(232, 232, 232)"
			// 				);
			// 				return cellProperties;
			// 			}
			// 		},
			// 	});
			// }
		} else if (change[1] == "containerNo") {
			if (change[3] && hot.getDataAtCell(change[0], 3)) {
				$("#detailBtn" + change[0]).prop("disabled", false);
			} else {
				$("#detailBtn" + change[0]).prop("disabled", true);
			}
		}
	});
}

function cleanCell(roww, coll, src) {
	hot.setDataAtCell(roww, coll, '');
	hot.updateSettings({
		cells: function (row, col, prop) {
			if (row == roww && col == coll) {
				let cellProperties = {};
				cellProperties.source = src;
				return cellProperties;
			}
		}
	});
}

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

// TRIGGER CHECK ALL SHIPMENT DETAIL
function checkAll() {
	if (!allChecked) {
		allChecked = true
		checkList = Array(rowAmount).fill(0);
		for (let i = 0; i < checkList.length; i++) {
			if (hot.getDataAtCell(i, 1) == null) {
				break;
			}
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
	updateLayout();
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
	let disposable = true, status = 1, diff = false, check = false, verify = false, contNull = false;
	allChecked = true;
	for (let i = 0; i < checkList.length; i++) {
		let cellStatus = hot.getDataAtCell(i, 1);
		if (cellStatus != null) {
			if (checkList[i] == 1) {
				if (cellStatus == 2 && 'Y' == sourceData[i].userVerifyStatus) {
					verify = true;
				}
				check = true;
				if (cellStatus > 2) {
					disposable = false;
				}
				if (status != 1 && status != cellStatus) {
					diff = true;
				} else {
					status = cellStatus;
				}
			} else {
				allChecked = false;
			}
		}
	}
	$('.checker').prop('checked', allChecked);
	if (disposable) {
		$("#deleteBtn").prop("disabled", false);
	} else {
		$("#deleteBtn").prop("disabled", true);
	}
	if (diff) {
		status = 1;
	} else {
		status++;
	}
	if (!check) {
		$("#deleteBtn").prop("disabled", true);
		status = 1;
	}
	switch (status) {
		case 1:
			setLayoutRegisterStatus();
			break;
		case 2:
			setLayoutSupplyContReq();
			break;
		case 3:
			setLayoutVerifyUserStatus();
			if (verify) {
				$("#verifyBtn").prop("disabled", true);
				$("#deleteBtn").prop("disabled", true);
			}
			break;
		case 4:
			setLayoutPaymentStatus();
			break;
		case 5:
			setLayoutFinishStatus();
			break;
		default:
			break;
	}
}

// LOAD SHIPMENT DETAIL LIST
function loadShipmentDetail(id) {
	$.modal.loading("Đang xử lý ...");
	$.ajax({
		url: prefix + "/shipment/" + id + "/shipment-detail",
		method: "GET",
		success: function (data) {
			$.modal.closeLoading();
			if (data.code == 0) {
				sourceData = data.shipmentDetails;
				if (rowAmount < sourceData.length) {
					sourceData = sourceData.slice(0, rowAmount);
				}
				sourceData.forEach(function (element, index) {
					if (
						element.sztp &&
						element.sztp.length > 3 &&
						element.sztp.substring(0, 4).includes("R")
					) {
						temperatureDisable[index] = 0;
					}
				});

				hot.destroy();
				configHandson();
				hot = new Handsontable(dogrid, config);
				hot.loadData(sourceData);
				hot.render();
				onChangeFlg = false;
				if (sourceData.length > 0) {
					currentVesselVoyage = sourceData[0].vslNm;
					let shipmentDetail = new Object();
					for (let i = 0; i < berthplanList.length; i++) {
						if (currentVesselVoyage == berthplanList[i].vslAndVoy) {
							currentEta = berthplanList[i].eta;
							shipmentDetail.vslNm = berthplanList[i].vslNm;
							shipmentDetail.voyNo = berthplanList[i].voyNo;
							shipmentDetail.year = berthplanList[i].year;
							$.modal.loading("Đang xử lý ...");
							$.ajax({
								url: ctx + "/logistic/pods",
								method: "POST",
								contentType: "application/json",
								data: JSON.stringify(shipmentDetail),
								success: function (data) {
									$.modal.closeLoading();
									if (data.code == 0) {
										hot.updateSettings({
											cells: function (row, col, prop) {
												if (col == 11) {
													let cellProperties = {};
													dischargePortList = data.dischargePorts;
													cellProperties.source = dischargePortList;
													return cellProperties;
												}
											}
										});
									}
								}
							});
						}
					}
				}
			}
		},
		error: function (data) {
			$.modal.closeLoading();
		}
	});
}

function reloadShipmentDetail() {
	checkList = Array(rowAmount).fill(0);
	temperatureDisable = Array(rowAmount).fill(1);
	allChecked = false;
	$('.checker').prop('checked', false);
	for (let i = 0; i < checkList.length; i++) {
		$('#check' + i).prop('checked', false);
	}
	$("#deleteBtn").prop("disabled", true);
	$("#verifyBtn").prop("disabled", true);
	$("#payBtn").prop("disabled", true);
	$("#exportBillBtn").prop("disabled", true);
	$("#exportReceiptBtn").prop("disabled", true);
	setLayoutRegisterStatus();
	loadTable();
	loadShipmentDetail(shipmentSelected.id);
}

// GET CHECKED SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataSelectedFromTable(isValidate) {
	let myTableData = hot.getSourceData();
	let errorFlg = false;
	let cleanedGridData = [];
	for (let i = 0; i < checkList.length; i++) {
		if (Object.keys(myTableData[i]).length > 0) {
			if (checkList[i] == 1) {
				cleanedGridData.push(myTableData[i]);
			}
		}
	}
	shipmentDetailIds = "";
	shipmentDetails = [];
	let regiterNos = [];
	$.each(cleanedGridData, function (index, object) {
		var shipmentDetail = new Object();
		if (object["containerNo"] && !/^[A-Z]{4}[0-9]{7}$/g.test(object["containerNo"]) && isValidate && shipmentSelected.specificContFlg == 1) {
			$.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
			errorFlg = true;
		}
		shipmentDetail.containerNo = object["containerNo"];
		shipmentDetail.processStatus = object["processStatus"];
		shipmentDetail.paymentStatus = object["paymentStatus"];
		shipmentDetail.userVerifyStatus = object["userVerifyStatus"];
		shipmentDetail.status = object["status"];
		shipmentDetail.eta = object["eta"];
		shipmentDetail.shipmentId = shipmentSelected.id;
		shipmentDetail.contSupplyStatus = object["contSupplyStatus"];
		shipmentDetail.id = object["id"];
		shipmentDetails.push(shipmentDetail);
		if (object["registerNo"] != null && !regiterNos.includes(object["registerNo"])) {
			regiterNos.push(object["registerNo"]);
		}
		shipmentDetailIds += object["id"] + ",";
	});

	let temProcessOrderIds = [];
	processOrderIds = '';
	$.each(cleanedGridData, function (index, object) {
		for (let i = 0; i < regiterNos.length; i++) {
			if (object["processOrderId"] != null && !temProcessOrderIds.includes(object["processOrderId"]) && regiterNos[i] == object["registerNo"]) {
				temProcessOrderIds.push(object["processOrderId"]);
				processOrderIds += object["processOrderId"] + ',';
			}
		}
	});

	if (processOrderIds != '') {
		processOrderIds.substring(0, processOrderIds.length - 1);
	}

	// Get result in "selectedList" variable
	if (shipmentDetails.length == 0 && isValidate) {
		$.modal.alert("Bạn chưa chọn container.");
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

// GET SHIPMENT DETAIL LIST, VALIDATE FIELD WHEN isValidate = true
function getDataFromTable(isValidate) {
	let myTableData = hot.getSourceData();
	let errorFlg = false;
	let cleanedGridData = [];
	for (let i = 0; i < checkList.length; i++) {
		if (Object.keys(myTableData[i]).length > 0) {
			if (myTableData[i].containerNo || myTableData[i].expiredDem || myTableData[i].vslNm ||
				myTableData[i].planningDate || myTableData[i].cargoType || myTableData[i].qualityRequirement ||
				myTableData[i].sztp || myTableData[i].dischargePort || myTableData[i].remark) {
				cleanedGridData.push(myTableData[i]);
			}
		}
	}
	shipmentDetails = [];
	contList = [];
	let opecode, vessel, voyage, pod;
	if (cleanedGridData.length > 0) {
		// opecode = cleanedGridData[0].opeCode;
		vessel = cleanedGridData[0].vslNm;
		//        voyage = cleanedGridData[0].voyNo;
		pod = cleanedGridData[0].dischargePort;
	}
	$.each(cleanedGridData, function (index, object) {
		var shipmentDetail = new Object();
		if (isValidate) {
			if (!object["containerNo"] && shipmentSelected.specificContFlg == 1) {
				$.modal.alertError("Hàng " + (index + 1) + ": Chưa nhập số container!");
				errorFlg = true;
				return false;
			} else if (!/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"]) && shipmentSelected.specificContFlg == 1) {
				$.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
				errorFlg = true;
				return false;
			} else if (!object["expiredDem"]) {
				$.modal.alertError("Hàng " + (index + 1) + ": Vui lòng nhập hạn lệnh!");
				errorFlg = true;
				return false;
			} else if (!object["consignee"]) {
				$.modal.alertError("Hàng " + (index + 1) + ": Vui lòng chọn chủ hàng từ danh sách!");
				errorFlg = true;
				return false;
				// } else if (!object["opeCode"]) {
				//     $.modal.alertError("Hàng " + (index + 1) + ": Quý khách chưa chọn Hãng tàu!");
				//     errorFlg = true;
				//     return false;
			} else if (!object["vslNm"]) {
				$.modal.alertError("Hàng " + (index + 1) + ": Vui lòng chọn Tàu và chuyến từ danh sách!");
				errorFlg = true;
				return false;
			} else if (!object["sztp"]) {
				$.modal.alertError("Hàng " + (index + 1) + ": Vui lòng chọn kích thước!");
				errorFlg = true;
				return false;
			} else if (!object["dischargePort"]) {
				$.modal.alertError("Hàng " + (index + 1) + ": Vui lòng chọn Cảng dở hàng từ danh sách!");
				errorFlg = true;
				return false;
			} else if (!object["planningDate"]) {
				$.modal.alertError("Hàng " + (index + 1) + ": Vui lòng nhập ngày dự kiến bốc!");
				errorFlg = true;
				return false;
			} else if (!object["cargoType"]) {
				$.modal.alertError("Hàng " + (index + 1) + ": Vui lòng chọn loại hàng!");
				errorFlg = true;
				return false;
				// } else if (opecode != object["opeCode"]) {
				//     $.modal.alertError("Hãng tàu không được khác nhau!");
				//     errorFlg = true;
				//     return false;
				//            } else if (vessel != object["vslNm"]) {
				//                $.modal.alertError("Tàu và Chuyến không được khác nhau!");
				//                errorFlg = true;
				//                return false;
			} else if (pod.split(": ")[0] != object["dischargePort"].split(": ")[0]) {
				$.modal.alertError("Cảng dỡ hàng không được khác nhau!");
				errorFlg = true;
				return false;
			}
		}
		// opecode = object["opeCode"];
		vessel = object["vslNm"];
		pod = object["dischargePort"];
		let expiredDem = new Date(object["expiredDem"].substring(6, 10) + "/" + object["expiredDem"].substring(3, 5) + "/" + object["expiredDem"].substring(0, 2));
		let planningDate = new Date(object["planningDate"].substring(6, 10) + "/" + object["planningDate"].substring(3, 5) + "/" + object["planningDate"].substring(0, 2));
		shipmentDetail.containerNo = object["containerNo"];
		contList.push(object["containerNo"]);
		let sizeType = object["sztp"].split(": ");
		shipmentDetail.sztp = sizeType[0];
		shipmentDetail.sztpDefine = sizeType[1];
		shipmentDetail.opeCode = shipmentSelected.opeCode;
		expiredDem.setHours(23, 59, 59);
		planningDate.setHours(23, 59, 59);
		shipmentDetail.expiredDem = expiredDem.getTime();
		shipmentDetail.planningDate = planningDate.getTime();
		shipmentDetail.cargoType = object["cargoType"];
		shipmentDetail.qualityRequirement = object["qualityRequirement"];
		shipmentDetail.consignee = object["consignee"];
		shipmentDetail.eta = object["eta"];
		shipmentDetail.dischargePort = object["dischargePort"].split(": ")[0];
		shipmentDetail.remark = object["remark"];

		if (detailInformationForContainerSpecial.data[index]) {
			shipmentDetail.humidity = detailInformationForContainerSpecial.data[index].humidity;
			shipmentDetail.temperature = detailInformationForContainerSpecial.data[index].temperature;
			shipmentDetail.ventilation = detailInformationForContainerSpecial.data[index].ventilation;
		}

		if (berthplanList) {
			for (let i = 0; i < berthplanList.length; i++) {
				if (object["vslNm"] == berthplanList[i].vslAndVoy) {
					shipmentDetail.vslNm = berthplanList[i].vslNm;
					shipmentDetail.voyNo = berthplanList[i].voyNo;
					shipmentDetail.year = berthplanList[i].year;
					shipmentDetail.vslName = berthplanList[i].vslAndVoy.split(" - ")[1];
					shipmentDetail.voyCarrier = berthplanList[i].voyCarrier;
				}
			}
		}
		shipmentDetail.bookingNo = shipmentSelected.bookingNo;
		shipmentDetail.shipmentId = shipmentSelected.id;
		shipmentDetail.id = object["id"];
		shipmentDetails.push(shipmentDetail);
		let now = new Date();
		now.setHours(0, 0, 0);
		if (expiredDem.getTime() < now.getTime() && isValidate) {
			errorFlg = true;
			$.modal.alertError("Hàng " + (index + 1) + ": Hạn lệnh không được trong quá khứ!")
		}
		if (planningDate.getTime() < planningDate.getTime() && isValidate) {
			errorFlg = true;
			$.modal.alertError("Hàng " + (index + 1) + ": Thời gian dự kiến bốc không được trong quá khứ!")
		}
	});

	if (isValidate && !errorFlg && shipmentSelected.specificContFlg == 1) {
		contList.sort();
		let contTemp = "";
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
// SAVE/EDIT/DELETE SHIPMENT DETAIL
function saveShipmentDetail() {
	if (shipmentSelected == null) {
		$.modal.alertError("Bạn cần chọn lô trước");
		return;
	} else {
		if (getDataFromTable(true)) {
			if (shipmentDetails.length > 0) {
				$.modal.loading("Đang xử lý...");
				$.ajax({
					url: prefix + "/shipment-detail",
					method: "post",
					contentType: "application/json",
					accept: 'text/plain',
					data: JSON.stringify(shipmentDetails),
					dataType: 'text',
					success: function (data) {
						var result = JSON.parse(data);
						if (result.code == 0) {
							$.modal.alertSuccess(result.msg);
							reloadShipmentDetail();
						} else {
							$.modal.alertError(result.msg);
						}
						$.modal.closeLoading();
					},
					error: function (result) {
						$.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau.");
						$.modal.closeLoading();
					},
				});

			} else {
				$.modal.alertError("Quý khách chưa nhập thông tin chi tiết lô.");
			}
		}
	}
}

// DELETE SHIPMENT DETAIL
function deleteShipmentDetail() {
	if (getDataSelectedFromTable(true) && shipmentDetails.length > 0) {
		$.modal.confirmShipment("Xác nhận xóa khai báo container ?", function () {
			$.modal.loading("Đang xử lý...");
			$.ajax({
				url: prefix + "/shipment/" + shipmentSelected.id + "/shipment-detail/" + shipmentDetailIds,
				method: "delete",
				success: function (result) {
					if (result.code == 0) {
						$.modal.alertSuccess(result.msg);
						reloadShipmentDetail();
					} else {
						$.modal.alertError(result.msg);
					}
					$.modal.closeLoading();
				},
				error: function (result) {
					$.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau.");
					$.modal.closeLoading();
				},
			});
		});
	}
}

// Handling logic
function requestContSupply() {
	getDataSelectedFromTable(true);
	if (shipmentDetails.length > 0) {

		// Check if list cont exists cont has been supplied
		let containers = '';
		shipmentDetails.forEach(function (element) {
			if (element.contSupplyStatus == 'Y') {
				containers += element.containerNo + ',';
			}
		});
		if (containers.length > 0) {
			containers = containers.substring(0, containers.length - 1);
			layer.confirm("Quý khách đang yêu cầu cấp rỗng lại cho số container: " + containers + ".", {
				icon: 3,
				title: "Xác Nhận",
				btn: ['Đồng Ý', 'Hủy Bỏ']
			}, function () {
				layer.close(layer.index);
				openFormRemarkBeforeReqCont();
			}, function () {
				// close form
			});
		} else {
			openFormRemarkBeforeReqCont();
		}
	}
}

function openFormRemarkBeforeReqCont() {
	// Form confirm req supply cont
	layer.open({
		type: 2,
		area: [500 + 'px', 230 + 'px'],
		fix: true,
		maxmin: true,
		shade: 0.3,
		title: 'Xác Nhận',
		content: prefix + "/req/supply/confirmation",
		btn: ["Xác Nhận", "Hủy"],
		shadeClose: false,
		yes: function (index, layero) {
			requestCont(index, layero);
		},
		cancel: function (index) {
			return true;
		}
	});
}

function requestCont(index, layero) {
	let childLayer = layero.find("iframe")[0].contentWindow.document;
	$.modal.loading("Đang xử lý ...");
	$.ajax({
		url: prefix + "/cont-req/shipment-detail",
		method: "POST",
		data: {
			shipmentDetailIds: shipmentDetailIds,
			contReqRemark: $(childLayer).find("#message").val()
		},
		success: function (result) {
			if (result.code == 0) {
				$.modal.alertSuccess(result.msg);
				reloadShipmentDetail();
			} else {
				$.modal.alertError(result.msg);
			}
			$.modal.closeLoading();
			layer.close(index);
		},
		error: function (result) {
			$.modal.alertError("Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau.");
			$.modal.closeLoading();
		},
	});
}

function verify() {
	getDataSelectedFromTable(true);
	if (shipmentDetails.length > 0) {
		$.ajax({
			url: prefix + "/shipment-detail/validation",
			method: "POST",
			data: {
				shipmentDetailIds: shipmentDetailIds
			},
			success: function (res) {
				if (res.code != 0) {
					$.modal.alertWarning(res.msg);
				} else {
					$.modal.openCustomForm("Xác nhận làm lệnh", prefix + "/otp/cont-list/confirmation/" + shipmentDetailIds, 700, 500);
				}
			},
			error: function (err) {
				$.modal.alertWarning("Lỗi hệ thống, quý khách vui lòng thử lại sau.");
			}
		});
	}
}

function verifyOtp(shipmentDtIds, taxCode, creditFlag) {
	getDataSelectedFromTable(true);
	if (shipmentDetails.length > 0) {
		$.modal.openCustomForm("Xác thực OTP", prefix + "/otp/verification/" + shipmentDtIds + "/" + creditFlag + "/" + taxCode + "/" + shipmentSelected.id, 600, 350);
	}
}

function pay() {
	getDataSelectedFromTable(true);
	if (shipmentDetails.length > 0) {
		$.modal.openCustomForm("Thanh toán", prefix + "/payment/" + processOrderIds, 800, 400);
	}
}

function exportBill() {

}

// Handling UI STATUS
function setLayoutRegisterStatus() {
	$("#registerStatus").removeClass("label-primary disable").addClass("active");
	$("#contSupplyStatus").removeClass("label-primary active").addClass("disable");
	$("#verifyStatus").removeClass("label-primary active").addClass("disable");
	$("#paymentStatus").removeClass("label-primary active").addClass("disable");
	$("#finishStatus").removeClass("label-primary active").addClass("disable");
	$("#contSupplyBtn").prop("disabled", true);
	$("#verifyBtn").prop("disabled", true);
	$("#payBtn").prop("disabled", true);
	$("#exportBillBtn").prop("disabled", true);
	$("#exportReceiptBtn").prop("disabled", true);
}

function setLayoutSupplyContReq() {
	$("#registerStatus").removeClass("active disable").addClass("label-primary");
	$("#contSupplyStatus").removeClass("label-primary disable").addClass("active");
	$("#verifyStatus").removeClass("label-primary active").addClass("disable");
	$("#paymentStatus").removeClass("label-primary active").addClass("disable");
	$("#finishStatus").removeClass("label-primary active").addClass("disable");
	$("#contSupplyBtn").prop("disabled", false);
	$("#verifyBtn").prop("disabled", true);
	$("#payBtn").prop("disabled", true);
	$("#exportBillBtn").prop("disabled", true);
	$("#exportReceiptBtn").prop("disabled", true);
}

function setLayoutVerifyUserStatus() {
	$("#registerStatus").removeClass("active disable").addClass("label-primary");
	$("#contSupplyStatus").removeClass("active disable").addClass("label-primary");
	$("#verifyStatus").removeClass("label-primary disable").addClass("active");
	$("#paymentStatus").removeClass("active label-primary").addClass("disable");
	$("#finishStatus").removeClass("active label-primary").addClass("disable");
	$("#contSupplyBtn").prop("disabled", false);
	$("#verifyBtn").prop("disabled", false);
	$("#payBtn").prop("disabled", true);
	$("#exportBillBtn").prop("disabled", true);
	$("#exportReceiptBtn").prop("disabled", true);
}

function setLayoutPaymentStatus() {
	$("#registerStatus").removeClass("active disable").addClass("label-primary");
	$("#contSupplyStatus").removeClass("active disable").addClass("label-primary");
	$("#verifyStatus").removeClass("active disable").addClass("label-primary");
	$("#paymentStatus").removeClass("label-primary disable").addClass("active");
	$("#finishStatus").removeClass("active label-primary").addClass("disable");
	$("#contSupplyBtn").prop("disabled", true);
	$("#deleteBtn").prop("disabled", true);
	$("#verifyBtn").prop("disabled", true);
	$("#payBtn").prop("disabled", false);
	$("#exportBillBtn").prop("disabled", true);
	$("#exportReceiptBtn").prop("disabled", true);
}

function setLayoutFinishStatus() {
	$("#registerStatus").removeClass("active disable").addClass("label-primary");
	$("#contSupplyStatus").removeClass("active disable").addClass("label-primary");
	$("#verifyStatus").removeClass("active disable").addClass("label-primary");
	$("#paymentStatus").removeClass("active disable").addClass("label-primary");
	$("#finishStatus").removeClass("label-primary disable").addClass("active");
	$("#contSupplyBtn").prop("disabled", true);
	$("#deleteBtn").prop("disabled", true);
	$("#verifyBtn").prop("disabled", true);
	$("#payBtn").prop("disabled", true);
	$("#exportBillBtn").prop("disabled", false);
	$("#exportReceiptBtn").prop("disabled", false);
}

function finishForm(result) {
	if (result.code == 0) {
		$.modal.alertSuccess(result.msg);
	} else {
		$.modal.alertError(result.msg);
	}
	reloadShipmentDetail();
}

function finishVerifyForm(result) {
	if (result.code == 0 || result.code == 301) {
		//$.modal.loading(result.msg);
		orders = result.processIds;
		orderNumber = result.orderNumber;
		// CONNECT WEB SOCKET
		connectToWebsocketServer();

		showProgress("Đang xử lý ...");
		timeout = setTimeout(() => {
			setTimeout(() => {
				hideProgress();
				reloadShipmentDetail();
				$.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");
			}, 1000);
		}, 200000);

	} else {
		reloadShipmentDetail();
		$.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");
	}
}

function napasPaymentForm() {
	// $.modal.openFullWithoutButton("Cổng Thanh Toán", ctx + "logistic/payment/napas/" + processOrderIds);
	window.open(ctx + "logistic/payment/napas/" + processOrderIds, "_blank");
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

		clearTimeout(timeout);

		setProgressPercent(currentPercent = 100);
		setTimeout(() => {
			hideProgress();

			reloadShipmentDetail();

			$.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");

			// Close loading
			//$.modal.closeLoading();

			// Close websocket connection
			$.websocket.disconnect(onDisconnected);
		}, 1000);
	} else {
		orderNumber--;
		if (orderNumber == 0) {

			clearTimeout(timeout);

			setProgressPercent(currentPercent = 100);
			setTimeout(() => {
				hideProgress();

				reloadShipmentDetail();

				$.modal.alertSuccess(message.msg);

				// Close loading
				//$.modal.closeLoading();

				// Close websocket connection
				$.websocket.disconnect(onDisconnected);
			}, 1000);
		}
	}
}

function onError(error) {
	console.error('Could not connect to WebSocket server. Please refresh this page to try again!');
	setTimeout(() => {
		hideProgress();
		$.modal.alertWarning("Yêu cầu của quý khách đang được tiếp nhận. bộ phận thủ tục đang xử lý, xin quý khách vui lòng đợi.");
	}, 1000);
}

function showProgress(title) {
	$('.progress-wrapper').show();
	$('.dim-bg').show();
	$('#titleProgress').text(title);
	$('.percent-text').text("0%");
	currentPercent = 0;
	interval = setInterval(function () {
		if (currentPercent <= 99) {
			setProgressPercent(++currentPercent);
		}
		if (currentPercent >= 99) {
			clearInterval(interval);
		}
	}, 1000);
}

function setProgressPercent(percent) {
	$('#progressBar').prop('aria-valuenow', percent)
	$('#progressBar').css('width', percent + "%")
	$('.percent-text').text(percent + "%");
}

function hideProgress() {
	$('.progress-wrapper').hide();
	$('.dim-bg').hide();
	currentPercent = 0;
	$('.percent-text').text("0%");
	setProgressPercent(0);
}
function exportReceipt() {
	if (!shipmentSelected) {
		$.modal.alertError("Bạn chưa chọn Lô!");
		return
	}
	$.modal.openTab("In Biên Nhận", ctx + "logistic/print/receipt/shipment/" + shipmentSelected.id);
}

function removeShipment() {
	if (!shipmentSelected) {
		$.modal.alertError("Bạn chưa chọn Lô!");
		return
	} else {
		//1- chua khai bao cont, 2- khai bao nhung chua lam cac buoc tiep theo
		if (shipmentSelected.status == '1' || shipmentSelected.status == '2') {
			$.modal.confirmShipment("Xác nhận thực hiện xóa Lô " + shipmentSelected.id + "  ?", function () {
				$.modal.loading("Đang xử lý...");
				$.ajax({
					url: ctx + 'logistic/shipment/remove',
					type: 'POST',
					data: {
						id: shipmentSelected.id,
					}
				}).done(function (rs) {
					$.modal.closeLoading();
					if (rs.code == 0) {
						$.modal.msgSuccess(rs.msg);
						loadTable();
					}
					else {
						$.modal.msgError(rs.msg)
					}
				});
			});
		} else {
			$.modal.msgError("Không thể xóa Lô " + shipmentSelected.id);
		}
	}
}

function dateToString(date) {
	return ("0" + date.getDate()).slice(-2) + "/" + ("0" + (date.getMonth() + 1)).slice(-2) + "/" + date.getFullYear()
		+ " " + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2) + ":" + ("0" + date.getSeconds()).slice(-2);
}

function search() {
	shipmentSearch.bookingNo = $("#bookingNo").textbox('getText');
	shipmentSearch.params.containerNo = $("#containerNo").textbox('getValue');
	shipmentSearch.params.consignee = $("#consignee").textbox('getValue');
	shipmentSearch.params.fromDate = $('#fromDate').datebox('getValue');
	shipmentSearch.params.toDate = $('#toDate').datebox('getValue');
	loadTable();
}

function clearInput() {
	$("#bookingNo").textbox('setText', '');
	$("#containerNo").textbox('setText', '');
	$("#consignee").textbox('setText', '');
	$('#fromDate').datebox('setValue', '');
	$('#toDate').datebox('setValue', '');
	shipmentSearch = new Object();
	shipmentSearch.params = new Object();
	shipmentSearch.serviceType = 3;
	fromDate = null;
	toDate = null;
	loadTable();
}

function loadListComment(shipmentCommentId) {
	let req = {
		serviceType: 3,
		shipmentId: shipmentSelected.id
	};
	$.ajax({
		url: ctx + "logistic/comment/list",
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify(req),
		success: function (data) {
			if (data.code == 0) {
				let html = '';
				// set title for panel comment
				let commentTitle = '<span>Hỗ Trợ<span>';
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

						let seenBackground = '';
						if ((shipmentCommentId && shipmentCommentId == element.id) || !element.seenFlg) {
							seenBackground = 'style="background-color: #ececec;"';
							commentNumber++;
						}

						html += '<div ' + seenBackground + '>';
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
	let topic = $('#topic').textbox('getText').trim();
	let content = $('.summernote').summernote('code');// get editor content
	let errorFlg = false;
	let contentTemp = content.replace(regexRemoveHtml, "").replaceAll('&nbsp;', '').trim();
	if (!topic) {
		errorFlg = true;
		$.modal.alertWarning('Vui lòng nhập chủ đề.');
	} else if (!contentTemp) {
		errorFlg = true;
		$.modal.alertWarning('Vui lòng nhập nội dung.');
	}
	if (!errorFlg) {
		let req = {
			topic: topic,
			content: content.replaceAll(domain, "#{domain}"),
			shipmentId: shipmentSelected.id
		};
		$.ajax({
			url: prefix + "/shipment/comment",
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

function requestCancelOrder() {
	getDataSelectedFromTable(true);
	if (shipmentDetails.length > 0) {
		// Check if list cont exists cont has been process
		let containers = '';
		shipmentDetails.forEach(function (element) {
			if (element.processStatus != 'Y') {
				containers += element.containerNo + ',';
			}
		});
		if (containers.length > 0) {
			containers = containers.substring(0, containers.length - 1);
			$.modal.alertWarning("Các contaienr quý khách chọn chưa được thực hiện làm lệnh, quý khách không thể yêu cầu hủy lệnh cho những container này.");
		} else {
			openFormRemarkBeforeReqCancelOrder();
		}
	}
}

function openFormRemarkBeforeReqCancelOrder() {
	// Form confirm req supply cont
	layer.open({
		type: 2,
		area: [500 + 'px', 230 + 'px'],
		fix: true,
		maxmin: true,
		shade: 0.3,
		title: 'Xác Nhận',
		content: prefix + "/req/cancel/confirmation",
		btn: ["Xác Nhận", "Hủy"],
		shadeClose: false,
		yes: function (index, layero) {
			let childLayer = layero.find("iframe")[0].contentWindow.document;
			$.modal.loading("Đang xử lý ...");
			$.ajax({
				url: prefix + "/order-cancel/shipment-detail",
				method: "POST",
				data: {
					shipmentDetailIds: shipmentDetailIds,
					contReqRemark: $(childLayer).find("#message").val()
				},
				success: function (result) {
					if (result.code == 0) {
						$.modal.alertSuccess(result.msg);
						reloadShipmentDetail();
					} else {
						$.modal.alertError(result.msg);
					}
					$.modal.closeLoading();
					layer.close(index);
				},
				error: function (result) {
					$.modal.alertError("Có lỗi trong quá trình xử lý dữ liệu, vui lòng thử lại sau.");
					$.modal.closeLoading();
				},
			});
		},
		cancel: function (index) {
			return true;
		}
	});
}

function requestCancelSupplyContainer() {

}

function requestExchangeContainer() {

}

function openDetail(id, containerNo, sztp, row, cargoType) {
	if (!id) {
		$.modal.alertWarning(
			"Container chưa được lưu. Vui lòng lưu khai báo trước."
		);
	}
	else {
		detailInformationForContainerSpecial.indexSelected = row;
		$.modal.openCustomForm(
			"Khai báo chi tiết",
			`${prefix}/shipment-detail/${id}/cont/${containerNo}/sztp/${sztp}/detail`,
			800,
			460
		);
	}
}

function submitDataFromDetailModal(data) {

	const { indexSelected } = detailInformationForContainerSpecial;
	detailInformationForContainerSpecial.data[indexSelected] = data;
	//saveShipmentDetailFollowIndex(indexSelected);
	saveShipmentDetailFollowIndexNew(indexSelected);

}

function saveShipmentDetailFollowIndexNew(index) {
	if (getDataFromTable(true)) {
		if (
			shipmentDetails.length > 0 &&
			shipmentDetails.length <= shipmentSelected.containerAmount
		) {
			shipmentDetails[0].processStatus = conts;

			$.modal.loading("Đang xử lý...");
			$.ajax({
				url: prefix + "/" + shipmentSelected.id + "/shipment-detail-special",
				method: "post",
				contentType: "application/json",
				accept: "text/plain",
				data: JSON.stringify([shipmentDetails[index]]),
				dataType: "text",
				success: function (data) {
					var result = JSON.parse(data);
					if (result.code == 0) {
						$.modal.msgSuccess(result.msg);
						reloadShipmentDetail();
					} else {
						if (result.conts != null) {
							$.modal.alertError(
								"Các container sau đã được thực hiện lệnh nâng/hạ trong hệ thống của Cảng. Xin vui lòng kiểm tra lại dữ liệu.<br>" +
								result.conts
							);
						} else {
							$.modal.alertError(result.msg);
						}
					}
					$.modal.closeLoading();
				},
				error: function (result) {
					$.modal.alertError(
						"Có lỗi trong quá trình thêm dữ liệu, vui lòng thử lại sau."
					);
					$.modal.closeLoading();
				},
			});
		} else if (shipmentDetails.length > shipmentSelected.containerAmount) {
			$.modal.alertError(
				"Số container nhập vào vượt quá số container khai báo của lô."
			);
		} else {
			$.modal.alertError("Hãy nhập thông tin chi tiết lô.");
		}
	}
}

