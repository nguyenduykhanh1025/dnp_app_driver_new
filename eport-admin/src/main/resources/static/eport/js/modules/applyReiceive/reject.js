/*var PREFIX = ctx + "system/checkCont"; */

const PREFIX = ctx + "system/checkCont";
const HIST_PREFIX = ctx + "om/controlling";

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
       
        
        $.modal.close();
      } else {
        $.modal.alertError(res.msg);
      }
    },
    error: function (data) {
      onCloseModel();
    },
  });
}

function onCloseModel() {
  $.modal.close();
}

function loadListComment(shipmentCommentId) {
	  let req = {
	    serviceType: 1,
	    shipmentId: shipmentSelected.id,
	  };
	  $.ajax({
	    url: ctx + "shipment-comment/shipment/list",
	    method: "POST",
	    contentType: "application/json",
	    data: JSON.stringify(req),
	    success: function (data) {
	      if (data.code == 0) {
	        let html = "";
	        // set title for panel comment
	        let commentTitle = '<span style="color: black">Hỗ Trợ<span>';
	        let commentNumber = 0;
	        if (data.shipmentComments != null) {
	          data.shipmentComments.forEach(function (element, index) {
	            let createTime = element.createTime;
	            let date = "";
	            let time = "";
	            if (createTime) {
	              date =
	                createTime.substring(8, 10) +
	                "/" +
	                createTime.substring(5, 7) +
	                "/" +
	                createTime.substring(0, 4);
	              time = createTime.substring(10, 19);
	            }

	            let resolvedBackground = "";
	            if (
	              (shipmentCommentId && shipmentCommentId == element.id) ||
	              !element.resolvedFlg
	            ) {
	              resolvedBackground = 'style="background-color: #ececec;"';
	              commentNumber++;
	            }

	            html += "<div " + resolvedBackground + ">";
	            // User name comment and date time comment
	            html +=
	              '<div><i style="font-size: 15px; color: #015198;" class="fa fa-user-circle" aria-hidden="true"></i><span> <a>' +
	              element.userName +
	              " (" +
	              element.userAlias +
	              ")</a>: <i>" +
	              date +
	              " at " +
	              time +
	              "</i></span></div>";
	            // Topic comment
	            html +=
	              "<div><span><strong>Yêu cầu:</strong> " +
	              element.topic +
	              "</span></div>";
	            // Content comment
	            html +=
	              "<div><span>" +
	              element.content.replaceAll("#{domain}", domain) +
	              "</span></div>";
	            html += "</div>";
	            html += "<hr>";
	          });
	        }
	        commentTitle +=
	          ' <span class="round-notify-count">' + commentNumber + "</span>";
	        $("#right-layout")
	          .layout("panel", "expandSouth")
	          .panel("setTitle", commentTitle);
	        $("#commentList").html(html);
	        // $("#comment-div").animate({ scrollTop: $("#comment-div")[0].scrollHeight}, 1000);
	      }
	    },
	  });
	}

 
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
        //parent.handleLoadCommentFromModelReject(result.shipmentCommentId);
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


function handleLoadTableFromModel() {
	  loadTable();
	}

function handleLoadCommentFromModelReject(shipmentCommentId) {
	  loadListComment(shipmentCommentId);
	}