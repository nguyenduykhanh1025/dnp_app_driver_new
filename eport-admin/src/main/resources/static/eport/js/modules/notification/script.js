var notiContent = CKEDITOR.replace("content");

$("input[type=checkbox]").change(function () {
  let selected = $(this).val();
  if (selected == 0) {
    //Select total checkbox
    $(":checkbox[value=1], :checkbox[value=2], :checkbox[value=3]").prop("checked",$(this).prop("checked"));
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

$(".btn-send").click(function (){
    let receivers = [];
    let title = $(":text[name=title]").val();
    let content = notiContent.getData();
    $(":checkbox:checked").each(function() {
        receivers.push($(this).val());
    })
    console.log(content);
})
