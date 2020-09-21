$(function() {
	
	if (window.layer !== undefined) {
		layer.config({
		    extend: 'moon/style.css',
		    skin: 'layer-ext-moon'
		});
	}
	
	if ($.fn.toTop !== undefined) {
		$('#scroll-up').toTop();
	}
	
	if ($.fn.select2 !== undefined) {
        $.fn.select2.defaults.set( "theme", "bootstrap" );
		$("select.form-control:not(.noselect2)").each(function () {
			$(this).select2().on("change", function () {
				$(this).valid();
			})
		})
	}
	
	if ($.fn.iCheck !== undefined) {
		$(".check-box:not(.noicheck),.radio-box:not(.noicheck)").each(function() {
            $(this).iCheck({
                checkboxClass: 'icheckbox-blue',
                radioClass: 'iradio-blue',
            })
        })
	}
	
	// 气泡弹出框特效（移到元素时）
	$(document).on("mouseenter", '.table [data-toggle="popover"]', function() {
		var _this = this;
		$(this).popover("show");
		$(".popover").on("mouseleave", function() {
			$(_this).popover('hide');
		});
	})

	// 气泡弹出框特效（离开元素时）
	$(document).on("mouseleave", '.table [data-toggle="popover"]', function() {
		var _this = this;
		setTimeout(function() {
			if (!$(".popover:hover").length) $(_this).popover("hide");
		}, 100);
	});
	
	// 取消回车自动提交表单
	$(document).on("keypress", ":input:not(textarea):not([type=submit])", function(event) {
        if (event.keyCode == 13) {
            event.preventDefault();
        }
    });
	 
	if ($(".select-time").length > 0) {
		layui.use('laydate', function() {
		    var laydate = layui.laydate;
		    var startDate = laydate.render({
		        elem: '#startTime',
		        max: $('#endTime').val(),
		        theme: 'molv',
		        trigger: 'click',
		        done: function(value, date) {
		            if (value !== '') {
		                endDate.config.min.year = date.year;
		                endDate.config.min.month = date.month - 1;
		                endDate.config.min.date = date.date;
		            } else {
		                endDate.config.min.year = '';
		                endDate.config.min.month = '';
		                endDate.config.min.date = '';
		            }
		        }
		    });
		    var endDate = laydate.render({
		        elem: '#endTime',
		        min: $('#startTime').val(),
		        theme: 'molv',
		        trigger: 'click',
		        done: function(value, date) {
		            if (value !== '') {
		                startDate.config.max.year = date.year;
		                startDate.config.max.month = date.month - 1;
		                startDate.config.max.date = date.date;
		            } else {
		                startDate.config.max.year = '2099';
		                startDate.config.max.month = '12';
		                startDate.config.max.date = '31';
		            }
		        }
		    });
		});
	}
	if ($(".time-input").length > 0) {
		layui.use('laydate', function () {
			var com = layui.laydate;
			$(".time-input").each(function (index, item) {
				var time = $(item);
				var type = time.attr("data-type") || 'date';
				var format = time.attr("data-format") || 'yyyy-MM-dd';
				var buttons = time.attr("data-btn") || 'clear|now|confirm', newBtnArr = [];
				var callback = time.attr("data-callback") || {};
				if (buttons) {
					if (buttons.indexOf("|") > 0) {
						var btnArr = buttons.split("|"), btnLen = btnArr.length;
						for (var j = 0; j < btnLen; j++) {
							if ("clear" === btnArr[j] || "now" === btnArr[j] || "confirm" === btnArr[j]) {
								newBtnArr.push(btnArr[j]);
							}
						}
					} else {
						if ("clear" === buttons || "now" === buttons || "confirm" === buttons) {
							newBtnArr.push(buttons);
						}
					}
				} else {
					newBtnArr = ['clear', 'now', 'confirm'];
				}
				com.render({
					elem: item,
					theme: 'molv',
					trigger: 'click',
					type: type,
					format: format,
					btns: newBtnArr,
					done: function (value, data) {
						if (typeof window[callback] != 'undefined'
							&& window[callback] instanceof Function) {
							window[callback](value, data);
						}
					}
				});
			});
		});
	}
	if ($("#keyword").length > 0) {
		$("#keyword").bind("focus", function focusKey(e) {
		    if ($("#keyword").hasClass("empty")) {
		        $("#keyword").removeClass("empty");
		    }
		}).bind("blur", function blurKey(e) {
		    if ($("#keyword").val() === "") {
		        $("#keyword").addClass("empty");
		    }
		    $.tree.searchNode(e);
		}).bind("input propertychange", $.tree.searchNode);
	}
	var expandFlag;
	$("#expandAllBtn").click(function() {
		var dataExpand = $.common.isEmpty(table.options.expandAll) ? true : table.options.expandAll;
		expandFlag = $.common.isEmpty(expandFlag) ? dataExpand : expandFlag;
	    if (!expandFlag) {
	    	$.bttTable.bootstrapTreeTable('expandAll');
	    } else {
	    	$.bttTable.bootstrapTreeTable('collapseAll');
	    }
	    expandFlag = expandFlag ? false: true;
	})
	$('body', document).on('keyup', function(e) {
	    if (e.which === 27) {
	        $.modal.closeAll();
	    }
	});
});

