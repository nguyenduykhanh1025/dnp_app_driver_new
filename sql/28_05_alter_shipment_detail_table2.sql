ALTER TABLE `shipment_detail`
	ADD COLUMN `transport_ids` varchar(255) COLLATE utf8_bin DEFAULT NULL AFTER `moving_cont_amount`;
	
	
	
	