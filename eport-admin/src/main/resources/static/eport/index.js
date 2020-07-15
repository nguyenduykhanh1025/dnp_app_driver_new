/**
 * Home Method Encapsulation
 */
layer.config({
  extend: "moon/style.css",
  skin: "layer-ext-moon",
});

var isMobile = $.common.isMobile() || $(window).width() < 769;

$(function () {
  // MetsiMenu
  $("#side-menu").metisMenu();
  $(".nav-third-level").addClass("collapse");
  // Fixed menu bar
  $(function () {
    $(".sidebar-collapse").slimScroll({
      height: "96%",
      railOpacity: 0.9,
      alwaysVisible: false,
    });
  });

  // Menu switch
  $(".navbar-minimalize").click(function () {
    if (isMobile) {
      $("body").toggleClass("canvas-menu");
    } else {
      $("body").toggleClass("mini-navbar");
    }
    SmoothlyMenu();
  });

  $("#side-menu>li").click(function () {
    if ($("body").hasClass("canvas-menu mini-navbar")) {
      NavToggle();
    }
  });
  $("#side-menu>li li a:not(:has(span))").click(function () {
    if ($(window).width() < 769) {
      NavToggle();
    }
  });

  $(".nav-close").click(NavToggle);

  //IOS browser compatibility processing
  if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
    $("#content-main").css("overflow-y", "auto");
  }
});

$(window).bind("load resize", function () {
  if ($(this).width() < 769) {
    $("body").addClass("canvas-menu");
    $("nav .logo").addClass("hide");
    $(".slimScrollDiv").css({ overflow: "hidden" });
  }
});

function NavToggle() {
  $(".navbar-minimalize").trigger("click");
}

function fixedSidebar() {
  $("#side-menu").hide();
  $("nav .logo").addClass("hide");
  setTimeout(function () {
    $("#side-menu").fadeIn(500);
  }, 100);
}

function SmoothlyMenu() {
  if (isMobile && !$("body").hasClass("canvas-menu")) {
    $(".navbar-static-side").fadeIn();
    fixedSidebar();
  } else if (!isMobile && !$("body").hasClass("mini-navbar")) {
    fixedSidebar();
    $("nav .logo").removeClass("hide");
  } else if (isMobile && $("body").hasClass("fixed-sidebar")) {
    $(".navbar-static-side").fadeOut();
    fixedSidebar();
  } else if (!isMobile && $("body").hasClass("fixed-sidebar")) {
    fixedSidebar();
  } else {
    $("#side-menu").removeAttr("style");
  }
}

/**
 * iframe processing
 */
