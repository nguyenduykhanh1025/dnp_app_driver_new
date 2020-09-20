var currentHeight = $(document).innerHeight() - 50;
$(".main-body").layout();

$(".collapse").click(function () {
  $(".main-body__search-wrapper").hide();
  $(".main-body__search-wrapper--container").hide();
  $(this).hide();
  $(".main-body").height($(document).height() - 10);
  $(".easyui-layout").height($(".main-body").height() - 40);
  $(".uncollapse").show();
  currentHeight = $(document).innerHeight() - 10;
  loadTable();
});

$(".uncollapse").click(function () {
  $(".main-body__search-wrapper").show();
  $(".main-body__search-wrapper--container").show();
  $(this).hide();
  $(".collapse").show();
  currentHeight = $(document).innerHeight() - 50;
  loadTable();
});

function dateformatter(date) {
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  var d = date.getDate();
  return (d < 10 ? "0" + d : d) + "/" + (m < 10 ? "0" + m : m) + "/" + y;
}
function dateparser(s) {
  var ss = s.split(".");
  var d = parseInt(ss[0], 10);
  var m = parseInt(ss[1], 10);
  var y = parseInt(ss[2], 10);
  if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
    return new Date(y, m - 1, d);
  }
}
