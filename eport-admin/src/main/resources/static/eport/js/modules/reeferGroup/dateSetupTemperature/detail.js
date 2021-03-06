const PREFIX = ctx + "reefer-group/date-setup-temperature";
const DANGEROUS_STATUS = {
  yet: "T", // là cont dangerous
  pending: "2", // là cont danger đang chờ xét duyết
  approve: "3", // là cont danger đã đc xét duyết
  reject: "4", // là cont danger đã bị từ chối
  NOT: "F", // không phải là cont danger
};
const SPECIAL_STATUS = {
  yet: "1",
  pending: "2",
  approve: "3",
  reject: "4",
};
const KEY_FORM = {
  OVERSIZE: "oversize",
  DANGEROUS: "dangerous",
  ICE: "ice",
};
var shipmentFilePaths = { oversize: [], dangerous: [], ice: [] };

$("#form-detail-add").validate({
  onkeyup: false,
  focusCleanup: true,
});

$("#datetimepicker1").datetimepicker({
  format: "dd/mm/yyyy",
  language: "vi_VN",
  minView: "month",
});

$(document).ready(function () {
  initValueToElementHTML();

  initOptionForSelectCargoTypeSelect();
  initOptionForSelectIMOSelect();
  initOptionForSelectUNNOSelect();
});

/**
 * * *Init Func* * *
 */

/**
 * @author Khanh
 * @description create another values if exist from server
 */
function initValueToElementHTML() {
  if (shipmentDetail) {
    $("#containerNo").val(shipmentDetail.containerNo);
    $("#sztp").val(shipmentDetail.sztp);
    $("#powerDrawDate").val(shipmentDetail.powerDrawDate);
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

  let daySetup = new Date(daySetupTemperature);
  $("#datetimepicker1 input").val(
    daySetupTemperature
      ? `${daySetup.getDate()}/${daySetup.getMonth() + 1}/${daySetup.getFullYear()}`
      : null
  );
  $("#humidity").val(humidity ? humidity : null);
  $("#ventilation").val(ventilation ? ventilation : null);
  initFileIsExist("preview-container-ice", "R");
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
  if (
    shipmentDetail.dangerous == DANGEROUS_STATUS.pending ||
    shipmentDetail.dangerous == DANGEROUS_STATUS.approve ||
    shipmentDetail.contSpecialStatus == SPECIAL_STATUS.pending ||
    shipmentDetail.contSpecialStatus == SPECIAL_STATUS.approve
  ) {
    $.modal.alertWarning(
      "Container đang hoặc đã được yêu cầu xác nhận, không thể chinh sửa thông tin"
    );
  } else {
    if ($.validate.form()) {
      var data = $("#form-detail-add").serializeArray();
      data = covertSerializeArrayToObject(data);

      data = {
        ...data,
        oversizeType: formatValuesCategoryOversize(),
        vgmMaxGross: $("#vgmChk").prop("checked")
          ? reFormatNumber($("#maxGross").val())
          : null,
        vgmInspectionDepartment: $("#vgmChk").prop("checked")
          ? $("#inspectionDepartment").val()
          : null,
        daySetupTemperature: new Date(data.daySetupTemperature).getTime(),
        dangerous:
          shipmentDetail.dangerous == data.dangerous
            ? shipmentDetail.dangerous
            : data.dangerous,
      };

      if (shipmentFilePaths.dangerous.length) {
        saveFile(shipmentFilePaths.dangerous, "");
      }
      if (shipmentFilePaths.oversize.length) {
        saveFile(shipmentFilePaths.oversize, shipmentDetail.sztp);
      }

      parent.submitDataFromDetailModal(data);
      onCloseModel();
    }
  }
}

function saveFile(filePaths, shipmentSztp) {
  $.ajax({
    url: PREFIX + "/uploadFile",
    method: "POST",
    data: {
      filePaths: filePaths,
      shipmentDetailId: shipmentDetail.id,
      shipmentId: shipmentDetail.shipmentId,
      shipmentSztp: shipmentSztp,
    },
    success: function (result) {
      if (result.code == 0) {
        $.modal.alertError(result.msg);
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
  return shipmentDetail.sztp.includes("P") ? true : false;
}

/**
 * @param {none}
 * @author Khanh
 * @description check cont is cont dangerous
 * @returns dangerous: T | not dangerous: F
 */
function isContDangerous(dangerous) {
  return !dangerous || dangerous !== "F";
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
        shipmentFiles.push(element.id);
        if (element.fileType == fileType) {
          htmlInit =
            `<div class="preview-block" style="width: 70px;float: left;margin-top: 10px;">
            <a href=${element.path} target="_blank"><img src="` +
            ctx +
            `img/document.png" alt="Tài liệu" style="max-width: 50px;"/></a>
                  </div>`;
          $(`.${previewClass}`).append(htmlInit);
        }
      }
    });
  }
}
