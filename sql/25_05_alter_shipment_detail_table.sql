ALTER TABLE `shipment_detail`
	ADD COLUMN `logistic_group_id` bigint(20) not null AFTER `shipment_id`;
	
ALTER TABLE `shipment`
	ADD COLUMN `bl_no` varchar(20) not null AFTER `service_id`;
	
	
	