var prefix = ctx + "om/executeCatos";
var dogrid = document.getElementById("grid");
var hot;

$(document).ready(function () {
  $("#toggle-status").bootstrapToggle({
    width: '160px',
  });
  loadTable();
});

// CONFIGURATE HANDSONTABLE
config = {
  stretchH: "all",
  width: "100%",
  rowHeights: 30,
  manualColumnMove: false,
  rowHeaders: true,
  className: "shipment-detail-grid",
  colHeaders: function (col) {
    switch (col) {
      case 0:
        return "B/L NO";
      case 1:
        return "CONTAINER NO";
      case 2:
        return "SZTP";
      case 3:
        return "F/E";
      case 4:
        return "OPR";
      case 5:
        return "<span>CARGO</span><br><span>TYPE</span>";
      case 6:
        return "VESSEL NAME";
      case 7:
        return "VESSEL CODE";
    }
  },
  colWidths: [150, 150, 70, 70, 70, 100, 100, 100],
  filter: "true",
  columns: [
    {
      data: "blNo",
      readOnly: true,
    },
    {
      data: "containerNo",
      readOnly: true,
    },
    {
      data: "sztp",
      readOnly: true,
    },
    {
      data: "fe",
      readOnly: true,
    },
    {
      data: "opeCode",
      readOnly: true,
    },
    {
      data: "",
      readOnly: true,
    },
    {
      data: "vslNm",
      readOnly: true,
    },
    {
      data: "",
      readOnly: true,
    },
  ],
};

// LOAD TABLE
function loadTable() {
  hot = new Handsontable(dogrid, config);
  hot.loadData(shipmentDetail);
  hot.render();
  $("section.content").css("overflow", "auto");
}

// COPY DATA OF THE TABLE
function submitHandler() {
  hot.selectCell(0, 0);
  hot.selectAll();
  document.execCommand('copy');
}
