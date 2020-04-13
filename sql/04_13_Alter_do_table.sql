ALTER TABLE `equipment_do`
	ALTER `bill_of_lading` DROP DEFAULT;
ALTER TABLE `equipment_do`
	CHANGE COLUMN `order_number` `order_number` VARCHAR(20) NULL DEFAULT NULL COMMENT 'So Lenh (Optional)' COLLATE 'utf8_bin' AFTER `carrier_code`,
	CHANGE COLUMN `bill_of_lading` `bill_of_lading` VARCHAR(20) NOT NULL COMMENT 'So B/L' COLLATE 'utf8_bin' AFTER `order_number`,
	CHANGE COLUMN `voy_no` `voy_no` VARCHAR(20) NULL DEFAULT NULL COMMENT 'Chuyen' COLLATE 'utf8_bin' AFTER `vessel`;
ALTER TABLE `equipment_do`
	ADD COLUMN `process_remark` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Ghi chu lam lenh' AFTER `remark`;