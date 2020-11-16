var PREFIX = ctx + "support-request/dangerous";

/**
 * @param {none}
 * @description handle event click btn Gửi
 * @author Khanh
 */
function submitHandler() {
  $.modal.loading("Đang xử lý ...");
  layer.close(layer.index);
  console.log(shipmentDetailIds);
  $.ajax({
    url: PREFIX + "/reject",
    method: "POST",
    data: {
      shipmentDetailIds: shipmentDetailIds,
    },
    success: function (res) {
      sendComment();
      parent.handleLoadTableFromModel();
      if (res.code == 0) {
        $.modal.alertSuccess(res.msg);
        onCloseModel();
      } else {
        $.modal.alertError(res.msg);
      }
    },
    error: function (data) {
      onCloseModel();
    },
  });
}

/**
 * @author Khanh
 * @description handle click close model
 */
function onCloseModel() {
  $.modal.close();
}

/**
 * @param {none}
 * @description Call api to add comment to server
 * @author Khanh
 */
function sendComment() {
  let req = {
    topic: "Lí do từ chối xác nhận yêu cầu",
    content: $("#content").val(),
    shipmentId: `${shipmentId}`,
    logisticGroupId: `${logisticGroupId}`,
  };

  $.ajax({
    url: PREFIX + "/shipment/comment",
    type: "post",
    contentType: "application/json",
    data: JSON.stringify(req),
    beforeSend: function () {
      $.modal.loading("Đang xử lý, vui lòng chờ...");
    },
    success: function (result) {
      $.modal.closeLoading();
      if (result.code == 0) {
        parent.handleLoadCommentFromModelReject(result.shipmentCommentId);
        $.modal.msgSuccess("Gửi thành công.");
        $("#topic").textbox("setText", "");
        $(".summernote").summernote("code", "");
      } else {
        $.modal.msgError("Gửi thất bại.");
      }
    },
    error: function (error) {
      $.modal.closeLoading();
      $.modal.msgError("Gửi thất bại.");
    },
  });
}




