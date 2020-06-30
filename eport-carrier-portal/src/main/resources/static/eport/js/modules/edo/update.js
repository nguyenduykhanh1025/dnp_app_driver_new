const PREFIX = "/edo";
var hot;
var statusTable = false;
var dataObj;

var myAvatarzone = new Dropzone("#bannarzone", {
    url: PREFIX + "/file",
    method: "post",
    paramName: "file",
    maxFiles: 55,
    maxFilesize: 2, //MB
    //acceptedFiles: ".edi",
    addRemoveLinks: true,
    //parallelUploads: 1,//Maxfile upload
    //previewsContainer:"#viewEdi",// previews Content
    dictDefaultMessage: 'KÉO THẢ FILE, HOẶC NHẤP VÀO ĐÂY ĐỂ NHẬP EDI',
    // dictMaxFilesExceeded: "You can upload only one!",
    dictResponseError: 'Upload error!',
    dictInvalidFileType: "Invalid EDI file. Please upload txt file only.",
    // dictFallbackMessage: "Browser not support",
    dictFileTooBig: "File max size!",
    init: function () {
        this.on("addedfile", function (file) {

        });
        this.on("success", function (data) {
            if (data != "") {
                let rs = JSON.parse(data.xhr.response);
                if (dataObj == null) {
                    dataObj = rs;
                    dataObj.forEach(element => {
                        element["file"] = data.upload.uuid;
                    });

                } else {
                    rs.forEach(element => {
                        element["file"] = data.upload.uuid;
                        dataObj.push(element);

                    });
                }
            }
            if (statusTable == true) {
                hot.render();
                return;
            }
            loadView();
        });
        this.on("error", function (file, data) {


        });
        this.on("removedfile", function (file) {
            let fileId = file.upload.uuid;

            let i = 0;
            for (i; i < dataObj.length; i++) {
                if (dataObj[i].file == fileId) {
                    dataObj.splice(i, 1);
                    i--;
                }
            }
            if (i == 0) {
                console.log("Remove");
            }
            hot.render();
        });
    }
});

function loadView() {
    var container = document.getElementById('viewEdi');
    hot = new Handsontable(container, {
        data: dataObj,


        columns: [{
                data: 'containerNumber',
                type: 'text',
                editor: false
            },
            {
                data: 'billOfLading',
                type: 'text',
                editor: false
            },
            {
                data: 'businessUnit',
                type: 'text',
                editor: false
            },
            {
                data: 'orderNumber',
                type: 'text',
                editor: false
            },
            {
                data: 'consignee',
                type: 'text',
                editor: false
            },
            {
                data: 'expiredDem',
                type: 'date',
                dateFormat: 'MM/DD/YYYY',
                editor: false
            },
            {
                data: 'emptyContDepot',
                type: 'text',
                editor: false
            },
            {
                data: 'detFreeTime',
                type: 'numeric',
                editor: false
            }
        ],
        rowHeaders: true,
        colHeaders: [
            'Số Cont',
            'Số Bill',
            'Chủ khai thác',
            'Order Number',
            'Người nhận hàng',
            'Ngày hết hạn lưu cont',
            'Nơi hạ rỗng',
            'Số ngày miễn lưu vỏ'
        ],

    });
    statusTable = true;
}
