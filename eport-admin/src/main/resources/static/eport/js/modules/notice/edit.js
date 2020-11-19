const PREFIX = ctx + 'system/notice';
var dateStart, dateFinish;

$('.summernote').summernote({
    placeholder: 'Nhập vào nội dung bản tin',
    height: 200,
    dialogsInBody: true,
    callbacks: {
        onImageUpload: function(files, editor, welEditable) {
            let file = files[0];
            getBase64(file).then(
                url => {
                    $('.summernote').summernote('insertImage', url, 'image');
                }
            );
        }
    }
});

$('#dateStart').datebox({
    onSelect: function (date) {
        date.setHours(0, 0, 0);
        dateStart = date;
        if (dateFinish != null && dateStart.getTime() > dateFinish.getTime()) {
            $.modal.alertWarning('Ngày bắt đầu không được lớn hơn ngày kết thúc!');
        }
        return date;
    }
});

$('#dateFinish').datebox({
    onSelect: function (date) {
        date.setHours(23, 59, 59);
        dateFinish = date;
        if (dateStart != null && dateStart.getTime() > dateFinish.getTime()) {
            $.modal.alertWarning('Ngày kết thúc không được bé hơn ngày kết thúc!');
        }
        return date;
    }
});

function dateformatter(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    return (d < 10 ? ('0' + d) : d) + '/' + (m < 10 ? ('0' + m) : m) + '/' + y;
}
function dateparser(s) {
    var ss = (s.split('/'));
    var d = parseInt(ss[0], 10);
    var m = parseInt(ss[1], 10);
    var y = parseInt(ss[2], 10);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
        return new Date(y, m - 1, d);
    }
}

$("#form-notice-edit").validate({
    focusCleanup: true
});

$(document).ready(function() {
    $('#noticeTitle').val(notice.noticeTitle),
    $('.summernote').summernote('code', notice.noticeContent),
    $('select[name=noticeType]').val(notice.noticeType),
    $("input[name=active][value=" + notice.active + "]").attr('checked', 'checked');
    dateStart = new Date(notice.dateStart);
    dateFinish = new Date(notice.dateFinish);
    $('#dateStart').datebox('setValue', dateStart.getDate() + '/' + (dateStart.getMonth() + 1) + '/' + dateStart.getFullYear());
    $('#dateFinish').datebox('setValue', dateFinish.getDate() + '/' + (dateFinish.getMonth() + 1) + '/' + dateFinish.getFullYear());
});

function submitHandler() {
    if ($.validate.form()) {
        if (dateStart == null) {
            $.modal.alertWarning('Bạn chưa nhập ngày bắt đầu bản tin.');
            return;
        }
        if (dateFinish == null) {
            $.modal.alertWarning('Bạn chưa nhập ngày kết thúc bản tin.');
            return;
        }
        if (dateStart.getTime() > dateFinish.getTime()) {
            $.modal.alertWarning('Ngày bắt đầu không được lớn hơn ngày kết thúc!');
            return;
        }
        let data = {
            noticeId : notice.noticeId,
            noticeTitle : $('#noticeTitle').val(),
            noticeContent : $('.summernote').summernote('code'),
            noticeType : $('select[name=noticeType]').val(),
            active : $('#active').prop('checked') ? 1 : 0,
            dateStart : dateStart.getTime(),
            dateFinish : dateFinish.getTime()
        }
        $.ajax({
            url: PREFIX + '/edit',
            method: 'POST',
            contentType: "application/json",
            data: JSON.stringify(data),
            beforeSend: function () {
                $.modal.loading("Đang xử lý, vui lòng chờ...");
                $.modal.disable();
            },
            success: function(res) {
                $.modal.closeLoading();
                parent.reloadTable(res);
                $.modal.close();
            },
            error: function(err) {
                $.modal.closeLoading();
                parent.reloadTable(err);
                $.modal.close();
            }
        });
    }
}