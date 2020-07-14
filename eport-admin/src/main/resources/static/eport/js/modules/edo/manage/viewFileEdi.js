const PREFIX = ctx + "edo/manage";
var hot;
var statusTable = false;
var dataObj = null;
var countFile = 0;
var temp = 0;
var checkFileInDropZone = 0;
$(function(){
    
});
var myAvatarzone = new Dropzone("#bannarzone", {
    url: PREFIX + "/file",
    method: "post",
    paramName: "file",
    maxFiles: 100,
    maxFilesize: 10, //MB
    autoProcessQueue: false,
    acceptedFiles: ".edi,.TO_VN",
    parallelUploads: 100,
    addRemoveLinks: true,
    dictDefaultMessage: 'Choose a file EDI, or drag it here !',
    dictResponseError: 'Upload error!',
    dictInvalidFileType: "Invalid EDI file. Please upload txt file only.",
    dictFileTooBig: "File max size!",
    init: function() {
        this.on("addedfile", function(file) {
            checkFileInDropZone += 1;
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
                        if (dataObj == null) {
                            dataObj = data;
                            console.log(data);
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
        this.on("maxfilesexceeded", function(file){
            $.modal.alertWarning("Bạn chỉ được nhập 10 file mỗi lần upload!");
            this.removeFile(file);
        });
        this.on('sending',function(data){
            countFile += 1;
            console.log("countFile" + countFile);
        })
        this.on("success", function(data) {
            temp += 1;
            if(temp >= countFile)
            {
                $.modal.alertSuccess("Import thành công");
                setTimeout(function(){
                    $.modal.reload();
                },1000);
            }  
        })
        this.on("error", function(file, data) {


        });
        this.on("removedfile", function(file) {
            let fileId = file.upload.uuid;
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
        width: '100%',
        height: 320,
        rowHeights: 23,
        rowHeaders: true,
        colHeaders: true,

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
                data: 'voyNo',
                type: 'text',
                editor: false
            },
            {
                data: 'vessel',
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
                data: 'emptyContainerDepot',
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
            'Số chuyến',
            'Số tàu',
            'Người nhận hàng',
            'Ngày hết hạn lưu cont',
            'Nơi hạ rỗng',
            'Số ngày miễn lưu vỏ'
        ],

    });
    statusTable = true;
}

$("#submitFile").click( function (e) {
    $.modal.alertWarning("Chức năng này chưa mở trên hệ thống ! ");
        return;
    // if(checkFileInDropZone == 0)
    // {
    //     $.modal.alertWarning("Bạn chưa nhập file nào lên hệ thống! ");
    //     return;
    // }
    e.preventDefault();
    myAvatarzone.processQueue();
    //$.modal.loading("Đang xử lý");
});