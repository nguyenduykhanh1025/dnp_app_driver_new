/*const FREFIX = ctx + 'system/notice';*/
const FREFIX = ctx + "listCar/InOut";
var noticeSearch = new Object();
noticeSearch.params = new Object();
var fromDate, toDate, inOut;
var dataSource = [];

var toolbar = [
];

$(document).ready(function () {

	$('#toDate').datebox({
		onSelect: function (date) {
			date.setHours(0, 0, 0);
			fromDate = date;
			noticeSearch.params.toDate = dateToString(date);
			loadTable();
			return date;
		}
	});

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

	$("#car-category").combobox({
		onSelect: function (option) {
			noticeSearch.truckNo = option.value;
			loadTable();
		}
	});

	$("#inOut").combobox({
		onSelect: function (option) {
			noticeSearch.params.inOut = option.value;
			loadTable();
		}
	});
	
	$('#fromDate').datebox({
		onSelect: function (date) {
			date.setHours(0, 0, 0);
			fromDate = date;
			noticeSearch.params.fromDate = dateToString(date);
			loadTable();
			return date;
		},
		value: dateformatter(new Date())
	});

	
});

function dateToString(date) {
	return ("0" + date.getDate()).slice(-2) + "/" + ("0" + (date.getMonth() + 1)).slice(-2) + "/" + date.getFullYear()
		+ " " + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2) + ":" + ("0" + date.getSeconds()).slice(-2);
}

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

// Formatter
function formatActionBtn(value, row, index) {
	let button = '';
	button += '<button class="btn btn-warning btn-xs " onclick="edit(' + row.noticeId + ')"><i class="fa fa-edit"></i>Sửa</button><span> </span>'
	button += '<button class="btn btn-danger btn-xs " onclick="remove(' + row.noticeId + ')"><i class="fa fa-remove"></i>Xóa</button>'
	return button;
}

function formatDate(value) {
	let date = new Date(value);
	let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	let month = date.getMonth() + 1;
	let monthText = month < 10 ? "0" + month : month;
	// let hours = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
	// let minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
	return day + "/" + monthText + "/" + date.getFullYear(); s
}

function formatCarNo(name, data) {
	let payloadHtml = '';
	const { truckNo, chassisNo } = data;
	if (truckNo) {
		payloadHtml += truckNo;
	} else if (chassisNo) {
		payloadHtml += chassisNo;
	}
	return payloadHtml;
}

function formatCarCategory(index, data) {
	let payloadHtml = '';
	const { truckNo, chassisNo } = data;
	if (truckNo) {
		payloadHtml += 'Xe Đầu Kéo';
	} else if (chassisNo) {
		payloadHtml += 'Xe Rơ Móc';
	}
	return payloadHtml;
}

function formatActive(value) {
	if (value == 1) {
		return '<span class="badge badge-primary">Phát hành</span>';
	}
	if (value == 0) {
		return '<span class="badge badge-success">Nháp</span>';
	}
	return '';
}

// function formatAreaContainerNoFirst(value, data) {
// 	return `${data.positionFirst}`
// }

function loadTable() {
	if(!noticeSearch.params.fromDate){
		let date = new Date();
		date.setHours(0, 0, 0);
		noticeSearch.params.fromDate = dateToString(date);
	}
	$("#dg").datagrid({
		url: FREFIX + '/listCar',
		height: $('.main-body').height() - 35,
		method: 'post',
		multiSelect: true,
		collapsible: true,
		clientPaging: false,
		pagination: true,
		rownumbers: true,
		toolbar: toolbar,
		pageSize: 50,
		nowrap: false,
		striped: true,
		loadMsg: " Đang xử lý...",
		loader: function (param, success, error) {
			let opts = $(this).datagrid("options");
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
					data: noticeSearch
				}),
				success: function (data) {
					success(data);
					dataSource = data;
				},
				error: function () {
					error.apply(this, arguments);
				},
			});
		},
	});
}

function add() {
	$.modal.open('Thêm bản tin', FREFIX + '/add');
}

function edit(id) {
	$.modal.open('Sửa bản tin', FREFIX + '/edit/' + id);
}

function remove(id) {
	layer.confirm("Xác nhận xóa bản tin này.", {
		icon: 3,
		title: "Xác Nhận",
		btn: ['Xác Nhận', 'Hủy Bỏ']
	}, function () {
		layer.close(layer.index);
		$.modal.loading('Đang xử lý...');
		$.ajax({
			url: FREFIX + '/remove',
			method: 'POST',
			data: {
				ids: id
			},
			success: function (res) {
				$.modal.closeLoading();
				if (res.code == 0) {
					$.modal.msgSuccess(res.msg);
					loadTable();
				} else {
					$.modal.msgError(res.msg);
				}
			},
			error: function (err) {
				$.modal.closeLoading();
				$.modal.msgError(err.msg);
			}
		});
	}, function () {
	});
}

function removeList() {
	let ids = [];
	let rows = $('#dg').datagrid('getSelections');
	for (let i = 0; i < rows.length; i++) {
		ids.push(rows[i].noticeId);
	}
	if (ids.length == 0) {
		$.modal.alertWarning('Bạn chưa chọn bản tin.');
		return;
	}
	layer.confirm("Xác nhận xóa các bản tin đã chọn.", {
		icon: 3,
		title: "Xác Nhận",
		btn: ['Xác Nhận', 'Hủy Bỏ']
	}, function () {
		layer.close(layer.index);
		$.modal.loading('Đang xử lý...');
		$.ajax({
			url: FREFIX + '/remove',
			method: 'POST',
			data: {
				ids: ids.join(',')
			},
			success: function (res) {
				$.modal.closeLoading();
				if (res.code == 0) {
					$.modal.msgSuccess(res.msg);
					loadTable();
				} else {
					$.modal.msgError(res.msg);
				}
			},
			error: function (err) {
				$.modal.closeLoading();
				$.modal.msgError(err.msg);
			}
		});
	}, function () {
	});
}

function search() {
	noticeSearch.params.containerNo = $("#containerNo").textbox('getValue');
	noticeSearch.params.truckNo = $("#truckNo").textbox('getValue');
	loadTable();
}

function clearInput() {
	$("#noticeTitle").textbox('setText', '');
	$("#active").combobox('setValue', '');
	$('#fromDate').datebox('setValue', '');
	$('#toDate').datebox('setValue', '');
	$('#inOut').combobox('setValue', '');

	noticeSearch = new Object();
	noticeSearch.params = new Object();
	fromDate = null;
	toDate = null;
	loadTable();
}

function reloadTable(res) {
	if (res.code == 0) {
		$.modal.msgSuccess(res.msg);
	} else {
		$.modal.msgError(res.msg);
	}
	loadTable();
}




