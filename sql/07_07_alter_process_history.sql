ALTER TABLE `process_history`
	ADD COLUMN `status` tinyint(1) NULL DEFAULT NULL COMMENT 'Trạng thái: 1 start, 2 finish' AFTER `result`;