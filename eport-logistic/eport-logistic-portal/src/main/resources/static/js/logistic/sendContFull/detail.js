const PREFIX = ctx + "logistic/send-cont-full";

const CARGO_TYPES = [
  {
    value: "AK",
    title: "AK:Over Dimension",
  },
  {
    value: "BB",
    title: "BB:Break Bulk",
  },
  {
    value: "BN",
    title: "BN:Bundle",
  },
  {
    value: "DG",
    title: "DG:Dangerous",
  },
  {
    value: "DR",
    title: "DR:Reefer & DG",
  },
  {
    value: "DE",
    title: "DE:Dangerous Empty",
  },
  {
    value: "FR",
    title: "FR:Fragile",
  },
  {
    value: "GP",
    title: "GP:General",
  },
  {
    value: "MT",
    title: "MT:Empty",
  },
  {
    value: "RF",
    title: "RF:Reefer",
  },
];

$(document).ready(function () {
  initValueToElementHTML();
  initOptionForSelectCargoTypeSelect();
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

  $("#form-detail-add").validate({
    onkeyup: false,
  });

  if (shipmentDetail) {
    console.log({ shipmentDetail });
    const { wgt, vgm, temperature } = shipmentDetail;
    initElementHTMLInInformationCommonTab(wgt, vgm, temperature);
    initElementHTMLInOversizeTab();
    initElementHTMLInDangerousTab();
  }
}

/**
 * @author Khanh
 * @description create another values on tab common if exist from server
 */
function initElementHTMLInInformationCommonTab(wgt, vgm, temperature) {
  $("#wgt").val(wgt ? formatMoney(wgt) : null);
  $("vgm").prop("checked", vgm ? true : false);

  $('input:radio[name="rdTemperature"]')
    .filter('[value="true"]')
    .attr("checked", isContIce())
    .prop("disabled", !isContIce());
  $('input:radio[name="rdTemperature"]')
    .filter('[value="false"]')
    .attr("checked", !isContIce())
    .prop("disabled", isContIce());

  $("#temperature")
    .val(temperature ? temperature : null)
    .prop(
      "disabled",
      $('input[name="rdTemperature"]:checked').val() == "true" ? false : true
    );
}

/**
 * @author Khanh
 * @description create another values on tab oversize if exist from server
 */
function initElementHTMLInOversizeTab() {
  $('input:radio[name="rdOversize"]')
    .filter('[value="true"]')
    .attr("checked", isContOversize());
  $('input:radio[name="rdOversize"]')
    .filter('[value="false"]')
    .attr("checked", !isContOversize());
  $('input:radio[name="rdOversize"]').change(function () {
    let isDisplay = $(this).val() == "true" ? true : false;
    $("#oversizeFormContainer").css("display", isDisplay ? "" : "none");
  });

  $("#oversizeFormContainer").css(
    "display",
    $('input[name="rdOversize"]:checked').val() == "true" ? "" : "none"
  );
}

/**
 * @author Khanh
 * @description create another values on tab dangerous if exist from server
 */
function initElementHTMLInDangerousTab() {
  $('input:radio[name="rdDangerous"]').change(function () {
    let isDisplay = $(this).val() == "true" ? true : false;
    $("#dangerousFormContainer").css("display", isDisplay ? "" : "none");
  });
}

/**
 * @author Khanh
 * @description init option for select CargoType
 */
function initOptionForSelectCargoTypeSelect() {
  for (let i = 0; i < CARGO_TYPES.length; ++i) {
    $("#cargoTypeSelect").append(
      $("<option>", {
        value: CARGO_TYPES[i].value,
        text: CARGO_TYPES[i].title,
        selected:
          shipmentDetail.cargoType === CARGO_TYPES[i].value ? true : false,
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
    data = { ...covertSerializeArrayToObject(data) };
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
function formatMoney(value) {
  return value.format(0, 3, ",", ".");
}
Number.prototype.format = function (n, x, s, c) {
  var re = "\\d(?=(\\d{" + (x || 3) + "})+" + (n > 0 ? "\\D" : "$") + ")",
    num = this.toFixed(Math.max(0, ~~n));

  return (c ? num.replace(".", c) : num).replace(
    new RegExp(re, "g"),
    "$&" + (s || ",")
  );
};

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
