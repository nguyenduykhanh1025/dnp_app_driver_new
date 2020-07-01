CKEDITOR.replace( 'content' );

$("input[type=checkbox]").change( function() {
    let selected = $(this).val();
    if (selected == !1) {
        $(":checkbox[value=1], :checkbox[value=2], :checkbox[value=3]").prop("checked", $(this).prop("checked"));
        return;
    }
    let count = $(":checkbox:checked").length;
    if (count < $(":checkbox").length) {
        $(":checkbox[value=0]").prop("checked", false);
    }
})