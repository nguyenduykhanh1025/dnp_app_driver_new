$(window).scroll(function() {
  var height = $(window).scrollTop();

  if (height  > 90) {
    $('.menu').addClass('scroller');
  } else {
    $('.menu').removeClass('scroller');
  }
});