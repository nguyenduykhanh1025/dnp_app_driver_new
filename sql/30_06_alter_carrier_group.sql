ALTER TABLE `carrier_group`
	ADD COLUMN `do_type` CHAR(1) NOT NULL DEFAULT '0' COMMENT '0: DO, 1:eDO' COLLATE 'utf8_bin' AFTER `main_email`;