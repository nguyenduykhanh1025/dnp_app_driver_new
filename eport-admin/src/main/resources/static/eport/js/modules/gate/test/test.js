const PREFIX = ctx + "gate/test"

$(".main-body").layout();

$(".left-side__collapse").click(function () {
  $("#main-layout").layout("collapse", "west");
  setTimeout(() => {
    hot.render();
  }, 200);
});