"use strict";
var notiContent = CKEDITOR.replace("content");
const PREFIX = ctx + "notifications";

$("input[type=checkbox]").change(function () {
  let selected = $(this).val();
  if (selected == 0) {
    //Select total checkbox
    $(":checkbox[value=1], :checkbox[value=2], :checkbox[value=3]").prop("checked", $(this).prop("checked"));
    return;
  }
  let checkedCheckbox = $(":checkbox:checked").length;
  let totalCheckbox = $(":checkbox").length;
  //Handle total checkbox when select or deselect other checkbox
  if (checkedCheckbox == totalCheckbox - 1) {
    if ($(":checkbox[value=0]").prop("checked")) {
      $(":checkbox[value=0]").prop("checked", 0);
    } else {
      $(":checkbox[value=0]").prop("checked", 1);
    }
  }
});

$(".btn-send").click(function () {
  let receivers = [];
  let title = $(":text[name=title]");
  let content = notiContent.getData();

  //Validate data 
  let success = true;
  hideErrorLabel();
  if ($(":checkbox:checked").length == 0) {
    $(".receiver-error").show();
    success = false;
  }
  if (title.val().trim() === "") {
    $(".title-error").show();
    title.focus();
    success = false;
  }
  if (content.trim() === "") {
    $(".content-error").show();
    success = false;
  }
  if (!success) {
    $.modal.msgError("Có lỗi xảy ra, vui lòng thử lại");
    return;
  }

  $(":checkbox:checked").each(function () {
    receivers += "," + $(this).val();
  });
  
  receivers = receivers.substr(1);
  $.ajax({
    method: "POST",
    url: PREFIX,
    data: {
      title: title.val(),
      content: content,
      receiverGroups: receivers
    },
    success: function(result){
      console.log(result);
    }
  });
});

function hideErrorLabel() {
  $(".content-error").hide();
  $(".title-error").hide();
  $(".receiver-error").hide();
}
