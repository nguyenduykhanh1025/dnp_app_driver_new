const PREFIX = ctx + "carrier/booking";
const PREFIX2 = ctx + "carrier/booking/detail";
var booking = new Object();
var bookingDetail = new Object();
var bookingSelected;
var dogrid = document.getElementById("container-grid"), hot;
var coutCheck = 0;
var rowAmount = 0;



// HANDLE COLLAPSE SHIPMENT LIST
$(document).ready(function () {
    loadTable();
    $(".left-side").css("height", $(document).height());
    $("#btn-collapse").click(function () {
        handleCollapse(true);
    });
    $("#btn-uncollapse").click(function () {
        handleCollapse(false);
    });
    $('#searchBookingNo').keyup(function (event) {
        if (event.keyCode == 13) {
            bookingNo = $('#searchBookingNo').val().toUpperCase();
          if (bookingNo == "" || bookingNo == undefined) {
            loadTable(bookingNo);
          }
          booking = new Object();
          booking.bookingNo = bookingNo;
          loadTable(booking);
        }
      });
});

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


loadTable();

function loadTable(booking) {
    $("#dg").datagrid({
      url: PREFIX + "/list",
      method: "POST",
      singleSelect: true,
      height: $(document).height() - 75,
      clientPaging: true,
      pagination: true,
      pageSize: 20,
      onClickRow: function () {
        getSelectedRow();
      },
      nowrap: false,
      striped: true,
      rownumbers: true,
      loader: function (param, success, error) {
        var opts = $(this).datagrid("options");
        if (!opts.url) return false;
        $.ajax({
          type: opts.method,
          url: opts.url,
          contentType: "application/json",
          accept: "text/plain",
          dataType: "text",
          data: JSON.stringify({
            pageNum: param.page,
            pageSize: param.rows,
            orderByColumn: param.sort,
            isAsc: param.order,
            data : booking
          }),
          dataType: "json",
          success: function (data) {
            success(data);
          },
          error: function () {
            error.apply(this, arguments);
          },
        });
      },
      error: function () {
        error.apply(this, arguments);
      },
    });
  }

// HANDSOME TABLE
// CONFIGURATE HANDSONTABLE
function configHandson() {
    dateValidator = function (value, callback) {
        if (!value || 0 === value.length) {
          callback(false);
        } else {
          if (isGoodDate(value)) {
            callback(true);
          } else {
            callback(false);
          }
        }
      };
    config = {
        stretchH: "all",
        height: document.documentElement.clientHeight - 100,
        minRows: rowAmount,
        maxRows: rowAmount,
        width: "100%",
        minSpareRows: 0,
        rowHeights: 30,
        fixedColumnsLeft: 3,
        manualColumnResize: true,
        manualRowResize: true,
        renderAllRows: true,
        rowHeaders: true,
        className: "htMiddle",
        colHeaders: function (col) {
            switch (col) {
                case 0:
                    return "Container No";
                case 1:
                    return "Sztp";
                case 2:
                    return "Tọa độ";
                case 3:
                    return "Cấp từ ngày";
                case 4:
                    return "Ngày hết hạn";
                case 5:
                    return "Loại hàng";
                case 6:
                    return "Tàu";
                case 7:
                    return "Chuyến";
                case 8:
                    return "Cảng dỡ";
                case 9:
                    return "Ghi chú";
            }
        },
        colWidths: [ 100, 100, 100, 120, 100, 100, 100, 150, 100, 150],
        filter: "true",
        columns: [
            {
                data: "containerNo",
            },
            {
                data: "sztp",
            },
            {
                data: "yardPosition",
            },
            {
                data: "releaseDate",
                type: "date",
                dateFormat: "DD/MM/YYYY",
                correctFormat: true,
                defaultDate: new Date(),
                validator: dateValidator,
            },
            {
                data: "expiredDate",
                type: "date",
                dateFormat: "DD/MM/YYYY",
                correctFormat: true,
                defaultDate: new Date(),
                validator: dateValidator,
            },
            {
                data: "cargoType",
            },
            {
                data: "userVoy", 
            },
            {
                data: "userVoy",
            },
            {
                data: "pod", 
            },
            {
                data: "remark",
            }
        ],
    };
}
configHandson();

// RENDER HANSONTABLE FIRST TIME
hot = new Handsontable(dogrid, config);

// FORMAT DATE FOR SHIPMENT LIST
function formatDate(value) {
    var date = new Date(value);
    var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    var month = date.getMonth() + 1;
    var monthText = month < 10 ? "0" + month : month;
    return day + "/" + monthText + "/" + date.getFullYear();
}

function formatStatus(value) {
    if(value == 'R')
    {
        return "Chưa phát hành";
    }
    if(value == 'H')
    {
        return "Đã phát hành";
    }
    return "Không xác định"
}

function pickupContainer(id) {
    $.modal.openWithOneButton('Cấp container', PREFIX + "/pickupContainer/", 1000, 400);
}

function addBooking(id) {
    $.modal.open('Cấp container', PREFIX + "/add/", 1000, 400);
}

