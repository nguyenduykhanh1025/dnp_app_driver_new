$(window).scroll(function() {
  var height = $(window).scrollTop();

  if (height  > 90) {
    $('.menu').addClass('scroller');
  } else {
    $('.menu').removeClass('scroller');
  }
});

$(document).ready(function() {
  $(".youtube-link").grtyoutube({
    autoPlay:true
  });

  $('.intro__content--main-text').height($(window).height() - $('.intro__header').height());
})