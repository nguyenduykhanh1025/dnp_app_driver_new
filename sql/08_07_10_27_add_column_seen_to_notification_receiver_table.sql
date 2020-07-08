use eport;
ALTER TABLE `notification_receiver`
	ADD COLUMN `seen_flg` tinyint(1) DEFAULT 0 COMMENT 'Trạng thái: 0 unseen, 1 seen' AFTER `user_device_id`;