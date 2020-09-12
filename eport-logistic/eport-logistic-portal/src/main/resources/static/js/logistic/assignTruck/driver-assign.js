const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var prefix = ctx + "logistic/assignTruck";
var shipmentType = 1;
var shipmentSelected;
var shipmentDetailSelected;//for assign follow cont
var dataAssignedDriver = [];
var dataDriver = [];
var dataContainerList = [];
var shipmentSearch = new Object;
//----------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------TOOLBAR TABLE------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------------------------
//toobar of shipmentDetail
var toolbar = [
  {
    text: '<a href="#" class="btn btn-sm btn-default" style="padding: 2px 3px; border-radius: 0;"><i class="fa fa-save text-success"></i> Ghi chú điều vận</a>',
    handler: function () {
      let pickedIds = [];
      let rows = $('#dgShipmentDetail').datagrid('getSelections');
      if(rows){
          for(let i=0; i< rows.length;i++){
              let id = rows[i].id;
              pickedIds.push(id);
          }
      }
      if(pickedIds.length >0) {
	      $.modal.open("Ghi chú điều vận", prefix + "/shipment-detail/"+ pickedIds +"/delivery-remark", 500,400);
      }else{
    	  $.modal.alertError("Bạn chưa chọn container nào!");
      }

    },
  },
];
//toolbar of driver follow batch
var addDriverForBatch = [
    {
        text: '<a href="#" class="btn btn-sm btn-default" style="padding: 2px 3px; border-radius: 0;"><i class="fa fa-plus text-success"></i> Thêm tài xế</a>',
        handler: function () {
        	if(!shipmentSelected){
        		$.modal.alertError("Bạn chưa chọn Lô!");
        	}
        	let pickedIds = [];
        	let records =  $('#driver-table-follow-batch').datagrid('getRows');
            if(records){
                for(let i = 0; i < records.length; i++){
                    pickedIds.push(records[i].id);
                }
            }
            if(pickedIds.length == 0){
            	pickedIds.push(-1);// cho TH chua assgin
            }
        	$.modal.open("Thêm tài xế", prefix + "/shipment/" + shipmentSelected.id + "/add-drivers/ids-assigned/" + pickedIds, 800,400);
        },
    },
    {
        text: '<a href="#" class="btn btn-sm btn-default" style="padding: 2px 3px; border-radius: 0;"><i class="fa fa-save text-success"></i> Lưu điều xe</a>',
        handler: function () {
        	if(!shipmentSelected){
        		$.modal.alertError("Bạn chưa chọn Lô!");
        	}
        	save();
        },
    },
]
//toolbar driver follow container
var addDriverForContainer = [
    {
        text: '<a href="#" class="btn btn-sm btn-default" style="padding: 2px 3px; border-radius: 0;"><i class="fa fa-plus text-success"></i> Thêm tài xế</a>',
        handler: function () {
        	if(!shipmentDetailSelected){
        		$.modal.alertError("Bạn chưa chọn Container!");
        	}
        	let pickedIds = [];
        	let records =  $('#driver-table-follow-cont').datagrid('getRows');
            if(records){
                for(let i = 0; i < records.length; i++){
                    pickedIds.push(records[i].id);
                }
            }
            if(pickedIds.length == 0){
            	pickedIds.push(-1);// cho TH chua assgin
            }
        	$.modal.open("Thêm tài xế", prefix + "/shipment-detail/" + shipmentSelected.id + "/add-drivers/ids-assigned-follow-container/" + pickedIds, 800,400);
        },
    },
    {
        text: '<a href="#" class="btn btn-sm btn-default" style="padding: 2px 3px; border-radius: 0;"><i class="fa fa-save text-success"></i> Lưu điều xe</a>',
        handler: function () {
        	if(!shipmentSelected){
        		$.modal.alertError("Bạn chưa chọn Lô!");
        	}
        	saveCont();
        },
    },
]

// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
    //DEFAULT SEARCH FOLLOW DATE
    let fromMonth = (new Date().getMonth() < 10) ? "0" + (new Date().getMonth()) : new Date().getMonth();
    let toMonth = (new Date().getMonth() +2 < 10) ? "0" + (new Date().getMonth() +2 ): new Date().getMonth() +2;
    $('#fromDate').val("01/"+ fromMonth + "/" + new Date().getFullYear());
    $('#toDate').val("01/"+ (toMonth > 12 ? "01" +"/"+ (new Date().getFullYear()+1)  : toMonth + "/" + new Date().getFullYear()));
    let fromDate = stringToDate($('#fromDate').val());
    let toDate =  stringToDate($('#toDate').val());
    fromDate.setHours(0,0,0);
    toDate.setHours(23, 59, 59);
    shipmentSearch.fromDate = fromDate.getTime();
    shipmentSearch.toDate = toDate.getTime();

    loadTable();
    $(".left-side").css("height", $(document).height());
    $("#btn-collapse").click(function () {
        handleCollapse(true);
    });
    $("#btn-uncollapse").click(function () {
        handleCollapse(false);
    });

    //find date
    $('.from-date').datetimepicker({
        language: 'en',
        format: 'dd/mm/yyyy',
        autoclose: true,
        todayBtn: true,
        todayHighlight: true,
        pickTime: false,
        minView: 2
    });
    $('.to-date').datetimepicker({
        language: 'en',
        format: 'dd/mm/yyyy',
        autoclose: true,
        todayBtn: true,
        todayHighlight: true,
        pickTime: false,
        minView: 2
    });
});

//change serviceType
$("#shipmentType").combobox({
    onSelect: function (serviceType) {
      shipmentSearch.serviceType = serviceType.value;
      loadTable();
    }
  });
//search date
function changeFromDate() {
    let fromDate = stringToDate($('#fromDate').val());
    if ($('#toDate').val() != '' && stringToDate($('#toDate').val()).getTime() < fromDate.getTime()) {
        $.modal.alertError('Quý khách không thể chọn từ ngày cao hơn đến ngày.')
        $('#fromDate').val('');
    } else {
        shipmentSearch.fromDate = fromDate.getTime();
        loadTable();
    }
}

function changeToDate() {
    let toDate = stringToDate($('.to-date').val());
    if ($('.from-date').val() != '' && stringToDate($('.from-date').val()).getTime() > toDate.getTime()) {
        $.modal.alertError('Quý khách không thể chọn đến ngày thấp hơn từ ngày.')
        $('.to-date').val('');
    } else {
        toDate.setHours(23, 59, 59);
        shipmentSearch.toDate = toDate.getTime();
        loadTable();
    }
}

function stringToDate(dateStr) {
    let dateParts = dateStr.split('/');
    return new Date(dateParts[2], dateParts[1] - 1, dateParts[0]);
}
function handleCollapse(status) {
    if (status) {
        $(".left-side").css("width", "0.5%");
        $(".left-side").children().hide();
        $("#btn-collapse").hide();
        $("#btn-uncollapse").show();
        $(".right-side").css("width", "99%");
        setTimeout(() => {
            checkForChanges();
            hot.render();
        }, 500);
        return;
    }
    $(".left-side").css("width", "33%");
    $(".left-side").children().show();
    $("#btn-collapse").show();
    $("#btn-uncollapse").hide();
    $(".right-side").css("width", "67%");
    setTimeout(() => {
        checkForChanges();
        hot.render();
    }, 500);
}
//function assignFollowBatchTab() {
//    $(".assignFollowBatch").show();
//    $("#batchBtn").css({"background-color": "#6c9dc7"});
//    $(".assignFollowContainer").hide();
//    $("#containerBtn").css({"background-color": "#c7c1c1"});
//    // let row = $("#dg").datagrid("getSelected");
//    // if(row){
//    //     loadDriver(row.id);
//    // }
//    if(dataAssignedDriver.length >0){
//        $("#pickedDriverTable").datagrid('loadData', dataAssignedDriver);
//        checkForChanges();
//
//    }
//    if(dataDriver.length > 0){
//        $("#driverTable").datagrid('loadData', dataDriver)
//        checkForChanges();
//    }
//}

//function assignFollowContainerTab() {
//    $(".assignFollowContainer").css("display","flex");;
//    $("#containerBtn").css({"background-color": "#6c9dc7"});
//    $(".assignFollowBatch").hide();
//    $("#batchBtn").css({"background-color": "#c7c1c1"});
//    // let row = $("#dg").datagrid("getSelected");
//    // if(row){
//    //     loadShipmentDetail(row.id);
//    // }
//    if(dataContainerList.length > 0){
//        $("#dgShipmentDetail").datagrid('loadData', dataContainerList)
//        checkForChanges();
//    }
//}
$(".main-body").layout();

