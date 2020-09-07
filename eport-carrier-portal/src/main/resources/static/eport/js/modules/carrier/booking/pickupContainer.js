var PREFIX = ctx + "carrier/booking/detail";
var shipmentDetails = [];
var pickedContainers = [];
var boxSztp;
var boxBlock;


$( document ).ready(function() {
    $("#dg").datagrid({
        height: window.innerHeight - 350,
        rownumbers:true
    });
});

// Call when changing block, bay, sztp to search what block, bay and sztp carrier want to get
function loadContainerList(reqData) {
    let reqData2 = {
        block: "Z1",
        bay: "17",
        sztp: "22G0"
    };

    $.ajax({
        cache: true,
        type: "POST",
        url: PREFIX + "/container/position",
        contentType: "application/json",
        data: JSON.stringify(reqData),
        async: false,
        error: function (request) {
            $.modal.closeLoading();
            $.modal.alertError("System error");
        },
        success: function (result) {
            $.modal.closeLoading();
            if (result.code == web_status.SUCCESS) {
                shipmentDetails = result.shipmentDetails
                loadPostion();
            } else if (result.code == web_status.WARNING) {
                $.modal.alertWarning(result.msg);
            } else {
                $.modal.alertError(result.msg);
            }
        },
    });
}

function loadPostion() {
    let str = '<div class="bayPosition">';
    for (let col = 0; col < 12; col++) {
        str += '<div class="columnDiv">';
        for (let row = 4; row >= 0; row--) {
            if (shipmentDetails[row][col] != null) {

                // Position is empty
                if (shipmentDetails[row][col].containerNo == null) {
                    str += '<div id="cell' + col + '-' + row + '" class="cellDiv" style="background-color: #dbcfcf;>CONT</div>';

                    // Container must be make into an order
                } else {
                    shipmentDetails[row][col].preorderPickup = 'N';
                    if (shipmentDetails[row][col].orderNo) {
                        str += '<div style="background-color: #d5e05d;" class="cellDiv" onclick="addOrRemove(' + col + ',' + row + ')">' + shipmentDetails[row][col].containerNo + '</div>';
                    } else {
                        str += '<div id="container' + col + '-' + row + '" style="background-color: #72ecea;" class="cellDiv" onclick="addOrRemove(' + col + ',' + row + ')">' + shipmentDetails[row][col].containerNo + '</div>';
                    }
                    

                }
            } else {
                str += '<div class="cellDivDisable"></div>';
            }
        }
        str += '</div>';
    }
    str += '</div><div style="margin-bottom: 10px;"><b>' + 'Z1-17' + '</b></div>';
    $(".contListPosition").html(str);
}

function addOrRemove(col, row) {
    let shipmentDetail = shipmentDetails[row][col];
    if (shipmentDetail.orderNo) {
        $.modal.alertWarning("Container " + shipmentDetail.containerNo + " Đã được làm lệnh cấp rỗng, quý khách vui lòng chọn container khác.");
    } else {
        if (row+1 < 6 && shipmentDetails[row+1][col].preorderPickup == 'N' && !shipmentDetails[row+1][col].orderNo) {
            // $.modal.alertWarning("Cảnh báo container " + shipmentDetail.containerNo + " bởi container " + shipmentDetails[row+1][col].containerNo); 
        }
        if ('Y' == shipmentDetail.preorderPickup) {
            shipmentDetails[row][col].preorderPickup = 'N';
            removePickedContainer(shipmentDetail.containerNo);
            $('#container' + col + '-' + row).css('background-color', '#72ecea');
        } else {
            shipmentDetails[row][col].preorderPickup = 'Y';
            pickedContainers.push(shipmentDetail);
            $('#container' + col + '-' + row).css('background-color', '#279c9a');
        }
        loadTable();
    }

}

function loadTable() {
    $("#dg").datagrid({
        height: window.innerHeight - 350,
        singleSelect: true,
        collapsible: true,
        clientPaging: false,
        rownumbers:true,
        pagination: false,
        nowrap: false,
        striped: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            success(pickedContainers);
        },
    });
}

