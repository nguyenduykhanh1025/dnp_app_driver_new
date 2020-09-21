var currentHeight = $(document).innerHeight() - 150;
$(".main-body").layout();

$(".collapse").click(function () {
    $(".main-body__search-wrapper").hide();
    $(".main-body__search-wrapper--container").hide();
    $(this).hide();
    $(".uncollapse").show();
    currentHeight = $(document).innerHeight() - 40;
    loadTable();
    setTimeout(() => {
      hot.updateSettings({ height: currentHeight });
      hot.render();
    }, 200);
});

$(".uncollapse").click(function () {
    $(".main-body__search-wrapper").show();
    $(".main-body__search-wrapper--container").show();
    $(this).hide();
    $(".collapse").show();
    currentHeight = $(document).innerHeight() - 70;
    loadTable();
    setTimeout(() => {
      hot.updateSettings({ height: currentHeight });
      hot.render();
    }, 200);
});

$(".left-side__collapse").click(function () {
  $("#main-layout").layout("collapse", "west");
});

$(".right-side__collapse").click(function () {
  $("#right-layout").layout("collapse", "south");
  setTimeout(() => {
    hot.updateSettings({ height: $("#right-side__main-table").height() - 35 });
    hot.render();
  }, 200);
});

$("#right-layout").layout({
  onExpand: function (region) {
    if (region == "south") {
      hot.updateSettings({ height: $("#right-side__main-table").height() - 35 });
      hot.render();
    }
  },
});

$("#right-layout").layout("collapse", "south");
setTimeout(() => {
  hot.updateSettings({ height: $("#right-side__main-table").height() - 35 });
  hot.render();
}, 200);
