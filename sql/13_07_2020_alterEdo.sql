ALTER TABLE `edo`
	CHANGE COLUMN `det_free_time` `det_free_time` SMALLINT NULL DEFAULT NULL COMMENT 'So Ngay Mien Luu Vo Cont' AFTER `empty_container_depot`,
	ADD COLUMN `vessel_no` VARCHAR(50) NULL DEFAULT NULL COMMENT 'Ma tau' COLLATE 'utf8_bin' AFTER `vessel`;