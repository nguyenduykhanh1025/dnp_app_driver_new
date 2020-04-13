      var doList;
      var hot;
      $("#billNo").html("No: " + firstDo.billOfLading);
      $("#billNumber").html(firstDo.billOfLading);
      $("#carrier").html(firstDo.carrierCode);
      var date = new Date(firstDo.expiredDem);
      var status = firstDo.status == 0 ? "<span class='label label-warning'>Chưa làm lệnh</span>" : "<span class='label label-success'>Đã làm lệnh</span>";
      $("#dostatus").html(status);
      $("#expiredDem").html(date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear());
      $.ajax({
        url: "/carrier/do/getInfoBl",
        method: 'get',
        data: {
          blNo: firstDo.billOfLading,
        },
        success: function(result) {
          doList = result;
          var example = document.getElementById('showDemo');
          hot = new Handsontable(example, {
            data: doList,
            stretchH: 'all',
            width: 'auto',
            manualColumnResize: true,
            manualRowResize: true,
            minSpareRows: 1,
            fillHandle: {
              autoInsertRow: true,
            },
            height: 500,
            colHeaders: [
              "ID<br>id",
              "Mã hãng tàu<br>Carrier",
              "Số vận đơn<br> Bill No",
              "Số Container<br>Container No.",
              "Tên khách hàng<br>Consignee",
              "Hạn lệnh<br> Valid to date",
              "Nơi hạ vỏ<br> Empty depot",
              "Ngày miễn lưu<br> DET Freetime",
              "Tên tàu<br>Vessel",
              "Chuyến<br>Voyage",
              // "XÃ¡c nháº­n <br> lÃ m lá»‡nh",
              // "Ä�Ã£ ná»™p <br> DO gá»‘c",
              // "NgÃ y ná»™p<br>DO date in",
              "Ghi chú<br>Remark"
            ],
            filter: true,
            licenseKey: "non-commercial-and-evaluation",
            columns: [{
                data: 'id',
                readOnly: true
              },
              {
                data: 'carrierCode',
                readOnly: true
              },
              {
                data: 'billOfLading',
                readOnly: true
              },
              {
                data: 'containerNumber',
                type: 'text',
              },
              {
                data: 'consignee',
                type: 'text',
              },
              {
                data: 'expiredDem',
                type: 'date',
                dateFormat: 'DD/MM/YYYY',
                correctFormat: true,
              },
              {
                data: 'emptyContainerDepot',
                type: 'text',
              },
              {
                data: 'detFreeTime',
                type: 'numeric',

              },
              {
                data: 'vessel',
                type: 'text',
              },
              {
                data: 'voyNo',
                type: 'text',
              },
              {
                data: 'remark',
                type: 'text',
              }
            ],
            rowHeaders: true,
            autoColumnSize: true,
            columnSorting: {
              indicator: true
            },
            colWidths: [0.1, 70, 70, 70, 160, 50, 140, 50, 70, 50, 150],
            manualColumnMove: true,
            filters: true
          });
          hot.validateCells();
        }
      })
      
      function isGoodDate(dt) {
        var reGoodDate = /^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|(([1][26]|[2468][048]|[3579][26])00))))$/g;
        return reGoodDate.test(dt);
      }

      function updateDO() {
        var myTableData = hot.getSourceData();
        // If the last row is empty, remove it before validation
        if (myTableData.length > 1 && hot.isEmptyRow(myTableData.length - 1)) {
          // hot.updateSettings({minSpareRows: 0});
          // Remove the last row if it's empty
          hot.alter("remove_row",parseInt(myTableData.length - 1),(keepEmptyRows = false));
        }
        var cleanedGridData = [];
        $.each(myTableData, function (rowKey, object) {
          if (!hot.isEmptyRow(rowKey)) {
            cleanedGridData.push(object);
          }
        });
        var doList = [];
        $.each(cleanedGridData, function (index, item) {
          var doObj = new Object();
          if (!isGoodDate(item['expiredDem']) || item['expiredDem'] == null ){
            $.modal.alert("Có lỗi tại hàng ["+(index+ 1) +"].<br>Lỗi: Hạn lệnh đang để trống hoặc chưa đúng format.");
            errorFlg = true;
            return;
          }
          var date = new Date(item['expiredDem'].replace( /(\d{2})\/(\d{2})\/(\d{4})/, "$2/$1/$3"));
          doObj.id = item['id'];
          doObj.carrierCode = item['carrierCode'];
          doObj.billOfLading = item['billOfLading'];
          doObj.containerNumber = item['containerNumber'];
          doObj.consignee = item['consignee'];
          doObj.expiredDem = date.getTime();
          doObj.detFreeTime = item['detFreeTime'];
          doObj.emptyContainerDepot = item['emptyContainerDepot'];
          doObj.voyNo = item['voyNo'];
          doObj.vessel = item['vessel'];
          doObj.remark = item['remark'];
          
          doList.push(doObj);
        });
        $.each(doList, function (index, item) {
          if (item['expiredDem'] < new Date()) {
            $.modal.alert("Có lỗi tại hàng ["+(index+ 1) +"].<br>Lỗi: Hạn lệnh không được nhỏ hơn ngày hiện tại.");
            errorFlg = true;
            return;
          }

          if (item['carrierCode'] == null) {
            $.modal.alert("Có lỗi tại hàng ["+(index+ 1) +"].<br>Lỗi: Mã khách hàng không được trống.");
            errorFlg = true;
            return;
          }

          if (item['billOfLading'] == null) {
            $.modal.alert("Có lỗi tại hàng ["+(index+ 1) +"].<br>Lỗi: Số vận đơn không được trống.");
            errorFlg = true;
            return;
          }

          if (item['containerNumber'] == null) {
            $.modal.alert("Có lỗi tại hàng ["+(index+ 1) +"].<br>Lỗi: Số container không được trống.");
            errorFlg = true;
            return;
          }

          if (item['consignee'] == null) {
            $.modal.alert("Có lỗi tại hàng ["+(index+ 1) +"].<br>Lỗi: Tên khách hàng không được trống.");
            errorFlg = true;
            return;
          }
          var regexNuber = /^[0-9]*$/;
          console.log(item[regexNuber.test(item['detFreeTime'])]);
          if (!regexNuber.test(item['detFreeTime'])) {
            $.modal.alert("Có lỗi tại hàng ["+(index+ 1) +"].<br>Lỗi: Số ngày miễn lưu vỏ phải là số.");
            errorFlg = true;
            return;
          }
        })
        if (errorFlg) {
          return;
        }

        $.modal.confirm("Bạn có chắc chắn cập nhật DO không?", function() {
          $.ajax({
            url: "/carrier/do/update",
            method: "post",
            contentType : "application/json",
            accept: 'text/plain',
            data: JSON.stringify(doList),
            dataType: 'text',
            success: function (result) {
              $.modal.alert("Cập nhật DO thành công!");
              closeItem();
            },
            error: function (result) {
              $.modal.alert("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
            },
          });
        },
        {title:"Xác nhận cập nhật DO",btn:["Đồng Ý","Hủy Bỏ"]});
      }

      function search() {
        $.ajax({
          url : "/carrier/do/searchCon",
          method : "get",
          data : {
            billOfLading : firstDo.billOfLading,
            contNo : $("#searchForm").val()
          }
        }).done(function(result){
          hot.loadData(result);
          hot.render();
          hot.validateCells();
        });       
      }
      document
        .getElementById("searchForm")
        .addEventListener("keyup", function (event) {
          event.preventDefault();
          if (event.keyCode === 13) {
            $("#searchBtn").click();
          }
        });

        function closeError(msg) {
          $.modal.alertError(msg);
        }