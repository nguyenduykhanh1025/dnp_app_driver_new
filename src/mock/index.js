const loginApp = [{
    code: 1,
    msg: "Thông báo thành công",
    token: ""
}]

const getInfoUser = [{
    code: 1,
    msg: "Thông báo thành công",
    Data: [{
        name: "Đặng Thu Anh",
        email: "",
        p_number: "0978737921",
        license_plates: '79-N2 0122'
    }]
}]

const getListHistory = [{
    code: 1,
    msg: "Thông báo thành công",
    Data: [
        {
            historyCode: 0,
            datetime: "09/06/2020",
            contCode: '1X0ABC',
            contSize: "F22",
            contType: "ABC",
            notes: "Ghi chú 0",
            gatePass: "",
            yardPosition: "",
        },
        {
            historyCode: 1,
            datetime: "09/06/2020",
            contCode: '1X0ABC',
            contSize: "F22",
            contType: "ABC",
            notes: "Ghi chú 1",
            gatePass: "ABC",
            yardPosition: "",
        },
        {
            historyCode: 2,
            datetime: "09/06/2020",
            contCode: '1X0ABC',
            contSize: "F22",
            contType: "ABC",
            notes: "Ghi chú 2",
            gatePass: "",
            yardPosition: "",
        },
        {
            historyCode: 3,
            datetime: "09/06/2020",
            contCode: '1X0ABC',
            contSize: "F22",
            contType: "ABC",
            notes: "Ghi chú 3",
            gatePass: "",
            yardPosition: "",
        },
        {
            historyCode: 4,
            datetime: "09/06/2020",
            contCode: '1X0ABC',
            contSize: "F22",
            contType: "ABC",
            notes: "Ghi chú 3",
            gatePass: "",
            yardPosition: "",
        },
        {
            historyCode: 5,
            datetime: "09/06/2020",
            contCode: '1X0ABC',
            contSize: "F22",
            contType: "ABC",
            notes: "Ghi chú 3",
            gatePass: "",
            yardPosition: "",
        },
        {
            historyCode: 6,
            datetime: "09/06/2020",
            contCode: '1X0ABC',
            contSize: "F22",
            contType: "ABC",
            notes: "Ghi chú 3",
            gatePass: "",
            yardPosition: "",
        }
    ]
}]

const getListNotification = [{
    code: 1,
    msg: "Thông báo thành công",
    Data: [
        {
            notificationCode: 0,
            datetime: "09/06/2020",
            title: "Gửi tới... 0",
            description: "Nội dung là .. 0",
        },
        {
            notificationCode: 1,
            datetime: "09/06/2020",
            title: "Gửi tới... 1",
            description: "Nội dung là .. 1",
        },
        {
            notificationCode: 2,
            datetime: "09/06/2020",
            title: "Gửi tới... 2",
            description: "Nội dung là .. 2",
        },
        {
            notificationCode: 3,
            datetime: "09/06/2020",
            title: "Gửi tới... 3",
            description: "Nội dung là .. 3",
        },
        {
            notificationCode: 4,
            datetime: "09/06/2020",
            title: "Gửi tới... 4",
            description: "Nội dung là .. 4",
        },
        {
            notificationCode: 5,
            datetime: "09/06/2020",
            title: "Gửi tới... 5",
            description: "Nội dung là .. 5",
        },
        {
            notificationCode: 6,
            datetime: "09/06/2020",
            title: "Gửi tới... 6",
            description: "Nội dung là .. 6",
        },
        {
            notificationCode: 7,
            datetime: "09/06/2020",
            title: "Gửi tới... 7",
            description: "Nội dung là .. 7",
        }

    ]
}]

