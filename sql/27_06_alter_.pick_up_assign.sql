ALTER TABLE `pickup_assign`
	CHANGE COLUMN `driver_id` `driver_id` BIGINT(20) NOT NULL COMMENT 'ID tài xế' AFTER `shipment_id`,
	CHANGE COLUMN `shipment_detail_id` `shipment_detail_id` BIGINT(20) NOT NULL COMMENT 'Shipment Detail Id' AFTER `driver_id`;