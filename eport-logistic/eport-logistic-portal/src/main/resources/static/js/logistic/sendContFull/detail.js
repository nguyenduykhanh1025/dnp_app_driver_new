const PREFIX = ctx + "logistic/send-cont-full";

const KEY_FORM = {
  OVERSIZE: "oversize",
  DANGEROUS: "dangerous",
  ICE: "ice",
};

const CONT_SPECIAL_STATUS = {
  INIT: "I", // cont đã được lưu
  REQ: "R", // cont đã được yêu cầu xác nhận
  YES: "Y", // cont đã được phê duyệt yêu cầu xác nhận
  CANCEL: "C", // cont đã bị từ chối yêu cầu xác nhận
};

var shipmentFilePaths = { oversize: [], dangerous: [], ice: [] };

$("#form-detail-add").validate({
  onkeyup: false,
  focusCleanup: true,
});

$("#datetimepicker1").datetimepicker({
  format: "dd/mm/yyyy hh:ii",
  language: "vi_VN",
  minuteStep: 30,
  autoClose: true
});

$(document).ready(function () {
  initTabs();
  initValueToElementHTML();
  
  initOptionForSelectUNNOSelect();
  initOptionForSelectIMOSelect();
});

/**
 * * *Init Func* * *
 */

/*function initTabs() {
  var keySize = shipmentDetail.sztp.substring(2, 3);
  if (keySize == 'R') {// nếu cont lạnh thì show table
    $(".tab-label-2").css("display", 'none');
    $(".tab-label-3").css("display", 'none');
  }
  if (keySize == 'G') {
    $(".tab-label-1").css("display", 'none');
    $(".tab-label-2").css("display", 'none');
  }
  if (keySize == 'P' || keySize == 'U') {
    $(".tab-label-1").css("display", 'none');
    $(".tab-label-3").css("display", 'none');
  }
}*/

function initTabs() {
  var keySize = shipmentDetail.sztp.substring(2, 3);
  var cargoType = shipmentDetail.cargoType;
  //console.log(shipmentDetail.sztp.includes("R"));
  if (keySize == "R") {
    $(".tab-label-2").css("display", 'none');
    $(".tab-label-3").css("display", 'none');  
    $("reeferContainer").show();
    $("dangerousContainer").hide();
    $("oversizeContainer").hide(); 
    $("#tab-1").prop('checked', true);
    $("#tab-2").prop('checked', false);
    $("#tab-3").prop('checked', false);
  } 
  if(keySize == "G" && cargoType == "DG"){
    $(".tab-label-1").css("display", 'none');
    $(".tab-label-2").css("display", 'none'); 
    $("dangerousContainer").show();
    $("reeferContainer").hide();
    $("oversizeContainer").hide(); 
    $("#tab-3").prop('checked', true);
    $("#tab-1").prop('checked', false);
    $("#tab-2").prop('checked', false);
  }
   if(keySize == "P" || keySize == "U"){
    $(".tab-label-1").css("display", 'none');
    $(".tab-label-3").css("display", 'none'); 
    $("oversizeContainer").show();
    $("reeferContainer").hide();
    $("dangerousContainer").hide(); 
    $("#tab-2").prop('checked', true);
    $("#tab-1").prop('checked', false);
    $("#tab-3").prop('checked', false);
  }
}
/**
 * @author Khanh
 * @description create another values if exist from server
 */