loadTable("#dg-right-tab1", rightHeight / 2);
loadTable("#dg-right-tab2", rightHeight / 2);

$(".collapse").click(function () {
    $(".main-body__search-wrapper").height(15);
    $(".main-body__search-wrapper--container").hide();
    $(this).hide();
    $(".uncollapse").show();
})

$(".uncollapse").click(function() {
    $(".main-body__search-wrapper").height(SEARCH_HEIGHT);
    $(".main-body__search-wrapper--container").show();
    $(this).hide();
    $(".collapse").show();
})

$(".left-side__collapse").click(function() {
  $('#main-layout').layout('collapse','west');
})
$(".right-side__collapse").click(function() {
  $('#right-layout').layout('collapse','south');
  loadTable("#dg-right", rightHeight + 50);
})

$('#right-layout').layout({
  onExpand: function(region){
      if (region == "south") {
        loadTable("#dg-right", rightHeight - 30);
      }
  }
})
//-------------------------------------------------------------------------------------------------------------------------
//------------------------------------------------- LOAD SHIPMENT LIST-----------------------------------------------------
//-------------------------------------------------------------------------------------------------------------------------
function loadTable() {
	//shipment
    $("#dg").datagrid({
        url: prefix + "/listShipment",
        height: leftHeight - 90,
        method:"post",
        singleSelect: true,
        collapsible: true,
        clientPaging: false,
        pagination: true,
        onClickRow: function () {
            getSelectedShipment();
        },
        rownumbers:true,
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
                  data: shipmentSearch
                }),
                success: function (data) {
                    success(data);
                    if ($('#shipmentType').val() == 1 || $('#shipmentType').val() == 2) {
                        $("#dg").datagrid("hideColumn", "bookingNo");
                        $("#dg").datagrid("showColumn", "blNo");
                        $("#bookingNoDiv").hide();
                        $("#blNoDiv").show();
                    } else if ($('#shipmentType').val() == 3 || $('#shipmentType').val() == 4) {
                        $("#dg").datagrid("hideColumn", "blNo");
                        $("#dg").datagrid("showColumn", "bookingNo");
                        $("#bookingNoDiv").show();
                        $("#blNoDiv").hide();
                    } else {
                        $("#dg").datagrid("hideColumn", "blNo");
                        $("#dg").datagrid("hideColumn", "bookingNo");
                        $("#bookingNoDiv").hide();
                        $("#blNoDiv").hide();
                    }
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });
}
//------------------------------------------------------------------------------------------------------------------------
//FORMAT QUANTITY FOR SHIPMENT LIST
function formatQuantity(){
    
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

// HANDLE WHEN SELECT A SHIPMENT
function getSelectedShipment() {
    let row = $("#dg").datagrid("getSelected");
    if (row) {
        shipmentSelected = row;
        loadShipmentDetail(row.id);
        loadDriver(row.id);
        loadOutSource(row.id);
    }
}
//HANDLE WHEN SELECT A SHIPMENT DETAIL
function getSelectedShipmentDetail(index , row) {
	shipmentDetailSelected = row;
	loadDriverFollowContainer(shipmentSelected.id, shipmentDetailSelected.id);
	loadOutSourceContainer(shipmentDetailSelected.id);
}

$("#dgShipmentDetail").datagrid({
    toolbar: toolbar,
});
$("#driver-table-follow-batch").datagrid({
    toolbar: addDriverForBatch,
});
$("#driver-table-follow-cont").datagrid({
	toolbar: addDriverForContainer,
});
//--------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------LOAD SHIPMENT DETAIL ----------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------
function loadShipmentDetail(id) {
    //reset dataContainerList
    dataContainerList = [];
    $("#dgShipmentDetail").datagrid({
        url: prefix + "/getShipmentDetail",
        height: rightHeight - 140,
        toolbar: toolbar,
        collapsible: true,
        rownumbers:true,
        clientPaging: false,
        onClickRow: function (index, row) {
            getSelectedShipmentDetail(index, row);
        },
        nowrap: false,
        striped: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            let opts = $(this).datagrid("options");
            if (!opts.url) return false;
            $.ajax({
                type: opts.method,
                url: opts.url,
                data: {
                    shipmentId: id
                },
                dataType: "json",
                success: function (data) {
                    dataContainerList = data.rows;
                    success(data);
                    $("#dgShipmentDetail").datagrid("hideColumn", "id");
                    $("#quantity").text(data.total);
                    if ($('#shipmentType').val() == 1) {
                        $("#dgShipmentDetail").datagrid("showColumn", "preorderPickup");
                    } else {
                        $("#dgShipmentDetail").datagrid("hideColumn", "preorderPickup");
                    }
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });
}


//---------------------------------------------------------------------------------------------------------------
//-------------------------------------------------LOAD TABLE DRIVER---------------------------------------------
//---------------------------------------------------------------------------------------------------------------
//load table driver follow batch
function loadDriver(shipmentId){
    //pickedDriverList
    $("#driver-table-follow-batch").datagrid({
        url: prefix + "/assignedDriverAccountList",
        height: rightHeight * 2 / 3 - 20,
        collapsible: true,
        clientPaging: false,
        nowrap: false,
        striped: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            let opts = $(this).datagrid("options");
            if (!opts.url) return false;
            $.ajax({
                type: "GET",
                url: opts.url,
                data: {
                    shipmentId:shipmentId
                },
                dataType: "json",
                success: function (data) {
                    success(data);
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });

}
//load table driver follow container
function loadDriverFollowContainer(shipmentId, shipmentDetailId){
    //pickedDriverList for container
    $("#driver-table-follow-cont").datagrid({
        url: prefix + "/assignedDriverAccountListForPreoderPickup",
        height: rightHeight * 2 / 3 - 20,
        collapsible: true,
        clientPaging: false,
        nowrap: false,
        striped: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            let opts = $(this).datagrid("options");
            if (!opts.url) return false;
            $.ajax({
                type: "GET",
                url: opts.url,
                data: {
                    shipmentId:shipmentId,
                    shipmentDetailId: shipmentDetailId,
                },
                dataType: "json",
                success: function (data) {
                    success(data);
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });

}
//----------------------------------------------------------------------------------------------------------------
//-------------------------------------------------SAVE ASSIGN DRIVER---------------------------------------------
//------------------------------------------------------------------------------------------------------------------
//save assign driver follow batch
function save(){
    let rows = $('#driver-table-follow-batch').datagrid('getRows');
    let pickupAssigns = [];
    if(rows){
        for(let i=0; i< rows.length;i++){
            let object = new Object();
            object.driverId = rows[i].id;
            object.shipmentId = shipmentSelected.id;
            object.fullName = rows[i].fullName;
            object.phoneNumber = rows[i].mobileNumber;
            pickupAssigns.push(object);
        }
    }
    if (getDataFromOutSource(true)){
        if(outsources.length > 0){
            for(let i=0;i < outsources.length; i++){
                pickupAssigns.push(outsources[i])
            }
        }
        $.modal.loading("Đang xử lý...");
        $.ajax({
            url: prefix + "/savePickupAssignFollowBatch",
            method: "post",
            contentType: "application/json",
            data: JSON.stringify(pickupAssigns),
            success: function(result){
                if(result.code == 0){
                    $.modal.msgSuccess(result.msg);
                    getSelectedShipment()
                }else{
                    $.modal.msgError(result.msg);
                }
                $.modal.closeLoading();
            },
            error: function (result) {
                $.modal.alertError("Có lỗi trong quá trình lưu dữ liệu, vui lòng liên hệ admin.");
                $.modal.closeLoading();
            },
        })
    }

}
//save assign driver follow container
function saveCont(){
    let rows = $('#driver-table-follow-cont').datagrid('getRows');
    let pickupAssigns = [];
    
    if(rows){
        for(let i=0; i< rows.length;i++){
            let object = new Object();
            object.driverId = rows[i].id;
            object.shipmentId = shipmentSelected.id;
            object.shipmentDetailId = shipmentDetailSelected.id;
            object.fullName = rows[i].fullName;
            object.phoneNumber = rows[i].mobileNumber;
            pickupAssigns.push(object);
        }
    }
    if (getDataFromOutSourceContainer(true)){
        if(outsourcesContainer.length > 0){
            for(let i=0;i < outsourcesContainer.length; i++){
                pickupAssigns.push(outsourcesContainer[i])
            }
        }
        $.modal.loading("Đang xử lý ...");
        $.ajax({
            url: prefix + "/savePickupAssignFollowContainer",
            method: "post",
            contentType: "application/json",
            data: JSON.stringify(pickupAssigns),
            success: function(result){
            	$.modal.closeLoading();
                if(result.code == 0){
                    $.modal.msgSuccess(result.msg);
                    loadShipmentDetail(shipmentSelected.id);
                }else{
                    $.modal.msgError(result.msg);
                }
            },
            error: function () {
            	$.modal.closeLoading();
                $.modal.alertError("Có lỗi trong quá trình lưu dữ liệu, vui lòng liên hệ admin.");
            },
        })
    }

}
//----------------------------------------------------------------------------------------------------------------
function formatAction(value, row, index) {
	let actions = [];
    actions.push('<a class="btn btn-danger btn-xs " onclick="removeDriver(\'' + index + '\')"><i class="fa fa-remove"></i>Xoá</a> ');
    return actions.join('');
}
function formatActionContainer(value, row, index) {
	let actions = [];
    actions.push('<a class="btn btn-danger btn-xs " onclick="removeDriverContainer(\'' + index + '\')"><i class="fa fa-remove"></i>Xoá</a> ');
    return actions.join('');
}
function removeDriver(index) {
    $('#driver-table-follow-batch').datagrid('deleteRow', index);
    //reload data
    let table_data = $('#driver-table-follow-batch').datagrid('getRows');
    $('#driver-table-follow-batch').datagrid('loadData', table_data);
}
function removeDriverContainer(index) {
    $('#driver-table-follow-cont').datagrid('deleteRow', index);
    let table_data = $('#driver-table-follow-cont').datagrid('getRows');
    $('#driver-table-follow-cont').datagrid('loadData', table_data);
}
function formatRemark(value) {
    let remark = value;
    if(value){
        return '<div class="easyui-tooltip" title="' + value + '" style="width: 80; text-align: center;"><span>' + (remark.length < 15 ? value : "...") + '</span></div>';
    }
    return 
  }
function formatPickup(value) {
    if (value == "Y") {
        return "<span class='label label-success'>Có</span>"
    } else{
        return "<span class='label label-default'>Không</span>"
    }
}
//function formatActionAssign(value, row, index) {
//    let button = '';
//    let shipment = $("#dg").datagrid("getSelected");
//    if(shipment.serviceType == 1){ //receiveContFull
//        if(row.preorderPickup == "Y"){
//            button += '<button class="btn btn-primary btn-xs" onclick="assignFollowContainer(\'' + row.id + '\')"><i class="fa fa-edit"></i>Điều xe</button> ';
//        }else{
//            button += '<button class="btn btn-primary btn-xs" onclick="assignFollowContainer(\'' + row.id + '\')" disabled><i class="fa fa-edit"></i>Điều xe</button> ';
//        }
//    }else {//3 serviecType con lai
//        button += '<button class="btn btn-primary btn-xs" onclick="assignFollowContainer(\'' + row.id + '\')"><i class="fa fa-edit"></i>Điều xe</button> ';
//    }
//    return button;
//}

//function editDriver(id){
//    $.modal.open("Thông tin Tài xế ", prefix +"/edit/driver/"+id);
//}

//function assignFollowContainer(id){
//    $.modal.openTab("Điều xe theo Container", prefix + "/preoderPickupAssign/" + id);
//}

//function addTruck(){
//    $.modal.open("Thêm xe mới", "/logistic/logisticTruck/add");
//}
//function addDriver(){
//    $.modal.open("Thêm xe mới", "/logistic/transport/add");
//}

function msgSuccess(msg) {
    $.modal.msgSuccess(msg);
    loadShipmentDetail(shipmentSelected.id);
}

function msgError(msg) {
    $.modal.msgError(msg);
}
function checkForChanges(){		
    $('#driver-table-follow-cont').datagrid('resize');

    $('#driver-table-follow-batch').datagrid('resize');

    $('#dgShipmentDetail').datagrid('resize');
 }
//---------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------THUE NGOAI---------------------------------------------------------------
//---------------------------------------------------------------------------------------------------------------------------
 var dogrid = document.getElementById("container-grid-follow-batch"), hot;
 var dogridContainer = document.getElementById("container-grid-follow-cont"), hotContainer;
 var config;
 var configContainer;
 var outsources = [];
 var outsourcesContainer = [];
 function loadOutSource(shipmentId) {
	$.modal.loading("Đang xử lý ...");
    $.ajax({
        url: prefix + "/out-source/batch/" + shipmentId,
        method: "GET",
        success: function (data) {
        	$.modal.closeLoading();
            if (data.code == 0) {
                hot.destroy();
                hot = new Handsontable(dogrid, config);
                hot.loadData(data.outSourceList);
                hot.render();
            }
        },
        error: function () {
        	$.modal.closeLoading();
            $.modal.alertError("Có lỗi trong quá trình tải dữ liệu, vui lòng liên hệ admin.");
        },
    });
}
 
 function loadOutSourceContainer(shipmentDetailId) {
		$.modal.loading("Đang xử lý ...");
	    $.ajax({
	        url: prefix + "/out-source/container/" + shipmentDetailId,
	        method: "GET",
	        success: function (data) {
	        	$.modal.closeLoading();
	            if (data.code == 0) {
	                hotContainer.destroy();
	                hotContainer = new Handsontable(dogridContainer, configContainer);
	                hotContainer.loadData(data.outSourceList);
	                hotContainer.render();
	            }
	        },
	        error: function () {
	        	$.modal.closeLoading();
	            $.modal.alertError("Có lỗi trong quá trình tải dữ liệu, vui lòng liên hệ admin.");
	        },
	    });
	}
// CONFIGURATE HANDSONTABLE
config = {
    stretchH: "all",
    height: "500",
    minRows: 5,
    maxRows: 20,
    width: "100%",
    minSpareRows: 1,
    rowHeights: 30,
    fixedColumnsLeft: 0,
    manualColumnResize: true,
    manualRowResize: true,
    renderAllRows: true,
    rowHeaders: true,
    className: "htMiddle",
    colHeaders: function (col) {
        switch (col) {
            case 0:
                return "Đơn vị chủ quản";
            case 1:
                return '<span class="required">Số điện thoại</span>';
            case 2:
                return '<span class="required">Họ và tên</span>';
            case 3:
                return '<span class="required">Xe đầu kéo</span>';
            case 4:
                return '<span class="required">Xe rơ mooc</span>';
        }
    },
    colWidths: [100, 100, 150, 100, 100],
    filter: "true",
    columns: [
        {
            data: "driverOwner",
            type: "autocomplete",
            source: driverOwnerList,
        },
        {
            data: "phoneNumber",
            type: "autocomplete",
        },
        {
            data: "fullName",
        },
        {
            data: "truckNo",
        },
        {
            data: "chassisNo",
        },
    ],
    afterChange: onChange
};

configContainer = {
	    stretchH: "all",
	    height: "500",
	    minRows: 5,
	    maxRows: 20,
	    width: "100%",
	    minSpareRows: 1,
	    rowHeights: 30,
	    fixedColumnsLeft: 0,
	    manualColumnResize: true,
	    manualRowResize: true,
	    renderAllRows: true,
	    rowHeaders: true,
	    className: "htMiddle",
	    colHeaders: function (col) {
	        switch (col) {
	            case 0:
	                return "Đơn vị chủ quản";
	            case 1:
	                return '<span class="required">Số điện thoại</span>';
	            case 2:
	                return '<span class="required">Họ và tên</span>';
	            case 3:
	                return '<span class="required">Xe đầu kéo</span>';
	            case 4:
	                return '<span class="required">Xe rơ mooc</span>';
	        }
	    },
	    colWidths: [100, 100, 150, 100, 100],
	    filter: "true",
	    columns: [
	        {
	            data: "driverOwner",
	            type: "autocomplete",
	            source: driverOwnerList,
	        },
	        {
	            data: "phoneNumber",
	            type: "autocomplete",
	        },
	        {
	            data: "fullName",
	        },
	        {
	            data: "truckNo",
	        },
	        {
	            data: "chassisNo",
	        },
	    ],
	    afterChange: onChangeContainer
	};

function onChange(changes, source) {
    if (!changes) {
        return;
    }
    changes.forEach(function (change) {
        if (change[1] == "driverOwner" && change[3] != null && change[3] != '') {
        	$.modal.loading("Đang xử lý ...");
            $.ajax({
                url: prefix + "/owner/"+ change[3] +"/driver-phone-list",
                method: "GET",
                success: function (data) {
                	$.modal.closeLoading();
                    if (data.code == 0) {
                        hot.updateSettings({
                            cells: function (row, col, prop) {
                                if (row == change[0] && col == 1) {
                                    let cellProperties = {};
                                    cellProperties.source = data.driverPhoneList;
                                    return cellProperties;
                                }
                            }
                        });
                    }
                },
                error: function () {
                	$.modal.closeLoading();
                    $.modal.alertError("Có lỗi trong quá trình tải dữ liệu, vui lòng liên hệ admin.");
                },
            });
        } else if (change[1] == "phoneNumber" && change[3] != null && change[3] != '') {
        	$.modal.loading("Đang xử lý ...");
            $.ajax({
                url: prefix + "/driver-phone/" + change[3] + "/infor",
                method: "GET",
                success: function (data) {
                	$.modal.closeLoading();
                    if (data.code == 0) {
                    	if(data.pickupAssign){
                            hot.setDataAtCell(change[0], 2, data.pickupAssign.fullName);
                            hot.setDataAtCell(change[0], 3, data.pickupAssign.truckNo);
                            hot.setDataAtCell(change[0], 4, data.pickupAssign.chassisNo);
                    	}
                    }
                },
                error: function () {
                	$.modal.closeLoading();
                    $.modal.alertError("Có lỗi trong quá trình tải dữ liệu, vui lòng liên hệ admin.");
                },
            });
        }
    });
}

function onChangeContainer(changes, source) {
    if (!changes) {
        return;
    }
    changes.forEach(function (change) {
        if (change[1] == "driverOwner" && change[3] != null && change[3] != '') {
        	$.modal.loading("Đang xử lý ...");
            $.ajax({
                url: prefix + "/owner/"+ change[3] +"/driver-phone-list",
                method: "GET",
                success: function (data) {
                	$.modal.closeLoading();
                    if (data.code == 0) {
                        hotContainer.updateSettings({
                            cells: function (row, col, prop) {
                                if (row == change[0] && col == 1) {
                                    let cellProperties = {};
                                    cellProperties.source = data.driverPhoneList;
                                    return cellProperties;
                                }
                            }
                        });
                    }
                },
                error: function () {
                	$.modal.closeLoading();
                    $.modal.alertError("Có lỗi trong quá trình tải dữ liệu, vui lòng liên hệ admin.");
                },
            });
        } else if (change[1] == "phoneNumber" && change[3] != null && change[3] != '') {
        	$.modal.loading("Đang xử lý ...");
            $.ajax({
                url: prefix + "/driver-phone/" + change[3] + "/infor",
                method: "GET",
                success: function (data) {
                	$.modal.closeLoading();
                    if (data.code == 0) {
                    	if(data.pickupAssign){
                            hotContainer.setDataAtCell(change[0], 2, data.pickupAssign.fullName);
                            hotContainer.setDataAtCell(change[0], 3, data.pickupAssign.truckNo);
                            hotContainer.setDataAtCell(change[0], 4, data.pickupAssign.chassisNo);
                    	}
                    }
                },
                error: function () {
                	$.modal.closeLoading();
                    $.modal.alertError("Có lỗi trong quá trình tải dữ liệu, vui lòng liên hệ admin.");
                },
            });
        }
    });
}
// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);
hotContainer = new Handsontable(dogridContainer, configContainer);

//GET DATA FROM HANDSOME
function getDataFromOutSource(){
    outsources = [];
    let myTableData = hot.getSourceData();
    let cleanedGridData = [];
    let errorFlg = false;
    for (let i = 0; i < myTableData.length; i++) {
        if (Object.keys(myTableData[i]).length > 0) {
            if (myTableData[i].driverOwner || myTableData[i].phoneNumber || myTableData[i].fullName || myTableData[i].truckNo || myTableData[i].chassisNo) {
                cleanedGridData.push(myTableData[i]);
            }
        }
    }
    $.each(cleanedGridData, function(index, object){
        let outsource = new Object();
        if(!object["phoneNumber"]){
            $.modal.alertError("Số điện thoại hàng:" + (index + 1) + " không được trống!");
            errorFlg = true;
            return false;
        }
        if(!object["fullName"]){
            $.modal.alertError("Họ tên hàng:" + (index + 1) + " không được trống!");
            errorFlg = true;
            return false;
        }
        if(!object["truckNo"]){
            $.modal.alertError("Biển số xe đầu kéo hàng:" + (index + 1) +" không được trống!");
            errorFlg = true;
            return false;
        }
        if(!object["chassisNo"]){
            $.modal.alertError("Biển số xe rơ mooc hàng:" + (index + 1) +" không được trống!");
            errorFlg = true;
            return false;
        }
        outsource.phoneNumber = object["phoneNumber"].trim();
        outsource.driverOwner = object["driverOwner"];
        outsource.truckNo = object["truckNo"].trim().toUpperCase();
        outsource.fullName = object["fullName"].trim();
        outsource.chassisNo = object["chassisNo"].trim().toUpperCase();
        outsource.shipmentId = shipmentSelected.id
        outsource.externalFlg = 1;
        outsources.push(outsource);
    })
    if (errorFlg) {
        return false;
    } else {
        return true;
    }
}

function getDataFromOutSourceContainer(){
    outsourcesContainer = [];
    let myTableData = hotContainer.getSourceData();
    let cleanedGridData = [];
    let errorFlg = false;
    for (let i = 0; i < myTableData.length; i++) {
        if (Object.keys(myTableData[i]).length > 0) {
            if (myTableData[i].driverOwner || myTableData[i].phoneNumber || myTableData[i].fullName || myTableData[i].truckNo || myTableData[i].chassisNo) {
                cleanedGridData.push(myTableData[i]);
            }
        }
    }
    $.each(cleanedGridData, function(index, object){
        let outsource = new Object();
        if(!object["phoneNumber"]){
            $.modal.alertError("Số điện thoại hàng:" + (index + 1) + " không được trống!");
            errorFlg = true;
            return false;
        }
        if(!object["fullName"]){
            $.modal.alertError("Họ tên hàng:" + (index + 1) + " không được trống!");
            errorFlg = true;
            return false;
        }
        if(!object["truckNo"]){
            $.modal.alertError("Biển số xe đầu kéo hàng:" + (index + 1) +" không được trống!");
            errorFlg = true;
            return false;
        }
        if(!object["chassisNo"]){
            $.modal.alertError("Biển số xe rơ mooc hàng:" + (index + 1) +" không được trống!");
            errorFlg = true;
            return false;
        }
        outsource.phoneNumber = object["phoneNumber"].trim();
        outsource.driverOwner = object["driverOwner"];
        outsource.truckNo = object["truckNo"].trim().toUpperCase();
        outsource.fullName = object["fullName"].trim();
        outsource.chassisNo = object["chassisNo"].trim().toUpperCase();
        outsource.shipmentId = shipmentSelected.id
        outsource.shipmentDetailId = shipmentDetailSelected.id;
        outsource.externalFlg = 1;
        outsourcesContainer.push(outsource);
    })
    if (errorFlg) {
        return false;
    } else {
        return true;
    }
}
function generatePDF() {
	if(!shipmentSelected){
		$.modal.alertError("Bạn chưa chọn Lô!");
		return
	}
    $.modal.openTab("In phiếu", ctx +"logistic/print/shipment/"+shipmentSelected.id);
}
function appendDriverList(rows) {
    if(rows){
        for(let i=0; i< rows.length;i++){
            $('#driver-table-follow-batch').datagrid('appendRow', rows[i]);
        }
    }
}
function appendDriverListCont(rows) {
    if(rows){
        for(let i=0; i< rows.length;i++){
            $('#driver-table-follow-cont').datagrid('appendRow', rows[i]);
        }
    }
}