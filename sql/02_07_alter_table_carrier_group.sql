ALTER TABLE `edo_history`
	ADD COLUMN `file_name` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Ten file EDI' COLLATE 'utf8_bin' AFTER `edi_content`,
	ADD COLUMN `send_mail_flag` CHAR(1) NULL DEFAULT '0' COMMENT ' 0: chua send mail, 1 : da send mail' COLLATE 'utf8_bin' AFTER `file_name`;