ALTER TABLE `driver_account`
	ADD COLUMN `identify_card_no` VARCHAR(50) NULL DEFAULT NULL COMMENT 'CMND' COLLATE 'utf8_bin' AFTER `full_name`;