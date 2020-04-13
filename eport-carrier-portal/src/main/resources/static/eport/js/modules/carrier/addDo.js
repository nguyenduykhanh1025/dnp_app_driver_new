      var tab=[];
      var prefix = "/carrier/do";
      function submitHandler() {
        // add condition to validate before submit add do
        if (true) {
          $.operate.save(prefix + "/add", $("#form-do-add").serialize());
        }
      }
      carrierCode();
      var dogrid = document.getElementById("dogrid"),
        hot,
        $hooksList,
        hooks;
      var groupName = null;
      var config;
      // Empty row and format date
      dateValidator = function (value, callback) {
        if (!value || 0 === value.length) {
          callback(false);
        } else {
          if (isGoodDate(value)) {
            callback(true);
          } else {
            callback(false);
          }
        }
      };
      // Empty row
      emptyValidator = function (value, callback) {
        if (!value || 0 === value.length) {
          callback(false);
        } else {
          callback(true);
        }
      };
      
    var doRenderer = function (instance,td, row, col, prop, value, cellProperties) {
    	  if($('#dogrid').handsontable('getDataAtCell',row, errorFlagCol) == ''){
    	     $(td).parent().css('background-color','#205199');
    	   }else{
    	     $(td).parent().css('background-color','#FFFFFF');
    	   }
    	   return td;
    };

      config = {
        stretchH: "all",
        height: document.documentElement.clientHeight - 70,
        minRows: 100,
        width: "100%",
        minSpareRows: 1,
        rowHeights: 30,
        manualColumnMove: false,
        rowHeaders: true,
        fillHandle: {
          autoInsertRow: true,
        },
        className: "htMiddle",
        colHeaders: [
          "Mã khách hàng <br> Carrier code",
          "Số vận đơn <i class='red'>(*)</i><br>B/L No.",
          "Số container <i class='red'>(*)</i><br> Container No.",
          "Tên khách hàng <i class='red'>(*)</i><br> Consignee",
          "Hạn lệnh <i class='red'>(*)</i><br> Valid to date",
          "Nơi hạ vỏ <br> Empty depot",
          "Số ngày miễn lưu vỏ <br> DET freetime",
          "Tên tàu <br> Vessel",
          "Chuyến <br> Voyage",
          "Ghi chú",
        ],
        colWidths:[10, 10, 20, 10, 15, 10, 5, 5, 15],
        filter: "true",
        columns: [
          {
            data: 'carrierCode',
            type: 'autocomplete',
            source: tab,
            strict: true
          },
          {
            data: 'blNo',
            validator: emptyValidator,
          },
          {
        	  data:'containerNo',
            validator: emptyValidator,
          },
          {
        	  data:'consignee',
            validator: emptyValidator,
          },
          {
          	data:'expiredDem',
            type: "date",
            dateFormat: "DD/MM/YYYY",
            correctFormat: true,
            defaultDate: new Date(),
            validator: dateValidator,
          },
          {
          	data:'emptyDepot',
          },
          {
          	data:'detFreetime',
            type: "numeric",
          },
          {
          	data:'vessel',},
          {
            data:'voyage',},
          {
            data:'remark',},
        ],
      };

      function carrierCode () {
        $.ajax({
          url: prefix + "/getOperateCode",
          method: "get"
        }).done(function(result){
          for (var i=0; i<result.length; i++) {
            tab.push(result[i]);
          }
        });
      }
      // Load table
      document.addEventListener("DOMContentLoaded", function () {

        setTimeout(function() {
          hot = new Handsontable(dogrid, config);
          hot.updateSettings({
            cells: function (row, col) {
              var cellProp = {};
              if (col === 5 && isGoodDate(hot.getDataAtCell(row, col))) {
                // cellProp.className = " above-fifty";
              } else if (col === 5) {
                cellProp.className = " not-date";
              }
              return cellProp;
            },
          });
        }, 200);

      
      });

      function isGoodDate(dt) {
        var reGoodDate = /^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|(([1][26]|[2468][048]|[3579][26])00))))$/g;
        return reGoodDate.test(dt);
      }

      function getAlert() {
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
        console.log(cleanedGridData);
        var doList = [];
        $.each(cleanedGridData, function (index, item) {
          var doObj = new Object();
          var date2 = "20/04/2020";
          doObj.carrierCode = item['carrierCode'];
          doObj.billOfLading = item['blNo'];
          doObj.containerNumber = item['containerNo'];
          doObj.consignee = item['consignee'];
          doObj.expiredDem = new Date(item['expiredDem']);
          doObj.detFreeTime = item['detFreetime'];
          doObj.emptyContainerDepot = item['emptyDepot'];
          doObj.voyNo = item['voyage'];
          doObj.vessel = item['vessel'];
          doObj.remark = item['remark'];
          // var doObj = {
          //   carrierCode:item['carrierCode'], 
          //   blNo: item['blNo'],
          //   containerNo: item['containerNo'],
          //   consignee: item['consignee'],
          //   expiredDem: item['expiredDem'],
          //   detFreetime: item['detFreetime'],
          //   emptyDepot: item['emptyDepot'],
          //   voyage: item['voyage'],
          //   vessel: item['vessel'],
          //   remark: item['remark'],
          // };
          doList.push(doObj);
        });
        console.log(doList);
        // var jsonDoList = JSON.stringify(doList);
        // console.log(jsonDoList);
        // $.each(cleanedGridData)
        //validate
        ///===============================
        // var errorFlg = false;
        // $.each(cleanedGridData, function (index, item) {
        //   if (
        //     item.blNo == null || item.blNo == '' ||
        //     item.containerNo == null || item.containerNo == '' ||
        //     item.consignee == null || item.consignee == '' ||
        //     item.expiredDem == null || item.expiredDem == '' ||
        //     !isGoodDate(item.expiredDem)) {
        // 	  $.modal.alertError("Có lỗi xảy ra tại dòng "+(index + 1)+".<br/>Vui lòng kiểm tra lại dữ liệu");
        	  errorFlg = false;
        // 	  return;
        //     }
//          else {
//              count++;
//            }
//          main_key = index;
        // });
//        main_key++;
//        console.log(main_key, count);
        if (!errorFlg) {
        	$.modal.confirm("Bạn có chắc chắn cập nhật DO này lên Web Portal của Cảng Đà Nẵng không?", function() {
                $.ajax({
                  url: "/carrier/do/add",
                  method: "post",
                  contentType : "application/json",
                  accept: 'text/plain',
                  data: JSON.stringify(doList),
                  dataType: 'text',
                  success: function (result) {
                    $.modal.alert("Khai báo DO thành công!");
                    closeItem();
                  },
                  error: function (result) {
                    $.modal.alert("Có lỗi trong quá trình thêm dữ liệu, vui lòng liên hệ admin.");
                  },
                });
        	},
        	{title:"Xác Nhận Gửi DO",btn:["Đồng Ý","Hủy Bỏ"]});
        }
      }
      // Validate the cells and submit the form via ajax or whatever
      // hot.validateCells(function (result, obj) {
      // if (result == true) {
      // var jsonData = JSON.stringify(myTableData);
      // console.log(jsonData);
      // } else {
      // //hotInstance.updateSettings({minSpareRows: 1});
      // }
      // });