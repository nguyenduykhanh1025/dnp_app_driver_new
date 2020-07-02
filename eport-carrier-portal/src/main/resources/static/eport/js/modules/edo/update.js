const PREFIX = "/edo";
var hot;
var statusTable = false;
var dataObj = null;
var countFile = 0;
var temp = 0;

var myAvatarzone = new Dropzone("#bannarzone", {
    url: PREFIX + "/file",
    method: "post",
    paramName: "file",
    maxFiles: 10,
    maxFilesize: 10, //MB
    autoProcessQueue: false,
    //acceptedFiles: ".edi",
    parallelUploads: 10,
    addRemoveLinks: true,
    //previewsContainer:"#viewEdi",// previews Content
    dictDefaultMessage: 'KÉO THẢ FILE, HOẶC NHẤP VÀO ĐÂY ĐỂ NHẬP EDI',
    // dictMaxFilesExceeded: "You can upload only one!",
    dictResponseError: 'Upload error!',
    dictInvalidFileType: "Invalid EDI file. Please upload txt file only.",
    // dictFallbackMessage: "Browser not support",
    dictFileTooBig: "File max size!",
    init: function() {
        this.on("addedfile", function(file) {
            countFile += 1;
            const reader = new FileReader();
            reader.onload = () => {
                const fileAsBinaryString = reader.result;
                $.ajax({ 
                    type: "POST", 
                    async: false, 
                    url: PREFIX + "/readEdiOnly", 
                    data: { 
                        fileContent : fileAsBinaryString,
                    }
                }).done(function( data ) { 
                    if (data != "") {
                        // let rs = JSON.parse(data.xhr.response);
                        if (dataObj == null) {
                            dataObj = data;
                            dataObj.forEach(element => {
                                element["file"] = file.upload.uuid;
                            });
        
                        } else {
                            data.forEach(element => {
                                element["file"] = file.upload.uuid;
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
            };
            reader.onabort = () => console.log('file reading was aborted');
            reader.onerror = () => console.log('file reading has failed');
    
            reader.readAsBinaryString(file);
           
           
       
        });
        this.on("success", function(data) {
            temp += 1;
            if(temp >= countFile)
            {
                $.modal.alertSuccess("Import thành công");
                $.modal.reload();
            }  
        });
        this.on("error", function(file, data) {


        });
        this.on("removedfile", function(file) {
            let fileId = file.upload.uuid;
            console.log(fileId);
            let i = 0;
            for (i; i < dataObj.length; i++) {
                if (dataObj[i].file == fileId) {
                    dataObj.splice(i, 1);
                    i--;
                }
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

$("#submitFile").click( function (e) {
    e.preventDefault();
    myAvatarzone.processQueue();
    $.modal.loading("Đang xử lý");
});