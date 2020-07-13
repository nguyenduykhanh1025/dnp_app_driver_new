'use strict';
const PREFIX = ctx + 'om/order/support';
const DOCUMENT_HEIGHT = $(document).height();
var tableQnt = 3;

$(document).ready(function () {
  for (let i = 1; i < tableQnt; i++) {
    let divClone = $('div#dgOrder' + i);
    let totalLabel = divClone.find('.txt-total');
    $(totalLabel).text(formatCurency(123456789) + ' VNĐ ');
    $('#dgOrder' + i + ' table').datagrid({
      height: DOCUMENT_HEIGHT / 2 - 70,
      width: $(document).width() - 50,
      singleSelect: true,
      clientPaging: false,
      pagination: false,
      rownumbers: true,
      nowrap: false,
      striped: true,
      loader: function (param, success, error) {
        success(orderList);
      },
    });
    let currentId = i + 1;
    let clonned = divClone.clone().prop('id', 'dgOrder' + currentId);
    divClone.after(clonned);
  }
 
  $('.content').height(DOCUMENT_HEIGHT - $('.header').height() - 20);
});

function formatCurency(x) {
  return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}

