const PREFIX = ctx + "mc/plan/management";
const SEARCH_HEIGHT = $(".main-body__search-wrapper").height();
var pickupHistory = new Object();
pickupHistory.params = new Object();

$( document ).ready(function() {
    $(".main-body").layout();

  $(".collapse").click(function () {
    $(".main-body__search-wrapper").height(15);
    $(".main-body__search-wrapper--container").hide();
    $(this).hide();
    $(".uncollapse").show();
  })

  $(".uncollapse").click(function () {
    $(".main-body__search-wrapper").height(SEARCH_HEIGHT);
    $(".main-body__search-wrapper--container").show();
    $(this).hide();
    $(".collapse").show();
  });

  $(".left-side__collapse").click(function () {
    $('#main-layout').layout('collapse', 'west');
  });

  loadTableLeft();
});

function loadTableLeft() {
    $("#dg-left").datagrid({
        url: PREFIX + '/vessel-voyage/list',
        height: $(document).height() - $(".main-body__search-wrapper").height() - 70,
        method: 'POST',
        multipleSelect: true,
        collapsible: true,
        clientPaging: false,
        pagination: true,
        rownumbers: true,
        
        pageSize: 50,
        nowrap: true,
        striped: true,
        loadMsg: " Đang xử lý...",
        loader: function (param, success, error) {
            var opts = $(this).datagrid("options");
            if (!opts.url) return false;
            $.ajax({
                type: opts.method,
                url: opts.url,
                contentType: "application/json",
                data: JSON.stringify({
                    pageNum: param.page,
                    pageSize: param.rows,
                    orderByColumn: param.sort,
                    isAsc: param.order,
                    data: pickupHistory
                }),
                success: function (res) {
                    if (res.code == 0 && res.vesselVoyageMcs) {
                        success(res.vesselVoyageMcs);
                    } else {
                        success([]);
                    }
                },
                error: function () {
                    error.apply(this, arguments);
                },
            });
        },
    });
}

function loadTableRight() {

}