function initValueToElementHTML() {
  if (shipmentDetail) {
    $("#containerNo").val(shipmentDetail.containerNo);
    $("#sztp").val(shipmentDetail.sztp);

    $('#oversizeRight').val(shipmentDetail.oversizeRight);
    $('#oversizeTop').val(shipmentDetail.oversizeTop);
    $('#oversizeLeft').val(shipmentDetail.oversizeLeft);
    $('#dgNameProduct').val(shipmentDetail.dangerousNameProduct);
    $('#dgPacking').val(shipmentDetail.dangerousPacking);

    const {
      temperature,
      daySetupTemperature,
      humidity,
      ventilation
    } = shipmentDetail;

    initElementHTMLInInformationCommonTab(
      temperature,
      daySetupTemperature,
      humidity,
      ventilation
    );

    /* initElementHTMLInOversizeTab(
       //oversize,
       oversizeType,
       oversizeTop,
       oversizeRight,
       oversizeLeft,
       oversizeFront,
       oversizeBack
     );
     initElementHTMLInDangerousTab(
       dangerous,
       dangerousImo,
       dangerousUnno,
       dangerousNameProduct,
       dangerousPacking
     );*/

    //"preview-container-dangerous",
    // initDropzone(
    //   "dropzoneOversize",
    //   "preview-container-oversize",
    //   "attachButtonOversize",
    //   KEY_FORM.OVERSIZE
    // );
    // initDropzone(
    //   "dropzoneDangerous",
    //   "preview-container-dangerous",
    //   "attachButtonDangerous",
    //   KEY_FORM.DANGEROUS
    // );
    // initDropzone(
    //   "dropzoneIce",
    //   "preview-container-ice",
    //   "attachButtonIce",
    //   KEY_FORM.ICE
    // );

  }
}

/**
 * @author Khanh
 * @description create another values on tab common if exist from server
 */
function initElementHTMLInInformationCommonTab(
  temperature,
  daySetupTemperature,
  humidity,
  ventilation
) {
  $("#temperature")
    .val(temperature ? temperature : null)
    .prop("disabled", !isContIce() ? true : false);
  $("#datetimepicker1 *")
    .css("pointer-events", !isContIce() ? "none" : "")
    .prop("disabled", !isContIce() ? true : false);
  $("#humidity").val(humidity ? humidity : null);
  $("#ventilation").val(ventilation ? ventilation : null);

  let daySetup = new Date(daySetupTemperature);
  $("#datetimepicker1").datetimepicker('setDate', daySetup);

  $("#attachButtonIce").prop("disabled", !isContIce());

  initFileIsExist("preview-container-ice", "R");

}

/**
 * @author Khanh
 * @description init dropzone + handle add file to client
 */

function initDropzone(
  idDropzone,
  classPreviewContainer,
  idButtonAttach,
  keyForm
) {
  let previewTemplate = "<span data-dz-name></span>";
  myDropzone = new Dropzone(`#${idDropzone}`, {
    url: PREFIX + "/cont-special/file",
    method: "post",
    paramName: "file",
    maxFiles: 5,
    maxFilesize: 10, //MB
    // autoProcessQueue: false,
    previewTemplate: previewTemplate,
    previewsContainer: `.${classPreviewContainer}`, // Define the container to display the previews
    clickable: `#${idButtonAttach}`, // Define the element that should be used as click trigger to select files.
    init: function () {
      this.on("maxfilesexceeded", function (file) {
        $.modal.alertError("Số lượng tệp đính kèm vượt số lượng cho phép.");
        this.removeFile(file);
      });
    },
    success: function (file, response) {
      if (response.code == 0) {
        $.modal.msgSuccess("Đính kèm tệp thành công.");
        shipmentFilePaths[`${keyForm}`].push(response.shipmentFileId);

        let html =
          `<div class="preview-block" style="width: 70px;float: left;">
          <a href=${ctx}${response.file} target="_blank"><img src="` +
          ctx +
          `img/document.png" alt="Tài liệu" style="max-width: 50px;"/></a>
                <button type="button" class="close ` +
          keyForm +
          `" aria-label="Close" onclick="removeImage(this,'${response.shipmentFileId}')" >
                <span aria-hidden="true">&times;</span>
                </button>
            </div>`;
        $(`.${classPreviewContainer}`).append(html);
      } else {
        $.modal.alertError("Đính kèm tệp thất bại, vui lòng thử lại sau.");
      }
    },
  });
}
/**
 * @author Khanh
 * @description create another values on tab oversize if exist from server
 */
