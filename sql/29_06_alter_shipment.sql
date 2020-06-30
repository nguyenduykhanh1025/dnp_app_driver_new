ALTER TABLE `shipment`
	ADD COLUMN `ope_code` VARCHAR(20) NULL DEFAULT NULL COMMENT 'Mã hãng tàu' AFTER `booking_no`,
	ADD COLUMN `group_name` VARCHAR(255)  NULL DEFAULT NULL COMMENT 'Tên cty theo MST' AFTER `tax_code`,
	ADD COLUMN `status` CHAR(1) NULL DEFAULT NULL COMMENT 'Shipment Status' COLLATE 'utf8_bin' AFTER `reference_no`;