var prefix = ctx + "carrier/do";
  $(function () {
	$("#dg").datagrid({
      url: ctx + "carrier/do/list",
      height: heightInfo,
      singleSelect:true,
      collapsible:true,
      clientPaging: false,
      pagination: true,
      rownumbers: true,
      pageSize: 50,
      nowrap: false,
      striped: true,
      loadMsg: " Đang xử lý...",
      loader: function (param, success, error) {
        var opts = $(this).datagrid("options");
        if (!opts.url) return false;
        $.ajax({
          type: opts.method,
          url: opts.url,
          data: {
            pageNum: param.page,
            pageSize: param.rows,
            orderByColumn: param.sort,
            isAsc: param.order,
            fromDate: $("#fromDate").val() == null ? "" : $("#fromDate").val(),
            toDate: $("#toDate").val() == null ? "" : $("#toDate").val(),
            voyageNo: $("#voyageNo").val() == null ? "" : $("#voyageNo").val(),
            blNo: $("#blNo").val() == null ? "" : $("#blNo").val(),
          },
          dataType: "json",
              success: function (data) {
                success(data);
              },
              error: function () {
                error.apply(this, arguments);
              },
            });
          },
        });
  });
  // **********************************
  // $("#searchForm").css({ height: 40 });

  function formatDate(value) {
    var date = new Date(value);
    var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
	var month = date.getMonth() + 1;
	var monthText = month < 10 ? "0" + month : month;
	return day +"/" +  monthText +"/" + date.getFullYear();
  }

  function formatStatus(value) {
    if (value != 0) {
      return "<span class='label label-success'>Đã làm lệnh</span>"
	}
	return "<span class='label label-default'>Chưa làm lệnh</span>"
  }
  
  function formatBL(value) {
	  return "<a onclick='viewBL(\""+value+"\")'>" + value + "</a>";
  }

  function searchDo() {
    var dg = $("#dg").datagrid({
	url: ctx + "carrier/do/list",
	singleSelection: true,
	clientPaging: false,
	pagination: true,
	rownumbers: true,
	pageSize: 50,
	loader: function (param, success, error) {
    var opts = $(this).datagrid("options");
    if (!opts.url) return false;
    $.ajax({
      type: opts.method,
      url: opts.url,
      data: {
        pageNum: param.page,
        pageSize: param.rows,
        orderByColumn: param.sort,
        isAsc: param.order,
        fromDate: $("#fromDate").val() == null ? "" : $("#fromDate").val(),
        toDate: $("#toDate").val() == null ? "" : $("#toDate").val(),
        voyageNo: $("#voyageNo").val() == null ? "" : $("#voyageNo").val(),
        blNo: $("#blNo").val() == null ? "" : $("#blNo").val(),
      },
      dataType: "json",
          success: function (data) {
            success(data);
          },
          error: function () {
            error.apply(this, arguments);
          },
        });
      },
    });
  }
  
  document.getElementById("voyageNo").addEventListener("keyup", function (event) {
	event.preventDefault();
	if (event.keyCode === 13) {
    $("#searchBtn").click();
      }
    });
  document.getElementById("blNo").addEventListener("keyup", function (event) {
    event.preventDefault();
    if (event.keyCode === 13) {
    $("#searchBtn").click();
      }
  });
  
  function viewBL(value) {
    $.modal.openTab("BL#"+value,"/carrier/do/viewbl/"+value);
  }
  
	// add full size do
  function addDo(id) {
    $.modal.openTab("Thêm DO","/carrier/do/add");
  }
  
  function addChangeExpired(id) {
	table.set();
	var url = $.common.isEmpty(id) ? table.options.createUrl : table.options.createUrl.replace("{id}", id);
	$.modal.openDo("Thay đổi hạn lệnh", url, 600, 400);
  }
  
	// 弹出层指定宽度
  function openDo(title, url, width, height, callback) {
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
  		// 不固定
  		maxmin: true,
  		shade: 0.3,
  		title: title,
  		content: url,
  	    // 弹层外区域关闭
  		shadeClose: true,
  		yes: callback,
  	    cancel: function(index) {
  	        return true;
  	    }
  	});
  }

