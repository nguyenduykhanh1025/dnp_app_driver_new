$(".main-body").layout();

$(".collapse").click(function () {
    $(".main-body__search-wrapper").hide();
    $(".main-body__search-wrapper--container").hide();
    $(this).hide();
    $(".main-body").height($(document).height());
    $(".easyui-layout").height($('.main-body').height());
    $(".uncollapse").show();
});

$(".right-side__collapse").click(function () {
  $('#right-layout').layout('collapse', 'south');
  setTimeout(() => {
    hot.updateSettings({height:$('#right-side__main-table').height() - 35});
    hot.render();
  }, 200);
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

$('#right-layout').layout({
  onExpand: function (region) {
    if (region == "south") {
      hot.updateSettings({height:$('#right-side__main-table').height() - 35});
      hot.render();
      let req = {
        shipmentId : shipmentSelected.id
      }
      $.ajax({
        url: ctx + "logistic/comment/update",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(req),
        success: function(res) {
          if (res.code == 0) {
            let commentTitle = '<span>Hỗ Trợ</span> <span class="round-notify-count">0</span>';
            $('#right-layout').layout('panel', 'expandSouth').panel('setTitle', commentTitle);
          }
        }
      });
    }
  }
});
$('#main-layout').layout({
  onExpand: function (region) {
    if (region == "west") {
      hot.render();
    }
  }
});
