var prefix = ctx + "carrier/do";
      var fromDate = "";
      var toDate = "";
      var dogrid = document.getElementById("container-grid"),
        hot;
      $(document).ready(function () {
        loadTable();
        $(".left-side").css("height", $(document).height());
        $("#btn-collapse").click(function () {
          handleCollapse(true);
        });
        $("#btn-uncollapse").click(function () {
          handleCollapse(false);
        });
      });

      function loadTable() {
        $("#dg").datagrid({
          url: ctx + "carrier/do/list",
          height: heightInfo,
          singleSelect: true,
          collapsible: true,
          clientPaging: false,
          pagination: true,
          onClickRow: function () {
            getSelected();
          },
          pageSize: 50,
          nowrap: false,
          striped: true,
          loadMsg: " Đang xử lý...",
          loader: function (param, success, error) {
            var opts = $(this).datagrid("options");
            if (!opts.url) return false;
            $.ajax({
              type: opts.method,
              url: opts.url,
              data: {
                pageNum: param.page,
                pageSize: param.rows,
                orderByColumn: param.sort,
                isAsc: param.order,
                fromDate: fromDate,
                toDate: toDate,
                voyageNo: $("#voyageNo").val() == null ? "" : $("#voyageNo").val(),
                blNo: $("#blNo").val() == null ? "" : $("#blNo").val(),
              },
              dataType: "json",
              success: function (data) {
                success(data);
                $("#dg").datagrid("hideColumn", "id");
              },
              error: function () {
                error.apply(this, arguments);
              },
            });
          },
        });
      }
      function handleCollapse(status) {
        if (status) {
          $(".left-side").css("width", "0.5%");
          $(".left-side").children().hide();
          $("#btn-collapse").hide();
          $("#btn-uncollapse").show();
          $(".right-side").css("width", "99%");
          setTimeout(function () {
            hot.render();
          }, 500);
          return;
        }
        $(".left-side").css("width", "20%");
        $(".left-side").children().show();
        $("#btn-collapse").show();
        $("#btn-uncollapse").hide();
        $(".right-side").css("width", "80%");
        setTimeout(function () {
          hot.render();
        }, 500);
      }

      config = {
        stretchH: "all",
        height: document.documentElement.clientHeight - 70,
        minRows: 100,
        width: "100%",
        minSpareRows: 1,
        rowHeights: 30,
        manualColumnMove: false,
        rowHeaders: true,
        className: "htMiddle",
        colHeaders: [
          "<input type='checkbox' id='parent-checkbox'/>",
          "Hãng Tàu",
          "B/L No",
          "Container No",
          "Kích Thước",
          "F/E",
          "Seal No",
          "Hạn Lệnh",
          "Tàu",
          "Chuyến",
          "Cảng Nguồn",
          "Cảng Đích",
          "VGM",
          "Phương Tiện",
          "Nơi Hạ Vỏ",
          "T.T Hải Quan",
          "T.T Thanh Toán",
          "T.T Làm Lệnh",
          "Ghi Chú",
        ],
        // colWidths: [7, 8, 8, 20, 8, 15, 8, 8, 8, 15],
        filter: "true",
        columns: [
          {
            data: "selected",
            type: "checkbox",
            className: "htCenter",
          },
          {
            data: "carrierCode",
          },
          {
            data: "blNo",
          },
          {
            data: "containerNo",
          },
          {
            data: "size",
          },
          {
            data: "fe",
          },
          {
            data: "sealNo",
          },
          {
            data: "expiredDem",
            type: "date",
            dateFormat: "DD/MM/YYYY",
            correctFormat: true,
            defaultDate: new Date(),
          },
          {
            data: "vessel",
            type: "autocomplete",
          },
          {
            data: "voyage",
          },
          {
            data: "loadingPort",
          },
          {
            data: "dischargePort",
          },
          {
            data: "vgm",
          },
          {
            data: "transportType",
          },
          {
            data: "emptyDepot",
          },
          {
            data: "customStatus",
          },
          {
            data: "paymentStatus",
          },
          {
            data: "processStatus",
          },
          {
            data: "remark",
          },
        ],
        afterChange: function (changes, src) {
          //Get data change in cell to render another column
          if (src !== "loadData" && (changes[0][3] != "")) {
            console.log(changes);
            console.log("Row change: " + changes[0][0])
            console.log("cell values: " + changes[0][3])
          }
        },
      };

      hot = new Handsontable(dogrid, config);

      function formatDate(value) {
        var date = new Date(value);
        var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
        var month = date.getMonth() + 1;
        var monthText = month < 10 ? "0" + month : month;
        return day + "/" + monthText + "/" + date.getFullYear();
      }

      function handleAdd() {
        $.modal.open("Thêm Lô Mới", "hbcx/add", null, 500, function () {
          console.log($("#taxCode").val());
        });
      }

      function handleEdit() {
        $.modal.open("Chỉnh sửa Lô", "hbcx/edit", null, 500, function () {
          console.log($("#taxCode").val());
        });
      }

      function handleRefresh() {
        loadTable();
      }

      function getSelected() {
        var row = $("#dg").datagrid("getSelected");
        if (row) {
          $("#loCode").text(row.carrierCode);
          $("#taxCode").text(row.billOfLading);
          $("#quantity").text(row.containerNumber);
          console.log(row.id);
          // $.messager.alert("Info", row.id)
        }
      }

      function getDataFromTable() {
        var myTableData = hot.getSourceData();
        if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
          hot.alter("remove_row", parseInt(myTableData.length - 1), (keepEmptyRows = false));
        }
        var cleanedGridData = [];
        $.each(myTableData, function (rowKey, object) {
          if (!hot.isEmptyRow(rowKey) && object["selected"]) {
            cleanedGridData.push(object);
          }
        });
        var selectedList = [];
        $.each(cleanedGridData, function (index, object) {
          var containerObj = new Object();
          containerObj.carrierCode = object["carrierCode"];
          containerObj.blNo = object["blNo"];
          containerObj.containerNo = object["containerNo"];
          containerObj.size = object["size"];
          containerObj.fe = object["fe"];
          containerObj.sealNo = object["sealNo"];
          containerObj.expiredDem = object["expiredDem"];
          containerObj.vessel = object["vessel"];
          containerObj.voyage = object["voyage"];
          containerObj.loadingPort = object["loadingPort"];
          containerObj.dischargePort = object["dischargePort"];
          containerObj.vgm = object["vgm"];
          containerObj.transportType = object["transportType"];
          containerObj.emptyDepot = object["emptyDepot"];
          containerObj.customStatus = object["customStatus"];
          containerObj.paymentStatus = object["paymentStatus"];
          containerObj.processStatus = object["processStatus"];
          containerObj.remark = object["remark"];
          selectedList.push(containerObj);
        });
        // Get result in "selectedList" variable
        if (selectedList.length == 0) {
          $.modal.alert("Bạn chưa nhập thông tin.");
          errorFlg = true;
        }

        if (errorFlg) {
          return false;
        }
      }