function initElementHTMLInOversizeTab(
  oversize,
  oversizeType,
  oversizeTop,
  oversizeRight,
  oversizeLeft,
  oversizeFront,
  oversizeBack
) {
  $('input:radio[name="oversize"]')
    .filter('[value="T"]')
    .attr("checked", isContOversize());
  $('input:radio[name="oversize"]')
    .filter('[value="F"]')
    .prop("disabled", shipmentDetail.sztp.includes("P"))
    .attr("checked", !isContOversize());
  $('input:radio[name="oversize"]').change(function () {
    let isDisable = $(this).val() == "T" ? false : true;
    $(".ipCategoryOversize").prop("disabled", isDisable);
    $("#oversizeTop").prop("disabled", isDisable);
    $("#oversizeRight").prop("disabled", isDisable);
    $("#oversizeLeft").prop("disabled", isDisable);
    $("#oversizeFront").prop("disabled", isDisable);
    $("#oversizeBack").prop("disabled", isDisable);
    $("#attachButtonOversize").prop("disabled", isDisable);
  });

  let isDisable = !isContOversize();

  $(".ipCategoryOversize").prop("disabled", isDisable);
  if (oversizeType) {
    $(".ipCategoryOversize").each(function () {
      let value = $(this).val();
      oversizeType.split(",").forEach((item) => {
        if (item === value) {
          $(this).attr("checked", true);
        }
      });
    });
  }

  $("#oversizeTop").prop("disabled", isDisable).val(oversizeTop);
  $("#oversizeRight").prop("disabled", isDisable).val(oversizeRight);
  $("#oversizeLeft").prop("disabled", isDisable).val(oversizeLeft);
  $("#oversizeFront").prop("disabled", isDisable).val(oversizeFront);
  $("#oversizeBack").prop("disabled", isDisable).val(oversizeBack);
  $("#attachButtonOversize").prop("disabled", isDisable);

  initFileIsExist("preview-container-oversize", "O");
}

/**
 * @author Khanh
 * @description create another values on tab dangerous if exist from server
 */
function initElementHTMLInDangerousTab(
  dangerous,
  dangerousImo,
  dangerousUnno,
  dangerousNameProduct,
  dangerousPacking
) {
  $('input:radio[name="dangerous"]')
    .filter('[value="F"]')
    .attr("checked", !isContDangerous(dangerous));
  $('input:radio[name="dangerous"]')
    .filter('[value="T"]')
    .attr("checked", isContDangerous(dangerous));

  $('input:radio[name="dangerous"]').change(function () {
    let isDisplay = $(this).val() == "T" ? true : false;
    $("#dgIMO").prop("disabled", !isDisplay);
    $("#dgUNNO").prop("disabled", !isDisplay);
    $("#dgPacking").prop("disabled", !isDisplay);
    $("#dgNameProduct").prop("disabled", !isDisplay);
    $("#attachButtonDangerous").prop("disabled", !isDisplay);
  });

  let isDisable = !isContDangerous(dangerous);

  $("#dgIMO").prop("disabled", isDisable).val(dangerousImo);
  $("#dgUNNO").prop("disabled", isDisable).val(dangerousUnno);
  $("#dgPacking").prop("disabled", isDisable).val(dangerousPacking);
  $("#dgNameProduct").prop("disabled", isDisable).val(dangerousNameProduct);
  $("#attachButtonDangerous").prop("disabled", isDisable);

  initFileIsExist("preview-container-dangerous", "D");
}

/**
 * @author Khanh
 * @description init option for select CargoType
 */
function initOptionForSelectCargoTypeSelect() {
  initSelect(
    "cargoTypeSelect",
    contCargoTypes.map((item) => {
      return { value: item.dictValue, title: item.dictLabel };
    }),
    shipmentDetail.cargoType
  );
}
 
/**
 * @author Khanh
 * @description init option for select IMO danger
 */
function initOptionForSelectIMOSelect() {
  initSelect(
    "dgIMO",
    contDangerousImos.map((item) => {
      return { value: item.dictValue, title: item.dictLabel };
    }),
    shipmentDetail.dangerousImo ? shipmentDetail.dangerousImo : ""
  );
}

/**
 * @author Khanh
 * @description init option for select IMO danger
 */