$(function () {
  //Calculate the total width of the set of elements
  function calSumWidth(elements) {
    var width = 0;
    $(elements).each(function () {
      width += $(this).outerWidth(true);
    });
    return width;
  }

  // Activate the specified tab
  function setActiveTab(element) {
    if (!$(element).hasClass("active")) {
      var currentId = $(element).data("id");
      // Display the content area corresponding to the tab
      $(".eport_iframe").each(function () {
        if ($(this).data("id") == currentId) {
          $(this).show().siblings(".eport_iframe").hide();
        }
      });
      $(element).addClass("active").siblings(".menuTab").removeClass("active");
      scrollToTab(element);
    }
  }

  //Scroll to the specified tab
  function scrollToTab(element) {
    var marginLeftVal = calSumWidth($(element).prevAll()),
      marginRightVal = calSumWidth($(element).nextAll());
    // Viewable area non-tab width
    var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".menuTabs"));
    //Viewable area tab width
    var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
    //Actual scroll width
    var scrollVal = 0;
    if ($(".page-tabs-content").outerWidth() < visibleWidth) {
      scrollVal = 0;
    } else if (marginRightVal <= visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true)) {
      if (visibleWidth - $(element).next().outerWidth(true) > marginRightVal) {
        scrollVal = marginLeftVal;
        var tabElement = element;
        while (scrollVal - $(tabElement).outerWidth() > $(".page-tabs-content").outerWidth() - visibleWidth) {
          scrollVal -= $(tabElement).prev().outerWidth();
          tabElement = $(tabElement).prev();
        }
      }
    } else if (marginLeftVal > visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true)) {
      scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
    }
    $(".page-tabs-content").animate(
      {
        marginLeft: 0 - scrollVal + "px",
      },
      "fast"
    );
  }

  //See the hidden tabs on the left
  function scrollTabLeft() {
    var marginLeftVal = Math.abs(parseInt($(".page-tabs-content").css("margin-left")));
    // Viewable area non-tab width
    var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".menuTabs"));
    //Viewable area tab width
    var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
    //Actual scroll width
    var scrollVal = 0;
    if ($(".page-tabs-content").width() < visibleWidth) {
      return false;
    } else {
      var tabElement = $(".menuTab:first");
      var offsetVal = 0;
      while (offsetVal + $(tabElement).outerWidth(true) <= marginLeftVal) {
        //Find the element closest to the current tab
        offsetVal += $(tabElement).outerWidth(true);
        tabElement = $(tabElement).next();
      }
      offsetVal = 0;
      if (calSumWidth($(tabElement).prevAll()) > visibleWidth) {
        while (offsetVal + $(tabElement).outerWidth(true) < visibleWidth && tabElement.length > 0) {
          offsetVal += $(tabElement).outerWidth(true);
          tabElement = $(tabElement).prev();
        }
        scrollVal = calSumWidth($(tabElement).prevAll());
      }
    }
    $(".page-tabs-content").animate(
      {
        marginLeft: 0 - scrollVal + "px",
      },
      "fast"
    );
  }

  //See the hidden tabs on the right
  function scrollTabRight() {
    var marginLeftVal = Math.abs(parseInt($(".page-tabs-content").css("margin-left")));
    // Viewable area non-tab width
    var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".menuTabs"));
    //Viewable area tab width
    var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
    //Actual scroll width
    var scrollVal = 0;
    if ($(".page-tabs-content").width() < visibleWidth) {
      return false;
    } else {
      var tabElement = $(".menuTab:first");
      var offsetVal = 0;
      while (offsetVal + $(tabElement).outerWidth(true) <= marginLeftVal) {
        //Find the element closest to the current tab
        offsetVal += $(tabElement).outerWidth(true);
        tabElement = $(tabElement).next();
      }
      offsetVal = 0;
      while (offsetVal + $(tabElement).outerWidth(true) < visibleWidth && tabElement.length > 0) {
        offsetVal += $(tabElement).outerWidth(true);
        tabElement = $(tabElement).next();
      }
      scrollVal = calSumWidth($(tabElement).prevAll());
      if (scrollVal > 0) {
        $(".page-tabs-content").animate(
          {
            marginLeft: 0 - scrollVal + "px",
          },
          "fast"
        );
      }
    }
  }

  //Add data-index attribute to menu item by traversing
  $(".menuItem").each(function (index) {
    if (!$(this).attr("data-index")) {
      $(this).attr("data-index", index);
    }
  });

  function menuItem() {
    // Get identification data
    var dataUrl = $(this).attr("href"),
      dataIndex = $(this).data("index"),
      menuName = $.trim($(this).text()),
      flag = true;
    $(".nav ul li, .nav li").removeClass("selected");
    $(this).parent("li").addClass("selected");
    setIframeUrl($(this).attr("href"));
    if (dataUrl == undefined || $.trim(dataUrl).length == 0) return false;

    // Tab menu already exists
    $(".menuTab").each(function () {
      if ($(this).data("id") == dataUrl) {
        if (!$(this).hasClass("active")) {
          $(this).addClass("active").siblings(".menuTab").removeClass("active");
          scrollToTab(this);
          // Display the content area corresponding to the tab
          $(".mainContent .eport_iframe").each(function () {
            if ($(this).data("id") == dataUrl) {
              $(this).show().siblings(".eport_iframe").hide();
              return false;
            }
          });
        }
        flag = false;
        return false;
      }
    });
    // Tab menu does not exist
    if (flag) {
      var str = '<a href="javascript:;" class="active menuTab" data-id="' + dataUrl + '">' + menuName + ' <i class="fa fa-times-circle"></i></a>';
      $(".menuTab").removeClass("active");

      var str1 = '<iframe class="eport_iframe" name="iframe' + dataIndex + '" width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless></iframe>';
      $(".mainContent").find("iframe.eport_iframe").hide().parents(".mainContent").append(str1);

      $.modal.loading(" Đang xử lý...");

      $(".mainContent iframe:visible").load(function () {
        $.modal.closeLoading();
      });

      $(".menuTabs .page-tabs-content").append(str);
      scrollToTab($(".menuTab.active"));
    }
    return false;
  }

  function menuBlank() {
    var dataUrl = $(this).attr("href");
    window.open(dataUrl);
    return false;
  }

  $(".menuItem").on("click", menuItem);

  $(".menuBlank").on("click", menuBlank);

  function closeTab() {
    var closeTabId = $(this).parents(".menuTab").data("id");
    var currentWidth = $(this).parents(".menuTab").width();
    var panelUrl = $(this).parents(".menuTab").data("panel");
    if ($(this).parents(".menuTab").hasClass("active")) {
      if ($(this).parents(".menuTab").next(".menuTab").size()) {
        var activeId = $(this).parents(".menuTab").next(".menuTab:eq(0)").data("id");
        $(this).parents(".menuTab").next(".menuTab:eq(0)").addClass("active");

        $(".mainContent .eport_iframe").each(function () {
          if ($(this).data("id") == activeId) {
            $(this).show().siblings(".eport_iframe").hide();
            return false;
          }
        });

        var marginLeftVal = parseInt($(".page-tabs-content").css("margin-left"));
        if (marginLeftVal < 0) {
          $(".page-tabs-content").animate(
            {
              marginLeft: marginLeftVal + currentWidth + "px",
            },
            "fast"
          );
        }

        $(this).parents(".menuTab").remove();

        $(".mainContent .eport_iframe").each(function () {
          if ($(this).data("id") == closeTabId) {
            $(this).remove();
            return false;
          }
        });
      }

      if ($(this).parents(".menuTab").prev(".menuTab").size()) {
        var activeId = $(this).parents(".menuTab").prev(".menuTab:last").data("id");
        $(this).parents(".menuTab").prev(".menuTab:last").addClass("active");
        $(".mainContent .eport_iframe").each(function () {
          if ($(this).data("id") == activeId) {
            $(this).show().siblings(".eport_iframe").hide();
            return false;
          }
        });

        $(this).parents(".menuTab").remove();

        $(".mainContent .eport_iframe").each(function () {
          if ($(this).data("id") == closeTabId) {
            $(this).remove();
            return false;
          }
        });

        if ($.common.isNotEmpty(panelUrl)) {
          $('.menuTab[data-id="' + panelUrl + '"]')
            .addClass("active")
            .siblings(".menuTab")
            .removeClass("active");
          $(".mainContent .eport_iframe").each(function () {
            if ($(this).data("id") == panelUrl) {
              $(this).show().siblings(".eport_iframe").hide();
              return false;
            }
          });
        }
      }
    } else {
      $(this).parents(".menuTab").remove();

      $(".mainContent .eport_iframe").each(function () {
        if ($(this).data("id") == closeTabId) {
          $(this).remove();
          return false;
        }
      });
    }
    scrollToTab($(".menuTab.active"));
    return false;
  }

  $(".menuTabs").on("click", ".menuTab i", closeTab);

  function showActiveTab() {
    scrollToTab($(".menuTab.active"));
  }
  $(".tabShowActive").on("click", showActiveTab);

  function activeTab() {
    if (!$(this).hasClass("active")) {
      var currentId = $(this).data("id");
      $(".mainContent .eport_iframe").each(function () {
        if ($(this).data("id") == currentId) {
          $(this).show().siblings(".eport_iframe").hide();
          return false;
        }
      });
      $(this).addClass("active").siblings(".menuTab").removeClass("active");
      scrollToTab(this);
    }
  }

  $(".menuTabs").on("click", ".menuTab", activeTab);

  function refreshTab() {
    var currentId = $(".page-tabs-content").find(".active").attr("data-id");
    var target = $('.eport_iframe[data-id="' + currentId + '"]');
    var url = target.attr("src");
    target.attr("src", url).ready();
  }

  function fullScreenTab() {
    var currentId = $(".page-tabs-content").find(".active").attr("data-id");
    var target = $('.eport_iframe[data-id="' + currentId + '"]');
    target.fullScreen(true);
  }

  function tabCloseCurrent() {
    $(".page-tabs-content").find(".active i").trigger("click");
  }

  function tabCloseOther() {
    $(".page-tabs-content")
      .children("[data-id]")
      .not(":first")
      .not(".active")
      .each(function () {
        $('.eport_iframe[data-id="' + $(this).data("id") + '"]').remove();
        $(this).remove();
      });
    $(".page-tabs-content").css("margin-left", "0");
  }

  function tabCloseAll() {
    $(".page-tabs-content")
      .children("[data-id]")
      .not(":first")
      .each(function () {
        $('.eport_iframe[data-id="' + $(this).data("id") + '"]').remove();
        $(this).remove();
      });
    $(".page-tabs-content")
      .children("[data-id]:first")
      .each(function () {
        $('.eport_iframe[data-id="' + $(this).data("id") + '"]').show();
        $(this).addClass("active");
      });
    $(".page-tabs-content").css("margin-left", "0");
  }

  $("#fullScreen").on("click", function () {
    $(document).toggleFullScreen();
  });

  $(".tabReload").on("click", refreshTab);

  $(".tabFullScreen").on("click", fullScreenTab);

  $(".menuTabs").on("dblclick", ".menuTab", activeTabMax);

  $(".tabLeft").on("click", scrollTabLeft);

  $(".tabRight").on("click", scrollTabRight);

  $(".tabCloseCurrent").on("click", tabCloseCurrent);

  $(".tabCloseOther").on("click", tabCloseOther);

  $(".tabCloseAll").on("click", tabCloseAll);

  $(".tabMaxCurrent").on("click", function () {
    $(".page-tabs-content").find(".active").trigger("dblclick");
  });

  $("#ax_close_max").click(function () {
    $("#content-main").toggleClass("max");
    $("#ax_close_max").hide();
  });

  function activeTabMax() {
    $("#content-main").toggleClass("max");
    $("#ax_close_max").show();
  }

  function setIframeUrl(href) {
    if ($.common.equals("history", mode)) {
      storage.set("publicPath", href);
    } else {
      var nowUrl = window.location.href;
      var newUrl = nowUrl.substring(0, nowUrl.indexOf("#"));
      window.location.href = newUrl + "#" + href;
    }
  }

  $(window).keydown(function (event) {
    if (event.keyCode == 27) {
      $("#content-main").removeClass("max");
      $("#ax_close_max").hide();
    }
  });

  window.onhashchange = function () {
    var hash = location.hash;
    var url = hash.substring(1, hash.length);
    $('a[href$="' + url + '"]').click();
  };

  $.contextMenu({
    selector: ".menuTab",
    trigger: "right",
    autoHide: true,
    items: {
      close_current: {
        name: "Đóng Tab",
        icon: "fa-close",
        callback: function (key, opt) {
          opt.$trigger.find("i").trigger("click");
        },
      },
      close_other: {
        name: "Đóng Tab Khác",
        icon: "fa-window-close-o",
        callback: function (key, opt) {
          setActiveTab(this);
          tabCloseOther();
        },
      },
      close_left: {
        name: "Đóng Tab Bên Trái",
        icon: "fa-reply",
        callback: function (key, opt) {
          setActiveTab(this);
          this.prevAll(".menuTab")
            .not(":last")
            .each(function () {
              if ($(this).hasClass("active")) {
                setActiveTab(this);
              }
              $('.eport_iframe[data-id="' + $(this).data("id") + '"]').remove();
              $(this).remove();
            });
          $(".page-tabs-content").css("margin-left", "0");
        },
      },
      close_right: {
        name: "Đóng Tab Bên Phải",
        icon: "fa-share",
        callback: function (key, opt) {
          setActiveTab(this);
          this.nextAll(".menuTab").each(function () {
            $('.menuTab[data-id="' + $(this).data("id") + '"]').remove();
            $(this).remove();
          });
        },
      },
      close_all: {
        name: "Đóng Tất Cả",
        icon: "fa-window-close",
        callback: function (key, opt) {
          tabCloseAll();
        },
      },
      step: "---------",
      full: {
        name: "Toàn Màn Hình",
        icon: "fa-arrows-alt",
        callback: function (key, opt) {
          setActiveTab(this);
          var target = $('.eport_iframe[data-id="' + this.data("id") + '"]');
          target.fullScreen(true);
        },
      },
      refresh: {
        name: "Refresh",
        icon: "fa-refresh",
        callback: function (key, opt) {
          setActiveTab(this);
          var target = $('.eport_iframe[data-id="' + this.data("id") + '"]');
          var url = target.attr("src");
          $.modal.loading(" Đang xử lý...");
          target.attr("src", url).load(function () {
            $.modal.closeLoading();
          });
        },
      },
      open: {
        name: "Mở Cửa Sổ Mới",
        icon: "fa-link",
        callback: function (key, opt) {
          var target = $('.eport_iframe[data-id="' + this.data("id") + '"]');
          window.open(target.attr("src"));
        },
      },
    },
  });
});
