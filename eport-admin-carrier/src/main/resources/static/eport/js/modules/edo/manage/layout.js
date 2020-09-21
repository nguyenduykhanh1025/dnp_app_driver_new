var currentHeight = $(document).innerHeight() - 150;

$(".main-body").layout();

$(".collapse").click(function () {
  $(".main-body__search-wrapper").hide();
  $(".main-body__search-wrapper--container").hide();
  $(this).hide();
  $(".uncollapse").show();
  currentHeight = $(document).innerHeight() - 30;
  loadTable();
  loadTableByContainer();
});

$(".uncollapse").click(function () {
  $(".main-body__search-wrapper").show();
  $(".main-body__search-wrapper--container").show();
  $(this).hide();
  $(".collapse").show();
  currentHeight = $(document).innerHeight() - 90;
  loadTable();
  loadTableByContainer();
});

$(".left-side__collapse").click(function () {
  $("#main-layout").layout("collapse", "west");
});
