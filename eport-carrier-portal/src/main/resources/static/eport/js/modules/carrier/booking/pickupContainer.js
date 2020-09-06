var PREFIX = ctx + "carrier/booking/detail";

$( document ).ready(function() {
    loadContainerList();
});

// Call when changing block, bay, sztp to search what block, bay and sztp carrier want to get
function loadContainerList() {
    let reqData = {
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
                console.log(result.shipmentDetails);
                loadPostion(result.shipmentDetails);
            } else if (result.code == web_status.WARNING) {
                $.modal.alertWarning(result.msg);
            } else {
                $.modal.alertError(result.msg);
            }
        },
    });
}

function loadPostion(shipmentDetails) {
    let str = '<div class="bayPosition">';
    for (let col = 0; col < 12; col++) {
        str += '<div class="columnDiv">';
        for (let row = 4; row >= 0; row--) {
            if (shipmentDetails[row][col] != null) {

                // Position is empty
                if (shipmentDetails[row][col].containerNo == null) {
                    str += '<div id="cell' + shipmentDetails[row][col].containerNo + '" class="cellDiv" style="background-color: #dbcfcf;>CONT</div>';

                    // Container must be make into an order
                } else {

                    str += '<div style="background-color: #72ecea;" class="cellDiv">' + shipmentDetails[row][col].containerNo + '</div>';

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