(function ($) {
    'use strict';
    $.fn.toTop = function(opt) {
        var elem = this;
        var win = $(window);
        var doc = $('html, body');
        var options = $.extend({
            autohide: true,
            offset: 50,
            speed: 500,
            position: true,
            right: 15,
            bottom: 5
        }, opt);
        elem.css({
            'cursor': 'pointer'
        });
        if (options.autohide) {
            elem.css('display', 'none');
        }
        if (options.position) {
            elem.css({
                'position': 'fixed',
                'right': options.right,
                'bottom': options.bottom,
            });
        }
        elem.click(function() {
            doc.animate({
                scrollTop: 0
            }, options.speed);
        });
        win.scroll(function() {
            var scrolling = win.scrollTop();
            if (options.autohide) {
                if (scrolling > options.offset) {
                    elem.fadeIn(options.speed);
                } else elem.fadeOut(options.speed);
            }
        });
    };
})(jQuery);

var refreshItem = function(){
    var topWindow = $(window.parent.document);
	var currentId = $('.page-tabs-content', topWindow).find('.active').attr('data-id');
	var target = $('.eport_iframe[data-id="' + currentId + '"]', topWindow);
    var url = target.attr('src');
    target.attr('src', url).ready();
}

var closeItem = function(dataId){
	var topWindow = $(window.parent.document);
	if($.common.isNotEmpty(dataId)){
		window.parent.$.modal.closeLoading();
		$('.menuTab[data-id="' + dataId + '"]', topWindow).remove();
		$('.mainContent .eport_iframe[data-id="' + dataId + '"]', topWindow).remove();
		return;
	}
	var panelUrl = window.frameElement.getAttribute('data-panel');
	$('.page-tabs-content .active i', topWindow).click();
	if($.common.isNotEmpty(panelUrl)){
		$('.menuTab[data-id="' + panelUrl + '"]', topWindow).addClass('active').siblings('.menuTab').removeClass('active');
		$('.mainContent .eport_iframe', topWindow).each(function() {
            if ($(this).data('id') == panelUrl) {
                $(this).show().siblings('.eport_iframe').hide();
                return false;
            }
		});
	}
}

function createMenuItem(dataUrl, menuName) {
	var panelUrl = window.frameElement.getAttribute('data-id');
    dataIndex = $.common.random(1,100),
    flag = true;
    if (dataUrl == undefined || $.trim(dataUrl).length == 0) return false;
    var topWindow = $(window.parent.document);
    $('.menuTab', topWindow).each(function() {
        if ($(this).data('id') == dataUrl) {
            if (!$(this).hasClass('active')) {
                $(this).addClass('active').siblings('.menuTab').removeClass('active');
                $('.page-tabs-content').animate({ marginLeft: ""}, "fast");
                $('.mainContent .eport_iframe', topWindow).each(function() {
                    if ($(this).data('id') == dataUrl) {
                        $(this).show().siblings('.eport_iframe').hide();
                        return false;
                    }
                });
            }
            flag = false;
            return false;
        }
    });
    if (flag) {
        var str = '<a href="javascript:;" class="active menuTab" data-id="' + dataUrl + '" data-panel="' + panelUrl + '">' + menuName + ' <i class="fa fa-times-circle"></i></a>';
        $('.menuTab', topWindow).removeClass('active');

        var str1 = '<iframe class="eport_iframe" name="iframe' + dataIndex + '" width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" data-panel="' + panelUrl + '" seamless></iframe>';
        $('.mainContent', topWindow).find('iframe.eport_iframe').hide().parents('.mainContent').append(str1);
        
        window.parent.$.modal.loading(" Đang xử lý...");
        $('.mainContent iframe:visible', topWindow).load(function () {
        	window.parent.$.modal.closeLoading();
        });

        $('.menuTabs .page-tabs-content', topWindow).append(str);
    }
    return false;
}

