ALTER TABLE `shipment`
	ADD COLUMN `address` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Địa chỉ theo MST' COLLATE 'utf8_bin' AFTER `group_name`;