const PREFIX = ctx + "mc/plan/management";
const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var pickupHistory = new Object();
pickupHistory.params = new Object();
var pickup = new Object();
pickup.params = new Object();
var vslNms = [], voyNos = [], sztps = [], currentDataSet = [];

$( document ).ready(function() {
    $(".main-body").layout();

  $(".collapse").click(function () {
    $(".main-body__search-wrapper").height(15);
    $(".main-body__search-wrapper--container").hide();
    $(this).hide();
    $(".uncollapse").show();
  })

  $(".uncollapse").click(function () {
    $(".main-body__search-wrapper").height(SEARCH_HEIGHT);
    $(".main-body__search-wrapper--container").show();
    $(this).hide();
    $(".collapse").show();
  });

  $(".left-side__collapse").click(function () {
    $('#main-layout').layout('collapse', 'west');
  });

    $("#blBookingNo").textbox('textbox').bind('keydown', function (e) {
        // enter key
        if (e.keyCode == 13) {
            pickupHistory.params.blBookingNo = $("#blBookingNo").textbox('getText').toUpperCase();
            pickup.params.blBookingNo = $("#blBookingNo").textbox('getText').toUpperCase();
            loadTableLeft();
            loadTableRight();
        }
    });

    vesselAndVoyages[0].selected = true;
    $("#vesselAndVoyages").combobox({
        valueField: 'vslAndVoy',
        textField: 'vslAndVoy',
        data: vesselAndVoyages,
        onSelect: function (vesselAndVoyage) {
            if (vesselAndVoyage.vslAndVoy != 'Chọn tàu chuyến') {
                let vslAndVoyArr = vesselAndVoyage.vslAndVoy.split(" - ");
                pickupHistory.params.vslNm = vslAndVoyArr[0];
                pickupHistory.params.voyNo = vslAndVoyArr[2];
                pickup.params.vslNm = vslAndVoyArr[0];
                pickup.params.voyNo = vslAndVoyArr[2];
            } else {
                pickupHistory.params.vslNm = null;
                pickupHistory.params.voyNo = null;
                pickup.params.vslNm = null;
                pickup.params.voyNo = null;
            }
            loadTableLeft();
            loadTableRight();
        }
    });

    loadTableLeft();
    loadTableRight();

    setInterval(() => {
        loadTableLeft();
        loadTableRight();
    }, 10000);
});

function loadTableLeft() {
    $("#dg-left").datagrid({
        url: PREFIX + '/vessel-voyage/list',
        height: $(document).height() - $(".main-body__search-wrapper").height() - 65,
        method: 'POST',
        multipleSelect: true,
        collapsible: true,
        clientPaging: false,
        pagination: true,
        rownumbers: true,
        onSelect: function (index, row) {
            selectRowLeftTable(index, row);
        },
        onUnselect: function (index, row) {
            unSelectRowLeftTable(index, row);
        },
        pageSize: 50,
        nowrap: true,
        striped: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            $("#dg-left").datagrid('loaded');
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
                    data: pickupHistory
                }),
                success: function (res) {
                    if (res.code == 0 && res.vesselVoyageMcs) {
                        success(res.vesselVoyageMcs);
                    } else {
                        success([]);
                    }
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });
}

function loadTableRight() {
    $("#dg-right").datagrid({
        url: PREFIX + '/pickup-history/list',
        height: $(document).height() - $(".main-body__search-wrapper").height() - 65,
        method: 'POST',
        singleSelect: true,
        collapsible: true,
        clientPaging: false,
        pagination: true,
        rownumbers: true,
        pageSize: 50,
        nowrap: true,
        striped: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            $("#dg-right").datagrid('loaded');
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
                    data: pickup
                }),
                success: function (res) {
                    if (res.code == 0 && res.pickupHistories) {
                        success(res.pickupHistories);
                    } else {
                        success([]);
                    }
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });
}

function formatDateTime(value) {
    if (value == null) {
        return '';
    } 
    let date = new Date(value);
    let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    let month = date.getMonth() + 1;
    let monthText = month < 10 ? "0" + month : month;
    let hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
    let minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
    let second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
    return day + "/" + monthText + "/" + date.getFullYear() + ' ' + hour + ':' + minute + ':' + second;
}

function selectRowLeftTable(index, row) {
    vslNms.push(row.vslNm);
    voyNos.push(row.voyNo);
    sztps.push(row.sztp);
    pickup.params.vslNms = vslNms;
    pickup.params.voyNos = voyNos;
    pickup.params.sztps = sztps;
    loadTableRight();
}

function unSelectRowLeftTable(index, row) {
    for (let i=0; i<vslNms.length; i++) {
        if (row.vslNm == vslNms[i] && row.voyNo == voyNos[i] && row.sztp == sztps[i]) {
            vslNms.splice(i, 1);
            voyNos.splice(i, 1);
            sztps.splice(i, 1);
        }
    }
    pickup.params.vslNms = vslNms;
    pickup.params.voyNos = voyNos;
    pickup.params.sztps = sztps;
    if (vslNms.length == 0) {
        pickup.params = new Object();
    }
    loadTableRight();
}

function search() {
    let vesselAndVoyage = $("#vesselAndVoyages").combobox('getValue');
    if (vesselAndVoyage != null && vesselAndVoyage != 'Chọn tàu chuyến') {
        let vslAndVoyArr = vesselAndVoyage.vslAndVoy.split(" - ");
        pickupHistory.params.vslNm = vslAndVoyArr[0];
        pickupHistory.params.voyNo = vslAndVoyArr[2];
        pickup.params.vslNm = vslAndVoyArr[0];
        pickup.params.voyNo = vslAndVoyArr[2];
    }
    pickupHistory.params.blBookingNo = $("#blBookingNo").textbox('getText');
    pickup.params.blBookingNo = $("#blBookingNo").textbox('getText');
    loadTableLeft();
    loadTableRight();
}

function clearInput() {
    $("#vesselAndVoyages").combobox('setValue', 'Chọn tàu chuyến');
    $("#blBookingNo").textbox('setText', '');
    pickupHistory = new Object();
    pickup = new Object();
    pickupHistory.params = new Object();
    pickup.params = new Object();
    loadTableLeft();
    loadTableRight();
}