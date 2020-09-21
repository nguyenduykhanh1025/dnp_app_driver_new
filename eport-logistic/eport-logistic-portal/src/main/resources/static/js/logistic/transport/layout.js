var currentHeight = $(document).innerHeight() - 50;
$(".main-body").layout();

$(".collapse").click(function () {
  $(".main-body__search-wrapper").hide();
  $(".main-body__search-wrapper--container").hide();
  $(this).hide();
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