ALTER TABLE `shipment_detail`
	ADD COLUMN `carrier_code` VARCHAR(5) NULL DEFAULT NULL COMMENT 'Carrier Code' AFTER `register_no`,
	ADD COLUMN `empty_depot` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Noi Ha Rong' AFTER `transport_type`,
	ADD COLUMN `do_status` VARCHAR(1) NULL DEFAULT NULL COMMENT 'Carrier Code' AFTER `process_status`;
	