function initOptionForSelectUNNOSelect() {
  initSelect(
    "dgUNNO",
    contDangerousUnnos.map((item) => {
      return { value: item.dictValue, title: item.dictLabel };
    }),
    shipmentDetail.dangerousUnno ? shipmentDetail.dangerousUnno : ""
  );
}

/**
 * @author Khanh
 * @param (String,Object,any)
 * @description init option for select
 *
 */
function initSelect(idSelect, data, valueChecked) {
  for (let i = 0; i < data.length; ++i) {
    $(`#${idSelect}`).append(
      $("<option>", {
        value: data[i].value,
        text: data[i].title,
        selected: valueChecked === data[i].value ? true : false,
      })
    );
  }
}
/**
 * * *End Init Func* * *
 */

/**
 * @author Khanh
 * @description handle click submit form-detail-add
 */
function submitHandler() {

  var data = $("#form-detail-add").serializeArray();
  data = covertSerializeArrayToObject(data);;
  data = {
    ...data,
    daySetupTemperature: $("#datetimepicker1").datetimepicker('getDate').getTime(),
  };
  parent.submitDataFromDetailModal(data);
  onCloseModel();

}

function formatDateToSendServer(data) {
  if (data) {
    let result = "";
    let arrDate = data.toString().split("/");
    let temp = arrDate[0];
    arrDate[0] = arrDate[1];
    arrDate[1] = temp;
    return arrDate.join("/");
  }
  return "";
}

function saveFile(fileIds, keyStatus) {
  $.ajax({
    url: PREFIX + "/uploadFile",
    method: "POST",
    data: {
      fileIds: fileIds,
      shipmentDetailId: shipmentDetail.id,
      shipmentId: shipmentDetail.shipmentId,
      keyStatus: keyStatus,
    },
    success: function (result) {
      if (result.code == 0) {
        $.modal.close();
      } else {
        $.modal.close();
      }
    },
  });
}
/**
 * @author Khanh
 * @description handle click close model
 */
function onCloseModel() {
  $.modal.close();
}

/**
 * @param {array} data
 * @author Khanh
 * @description cover array to object => json with  [{name : value}]
 * @returns {object} {{name: value}}
 */
function covertSerializeArrayToObject(data) {
  var rv = {};
  for (var i = 0; i < data.length; ++i) rv[data[i].name] = data[i].value;
  return rv;
}

/**
 * @param {number} data 30000
 * @author Khanh
 * @description format number
 * @returns {string} 30,000
 */

function formatNumber(value) {
  if (!value) return "";
  return value.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
}

/**
 * @param {number} data 30,000
 * @author Khanh
 * @description reformat number
 * @returns {string} 30000
 */
function reFormatNumber(value) {
  if (!value) return 0;
  return parseInt(value.replaceAll(",", ""));
}
/**
 * @param {none}
 * @author Khanh
 * @description check cont is cont ice
 * @returns ice: true | not ice: false
 */
function isContIce() {
  return shipmentDetail.sztp.includes("R") ? true : false;
}

/**
 * @param {none}
 * @author Khanh
 * @description check cont is cont oversize
 * @returns oversize: true | not oversize: false
 */
function isContOversize() {
  return shipmentDetail.sztp.includes("P") || shipmentDetail.oversize
    ? true
    : false;
}

/**
 * @param {none}
 * @author Khanh
 * @description check cont is cont dangerous
 * @returns dangerous: T | not dangerous: F
 */
function isContDangerous(dangerous) {
  return dangerous && dangerous !== "F";
}

/**
 * @param {none}
 * @author Khanh
 * @description get value is checked in category oversize checkbox
 * @returns {String} OH,OL
 */
function formatValuesCategoryOversize() {
  return $(".ipCategoryOversize:checked")
    .map(function () {
      return this.value;
    })
    .get()
    .join();
}

function dateformatter(date) {
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  var d = date.getDate();
  return (d < 10 ? "0" + d : d) + "/" + (m < 10 ? "0" + m : m) + "/" + y;
}