function removePickedContainer(containerNo) {
    pickedContainers.forEach(function iterate(element, index, array) {
        if (element.containerNo == containerNo) {
            pickedContainers.splice(index, 1);
            return false;
        }
    }); 
}

function formatYardPosition(value, row) {
    return row.block + '-' + row.bay + '-' + row.row + '-' + row.tier;
}


function confirm() {
    layer.confirm("Xác nhận lấy thông tin container trên bãi?", {
        icon: 3,
        title: "Xác Nhận",
        btn: ['Đồng Ý', 'Hủy Bỏ']
    }, function () {
        parent.loadShipmentDetailPickContainer(pickedContainers);
        $.modal.close();
    }, function () {
    });   
}

function closeForm() {
    $.modal.close();
}

$(".c-search-box-sztp").on("select2:open", function(e) {
     $(".c-search-box-sztp").text(null)
     $(".c-search-box-block").text(null)
     $(".c-search-box-bays").text(null)
  });
$(".c-search-box-sztp").select2({
    theme: "bootstrap",
    allowClear: true,
    delay: 250,
    ajax: {
        url: PREFIX + "/size/container/list",
        method: "GET",
        dataType : 'json',
      data: function (params) {
        return {
          keyString: params.term,
        };
      },
      processResults: function (data) {
        let results = []
        data.data.forEach(function (element, i) {
          let obj = {};
          obj.id = i;
          obj.text = element.dictValue;
          results.push(obj);
        })
        return {
          results: results,
        };
      },
    },
    placeholder: "Sztp",
  });


  $(".c-search-box-block").on("select2:open", function(e) {
    $(".c-search-box-block").text(null)
    $(".c-search-box-bays").text(null)
    boxSztp = $(".c-search-box-sztp").text().trim();
    if(boxSztp == null || boxSztp == "")
    {
        $.modal.alertWarning("Vui lòng chọn Size / Type trước");
        return;
    }

  });

  $(".c-search-box-block").select2({
    theme: "bootstrap",
    allowClear: true,
    delay: 250,
    ajax: {
        url: PREFIX + "/sztp/blocks",
        method: "POST",
        dataType: "json",
        data: function (params) {
        return {
            keyString: params.term,
            boxSztp: boxSztp,
        };
      },
      processResults: function (data) {
        let results = []
        data.blocks.forEach(function (element, i) {
          let obj = {};
          obj.id = i;
          obj.text = element;
          results.push(obj);
        })
        return {
          results: results,
        };
      },
    },
    placeholder: "Block",
  });


  $(".c-search-box-bays").on("select2:open", function(e) {
    boxBlock = $(".c-search-box-block").text().trim();
    if(boxBlock == null || boxBlock == "")
    {
        $.modal.alertWarning("Vui lòng chọn block trước");
        return;
    }
  });

  $(".c-search-box-bays").select2({
    theme: "bootstrap",
    allowClear: true,
    delay: 250,
    ajax: {
        url: PREFIX + "/sztp/block/bays",
        method: "POST",
        dataType: "json",
        data: function (params) {
        return {
            keyString: params.term,
            boxSztp : boxSztp,
            boxBlock: boxBlock
        };
      },
      processResults: function (data) {
      console.log("TCL: data", data)
        let results = []
        data.bays.forEach(function (element, i) {
          let obj = {};
          obj.id = i;
          obj.text = element;
          results.push(obj);
        })
        return {
          results: results,
        };        
      },
    },
    placeholder: "Bays",
  });

  $(".c-search-box-bays").change(function () {
    let reqData = new Object();
    reqData.sztp = $(".c-search-box-sztp").text().trim();
    reqData.block = $(".c-search-box-block").text().trim();
    reqData.bay = $(".c-search-box-bays").text().trim();
    loadContainerList(reqData);
  });





