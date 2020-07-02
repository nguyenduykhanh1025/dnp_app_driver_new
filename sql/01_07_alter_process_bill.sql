ALTER TABLE `process_bill` 
    CHANGE `reference_no` `reference_no` VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Mã tham chiếu',
    CHANGE `exchange_fee` `exchange_fee` bigint(20) NULL DEFAULT NULL COMMENT 'Phí giao nhận',
	ADD COLUMN `pay_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'PT thanh toán' AFTER `service_type`,
	ADD COLUMN `payment_status` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Payment Status (Y,N)' AFTER `pay_type`,
	ADD COLUMN `payment_time` datetime DEFAULT NULL COMMENT 'Payment Time' after `payment_status`;