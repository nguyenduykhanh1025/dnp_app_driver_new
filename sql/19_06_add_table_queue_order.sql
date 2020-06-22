DROP TABLE IF EXISTS `queue_order`;
CREATE TABLE IF NOT EXISTS `queue_order`(
    `id` bigint(20) NOT NULL COMMENT 'ID',
    `mode`varchar(50) COLLATE utf8_bin NULL COMMENT 'Loại lệnh',
    `consignee` varchar(255) COLLATE utf8_bin NULL COMMENT 'Chủ hàng',
    `truck_co` varchar(255) COLLATE utf8_bin NULL COMMENT 'MST-Tên cty',
    `tax_code` varchar(15) COLLATE utf8_bin NOT NULL COMMENT 'MST',
    `pay_type` varchar(20) COLLATE utf8_bin NOT NULL COMMENT 'PT thanh toán',
    `bl_no` varchar(20) COLLATE utf8_bin NULL COMMENT 'Billing No',
    `pickup_date` DATETIME DEFAULT NULL COMMENT 'Ngày bốc',
    `vessel` varchar(10) COLLATE utf8_bin NULL COMMENT 'Vessel',
    `voyage` varchar(10) COLLATE utf8_bin NULL COMMENT 'Voyage',
    `year` varchar(10) COLLATE utf8_bin NULL COMMENT 'year',
    `invoice_type` varchar(10) COLLATE utf8_bin NULL COMMENT 'Loại hóa đơn',
    `invoice_template` varchar(100) COLLATE utf8_bin NULL COMMENT 'Mẫu hóa đơn',
    `cont_number` int(10) NOT NULL COMMENT 'Số container',
    `before_after` varchar(10) COLLATE utf8_bin NULL COMMENT 'Trước-Sau',
    `booking_no` varchar(15) COLLATE utf8_bin NULL COMMENT 'Booking no',
    `size_type` varchar(10) COLLATE utf8_bin NULL COMMENT 'Kích thước cont',
    `service_id` char(1) COLLATE utf8_bin NULL COMMENT 'Dich vu',
    PRIMARY KEY (`id`)
)CHARSET=utf8 COLLATE=utf8_bin COMMENT='queue order';