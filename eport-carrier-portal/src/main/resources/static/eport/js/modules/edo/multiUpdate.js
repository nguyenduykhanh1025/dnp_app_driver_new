const PREFIX = ctx + "edo";

function confirm()
{
    $.modal.confirm(
        "Bạn có chắc chắn muốn cập nhật DO không? Hành động này không thể hoàn tác",
        function () {
          $.ajax({
            url: PREFIX + "/multiUpdate",
            method: "post",
            dataType: "json",
            data : {
                ids : ids,
                expiredDem : formatDateForSubmit($("#expiredDem").val()),
                detFreeTime : $("#detFreeTime").val(),
                emptyContainerDepot : $("#emptyContainerDepot").val()
            },
            success: function (data) {
              if (data.code == 0) {
                  $.modal.alertSuccess("Cập nhật thành công");
              } else {
                $.modal.alertError(data.msg)
              }
              
            },
            error: function (data) {
              $.modal.alertError(
                "Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin."
              )
            },
          })
        },
        { title: "Xác nhận cập nhật DO", btn: ["Đồng Ý", "Hủy Bỏ"] }
      )
}

function formatDateForSubmit(value) {
    var newdate = value.split("/").reverse();
    var date = new Date(newdate)
    var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    var month = date.getMonth() + 1;
    var monthText = month < 10 ? "0" + month : month;
    return date.getFullYear() + "-" + monthText + "-" + day;
  }

function closeForm()
{
    $.modal.close();
}