const PREFIX = ctx + "system/notice";
var dateStart, dateFinish;

$('.summernote').summernote({
    placeholder: 'Nhập vào nội dung bản tin',
    height: 200
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
    var ss = (s.split('\.'));
    var d = parseInt(ss[0], 10);
    var m = parseInt(ss[1], 10);
    var y = parseInt(ss[2], 10);
    if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
        return new Date(y, m - 1, d);
    }
}

$("#form-notice-add").validate({
    focusCleanup: true
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
            noticeTitle : $('#noticeTitle').val(),
            noticeContent : $('.summernote').summernote('code'),
            noticeType : $('select[name=noticeType]').val(),
            active : $('#active').prop('checked') ? 1 : 0,
            dateStart : dateStart.getTime(),
            dateFinish : dateFinish.getTime()
        }
        $.ajax({
            url: PREFIX + '/add',
            method: 'POST',
            contentType: "application/json",
            beforeSend: function () {
                $.modal.loading("Đang xử lý, vui lòng chờ...");
                $.modal.disable();
            },
            data: JSON.stringify(data),
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