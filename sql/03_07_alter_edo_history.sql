ALTER TABLE `edo_history`
	ADD COLUMN `create_source` VARCHAR(50) NULL DEFAULT NULL COMMENT 'Nguồi File EDI' COLLATE 'utf8_bin' AFTER `file_name`;