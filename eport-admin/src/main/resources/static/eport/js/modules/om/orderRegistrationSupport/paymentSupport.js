'use strict';
const PREFIX = ctx + 'om/order/support';
const DOCUMENT_HEIGHT = $(document).height();
var matrixBill = [];

$(document).ready(function () {

  // CHECK BILL LIST NOT NULL OR EMPTY
  if (billList != null && billList.length > 0) {
    let bill = billList[0];

    // SET HEADER INFO
    $('#batchCode').text(bill.shipmentId);
    $('#taxCode').text(bill.processOrder.taxCode);
    switch (bill.serviceType) {
      case 1:
        $('#serviceType').text("Bốc Hàng");
        break;
      case 2:
        $('#serviceType').text("Hạ Rỗng");
        break;
      case 3:
        $('#serviceType').text("Bốc Rỗng");
        break;
      case 4:
        $('#serviceType').text("Hạ Hàng");
        break;
    }
    $('#blNo').text(bill.processOrder.blNo);
    $('#bookingNo').text(bill.processOrder.bookingNo);
    $('#vessel').text(bill.processOrder.vessel);
    $('#voyage').text(bill.processOrder.voyage);

    let invoiceNo = bill.referenceNo;

    // PAYMENT STATUS
    let paymentStatus = bill.paymentStatus;

    // NUMBER OF BILL
    let count = 1;

    // STORE CURRENT BILL
    let dataList = [];

    // TOTAL OF BILL
    let total = 0;

    // CURRENT PROCESS ORDER ID
    let processOrderId = bill.processOrderId;

    // START LOADING DATA
    $.modal.loading("Đang xử lý...");
    for (let i=0; i<billList.length; i++) {
      if (processOrderId != billList[i].processOrderId) {
        matrixBill.push(dataList);
        let divClone = $('div#dgOrder' + count);
        let clonned = divClone.clone().prop('id', 'dgOrder' + (count + 1));
        let totalLabel = clonned.find('.txt-total');
        clonned.find(".btn-payment").attr("id", "paymentButton"+(count+1))
        .attr('onclick', 'paymentHandle(' + processOrderId + ', "' + paymentStatus + '", ' + (count + 1) + ')');
        clonned.find(".bill-title").attr("id", "billTitle" + (count+1))
        .text('Invoice No: ' + invoiceNo + ' Mã Lệnh: ' + processOrderId + ' Trạng Thái: ' + (paymentStatus=='Y'?'Đã thanh toán':'Chưa thanh toán'));
        $(totalLabel).text(formatMoney(total) + ' VND ');
        divClone.after(clonned);
        $('#dgOrder' + (count + 1) + ' table').datagrid({
          height: DOCUMENT_HEIGHT / 2 - 70,
          singleSelect: true,
          clientPaging: false,
          pagination: false,
          rownumbers: true,
          nowrap: false,
          striped: true,
          loader: function (param, success, error) {
            success(dataList);
          },
        });
        
        // REFRESH DATA TO STORE NEW BILL
        dataList = [];
        dataList.push(billList[i]);
        processOrderId = billList[i].processOrderId;
        total = 0;
        total += billList[i].vatAfterFee;
        paymentStatus = billList[i].paymentStatus;
        invoiceNo = billList[i].referenceNo;
        count++;
      } else {
        dataList.push(billList[i]);
        total += billList[i].vatAfterFee;
      }
    }
    // LOAD THE LAST BILL
    matrixBill.push(dataList);
    let divClone = $('div#dgOrder' + count);
    let clonned = divClone.clone().prop('id', 'dgOrder' + (count + 1));
    let totalLabel = clonned.find('.txt-total');
    clonned.find(".btn-payment").attr("id", "paymentButton" + (count+1))
    .attr('onclick', 'paymentHandle(' + processOrderId + ', "' + paymentStatus + '", ' + (count + 1) + ')');
    clonned.find(".bill-title").attr("id", "billTitle" + (count+1))
    .text('Invoice No: ' + invoiceNo + ' Mã Lệnh: ' + processOrderId + ' Trạng Thái: ' + (paymentStatus=='Y'?'Đã thanh toán':'Chưa thanh toán'));
    $(totalLabel).text(formatMoney(total) + ' VND ');
    divClone.after(clonned);
    console.log("start");
    $('#dgOrder' + (count + 1)   + ' table').datagrid({
      height: DOCUMENT_HEIGHT / 2 - 70,
      singleSelect: true,
      clientPaging: false,
      pagination: false,
      rownumbers: true,
      nowrap: false,
      striped: true,
      loader: function (param, success, error) {
        success(dataList);
        console.log("ok");
      },
    });
    console.log("end");
    // FINISH LOADING DATA
    $.modal.closeLoading();
  } else {
    layer.confirm("Lô này hiện tại không có bill nào để hiển thị.", {
      icon: 3,
      title: "Xác Nhận",
      btn: ['Xác nhận']
    }, function () {
      $.modal.close();
    });
  }
});

function formatMoney(value) {
  return value.format(2, 3, ',', '.');
}

Number.prototype.format = function(n, x, s, c) {
  var re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\D' : '$') + ')',
      num = this.toFixed(Math.max(0, ~~n));

  return (c ? num.replace('.', c) : num).replace(new RegExp(re, 'g'), '$&' + (s || ','));
};

function paymentHandle(processOrderId, paymentStatus, index) {
  let bills = matrixBill[index-2];
  if ('Y' == paymentStatus) {
    $.modal.alertError('Bill này đã được thanh toán.');
  } else {
    layer.confirm("Xác nhận bill này đã thanh toán.", {
      icon: 3,
      title: "Xác Nhận",
      btn: ['Đồng Ý', 'Hủy Bỏ']
    }, function () {
      $.modal.loading("Đang xử lý...");
      $.ajax({
        url: PREFIX + "/payment",
        method: "GET",
        data: {
          processOrderId: processOrderId
        }
      }).done(function (res) {
        if (res.code == 0) {
          for (let i=0; i<bills.length; i++) {
            bills[i].paymentStatus = 'Y';
          }
          $("#billTitle"+index).text('Invoice No: ' + bills[0].referenceNo + ' Mã Lệnh: ' + processOrderId + ' Trạng Thái: ' + (bills[0].paymentStatus=='Y'?'Đã thanh toán':'Chưa thanh toán'));
          $("#paymentButton"+index).attr('onclick', 'paymentHandle(' + processOrderId + ', "Y", ' + index + ')');
          $('#dgOrder' + index + ' table').datagrid({
            height: DOCUMENT_HEIGHT / 2 - 70,
            width: $(document).width() - 50,
            singleSelect: true,
            clientPaging: false,
            pagination: false,
            rownumbers: true,
            nowrap: false,
            striped: true,
            loader: function (param, success, error) {
              success(bills);
            },
          });
        }
        $.modal.closeLoading();
        $.modal.msgSuccess(res.msg);
      });
    }, function () {
      // DO NOTHING
    });
  }
}

function closeForm() {
  $.modal.close();
}
