var prefix = "/edo"
$(function() {
    $("#containerNumber").text(containerNumber);
    $("#expiredDem").val(formatDate(expiredDem));
    $("#detFreeTime").val(detFreeTime);
})

function formatDate(value) {
    if (value == null) {
      return;
    }
    var date = new Date(value)
    var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    var month = date.getMonth() + 1;
    var monthText = month < 10 ? "0" + month : month;
    return day + "/" + monthText + "/" + date.getFullYear();
  }

function closeForm()
{
    $.modal.close();
}

function confirm()
{
    $.modal.confirm(
        "Bạn có chắc chắn muốn cập nhật DO không?",
        function () {
          $.ajax({
            url: prefix + "/updateEdo",
            method: "post",
            dataType: "json",
            data : {
                id : id,
                expiredDem : formatDateForSubmit($("#expiredDem").val()),
                detFreeTime : $("#detFreeTime").val() == detFreeTime ? "" :  $("#detFreeTime").val()
            },
            success: function (data) {
              console.log(data);
              if (data.code == 0) {
                $.modal.confirm("Cập nhật DO thành công!", function () {}, {
                  title: "Thông báo",
                  btn: ["Đồng Ý"],
                })
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
    let checkDate = validateUpdateDate(formatDate(expiredDem),$("#expiredDem").val());
    if(checkDate == 1)
    {
      return;
    }
    if (value == null) {
      return;
    }
    var newdate = value.split("/").reverse();
    var date = new Date(newdate)
    var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    var month = date.getMonth() + 1;
    var monthText = month < 10 ? "0" + month : month;
    return date.getFullYear() + "-" + monthText + "-" + day;
  }


  function validateUpdateDate(fromDate, toDate) {
    console.log(fromDate + "/" + toDate)
    if (fromDate == toDate) {
      return 1;
    }
    return 0;

  }


