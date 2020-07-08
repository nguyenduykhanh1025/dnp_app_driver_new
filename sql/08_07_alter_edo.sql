ALTER TABLE `edo`
	ADD COLUMN `transaction_id` VARCHAR(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'Transaction id' AFTER `create_source`;