function releaseBooking() {
    let row = $('#dg').datagrid('getSelected');
    layer.confirm("Bạn có muốn phát hành Booking này?", {
        icon: 3,
        title: "Xác Nhận",
        btn: ['Đồng Ý', 'Hủy Bỏ']
    }, function () {
        layer.close(layer.index);
        $.ajax({
            url: PREFIX + "/releaseBooking",
            method: "POST",
            data : {
                id : row.id
            }
        }).done(function (res) {
            if (res.code == 0) {
                loadTable();
            } else {
                $.modal.alertError(res.msg);
            }
        });
    }, function () {
    });   
}

function editBooking() {
    let row = $('#dg').datagrid('getSelected');
    $.modal.open('Sửa booking', PREFIX + "/edit/" + row.id, 1000, 400);
}


function delBooking() {
    var row = $('#dg').datagrid('getSelected');
    layer.confirm("Xác nhận xóa xóa booking.", {
        icon: 3,
        title: "Xác Nhận",
        btn: ['Đồng Ý', 'Hủy Bỏ']
    }, function () {
        layer.close(layer.index);
        $.ajax({
            url: PREFIX + "/remove",
            method: "POST",
            data : {
                ids : row.id
            }
        }).done(function (res) {
            if (res.code == 0) {
                $.modal.alertSuccess("Xóa booking thành công.");
                loadTable();
            } else {
                $.modal.alertError("Xóa booking thất bại.");
            }
        });
    }, function () {
    });   
}

// HANDLE WHEN SELECT A SHIPMENT
function getSelectedRow() {
    let row = $("#dg").datagrid("getSelected");
    if(row.bookStatus == 'H')
    {
        $('#pickupContainer').prop('disabled', false);
        $('#saveInput').prop('disabled', false);
        $('#releaseBooking').prop('disabled', false);
        $('#releaseStatusTitle').text("Chưa phát hành!")
    }else {
        $('#pickupContainer').prop('disabled', true);
        $('#saveInput').prop('disabled', true);
        $('#releaseBooking').prop('disabled', true);
        $('#releaseStatusTitle').text("Đã phát hành!")
    }
    if (row) {
        rowAmount = row.bookQty;
        bookingSelected = row;
        loadShipmentDetail(row.id);
    }
}
function loadShipmentDetail(id) {
    bookingDetail.bookingId = id;
    $.ajax({
        url: PREFIX2 + "/list",
        method: "POST",
        data: bookingDetail,
        success: function (data) {
            if (data.code == 0) {
                sourceData = data.rows;
                hot.destroy();
                configHandson();
                hot = new Handsontable(dogrid, config);
                hot.loadData(sourceData);
                hot.render();
            }
        }
    });
}

function getDataFromTable() {
    let row = $('#dg').datagrid('getSelected');
    let myTableData = hot.getSourceData();
    let errorFlg = false;
    let cleanedGridData = [];
    for (let i = 0; i < myTableData.length; i++) {
        if (myTableData[i].containerNo != null) {
            cleanedGridData.push(myTableData[i]);
        }
    }
    bookingDetails = [];
    contList = [];
    $.each(cleanedGridData, function (index, object) {
        var bookingDetail = new Object();
        if (object["containerNo"] != null && object["containerNo"] != "" && !/[A-Z]{4}[0-9]{7}/g.test(object["containerNo"])) {
            $.modal.alertError("Hàng " + (index + 1) + ": Số container không hợp lệ!");
            errorFlg = true;
        }
        bookingDetail.id = object["id"]
        bookingDetail.containerNo = object["containerNo"];
        bookingDetail.cargoType = object["cargoType"];
        bookingDetail.expiredDate = object["expiredDate"];
        bookingDetail.pod = object["pod"];
        bookingDetail.releaseDate = object["releaseDate"];
        bookingDetail.remark = object["remark"];
        bookingDetail.sztp = object["sztp"];
        bookingDetail.userVoy = object["userVoy"];
        bookingDetail.yardPosition = object["yardPosition"];
        contList.push(object["containerNo"]);
        bookingDetail.bookingId = row.id;
        bookingDetails.push(bookingDetail);
    });
    if (!errorFlg) {
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

    if (errorFlg) {
        return false;
    } else {
        return true;
    }
}

function saveInput() {
    if (getDataFromTable()) {
        if (bookingDetails.length == bookingSelected.bookQty) {
            $.modal.loading("Đang xử lý...");
            $.ajax({
                url: PREFIX2 + "/add/",
                method: "POST",
                contentType: "application/json",
                accept: 'text/plain',
                data: JSON.stringify(bookingDetails),
                dataType: 'text',
                success: function (data) {
                    var result = JSON.parse(data);
                    if (result.code == 0) {
                        $.modal.msgSuccess(result.msg);
                        loadShipmentDetail(bookingSelected.id);
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
        } else {
            $.modal.alertError("Bạn chưa nhập đủ container yêu cầu.");
        }
    }
}


  function isGoodDate(dt) {
    var reGoodDate = /^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|(([1][26]|[2468][048]|[3579][26])00))))$/g;
    return reGoodDate.test(dt);
  }

