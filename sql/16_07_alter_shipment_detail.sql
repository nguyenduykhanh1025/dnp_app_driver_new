ALTER TABLE `shipment_detail`
	ADD COLUMN `vsl_name` VARCHAR(50) NULL DEFAULT NULL COMMENT 'Vessel name' COLLATE 'utf8_bin' AFTER `wgt`,
	CHANGE COLUMN `vsl_nm` `vsl_nm` VARCHAR(20) NOT NULL COMMENT 'Vessel code' COLLATE 'utf8_bin' AFTER `vsl_name`;