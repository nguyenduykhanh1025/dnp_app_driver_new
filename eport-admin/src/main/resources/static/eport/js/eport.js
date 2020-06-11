var table = {
    config: {},
    options: {},
    set: function(id) {
    	if($.common.getLength(table.config) > 1) {
    		var tableId = $.common.isEmpty(id) ? $(event.currentTarget).parents(".bootstrap-table").find(".table").attr("id") : id;
            if ($.common.isNotEmpty(tableId)) {
                table.options = table.get(tableId);
            }
    	}
    },
    get: function(id) {
        return table.config[id];
    },
    rememberSelecteds: {},
    rememberSelectedIds: {}
};

(function ($) {
    $.extend({
    	_tree: {},
    	bttTable: {},
    	table: {
            init: function(options) {
            	var defaults = {
            		id: "bootstrap-table",
            		type: 0, // 0 bootstrapTable 1 bootstrapTreeTable
        		    height: undefined,
        		    sidePagination: "server",
        		    sortName: "",
        		    sortOrder: "asc",
        		    pagination: true,
        		    pageSize: 10,
        		    pageList: [10, 25, 50],
        		    toolbar: "toolbar",
        		    striped: false,
        		    escape: false,
        		    firstLoad: true,
        		    showFooter: false,
        		    search: false,
                    showSearch: true,
                    showPageGo: false,
                    showRefresh: true,
                    showColumns: true,
                    showToggle: true,
                    showExport: false,
                    clickToSelect: false,
                    singleSelect: false,
                    mobileResponsive: true,
                    rememberSelected: false,
        		    fixedColumns: false,
        		    fixedNumber: 0,
        		    rightFixedColumns: false,
        		    rightFixedNumber: 0,
        		    queryParams: $.table.queryParams,
        		    rowStyle: {},
        		};
            	var options = $.extend(defaults, options);
            	table.options = options;
            	table.config[options.id] = options;
                $.table.initEvent();
                $('#' + options.id).bootstrapTable({
                	id: options.id,
                    url: options.url,                                   // 请求后台的URL（*）
                    contentType: "application/x-www-form-urlencoded",   // 编码类型
                    method: 'post',                                     // 请求方式（*）
                    cache: false,                                       // 是否使用缓存
                    height: options.height,                             // 表格的高度
                    striped: options.striped,                           // 是否显示行间隔色
                    sortable: true,                                     // 是否启用排序
                    sortStable: true,                                   // 设置为 true 将获得稳定的排序
                    sortName: options.sortName,                         // 排序列名称
                    sortOrder: options.sortOrder,                       // 排序方式  asc 或者 desc
                    pagination: options.pagination,                     // 是否显示分页（*）
                    pageNumber: 1,                                      // 初始化加载第一页，默认第一页
                    pageSize: options.pageSize,                         // 每页的记录行数（*） 
                    pageList: options.pageList,                         // 可供选择的每页的行数（*）
                    firstLoad: options.firstLoad,                       // 是否首次请求加载数据，对于数据较大可以配置false
                    escape: options.escape,                             // 转义HTML字符串
                    showFooter: options.showFooter,                     // 是否显示表尾
                    iconSize: 'outline',                                // 图标大小：undefined默认的按钮尺寸 xs超小按钮sm小按钮lg大按钮
                    toolbar: '#' + options.toolbar,                     // 指定工作栏
                    sidePagination: options.sidePagination,             // server启用服务端分页client客户端分页
                    search: options.search,                             // 是否显示搜索框功能
                    searchText: options.searchText,                     // 搜索框初始显示的内容，默认为空
                    showSearch: options.showSearch,                     // 是否显示检索信息
                    showPageGo: options.showPageGo,               		// 是否显示跳转页
                    showRefresh: options.showRefresh,                   // 是否显示刷新按钮
                    showColumns: options.showColumns,                   // 是否显示隐藏某列下拉框
                    showToggle: options.showToggle,                     // 是否显示详细视图和列表视图的切换按钮
                    showExport: options.showExport,                     // 是否支持导出文件
                    uniqueId: options.uniqueId,                         // 唯 一的标识符
                    clickToSelect: options.clickToSelect,				// 是否启用点击选中行
                    singleSelect: options.singleSelect,                 // 是否单选checkbox
                    mobileResponsive: options.mobileResponsive,         // 是否支持移动端适配
                    detailView: options.detailView,                     // 是否启用显示细节视图
                    onClickRow: options.onClickRow,                     // 点击某行触发的事件
                    onDblClickRow: options.onDblClickRow,               // 双击某行触发的事件
                    onClickCell: options.onClickCell,                   // 单击某格触发的事件
                    onDblClickCell: options.onDblClickCell,             // 双击某格触发的事件
                    onEditableSave: options.onEditableSave,             // 行内编辑保存的事件
                    onExpandRow: options.onExpandRow,                   // 点击详细视图的事件
                    rememberSelected: options.rememberSelected,         // 启用翻页记住前面的选择
                    fixedColumns: options.fixedColumns,                 // 是否启用冻结列（左侧）
                    fixedNumber: options.fixedNumber,                   // 列冻结的个数（左侧）
                    rightFixedColumns: options.rightFixedColumns,       // 是否启用冻结列（右侧）
                    rightFixedNumber: options.rightFixedNumber,         // 列冻结的个数（右侧）
                    onReorderRow: options.onReorderRow,                 // 当拖拽结束后处理函数
                    queryParams: options.queryParams,                   // 传递参数（*）
                    rowStyle: options.rowStyle,                         // 通过自定义函数设置行样式
                    columns: options.columns,                           // 显示列信息（*）
                    responseHandler: $.table.responseHandler,           // 在加载服务器发送来的数据之前处理函数
                    onLoadSuccess: $.table.onLoadSuccess,               // 当所有数据被加载时触发处理函数
                    exportOptions: options.exportOptions,               // 前端导出忽略列索引
                    detailFormatter: options.detailFormatter,           // 在行下面展示其他数据列表
                });
            },
            getOptionsIds: function(separator) {
            	var _separator = $.common.isEmpty(separator) ? "," : separator;
            	var optionsIds = "";  
            	$.each(table.config, function(key, value){
            		optionsIds += "#" + key + _separator;
            	});
            	return optionsIds.substring(0, optionsIds.length - 1);
            },
            queryParams: function(params) {
            	var curParams = {
                        pageSize:       params.limit,
                        pageNum:        params.offset / params.limit + 1,
                        searchValue:    params.search,
                        orderByColumn:  params.sort,
                        isAsc:          params.order
            		};
            	var currentId = $.common.isEmpty(table.options.formId) ? $('form').attr('id') : table.options.formId;
            	return $.extend(curParams, $.common.formToJSON(currentId)); 
            },
            responseHandler: function(res) {
            	if (typeof table.options.responseHandler == "function") {
            		table.options.responseHandler(res);
                }
                if (res.code == 0) {
                    if ($.common.isNotEmpty(table.options.sidePagination) && table.options.sidePagination == 'client') {
                    	return res.rows;
                    } else {
                    	if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                    		var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
                    		$.each(res.rows, function(i, row) {
                    			row.state = $.inArray(row[column], table.rememberSelectedIds[table.options.id]) !== -1;
                            })
                    	}
                        return { rows: res.rows, total: res.total };
                    }
                } else {
                    $.modal.alertWarning(res.msg);
                    return { rows: [], total: 0 };
                }
            },
            initEvent: function() {
            	var optionsIds = $.table.getOptionsIds();
            	$(optionsIds).on(TABLE_EVENTS, function () {
            		table.set($(this).attr("id"));
            	});
            	$(optionsIds).on("check.bs.table check-all.bs.table uncheck.bs.table uncheck-all.bs.table", function (e, rows) {
            		var rowIds = $.table.affectedRowIds(rows);
            		if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
            			func = $.inArray(e.type, ['check', 'check-all']) > -1 ? 'union' : 'difference';
            			var selectedIds = table.rememberSelectedIds[table.options.id];
            			if($.common.isNotEmpty(selectedIds)) {
            				table.rememberSelectedIds[table.options.id] = _[func](selectedIds, rowIds);
            			} else {
            				table.rememberSelectedIds[table.options.id] = _[func]([], rowIds);
            			}
            			var selectedRows = table.rememberSelecteds[table.options.id];
            			if($.common.isNotEmpty(selectedRows)) {
            				table.rememberSelecteds[table.options.id] = _[func](selectedRows, rows);
            			} else {
            				table.rememberSelecteds[table.options.id] = _[func]([], rows);
            			}
            		}
            	});
            	$(optionsIds).on("check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table load-success.bs.table", function () {
            		var toolbar = table.options.toolbar;
            		var uniqueId = table.options.uniqueId;
            		var rows = $.common.isEmpty(uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(uniqueId);
            		$('#' + toolbar + ' .multiple').toggleClass('disabled', !rows.length);
            		$('#' + toolbar + ' .single').toggleClass('disabled', rows.length!=1);
            	});
            	$(optionsIds).off("click").on("click", '.img-circle', function() {
    			    var src = $(this).attr('src');
    			    var width = $(this).data('width');
    			    if($.common.equals("self", target)) {
    			    	var height = $(this).data('height');
						var width = $(this).data('width');
						if ($.common.isMobile()) {
							width = 'auto';
							height = 'auto';
						}
    			    	layer.open({
        			        title: false,
        			        type: 1,
        			        closeBtn: true,
        			        shadeClose: true,
        			        area: ['auto', 'auto'],
        			        content: "<img src='" + src + "' height='" + height + "' width='" + width + "'/>"
        			    });
    			    } else if ($.common.equals("blank", target)) {
    			        window.open(src);
    			    }
    			});
            	$(optionsIds).on("click", '.tooltip-show', function() {
            		var target = $(this).data('target');
            		var input = $(this).prev();
            		if ($.common.equals("copy", target)) {
            		    input.select();
            		    document.execCommand("copy");
            		} else if ($.common.equals("open", target)) {
            			parent.layer.alert(input.val(), {
                	        title: "Thông Báo",
                	        shadeClose: true,
                	        btn: ['OK'],
                	        btnclass: ['btn btn-primary'],
                	    });
            		}
            	});
            },
            onLoadSuccess: function(data) {
            	if (typeof table.options.onLoadSuccess == "function") {
            		table.options.onLoadSuccess(data);
            	}
            	$(".table [data-toggle='tooltip']").tooltip();
            	
            	$('.table [data-toggle="popover"]').each(function() {
            	    $(this).popover({ trigger: "manual", html: true, animation: false, container: "body", placement: "left" 
            	    }).on("mouseenter",
                        function() {
	            	        var _this = this;
	            	        $(this).popover("show");
	            	        $(".popover").on("mouseleave", function() {
                                $(_this).popover('hide'); 
	            	        });
            	    }).on("mouseleave",
                        function() {
            	            var _this = this;
	            	        setTimeout(function() {
		            	        if (!$(".popover:hover").length)
			            	        $(_this).popover("hide");
            	        }, 100);
            	    });
            	});
            },
            destroy: function (tableId) {
            	var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
            	$("#" + currentId).bootstrapTable('destroy');
	        },
            serialNumber: function (index, tableId) {
            	var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
				var tableParams = $("#" + currentId).bootstrapTable('getOptions');
				var pageSize = tableParams.pageSize;
				var pageNumber = tableParams.pageNumber;
				return pageSize * (pageNumber - 1) + index + 1;
			},
			tooltip: function (value, length, target) {
				var _length = $.common.isEmpty(length) ? 20 : length;
				var _text = "";
				var _value = $.common.nullToStr(value);
				var _target = $.common.isEmpty(target) ? 'copy' : target;
				if (_value.length > _length) {
					_text = _value.substr(0, _length) + "...";
					_value = _value.replace(/\'/g,"&apos;");
					_value = _value.replace(/\"/g,"&quot;");
					var actions = [];
					actions.push($.common.sprintf('<input id="tooltip-show" style="opacity: 0;position: absolute;z-index:-1" type="text" value="%s"/>', _value));
                	actions.push($.common.sprintf('<a href="###" class="tooltip-show" data-toggle="tooltip" data-target="%s" title="%s">%s</a>', _target, _value, _text));
					return actions.join('');
				} else {
					_text = _value;
					return _text;
				}
			},
			dropdownToggle: function (value) {
				var actions = [];
				actions.push('<div class="btn-group">');
				actions.push('<button type="button" class="btn btn-xs dropdown-toggle" data-toggle="dropdown" aria-expanded="false">');
				actions.push('<i class="fa fa-cog"></i>&nbsp;<span class="fa fa-chevron-down"></span></button>');
				actions.push('<ul class="dropdown-menu">');
				actions.push(value.replace(/<a/g,"<li><a").replace(/<\/a>/g,"</a></li>"));
				actions.push('</ul>');
				actions.push('</div>');
				return actions.join('');
			},
			imageView: function (value, height, width, target) {
				if ($.common.isEmpty(width)) {
                	width = 'auto';
                }
                if ($.common.isEmpty(height)) {
                	height = 'auto';
                }
				// blank or self
				var _target = $.common.isEmpty(target) ? 'self' : target;
				if ($.common.isNotEmpty(value)) {
					return $.common.sprintf("<img class='img-circle img-xs' data-height='%s' data-width='%s' data-target='%s' src='%s'/>", height, width, _target, value);
				} else {
					return $.common.nullToStr(value);
				}
			},
            search: function(formId, tableId, data) {
            	table.set(tableId);
            	var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
            	var params = $.common.isEmpty(tableId) ? $("#" + table.options.id).bootstrapTable('getOptions') : $("#" + tableId).bootstrapTable('getOptions');
            	params.queryParams = function(params) {
                    var search = $.common.formToJSON(currentId);
                    if($.common.isNotEmpty(data)){
	                    $.each(data, function(key) {
	                        search[key] = data[key];
	                    });
                    }
                    search.pageSize = params.limit;
                    search.pageNum = params.offset / params.limit + 1;
                    search.searchValue = params.search;
                    search.orderByColumn = params.sort;
                    search.isAsc = params.order;
    		        return search;
    		    }
    		    if($.common.isNotEmpty(tableId)){
    				$("#" + tableId).bootstrapTable('refresh', params);
    			} else{
    				$("#" + table.options.id).bootstrapTable('refresh', params);
    			}
    		},
    		exportExcel: function(formId) {
    			table.set();
    			$.modal.confirm("Xuất tất cả " + table.options.modalName + " ra Excel?", function() {
	    			var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
	    			var params = $("#" + table.options.id).bootstrapTable('getOptions');
	    			var dataParam = $("#" + currentId).serializeArray();
	    			dataParam.push({ "name": "orderByColumn", "value": params.sortName });
	    			dataParam.push({ "name": "isAsc", "value": params.sortOrder });
	    			$.modal.loading("Đang xử lý, vui lòng chờ...");
	    			$.post(table.options.exportUrl, dataParam, function(result) {
	    				if (result.code == web_status.SUCCESS) {
	    			        window.location.href = ctx + "common/download?fileName=" + encodeURI(result.msg) + "&delete=" + true;
	    				} else if (result.code == web_status.WARNING) {
	                        $.modal.alertWarning(result.msg)
	                    } else {
	    					$.modal.alertError(result.msg);
	    				}
	    				$.modal.closeLoading();
	    			});
    			});
    		},
    		importTemplate: function() {
    			table.set();
    			$.get(table.options.importTemplateUrl, function(result) {
    				if (result.code == web_status.SUCCESS) {
    			        window.location.href = ctx + "common/download?fileName=" + encodeURI(result.msg) + "&delete=" + true;
    				} else if (result.code == web_status.WARNING) {
                        $.modal.alertWarning(result.msg)
                    } else {
    					$.modal.alertError(result.msg);
    				}
    			});
            },
            importExcel: function(formId) {
            	table.set();
            	var currentId = $.common.isEmpty(formId) ? 'importTpl' : formId;
            	layer.open({
            		type: 1,
            		area: ['400px', '230px'],
            		fix: false,
            		maxmin: true,
            		shade: 0.3,
            		title: 'Nhập Từ File Cho ' + table.options.modalName + '',
            		content: $('#' + currentId).html(),
            		btn: ['<i class="fa fa-check"></i> Nhập', '<i class="fa fa-remove"></i> Bỏ Qua'],
            		shadeClose: true,
            		btn1: function(index, layero){
            			var file = layero.find('#file').val();
            			if (file == '' || (!$.common.endWith(file, '.xls') && !$.common.endWith(file, '.xlsx'))){
            				$.modal.msgWarning("Hãy chọn file Excel có đuôi “xls” hoạc “xlsx”");
            				return false;
            			}
            			var index = layer.load(2, {shade: false});
            			$.modal.disable();
            			var formData = new FormData(layero.find('form')[0]);
            			$.ajax({
            				url: table.options.importUrl,
            				data: formData,
            				cache: false,
            				contentType: false,
            				processData: false,
            				type: 'POST',
            				success: function (result) {
            					if (result.code == web_status.SUCCESS) {
            						$.modal.closeAll();
            						$.modal.alertSuccess(result.msg);
            						$.table.refresh();
            					} else if (result.code == web_status.WARNING) {
            						layer.close(index);
            						$.modal.enable();
        	                        $.modal.alertWarning(result.msg)
        	                    } else {
            						layer.close(index);
            						$.modal.enable();
            						$.modal.alertError(result.msg);
            					}
            				}
            			});
            		}
            	});
            },
            refresh: function(tableId) {
            	var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
            	$("#" + currentId).bootstrapTable('refresh', {
                    silent: true
                });
            },
            selectColumns: function(column) {
            	var rows = $.map($("#" + table.options.id).bootstrapTable('getSelections'), function (row) {
        	        return row[column];
        	    });
            	if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
            		var selectedRows = table.rememberSelecteds[table.options.id];
            		if($.common.isNotEmpty(selectedRows)) {
	            		rows = $.map(table.rememberSelecteds[table.options.id], function (row) {
	                        return row[column];
	                    });
            		}
            	}
            	return $.common.uniqueFn(rows);
            },
            affectedRowIds: function(rows) {
            	var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
            	var rowIds;
            	if ($.isArray(rows)) {
            	    rowIds = $.map(rows, function(row) {
            	        return row[column];
            	    });
            	} else {
            	    rowIds = [rows[column]];
            	}
            	return rowIds;
            },
            selectFirstColumns: function() {
            	var rows = $.map($("#" + table.options.id).bootstrapTable('getSelections'), function (row) {
        	        return row[table.options.columns[1].field];
        	    });
            	if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
            		var selectedRows = table.rememberSelecteds[table.options.id];
            		if($.common.isNotEmpty(selectedRows)) {
            			rows = $.map(selectedRows, function (row) {
                            return row[table.options.columns[1].field];
                        });
            		}
            	}
            	return $.common.uniqueFn(rows);
            },
            selectDictLabel: function(datas, value) {
            	var actions = [];
                $.each(datas, function(index, dict) {
                    if (dict.dictValue == ('' + value)) {
                    	var listClass = $.common.equals("default", dict.listClass) || $.common.isEmpty(dict.listClass) ? "" : "badge badge-" + dict.listClass;
                    	actions.push($.common.sprintf("<span class='%s'>%s</span>", listClass, dict.dictLabel));
                        return false;
                    }
                });
                return actions.join('');
            },
            showColumn: function(column, tableId) {
            	var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
            	$("#" + currentId).bootstrapTable('showColumn', column);
            },
            hideColumn: function(column, tableId) {
            	var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
            	$("#" + currentId).bootstrapTable('hideColumn', column);
            }
        },
        treeTable: {
            init: function(options) {
            	var defaults = {
            		id: "bootstrap-tree-table",
                    type: 1, // 0  bootstrapTable 1 bootstrapTreeTable
        		    height: 0,
        		    rootIdValue: null,
        		    ajaxParams: {},
        		    toolbar: "toolbar",
        		    striped: false,
        		    expandColumn: 1,
        		    showSearch: true,
        		    showRefresh: true,
        			showColumns: true,
        			expandAll: true,
        			expandFirst: true
        		};
            	var options = $.extend(defaults, options);
            	table.options = options;
            	table.config[options.id] = options;
                $.bttTable = $('#' + options.id).bootstrapTreeTable({
                	code: options.code,                                 // 用于设置父子关系
        		    parentCode: options.parentCode,                     // 用于设置父子关系
        	    	type: 'post',                                       // 请求方式（*）
        	        url: options.url,                                   // 请求后台的URL（*）
        	        data: options.data,                                 // 无url时用于渲染的数据
        	        ajaxParams: options.ajaxParams,                     // 请求数据的ajax的data属性
        	        rootIdValue: options.rootIdValue,                   // 设置指定根节点id值
        	        height: options.height,                             // 表格树的高度
        			expandColumn: options.expandColumn,                 // 在哪一列上面显示展开按钮
        			striped: options.striped,                           // 是否显示行间隔色
        			bordered: false,                                    // 是否显示边框
        			toolbar: '#' + options.toolbar,                     // 指定工作栏
        			showSearch: options.showSearch,                     // 是否显示检索信息
        			showRefresh: options.showRefresh,                   // 是否显示刷新按钮
        			showColumns: options.showColumns,                   // 是否显示隐藏某列下拉框
        			expandAll: options.expandAll,                       // 是否全部展开
        			expandFirst: options.expandFirst,                   // 是否默认第一级展开--expandAll为false时生效
        	        columns: options.columns,                           // 显示列信息（*）
        	        responseHandler: $.treeTable.responseHandler        // 当所有数据被加载时触发处理函数
        	    });
            },
            search: function(formId) {
            	var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
            	var params = $.common.formToJSON(currentId);
                $.bttTable.bootstrapTreeTable('refresh', params);
            },
            refresh: function() {
            	$.bttTable.bootstrapTreeTable('refresh');
            },
            selectColumns: function(column) {
            	var rows = $.map($.bttTable.bootstrapTreeTable('getSelections'), function (row) {
        	        return row[column];
        	    });
            	return $.common.uniqueFn(rows);
            },
            responseHandler: function(res) {
            	if (typeof table.options.responseHandler == "function") {
            		table.options.responseHandler(res);
                }
            	if (res.code != undefined && res.code != 0) {
            		$.modal.alertWarning(res.msg);
            		return [];
                } else {
                    return res;
                }
            },
        },
    	form: {
    		reset: function(formId, tableId) {
    			table.set(tableId);
            	var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
            	$("#" + currentId)[0].reset();
            	if (table.options.type == table_type.bootstrapTable) {
            	    if($.common.isEmpty(tableId)){
            	    	$("#" + table.options.id).bootstrapTable('refresh');
                	} else{
                	    $("#" + tableId).bootstrapTable('refresh');
                	}
            	} else if (table.options.type == table_type.bootstrapTreeTable) {
            		if($.common.isEmpty(tableId)){
            	    	$("#" + table.options.id).bootstrapTreeTable('refresh', []);
                	} else{
                	    $("#" + tableId).bootstrapTreeTable('refresh', []);
                	}
            	}
            },
            selectCheckeds: function(name) {
            	var checkeds = "";
        	    $('input:checkbox[name="' + name + '"]:checked').each(function(i) {
        	        if (0 == i) {
        	        	checkeds = $(this).val();
        	        } else {
        	        	checkeds += ("," + $(this).val());
        	        }
        	    });
        	    return checkeds;
            },
            selectSelects: function(name) {
            	var selects = "";
        	    $('#' + name + ' option:selected').each(function (i) {
        	        if (0 == i) {
        	        	selects = $(this).val();
        	        } else {
        	        	selects += ("," + $(this).val());
        	        }
        	    });
        	    return selects;
            }
        },
    	modal: {
    		icon: function(type) {
            	var icon = "";
        	    if (type == modal_status.WARNING) {
        	        icon = 0;
        	    } else if (type == modal_status.SUCCESS) {
        	        icon = 1;
        	    } else if (type == modal_status.FAIL) {
        	        icon = 2;
        	    } else {
        	        icon = 3;
        	    }
        	    return icon;
            },
            msg: function(content, type) {
            	if (type != undefined) {
                    layer.msg(content, { icon: $.modal.icon(type), time: 1000, shift: 5 });
                } else {
                    layer.msg(content);
                }
            },
            msgError: function(content) {
            	$.modal.msg(content, modal_status.FAIL);
            },
            msgSuccess: function(content) {
            	$.modal.msg(content, modal_status.SUCCESS);
            },
            msgWarning: function(content) {
            	$.modal.msg(content, modal_status.WARNING);
            },
            alert: function(content, type) {
        	    layer.alert(content, {
        	        icon: $.modal.icon(type),
        	        title: "Thông Báo",
        	        btn: ['OK'],
        	        btnclass: ['btn btn-primary'],
        	    });
            },
            msgReload: function(msg, type) {
            	layer.msg(msg, {
            	    icon: $.modal.icon(type),
            	    time: 500,
            	    shade: [0.1, '#8F8F8F']
            	},
            	function() {
            	    $.modal.reload();
            	});
            },
            alertError: function(content) {
            	$.modal.alert(content, modal_status.FAIL);
            },
            alertSuccess: function(content) {
            	$.modal.alert(content, modal_status.SUCCESS);
            },
            alertWarning: function(content) {
            	$.modal.alert(content, modal_status.WARNING);
            },
            close: function () {
            	var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            },
            closeAll: function () {
                layer.closeAll();
            },
            confirm: function (content, callBack) {
            	layer.confirm(content, {
        	        icon: 3,
        	        title: "Xác Nhận",
        	        btn: ['Đồng Ý', 'Hủy Bỏ']
        	    }, function (index) {
        	    	layer.close(index);
        	        callBack(true);
        	    });
            },
            open: function (title, url, width, height, callback) {
            	if ($.common.isMobile()) {
            	    width = 'auto';
            	    height = 'auto';
            	}
            	if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html";
                }
                if ($.common.isEmpty(width)) {
                	width = 800;
                }
                if ($.common.isEmpty(height)) {
                	height = ($(window).height() - 50);
                }
                if ($.common.isEmpty(callback)) {
                    callback = function(index, layero) {
                        var iframeWin = layero.find('iframe')[0];
                        iframeWin.contentWindow.submitHandler(index, layero);
                    }
                }
            	layer.open({
            		type: 2,
            		area: [width + 'px', height + 'px'],
            		fix: false,
            		maxmin: true,
            		shade: 0.3,
            		title: title,
            		content: url,
            		btn: ['OK', 'Đóng'],
            		shadeClose: true,
            		yes: callback,
            	    cancel: function(index) {
            	        return true;
            	    }
            	});
			},
            openDo: function (title, url, width, height, callback) {
            	if ($.common.isMobile()) {
            	    width = 'auto';
            	    height = 'auto';
            	}
            	if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html";
                }
                if ($.common.isEmpty(width)) {
                	width = 800;
                }
                if ($.common.isEmpty(height)) {
                	height = ($(window).height() - 50);
                }
                if ($.common.isEmpty(callback)) {
                    callback = function(index, layero) {
                        var iframeWin = layero.find('iframe')[0];
                        iframeWin.contentWindow.submitHandler(index, layero);
                    }
                }
            	layer.open({
            		type: 2,
            		area: [width + 'px', height + 'px'],
            		fix: false,
            		maxmin: true,
            		shade: 0.3,
            		title: title,
            		content: url,
            		shadeClose: true,
            		yes: callback,
            	    cancel: function(index) {
            	        return true;
            	    }
            	});
            },
            openOptions: function (options) {
            	var _url = $.common.isEmpty(options.url) ? "/404.html" : options.url; 
            	var _title = $.common.isEmpty(options.title) ? "Hệ Thống" : options.title; 
                var _width = $.common.isEmpty(options.width) ? "800" : options.width; 
                var _height = $.common.isEmpty(options.height) ? ($(window).height() - 50) : options.height;
                var _btn = ['<i class="fa fa-check"></i> OK', '<i class="fa fa-close"></i> Đóng'];
                if ($.common.isEmpty(options.yes)) {
                	options.yes = function(index, layero) {
                    	options.callBack(index, layero);
                    }
                }
                layer.open({
                    type: 2,
            		maxmin: true,
                    shade: 0.3,
                    title: _title,
                    fix: false,
                    area: [_width + 'px', _height + 'px'],
                    content: _url,
                    shadeClose: $.common.isEmpty(options.shadeClose) ? true : options.shadeClose,
                    skin: options.skin,
                    btn: $.common.isEmpty(options.btn) ? _btn : options.btn,
										yes: options.yes,
										btn2: options.btn2,
										btn3: options.btn3,
                    cancel: function () {
                        return true;
										}
                });
            },
            openFull: function (title, url, width, height) {
            	if ($.common.isMobile()) {
            	    width = 'auto';
            	    height = 'auto';
            	}
            	if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html";
                }
                if ($.common.isEmpty(width)) {
                	width = 800;
                }
                if ($.common.isEmpty(height)) {
                	height = ($(window).height() - 50);
                }
                var index = layer.open({
            		type: 2,
            		area: [width + 'px', height + 'px'],
            		fix: false,
            		maxmin: true,
            		shade: 0.3,
            		title: title,
            		content: url,
            		btn: ['OK', 'Đóng'],
            		shadeClose: true,
            		yes: function(index, layero) {
            	        var iframeWin = layero.find('iframe')[0];
            	        iframeWin.contentWindow.submitHandler(index, layero);
            	    },
            	    cancel: function(index) {
            	        return true;
            	    }
            	});
                layer.full(index);
			},
			// open full
            openFullDo: function (title, url, width, height) {
            	if ($.common.isMobile()) {
            	    width = 'auto';
            	    height = 'auto';
            	}
            	if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html";
                }
                if ($.common.isEmpty(width)) {
                	width = 800;
                }
                if ($.common.isEmpty(height)) {
                	height = ($(window).height() - 50);
                }
                var index = layer.open({
            		type: 2,
            		area: [width + 'px', height + 'px'],
            		fix: false,
            		maxmin: true,
            		shade: 0.3,
            		title: title,
            		content: url,
            		shadeClose: true,
            		yes: function(index, layero) {
            	        var iframeWin = layero.find('iframe')[0];
            	        iframeWin.contentWindow.submitHandler(index, layero);
            	    },
            	    cancel: function(index) {
            	        return true;
            	    }
            	});
                layer.full(index);
            },
            openTab: function (title, url) {
            	createMenuItem(url, title);
            },
            parentTab: function (title, url) {
            	var dataId = window.frameElement.getAttribute('data-id');
            	createMenuItem(url, title);
            	closeItem(dataId);
            },
            closeTab: function (dataId) {
            	closeItem(dataId);
            },
            disable: function() {
            	var doc = window.top == window.parent ? window.document : window.parent.document;
	        	$("a[class*=layui-layer-btn]", doc).addClass("layer-disabled");
            },
            enable: function() {
            	var doc = window.top == window.parent ? window.document : window.parent.document;
            	$("a[class*=layui-layer-btn]", doc).removeClass("layer-disabled");
            },
            loading: function (message) {
            	$.blockUI({ message: '<div class="loaderbox"><div class="loading-activity"></div> ' + message + '</div>' });
            },
            closeLoading: function () {
            	setTimeout(function(){
            		$.unblockUI();
            	}, 50);
            },
            reload: function () {
            	parent.location.reload();
            }
        },
        operate: {
        	submit: function(url, type, dataType, data, callback) {
            	var config = {
        	        url: url,
        	        type: type,
        	        dataType: dataType,
        	        data: data,
        	        beforeSend: function () {
        	        	$.modal.loading("Đang xử lý, vui long chờ...");
        	        },
        	        success: function(result) {
        	        	if (typeof callback == "function") {
        	        	    callback(result);
        	        	}
        	        	$.operate.ajaxSuccess(result);
        	        }
        	    };
        	    $.ajax(config)
            },
            post: function(url, data, callback) {
            	$.operate.submit(url, "post", "json", data, callback);
            },
            get: function(url, callback) {
            	$.operate.submit(url, "get", "json", "", callback);
            },
            detail: function(id, width, height) {
            	table.set();
            	var _url = $.operate.detailUrl(id);
            	var _width = $.common.isEmpty(width) ? "800" : width; 
                var _height = $.common.isEmpty(height) ? ($(window).height() - 50) : height;
            	if ($.common.isMobile()) {
            	    _width = 'auto';
            	    _height = 'auto';
            	}
            	var options = {
       				title: "Chi Tiết " + table.options.modalName,
       				width: _width,
       				height: _height,
       				url: _url,
       				skin: 'layui-layer-gray', 
       				btn: ['Đóng'],
       				yes: function (index, layero) {
       	                layer.close(index);
                    }
       			};
            	$.modal.openOptions(options);
            },
            detailUrl: function(id) {
            	var url = "/404.html";
            	if ($.common.isNotEmpty(id)) {
            	    url = table.options.detailUrl.replace("{id}", id);
            	} else {
            	    var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
            	    if (id.length == 0) {
            			$.modal.alertWarning("Hãy chọn dòng để xử lý");
            			return;
            		}
            	    url = table.options.detailUrl.replace("{id}", id);
            	}
                return url;
            },
            remove: function(id) {
            	table.set();
            	$.modal.confirm("Xác nhận thực hiện xóa thông tin " + table.options.modalName + "?", function() {
                    var url = $.common.isEmpty(id) ? table.options.removeUrl : table.options.removeUrl.replace("{id}", id);
                    if(table.options.type == table_type.bootstrapTreeTable) {
                    	$.operate.get(url);
                    } else {
	            	    var data = { "ids": id };
	            	    $.operate.submit(url, "post", "json", data);
	                }
            	});
            	
            },
            removeAll: function() {
            	table.set();
        		var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
        		if (rows.length == 0) {
        			$.modal.alertWarning("Hãy chọn dòng để xử lý");
        			return;
        		}
        		$.modal.confirm("Xác nhận xóa " + rows.length + " dòng đang chọn?", function() {
        			var url = table.options.removeUrl;
        			var data = { "ids": rows.join() };
        			$.operate.submit(url, "post", "json", data);
        		});
            },
            clean: function() {
            	table.set();
            	$.modal.confirm("Xác nhận xóa tất cả " + table.options.modalName + "?", function() {
	            	var url = table.options.cleanUrl;
	            	$.operate.submit(url, "post", "json", "");
            	});
            },
            add: function(id) {
            	table.set();
            	$.modal.open("Thêm " + table.options.modalName, $.operate.addUrl(id));
			},
			addGroup: function(id) {
            	table.set();
            	$.modal.open("Thêm " + table.options.modalName, $.operate.addUrl(id), 500, 380);
            },
			addCarrierAccount: function(id) {
            	table.set();
            	$.modal.open("Thêm " + table.options.modalName, $.operate.addUrl(id), 500, 450);
            },
			addLogisticAccount: function(id) {
            	table.set();
            	$.modal.open("Thêm " + table.options.modalName, $.operate.addUrl(id), 800, 440);
            },
			addLogisticGroup: function(id) {
            	table.set();
            	$.modal.openTab("Thêm " + table.options.modalName, $.operate.addUrl(id));
            },
            addTab: function (id) {
            	table.set();
                $.modal.openTab("Thêm " + table.options.modalName, $.operate.addUrl(id));
            },
            addFull: function(id) {
            	table.set();
            	var url = $.common.isEmpty(id) ? table.options.createUrl : table.options.createUrl.replace("{id}", id);
                $.modal.openFull("Thêm " + table.options.modalName, url);
			},
			// add full size do
            addFullDo: function(id) {
            	table.set();
				var url = $.common.isEmpty(id) ? table.options.createUrl : table.options.createUrl.replace("{id}", id);
                $.modal.openDo("Thêm " + table.options.modalName, url, 1200);
            },
            addUrl: function(id) {
            	var url = $.common.isEmpty(id) ? table.options.createUrl.replace("{id}", "") : table.options.createUrl.replace("{id}", id);
                return url;
            },
            edit: function(id) {
            	table.set();
            	if($.common.isEmpty(id) && table.options.type == table_type.bootstrapTreeTable) {
            		var row = $("#" + table.options.id).bootstrapTreeTable('getSelections')[0];
                	if ($.common.isEmpty(row)) {
            			$.modal.alertWarning("Hãy chọn dong để xử lý");
            			return;
            		}
					var url = table.options.updateUrl.replace("{id}", row[table.options.uniqueId]);
                    $.modal.open("Chỉnh Sửa " + table.options.modalName, url);
            	} else {
            	    $.modal.open("Chỉnh Sửa " + table.options.modalName, $.operate.editUrl(id));
            	}
			},
			editGroup: function(id) {
            	table.set();
            	if($.common.isEmpty(id) && table.options.type == table_type.bootstrapTreeTable) {
            		var row = $("#" + table.options.id).bootstrapTreeTable('getSelections')[0];
                	if ($.common.isEmpty(row)) {
            			$.modal.alertWarning("Hãy chọn dong để xử lý");
            			return;
            		}
                    var url = table.options.updateUrl.replace("{id}", row[table.options.uniqueId]);
                    $.modal.open("Chỉnh Sửa " + table.options.modalName, url, 500, 380);
            	} else {
            	    $.modal.open("Chỉnh Sửa " + table.options.modalName, $.operate.editUrl(id), 500, 380);
            	}
            },
			editCarrierAccount: function(id) {
            	table.set();
            	if($.common.isEmpty(id) && table.options.type == table_type.bootstrapTreeTable) {
            		var row = $("#" + table.options.id).bootstrapTreeTable('getSelections')[0];
                	if ($.common.isEmpty(row)) {
            			$.modal.alertWarning("Hãy chọn dong để xử lý");
            			return;
            		}
                    var url = table.options.updateUrl.replace("{id}", row[table.options.uniqueId]);
                    $.modal.open("Chỉnh Sửa " + table.options.modalName, url, 500, 450);
            	} else {
            	    $.modal.open("Chỉnh Sửa " + table.options.modalName, $.operate.editUrl(id), 500, 400);
            	}
            },
			editLogisticAccount: function(id) {
            	table.set();
            	if($.common.isEmpty(id) && table.options.type == table_type.bootstrapTreeTable) {
            		var row = $("#" + table.options.id).bootstrapTreeTable('getSelections')[0];
                	if ($.common.isEmpty(row)) {
            			$.modal.alertWarning("Hãy chọn dong để xử lý");
            			return;
            		}
                    var url = table.options.updateUrl.replace("{id}", row[table.options.uniqueId]);
                    $.modal.open("Chỉnh Sửa " + table.options.modalName, url, 800, 400);
            	} else {
            	    $.modal.open("Chỉnh Sửa " + table.options.modalName, $.operate.editUrl(id), 800, 400);
            	}
            },
			editLogisticGroup: function(id) {
            	table.set();
            	if($.common.isEmpty(id) && table.options.type == table_type.bootstrapTreeTable) {
            		var row = $("#" + table.options.id).bootstrapTreeTable('getSelections')[0];
                	if ($.common.isEmpty(row)) {
            			$.modal.alertWarning("Hãy chọn dong để xử lý");
            			return;
            		}
                    var url = table.options.updateUrl.replace("{id}", row[table.options.uniqueId]);
                    $.modal.openTab("Chỉnh Sửa " + table.options.modalName, url);
            	} else {
            	    $.modal.openTab("Chỉnh Sửa " + table.options.modalName, $.operate.editUrl(id));
            	}
            },
            editTab: function(id) {
            	table.set();
            	$.modal.openTab("Chỉnh Sửa" + table.options.modalName, $.operate.editUrl(id));
            },
            editFull: function(id) {
            	table.set();
            	var url = "/404.html";
            	if ($.common.isNotEmpty(id)) {
            	    url = table.options.updateUrl.replace("{id}", id);
            	} else {
            		if(table.options.type == table_type.bootstrapTreeTable) {
            			var row = $("#" + table.options.id).bootstrapTreeTable('getSelections')[0];
            			if ($.common.isEmpty(row)) {
            				$.modal.alertWarning("Hãy chọn dong để xử lý");
            				return;
            			}
            			url = table.options.updateUrl.replace("{id}", row[table.options.uniqueId]);
            		} else {
            			var row = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                	    url = table.options.updateUrl.replace("{id}", row);
            		}
            	}
            	$.modal.openFull("Chỉnh Sửa" + table.options.modalName, url);
            },
            editUrl: function(id) {
            	var url = "/404.html";
            	if ($.common.isNotEmpty(id)) {
            	    url = table.options.updateUrl.replace("{id}", id);
            	} else {
            	    var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
            	    if (id.length == 0) {
            			$.modal.alertWarning("Hãy chọn dong để xử lý");
            			return;
            		}
            	    url = table.options.updateUrl.replace("{id}", id);
				}
                return url;
            },
            save: function(url, data, callback) {
            	var config = {
        	        url: url,
        	        type: "post",
        	        dataType: "json",
        	        data: data,
        	        beforeSend: function () {
        	        	$.modal.loading("Đang xử lý, vui lòng chờ...");
        	        	$.modal.disable();
        	        },
        	        success: function(result) {
        	        	if (typeof callback == "function") {
        	        	    callback(result);
        	        	}
        	        	$.operate.successCallback(result);
        	        }
        	    };
        	    $.ajax(config)
            },
            saveModal: function(url, data, callback) {
            	var config = {
        	        url: url,
        	        type: "post",
        	        dataType: "json",
        	        data: data,
        	        beforeSend: function () {
        	        	$.modal.loading("Đang xử lý, vui lòng chờ...");
        	        },
        	        success: function(result) {
        	        	if (typeof callback == "function") {
        	        	    callback(result);
        	        	}
        	        	if (result.code == web_status.SUCCESS) {
	                        $.modal.alertSuccess(result.msg)
	                    } else if (result.code == web_status.WARNING) {
	                        $.modal.alertWarning(result.msg)
	                    } else {
	                    	$.modal.alertError(result.msg);
	                    }
        	        	$.modal.closeLoading();
        	        }
        	    };
        	    $.ajax(config)
            },
            saveTab: function(url, data, callback) {
            	var config = {
        	        url: url,
        	        type: "post",
        	        dataType: "json",
        	        data: data,
        	        beforeSend: function () {
        	        	$.modal.loading("Đang xử lý, vui lòng chờ..");
        	        },
        	        success: function(result) {
        	        	if (typeof callback == "function") {
        	        	    callback(result);
        	        	}
        	        	$.operate.successTabCallback(result);
        	        }
        	    };
        	    $.ajax(config)
            },
            ajaxSuccess: function (result) {
            	if (result.code == web_status.SUCCESS && table.options.type == table_type.bootstrapTable) {
                	$.modal.msgSuccess(result.msg);
            		$.table.refresh();
                } else if (result.code == web_status.SUCCESS && table.options.type == table_type.bootstrapTreeTable) {
                	$.modal.msgSuccess(result.msg);
                	$.treeTable.refresh();
                } else if (result.code == web_status.SUCCESS && table.option.type == undefined) {
                    $.modal.msgSuccess(result.msg)
                }  else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                }  else {
                	$.modal.alertError(result.msg);
                }
            	$.modal.closeLoading();
            },
            saveSuccess: function (result) {
            	if (result.code == web_status.SUCCESS) {
            		$.modal.msgReload("Lưu thành công! Vui lòng chờ trong khi refresh dữ liệu...", modal_status.SUCCESS);
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                }  else {
                	$.modal.alertError(result.msg);
                }
            	$.modal.closeLoading();
            },
            successCallback: function(result) {
                if (result.code == web_status.SUCCESS) {
                	var parent = window.parent;
                    if (parent.table.options.type == table_type.bootstrapTable) {
                        $.modal.close();
                        parent.$.modal.msgSuccess(result.msg);
                        parent.$.table.refresh();
                    } else if (parent.table.options.type == table_type.bootstrapTreeTable) {
                        $.modal.close();
                        parent.$.modal.msgSuccess(result.msg);
                        parent.$.treeTable.refresh();
                    } else {
                        $.modal.msgReload("Lưu thành công! Vui lòng chờ trong khi refresh dữ liêu...", modal_status.SUCCESS);
                    }
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                }  else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
                $.modal.enable();
            },
            successTabCallback: function(result) {
                if (result.code == web_status.SUCCESS) {
                	var topWindow = $(window.parent.document);
    	            var currentId = $('.page-tabs-content', topWindow).find('.active').attr('data-panel');
    	            var $contentWindow = $('.eport_iframe[data-id="' + currentId + '"]', topWindow)[0].contentWindow;
    	            $.modal.close();
    	            $contentWindow.$.modal.msgSuccess(result.msg);
    	            $contentWindow.$(".layui-layer-padding").removeAttr("style");
    	            if ($contentWindow.table.options.type == table_type.bootstrapTable) {
    	        		$contentWindow.$.table.refresh();
    	        	} else if ($contentWindow.table.options.type == table_type.bootstrapTreeTable) {
    	        		$contentWindow.$.treeTable.refresh();
                    }
    	            $.modal.closeTab();
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
            }
        },
        validate: {
        	unique: function (value) {
            	if (value == "0") {
                    return true;
                }
                return false;
            },
            form: function (formId) {
            	var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                return $("#" + currentId).validate().form();
            },
            reset: function (formId) {
            	var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                return $("#" + currentId).validate().resetForm();
            }
        },
        tree: {
        	_option: {},
        	_lastValue: {},
        	init: function(options) {
        		var defaults = {
            		id: "tree",                    // 属性ID
            		expandLevel: 0,                // 展开等级节点
            		view: {
    			        selectedMulti: false,      // 设置是否允许同时选中多个节点
    			        nameIsHTML: true           // 设置 name 属性是否支持 HTML 脚本
    			    },
            		check: {
    				    enable: false,             // 置 zTree 的节点上是否显示 checkbox / radio
    				    nocheckInherit: true,      // 设置子节点是否自动继承
    				},
    				data: {
    			        key: {
    			            title: "title"         // 节点数据保存节点提示信息的属性名称
    			        },
    			        simpleData: {
    			            enable: true           // true / false 分别表示 使用 / 不使用 简单数据模式
    			        }
    			    },
        		};
            	var options = $.extend(defaults, options);
        		$.tree._option = options;
        		var setting = {
    				callback: {
    			        onClick: options.onClick,                      // 用于捕获节点被点击的事件回调函数
    			        onCheck: options.onCheck,                      // 用于捕获 checkbox / radio 被勾选 或 取消勾选的事件回调函数
    			        onDblClick: options.onDblClick                 // 用于捕获鼠标双击之后的事件回调函数
    			    },
    				check: options.check,
    			    view: options.view,
    			    data: options.data
    			};
        	    $.get(options.url, function(data) {
        			var treeId = $("#treeId").val();
        			tree = $.fn.zTree.init($("#" + options.id), setting, data);
        			$._tree = tree;
        			var nodes = tree.getNodesByParam("level", options.expandLevel - 1);
        			for (var i = 0; i < nodes.length; i++) {
        				tree.expandNode(nodes[i], true, false, false);
        			}
        			var node = tree.getNodesByParam("id", treeId, null)[0];
        			$.tree.selectByIdName(treeId, node);
        		});
        	},
        	searchNode: function() {
        		var value = $.common.trim($("#keyword").val());
        		if ($.tree._lastValue == value) {
        		    return;
        		}
        		$.tree._lastValue = value;
        		var nodes = $._tree.getNodes();
        		if (value == "") {
        		    $.tree.showAllNode(nodes);
        		    return;
        		}
        		$.tree.hideAllNode(nodes);
        		$.tree.updateNodes($._tree.getNodesByParamFuzzy("name", value));
        	},
        	selectByIdName: function(treeId, node) {
        		if ($.common.isNotEmpty(treeId) && treeId == node.id) {
        			$._tree.selectNode(node, true);
        		}
        	},
        	showAllNode: function(nodes) {
        		nodes = $._tree.transformToArray(nodes);
        		for (var i = nodes.length - 1; i >= 0; i--) {
        		    if (nodes[i].getParentNode() != null) {
        		    	$._tree.expandNode(nodes[i], true, false, false, false);
        		    } else {
        		    	$._tree.expandNode(nodes[i], true, true, false, false);
        		    }
        		    $._tree.showNode(nodes[i]);
        		    $.tree.showAllNode(nodes[i].children);
        		}
        	},
        	hideAllNode: function(nodes) {
        	    var tree = $.fn.zTree.getZTreeObj("tree");
        	    var nodes = $._tree.transformToArray(nodes);
        	    for (var i = nodes.length - 1; i >= 0; i--) {
        	    	$._tree.hideNode(nodes[i]);
        	    }
        	},
        	showParent: function(treeNode) {
        		var parentNode;
        		while ((parentNode = treeNode.getParentNode()) != null) {
        			$._tree.showNode(parentNode);
        			$._tree.expandNode(parentNode, true, false, false);
        		    treeNode = parentNode;
        		}
        	},
        	showChildren: function(treeNode) {
        		if (treeNode.isParent) {
        		    for (var idx in treeNode.children) {
        		        var node = treeNode.children[idx];
        		        $._tree.showNode(node);
        		        $.tree.showChildren(node);
        		    }
        		}
        	},
        	updateNodes: function(nodeList) {
        		$._tree.showNodes(nodeList);
        		for (var i = 0, l = nodeList.length; i < l; i++) {
        		    var treeNode = nodeList[i];
        		    $.tree.showChildren(treeNode);
        		    $.tree.showParent(treeNode)
        		}
        	},
        	getCheckedNodes: function(column) {
        		var _column = $.common.isEmpty(column) ? "id" : column;
        		var nodes = $._tree.getCheckedNodes(true);
        		return $.map(nodes, function (row) {
        	        return row[_column];
        	    }).join();
        	},
        	notAllowParents: function(_tree) {
    		    var nodes = _tree.getSelectedNodes();
    		    if(nodes.length == 0){
                    $.modal.msgError("Vui lòng chọn một node");
                    return false;
				}
    		    for (var i = 0; i < nodes.length; i++) {
    		        if (nodes[i].level == 0) {
    		            $.modal.msgError("Không thể chọn root (" + nodes[i].name + ")");
    		            return false;
    		        }
    		        if (nodes[i].isParent) {
    		            $.modal.msgError("Không thể chọn node cha (" + nodes[i].name + ")");
    		            return false;
    		        }
    		    }
        		return true;
        	},
        	notAllowLastLevel: function(_tree) {
    		    var nodes = _tree.getSelectedNodes();
    		    for (var i = 0; i < nodes.length; i++) {
                    if (!nodes[i].isParent) {
    		    		$.modal.msgError("Không thể chọn node lá (" + nodes[i].name + ")");
    		            return false;
    		        }
    		    }
        		return true;
        	},
        	toggleSearch: function() {
        		$('#search').slideToggle(200);
        		$('#btnShow').toggle();
        		$('#btnHide').toggle();
        		$('#keyword').focus();
        	},
        	collapse: function() {
        		$._tree.expandAll(false);
        	},
        	expand: function() {
        		$._tree.expandAll(true);
        	}
        },
    	common: {
            isEmpty: function (value) {
                if (value == null || this.trim(value) == "") {
                    return true;
                }
                return false;
            },
            isNotEmpty: function (value) {
            	return !$.common.isEmpty(value);
            },
            nullToStr: function(value) {
                if ($.common.isEmpty(value)) {
                    return "-";
                }
                return value;
            },
            visible: function (value) {
                if ($.common.isEmpty(value) || value == true) {
                    return true;
                }
                return false;
            },
            trim: function (value) {
                if (value == null) {
                    return "";
                }
                return value.toString().replace(/(^\s*)|(\s*$)|\r|\n/g, "");
            },
            equals: function (str, that) {
            	return str == that;
            },
            equalsIgnoreCase: function (str, that) {
            	return String(str).toUpperCase() === String(that).toUpperCase();
            },
            split: function (str, sep, maxLen) {
            	if ($.common.isEmpty(str)) {
            	    return null;
            	}
            	var value = String(str).split(sep);
            	return maxLen ? value.slice(0, maxLen - 1) : value;
            },
            sprintf: function (str) {
                var args = arguments, flag = true, i = 1;
                str = str.replace(/%s/g, function () {
                    var arg = args[i++];
                    if (typeof arg === 'undefined') {
                        flag = false;
                        return '';
                    }
                    return arg;
                });
                return flag ? str : '';
            },
            random: function (min, max) {
                return Math.floor((Math.random() * max) + min);
            },
            startWith: function(value, start) {
                var reg = new RegExp("^" + start);
                return reg.test(value)
            },
            endWith: function(value, end) {
                var reg = new RegExp(end + "$");
                return reg.test(value)
            },
            uniqueFn: function(array) {
                var result = [];
                var hashObj = {};
                for (var i = 0; i < array.length; i++) {
                    if (!hashObj[array[i]]) {
                        hashObj[array[i]] = true;
                        result.push(array[i]);
                    }
                }
                return result;
            },
            join: function(array, separator) {
            	if ($.common.isEmpty(array)) {
            	    return null;
            	}
                return array.join(separator);
            },
            formToJSON: function(formId) {
            	 var json = {};
                 $.each($("#" + formId).serializeArray(), function(i, field) {
                 	 if(json[field.name]) {
                         json[field.name] += ("," + field.value);
					 } else {
                         json[field.name] = field.value;
                     }
                 });
            	return json;
            },
            getLength: function(obj) {
                var count = 0;　　
                for (var i in obj) {
                    if (obj.hasOwnProperty(i)) {
                        count++;
                    }　　
                }
                return count;
            },
            isMobile: function () {
                return navigator.userAgent.match(/(Android|iPhone|SymbianOS|Windows Phone|iPad|iPod)/i);
            },
        }
    });
})(jQuery);

table_type = {
    bootstrapTable: 0,
    bootstrapTreeTable: 1
};

web_status = {
    SUCCESS: 0,
    FAIL: 500,
    WARNING: 301
};

modal_status = {
    SUCCESS: "success",
    FAIL: "error",
    WARNING: "warning"
};