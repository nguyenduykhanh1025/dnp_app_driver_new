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
    acceptedFiles: ".edi,.TO_VN,.evu,.txt,",
    parallelUploads: 100,
    addRemoveLinks: true,
    dictDefaultMessage: 'Click để chọn file, hoặc kéo thả file vào đây',
    dictResponseError: 'Upload lỗi!',
    dictInvalidFileType: "File EDI không đúng. Hãy chọn file được hỗ trợ.",
    dictFileTooBig: "Dung lượng file quá lớn!",
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
            //reader.onabort = () => console.log('file reading was aborted');
            //reader.onerror = () => console.log('file reading has failed');
            reader.readAsBinaryString(file);
        });
        this.on("maxfilesexceeded", function(file){
            $.modal.alertWarning("Hãy chọn 10 file mỗi lần upload!");
            this.removeFile(file);
        });
        this.on('sending',function(data){
            countFile += 1;
        })
        this.on("success", function(data) {
            temp += 1;
            if(temp >= countFile)
            {
                $.modal.alertSuccess("Nhập thành công");
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
        columns: [{
                data: 'containerNumber',
                type: 'text',
                editor: false
            },
            {
                data: 'sztp',
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
                data: 'carrierCode',
                type: 'text',
                editor: false
            },
            {
                data: 'orderNumber',
                type: 'text',
                editor: false
            },
            {
                data: 'vessel',
                type: 'text',
                editor: false
            },
            {
                data: 'vesselNo',
                type: 'text',
                editor: false
            },
            {
                data: 'voyNo',
                type: 'text',
                editor: false
            },
            {
                data: 'pol',
                type: 'text',
                editor: false
            },
            {
                data: 'pod',
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
                type: 'text',
                editor: false
            },
            {
                data: 'fileCreateTime',
                type: 'text',
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
            'Sztp',
            'Số Bill',
            'Chủ khai thác',
            'OPR Code',
            'Order Number',
            'Tên tàu',
            'Mã tàu',
            'Số chuyến',
            'Cảng xếp hàng',
            'Cảng dỡ hàng',
            'Chủ hàng',
            'Ngày hết hạn lưu cont',
            'Ngày tạo file',
            'Nơi hạ rỗng',
            'Số ngày miễn lưu vỏ'
        ],
    });
    statusTable = true;
  
}

$('#export-file').click(function() {
    console.log("dataObj", dataObj)
    $.ajax({
        url : PREFIX + "/export",
        method : "POST",
        contentType: "application/json",
        dataType: 'json',
        data: dataObj
    }).done(function(result){
        console.log(result);
    });
});
function printEdoPDF() {
	if(!dataObj){
		$.modal.alertError("Hãy chọn file để in.");
		return
	}
    $.ajax({
        url : ctx + "edo/print/post-data",
        method : "POST",
        contentType: "application/json",
        data: JSON.stringify(dataObj),
        success: function (result) {
            if (result.code == 0) {
            	$.modal.openTab("In Phiếu eDO", ctx + "edo/print/view/"+ result.key)
            } else {
                $.modal.alertError(result.msg);
            }
        }
    })
}
