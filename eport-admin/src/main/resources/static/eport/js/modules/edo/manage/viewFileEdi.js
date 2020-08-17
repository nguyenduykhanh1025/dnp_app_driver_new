const PREFIX = ctx + "edo/manage";
var hot;
var statusTable = false;
var dataObj = null;
var countFile = 0;
var temp = 0;
var checkFileInDropZone = 0;
$(function () {
    loadTable();
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
    init: function () {
        this.on("addedfile", function (file) {
            checkFileInDropZone += 1;
            const reader = new FileReader();
            reader.onload = () => {
                const fileAsBinaryString = reader.result;
                $.ajax({
                    type: "POST",
                    async: false,
                    url: PREFIX + "/readEdiOnly",
                    data: {
                        fileContent: fileAsBinaryString,
                    }
                }).done(function (data) {
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
                        loadLocal(dataObj);
                        return;
                    }
                    loadLocal(dataObj);
                });
            };
            reader.readAsBinaryString(file);
        });
        this.on("maxfilesexceeded", function (file) {
            $.modal.alertWarning("Hãy chọn 10 file mỗi lần upload!");
            this.removeFile(file);
        });
        this.on('sending', function (data) {
            countFile += 1;
        })
        this.on("success", function (data) {
            temp += 1;
            if (temp >= countFile) {
                $.modal.alertSuccess("Nhập thành công");
                setTimeout(function () {
                    $.modal.reload();
                }, 1000);
            }
        })
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
            loadLocal(dataObj);
        });
    }
});


function loadTable() {
    $('#dg').datagrid({
        clientPaging: true,
        collapsible: true,
        height: $(document).height() - 295,
    });
}

function loadLocal(data) {

    $('#dg').datagrid('loadData', data);
}

function printEdoPDF() {
    if (!dataObj) {
        $.modal.alertError("Hãy chọn file để in.");
        return
    }
    $.ajax({
        url: ctx + "edo/print/post-data",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(dataObj),
        success: function (result) {
            if (result.code == 0) {
                $.modal.openTab("In Phiếu eDO", ctx + "edo/print/view/" + result.key)
            } else {
                $.modal.alertError(result.msg);
            }
        }
    })
}