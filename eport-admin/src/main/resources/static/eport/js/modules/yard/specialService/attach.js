var prefix = ctx + "yard/monitor/special-service";
var shipmentFileIds = [];

$(document).ready(function () {

    if (files != null) {
        let htmlInit = '';
        files.forEach(function (element, index) {
            shipmentFileIds.push(element.id);
            htmlInit = `<div class="preview-block">
                    <a href="${element.path}" target="_blank"><img src="` + ctx + `img/document.png" style="width: 68px; height: 70px;" alt="Tài liệu" /></a>
                    <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` + element.id + `)" disabled>
                    <span aria-hidden="true">&times;</span>
                    </button>
                </div>`;
            $('.preview-container').append(htmlInit);
        });
    }
});





