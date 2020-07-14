ALTER TABLE `shipment_detail`
	CHANGE COLUMN `sztp` `sztp` VARCHAR(4) NULL COMMENT 'Size Type' COLLATE 'utf8_bin' AFTER `container_status`;