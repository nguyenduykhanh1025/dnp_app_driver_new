const PREFIX = ctx + "logistic/send-cont-full";

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
  $("#containerNo").val(containerNo);
  $("#sztp").val(sztp);

  if (shipmentDetail) {
    const {
      wgt,
      vgmChk,
      vgmInspectionDepartment,
      vgmMaxGross,
      temperature,
      daySetupTemperature,
      oversize,
      oversizeType,
      oversizeTop,
      oversizeRight,
      oversizeLeft,
      oversizeFront,
      oversizeBack,
      dangerous,
      dangerousImo,
      dangerousUnno,
      dangerousNameProduct,
      dangerousPacking,
    } = shipmentDetail;
    initElementHTMLInInformationCommonTab(
      wgt,
      vgmChk,
      vgmInspectionDepartment,
      vgmMaxGross,
      temperature,
      daySetupTemperature
    );
    initElementHTMLInOversizeTab(
      oversize,
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
    );
  }
}

/**
 * @author Khanh
 * @description create another values on tab common if exist from server
 */
function initElementHTMLInInformationCommonTab(
  wgt,
  vgmChk,
  vgmInspectionDepartment,
  vgmMaxGross,
  temperature,
  daySetupTemperature
) {
  $("#wgt").val(formatNumber(wgt));
  $("#wgt").change(function () {
    const valueNumber = reFormatNumber($(this).val());
    $(this).val(formatNumber(valueNumber));
  });
  $("#wgt").focus(function () {
    const valueNumber = reFormatNumber($(this).val());
    $(this).val(valueNumber);
  });

  $("#vgmChk")
    .prop("checked", vgmChk ? true : false)
    .change(function () {
      $("#inspectionDepartment").prop("disabled", !this.checked);
      $("#maxGross").prop("disabled", !this.checked);
    });

  // $('input:radio[name="isTemperature"]')
  //   .filter('[value="true"]')
  //   .attr("checked", isContIce())
  //   .prop("disabled", !isContIce());
  // $('input:radio[name="isTemperature"]')
  //   .filter('[value="false"]')
  //   .attr("checked", !isContIce())
  //   .prop("disabled", isContIce());

  $("#temperature")
    .val(temperature ? temperature : null)
    .prop("disabled", !isContIce() ? true : false);
  $("#datetimepicker1 *")
    .css("pointer-events", !isContIce() ? "none" : "")
    .prop("disabled", !isContIce() ? true : false);

  $("#datetimepicker1 input").val(
    daySetupTemperature
      ? new Date(daySetupTemperature).toLocaleDateString()
      : null
  );

  $("#inspectionDepartment")
    .prop("disabled", vgmChk ? false : true)
    .val(vgmInspectionDepartment);

  $("#maxGross")
    .prop("disabled", vgmChk ? false : true)
    .val(vgmMaxGross)
    .change(function () {
      const valueNumber = reFormatNumber($(this).val());
      $(this).val(formatNumber(valueNumber));
    })
    .focus(function () {
      const valueNumber = reFormatNumber($(this).val());
      $(this).val(valueNumber);
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
    .attr("checked", !isContOversize());
  $('input:radio[name="oversize"]').change(function () {
    let isDisable = $(this).val() == "T" ? false : true;
    $(".ipCategoryOversize").prop("disabled", isDisable);
    $("#oversizeTop").prop("disabled", isDisable);
    $("#oversizeRight").prop("disabled", isDisable);
    $("#oversizeLeft").prop("disabled", isDisable);
    $("#oversizeFront").prop("disabled", isDisable);
    $("#oversizeBack").prop("disabled", isDisable);
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
  });

  let isDisable = !isContDangerous(dangerous);

  $("#dgIMO").prop("disabled", isDisable).val(dangerousImo);
  $("#dgUNNO").prop("disabled", isDisable).val(dangerousUnno);
  $("#dgPacking").prop("disabled", isDisable).val(dangerousPacking);
  $("#dgNameProduct").prop("disabled", isDisable).val(dangerousNameProduct);
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
  if ($.validate.form()) {
    var data = $("#form-detail-add").serializeArray();
    data = covertSerializeArrayToObject(data);

    // console.log(data.daySetupTemperature.getTime());
    data = {
      ...data,
      oversizeType: formatValuesCategoryOversize(),
      wgt: reFormatNumber($("#wgt").val()),
      vgmMaxGross: $("#vgmChk").prop("checked")
        ? reFormatNumber($("#maxGross").val())
        : null,
      vgmInspectionDepartment: $("#vgmChk").prop("checked")
        ? $("#inspectionDepartment").val()
        : null,
      daySetupTemperature: new Date(data.daySetupTemperature).getTime(),
    };
    parent.submitDataFromDetailModal(data);
    onCloseModel();
  }
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
  return sztp.includes("R") ? true : false;
}

/**
 * @param {none}
 * @author Khanh
 * @description check cont is cont oversize
 * @returns oversize: true | not oversize: false
 */
function isContOversize() {
  return sztp.includes("P") ? true : false;
}

/**
 * @param {none}
 * @author Khanh
 * @description check cont is cont dangerous
 * @returns dangerous: T | not dangerous: F
 */
function isContDangerous(dangerous) {
  return dangerous == "T";
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