var log = {
    log: function(msg) {
        console.log(msg);
    },
    info: function(msg) {
        console.info(msg);
    },
    warn: function(msg) {
        console.warn(msg);
    },
    error: function(msg) {
        console.error(msg);
    }
};

var storage = {
    set: function(key, value) {
        window.localStorage.setItem(key, value);
    },
    get: function(key) {
        return window.localStorage.getItem(key);
    },
    remove: function(key) {
        window.localStorage.removeItem(key);
    },
    clear: function() {
        window.localStorage.clear();
    }
};

var sub = {
    editColumn: function() {
    	var count = $("#" + table.options.id).bootstrapTable('getData').length;
    	var params = new Array();
    	for (var dataIndex = 0; dataIndex <= count; dataIndex++) {
    		var columns = $('#' + table.options.id + ' tr[data-index="' + dataIndex + '"] td');
    		var obj = new Object();
    		for (var i = 0; i < columns.length; i++) {
    			var inputValue = $(columns[i]).find('input');
    			var selectValue = $(columns[i]).find('select');
    			var key = table.options.columns[i].field;
    			if ($.common.isNotEmpty(inputValue.val())) {
    				obj[key] = inputValue.val();
    			} else if ($.common.isNotEmpty(selectValue.val())) {
    				obj[key] = selectValue.val();
    			} else {
    				obj[key] = "";
    			}
    		}
    		params.push({ index: dataIndex, row: obj });
    	}
    	$("#" + table.options.id).bootstrapTable("updateRow", params);
    },
    delColumn: function(column) {
    	sub.editColumn();
    	var subColumn = $.common.isEmpty(column) ? "index" : column;
    	var ids = $.table.selectColumns(subColumn);
        if (ids.length == 0) {
            $.modal.alertWarning("Please select at least one record");
            return;
        }
        $("#" + table.options.id).bootstrapTable('remove', { field: subColumn, values: ids });
    }
};

$.ajaxSetup({
    complete: function(XMLHttpRequest, textStatus) {
        if (textStatus == 'timeout') {
        	$.modal.alertWarning("Hết thời gian chờ server, hãy thử lại!");
        	$.modal.enable();
            $.modal.closeLoading();
        } else if (textStatus == "parsererror" || textStatus == "error") {
        	$.modal.alertWarning("Có lỗi khi gửi yêu cầu đến server, vui lòng thử lại.");
        	$.modal.enable();
            $.modal.closeLoading();
        }
    }
});

/**
 * Resize image when upload
 * 
 * @param files
 * @returns
 */
function summernoteOnImageUpload(files, editor) {
    $.each(files, function(idx, file) {
        var max_width = 1500;
        var max_height = 1500;
        var reader = new FileReader();
        reader.onload = function() {
            var tmpImg = new Image();
            tmpImg.src = reader.result;

            tmpImg.onload = function() {
                var tmpW = tmpImg.width;
                var tmpH = tmpImg.height;

                if (tmpW > tmpH) {
                    if (tmpW > max_width) {
                       tmpH *= max_width / tmpW;
                       tmpW = max_width;
                    }
                } else {
                    if (tmpH > max_height) {
                       tmpW *= max_height / tmpH;
                       tmpH = max_height;
                    }
                }

                var canvas = document.createElement('canvas');
                canvas.width = tmpW;
                canvas.height = tmpH;
                var ctx = canvas.getContext('2d');
                ctx.drawImage(this, 0, 0, tmpW, tmpH);
                sURL = canvas.toDataURL("image/jpeg");
                editor.summernote('insertImage', sURL, file.name);
            }
        }
        reader.readAsDataURL(file);
    });
}
$(document).ready(function () {
  if($('.summernote')[0]) {
	  $('.summernote').summernote({
	    minHeight: 100,
	    placeholder: 'Hãy nhập nội dung cần hỗ trợ',
	    callbacks: {
		  onImageUpload: function(files) {
			  summernoteOnImageUpload(files, $('.summernote'));
		  },
		  onImageUploadError: function() {
		        $.modal.alertError('Hình quá lớn, chỉ cho phép hình dung lượng tối đa 150kb.');
		  }
	    }
	  });
  }
});