function dateparser(s) {
  var ss = s.split(".");
  var d = parseInt(ss[0], 10);
  var m = parseInt(ss[1], 10);
  var y = parseInt(ss[2], 10);
  if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
    return new Date(y, m - 1, d);
  }
}

$(document).ready(function () { });

function initFileIsExist(previewClass, fileType) {
  if (shipmentFiles != null) {
    let htmlInit = "";
    shipmentFiles.forEach(function (element, index) {
      if (element) {
        if (element.fileType == fileType) {
          shipmentFilePaths[`${getKeyFormByKeyType(fileType)}`].push(
            element.id
          );
          htmlInit =
            `<div class="preview-block" style="width: 70px;float: left;">
            <a href=${ctx}${element.path} target="_blank"><img src="` +
            ctx +
            `img/document.png" alt="Tài liệu" style="max-width: 50px;"/></a>
            <button type="button" class="close" aria-label="Close" onclick="removeImage(this, ` +
            element.id +
            `)">
                      <span aria-hidden="true">&times;</span>
                      </button>
                  </div>`;
          $(`.${previewClass}`).append(htmlInit);
        }
      }
    });
  }
}

function removeImage(element, fileIndex) {
  const status = getStatusContFollowIndex();
  if (status == CONT_SPECIAL_STATUS.REQ || status == CONT_SPECIAL_STATUS.YES) {
    $.modal.alertWarning(
      "Container đang hoặc đã yêu cầu xác nhận, không thể xóa tệp đã đính kèm."
    );
  } else {
    $.ajax({
      url: PREFIX + "/cont-special/file/" + fileIndex,
      method: "DELETE",
      beforeSend: function () {
        $.modal.loading("Đang xử lý, vui lòng chờ...");
      },
      success: function (result) {
        $.modal.closeLoading();
        if (result.code == 0) {
          $.modal.msgSuccess("Xóa tệp thành công.");
          $(element).parent("div.preview-block").remove();
          let indexIsClick = $(".close").index(element);
          shipmentFilePaths[`${element.className.split(" ")[1]}`].splice(
            indexIsClick,
            1
          );
        } else {
          $.modal.alertWarning("Xóa tệp thất bại.");
        }
      },
    });
  }
}

function getKeyFormByKeyType(keyType) {
  if (keyType == "D") {
    return KEY_FORM.DANGEROUS;
  } else if (keyType == "R") {
    return KEY_FORM.ICE;
  } else if (keyType == "O") {
    return KEY_FORM.OVERSIZE;
  } else {
    return "";
  }
}

function getStatusContFollowIndex() {
  if (
    !shipmentDetail.oversize &&
    !shipmentDetail.dangerous &&
    !shipmentDetail.frozenStatus
  ) {
    return null;
  } else if (
    shipmentDetail.dangerous == CONT_SPECIAL_STATUS.CANCEL ||
    shipmentDetail.oversize == CONT_SPECIAL_STATUS.CANCEL ||
    shipmentDetail.frozenStatus == CONT_SPECIAL_STATUS.CANCEL
  ) {
    // là cont bị từ chối
    return CONT_SPECIAL_STATUS.CANCEL;
  } else if (
    shipmentDetail.dangerous == CONT_SPECIAL_STATUS.REQ ||
    shipmentDetail.oversize == CONT_SPECIAL_STATUS.REQ ||
    shipmentDetail.frozenStatus == CONT_SPECIAL_STATUS.REQ
  ) {
    // là cont đang chờ xác nhận
    return CONT_SPECIAL_STATUS.REQ;
  } else if (
    shipmentDetail.dangerous == CONT_SPECIAL_STATUS.INIT ||
    shipmentDetail.oversize == CONT_SPECIAL_STATUS.INIT ||
    shipmentDetail.frozenStatus == CONT_SPECIAL_STATUS.INIT
  ) {
    // là cont đã được xét duyệt
    return CONT_SPECIAL_STATUS.INIT;
  } else {
    // là cont chỉ mới được tạo
    return CONT_SPECIAL_STATUS.YES;
  }
}
