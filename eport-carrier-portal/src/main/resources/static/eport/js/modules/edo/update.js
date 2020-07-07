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
    let checkDate = validateDateSearch(formatDate(expiredDem),formatDate($("#expiredDem").val()));
    console.log("checkDate",checkDate);
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
                detFreeTime : $("#detFreeTime").val()
                // $("#toDate").val() == null ? "" : $("#toDate").val()
            },
            success: function (data) {
              console.log(data);
              if (data.code == 0) {
                $.modal.confirm("Cập nhật DO thành công!", function () {}, {
                  title: "Thông báo",
                  btn: ["Đồng Ý"],
                })
                $.modal.reload();
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


  function validateDateSearch(fromDate, toDate) {

    if (fromDate == "" || toDate == "") {
      return 1;
    }
    var formatDate1 = new Date(fromDate);
    var toDate1 = new Date(toDate);
    var offset = toDate1.getTime() - formatDate1.getTime();
    var totalDays = Math.round(offset / 1000 / 60 / 60 / 24);
    console.log(fromDate,toDate)
    if (totalDays < 0) {
      return -1;
    } else if (totalDays <= 40) {
      return 1;
    }
    return 0;

  }

