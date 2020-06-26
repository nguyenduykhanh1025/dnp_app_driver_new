ALTER TABLE `shipment` 
    CHANGE `bl_no` `bl_no` VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL;
ALTER TABLE `shipment_detail` 
    CHANGE `container_no` `container_no` VARCHAR(12) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Container Number',
    CHANGE `fe` `fe` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'FE',
    CHANGE `wgt` `wgt` INT(11) NULL DEFAULT NULL COMMENT 'Weight ',
    CHANGE `loading_port` `loading_port` VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'Cang Xep Hang',
    CHANGE `discharge_port` `discharge_port` VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Cang Do Hang',
    CHANGE `payment_status` `payment_status` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Payment Status (Y,N,W,E)',
    CHANGE `process_status` `process_status` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Process Status(Y,N,E)',
    CHANGE `user_verify_status` `user_verify_status` VARCHAR(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Xac Thuc (Y,N)',
    CHANGE `status` `status` TINYINT(4) NOT NULL COMMENT 'Status';

ALTER TABLE `logistic_truck`
	CHANGE COLUMN `type` `type` CHAR(1) NOT NULL COMMENT '0: đầu kéo, 1: rơ mooc' COLLATE 'utf8_bin' AFTER `plate_number`;
ALTER TABLE `process_bill`
	ADD COLUMN `logistic_group_id` BIGINT(20) NOT NULL COMMENT 'Mã logistic' AFTER `shipment_id`;
	
ALTER TABLE `process_order`
	ADD COLUMN `logistic_group_id` BIGINT(20) NOT NULL COMMENT 'Mã logistic' AFTER `shipment_id`;