const getListInfoHome = [{
    code: 1,
    msg: "Thông báo thành công",
    Data: [
        {
            title: "Bốc cont hàng",
            Data: [
                {
                    batchCode: "BC001",
                    billNumber: "BN001",
                    size: "A12",
                    type: "AB23",
                    contCount: "10",
                },
                {
                    batchCode: "BC002",
                    billNumber: "BN002",
                    size: "A12",
                    type: "AB23",
                    contCount: "20",
                },
                {
                    batchCode: "BC003",
                    billNumber: "BN003",
                    size: "A12",
                    type: "AB23",
                    contCount: "12",
                },
                {
                    batchCode: "BC004",
                    billNumber: "BN004",
                    size: "A12",
                    type: "AB23",
                    contCount: "9",
                },
                {
                    batchCode: "BC005",
                    billNumber: "BN005",
                    size: "A12",
                    type: "AB23",
                    contCount: "3",
                },
            ]
        },
        {
            title: "Bốc cont rỗng",
            Data: [
                {
                    batchCode: "BR001",
                    billNumber: "BN001",
                    size: "A12",
                    type: "AB23",
                    contCount: "10",
                },
                {
                    batchCode: "BR002",
                    billNumber: "BN002",
                    size: "A12",
                    type: "AB23",
                    contCount: "20",
                },
                {
                    batchCode: "BR003",
                    billNumber: "BN003",
                    size: "A12",
                    type: "AB23",
                    contCount: "12",
                },
                {
                    batchCode: "BR004",
                    billNumber: "BN004",
                    size: "A12",
                    type: "AB23",
                    contCount: "9",
                },
                {
                    batchCode: "BR005",
                    billNumber: "BN005",
                    size: "A12",
                    type: "AB23",
                    contCount: "3",
                },
            ]
        },
        {
            title: "Hạ cont hàng",
            Data: [
                {
                    batchCode: "HH001",
                    billNumber: "BN001",
                    size: "A12",
                    type: "AB23",
                    contCount: "10",
                },
                {
                    batchCode: "HH002",
                    billNumber: "BN002",
                    size: "A12",
                    type: "AB23",
                    contCount: "20",
                },
                {
                    batchCode: "HH003",
                    billNumber: "BN003",
                    size: "A12",
                    type: "AB23",
                    contCount: "12",
                },
                {
                    batchCode: "HH004",
                    billNumber: "BN004",
                    size: "A12",
                    type: "AB23",
                    contCount: "9",
                },
                {
                    batchCode: "HH005",
                    billNumber: "BN005",
                    size: "A12",
                    type: "AB23",
                    contCount: "3",
                },
            ]
        },
        {
            title: "Hạ cont rỗng",
            Data: [
                {
                    batchCode: "HR001",
                    billNumber: "BN001",
                    size: "A12",
                    type: "AB23",
                    contCount: "10",
                },
                {
                    batchCode: "HR002",
                    billNumber: "BN002",
                    size: "A12",
                    type: "AB23",
                    contCount: "20",
                },
                {
                    batchCode: "HR003",
                    billNumber: "BN003",
                    size: "A12",
                    type: "AB23",
                    contCount: "12",
                },
                {
                    batchCode: "HR004",
                    billNumber: "BN004",
                    size: "A12",
                    type: "AB23",
                    contCount: "9",
                },
                {
                    batchCode: "HR005",
                    billNumber: "BN005",
                    size: "A12",
                    type: "AB23",
                    contCount: "3",
                },
            ]
        },
    ]
}]

const getDetailHistory = [{
    code: 1,
    msg: "Thông báo thành công",
    Data: [
        {
            contCode: "",
            contSize: "",
            contType: "",
            yardPosition: "",
            gatePass: "",
            notes: "",
            datetime: "",
        }
    ]
}]

const getDetailNotification = [{
    code: 1,
    msg: "Thông báo thành công",
    Data: [
        {
            contCode: "",
            contSize: "",
            contType: "",
            yardPosition: "",
            gatePass: "",
            notes: "",
            datetime: "",
        }
    ]
}]

const getDetailContItem = [{
    code: 1,
    msg: "Thông báo thành công",
    Data: [
        {
            Master: {
                batchCode: "1854",
                contCount: "20",
                size: "A12",
                type: "AB23",
            },
            Detail: [
                {
                    batchCode: "1854",
                    contNumber: "",
                    size: "A12",
                    type: "AB23",
                    note: "OK 123"
                },
                {
                    batchCode: "1855",
                    contNumber: "",
                    size: "A12",
                    type: "AB23",
                    note: "OK 123"
                },
                {
                    batchCode: "1856",
                    contNumber: "",
                    size: "A12",
                    type: "AB23",
                    note: "OK 123"
                },
                {
                    batchCode: "1857",
                    contNumber: "",
                    size: "A12",
                    type: "AB23",
                    note: "OK 123"
                },
                {
                    batchCode: "1858",
                    contNumber: "",
                    size: "A12",
                    type: "AB23",
                    note: "OK 123"
                },
                {
                    batchCode: "1859",
                    contNumber: "",
                    size: "A12",
                    type: "AB23",
                    note: "OK 123"
                }
            ]
        }
    ]
}]

const SearchQRCode = {
    code: 0,
    msg: "Container AGEA1231245 đã quá hạn lệnh hoặc không đủ điều kiện để nhận. Xin mời đến phòng dịch vụ khách hàng để làm thủ tục. Xe rơmooc biển số 43R5555 không đủ tải trong để bốc container AGEA1231245 (20,000kg). Xin vui lòng quay đầu xe chọn rơmooc khác rồi làm lại thủ tục",
    Data:   
        [
            {
                name: "Giao container hàng vào cảng",
                containerNo: "AGEA1231245",
                coordinates: "VJS1-45-5-5",
            },
            {
                name: "Nhận container hàng từ cảng",
                containerNo: "AGEA1231245",
                coordinates: "A-20-3-2",
            },
        ]
}

export {
    loginApp,
    getInfoUser,
    getListHistory,
    getListNotification,
    getListInfoHome,
    getDetailHistory,
    getDetailNotification,
    getDetailContItem,
    SearchQRCode,
}